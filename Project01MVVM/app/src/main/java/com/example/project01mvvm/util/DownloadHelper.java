package com.example.project01mvvm.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

import com.example.project01mvvm.models.DownloadModel;

import java.io.File;

import io.realm.Realm;

public class DownloadHelper {
    public static Realm realm;

    public static void startDownloadFIle(Context context, String url) {
        String urlFile = url;
        String fileName = URLUtil.guessFileName(urlFile, null, null);
        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(downloadPath, fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(fileName);
        request.setDescription("Downloading...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationUri(Uri.fromFile(file));
        request.setRequiresCharging(false);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadModel downloadModel = new DownloadModel();
        long downloadId = downloadManager.enqueue(request);

        Number currentNum = realm.where(DownloadModel.class).max("id");
        int nextId;
        if (currentNum == null) {
            nextId = 1;
        } else nextId = currentNum.intValue() + 1;

        downloadModel.setId(nextId);
        downloadModel.setStatus("Downloading");
        downloadModel.setTitle(fileName);
        downloadModel.setFile_size("0");
        downloadModel.setProgress("0");
        downloadModel.setIs_paused(false);
        downloadModel.setDownloadId(downloadId);
        downloadModel.setFile_path("");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(downloadModel);
            }
        });
    }
}
