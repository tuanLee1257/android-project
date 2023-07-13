package com.example.project01;

import android.media.Image;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class BindingAdapterClas {

    @BindingAdapter("imageUrl")
    public void loadImage(ImageView imageView,String url){
        Picasso.get().load(url).into(imageView);
    }
}
