package com.example.project01.screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.project01.R;
import com.example.project01.databinding.ActivityAddPhotoBinding;
import com.squareup.picasso.Picasso;

public class AddPhotoActivity extends AppCompatActivity {
    ActivityAddPhotoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Picasso.get().load(uri).into(binding.photo);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
//        binding.addPhotoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityResultContracts.PickVisualMedia.VisualMediaType mediaType = (ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE;
//                pickMedia.launch(new PickVisualMediaRequest.Builder()
//                        .setMediaType(mediaType)
//                        .build());
//            }
//        });



    }
}