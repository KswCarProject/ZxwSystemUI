package com.android.systemui.media;

import android.content.res.ColorStateList;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.monet.ColorScheme;
import java.util.Set;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: GutsViewHolder.kt */
public final class GutsViewHolder {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Set<Integer> ids = SetsKt__SetsKt.setOf(Integer.valueOf(R$id.remove_text), Integer.valueOf(R$id.cancel), Integer.valueOf(R$id.dismiss), Integer.valueOf(R$id.settings));
    @NotNull
    public final View cancel;
    @NotNull
    public final TextView cancelText;
    @Nullable
    public ColorScheme colorScheme;
    @NotNull
    public final ViewGroup dismiss;
    @NotNull
    public final TextView dismissText;
    @NotNull
    public final TextView gutsText;
    public boolean isDismissible = true;
    @NotNull
    public final ImageButton settings;

    public GutsViewHolder(@NotNull View view) {
        this.gutsText = (TextView) view.requireViewById(R$id.remove_text);
        this.cancel = view.requireViewById(R$id.cancel);
        this.cancelText = (TextView) view.requireViewById(R$id.cancel_text);
        this.dismiss = (ViewGroup) view.requireViewById(R$id.dismiss);
        this.dismissText = (TextView) view.requireViewById(R$id.dismiss_text);
        this.settings = (ImageButton) view.requireViewById(R$id.settings);
    }

    @NotNull
    public final TextView getGutsText() {
        return this.gutsText;
    }

    @NotNull
    public final View getCancel() {
        return this.cancel;
    }

    @NotNull
    public final TextView getCancelText() {
        return this.cancelText;
    }

    @NotNull
    public final ViewGroup getDismiss() {
        return this.dismiss;
    }

    @NotNull
    public final TextView getDismissText() {
        return this.dismissText;
    }

    @NotNull
    public final ImageButton getSettings() {
        return this.settings;
    }

    public final void setColorScheme(@Nullable ColorScheme colorScheme2) {
        this.colorScheme = colorScheme2;
    }

    public final void marquee(boolean z, long j, @NotNull String str) {
        Handler handler = this.gutsText.getHandler();
        if (handler == null) {
            Log.d(str, "marquee while longPressText.getHandler() is null", new Exception());
        } else {
            handler.postDelayed(new GutsViewHolder$marquee$1(this, z), j);
        }
    }

    public final void setDismissible(boolean z) {
        if (this.isDismissible != z) {
            this.isDismissible = z;
            ColorScheme colorScheme2 = this.colorScheme;
            if (colorScheme2 != null) {
                setColors(colorScheme2);
            }
        }
    }

    public final void setColors(@NotNull ColorScheme colorScheme2) {
        this.colorScheme = colorScheme2;
        setSurfaceColor(MediaColorSchemesKt.surfaceFromScheme(colorScheme2));
        setTextPrimaryColor(MediaColorSchemesKt.textPrimaryFromScheme(colorScheme2));
        setAccentPrimaryColor(MediaColorSchemesKt.accentPrimaryFromScheme(colorScheme2));
    }

    public final void setSurfaceColor(int i) {
        this.dismissText.setTextColor(i);
        if (!this.isDismissible) {
            this.cancelText.setTextColor(i);
        }
    }

    public final void setAccentPrimaryColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.settings.setImageTintList(valueOf);
        this.cancelText.setBackgroundTintList(valueOf);
        this.dismissText.setBackgroundTintList(valueOf);
    }

    public final void setTextPrimaryColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.gutsText.setTextColor(valueOf);
        if (this.isDismissible) {
            this.cancelText.setTextColor(valueOf);
        }
    }

    /* compiled from: GutsViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final Set<Integer> getIds() {
            return GutsViewHolder.ids;
        }
    }
}
