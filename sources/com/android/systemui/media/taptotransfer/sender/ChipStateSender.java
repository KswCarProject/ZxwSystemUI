package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.R$string;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChipStateSender.kt */
public enum ChipStateSender {
    ALMOST_CLOSE_TO_START_CAST(0, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_ALMOST_CLOSE_TO_START_CAST, Integer.valueOf(R$string.media_move_closer_to_start_cast), false, false, 0, 56, (long) null),
    ALMOST_CLOSE_TO_END_CAST(1, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_ALMOST_CLOSE_TO_END_CAST, Integer.valueOf(R$string.media_move_closer_to_end_cast), false, false, 0, 56, (long) null),
    TRANSFER_TO_RECEIVER_TRIGGERED(2, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_RECEIVER_TRIGGERED, Integer.valueOf(R$string.media_transfer_playing_different_device), true, false, WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS, 16, (long) null),
    TRANSFER_TO_THIS_DEVICE_TRIGGERED(3, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_TRIGGERED, Integer.valueOf(R$string.media_transfer_playing_this_device), true, false, WifiTracker.MAX_SCAN_RESULT_AGE_MILLIS, 16, (long) null),
    TRANSFER_TO_RECEIVER_FAILED(6, r7, Integer.valueOf(r1), false, true, 0, 40, (long) null),
    TRANSFER_TO_THIS_DEVICE_FAILED(7, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_FAILED, Integer.valueOf(r1), false, true, 0, 40, (long) null),
    FAR_FROM_RECEIVER(8, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_FAR_FROM_RECEIVER, (int) null, false, false, 0, 56, (long) null);
    
    @NotNull
    public static final Companion Companion = null;
    private final boolean isMidTransfer;
    private final boolean isTransferFailure;
    private final int stateInt;
    @Nullable
    private final Integer stringResId;
    private final long timeout;
    @NotNull
    private final UiEventLogger.UiEventEnum uiEvent;

    @Nullable
    public View.OnClickListener undoClickListener(@NotNull MediaTttChipControllerSender mediaTttChipControllerSender, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable IUndoMediaTransferCallback iUndoMediaTransferCallback, @NotNull MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
        return null;
    }

    /* access modifiers changed from: public */
    ChipStateSender(int i, UiEventLogger.UiEventEnum uiEventEnum, Integer num, boolean z, boolean z2, long j) {
        this.stateInt = i;
        this.uiEvent = uiEventEnum;
        this.stringResId = num;
        this.isMidTransfer = z;
        this.isTransferFailure = z2;
        this.timeout = j;
    }

    public final int getStateInt() {
        return this.stateInt;
    }

    @NotNull
    public final UiEventLogger.UiEventEnum getUiEvent() {
        return this.uiEvent;
    }

    public final boolean isMidTransfer() {
        return this.isMidTransfer;
    }

    public final boolean isTransferFailure() {
        return this.isTransferFailure;
    }

    public final long getTimeout() {
        return this.timeout;
    }

    /* access modifiers changed from: public */
    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    /* compiled from: ChipStateSender.kt */
    public static final class TRANSFER_TO_RECEIVER_SUCCEEDED extends ChipStateSender {
        public TRANSFER_TO_RECEIVER_SUCCEEDED(String str, int i) {
            super(str, i, 4, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_RECEIVER_SUCCEEDED, Integer.valueOf(R$string.media_transfer_playing_different_device), false, false, 0, 56, (DefaultConstructorMarker) null);
        }

        @Nullable
        public View.OnClickListener undoClickListener(@NotNull MediaTttChipControllerSender mediaTttChipControllerSender, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable IUndoMediaTransferCallback iUndoMediaTransferCallback, @NotNull MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
            if (iUndoMediaTransferCallback == null) {
                return null;
            }
            return new ChipStateSender$TRANSFER_TO_RECEIVER_SUCCEEDED$undoClickListener$1(mediaTttSenderUiEventLogger, iUndoMediaTransferCallback, mediaTttChipControllerSender, mediaRoute2Info);
        }
    }

    /* compiled from: ChipStateSender.kt */
    public static final class TRANSFER_TO_THIS_DEVICE_SUCCEEDED extends ChipStateSender {
        public TRANSFER_TO_THIS_DEVICE_SUCCEEDED(String str, int i) {
            super(str, i, 5, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_SUCCEEDED, Integer.valueOf(R$string.media_transfer_playing_this_device), false, false, 0, 56, (DefaultConstructorMarker) null);
        }

        @Nullable
        public View.OnClickListener undoClickListener(@NotNull MediaTttChipControllerSender mediaTttChipControllerSender, @NotNull MediaRoute2Info mediaRoute2Info, @Nullable IUndoMediaTransferCallback iUndoMediaTransferCallback, @NotNull MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
            if (iUndoMediaTransferCallback == null) {
                return null;
            }
            return new ChipStateSender$TRANSFER_TO_THIS_DEVICE_SUCCEEDED$undoClickListener$1(mediaTttSenderUiEventLogger, iUndoMediaTransferCallback, mediaTttChipControllerSender, mediaRoute2Info);
        }
    }

    @Nullable
    public final String getChipTextString(@NotNull Context context, @NotNull String str) {
        Integer num = this.stringResId;
        if (num == null) {
            return null;
        }
        return context.getString(num.intValue(), new Object[]{str});
    }

    /* compiled from: ChipStateSender.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @Nullable
        public final ChipStateSender getSenderStateFromId(int i) {
            boolean z;
            try {
                ChipStateSender[] values = ChipStateSender.values();
                int length = values.length;
                int i2 = 0;
                while (i2 < length) {
                    ChipStateSender chipStateSender = values[i2];
                    i2++;
                    if (chipStateSender.getStateInt() == i) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (z) {
                        return chipStateSender;
                    }
                }
                throw new NoSuchElementException("Array contains no element matching the predicate.");
            } catch (NoSuchElementException e) {
                Log.e("ChipStateSender", Intrinsics.stringPlus("Could not find requested state ", Integer.valueOf(i)), e);
                return null;
            }
        }

        public final int getSenderStateIdFromName(@NotNull String str) {
            return ChipStateSender.valueOf(str).getStateInt();
        }
    }
}
