package com.example.project01.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01.R;
import com.example.project01.interfaces.OnItemClickListener;
import com.example.project01.mvp.model.DownloadModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import io.realm.Realm;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {
    Context context;
    List<DownloadModel> downloadModels;
    OnItemClickListener onItemClickListener;

    public DownloadListAdapter(Context context, List<DownloadModel> downloadModels, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.downloadModels = downloadModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadModel downloadModel = downloadModels.get(position);

        holder.name.setText(downloadModel.getTitle());
        holder.file_size.setText(downloadModel.getFile_size());
        holder.status.setText(downloadModel.getStatus());
        holder.progressIndicator.setProgress(Integer.parseInt(downloadModel.getProgress()));
        holder.setOnItemClickListener(onItemClickListener);
//        if (downloadModel.isIs_paused()) {
//            holder.pause_btn.setText("Resume");
//        } else {
//            holder.pause_btn.setText("Pause");
//        }
//        if (downloadModel.getStatus().equalsIgnoreCase("Resume")) {
//            holder.status.setText("Running");
//        }

//        holder.pause_btn.setOnClickListener(view -> {
//            if (downloadModel.isIs_paused()) {
//                downloadModel.setIs_paused(false);
////                downloadModel.setStatus("Resume");
//                holder.pause_btn.setText("Resume");
//                resumeDownload(downloadModel);
//                notifyItemChanged(position);
//            } else {
//                downloadModel.setIs_paused(true);
////                downloadModel.setStatus("Resume");
//                holder.pause_btn.setText("Pause");
//                pauseDownload(downloadModel);
//                notifyItemChanged(position);
//
//            }
//        });
    }
//
//    private boolean pauseDownload(DownloadModel downloadModel) {
//        int updateRow = 0;
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("control", 1);
//        try {
//            updateRow = context.getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{downloadModel.getTitle()});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0 < updateRow;
//    }
//
//    private boolean resumeDownload(DownloadModel downloadModel) {
//        int updateRow = 0;
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("control", 0);
//        try {
//            updateRow = context.getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{downloadModel.getTitle()});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0 < updateRow;
//    }


    @Override
    public int getItemCount() {
        return downloadModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView name;
        TextView status;
        TextView file_size;
        Button remove_btn;
        LinearProgressIndicator progressIndicator;
        OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fileName);
            status = itemView.findViewById(R.id.status);
            file_size = itemView.findViewById(R.id.file_size);
            remove_btn = itemView.findViewById(R.id.remove_btn);
//            pause_btn = itemView.findViewById(R.id.pause_btn);
            progressIndicator = itemView.findViewById(R.id.progressIndicator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public void ChangeItem(long downloadId) {
        int i = 0;
        for (DownloadModel downloadModel : downloadModels) {
            if (downloadModel.getDownloadId() == downloadId) {
                notifyItemChanged(i);
            }
            i++;
        }
    }

    public boolean ChangeItemWithStatus(String message, long downloadId) {
        int i = 0;
        boolean comp = false;

        Realm realm = Realm.getDefaultInstance();

        for (DownloadModel downloadModel : downloadModels) {
            if (downloadModel.getDownloadId() == downloadId) {
                final int finalI = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        downloadModels.get(finalI).setStatus(message);
                        notifyItemChanged(finalI);
                        realm.copyToRealmOrUpdate(downloadModels.get(finalI));
                    }
                });
                comp = true;
            }
            i++;
        }
        return comp;
    }

    public void setChangeItemFilePath(String downloadPath, long downloadId) {
        int i = 0;
        Realm realm = Realm.getDefaultInstance();

        for (DownloadModel downloadModel : downloadModels) {
            if (downloadModel.getDownloadId() == downloadId) {
                final int finalI = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        downloadModels.get(finalI).setFile_path(downloadPath);
                        notifyItemChanged(finalI);
                        realm.copyToRealmOrUpdate(downloadModels.get(finalI));
                    }
                });
            }
            i++;
        }
    }

    public void deleteDownload(DownloadModel downloadModel) {

    }
}
