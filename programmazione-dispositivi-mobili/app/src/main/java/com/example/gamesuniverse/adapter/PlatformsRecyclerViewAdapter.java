package com.example.gamesuniverse.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.GameDetail;

import java.util.List;

public class PlatformsRecyclerViewAdapter extends RecyclerView.Adapter<PlatformsRecyclerViewAdapter.PlatformsViewHolder>{
    private final List<GameDetail.Platform> platformsList;
    private final Application application;

    public PlatformsRecyclerViewAdapter(List<GameDetail.Platform> platformsList, Application application) {
        this.platformsList = platformsList;
        this.application = application;
    }

    @NonNull
    @Override
    public PlatformsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.platforms_list_item, parent, false);
        return new PlatformsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatformsViewHolder holder, int position) {
        holder.bind(platformsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (platformsList != null)
            return platformsList.size();
        return 0;
    }

    public class PlatformsViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;
        public PlatformsViewHolder(@NonNull View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.textView_platform);
        }

        public void bind(GameDetail.Platform platform){
            textViewTitle.setText(platform.getPlatform().getName());
        }
    }

}
