package com.android.systemui.qs.external;

import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: TileRequestDialogEventLogger.kt */
public final class TileRequestDialogEventLogger {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final InstanceIdSequence instanceIdSequence;
    @NotNull
    public final UiEventLogger uiEventLogger;

    public TileRequestDialogEventLogger(@NotNull UiEventLogger uiEventLogger2, @NotNull InstanceIdSequence instanceIdSequence2) {
        this.uiEventLogger = uiEventLogger2;
        this.instanceIdSequence = instanceIdSequence2;
    }

    /* compiled from: TileRequestDialogEventLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public TileRequestDialogEventLogger() {
        this(new UiEventLoggerImpl(), new InstanceIdSequence(1048576));
    }

    @NotNull
    public final InstanceId newInstanceId() {
        return this.instanceIdSequence.newInstanceId();
    }

    public final void logDialogShown(@NotNull String str, @NotNull InstanceId instanceId) {
        this.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_SHOWN, 0, str, instanceId);
    }

    public final void logUserResponse(int i, @NotNull String str, @NotNull InstanceId instanceId) {
        TileRequestDialogEvent tileRequestDialogEvent;
        if (i == 0) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_NOT_ADDED;
        } else if (i == 2) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ADDED;
        } else if (i == 3) {
            tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_DISMISSED;
        } else {
            throw new IllegalArgumentException(Intrinsics.stringPlus("User response not valid: ", Integer.valueOf(i)));
        }
        this.uiEventLogger.logWithInstanceId(tileRequestDialogEvent, 0, str, instanceId);
    }

    public final void logTileAlreadyAdded(@NotNull String str, @NotNull InstanceId instanceId) {
        this.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ALREADY_ADDED, 0, str, instanceId);
    }
}
