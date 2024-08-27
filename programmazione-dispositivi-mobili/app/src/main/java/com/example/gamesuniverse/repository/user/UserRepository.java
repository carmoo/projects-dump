package com.example.gamesuniverse.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.gamesuniverse.source.user.BaseUserDataRemoteDataSource;
import com.example.gamesuniverse.model.User;

import java.util.List;

public class UserRepository implements UserResponseCallback{

    private final BaseUserAuthenticationRemoteDataSource userAuthRemoteDataSource;
    private final BaseUserDataRemoteDataSource userRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource baseUserAuthenticationRemoteDataSource,
                          BaseUserDataRemoteDataSource baseUserRemoteDataSource){
        this.userAuthRemoteDataSource = baseUserAuthenticationRemoteDataSource;
        this.userRemoteDataSource = baseUserRemoteDataSource;
        userMutableLiveData = new MutableLiveData<>();
        userAuthRemoteDataSource.setUserResponseCallback(this);
        userRemoteDataSource.setUserResponseCallback(this);
    }

    public MutableLiveData<Result> getUser(User user, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(user.getEmail(), password);
        } else {
            signUp(user, password);
        }
        return userMutableLiveData;
    }

    public User getLoggedUser() {
        return userAuthRemoteDataSource.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        userAuthRemoteDataSource.logout();
        return userMutableLiveData;
    }

    public void signUp(User user, String password) {
        userAuthRemoteDataSource.signUp(user, password);
    }

    public void signIn(String email, String password) {
        userAuthRemoteDataSource.signIn(email, password);
    }

    public void resetPassword(String email){
        userAuthRemoteDataSource.resetPassword(email);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.SuccessUser result = new Result.SuccessUser(user);
        userMutableLiveData.postValue(result);
    }


    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {

    }
}
