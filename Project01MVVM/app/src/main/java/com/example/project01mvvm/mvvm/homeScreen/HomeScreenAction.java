package com.example.project01mvvm.mvvm.homeScreen;

import com.example.project01mvvm.models.Photo;

import java.util.List;

public interface HomeScreenAction {
    void openPhoto(Photo photo);
    void selectPhoto(int position);
    void downloadPhotos(List<Photo> photoList);
}
