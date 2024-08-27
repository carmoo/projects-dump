package com.example.gamesuniverse.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamesuniverse.R;
import com.example.gamesuniverse.adapter.GameGenresRecyclerViewAdapter;
import com.example.gamesuniverse.adapter.PlatformsRecyclerViewAdapter;
import com.example.gamesuniverse.adapter.PublishersRecyclerViewAdapter;
import com.example.gamesuniverse.model.Game;
import com.example.gamesuniverse.model.GameDetail;
import com.example.gamesuniverse.model.Genre;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.repository.games.GamesRepository;
import com.example.gamesuniverse.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class GameDetailFragment extends Fragment {

    private static final String TAG = GameDetailFragment.class.getSimpleName();
    private GamesViewModel gamesViewModel;
    private ImageView gameCover;
    private ImageView backgroundImage;
    private TextView gameTitle;
    private TextView gameDescription;
    private TextView rating;
    private TextView releaseDate;
    private TextView playTime;
    private ProgressBar progressBar;
    private List<GameDetail.Platform> platformList;
    private PlatformsRecyclerViewAdapter platformsRecyclerViewAdapter;
    private List<Genre> genresList;
    private GameGenresRecyclerViewAdapter gameGenresRecyclerViewAdapter;
    private List<GameDetail.Publisher> publishersList;
    private PublishersRecyclerViewAdapter publishersRecyclerViewAdapter;
    public GameDetailFragment() {
        // Required empty public constructor
    }
    public static GameDetailFragment newInstance() {
        return new GameDetailFragment();
    }
    public static String html2text(final String html) {
        Document document = Jsoup.parse(html);
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        return document.text().replaceAll("\\\\n", "\n");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GamesRepository gamesRepository = ServiceLocator.getInstance().getGamsRepository(requireActivity().getApplication());
        gamesViewModel = new ViewModelProvider(requireActivity(), new GamesViewModelFactory(gamesRepository)).get(GamesViewModel.class);
        platformList = new ArrayList<>();
        genresList = new ArrayList<>();
        publishersList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_detail, container, false);
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
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        Button favouriteButton = view.findViewById(R.id.button_favourite);
        Button deleteFavouriteButton = view.findViewById(R.id.button_delete_favourite);

        gameCover = view.findViewById(R.id.imageView_Game_Logo);
        backgroundImage = view.findViewById(R.id.imageView_Game_Background);
        gameTitle = view.findViewById(R.id.textView_Game_Title);
        gameDescription = view.findViewById(R.id.textView_Game_Description);
        rating = view.findViewById(R.id.textView_Game_Rating);
        releaseDate = view.findViewById(R.id.textView_Game_ReleaseDate);
        playTime = view.findViewById(R.id.textView_Game_PlayTime);


        String slug = GameDetailFragmentArgs.fromBundle(getArguments()).getSlug();

        // Piattaforme
        RecyclerView recyclerViewPlatforms = view.findViewById(R.id.ReclyclerView_Game_Platform);

        LinearLayoutManager platformLayoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false);

        platformsRecyclerViewAdapter = new PlatformsRecyclerViewAdapter(platformList,requireActivity().getApplication());

        recyclerViewPlatforms.setLayoutManager(platformLayoutManager);
        recyclerViewPlatforms.setAdapter(platformsRecyclerViewAdapter);

        // Generi
        RecyclerView recyclerViewGenres = view.findViewById(R.id.ReclyclerView_Game_Genre);

        LinearLayoutManager genreLayoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false);

        gameGenresRecyclerViewAdapter = new GameGenresRecyclerViewAdapter(genresList,requireActivity().getApplication());

        recyclerViewGenres.setLayoutManager(genreLayoutManager);
        recyclerViewGenres.setAdapter(gameGenresRecyclerViewAdapter);

        // Publishers
        RecyclerView recyclerViewPublishers = view.findViewById(R.id.ReclyclerView_Game_Publisher);

        LinearLayoutManager publisherLayoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL, false);

        publishersRecyclerViewAdapter = new PublishersRecyclerViewAdapter(publishersList,requireActivity().getApplication());

        recyclerViewPublishers.setLayoutManager(publisherLayoutManager);
        recyclerViewPublishers.setAdapter(publishersRecyclerViewAdapter);

        gamesViewModel.getSingleGame(slug).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccessSingleGame()){
                        GameDetail singleGame = ((Result.SuccessSingleGame) result).getData();
                        Log.d(TAG, singleGame.getName());

                        Glide.with(requireActivity())
                                .load(singleGame.getBackground_image())
                                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                                .into(gameCover);
                        Glide.with(requireActivity())
                                .load(singleGame.getBackground_image_additional())
                                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                                .into(backgroundImage);
                        gameTitle.setText(singleGame.getName());
                        gameDescription.setText(html2text(singleGame.getDescription()));
                        rating.setText(String.valueOf(singleGame.getRating()));
                        releaseDate.setText(String.valueOf(singleGame.getReleased()));
                        playTime.setText(String.valueOf(singleGame.getPlaytime()));

                        platformList.clear();
                        platformList.addAll(singleGame.getPlatforms());

                        platformsRecyclerViewAdapter.notifyDataSetChanged();

                        genresList.clear();
                        genresList.addAll(singleGame.getGenres());

                        gameGenresRecyclerViewAdapter.notifyDataSetChanged();

                        publishersList.clear();
                        publishersList.addAll(singleGame.getPublishers());

                        publishersRecyclerViewAdapter.notifyDataSetChanged();

                        favouriteButton.setOnClickListener(item -> {
                            Game game = new Game(singleGame.getId(), singleGame.getName(), singleGame.getSlug(),
                                    singleGame.getReleased(), singleGame.isTba(), singleGame.getBackground_image(),
                                    singleGame.getRating(), singleGame.getMetacritic(), singleGame.getPlaytime(),
                                    singleGame.getUpdated());
                            Snackbar.make(view, "added", Snackbar.LENGTH_SHORT).show();
                            gamesViewModel.addFavourite(game);
                        });
                        deleteFavouriteButton.setOnClickListener(item -> {
                            Game game = new Game(singleGame.getId(), singleGame.getName(), singleGame.getSlug(),
                                    singleGame.getReleased(), singleGame.isTba(), singleGame.getBackground_image(),
                                    singleGame.getRating(), singleGame.getMetacritic(), singleGame.getPlaytime(),
                                    singleGame.getUpdated());
                            Snackbar.make(view, "deleted", Snackbar.LENGTH_SHORT).show();
                            gamesViewModel.removeFromFavourite(game);
                        });

                        //progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "gioco non preso");
                        Snackbar.make(view, "game not found", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigateUp();
                        //progressBar.setVisibility(View.GONE);
                    }
                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}