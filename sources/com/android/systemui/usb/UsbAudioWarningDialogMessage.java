package com.android.systemui.usb;

import android.util.Log;
import com.android.systemui.R$string;

public class UsbAudioWarningDialogMessage {
    public UsbDialogHelper mDialogHelper;
    public int mDialogType;

    public void init(int i, UsbDialogHelper usbDialogHelper) {
        this.mDialogType = i;
        this.mDialogHelper = usbDialogHelper;
    }

    public boolean hasRecordPermission() {
        return this.mDialogHelper.packageHasAudioRecordingPermission();
    }

    public boolean isUsbAudioDevice() {
        return this.mDialogHelper.isUsbDevice() && (this.mDialogHelper.deviceHasAudioCapture() || this.mDialogHelper.deviceHasAudioPlayback());
    }

    public boolean hasAudioPlayback() {
        return this.mDialogHelper.deviceHasAudioPlayback();
    }

    public boolean hasAudioCapture() {
        return this.mDialogHelper.deviceHasAudioCapture();
    }

    public int getMessageId() {
        if (!this.mDialogHelper.isUsbDevice()) {
            return getUsbAccessoryPromptId();
        }
        if (hasRecordPermission() && isUsbAudioDevice()) {
            return R$string.usb_audio_device_prompt;
        }
        if (!hasRecordPermission() && isUsbAudioDevice() && hasAudioPlayback() && !hasAudioCapture()) {
            return R$string.usb_audio_device_prompt;
        }
        if (!hasRecordPermission() && isUsbAudioDevice() && hasAudioCapture()) {
            return R$string.usb_audio_device_prompt_warn;
        }
        Log.w("UsbAudioWarningDialogMessage", "Only shows title with empty content description!");
        return 0;
    }

    public int getPromptTitleId() {
        if (this.mDialogType == 0) {
            return R$string.usb_audio_device_permission_prompt_title;
        }
        return R$string.usb_audio_device_confirm_prompt_title;
    }

    public int getUsbAccessoryPromptId() {
        return this.mDialogType == 0 ? R$string.usb_accessory_permission_prompt : R$string.usb_accessory_confirm_prompt;
    }
}
