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
import android.widget.ProgressBar;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.adapter.GamesRecyclerViewAdapter;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.repository.games.GamesRepository;
import com.example.gamesuniverse.util.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class GenreFragment extends Fragment {
    private static final String TAG = GenreFragment.class.getSimpleName();
    private GamesRecyclerViewAdapter gamesRecyclerViewAdapter;
    private GamesViewModel gamesViewModel;
    private List<Game> gameList;
    private ProgressBar progressBar;

    public GenreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GamesRepository gamesRepository = ServiceLocator.getInstance().getGamsRepository(requireActivity().getApplication());
        gamesViewModel = new ViewModelProvider(requireActivity(), new GamesViewModelFactory(gamesRepository)).get(GamesViewModel.class);
        gameList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genre, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        progressBar = view.findViewById(R.id.progress_bar_genre);

        long genre = GenreFragmentArgs.fromBundle(getArguments()).getGenre().getId();

        RecyclerView recyclerViewGames = view.findViewById(R.id.recyclerview_genres_game);
        GridLayoutManager layoutManager =
                new GridLayoutManager(requireContext(),2);

        gamesRecyclerViewAdapter = new GamesRecyclerViewAdapter(gameList,
                requireActivity().getApplication(),
                new GamesRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onGameItemClick(Game game) {
                        Log.d(TAG, game.getName());
                        GenreFragmentDirections.ActionGenreFragmentToGameDetailFragment action =
                                GenreFragmentDirections.actionGenreFragmentToGameDetailFragment(game.getSlug());
                        Navigation.findNavController(view).navigate(action);
                    }
                });

        recyclerViewGames.setLayoutManager(layoutManager);
        recyclerViewGames.setAdapter(gamesRecyclerViewAdapter);

        progressBar.setVisibility(View.VISIBLE);

        gamesViewModel.getGenresGames(genre).observe(getViewLifecycleOwner(),
                result -> {
                    if(result.isSuccess()){
                        gameList.clear();
                        GamesResponse gamesResponse = ((Result.Success) result).getData();
                        List<Game> fetchedGames = gamesResponse.getResults();
                        this.gameList.addAll(fetchedGames);
                        gamesRecyclerViewAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
