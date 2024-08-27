package com.example.gamesuniverse.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.Genre;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Game.class, Genre.class}, version = 1)
public abstract class GamesRoomDatabase extends RoomDatabase {

    public abstract GameDao gameDao();

    public abstract GenreDao genreDao();

    private static volatile GamesRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GamesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesRoomDatabase.class, "games_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
