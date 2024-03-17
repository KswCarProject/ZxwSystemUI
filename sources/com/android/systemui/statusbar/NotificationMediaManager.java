package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.widget.ImageView;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class NotificationMediaManager implements Dumpable {
    public static final HashSet<Integer> CONNECTING_MEDIA_STATES;
    public static final HashSet<Integer> PAUSED_MEDIA_STATES;
    public BackDropView mBackdrop;
    public ImageView mBackdropBack;
    public ImageView mBackdropFront;
    public BiometricUnlockController mBiometricUnlockController;
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final SysuiColorExtractor mColorExtractor = ((SysuiColorExtractor) Dependency.get(SysuiColorExtractor.class));
    public final Context mContext;
    public final NotificationEntryManager mEntryManager;
    public final Runnable mHideBackdropFront = new Runnable() {
        public void run() {
            NotificationMediaManager.this.mBackdropFront.setVisibility(4);
            NotificationMediaManager.this.mBackdropFront.animate().cancel();
            NotificationMediaManager.this.mBackdropFront.setImageDrawable((Drawable) null);
        }
    };
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardStateController mKeyguardStateController = ((KeyguardStateController) Dependency.get(KeyguardStateController.class));
    public LockscreenWallpaper mLockscreenWallpaper;
    public final DelayableExecutor mMainExecutor;
    public final MediaArtworkProcessor mMediaArtworkProcessor;
    public MediaController mMediaController;
    public final MediaDataManager mMediaDataManager;
    public final MediaController.Callback mMediaListener = new MediaController.Callback() {
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            super.onPlaybackStateChanged(playbackState);
            if (playbackState != null) {
                if (!NotificationMediaManager.this.isPlaybackActive(playbackState.getState())) {
                    NotificationMediaManager.this.clearCurrentMediaNotification();
                }
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }
        }

        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            super.onMetadataChanged(mediaMetadata);
            NotificationMediaManager.this.mMediaArtworkProcessor.clearCache();
            NotificationMediaManager.this.mMediaMetadata = mediaMetadata;
            NotificationMediaManager.this.dispatchUpdateMediaMetaData(true, true);
        }
    };
    public final ArrayList<MediaListener> mMediaListeners;
    public MediaMetadata mMediaMetadata;
    public String mMediaNotificationKey;
    public final NotifCollection mNotifCollection;
    public final NotifPipeline mNotifPipeline;
    public Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    public NotificationPresenter mPresenter;
    public final Set<AsyncTask<?, ?, ?>> mProcessArtworkTasks = new ArraySet();
    public ScrimController mScrimController;
    public final StatusBarStateController mStatusBarStateController = ((StatusBarStateController) Dependency.get(StatusBarStateController.class));
    public final boolean mUsingNotifPipeline;
    public final NotificationVisibilityProvider mVisibilityProvider;

    public interface MediaListener {
        void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        }
    }

    public final boolean isPlaybackActive(int i) {
        return (i == 1 || i == 7 || i == 0) ? false : true;
    }

    static {
        HashSet<Integer> hashSet = new HashSet<>();
        PAUSED_MEDIA_STATES = hashSet;
        HashSet<Integer> hashSet2 = new HashSet<>();
        CONNECTING_MEDIA_STATES = hashSet2;
        hashSet.add(0);
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(7);
        hashSet2.add(8);
        hashSet2.add(6);
    }

    public NotificationMediaManager(Context context, Lazy<Optional<CentralSurfaces>> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, NotifPipelineFlags notifPipelineFlags, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, DumpManager dumpManager) {
        this.mContext = context;
        this.mMediaArtworkProcessor = mediaArtworkProcessor;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mMediaListeners = new ArrayList<>();
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mNotificationShadeWindowController = lazy2;
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mEntryManager = notificationEntryManager;
        this.mMainExecutor = delayableExecutor;
        this.mMediaDataManager = mediaDataManager;
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        if (!notifPipelineFlags.isNewPipelineEnabled()) {
            setupNEM();
            this.mUsingNotifPipeline = false;
        } else {
            setupNotifPipeline();
            this.mUsingNotifPipeline = true;
        }
        dumpManager.registerDumpable(this);
    }

    public final void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() {
            public void onEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            public void onEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            public void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }

            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new MediaDataManager.Listener() {
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
            }

            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
            }

            public void onMediaDataRemoved(String str) {
                NotificationMediaManager.this.mNotifPipeline.getAllNotifs().stream().filter(new NotificationMediaManager$3$$ExternalSyntheticLambda0(str)).findAny().ifPresent(new NotificationMediaManager$3$$ExternalSyntheticLambda1(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onMediaDataRemoved$1(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mNotifCollection.dismissNotification(notificationEntry, NotificationMediaManager.this.getDismissedByUserStats(notificationEntry));
            }
        });
    }

    public final void setupNEM() {
        this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() {
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            public void onEntryInflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            public void onEntryReinflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mEntryManager.addCollectionListener(new NotifCollectionListener() {
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new MediaDataManager.Listener() {
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
            }

            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
            }

            public void onMediaDataRemoved(String str) {
                NotificationEntry pendingOrActiveNotif = NotificationMediaManager.this.mEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    NotificationMediaManager.this.mEntryManager.performRemoveNotification(pendingOrActiveNotif.getSbn(), NotificationMediaManager.this.getDismissedByUserStats(pendingOrActiveNotif), 2);
                }
            }
        });
    }

    public final DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry) {
        return new DismissedByUserStats(3, 1, this.mVisibilityProvider.obtain(notificationEntry, true));
    }

    public final void removeEntry(NotificationEntry notificationEntry) {
        onNotificationRemoved(notificationEntry.getKey());
        this.mMediaDataManager.onNotificationRemoved(notificationEntry.getKey());
    }

    public static boolean isPlayingState(int i) {
        return !PAUSED_MEDIA_STATES.contains(Integer.valueOf(i)) && !CONNECTING_MEDIA_STATES.contains(Integer.valueOf(i));
    }

    public static boolean isConnectingState(int i) {
        return CONNECTING_MEDIA_STATES.contains(Integer.valueOf(i));
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
    }

    public void onNotificationRemoved(String str) {
        if (str.equals(this.mMediaNotificationKey)) {
            clearCurrentMediaNotification();
            dispatchUpdateMediaMetaData(true, true);
        }
    }

    public String getMediaNotificationKey() {
        return this.mMediaNotificationKey;
    }

    public MediaMetadata getMediaMetadata() {
        return this.mMediaMetadata;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Icon getMediaIcon() {
        /*
            r3 = this;
            java.lang.String r0 = r3.mMediaNotificationKey
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            boolean r2 = r3.mUsingNotifPipeline
            if (r2 == 0) goto L_0x002d
            com.android.systemui.statusbar.notification.collection.NotifPipeline r3 = r3.mNotifPipeline
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.getEntry(r0)
            java.util.Optional r3 = java.util.Optional.ofNullable(r3)
            com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda0 r0 = new com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda0
            r0.<init>()
            java.util.Optional r3 = r3.map(r0)
            com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda1 r0 = new com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda1
            r0.<init>()
            java.util.Optional r3 = r3.map(r0)
            java.lang.Object r3 = r3.orElse(r1)
            android.graphics.drawable.Icon r3 = (android.graphics.drawable.Icon) r3
            return r3
        L_0x002d:
            com.android.systemui.statusbar.notification.NotificationEntryManager r0 = r3.mEntryManager
            monitor-enter(r0)
            com.android.systemui.statusbar.notification.NotificationEntryManager r2 = r3.mEntryManager     // Catch:{ all -> 0x0055 }
            java.lang.String r3 = r3.mMediaNotificationKey     // Catch:{ all -> 0x0055 }
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r2.getActiveNotificationUnfiltered(r3)     // Catch:{ all -> 0x0055 }
            if (r3 == 0) goto L_0x0053
            com.android.systemui.statusbar.notification.icon.IconPack r2 = r3.getIcons()     // Catch:{ all -> 0x0055 }
            com.android.systemui.statusbar.StatusBarIconView r2 = r2.getShelfIcon()     // Catch:{ all -> 0x0055 }
            if (r2 != 0) goto L_0x0045
            goto L_0x0053
        L_0x0045:
            com.android.systemui.statusbar.notification.icon.IconPack r3 = r3.getIcons()     // Catch:{ all -> 0x0055 }
            com.android.systemui.statusbar.StatusBarIconView r3 = r3.getShelfIcon()     // Catch:{ all -> 0x0055 }
            android.graphics.drawable.Icon r3 = r3.getSourceIcon()     // Catch:{ all -> 0x0055 }
            monitor-exit(r0)     // Catch:{ all -> 0x0055 }
            return r3
        L_0x0053:
            monitor-exit(r0)     // Catch:{ all -> 0x0055 }
            return r1
        L_0x0055:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0055 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationMediaManager.getMediaIcon():android.graphics.drawable.Icon");
    }

    public void addCallback(MediaListener mediaListener) {
        this.mMediaListeners.add(mediaListener);
        mediaListener.onPrimaryMetadataOrStateChanged(this.mMediaMetadata, getMediaControllerPlaybackState(this.mMediaController));
    }

    public void findAndUpdateMediaNotifications() {
        boolean z;
        boolean findPlayingMediaNotification;
        if (this.mUsingNotifPipeline) {
            z = findPlayingMediaNotification(this.mNotifPipeline.getAllNotifs());
        } else {
            synchronized (this.mEntryManager) {
                findPlayingMediaNotification = findPlayingMediaNotification(this.mEntryManager.getAllNotifs());
            }
            if (findPlayingMediaNotification) {
                this.mEntryManager.updateNotifications("NotificationMediaManager - metaDataChanged");
            }
            z = findPlayingMediaNotification;
        }
        dispatchUpdateMediaMetaData(z, true);
    }

    public boolean findPlayingMediaNotification(Collection<NotificationEntry> collection) {
        MediaController mediaController;
        NotificationEntry notificationEntry;
        boolean z;
        MediaSession.Token token;
        Iterator<NotificationEntry> it = collection.iterator();
        while (true) {
            mediaController = null;
            if (!it.hasNext()) {
                notificationEntry = null;
                break;
            }
            NotificationEntry next = it.next();
            if (next.getSbn().getNotification().isMediaNotification() && (token = (MediaSession.Token) next.getSbn().getNotification().extras.getParcelable("android.mediaSession", MediaSession.Token.class)) != null) {
                MediaController mediaController2 = new MediaController(this.mContext, token);
                if (3 == getMediaControllerPlaybackState(mediaController2)) {
                    notificationEntry = next;
                    mediaController = mediaController2;
                    break;
                }
            }
        }
        if (mediaController == null || sameSessions(this.mMediaController, mediaController)) {
            z = false;
        } else {
            clearCurrentMediaNotificationSession();
            this.mMediaController = mediaController;
            mediaController.registerCallback(this.mMediaListener);
            this.mMediaMetadata = this.mMediaController.getMetadata();
            z = true;
        }
        if (notificationEntry != null && !notificationEntry.getSbn().getKey().equals(this.mMediaNotificationKey)) {
            this.mMediaNotificationKey = notificationEntry.getSbn().getKey();
        }
        return z;
    }

    public void clearCurrentMediaNotification() {
        this.mMediaNotificationKey = null;
        clearCurrentMediaNotificationSession();
    }

    public final void dispatchUpdateMediaMetaData(boolean z, boolean z2) {
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            notificationPresenter.updateMediaMetaData(z, z2);
        }
        int mediaControllerPlaybackState = getMediaControllerPlaybackState(this.mMediaController);
        ArrayList arrayList = new ArrayList(this.mMediaListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((MediaListener) arrayList.get(i)).onPrimaryMetadataOrStateChanged(this.mMediaMetadata, mediaControllerPlaybackState);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("    mMediaNotificationKey=");
        printWriter.println(this.mMediaNotificationKey);
        printWriter.print("    mMediaController=");
        printWriter.print(this.mMediaController);
        if (this.mMediaController != null) {
            printWriter.print(" state=" + this.mMediaController.getPlaybackState());
        }
        printWriter.println();
        printWriter.print("    mMediaMetadata=");
        printWriter.print(this.mMediaMetadata);
        if (this.mMediaMetadata != null) {
            printWriter.print(" title=" + this.mMediaMetadata.getText("android.media.metadata.TITLE"));
        }
        printWriter.println();
    }

    public final boolean sameSessions(MediaController mediaController, MediaController mediaController2) {
        if (mediaController == mediaController2) {
            return true;
        }
        if (mediaController == null) {
            return false;
        }
        return mediaController.controlsSameSession(mediaController2);
    }

    public final int getMediaControllerPlaybackState(MediaController mediaController) {
        PlaybackState playbackState;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null) {
            return 0;
        }
        return playbackState.getState();
    }

    public final void clearCurrentMediaNotificationSession() {
        this.mMediaArtworkProcessor.clearCache();
        this.mMediaMetadata = null;
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(this.mMediaListener);
        }
        this.mMediaController = null;
    }

    public void updateMediaMetaData(boolean z, boolean z2) {
        Bitmap bitmap;
        Trace.beginSection("CentralSurfaces#updateMediaMetaData");
        if (this.mBackdrop == null) {
            Trace.endSection();
            return;
        }
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        boolean z3 = biometricUnlockController != null && biometricUnlockController.isWakeAndUnlock();
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway() || z3) {
            this.mBackdrop.setVisibility(4);
            Trace.endSection();
            return;
        }
        MediaMetadata mediaMetadata = getMediaMetadata();
        if (mediaMetadata == null || this.mKeyguardBypassController.getBypassEnabled()) {
            bitmap = null;
        } else {
            bitmap = mediaMetadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null) {
                bitmap = mediaMetadata.getBitmap("android.media.metadata.ALBUM_ART");
            }
        }
        if (z) {
            for (AsyncTask<?, ?, ?> cancel : this.mProcessArtworkTasks) {
                cancel.cancel(true);
            }
            this.mProcessArtworkTasks.clear();
        }
        if (bitmap == null || Utils.useQsMediaPlayer(this.mContext)) {
            finishUpdateMediaMetaData(z, z2, (Bitmap) null);
        } else {
            this.mProcessArtworkTasks.add(new ProcessArtworkTask(this, z, z2).execute(new Bitmap[]{bitmap}));
        }
        Trace.endSection();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:75:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void finishUpdateMediaMetaData(boolean r10, boolean r11, android.graphics.Bitmap r12) {
        /*
            r9 = this;
            r0 = 0
            if (r12 == 0) goto L_0x000f
            android.graphics.drawable.BitmapDrawable r1 = new android.graphics.drawable.BitmapDrawable
            android.widget.ImageView r2 = r9.mBackdropBack
            android.content.res.Resources r2 = r2.getResources()
            r1.<init>(r2, r12)
            goto L_0x0010
        L_0x000f:
            r1 = r0
        L_0x0010:
            r12 = 1
            r2 = 0
            if (r1 == 0) goto L_0x0016
            r3 = r12
            goto L_0x0017
        L_0x0016:
            r3 = r2
        L_0x0017:
            if (r1 != 0) goto L_0x003a
            com.android.systemui.statusbar.phone.LockscreenWallpaper r4 = r9.mLockscreenWallpaper
            if (r4 == 0) goto L_0x0022
            android.graphics.Bitmap r4 = r4.getBitmap()
            goto L_0x0023
        L_0x0022:
            r4 = r0
        L_0x0023:
            if (r4 == 0) goto L_0x003a
            com.android.systemui.statusbar.phone.LockscreenWallpaper$WallpaperDrawable r1 = new com.android.systemui.statusbar.phone.LockscreenWallpaper$WallpaperDrawable
            android.widget.ImageView r5 = r9.mBackdropBack
            android.content.res.Resources r5 = r5.getResources()
            r1.<init>((android.content.res.Resources) r5, (android.graphics.Bitmap) r4)
            com.android.systemui.plugins.statusbar.StatusBarStateController r4 = r9.mStatusBarStateController
            int r4 = r4.getState()
            if (r4 != r12) goto L_0x003a
            r4 = r12
            goto L_0x003b
        L_0x003a:
            r4 = r2
        L_0x003b:
            dagger.Lazy<com.android.systemui.statusbar.NotificationShadeWindowController> r5 = r9.mNotificationShadeWindowController
            java.lang.Object r5 = r5.get()
            com.android.systemui.statusbar.NotificationShadeWindowController r5 = (com.android.systemui.statusbar.NotificationShadeWindowController) r5
            dagger.Lazy<java.util.Optional<com.android.systemui.statusbar.phone.CentralSurfaces>> r6 = r9.mCentralSurfacesOptionalLazy
            java.lang.Object r6 = r6.get()
            java.util.Optional r6 = (java.util.Optional) r6
            com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda2 r7 = new com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda2
            r7.<init>()
            java.util.Optional r6 = r6.map(r7)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            java.lang.Object r6 = r6.orElse(r7)
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            if (r1 == 0) goto L_0x0064
            r7 = r12
            goto L_0x0065
        L_0x0064:
            r7 = r2
        L_0x0065:
            com.android.systemui.colorextraction.SysuiColorExtractor r8 = r9.mColorExtractor
            r8.setHasMediaArtwork(r3)
            com.android.systemui.statusbar.phone.ScrimController r3 = r9.mScrimController
            if (r3 == 0) goto L_0x0071
            r3.setHasBackdrop(r7)
        L_0x0071:
            r3 = 2
            r8 = 0
            if (r7 != 0) goto L_0x0077
            goto L_0x011d
        L_0x0077:
            com.android.systemui.plugins.statusbar.StatusBarStateController r7 = r9.mStatusBarStateController
            int r7 = r7.getState()
            if (r7 != 0) goto L_0x0081
            if (r4 == 0) goto L_0x011d
        L_0x0081:
            com.android.systemui.statusbar.phone.BiometricUnlockController r4 = r9.mBiometricUnlockController
            if (r4 == 0) goto L_0x011d
            int r4 = r4.getMode()
            if (r4 == r3) goto L_0x011d
            if (r6 != 0) goto L_0x011d
            com.android.systemui.statusbar.BackDropView r0 = r9.mBackdrop
            int r0 = r0.getVisibility()
            r3 = 1065353216(0x3f800000, float:1.0)
            if (r0 == 0) goto L_0x00c1
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            r10.setVisibility(r2)
            if (r11 == 0) goto L_0x00ad
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            r10.setAlpha(r8)
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            android.view.ViewPropertyAnimator r10 = r10.animate()
            r10.alpha(r3)
            goto L_0x00bb
        L_0x00ad:
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            android.view.ViewPropertyAnimator r10 = r10.animate()
            r10.cancel()
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            r10.setAlpha(r3)
        L_0x00bb:
            if (r5 == 0) goto L_0x00c0
            r5.setBackdropShowing(r12)
        L_0x00c0:
            r10 = r12
        L_0x00c1:
            if (r10 == 0) goto L_0x01b3
            android.widget.ImageView r10 = r9.mBackdropBack
            android.graphics.drawable.Drawable r10 = r10.getDrawable()
            if (r10 == 0) goto L_0x00f3
            android.widget.ImageView r10 = r9.mBackdropBack
            android.graphics.drawable.Drawable r10 = r10.getDrawable()
            android.graphics.drawable.Drawable$ConstantState r10 = r10.getConstantState()
            android.widget.ImageView r11 = r9.mBackdropFront
            android.content.res.Resources r11 = r11.getResources()
            android.graphics.drawable.Drawable r10 = r10.newDrawable(r11)
            android.graphics.drawable.Drawable r10 = r10.mutate()
            android.widget.ImageView r11 = r9.mBackdropFront
            r11.setImageDrawable(r10)
            android.widget.ImageView r10 = r9.mBackdropFront
            r10.setAlpha(r3)
            android.widget.ImageView r10 = r9.mBackdropFront
            r10.setVisibility(r2)
            goto L_0x00f9
        L_0x00f3:
            android.widget.ImageView r10 = r9.mBackdropFront
            r11 = 4
            r10.setVisibility(r11)
        L_0x00f9:
            android.widget.ImageView r10 = r9.mBackdropBack
            r10.setImageDrawable(r1)
            android.widget.ImageView r10 = r9.mBackdropFront
            int r10 = r10.getVisibility()
            if (r10 != 0) goto L_0x01b3
            android.widget.ImageView r10 = r9.mBackdropFront
            android.view.ViewPropertyAnimator r10 = r10.animate()
            r11 = 250(0xfa, double:1.235E-321)
            android.view.ViewPropertyAnimator r10 = r10.setDuration(r11)
            android.view.ViewPropertyAnimator r10 = r10.alpha(r8)
            java.lang.Runnable r9 = r9.mHideBackdropFront
            r10.withEndAction(r9)
            goto L_0x01b3
        L_0x011d:
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            int r10 = r10.getVisibility()
            r11 = 8
            if (r10 == r11) goto L_0x01b3
            com.android.systemui.plugins.statusbar.StatusBarStateController r10 = r9.mStatusBarStateController
            boolean r10 = r10.isDozing()
            if (r10 == 0) goto L_0x0138
            com.android.systemui.statusbar.phone.ScrimState r10 = com.android.systemui.statusbar.phone.ScrimState.AOD
            boolean r10 = r10.getAnimateChange()
            if (r10 != 0) goto L_0x0138
            goto L_0x0139
        L_0x0138:
            r12 = r2
        L_0x0139:
            com.android.systemui.statusbar.policy.KeyguardStateController r10 = r9.mKeyguardStateController
            boolean r10 = r10.isBypassFadingAnimation()
            com.android.systemui.statusbar.phone.BiometricUnlockController r1 = r9.mBiometricUnlockController
            if (r1 == 0) goto L_0x0149
            int r1 = r1.getMode()
            if (r1 == r3) goto L_0x014b
        L_0x0149:
            if (r12 == 0) goto L_0x014d
        L_0x014b:
            if (r10 == 0) goto L_0x014f
        L_0x014d:
            if (r6 == 0) goto L_0x015f
        L_0x014f:
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            r10.setVisibility(r11)
            android.widget.ImageView r9 = r9.mBackdropBack
            r9.setImageDrawable(r0)
            if (r5 == 0) goto L_0x01b3
            r5.setBackdropShowing(r2)
            goto L_0x01b3
        L_0x015f:
            if (r5 == 0) goto L_0x0164
            r5.setBackdropShowing(r2)
        L_0x0164:
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            android.view.ViewPropertyAnimator r10 = r10.animate()
            android.view.ViewPropertyAnimator r10 = r10.alpha(r8)
            android.view.animation.Interpolator r11 = com.android.systemui.animation.Interpolators.ACCELERATE_DECELERATE
            android.view.ViewPropertyAnimator r10 = r10.setInterpolator(r11)
            r11 = 300(0x12c, double:1.48E-321)
            android.view.ViewPropertyAnimator r10 = r10.setDuration(r11)
            r11 = 0
            android.view.ViewPropertyAnimator r10 = r10.setStartDelay(r11)
            com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda3 r11 = new com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda3
            r11.<init>(r9)
            r10.withEndAction(r11)
            com.android.systemui.statusbar.policy.KeyguardStateController r10 = r9.mKeyguardStateController
            boolean r10 = r10.isKeyguardFadingAway()
            if (r10 == 0) goto L_0x01b3
            com.android.systemui.statusbar.BackDropView r10 = r9.mBackdrop
            android.view.ViewPropertyAnimator r10 = r10.animate()
            com.android.systemui.statusbar.policy.KeyguardStateController r11 = r9.mKeyguardStateController
            long r11 = r11.getShortenedFadingAwayDuration()
            android.view.ViewPropertyAnimator r10 = r10.setDuration(r11)
            com.android.systemui.statusbar.policy.KeyguardStateController r9 = r9.mKeyguardStateController
            long r11 = r9.getKeyguardFadingAwayDelay()
            android.view.ViewPropertyAnimator r9 = r10.setStartDelay(r11)
            android.view.animation.Interpolator r10 = com.android.systemui.animation.Interpolators.LINEAR
            android.view.ViewPropertyAnimator r9 = r9.setInterpolator(r10)
            r9.start()
        L_0x01b3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationMediaManager.finishUpdateMediaMetaData(boolean, boolean, android.graphics.Bitmap):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUpdateMediaMetaData$1() {
        this.mBackdrop.setVisibility(8);
        this.mBackdropFront.animate().cancel();
        this.mBackdropBack.setImageDrawable((Drawable) null);
        this.mMainExecutor.execute(this.mHideBackdropFront);
    }

    public void setup(BackDropView backDropView, ImageView imageView, ImageView imageView2, ScrimController scrimController, LockscreenWallpaper lockscreenWallpaper) {
        this.mBackdrop = backDropView;
        this.mBackdropFront = imageView;
        this.mBackdropBack = imageView2;
        this.mScrimController = scrimController;
        this.mLockscreenWallpaper = lockscreenWallpaper;
    }

    public void setBiometricUnlockController(BiometricUnlockController biometricUnlockController) {
        this.mBiometricUnlockController = biometricUnlockController;
    }

    public final Bitmap processArtwork(Bitmap bitmap) {
        return this.mMediaArtworkProcessor.processArtwork(this.mContext, bitmap);
    }

    public final void removeTask(AsyncTask<?, ?, ?> asyncTask) {
        this.mProcessArtworkTasks.remove(asyncTask);
    }

    public static final class ProcessArtworkTask extends AsyncTask<Bitmap, Void, Bitmap> {
        public final boolean mAllowEnterAnimation;
        public final WeakReference<NotificationMediaManager> mManagerRef;
        public final boolean mMetaDataChanged;

        public ProcessArtworkTask(NotificationMediaManager notificationMediaManager, boolean z, boolean z2) {
            this.mManagerRef = new WeakReference<>(notificationMediaManager);
            this.mMetaDataChanged = z;
            this.mAllowEnterAnimation = z2;
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            NotificationMediaManager notificationMediaManager = (NotificationMediaManager) this.mManagerRef.get();
            if (notificationMediaManager == null || bitmapArr.length == 0 || isCancelled()) {
                return null;
            }
            return notificationMediaManager.processArtwork(bitmapArr[0]);
        }

        public void onPostExecute(Bitmap bitmap) {
            NotificationMediaManager notificationMediaManager = (NotificationMediaManager) this.mManagerRef.get();
            if (notificationMediaManager != null && !isCancelled()) {
                notificationMediaManager.removeTask(this);
                notificationMediaManager.finishUpdateMediaMetaData(this.mMetaDataChanged, this.mAllowEnterAnimation, bitmap);
            }
        }

        public void onCancelled(Bitmap bitmap) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            NotificationMediaManager notificationMediaManager = (NotificationMediaManager) this.mManagerRef.get();
            if (notificationMediaManager != null) {
                notificationMediaManager.removeTask(this);
            }
        }
    }
}
