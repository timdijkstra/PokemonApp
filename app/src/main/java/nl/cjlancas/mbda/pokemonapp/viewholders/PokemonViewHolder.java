package nl.cjlancas.mbda.pokemonapp.viewholders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nl.cjlancas.mbda.pokemonapp.R;
import nl.cjlancas.mbda.pokemonapp.helpers.StringHelper;

public class PokemonViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView idTextView;

    public PokemonViewHolder(@NonNull View itemView) {
        super(itemView);

        this.nameTextView = itemView.findViewById(R.id.pokemon_name_text_view);
        this.idTextView = itemView.findViewById(R.id.pokemon_id_text_view);
    }

    public void setName(String name) {
        nameTextView.setText(StringHelper.capitalize(name));
    }

    @SuppressLint("SetTextI18n")
    public void setId(int id) {
        idTextView.setText("#" + id);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

}
