package com.example.project01.screens;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.project01.adaper.TopicListAdapter;
import com.example.project01.apiServices.ApiService;
import com.example.project01.apiServices.ApiServiceGenerator;
import com.example.project01.databinding.FragmentHomeBinding;
import com.example.project01.interfaces.OnItemClickListener;
import com.example.project01.mvp.model.DownloadModel;
import com.example.project01.mvp.model.Photo;
import com.example.project01.adaper.PhotoListAdapter;
import com.example.project01.mvp.model.Topic;
import com.example.project01.ui.GridSpacingItemDecoration;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ApiService apiService;

    RecyclerView photoListView;

    PhotoListAdapter photoListAdapter;
    TopicListAdapter topicListAdapter;
    List<Photo> photoList = new ArrayList<>();
    List<Topic> topicList = new ArrayList<>();
    List<Photo> selectedPhotoList = new ArrayList<>();

    Realm realm;

    int selectedTopic = -1;

    String TAG = "TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(realmConfig);
        apiService = ApiServiceGenerator.getClient("XiKGcaTpPyb0ltrlB4c91puuq5UTTWeI1kiEgErXWvc").create(ApiService.class);
        loadTopics();
        loadPhotos();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        photoListView = binding.photoListView;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Photo photo : selectedPhotoList) {
                    startDownloadFIle(photo.getUrls().getFull());
                    Toast.makeText(getContext(), "Start downloading", Toast.LENGTH_SHORT).show();
                }
                selectedPhotoList.clear();
                for (Photo photo : photoList) {
                    photo.setChecked(false);
                }
            }
        });

        //
        photoListAdapter = new PhotoListAdapter(this.getContext(), photoList);
        photoListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
                selectPhoto(position);
            }

            @Override
            public void onItemClick(int position) {
                if (selectedPhotoList.isEmpty()) {
                    Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                    intent.putExtra("photo", photoList.get(position));
                    startActivity(intent);
                } else {
                    selectPhoto(position);
                }
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        photoListView.setLayoutManager(staggeredGridLayoutManager);
        photoListView.addItemDecoration(new GridSpacingItemDecoration(8));
        photoListView.setAdapter(photoListAdapter);
        photoListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled: "+ dx + "--" + dy);

            }
        });

        // topic
        topicListAdapter = new TopicListAdapter(this.getContext(), topicList);
        topicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedTopic = position == selectedTopic ? -1 : position;
                topicListAdapter.setSetSelectedTopic(selectedTopic);
                selectedPhotoList.clear();
                loadPhotos();
                topicListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
        binding.topicListView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.topicListView.setAdapter(topicListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }




    private void startDownloadFIle(String url) {
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

        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

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


    private void selectPhoto(int position) {
        Photo photo = photoList.get(position);
        if (selectedPhotoList.contains(photo)) {
            selectedPhotoList.remove(photo);
        } else {
            selectedPhotoList.add(photo);
        }
        boolean isChecked = photo.isChecked();
        photoList.get(position).setChecked(!isChecked);
        photoListAdapter.notifyItemChanged(position);
        if (selectedPhotoList.isEmpty()) {
            binding.downloadBtn.setVisibility(View.INVISIBLE);
        } else {
            binding.downloadBtn.setVisibility(View.VISIBLE);
        }
    }

    private void loadPhotos() {
        if (selectedTopic == -1) {
            apiService.listPhoto().enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    photoList.clear();
                    photoList.addAll(response.body());
                    photoListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Log.e(TAG, "onError: " + t.getMessage());

                }
            });
        } else {
            apiService.listPhotoUponTopic(topicList.get(selectedTopic).getId()).enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    photoList.clear();
                    photoList.addAll(response.body());
                    photoListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Log.e(TAG, "onError: " + t.getMessage());

                }
            });
        }
    }

    private void loadTopics() {
        apiService.listTopics().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                topicList.addAll(response.body());
                topicListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }
}