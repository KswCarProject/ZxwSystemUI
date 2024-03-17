package com.android.systemui.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.monet.ColorScheme;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition {
    @NotNull
    public final AnimatingColorTransition accentPrimary;
    @NotNull
    public final AnimatingColorTransition accentSecondary;
    public final int bgColor;
    @NotNull
    public final AnimatingColorTransition bgGradientEnd;
    @NotNull
    public final AnimatingColorTransition bgGradientStart;
    @NotNull
    public final AnimatingColorTransition colorSeamless;
    @NotNull
    public final AnimatingColorTransition[] colorTransitions;
    @NotNull
    public final Context context;
    public boolean isGradientEnabled;
    @NotNull
    public final MediaViewHolder mediaViewHolder;
    @NotNull
    public final AnimatingColorTransition surfaceColor;
    @NotNull
    public final AnimatingColorTransition textPrimary;
    @NotNull
    public final AnimatingColorTransition textPrimaryInverse;
    @NotNull
    public final AnimatingColorTransition textSecondary;
    @NotNull
    public final AnimatingColorTransition textTertiary;

    public ColorSchemeTransition(@NotNull Context context2, @NotNull MediaViewHolder mediaViewHolder2, @NotNull Function3<? super Integer, ? super Function1<? super ColorScheme, Integer>, ? super Function1<? super Integer, Unit>, ? extends AnimatingColorTransition> function3) {
        this.context = context2;
        this.mediaViewHolder = mediaViewHolder2;
        this.isGradientEnabled = true;
        int color = context2.getColor(R$color.material_dynamic_secondary95);
        this.bgColor = color;
        AnimatingColorTransition animatingColorTransition = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), ColorSchemeTransition$surfaceColor$1.INSTANCE, new ColorSchemeTransition$surfaceColor$2(this));
        this.surfaceColor = animatingColorTransition;
        AnimatingColorTransition animatingColorTransition2 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$accentPrimary$1.INSTANCE, new ColorSchemeTransition$accentPrimary$2(this));
        this.accentPrimary = animatingColorTransition2;
        AnimatingColorTransition animatingColorTransition3 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$accentSecondary$1.INSTANCE, new ColorSchemeTransition$accentSecondary$2(this));
        this.accentSecondary = animatingColorTransition3;
        AnimatingColorTransition animatingColorTransition4 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), new ColorSchemeTransition$colorSeamless$1(this), new ColorSchemeTransition$colorSeamless$2(this));
        this.colorSeamless = animatingColorTransition4;
        AnimatingColorTransition animatingColorTransition5 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$textPrimary$1.INSTANCE, new ColorSchemeTransition$textPrimary$2(this));
        this.textPrimary = animatingColorTransition5;
        AnimatingColorTransition animatingColorTransition6 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842809)), ColorSchemeTransition$textPrimaryInverse$1.INSTANCE, new ColorSchemeTransition$textPrimaryInverse$2(this));
        this.textPrimaryInverse = animatingColorTransition6;
        AnimatingColorTransition animatingColorTransition7 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842808)), ColorSchemeTransition$textSecondary$1.INSTANCE, new ColorSchemeTransition$textSecondary$2(this));
        this.textSecondary = animatingColorTransition7;
        AnimatingColorTransition animatingColorTransition8 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16843282)), ColorSchemeTransition$textTertiary$1.INSTANCE, new ColorSchemeTransition$textTertiary$2(this));
        this.textTertiary = animatingColorTransition8;
        AnimatingColorTransition animatingColorTransition9 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), albumGradientPicker(ColorSchemeTransition$bgGradientStart$1.INSTANCE, 0.25f), new ColorSchemeTransition$bgGradientStart$2(this));
        this.bgGradientStart = animatingColorTransition9;
        AnimatingColorTransition animatingColorTransition10 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), albumGradientPicker(ColorSchemeTransition$bgGradientEnd$1.INSTANCE, 0.9f), new ColorSchemeTransition$bgGradientEnd$2(this));
        this.bgGradientEnd = animatingColorTransition10;
        this.colorTransitions = new AnimatingColorTransition[]{animatingColorTransition, animatingColorTransition4, animatingColorTransition2, animatingColorTransition3, animatingColorTransition5, animatingColorTransition6, animatingColorTransition7, animatingColorTransition8, animatingColorTransition9, animatingColorTransition10};
    }

    public ColorSchemeTransition(@NotNull Context context2, @NotNull MediaViewHolder mediaViewHolder2) {
        this(context2, mediaViewHolder2, AnonymousClass1.INSTANCE);
    }

    public final int getBgColor() {
        return this.bgColor;
    }

    @NotNull
    public final AnimatingColorTransition getAccentPrimary() {
        return this.accentPrimary;
    }

    public final void updateAlbumGradient() {
        Drawable foreground = this.mediaViewHolder.getAlbumView().getForeground();
        Drawable mutate = foreground == null ? null : foreground.mutate();
        if (mutate instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) mutate;
            int[] iArr = new int[2];
            AnimatingColorTransition animatingColorTransition = this.bgGradientStart;
            int i = 0;
            iArr[0] = animatingColorTransition == null ? 0 : animatingColorTransition.getCurrentColor();
            AnimatingColorTransition animatingColorTransition2 = this.bgGradientEnd;
            if (animatingColorTransition2 != null) {
                i = animatingColorTransition2.getCurrentColor();
            }
            iArr[1] = i;
            gradientDrawable.setColors(iArr);
        }
    }

    public final Function1<ColorScheme, Integer> albumGradientPicker(Function1<? super ColorScheme, Integer> function1, float f) {
        return new ColorSchemeTransition$albumGradientPicker$1(this, function1, f);
    }

    public final int loadDefaultColor(int i) {
        return Utils.getColorAttr(this.context, i).getDefaultColor();
    }

    public final void updateColorScheme(@Nullable ColorScheme colorScheme, boolean z) {
        this.isGradientEnabled = z;
        AnimatingColorTransition[] animatingColorTransitionArr = this.colorTransitions;
        int length = animatingColorTransitionArr.length;
        int i = 0;
        while (i < length) {
            AnimatingColorTransition animatingColorTransition = animatingColorTransitionArr[i];
            i++;
            animatingColorTransition.updateColorScheme(colorScheme);
        }
        if (colorScheme != null) {
            this.mediaViewHolder.getGutsViewHolder().setColorScheme(colorScheme);
        }
    }
}
