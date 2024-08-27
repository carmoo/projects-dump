package com.example.gamesuniverse.ui.welcome;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.model.User;
import com.example.gamesuniverse.util.DataEncryptionUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG= SignUpFragment.class.getSimpleName();

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout surnameTextInputLayout;

    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;
    private LinearProgressIndicator progressIndicator;

    public SignUpFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle SavedInstanceState) {

        super.onViewCreated(view, SavedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        emailTextInputLayout = view.findViewById(R.id.textInputMail);
        //assegno a passwordTextInputLayout la password inserita dall'utente
        passwordTextInputLayout = view.findViewById(R.id.textInputPassword);
        //assegno a passwordTextInputLayout la mail inserita dall'utente
        confirmPasswordTextInputLayout = view.findViewById(R.id.textInputConfirmPassword);

        nameTextInputLayout=view.findViewById(R.id.TextInputName);
        surnameTextInputLayout=view.findViewById(R.id.TextInputSurname);

        progressIndicator = view.findViewById(R.id.progress_bar_registration);
        //riferimenti ai bottoni
        Button signInButton = view.findViewById(R.id.button_confirm_reset);
        ImageButton backButton = view.findViewById(R.id.backButton);

        //listener del signInButton
        signInButton.setOnClickListener(item -> {
            Log.d(TAG, "Click Bottone");
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            String confirmPassword = confirmPasswordTextInputLayout.getEditText().getText().toString();
            String name = nameTextInputLayout.getEditText().getText().toString();
            String surname = surnameTextInputLayout.getEditText().getText().toString();

            Log.d(TAG, "Email: " + email);
            Log.d(TAG, "Passord " + password);
            Log.d(TAG, "Confirm Password" + confirmPassword);

            //controlli validità email e password
            boolean result_mail = isEmailOk(email);
            boolean result_password = isPasswordOk(password);
            boolean result_confirm_password = isConfirmPasswordOk(password, confirmPassword);
            if (result_mail == true) {
                emailTextInputLayout.setError(null);
                if (result_password == true) {
                    passwordTextInputLayout.setError(null);
                    if (result_confirm_password == true) {
                        confirmPasswordTextInputLayout.setError(null);
                        //CONOTINUA DA QUI PER SALVARE IL NUOVO UTENTE

                        User regUser = new User(name, surname, email, "");

                        userViewModel.setAuthenticationError(false);
                        if (!userViewModel.isAuthenticationError()) {
                            progressIndicator.setVisibility(View.VISIBLE);
                            userViewModel.getUserMutableLiveData(regUser, password, false).observe(
                                    getViewLifecycleOwner(), result -> {
                                        if (result.isSuccessUser()) {
                                            User user = ((Result.SuccessUser) result).getData();
                                            saveLoginData(email, password, user.getIdToken());
                                            userViewModel.setAuthenticationError(false);
                                            progressIndicator.setVisibility(View.GONE);
                                            Navigation.findNavController(view).navigate(R.id.mainActivity);
                                        } else {
                                            userViewModel.setAuthenticationError(true);
                                            progressIndicator.setVisibility(View.GONE);
                                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                                    ((Result.Error) result).getMessage(),
                                                    Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            userViewModel.getUser(regUser, password, false);
                        }
                    }
                }
            } else {
                //email non corretta
                emailTextInputLayout.setError("the email isn't correct");
                userViewModel.setAuthenticationError(true);
            }
            if (result_password == false) {
                // password non corretta (meno di 8 caratteri)
                passwordTextInputLayout.setError("password must have 8 or more characters");
                userViewModel.setAuthenticationError(true);
            } else {
                passwordTextInputLayout.setError(null);
            }
            if (result_confirm_password == false) {
                // le due password non corrispondono
                confirmPasswordTextInputLayout.setError("the two passwords aren't equals");
                userViewModel.setAuthenticationError(true);
            } else {
                confirmPasswordTextInputLayout.setError(null);
            }
        });

        //lister backButton
        backButton.setOnClickListener(item -> {
            //dico al navigation graph di tornare a LoginActivity
            Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        });

    }

    private boolean isEmailOk(String email){
        //usando tool esterno controlla se la mail inserita esiste realmente
        boolean result = EmailValidator.getInstance().isValid(email);
        return result;
    }

    private boolean isPasswordOk(String password){
        // controlla se la password è lunga almeno 8 caratteri
        boolean result= password != null && password.length()>= 8;
        return result;
    }

    private boolean isConfirmPasswordOk(String password, String confirmedPassword){
        // controlla se password e confirmedPassword sono uguali
        boolean result = (password != null && confirmedPassword != null) && password.equals(confirmedPassword);
        return result;
    }

    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.gamesuniverse.encrypted_preferences", "email_address", email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.gamesuniverse.encrypted_preferences", "password", password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    "com.example.gamesuniverse.encrypted_preferences", "google_token", idToken);
            dataEncryptionUtil.writeSecreteDataOnFile("com.example.gamesuniverse.encrypted_preferences",
                    email.concat(":").concat(password));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}