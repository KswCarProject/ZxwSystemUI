package com.android.systemui.usb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.PermissionChecker;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;

public class UsbDialogHelper {
    public static final String TAG = "UsbDialogHelper";
    public final UsbAccessory mAccessory;
    public final CharSequence mAppName;
    public final boolean mCanBeDefault;
    public final Context mContext;
    public final UsbDevice mDevice;
    public UsbDisconnectedReceiver mDisconnectedReceiver;
    public boolean mIsUsbDevice;
    public final String mPackageName;
    public final PendingIntent mPendingIntent;
    public final ResolveInfo mResolveInfo;
    public boolean mResponseSent;
    public final int mUid;
    public final IUsbManager mUsbService;

    public UsbDialogHelper(Context context, Intent intent) throws IllegalStateException {
        this.mContext = context;
        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
        this.mDevice = usbDevice;
        UsbAccessory usbAccessory = (UsbAccessory) intent.getParcelableExtra("accessory");
        this.mAccessory = usbAccessory;
        this.mCanBeDefault = intent.getBooleanExtra("android.hardware.usb.extra.CAN_BE_DEFAULT", false);
        if (usbDevice == null && usbAccessory == null) {
            throw new IllegalStateException("Device and accessory are both null.");
        }
        if (usbDevice != null) {
            this.mIsUsbDevice = true;
        }
        ResolveInfo resolveInfo = (ResolveInfo) intent.getParcelableExtra("rinfo");
        this.mResolveInfo = resolveInfo;
        PackageManager packageManager = context.getPackageManager();
        if (resolveInfo != null) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            this.mUid = activityInfo.applicationInfo.uid;
            this.mPackageName = activityInfo.packageName;
            this.mPendingIntent = null;
        } else {
            this.mUid = intent.getIntExtra("android.intent.extra.UID", -1);
            this.mPackageName = intent.getStringExtra("android.hardware.usb.extra.PACKAGE");
            this.mPendingIntent = (PendingIntent) intent.getParcelableExtra("android.intent.extra.INTENT");
        }
        try {
            this.mAppName = packageManager.getApplicationInfo(this.mPackageName, 0).loadLabel(packageManager);
            this.mUsbService = IUsbManager.Stub.asInterface(ServiceManager.getService("usb"));
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("unable to look up package name", e);
        }
    }

    public void registerUsbDisconnectedReceiver(Activity activity) {
        if (this.mIsUsbDevice) {
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(activity, this.mDevice);
        } else {
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(activity, this.mAccessory);
        }
    }

    public void unregisterUsbDisconnectedReceiver(Activity activity) {
        UsbDisconnectedReceiver usbDisconnectedReceiver = this.mDisconnectedReceiver;
        if (usbDisconnectedReceiver != null) {
            try {
                activity.unregisterReceiver(usbDisconnectedReceiver);
            } catch (Exception unused) {
            }
            this.mDisconnectedReceiver = null;
        }
    }

    public boolean deviceHasAudioCapture() {
        UsbDevice usbDevice = this.mDevice;
        return usbDevice != null && usbDevice.getHasAudioCapture();
    }

    public boolean deviceHasAudioPlayback() {
        UsbDevice usbDevice = this.mDevice;
        return usbDevice != null && usbDevice.getHasAudioPlayback();
    }

    public boolean packageHasAudioRecordingPermission() {
        return PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.RECORD_AUDIO", -1, this.mUid, this.mPackageName) == 0;
    }

    public boolean isUsbDevice() {
        return this.mIsUsbDevice;
    }

    public boolean isUsbAccessory() {
        return !this.mIsUsbDevice;
    }

    public void grantUidAccessPermission() {
        try {
            if (this.mIsUsbDevice) {
                this.mUsbService.grantDevicePermission(this.mDevice, this.mUid);
            } else {
                this.mUsbService.grantAccessoryPermission(this.mAccessory, this.mUid);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IUsbService connection failed", e);
        }
    }

    public void setDefaultPackage() {
        int myUserId = UserHandle.myUserId();
        try {
            if (this.mIsUsbDevice) {
                this.mUsbService.setDevicePackage(this.mDevice, this.mPackageName, myUserId);
            } else {
                this.mUsbService.setAccessoryPackage(this.mAccessory, this.mPackageName, myUserId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IUsbService connection failed", e);
        }
    }

    public void clearDefaultPackage() {
        int myUserId = UserHandle.myUserId();
        try {
            if (this.mIsUsbDevice) {
                this.mUsbService.setDevicePackage(this.mDevice, (String) null, myUserId);
            } else {
                this.mUsbService.setAccessoryPackage(this.mAccessory, (String) null, myUserId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IUsbService connection failed", e);
        }
    }

    public void confirmDialogStartActivity() {
        Intent intent;
        int myUserId = UserHandle.myUserId();
        if (this.mIsUsbDevice) {
            intent = new Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            intent.putExtra("device", this.mDevice);
        } else {
            intent = new Intent("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
            intent.putExtra("accessory", this.mAccessory);
        }
        intent.addFlags(268435456);
        ActivityInfo activityInfo = this.mResolveInfo.activityInfo;
        intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        try {
            this.mContext.startActivityAsUser(intent, new UserHandle(myUserId));
        } catch (Exception e) {
            Log.e(TAG, "Unable to start activity", e);
        }
    }

    public void sendPermissionDialogResponse(boolean z) {
        if (!this.mResponseSent) {
            Intent intent = new Intent();
            if (this.mIsUsbDevice) {
                intent.putExtra("device", this.mDevice);
            } else {
                intent.putExtra("accessory", this.mAccessory);
            }
            intent.putExtra("permission", z);
            try {
                this.mPendingIntent.send(this.mContext, 0, intent);
                this.mResponseSent = true;
            } catch (PendingIntent.CanceledException unused) {
                Log.w(TAG, "PendingIntent was cancelled");
            }
        }
    }

    public String getDeviceDescription() {
        if (this.mIsUsbDevice) {
            String productName = this.mDevice.getProductName();
            if (productName == null) {
                return this.mDevice.getDeviceName();
            }
            return productName;
        }
        String description = this.mAccessory.getDescription();
        if (description != null) {
            return description;
        }
        return String.format("%s %s", new Object[]{this.mAccessory.getManufacturer(), this.mAccessory.getModel()});
    }

    public boolean canBeDefault() {
        return this.mCanBeDefault;
    }

    public CharSequence getAppName() {
        return this.mAppName;
    }
}
