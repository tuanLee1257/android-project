package com.example.project01mvvm.mvvm.downloadManagerScreen;

import androidx.databinding.ObservableArrayList;

import com.example.project01mvvm.models.DownloadModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DownloadManagerViewModel {
    private ObservableArrayList<DownloadModel> downloadingFiles = new ObservableArrayList<>();
    private String TAG = "TAG";

    public void loadDownloadingFiles() {
        List<DownloadModel> downloadModelListLocal = getAllDownloadFiles();
        if (downloadModelListLocal != null) {
            if (downloadModelListLocal.size() > 0) {
                downloadingFiles.addAll(downloadModelListLocal);
            }
        }
    }

    public RealmResults<DownloadModel> getAllDownloadFiles() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(DownloadModel.class).findAll();
    }

    public ObservableArrayList<DownloadModel> getDownloadingFiles() {
        return downloadingFiles;
    }

    public void setDownloadingFiles(ObservableArrayList<DownloadModel> downloadingFiles) {
        this.downloadingFiles = downloadingFiles;
    }
}
