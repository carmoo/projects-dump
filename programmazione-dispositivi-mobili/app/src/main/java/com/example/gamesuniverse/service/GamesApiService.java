package com.example.gamesuniverse.service;

import com.example.gamesuniverse.model.GameDetail;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.GenreResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GamesApiService {

    @GET("games")
    Call<GamesResponse> getGames(
            @Query("key") String apiKey,
            @Query("page") int page
    );

    @GET("games")
    Call<GamesResponse> getGames(
            @Query("key") String apiKey,
            @Query("genres") long genres
    );

    @GET("games/{slug}")
    Call<GameDetail> getSingleGame(
            @Path("slug") String gameSlug,
            @Query("key") String apiKey
    );

    @GET("genres")
    Call<GenreResponse> getGenres(
            @Query("key") String apiKey
    );
}
