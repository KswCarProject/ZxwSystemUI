package com.android.systemui.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressLint({"AppCompatCustomView"})
/* compiled from: SafeMarqueeTextView.kt */
public class SafeMarqueeTextView extends TextView {
    public boolean safelyIgnoreLayout;

    public SafeMarqueeTextView(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
    }

    public SafeMarqueeTextView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
    }

    public SafeMarqueeTextView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ SafeMarqueeTextView(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public SafeMarqueeTextView(@NotNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public final boolean getHasStableWidth() {
        return getLayoutParams().width != -2;
    }

    public void requestLayout() {
        if (!this.safelyIgnoreLayout) {
            super.requestLayout();
        }
    }

    public void startMarquee() {
        boolean z = this.safelyIgnoreLayout;
        this.safelyIgnoreLayout = getHasStableWidth();
        super.startMarquee();
        this.safelyIgnoreLayout = z;
    }

    public void stopMarquee() {
        boolean z = this.safelyIgnoreLayout;
        this.safelyIgnoreLayout = getHasStableWidth();
        super.stopMarquee();
        this.safelyIgnoreLayout = z;
    }
}
