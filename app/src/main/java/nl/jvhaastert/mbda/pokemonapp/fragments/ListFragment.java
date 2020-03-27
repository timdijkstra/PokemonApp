package nl.jvhaastert.mbda.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.jvhaastert.mbda.pokemonapp.R;
import nl.jvhaastert.mbda.pokemonapp.adapters.PokemonAdapter;
import nl.jvhaastert.mbda.pokemonapp.helpers.VolleyHelper;
import nl.jvhaastert.mbda.pokemonapp.listeners.EndlessScrollListener;
import nl.jvhaastert.mbda.pokemonapp.models.Pokemon;

public class ListFragment extends Fragment {

    private static final String GET_ALL_POKEMONS_URL = "https://pokeapi.co/api/v2/pokemon";

    private RecyclerView recyclerView;
    private PokemonAdapter adapter;

    private String nextUrl;

    public ListFragment() {
        super(R.layout.fragment_list);

        VolleyLog.DEBUG = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.pokemon_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void loadMore() {
                if (nextUrl == null) return;

                executeAllPokemonsRequest(view.getContext(), nextUrl, ListFragment.this::appendData);
            }
        });

        executeAllPokemonsRequest(view.getContext(), GET_ALL_POKEMONS_URL, this::createAdapter);
    }

    private void createAdapter(JSONObject response) {
        List<Pokemon> pokemons = convertResponseToPokemonList(response);

        adapter = new PokemonAdapter(pokemons);
        recyclerView.setAdapter(adapter);
    }

    private void appendData(JSONObject response) {
        List<Pokemon> pokemons = convertResponseToPokemonList(response);
        adapter.appendData(pokemons);
    }

    private void executeAllPokemonsRequest(Context context, String url, Response.Listener<JSONObject> onSuccessListener) {
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                onSuccessListener,
                error -> Log.e("ListFragment", error.getMessage(), error)
        );
        VolleyHelper.getInstance(context).addToRequestQueue(request);
    }

    private List<Pokemon> convertResponseToPokemonList(JSONObject response) {
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        try {
            nextUrl = !response.isNull("next") ? response.getString("next") : null;

            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);

                String url = result.getString("url");
                String name = result.getString("name");

                Pokemon pokemon = new Pokemon(url, name);
                pokemons.add(pokemon);
            }
        } catch (JSONException e) {
            Log.e("ListFragment", e.getMessage(), e);
        }

        return pokemons;
    }

}
