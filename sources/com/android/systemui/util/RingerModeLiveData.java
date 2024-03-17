package com.android.systemui.util;

import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/* compiled from: RingerModeTrackerImpl.kt */
public final class RingerModeLiveData extends MutableLiveData<Integer> {
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final Executor executor;
    @NotNull
    public final IntentFilter filter;
    @NotNull
    public final Function0<Integer> getter;
    public boolean initialSticky;
    @NotNull
    public final RingerModeLiveData$receiver$1 receiver = new RingerModeLiveData$receiver$1(this);

    public RingerModeLiveData(@NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull Executor executor2, @NotNull String str, @NotNull Function0<Integer> function0) {
        this.broadcastDispatcher = broadcastDispatcher2;
        this.executor = executor2;
        this.getter = function0;
        this.filter = new IntentFilter(str);
    }

    public final boolean getInitialSticky() {
        return this.initialSticky;
    }

    @NotNull
    public Integer getValue() {
        Integer num = (Integer) super.getValue();
        return Integer.valueOf(num == null ? -1 : num.intValue());
    }

    public void onActive() {
        super.onActive();
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.receiver, this.filter, this.executor, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        this.executor.execute(new RingerModeLiveData$onActive$1(this));
    }

    public void onInactive() {
        super.onInactive();
        this.broadcastDispatcher.unregisterReceiver(this.receiver);
    }
}
