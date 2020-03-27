package nl.cjlancas.mbda.pokemonapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.cjlancas.mbda.pokemonapp.R;
import nl.cjlancas.mbda.pokemonapp.adapters.PokemonAdapter;
import nl.cjlancas.mbda.pokemonapp.helpers.FavoritesHelper;
import nl.cjlancas.mbda.pokemonapp.helpers.VolleyHelper;
import nl.cjlancas.mbda.pokemonapp.models.Pokemon;

public class FavoritesFragment extends Fragment {

    private PokemonAdapter adapter;

    private FavoritesHelper favoritesHelper;
    private VolleyHelper volleyHelper;

    private static final String GET_POKEMON_URL_FORMAT = "https://pokeapi.co/api/v2/pokemon/%d";

    public FavoritesFragment() {
        super(R.layout.fragment_favorites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favoritesHelper = FavoritesHelper.getInstance(view.getContext());
        volleyHelper = VolleyHelper.getInstance(view.getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new PokemonAdapter(new ArrayList<>());

        RecyclerView recyclerView = view.findViewById(R.id.pokemon_favorites_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clearData();
        favoritesHelper.getFavoriteIds().forEach(this::retrievePokemon);
    }

    @SuppressLint("DefaultLocale")
    private void retrievePokemon(int id) {
        JsonObjectRequest request = new JsonObjectRequest(
                String.format(GET_POKEMON_URL_FORMAT, id),
                null,
                response -> {
                    Pokemon favoritePokemon = convertResponseToPokemonList(response);
                    adapter.appendData(favoritePokemon);
                },
                error -> Log.e("FavoritesFragment", error.getMessage(), error)
        );
        volleyHelper.addToRequestQueue(request);
    }

    @SuppressLint("DefaultLocale")
    private Pokemon convertResponseToPokemonList(JSONObject response) {
        try {
            String url = String.format(GET_POKEMON_URL_FORMAT, response.getInt("id"));
            String name = response.getString("name");

            return new Pokemon(url, name);
        } catch (JSONException e) {
            Log.e("FavoritesFragment", e.getMessage(), e);
            return null;
        }
    }

}
