package com.example.gamesuniverse.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.Game;

import java.util.List;


public class GamesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int GAME_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    public interface OnItemClickListener {
        void onGameItemClick(Game game);
    }
    private final List<Game> gameList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public GamesRecyclerViewAdapter(List<Game> gameList, Application application, OnItemClickListener onItemClickListener) {
        this.gameList = gameList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (gameList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return GAME_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == GAME_VIEW_TYPE){
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.games_list_item, parent, false);
            return new GamesViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.games_loading_item, parent, false);
            return new LoadingGamesViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GamesViewHolder){
            ((GamesViewHolder) holder).bind(gameList.get(position));
        } else if (holder instanceof LoadingGamesViewHolder){
            ((LoadingGamesViewHolder) holder).activate();
        }
    }

    @Override
    public int getItemCount() {
        if (gameList != null)
            return gameList.size();
        return 0;
    }

    public class GamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewTitle;
        private final ImageView imageViewCover;
        public GamesViewHolder(@NonNull View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.textview_title);
            this.imageViewCover = view.findViewById(R.id.imageGame);
            view.setOnClickListener(this);
        }

        public void bind(Game game){
            textViewTitle.setText(game.getName());
            Glide.with(application)
                    .load(game.getBackground_image())
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(imageViewCover);
            //imageViewCover.setImageURI();
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onGameItemClick(gameList.get(getAdapterPosition()));
        }
    }

    public static class LoadingGamesViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingGamesViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressbar_loading_games);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }
}
