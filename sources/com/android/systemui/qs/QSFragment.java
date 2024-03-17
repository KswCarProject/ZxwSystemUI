package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Trace;
import android.util.IndentingPrintWriter;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.util.LifecycleFragment;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Consumer;

public class QSFragment extends LifecycleFragment implements QS, CommandQueue.Callbacks, StatusBarStateController.StateListener, Dumpable {
    public final Animator.AnimatorListener mAnimateHeaderSlidingInListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            QSFragment.this.mHeaderAnimating = false;
            QSFragment.this.updateQsState();
            QSFragment.this.getView().animate().setListener((Animator.AnimatorListener) null);
        }
    };
    public final KeyguardBypassController mBypassController;
    public QSContainerImpl mContainer;
    public long mDelay;
    public final DumpManager mDumpManager;
    public final FalsingManager mFalsingManager;
    public QSFooter mFooter;
    public float mFullShadeProgress;
    public QuickStatusBarHeader mHeader;
    public boolean mHeaderAnimating;
    public final QSTileHost mHost;
    public boolean mInSplitShade;
    public float mLastHeaderTranslation;
    public boolean mLastKeyguardAndExpanded;
    public float mLastPanelFraction;
    public float mLastQSExpansion = -1.0f;
    public int mLastViewHeight;
    public int mLayoutDirection;
    public boolean mListening;
    public int[] mLocationTemp = new int[2];
    public boolean mOverScrolling;
    public QS.HeightListener mPanelView;
    public QSAnimator mQSAnimator;
    public QSContainerImplController mQSContainerImplController;
    public QSCustomizerController mQSCustomizerController;
    public FooterActionsController mQSFooterActionController;
    public QSPanelController mQSPanelController;
    public NonInterceptingScrollView mQSPanelScrollView;
    public QSSquishinessController mQSSquishinessController;
    public final MediaHost mQqsMediaHost;
    public final Rect mQsBounds = new Rect();
    public final QSFragmentComponent.Factory mQsComponentFactory;
    public boolean mQsDisabled;
    public boolean mQsExpanded;
    public final QSFragmentDisableFlagsLogger mQsFragmentDisableFlagsLogger;
    public final MediaHost mQsMediaHost;
    public boolean mQsVisible;
    public QuickQSPanelController mQuickQSPanelController;
    public final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    public QS.ScrollListener mScrollListener;
    public boolean mShowCollapsedOnKeyguard;
    public float mSquishinessFraction = 1.0f;
    public boolean mStackScrollerOverscrolling;
    public final ViewTreeObserver.OnPreDrawListener mStartHeaderSlidingIn = new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
            QSFragment.this.getView().getViewTreeObserver().removeOnPreDrawListener(this);
            QSFragment.this.getView().animate().translationY(0.0f).setStartDelay(QSFragment.this.mDelay).setDuration(448).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setListener(QSFragment.this.mAnimateHeaderSlidingInListener).start();
            return true;
        }
    };
    public int mState;
    public final StatusBarStateController mStatusBarStateController;
    public int[] mTmpLocation = new int[2];
    public boolean mTransitioningToFullShade;

    public static String visibilityToString(int i) {
        return i == 0 ? "VISIBLE" : i == 4 ? "INVISIBLE" : "GONE";
    }

    public void setHasNotifications(boolean z) {
    }

    public void setHeaderClickable(boolean z) {
    }

    public QSFragment(RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, QSTileHost qSTileHost, StatusBarStateController statusBarStateController, CommandQueue commandQueue, MediaHost mediaHost, MediaHost mediaHost2, KeyguardBypassController keyguardBypassController, QSFragmentComponent.Factory factory, QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger, FalsingManager falsingManager, DumpManager dumpManager) {
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mQsMediaHost = mediaHost;
        this.mQqsMediaHost = mediaHost2;
        this.mQsComponentFactory = factory;
        this.mQsFragmentDisableFlagsLogger = qSFragmentDisableFlagsLogger;
        commandQueue.observe(getLifecycle(), this);
        this.mHost = qSTileHost;
        this.mFalsingManager = falsingManager;
        this.mBypassController = keyguardBypassController;
        this.mStatusBarStateController = statusBarStateController;
        this.mDumpManager = dumpManager;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            Trace.beginSection("QSFragment#onCreateView");
            return layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), R$style.Theme_SystemUI_QuickSettings)).inflate(R$layout.qs_panel, viewGroup, false);
        } finally {
            Trace.endSection();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        QSFragmentComponent create = this.mQsComponentFactory.create(this);
        this.mQSPanelController = create.getQSPanelController();
        this.mQuickQSPanelController = create.getQuickQSPanelController();
        this.mQSFooterActionController = create.getQSFooterActionController();
        this.mQSPanelController.init();
        this.mQuickQSPanelController.init();
        this.mQSFooterActionController.init();
        NonInterceptingScrollView nonInterceptingScrollView = (NonInterceptingScrollView) view.findViewById(R$id.expanded_qs_scroll_view);
        this.mQSPanelScrollView = nonInterceptingScrollView;
        nonInterceptingScrollView.addOnLayoutChangeListener(new QSFragment$$ExternalSyntheticLambda0(this));
        this.mQSPanelScrollView.setOnScrollChangeListener(new QSFragment$$ExternalSyntheticLambda1(this));
        this.mHeader = (QuickStatusBarHeader) view.findViewById(R$id.header);
        this.mFooter = create.getQSFooter();
        QSContainerImplController qSContainerImplController = create.getQSContainerImplController();
        this.mQSContainerImplController = qSContainerImplController;
        qSContainerImplController.init();
        QSContainerImpl view2 = this.mQSContainerImplController.getView();
        this.mContainer = view2;
        this.mDumpManager.registerDumpable(view2.getClass().getName(), this.mContainer);
        this.mQSAnimator = create.getQSAnimator();
        this.mQSSquishinessController = create.getQSSquishinessController();
        QSCustomizerController qSCustomizerController = create.getQSCustomizerController();
        this.mQSCustomizerController = qSCustomizerController;
        qSCustomizerController.init();
        this.mQSCustomizerController.setQs(this);
        if (bundle != null) {
            setQsVisible(bundle.getBoolean("visible"));
            setExpanded(bundle.getBoolean("expanded"));
            setListening(bundle.getBoolean("listening"));
            setEditLocation(view);
            this.mQSCustomizerController.restoreInstanceState(bundle);
            if (this.mQsExpanded) {
                this.mQSPanelController.getTileLayout().restoreInstanceState(bundle);
            }
        }
        this.mStatusBarStateController.addCallback(this);
        onStateChanged(this.mStatusBarStateController.getState());
        view.addOnLayoutChangeListener(new QSFragment$$ExternalSyntheticLambda2(this));
        this.mQSPanelController.setUsingHorizontalLayoutChangeListener(new QSFragment$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateQsBounds();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(View view, int i, int i2, int i3, int i4) {
        this.mQSAnimator.requestAnimatorUpdate();
        this.mHeader.setExpandedScrollAmount(i2);
        QS.ScrollListener scrollListener = this.mScrollListener;
        if (scrollListener != null) {
            scrollListener.onQsPanelScrollChanged(i2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i6 - i8 != i2 - i4) {
            setQsExpansion(this.mLastQSExpansion, this.mLastPanelFraction, this.mLastHeaderTranslation, this.mSquishinessFraction);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$3() {
        this.mQSPanelController.getMediaHost().getHostView().setAlpha(1.0f);
        this.mQSAnimator.requestAnimatorUpdate();
    }

    public void setScrollListener(QS.ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDumpManager.registerDumpable(getClass().getName(), this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mStatusBarStateController.removeCallback(this);
        if (this.mListening) {
            setListening(false);
        }
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        if (qSCustomizerController != null) {
            qSCustomizerController.setQs((QSFragment) null);
        }
        this.mScrollListener = null;
        QSContainerImpl qSContainerImpl = this.mContainer;
        if (qSContainerImpl != null) {
            this.mDumpManager.unregisterDumpable(qSContainerImpl.getClass().getName());
        }
        this.mDumpManager.unregisterDumpable(getClass().getName());
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("expanded", this.mQsExpanded);
        bundle.putBoolean("listening", this.mListening);
        bundle.putBoolean("visible", this.mQsVisible);
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        if (qSCustomizerController != null) {
            qSCustomizerController.saveInstanceState(bundle);
        }
        if (this.mQsExpanded) {
            this.mQSPanelController.getTileLayout().saveInstanceState(bundle);
        }
    }

    public boolean isListening() {
        return this.mListening;
    }

    public boolean isExpanded() {
        return this.mQsExpanded;
    }

    public boolean isQsVisible() {
        return this.mQsVisible;
    }

    public View getHeader() {
        return this.mHeader;
    }

    public void setPanelView(QS.HeightListener heightListener) {
        this.mPanelView = heightListener;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setEditLocation(getView());
        if (configuration.getLayoutDirection() != this.mLayoutDirection) {
            this.mLayoutDirection = configuration.getLayoutDirection();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.onRtlChanged();
            }
        }
        updateQsState();
    }

    public void setFancyClipping(int i, int i2, int i3, boolean z) {
        if (getView() instanceof QSContainerImpl) {
            ((QSContainerImpl) getView()).setFancyClipping(i, i2, i3, z);
        }
    }

    public boolean isFullyCollapsed() {
        float f = this.mLastQSExpansion;
        return f == 0.0f || f == -1.0f;
    }

    public void setCollapsedMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mQuickQSPanelController.setMediaVisibilityChangedListener(consumer);
    }

    public final void setEditLocation(View view) {
        View findViewById = view.findViewById(16908291);
        int[] locationOnScreen = findViewById.getLocationOnScreen();
        this.mQSCustomizerController.setEditLocation(locationOnScreen[0] + (findViewById.getWidth() / 2), locationOnScreen[1] + (findViewById.getHeight() / 2));
    }

    public void setContainerController(QSContainerController qSContainerController) {
        this.mQSCustomizerController.setContainerController(qSContainerController);
    }

    public boolean isCustomizing() {
        return this.mQSCustomizerController.isCustomizing();
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getContext().getDisplayId()) {
            int adjustDisableFlags = this.mRemoteInputQuickSettingsDisabler.adjustDisableFlags(i3);
            this.mQsFragmentDisableFlagsLogger.logDisableFlagChange(new DisableFlagsLogger.DisableState(i2, i3), new DisableFlagsLogger.DisableState(i2, adjustDisableFlags));
            boolean z2 = (adjustDisableFlags & 1) != 0;
            if (z2 != this.mQsDisabled) {
                this.mQsDisabled = z2;
                this.mContainer.disable(i2, adjustDisableFlags, z);
                this.mHeader.disable(i2, adjustDisableFlags, z);
                this.mFooter.disable(i2, adjustDisableFlags, z);
                this.mQSFooterActionController.disable(adjustDisableFlags);
                updateQsState();
            }
        }
    }

    public final void updateQsState() {
        boolean z = true;
        int i = 0;
        boolean z2 = this.mQsExpanded || this.mInSplitShade;
        boolean z3 = z2 || this.mStackScrollerOverscrolling || this.mHeaderAnimating;
        this.mQSPanelController.setExpanded(z2);
        boolean isKeyguardState = isKeyguardState();
        this.mHeader.setVisibility((z2 || !isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) ? 0 : 4);
        this.mHeader.setExpanded((isKeyguardState && !this.mHeaderAnimating && !this.mShowCollapsedOnKeyguard) || (z2 && !this.mStackScrollerOverscrolling), this.mQuickQSPanelController);
        boolean z4 = !this.mQsDisabled && z3;
        boolean z5 = z4 && (z2 || !isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard);
        this.mFooter.setVisibility(z5 ? 0 : 4);
        this.mQSFooterActionController.setVisible(z5);
        QSFooter qSFooter = this.mFooter;
        if ((!isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) && (!z2 || this.mStackScrollerOverscrolling)) {
            z = false;
        }
        qSFooter.setExpanded(z);
        QSPanelController qSPanelController = this.mQSPanelController;
        if (!z4) {
            i = 4;
        }
        qSPanelController.setVisibility(i);
    }

    public final boolean isKeyguardState() {
        return this.mStatusBarStateController.getState() == 1;
    }

    public final void updateShowCollapsedOnKeyguard() {
        boolean z = this.mBypassController.getBypassEnabled() || (this.mTransitioningToFullShade && !this.mInSplitShade);
        if (z != this.mShowCollapsedOnKeyguard) {
            this.mShowCollapsedOnKeyguard = z;
            updateQsState();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.setShowCollapsedOnKeyguard(z);
            }
            if (!z && isKeyguardState()) {
                setQsExpansion(this.mLastQSExpansion, this.mLastPanelFraction, 0.0f, this.mSquishinessFraction);
            }
        }
    }

    public QSPanelController getQSPanelController() {
        return this.mQSPanelController;
    }

    public void setBrightnessMirrorController(BrightnessMirrorController brightnessMirrorController) {
        this.mQSPanelController.setBrightnessMirror(brightnessMirrorController);
    }

    public boolean isShowingDetail() {
        return this.mQSCustomizerController.isCustomizing();
    }

    public void setExpanded(boolean z) {
        this.mQsExpanded = z;
        updateQsPanelControllerListening();
        updateQsState();
    }

    public final void setKeyguardShowing(boolean z) {
        this.mLastQSExpansion = -1.0f;
        QSAnimator qSAnimator = this.mQSAnimator;
        if (qSAnimator != null) {
            qSAnimator.setOnKeyguard(z);
        }
        this.mFooter.setKeyguardShowing(z);
        this.mQSFooterActionController.setKeyguardShowing(z);
        updateQsState();
    }

    public void setOverscrolling(boolean z) {
        this.mStackScrollerOverscrolling = z;
        updateQsState();
    }

    public void setListening(boolean z) {
        this.mListening = z;
        boolean z2 = true;
        this.mQSContainerImplController.setListening(z && this.mQsVisible);
        FooterActionsController footerActionsController = this.mQSFooterActionController;
        if (!z || !this.mQsVisible) {
            z2 = false;
        }
        footerActionsController.setListening(z2);
        updateQsPanelControllerListening();
    }

    public final void updateQsPanelControllerListening() {
        this.mQSPanelController.setListening(this.mListening && this.mQsVisible, this.mQsExpanded);
    }

    public void setQsVisible(boolean z) {
        this.mQsVisible = z;
        setListening(this.mListening);
    }

    public void setHeaderListening(boolean z) {
        this.mQSContainerImplController.setListening(z);
    }

    public void setInSplitShade(boolean z) {
        this.mInSplitShade = z;
        this.mQSAnimator.setTranslateWhileExpanding(z);
        updateShowCollapsedOnKeyguard();
        updateQsState();
    }

    public void setTransitionToFullShadeAmount(float f, float f2) {
        boolean z = f > 0.0f;
        if (z != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z;
            updateShowCollapsedOnKeyguard();
        }
        this.mFullShadeProgress = f2;
        float f3 = this.mLastQSExpansion;
        float f4 = this.mLastPanelFraction;
        float f5 = this.mLastHeaderTranslation;
        if (!z) {
            f2 = this.mSquishinessFraction;
        }
        setQsExpansion(f3, f4, f5, f2);
    }

    public void setOverScrollAmount(int i) {
        this.mOverScrolling = i != 0;
        View view = getView();
        if (view != null) {
            view.setTranslationY((float) i);
        }
    }

    public int getHeightDiff() {
        return (this.mQSPanelScrollView.getBottom() - this.mHeader.getBottom()) + this.mHeader.getPaddingBottom();
    }

    public void setQsExpansion(float f, float f2, float f3, float f4) {
        float f5;
        boolean z = this.mTransitioningToFullShade;
        float f6 = z ? 0.0f : f3;
        boolean z2 = true;
        if (z || this.mState == 1) {
            f5 = this.mFullShadeProgress;
        } else {
            f5 = f2;
        }
        float f7 = 1.0f;
        if (!this.mInSplitShade) {
            f5 = 1.0f;
        }
        setAlphaAnimationProgress(f5);
        this.mContainer.setExpansion(f);
        float f8 = (this.mInSplitShade ? 1.0f : 0.1f) * (f - 1.0f);
        boolean isKeyguardState = isKeyguardState();
        boolean z3 = isKeyguardState && !this.mShowCollapsedOnKeyguard;
        if (!this.mHeaderAnimating && !headerWillBeAnimating() && !this.mOverScrolling) {
            getView().setTranslationY(z3 ? ((float) this.mHeader.getHeight()) * f8 : f6);
        }
        int height = getView().getHeight();
        if (f != this.mLastQSExpansion || this.mLastKeyguardAndExpanded != z3 || this.mLastViewHeight != height || this.mLastHeaderTranslation != f6 || this.mSquishinessFraction != f4) {
            this.mLastHeaderTranslation = f6;
            this.mLastPanelFraction = f2;
            this.mSquishinessFraction = f4;
            this.mLastQSExpansion = f;
            this.mLastKeyguardAndExpanded = z3;
            this.mLastViewHeight = height;
            boolean z4 = f == 1.0f;
            if (f != 0.0f) {
                z2 = false;
            }
            float heightDiff = f8 * ((float) getHeightDiff());
            this.mHeader.setExpansion(z3, f, heightDiff);
            if (f < 1.0f && ((double) f) > 0.99d && this.mQuickQSPanelController.switchTileLayout(false)) {
                this.mHeader.updateResources();
            }
            this.mQSPanelController.setIsOnKeyguard(isKeyguardState);
            this.mFooter.setExpansion(z3 ? 1.0f : f);
            FooterActionsController footerActionsController = this.mQSFooterActionController;
            if (!z3) {
                f7 = f;
            }
            footerActionsController.setExpansion(f7);
            this.mQSPanelController.setRevealExpansion(f);
            this.mQSPanelController.getTileLayout().setExpansion(f, f3);
            this.mQuickQSPanelController.getTileLayout().setExpansion(f, f3);
            this.mQSPanelScrollView.setTranslationY(heightDiff);
            if (z2) {
                this.mQSPanelScrollView.setScrollY(0);
            }
            if (!z4) {
                this.mQsBounds.top = (int) (-this.mQSPanelScrollView.getTranslationY());
                this.mQsBounds.right = this.mQSPanelScrollView.getWidth();
                this.mQsBounds.bottom = this.mQSPanelScrollView.getHeight();
            }
            updateQsBounds();
            QSSquishinessController qSSquishinessController = this.mQSSquishinessController;
            if (qSSquishinessController != null) {
                qSSquishinessController.setSquishiness(this.mSquishinessFraction);
            }
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.setPosition(f);
            }
            updateMediaPositions();
        }
    }

    public final void setAlphaAnimationProgress(float f) {
        float f2;
        View view = getView();
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0 && view.getVisibility() != 4) {
            view.setVisibility(4);
        } else if (i > 0 && view.getVisibility() != 0) {
            view.setVisibility(0);
        }
        if (this.mQSPanelController.isBouncerInTransit()) {
            f2 = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f);
        } else {
            f2 = ShadeInterpolation.getContentAlpha(f);
        }
        view.setAlpha(f2);
    }

    public final void updateQsBounds() {
        if (this.mLastQSExpansion == 1.0f) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelScrollView.getLayoutParams();
            this.mQsBounds.set(-marginLayoutParams.leftMargin, 0, this.mQSPanelScrollView.getWidth() + marginLayoutParams.rightMargin, this.mQSPanelScrollView.getHeight());
        }
        this.mQSPanelScrollView.setClipBounds(this.mQsBounds);
        this.mQSPanelScrollView.getLocationOnScreen(this.mLocationTemp);
        int[] iArr = this.mLocationTemp;
        int i = iArr[0];
        int i2 = iArr[1];
        this.mQsMediaHost.getCurrentClipping().set(i, i2, getView().getMeasuredWidth() + i, (this.mQSPanelScrollView.getMeasuredHeight() + i2) - this.mQSPanelScrollView.getPaddingBottom());
    }

    public final void updateMediaPositions() {
        if (Utils.useQsMediaPlayer(getContext())) {
            this.mContainer.getLocationOnScreen(this.mTmpLocation);
            float height = (float) (this.mTmpLocation[1] + this.mContainer.getHeight());
            pinToBottom((height - ((float) this.mQSPanelScrollView.getScrollY())) + ((float) this.mQSPanelScrollView.getScrollRange()), this.mQsMediaHost, true);
            pinToBottom(height, this.mQqsMediaHost, false);
        }
    }

    public final void pinToBottom(float f, MediaHost mediaHost, boolean z) {
        float f2;
        UniqueObjectHostView hostView = mediaHost.getHostView();
        if (this.mLastQSExpansion <= 0.0f || isKeyguardState() || !this.mQqsMediaHost.getVisible()) {
            hostView.setTranslationY(0.0f);
            return;
        }
        float totalBottomMargin = ((f - getTotalBottomMargin(hostView)) - ((float) hostView.getHeight())) - (((float) mediaHost.getCurrentBounds().top) - hostView.getTranslationY());
        if (z) {
            f2 = Math.min(totalBottomMargin, 0.0f);
        } else {
            f2 = Math.max(totalBottomMargin, 0.0f);
        }
        hostView.setTranslationY(f2);
    }

    public final float getTotalBottomMargin(View view) {
        View view2 = (View) view.getParent();
        int i = 0;
        while (true) {
            View view3 = view;
            view = view2;
            View view4 = view3;
            if (!(view instanceof QSContainerImpl) && view != null) {
                i += view.getHeight() - view4.getBottom();
                view2 = (View) view.getParent();
            }
        }
        return (float) i;
    }

    public final boolean headerWillBeAnimating() {
        if (this.mState != 1 || !this.mShowCollapsedOnKeyguard || isKeyguardState()) {
            return false;
        }
        return true;
    }

    public void animateHeaderSlidingOut() {
        if (getView().getY() != ((float) (-this.mHeader.getHeight()))) {
            this.mHeaderAnimating = true;
            getView().animate().y((float) (-this.mHeader.getHeight())).setStartDelay(0).setDuration(360).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (QSFragment.this.getView() != null) {
                        QSFragment.this.getView().animate().setListener((Animator.AnimatorListener) null);
                    }
                    QSFragment.this.mHeaderAnimating = false;
                    QSFragment.this.updateQsState();
                }
            }).start();
        }
    }

    public void setCollapseExpandAction(Runnable runnable) {
        this.mQSPanelController.setCollapseExpandAction(runnable);
        this.mQuickQSPanelController.setCollapseExpandAction(runnable);
    }

    public void closeDetail() {
        this.mQSPanelController.closeDetail();
    }

    public void closeCustomizer() {
        this.mQSCustomizerController.hide();
    }

    public void notifyCustomizeChanged() {
        this.mContainer.updateExpansion();
        boolean isCustomizing = isCustomizing();
        int i = 0;
        this.mQSPanelScrollView.setVisibility(!isCustomizing ? 0 : 4);
        this.mFooter.setVisibility(!isCustomizing ? 0 : 4);
        this.mQSFooterActionController.setVisible(!isCustomizing);
        QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
        if (isCustomizing) {
            i = 4;
        }
        quickStatusBarHeader.setVisibility(i);
        this.mPanelView.onQsHeightChanged();
    }

    public int getDesiredHeight() {
        if (this.mQSCustomizerController.isCustomizing()) {
            return getView().getHeight();
        }
        return getView().getMeasuredHeight();
    }

    public void setHeightOverride(int i) {
        this.mContainer.setHeightOverride(i);
    }

    public int getQsMinExpansionHeight() {
        if (this.mInSplitShade) {
            return getQsMinExpansionHeightForSplitShade();
        }
        return this.mHeader.getHeight();
    }

    public final int getQsMinExpansionHeightForSplitShade() {
        getView().getLocationOnScreen(this.mLocationTemp);
        return ((int) (((float) this.mLocationTemp[1]) - getView().getTranslationY())) + getView().getHeight();
    }

    public void hideImmediately() {
        getView().animate().cancel();
        getView().setY((float) (-getQsMinExpansionHeight()));
    }

    public void onStateChanged(int i) {
        this.mState = i;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        setKeyguardShowing(z);
        updateShowCollapsedOnKeyguard();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("QSFragment:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mQsBounds: " + this.mQsBounds);
        indentingPrintWriter.println("mQsExpanded: " + this.mQsExpanded);
        indentingPrintWriter.println("mHeaderAnimating: " + this.mHeaderAnimating);
        indentingPrintWriter.println("mStackScrollerOverscrolling: " + this.mStackScrollerOverscrolling);
        indentingPrintWriter.println("mListening: " + this.mListening);
        indentingPrintWriter.println("mQsVisible: " + this.mQsVisible);
        indentingPrintWriter.println("mLayoutDirection: " + this.mLayoutDirection);
        indentingPrintWriter.println("mLastQSExpansion: " + this.mLastQSExpansion);
        indentingPrintWriter.println("mLastPanelFraction: " + this.mLastPanelFraction);
        indentingPrintWriter.println("mSquishinessFraction: " + this.mSquishinessFraction);
        indentingPrintWriter.println("mQsDisabled: " + this.mQsDisabled);
        indentingPrintWriter.println("mTemp: " + Arrays.toString(this.mLocationTemp));
        indentingPrintWriter.println("mShowCollapsedOnKeyguard: " + this.mShowCollapsedOnKeyguard);
        indentingPrintWriter.println("mLastKeyguardAndExpanded: " + this.mLastKeyguardAndExpanded);
        indentingPrintWriter.println("mState: " + StatusBarState.toString(this.mState));
        indentingPrintWriter.println("mTmpLocation: " + Arrays.toString(this.mTmpLocation));
        indentingPrintWriter.println("mLastViewHeight: " + this.mLastViewHeight);
        indentingPrintWriter.println("mLastHeaderTranslation: " + this.mLastHeaderTranslation);
        indentingPrintWriter.println("mInSplitShade: " + this.mInSplitShade);
        indentingPrintWriter.println("mTransitioningToFullShade: " + this.mTransitioningToFullShade);
        indentingPrintWriter.println("mFullShadeProgress: " + this.mFullShadeProgress);
        indentingPrintWriter.println("mOverScrolling: " + this.mOverScrolling);
        indentingPrintWriter.println("isCustomizing: " + this.mQSCustomizerController.isCustomizing());
        View view = getView();
        if (view != null) {
            indentingPrintWriter.println("top: " + view.getTop());
            indentingPrintWriter.println("y: " + view.getY());
            indentingPrintWriter.println("translationY: " + view.getTranslationY());
            indentingPrintWriter.println("alpha: " + view.getAlpha());
            indentingPrintWriter.println("height: " + view.getHeight());
            indentingPrintWriter.println("measuredHeight: " + view.getMeasuredHeight());
            indentingPrintWriter.println("clipBounds: " + view.getClipBounds());
        } else {
            indentingPrintWriter.println("getView(): null");
        }
        QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
        if (quickStatusBarHeader != null) {
            indentingPrintWriter.println("headerHeight: " + quickStatusBarHeader.getHeight());
            indentingPrintWriter.println("Header visibility: " + visibilityToString(quickStatusBarHeader.getVisibility()));
            return;
        }
        indentingPrintWriter.println("mHeader: null");
    }
}
