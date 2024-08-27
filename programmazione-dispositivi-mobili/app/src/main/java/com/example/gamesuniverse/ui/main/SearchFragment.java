package com.example.gamesuniverse.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamesuniverse.R;
import com.example.gamesuniverse.adapter.GenresRecyclerViewAdapter;
import com.example.gamesuniverse.model.Genre;
import com.example.gamesuniverse.model.GenreResponse;
import com.example.gamesuniverse.model.Result;
import com.example.gamesuniverse.repository.games.GamesRepository;
import com.example.gamesuniverse.util.ServiceLocator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private TextInputLayout searchTextInputLayout;
    private GamesViewModel gamesViewModel;
    private GenresRecyclerViewAdapter genresRecyclerViewAdapter;
    private List<Genre> genresList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GamesRepository gamesRepository = ServiceLocator.getInstance().getGamsRepository(requireActivity().getApplication());
        gamesViewModel = new ViewModelProvider(requireActivity(), new GamesViewModelFactory(gamesRepository)).get(GamesViewModel.class);
        genresList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle SavedInstanceState){
        super.onViewCreated(view, SavedInstanceState);

        searchTextInputLayout = view.findViewById(R.id.textInputSearch);

        searchTextInputLayout.setStartIconOnClickListener(item -> {
            String name = searchTextInputLayout.getEditText().getText().toString();
            String slug = fixString(name);
            Log.d(TAG, slug);

            SearchFragmentDirections.ActionSearchFragmentToGameDetailFragment action =
                    SearchFragmentDirections.actionSearchFragmentToGameDetailFragment(slug);
            Navigation.findNavController(view).navigate(action);
        });

        RecyclerView recyclerViewGenres = view.findViewById(R.id.recyclerview_genres);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        genresRecyclerViewAdapter = new GenresRecyclerViewAdapter(genresList,
                requireActivity().getApplication(),
                new GenresRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onGenreItemClick(Genre genre) {
                        Log.d(TAG, genre.getName());
                        SearchFragmentDirections.ActionSearchFragmentToGenreFragment action =
                                SearchFragmentDirections.actionSearchFragmentToGenreFragment(genre);
                        Navigation.findNavController(view).navigate(action);
                    }
                });

        recyclerViewGenres.setLayoutManager(layoutManager);
        recyclerViewGenres.setAdapter(genresRecyclerViewAdapter);

        gamesViewModel.getGenres(0).observe(getViewLifecycleOwner(),
                result -> {
                    if(result.isSuccessGenres()){
                        GenreResponse genreResponse = ((Result.SuccessGenres) result).getData();
                        List<Genre> fetchedGenres = genreResponse.getResults();
                        this.genresList.addAll(fetchedGenres);
                        genresRecyclerViewAdapter.notifyItemRangeInserted(0, this.genresList.size());
                    }
                });
    }

    private String fixString(String name){
        String slug = name.toLowerCase();
        slug = slug.replaceAll(":", "");
        slug = slug.replaceAll("\\s", "-");
        return slug;
    }

}