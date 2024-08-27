package com.example.gamesuniverse.source.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gamesuniverse.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * Class that manages the user authentication using Firebase Authentication.
 */
public class UserAuthenticationRemoteDataSource extends BaseUserAuthenticationRemoteDataSource {

    private static final String TAG = UserAuthenticationRemoteDataSource.class.getSimpleName();

    private final FirebaseAuth firebaseAuth;

    public UserAuthenticationRemoteDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return null;
        } else {
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
    }

    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    Log.d(TAG, "User logged out");
                    userResponseCallback.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }

    @Override
    public void signUp(User user, String password) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "registrazione effettuata");
                    user.setIdToken(firebaseUser.getUid());
                    userResponseCallback.onSuccessFromAuthentication(user);
                        //new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())

                } else {
                    Log.d(TAG, "errore 1");
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                Log.d(TAG, "errore 2");
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(TAG, "login effettuata");
                    userResponseCallback.onSuccessFromAuthentication(
                        new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())
                    );
                } else {
                    Log.d(TAG, "errore 1");
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                Log.d(TAG, "errore 2");
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void resetPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d(TAG, "mail inviata");
            }
        });
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "passwordIsWeak";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "invalidCredentials";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return "invalidUserError";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "userCollisionError";
        }
        return "unexpected_error";
    }
}
