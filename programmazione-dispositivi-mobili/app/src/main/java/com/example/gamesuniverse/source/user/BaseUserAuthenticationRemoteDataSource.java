package com.example.gamesuniverse.source.user;


import com.example.gamesuniverse.model.User;
import com.example.gamesuniverse.repository.user.UserResponseCallback;

/**
 * Base class to manage the user authentication.
 */
public abstract class BaseUserAuthenticationRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(User user, String password);
    public abstract void signIn(String email, String password);
    public abstract void resetPassword(String email);
}
