package com.example.gamesuniverse.source.games;

public abstract class BaseGamesRemoteDataSource {
    protected GamesCallback gamesCallback;

    public void setGamesCallback(GamesCallback gamesCallback) {
        this.gamesCallback = gamesCallback;
    }
    public abstract void getGames(int page);
    public abstract void getGames(long genres);
    public abstract void getSingleGame(String slug);
    public abstract void getGenres();
}
