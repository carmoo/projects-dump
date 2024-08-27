package com.example.gamesuniverse.repository.user;

import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.User;

import java.util.List;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}