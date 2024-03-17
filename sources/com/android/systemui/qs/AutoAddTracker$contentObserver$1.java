package com.android.systemui.qs;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/* compiled from: AutoAddTracker.kt */
public final class AutoAddTracker$contentObserver$1 extends ContentObserver {
    public final /* synthetic */ AutoAddTracker this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AutoAddTracker$contentObserver$1(AutoAddTracker autoAddTracker, Handler handler) {
        super(handler);
        this.this$0 = autoAddTracker;
    }

    public void onChange(boolean z, @NotNull Collection<? extends Uri> collection, int i, int i2) {
        if (i2 == this.this$0.userId) {
            this.this$0.loadTiles();
        }
    }
}
