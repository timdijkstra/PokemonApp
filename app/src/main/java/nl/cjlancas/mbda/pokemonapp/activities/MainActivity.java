package nl.cjlancas.mbda.pokemonapp.activities;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.File;
import java.io.IOException;
import androidx.fragment.app.Fragment;
import nl.cjlancas.mbda.pokemonapp.R;
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionBar = findViewById(R.id.action_bar);
        setSupportActionBar(actionBar);

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

    protected void onStop() {

        super.onStop();

        HttpResponseCache cache = HttpResponseCache.getInstalled();

        if (cache != null) {

            cache.flush();

        }
    }

}
