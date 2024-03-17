package com.android.systemui.decor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDotDecorProviderFactory.kt */
public final class PrivacyDotCornerDecorProviderImpl extends CornerDecorProvider {
    public final int alignedBound1;
    public final int alignedBound2;
    public final int layoutId;
    public final int viewId;

    public void onReloadResAndMeasure(@NotNull View view, int i, int i2, @Nullable String str) {
    }

    public int getViewId() {
        return this.viewId;
    }

    public int getAlignedBound1() {
        return this.alignedBound1;
    }

    public int getAlignedBound2() {
        return this.alignedBound2;
    }

    public PrivacyDotCornerDecorProviderImpl(int i, int i2, int i3, int i4) {
        this.viewId = i;
        this.alignedBound1 = i2;
        this.alignedBound2 = i3;
        this.layoutId = i4;
    }

    @NotNull
    public View inflateView(@NotNull Context context, @NotNull ViewGroup viewGroup, int i) {
        LayoutInflater.from(context).inflate(this.layoutId, viewGroup, true);
        return viewGroup.getChildAt(viewGroup.getChildCount() - 1);
    }
}
