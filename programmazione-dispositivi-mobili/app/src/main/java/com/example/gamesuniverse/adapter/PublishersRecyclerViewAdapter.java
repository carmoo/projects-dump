package com.example.gamesuniverse.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.GameDetail;

import java.util.List;

public class PublishersRecyclerViewAdapter extends RecyclerView.Adapter<PublishersRecyclerViewAdapter.PublishersViewHolder>{
    private final List<GameDetail.Publisher> publishersList;
    private final Application application;

    public PublishersRecyclerViewAdapter(List<GameDetail.Publisher> publishersList, Application application) {
        this.publishersList = publishersList;
        this.application = application;
    }

    @NonNull
    @Override
    public PublishersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.publishers_list_item, parent, false);
        return new PublishersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishersViewHolder holder, int position) {
        holder.bind(publishersList.get(position));
    }

    @Override
    public int getItemCount() {
        if (publishersList != null)
            return publishersList.size();
        return 0;
    }

    public class PublishersViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;
        public PublishersViewHolder(@NonNull View view) {
            super(view);
            this.textViewTitle = view.findViewById(R.id.textView_publisher);
        }

        public void bind(GameDetail.Publisher publisher){
            textViewTitle.setText(publisher.getName());
        }
    }

}
