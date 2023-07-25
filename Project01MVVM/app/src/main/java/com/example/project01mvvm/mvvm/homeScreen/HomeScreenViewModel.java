package com.example.project01mvvm.mvvm.homeScreen;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.project01mvvm.models.Photo;
import com.example.project01mvvm.models.Topic;
import com.example.project01mvvm.services.ApiService;
import com.example.project01mvvm.util.DownloadHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeScreenViewModel {
    private Disposable mDisposable;
    private ObservableArrayList<Photo> listPhoto = new ObservableArrayList<>();
    private ObservableArrayList<Photo> selectedPhoto = new ObservableArrayList<>();
    private ObservableArrayList<Topic> listTopic = new ObservableArrayList<>();
    private ObservableBoolean isSuccess = new ObservableBoolean();
    private ObservableField<String> selectedTopicId = new ObservableField<>();
    private ObservableInt page = new ObservableInt(1);
    private HomeScreenAction homeScreenAction;
    private String TAG = "TAG";

    public void loadListPhoto() {
        ApiService.apiService.listPhoto(page.get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Photo>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull List<Photo> photos) {
                listPhoto.addAll(photos);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("TAG", "onError: ." + e.getMessage());
                isSuccess.set(false);
            }

            @Override
            public void onComplete() {
                isSuccess.set(true);

            }
        });
    }

    public void loadListPhotoWithTopic() {
        if (selectedTopicId.get() != null) {
            ApiService.apiService.listPhotoUponTopic(selectedTopicId.get(), page.get()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Photo>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mDisposable = d;
                }

                @Override
                public void onNext(@NonNull List<Photo> photos) {
                    listPhoto.addAll(photos);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.e("TAG", "onError: ." + e.getMessage());
                    isSuccess.set(false);
                }

                @Override
                public void onComplete() {
                    isSuccess.set(true);

                }
            });
        }
    }

    public void loadListTopic() {
        ApiService.apiService.listTopics().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Topic>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull List<Topic> topics) {
                listTopic.addAll(topics);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("TAG", "onError: ." + e.getMessage());
                isSuccess.set(false);
            }

            @Override
            public void onComplete() {
                isSuccess.set(true);

            }
        });
    }

    public void changeSelectedTopic(String topicId) {
        selectedTopicId.set(topicId);
        listPhoto.clear();
        loadListPhotoWithTopic();
    }

    public void loadMorePhotos() {
        int next_page = page.get() + 1;
        page.set(next_page);
        if (selectedTopicId.get() == null | selectedTopicId.get() == "")
            loadListPhoto();
        else loadListPhotoWithTopic();
    }

    public void startPhotoDetailScreen(Photo photo) {
        if (homeScreenAction != null) {
            homeScreenAction.openPhoto(photo);
        }
    }

    public boolean selectPhoto(Photo photo) {
        if (homeScreenAction != null) {
            int position = listPhoto.indexOf(photo);
            if (selectedPhoto.contains(photo)) {
                selectedPhoto.remove(photo);
            } else selectedPhoto.add(photo);
            photo.setChecked(!photo.isChecked());
            homeScreenAction.selectPhoto(position);
        }
        return true;
    }

    public void startDownload() {
        homeScreenAction.downloadPhotos(selectedPhoto);
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public void setListPhoto(ObservableArrayList<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public void setListTopic(ObservableArrayList<Topic> listTopic) {
        this.listTopic = listTopic;
    }

    public ObservableArrayList<Photo> getListPhoto() {
        return listPhoto;
    }

    public ObservableArrayList<Topic> getListTopic() {
        return listTopic;
    }

    public ObservableBoolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(ObservableBoolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ObservableField<String> getSelectedTopicId() {
        return selectedTopicId;
    }

    public void setSelectedTopicId(ObservableField<String> selectedTopicId) {
        this.selectedTopicId = selectedTopicId;
    }

    public HomeScreenAction getHomeScreenAction() {
        return homeScreenAction;
    }

    public void setHomeScreenAction(HomeScreenAction homeScreenAction) {
        this.homeScreenAction = homeScreenAction;
    }

    public ObservableArrayList<Photo> getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(ObservableArrayList<Photo> selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }
}
