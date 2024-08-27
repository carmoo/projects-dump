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
import com.example.gamesuniverse.model.Genre;

import java.util.List;

public class GameGenresRecyclerViewAdapter extends RecyclerView.Adapter<GameGenresRecyclerViewAdapter.GenresViewHolder>{
    public interface OnItemClickListener {
        void onGenreItemClick(Genre genre);
    }
    private final List<Genre> genresList;
    private final Application application;

    public GameGenresRecyclerViewAdapter(List<Genre> genresList, Application application) {
        this.genresList = genresList;
        this.application = application;
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.game_genres_list_item, parent, false);
        return new GenresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        holder.bind(genresList.get(position));
    }

    @Override
    public int getItemCount() {
        if (genresList != null)
            return genresList.size();
        return 0;
    }

    public class GenresViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final ImageView imageViewCover;

        public GenresViewHolder(@NonNull View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.textView_genre);
            this.imageViewCover = view.findViewById(R.id.imageGenre);
        }

        public void bind(Genre genre){
            textViewTitle.setText(genre.getName());
            Glide.with(application)
                    .load(genre.getImage_background())
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(imageViewCover);
        }
    }
}
