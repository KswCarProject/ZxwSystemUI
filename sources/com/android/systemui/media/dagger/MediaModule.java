package com.android.systemui.media.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import dagger.Lazy;
import java.util.Optional;

public interface MediaModule {
    static MediaHost providesQSMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    static MediaHost providesQuickQSMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    static MediaHost providesKeyguardMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager);
    }

    static Optional<MediaTttChipControllerSender> providesMediaTttChipControllerSender(MediaTttFlags mediaTttFlags, Lazy<MediaTttChipControllerSender> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.of(lazy.get());
    }

    static Optional<MediaTttChipControllerReceiver> providesMediaTttChipControllerReceiver(MediaTttFlags mediaTttFlags, Lazy<MediaTttChipControllerReceiver> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.of(lazy.get());
    }

    static MediaTttLogger providesMediaTttSenderLogger(LogBuffer logBuffer) {
        return new MediaTttLogger("Sender", logBuffer);
    }

    static MediaTttLogger providesMediaTttReceiverLogger(LogBuffer logBuffer) {
        return new MediaTttLogger("Receiver", logBuffer);
    }

    static Optional<MediaTttCommandLineHelper> providesMediaTttCommandLineHelper(MediaTttFlags mediaTttFlags, Lazy<MediaTttCommandLineHelper> lazy) {
        if (!mediaTttFlags.isMediaTttEnabled()) {
            return Optional.empty();
        }
        return Optional.of(lazy.get());
    }

    static Optional<MediaMuteAwaitConnectionCli> providesMediaMuteAwaitConnectionCli(MediaFlags mediaFlags, Lazy<MediaMuteAwaitConnectionCli> lazy) {
        if (!mediaFlags.areMuteAwaitConnectionsEnabled()) {
            return Optional.empty();
        }
        return Optional.of(lazy.get());
    }

    static Optional<NearbyMediaDevicesManager> providesNearbyMediaDevicesManager(MediaFlags mediaFlags, Lazy<NearbyMediaDevicesManager> lazy) {
        if (!mediaFlags.areNearbyMediaDevicesEnabled()) {
            return Optional.empty();
        }
        return Optional.of(lazy.get());
    }
}
