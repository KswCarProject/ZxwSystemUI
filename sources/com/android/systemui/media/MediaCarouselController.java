package com.android.systemui.media;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.Dumpable;
import com.android.systemui.R$color;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.provider.OnReorderingAllowedListener;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.systemui.util.animation.UniqueObjectHostViewKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselController implements Dumpable {
    @NotNull
    public final ActivityStarter activityStarter;
    public int carouselMeasureHeight;
    public int carouselMeasureWidth;
    @NotNull
    public final MediaCarouselController$configListener$1 configListener;
    @NotNull
    public final Context context;
    public int currentCarouselHeight;
    public int currentCarouselWidth;
    public int currentEndLocation = -1;
    public int currentStartLocation = -1;
    public float currentTransitionProgress = 1.0f;
    public boolean currentlyExpanded = true;
    public boolean currentlyShowingOnlyActive;
    @NotNull
    public final MediaCarouselControllerLogger debugLogger;
    @Nullable
    public MediaHostState desiredHostState;
    public int desiredLocation = -1;
    public boolean isRtl;
    @NotNull
    public Set<String> keysNeedRemoval = new LinkedHashSet();
    @NotNull
    public final MediaUiEventLogger logger;
    @NotNull
    public final MediaScrollView mediaCarousel;
    @NotNull
    public final MediaCarouselScrollHandler mediaCarouselScrollHandler;
    @NotNull
    public final ViewGroup mediaContent;
    @NotNull
    public final Provider<MediaControlPanel> mediaControlPanelFactory;
    @NotNull
    public final ViewGroup mediaFrame;
    @NotNull
    public final MediaHostStatesManager mediaHostStatesManager;
    @NotNull
    public final MediaDataManager mediaManager;
    public boolean needsReordering;
    @NotNull
    public final PageIndicator pageIndicator;
    public boolean playersVisible;
    public View settingsButton;
    public boolean shouldScrollToActivePlayer;
    @NotNull
    public final SystemClock systemClock;
    public Function0<Unit> updateUserVisibility;
    @NotNull
    public final OnReorderingAllowedListener visualStabilityCallback;
    @NotNull
    public final VisualStabilityProvider visualStabilityProvider;

    public static /* synthetic */ void getSettingsButton$annotations() {
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, @NotNull int[] iArr, int i4, int i5) {
        logSmartspaceCardReported$default(this, i, i2, i3, iArr, i4, i5, 0, 0, false, 448, (Object) null);
    }

    public final boolean addOrUpdatePlayer(String str, String str2, MediaData mediaData, boolean z) {
        Trace.beginSection("MediaCarouselController#addOrUpdatePlayer");
        try {
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            MediaPlayerData.moveIfExists$default(mediaPlayerData, str2, str, (MediaCarouselControllerLogger) null, 4, (Object) null);
            MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str);
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) CollectionsKt___CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), getMediaCarouselScrollHandler().getVisibleMediaIndex());
            boolean z2 = true;
            if (mediaPlayer == null) {
                MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
                mediaControlPanel.attachPlayer(MediaViewHolder.Companion.create(LayoutInflater.from(this.context), this.mediaContent));
                mediaControlPanel.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addOrUpdatePlayer$1$1(this));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                MediaViewHolder mediaViewHolder = mediaControlPanel.getMediaViewHolder();
                if (mediaViewHolder != null) {
                    TransitionLayout player = mediaViewHolder.getPlayer();
                    if (player != null) {
                        player.setLayoutParams(layoutParams);
                    }
                }
                mediaControlPanel.bindPlayer(mediaData, str);
                mediaControlPanel.setListening(this.currentlyExpanded);
                mediaPlayerData.addMediaPlayer(str, mediaData, mediaControlPanel, this.systemClock, z, this.debugLogger);
                updatePlayerToState(mediaControlPanel, true);
                reorderAllPlayers(mediaSortKey);
            } else {
                mediaPlayer.bindPlayer(mediaData, str);
                mediaPlayerData.addMediaPlayer(str, mediaData, mediaPlayer, this.systemClock, z, this.debugLogger);
                if (!isReorderingAllowed()) {
                    if (!getShouldScrollToActivePlayer()) {
                        this.needsReordering = true;
                    }
                }
                reorderAllPlayers(mediaSortKey);
            }
            updatePageIndicator();
            getMediaCarouselScrollHandler().onPlayersChanged();
            UniqueObjectHostViewKt.setRequiresRemeasuring(getMediaFrame(), true);
            if (mediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
                Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
            }
            if (mediaPlayer != null) {
                z2 = false;
            }
            return z2;
        } finally {
            Trace.endSection();
        }
    }

    public final void addSmartspaceMediaRecommendations(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Trace.beginSection("MediaCarouselController#addSmartspaceMediaRecommendations");
        try {
            if (MediaCarouselControllerKt.DEBUG) {
                Log.d("MediaCarouselController", "Updating smartspace target in carousel");
            }
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            if (mediaPlayerData.getMediaPlayer(str) != null) {
                Log.w("MediaCarouselController", "Skip adding smartspace target in carousel");
                return;
            }
            String smartspaceMediaKey = mediaPlayerData.smartspaceMediaKey();
            if (smartspaceMediaKey != null) {
                if (mediaPlayerData.removeMediaPlayer(smartspaceMediaKey) != null) {
                    this.debugLogger.logPotentialMemoryLeak(smartspaceMediaKey);
                }
            }
            MediaControlPanel mediaControlPanel = this.mediaControlPanelFactory.get();
            mediaControlPanel.attachRecommendation(RecommendationViewHolder.Companion.create(LayoutInflater.from(this.context), this.mediaContent));
            mediaControlPanel.getMediaViewController().setSizeChangedListener(new MediaCarouselController$addSmartspaceMediaRecommendations$1$2(this));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            RecommendationViewHolder recommendationViewHolder = mediaControlPanel.getRecommendationViewHolder();
            if (recommendationViewHolder != null) {
                TransitionLayout recommendations = recommendationViewHolder.getRecommendations();
                if (recommendations != null) {
                    recommendations.setLayoutParams(layoutParams);
                }
            }
            mediaControlPanel.bindRecommendation(smartspaceMediaData);
            mediaPlayerData.addMediaRecommendation(str, smartspaceMediaData, mediaControlPanel, z, this.systemClock, this.debugLogger);
            updatePlayerToState(mediaControlPanel, true);
            reorderAllPlayers((MediaPlayerData.MediaSortKey) CollectionsKt___CollectionsKt.elementAtOrNull(mediaPlayerData.playerKeys(), getMediaCarouselScrollHandler().getVisibleMediaIndex()));
            updatePageIndicator();
            UniqueObjectHostViewKt.setRequiresRemeasuring(getMediaFrame(), true);
            if (mediaPlayerData.players().size() != this.mediaContent.getChildCount()) {
                Log.wtf("MediaCarouselController", "Size of players list and number of views in carousel are out of sync");
            }
            Unit unit = Unit.INSTANCE;
            Trace.endSection();
        } finally {
            Trace.endSection();
        }
    }

    @Nullable
    public final Unit onDesiredLocationChanged(int i, @Nullable MediaHostState mediaHostState, boolean z, long j, long j2) {
        Trace.beginSection("MediaCarouselController#onDesiredLocationChanged");
        Unit unit = null;
        if (mediaHostState != null) {
            try {
                if (this.desiredLocation != i) {
                    this.logger.logCarouselPosition(i);
                }
                this.desiredLocation = i;
                this.desiredHostState = mediaHostState;
                setCurrentlyExpanded(mediaHostState.getExpansion() > 0.0f);
                boolean z2 = !this.currentlyExpanded && !this.mediaManager.hasActiveMediaOrRecommendation() && mediaHostState.getShowsOnlyActiveMedia();
                for (MediaControlPanel next : MediaPlayerData.INSTANCE.players()) {
                    if (z) {
                        next.getMediaViewController().animatePendingStateChange(j, j2);
                    }
                    if (z2 && next.getMediaViewController().isGutsVisible()) {
                        next.closeGuts(!z);
                    }
                    next.getMediaViewController().onLocationPreChange(i);
                }
                getMediaCarouselScrollHandler().setShowsSettingsButton(!mediaHostState.getShowsOnlyActiveMedia());
                getMediaCarouselScrollHandler().setFalsingProtectionNeeded(mediaHostState.getFalsingProtectionNeeded());
                boolean visible = mediaHostState.getVisible();
                if (visible != this.playersVisible) {
                    this.playersVisible = visible;
                    if (visible) {
                        MediaCarouselScrollHandler.resetTranslation$default(getMediaCarouselScrollHandler(), false, 1, (Object) null);
                    }
                }
                updateCarouselSize();
                unit = Unit.INSTANCE;
            } catch (Throwable th) {
                Trace.endSection();
                throw th;
            }
        }
        Trace.endSection();
        return unit;
    }

    public MediaCarouselController(@NotNull Context context2, @NotNull Provider<MediaControlPanel> provider, @NotNull VisualStabilityProvider visualStabilityProvider2, @NotNull MediaHostStatesManager mediaHostStatesManager2, @NotNull ActivityStarter activityStarter2, @NotNull SystemClock systemClock2, @NotNull DelayableExecutor delayableExecutor, @NotNull MediaDataManager mediaDataManager, @NotNull ConfigurationController configurationController, @NotNull FalsingCollector falsingCollector, @NotNull FalsingManager falsingManager, @NotNull DumpManager dumpManager, @NotNull MediaUiEventLogger mediaUiEventLogger, @NotNull MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        this.context = context2;
        this.mediaControlPanelFactory = provider;
        this.visualStabilityProvider = visualStabilityProvider2;
        this.mediaHostStatesManager = mediaHostStatesManager2;
        this.activityStarter = activityStarter2;
        this.systemClock = systemClock2;
        this.mediaManager = mediaDataManager;
        MediaUiEventLogger mediaUiEventLogger2 = mediaUiEventLogger;
        this.logger = mediaUiEventLogger2;
        this.debugLogger = mediaCarouselControllerLogger;
        MediaCarouselController$configListener$1 mediaCarouselController$configListener$1 = new MediaCarouselController$configListener$1(this);
        this.configListener = mediaCarouselController$configListener$1;
        dumpManager.registerDumpable("MediaCarouselController", this);
        ViewGroup inflateMediaCarousel = inflateMediaCarousel();
        this.mediaFrame = inflateMediaCarousel;
        MediaScrollView mediaScrollView = (MediaScrollView) inflateMediaCarousel.requireViewById(R$id.media_carousel_scroller);
        this.mediaCarousel = mediaScrollView;
        PageIndicator pageIndicator2 = (PageIndicator) inflateMediaCarousel.requireViewById(R$id.media_page_indicator);
        this.pageIndicator = pageIndicator2;
        MediaCarouselScrollHandler mediaCarouselScrollHandler2 = r5;
        MediaScrollView mediaScrollView2 = mediaScrollView;
        ViewGroup viewGroup = inflateMediaCarousel;
        MediaCarouselController$configListener$1 mediaCarouselController$configListener$12 = mediaCarouselController$configListener$1;
        MediaCarouselScrollHandler mediaCarouselScrollHandler3 = new MediaCarouselScrollHandler(mediaScrollView, pageIndicator2, delayableExecutor, new Function0<Unit>(this) {
            public final void invoke() {
                ((MediaCarouselController) this.receiver).onSwipeToDismiss();
            }
        }, new Function0<Unit>(this) {
            public final void invoke() {
                ((MediaCarouselController) this.receiver).updatePageIndicatorLocation();
            }
        }, new Function1<Boolean, Unit>(this) {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).closeGuts(z);
            }
        }, falsingCollector, falsingManager, new Function1<Boolean, Unit>(this) {
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                ((MediaCarouselController) this.receiver).logSmartspaceImpression(z);
            }
        }, mediaUiEventLogger2);
        this.mediaCarouselScrollHandler = mediaCarouselScrollHandler2;
        setRtl(context2.getResources().getConfiguration().getLayoutDirection() == 1);
        inflateSettingsButton();
        this.mediaContent = (ViewGroup) mediaScrollView2.requireViewById(R$id.media_carousel);
        configurationController.addCallback(mediaCarouselController$configListener$12);
        AnonymousClass5 r1 = new OnReorderingAllowedListener(this) {
            public final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public final void onReorderingAllowed() {
                if (this.this$0.needsReordering) {
                    this.this$0.needsReordering = false;
                    this.this$0.reorderAllPlayers((MediaPlayerData.MediaSortKey) null);
                }
                MediaCarouselController mediaCarouselController = this.this$0;
                for (String removePlayer$default : this.this$0.keysNeedRemoval) {
                    MediaCarouselController.removePlayer$default(mediaCarouselController, removePlayer$default, false, false, 6, (Object) null);
                }
                this.this$0.keysNeedRemoval.clear();
                MediaCarouselController mediaCarouselController2 = this.this$0;
                if (mediaCarouselController2.updateUserVisibility != null) {
                    mediaCarouselController2.getUpdateUserVisibility().invoke();
                }
                this.this$0.getMediaCarouselScrollHandler().scrollToStart();
            }
        };
        this.visualStabilityCallback = r1;
        visualStabilityProvider2.addPersistentReorderingAllowedListener(r1);
        mediaDataManager.addListener(new MediaDataManager.Listener(this) {
            public final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public void onMediaDataLoaded(@NotNull String str, @Nullable String str2, @NotNull MediaData mediaData, boolean z, int i, boolean z2) {
                MediaCarouselController mediaCarouselController;
                String str3 = str;
                boolean z3 = false;
                if (this.this$0.addOrUpdatePlayer(str3, str2, mediaData, z2)) {
                    MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                    MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str3);
                    if (mediaPlayer != null) {
                        MediaCarouselController.logSmartspaceCardReported$default(this.this$0, 759, mediaPlayer.mSmartspaceId, mediaPlayer.mUid, new int[]{4, 2, 5}, 0, 0, mediaPlayerData.getMediaPlayerIndex(str3), 0, false, 432, (Object) null);
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && this.this$0.getMediaCarouselScrollHandler().getVisibleMediaIndex() == mediaPlayerData.getMediaPlayerIndex(str3)) {
                        MediaCarouselController mediaCarouselController2 = this.this$0;
                        mediaCarouselController2.logSmartspaceImpression(mediaCarouselController2.getMediaCarouselScrollHandler().getQsExpanded());
                    }
                } else if (i != 0) {
                    MediaCarouselController mediaCarouselController3 = this.this$0;
                    int i2 = 0;
                    for (Object next : MediaPlayerData.INSTANCE.players()) {
                        int i3 = i2 + 1;
                        if (i2 < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
                        if (mediaControlPanel.getRecommendationViewHolder() == null) {
                            int hash = SmallHash.hash(mediaControlPanel.mUid + ((int) mediaCarouselController3.systemClock.currentTimeMillis()));
                            mediaControlPanel.mSmartspaceId = hash;
                            mediaControlPanel.mIsImpressed = false;
                            mediaCarouselController = mediaCarouselController3;
                            MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController3, 759, hash, mediaControlPanel.mUid, new int[]{4, 2, 5}, 0, 0, i2, i, false, 304, (Object) null);
                        } else {
                            mediaCarouselController = mediaCarouselController3;
                        }
                        i2 = i3;
                        mediaCarouselController3 = mediaCarouselController;
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && !this.this$0.getMediaCarouselScrollHandler().getQsExpanded()) {
                        MediaCarouselController mediaCarouselController4 = this.this$0;
                        mediaCarouselController4.logSmartspaceImpression(mediaCarouselController4.getMediaCarouselScrollHandler().getQsExpanded());
                    }
                }
                Boolean isPlaying = mediaData.isPlaying();
                Boolean valueOf = isPlaying == null ? null : Boolean.valueOf(!isPlaying.booleanValue());
                if ((valueOf == null ? mediaData.isClearable() : valueOf.booleanValue()) && !mediaData.getActive()) {
                    z3 = true;
                }
                if (!z3 || Utils.useMediaResumption(this.this$0.context)) {
                    this.this$0.keysNeedRemoval.remove(str3);
                } else if (this.this$0.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str3);
                }
            }

            public void onSmartspaceMediaDataLoaded(@NotNull String str, @NotNull SmartspaceMediaData smartspaceMediaData, boolean z) {
                MediaCarouselController mediaCarouselController;
                String str2 = str;
                boolean z2 = z;
                if (MediaCarouselControllerKt.DEBUG) {
                    Log.d("MediaCarouselController", "Loading Smartspace media update");
                }
                boolean z3 = true;
                if (smartspaceMediaData.isActive()) {
                    boolean z4 = false;
                    if (this.this$0.mediaManager.hasActiveMedia() || !this.this$0.mediaManager.hasAnyMedia() || !z2) {
                        z3 = false;
                    }
                    if (z3) {
                        MediaCarouselController mediaCarouselController2 = this.this$0;
                        int i = 0;
                        for (Object next : MediaPlayerData.INSTANCE.players()) {
                            int i2 = i + 1;
                            if (i < 0) {
                                CollectionsKt__CollectionsKt.throwIndexOverflow();
                            }
                            MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
                            if (mediaControlPanel.getRecommendationViewHolder() == null) {
                                int hash = SmallHash.hash(mediaControlPanel.mUid + ((int) mediaCarouselController2.systemClock.currentTimeMillis()));
                                mediaControlPanel.mSmartspaceId = hash;
                                mediaControlPanel.mIsImpressed = z4;
                                mediaCarouselController = mediaCarouselController2;
                                MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController2, 759, hash, mediaControlPanel.mUid, new int[]{4, 2, 5}, 0, 0, i, (int) (mediaCarouselController2.systemClock.currentTimeMillis() - smartspaceMediaData.getHeadphoneConnectionTimeMillis()), false, 304, (Object) null);
                            } else {
                                mediaCarouselController = mediaCarouselController2;
                            }
                            i = i2;
                            mediaCarouselController2 = mediaCarouselController;
                            z4 = false;
                        }
                    }
                    this.this$0.addSmartspaceMediaRecommendations(str2, smartspaceMediaData, z2);
                    MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                    MediaControlPanel mediaPlayer = mediaPlayerData.getMediaPlayer(str2);
                    if (mediaPlayer != null) {
                        MediaCarouselController mediaCarouselController3 = this.this$0;
                        MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController3, 759, mediaPlayer.mSmartspaceId, mediaPlayer.mUid, new int[]{4, 2, 5}, 0, 0, mediaPlayerData.getMediaPlayerIndex(str2), (int) (mediaCarouselController3.systemClock.currentTimeMillis() - smartspaceMediaData.getHeadphoneConnectionTimeMillis()), false, 304, (Object) null);
                    }
                    if (this.this$0.getMediaCarouselScrollHandler().getVisibleToUser() && this.this$0.getMediaCarouselScrollHandler().getVisibleMediaIndex() == mediaPlayerData.getMediaPlayerIndex(str2)) {
                        MediaCarouselController mediaCarouselController4 = this.this$0;
                        mediaCarouselController4.logSmartspaceImpression(mediaCarouselController4.getMediaCarouselScrollHandler().getQsExpanded());
                        return;
                    }
                    return;
                }
                SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
                onSmartspaceMediaDataRemoved(smartspaceMediaData.getTargetId(), true);
            }

            public void onMediaDataRemoved(@NotNull String str) {
                MediaCarouselController.removePlayer$default(this.this$0, str, false, false, 6, (Object) null);
            }

            public void onSmartspaceMediaDataRemoved(@NotNull String str, boolean z) {
                if (MediaCarouselControllerKt.DEBUG) {
                    Log.d("MediaCarouselController", "My Smartspace media removal request is received");
                }
                if (z || this.this$0.isReorderingAllowed()) {
                    onMediaDataRemoved(str);
                } else {
                    this.this$0.keysNeedRemoval.add(str);
                }
            }
        });
        viewGroup.addOnLayoutChangeListener(new View.OnLayoutChangeListener(this) {
            public final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                this.this$0.updatePageIndicatorLocation();
            }
        });
        mediaHostStatesManager2.addCallback(new MediaHostStatesManager.Callback(this) {
            public final /* synthetic */ MediaCarouselController this$0;

            {
                this.this$0 = r1;
            }

            public void onHostStateChanged(int i, @NotNull MediaHostState mediaHostState) {
                if (i == this.this$0.desiredLocation) {
                    MediaCarouselController mediaCarouselController = this.this$0;
                    MediaCarouselController.onDesiredLocationChanged$default(mediaCarouselController, mediaCarouselController.desiredLocation, mediaHostState, false, 0, 0, 24, (Object) null);
                }
            }
        });
    }

    @NotNull
    public final MediaCarouselScrollHandler getMediaCarouselScrollHandler() {
        return this.mediaCarouselScrollHandler;
    }

    @NotNull
    public final ViewGroup getMediaFrame() {
        return this.mediaFrame;
    }

    @NotNull
    public final View getSettingsButton() {
        View view = this.settingsButton;
        if (view != null) {
            return view;
        }
        return null;
    }

    public final boolean getShouldScrollToActivePlayer() {
        return this.shouldScrollToActivePlayer;
    }

    public final void setShouldScrollToActivePlayer(boolean z) {
        this.shouldScrollToActivePlayer = z;
    }

    public final void setRtl(boolean z) {
        if (z != this.isRtl) {
            this.isRtl = z;
            this.mediaFrame.setLayoutDirection(z ? 1 : 0);
            this.mediaCarouselScrollHandler.scrollToStart();
        }
    }

    public final void setCurrentlyExpanded(boolean z) {
        if (this.currentlyExpanded != z) {
            this.currentlyExpanded = z;
            for (MediaControlPanel listening : MediaPlayerData.INSTANCE.players()) {
                listening.setListening(this.currentlyExpanded);
            }
        }
    }

    @NotNull
    public final Function0<Unit> getUpdateUserVisibility() {
        Function0<Unit> function0 = this.updateUserVisibility;
        if (function0 != null) {
            return function0;
        }
        return null;
    }

    public final void setUpdateUserVisibility(@NotNull Function0<Unit> function0) {
        this.updateUserVisibility = function0;
    }

    public final boolean isReorderingAllowed() {
        return this.visualStabilityProvider.isReorderingAllowed();
    }

    public final void inflateSettingsButton() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel_settings_button, this.mediaFrame, false);
        if (inflate != null) {
            if (this.settingsButton != null) {
                this.mediaFrame.removeView(getSettingsButton());
            }
            this.settingsButton = inflate;
            this.mediaFrame.addView(getSettingsButton());
            this.mediaCarouselScrollHandler.onSettingsButtonUpdated(inflate);
            getSettingsButton().setOnClickListener(new MediaCarouselController$inflateSettingsButton$2(this));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    public final ViewGroup inflateMediaCarousel() {
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.media_carousel, new UniqueObjectHostView(this.context), false);
        if (inflate != null) {
            ViewGroup viewGroup = (ViewGroup) inflate;
            viewGroup.setLayoutDirection(3);
            return viewGroup;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final void reorderAllPlayers(MediaPlayerData.MediaSortKey mediaSortKey) {
        Unit unit;
        RecommendationViewHolder recommendationViewHolder;
        this.mediaContent.removeAllViews();
        Iterator<MediaControlPanel> it = MediaPlayerData.INSTANCE.players().iterator();
        while (true) {
            Unit unit2 = null;
            if (!it.hasNext()) {
                break;
            }
            MediaControlPanel next = it.next();
            MediaViewHolder mediaViewHolder = next.getMediaViewHolder();
            if (mediaViewHolder != null) {
                this.mediaContent.addView(mediaViewHolder.getPlayer());
                unit2 = Unit.INSTANCE;
            }
            if (unit2 == null && (recommendationViewHolder = next.getRecommendationViewHolder()) != null) {
                this.mediaContent.addView(recommendationViewHolder.getRecommendations());
            }
        }
        this.mediaCarouselScrollHandler.onPlayersChanged();
        if (this.shouldScrollToActivePlayer) {
            this.shouldScrollToActivePlayer = false;
            MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
            int firstActiveMediaIndex = mediaPlayerData.firstActiveMediaIndex();
            int i = -1;
            if (firstActiveMediaIndex != -1) {
                if (mediaSortKey == null) {
                    unit = null;
                } else {
                    Iterator it2 = mediaPlayerData.playerKeys().iterator();
                    int i2 = 0;
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        Object next2 = it2.next();
                        if (i2 < 0) {
                            CollectionsKt__CollectionsKt.throwIndexOverflow();
                        }
                        if (Intrinsics.areEqual((Object) mediaSortKey, (Object) (MediaPlayerData.MediaSortKey) next2)) {
                            i = i2;
                            break;
                        }
                        i2++;
                    }
                    getMediaCarouselScrollHandler().scrollToPlayer(i, firstActiveMediaIndex);
                    unit = Unit.INSTANCE;
                }
                if (unit == null) {
                    MediaCarouselScrollHandler.scrollToPlayer$default(this.mediaCarouselScrollHandler, 0, firstActiveMediaIndex, 1, (Object) null);
                }
            }
        }
    }

    public static /* synthetic */ void removePlayer$default(MediaCarouselController mediaCarouselController, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        if ((i & 4) != 0) {
            z2 = true;
        }
        mediaCarouselController.removePlayer(str, z, z2);
    }

    public final void removePlayer(@NotNull String str, boolean z, boolean z2) {
        SmartspaceMediaData smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core;
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (Intrinsics.areEqual((Object) str, (Object) mediaPlayerData.smartspaceMediaKey()) && (smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) != null) {
            this.logger.logRecommendationRemoved(smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getPackageName(), smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getInstanceId());
        }
        MediaControlPanel removeMediaPlayer = mediaPlayerData.removeMediaPlayer(str);
        if (removeMediaPlayer != null) {
            getMediaCarouselScrollHandler().onPrePlayerRemoved(removeMediaPlayer);
            ViewGroup viewGroup = this.mediaContent;
            MediaViewHolder mediaViewHolder = removeMediaPlayer.getMediaViewHolder();
            TransitionLayout transitionLayout = null;
            viewGroup.removeView(mediaViewHolder == null ? null : mediaViewHolder.getPlayer());
            ViewGroup viewGroup2 = this.mediaContent;
            RecommendationViewHolder recommendationViewHolder = removeMediaPlayer.getRecommendationViewHolder();
            if (recommendationViewHolder != null) {
                transitionLayout = recommendationViewHolder.getRecommendations();
            }
            viewGroup2.removeView(transitionLayout);
            removeMediaPlayer.onDestroy();
            getMediaCarouselScrollHandler().onPlayersChanged();
            updatePageIndicator();
            if (z) {
                this.mediaManager.dismissMediaData(str, 0);
            }
            if (z2) {
                this.mediaManager.dismissSmartspaceRecommendation(str, 0);
            }
        }
    }

    public final void updatePlayers(boolean z) {
        this.pageIndicator.setTintList(ColorStateList.valueOf(this.context.getColor(R$color.media_paging_indicator)));
        for (Triple triple : MediaPlayerData.INSTANCE.mediaData()) {
            String str = (String) triple.component1();
            MediaData mediaData = (MediaData) triple.component2();
            if (((Boolean) triple.component3()).booleanValue()) {
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                SmartspaceMediaData smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                removePlayer(str, false, false);
                if (smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core != null) {
                    addSmartspaceMediaRecommendations(smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getTargetId(), smartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core, mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
                }
            } else {
                boolean isSsReactivated = MediaPlayerData.INSTANCE.isSsReactivated(str);
                if (z) {
                    removePlayer(str, false, false);
                }
                addOrUpdatePlayer(str, (String) null, mediaData, isSsReactivated);
            }
        }
    }

    public final void updatePageIndicator() {
        int childCount = this.mediaContent.getChildCount();
        this.pageIndicator.setNumPages(childCount);
        if (childCount == 1) {
            this.pageIndicator.setLocation(0.0f);
        }
        updatePageIndicatorAlpha();
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        if (i == this.currentStartLocation && i2 == this.currentEndLocation) {
            if ((f == this.currentTransitionProgress) && !z) {
                return;
            }
        }
        this.currentStartLocation = i;
        this.currentEndLocation = i2;
        this.currentTransitionProgress = f;
        for (MediaControlPanel updatePlayerToState : MediaPlayerData.INSTANCE.players()) {
            updatePlayerToState(updatePlayerToState, z);
        }
        maybeResetSettingsCog();
        updatePageIndicatorAlpha();
    }

    public final void updatePageIndicatorAlpha() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean z = false;
        boolean visible = mediaHostState == null ? false : mediaHostState.getVisible();
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        if (mediaHostState2 != null) {
            z = mediaHostState2.getVisible();
        }
        float f = 1.0f;
        float f2 = z ? 1.0f : 0.0f;
        float f3 = visible ? 1.0f : 0.0f;
        if (!visible || !z) {
            float f4 = this.currentTransitionProgress;
            if (!visible) {
                f4 = 1.0f - f4;
            }
            f = MathUtils.lerp(f2, f3, MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 0.0f, 1.0f, f4), 0.0f, 1.0f));
        }
        this.pageIndicator.setAlpha(f);
    }

    public final void updatePageIndicatorLocation() {
        int i;
        int i2;
        if (this.isRtl) {
            i2 = this.pageIndicator.getWidth();
            i = this.currentCarouselWidth;
        } else {
            i2 = this.currentCarouselWidth;
            i = this.pageIndicator.getWidth();
        }
        this.pageIndicator.setTranslationX((((float) (i2 - i)) / 2.0f) + this.mediaCarouselScrollHandler.getContentTranslation());
        ViewGroup.LayoutParams layoutParams = this.pageIndicator.getLayoutParams();
        if (layoutParams != null) {
            PageIndicator pageIndicator2 = this.pageIndicator;
            pageIndicator2.setTranslationY((float) ((this.currentCarouselHeight - pageIndicator2.getHeight()) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
    }

    public final void updateCarouselDimensions() {
        int i = 0;
        int i2 = 0;
        for (MediaControlPanel mediaViewController : MediaPlayerData.INSTANCE.players()) {
            MediaViewController mediaViewController2 = mediaViewController.getMediaViewController();
            i = Math.max(i, mediaViewController2.getCurrentWidth() + ((int) mediaViewController2.getTranslationX()));
            i2 = Math.max(i2, mediaViewController2.getCurrentHeight() + ((int) mediaViewController2.getTranslationY()));
        }
        if (i != this.currentCarouselWidth || i2 != this.currentCarouselHeight) {
            this.currentCarouselWidth = i;
            this.currentCarouselHeight = i2;
            this.mediaCarouselScrollHandler.setCarouselBounds(i, i2);
            updatePageIndicatorLocation();
        }
    }

    public final void maybeResetSettingsCog() {
        Map<Integer, MediaHostState> mediaHostStates = this.mediaHostStatesManager.getMediaHostStates();
        MediaHostState mediaHostState = mediaHostStates.get(Integer.valueOf(this.currentEndLocation));
        boolean showsOnlyActiveMedia = mediaHostState == null ? true : mediaHostState.getShowsOnlyActiveMedia();
        MediaHostState mediaHostState2 = mediaHostStates.get(Integer.valueOf(this.currentStartLocation));
        boolean showsOnlyActiveMedia2 = mediaHostState2 == null ? showsOnlyActiveMedia : mediaHostState2.getShowsOnlyActiveMedia();
        if (this.currentlyShowingOnlyActive == showsOnlyActiveMedia) {
            float f = this.currentTransitionProgress;
            boolean z = false;
            if (!(f == 1.0f)) {
                if (f == 0.0f) {
                    z = true;
                }
                if (z || showsOnlyActiveMedia2 == showsOnlyActiveMedia) {
                    return;
                }
            } else {
                return;
            }
        }
        this.currentlyShowingOnlyActive = showsOnlyActiveMedia;
        this.mediaCarouselScrollHandler.resetTranslation(true);
    }

    public final void updatePlayerToState(MediaControlPanel mediaControlPanel, boolean z) {
        mediaControlPanel.getMediaViewController().setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
    }

    public static /* synthetic */ Unit onDesiredLocationChanged$default(MediaCarouselController mediaCarouselController, int i, MediaHostState mediaHostState, boolean z, long j, long j2, int i2, Object obj) {
        return mediaCarouselController.onDesiredLocationChanged(i, mediaHostState, z, (i2 & 8) != 0 ? 200 : j, (i2 & 16) != 0 ? 0 : j2);
    }

    public static /* synthetic */ void closeGuts$default(MediaCarouselController mediaCarouselController, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        mediaCarouselController.closeGuts(z);
    }

    public final void closeGuts(boolean z) {
        for (MediaControlPanel closeGuts : MediaPlayerData.INSTANCE.players()) {
            closeGuts.closeGuts(z);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r0 = r0.getMeasurementInput();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateCarouselSize() {
        /*
            r6 = this;
            com.android.systemui.media.MediaHostState r0 = r6.desiredHostState
            r1 = 0
            if (r0 != 0) goto L_0x0007
        L_0x0005:
            r0 = r1
            goto L_0x0012
        L_0x0007:
            com.android.systemui.util.animation.MeasurementInput r0 = r0.getMeasurementInput()
            if (r0 != 0) goto L_0x000e
            goto L_0x0005
        L_0x000e:
            int r0 = r0.getWidth()
        L_0x0012:
            com.android.systemui.media.MediaHostState r2 = r6.desiredHostState
            if (r2 != 0) goto L_0x0018
        L_0x0016:
            r2 = r1
            goto L_0x0023
        L_0x0018:
            com.android.systemui.util.animation.MeasurementInput r2 = r2.getMeasurementInput()
            if (r2 != 0) goto L_0x001f
            goto L_0x0016
        L_0x001f:
            int r2 = r2.getHeight()
        L_0x0023:
            int r3 = r6.carouselMeasureWidth
            if (r0 == r3) goto L_0x0029
            if (r0 != 0) goto L_0x002f
        L_0x0029:
            int r3 = r6.carouselMeasureHeight
            if (r2 == r3) goto L_0x0075
            if (r2 == 0) goto L_0x0075
        L_0x002f:
            r6.carouselMeasureWidth = r0
            r6.carouselMeasureHeight = r2
            android.content.Context r2 = r6.context
            android.content.res.Resources r2 = r2.getResources()
            int r3 = com.android.systemui.R$dimen.qs_media_padding
            int r2 = r2.getDimensionPixelSize(r3)
            int r2 = r2 + r0
            com.android.systemui.media.MediaHostState r3 = r6.desiredHostState
            if (r3 != 0) goto L_0x0046
        L_0x0044:
            r3 = r1
            goto L_0x0051
        L_0x0046:
            com.android.systemui.util.animation.MeasurementInput r3 = r3.getMeasurementInput()
            if (r3 != 0) goto L_0x004d
            goto L_0x0044
        L_0x004d:
            int r3 = r3.getWidthMeasureSpec()
        L_0x0051:
            com.android.systemui.media.MediaHostState r4 = r6.desiredHostState
            if (r4 != 0) goto L_0x0057
        L_0x0055:
            r4 = r1
            goto L_0x0062
        L_0x0057:
            com.android.systemui.util.animation.MeasurementInput r4 = r4.getMeasurementInput()
            if (r4 != 0) goto L_0x005e
            goto L_0x0055
        L_0x005e:
            int r4 = r4.getHeightMeasureSpec()
        L_0x0062:
            com.android.systemui.media.MediaScrollView r5 = r6.mediaCarousel
            r5.measure(r3, r4)
            com.android.systemui.media.MediaScrollView r3 = r6.mediaCarousel
            int r4 = r3.getMeasuredHeight()
            r3.layout(r1, r1, r0, r4)
            com.android.systemui.media.MediaCarouselScrollHandler r6 = r6.mediaCarouselScrollHandler
            r6.setPlayerWidthPlusPadding(r2)
        L_0x0075:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaCarouselController.updateCarouselSize():void");
    }

    public final void logSmartspaceImpression(boolean z) {
        int visibleMediaIndex = this.mediaCarouselScrollHandler.getVisibleMediaIndex();
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.players().size() > visibleMediaIndex) {
            MediaControlPanel mediaControlPanel = (MediaControlPanel) CollectionsKt___CollectionsKt.elementAt(mediaPlayerData.players(), visibleMediaIndex);
            if (mediaPlayerData.hasActiveMediaOrRecommendationCard() || z) {
                logSmartspaceCardReported$default(this, 800, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{mediaControlPanel.getSurfaceForSmartspaceLogging()}, 0, 0, 0, 0, false, 496, (Object) null);
                mediaControlPanel.mIsImpressed = true;
            }
        }
    }

    public static /* synthetic */ void logSmartspaceCardReported$default(MediaCarouselController mediaCarouselController, int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, boolean z, int i8, Object obj) {
        int i9;
        int i10 = i8;
        int i11 = (i10 & 16) != 0 ? 0 : i4;
        int i12 = (i10 & 32) != 0 ? 0 : i5;
        if ((i10 & 64) != 0) {
            i9 = mediaCarouselController.mediaCarouselScrollHandler.getVisibleMediaIndex();
        } else {
            MediaCarouselController mediaCarouselController2 = mediaCarouselController;
            i9 = i6;
        }
        mediaCarouselController.logSmartspaceCardReported(i, i2, i3, iArr, i11, i12, i9, (i10 & 128) != 0 ? 0 : i7, (i10 & 256) != 0 ? false : z);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3, @NotNull int[] iArr, int i4, int i5, int i6, int i7, boolean z) {
        int i8;
        int[] iArr2 = iArr;
        int i9 = i6;
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        if (mediaPlayerData.players().size() > i9) {
            MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) CollectionsKt___CollectionsKt.elementAt(mediaPlayerData.playerKeys(), i9);
            if (mediaSortKey.isSsMediaRec() || this.mediaManager.getSmartspaceMediaData().isActive() || mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() != null) {
                int childCount = this.mediaContent.getChildCount();
                int i10 = 0;
                int length = iArr2.length;
                while (i10 < length) {
                    int i11 = iArr2[i10];
                    i10++;
                    int i12 = z ? -1 : i9;
                    if (mediaSortKey.isSsMediaRec()) {
                        i8 = 15;
                    } else {
                        i8 = mediaSortKey.isSsReactivated() ? 43 : 31;
                    }
                    int i13 = i11;
                    int i14 = length;
                    SysUiStatsLog.write(352, i, i2, 0, i11, i12, childCount, i8, i3, i4, i5, i7, (byte[]) null);
                    if (MediaCarouselControllerKt.DEBUG) {
                        Log.d("MediaCarouselController", "Log Smartspace card event id: " + i + " instance id: " + i2 + " surface: " + i13 + " rank: " + i9 + " cardinality: " + childCount + " isRecommendationCard: " + mediaSortKey.isSsMediaRec() + " isSsReactivated: " + mediaSortKey.isSsReactivated() + "uid: " + i3 + " interactedSubcardRank: " + i4 + " interactedSubcardCardinality: " + i5 + " received_latency_millis: " + i7);
                    } else {
                        int i15 = i;
                        int i16 = i2;
                        int i17 = i3;
                        int i18 = i4;
                        int i19 = i5;
                        int i20 = i7;
                    }
                    length = i14;
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        Iterator it;
        Iterator it2 = MediaPlayerData.INSTANCE.players().iterator();
        int i = 0;
        while (it2.hasNext()) {
            Object next = it2.next();
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            MediaControlPanel mediaControlPanel = (MediaControlPanel) next;
            if (mediaControlPanel.mIsImpressed) {
                it = it2;
                logSmartspaceCardReported$default(this, 761, mediaControlPanel.mSmartspaceId, mediaControlPanel.mUid, new int[]{mediaControlPanel.getSurfaceForSmartspaceLogging()}, 0, 0, i, 0, true, 176, (Object) null);
                mediaControlPanel.mIsImpressed = false;
            } else {
                it = it2;
            }
            i = i2;
            it2 = it;
        }
        this.logger.logSwipeDismiss();
        this.mediaManager.onSwipeToDismiss();
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("keysNeedRemoval: ", this.keysNeedRemoval));
        MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
        printWriter.println(Intrinsics.stringPlus("dataKeys: ", mediaPlayerData.dataKeys()));
        printWriter.println(Intrinsics.stringPlus("playerSortKeys: ", mediaPlayerData.playerKeys()));
        printWriter.println(Intrinsics.stringPlus("smartspaceMediaData: ", mediaPlayerData.getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        printWriter.println(Intrinsics.stringPlus("shouldPrioritizeSs: ", Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
        printWriter.println("current size: " + this.currentCarouselWidth + " x " + this.currentCarouselHeight);
        printWriter.println(Intrinsics.stringPlus("location: ", Integer.valueOf(this.desiredLocation)));
        StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        MediaHostState mediaHostState = this.desiredHostState;
        Boolean bool = null;
        sb.append(mediaHostState == null ? null : Float.valueOf(mediaHostState.getExpansion()));
        sb.append(", only active ");
        MediaHostState mediaHostState2 = this.desiredHostState;
        if (mediaHostState2 != null) {
            bool = Boolean.valueOf(mediaHostState2.getShowsOnlyActiveMedia());
        }
        sb.append(bool);
        printWriter.println(sb.toString());
    }
}
