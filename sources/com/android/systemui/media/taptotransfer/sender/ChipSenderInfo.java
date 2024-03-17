package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerSender.kt */
public final class ChipSenderInfo implements ChipInfoCommon {
    @NotNull
    public final MediaRoute2Info routeInfo;
    @NotNull
    public final ChipStateSender state;
    @Nullable
    public final IUndoMediaTransferCallback undoCallback;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChipSenderInfo)) {
            return false;
        }
        ChipSenderInfo chipSenderInfo = (ChipSenderInfo) obj;
        return this.state == chipSenderInfo.state && Intrinsics.areEqual((Object) this.routeInfo, (Object) chipSenderInfo.routeInfo) && Intrinsics.areEqual((Object) this.undoCallback, (Object) chipSenderInfo.undoCallback);
    }

    public int hashCode() {
        int hashCode = ((this.state.hashCode() * 31) + this.routeInfo.hashCode()) * 31;
        IUndoMediaTransferCallback iUndoMediaTransferCallback = this.undoCallback;
        return hashCode + (iUndoMediaTransferCallback == null ? 0 : iUndoMediaTransferCallback.hashCode());
    }

    @NotNull
    public String toString() {
        return "ChipSenderInfo(state=" + this.state + ", routeInfo=" + this.routeInfo + ", undoCallback=" + this.undoCallback + ')';
    }

    public ChipSenderInfo(@NotNull ChipStateSender chipStateSender, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        this.state = chipStateSender;
        this.routeInfo = mediaRoute2Info;
        this.undoCallback = iUndoMediaTransferCallback;
    }

    @NotNull
    public final ChipStateSender getState() {
        return this.state;
    }

    @NotNull
    public final MediaRoute2Info getRouteInfo() {
        return this.routeInfo;
    }

    @Nullable
    public final IUndoMediaTransferCallback getUndoCallback() {
        return this.undoCallback;
    }

    public long getTimeoutMs() {
        return this.state.getTimeout();
    }
}
