package com.android.systemui.dreams.touch;

import android.os.Handler;
import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.touch.TouchInsetManager;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class HideComplicationTouchHandler implements DreamTouchHandler {
    public static final boolean DEBUG = Log.isLoggable("HideComplicationHandler", 3);
    public final Executor mExecutor;
    public final Handler mHandler;
    public final Runnable mRestoreComplications = new Runnable() {
        public void run() {
            HideComplicationTouchHandler.this.mVisibilityController.setVisibility(0, true);
        }
    };
    public final int mRestoreTimeout;
    public final TouchInsetManager mTouchInsetManager;
    public final Complication.VisibilityController mVisibilityController;

    public HideComplicationTouchHandler(Complication.VisibilityController visibilityController, int i, TouchInsetManager touchInsetManager, Executor executor, Handler handler) {
        this.mVisibilityController = visibilityController;
        this.mRestoreTimeout = i;
        this.mHandler = handler;
        this.mTouchInsetManager = touchInsetManager;
        this.mExecutor = executor;
    }

    public void onSessionStart(DreamTouchHandler.TouchSession touchSession) {
        boolean z = DEBUG;
        if (z) {
            Log.d("HideComplicationHandler", "onSessionStart");
        }
        if (touchSession.getActiveSessionCount() > 1) {
            if (z) {
                Log.d("HideComplicationHandler", "multiple active touch sessions, not fading");
            }
            touchSession.pop();
            return;
        }
        touchSession.registerInputListener(new HideComplicationTouchHandler$$ExternalSyntheticLambda0(this, touchSession));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSessionStart$1(DreamTouchHandler.TouchSession touchSession, InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            if (motionEvent.getAction() == 0) {
                if (DEBUG) {
                    Log.d("HideComplicationHandler", "ACTION_DOWN received");
                }
                ListenableFuture<Boolean> checkWithinTouchRegion = this.mTouchInsetManager.checkWithinTouchRegion(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()));
                checkWithinTouchRegion.addListener(new HideComplicationTouchHandler$$ExternalSyntheticLambda1(this, checkWithinTouchRegion, touchSession), this.mExecutor);
            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                touchSession.pop();
                this.mHandler.postDelayed(this.mRestoreComplications, (long) this.mRestoreTimeout);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSessionStart$0(ListenableFuture listenableFuture, DreamTouchHandler.TouchSession touchSession) {
        try {
            if (!((Boolean) listenableFuture.get()).booleanValue()) {
                this.mHandler.removeCallbacks(this.mRestoreComplications);
                this.mVisibilityController.setVisibility(4, true);
                return;
            }
            touchSession.pop();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("HideComplicationHandler", "could not check TouchInsetManager:" + e);
        }
    }
}
