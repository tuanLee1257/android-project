package com.example.project01mvvm.mvvm.homeScreen;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.adapters.PhotoListAdapter;
import com.example.project01mvvm.adapters.TopicListAdapter;
import com.example.project01mvvm.databinding.FragmentHomeBinding;
import com.example.project01mvvm.models.Photo;
import com.example.project01mvvm.models.Topic;
import com.example.project01mvvm.mvvm.detailScreen.PhotoDetailActivity;
import com.example.project01mvvm.ui.GridSpacingItemDecoration;
import com.example.project01mvvm.util.DownloadHelper;

import java.util.List;

public class HomeFragment extends Fragment implements HomeScreenAction {
    FragmentHomeBinding homeScreenBinding;
    PhotoListAdapter photoListAdapter;
    public static String TAG = "TAG";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeScreenBinding = FragmentHomeBinding.inflate(inflater);

        HomeScreenViewModel homeScreenViewModel = new HomeScreenViewModel();
        homeScreenBinding.setHomeViewModel(homeScreenViewModel);
        homeScreenViewModel.loadListPhoto();
        homeScreenViewModel.loadListTopic();
        homeScreenViewModel.setHomeScreenAction(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        homeScreenBinding.photoListView.setLayoutManager(staggeredGridLayoutManager);
        homeScreenBinding.photoListView.addItemDecoration(new GridSpacingItemDecoration(8));
        photoListAdapter = new PhotoListAdapter(homeScreenViewModel.getListPhoto(), homeScreenViewModel);
        homeScreenBinding.photoListView.setAdapter(photoListAdapter);
        boolean isLoading = false;
//        homeScreenBinding.photoListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int[] pastVisibleItems = new int[0];
//                layoutManager.findLastCompletelyVisibleItemPositions(pastVisibleItems);
//
//
//            }
//        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        homeScreenBinding.topicListView.setLayoutManager(linearLayoutManager);
        TopicListAdapter topicListAdapter = new TopicListAdapter(homeScreenViewModel.getListTopic(), homeScreenViewModel);
        homeScreenBinding.topicListView.setAdapter(topicListAdapter);
        return homeScreenBinding.getRoot();
    }


    @BindingAdapter({"list_photo", "is_success"})
    public static void loadListPhoto(RecyclerView recyclerView, ObservableArrayList<Photo> list, ObservableBoolean isSuccess) {
        Log.e(TAG, "loadListPhoto: " + list.size());
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    @BindingAdapter({"list_topic", "is_success"})
    public static void loadListTopic(RecyclerView recyclerView, ObservableArrayList<Topic> list, ObservableBoolean isSuccess) {
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

    @BindingAdapter("is_photo_selected")
    public static void isPhotoSelected(Button button, ObservableArrayList<Photo> list) {
        if (list.isEmpty()) {
            button.setVisibility(View.INVISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void openPhoto(Photo photo) {
        Intent intent = new Intent(getContext(), PhotoDetailActivity.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    @Override
    public void selectPhoto(int position) {
        photoListAdapter.notifyItemChanged(position);
    }

    @Override
    public void downloadPhotos(List<Photo> photoList) {
        DownloadHelper downloadHelper = new DownloadHelper();
        for (Photo photo : photoList) {
            downloadHelper.startDownloadFIle(getContext(), photo.getUrls().getFull());
        }
    }
}