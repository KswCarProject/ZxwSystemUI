package com.android.systemui.usb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;

public class UsbConfirmActivity extends UsbDialogActivity {
    public UsbAudioWarningDialogMessage mUsbConfirmMessageHandler;

    public /* bridge */ /* synthetic */ void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        super.onCheckedChanged(compoundButton, z);
    }

    public /* bridge */ /* synthetic */ void onClick(DialogInterface dialogInterface, int i) {
        super.onClick(dialogInterface, i);
    }

    public UsbConfirmActivity(UsbAudioWarningDialogMessage usbAudioWarningDialogMessage) {
        this.mUsbConfirmMessageHandler = usbAudioWarningDialogMessage;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUsbConfirmMessageHandler.init(1, this.mDialogHelper);
    }

    public void onResume() {
        super.onResume();
        boolean z = this.mDialogHelper.isUsbDevice() && this.mDialogHelper.deviceHasAudioCapture() && !this.mDialogHelper.packageHasAudioRecordingPermission();
        String string = getString(this.mUsbConfirmMessageHandler.getPromptTitleId(), new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()});
        int messageId = this.mUsbConfirmMessageHandler.getMessageId();
        setAlertParams(string, messageId != 0 ? getString(messageId, new Object[]{this.mDialogHelper.getAppName(), this.mDialogHelper.getDeviceDescription()}) : null);
        if (!z) {
            addAlwaysUseCheckbox();
        }
        setupAlert();
    }

    public void onConfirm() {
        this.mDialogHelper.grantUidAccessPermission();
        if (isAlwaysUseChecked()) {
            this.mDialogHelper.setDefaultPackage();
        } else {
            this.mDialogHelper.clearDefaultPackage();
        }
        this.mDialogHelper.confirmDialogStartActivity();
        finish();
    }
}
