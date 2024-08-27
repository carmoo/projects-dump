package com.example.gamesuniverse.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.repository.games.GamesRepository;

public class GamesViewModel extends ViewModel {
    private final GamesRepository gamesRepository;
    private int page;
    private int currentResults;
    private int totalResults;
    private boolean isLoading;
    private boolean firstLoading;
    private MutableLiveData<Result> gamesListLiveData;
    private MutableLiveData<Result> genresGamesListLiveData;
    private MutableLiveData<Result> favouriteGamesListLiveData;
    private MutableLiveData<Result> singleGameLiveData;
    private MutableLiveData<Result> genresListLiveData;

    public GamesViewModel(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
    }

    public MutableLiveData<Result> getGames(long lastUpdate) {
        if (gamesListLiveData == null) {
            fetchGames(lastUpdate);
        }
        return gamesListLiveData;
    }

    public MutableLiveData<Result> getGenresGames(long genres) {
        fetchGenresGames(genres);
        return genresGamesListLiveData;
    }

    public MutableLiveData<Result> getFavouriteGamesLiveData() {
        if (favouriteGamesListLiveData == null) {
            getFavouriteGames();
        }
        return favouriteGamesListLiveData;
    }

    public MutableLiveData<Result> getSingleGame(String slug){
        fetchGamesSingleGame(slug);
        return singleGameLiveData;
    }

    public MutableLiveData<Result> getGenres(long lastUpdate){
        if (genresListLiveData == null) {
            fetchGenres(lastUpdate);
        }
        return genresListLiveData;
    }
    public void fetchGames() {
        gamesRepository.fetchGames(page);
    }


    public void removeFromFavourite(Game game){
        gamesRepository.deleteFavourite(game);
    }

    private void fetchGames(long lastUpdate) {
        gamesListLiveData = gamesRepository.fetchGames(lastUpdate, page);
    }

    private void fetchGamesSingleGame(String slug){
        singleGameLiveData = gamesRepository.fetchSingleGame(slug);
    }
    private void fetchGenres(long lastUpdate){
        genresListLiveData = gamesRepository.fetchGenres(lastUpdate);
    }

    private void fetchGenresGames(long genres) {
        genresGamesListLiveData = gamesRepository.fetchGames(genres);
    }

    public void addFavourite(Game game){
        gamesRepository.addFavourite(game);
    }
    private void getFavouriteGames(){
        favouriteGamesListLiveData = gamesRepository.getFavouriteGames();
    }

    public int getPage() {
        return page;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setPage(int page){
        this.page = page;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }

    public MutableLiveData<Result> getGamesResponse() {
        return gamesListLiveData;
    }
}
