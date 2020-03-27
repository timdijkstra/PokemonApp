package nl.jvhaastert.mbda.pokemonapp.helpers;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class FavoritesHelper {

    private Set<Integer> favoriteIds;

    private static FavoritesHelper instance;

    private static final String FILE_NAME = "favorites";

    private FavoritesHelper(Context context) {
        readFile(context);
    }

    public Set<Integer> getFavoriteIds() {
        return favoriteIds;
    }

    public boolean isFavorite(Integer id) {
        return favoriteIds.contains(id);
    }

    public void setFavorite(Context context, Integer id, boolean favorite) {
        if (favorite) {
            favoriteIds.add(id);
        } else {
            favoriteIds.remove(id);
        }

        writeFile(context);
    }

    public static FavoritesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesHelper(context);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void readFile(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            favoriteIds = (Set<Integer>) objectInputStream.readObject();
        } catch (IOException e) {
            favoriteIds = new HashSet<>();
        } catch (ClassNotFoundException e) {
            Log.e("FavoriteHelper", e.getMessage(), e);
            favoriteIds = new HashSet<>();
        }
    }

    private void writeFile(Context context) {
        try (FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(favoriteIds);
        } catch (IOException e) {
            Log.e("FavoriteHelper", e.getMessage(), e);
        }
    }

}
