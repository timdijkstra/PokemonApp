package nl.cjlancas.mbda.pokemonapp.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {

    private static VolleyHelper instance;

    private RequestQueue requestQueue;

    private VolleyHelper(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHelper(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
