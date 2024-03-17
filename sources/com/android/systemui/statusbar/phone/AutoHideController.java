package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.MotionEvent;
import com.android.systemui.statusbar.AutoHideUiElement;

public class AutoHideController {
    public final Runnable mAutoHide = new AutoHideController$$ExternalSyntheticLambda0(this);
    public boolean mAutoHideSuspended;
    public int mDisplayId;
    public final Handler mHandler;
    public AutoHideUiElement mNavigationBar;
    public AutoHideUiElement mStatusBar;
    public final IWindowManager mWindowManagerService;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (isAnyTransientBarShown()) {
            hideTransientBars();
        }
    }

    public AutoHideController(Context context, Handler handler, IWindowManager iWindowManager) {
        this.mHandler = handler;
        this.mWindowManagerService = iWindowManager;
        this.mDisplayId = context.getDisplayId();
    }

    public void setStatusBar(AutoHideUiElement autoHideUiElement) {
        this.mStatusBar = autoHideUiElement;
    }

    public void setNavigationBar(AutoHideUiElement autoHideUiElement) {
        this.mNavigationBar = autoHideUiElement;
    }

    public final void hideTransientBars() {
        try {
            this.mWindowManagerService.hideTransientBars(this.mDisplayId);
        } catch (RemoteException unused) {
            Log.w("AutoHideController", "Cannot get WindowManager");
        }
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null) {
            autoHideUiElement.hide();
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 != null) {
            autoHideUiElement2.hide();
        }
    }

    public void resumeSuspendedAutoHide() {
        if (this.mAutoHideSuspended) {
            scheduleAutoHide();
            Runnable checkBarModesRunnable = getCheckBarModesRunnable();
            if (checkBarModesRunnable != null) {
                this.mHandler.postDelayed(checkBarModesRunnable, 500);
            }
        }
    }

    public void suspendAutoHide() {
        this.mHandler.removeCallbacks(this.mAutoHide);
        Runnable checkBarModesRunnable = getCheckBarModesRunnable();
        if (checkBarModesRunnable != null) {
            this.mHandler.removeCallbacks(checkBarModesRunnable);
        }
        this.mAutoHideSuspended = isAnyTransientBarShown();
    }

    public void touchAutoHide() {
        if (isAnyTransientBarShown()) {
            scheduleAutoHide();
        } else {
            cancelAutoHide();
        }
    }

    public final Runnable getCheckBarModesRunnable() {
        if (this.mStatusBar != null) {
            return new AutoHideController$$ExternalSyntheticLambda1(this);
        }
        if (this.mNavigationBar != null) {
            return new AutoHideController$$ExternalSyntheticLambda2(this);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getCheckBarModesRunnable$1() {
        this.mStatusBar.synchronizeState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getCheckBarModesRunnable$2() {
        this.mNavigationBar.synchronizeState();
    }

    public final void cancelAutoHide() {
        this.mAutoHideSuspended = false;
        this.mHandler.removeCallbacks(this.mAutoHide);
    }

    public final void scheduleAutoHide() {
        cancelAutoHide();
        this.mHandler.postDelayed(this.mAutoHide, 2250);
    }

    public void checkUserAutoHide(MotionEvent motionEvent) {
        boolean z = isAnyTransientBarShown() && motionEvent.getAction() == 4 && motionEvent.getX() == 0.0f && motionEvent.getY() == 0.0f;
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null) {
            z &= autoHideUiElement.shouldHideOnTouch();
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 != null) {
            z &= autoHideUiElement2.shouldHideOnTouch();
        }
        if (z) {
            userAutoHide();
        }
    }

    public final void userAutoHide() {
        cancelAutoHide();
        this.mHandler.postDelayed(this.mAutoHide, 350);
    }

    public final boolean isAnyTransientBarShown() {
        AutoHideUiElement autoHideUiElement = this.mStatusBar;
        if (autoHideUiElement != null && autoHideUiElement.isVisible()) {
            return true;
        }
        AutoHideUiElement autoHideUiElement2 = this.mNavigationBar;
        if (autoHideUiElement2 == null || !autoHideUiElement2.isVisible()) {
            return false;
        }
        return true;
    }

    public static class Factory {
        public final Handler mHandler;
        public final IWindowManager mIWindowManager;

        public Factory(Handler handler, IWindowManager iWindowManager) {
            this.mHandler = handler;
            this.mIWindowManager = iWindowManager;
        }

        public AutoHideController create(Context context) {
            return new AutoHideController(context, this.mHandler, this.mIWindowManager);
        }
    }
}
