package com.example.project01mvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01mvvm.R;
import com.example.project01mvvm.databinding.ItemTopicBinding;
import com.example.project01mvvm.mvvm.homeScreen.HomeScreenViewModel;
import com.example.project01mvvm.models.Topic;

import java.util.List;


public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {
    List<Topic> topicList;
    HomeScreenViewModel viewModel;
    String TAG = "TAG";

    public TopicListAdapter(List<Topic> topicList, HomeScreenViewModel viewModel) {
        this.topicList = topicList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        ItemTopicBinding itemTopicBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_topic, parent, false);

        return new ViewHolder(itemTopicBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        holder.itemTopicBinding.setTopic(topic);
        holder.itemTopicBinding.setHomeViewModel(viewModel);
        holder.itemTopicBinding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return topicList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemTopicBinding itemTopicBinding;

        public ViewHolder(@NonNull ItemTopicBinding itemTopicBinding) {
            super(itemTopicBinding.getRoot());
            this.itemTopicBinding = itemTopicBinding;
        }
    }

}
