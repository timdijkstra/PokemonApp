package nl.cjlancas.mbda.pokemonapp.listeners;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int MARGIN = 5;

    private boolean loading = false;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (loading || layoutManager == null || adapter == null){
            return;
        }

        if (layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - MARGIN) {
            Handler handler = new Handler();
            loading = true;
            handler.post(() -> {
                loadMore();
                loading = false;
            });
        }
    }

    public abstract void loadMore();

}
