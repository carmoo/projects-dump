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
import com.example.gamesuniverse.model.Genre;

import java.util.List;

public class GenresRecyclerViewAdapter extends RecyclerView.Adapter<GenresRecyclerViewAdapter.GenresViewHolder>{
    public interface OnItemClickListener {
        void onGenreItemClick(Genre genre);
    }
    private final List<Genre> genresList;
    private final Application application;
    private final GenresRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public GenresRecyclerViewAdapter(List<Genre> genresList, Application application, GenresRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        this.genresList = genresList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.genres_list_item, parent, false);
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

    public class GenresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewTitle;
        private final ImageView imageViewCover;

        public GenresViewHolder(@NonNull View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.textView_genre);
            this.imageViewCover = view.findViewById(R.id.imageGenre);
            view.setOnClickListener(this);
        }

        public void bind(Genre genre){
            textViewTitle.setText(genre.getName());
            Glide.with(application)
                    .load(genre.getImage_background())
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(imageViewCover);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onGenreItemClick(genresList.get(getAdapterPosition()));
        }
    }

}
