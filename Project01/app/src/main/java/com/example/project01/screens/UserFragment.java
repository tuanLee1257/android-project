package com.example.project01.screens;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.example.project01.R;
import com.example.project01.adaper.DownloadListAdapter;
import com.example.project01.databinding.FragmentUserBinding;
import com.example.project01.interfaces.OnItemClickListener;
import com.example.project01.mvp.model.DownloadModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class UserFragment extends Fragment {
    FragmentUserBinding binding;

    EditText input_url;
    Button download_btn;

    RecyclerView downloadListView;
    DownloadListAdapter downloadListAdapter;

    List<DownloadModel> downloadModelList = new ArrayList<>();
    HashMap<Long, DownloadStatusTask> taskList = new HashMap<>();

    Realm realm;
    String TAG = "TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(onComplete, filter);
        getActivity().registerReceiver(onComplete, filter);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(realmConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(getLayoutInflater());
        downloadListView = binding.downloadListView;



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(downloadListView);

        downloadListAdapter = new DownloadListAdapter(this.getContext(), downloadModelList);
        downloadListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        downloadListView.setAdapter(downloadListAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<DownloadModel> downloadModelListLocal = getAllDownloadFiles();
        if (downloadModelListLocal != null) {
            if (downloadModelListLocal.size() > 0) {
                downloadModelList.addAll(downloadModelListLocal);
                for (int i = 0; i < downloadModelList.size(); i++) {
                    if (downloadModelList.get(i).getStatus().equalsIgnoreCase("Pending") || downloadModelList.get(i).getStatus().equalsIgnoreCase("Running") || downloadModelList.get(i).getStatus().equalsIgnoreCase("Downloading")) {
                        DownloadStatusTask downloadStatusTask = new DownloadStatusTask(downloadModelList.get(i));
                        runTask(downloadStatusTask, "" + downloadModelList.get(i).getDownloadId());
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onComplete);
        realm.close();
    }

    ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            removeDownloadItem(position);
            Log.e(TAG, "onSwiped: " + position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.download_fill0_wght500_grad200_opsz48)
                    .addSwipeLeftBackgroundColor(R.color.md_theme_light_error)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void removeDownloadItem(int position) {
        // remove from adapter
        DownloadModel removeItem = downloadModelList.get(position);
        downloadModelList.remove(removeItem);
        downloadListAdapter.notifyItemRemoved(position);
        // remove task
        DownloadStatusTask downloadStatusTask = taskList.get(removeItem.getDownloadId());
        if (downloadStatusTask != null) {
            downloadStatusTask.cancel(true);
        }
        // remove from realm
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DownloadModel removeRealmItem = realm.where(DownloadModel.class).equalTo("id", removeItem.getId()).findFirst();
                removeRealmItem.deleteFromRealm();
            }
        });
    }

    // on adapter item clicked



    public class DownloadStatusTask extends AsyncTask<String, String, String> {
        DownloadModel downloadModel;

        public DownloadStatusTask(DownloadModel downloadModel) {
            this.downloadModel = downloadModel;
        }

        @Override
        protected String doInBackground(String... strings) {
            downloadFileProcess(strings[0]);
            return null;
        }

        private void downloadFileProcess(String downloadId) {
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            boolean downloading = true;
            while (downloading) {

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(Long.parseLong(downloadId));
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();


                int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int bytesDownloaded = cursor.getInt(bytesDownloadedIndex);

                int totalSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int totalSize = cursor.getInt(totalSizeIndex);

                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (cursor.getInt(statusIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }

                int progress = 0;
                if (totalSize != 0) {
                     progress = (int) ((bytesDownloaded * 100L) / totalSize);
                }
                String status = getStatusMessages(cursor);
                publishProgress(new String[]{String.valueOf(progress), String.valueOf(bytesDownloaded), status});
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    downloadModel.setProgress(values[0]);
                    downloadModel.setFile_size(values[1]);
                    downloadModel.setStatus(values[2]);

                    downloadListAdapter.ChangeItem(downloadModel.getDownloadId());
                }
            });
        }

        private String getStatusMessages(Cursor cursor) {
            String msg = "";
            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            switch (cursor.getInt(statusIndex)) {
                case DownloadManager.STATUS_FAILED:
                    msg = "Failed";
                    break;
                case DownloadManager.STATUS_PAUSED:
                    msg = "Paused";
                    break;
                case DownloadManager.STATUS_RUNNING:
                    msg = "Running";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    msg = "Completed";
                    break;
                case DownloadManager.STATUS_PENDING:
                    msg = "Pending";
                    break;
                default:
                    msg = "Unknown";
                    break;
            }
            return msg;
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            boolean comp = downloadListAdapter.ChangeItemWithStatus("Completed", id);

            if (comp) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();

                int localUrlIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String downloadPath = cursor.getString(localUrlIndex);
                downloadListAdapter.setChangeItemFilePath(downloadPath, id);
            }
        }
    };

    public void runTask(DownloadStatusTask downloadStatusTask, String id) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                downloadStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{id});
            } else {
                downloadStatusTask.execute(new String[]{id});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RealmResults<DownloadModel> getAllDownloadFiles() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(DownloadModel.class).findAll();
    }
}
