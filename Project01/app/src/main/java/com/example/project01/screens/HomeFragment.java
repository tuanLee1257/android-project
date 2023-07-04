package com.example.project01.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.project01.adaper.TopicListAdapter;
import com.example.project01.apiServices.ApiService;
import com.example.project01.apiServices.ApiServiceGenerator;
import com.example.project01.databinding.FragmentHomeBinding;
import com.example.project01.interfaces.OnItemClickListener;
import com.example.project01.mvp.model.Photo;
import com.example.project01.adaper.PhotoListAdapter;
import com.example.project01.mvp.model.Topic;
import com.example.project01.ui.GridSpacingItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ApiService apiService;


    PhotoListAdapter photoListAdapter;
    TopicListAdapter topicListAdapter;
    List<Photo> photoList = new ArrayList<>();
    List<Topic> topicList = new ArrayList<>();

    int selectedTopic = -1;

    String TAG = "TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiServiceGenerator.getClient("XiKGcaTpPyb0ltrlB4c91puuq5UTTWeI1kiEgErXWvc").create(ApiService.class);
        loadTopics();
        loadPhotos();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // photo
        photoListAdapter = new PhotoListAdapter(this.getContext(), photoList);
        photoListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(),PhotoDetailActivity.class);
                intent.putExtra("photo", photoList.get(position));
                startActivity(intent);
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.photoListView.setLayoutManager(staggeredGridLayoutManager);
        binding.photoListView.addItemDecoration(new GridSpacingItemDecoration(8));
        binding.photoListView.setAdapter(photoListAdapter);

        // topic
        topicListAdapter = new TopicListAdapter(this.getContext(), topicList);
        topicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedTopic = position == selectedTopic ? -1 : position;
                topicListAdapter.setSetSelectedTopic(selectedTopic);
                loadPhotos();
                topicListAdapter.notifyDataSetChanged();
            }
        });
        binding.topicListView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.topicListView.setAdapter(topicListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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