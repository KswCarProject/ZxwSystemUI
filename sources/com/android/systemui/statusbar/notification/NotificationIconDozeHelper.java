package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import com.android.systemui.R$integer;

public class NotificationIconDozeHelper extends NotificationDozeHelper {
    public int mColor = -16777216;
    public PorterDuffColorFilter mImageColorFilter = null;
    public final int mImageDarkAlpha;
    public final int mImageDarkColor = -1;

    public NotificationIconDozeHelper(Context context) {
        this.mImageDarkAlpha = context.getResources().getInteger(R$integer.doze_small_icon_alpha);
    }

    public void setColor(int i) {
        this.mColor = i;
    }
}
