package com.google.android.material.resources;

import android.graphics.Typeface;

public final class CancelableFontCallback extends TextAppearanceFontCallback {
    public final ApplyFont applyFont;
    public boolean cancelled;
    public final Typeface fallbackFont;

    public interface ApplyFont {
        void apply(Typeface typeface);
    }

    public CancelableFontCallback(ApplyFont applyFont2, Typeface typeface) {
        this.fallbackFont = typeface;
        this.applyFont = applyFont2;
    }

    public void onFontRetrieved(Typeface typeface, boolean z) {
        updateIfNotCancelled(typeface);
    }

    public void onFontRetrievalFailed(int i) {
        updateIfNotCancelled(this.fallbackFont);
    }

    public void cancel() {
        this.cancelled = true;
    }

    public final void updateIfNotCancelled(Typeface typeface) {
        if (!this.cancelled) {
            this.applyFont.apply(typeface);
        }
    }
}
