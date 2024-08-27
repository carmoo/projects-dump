package com.example.gamesuniverse.source.user;

import com.example.gamesuniverse.model.User;
import com.example.gamesuniverse.repository.user.UserResponseCallback;

import java.util.Set;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
}
