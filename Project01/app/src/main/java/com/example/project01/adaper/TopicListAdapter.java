package com.example.project01.adaper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01.R;
import com.example.project01.interfaces.OnItemClickListener;
import com.example.project01.mvp.model.Topic;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {
    Context context;
    List<Topic> topicList;
    OnItemClickListener onItemClickListener;
    int setSelectedTopic =-1;

    public TopicListAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        holder.topicName.setText(topic.getTitle());
        holder.setSelected(position == setSelectedTopic,position );
        holder.setOnItemClickListener(onItemClickListener);
//        holder.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                setSelectedTopic = position == setSelectedTopic ? -1 : position;
////                setSelectedTopic = position;
//                Log.e("TAG", "onItemClick: "+setSelectedTopic );
//                notifyDataSetChanged();
//            }
//        });
    }

    public int getSetSelectedTopic() {
        return setSelectedTopic;
    }

    public void setSetSelectedTopic(int setSelectedTopic) {
        this.setSelectedTopic = setSelectedTopic;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView topicName;
        private OnItemClickListener onItemClickListener;
        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener = onItemClickListener;

        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.topic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

        public void setSelected(boolean isSelected, int position) {
            if (isSelected) {
                topicName.setTextColor(context.getColor(R.color.md_theme_light_onPrimary));
                topicName.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.md_theme_light_primary)));
            } else {
                topicName.setTextColor(context.getColor(R.color.md_theme_light_primary));
                topicName.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.shape_background_color)));
            }
        }

    }

}
