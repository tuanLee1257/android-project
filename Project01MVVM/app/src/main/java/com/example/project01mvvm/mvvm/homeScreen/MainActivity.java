package com.example.project01mvvm.mvvm.homeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.adapters.PhotoListAdapter;
import com.example.project01mvvm.adapters.TopicListAdapter;
import com.example.project01mvvm.databinding.ActivityMainBinding;
import com.example.project01mvvm.models.Photo;
import com.example.project01mvvm.models.Topic;
import com.example.project01mvvm.mvvm.detailScreen.PhotoDetailActivity;
import com.example.project01mvvm.ui.GridSpacingItemDecoration;

public class MainActivity extends AppCompatActivity implements HomeScreenAction{
    ActivityMainBinding homeScreenBinding;
    public static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(homeScreenBinding.getRoot());

        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();
        homeScreenBinding.setHomeViewModel(homeScreenViewModel);
        homeScreenViewModel.loadListPhoto();
        homeScreenViewModel.loadListTopic();
        homeScreenViewModel.setHomeScreenAction(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        homeScreenBinding.photoListView.setLayoutManager(staggeredGridLayoutManager);
        homeScreenBinding.photoListView.addItemDecoration(new GridSpacingItemDecoration(8));
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(homeScreenViewModel.getListPhoto(),homeScreenViewModel);
        homeScreenBinding.photoListView.setAdapter(photoListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false);
        homeScreenBinding.topicListView.setLayoutManager(linearLayoutManager);
        TopicListAdapter topicListAdapter = new TopicListAdapter(homeScreenViewModel.getListTopic(),homeScreenViewModel);
        homeScreenBinding.topicListView.setAdapter(topicListAdapter);
    }

    @BindingAdapter({"list_photo","is_success"})
    public static void loadListPhoto(RecyclerView recyclerView, ObservableArrayList<Photo> list, ObservableBoolean isSuccess) {
        Log.e(TAG, "loadListPhoto: " +list.size());
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    @BindingAdapter({"list_topic","is_success"})
    public static void loadListTopic(RecyclerView recyclerView, ObservableArrayList<Topic> list , ObservableBoolean isSuccess) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @BindingAdapter({"topic_selected", "topic_id"})
    public static void selectedTopic(TextView textView, ObservableField<String> topic_selected, String topicId) {
        if (topicId == topic_selected.get()) {
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.md_theme_light_onPrimary));
            textView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(textView.getContext(), R.color.md_theme_light_primary)));
        } else {
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.md_theme_light_primary));
            textView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(textView.getContext(), R.color.shape_background_color)));
        }
    }

    @Override
    public void openPhoto(Photo photo) {
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra("photo",photo);
        startActivity(intent);
    }
}