package nl.jvhaastert.mbda.pokemonapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import nl.jvhaastert.mbda.pokemonapp.R;
import nl.jvhaastert.mbda.pokemonapp.helpers.FavoritesHelper;
import nl.jvhaastert.mbda.pokemonapp.helpers.ImageHelper;
import nl.jvhaastert.mbda.pokemonapp.helpers.StringHelper;
import nl.jvhaastert.mbda.pokemonapp.models.Pokemon;
import nl.jvhaastert.mbda.pokemonapp.models.Stat;
import nl.jvhaastert.mbda.pokemonapp.models.Type;
import nl.jvhaastert.mbda.pokemonapp.views.CheckableImageButton;

import static android.app.Activity.RESULT_OK;

public class PokemonFragment extends Fragment {

    private Pokemon pokemon;

    private CheckableImageButton favoriteCheckableImageButton;
    private TextView nameTextView;
    private TextView idTextView;
    private TextView typesTextView;
    private ImageView frontImageView;
    private Button saveImageButton;
    private TextView statsTextView;
    private Button shareButton;
    private Button changeImageButton;
    private FragmentActivity activity;
    private ImageHelper imageHelper;
    private static final int RESULT_LOAD_IMAGE = 1;
    private FavoritesHelper favoritesHelper;
    private static final int STORAGE_PERMISSION_CODE = 1;

    public PokemonFragment() {
        super(R.layout.fragment_pokemon);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments == null) return;

        activity = getActivity();
        imageHelper = new ImageHelper();
        favoritesHelper = FavoritesHelper.getInstance(activity);

        favoriteCheckableImageButton = view.findViewById(R.id.pokemon_favorite_checkable_image_button);
        nameTextView = view.findViewById(R.id.pokemon_name_text_view);
        idTextView = view.findViewById(R.id.pokemon_id_text_view);
        typesTextView = view.findViewById(R.id.pokemon_types_text_view);
        frontImageView = view.findViewById(R.id.pokemon_image_view);
        saveImageButton = view.findViewById(R.id.pokemon_save_image_button);
        statsTextView = view.findViewById(R.id.pokemon_stats_text_view);
        shareButton = view.findViewById(R.id.pokemon_share_stats_button);
        changeImageButton = view.findViewById(R.id.pokemon_change_image_button);

        pokemon = arguments.getParcelable("pokemon");
        if (pokemon == null) return;

        populateViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Toegang toegekend", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateViews() {
        try {
            favoriteCheckableImageButton.setChecked(favoritesHelper.isFavorite(pokemon.getId()));
            favoriteCheckableImageButton.setOnCheckedChangedListener(this::onFavoriteCheckedChanged);

            String name = StringHelper.capitalize(pokemon.getName());
            nameTextView.setText(name);

            String id = "#" + pokemon.getId();
            idTextView.setText(id);

            String typesString = pokemon.getTypes()
                    .stream()
                    .sorted(Comparator.comparingInt(Type::getSlot))
                    .map(Type::getName)
                    .map(StringHelper::capitalize)
                    .collect(Collectors.joining(", "));
            typesTextView.setText(typesString);

            Bitmap bitmap = new DownloadImageTask()
                    .execute(pokemon.getImageUrl())
                    .get();
            frontImageView.setImageBitmap(bitmap);

            saveImageButton.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    imageHelper.saveImageToFile(frontImageView, getActivity());
                } else {
                    requestWriteExternalStoragePermission();
                }
            });

            changeImageButton.setOnClickListener(v -> {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            });


            String statsString = pokemon.getStats()
                    .stream()
                    .map(Stat::getBaseStat)
                    .map(StringHelper::capitalize)
                    .collect(Collectors.joining("\n"));
            statsTextView.setText(statsString);

            shareButton.setOnClickListener(v -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, name + "\n" + typesString + "\n" + statsString);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            });
        } catch (ExecutionException | InterruptedException e) {
            Log.e("PokemonFragment", e.getMessage(), e);
        }
    }

    private void requestWriteExternalStoragePermission() {
        /*
         * If the user has declined permission before but asks for access again,
         * we explain why we are asking for this permission.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Waarom vragen wij toegang?")
                    .setMessage("Wij hebben toegang tot uw telefoonopslag nodig om zodoende de afbeelding op te kunnen slaan")
                    .setPositiveButton("Oke!", (dialog, which) ->
                            ActivityCompat.requestPermissions(
                                    activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE))
                    .setNegativeButton("Annuleren", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    private void onFavoriteCheckedChanged(CheckableImageButton view, boolean checked) {
        favoritesHelper.setFavorite(activity, pokemon.getId(), checked);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try (InputStream imageStream = activity.getContentResolver().openInputStream(imageUri)) {
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                frontImageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                Log.e("PokemonFragment", e.getMessage(), e);
                Toast.makeText(activity, "Iets ging fout, probeer het aub nogmaals", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(activity, "Je hebt geen afbeelding geselecteerd. Probeer het aub nogmaals",Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String apiRequestUrl = urls[0];

            try {
                //Make an API request for the image
                InputStream in = new java.net.URL(apiRequestUrl).openStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            } catch (Exception e) {
                Log.e("PokemonFragment", e.getMessage(), e);
                return null;
            }
        }

    }

}
