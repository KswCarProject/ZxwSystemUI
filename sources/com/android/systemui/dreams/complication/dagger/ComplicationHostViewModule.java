package com.android.systemui.dreams.complication.dagger;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.util.Preconditions;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;

public abstract class ComplicationHostViewModule {
    public static ConstraintLayout providesComplicationHostView(LayoutInflater layoutInflater) {
        return (ConstraintLayout) Preconditions.checkNotNull((ConstraintLayout) layoutInflater.inflate(R$layout.dream_overlay_complications_layer, (ViewGroup) null), "R.layout.dream_overlay_complications_layer did not properly inflated");
    }

    public static int providesComplicationPadding(Resources resources) {
        return resources.getDimensionPixelSize(R$dimen.dream_overlay_complication_margin);
    }

    public static int providesComplicationsFadeOutDuration(Resources resources) {
        return resources.getInteger(R$integer.complicationFadeOutMs);
    }

    public static int providesComplicationsFadeInDuration(Resources resources) {
        return resources.getInteger(R$integer.complicationFadeInMs);
    }

    public static int providesComplicationsRestoreTimeout(Resources resources) {
        return resources.getInteger(R$integer.complicationRestoreMs);
    }
}
