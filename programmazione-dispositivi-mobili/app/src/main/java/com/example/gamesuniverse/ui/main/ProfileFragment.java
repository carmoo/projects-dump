package com.example.gamesuniverse.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.repository.user.UserRepository;
import com.example.gamesuniverse.ui.welcome.LoginFragmentDirections;
import com.example.gamesuniverse.ui.welcome.UserViewModel;
import com.example.gamesuniverse.ui.welcome.UserViewModelFactory;
import com.example.gamesuniverse.util.DataEncryptionUtil;
import com.example.gamesuniverse.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileFragment extends Fragment {

    private TextView username;
    private Button logout;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository();
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        String ret = null;
        try {
            ret = dataEncryptionUtil.readSecretDataWithEncryptedSharedPreferences(
                    "com.example.gamesuniverse.encrypted_preferences", "email_address");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        username = view.findViewById(R.id.username);
        username.setText(ret);
        logout = view.findViewById(R.id.button_logout);


        logout.setOnClickListener(item -> {
            userViewModel.logout();
            Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionFragmentProfileToWelcomeActivity());
            /*userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccessUser()) {
                    Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionFragmentProfileToWelcomeActivity());
                } else {
                    Snackbar.make(view, "unexpected error", Snackbar.LENGTH_SHORT).show();
                }
            });*/
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
