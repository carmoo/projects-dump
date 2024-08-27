package com.example.gamesuniverse.ui.welcome;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamesuniverse.repository.user.UserRepository;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    private final UserRepository userRepository;

    public UserViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserViewModel(userRepository);
    }
}
