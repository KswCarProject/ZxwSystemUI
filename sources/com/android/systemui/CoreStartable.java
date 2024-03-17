package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

public abstract class CoreStartable implements Dumpable {
    public final Context mContext;

    public void dump(PrintWriter printWriter, String[] strArr) {
    }

    @VisibleForTesting
    public void onBootCompleted() {
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public abstract void start();

    public CoreStartable(Context context) {
        this.mContext = context;
    }
}
