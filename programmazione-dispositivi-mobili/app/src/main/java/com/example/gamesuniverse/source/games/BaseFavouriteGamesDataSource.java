package com.example.gamesuniverse.source.games;

import com.example.gamesuniverse.model.Game;

public abstract class BaseFavouriteGamesDataSource {
    protected GamesCallback gamesCallback;

    public void setGamesCallback(GamesCallback gamesCallback){
        this.gamesCallback = gamesCallback;
    }
    public abstract void getFavouriteGames();
    public abstract void addFavouriteGame(Game game);
    public abstract void deleteFavouriteGame(Game game);
}
