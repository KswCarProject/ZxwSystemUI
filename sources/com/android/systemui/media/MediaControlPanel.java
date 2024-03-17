package com.android.systemui.media;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.PendingIntent;
import android.app.WallpaperColors;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.TransitionDrawable;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.settingslib.widget.AdaptiveIcon;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.R$anim;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.monet.Style;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.time.SystemClock;
import dagger.Lazy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;

public class MediaControlPanel {
    public static final List<Integer> SEMANTIC_ACTIONS_ALL;
    public static final List<Integer> SEMANTIC_ACTIONS_COMPACT;
    public static final List<Integer> SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING;
    public static final Intent SETTINGS_INTENT = new Intent("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
    public final ActivityIntentHelper mActivityIntentHelper;
    public final ActivityStarter mActivityStarter;
    public int mArtworkBoundId = 0;
    public int mArtworkNextBindRequestId = 0;
    public final Executor mBackgroundExecutor;
    public final BroadcastSender mBroadcastSender;
    public ColorSchemeTransition mColorSchemeTransition;
    public Context mContext;
    public MediaController mController;
    public final SeekBarViewModel.EnabledChangeListener mEnabledChangeListener = new MediaControlPanel$$ExternalSyntheticLambda7(this);
    public final FalsingManager mFalsingManager;
    public InstanceId mInstanceId;
    public boolean mIsArtworkBound = false;
    public boolean mIsImpressed = false;
    public boolean mIsScrubbing = false;
    public boolean mIsSeekBarEnabled = false;
    public String mKey;
    public final KeyguardStateController mKeyguardStateController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public MediaUiEventLogger mLogger;
    public final Executor mMainExecutor;
    public MediaCarouselController mMediaCarouselController;
    public MediaData mMediaData;
    public Lazy<MediaDataManager> mMediaDataManagerLazy;
    public final MediaOutputDialogFactory mMediaOutputDialogFactory;
    public MediaViewController mMediaViewController;
    public MediaViewHolder mMediaViewHolder;
    public MetadataAnimationHandler mMetadataAnimationHandler;
    public String mPackageName;
    public Drawable mPrevArtwork = null;
    public SmartspaceMediaData mRecommendationData;
    public RecommendationViewHolder mRecommendationViewHolder;
    public final SeekBarViewModel.ScrubbingChangeListener mScrubbingChangeListener = new MediaControlPanel$$ExternalSyntheticLambda6(this);
    public SeekBarObserver mSeekBarObserver;
    public final SeekBarViewModel mSeekBarViewModel;
    public int mSmartspaceId = -1;
    public int mSmartspaceMediaItemsCount;
    public SystemClock mSystemClock;
    public MediaSession.Token mToken;
    public int mUid = -1;

    static {
        int i = R$id.actionPlayPause;
        Integer valueOf = Integer.valueOf(i);
        int i2 = R$id.actionPrev;
        Integer valueOf2 = Integer.valueOf(i2);
        int i3 = R$id.actionNext;
        SEMANTIC_ACTIONS_COMPACT = List.of(valueOf, valueOf2, Integer.valueOf(i3));
        SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING = List.of(Integer.valueOf(i2), Integer.valueOf(i3));
        SEMANTIC_ACTIONS_ALL = List.of(Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(R$id.action0), Integer.valueOf(R$id.action1));
    }

    public MediaControlPanel(Context context, Executor executor, Executor executor2, ActivityStarter activityStarter, BroadcastSender broadcastSender, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController, FalsingManager falsingManager, SystemClock systemClock, MediaUiEventLogger mediaUiEventLogger, KeyguardStateController keyguardStateController, ActivityIntentHelper activityIntentHelper, NotificationLockscreenUserManager notificationLockscreenUserManager) {
        this.mContext = context;
        this.mBackgroundExecutor = executor;
        this.mMainExecutor = executor2;
        this.mActivityStarter = activityStarter;
        this.mBroadcastSender = broadcastSender;
        this.mSeekBarViewModel = seekBarViewModel;
        this.mMediaViewController = mediaViewController;
        this.mMediaDataManagerLazy = lazy;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mMediaCarouselController = mediaCarouselController;
        this.mFalsingManager = falsingManager;
        this.mSystemClock = systemClock;
        this.mLogger = mediaUiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mActivityIntentHelper = activityIntentHelper;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        seekBarViewModel.setLogSeek(new MediaControlPanel$$ExternalSyntheticLambda8(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0() {
        InstanceId instanceId;
        String str = this.mPackageName;
        if (!(str == null || (instanceId = this.mInstanceId) == null)) {
            this.mLogger.logSeek(this.mUid, str, instanceId);
        }
        logSmartspaceCardReported(760);
        return Unit.INSTANCE;
    }

    public void onDestroy() {
        if (this.mSeekBarObserver != null) {
            this.mSeekBarViewModel.getProgress().removeObserver(this.mSeekBarObserver);
        }
        this.mSeekBarViewModel.removeScrubbingChangeListener(this.mScrubbingChangeListener);
        this.mSeekBarViewModel.removeEnabledChangeListener(this.mEnabledChangeListener);
        this.mSeekBarViewModel.onDestroy();
        this.mMediaViewController.onDestroy();
    }

    public MediaViewHolder getMediaViewHolder() {
        return this.mMediaViewHolder;
    }

    public RecommendationViewHolder getRecommendationViewHolder() {
        return this.mRecommendationViewHolder;
    }

    public MediaViewController getMediaViewController() {
        return this.mMediaViewController;
    }

    public void setListening(boolean z) {
        this.mSeekBarViewModel.setListening(z);
    }

    public final void setIsScrubbing(boolean z) {
        MediaData mediaData = this.mMediaData;
        if (mediaData != null && mediaData.getSemanticActions() != null && z != this.mIsScrubbing) {
            this.mIsScrubbing = z;
            this.mMainExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda20(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsScrubbing$1() {
        updateDisplayForScrubbingChange(this.mMediaData.getSemanticActions());
    }

    public final void setIsSeekBarEnabled(boolean z) {
        if (z != this.mIsSeekBarEnabled) {
            this.mIsSeekBarEnabled = z;
            updateSeekBarVisibility();
        }
    }

    public void attachPlayer(MediaViewHolder mediaViewHolder) {
        this.mMediaViewHolder = mediaViewHolder;
        TransitionLayout player = mediaViewHolder.getPlayer();
        this.mSeekBarObserver = new SeekBarObserver(mediaViewHolder);
        this.mSeekBarViewModel.getProgress().observeForever(this.mSeekBarObserver);
        this.mSeekBarViewModel.attachTouchHandlers(mediaViewHolder.getSeekBar());
        this.mSeekBarViewModel.setScrubbingChangeListener(this.mScrubbingChangeListener);
        this.mSeekBarViewModel.setEnabledChangeListener(this.mEnabledChangeListener);
        this.mMediaViewController.attach(player, MediaViewController.TYPE.PLAYER);
        mediaViewHolder.getPlayer().setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda5(this));
        this.mMediaViewHolder.getAlbumView().setLayerType(2, (Paint) null);
        TextView titleText = this.mMediaViewHolder.getTitleText();
        TextView artistText = this.mMediaViewHolder.getArtistText();
        AnimatorSet loadAnimator = loadAnimator(R$anim.media_metadata_enter, Interpolators.EMPHASIZED_DECELERATE, titleText, artistText);
        AnimatorSet loadAnimator2 = loadAnimator(R$anim.media_metadata_exit, Interpolators.EMPHASIZED_ACCELERATE, titleText, artistText);
        this.mColorSchemeTransition = new ColorSchemeTransition(this.mContext, this.mMediaViewHolder);
        this.mMetadataAnimationHandler = new MetadataAnimationHandler(loadAnimator2, loadAnimator);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$attachPlayer$2(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    @VisibleForTesting
    public AnimatorSet loadAnimator(int i, Interpolator interpolator, View... viewArr) {
        ArrayList arrayList = new ArrayList();
        for (View target : viewArr) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.mContext, i);
            animatorSet.getChildAnimations().get(0).setInterpolator(interpolator);
            animatorSet.setTarget(target);
            arrayList.add(animatorSet);
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(arrayList);
        return animatorSet2;
    }

    public void attachRecommendation(RecommendationViewHolder recommendationViewHolder) {
        this.mRecommendationViewHolder = recommendationViewHolder;
        this.mMediaViewController.attach(recommendationViewHolder.getRecommendations(), MediaViewController.TYPE.RECOMMENDATION);
        this.mRecommendationViewHolder.getRecommendations().setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$attachRecommendation$3(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    public void bindPlayer(MediaData mediaData, String str) {
        if (this.mMediaViewHolder != null) {
            Trace.beginSection("MediaControlPanel#bindPlayer<" + str + ">");
            this.mKey = str;
            this.mMediaData = mediaData;
            MediaSession.Token token = mediaData.getToken();
            this.mPackageName = mediaData.getPackageName();
            int appUid = mediaData.getAppUid();
            this.mUid = appUid;
            if (this.mSmartspaceId == -1) {
                this.mSmartspaceId = SmallHash.hash(appUid + ((int) this.mSystemClock.currentTimeMillis()));
            }
            this.mInstanceId = mediaData.getInstanceId();
            MediaSession.Token token2 = this.mToken;
            if (token2 == null || !token2.equals(token)) {
                this.mToken = token;
            }
            if (this.mToken != null) {
                this.mController = new MediaController(this.mContext, this.mToken);
            } else {
                this.mController = null;
            }
            PendingIntent clickIntent = mediaData.getClickIntent();
            if (clickIntent != null) {
                this.mMediaViewHolder.getPlayer().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda9(this, clickIntent));
            }
            this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda10(this, getController()));
            bindOutputSwitcherChip(mediaData);
            bindGutsMenuForPlayer(mediaData);
            bindPlayerContentDescription(mediaData);
            bindScrubbingTime(mediaData);
            bindActionButtons(mediaData);
            bindArtworkAndColors(mediaData, str, bindSongMetadata(mediaData));
            if (!this.mMetadataAnimationHandler.isRunning()) {
                this.mMediaViewController.refreshState();
            }
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$4(PendingIntent pendingIntent, View view) {
        if (!this.mFalsingManager.isFalseTap(1) && !this.mMediaViewController.isGutsVisible()) {
            this.mLogger.logTapContentView(this.mUid, this.mPackageName, this.mInstanceId);
            logSmartspaceCardReported(760);
            if (this.mKeyguardStateController.isShowing() && this.mActivityIntentHelper.wouldShowOverLockscreen(pendingIntent.getIntent(), this.mLockscreenUserManager.getCurrentUserId())) {
                this.mActivityStarter.startActivity(pendingIntent.getIntent(), true, (ActivityLaunchAnimator.Controller) null, true);
            } else {
                this.mActivityStarter.postStartActivityDismissingKeyguard(pendingIntent, buildLaunchAnimatorController(this.mMediaViewHolder.getPlayer()));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$5(MediaController mediaController) {
        this.mSeekBarViewModel.updateController(mediaController);
    }

    public final void bindOutputSwitcherChip(MediaData mediaData) {
        ViewGroup seamless = this.mMediaViewHolder.getSeamless();
        boolean z = false;
        seamless.setVisibility(0);
        ImageView seamlessIcon = this.mMediaViewHolder.getSeamlessIcon();
        TextView seamlessText = this.mMediaViewHolder.getSeamlessText();
        MediaDeviceData device = mediaData.getDevice();
        if ((device != null && !device.getEnabled()) || mediaData.getResumption()) {
            z = true;
        }
        this.mMediaViewHolder.getSeamlessButton().setAlpha(z ? 0.38f : 1.0f);
        seamless.setEnabled(!z);
        CharSequence string = this.mContext.getString(R$string.media_seamless_other_device);
        if (device != null) {
            Drawable icon = device.getIcon();
            if (icon instanceof AdaptiveIcon) {
                AdaptiveIcon adaptiveIcon = (AdaptiveIcon) icon;
                adaptiveIcon.setBackgroundColor(this.mColorSchemeTransition.getBgColor());
                seamlessIcon.setImageDrawable(adaptiveIcon);
            } else {
                seamlessIcon.setImageDrawable(icon);
            }
            string = device.getName();
        } else {
            seamlessIcon.setImageResource(R$drawable.ic_media_home_devices);
        }
        seamlessText.setText(string);
        seamless.setContentDescription(string);
        seamless.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda19(this, device));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindOutputSwitcherChip$6(MediaDeviceData mediaDeviceData, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mLogger.logOpenOutputSwitcher(this.mUid, this.mPackageName, this.mInstanceId);
            if (mediaDeviceData.getIntent() == null) {
                this.mMediaOutputDialogFactory.create(this.mPackageName, true, this.mMediaViewHolder.getSeamlessButton());
            } else if (mediaDeviceData.getIntent().isActivity()) {
                this.mActivityStarter.startActivity(mediaDeviceData.getIntent().getIntent(), true);
            } else {
                try {
                    mediaDeviceData.getIntent().send();
                } catch (PendingIntent.CanceledException unused) {
                    Log.e("MediaControlPanel", "Device pending intent was canceled");
                }
            }
        }
    }

    public final void bindGutsMenuForPlayer(MediaData mediaData) {
        bindGutsMenuCommon(mediaData.isClearable(), mediaData.getApp(), this.mMediaViewHolder.getGutsViewHolder(), new MediaControlPanel$$ExternalSyntheticLambda21(this, mediaData));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGutsMenuForPlayer$7(MediaData mediaData) {
        if (this.mKey != null) {
            closeGuts();
            if (!this.mMediaDataManagerLazy.get().dismissMediaData(this.mKey, MediaViewController.GUTS_ANIMATION_DURATION + 100)) {
                Log.w("MediaControlPanel", "Manager failed to dismiss media " + this.mKey);
                this.mMediaCarouselController.removePlayer(this.mKey, false, false);
                return;
            }
            return;
        }
        Log.w("MediaControlPanel", "Dismiss media with null notification. Token uid=" + mediaData.getToken().getUid());
    }

    public final boolean bindSongMetadata(MediaData mediaData) {
        return this.mMetadataAnimationHandler.setNext(Pair.create(mediaData.getSong(), mediaData.getArtist()), new MediaControlPanel$$ExternalSyntheticLambda11(this, this.mMediaViewHolder.getTitleText(), mediaData, this.mMediaViewHolder.getArtistText()), new MediaControlPanel$$ExternalSyntheticLambda12(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$bindSongMetadata$8(TextView textView, MediaData mediaData, TextView textView2) {
        textView.setText(mediaData.getSong());
        textView2.setText(mediaData.getArtist());
        this.mMediaViewController.refreshState();
        return Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$bindSongMetadata$9() {
        this.mMediaViewController.refreshState();
        return Unit.INSTANCE;
    }

    public final void bindPlayerContentDescription(MediaData mediaData) {
        CharSequence charSequence;
        if (this.mMediaViewHolder != null) {
            if (this.mMediaViewController.isGutsVisible()) {
                charSequence = this.mMediaViewHolder.getGutsViewHolder().getGutsText().getText();
            } else if (mediaData != null) {
                charSequence = this.mContext.getString(R$string.controls_media_playing_item_description, new Object[]{mediaData.getSong(), mediaData.getArtist(), mediaData.getApp()});
            } else {
                charSequence = null;
            }
            this.mMediaViewHolder.getPlayer().setContentDescription(charSequence);
        }
    }

    public final void bindRecommendationContentDescription(SmartspaceMediaData smartspaceMediaData) {
        CharSequence charSequence;
        if (this.mRecommendationViewHolder != null) {
            if (this.mMediaViewController.isGutsVisible()) {
                charSequence = this.mRecommendationViewHolder.getGutsViewHolder().getGutsText().getText();
            } else if (smartspaceMediaData != null) {
                Context context = this.mContext;
                charSequence = context.getString(R$string.controls_media_smartspace_rec_description, new Object[]{smartspaceMediaData.getAppName(context)});
            } else {
                charSequence = null;
            }
            this.mRecommendationViewHolder.getRecommendations().setContentDescription(charSequence);
        }
    }

    public final void bindArtworkAndColors(MediaData mediaData, String str, boolean z) {
        int hashCode = mediaData.hashCode();
        String str2 = "MediaControlPanel#bindArtworkAndColors<" + str + ">";
        Trace.beginAsyncSection(str2, hashCode);
        int i = this.mArtworkNextBindRequestId;
        this.mArtworkNextBindRequestId = i + 1;
        if (z) {
            this.mIsArtworkBound = false;
        }
        this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda17(this, mediaData, this.mMediaViewHolder.getAlbumView().getMeasuredWidth(), this.mMediaViewHolder.getAlbumView().getMeasuredHeight(), i, str2, hashCode, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindArtworkAndColors$11(MediaData mediaData, int i, int i2, int i3, String str, int i4, boolean z) {
        ColorScheme colorScheme;
        ColorDrawable colorDrawable;
        boolean z2;
        Icon artwork = mediaData.getArtwork();
        if (artwork != null) {
            ColorScheme colorScheme2 = new ColorScheme(WallpaperColors.fromBitmap(artwork.getBitmap()), true, Style.CONTENT);
            colorDrawable = getScaledBackground(artwork, i, i2);
            z2 = true;
            colorScheme = colorScheme2;
        } else {
            int i5 = i;
            int i6 = i2;
            ColorDrawable colorDrawable2 = new ColorDrawable(0);
            try {
                z2 = false;
                colorDrawable = colorDrawable2;
                colorScheme = new ColorScheme(WallpaperColors.fromDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.getPackageName())), true, Style.CONTENT);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("MediaControlPanel", "Cannot find icon for package " + mediaData.getPackageName(), e);
                colorScheme = null;
                z2 = false;
                colorDrawable = colorDrawable2;
            }
        }
        this.mMainExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda25(this, i3, str, i4, z, z2, colorDrawable, i, i2, colorScheme, mediaData));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindArtworkAndColors$10(int i, String str, int i2, boolean z, boolean z2, Drawable drawable, int i3, int i4, ColorScheme colorScheme, MediaData mediaData) {
        if (i < this.mArtworkBoundId) {
            Trace.endAsyncSection(str, i2);
            return;
        }
        this.mArtworkBoundId = i;
        ImageView albumView = this.mMediaViewHolder.getAlbumView();
        albumView.setPadding(0, 0, 0, 0);
        if (z || (!this.mIsArtworkBound && z2)) {
            if (this.mPrevArtwork == null) {
                albumView.setImageDrawable(drawable);
            } else {
                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{this.mPrevArtwork, drawable});
                scaleTransitionDrawableLayer(transitionDrawable, 0, i3, i4);
                scaleTransitionDrawableLayer(transitionDrawable, 1, i3, i4);
                transitionDrawable.setLayerGravity(0, 17);
                transitionDrawable.setLayerGravity(1, 17);
                transitionDrawable.setCrossFadeEnabled(!z2);
                albumView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(z2 ? 333 : 80);
            }
            this.mPrevArtwork = drawable;
            this.mIsArtworkBound = z2;
        }
        this.mColorSchemeTransition.updateColorScheme(colorScheme, this.mIsArtworkBound);
        ImageView appIcon = this.mMediaViewHolder.getAppIcon();
        appIcon.clearColorFilter();
        if (mediaData.getAppIcon() == null || mediaData.getResumption()) {
            appIcon.setColorFilter(getGrayscaleFilter());
            try {
                appIcon.setImageDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.getPackageName()));
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("MediaControlPanel", "Cannot find icon for package " + mediaData.getPackageName(), e);
                appIcon.setImageResource(R$drawable.ic_music_note);
            }
        } else {
            appIcon.setImageIcon(mediaData.getAppIcon());
            appIcon.setColorFilter(this.mColorSchemeTransition.getAccentPrimary().getTargetColor());
        }
        Trace.endAsyncSection(str, i2);
    }

    public final void scaleTransitionDrawableLayer(TransitionDrawable transitionDrawable, int i, int i2, int i3) {
        Drawable drawable = transitionDrawable.getDrawable(i);
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth != 0 && intrinsicHeight != 0 && i2 != 0 && i3 != 0) {
                float f = (float) intrinsicWidth;
                float f2 = (float) intrinsicHeight;
                float f3 = (float) i2;
                float f4 = (float) i3;
                float f5 = f / f2 > f3 / f4 ? f4 / f2 : f3 / f;
                transitionDrawable.setLayerSize(i, (int) (f * f5), (int) (f5 * f2));
            }
        }
    }

    public final void bindActionButtons(MediaData mediaData) {
        MediaButton semanticActions = mediaData.getSemanticActions();
        ArrayList<ImageButton> arrayList = new ArrayList<>();
        for (Integer intValue : MediaViewHolder.Companion.getGenericButtonIds()) {
            arrayList.add(this.mMediaViewHolder.getAction(intValue.intValue()));
        }
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        if (semanticActions != null) {
            for (ImageButton imageButton : arrayList) {
                setVisibleAndAlpha(collapsedLayout, imageButton.getId(), false);
                setVisibleAndAlpha(expandedLayout, imageButton.getId(), false);
            }
            for (Integer intValue2 : SEMANTIC_ACTIONS_ALL) {
                int intValue3 = intValue2.intValue();
                setSemanticButton(this.mMediaViewHolder.getAction(intValue3), semanticActions.getActionById(intValue3), semanticActions);
            }
        } else {
            for (Integer intValue4 : SEMANTIC_ACTIONS_COMPACT) {
                int intValue5 = intValue4.intValue();
                setVisibleAndAlpha(collapsedLayout, intValue5, false);
                setVisibleAndAlpha(expandedLayout, intValue5, false);
            }
            List<Integer> actionsToShowInCompact = mediaData.getActionsToShowInCompact();
            List<MediaAction> actions = mediaData.getActions();
            int i = 0;
            while (i < actions.size() && i < arrayList.size()) {
                setGenericButton((ImageButton) arrayList.get(i), actions.get(i), collapsedLayout, expandedLayout, actionsToShowInCompact.contains(Integer.valueOf(i)));
                i++;
            }
            while (i < arrayList.size()) {
                setGenericButton((ImageButton) arrayList.get(i), (MediaAction) null, collapsedLayout, expandedLayout, false);
                i++;
            }
        }
        updateSeekBarVisibility();
    }

    public final void updateSeekBarVisibility() {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        int i = R$id.media_progress_bar;
        expandedLayout.setVisibility(i, getSeekBarVisibility());
        expandedLayout.setAlpha(i, this.mIsSeekBarEnabled ? 1.0f : 0.0f);
    }

    public final int getSeekBarVisibility() {
        if (this.mIsSeekBarEnabled) {
            return 0;
        }
        return areAnyExpandedBottomActionsVisible() ? 4 : 8;
    }

    public final boolean areAnyExpandedBottomActionsVisible() {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        for (Integer intValue : MediaViewHolder.Companion.getExpandedBottomActionIds()) {
            if (expandedLayout.getVisibility(intValue.intValue()) == 0) {
                return true;
            }
        }
        return false;
    }

    public final void setGenericButton(ImageButton imageButton, MediaAction mediaAction, ConstraintSet constraintSet, ConstraintSet constraintSet2, boolean z) {
        bindButtonCommon(imageButton, mediaAction);
        boolean z2 = true;
        boolean z3 = mediaAction != null;
        setVisibleAndAlpha(constraintSet2, imageButton.getId(), z3);
        int id = imageButton.getId();
        if (!z3 || !z) {
            z2 = false;
        }
        setVisibleAndAlpha(constraintSet, id, z2);
    }

    public final void setSemanticButton(ImageButton imageButton, MediaAction mediaAction, MediaButton mediaButton) {
        AnimationBindHandler animationBindHandler;
        if (imageButton.getTag() == null) {
            animationBindHandler = new AnimationBindHandler();
            imageButton.setTag(animationBindHandler);
        } else {
            animationBindHandler = (AnimationBindHandler) imageButton.getTag();
        }
        animationBindHandler.tryExecute(new MediaControlPanel$$ExternalSyntheticLambda22(this, imageButton, mediaAction, animationBindHandler, mediaButton));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$setSemanticButton$12(ImageButton imageButton, MediaAction mediaAction, AnimationBindHandler animationBindHandler, MediaButton mediaButton) {
        bindButtonWithAnimations(imageButton, mediaAction, animationBindHandler);
        setSemanticButtonVisibleAndAlpha(imageButton.getId(), mediaAction, mediaButton);
        return Unit.INSTANCE;
    }

    public final void bindButtonWithAnimations(ImageButton imageButton, MediaAction mediaAction, AnimationBindHandler animationBindHandler) {
        if (mediaAction == null) {
            animationBindHandler.unregisterAll();
            clearButton(imageButton);
        } else if (animationBindHandler.updateRebindId(mediaAction.getRebindId())) {
            animationBindHandler.unregisterAll();
            animationBindHandler.tryRegister(mediaAction.getIcon());
            animationBindHandler.tryRegister(mediaAction.getBackground());
            bindButtonCommon(imageButton, mediaAction);
        }
    }

    public final void bindButtonCommon(ImageButton imageButton, MediaAction mediaAction) {
        if (mediaAction != null) {
            Drawable icon = mediaAction.getIcon();
            imageButton.setImageDrawable(icon);
            imageButton.setContentDescription(mediaAction.getContentDescription());
            Drawable background = mediaAction.getBackground();
            imageButton.setBackground(background);
            Runnable action = mediaAction.getAction();
            if (action == null) {
                imageButton.setEnabled(false);
                return;
            }
            imageButton.setEnabled(true);
            imageButton.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda27(this, imageButton, action, icon, background));
            return;
        }
        clearButton(imageButton);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindButtonCommon$13(ImageButton imageButton, Runnable runnable, Drawable drawable, Drawable drawable2, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mLogger.logTapAction(imageButton.getId(), this.mUid, this.mPackageName, this.mInstanceId);
            logSmartspaceCardReported(760);
            runnable.run();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            if (drawable2 instanceof Animatable) {
                ((Animatable) drawable2).start();
            }
        }
    }

    public final void clearButton(ImageButton imageButton) {
        imageButton.setImageDrawable((Drawable) null);
        imageButton.setContentDescription((CharSequence) null);
        imageButton.setEnabled(false);
        imageButton.setBackground((Drawable) null);
    }

    public final void setSemanticButtonVisibleAndAlpha(int i, MediaAction mediaAction, MediaButton mediaButton) {
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        boolean contains = SEMANTIC_ACTIONS_COMPACT.contains(Integer.valueOf(i));
        boolean z = true;
        boolean z2 = mediaAction != null && !(scrubbingTimeViewsEnabled(mediaButton) && SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.contains(Integer.valueOf(i)) && this.mIsScrubbing);
        setVisibleAndAlpha(expandedLayout, i, z2, ((i != R$id.actionPrev || !mediaButton.getReservePrev()) && (i != R$id.actionNext || !mediaButton.getReserveNext())) ? 8 : 4);
        if (!z2 || !contains) {
            z = false;
        }
        setVisibleAndAlpha(collapsedLayout, i, z);
    }

    public final void updateDisplayForScrubbingChange(MediaButton mediaButton) {
        bindScrubbingTime(this.mMediaData);
        SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.forEach(new MediaControlPanel$$ExternalSyntheticLambda26(this, mediaButton));
        if (!this.mMetadataAnimationHandler.isRunning()) {
            this.mMediaViewController.refreshState();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDisplayForScrubbingChange$14(MediaButton mediaButton, Integer num) {
        setSemanticButtonVisibleAndAlpha(num.intValue(), mediaButton.getActionById(num.intValue()), mediaButton);
    }

    public final void bindScrubbingTime(MediaData mediaData) {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        int id = this.mMediaViewHolder.getScrubbingElapsedTimeView().getId();
        int id2 = this.mMediaViewHolder.getScrubbingTotalTimeView().getId();
        boolean z = scrubbingTimeViewsEnabled(mediaData.getSemanticActions()) && this.mIsScrubbing;
        setVisibleAndAlpha(expandedLayout, id, z);
        setVisibleAndAlpha(expandedLayout, id2, z);
        setVisibleAndAlpha(collapsedLayout, id, false);
        setVisibleAndAlpha(collapsedLayout, id2, false);
    }

    public final boolean scrubbingTimeViewsEnabled(MediaButton mediaButton) {
        return mediaButton != null && SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.stream().allMatch(new MediaControlPanel$$ExternalSyntheticLambda24(mediaButton));
    }

    public static /* synthetic */ boolean lambda$scrubbingTimeViewsEnabled$15(MediaButton mediaButton, Integer num) {
        return mediaButton.getActionById(num.intValue()) != null;
    }

    public final ActivityLaunchAnimator.Controller buildLaunchAnimatorController(TransitionLayout transitionLayout) {
        if (transitionLayout.getParent() instanceof ViewGroup) {
            return new GhostedViewLaunchAnimatorController(transitionLayout, 31) {
                public float getCurrentTopCornerRadius() {
                    return MediaControlPanel.this.mContext.getResources().getDimension(R$dimen.notification_corner_radius);
                }

                public float getCurrentBottomCornerRadius() {
                    return getCurrentTopCornerRadius();
                }
            };
        }
        Log.wtf("MediaControlPanel", "Skipping player animation as it is not attached to a ViewGroup", new Exception());
        return null;
    }

    public void bindRecommendation(SmartspaceMediaData smartspaceMediaData) {
        char c;
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        if (this.mRecommendationViewHolder != null) {
            if (!smartspaceMediaData.isValid()) {
                Log.e("MediaControlPanel", "Received an invalid recommendation list; returning");
                return;
            }
            Trace.beginSection("MediaControlPanel#bindRecommendation<" + smartspaceMediaData.getPackageName() + ">");
            this.mRecommendationData = smartspaceMediaData2;
            this.mSmartspaceId = SmallHash.hash(smartspaceMediaData.getTargetId());
            this.mPackageName = smartspaceMediaData.getPackageName();
            this.mInstanceId = smartspaceMediaData.getInstanceId();
            try {
                char c2 = 0;
                ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(smartspaceMediaData.getPackageName(), 0);
                this.mUid = applicationInfo.uid;
                CharSequence appName = smartspaceMediaData2.getAppName(this.mContext);
                if (appName == null) {
                    Log.w("MediaControlPanel", "Fail to get media recommendation's app name");
                    Trace.endSection();
                    return;
                }
                Drawable applicationIcon = this.mContext.getPackageManager().getApplicationIcon(applicationInfo);
                this.mRecommendationViewHolder.getCardIcon().setImageDrawable(applicationIcon);
                fetchAndUpdateRecommendationColors(applicationIcon);
                setSmartspaceRecItemOnClickListener(this.mRecommendationViewHolder.getRecommendations(), smartspaceMediaData.getCardAction(), -1);
                bindRecommendationContentDescription(smartspaceMediaData);
                List<ImageView> mediaCoverItems = this.mRecommendationViewHolder.getMediaCoverItems();
                List<ViewGroup> mediaCoverContainers = this.mRecommendationViewHolder.getMediaCoverContainers();
                List<SmartspaceAction> validRecommendations = smartspaceMediaData.getValidRecommendations();
                int i = 0;
                boolean z = false;
                boolean z2 = false;
                while (i < 3) {
                    SmartspaceAction smartspaceAction = validRecommendations.get(i);
                    ImageView imageView = mediaCoverItems.get(i);
                    imageView.setImageIcon(smartspaceAction.getIcon());
                    ViewGroup viewGroup = mediaCoverContainers.get(i);
                    setSmartspaceRecItemOnClickListener(viewGroup, smartspaceAction, i);
                    viewGroup.setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda0());
                    String str = "";
                    String string = smartspaceAction.getExtras().getString("artist_name", str);
                    if (string.isEmpty()) {
                        Context context = this.mContext;
                        int i2 = R$string.controls_media_smartspace_rec_item_no_artist_description;
                        Object[] objArr = new Object[2];
                        objArr[c2] = smartspaceAction.getTitle();
                        objArr[1] = appName;
                        imageView.setContentDescription(context.getString(i2, objArr));
                        c = c2;
                    } else {
                        c = 0;
                        imageView.setContentDescription(this.mContext.getString(R$string.controls_media_smartspace_rec_item_description, new Object[]{smartspaceAction.getTitle(), string, appName}));
                    }
                    CharSequence title = smartspaceAction.getTitle();
                    z |= !TextUtils.isEmpty(title);
                    this.mRecommendationViewHolder.getMediaTitles().get(i).setText(title);
                    CharSequence subtitle = TextUtils.isEmpty(title) ^ true ? smartspaceAction.getSubtitle() : str;
                    z2 |= !TextUtils.isEmpty(subtitle);
                    this.mRecommendationViewHolder.getMediaSubtitles().get(i).setText(subtitle);
                    i++;
                    c2 = c;
                }
                this.mSmartspaceMediaItemsCount = 3;
                ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
                this.mRecommendationViewHolder.getMediaTitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda1(this, expandedLayout, z));
                this.mRecommendationViewHolder.getMediaSubtitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda2(this, expandedLayout, z2));
                bindGutsMenuCommon(true, appName.toString(), this.mRecommendationViewHolder.getGutsViewHolder(), new MediaControlPanel$$ExternalSyntheticLambda3(this, smartspaceMediaData2));
                this.mController = null;
                MetadataAnimationHandler metadataAnimationHandler = this.mMetadataAnimationHandler;
                if (metadataAnimationHandler == null || !metadataAnimationHandler.isRunning()) {
                    this.mMediaViewController.refreshState();
                }
                Trace.endSection();
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("MediaControlPanel", "Fail to get media recommendation's app info", e);
                Trace.endSection();
            }
        }
    }

    public static /* synthetic */ boolean lambda$bindRecommendation$16(View view) {
        View view2 = (View) view.getParent();
        if (view2 == null) {
            return true;
        }
        view2.performLongClick();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindRecommendation$17(ConstraintSet constraintSet, boolean z, TextView textView) {
        setVisibleAndAlpha(constraintSet, textView.getId(), z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindRecommendation$18(ConstraintSet constraintSet, boolean z, TextView textView) {
        setVisibleAndAlpha(constraintSet, textView.getId(), z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindRecommendation$19(SmartspaceMediaData smartspaceMediaData) {
        closeGuts();
        this.mMediaDataManagerLazy.get().dismissSmartspaceRecommendation(smartspaceMediaData.getTargetId(), MediaViewController.GUTS_ANIMATION_DURATION + 100);
        Intent dismissIntent = smartspaceMediaData.getDismissIntent();
        if (dismissIntent == null) {
            Log.w("MediaControlPanel", "Cannot create dismiss action click action: extras missing dismiss_intent.");
        } else if (dismissIntent.getComponent() == null || !dismissIntent.getComponent().getClassName().equals("com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
            this.mBroadcastSender.sendBroadcast(dismissIntent);
        } else {
            this.mContext.startActivity(dismissIntent);
        }
    }

    public final void fetchAndUpdateRecommendationColors(Drawable drawable) {
        this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda18(this, drawable));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchAndUpdateRecommendationColors$21(Drawable drawable) {
        this.mMainExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda23(this, new ColorScheme(WallpaperColors.fromDrawable(drawable), true)));
    }

    /* renamed from: setRecommendationColors */
    public final void lambda$fetchAndUpdateRecommendationColors$20(ColorScheme colorScheme) {
        if (this.mRecommendationViewHolder != null) {
            int surfaceFromScheme = MediaColorSchemesKt.surfaceFromScheme(colorScheme);
            int textPrimaryFromScheme = MediaColorSchemesKt.textPrimaryFromScheme(colorScheme);
            int textSecondaryFromScheme = MediaColorSchemesKt.textSecondaryFromScheme(colorScheme);
            this.mRecommendationViewHolder.getRecommendations().setBackgroundTintList(ColorStateList.valueOf(surfaceFromScheme));
            this.mRecommendationViewHolder.getMediaTitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda28(textPrimaryFromScheme));
            this.mRecommendationViewHolder.getMediaSubtitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda29(textSecondaryFromScheme));
            this.mRecommendationViewHolder.getGutsViewHolder().setColors(colorScheme);
        }
    }

    public final void bindGutsMenuCommon(boolean z, String str, GutsViewHolder gutsViewHolder, Runnable runnable) {
        String str2;
        int i = 0;
        if (z) {
            str2 = this.mContext.getString(R$string.controls_media_close_session, new Object[]{str});
        } else {
            str2 = this.mContext.getString(R$string.controls_media_active_session);
        }
        gutsViewHolder.getGutsText().setText(str2);
        TextView dismissText = gutsViewHolder.getDismissText();
        if (!z) {
            i = 8;
        }
        dismissText.setVisibility(i);
        gutsViewHolder.getDismiss().setEnabled(z);
        gutsViewHolder.getDismiss().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda14(this, runnable));
        TextView cancelText = gutsViewHolder.getCancelText();
        if (z) {
            cancelText.setBackground(this.mContext.getDrawable(R$drawable.qs_media_outline_button));
        } else {
            cancelText.setBackground(this.mContext.getDrawable(R$drawable.qs_media_solid_button));
        }
        gutsViewHolder.getCancel().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda15(this));
        gutsViewHolder.setDismissible(z);
        gutsViewHolder.getSettings().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda16(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGutsMenuCommon$24(Runnable runnable, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            logSmartspaceCardReported(761);
            this.mLogger.logLongPressDismiss(this.mUid, this.mPackageName, this.mInstanceId);
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGutsMenuCommon$25(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            closeGuts();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGutsMenuCommon$26(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mLogger.logLongPressSettings(this.mUid, this.mPackageName, this.mInstanceId);
            this.mActivityStarter.startActivity(SETTINGS_INTENT, true);
        }
    }

    public void closeGuts(boolean z) {
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
            }
        }
        this.mMediaViewController.closeGuts(z);
        if (this.mMediaViewHolder != null) {
            bindPlayerContentDescription(this.mMediaData);
        } else if (this.mRecommendationViewHolder != null) {
            bindRecommendationContentDescription(this.mRecommendationData);
        }
    }

    public final void closeGuts() {
        closeGuts(false);
    }

    public final void openGuts() {
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
            }
        }
        this.mMediaViewController.openGuts();
        if (this.mMediaViewHolder != null) {
            bindPlayerContentDescription(this.mMediaData);
        } else if (this.mRecommendationViewHolder != null) {
            bindRecommendationContentDescription(this.mRecommendationData);
        }
        this.mLogger.logLongPressOpen(this.mUid, this.mPackageName, this.mInstanceId);
    }

    public final Drawable getScaledBackground(Icon icon, int i, int i2) {
        if (icon == null) {
            return null;
        }
        Drawable loadDrawable = icon.loadDrawable(this.mContext);
        Rect rect = new Rect(0, 0, i, i2);
        if (rect.width() > i || rect.height() > i2) {
            rect.offset((int) (-(((float) (rect.width() - i)) / 2.0f)), (int) (-(((float) (rect.height() - i2)) / 2.0f)));
        }
        loadDrawable.setBounds(rect);
        return loadDrawable;
    }

    public MediaController getController() {
        return this.mController;
    }

    public final ColorMatrixColorFilter getGrayscaleFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    public final void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z) {
        setVisibleAndAlpha(constraintSet, i, z, 8);
    }

    public final void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z, int i2) {
        if (z) {
            i2 = 0;
        }
        constraintSet.setVisibility(i, i2);
        constraintSet.setAlpha(i, z ? 1.0f : 0.0f);
    }

    public final void setSmartspaceRecItemOnClickListener(View view, SmartspaceAction smartspaceAction, int i) {
        if (view == null || smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null) {
            Log.e("MediaControlPanel", "No tap action can be set up");
        } else {
            view.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda13(this, i, smartspaceAction, view));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSmartspaceRecItemOnClickListener$27(int i, SmartspaceAction smartspaceAction, View view, View view2) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            if (i == -1) {
                this.mLogger.logRecommendationCardTap(this.mPackageName, this.mInstanceId);
            } else {
                this.mLogger.logRecommendationItemTap(this.mPackageName, this.mInstanceId, i);
            }
            logSmartspaceCardReported(760, i, this.mSmartspaceMediaItemsCount);
            if (shouldSmartspaceRecItemOpenInForeground(smartspaceAction)) {
                this.mActivityStarter.postStartActivityDismissingKeyguard(smartspaceAction.getIntent(), 0, buildLaunchAnimatorController(this.mRecommendationViewHolder.getRecommendations()));
            } else {
                view.getContext().startActivity(smartspaceAction.getIntent());
            }
            this.mMediaCarouselController.setShouldScrollToActivePlayer(true);
        }
    }

    public final boolean shouldSmartspaceRecItemOpenInForeground(SmartspaceAction smartspaceAction) {
        String string;
        if (smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null || (string = smartspaceAction.getIntent().getExtras().getString("com.google.android.apps.gsa.smartspace.extra.SMARTSPACE_INTENT")) == null) {
            return false;
        }
        try {
            return Intent.parseUri(string, 1).getBooleanExtra("KEY_OPEN_IN_FOREGROUND", false);
        } catch (URISyntaxException e) {
            Log.wtf("MediaControlPanel", "Failed to create intent from URI: " + string);
            e.printStackTrace();
            return false;
        }
    }

    public int getSurfaceForSmartspaceLogging() {
        int currentEndLocation = this.mMediaViewController.getCurrentEndLocation();
        if (currentEndLocation == 1 || currentEndLocation == 0) {
            return 4;
        }
        if (currentEndLocation == 2) {
            return 2;
        }
        return currentEndLocation == 3 ? 5 : 0;
    }

    public final void logSmartspaceCardReported(int i) {
        logSmartspaceCardReported(i, 0, 0);
    }

    public final void logSmartspaceCardReported(int i, int i2, int i3) {
        this.mMediaCarouselController.logSmartspaceCardReported(i, this.mSmartspaceId, this.mUid, new int[]{getSurfaceForSmartspaceLogging()}, i2, i3);
    }
}
