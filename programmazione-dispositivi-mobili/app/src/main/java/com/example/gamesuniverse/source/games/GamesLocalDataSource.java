package com.example.gamesuniverse.source.games;

import com.example.gamesuniverse.database.GameDao;
import com.example.gamesuniverse.database.GamesRoomDatabase;
import com.example.gamesuniverse.database.GenreDao;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.Genre;
import com.example.gamesuniverse.model.GenreResponse;

import java.util.List;

public class GamesLocalDataSource extends BaseGamesLocalDataSource{
    private final GameDao gameDao;
    private final GenreDao genreDao;

    public GamesLocalDataSource(GamesRoomDatabase gamesRoomDatabase){
        this.gameDao = gamesRoomDatabase.gameDao();
        this.genreDao = gamesRoomDatabase.genreDao();
    }

    @Override
    public void getGames() {
        GamesRoomDatabase.databaseWriteExecutor.execute(() -> {
            GamesResponse gamesResponse = new GamesResponse();
            gamesResponse.setResults(gameDao.getAll());
            gamesCallback.onSuccessFromLocal(gamesResponse);
        });
    }

    @Override
    public void insertGames(GamesResponse response) {
        GamesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Game> allGames = gameDao.getAll();
            List<Game> gameList = response.getResults();

            if (gameList != null) {
                for (Game game : allGames) {
                    if (gameList.contains(game)) {
                        gameList.set(gameList.indexOf(game), game);
                    }
                }

                List<Long> insertedGamesIds = gameDao.insertGamesList(gameList);
                for (int i = 0; i < gameList.size(); i++) {
                    gameList.get(i).setId(insertedGamesIds.get(i));
                }
                gamesCallback.onSuccessFromLocal(response);
            }
        });
    }

    @Override
    public void getGenres() {
        GamesRoomDatabase.databaseWriteExecutor.execute(() -> {
            GenreResponse genreResponse = new GenreResponse();
            genreResponse.setResults(genreDao.getAll());
            gamesCallback.onSuccessFromLocalGenre(genreResponse);
        });
    }

    @Override
    public void insertGenres(GenreResponse response) {
        GamesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Genre> allGenres = genreDao.getAll();
            List<Genre> genreList = response.getResults();

            if (genreList != null) {
                for (Genre genre : allGenres) {
                    if (genreList.contains(genre)) {
                        genreList.set(genreList.indexOf(genre), genre);
                    }
                }

                List<Long> insertedGenresIds = genreDao.insertGenresList(genreList);
                for (int i = 0; i < genreList.size(); i++) {
                    genreList.get(i).setId(insertedGenresIds.get(i));
                }
                gamesCallback.onSuccessFromLocalGenre(response);
            }
        });
    }
}
