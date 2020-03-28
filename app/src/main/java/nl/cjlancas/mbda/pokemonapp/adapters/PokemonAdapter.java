package nl.cjlancas.mbda.pokemonapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import nl.cjlancas.mbda.pokemonapp.R;
import nl.cjlancas.mbda.pokemonapp.activities.PokemonActivity;
import nl.cjlancas.mbda.pokemonapp.models.Pokemon;
import nl.cjlancas.mbda.pokemonapp.viewholders.PokemonViewHolder;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonViewHolder> {

    private List<Pokemon> pokemons;

    public PokemonAdapter(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    // In this method a new ViewHolder is created.
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    // This method is called after onCreateViewHolder. Here the data should be set.
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.setName(pokemon.getName());

        holder.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intent = new Intent(context, PokemonActivity.class);
            intent.putExtra("url", pokemon.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void clearData() {
        pokemons.clear();
        notifyDataSetChanged();
    }

    public void appendData(Pokemon pokemon) {
        appendData(Collections.singletonList(pokemon));
    }

    public void appendData(List<Pokemon> pokemons) {
        int oldItemCount = this.pokemons.size();
        this.pokemons.addAll(pokemons);
        notifyItemRangeInserted(oldItemCount, pokemons.size());
    }

}
