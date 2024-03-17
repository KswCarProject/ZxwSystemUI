package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;

public class ViewOverlayApi18 implements ViewOverlayImpl {
    public final ViewOverlay viewOverlay;

    public ViewOverlayApi18(View view) {
        this.viewOverlay = view.getOverlay();
    }

    public void add(Drawable drawable) {
        this.viewOverlay.add(drawable);
    }

    public void remove(Drawable drawable) {
        this.viewOverlay.remove(drawable);
    }
}
