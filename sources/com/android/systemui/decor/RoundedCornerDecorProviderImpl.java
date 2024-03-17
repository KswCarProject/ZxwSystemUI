package com.android.systemui.decor;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RoundedCornerDecorProviderImpl.kt */
public final class RoundedCornerDecorProviderImpl extends CornerDecorProvider {
    public final int alignedBound1;
    public final int alignedBound2;
    public final boolean isTop = getAlignedBounds().contains(1);
    @NotNull
    public final RoundedCornerResDelegate roundedCornerResDelegate;
    public final int viewId;

    public int getViewId() {
        return this.viewId;
    }

    public int getAlignedBound1() {
        return this.alignedBound1;
    }

    public int getAlignedBound2() {
        return this.alignedBound2;
    }

    public RoundedCornerDecorProviderImpl(int i, int i2, int i3, @NotNull RoundedCornerResDelegate roundedCornerResDelegate2) {
        this.viewId = i;
        this.alignedBound1 = i2;
        this.alignedBound2 = i3;
        this.roundedCornerResDelegate = roundedCornerResDelegate2;
    }

    @NotNull
    public View inflateView(@NotNull Context context, @NotNull ViewGroup viewGroup, int i) {
        Size size;
        ImageView imageView = new ImageView(context);
        imageView.setId(getViewId());
        initView(imageView, i);
        if (this.isTop) {
            size = this.roundedCornerResDelegate.getTopRoundedSize();
        } else {
            size = this.roundedCornerResDelegate.getBottomRoundedSize();
        }
        viewGroup.addView(imageView, new FrameLayout.LayoutParams(size.getWidth(), size.getHeight(), RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound2(), i) | RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound1(), i)));
        return imageView;
    }

    public final void initView(ImageView imageView, int i) {
        RoundedCornerDecorProviderImplKt.setRoundedCornerImage(imageView, this.roundedCornerResDelegate, this.isTop);
        RoundedCornerDecorProviderImplKt.adjustRotation(imageView, getAlignedBounds(), i);
        imageView.setImageTintList(this.roundedCornerResDelegate.getColorTintList());
    }

    public void onReloadResAndMeasure(@NotNull View view, int i, int i2, @Nullable String str) {
        Size size;
        this.roundedCornerResDelegate.updateDisplayUniqueId(str, Integer.valueOf(i));
        ImageView imageView = (ImageView) view;
        initView(imageView, i2);
        if (this.isTop) {
            size = this.roundedCornerResDelegate.getTopRoundedSize();
        } else {
            size = this.roundedCornerResDelegate.getBottomRoundedSize();
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.width = size.getWidth();
            layoutParams2.height = size.getHeight();
            layoutParams2.gravity = RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound2(), i2) | RoundedCornerDecorProviderImplKt.toLayoutGravity(getAlignedBound1(), i2);
            view.setLayoutParams(layoutParams2);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
    }
}
