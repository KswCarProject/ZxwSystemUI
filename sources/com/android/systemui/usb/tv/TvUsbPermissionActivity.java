package com.android.systemui.usb.tv;

import android.view.View;
import com.android.systemui.R$string;

public class TvUsbPermissionActivity extends TvUsbDialogActivity {
    public static final String TAG = TvUsbPermissionActivity.class.getSimpleName();
    public boolean mPermissionGranted = false;

    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    public void onResume() {
        int i;
        super.onResume();
        if (this.mDialogHelper.isUsbDevice()) {
            if (this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission()) {
                i = R$string.usb_device_permission_prompt_warn;
            } else {
                i = R$string.usb_device_permission_prompt;
            }
        } else {
            i = R$string.usb_accessory_permission_prompt;
        }
        initUI(this.mDialogHelper.getAppName(), getString(i, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}));
    }

    public void onPause() {
        if (isFinishing()) {
            this.mDialogHelper.sendPermissionDialogResponse(this.mPermissionGranted);
        }
        super.onPause();
    }

    public void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        this.mPermissionGranted = true;
        finish();
    }
}
