package com.android.systemui.unfold.util;

import android.content.Context;
import android.os.RemoteException;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import org.jetbrains.annotations.NotNull;

/* compiled from: NaturalRotationUnfoldProgressProvider.kt */
public final class NaturalRotationUnfoldProgressProvider implements UnfoldTransitionProgressProvider {
    @NotNull
    public final Context context;
    public boolean isNaturalRotation;
    @NotNull
    public final RotationWatcher rotationWatcher = new RotationWatcher();
    @NotNull
    public final ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider;
    @NotNull
    public final IWindowManager windowManagerInterface;

    public NaturalRotationUnfoldProgressProvider(@NotNull Context context2, @NotNull IWindowManager iWindowManager, @NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.context = context2;
        this.windowManagerInterface = iWindowManager;
        this.scopedUnfoldTransitionProgressProvider = new ScopedUnfoldTransitionProgressProvider(unfoldTransitionProgressProvider);
    }

    public final void init() {
        try {
            this.windowManagerInterface.watchRotation(this.rotationWatcher, this.context.getDisplay().getDisplayId());
            onRotationChanged(this.context.getDisplay().getRotation());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public final void onRotationChanged(int i) {
        boolean z = i == 0 || i == 2;
        if (this.isNaturalRotation != z) {
            this.isNaturalRotation = z;
            this.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(z);
        }
    }

    public void addCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.scopedUnfoldTransitionProgressProvider.addCallback(transitionProgressListener);
    }

    public void removeCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.scopedUnfoldTransitionProgressProvider.removeCallback(transitionProgressListener);
    }

    /* compiled from: NaturalRotationUnfoldProgressProvider.kt */
    public final class RotationWatcher extends IRotationWatcher.Stub {
        public RotationWatcher() {
        }

        public void onRotationChanged(int i) {
            NaturalRotationUnfoldProgressProvider.this.onRotationChanged(i);
        }
    }
}
