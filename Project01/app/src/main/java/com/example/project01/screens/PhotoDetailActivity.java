package com.example.project01.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project01.R;
import com.example.project01.databinding.ActivityPhotoDetailBinding;
import com.example.project01.mvp.model.Photo;
import com.example.project01.services.DownloadService;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;

public class PhotoDetailActivity extends AppCompatActivity {
    ActivityPhotoDetailBinding binding;
    Photo photo;
    LinearProgressIndicator progressBar;

    String TAG = "TAG";
    static final int REQUEST_PERMISSION_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        progressBar = binding.downloadProgress;
        //
        Intent intent = getIntent();
        photo = (Photo) intent.getSerializableExtra("photo");


        Picasso.get().load(photo.getUrls().getRegular()).into(binding.photo);
        Picasso.get().load(photo.getUser().getProfileImage().getMedium()).into(binding.profileImage);
        binding.userName.setText(photo.getUser().getName());
        binding.userBio.setText(photo.getUser().getBio());
        binding.download.setOnClickListener(view -> {
            Log.e(TAG, "onCreate: Download");
            checkPermissions();
//            Intent intentDownload = new Intent(getBaseContext(), DownloadService.class);
//            intentDownload.putExtra("url", photo.getLinks().getDownload());
//            intentDownload.putExtra("receiver", new DownloadReceiver(new Handler()));
//            startService(intentDownload);
        });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            } else {
                startDownloadFIle();
            }
        } else {
            startDownloadFIle();
        }
    }

    private void startDownloadFIle() {
        String urlFile = photo.getLinks().getDownload();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(photo.getId());
        request.setDescription("download...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null){
            downloadManager.enqueue(request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadFIle();
            } else {
                Log.e(TAG, "Permission Denied ");
            }
        }
    }
//    private class DownloadReceiver extends ResultReceiver {
//
//        public DownloadReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//            super.onReceiveResult(resultCode, resultData);
//
//            if (resultCode == DownloadService.UPDATE_PROGRESS) {
//
//                int progress = resultData.getInt("progress"); //get the progress
//                progressBar.setProgress(progress);
//
//
//            }
//        }
//    }

//    private class DownloadTask extends AsyncTask {
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//
//        public DownloadTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            InputStream input = null;
//            OutputStream output = null;
//            HttpURLConnection connection = null;
//            try {
//                URL url = new URL((String) objects[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // expect HTTP 200 OK, so we don't mistakenly save error report
//                // instead of the file
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
//                }
//
//                // this will be useful to display download percentage
//                // might be -1: server did not report the length
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                input = connection.getInputStream();
//                output = new FileOutputStream("/sdcard/file_name.extension");
//
//                byte data[] = new byte[4096];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    // allow canceling with back button
//                    if (isCancelled()) {
//                        input.close();
//                        return null;
//                    }
//                    total += count;
//                    // publishing the progress....
//                    if (fileLength > 0) // only if total length is known
//                        publishProgress((int) (total * 100 / fileLength));
//                    output.write(data, 0, count);
//                }
//            } catch (Exception e) {
//                return e.toString();
//            } finally {
//                try {
//                    if (output != null) output.close();
//                    if (input != null) input.close();
//                } catch (IOException ignored) {
//                }
//
//                if (connection != null) connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
//            mWakeLock.acquire();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onProgressUpdate(Object[] values) {
//            super.onProgressUpdate(values);
//            progressBar.setIndeterminate(false);
//            progressBar.setMax(100);
//            progressBar.setProgress((Integer) values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            mWakeLock.release();
//            Log.e(TAG, "onPostExecute: " + o.toString());
//            progressBar.setVisibility(View.GONE);
////            mProgressDialog.dismiss();
//            if (o != null)
//                Toast.makeText(context, "Download error: " + o.toString(), Toast.LENGTH_LONG).show();
//            else Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
//        }
//    }
}