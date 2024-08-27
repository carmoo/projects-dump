package com.example.gamesuniverse.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.model.User;
import com.example.gamesuniverse.repository.user.UserRepository;
import com.example.gamesuniverse.util.DataEncryptionUtil;
import com.example.gamesuniverse.util.ServiceLocator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private static final String TAG= WelcomeActivity.class.getSimpleName();

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;
    private LinearProgressIndicator progressIndicator;
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository();
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle SavedInstanceState){
        super.onViewCreated(view, SavedInstanceState);
        //assegno a passwordTextInputLayout la email inserita dall'utente
        emailTextInputLayout=view.findViewById(R.id.textInputMail);
        //assegno a passwordTextInputLayout la password inserita dall'utente
        passwordTextInputLayout=view.findViewById(R.id.textInputPassword);

        //userViewModel.getLoggedUser();
        progressIndicator = view.findViewById(R.id.progress_bar_login);

        //riferimenti ai bottoni
        Button loginButton=view.findViewById(R.id.button_login);
        Button registerButton=view.findViewById(R.id.buttonRegister);
        Button forgotButton=view.findViewById(R.id.buttonForgot);

        //dataEncryptionUtil.deleteAll("com.example.gamesuniverse.encrypted_preferences");

        try {
            Log.d(TAG, "Email address from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            "com.example.gamesuniverse.encrypted_preferences", "email_address"));
            Log.d(TAG, "Password from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            "com.example.gamesuniverse.encrypted_preferences", "password"));
            Log.d(TAG, "Token from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            "com.example.gamesuniverse.encrypted_preferences", "google_token"));
            Log.d(TAG, "Login data from encrypted file: " + dataEncryptionUtil.
                    readSecretDataOnFile("com.example.gamesuniverse.encrypted_preferences"));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        //listener loginButton
        loginButton.setOnClickListener(item -> {
            Log.d(TAG, "Click Bottone");
            String email=emailTextInputLayout.getEditText().getText().toString();
            String password=passwordTextInputLayout.getEditText().getText().toString();
            Log.d(TAG, "Email: "+ email);
            Log.d(TAG, "Passord "+ password);

            //Log.d(TAG, "Email: "+ isEmailOk(email));
            //Log.d(TAG, "Password "+ isEmailOk(password));

            boolean result_mail=isEmailOk(email);
            boolean result_password=isPasswordOk(password);
            if(result_mail == true) {
                emailTextInputLayout.setError(null);
                if (result_password == true) {
                    passwordTextInputLayout.setError(null);
                    // CONTINUA DA QUI CON LA CHIAMATA ALLA PROSSIMA ACTIVITY
                    User logUser = new User();
                    logUser.setEmail(email);
                    if (!userViewModel.isAuthenticationError()) {
                        progressIndicator.setVisibility(View.VISIBLE);
                        userViewModel.getUserMutableLiveData(logUser, password, true).observe(
                                getViewLifecycleOwner(), result -> {
                                    if (result.isSuccessUser()) {
                                        User user = ((Result.SuccessUser) result).getData();
                                        saveLoginData(email, password, user.getIdToken());
                                        userViewModel.setAuthenticationError(false);
                                        progressIndicator.setVisibility(View.GONE);
                                        Navigation.findNavController(view).navigate(LoginFragmentDirections.navigateToMainActivity());
                                    } else {
                                        userViewModel.setAuthenticationError(true);
                                        progressIndicator.setVisibility(View.GONE);
                                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                                ((Result.Error) result).getMessage(),
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        userViewModel.getUser(logUser, password, true);
                    }

                }
            }
            else{
                emailTextInputLayout.setError("the email isn't correct");

                // USO LA SNACKBAR
                        Snackbar.make(requireView(),
                        getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT).show();
            }
            if(result_password == false){
                passwordTextInputLayout.setError("password must have 8 or more characters");
            }
            else{
                passwordTextInputLayout.setError(null);
            }
        });
        //listener registerButton
        registerButton.setOnClickListener(item -> {
            //dico al navigation graph di andare a signInActivity
            Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        //listener forgotButton
        forgotButton.setOnClickListener(item -> {
            //dico al navigation graph di andare a forgotPasswordActivity
            Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment());
        });
    }

    private boolean isEmailOk(String email){

        //controlla se la mail è una mail esistente (usando un tool esterno)
        boolean result = EmailValidator.getInstance().isValid(email);

        return result;
    }

    private boolean isPasswordOk(String password){
        // controlla se la password è lunga almeno 8 caratteri
        boolean result= password != null && password.length()>= 8;
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
    public void onResume() {
        super.onResume();
        userViewModel.setAuthenticationError(false);
    }
}