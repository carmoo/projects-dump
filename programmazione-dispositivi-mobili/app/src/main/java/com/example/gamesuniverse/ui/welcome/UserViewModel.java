package com.example.gamesuniverse.ui.welcome;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.model.User;
import com.example.gamesuniverse.repository.user.UserRepository;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final UserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private boolean authenticationError;

    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            User user, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(user, password, isUserRegistered);
        }
        return userMutableLiveData;
    }


    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public void resetPassword(String email){
        userRepository.resetPassword(email);
    }


    public void getUser(User user, String password, boolean isUserRegistered) {
        userRepository.getUser(user, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }


    private void getUserData(User user, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(user, password, isUserRegistered);
    }

}
