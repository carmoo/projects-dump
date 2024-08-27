package com.example.gamesuniverse.source.games;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.GenreResponse;

public abstract class BaseGamesLocalDataSource {
    protected GamesCallback gamesCallback;

    public void setGamesCallback(GamesCallback gamesCallback) {
        this.gamesCallback = gamesCallback;
    }

    public abstract void getGames();
    public abstract void insertGames(GamesResponse response);

    public abstract void getGenres();
    public abstract void insertGenres(GenreResponse response);
}
