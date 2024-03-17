package com.android.systemui.statusbar.phone.panelstate;

import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.Nullable;

/* compiled from: PanelExpansionStateManager.kt */
public final class PanelExpansionStateManagerKt {
    @Nullable
    public static final String TAG = Reflection.getOrCreateKotlinClass(PanelExpansionStateManager.class).getSimpleName();

    public static final String stateToString(int i) {
        if (i == 0) {
            return "CLOSED";
        }
        if (i != 1) {
            return i != 2 ? String.valueOf(i) : "OPEN";
        }
        return "OPENING";
    }
}
