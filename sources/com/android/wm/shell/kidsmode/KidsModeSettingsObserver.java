package com.android.wm.shell.kidsmode;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import java.util.Collection;

public class KidsModeSettingsObserver extends ContentObserver {
    public Context mContext;
    public Runnable mOnChangeRunnable;

    public KidsModeSettingsObserver(Handler handler, Context context) {
        super(handler);
        this.mContext = context;
    }

    public void setOnChangeRunnable(Runnable runnable) {
        this.mOnChangeRunnable = runnable;
    }

    public void register() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("navigation_mode"), false, this, -1);
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("nav_bar_kids_mode"), false, this, -1);
    }

    public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
        Runnable runnable;
        if (i2 == ActivityManager.getCurrentUser() && (runnable = this.mOnChangeRunnable) != null) {
            runnable.run();
        }
    }

    public boolean isEnabled() {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "navigation_mode", 0, -2) == 0 && Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "nav_bar_kids_mode", 0, -2) == 1) {
            return true;
        }
        return false;
    }
}
