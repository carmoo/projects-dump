package com.example.gamesuniverse.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gamesuniverse.model.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM game")
    List<Game> getAll();

    @Query("SELECT * FROM game WHERE id = :id")
    Game getGame(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertGamesList(List<Game> games);

    @Delete
    void delete(Game game);

    @Query("DELETE FROM game")
    void deleteAll();

    @Update
    int updateSingleFavouriteGame(Game game);

    @Update
    int updateListFavouriteGames(List<Game> games);
}
