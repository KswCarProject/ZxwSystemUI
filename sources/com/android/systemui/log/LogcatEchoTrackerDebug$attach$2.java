package com.android.systemui.log;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import org.jetbrains.annotations.Nullable;

/* compiled from: LogcatEchoTrackerDebug.kt */
public final class LogcatEchoTrackerDebug$attach$2 extends ContentObserver {
    public final /* synthetic */ LogcatEchoTrackerDebug this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LogcatEchoTrackerDebug$attach$2(LogcatEchoTrackerDebug logcatEchoTrackerDebug, Handler handler) {
        super(handler);
        this.this$0 = logcatEchoTrackerDebug;
    }

    public void onChange(boolean z, @Nullable Uri uri) {
        super.onChange(z, uri);
        this.this$0.cachedTagLevels.clear();
    }
}
