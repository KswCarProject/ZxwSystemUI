package com.android.systemui.media;

import android.content.res.Resources;
import android.graphics.Outline;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import androidx.core.view.GestureDetectorCompat;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.animation.PhysicsAnimator;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler {
    @NotNull
    public static final MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 CONTENT_TRANSLATION = new MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1();
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public int carouselHeight;
    public int carouselWidth;
    @NotNull
    public final Function1<Boolean, Unit> closeGuts;
    public float contentTranslation;
    public int cornerRadius;
    @NotNull
    public final Function0<Unit> dismissCallback;
    @NotNull
    public final FalsingCollector falsingCollector;
    @NotNull
    public final FalsingManager falsingManager;
    public boolean falsingProtectionNeeded;
    @NotNull
    public final GestureDetectorCompat gestureDetector;
    @NotNull
    public final MediaCarouselScrollHandler$gestureListener$1 gestureListener;
    @NotNull
    public final Function1<Boolean, Unit> logSmartspaceImpression;
    @NotNull
    public final MediaUiEventLogger logger;
    @NotNull
    public final DelayableExecutor mainExecutor;
    @NotNull
    public ViewGroup mediaContent;
    @NotNull
    public final PageIndicator pageIndicator;
    public int playerWidthPlusPadding;
    public boolean qsExpanded;
    @NotNull
    public final MediaCarouselScrollHandler$scrollChangedListener$1 scrollChangedListener;
    public int scrollIntoCurrentMedia;
    @NotNull
    public final MediaScrollView scrollView;
    public View settingsButton;
    public boolean showsSettingsButton;
    @NotNull
    public final MediaCarouselScrollHandler$touchListener$1 touchListener;
    @NotNull
    public Function0<Unit> translationChangedListener;
    public int visibleMediaIndex;
    public boolean visibleToUser;

    public MediaCarouselScrollHandler(@NotNull MediaScrollView mediaScrollView, @NotNull PageIndicator pageIndicator2, @NotNull DelayableExecutor delayableExecutor, @NotNull Function0<Unit> function0, @NotNull Function0<Unit> function02, @NotNull Function1<? super Boolean, Unit> function1, @NotNull FalsingCollector falsingCollector2, @NotNull FalsingManager falsingManager2, @NotNull Function1<? super Boolean, Unit> function12, @NotNull MediaUiEventLogger mediaUiEventLogger) {
        this.scrollView = mediaScrollView;
        this.pageIndicator = pageIndicator2;
        this.mainExecutor = delayableExecutor;
        this.dismissCallback = function0;
        this.translationChangedListener = function02;
        this.closeGuts = function1;
        this.falsingCollector = falsingCollector2;
        this.falsingManager = falsingManager2;
        this.logSmartspaceImpression = function12;
        this.logger = mediaUiEventLogger;
        MediaCarouselScrollHandler$gestureListener$1 mediaCarouselScrollHandler$gestureListener$1 = new MediaCarouselScrollHandler$gestureListener$1(this);
        this.gestureListener = mediaCarouselScrollHandler$gestureListener$1;
        MediaCarouselScrollHandler$touchListener$1 mediaCarouselScrollHandler$touchListener$1 = new MediaCarouselScrollHandler$touchListener$1(this);
        this.touchListener = mediaCarouselScrollHandler$touchListener$1;
        MediaCarouselScrollHandler$scrollChangedListener$1 mediaCarouselScrollHandler$scrollChangedListener$1 = new MediaCarouselScrollHandler$scrollChangedListener$1(this);
        this.scrollChangedListener = mediaCarouselScrollHandler$scrollChangedListener$1;
        this.gestureDetector = new GestureDetectorCompat(mediaScrollView.getContext(), mediaCarouselScrollHandler$gestureListener$1);
        mediaScrollView.setTouchListener(mediaCarouselScrollHandler$touchListener$1);
        mediaScrollView.setOverScrollMode(2);
        this.mediaContent = mediaScrollView.getContentContainer();
        mediaScrollView.setOnScrollChangeListener(mediaCarouselScrollHandler$scrollChangedListener$1);
        mediaScrollView.setOutlineProvider(new ViewOutlineProvider(this) {
            public final /* synthetic */ MediaCarouselScrollHandler this$0;

            {
                this.this$0 = r1;
            }

            public void getOutline(@Nullable View view, @Nullable Outline outline) {
                if (outline != null) {
                    outline.setRoundRect(0, 0, this.this$0.carouselWidth, this.this$0.carouselHeight, (float) this.this$0.cornerRadius);
                }
            }
        });
    }

    @NotNull
    public final Function0<Unit> getDismissCallback() {
        return this.dismissCallback;
    }

    public final boolean isRtl() {
        return this.scrollView.isLayoutRtl();
    }

    public final boolean getFalsingProtectionNeeded() {
        return this.falsingProtectionNeeded;
    }

    public final void setFalsingProtectionNeeded(boolean z) {
        this.falsingProtectionNeeded = z;
    }

    public final int getVisibleMediaIndex() {
        return this.visibleMediaIndex;
    }

    public final float getContentTranslation() {
        return this.contentTranslation;
    }

    public final void setContentTranslation(float f) {
        this.contentTranslation = f;
        this.mediaContent.setTranslationX(f);
        updateSettingsPresentation();
        this.translationChangedListener.invoke();
        updateClipToOutline();
    }

    public final int getPlayerWidthPlusPadding() {
        return this.playerWidthPlusPadding;
    }

    public final void setPlayerWidthPlusPadding(int i) {
        this.playerWidthPlusPadding = i;
        int i2 = this.visibleMediaIndex * i;
        int i3 = this.scrollIntoCurrentMedia;
        this.scrollView.setRelativeScrollX(i3 > i ? i2 + (i - (i3 - i)) : i2 + i3);
    }

    public final void setShowsSettingsButton(boolean z) {
        this.showsSettingsButton = z;
    }

    public final boolean getVisibleToUser() {
        return this.visibleToUser;
    }

    public final void setVisibleToUser(boolean z) {
        this.visibleToUser = z;
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final void onSettingsButtonUpdated(@NotNull View view) {
        this.settingsButton = view;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        Resources resources = view.getResources();
        View view3 = this.settingsButton;
        if (view3 != null) {
            view2 = view3;
        }
        this.cornerRadius = resources.getDimensionPixelSize(Utils.getThemeAttr(view2.getContext(), 16844145));
        updateSettingsPresentation();
        this.scrollView.invalidateOutline();
    }

    public final void updateSettingsPresentation() {
        int i = 4;
        View view = null;
        if (this.showsSettingsButton) {
            View view2 = this.settingsButton;
            if (view2 == null) {
                view2 = null;
            }
            if (view2.getWidth() > 0) {
                float map = MathUtils.map(0.0f, (float) getMaxTranslation(), 0.0f, 1.0f, Math.abs(this.contentTranslation));
                float f = 1.0f - map;
                View view3 = this.settingsButton;
                if (view3 == null) {
                    view3 = null;
                }
                float f2 = ((float) (-view3.getWidth())) * f * 0.3f;
                if (isRtl()) {
                    if (this.contentTranslation > 0.0f) {
                        float width = ((float) this.scrollView.getWidth()) - f2;
                        View view4 = this.settingsButton;
                        if (view4 == null) {
                            view4 = null;
                        }
                        f2 = -(width - ((float) view4.getWidth()));
                    } else {
                        f2 = -f2;
                    }
                } else if (this.contentTranslation <= 0.0f) {
                    float width2 = ((float) this.scrollView.getWidth()) - f2;
                    View view5 = this.settingsButton;
                    if (view5 == null) {
                        view5 = null;
                    }
                    f2 = width2 - ((float) view5.getWidth());
                }
                float f3 = f * ((float) 50);
                View view6 = this.settingsButton;
                if (view6 == null) {
                    view6 = null;
                }
                view6.setRotation(f3 * (-Math.signum(this.contentTranslation)));
                float saturate = MathUtils.saturate(MathUtils.map(0.5f, 1.0f, 0.0f, 1.0f, map));
                View view7 = this.settingsButton;
                if (view7 == null) {
                    view7 = null;
                }
                view7.setAlpha(saturate);
                View view8 = this.settingsButton;
                if (view8 == null) {
                    view8 = null;
                }
                if (!(saturate == 0.0f)) {
                    i = 0;
                }
                view8.setVisibility(i);
                View view9 = this.settingsButton;
                if (view9 == null) {
                    view9 = null;
                }
                view9.setTranslationX(f2);
                View view10 = this.settingsButton;
                if (view10 == null) {
                    view10 = null;
                }
                int height = this.scrollView.getHeight();
                View view11 = this.settingsButton;
                if (view11 != null) {
                    view = view11;
                }
                view10.setTranslationY(((float) (height - view.getHeight())) / 2.0f);
                return;
            }
        }
        View view12 = this.settingsButton;
        if (view12 != null) {
            view = view12;
        }
        view.setVisibility(4);
    }

    public final boolean onTouch(MotionEvent motionEvent) {
        float f;
        boolean z = true;
        boolean z2 = motionEvent.getAction() == 1;
        if (z2 && this.falsingProtectionNeeded) {
            this.falsingCollector.onNotificationStopDismissing();
        }
        if (!this.gestureDetector.onTouchEvent(motionEvent)) {
            if (z2 || motionEvent.getAction() == 3) {
                int relativeScrollX = this.scrollView.getRelativeScrollX();
                int i = this.playerWidthPlusPadding;
                int i2 = relativeScrollX % i;
                int i3 = i2 > i / 2 ? i - i2 : i2 * -1;
                if (i3 != 0) {
                    if (isRtl()) {
                        i3 = -i3;
                    }
                    this.mainExecutor.execute(new MediaCarouselScrollHandler$onTouch$1(this, this.scrollView.getRelativeScrollX() + i3));
                }
                float contentTranslation2 = this.scrollView.getContentTranslation();
                if (!(contentTranslation2 == 0.0f)) {
                    if (Math.abs(contentTranslation2) >= ((float) (getMaxTranslation() / 2)) && !isFalseTouch()) {
                        z = false;
                    }
                    if (z) {
                        f = 0.0f;
                    } else {
                        f = ((float) getMaxTranslation()) * Math.signum(contentTranslation2);
                        if (!this.showsSettingsButton) {
                            this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$onTouch$2(this), 100);
                        }
                    }
                    PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
                    this.scrollView.setAnimationTargetX(f);
                }
            }
            return false;
        } else if (!z2) {
            return false;
        } else {
            this.scrollView.cancelCurrentScroll();
            return true;
        }
    }

    public final boolean isFalseTouch() {
        if (!this.falsingProtectionNeeded || !this.falsingManager.isFalseTouch(1)) {
            return false;
        }
        return true;
    }

    public final int getMaxTranslation() {
        if (!this.showsSettingsButton) {
            return this.playerWidthPlusPadding;
        }
        View view = this.settingsButton;
        if (view == null) {
            view = null;
        }
        return view.getWidth();
    }

    public final boolean onInterceptTouch(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public final boolean onScroll(@NotNull MotionEvent motionEvent, @NotNull MotionEvent motionEvent2, float f) {
        float x = motionEvent2.getX() - motionEvent.getX();
        float contentTranslation2 = this.scrollView.getContentTranslation();
        int i = (contentTranslation2 > 0.0f ? 1 : (contentTranslation2 == 0.0f ? 0 : -1));
        boolean z = false;
        if ((i == 0) && this.scrollView.canScrollHorizontally((int) (-x))) {
            return false;
        }
        float f2 = contentTranslation2 - f;
        float abs = Math.abs(f2);
        if (abs > ((float) getMaxTranslation())) {
            if (!(Math.signum(f) == Math.signum(contentTranslation2))) {
                f2 = Math.abs(contentTranslation2) > ((float) getMaxTranslation()) ? contentTranslation2 - (f * 0.2f) : Math.signum(f2) * (((float) getMaxTranslation()) + ((abs - ((float) getMaxTranslation())) * 0.2f));
            }
        }
        if (!(Math.signum(f2) == Math.signum(contentTranslation2))) {
            if (i == 0) {
                z = true;
            }
            if (!z && this.scrollView.canScrollHorizontally(-((int) f2))) {
                f2 = 0.0f;
            }
        }
        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(this);
        if (instance.isRunning()) {
            instance.spring(CONTENT_TRANSLATION, f2, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
        } else {
            setContentTranslation(f2);
        }
        this.scrollView.setAnimationTargetX(f2);
        return true;
    }

    public final boolean onFling(float f, float f2) {
        float f3 = f * f;
        double d = (double) f2;
        boolean z = false;
        if (((double) f3) < 0.5d * d * d || f3 < 1000000.0f) {
            return false;
        }
        float contentTranslation2 = this.scrollView.getContentTranslation();
        float f4 = 0.0f;
        if (!(contentTranslation2 == 0.0f)) {
            if (Math.signum(f) == Math.signum(contentTranslation2)) {
                z = true;
            }
            if (z && !isFalseTouch()) {
                f4 = ((float) getMaxTranslation()) * Math.signum(contentTranslation2);
                if (!this.showsSettingsButton) {
                    this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$onFling$1(this), 100);
                }
            }
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, f4, f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(f4);
        } else {
            int relativeScrollX = this.scrollView.getRelativeScrollX();
            int i = this.playerWidthPlusPadding;
            int i2 = i > 0 ? relativeScrollX / i : 0;
            if (!isRtl() ? f < 0.0f : f > 0.0f) {
                i2++;
            }
            this.mainExecutor.execute(new MediaCarouselScrollHandler$onFling$2(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, Math.max(0, i2)))));
        }
        return true;
    }

    public static /* synthetic */ void resetTranslation$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        mediaCarouselScrollHandler.resetTranslation(z);
    }

    public final void resetTranslation(boolean z) {
        if (this.scrollView.getContentTranslation() == 0.0f) {
            return;
        }
        if (z) {
            PhysicsAnimator.Companion.getInstance(this).spring(CONTENT_TRANSLATION, 0.0f, MediaCarouselScrollHandlerKt.translationConfig).start();
            this.scrollView.setAnimationTargetX(0.0f);
            return;
        }
        PhysicsAnimator.Companion.getInstance(this).cancel();
        setContentTranslation(0.0f);
    }

    public final void updateClipToOutline() {
        boolean z = true;
        if ((this.contentTranslation == 0.0f) && this.scrollIntoCurrentMedia == 0) {
            z = false;
        }
        this.scrollView.setClipToOutline(z);
    }

    public final void onMediaScrollingChanged(int i, int i2) {
        boolean z = false;
        boolean z2 = this.scrollIntoCurrentMedia != 0;
        this.scrollIntoCurrentMedia = i2;
        if (i2 != 0) {
            z = true;
        }
        int i3 = this.visibleMediaIndex;
        if (!(i == i3 && z2 == z)) {
            this.visibleMediaIndex = i;
            if (i3 != i && this.visibleToUser) {
                this.logSmartspaceImpression.invoke(Boolean.valueOf(this.qsExpanded));
                this.logger.logMediaCarouselPage(i);
            }
            this.closeGuts.invoke(Boolean.FALSE);
            updatePlayerVisibilities();
        }
        float f = (float) this.visibleMediaIndex;
        int i4 = this.playerWidthPlusPadding;
        float f2 = f + (i4 > 0 ? ((float) i2) / ((float) i4) : 0.0f);
        if (isRtl()) {
            f2 = (((float) this.mediaContent.getChildCount()) - f2) - ((float) 1);
        }
        this.pageIndicator.setLocation(f2);
        updateClipToOutline();
    }

    public final void onPlayersChanged() {
        updatePlayerVisibilities();
        updateMediaPaddings();
    }

    public final void updateMediaPaddings() {
        int dimensionPixelSize = this.scrollView.getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_padding);
        int childCount = this.mediaContent.getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = this.mediaContent.getChildAt(i);
            int i3 = i == childCount + -1 ? 0 : dimensionPixelSize;
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                if (marginLayoutParams.getMarginEnd() != i3) {
                    marginLayoutParams.setMarginEnd(i3);
                    childAt.setLayoutParams(marginLayoutParams);
                }
                i = i2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
        }
    }

    public final void updatePlayerVisibilities() {
        boolean z = this.scrollIntoCurrentMedia != 0;
        int childCount = this.mediaContent.getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            View childAt = this.mediaContent.getChildAt(i);
            int i3 = this.visibleMediaIndex;
            childAt.setVisibility(i == i3 || (i == i3 + 1 && z) ? 0 : 4);
            i = i2;
        }
    }

    public final void onPrePlayerRemoved(@NotNull MediaControlPanel mediaControlPanel) {
        ViewGroup viewGroup = this.mediaContent;
        MediaViewHolder mediaViewHolder = mediaControlPanel.getMediaViewHolder();
        int indexOfChild = viewGroup.indexOfChild(mediaViewHolder == null ? null : mediaViewHolder.getPlayer());
        int i = this.visibleMediaIndex;
        boolean z = true;
        boolean z2 = indexOfChild <= i;
        if (z2) {
            this.visibleMediaIndex = Math.max(0, i - 1);
        }
        if (!isRtl()) {
            z = z2;
        } else if (z2) {
            z = false;
        }
        if (z) {
            MediaScrollView mediaScrollView = this.scrollView;
            mediaScrollView.setScrollX(Math.max(mediaScrollView.getScrollX() - this.playerWidthPlusPadding, 0));
        }
    }

    public final void setCarouselBounds(int i, int i2) {
        int i3 = this.carouselHeight;
        if (i2 != i3 || i != i3) {
            this.carouselWidth = i;
            this.carouselHeight = i2;
            this.scrollView.invalidateOutline();
        }
    }

    public final void scrollToStart() {
        this.scrollView.setRelativeScrollX(0);
    }

    public static /* synthetic */ void scrollToPlayer$default(MediaCarouselScrollHandler mediaCarouselScrollHandler, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = -1;
        }
        mediaCarouselScrollHandler.scrollToPlayer(i, i2);
    }

    public final void scrollToPlayer(int i, int i2) {
        if (i >= 0 && i < this.mediaContent.getChildCount()) {
            this.scrollView.setRelativeScrollX(i * this.playerWidthPlusPadding);
        }
        this.mainExecutor.executeDelayed(new MediaCarouselScrollHandler$scrollToPlayer$1(this, this.mediaContent.getChildAt(Math.min(this.mediaContent.getChildCount() - 1, i2))), 100);
    }

    /* compiled from: MediaCarouselScrollHandler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
