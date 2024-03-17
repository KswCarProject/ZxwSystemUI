package com.android.systemui.media.taptotransfer.receiver;

import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChipStateReceiver.kt */
public enum ChipStateReceiver {
    CLOSE_TO_SENDER(0, MediaTttReceiverUiEvents.MEDIA_TTT_RECEIVER_CLOSE_TO_SENDER),
    FAR_FROM_SENDER(1, MediaTttReceiverUiEvents.MEDIA_TTT_RECEIVER_FAR_FROM_SENDER);
    
    @NotNull
    public static final Companion Companion = null;
    private final int stateInt;
    @NotNull
    private final UiEventLogger.UiEventEnum uiEvent;

    /* access modifiers changed from: public */
    ChipStateReceiver(int i, UiEventLogger.UiEventEnum uiEventEnum) {
        this.stateInt = i;
        this.uiEvent = uiEventEnum;
    }

    public final int getStateInt() {
        return this.stateInt;
    }

    @NotNull
    public final UiEventLogger.UiEventEnum getUiEvent() {
        return this.uiEvent;
    }

    /* access modifiers changed from: public */
    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    /* compiled from: ChipStateReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @Nullable
        public final ChipStateReceiver getReceiverStateFromId(int i) {
            boolean z;
            try {
                ChipStateReceiver[] values = ChipStateReceiver.values();
                int length = values.length;
                int i2 = 0;
                while (i2 < length) {
                    ChipStateReceiver chipStateReceiver = values[i2];
                    i2++;
                    if (chipStateReceiver.getStateInt() == i) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (z) {
                        return chipStateReceiver;
                    }
                }
                throw new NoSuchElementException("Array contains no element matching the predicate.");
            } catch (NoSuchElementException e) {
                Log.e("ChipStateReceiver", Intrinsics.stringPlus("Could not find requested state ", Integer.valueOf(i)), e);
                return null;
            }
        }

        public final int getReceiverStateIdFromName(@NotNull String str) {
            return ChipStateReceiver.valueOf(str).getStateInt();
        }
    }
}
