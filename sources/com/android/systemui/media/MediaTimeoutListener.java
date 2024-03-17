package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTimeoutListener.kt */
public final class MediaTimeoutListener implements MediaDataManager.Listener {
    @NotNull
    public final MediaTimeoutLogger logger;
    @NotNull
    public final DelayableExecutor mainExecutor;
    @NotNull
    public final MediaControllerFactory mediaControllerFactory;
    @NotNull
    public final Map<String, PlaybackStateListener> mediaListeners = new LinkedHashMap();
    public Function2<? super String, ? super PlaybackState, Unit> stateCallback;
    @NotNull
    public final SystemClock systemClock;
    public Function2<? super String, ? super Boolean, Unit> timeoutCallback;

    public MediaTimeoutListener(@NotNull MediaControllerFactory mediaControllerFactory2, @NotNull DelayableExecutor delayableExecutor, @NotNull MediaTimeoutLogger mediaTimeoutLogger, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull SystemClock systemClock2) {
        this.mediaControllerFactory = mediaControllerFactory2;
        this.mainExecutor = delayableExecutor;
        this.logger = mediaTimeoutLogger;
        this.systemClock = systemClock2;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            public final /* synthetic */ MediaTimeoutListener this$0;

            {
                this.this$0 = r1;
            }

            public void onDozingChanged(boolean z) {
                if (!z) {
                    Map access$getMediaListeners$p = this.this$0.mediaListeners;
                    MediaTimeoutListener mediaTimeoutListener = this.this$0;
                    for (Map.Entry entry : access$getMediaListeners$p.entrySet()) {
                        String str = (String) entry.getKey();
                        PlaybackStateListener playbackStateListener = (PlaybackStateListener) entry.getValue();
                        if (playbackStateListener.getCancellation() != null && playbackStateListener.getExpiration() <= mediaTimeoutListener.systemClock.elapsedRealtime()) {
                            playbackStateListener.expireMediaTimeout(str, "timeout happened while dozing");
                            playbackStateListener.doTimeout();
                        }
                    }
                }
            }
        });
    }

    public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    @NotNull
    public final Function2<String, Boolean, Unit> getTimeoutCallback() {
        Function2<? super String, ? super Boolean, Unit> function2 = this.timeoutCallback;
        if (function2 != null) {
            return function2;
        }
        return null;
    }

    public final void setTimeoutCallback(@NotNull Function2<? super String, ? super Boolean, Unit> function2) {
        this.timeoutCallback = function2;
    }

    @NotNull
    public final Function2<String, PlaybackState, Unit> getStateCallback() {
        Function2<? super String, ? super PlaybackState, Unit> function2 = this.stateCallback;
        if (function2 != null) {
            return function2;
        }
        return null;
    }

    public final void setStateCallback(@NotNull Function2<? super String, ? super PlaybackState, Unit> function2) {
        this.stateCallback = function2;
    }

    public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
        PlaybackStateListener playbackStateListener = this.mediaListeners.get(str);
        if (playbackStateListener == null) {
            playbackStateListener = null;
        } else if (playbackStateListener.getDestroyed()) {
            this.logger.logReuseListener(str);
        } else {
            return;
        }
        boolean z3 = true;
        if (str2 != null && !Intrinsics.areEqual((Object) str, (Object) str2)) {
            playbackStateListener = this.mediaListeners.remove(str2);
            MediaTimeoutLogger mediaTimeoutLogger = this.logger;
            if (playbackStateListener == null) {
                z3 = false;
            }
            mediaTimeoutLogger.logMigrateListener(str2, str, z3);
        }
        PlaybackStateListener playbackStateListener2 = playbackStateListener;
        if (playbackStateListener2 == null) {
            this.mediaListeners.put(str, new PlaybackStateListener(str, mediaData));
            return;
        }
        boolean isPlaying = playbackStateListener2.isPlaying();
        this.logger.logUpdateListener(str, isPlaying);
        playbackStateListener2.setMediaData(mediaData);
        playbackStateListener2.setKey(str);
        this.mediaListeners.put(str, playbackStateListener2);
        if (isPlaying != playbackStateListener2.isPlaying()) {
            this.mainExecutor.execute(new MediaTimeoutListener$onMediaDataLoaded$2$1(this, str));
        }
    }

    public void onMediaDataRemoved(@NotNull String str) {
        PlaybackStateListener remove = this.mediaListeners.remove(str);
        if (remove != null) {
            remove.destroy();
        }
    }

    /* compiled from: MediaTimeoutListener.kt */
    public final class PlaybackStateListener extends MediaController.Callback {
        @Nullable
        public Runnable cancellation;
        public boolean destroyed;
        public long expiration = Long.MAX_VALUE;
        @NotNull
        public String key;
        @Nullable
        public PlaybackState lastState;
        @Nullable
        public MediaController mediaController;
        @NotNull
        public MediaData mediaData;
        @Nullable
        public Boolean resumption;
        public boolean timedOut;

        public PlaybackStateListener(@NotNull String str, @NotNull MediaData mediaData2) {
            this.key = str;
            this.mediaData = mediaData2;
            setMediaData(mediaData2);
        }

        public final void setKey(@NotNull String str) {
            this.key = str;
        }

        public final boolean getDestroyed() {
            return this.destroyed;
        }

        public final long getExpiration() {
            return this.expiration;
        }

        public final void setMediaData(@NotNull MediaData mediaData2) {
            this.destroyed = false;
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this);
            }
            this.mediaData = mediaData2;
            MediaSession.Token token = mediaData2.getToken();
            PlaybackState playbackState = null;
            MediaController create = token != null ? MediaTimeoutListener.this.mediaControllerFactory.create(token) : null;
            this.mediaController = create;
            if (create != null) {
                create.registerCallback(this);
            }
            MediaController mediaController3 = this.mediaController;
            if (mediaController3 != null) {
                playbackState = mediaController3.getPlaybackState();
            }
            processState(playbackState, false);
        }

        @Nullable
        public final Runnable getCancellation() {
            return this.cancellation;
        }

        public final boolean isPlaying(int i) {
            return NotificationMediaManager.isPlayingState(i);
        }

        public final boolean isPlaying() {
            PlaybackState playbackState = this.lastState;
            if (playbackState == null) {
                return false;
            }
            return isPlaying(playbackState.getState());
        }

        public final void destroy() {
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this);
            }
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            this.destroyed = true;
        }

        public void onPlaybackStateChanged(@Nullable PlaybackState playbackState) {
            processState(playbackState, true);
        }

        public void onSessionDestroyed() {
            MediaTimeoutListener.this.logger.logSessionDestroyed(this.key);
            if (Intrinsics.areEqual((Object) this.resumption, (Object) Boolean.TRUE)) {
                MediaController mediaController2 = this.mediaController;
                if (mediaController2 != null) {
                    mediaController2.unregisterCallback(this);
                    return;
                }
                return;
            }
            destroy();
        }

        /* JADX WARNING: Removed duplicated region for block: B:36:0x00a7  */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x011b  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void processState(android.media.session.PlaybackState r8, boolean r9) {
            /*
                r7 = this;
                com.android.systemui.media.MediaTimeoutListener r0 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.media.MediaTimeoutLogger r0 = r0.logger
                java.lang.String r1 = r7.key
                r0.logPlaybackState(r1, r8)
                r0 = 1
                r1 = 0
                if (r8 != 0) goto L_0x0011
            L_0x000f:
                r2 = r1
                goto L_0x0020
            L_0x0011:
                int r2 = r8.getState()
                boolean r2 = r7.isPlaying(r2)
                boolean r3 = r7.isPlaying()
                if (r2 != r3) goto L_0x000f
                r2 = r0
            L_0x0020:
                android.media.session.PlaybackState r3 = r7.lastState
                r4 = 0
                if (r3 != 0) goto L_0x0027
                r3 = r4
                goto L_0x002f
            L_0x0027:
                long r5 = r3.getActions()
                java.lang.Long r3 = java.lang.Long.valueOf(r5)
            L_0x002f:
                if (r8 != 0) goto L_0x0033
                r5 = r4
                goto L_0x003b
            L_0x0033:
                long r5 = r8.getActions()
                java.lang.Long r5 = java.lang.Long.valueOf(r5)
            L_0x003b:
                boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r5)
                if (r3 == 0) goto L_0x005c
                com.android.systemui.media.MediaTimeoutListener r3 = com.android.systemui.media.MediaTimeoutListener.this
                android.media.session.PlaybackState r5 = r7.lastState
                if (r5 != 0) goto L_0x0049
                r5 = r4
                goto L_0x004d
            L_0x0049:
                java.util.List r5 = r5.getCustomActions()
            L_0x004d:
                if (r8 != 0) goto L_0x0050
                goto L_0x0054
            L_0x0050:
                java.util.List r4 = r8.getCustomActions()
            L_0x0054:
                boolean r3 = r3.areCustomActionListsEqual(r5, r4)
                if (r3 == 0) goto L_0x005c
                r3 = r0
                goto L_0x005d
            L_0x005c:
                r3 = r1
            L_0x005d:
                java.lang.Boolean r4 = r7.resumption
                com.android.systemui.media.MediaData r5 = r7.mediaData
                boolean r5 = r5.getResumption()
                java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)
                boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r5)
                r0 = r0 ^ r4
                r7.lastState = r8
                if (r3 == 0) goto L_0x0074
                if (r2 != 0) goto L_0x008e
            L_0x0074:
                if (r8 == 0) goto L_0x008e
                if (r9 == 0) goto L_0x008e
                com.android.systemui.media.MediaTimeoutListener r3 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.media.MediaTimeoutLogger r3 = r3.logger
                java.lang.String r4 = r7.key
                r3.logStateCallback(r4)
                com.android.systemui.media.MediaTimeoutListener r3 = com.android.systemui.media.MediaTimeoutListener.this
                kotlin.jvm.functions.Function2 r3 = r3.getStateCallback()
                java.lang.String r4 = r7.key
                r3.invoke(r4, r8)
            L_0x008e:
                if (r2 == 0) goto L_0x0093
                if (r0 != 0) goto L_0x0093
                return
            L_0x0093:
                com.android.systemui.media.MediaData r2 = r7.mediaData
                boolean r2 = r2.getResumption()
                java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
                r7.resumption = r2
                boolean r2 = r7.isPlaying()
                java.lang.String r3 = ", "
                if (r2 != 0) goto L_0x011b
                com.android.systemui.media.MediaTimeoutListener r9 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.media.MediaTimeoutLogger r9 = r9.logger
                java.lang.String r1 = r7.key
                java.lang.Boolean r4 = r7.resumption
                kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
                boolean r4 = r4.booleanValue()
                r9.logScheduleTimeout(r1, r2, r4)
                java.lang.Runnable r9 = r7.cancellation
                if (r9 == 0) goto L_0x00cd
                if (r0 != 0) goto L_0x00cd
                com.android.systemui.media.MediaTimeoutListener r8 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.media.MediaTimeoutLogger r8 = r8.logger
                java.lang.String r7 = r7.key
                r8.logCancelIgnored(r7)
                return
            L_0x00cd:
                java.lang.String r9 = r7.key
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "PLAYBACK STATE CHANGED - "
                r0.append(r1)
                r0.append(r8)
                r0.append(r3)
                java.lang.Boolean r8 = r7.resumption
                r0.append(r8)
                java.lang.String r8 = r0.toString()
                r7.expireMediaTimeout(r9, r8)
                com.android.systemui.media.MediaData r8 = r7.mediaData
                boolean r8 = r8.getResumption()
                if (r8 == 0) goto L_0x00f8
                long r8 = com.android.systemui.media.MediaTimeoutListenerKt.getRESUME_MEDIA_TIMEOUT()
                goto L_0x00fc
            L_0x00f8:
                long r8 = com.android.systemui.media.MediaTimeoutListenerKt.getPAUSED_MEDIA_TIMEOUT()
            L_0x00fc:
                com.android.systemui.media.MediaTimeoutListener r0 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.util.time.SystemClock r0 = r0.systemClock
                long r0 = r0.elapsedRealtime()
                long r0 = r0 + r8
                r7.expiration = r0
                com.android.systemui.media.MediaTimeoutListener r0 = com.android.systemui.media.MediaTimeoutListener.this
                com.android.systemui.util.concurrency.DelayableExecutor r0 = r0.mainExecutor
                com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$processState$1 r1 = new com.android.systemui.media.MediaTimeoutListener$PlaybackStateListener$processState$1
                r1.<init>(r7)
                java.lang.Runnable r8 = r0.executeDelayed(r1, r8)
                r7.cancellation = r8
                goto L_0x014e
            L_0x011b:
                java.lang.String r0 = r7.key
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r4 = "playback started - "
                r2.append(r4)
                r2.append(r8)
                r2.append(r3)
                java.lang.String r8 = r7.key
                r2.append(r8)
                java.lang.String r8 = r2.toString()
                r7.expireMediaTimeout(r0, r8)
                r7.timedOut = r1
                if (r9 == 0) goto L_0x014e
                com.android.systemui.media.MediaTimeoutListener r8 = com.android.systemui.media.MediaTimeoutListener.this
                kotlin.jvm.functions.Function2 r8 = r8.getTimeoutCallback()
                java.lang.String r9 = r7.key
                boolean r7 = r7.timedOut
                java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
                r8.invoke(r9, r7)
            L_0x014e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaTimeoutListener.PlaybackStateListener.processState(android.media.session.PlaybackState, boolean):void");
        }

        public final void doTimeout() {
            this.cancellation = null;
            MediaTimeoutListener.this.logger.logTimeout(this.key);
            this.timedOut = true;
            this.expiration = Long.MAX_VALUE;
            MediaTimeoutListener.this.getTimeoutCallback().invoke(this.key, Boolean.valueOf(this.timedOut));
        }

        public final void expireMediaTimeout(@NotNull String str, @NotNull String str2) {
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                MediaTimeoutListener.this.logger.logTimeoutCancelled(str, str2);
                runnable.run();
            }
            this.expiration = Long.MAX_VALUE;
            this.cancellation = null;
        }
    }

    public final boolean areCustomActionListsEqual(List<PlaybackState.CustomAction> list, List<PlaybackState.CustomAction> list2) {
        if (list == list2) {
            return true;
        }
        if (list == null || list2 == null || list.size() != list2.size()) {
            return false;
        }
        for (Pair pair : SequencesKt___SequencesKt.zip(CollectionsKt___CollectionsKt.asSequence(list), CollectionsKt___CollectionsKt.asSequence(list2))) {
            if (!areCustomActionsEqual((PlaybackState.CustomAction) pair.component1(), (PlaybackState.CustomAction) pair.component2())) {
                return false;
            }
        }
        return true;
    }

    public final boolean areCustomActionsEqual(PlaybackState.CustomAction customAction, PlaybackState.CustomAction customAction2) {
        if (!Intrinsics.areEqual((Object) customAction.getAction(), (Object) customAction2.getAction()) || !Intrinsics.areEqual((Object) customAction.getName(), (Object) customAction2.getName()) || customAction.getIcon() != customAction2.getIcon()) {
            return false;
        }
        if ((customAction.getExtras() == null) != (customAction2.getExtras() == null)) {
            return false;
        }
        if (customAction.getExtras() != null) {
            for (String str : customAction.getExtras().keySet()) {
                if (!Intrinsics.areEqual(customAction.getExtras().get(str), customAction2.getExtras().get(str))) {
                    return false;
                }
            }
        }
        return true;
    }
}
