package com.android.systemui.unfold.updates;

import com.android.systemui.statusbar.policy.CallbackController;

/* compiled from: FoldStateProvider.kt */
public interface FoldStateProvider extends CallbackController<FoldUpdatesListener> {

    /* compiled from: FoldStateProvider.kt */
    public interface FoldUpdatesListener {
        void onFoldUpdate(int i);

        void onHingeAngleUpdate(float f);
    }

    boolean isFinishedOpening();

    void start();
}
