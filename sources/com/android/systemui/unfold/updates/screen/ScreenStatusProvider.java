package com.android.systemui.unfold.updates.screen;

import com.android.systemui.statusbar.policy.CallbackController;

/* compiled from: ScreenStatusProvider.kt */
public interface ScreenStatusProvider extends CallbackController<ScreenListener> {

    /* compiled from: ScreenStatusProvider.kt */
    public interface ScreenListener {
        void onScreenTurnedOn();
    }
}
