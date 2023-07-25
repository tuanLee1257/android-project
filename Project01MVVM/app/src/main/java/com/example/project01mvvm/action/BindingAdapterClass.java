package com.example.project01mvvm.action;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class BindingAdapterClass {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    @BindingAdapter("shapeableImageUrl")
    public static void loadImage(ShapeableImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
