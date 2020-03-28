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
        //cache all files on creation up to 10 MiB
        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024;
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("cache", "Failed to create the cache:" + e);
        }

        //TODO: waarom wordt de super aangeroepen? en de savedInstance is informatie van een vorige sessie, right?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionBar = findViewById(R.id.action_bar);
        setSupportActionBar(actionBar);

        //TODO: waarom wordt de bottomnav hier geinitialiseerd?
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigationView.setOnNavigationItemReselectedListener(this::onNavigationItemReselected);

        if(savedInstanceState == null){
            replaceFragment(new ListFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container_view, fragment)
                .commit();
    }

    //TODO: wanneer wordt de cache werkelijk leeggemaakt?
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    //navigeren tussen tabs.
    private boolean onNavigationItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.list_menu_item){
            replaceFragment(new ListFragment());
            setTitle(R.string.list);
            return true;
        } else if (itemId == R.id.favorites_menu_item){
            replaceFragment(new FavoritesFragment());
            setTitle(R.string.favorites);
            return true;
        } else {
            return false;
        }
    }

    private void onNavigationItemReselected(MenuItem item) {

    }
}
