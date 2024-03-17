package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaDeviceManager.kt */
public final class MediaDeviceManager implements MediaDataManager.Listener, Dumpable {
    @NotNull
    public final Executor bgExecutor;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final MediaControllerFactory controllerFactory;
    @NotNull
    public final Map<String, Entry> entries = new LinkedHashMap();
    @NotNull
    public final Executor fgExecutor;
    @NotNull
    public final Set<Listener> listeners = new LinkedHashSet();
    @NotNull
    public final LocalMediaManagerFactory localMediaManagerFactory;
    @NotNull
    public final MediaRouter2Manager mr2manager;
    @NotNull
    public final MediaMuteAwaitConnectionManagerFactory muteAwaitConnectionManagerFactory;

    /* compiled from: MediaDeviceManager.kt */
    public interface Listener {
        void onKeyRemoved(@NotNull String str);

        void onMediaDeviceChanged(@NotNull String str, @Nullable String str2, @Nullable MediaDeviceData mediaDeviceData);
    }

    public MediaDeviceManager(@NotNull MediaControllerFactory mediaControllerFactory, @NotNull LocalMediaManagerFactory localMediaManagerFactory2, @NotNull MediaRouter2Manager mediaRouter2Manager, @NotNull MediaMuteAwaitConnectionManagerFactory mediaMuteAwaitConnectionManagerFactory, @NotNull ConfigurationController configurationController2, @NotNull Executor executor, @NotNull Executor executor2, @NotNull DumpManager dumpManager) {
        this.controllerFactory = mediaControllerFactory;
        this.localMediaManagerFactory = localMediaManagerFactory2;
        this.mr2manager = mediaRouter2Manager;
        this.muteAwaitConnectionManagerFactory = mediaMuteAwaitConnectionManagerFactory;
        this.configurationController = configurationController2;
        this.fgExecutor = executor;
        this.bgExecutor = executor2;
        dumpManager.registerDumpable(MediaDeviceManager.class.getName(), this);
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    public final boolean addListener(@NotNull Listener listener) {
        return this.listeners.add(listener);
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        MediaController mediaController;
        Entry remove;
        if (!(str2 == null || Intrinsics.areEqual((Object) str2, (Object) str) || (remove = this.entries.remove(str2)) == null)) {
            remove.stop();
        }
        Entry entry = this.entries.get(str);
        if (entry == null || !Intrinsics.areEqual((Object) entry.getToken(), (Object) mediaData.getToken())) {
            if (entry != null) {
                entry.stop();
            }
            if (mediaData.getDevice() != null) {
                processDevice(str, str2, mediaData.getDevice());
                return;
            }
            MediaSession.Token token = mediaData.getToken();
            if (token == null) {
                mediaController = null;
            } else {
                mediaController = this.controllerFactory.create(token);
            }
            LocalMediaManager create = this.localMediaManagerFactory.create(mediaData.getPackageName());
            String str3 = str;
            String str4 = str2;
            Entry entry2 = new Entry(str3, str4, mediaController, create, this.muteAwaitConnectionManagerFactory.create(create));
            this.entries.put(str, entry2);
            entry2.start();
        }
    }

    public void onMediaDataRemoved(@NotNull String str) {
        Entry remove = this.entries.remove(str);
        if (remove != null) {
            remove.stop();
        }
        if (remove != null) {
            for (Listener onKeyRemoved : this.listeners) {
                onKeyRemoved.onKeyRemoved(str);
            }
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("MediaDeviceManager state:");
        for (Map.Entry next : this.entries.entrySet()) {
            printWriter.println(Intrinsics.stringPlus("  key=", (String) next.getKey()));
            ((Entry) next.getValue()).dump(printWriter);
        }
    }

    public final void processDevice(String str, String str2, MediaDeviceData mediaDeviceData) {
        for (Listener onMediaDeviceChanged : this.listeners) {
            onMediaDeviceChanged.onMediaDeviceChanged(str, str2, mediaDeviceData);
        }
    }

    /* compiled from: MediaDeviceManager.kt */
    public final class Entry extends MediaController.Callback implements LocalMediaManager.DeviceCallback {
        @Nullable
        public AboutToConnectDevice aboutToConnectDeviceOverride;
        @NotNull
        public final MediaDeviceManager$Entry$configListener$1 configListener = new MediaDeviceManager$Entry$configListener$1(this);
        @Nullable
        public final MediaController controller;
        @Nullable
        public MediaDeviceData current;
        @NotNull
        public final String key;
        @NotNull
        public final LocalMediaManager localMediaManager;
        @Nullable
        public final MediaMuteAwaitConnectionManager muteAwaitConnectionManager;
        @Nullable
        public final String oldKey;
        public int playbackType;
        public boolean started;

        public Entry(@NotNull String str, @Nullable String str2, @Nullable MediaController mediaController, @NotNull LocalMediaManager localMediaManager2, @Nullable MediaMuteAwaitConnectionManager mediaMuteAwaitConnectionManager) {
            this.key = str;
            this.oldKey = str2;
            this.controller = mediaController;
            this.localMediaManager = localMediaManager2;
            this.muteAwaitConnectionManager = mediaMuteAwaitConnectionManager;
        }

        @NotNull
        public final String getKey() {
            return this.key;
        }

        @Nullable
        public final String getOldKey() {
            return this.oldKey;
        }

        @Nullable
        public final MediaController getController() {
            return this.controller;
        }

        @NotNull
        public final LocalMediaManager getLocalMediaManager() {
            return this.localMediaManager;
        }

        @Nullable
        public final MediaMuteAwaitConnectionManager getMuteAwaitConnectionManager() {
            return this.muteAwaitConnectionManager;
        }

        @Nullable
        public final MediaSession.Token getToken() {
            MediaController mediaController = this.controller;
            if (mediaController == null) {
                return null;
            }
            return mediaController.getSessionToken();
        }

        public final void setCurrent(MediaDeviceData mediaDeviceData) {
            boolean z = mediaDeviceData != null && mediaDeviceData.equalsWithoutIcon(this.current);
            if (!this.started || !z) {
                this.current = mediaDeviceData;
                MediaDeviceManager.this.fgExecutor.execute(new MediaDeviceManager$Entry$current$1(MediaDeviceManager.this, this, mediaDeviceData));
            }
        }

        public final void start() {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$start$1(this, MediaDeviceManager.this));
        }

        public final void stop() {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$stop$1(this, MediaDeviceManager.this));
        }

        public final void dump(@NotNull PrintWriter printWriter) {
            RoutingSessionInfo routingSessionInfo;
            List list;
            MediaController.PlaybackInfo playbackInfo;
            MediaController mediaController = this.controller;
            Integer num = null;
            if (mediaController == null) {
                routingSessionInfo = null;
            } else {
                routingSessionInfo = MediaDeviceManager.this.mr2manager.getRoutingSessionForMediaController(mediaController);
            }
            if (routingSessionInfo == null) {
                list = null;
            } else {
                list = MediaDeviceManager.this.mr2manager.getSelectedRoutes(routingSessionInfo);
            }
            MediaDeviceData mediaDeviceData = this.current;
            printWriter.println(Intrinsics.stringPlus("    current device is ", mediaDeviceData == null ? null : mediaDeviceData.getName()));
            MediaController controller2 = getController();
            if (!(controller2 == null || (playbackInfo = controller2.getPlaybackInfo()) == null)) {
                num = Integer.valueOf(playbackInfo.getPlaybackType());
            }
            printWriter.println("    PlaybackType=" + num + " (1 for local, 2 for remote) cached=" + this.playbackType);
            printWriter.println(Intrinsics.stringPlus("    routingSession=", routingSessionInfo));
            printWriter.println(Intrinsics.stringPlus("    selectedRoutes=", list));
        }

        public void onAudioInfoChanged(@Nullable MediaController.PlaybackInfo playbackInfo) {
            int playbackType2 = playbackInfo == null ? 0 : playbackInfo.getPlaybackType();
            if (playbackType2 != this.playbackType) {
                this.playbackType = playbackType2;
                updateCurrent();
            }
        }

        public void onDeviceListUpdate(@Nullable List<? extends MediaDevice> list) {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$onDeviceListUpdate$1(this));
        }

        public void onSelectedDeviceStateChanged(@NotNull MediaDevice mediaDevice, int i) {
            MediaDeviceManager.this.bgExecutor.execute(new MediaDeviceManager$Entry$onSelectedDeviceStateChanged$1(this));
        }

        public void onAboutToConnectDeviceAdded(@NotNull String str, @NotNull String str2, @Nullable Drawable drawable) {
            this.aboutToConnectDeviceOverride = new AboutToConnectDevice(this.localMediaManager.getMediaDeviceById(str), new MediaDeviceData(true, drawable, str2, (PendingIntent) null, (String) null, 24, (DefaultConstructorMarker) null));
            updateCurrent();
        }

        public void onAboutToConnectDeviceRemoved() {
            this.aboutToConnectDeviceOverride = null;
            updateCurrent();
        }

        /* JADX WARNING: Removed duplicated region for block: B:37:0x0063  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x0065  */
        /* JADX WARNING: Removed duplicated region for block: B:41:0x006e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void updateCurrent() {
            /*
                r12 = this;
                com.android.systemui.media.AboutToConnectDevice r0 = r12.aboutToConnectDeviceOverride
                if (r0 == 0) goto L_0x0018
                com.android.settingslib.media.MediaDevice r1 = r0.getFullMediaDevice()
                if (r1 != 0) goto L_0x0018
                com.android.systemui.media.MediaDeviceData r1 = r0.getBackupMediaDeviceData()
                if (r1 == 0) goto L_0x0018
                com.android.systemui.media.MediaDeviceData r0 = r0.getBackupMediaDeviceData()
                r12.setCurrent(r0)
                return
            L_0x0018:
                r1 = 0
                if (r0 != 0) goto L_0x001d
                r0 = r1
                goto L_0x0021
            L_0x001d:
                com.android.settingslib.media.MediaDevice r0 = r0.getFullMediaDevice()
            L_0x0021:
                if (r0 != 0) goto L_0x0029
                com.android.settingslib.media.LocalMediaManager r0 = r12.localMediaManager
                com.android.settingslib.media.MediaDevice r0 = r0.getCurrentConnectedDevice()
            L_0x0029:
                android.media.session.MediaController r2 = r12.controller
                if (r2 != 0) goto L_0x002f
                r2 = r1
                goto L_0x0039
            L_0x002f:
                com.android.systemui.media.MediaDeviceManager r3 = com.android.systemui.media.MediaDeviceManager.this
                android.media.MediaRouter2Manager r3 = r3.mr2manager
                android.media.RoutingSessionInfo r2 = r3.getRoutingSessionForMediaController(r2)
            L_0x0039:
                if (r0 == 0) goto L_0x0043
                android.media.session.MediaController r3 = r12.controller
                if (r3 == 0) goto L_0x0041
                if (r2 == 0) goto L_0x0043
            L_0x0041:
                r3 = 1
                goto L_0x0044
            L_0x0043:
                r3 = 0
            L_0x0044:
                r5 = r3
                if (r2 != 0) goto L_0x0049
            L_0x0047:
                r2 = r1
                goto L_0x0054
            L_0x0049:
                java.lang.CharSequence r2 = r2.getName()
                if (r2 != 0) goto L_0x0050
                goto L_0x0047
            L_0x0050:
                java.lang.String r2 = r2.toString()
            L_0x0054:
                if (r2 != 0) goto L_0x005e
                if (r0 != 0) goto L_0x005a
                r7 = r1
                goto L_0x005f
            L_0x005a:
                java.lang.String r2 = r0.getName()
            L_0x005e:
                r7 = r2
            L_0x005f:
                com.android.systemui.media.MediaDeviceData r2 = new com.android.systemui.media.MediaDeviceData
                if (r0 != 0) goto L_0x0065
                r6 = r1
                goto L_0x006a
            L_0x0065:
                android.graphics.drawable.Drawable r3 = r0.getIconWithoutBackground()
                r6 = r3
            L_0x006a:
                r8 = 0
                if (r0 != 0) goto L_0x006e
                goto L_0x0072
            L_0x006e:
                java.lang.String r1 = r0.getId()
            L_0x0072:
                r9 = r1
                r10 = 8
                r11 = 0
                r4 = r2
                r4.<init>(r5, r6, r7, r8, r9, r10, r11)
                r12.setCurrent(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDeviceManager.Entry.updateCurrent():void");
        }
    }
}
