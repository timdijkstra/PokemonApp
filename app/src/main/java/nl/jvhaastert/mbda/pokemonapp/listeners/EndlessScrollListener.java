package nl.jvhaastert.mbda.pokemonapp.listeners;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int MARGIN = 4;

    private boolean loading = false;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (loading || layoutManager == null || adapter == null) return;

        if (layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - MARGIN) {
            loading = true;

            Handler handler = new Handler();
            handler.post(() -> {
                loadMore();
                loading = false;
            });
        }
    }

    public abstract void loadMore();

}
