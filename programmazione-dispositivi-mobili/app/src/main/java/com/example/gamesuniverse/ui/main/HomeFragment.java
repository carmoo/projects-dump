package com.example.gamesuniverse.ui.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import com.example.gamesuniverse.model.GamesResponse;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.repository.games.GamesRepository;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private GamesViewModel gamesViewModel;
    private GamesRecyclerViewAdapter gamesRecyclerViewAdapter;
    private List<Game> gameList;
    private ProgressBar progressBar;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleItemCount;
    private final int threshold = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
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

        progressBar = view.findViewById(R.id.progress_bar_login);

        RecyclerView recyclerViewGames = view.findViewById(R.id.recyclerview_home);


        GridLayoutManager layoutManager =
                new GridLayoutManager(requireContext(),2);

        gamesRecyclerViewAdapter = new GamesRecyclerViewAdapter(gameList,
                requireActivity().getApplication(),
                new GamesRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onGameItemClick(Game game) {
                        Log.d(TAG, game.getSlug());
                        HomeFragmentDirections.ActionHomeFragmentToGameDetailFragment action =
                                HomeFragmentDirections.actionHomeFragmentToGameDetailFragment(game.getSlug());
                        Navigation.findNavController(view).navigate(action);
                    }
                });

        recyclerViewGames.setLayoutManager(layoutManager);
        recyclerViewGames.setAdapter(gamesRecyclerViewAdapter);

        progressBar.setVisibility(View.VISIBLE);

        gamesViewModel.getGames(0).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()){

                        GamesResponse gamesResponse = ((Result.Success) result).getData();
                        List<Game> fetchedGames = gamesResponse.getResults();

                        if (!gamesViewModel.isLoading()) {
                            if (gamesViewModel.isFirstLoading()){
                                gamesViewModel.setTotalResults(((GamesResponse) gamesResponse).getCount());
                                Log.d(TAG, "totale" + String.valueOf(gamesResponse.getCount()));
                                gamesViewModel.setFirstLoading(false);
                                this.gameList.addAll(fetchedGames);
                                gamesRecyclerViewAdapter.notifyItemRangeInserted(0, this.gameList.size());
                            } else {
                                gameList.clear();
                                gameList.addAll(fetchedGames);
                                gamesRecyclerViewAdapter.notifyItemChanged(0, fetchedGames.size());
                            }
                            progressBar.setVisibility(View.GONE);
                        } else {
                            gamesViewModel.setLoading(false);
                            gamesViewModel.setCurrentResults(gameList.size());
                            Log.d(TAG, "correnti" + String.valueOf(gamesViewModel.getCurrentResults()));
                            int initialSize = gameList.size();

                            for (int i = 0; i < gameList.size(); i++) {
                                if (gameList.get(i) == null) {
                                    gameList.remove(gameList.get(i));
                                }
                            }
                            int startIndex = (gamesViewModel.getPage()*20) - 20;
                            for (int i = startIndex; i < fetchedGames.size(); i++) {
                                gameList.add(fetchedGames.get(i));
                            }
                            gamesRecyclerViewAdapter.notifyItemRangeInserted(initialSize, gameList.size());
                        }
                    } else {
                        Log.d(TAG, "sono dall'altra parte");
                        Snackbar.make(view, "errore", Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

        recyclerViewGames.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isConnected = isConnected();

                if (isConnected && totalItemCount != gamesViewModel.getTotalResults()) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();

                    if (totalItemCount == visibleItemCount ||
                            (totalItemCount <= (lastVisibleItem + threshold) &&
                                    dy > 0 &&
                                    !gamesViewModel.isLoading()
                            ) &&
                                    gamesViewModel.getGamesResponse().getValue() != null &&
                                    gamesViewModel.getCurrentResults() != gamesViewModel.getTotalResults()
                    ) {
                        MutableLiveData<Result> gamesListMutableLiveData = gamesViewModel.getGamesResponse();

                        if (gamesListMutableLiveData.getValue() != null &&
                                gamesListMutableLiveData.getValue().isSuccess()) {

                            gamesViewModel.setLoading(true);
                            gameList.add(null);
                            gamesRecyclerViewAdapter.notifyItemRangeInserted(gameList.size(),
                                    gameList.size() + 1);

                            int page = gamesViewModel.getPage() + 1;
                            gamesViewModel.setPage(page);
                            gamesViewModel.fetchGames();
                        }
                    }
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gamesViewModel.setFirstLoading(true);
        gamesViewModel.setLoading(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}