package com.example.gamesuniverse.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamesuniverse.repository.games.GamesRepository;

public class GamesViewModelFactory implements ViewModelProvider.Factory {
    private final GamesRepository gamesRepository;

    public GamesViewModelFactory(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GamesViewModel(gamesRepository);
    }
}
