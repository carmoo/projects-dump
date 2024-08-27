package com.example.gamesuniverse.source.games;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GameDetail;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.GenreResponse;

import java.util.List;

public interface GamesCallback {
    void onSuccessFromRemote(GamesResponse response);
    void onSuccessFromRemoteGenresGames(GamesResponse response);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(GamesResponse response);
    void onFailureFromLocal(Exception exception);
    void onGameFavouriteStatusChanged(Game game, List<Game> favouriteGams);
    void onGamesFavouriteStatusChanged(List<Game> games);
    void onDeleteFavouriteGameSuccess(List<Game> favouriteGames);

    void onSuccessSingleGameFromRemote(GameDetail game);
    void onFailureSingleGameFromRemote(Exception exception);

    void onSuccessFromRemoteGenre(GenreResponse response);
    void onFailureFromRemoteGenre(Exception exception);
    void onSuccessFromLocalGenre(GenreResponse response);
    void onFailureFromLocalGenre(Exception exception);

    void onSuccessFromCloudReading(List<Game> games);
    void onSuccessFromCloudWriting(Game game);
    void onFailureFromCloud(Exception exception);
}
