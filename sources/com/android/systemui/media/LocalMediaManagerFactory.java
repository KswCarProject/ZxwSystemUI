package com.android.systemui.media;

import android.app.Notification;
import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LocalMediaManagerFactory.kt */
public final class LocalMediaManagerFactory {
    @NotNull
    public final Context context;
    @Nullable
    public final LocalBluetoothManager localBluetoothManager;

    public LocalMediaManagerFactory(@NotNull Context context2, @Nullable LocalBluetoothManager localBluetoothManager2) {
        this.context = context2;
        this.localBluetoothManager = localBluetoothManager2;
    }

    @NotNull
    public final LocalMediaManager create(@NotNull String str) {
        return new LocalMediaManager(this.context, this.localBluetoothManager, new InfoMediaManager(this.context, str, (Notification) null, this.localBluetoothManager), str);
    }
}
