package com.example.project01mvvm.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.ItemPhotoBinding;
import com.example.project01mvvm.mvvm.homeScreen.HomeScreenAction;
import com.example.project01mvvm.mvvm.homeScreen.HomeScreenViewModel;
import com.example.project01mvvm.models.Photo;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<Photo> photos;
    private HomeScreenViewModel viewModel;
    private HomeScreenAction homeScreenAction;



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
        if (photos != null && position >= getItemCount()-1){
            Log.e("TAG", "onBindViewHolder: end reach" );
            viewModel.loadMorePhotos();
        }
        holder.itemPhotoBinding.photoCard.setChecked(photo.isChecked());
        holder.itemPhotoBinding.setPhoto(photo);
        holder.itemPhotoBinding.setHomeViewModel(viewModel);
        holder.itemPhotoBinding.executePendingBindings();
    }
    public void setHomeScreenAction(HomeScreenAction homeScreenAction) {
        this.homeScreenAction = homeScreenAction;
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
