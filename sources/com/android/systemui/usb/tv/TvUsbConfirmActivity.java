package com.android.systemui.usb.tv;

import android.view.View;
import com.android.systemui.R$string;

public class TvUsbConfirmActivity extends TvUsbDialogActivity {
    public static final String TAG = TvUsbConfirmActivity.class.getSimpleName();

    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    public void onResume() {
        int i;
        super.onResume();
        if (this.mDialogHelper.isUsbDevice()) {
            if (this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission()) {
                i = R$string.usb_device_confirm_prompt_warn;
            } else {
                i = R$string.usb_device_confirm_prompt;
            }
        } else {
            i = R$string.usb_accessory_confirm_prompt;
        }
        initUI(this.mDialogHelper.getAppName(), getString(i, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}));
    }

    public void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        this.mDialogHelper.confirmDialogStartActivity();
        finish();
    }
}
