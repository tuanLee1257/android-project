package com.example.project01mvvm.mvvm.detailScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.ActivityPhotoDetailBinding;
import com.example.project01mvvm.models.Photo;

public class PhotoDetailActivity extends AppCompatActivity {
    ActivityPhotoDetailBinding photoDetailScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoDetailScreenBinding = ActivityPhotoDetailBinding.inflate(getLayoutInflater());
        setContentView(photoDetailScreenBinding.getRoot());

        Intent intent = getIntent();
        Photo photo =  (Photo) intent.getSerializableExtra("photo");

        PhotoDetailScreenViewModel photoDetailScreenViewModel = new PhotoDetailScreenViewModel();
        photoDetailScreenViewModel.setPhoto(photo);
        photoDetailScreenBinding.setPhotoDetailViewModel(photoDetailScreenViewModel);

    }
}