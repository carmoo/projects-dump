package com.example.gamesuniverse.source.games;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GameDetail;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.GenreResponse;
import com.example.gamesuniverse.service.GamesApiService;
import com.example.gamesuniverse.util.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesRemoteDataSource extends BaseGamesRemoteDataSource{
    private static final String TAG = GamesRemoteDataSource.class.getSimpleName();
    private final GamesApiService gamesApiService;
    private final String apiKey;

    public GamesRemoteDataSource(){
        gamesApiService = ServiceLocator.getInstance().getGamesApiService();
        apiKey = "eccaef4f3c5c42d28cdee4a9f9a1bfbb";
    }

    @Override
    public void getGames(int page) {
        Call<GamesResponse> gamesResponseCall = gamesApiService.getGames(apiKey, page);
        gamesResponseCall.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(@NonNull Call<GamesResponse> call,
                                   @NonNull Response<GamesResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, String.valueOf(games.size()));
                    Log.d(TAG, games.get(1).getName());
                    Log.d(TAG, String.valueOf(response.body().getCount()));
                    Log.d(TAG, games.get(1).getBackground_image());
                    gamesCallback.onSuccessFromRemote(response.body());
                } else {
                    gamesCallback.onFailureFromRemote(new Exception("Retrofit error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GamesResponse> call, @NonNull Throwable t) {
                gamesCallback.onFailureFromRemote(new Exception("Retrofit error"));
            }
        });
    }

    @Override
    public void getGames(long genres) {
        Call<GamesResponse> gamesResponseCall = gamesApiService.getGames(apiKey, genres);
        gamesResponseCall.enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(@NonNull Call<GamesResponse> call,
                                   @NonNull Response<GamesResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Game> games = response.body().getResults();
                    Log.d(TAG, String.valueOf(games.size()));
                    gamesCallback.onSuccessFromRemoteGenresGames(response.body());
                } else {
                    gamesCallback.onFailureFromRemote(new Exception("Retrofit error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GamesResponse> call, @NonNull Throwable t) {
                gamesCallback.onFailureFromRemote(new Exception("Retrofit error"));
            }
        });
    }

    @Override
    public void getSingleGame(String slug) {
        Call<GameDetail> gamesResponseCall = gamesApiService.getSingleGame(slug, apiKey);
        gamesResponseCall.enqueue(new Callback<GameDetail>() {
            @Override
            public void onResponse(@NonNull Call<GameDetail> call,
                                   @NonNull Response<GameDetail> response) {

                if (response.body() != null && response.isSuccessful()) {
                    GameDetail games = response.body();
                    Log.d(TAG, "gioco ottenuto");
                    gamesCallback.onSuccessSingleGameFromRemote(response.body());
                } else {
                    //Log.d(TAG, String.valueOf(response.code()));
                    gamesCallback.onFailureSingleGameFromRemote(new Exception("Retrofit error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameDetail> call, @NonNull Throwable t) {
                gamesCallback.onFailureSingleGameFromRemote(new Exception("Retrofit error"));
            }
        });
    }

    @Override
    public void getGenres() {
        Call<GenreResponse> genresResponseCall = gamesApiService.getGenres(apiKey);
        genresResponseCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenreResponse> call,
                                   @NonNull Response<GenreResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    //List<Genre> genres = response.body().getResults();
                    Log.d(TAG, "genres ottenuti");
                    gamesCallback.onSuccessFromRemoteGenre(response.body());
                } else {
                    gamesCallback.onFailureFromRemoteGenre(new Exception("Retrofit error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                gamesCallback.onFailureFromRemoteGenre(new Exception("Retrofit error"));
            }
        });
    }
}
