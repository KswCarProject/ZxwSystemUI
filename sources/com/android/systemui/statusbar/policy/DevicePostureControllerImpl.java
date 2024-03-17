package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.util.SparseIntArray;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class DevicePostureControllerImpl implements DevicePostureController {
    public int mCurrentDevicePosture = 0;
    public final SparseIntArray mDeviceStateToPostureMap = new SparseIntArray();
    public final List<DevicePostureController.Callback> mListeners = new ArrayList();

    public DevicePostureControllerImpl(Context context, DeviceStateManager deviceStateManager, Executor executor) {
        for (String split : context.getResources().getStringArray(17236029)) {
            String[] split2 = split.split(":");
            if (split2.length == 2) {
                try {
                    this.mDeviceStateToPostureMap.put(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                } catch (NumberFormatException unused) {
                }
            }
        }
        deviceStateManager.registerCallback(executor, new DevicePostureControllerImpl$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        Assert.isMainThread();
        this.mCurrentDevicePosture = this.mDeviceStateToPostureMap.get(i, 0);
        this.mListeners.forEach(new DevicePostureControllerImpl$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DevicePostureController.Callback callback) {
        callback.onPostureChanged(this.mCurrentDevicePosture);
    }

    public void addCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.add(callback);
    }

    public void removeCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.remove(callback);
    }

    public int getDevicePosture() {
        return this.mCurrentDevicePosture;
    }
}
