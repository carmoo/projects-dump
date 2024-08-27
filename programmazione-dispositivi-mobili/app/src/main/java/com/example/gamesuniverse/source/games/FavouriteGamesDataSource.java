package com.example.gamesuniverse.source.games;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gamesuniverse.model.Game;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavouriteGamesDataSource extends BaseFavouriteGamesDataSource{
    private static final String TAG = FavouriteGamesDataSource.class.getSimpleName();
    private final DatabaseReference databaseReference;
    private final String identifier;

    public FavouriteGamesDataSource(String identifier){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://gamesuniverse-c3a7e-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference().getRef();
        this.identifier = identifier;
    }
    @Override
    public void getFavouriteGames() {
        databaseReference.child("user").child(identifier).
                child("favourite_games").get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting data", task.getException());
                    }
                    else {
                        Log.d(TAG, "Successful read: " + task.getResult().getValue());

                        List<Game> gameList = new ArrayList<>();
                        for(DataSnapshot ds : task.getResult().getChildren()) {
                            Game game = ds.getValue(Game.class);
                            //game.setFavourite(true);
                            gameList.add(game);
                        }

                        gamesCallback.onSuccessFromCloudReading(gameList);
                    }
                });
    }

    @Override
    public void addFavouriteGame(Game game) {
        databaseReference.child("user").child(identifier).
                child("favourite_games").child(String.valueOf(game.getId())).setValue(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //news.setSynchronized(true);
                        gamesCallback.onSuccessFromCloudWriting(game);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        gamesCallback.onFailureFromCloud(e);
                    }
                });
    }

    @Override
    public void deleteFavouriteGame(Game game) {
        databaseReference.child("user").child(identifier).
                child("favourite_games").child(String.valueOf(game.getId())).
                removeValue().addOnSuccessListener(aVoid -> {
                    //news.setSynchronized(false);
                    gamesCallback.onSuccessFromCloudWriting(game);
                }).addOnFailureListener(e -> {
                    gamesCallback.onFailureFromCloud(e);
                });
    }
}
