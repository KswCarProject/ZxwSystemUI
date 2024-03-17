package com.android.settingslib.users;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;

public class PhotoCapabilityUtils {
    public static boolean canTakePhoto(Context context) {
        return context.getPackageManager().queryIntentActivities(new Intent("android.media.action.IMAGE_CAPTURE"), 65536).size() > 0;
    }

    public static boolean canChoosePhoto(Context context) {
        Intent intent = new Intent("android.provider.action.PICK_IMAGES");
        intent.setType("image/*");
        if (!(context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) || isDeviceLocked(context)) {
            return false;
        }
        return true;
    }

    public static boolean isDeviceLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        return keyguardManager == null || keyguardManager.isDeviceLocked();
    }
}
