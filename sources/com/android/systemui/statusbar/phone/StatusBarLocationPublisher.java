package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarLocationPublisher.kt */
public final class StatusBarLocationPublisher implements CallbackController<StatusBarMarginUpdatedListener> {
    @NotNull
    public final Set<WeakReference<StatusBarMarginUpdatedListener>> listeners = new LinkedHashSet();
    public int marginLeft;
    public int marginRight;

    public final int getMarginLeft() {
        return this.marginLeft;
    }

    public final int getMarginRight() {
        return this.marginRight;
    }

    public void addCallback(@NotNull StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        this.listeners.add(new WeakReference(statusBarMarginUpdatedListener));
    }

    public void removeCallback(@NotNull StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        WeakReference weakReference = null;
        for (WeakReference next : this.listeners) {
            if (Intrinsics.areEqual(next.get(), (Object) statusBarMarginUpdatedListener)) {
                weakReference = next;
            }
        }
        if (weakReference != null) {
            this.listeners.remove(weakReference);
        }
    }

    public final void updateStatusBarMargin(int i, int i2) {
        this.marginLeft = i;
        this.marginRight = i2;
        notifyListeners();
    }

    public final void notifyListeners() {
        List<WeakReference> list;
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.listeners);
            Unit unit = Unit.INSTANCE;
        }
        for (WeakReference weakReference : list) {
            if (weakReference.get() == null) {
                this.listeners.remove(weakReference);
            }
            StatusBarMarginUpdatedListener statusBarMarginUpdatedListener = (StatusBarMarginUpdatedListener) weakReference.get();
            if (statusBarMarginUpdatedListener != null) {
                statusBarMarginUpdatedListener.onStatusBarMarginUpdated(getMarginLeft(), getMarginRight());
            }
        }
    }
}
