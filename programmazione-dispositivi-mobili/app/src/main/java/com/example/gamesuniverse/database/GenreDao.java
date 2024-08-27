package com.example.gamesuniverse.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gamesuniverse.model.Genre;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT * FROM genre")
    List<Genre> getAll();

    @Query("SELECT * FROM genre WHERE id = :id")
    Genre getGenre(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertGenresList(List<Genre> genres);

    @Delete
    void delete(Genre genre);

    @Query("DELETE FROM genre")
    void deleteAll();
}
