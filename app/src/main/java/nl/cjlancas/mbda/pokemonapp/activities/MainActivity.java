package nl.cjlancas.mbda.pokemonapp.activities;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.File;
import java.io.IOException;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import nl.cjlancas.mbda.pokemonapp.R;
import nl.cjlancas.mbda.pokemonapp.fragments.FavoritesFragment;
import nl.cjlancas.mbda.pokemonapp.fragments.ListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("cache", "Something went wrong while installing the cache:" + e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionBar = findViewById(R.id.action_bar);
        setSupportActionBar(actionBar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigationView.setOnNavigationItemReselectedListener(this::onNavigationItemReselected);

        if(savedInstanceState == null) replaceFragment(new ListFragment());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container_view, fragment)
                .commit();
    }

    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_menu_item:
                replaceFragment(new ListFragment());
                setTitle(R.string.app_name);

                return true;
            case R.id.favorites_menu_item:
                replaceFragment(new FavoritesFragment());
                setTitle(R.string.favorites);

                return true;
            default:
                return false;
        }
    }

    private void onNavigationItemReselected(MenuItem item) {
    }
}
