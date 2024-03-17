package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;

public abstract class EndIconDelegate {
    public Context context;
    public final int customEndIcon;
    public CheckableImageButton endIconView;
    public TextInputLayout textInputLayout;

    public abstract void initialize();

    public boolean isBoxBackgroundModeSupported(int i) {
        return true;
    }

    public void onSuffixVisibilityChanged(boolean z) {
    }

    public boolean shouldTintIconOnError() {
        return false;
    }

    public EndIconDelegate(TextInputLayout textInputLayout2, int i) {
        this.textInputLayout = textInputLayout2;
        this.context = textInputLayout2.getContext();
        this.endIconView = textInputLayout2.getEndIconView();
        this.customEndIcon = i;
    }
}
