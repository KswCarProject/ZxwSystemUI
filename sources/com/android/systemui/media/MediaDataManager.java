package com.android.systemui.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.R$styleable;
import com.android.internal.logging.InstanceId;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager implements Dumpable, BcSmartspaceDataPlugin.SmartspaceTargetListener {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final String EXTRAS_MEDIA_SOURCE_PACKAGE_NAME = "package_name";
    public static final int MAX_COMPACT_ACTIONS = 3;
    public static final int MAX_NOTIFICATION_ACTIONS = MediaViewHolder.Companion.getGenericButtonIds().size();
    @NotNull
    public static final String SMARTSPACE_UI_SURFACE_LABEL = "media_data_manager";
    @NotNull
    public final ActivityStarter activityStarter;
    public boolean allowMediaRecommendations;
    @NotNull
    public final MediaDataManager$appChangeReceiver$1 appChangeReceiver;
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final Context context;
    @NotNull
    public final DelayableExecutor foregroundExecutor;
    @NotNull
    public final Set<Listener> internalListeners;
    @NotNull
    public final MediaUiEventLogger logger;
    @NotNull
    public final MediaControllerFactory mediaControllerFactory;
    @NotNull
    public final MediaDataFilter mediaDataFilter;
    @NotNull
    public final LinkedHashMap<String, MediaData> mediaEntries;
    @NotNull
    public final MediaFlags mediaFlags;
    @NotNull
    public SmartspaceMediaData smartspaceMediaData;
    @NotNull
    public final SmartspaceMediaDataProvider smartspaceMediaDataProvider;
    @Nullable
    public SmartspaceSession smartspaceSession;
    @NotNull
    public final SystemClock systemClock;
    public final int themeText;
    @NotNull
    public final TunerService tunerService;
    public boolean useMediaResumption;
    public final boolean useQsMediaPlayer;

    public final boolean includesAction(long j, long j2) {
        return ((j2 == 4 || j2 == 2) && (512 & j) > 0) || (j & j2) != 0;
    }

    public final void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData) {
        Trace.beginSection("MediaDataManager#onMediaDataLoaded");
        try {
            Assert.isMainThread();
            if (this.mediaEntries.containsKey(str)) {
                this.mediaEntries.put(str, mediaData);
                notifyMediaDataLoaded(str, str2, mediaData);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public MediaDataManager(@NotNull Context context2, @NotNull Executor executor, @NotNull DelayableExecutor delayableExecutor, @NotNull MediaControllerFactory mediaControllerFactory2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull DumpManager dumpManager, @NotNull MediaTimeoutListener mediaTimeoutListener, @NotNull MediaResumeListener mediaResumeListener, @NotNull MediaSessionBasedFilter mediaSessionBasedFilter, @NotNull MediaDeviceManager mediaDeviceManager, @NotNull MediaDataCombineLatest mediaDataCombineLatest, @NotNull MediaDataFilter mediaDataFilter2, @NotNull ActivityStarter activityStarter2, @NotNull SmartspaceMediaDataProvider smartspaceMediaDataProvider2, boolean z, boolean z2, @NotNull SystemClock systemClock2, @NotNull TunerService tunerService2, @NotNull MediaFlags mediaFlags2, @NotNull MediaUiEventLogger mediaUiEventLogger) {
        MediaTimeoutListener mediaTimeoutListener2 = mediaTimeoutListener;
        MediaResumeListener mediaResumeListener2 = mediaResumeListener;
        MediaSessionBasedFilter mediaSessionBasedFilter2 = mediaSessionBasedFilter;
        MediaDataFilter mediaDataFilter3 = mediaDataFilter2;
        SmartspaceMediaDataProvider smartspaceMediaDataProvider3 = smartspaceMediaDataProvider2;
        TunerService tunerService3 = tunerService2;
        this.context = context2;
        this.backgroundExecutor = executor;
        this.foregroundExecutor = delayableExecutor;
        this.mediaControllerFactory = mediaControllerFactory2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.mediaDataFilter = mediaDataFilter3;
        this.activityStarter = activityStarter2;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider3;
        this.useMediaResumption = z;
        this.useQsMediaPlayer = z2;
        this.systemClock = systemClock2;
        this.tunerService = tunerService3;
        this.mediaFlags = mediaFlags2;
        this.logger = mediaUiEventLogger;
        this.themeText = Utils.getColorAttr(context2, 16842806).getDefaultColor();
        this.internalListeners = new LinkedHashSet();
        this.mediaEntries = new LinkedHashMap<>();
        this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        this.allowMediaRecommendations = MediaDataManagerKt.allowMediaRecommendations(context2);
        MediaDataManager$appChangeReceiver$1 mediaDataManager$appChangeReceiver$1 = new MediaDataManager$appChangeReceiver$1(this);
        this.appChangeReceiver = mediaDataManager$appChangeReceiver$1;
        dumpManager.registerDumpable("MediaDataManager", this);
        addInternalListener(mediaTimeoutListener2);
        addInternalListener(mediaResumeListener2);
        addInternalListener(mediaSessionBasedFilter2);
        mediaSessionBasedFilter.addListener(mediaDeviceManager);
        mediaSessionBasedFilter2.addListener(mediaDataCombineLatest);
        mediaDeviceManager.addListener(mediaDataCombineLatest);
        mediaDataCombineLatest.addListener(mediaDataFilter2);
        mediaTimeoutListener2.setTimeoutCallback(new Function2<String, Boolean, Unit>(this) {
            public final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke((String) obj, ((Boolean) obj2).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull String str, boolean z) {
                MediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(this.this$0, str, z, false, 4, (Object) null);
            }
        });
        mediaTimeoutListener2.setStateCallback(new Function2<String, PlaybackState, Unit>(this) {
            public final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke((String) obj, (PlaybackState) obj2);
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull String str, @NotNull PlaybackState playbackState) {
                this.this$0.updateState(str, playbackState);
            }
        });
        mediaResumeListener2.setManager(this);
        mediaDataFilter3.setMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this);
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, mediaDataManager$appChangeReceiver$1, new IntentFilter("android.intent.action.PACKAGES_SUSPENDED"), (Executor) null, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        context2.registerReceiver(mediaDataManager$appChangeReceiver$1, intentFilter);
        smartspaceMediaDataProvider3.registerListener(this);
        SmartspaceSession createSmartspaceSession = ((SmartspaceManager) context2.getSystemService(SmartspaceManager.class)).createSmartspaceSession(new SmartspaceConfig.Builder(context2, SMARTSPACE_UI_SURFACE_LABEL).build());
        this.smartspaceSession = createSmartspaceSession;
        if (createSmartspaceSession != null) {
            createSmartspaceSession.addOnTargetsAvailableListener(Executors.newCachedThreadPool(), new MediaDataManager$3$1(this));
        }
        SmartspaceSession smartspaceSession2 = this.smartspaceSession;
        if (smartspaceSession2 != null) {
            smartspaceSession2.requestSmartspaceUpdate();
        }
        tunerService3.addTunable(new TunerService.Tunable(this) {
            public final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public void onTuningChanged(@Nullable String str, @Nullable String str2) {
                MediaDataManager mediaDataManager = this.this$0;
                mediaDataManager.allowMediaRecommendations = MediaDataManagerKt.allowMediaRecommendations(mediaDataManager.context);
                if (!this.this$0.allowMediaRecommendations) {
                    MediaDataManager mediaDataManager2 = this.this$0;
                    mediaDataManager2.dismissSmartspaceRecommendation(mediaDataManager2.getSmartspaceMediaData().getTargetId(), 0);
                }
            }
        }, "qs_media_recommend");
    }

    /* compiled from: MediaDataManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public final SmartspaceMediaData getSmartspaceMediaData() {
        return this.smartspaceMediaData;
    }

    public final boolean isRemoteCastNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().extras.containsKey("android.mediaRemoteDevice");
    }

    public MediaDataManager(@NotNull Context context2, @NotNull Executor executor, @NotNull DelayableExecutor delayableExecutor, @NotNull MediaControllerFactory mediaControllerFactory2, @NotNull DumpManager dumpManager, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull MediaTimeoutListener mediaTimeoutListener, @NotNull MediaResumeListener mediaResumeListener, @NotNull MediaSessionBasedFilter mediaSessionBasedFilter, @NotNull MediaDeviceManager mediaDeviceManager, @NotNull MediaDataCombineLatest mediaDataCombineLatest, @NotNull MediaDataFilter mediaDataFilter2, @NotNull ActivityStarter activityStarter2, @NotNull SmartspaceMediaDataProvider smartspaceMediaDataProvider2, @NotNull SystemClock systemClock2, @NotNull TunerService tunerService2, @NotNull MediaFlags mediaFlags2, @NotNull MediaUiEventLogger mediaUiEventLogger) {
        this(context2, executor, delayableExecutor, mediaControllerFactory2, broadcastDispatcher2, dumpManager, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter2, activityStarter2, smartspaceMediaDataProvider2, com.android.systemui.util.Utils.useMediaResumption(context2), com.android.systemui.util.Utils.useQsMediaPlayer(context2), systemClock2, tunerService2, mediaFlags2, mediaUiEventLogger);
    }

    public final void onNotificationAdded(@NotNull String str, @NotNull StatusBarNotification statusBarNotification) {
        String str2 = str;
        if (!this.useQsMediaPlayer || !MediaDataManagerKt.isMediaNotification(statusBarNotification)) {
            onNotificationRemoved(str);
            return;
        }
        boolean z = false;
        Assert.isMainThread();
        String findExistingEntry = findExistingEntry(str2, statusBarNotification.getPackageName());
        if (findExistingEntry == null) {
            this.mediaEntries.put(str2, MediaData.copy$default(MediaDataManagerKt.LOADING, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, statusBarNotification.getPackageName(), (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, this.logger.getNewInstanceId(), 0, 25164799, (Object) null));
        } else {
            if (!Intrinsics.areEqual((Object) findExistingEntry, (Object) str2)) {
                Object remove = this.mediaEntries.remove(findExistingEntry);
                Intrinsics.checkNotNull(remove);
                this.mediaEntries.put(str2, (MediaData) remove);
            }
            loadMediaData(str2, statusBarNotification, findExistingEntry, z);
        }
        z = true;
        loadMediaData(str2, statusBarNotification, findExistingEntry, z);
    }

    public final void removeAllForPackage(String str) {
        Assert.isMainThread();
        LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry next : linkedHashMap.entrySet()) {
            if (Intrinsics.areEqual((Object) ((MediaData) next.getValue()).getPackageName(), (Object) str)) {
                linkedHashMap2.put(next.getKey(), next.getValue());
            }
        }
        for (Map.Entry key : linkedHashMap2.entrySet()) {
            removeEntry((String) key.getKey());
        }
    }

    public final void setResumeAction(@NotNull String str, @Nullable Runnable runnable) {
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            mediaData.setResumeAction(runnable);
            mediaData.setHasCheckedForResume(true);
        }
    }

    public final void addResumptionControls(int i, @NotNull MediaDescription mediaDescription, @NotNull Runnable runnable, @NotNull MediaSession.Token token, @NotNull String str, @NotNull PendingIntent pendingIntent, @NotNull String str2) {
        int i2;
        String str3 = str2;
        if (!this.mediaEntries.containsKey(str3)) {
            InstanceId newInstanceId = this.logger.getNewInstanceId();
            try {
                ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(str3, 0);
                Integer valueOf = applicationInfo == null ? null : Integer.valueOf(applicationInfo.uid);
                Intrinsics.checkNotNull(valueOf);
                i2 = valueOf.intValue();
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("MediaDataManager", Intrinsics.stringPlus("Could not get app UID for ", str3), e);
                i2 = -1;
            }
            InstanceId instanceId = newInstanceId;
            this.mediaEntries.put(str3, MediaData.copy$default(MediaDataManagerKt.LOADING, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, str2, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, runnable, 0, false, (String) null, true, (Boolean) null, false, 0, instanceId, i2, 7830527, (Object) null));
            this.logger.logResumeMediaAdded(i2, str3, instanceId);
        }
        this.backgroundExecutor.execute(new MediaDataManager$addResumptionControls$1(this, i, mediaDescription, runnable, token, str, pendingIntent, str2));
    }

    public final String findExistingEntry(String str, String str2) {
        if (this.mediaEntries.containsKey(str)) {
            return str;
        }
        if (this.mediaEntries.containsKey(str2)) {
            return str2;
        }
        return null;
    }

    public final void loadMediaData(String str, StatusBarNotification statusBarNotification, String str2, boolean z) {
        this.backgroundExecutor.execute(new MediaDataManager$loadMediaData$1(this, str, statusBarNotification, str2, z));
    }

    public final void addListener(@NotNull Listener listener) {
        this.mediaDataFilter.addListener(listener);
    }

    public final void removeListener(@NotNull Listener listener) {
        this.mediaDataFilter.removeListener(listener);
    }

    public final boolean addInternalListener(Listener listener) {
        return this.internalListeners.add(listener);
    }

    public final void notifyMediaDataLoaded(String str, String str2, MediaData mediaData) {
        for (Listener onMediaDataLoaded$default : this.internalListeners) {
            Listener.DefaultImpls.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, false, 0, false, 56, (Object) null);
        }
    }

    public final void notifySmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData2) {
        for (Listener onSmartspaceMediaDataLoaded$default : this.internalListeners) {
            Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(onSmartspaceMediaDataLoaded$default, str, smartspaceMediaData2, false, 4, (Object) null);
        }
    }

    public final void notifyMediaDataRemoved(String str) {
        for (Listener onMediaDataRemoved : this.internalListeners) {
            onMediaDataRemoved.onMediaDataRemoved(str);
        }
    }

    public final void notifySmartspaceMediaDataRemoved(String str, boolean z) {
        for (Listener onSmartspaceMediaDataRemoved : this.internalListeners) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public static /* synthetic */ void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(MediaDataManager mediaDataManager, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 4) != 0) {
            z2 = false;
        }
        mediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(str, z, z2);
    }

    public final void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull String str, boolean z, boolean z2) {
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            if (z && !z2) {
                this.logger.logMediaTimeout(mediaData.getAppUid(), mediaData.getPackageName(), mediaData.getInstanceId());
            }
            if (mediaData.getActive() != (!z) || z2) {
                mediaData.setActive(!z);
                Log.d("MediaDataManager", "Updating " + str + " timedOut: " + z);
                onMediaDataLoaded(str, str, mediaData);
            } else if (mediaData.getResumption()) {
                Log.d("MediaDataManager", Intrinsics.stringPlus("timing out resume player ", str));
                dismissMediaData(str, 0);
            }
        }
    }

    public final void updateState(String str, PlaybackState playbackState) {
        String str2 = str;
        MediaData mediaData = this.mediaEntries.get(str2);
        if (mediaData != null) {
            if (mediaData.getToken() == null) {
                Log.d("MediaDataManager", "State updated, but token was null");
                return;
            }
            MediaData copy$default = MediaData.copy$default(mediaData, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, createActionsFromState(mediaData.getPackageName(), this.mediaControllerFactory.create(mediaData.getToken()), new UserHandle(mediaData.getUserId())), (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, Boolean.valueOf(NotificationMediaManager.isPlayingState(playbackState.getState())), false, 0, (InstanceId) null, 0, 32505343, (Object) null);
            Log.d("MediaDataManager", "State updated outside of notification");
            onMediaDataLoaded(str2, str2, copy$default);
        }
    }

    public final void removeEntry(String str) {
        MediaData mediaData = (MediaData) this.mediaEntries.remove(str);
        if (mediaData != null) {
            this.logger.logMediaRemoved(mediaData.getAppUid(), mediaData.getPackageName(), mediaData.getInstanceId());
        }
        notifyMediaDataRemoved(str);
    }

    public final boolean dismissMediaData(@NotNull String str, long j) {
        boolean z = this.mediaEntries.get(str) != null;
        this.backgroundExecutor.execute(new MediaDataManager$dismissMediaData$1(this, str));
        this.foregroundExecutor.executeDelayed(new MediaDataManager$dismissMediaData$2(this, str), j);
        return z;
    }

    public final void dismissSmartspaceRecommendation(@NotNull String str, long j) {
        if (Intrinsics.areEqual((Object) this.smartspaceMediaData.getTargetId(), (Object) str) && this.smartspaceMediaData.isValid()) {
            Log.d("MediaDataManager", "Dismissing Smartspace media target");
            if (this.smartspaceMediaData.isActive()) {
                this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), R$styleable.AppCompatTheme_windowNoTitle, (Object) null);
            }
            this.foregroundExecutor.executeDelayed(new MediaDataManager$dismissSmartspaceRecommendation$1(this), j);
        }
    }

    public final void loadMediaDataInBgForResumption(int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        int i2;
        String str3 = str2;
        if (TextUtils.isEmpty(mediaDescription.getTitle())) {
            Log.e("MediaDataManager", "Description incomplete");
            this.mediaEntries.remove(str3);
            return;
        }
        Log.d("MediaDataManager", "adding track for " + i + " from browser: " + mediaDescription);
        Bitmap iconBitmap = mediaDescription.getIconBitmap();
        if (iconBitmap == null && mediaDescription.getIconUri() != null) {
            Uri iconUri = mediaDescription.getIconUri();
            Intrinsics.checkNotNull(iconUri);
            iconBitmap = loadBitmapFromUri(iconUri);
        }
        InstanceId instanceId = null;
        Icon createWithBitmap = iconBitmap != null ? Icon.createWithBitmap(iconBitmap) : null;
        MediaData mediaData = this.mediaEntries.get(str3);
        if (mediaData != null) {
            instanceId = mediaData.getInstanceId();
        }
        if (instanceId == null) {
            instanceId = this.logger.getNewInstanceId();
        }
        InstanceId instanceId2 = instanceId;
        if (mediaData == null) {
            i2 = -1;
        } else {
            i2 = mediaData.getAppUid();
        }
        MediaAction resumeMediaAction = getResumeMediaAction(runnable);
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        DelayableExecutor delayableExecutor = this.foregroundExecutor;
        MediaDataManager$loadMediaDataInBgForResumption$1 mediaDataManager$loadMediaDataInBgForResumption$1 = r0;
        MediaDataManager$loadMediaDataInBgForResumption$1 mediaDataManager$loadMediaDataInBgForResumption$12 = new MediaDataManager$loadMediaDataInBgForResumption$1(this, str2, i, str, mediaDescription, createWithBitmap, resumeMediaAction, token, pendingIntent, runnable, elapsedRealtime, instanceId2, i2);
        delayableExecutor.execute(mediaDataManager$loadMediaDataInBgForResumption$1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:91:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01f3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void loadMediaDataInBg(@org.jetbrains.annotations.NotNull java.lang.String r32, @org.jetbrains.annotations.NotNull android.service.notification.StatusBarNotification r33, @org.jetbrains.annotations.Nullable java.lang.String r34, boolean r35) {
        /*
            r31 = this;
            r2 = r31
            r3 = r32
            r5 = r33
            android.app.Notification r0 = r33.getNotification()
            android.os.Bundle r0 = r0.extras
            java.lang.Class<android.media.session.MediaSession$Token> r1 = android.media.session.MediaSession.Token.class
            java.lang.String r4 = "android.mediaSession"
            java.lang.Object r0 = r0.getParcelable(r4, r1)
            r14 = r0
            android.media.session.MediaSession$Token r14 = (android.media.session.MediaSession.Token) r14
            if (r14 != 0) goto L_0x001a
            return
        L_0x001a:
            com.android.systemui.media.MediaControllerFactory r0 = r2.mediaControllerFactory
            android.media.session.MediaController r0 = r0.create(r14)
            android.media.MediaMetadata r1 = r0.getMetadata()
            android.app.Notification r15 = r33.getNotification()
            r4 = 0
            if (r1 != 0) goto L_0x002d
            r6 = r4
            goto L_0x0031
        L_0x002d:
            android.graphics.Bitmap r6 = r2.loadBitmapFromUri((android.media.MediaMetadata) r1)
        L_0x0031:
            if (r6 != 0) goto L_0x003d
            if (r1 != 0) goto L_0x0037
            r6 = r4
            goto L_0x003d
        L_0x0037:
            java.lang.String r6 = "android.media.metadata.ART"
            android.graphics.Bitmap r6 = r1.getBitmap(r6)
        L_0x003d:
            if (r6 != 0) goto L_0x0049
            if (r1 != 0) goto L_0x0043
            r6 = r4
            goto L_0x0049
        L_0x0043:
            java.lang.String r6 = "android.media.metadata.ALBUM_ART"
            android.graphics.Bitmap r6 = r1.getBitmap(r6)
        L_0x0049:
            if (r6 != 0) goto L_0x0050
            android.graphics.drawable.Icon r6 = r15.getLargeIcon()
            goto L_0x0054
        L_0x0050:
            android.graphics.drawable.Icon r6 = android.graphics.drawable.Icon.createWithBitmap(r6)
        L_0x0054:
            r10 = r6
            android.content.Context r6 = r2.context
            android.app.Notification$Builder r6 = android.app.Notification.Builder.recoverBuilder(r6, r15)
            java.lang.String r6 = r6.loadHeaderAppName()
            android.app.Notification r7 = r33.getNotification()
            android.graphics.drawable.Icon r7 = r7.getSmallIcon()
            kotlin.jvm.internal.Ref$ObjectRef r9 = new kotlin.jvm.internal.Ref$ObjectRef
            r9.<init>()
            if (r1 != 0) goto L_0x0070
            r8 = r4
            goto L_0x0076
        L_0x0070:
            java.lang.String r8 = "android.media.metadata.DISPLAY_TITLE"
            java.lang.String r8 = r1.getString(r8)
        L_0x0076:
            r9.element = r8
            if (r8 != 0) goto L_0x0086
            if (r1 != 0) goto L_0x007e
            r8 = r4
            goto L_0x0084
        L_0x007e:
            java.lang.String r8 = "android.media.metadata.TITLE"
            java.lang.String r8 = r1.getString(r8)
        L_0x0084:
            r9.element = r8
        L_0x0086:
            T r8 = r9.element
            if (r8 != 0) goto L_0x0090
            java.lang.CharSequence r8 = com.android.systemui.statusbar.notification.row.HybridGroupManager.resolveTitle(r15)
            r9.element = r8
        L_0x0090:
            kotlin.jvm.internal.Ref$ObjectRef r8 = new kotlin.jvm.internal.Ref$ObjectRef
            r8.<init>()
            if (r1 != 0) goto L_0x0099
            r1 = r4
            goto L_0x009f
        L_0x0099:
            java.lang.String r11 = "android.media.metadata.ARTIST"
            java.lang.String r1 = r1.getString(r11)
        L_0x009f:
            r8.element = r1
            if (r1 != 0) goto L_0x00a9
            java.lang.CharSequence r1 = com.android.systemui.statusbar.notification.row.HybridGroupManager.resolveText(r15)
            r8.element = r1
        L_0x00a9:
            kotlin.jvm.internal.Ref$ObjectRef r13 = new kotlin.jvm.internal.Ref$ObjectRef
            r13.<init>()
            boolean r1 = r2.isRemoteCastNotification(r5)
            java.lang.String r11 = "MediaDataManager"
            if (r1 == 0) goto L_0x0126
            android.app.Notification r1 = r33.getNotification()
            android.os.Bundle r1 = r1.extras
            java.lang.String r12 = "android.mediaRemoteDevice"
            java.lang.CharSequence r12 = r1.getCharSequence(r12, r4)
            java.lang.String r4 = "android.mediaRemoteIcon"
            r27 = r15
            r15 = -1
            int r4 = r1.getInt(r4, r15)
            java.lang.Class<android.app.PendingIntent> r15 = android.app.PendingIntent.class
            r28 = r14
            java.lang.String r14 = "android.mediaRemoteIntent"
            java.lang.Object r1 = r1.getParcelable(r14, r15)
            r23 = r1
            android.app.PendingIntent r23 = (android.app.PendingIntent) r23
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r3)
            java.lang.String r14 = " is RCN for "
            r1.append(r14)
            r1.append(r12)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r11, r1)
            r1 = -1
            if (r12 == 0) goto L_0x012b
            if (r4 <= r1) goto L_0x012b
            if (r23 == 0) goto L_0x0100
            boolean r14 = r23.isActivity()
            if (r14 == 0) goto L_0x0100
            r20 = 1
            goto L_0x0102
        L_0x0100:
            r20 = 0
        L_0x0102:
            java.lang.String r14 = r33.getPackageName()
            android.graphics.drawable.Icon r4 = android.graphics.drawable.Icon.createWithResource(r14, r4)
            android.content.Context r14 = r2.context
            android.content.Context r14 = r5.getPackageContext(r14)
            android.graphics.drawable.Drawable r21 = r4.loadDrawable(r14)
            com.android.systemui.media.MediaDeviceData r4 = new com.android.systemui.media.MediaDeviceData
            r24 = 0
            r25 = 16
            r26 = 0
            r19 = r4
            r22 = r12
            r19.<init>(r20, r21, r22, r23, r24, r25, r26)
            r13.element = r4
            goto L_0x012b
        L_0x0126:
            r28 = r14
            r27 = r15
            r1 = -1
        L_0x012b:
            kotlin.jvm.internal.Ref$ObjectRef r12 = new kotlin.jvm.internal.Ref$ObjectRef
            r12.<init>()
            java.util.List r4 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
            r12.element = r4
            kotlin.jvm.internal.Ref$ObjectRef r14 = new kotlin.jvm.internal.Ref$ObjectRef
            r14.<init>()
            java.util.List r4 = kotlin.collections.CollectionsKt__CollectionsKt.emptyList()
            r14.element = r4
            java.lang.String r4 = r33.getPackageName()
            android.os.UserHandle r15 = r33.getUser()
            com.android.systemui.media.MediaButton r15 = r2.createActionsFromState(r4, r0, r15)
            if (r15 != 0) goto L_0x015f
            kotlin.Pair r4 = r2.createActionsFromNotification(r5)
            java.lang.Object r1 = r4.getFirst()
            r12.element = r1
            java.lang.Object r1 = r4.getSecond()
            r14.element = r1
        L_0x015f:
            boolean r1 = r2.isRemoteCastNotification(r5)
            if (r1 == 0) goto L_0x0168
            r1 = 2
            r4 = 1
            goto L_0x0180
        L_0x0168:
            android.media.session.MediaController$PlaybackInfo r1 = r0.getPlaybackInfo()
            if (r1 != 0) goto L_0x0171
            r1 = 0
            r4 = 1
            goto L_0x017b
        L_0x0171:
            int r1 = r1.getPlaybackType()
            r4 = 1
            if (r1 != r4) goto L_0x017a
            r1 = r4
            goto L_0x017b
        L_0x017a:
            r1 = 0
        L_0x017b:
            if (r1 == 0) goto L_0x017f
            r1 = 0
            goto L_0x0180
        L_0x017f:
            r1 = r4
        L_0x0180:
            android.media.session.PlaybackState r0 = r0.getPlaybackState()
            if (r0 != 0) goto L_0x0189
            r21 = 0
            goto L_0x0197
        L_0x0189:
            int r0 = r0.getState()
            boolean r0 = com.android.systemui.statusbar.NotificationMediaManager.isPlayingState(r0)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r21 = r0
        L_0x0197:
            java.util.LinkedHashMap<java.lang.String, com.android.systemui.media.MediaData> r0 = r2.mediaEntries
            java.lang.Object r0 = r0.get(r3)
            r17 = r0
            com.android.systemui.media.MediaData r17 = (com.android.systemui.media.MediaData) r17
            if (r17 != 0) goto L_0x01a5
            r0 = 0
            goto L_0x01a9
        L_0x01a5:
            com.android.internal.logging.InstanceId r0 = r17.getInstanceId()
        L_0x01a9:
            if (r0 != 0) goto L_0x01b1
            com.android.systemui.media.MediaUiEventLogger r0 = r2.logger
            com.android.internal.logging.InstanceId r0 = r0.getNewInstanceId()
        L_0x01b1:
            r22 = r13
            r13 = r0
            android.content.Context r0 = r2.context     // Catch:{ NameNotFoundException -> 0x01d7 }
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch:{ NameNotFoundException -> 0x01d7 }
            java.lang.String r4 = r33.getPackageName()     // Catch:{ NameNotFoundException -> 0x01d7 }
            r3 = 0
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo(r4, r3)     // Catch:{ NameNotFoundException -> 0x01d5 }
            if (r0 != 0) goto L_0x01c7
            r4 = 0
            goto L_0x01cd
        L_0x01c7:
            int r0 = r0.uid     // Catch:{ NameNotFoundException -> 0x01d5 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r0)     // Catch:{ NameNotFoundException -> 0x01d5 }
        L_0x01cd:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)     // Catch:{ NameNotFoundException -> 0x01d5 }
            int r0 = r4.intValue()     // Catch:{ NameNotFoundException -> 0x01d5 }
            goto L_0x01e7
        L_0x01d5:
            r0 = move-exception
            goto L_0x01d9
        L_0x01d7:
            r0 = move-exception
            r3 = 0
        L_0x01d9:
            java.lang.String r4 = r33.getPackageName()
            java.lang.String r3 = "Could not get app UID for "
            java.lang.String r3 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r4)
            android.util.Log.w(r11, r3, r0)
            r0 = -1
        L_0x01e7:
            if (r35 == 0) goto L_0x01f3
            com.android.systemui.media.MediaUiEventLogger r3 = r2.logger
            java.lang.String r4 = r33.getPackageName()
            r3.logActiveMediaAdded(r0, r4, r13, r1)
            goto L_0x020b
        L_0x01f3:
            if (r17 != 0) goto L_0x01f8
        L_0x01f5:
            r18 = 0
            goto L_0x0200
        L_0x01f8:
            int r3 = r17.getPlaybackLocation()
            if (r1 != r3) goto L_0x01f5
            r18 = 1
        L_0x0200:
            if (r18 != 0) goto L_0x020b
            com.android.systemui.media.MediaUiEventLogger r3 = r2.logger
            java.lang.String r4 = r33.getPackageName()
            r3.logPlaybackLocationChange(r0, r4, r13, r1)
        L_0x020b:
            com.android.systemui.util.time.SystemClock r3 = r2.systemClock
            long r19 = r3.elapsedRealtime()
            com.android.systemui.util.concurrency.DelayableExecutor r11 = r2.foregroundExecutor
            com.android.systemui.media.MediaDataManager$loadMediaDataInBg$1 r4 = new com.android.systemui.media.MediaDataManager$loadMediaDataInBg$1
            r18 = r1
            r1 = r4
            r2 = r31
            r3 = r32
            r29 = r4
            r4 = r34
            r5 = r33
            r30 = r11
            r11 = r12
            r12 = r14
            r16 = r22
            r22 = r13
            r13 = r15
            r14 = r28
            r15 = r27
            r17 = r18
            r18 = r21
            r21 = r22
            r22 = r0
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r21, r22)
            r1 = r29
            r0 = r30
            r0.execute(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataManager.loadMediaDataInBg(java.lang.String, android.service.notification.StatusBarNotification, java.lang.String, boolean):void");
    }

    public final Pair<List<MediaAction>, List<Integer>> createActionsFromNotification(StatusBarNotification statusBarNotification) {
        List list;
        Icon icon;
        Notification notification = statusBarNotification.getNotification();
        ArrayList arrayList = new ArrayList();
        Notification.Action[] actionArr = notification.actions;
        int[] intArray = notification.extras.getIntArray("android.compactActions");
        if (intArray == null) {
            list = null;
        } else {
            list = ArraysKt___ArraysKt.toMutableList(intArray);
        }
        if (list == null) {
            list = new ArrayList();
        }
        int size = list.size();
        int i = MAX_COMPACT_ACTIONS;
        if (size > i) {
            Log.e("MediaDataManager", "Too many compact actions for " + statusBarNotification.getKey() + ",limiting to first " + i);
            list = list.subList(0, i);
        }
        if (actionArr != null) {
            int length = actionArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Notification.Action action = actionArr[i2];
                int i3 = i2 + 1;
                int i4 = MAX_NOTIFICATION_ACTIONS;
                if (i2 == i4) {
                    Log.w("MediaDataManager", "Too many notification actions for " + statusBarNotification.getKey() + ", limiting to first " + i4);
                    break;
                }
                if (action.getIcon() == null) {
                    Log.i("MediaDataManager", "No icon for action " + i2 + ' ' + action.title);
                    list.remove(Integer.valueOf(i2));
                } else {
                    MediaDataManager$createActionsFromNotification$runnable$1 mediaDataManager$createActionsFromNotification$runnable$1 = action.actionIntent != null ? new MediaDataManager$createActionsFromNotification$runnable$1(action, this) : null;
                    Icon icon2 = action.getIcon();
                    if (icon2 != null && icon2.getType() == 2) {
                        String packageName = statusBarNotification.getPackageName();
                        Icon icon3 = action.getIcon();
                        Intrinsics.checkNotNull(icon3);
                        icon = Icon.createWithResource(packageName, icon3.getResId());
                    } else {
                        icon = action.getIcon();
                    }
                    arrayList.add(new MediaAction(icon.setTint(this.themeText).loadDrawable(this.context), mediaDataManager$createActionsFromNotification$runnable$1, action.title, (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null));
                }
                i2 = i3;
            }
        }
        return new Pair<>(arrayList, list);
    }

    public final MediaButton createActionsFromState(String str, MediaController mediaController, UserHandle userHandle) {
        MediaAction mediaAction;
        MediaAction mediaAction2;
        MediaAction mediaAction3;
        MediaAction mediaAction4;
        PlaybackState playbackState = mediaController.getPlaybackState();
        MediaAction mediaAction5 = null;
        if (playbackState == null || !this.mediaFlags.areMediaSessionActionsEnabled(str, userHandle)) {
            return null;
        }
        if (NotificationMediaManager.isConnectingState(playbackState.getState())) {
            Drawable drawable = this.context.getDrawable(17303369);
            if (drawable != null) {
                ((Animatable) drawable).start();
                mediaAction = new MediaAction(drawable, (Runnable) null, this.context.getString(R$string.controls_media_button_connecting), this.context.getDrawable(R$drawable.ic_media_connecting_container), 17303369);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.Animatable");
            }
        } else {
            if (NotificationMediaManager.isPlayingState(playbackState.getState())) {
                mediaAction4 = getStandardAction(mediaController, playbackState.getActions(), 2);
            } else {
                mediaAction4 = getStandardAction(mediaController, playbackState.getActions(), 4);
            }
            mediaAction = mediaAction4;
        }
        MediaController mediaController2 = mediaController;
        MediaAction standardAction = getStandardAction(mediaController2, playbackState.getActions(), 16);
        MediaAction standardAction2 = getStandardAction(mediaController2, playbackState.getActions(), 32);
        Iterator it = SequencesKt___SequencesKt.map(SequencesKt___SequencesKt.filterNotNull(CollectionsKt___CollectionsKt.asSequence(playbackState.getCustomActions())), new MediaDataManager$createActionsFromState$customActions$1(this, playbackState, str, mediaController)).iterator();
        Bundle extras = mediaController.getExtras();
        boolean z = extras != null && extras.getBoolean("android.media.playback.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS");
        Bundle extras2 = mediaController.getExtras();
        boolean z2 = extras2 != null && extras2.getBoolean("android.media.playback.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT");
        if (standardAction != null) {
            mediaAction2 = standardAction;
        } else {
            mediaAction2 = !z ? createActionsFromState$nextCustomAction(it) : null;
        }
        if (standardAction2 != null) {
            mediaAction3 = standardAction2;
        } else {
            if (!z2) {
                mediaAction5 = createActionsFromState$nextCustomAction(it);
            }
            mediaAction3 = mediaAction5;
        }
        return new MediaButton(mediaAction, mediaAction3, mediaAction2, createActionsFromState$nextCustomAction(it), createActionsFromState$nextCustomAction(it), z2, z);
    }

    public static final MediaAction createActionsFromState$nextCustomAction(Iterator<MediaAction> it) {
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public final MediaAction getStandardAction(MediaController mediaController, long j, long j2) {
        if (!includesAction(j, j2)) {
            return null;
        }
        if (j2 == 4) {
            return new MediaAction(this.context.getDrawable(R$drawable.ic_media_play), new MediaDataManager$getStandardAction$1(mediaController), this.context.getString(R$string.controls_media_button_play), this.context.getDrawable(R$drawable.ic_media_play_container), (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 2) {
            return new MediaAction(this.context.getDrawable(R$drawable.ic_media_pause), new MediaDataManager$getStandardAction$2(mediaController), this.context.getString(R$string.controls_media_button_pause), this.context.getDrawable(R$drawable.ic_media_pause_container), (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 16) {
            return new MediaAction(this.context.getDrawable(R$drawable.ic_media_prev), new MediaDataManager$getStandardAction$3(mediaController), this.context.getString(R$string.controls_media_button_prev), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 32) {
            return new MediaAction(this.context.getDrawable(R$drawable.ic_media_next), new MediaDataManager$getStandardAction$4(mediaController), this.context.getString(R$string.controls_media_button_next), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        return null;
    }

    public final MediaAction getCustomAction(PlaybackState playbackState, String str, MediaController mediaController, PlaybackState.CustomAction customAction) {
        return new MediaAction(Icon.createWithResource(str, customAction.getIcon()).loadDrawable(this.context), new MediaDataManager$getCustomAction$1(mediaController, customAction), customAction.getName(), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
    }

    public final Bitmap loadBitmapFromUri(MediaMetadata mediaMetadata) {
        Bitmap loadBitmapFromUri;
        String[] access$getART_URIS$p = MediaDataManagerKt.ART_URIS;
        int length = access$getART_URIS$p.length;
        int i = 0;
        while (i < length) {
            String str = access$getART_URIS$p[i];
            i++;
            String string = mediaMetadata.getString(str);
            if (!TextUtils.isEmpty(string) && (loadBitmapFromUri = loadBitmapFromUri(Uri.parse(string))) != null) {
                Log.d("MediaDataManager", Intrinsics.stringPlus("loaded art from ", str));
                return loadBitmapFromUri;
            }
        }
        return null;
    }

    public final boolean sendPendingIntent(PendingIntent pendingIntent) {
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            Log.d("MediaDataManager", "Intent canceled", e);
            return false;
        }
    }

    public final Bitmap loadBitmapFromUri(Uri uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        if (!uri.getScheme().equals("content") && !uri.getScheme().equals("android.resource") && !uri.getScheme().equals("file")) {
            return null;
        }
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.context.getContentResolver(), uri), MediaDataManager$loadBitmapFromUri$1.INSTANCE);
        } catch (IOException e) {
            Log.e("MediaDataManager", "Unable to load bitmap", e);
            return null;
        } catch (RuntimeException e2) {
            Log.e("MediaDataManager", "Unable to load bitmap", e2);
            return null;
        }
    }

    public final MediaAction getResumeMediaAction(Runnable runnable) {
        return new MediaAction(Icon.createWithResource(this.context, R$drawable.ic_media_play).setTint(this.themeText).loadDrawable(this.context), runnable, this.context.getString(R$string.controls_media_resume), this.context.getDrawable(R$drawable.ic_media_play_container), (Integer) null, 16, (DefaultConstructorMarker) null);
    }

    public void onSmartspaceTargetsUpdated(@NotNull List<? extends Parcelable> list) {
        if (!this.allowMediaRecommendations) {
            Log.d("MediaDataManager", "Smartspace recommendation is disabled in Settings.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            if (next instanceof SmartspaceTarget) {
                arrayList.add(next);
            }
        }
        int size = arrayList.size();
        if (size != 0) {
            if (size != 1) {
                Log.wtf("MediaDataManager", "More than 1 Smartspace Media Update. Resetting the status...");
                notifySmartspaceMediaDataRemoved(this.smartspaceMediaData.getTargetId(), false);
                this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
                return;
            }
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) arrayList.get(0);
            if (!Intrinsics.areEqual((Object) this.smartspaceMediaData.getTargetId(), (Object) smartspaceTarget.getSmartspaceTargetId())) {
                Log.d("MediaDataManager", "Forwarding Smartspace media update.");
                SmartspaceMediaData smartspaceMediaData2 = toSmartspaceMediaData(smartspaceTarget, true);
                this.smartspaceMediaData = smartspaceMediaData2;
                notifySmartspaceMediaDataLoaded(smartspaceMediaData2.getTargetId(), this.smartspaceMediaData);
            }
        } else if (this.smartspaceMediaData.isActive()) {
            Log.d("MediaDataManager", "Set Smartspace media to be inactive for the data update");
            SmartspaceMediaData copy$default = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), R$styleable.AppCompatTheme_windowNoTitle, (Object) null);
            this.smartspaceMediaData = copy$default;
            notifySmartspaceMediaDataRemoved(copy$default.getTargetId(), false);
        }
    }

    public final void onNotificationRemoved(@NotNull String str) {
        String str2 = str;
        Assert.isMainThread();
        MediaData mediaData = (MediaData) this.mediaEntries.remove(str2);
        if (this.useMediaResumption) {
            Boolean bool = null;
            if ((mediaData == null ? null : mediaData.getResumeAction()) != null) {
                if (mediaData != null) {
                    bool = Boolean.valueOf(mediaData.isLocalSession());
                }
                if (bool.booleanValue()) {
                    Log.d("MediaDataManager", "Not removing " + str2 + " because resumable");
                    Runnable resumeAction = mediaData.getResumeAction();
                    Intrinsics.checkNotNull(resumeAction);
                    MediaAction resumeMediaAction = getResumeMediaAction(resumeAction);
                    List listOf = CollectionsKt__CollectionsJVMKt.listOf(resumeMediaAction);
                    MediaButton mediaButton = r14;
                    MediaButton mediaButton2 = new MediaButton(resumeMediaAction, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, R$styleable.AppCompatTheme_windowNoTitle, (DefaultConstructorMarker) null);
                    boolean z = false;
                    MediaData copy$default = MediaData.copy$default(mediaData, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, listOf, CollectionsKt__CollectionsJVMKt.listOf(0), mediaButton, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, true, (String) null, false, Boolean.FALSE, true, 0, (InstanceId) null, 0, 30258303, (Object) null);
                    String packageName = mediaData.getPackageName();
                    if (this.mediaEntries.put(packageName, copy$default) == null) {
                        z = true;
                    }
                    if (z) {
                        notifyMediaDataLoaded(packageName, str2, copy$default);
                    } else {
                        notifyMediaDataRemoved(str);
                        notifyMediaDataLoaded(packageName, packageName, copy$default);
                    }
                    this.logger.logActiveConvertedToResume(copy$default.getAppUid(), packageName, copy$default.getInstanceId());
                    return;
                }
            }
        }
        if (mediaData != null) {
            notifyMediaDataRemoved(str);
            this.logger.logMediaRemoved(mediaData.getAppUid(), mediaData.getPackageName(), mediaData.getInstanceId());
        }
    }

    public final void setMediaResumptionEnabled(boolean z) {
        if (this.useMediaResumption != z) {
            this.useMediaResumption = z;
            if (!z) {
                LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                for (Map.Entry next : linkedHashMap.entrySet()) {
                    if (!((MediaData) next.getValue()).getActive()) {
                        linkedHashMap2.put(next.getKey(), next.getValue());
                    }
                }
                for (Map.Entry entry : linkedHashMap2.entrySet()) {
                    this.mediaEntries.remove(entry.getKey());
                    notifyMediaDataRemoved((String) entry.getKey());
                    this.logger.logMediaRemoved(((MediaData) entry.getValue()).getAppUid(), ((MediaData) entry.getValue()).getPackageName(), ((MediaData) entry.getValue()).getInstanceId());
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        this.mediaDataFilter.onSwipeToDismiss();
    }

    public final boolean hasActiveMediaOrRecommendation() {
        return this.mediaDataFilter.hasActiveMediaOrRecommendation();
    }

    public final boolean hasAnyMediaOrRecommendation() {
        return this.mediaDataFilter.hasAnyMediaOrRecommendation();
    }

    public final boolean hasActiveMedia() {
        return this.mediaDataFilter.hasActiveMedia();
    }

    public final boolean hasAnyMedia() {
        return this.mediaDataFilter.hasAnyMedia();
    }

    /* compiled from: MediaDataManager.kt */
    public interface Listener {
        void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2);

        void onMediaDataRemoved(@NotNull String str);

        void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z);

        void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z);

        /* compiled from: MediaDataManager.kt */
        public static final class DefaultImpls {
            public static void onMediaDataRemoved(@NotNull Listener listener, @NotNull String str) {
            }

            public static void onSmartspaceMediaDataLoaded(@NotNull Listener listener, @NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            public static void onSmartspaceMediaDataRemoved(@NotNull Listener listener, @NotNull String str, boolean z) {
            }

            public static /* synthetic */ void onMediaDataLoaded$default(Listener listener, String str, String str2, MediaData mediaData, boolean z, int i, boolean z2, int i2, Object obj) {
                if (obj == null) {
                    if ((i2 & 8) != 0) {
                        z = true;
                    }
                    listener.onMediaDataLoaded(str, str2, mediaData, z, (i2 & 16) != 0 ? 0 : i, (i2 & 32) != 0 ? false : z2);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onMediaDataLoaded");
            }

            public static /* synthetic */ void onSmartspaceMediaDataLoaded$default(Listener listener, String str, SmartspaceMediaData smartspaceMediaData, boolean z, int i, Object obj) {
                if (obj == null) {
                    if ((i & 4) != 0) {
                        z = false;
                    }
                    listener.onSmartspaceMediaDataLoaded(str, smartspaceMediaData, z);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSmartspaceMediaDataLoaded");
            }
        }
    }

    public final SmartspaceMediaData toSmartspaceMediaData(SmartspaceTarget smartspaceTarget, boolean z) {
        Intent intent = (smartspaceTarget.getBaseAction() == null || smartspaceTarget.getBaseAction().getExtras() == null) ? null : (Intent) smartspaceTarget.getBaseAction().getExtras().getParcelable("dismiss_intent");
        String packageName = packageName(smartspaceTarget);
        if (packageName == null) {
            return SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), smartspaceTarget.getSmartspaceTargetId(), z, (String) null, (SmartspaceAction) null, (List) null, intent, smartspaceTarget.getCreationTimeMillis(), this.logger.getNewInstanceId(), 28, (Object) null);
        }
        return new SmartspaceMediaData(smartspaceTarget.getSmartspaceTargetId(), z, packageName, smartspaceTarget.getBaseAction(), smartspaceTarget.getIconGrid(), intent, smartspaceTarget.getCreationTimeMillis(), this.logger.getNewInstanceId());
    }

    public final String packageName(SmartspaceTarget smartspaceTarget) {
        String string;
        List<SmartspaceAction> iconGrid = smartspaceTarget.getIconGrid();
        if (iconGrid == null || iconGrid.isEmpty()) {
            Log.w("MediaDataManager", "Empty or null media recommendation list.");
            return null;
        }
        for (SmartspaceAction extras : iconGrid) {
            Bundle extras2 = extras.getExtras();
            if (extras2 != null && (string = extras2.getString(EXTRAS_MEDIA_SOURCE_PACKAGE_NAME)) != null) {
                return string;
            }
        }
        Log.w("MediaDataManager", "No valid package name is provided.");
        return null;
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("internalListeners: ", this.internalListeners));
        printWriter.println(Intrinsics.stringPlus("externalListeners: ", this.mediaDataFilter.getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        printWriter.println(Intrinsics.stringPlus("mediaEntries: ", this.mediaEntries));
        printWriter.println(Intrinsics.stringPlus("useMediaResumption: ", Boolean.valueOf(this.useMediaResumption)));
    }
}
