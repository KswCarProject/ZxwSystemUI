package com.android.systemui.util.animation;

import android.view.View;
import com.android.systemui.R$id;
import org.jetbrains.annotations.NotNull;

/* compiled from: UniqueObjectHostView.kt */
public final class UniqueObjectHostViewKt {
    public static final boolean getRequiresRemeasuring(@NotNull View view) {
        Object tag = view.getTag(R$id.requires_remeasuring);
        if (tag == null) {
            return false;
        }
        return tag.equals(Boolean.TRUE);
    }

    public static final void setRequiresRemeasuring(@NotNull View view, boolean z) {
        view.setTag(R$id.requires_remeasuring, Boolean.valueOf(z));
    }
}
