package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInputCoordinator.kt */
public final class RemoteInputCoordinatorKt$DEBUG$2 extends Lambda implements Function0<Boolean> {
    public static final RemoteInputCoordinatorKt$DEBUG$2 INSTANCE = new RemoteInputCoordinatorKt$DEBUG$2();

    public RemoteInputCoordinatorKt$DEBUG$2() {
        super(0);
    }

    @NotNull
    public final Boolean invoke() {
        return Boolean.valueOf(Log.isLoggable("RemoteInputCoordinator", 3));
    }
}
