package nl.cjlancas.mbda.pokemonapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import nl.cjlancas.mbda.pokemonapp.R;

public class CheckableImageButton extends androidx.appcompat.widget.AppCompatImageButton implements Checkable {

    private boolean checked;
    private OnCheckedChangeListener onCheckedChangeListener;

    private static final int[] CHECKED_STATE = {android.R.attr.state_checked};

    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.imageButtonStyle);
    }

    public CheckableImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnCheckedChangedListener(OnCheckedChangeListener onCheckedChangedListener) {
        this.onCheckedChangeListener = onCheckedChangedListener;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;

        refreshDrawableState();
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, checked);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE);
        }

        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckableImageButton view, boolean checked);
    }

}
