package com.android.systemui.dreams.touch;

import android.graphics.Region;
import android.view.GestureDetector;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.google.common.util.concurrent.ListenableFuture;

public interface DreamTouchHandler {

    public interface TouchSession {

        public interface Callback {
            void onRemoved();
        }

        int getActiveSessionCount();

        ListenableFuture<TouchSession> pop();

        void registerCallback(Callback callback);

        boolean registerGestureListener(GestureDetector.OnGestureListener onGestureListener);

        boolean registerInputListener(InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener);
    }

    void getTouchInitiationRegion(Region region) {
    }

    void onSessionStart(TouchSession touchSession);
}
