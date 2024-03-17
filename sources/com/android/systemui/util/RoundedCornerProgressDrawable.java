package com.android.systemui.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RoundedCornerProgressDrawable.kt */
public final class RoundedCornerProgressDrawable extends InsetDrawable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public RoundedCornerProgressDrawable() {
        this((Drawable) null, 1, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ RoundedCornerProgressDrawable(Drawable drawable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : drawable);
    }

    public RoundedCornerProgressDrawable(@Nullable Drawable drawable) {
        super(drawable, 0);
    }

    /* compiled from: RoundedCornerProgressDrawable.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public boolean onLayoutDirectionChanged(int i) {
        onLevelChange(getLevel());
        return super.onLayoutDirectionChanged(i);
    }

    public void onBoundsChange(@NotNull Rect rect) {
        super.onBoundsChange(rect);
        onLevelChange(getLevel());
    }

    public boolean onLevelChange(int i) {
        Drawable drawable = getDrawable();
        Rect bounds = drawable == null ? null : drawable.getBounds();
        Intrinsics.checkNotNull(bounds);
        int height = getBounds().height() + (((getBounds().width() - getBounds().height()) * i) / 10000);
        Drawable drawable2 = getDrawable();
        if (drawable2 != null) {
            drawable2.setBounds(getBounds().left, bounds.top, getBounds().left + height, bounds.bottom);
        }
        return super.onLevelChange(i);
    }

    @NotNull
    public Drawable.ConstantState getConstantState() {
        Drawable.ConstantState constantState = super.getConstantState();
        Intrinsics.checkNotNull(constantState);
        return new RoundedCornerState(constantState);
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | 4096;
    }

    public boolean canApplyTheme() {
        Drawable drawable = getDrawable();
        return (drawable == null ? false : drawable.canApplyTheme()) || super.canApplyTheme();
    }

    /* compiled from: RoundedCornerProgressDrawable.kt */
    public static final class RoundedCornerState extends Drawable.ConstantState {
        @NotNull
        public final Drawable.ConstantState wrappedState;

        public boolean canApplyTheme() {
            return true;
        }

        public RoundedCornerState(@NotNull Drawable.ConstantState constantState) {
            this.wrappedState = constantState;
        }

        @NotNull
        public Drawable newDrawable() {
            return newDrawable((Resources) null, (Resources.Theme) null);
        }

        @NotNull
        public Drawable newDrawable(@Nullable Resources resources, @Nullable Resources.Theme theme) {
            Drawable newDrawable = this.wrappedState.newDrawable(resources, theme);
            if (newDrawable != null) {
                return new RoundedCornerProgressDrawable(((DrawableWrapper) newDrawable).getDrawable());
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.DrawableWrapper");
        }

        public int getChangingConfigurations() {
            return this.wrappedState.getChangingConfigurations();
        }
    }
}
