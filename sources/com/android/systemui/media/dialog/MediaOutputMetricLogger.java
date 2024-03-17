package com.android.systemui.media.dialog;

import android.content.Context;
import android.util.Log;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.shared.system.SysUiStatsLog;
import java.util.List;

public class MediaOutputMetricLogger {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputMetricLogger", 3);
    public int mAppliedDeviceCountWithinRemoteGroup;
    public int mConnectedBluetoothDeviceCount;
    public final Context mContext;
    public final String mPackageName;
    public int mRemoteDeviceCount;
    public MediaDevice mSourceDevice;
    public MediaDevice mTargetDevice;
    public int mWiredDeviceCount;

    public final int getLoggingSwitchOpSubResult(int i) {
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 3;
        }
        if (i != 3) {
            return i != 4 ? 0 : 5;
        }
        return 4;
    }

    public MediaOutputMetricLogger(Context context, String str) {
        this.mContext = context;
        this.mPackageName = str;
    }

    public void updateOutputEndPoints(MediaDevice mediaDevice, MediaDevice mediaDevice2) {
        this.mSourceDevice = mediaDevice;
        this.mTargetDevice = mediaDevice2;
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "updateOutputEndPoints - source:" + this.mSourceDevice.toString() + " target:" + this.mTargetDevice.toString());
        }
    }

    public void logOutputSuccess(String str, List<MediaDevice> list) {
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "logOutputSuccess - selected device: " + str);
        }
        updateLoggingDeviceCount(list);
        SysUiStatsLog.write(277, getLoggingDeviceType(this.mSourceDevice, true), getLoggingDeviceType(this.mTargetDevice, false), 1, 1, getLoggingPackageName(), this.mWiredDeviceCount, this.mConnectedBluetoothDeviceCount, this.mRemoteDeviceCount, this.mAppliedDeviceCountWithinRemoteGroup);
    }

    public void logInteractionAdjustVolume(MediaDevice mediaDevice) {
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "logInteraction - AdjustVolume");
        }
        SysUiStatsLog.write(466, 1, getInteractionDeviceType(mediaDevice));
    }

    public void logInteractionStopCasting() {
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "logInteraction - Stop casting");
        }
        SysUiStatsLog.write(466, 2, 0);
    }

    public void logInteractionExpansion(MediaDevice mediaDevice) {
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "logInteraction - Expansion");
        }
        SysUiStatsLog.write(466, 0, getInteractionDeviceType(mediaDevice));
    }

    public void logOutputFailure(List<MediaDevice> list, int i) {
        if (DEBUG) {
            Log.e("MediaOutputMetricLogger", "logRequestFailed - " + i);
        }
        updateLoggingDeviceCount(list);
        SysUiStatsLog.write(277, getLoggingDeviceType(this.mSourceDevice, true), getLoggingDeviceType(this.mTargetDevice, false), 0, getLoggingSwitchOpSubResult(i), getLoggingPackageName(), this.mWiredDeviceCount, this.mConnectedBluetoothDeviceCount, this.mRemoteDeviceCount, this.mAppliedDeviceCountWithinRemoteGroup);
    }

    public final void updateLoggingDeviceCount(List<MediaDevice> list) {
        this.mRemoteDeviceCount = 0;
        this.mConnectedBluetoothDeviceCount = 0;
        this.mWiredDeviceCount = 0;
        this.mAppliedDeviceCountWithinRemoteGroup = 0;
        for (MediaDevice next : list) {
            if (next.isConnected()) {
                int deviceType = next.getDeviceType();
                if (deviceType == 2 || deviceType == 3) {
                    this.mWiredDeviceCount++;
                } else if (deviceType == 5) {
                    this.mConnectedBluetoothDeviceCount++;
                } else if (deviceType == 6 || deviceType == 7) {
                    this.mRemoteDeviceCount++;
                }
            }
        }
        if (DEBUG) {
            Log.d("MediaOutputMetricLogger", "connected devices: wired: " + this.mWiredDeviceCount + " bluetooth: " + this.mConnectedBluetoothDeviceCount + " remote: " + this.mRemoteDeviceCount);
        }
    }

    public final int getLoggingDeviceType(MediaDevice mediaDevice, boolean z) {
        int deviceType = mediaDevice.getDeviceType();
        if (deviceType == 1) {
            return 1;
        }
        if (deviceType == 2) {
            return 200;
        }
        if (deviceType == 3) {
            return 100;
        }
        if (deviceType == 5) {
            return 300;
        }
        if (deviceType != 6) {
            return deviceType != 7 ? 0 : 500;
        }
        return 400;
    }

    public final int getInteractionDeviceType(MediaDevice mediaDevice) {
        int deviceType = mediaDevice.getDeviceType();
        if (deviceType == 1) {
            return 1;
        }
        if (deviceType == 2) {
            return 200;
        }
        if (deviceType == 3) {
            return 100;
        }
        if (deviceType == 5) {
            return 300;
        }
        if (deviceType != 6) {
            return deviceType != 7 ? 0 : 500;
        }
        return 400;
    }

    public final String getLoggingPackageName() {
        String str = this.mPackageName;
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            int i = this.mContext.getPackageManager().getApplicationInfo(this.mPackageName, 0).flags;
            if ((i & 1) == 0 && (i & 128) == 0) {
                return "";
            }
            return this.mPackageName;
        } catch (Exception unused) {
            Log.e("MediaOutputMetricLogger", this.mPackageName + " is invalid.");
            return "";
        }
    }
}
