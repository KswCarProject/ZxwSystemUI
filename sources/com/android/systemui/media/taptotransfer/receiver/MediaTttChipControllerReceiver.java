package com.android.systemui.media.taptotransfer.receiver;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerReceiver.kt */
public final class MediaTttChipControllerReceiver extends MediaTttChipControllerCommon<ChipReceiverInfo> {
    @NotNull
    public final MediaTttChipControllerReceiver$commandQueueCallbacks$1 commandQueueCallbacks;
    @NotNull
    public final Handler mainHandler;
    @NotNull
    public final MediaTttReceiverUiEventLogger uiEventLogger;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaTttChipControllerReceiver(@NotNull CommandQueue commandQueue, @NotNull Context context, @NotNull MediaTttLogger mediaTttLogger, @NotNull WindowManager windowManager, @NotNull ViewUtil viewUtil, @NotNull DelayableExecutor delayableExecutor, @NotNull TapGestureDetector tapGestureDetector, @NotNull PowerManager powerManager, @NotNull Handler handler, @NotNull MediaTttReceiverUiEventLogger mediaTttReceiverUiEventLogger) {
        super(context, mediaTttLogger, windowManager, viewUtil, delayableExecutor, tapGestureDetector, powerManager, R$layout.media_ttt_chip_receiver);
        this.mainHandler = handler;
        this.uiEventLogger = mediaTttReceiverUiEventLogger;
        MediaTttChipControllerReceiver$commandQueueCallbacks$1 mediaTttChipControllerReceiver$commandQueueCallbacks$1 = new MediaTttChipControllerReceiver$commandQueueCallbacks$1(this);
        this.commandQueueCallbacks = mediaTttChipControllerReceiver$commandQueueCallbacks$1;
        CommandQueue commandQueue2 = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) mediaTttChipControllerReceiver$commandQueueCallbacks$1);
    }

    public final void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        String name;
        ChipStateReceiver receiverStateFromId = ChipStateReceiver.Companion.getReceiverStateFromId(i);
        String str = "Invalid";
        if (!(receiverStateFromId == null || (name = receiverStateFromId.name()) == null)) {
            str = name;
        }
        getLogger$frameworks__base__packages__SystemUI__android_common__SystemUI_core().logStateChange(str, mediaRoute2Info.getId());
        if (receiverStateFromId == null) {
            Log.e("MediaTapToTransferRcvr", Intrinsics.stringPlus("Unhandled MediaTransferReceiverState ", Integer.valueOf(i)));
            return;
        }
        this.uiEventLogger.logReceiverStateChange(receiverStateFromId);
        ChipStateReceiver chipStateReceiver = ChipStateReceiver.FAR_FROM_SENDER;
        if (receiverStateFromId == chipStateReceiver) {
            String simpleName = Reflection.getOrCreateKotlinClass(chipStateReceiver.getClass()).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            removeChip(simpleName);
        } else if (icon == null) {
            displayChip(new ChipReceiverInfo(mediaRoute2Info, (Drawable) null, charSequence));
        } else {
            icon.loadDrawableAsync(getContext$frameworks__base__packages__SystemUI__android_common__SystemUI_core(), new MediaTttChipControllerReceiver$updateMediaTapToTransferReceiverDisplay$1(this, mediaRoute2Info, charSequence), this.mainHandler);
        }
    }

    public void updateChipView(@NotNull ChipReceiverInfo chipReceiverInfo, @NotNull ViewGroup viewGroup) {
        setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(viewGroup, chipReceiverInfo.getRouteInfo().getPackageName(), chipReceiverInfo.getAppIconDrawableOverride(), chipReceiverInfo.getAppNameOverride());
    }

    @Nullable
    public Integer getIconSize(boolean z) {
        int i;
        Resources resources = getContext$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getResources();
        if (z) {
            i = R$dimen.media_ttt_icon_size_receiver;
        } else {
            i = R$dimen.media_ttt_generic_icon_size_receiver;
        }
        return Integer.valueOf(resources.getDimensionPixelSize(i));
    }
}
