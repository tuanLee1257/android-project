package com.example.project01mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.models.DownloadModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import io.realm.Realm;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {
    Context context;
    List<DownloadModel> downloadModels;

    public DownloadListAdapter(Context context, List<DownloadModel> downloadModels) {
        this.context = context;
        this.downloadModels = downloadModels;
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

    }


    @Override
    public int getItemCount() {
        return downloadModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        TextView file_size;
        LinearProgressIndicator progressIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fileName);
            status = itemView.findViewById(R.id.status);
            file_size = itemView.findViewById(R.id.file_size);
            progressIndicator = itemView.findViewById(R.id.progressIndicator);
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

}
