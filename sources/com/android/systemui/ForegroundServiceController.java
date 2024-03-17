package com.android.systemui;

import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.SparseArray;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Assert;

public class ForegroundServiceController {
    public static final int[] APP_OPS = {24};
    public final Handler mMainHandler;
    public final Object mMutex = new Object();
    public final SparseArray<ForegroundServicesUserState> mUserServices = new SparseArray<>();

    public interface UserStateUpdateCallback {
        boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState);
    }

    public ForegroundServiceController(AppOpsController appOpsController, Handler handler) {
        this.mMainHandler = handler;
        appOpsController.addCallback(APP_OPS, new ForegroundServiceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, int i2, String str, boolean z) {
        this.mMainHandler.post(new ForegroundServiceController$$ExternalSyntheticLambda1(this, i, i2, str, z));
    }

    public boolean isDisclosureNeededForUser(int i) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                return false;
            }
            boolean isDisclosureNeeded = foregroundServicesUserState.isDisclosureNeeded();
            return isDisclosureNeeded;
        }
    }

    /* renamed from: onAppOpChanged */
    public void lambda$new$0(int i, int i2, String str, boolean z) {
        Assert.isMainThread();
        int userId = UserHandle.getUserId(i2);
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(userId);
            if (foregroundServicesUserState == null) {
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(userId, foregroundServicesUserState);
            }
            if (z) {
                foregroundServicesUserState.addOp(str, i);
            } else {
                foregroundServicesUserState.removeOp(str, i);
            }
        }
    }

    public boolean updateUserState(int i, UserStateUpdateCallback userStateUpdateCallback, boolean z) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                if (!z) {
                    return false;
                }
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(i, foregroundServicesUserState);
            }
            boolean updateUserState = userStateUpdateCallback.updateUserState(foregroundServicesUserState);
            return updateUserState;
        }
    }

    public boolean isDisclosureNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getId() == 40 && statusBarNotification.getTag() == null && statusBarNotification.getPackageName().equals(ThemeOverlayApplier.ANDROID_PACKAGE);
    }
}
