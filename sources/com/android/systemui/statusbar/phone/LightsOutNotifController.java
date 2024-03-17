package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.util.ViewController;

public class LightsOutNotifController extends ViewController<View> {
    @VisibleForTesting
    public int mAppearance;
    public final CommandQueue.Callbacks mCallback = new CommandQueue.Callbacks() {
        public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
            if (i == LightsOutNotifController.this.mDisplayId) {
                LightsOutNotifController lightsOutNotifController = LightsOutNotifController.this;
                lightsOutNotifController.mAppearance = i2;
                lightsOutNotifController.updateLightsOutView();
            }
        }
    };
    public final CommandQueue mCommandQueue;
    public int mDisplayId;
    public final NotifLiveDataStore mNotifDataStore;
    public final Observer<Boolean> mObserver = new LightsOutNotifController$$ExternalSyntheticLambda0(this);
    public final WindowManager mWindowManager;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        updateLightsOutView();
    }

    public LightsOutNotifController(View view, WindowManager windowManager, NotifLiveDataStore notifLiveDataStore, CommandQueue commandQueue) {
        super(view);
        this.mWindowManager = windowManager;
        this.mNotifDataStore = notifLiveDataStore;
        this.mCommandQueue = commandQueue;
    }

    public void onViewDetached() {
        this.mNotifDataStore.getHasActiveNotifs().removeObserver(this.mObserver);
        this.mCommandQueue.removeCallback(this.mCallback);
    }

    public void onViewAttached() {
        this.mView.setVisibility(8);
        this.mView.setAlpha(0.0f);
        this.mDisplayId = this.mWindowManager.getDefaultDisplay().getDisplayId();
        this.mNotifDataStore.getHasActiveNotifs().addSyncObserver(this.mObserver);
        this.mCommandQueue.addCallback(this.mCallback);
        updateLightsOutView();
    }

    public final boolean hasActiveNotifications() {
        return this.mNotifDataStore.getHasActiveNotifs().getValue().booleanValue();
    }

    @VisibleForTesting
    public void updateLightsOutView() {
        final boolean shouldShowDot = shouldShowDot();
        if (shouldShowDot != isShowingDot()) {
            float f = 0.0f;
            if (shouldShowDot) {
                this.mView.setAlpha(0.0f);
                this.mView.setVisibility(0);
            }
            ViewPropertyAnimator animate = this.mView.animate();
            if (shouldShowDot) {
                f = 1.0f;
            }
            animate.alpha(f).setDuration(shouldShowDot ? 750 : 250).setInterpolator(new AccelerateInterpolator(2.0f)).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    LightsOutNotifController.this.mView.setAlpha(shouldShowDot ? 1.0f : 0.0f);
                    LightsOutNotifController.this.mView.setVisibility(shouldShowDot ? 0 : 8);
                    LightsOutNotifController.this.mView.animate().setListener((Animator.AnimatorListener) null);
                }
            }).start();
        }
    }

    @VisibleForTesting
    public boolean isShowingDot() {
        return this.mView.getVisibility() == 0 && this.mView.getAlpha() == 1.0f;
    }

    @VisibleForTesting
    public boolean shouldShowDot() {
        return hasActiveNotifications() && areLightsOut();
    }

    @VisibleForTesting
    public boolean areLightsOut() {
        return (this.mAppearance & 4) != 0;
    }
}
