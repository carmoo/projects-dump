package com.example.gamesuniverse.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.adapter.GamesRecyclerViewAdapter;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.Result;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {
    private static final String TAG = FavouritesFragment.class.getSimpleName();
    private GamesRecyclerViewAdapter gamesRecyclerViewAdapter;
    private GamesViewModel gamesViewModel;
    private List<Game> gameList;
    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameList = new ArrayList<>();
        gamesViewModel = new ViewModelProvider(requireActivity()).get(GamesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
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
            // Use getViewLifecycleOwner() to avoid that the listener
            // associated with a menu icon is called twice
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        RecyclerView recyclerViewGames = view.findViewById(R.id.recyclerview_favourites);
        GridLayoutManager layoutManager =
                new GridLayoutManager(requireContext(),2);

        gamesRecyclerViewAdapter = new GamesRecyclerViewAdapter(gameList,
                requireActivity().getApplication(),
                new GamesRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onGameItemClick(Game game) {
                        Log.d(TAG, game.getSlug());
                        FavouritesFragmentDirections.ActionFavouritesFragmentToGameDetailFragment action =
                                FavouritesFragmentDirections.actionFavouritesFragmentToGameDetailFragment(game.getSlug());
                        Navigation.findNavController(view).navigate(action);
                    }

                });
        recyclerViewGames.setLayoutManager(layoutManager);
        recyclerViewGames.setAdapter(gamesRecyclerViewAdapter);

        gamesViewModel.getFavouriteGamesLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result != null){
                if (result.isSuccess()) {
                    Log.d(TAG, "successo "+ ((Result.Success) result).getData().getResults().size());
                    gameList.clear();
                    List<Game> fetchedGames = ((Result.Success) result).getData().getResults();
                    gameList.addAll(fetchedGames);
                    gamesRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(view, "errere preferiti", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}