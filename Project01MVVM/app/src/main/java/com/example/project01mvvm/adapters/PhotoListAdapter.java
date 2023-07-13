package com.example.project01mvvm.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.ItemPhotoBinding;
import com.example.project01mvvm.mvvm.homeScreen.HomeScreenViewModel;
import com.example.project01mvvm.models.Photo;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> photos;
    private HomeScreenViewModel viewModel;

    public PhotoListAdapter(List<Photo> photos, HomeScreenViewModel viewModel) {
        this.photos = photos;
        this.viewModel = viewModel;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public HomeScreenViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(HomeScreenViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhotoBinding itemPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_photo, parent, false);
        return new ViewHolder(itemPhotoBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.itemPhotoBinding.setPhoto(photo);
        holder.itemPhotoBinding.setHomeViewModel(viewModel);
        holder.itemPhotoBinding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPhotoBinding itemPhotoBinding;

        public ViewHolder(final ItemPhotoBinding itemPhotoBinding) {
            super(itemPhotoBinding.getRoot());
            this.itemPhotoBinding = itemPhotoBinding;
        }
    }

}
