package com.example.gamesuniverse.util;

import android.app.Application;

import com.example.gamesuniverse.database.GamesRoomDatabase;
import com.example.gamesuniverse.repository.games.GamesRepository;
import com.example.gamesuniverse.repository.user.UserRepository;
import com.example.gamesuniverse.service.GamesApiService;
import com.example.gamesuniverse.source.games.BaseFavouriteGamesDataSource;
import com.example.gamesuniverse.source.games.BaseGamesLocalDataSource;
import com.example.gamesuniverse.source.games.BaseGamesRemoteDataSource;
import com.example.gamesuniverse.source.games.FavouriteGamesDataSource;
import com.example.gamesuniverse.source.games.GamesLocalDataSource;
import com.example.gamesuniverse.source.games.GamesRemoteDataSource;
import com.example.gamesuniverse.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.gamesuniverse.source.user.BaseUserDataRemoteDataSource;
import com.example.gamesuniverse.source.user.UserAuthenticationRemoteDataSource;
import com.example.gamesuniverse.source.user.UserDataRemoteDataSource;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }
    public GamesApiService getGamesApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.rawg.io/api/").
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(GamesApiService.class);
    }

    public GamesRoomDatabase getGameDao(Application application) {
        return GamesRoomDatabase.getDatabase(application);
    }

    public GamesRepository getGamsRepository(Application application) {
        BaseGamesRemoteDataSource gamesRemoteDataSource = new GamesRemoteDataSource();
        BaseGamesLocalDataSource gamesLocalDataSource = new GamesLocalDataSource(getGameDao(application));
        BaseFavouriteGamesDataSource favouriteGamesDataSource;
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        try {
            favouriteGamesDataSource = new FavouriteGamesDataSource(dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            "com.example.gamesuniverse.encrypted_preferences", "google_token"
                    )
            );
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
        return new GamesRepository(gamesRemoteDataSource, gamesLocalDataSource, favouriteGamesDataSource);
    }

    public UserRepository getUserRepository() {
        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource();

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }
}
