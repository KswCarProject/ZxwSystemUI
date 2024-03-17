package com.android.systemui.statusbar.notification.collection.provider;

import android.util.ArraySet;
import com.android.systemui.util.ListenerSet;
import org.jetbrains.annotations.NotNull;

/* compiled from: VisualStabilityProvider.kt */
public final class VisualStabilityProvider {
    @NotNull
    public final ListenerSet<OnReorderingAllowedListener> allListeners = new ListenerSet<>();
    public boolean isReorderingAllowed = true;
    @NotNull
    public final ArraySet<OnReorderingAllowedListener> temporaryListeners = new ArraySet<>();

    public final boolean isReorderingAllowed() {
        return this.isReorderingAllowed;
    }

    public final void setReorderingAllowed(boolean z) {
        if (this.isReorderingAllowed != z) {
            this.isReorderingAllowed = z;
            if (z) {
                notifyReorderingAllowed();
            }
        }
    }

    public final void notifyReorderingAllowed() {
        for (OnReorderingAllowedListener onReorderingAllowedListener : this.allListeners) {
            if (this.temporaryListeners.remove(onReorderingAllowedListener)) {
                this.allListeners.remove(onReorderingAllowedListener);
            }
            onReorderingAllowedListener.onReorderingAllowed();
        }
    }

    public final void addPersistentReorderingAllowedListener(@NotNull OnReorderingAllowedListener onReorderingAllowedListener) {
        this.temporaryListeners.remove(onReorderingAllowedListener);
        this.allListeners.addIfAbsent(onReorderingAllowedListener);
    }

    public final void addTemporaryReorderingAllowedListener(@NotNull OnReorderingAllowedListener onReorderingAllowedListener) {
        if (this.allListeners.addIfAbsent(onReorderingAllowedListener)) {
            this.temporaryListeners.add(onReorderingAllowedListener);
        }
    }
}
