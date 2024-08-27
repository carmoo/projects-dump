package com.example.gamesuniverse.repository.games;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.gamesuniverse.model.GameDetail;
import com.example.gamesuniverse.model.Genre;
import com.example.gamesuniverse.model.GenreResponse;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.source.games.BaseFavouriteGamesDataSource;
import com.example.gamesuniverse.source.games.BaseGamesLocalDataSource;
import com.example.gamesuniverse.source.games.BaseGamesRemoteDataSource;
import com.example.gamesuniverse.source.games.GamesCallback;

import java.util.List;

public class GamesRepository implements GamesCallback {
    private static final String TAG = GamesRepository.class.getSimpleName();
    private final MutableLiveData<Result> allGamesMutableLiveData;
    private final MutableLiveData<Result> genresGamesMutableLiveData;
    private final MutableLiveData<Result> favouriteGamesMutableLiveData;
    private final MutableLiveData<Result> singleGameMutableLiveData;
    private final MutableLiveData<Result> genresMutableLiveData;
    private final BaseGamesRemoteDataSource gamesRemoteDataSource;
    private final BaseGamesLocalDataSource gamesLocalDataSource;
    private final BaseFavouriteGamesDataSource backupDataSource;

    public GamesRepository(BaseGamesRemoteDataSource gamesRemoteDataSource,
                           BaseGamesLocalDataSource baseGamesLocalDataSource,
                           BaseFavouriteGamesDataSource favouriteGamesDataSource){
        allGamesMutableLiveData = new MutableLiveData<>();
        genresGamesMutableLiveData = new MutableLiveData<>();
        favouriteGamesMutableLiveData = new MutableLiveData<>();
        singleGameMutableLiveData = new MutableLiveData<>();
        genresMutableLiveData = new MutableLiveData<>();
        this.gamesRemoteDataSource = gamesRemoteDataSource;
        this.gamesRemoteDataSource.setGamesCallback(this);
        this.gamesLocalDataSource = baseGamesLocalDataSource;
        this.gamesLocalDataSource.setGamesCallback(this);
        this.backupDataSource = favouriteGamesDataSource;
        this.backupDataSource.setGamesCallback(this);
    }

    public MutableLiveData<Result> fetchGames(long lastUpdate, int page){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > 1000) {
            gamesRemoteDataSource.getGames(page);
        } else {
            gamesLocalDataSource.getGames();
        }
        return allGamesMutableLiveData;
    }

    public MutableLiveData<Result> fetchGames(long genres){
        gamesRemoteDataSource.getGames(genres);
        return genresGamesMutableLiveData;
    }

    public void fetchGames(int page) {
        gamesRemoteDataSource.getGames(page);
    }

    public MutableLiveData<Result> getFavouriteGames(){
        backupDataSource.getFavouriteGames();
        //gamesLocalDataSource.getFavouriteGames();
        return favouriteGamesMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(GamesResponse response) {
        gamesLocalDataSource.insertGames(response);
    }
    @Override
    public void onSuccessFromRemoteGenresGames(GamesResponse response) {
        Result.Success result = new Result.Success(response);
        genresGamesMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        allGamesMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocal(GamesResponse response) {
        if (allGamesMutableLiveData.getValue() != null && allGamesMutableLiveData.getValue().isSuccess()) {
            List<Game> gameList = ((Result.Success)allGamesMutableLiveData.getValue()).getData().getResults();
            gameList.addAll(response.getResults());
            response.setResults(gameList);
            Result.Success result = new Result.Success(response);
            allGamesMutableLiveData.postValue(result);
        } else {
            Result.Success result = new Result.Success(response);
            allGamesMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allGamesMutableLiveData.postValue(resultError);
        favouriteGamesMutableLiveData.postValue(resultError);
    }

    @Override
    public void onGameFavouriteStatusChanged(Game game, List<Game> favouriteGams) {
        Result allGamesResult = allGamesMutableLiveData.getValue();

        if (allGamesResult != null && allGamesResult.isSuccess()) {
            List<Game> oldAllGames = ((Result.Success)allGamesResult).getData().getResults();
            if (oldAllGames.contains(game)) {
                oldAllGames.set(oldAllGames.indexOf(game), game);
                allGamesMutableLiveData.postValue(allGamesResult);
            }
        }
        favouriteGamesMutableLiveData.postValue(new Result.Success(new GamesResponse(0, "", "",favouriteGams)));
    }

    @Override
    public void onGamesFavouriteStatusChanged(List<Game> games) {
        favouriteGamesMutableLiveData.postValue(new Result.Success(new GamesResponse(0, "", "",games)));
    }

    @Override
    public void onDeleteFavouriteGameSuccess(List<Game> favouriteGames) {
        Result allGamesResult = allGamesMutableLiveData.getValue();

        if (allGamesResult != null && allGamesResult.isSuccess()) {
            List<Game> oldAllGames = ((Result.Success)allGamesResult).getData().getResults();
            for (Game game : favouriteGames) {
                if (oldAllGames.contains(game)) {
                    oldAllGames.set(oldAllGames.indexOf(game), game);
                }
            }
            allGamesMutableLiveData.postValue(allGamesResult);
        }

        if (favouriteGamesMutableLiveData.getValue() != null && favouriteGamesMutableLiveData.getValue().isSuccess()) {
            favouriteGames.clear();
            Result.Success result = new Result.Success(new GamesResponse(0, "", "", favouriteGames));
            favouriteGamesMutableLiveData.postValue(result);
        }
    }


    public MutableLiveData<Result> fetchSingleGame(String slug){
        gamesRemoteDataSource.getSingleGame(slug);
        return singleGameMutableLiveData;
    }
    @Override
    public void onSuccessSingleGameFromRemote(GameDetail game) {
        Result.SuccessSingleGame result = new Result.SuccessSingleGame(game);
        singleGameMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureSingleGameFromRemote(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        singleGameMutableLiveData.postValue(result);
    }

    public MutableLiveData<Result> fetchGenres(long lastUpdate){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > 1000) {
            gamesRemoteDataSource.getGenres();
        } else {
            gamesLocalDataSource.getGenres();
        }
        return genresMutableLiveData;
    }

    @Override
    public void onSuccessFromRemoteGenre(GenreResponse response) {
        gamesLocalDataSource.insertGenres(response);
    }

    @Override
    public void onFailureFromRemoteGenre(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        singleGameMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocalGenre(GenreResponse response) {
        if (genresMutableLiveData.getValue() != null && genresMutableLiveData.getValue().isSuccessGenres()) {
            List<Genre> genreList = ((Result.SuccessGenres)genresMutableLiveData.getValue()).getData().getResults();
            genreList.addAll(response.getResults());
            response.setResults(genreList);
            Result.SuccessGenres result = new Result.SuccessGenres(response);
            genresMutableLiveData.postValue(result);
        } else {
            Result.SuccessGenres result = new Result.SuccessGenres(response);
            genresMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromLocalGenre(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        genresMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccessFromCloudReading(List<Game> games) {
        if(games != null){
            Log.d(TAG, "dati presi " + String.valueOf(games.size()));
            favouriteGamesMutableLiveData.postValue(new Result.Success(new GamesResponse(0, "", "", games)));
        }
    }

    @Override
    public void onSuccessFromCloudWriting(Game game) {
        backupDataSource.getFavouriteGames();
    }

    @Override
    public void onFailureFromCloud(Exception exception) {
        Result.Error result = new Result.Error(exception.getMessage());
        favouriteGamesMutableLiveData.postValue(result);
    }


    public void addFavourite(Game game){
        backupDataSource.addFavouriteGame(game);
    }

    public void deleteFavourite(Game game){
        backupDataSource.deleteFavouriteGame(game);
    }
}
