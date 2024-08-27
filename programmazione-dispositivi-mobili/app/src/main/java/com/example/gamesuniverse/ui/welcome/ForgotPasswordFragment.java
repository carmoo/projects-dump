package com.example.gamesuniverse.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.model.Result;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    private static final String TAG= ForgotPasswordFragment.class.getSimpleName();

    private TextInputLayout emailTextInputLayout;
    private UserViewModel userViewModel;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle SavedInstanceState) {
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

        super.onViewCreated(view, SavedInstanceState);
        emailTextInputLayout = view.findViewById(R.id.textInputMailReset);

        //riferimenti ai bottoni
        Button resetButton = view.findViewById(R.id.button_confirm_reset);

        //listener del signInButton
        resetButton.setOnClickListener(item -> {
            Log.d(TAG, "Click Bottone");
            String email = emailTextInputLayout.getEditText().getText().toString();
            Log.d(TAG, "Email: " + email);

            //controlli validità email e password
            boolean result_mail = isEmailOk(email);
            if (result_mail == true) {
                emailTextInputLayout.setError(null);
                userViewModel.resetPassword(email);
                Snackbar.make(requireView(), "mail inviata", Snackbar.LENGTH_SHORT).show();
            } else {
                //email non corretta
                emailTextInputLayout.setError("the email isn't correct");
            }
            });

    }

    private boolean isEmailOk(String email){
        //usando tool esterno controlla se la mail inserita esiste realmente
        boolean result = EmailValidator.getInstance().isValid(email);
        return result;
    }
}