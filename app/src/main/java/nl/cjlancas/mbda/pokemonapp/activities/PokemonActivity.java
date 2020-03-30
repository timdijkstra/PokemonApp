package nl.cjlancas.mbda.pokemonapp.activities;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.cjlancas.mbda.pokemonapp.R;
import nl.cjlancas.mbda.pokemonapp.fragments.PokemonFragment;
import nl.cjlancas.mbda.pokemonapp.helpers.VolleyHelper;
import nl.cjlancas.mbda.pokemonapp.models.Pokemon;
import nl.cjlancas.mbda.pokemonapp.models.Stat;
import nl.cjlancas.mbda.pokemonapp.models.Type;

public class PokemonActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        setupActionBar();

        //get information from intent with the url key
        url = getIntent().getStringExtra("url");
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                this::onResponse,
                error -> Log.e("PokemonActivity", error.getMessage(), error)
        );
        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }


    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void onResponse(JSONObject response) {
        try {
            String name = response.getString("name");
            int id = response.getInt("id");
            List<Type> types = getPokemonTypes(response);
            String imageUrl = getImageUrl(response);
            List<Stat> stats = getStats(response);

            Pokemon pokemon = new Pokemon(url, name, id, types, imageUrl, stats);

            showPokemonFragment(pokemon);
        } catch (JSONException e) {
            Log.e("PokemonActivity", e.getMessage(), e);
        }
    }

    private void showPokemonFragment(Pokemon pokemon) {

        Bundle arguments = new Bundle();

        arguments.putParcelable("pokemon", pokemon);

        PokemonFragment pokemonFragment = new PokemonFragment();

        pokemonFragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.pokemon_fragment_container_view, pokemonFragment)
                .commit();
    }

    private List<Type> getPokemonTypes(JSONObject pokemonObject) {

        try {
            ArrayList<Type> types = new ArrayList<>();
            JSONArray typesArray = pokemonObject.getJSONArray("types");

            for (int i = 0; i < typesArray.length(); i++) {

                JSONObject arrayObject = typesArray.getJSONObject(i);
                int slot = arrayObject.getInt("slot");

                JSONObject typeObject = arrayObject.getJSONObject("type");
                String typeName = typeObject.getString("name");

                Type type = new Type(slot, typeName);
                types.add(type);
            }

            return types;

        } catch (JSONException e) {
            Log.e("PokemonActivity", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private String getImageUrl(JSONObject pokemonObject) {
        try {
            JSONObject spritesArray = pokemonObject.getJSONObject("sprites");
            return spritesArray.getString("front_default");
        } catch (JSONException e) {
            Log.e("PokemonActivity", e.getMessage(), e);
            return null;
        }
    }

    private List<Stat> getStats(JSONObject pokemonObject) {
        try {
            ArrayList<Stat> stats = new ArrayList<>();

            JSONArray statsArray = pokemonObject.getJSONArray("stats");
            for (int i = 0; i < statsArray.length(); i++) {
                JSONObject baseStatObject = statsArray.getJSONObject(i);

                JSONObject statNameObject = baseStatObject.getJSONObject("stat");
                String statName = statNameObject.getString("name");

                int baseStat = baseStatObject.getInt("base_stat");

                Stat stat = new Stat(statName + ": " + baseStat);
                stats.add(stat);
            }

            return stats;
        } catch (JSONException e) {

            Log.e("PokemonActivity", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
