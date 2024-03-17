package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.ExpandHelper;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.EmptyShadeView;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.notification.LaunchAnimationParameters;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpTouchHelper;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import com.android.systemui.statusbar.policy.ScrollAdapter;
import com.android.systemui.util.Assert;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.LargeScreenUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NotificationStackScrollLayout extends ViewGroup implements Dumpable {
    public static final boolean SPEW = Log.isLoggable("StackScroller", 2);
    public final int DELAY_BEFORE_SHADE_CLOSE = 200;
    public boolean mActivateNeedsAnimation;
    public int mActivePointerId;
    public ArrayList<View> mAddedHeadsUpChildren;
    public final AmbientState mAmbientState;
    public boolean mAnimateBottomOnLayout;
    public boolean mAnimateNextBackgroundBottom;
    public boolean mAnimateNextBackgroundTop;
    public boolean mAnimateNextSectionBoundsChange;
    public boolean mAnimateNextTopPaddingChange;
    public boolean mAnimateStackYForContentHeightChange;
    public ArrayList<AnimationEvent> mAnimationEvents;
    public HashSet<Runnable> mAnimationFinishedRunnables;
    public boolean mAnimationRunning;
    public boolean mAnimationsEnabled;
    public final Rect mBackgroundAnimationRect;
    public final Paint mBackgroundPaint;
    public ViewTreeObserver.OnPreDrawListener mBackgroundUpdater;
    public float mBackgroundXFactor;
    public boolean mBackwardScrollable;
    public int mBgColor;
    public float[] mBgCornerRadii;
    public int mBottomInset;
    public int mBottomPadding;
    public int mCachedBackgroundColor;
    public CentralSurfaces mCentralSurfaces;
    public boolean mChangePositionInProgress;
    public boolean mCheckForLeavebehind;
    public boolean mChildTransferInProgress;
    public ArrayList<ExpandableView> mChildrenChangingPositions;
    public HashSet<ExpandableView> mChildrenToAddAnimated;
    public ArrayList<ExpandableView> mChildrenToRemoveAnimated;
    public boolean mChildrenUpdateRequested;
    public ViewTreeObserver.OnPreDrawListener mChildrenUpdater;
    public ClearAllAnimationListener mClearAllAnimationListener;
    public boolean mClearAllEnabled;
    public boolean mClearAllInProgress;
    public ClearAllListener mClearAllListener;
    public HashSet<ExpandableView> mClearTransientViewsWhenFinished;
    public final Rect mClipRect;
    public int mCollapsedSize;
    public int mContentHeight;
    public boolean mContinuousBackgroundUpdate;
    public boolean mContinuousShadowUpdate;
    public NotificationStackScrollLayoutController mController;
    public int mCornerRadius;
    public int mCurrentStackHeight = Integer.MAX_VALUE;
    public final boolean mDebugLines;
    public Paint mDebugPaint;
    public final boolean mDebugRemoveAnimation;
    public Set<Integer> mDebugTextUsedYPositions;
    public float mDimAmount;
    public ValueAnimator mDimAnimator;
    public final Animator.AnimatorListener mDimEndListener;
    public ValueAnimator.AnimatorUpdateListener mDimUpdateListener;
    public boolean mDimmedNeedsAnimation;
    public boolean mDisallowDismissInThisMotion;
    public boolean mDisallowScrollingInThisMotion;
    public boolean mDismissUsingRowTranslationX;
    public boolean mDontClampNextScroll;
    public boolean mDontReportNextOverScroll;
    public int mDownX;
    public EmptyShadeView mEmptyShadeView;
    public boolean mEverythingNeedsAnimation;
    public ExpandHelper mExpandHelper;
    public ExpandHelper.Callback mExpandHelperCallback;
    public ExpandableView mExpandedGroupView;
    public float mExpandedHeight;
    public ArrayList<BiConsumer<Float, Float>> mExpandedHeightListeners;
    public boolean mExpandedInThisMotion;
    public boolean mExpandingNotification;
    public ExpandableNotificationRow mExpandingNotificationRow;
    public float mExtraTopInsetForFullShadeTransition;
    public Runnable mFinishScrollingCallback;
    public boolean mFlingAfterUpEvent;
    public FooterClearAllListener mFooterClearAllListener;
    public FooterView mFooterView;
    public boolean mForceNoOverlappingRendering;
    public View mForcedScroll;
    public boolean mForwardScrollable;
    public HashSet<View> mFromMoreCardAdditions;
    public int mGapHeight;
    public boolean mGenerateChildOrderChangedEvent;
    public long mGoToFullShadeDelay;
    public boolean mGoToFullShadeNeedsAnimation;
    public GroupExpansionManager mGroupExpansionManager;
    public GroupMembershipManager mGroupMembershipManager;
    public boolean mHeadsUpAnimatingAway;
    public HeadsUpAppearanceController mHeadsUpAppearanceController;
    public final HeadsUpTouchHelper.Callback mHeadsUpCallback;
    public HashSet<Pair<ExpandableNotificationRow, Boolean>> mHeadsUpChangeAnimations;
    public boolean mHeadsUpGoingAwayAnimationsAllowed;
    public int mHeadsUpInset;
    public boolean mHideSensitiveNeedsAnimation;
    public Interpolator mHideXInterpolator;
    public boolean mHighPriorityBeforeSpeedBump;
    public boolean mInHeadsUpPinnedMode;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public float mInterpolatedHideAmount;
    public float mIntrinsicContentHeight;
    public int mIntrinsicPadding;
    public boolean mIsBeingDragged;
    public boolean mIsClipped;
    public boolean mIsCurrentUserSetup;
    public boolean mIsExpanded;
    public boolean mIsExpansionChanging;
    public boolean mIsRemoteInputActive;
    public float mKeyguardBottomPadding;
    public boolean mKeyguardBypassEnabled;
    public int mLastMotionY;
    public float mLastSentAppear;
    public float mLastSentExpandedHeight;
    public LaunchAnimationParameters mLaunchAnimationParams;
    public final Path mLaunchedNotificationClipPath;
    public float[] mLaunchedNotificationRadii;
    public boolean mLaunchingNotification;
    public boolean mLaunchingNotificationNeedsToBeClipped;
    public float mLinearHideAmount;
    public NotificationLogger.OnChildLocationsChangedListener mListener;
    public NotificationStackScrollLogger mLogger;
    public View.OnClickListener mManageButtonClickListener;
    public int mMaxDisplayedNotifications;
    public int mMaxLayoutHeight;
    public float mMaxOverScroll;
    public int mMaxScrollAfterExpand;
    public int mMaxTopPadding;
    public int mMaximumVelocity;
    public int mMinInteractionHeight;
    public float mMinTopOverScrollToEscape;
    public int mMinimumPaddings;
    public int mMinimumVelocity;
    public boolean mNeedViewResizeAnimation;
    public boolean mNeedsAnimation;
    public NotificationStackSizeCalculator mNotificationStackSizeCalculator;
    public long mNumHeadsUp;
    public final ExpandableView.OnHeightChangedListener mOnChildHeightChangedListener;
    public OnEmptySpaceClickListener mOnEmptySpaceClickListener;
    public ExpandableView.OnHeightChangedListener mOnHeightChangedListener;
    public Consumer<Boolean> mOnStackYChanged;
    public boolean mOnlyScrollingInThisMotion;
    public final ViewOutlineProvider mOutlineProvider;
    public float mOverScrolledBottomPixels;
    public float mOverScrolledTopPixels;
    public int mOverflingDistance;
    public OnOverscrollTopChangedListener mOverscrollTopChangedListener;
    public int mOwnScrollY;
    public int mPaddingBetweenElements;
    public boolean mPanelTracking;
    public boolean mPulsing;
    public float mQsExpansionFraction;
    public boolean mQsFullScreen;
    public ViewGroup mQsHeader;
    public Rect mQsHeaderBound;
    public int mQsScrollBoundaryPosition;
    public int mQsTilePadding;
    public Runnable mReclamp;
    public Runnable mReflingAndAnimateScroll;
    public Rect mRequestedClipBounds;
    public final Path mRoundedClipPath;
    public int mRoundedRectClippingBottom;
    public int mRoundedRectClippingLeft;
    public int mRoundedRectClippingRight;
    public int mRoundedRectClippingTop;
    public ViewTreeObserver.OnPreDrawListener mRunningAnimationUpdater;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public final ScrollAdapter mScrollAdapter;
    public Consumer<Integer> mScrollListener;
    public boolean mScrollable;
    public boolean mScrolledToTopOnFirstDown;
    public OverScroller mScroller;
    public boolean mScrollingEnabled;
    public NotificationSection[] mSections;
    public final NotificationSectionsManager mSectionsManager;
    public ShadeController mShadeController;
    public boolean mShadeNeedsToClose = false;
    public ViewTreeObserver.OnPreDrawListener mShadowUpdater;
    public NotificationShelf mShelf;
    public final boolean mShouldDrawNotificationBackground;
    public boolean mShouldShowShelfOnly;
    public boolean mShouldUseRoundedRectClipping;
    public boolean mShouldUseSplitNotificationShade;
    public int mSidePaddings;
    public boolean mSkinnyNotifsInLandscape;
    public float mSlopMultiplier;
    public int mSpeedBumpIndex;
    public boolean mSpeedBumpIndexDirty;
    public final int mSplitShadeMinContentHeight;
    public final StackScrollAlgorithm mStackScrollAlgorithm;
    public float mStackTranslation;
    public final StackStateAnimator mStateAnimator;
    @VisibleForTesting
    public int mStatusBarHeight;
    public int mStatusBarState;
    public NotificationSwipeHelper mSwipeHelper;
    public ArrayList<View> mSwipedOutViews;
    public int[] mTempInt2;
    public final ArrayList<Pair<ExpandableNotificationRow, Boolean>> mTmpList;
    public final Rect mTmpRect;
    public ArrayList<ExpandableView> mTmpSortedChildren;
    public NotificationEntry mTopHeadsUpEntry;
    public int mTopPadding;
    public boolean mTopPaddingNeedsAnimation;
    public float mTopPaddingOverflow;
    public NotificationStackScrollLayoutController.TouchHandler mTouchHandler;
    public boolean mTouchIsClick;
    public int mTouchSlop;
    public boolean mTrackingHeadsUp;
    public int mUpcomingStatusBarState;
    public VelocityTracker mVelocityTracker;
    public Comparator<ExpandableView> mViewPositionComparator;
    public int mWaterfallTopInset;
    public boolean mWillExpand;

    public interface ClearAllAnimationListener {
        void onAnimationEnd(List<ExpandableNotificationRow> list, int i);
    }

    public interface ClearAllListener {
        void onClearAll(int i);
    }

    public interface FooterClearAllListener {
        void onClearAll();
    }

    public interface OnEmptySpaceClickListener {
        void onEmptySpaceClicked(float f, float f2);
    }

    public interface OnOverscrollTopChangedListener {
        void flingTopOverscroll(float f, boolean z);

        void onOverscrollTopChanged(float f, boolean z);
    }

    public ViewGroup getViewParentForNotification(NotificationEntry notificationEntry) {
        return this;
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0() {
        updateViewShadows();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1() {
        updateBackground();
        return true;
    }

    public static /* synthetic */ int lambda$new$2(ExpandableView expandableView, ExpandableView expandableView2) {
        float translationY = expandableView.getTranslationY() + ((float) expandableView.getActualHeight());
        float translationY2 = expandableView2.getTranslationY() + ((float) expandableView2.getActualHeight());
        if (translationY < translationY2) {
            return -1;
        }
        return translationY > translationY2 ? 1 : 0;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationStackScrollLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        boolean z = false;
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        this.mActivePointerId = -1;
        this.mBottomInset = 0;
        this.mChildrenToAddAnimated = new HashSet<>();
        this.mAddedHeadsUpChildren = new ArrayList<>();
        this.mChildrenToRemoveAnimated = new ArrayList<>();
        this.mChildrenChangingPositions = new ArrayList<>();
        this.mFromMoreCardAdditions = new HashSet<>();
        this.mAnimationEvents = new ArrayList<>();
        this.mSwipedOutViews = new ArrayList<>();
        this.mStateAnimator = new StackStateAnimator(this);
        this.mSpeedBumpIndex = -1;
        this.mSpeedBumpIndexDirty = true;
        this.mIsExpanded = true;
        this.mChildrenUpdater = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                NotificationStackScrollLayout.this.updateForcedScroll();
                NotificationStackScrollLayout.this.updateChildren();
                NotificationStackScrollLayout.this.mChildrenUpdateRequested = false;
                NotificationStackScrollLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        };
        this.mTempInt2 = new int[2];
        this.mAnimationFinishedRunnables = new HashSet<>();
        this.mClearTransientViewsWhenFinished = new HashSet<>();
        this.mHeadsUpChangeAnimations = new HashSet<>();
        this.mTmpList = new ArrayList<>();
        this.mRunningAnimationUpdater = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                NotificationStackScrollLayout.this.onPreDrawDuringAnimation();
                return true;
            }
        };
        this.mTmpSortedChildren = new ArrayList<>();
        this.mDimEndListener = new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                NotificationStackScrollLayout.this.mDimAnimator = null;
            }
        };
        this.mDimUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NotificationStackScrollLayout.this.setDimAmount(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        };
        this.mQsHeaderBound = new Rect();
        this.mShadowUpdater = new NotificationStackScrollLayout$$ExternalSyntheticLambda2(this);
        this.mBackgroundUpdater = new NotificationStackScrollLayout$$ExternalSyntheticLambda3(this);
        this.mViewPositionComparator = new NotificationStackScrollLayout$$ExternalSyntheticLambda4();
        AnonymousClass5 r3 = new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                if (NotificationStackScrollLayout.this.mAmbientState.isHiddenAtAll()) {
                    outline.setRoundRect(NotificationStackScrollLayout.this.mBackgroundAnimationRect, MathUtils.lerp(((float) NotificationStackScrollLayout.this.mCornerRadius) / 2.0f, (float) NotificationStackScrollLayout.this.mCornerRadius, NotificationStackScrollLayout.this.mHideXInterpolator.getInterpolation((1.0f - NotificationStackScrollLayout.this.mLinearHideAmount) * NotificationStackScrollLayout.this.mBackgroundXFactor)));
                    outline.setAlpha(1.0f - NotificationStackScrollLayout.this.mAmbientState.getHideAmount());
                    return;
                }
                ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
            }
        };
        this.mOutlineProvider = r3;
        this.mInterpolatedHideAmount = 0.0f;
        this.mLinearHideAmount = 0.0f;
        this.mBackgroundXFactor = 1.0f;
        this.mMaxDisplayedNotifications = -1;
        this.mKeyguardBottomPadding = -1.0f;
        this.mClipRect = new Rect();
        this.mHeadsUpGoingAwayAnimationsAllowed = true;
        this.mReflingAndAnimateScroll = new NotificationStackScrollLayout$$ExternalSyntheticLambda5(this);
        this.mBackgroundAnimationRect = new Rect();
        this.mExpandedHeightListeners = new ArrayList<>();
        this.mTmpRect = new Rect();
        this.mHideXInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        this.mRoundedClipPath = new Path();
        this.mLaunchedNotificationClipPath = new Path();
        this.mShouldUseRoundedRectClipping = false;
        this.mBgCornerRadii = new float[8];
        this.mAnimateStackYForContentHeightChange = false;
        this.mLaunchedNotificationRadii = new float[8];
        this.mDismissUsingRowTranslationX = true;
        this.mOnChildHeightChangedListener = new ExpandableView.OnHeightChangedListener() {
            public void onHeightChanged(ExpandableView expandableView, boolean z) {
                NotificationStackScrollLayout.this.onChildHeightChanged(expandableView, z);
            }

            public void onReset(ExpandableView expandableView) {
                NotificationStackScrollLayout.this.onChildHeightReset(expandableView);
            }
        };
        AnonymousClass7 r1 = new ScrollAdapter() {
            public boolean isScrolledToTop() {
                return NotificationStackScrollLayout.this.mOwnScrollY == 0;
            }

            public boolean isScrolledToBottom() {
                return NotificationStackScrollLayout.this.mOwnScrollY >= NotificationStackScrollLayout.this.getScrollRange();
            }

            public View getHostView() {
                return NotificationStackScrollLayout.this;
            }
        };
        this.mScrollAdapter = r1;
        this.mReclamp = new Runnable() {
            public void run() {
                NotificationStackScrollLayout.this.mScroller.startScroll(NotificationStackScrollLayout.this.mScrollX, NotificationStackScrollLayout.this.mOwnScrollY, 0, NotificationStackScrollLayout.this.getScrollRange() - NotificationStackScrollLayout.this.mOwnScrollY);
                NotificationStackScrollLayout.this.mDontReportNextOverScroll = true;
                NotificationStackScrollLayout.this.mDontClampNextScroll = true;
                NotificationStackScrollLayout.this.lambda$new$3();
            }
        };
        this.mHeadsUpCallback = new HeadsUpTouchHelper.Callback() {
            public ExpandableView getChildAtRawPosition(float f, float f2) {
                return NotificationStackScrollLayout.this.getChildAtRawPosition(f, f2);
            }

            public boolean isExpanded() {
                return NotificationStackScrollLayout.this.mIsExpanded;
            }

            public Context getContext() {
                return NotificationStackScrollLayout.this.mContext;
            }
        };
        this.mExpandHelperCallback = new ExpandHelper.Callback() {
            public ExpandableView getChildAtPosition(float f, float f2) {
                return NotificationStackScrollLayout.this.getChildAtPosition(f, f2);
            }

            public ExpandableView getChildAtRawPosition(float f, float f2) {
                return NotificationStackScrollLayout.this.getChildAtRawPosition(f, f2);
            }

            public boolean canChildBeExpanded(View view) {
                if (view instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                    return expandableNotificationRow.isExpandable() && !expandableNotificationRow.areGutsExposed() && (NotificationStackScrollLayout.this.mIsExpanded || !expandableNotificationRow.isPinned());
                }
            }

            public void setUserExpandedChild(View view, boolean z) {
                if (view instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                    if (!z || !NotificationStackScrollLayout.this.onKeyguard()) {
                        expandableNotificationRow.setUserExpanded(z, true);
                        expandableNotificationRow.onExpandedByGesture(z);
                        return;
                    }
                    expandableNotificationRow.setUserLocked(false);
                    NotificationStackScrollLayout.this.updateContentHeight();
                    NotificationStackScrollLayout.this.notifyHeightChangeListener(expandableNotificationRow);
                }
            }

            public void setExpansionCancelled(View view) {
                if (view instanceof ExpandableNotificationRow) {
                    ((ExpandableNotificationRow) view).setGroupExpansionChanging(false);
                }
            }

            public void setUserLockedChild(View view, boolean z) {
                if (view instanceof ExpandableNotificationRow) {
                    ((ExpandableNotificationRow) view).setUserLocked(z);
                }
                NotificationStackScrollLayout.this.cancelLongPress();
                NotificationStackScrollLayout.this.requestDisallowInterceptTouchEvent(true);
            }

            public void expansionStateChanged(boolean z) {
                NotificationStackScrollLayout.this.mExpandingNotification = z;
                if (!NotificationStackScrollLayout.this.mExpandedInThisMotion) {
                    NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
                    notificationStackScrollLayout.mMaxScrollAfterExpand = notificationStackScrollLayout.mOwnScrollY;
                    NotificationStackScrollLayout.this.mExpandedInThisMotion = true;
                }
            }

            public int getMaxExpandHeight(ExpandableView expandableView) {
                return expandableView.getMaxContentHeight();
            }
        };
        Resources resources = getResources();
        FeatureFlags featureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
        boolean isEnabled = featureFlags.isEnabled(Flags.NSSL_DEBUG_LINES);
        this.mDebugLines = isEnabled;
        this.mDebugRemoveAnimation = featureFlags.isEnabled(Flags.NSSL_DEBUG_REMOVE_ANIMATION);
        NotificationSectionsManager notificationSectionsManager = (NotificationSectionsManager) Dependency.get(NotificationSectionsManager.class);
        this.mSectionsManager = notificationSectionsManager;
        this.mScreenOffAnimationController = (ScreenOffAnimationController) Dependency.get(ScreenOffAnimationController.class);
        updateSplitNotificationShade();
        notificationSectionsManager.initialize(this);
        this.mSections = notificationSectionsManager.createSectionsForBuckets();
        this.mAmbientState = (AmbientState) Dependency.get(AmbientState.class);
        this.mBgColor = Utils.getColorAttr(this.mContext, 16844002).getDefaultColor();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.notification_min_height);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.notification_max_height);
        this.mSplitShadeMinContentHeight = resources.getDimensionPixelSize(R$dimen.nssl_split_shade_min_content_height);
        ExpandHelper expandHelper = new ExpandHelper(getContext(), this.mExpandHelperCallback, dimensionPixelSize, dimensionPixelSize2);
        this.mExpandHelper = expandHelper;
        expandHelper.setEventSource(this);
        this.mExpandHelper.setScrollAdapter(r1);
        this.mStackScrollAlgorithm = createStackScrollAlgorithm(context);
        boolean z2 = resources.getBoolean(R$bool.config_drawNotificationBackground);
        this.mShouldDrawNotificationBackground = z2;
        setOutlineProvider(r3);
        setWillNotDraw(!((z2 || isEnabled) ? true : z));
        paint.setAntiAlias(true);
        if (isEnabled) {
            Paint paint2 = new Paint();
            this.mDebugPaint = paint2;
            paint2.setColor(-65536);
            this.mDebugPaint.setStrokeWidth(2.0f);
            this.mDebugPaint.setStyle(Paint.Style.STROKE);
            this.mDebugPaint.setTextSize(25.0f);
        }
        this.mClearAllEnabled = resources.getBoolean(R$bool.config_enableNotificationsClearAll);
        this.mGroupMembershipManager = (GroupMembershipManager) Dependency.get(GroupMembershipManager.class);
        this.mGroupExpansionManager = (GroupExpansionManager) Dependency.get(GroupExpansionManager.class);
        setImportantForAccessibility(1);
    }

    public void setOverExpansion(float f) {
        this.mAmbientState.setOverExpansion(f);
        updateStackPosition();
        requestChildrenUpdate();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        inflateEmptyShadeView();
        inflateFooterView();
    }

    public void setKeyguardBypassEnabled(boolean z) {
        this.mKeyguardBypassEnabled = z;
    }

    public float getWakeUpHeight() {
        int collapsedHeight;
        ExpandableView firstChildWithBackground = getFirstChildWithBackground();
        if (firstChildWithBackground == null) {
            return 0.0f;
        }
        if (this.mKeyguardBypassEnabled) {
            collapsedHeight = firstChildWithBackground.getHeadsUpHeightWithoutHeader();
        } else {
            collapsedHeight = firstChildWithBackground.getCollapsedHeight();
        }
        return (float) collapsedHeight;
    }

    public void setLogger(NotificationStackScrollLogger notificationStackScrollLogger) {
        this.mLogger = notificationStackScrollLogger;
    }

    public float getNotificationSquishinessFraction() {
        return this.mStackScrollAlgorithm.getNotificationSquishinessFraction(this.mAmbientState);
    }

    public void reinflateViews() {
        inflateFooterView();
        inflateEmptyShadeView();
        updateFooter();
        this.mSectionsManager.reinflateViews();
    }

    public void setIsRemoteInputActive(boolean z) {
        this.mIsRemoteInputActive = z;
        updateFooter();
    }

    @VisibleForTesting
    public void updateFooter() {
        if (this.mFooterView != null) {
            boolean z = true;
            boolean z2 = this.mClearAllEnabled && this.mController.hasActiveClearableNotifications(0);
            if ((!z2 && this.mController.getVisibleNotificationCount() <= 0) || !this.mIsCurrentUserSetup || onKeyguard() || this.mUpcomingStatusBarState == 1 || ((this.mQsExpansionFraction == 1.0f && this.mQsFullScreen) || this.mScreenOffAnimationController.shouldHideNotificationsFooter() || this.mIsRemoteInputActive)) {
                z = false;
            }
            updateFooterView(z, z2, this.mController.isHistoryEnabled());
        }
    }

    public boolean hasActiveClearableNotifications(int i) {
        return this.mController.hasActiveClearableNotifications(i);
    }

    public void updateBgColor() {
        this.mBgColor = Utils.getColorAttr(this.mContext, 16844002).getDefaultColor();
        updateBackgroundDimming();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ActivatableNotificationView) {
                ((ActivatableNotificationView) childAt).updateBackgroundColors();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(android.graphics.Canvas r4) {
        /*
            r3 = this;
            boolean r0 = r3.mShouldDrawNotificationBackground
            if (r0 == 0) goto L_0x002a
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r0 = r3.mSections
            r1 = 0
            r0 = r0[r1]
            android.graphics.Rect r0 = r0.getCurrentBounds()
            int r0 = r0.top
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r1 = r3.mSections
            int r2 = r1.length
            int r2 = r2 + -1
            r1 = r1[r2]
            android.graphics.Rect r1 = r1.getCurrentBounds()
            int r1 = r1.bottom
            if (r0 < r1) goto L_0x0026
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r3.mAmbientState
            boolean r0 = r0.isDozing()
            if (r0 == 0) goto L_0x002a
        L_0x0026:
            r3.drawBackground(r4)
            goto L_0x0035
        L_0x002a:
            boolean r0 = r3.mInHeadsUpPinnedMode
            if (r0 != 0) goto L_0x0032
            boolean r0 = r3.mHeadsUpAnimatingAway
            if (r0 == 0) goto L_0x0035
        L_0x0032:
            r3.drawHeadsUpBackground(r4)
        L_0x0035:
            boolean r0 = r3.mDebugLines
            if (r0 == 0) goto L_0x003c
            r3.onDrawDebug(r4)
        L_0x003c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onDraw(android.graphics.Canvas):void");
    }

    public final void logHunSkippedForUnexpectedState(String str, boolean z, boolean z2) {
        NotificationStackScrollLogger notificationStackScrollLogger = this.mLogger;
        if (notificationStackScrollLogger != null) {
            notificationStackScrollLogger.hunSkippedForUnexpectedState(str, z, z2);
        }
    }

    public final void logHunAnimationSkipped(String str, String str2) {
        NotificationStackScrollLogger notificationStackScrollLogger = this.mLogger;
        if (notificationStackScrollLogger != null) {
            notificationStackScrollLogger.hunAnimationSkipped(str, str2);
        }
    }

    public final void logHunAnimationEventAdded(String str, int i) {
        NotificationStackScrollLogger notificationStackScrollLogger = this.mLogger;
        if (notificationStackScrollLogger != null) {
            notificationStackScrollLogger.hunAnimationEventAdded(str, i);
        }
    }

    public final void onDrawDebug(Canvas canvas) {
        Set<Integer> set = this.mDebugTextUsedYPositions;
        if (set == null) {
            this.mDebugTextUsedYPositions = new HashSet();
        } else {
            set.clear();
        }
        drawDebugInfo(canvas, 0, -65536, "y = " + 0);
        int i = this.mTopPadding;
        drawDebugInfo(canvas, i, -65536, "mTopPadding = " + i);
        int layoutHeight = getLayoutHeight();
        drawDebugInfo(canvas, layoutHeight, -256, "getLayoutHeight() = " + layoutHeight);
        int i2 = this.mMaxLayoutHeight;
        drawDebugInfo(canvas, i2, -65281, "mMaxLayoutHeight = " + i2);
        if (this.mKeyguardBottomPadding >= 0.0f) {
            int height = getHeight() - ((int) this.mKeyguardBottomPadding);
            drawDebugInfo(canvas, height, -65536, "getHeight() - mKeyguardBottomPadding = " + height);
        }
        int height2 = getHeight() - getEmptyBottomMargin();
        drawDebugInfo(canvas, height2, -16711936, "getHeight() - getEmptyBottomMargin() = " + height2);
        int stackY = (int) this.mAmbientState.getStackY();
        drawDebugInfo(canvas, stackY, -16711681, "mAmbientState.getStackY() = " + stackY);
        int stackY2 = (int) (this.mAmbientState.getStackY() + this.mAmbientState.getStackHeight());
        drawDebugInfo(canvas, stackY2, -3355444, "mAmbientState.getStackY() + mAmbientState.getStackHeight() = " + stackY2);
        int stackY3 = ((int) this.mAmbientState.getStackY()) + this.mContentHeight;
        drawDebugInfo(canvas, stackY3, -65281, "mAmbientState.getStackY() + mContentHeight = " + stackY3);
        int stackY4 = (int) (this.mAmbientState.getStackY() + this.mIntrinsicContentHeight);
        drawDebugInfo(canvas, stackY4, -256, "mAmbientState.getStackY() + mIntrinsicContentHeight = " + stackY4);
        int i3 = this.mRoundedRectClippingBottom;
        drawDebugInfo(canvas, i3, -12303292, "mRoundedRectClippingBottom) = " + stackY4);
    }

    public final void drawDebugInfo(Canvas canvas, int i, int i2, String str) {
        this.mDebugPaint.setColor(i2);
        float f = (float) i;
        canvas.drawLine(0.0f, f, (float) getWidth(), f, this.mDebugPaint);
        canvas.drawText(str, 0.0f, (float) computeDebugYTextPosition(i), this.mDebugPaint);
    }

    public final int computeDebugYTextPosition(int i) {
        while (this.mDebugTextUsedYPositions.contains(Integer.valueOf(i))) {
            i = (int) (((float) i) + this.mDebugPaint.getTextSize());
        }
        this.mDebugTextUsedYPositions.add(Integer.valueOf(i));
        return i;
    }

    public final void drawBackground(Canvas canvas) {
        boolean z;
        boolean z2;
        int i = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        boolean z3 = false;
        int i2 = this.mSections[0].getCurrentBounds().top;
        NotificationSection[] notificationSectionArr = this.mSections;
        int i3 = notificationSectionArr[notificationSectionArr.length - 1].getCurrentBounds().bottom;
        int width2 = getWidth() / 2;
        int i4 = this.mTopPadding;
        float f = 1.0f - this.mInterpolatedHideAmount;
        float interpolation = this.mHideXInterpolator.getInterpolation((1.0f - this.mLinearHideAmount) * this.mBackgroundXFactor);
        int lerp = (int) MathUtils.lerp(width2, i, interpolation);
        int lerp2 = (int) MathUtils.lerp(width2, width, interpolation);
        int lerp3 = (int) MathUtils.lerp(i4, i2, f);
        this.mBackgroundAnimationRect.set(lerp, lerp3, lerp2, (int) MathUtils.lerp(i4, i3, f));
        int i5 = lerp3 - i2;
        NotificationSection[] notificationSectionArr2 = this.mSections;
        int length = notificationSectionArr2.length;
        int i6 = 0;
        while (true) {
            if (i6 >= length) {
                z = false;
                break;
            } else if (notificationSectionArr2[i6].needsBackground()) {
                z = true;
                break;
            } else {
                i6++;
            }
        }
        if (!this.mKeyguardBypassEnabled || !onKeyguard()) {
            if (!this.mAmbientState.isDozing() || z) {
                z3 = true;
            }
            z2 = z3;
        } else {
            z2 = isPulseExpanding();
        }
        if (z2) {
            drawBackgroundRects(canvas, lerp, lerp2, lerp3, i5);
        }
        updateClipping();
    }

    public final void drawBackgroundRects(Canvas canvas, int i, int i2, int i3, int i4) {
        int i5 = i2;
        NotificationSection[] notificationSectionArr = this.mSections;
        int length = notificationSectionArr.length;
        int i6 = 1;
        int i7 = i;
        int i8 = i5;
        int i9 = this.mSections[0].getCurrentBounds().bottom + i4;
        int i10 = 0;
        boolean z = true;
        int i11 = i3;
        while (i10 < length) {
            NotificationSection notificationSection = notificationSectionArr[i10];
            if (!notificationSection.needsBackground()) {
                int i12 = i;
            } else {
                int i13 = notificationSection.getCurrentBounds().top + i4;
                int min = Math.min(Math.max(i, notificationSection.getCurrentBounds().left), i5);
                int max = Math.max(Math.min(i5, notificationSection.getCurrentBounds().right), min);
                if (i13 - i9 > i6 || (!(i7 == min && i8 == max) && !z)) {
                    float f = (float) i7;
                    float f2 = (float) i8;
                    int i14 = this.mCornerRadius;
                    canvas.drawRoundRect(f, (float) i11, f2, (float) i9, (float) i14, (float) i14, this.mBackgroundPaint);
                    i11 = i13;
                }
                i9 = notificationSection.getCurrentBounds().bottom + i4;
                i8 = max;
                i7 = min;
                z = false;
            }
            i10++;
            i5 = i2;
            i6 = 1;
        }
        int i15 = this.mCornerRadius;
        canvas.drawRoundRect((float) i7, (float) i11, (float) i8, (float) i9, (float) i15, (float) i15, this.mBackgroundPaint);
    }

    public final void drawHeadsUpBackground(Canvas canvas) {
        int i = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        int childCount = getChildCount();
        float height = (float) getHeight();
        float f = 0.0f;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                if ((expandableNotificationRow.isPinned() || expandableNotificationRow.isHeadsUpAnimatingAway()) && expandableNotificationRow.getTranslation() < 0.0f && expandableNotificationRow.getProvider().shouldShowGutsOnSnapOpen()) {
                    float min = Math.min(height, expandableNotificationRow.getTranslationY());
                    f = Math.max(f, expandableNotificationRow.getTranslationY() + ((float) expandableNotificationRow.getActualHeight()));
                    height = min;
                }
            }
        }
        if (height < f) {
            int i3 = this.mCornerRadius;
            canvas.drawRoundRect((float) i, height, (float) width, f, (float) i3, (float) i3, this.mBackgroundPaint);
        }
    }

    public void updateBackgroundDimming() {
        int blendARGB;
        if (this.mShouldDrawNotificationBackground && this.mCachedBackgroundColor != (blendARGB = ColorUtils.blendARGB(this.mBgColor, -1, MathUtils.smoothStep(0.4f, 1.0f, this.mLinearHideAmount)))) {
            this.mCachedBackgroundColor = blendARGB;
            this.mBackgroundPaint.setColor(blendARGB);
            invalidate();
        }
    }

    public final void reinitView() {
        initView(getContext(), this.mSwipeHelper, this.mNotificationStackSizeCalculator);
    }

    public void initView(Context context, NotificationSwipeHelper notificationSwipeHelper, NotificationStackSizeCalculator notificationStackSizeCalculator) {
        this.mScroller = new OverScroller(getContext());
        this.mSwipeHelper = notificationSwipeHelper;
        this.mNotificationStackSizeCalculator = notificationStackSizeCalculator;
        setDescendantFocusability(262144);
        setClipChildren(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mOverflingDistance = viewConfiguration.getScaledOverflingDistance();
        Resources resources = context.getResources();
        this.mCollapsedSize = resources.getDimensionPixelSize(R$dimen.notification_min_height);
        this.mGapHeight = resources.getDimensionPixelSize(R$dimen.notification_section_divider_height);
        this.mStackScrollAlgorithm.initView(context);
        this.mAmbientState.reload(context);
        this.mPaddingBetweenElements = Math.max(1, resources.getDimensionPixelSize(R$dimen.notification_divider_height));
        this.mMinTopOverScrollToEscape = (float) resources.getDimensionPixelSize(R$dimen.min_top_overscroll_to_qs);
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        this.mBottomPadding = resources.getDimensionPixelSize(R$dimen.notification_panel_padding_bottom);
        this.mMinimumPaddings = resources.getDimensionPixelSize(R$dimen.notification_side_paddings);
        this.mQsTilePadding = resources.getDimensionPixelOffset(R$dimen.qs_tile_margin_horizontal);
        this.mSkinnyNotifsInLandscape = resources.getBoolean(R$bool.config_skinnyNotifsInLandscape);
        this.mSidePaddings = this.mMinimumPaddings;
        this.mMinInteractionHeight = resources.getDimensionPixelSize(R$dimen.notification_min_interaction_height);
        this.mCornerRadius = resources.getDimensionPixelSize(R$dimen.notification_corner_radius);
        this.mHeadsUpInset = this.mStatusBarHeight + resources.getDimensionPixelSize(R$dimen.heads_up_status_bar_padding);
        this.mQsScrollBoundaryPosition = SystemBarUtils.getQuickQsOffsetHeight(this.mContext);
    }

    public void updateSidePadding(int i) {
        if (i == 0 || !this.mSkinnyNotifsInLandscape) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else if (getResources().getConfiguration().orientation == 1) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else {
            int i2 = this.mMinimumPaddings;
            int i3 = this.mQsTilePadding;
            this.mSidePaddings = i2 + (((i - (i2 * 2)) - (i3 * 3)) / 4) + i3;
        }
    }

    public void updateCornerRadius() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.notification_corner_radius);
        if (this.mCornerRadius != dimensionPixelSize) {
            this.mCornerRadius = dimensionPixelSize;
            invalidate();
        }
    }

    public final void notifyHeightChangeListener(ExpandableView expandableView) {
        notifyHeightChangeListener(expandableView, false);
    }

    public final void notifyHeightChangeListener(ExpandableView expandableView, boolean z) {
        ExpandableView.OnHeightChangedListener onHeightChangedListener = this.mOnHeightChangedListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onHeightChanged(expandableView, z);
        }
    }

    public boolean isPulseExpanding() {
        return this.mAmbientState.isPulseExpanding();
    }

    public int getSpeedBumpIndex() {
        if (this.mSpeedBumpIndexDirty) {
            this.mSpeedBumpIndexDirty = false;
            int childCount = getChildCount();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    i2++;
                    boolean z = true;
                    if (!this.mHighPriorityBeforeSpeedBump) {
                        z = true ^ expandableNotificationRow.getEntry().isAmbient();
                    } else if (expandableNotificationRow.getEntry().getBucket() >= 6) {
                        z = false;
                    }
                    if (z) {
                        i = i2;
                    }
                }
            }
            this.mSpeedBumpIndex = i;
        }
        return this.mSpeedBumpIndex;
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        updateSidePadding(size);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size - (this.mSidePaddings * 2), View.MeasureSpec.getMode(i));
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 0);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            measureChild(getChildAt(i3), makeMeasureSpec, makeMeasureSpec2);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float width = ((float) getWidth()) / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            float measuredWidth = ((float) childAt.getMeasuredWidth()) / 2.0f;
            childAt.layout((int) (width - measuredWidth), 0, (int) (measuredWidth + width), (int) ((float) childAt.getMeasuredHeight()));
        }
        setMaxLayoutHeight(getHeight());
        updateContentHeight();
        clampScrollPosition();
        requestChildrenUpdate();
        updateFirstAndLastBackgroundViews();
        updateAlgorithmLayoutMinHeight();
        updateOwnTranslationZ();
        this.mAnimateStackYForContentHeightChange = false;
    }

    public final void requestAnimationOnViewResize(ExpandableNotificationRow expandableNotificationRow) {
        if (!this.mAnimationsEnabled) {
            return;
        }
        if (this.mIsExpanded || (expandableNotificationRow != null && expandableNotificationRow.isPinned())) {
            this.mNeedViewResizeAnimation = true;
            this.mNeedsAnimation = true;
        }
    }

    public void setChildLocationsChangedListener(NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener) {
        this.mListener = onChildLocationsChangedListener;
    }

    public final void setMaxLayoutHeight(int i) {
        this.mMaxLayoutHeight = i;
        updateAlgorithmHeightAndPadding();
    }

    public final void updateAlgorithmHeightAndPadding() {
        this.mAmbientState.setLayoutHeight(getLayoutHeight());
        this.mAmbientState.setLayoutMaxHeight(this.mMaxLayoutHeight);
        updateAlgorithmLayoutMinHeight();
        this.mAmbientState.setTopPadding(this.mTopPadding);
    }

    public final void updateAlgorithmLayoutMinHeight() {
        int i;
        AmbientState ambientState = this.mAmbientState;
        if (this.mQsFullScreen || isHeadsUpTransition()) {
            i = getLayoutMinHeight();
        } else {
            i = 0;
        }
        ambientState.setLayoutMinHeight(i);
    }

    public final void updateChildren() {
        float f;
        updateScrollStateForAddedChildren();
        AmbientState ambientState = this.mAmbientState;
        if (this.mScroller.isFinished()) {
            f = 0.0f;
        } else {
            f = this.mScroller.getCurrVelocity();
        }
        ambientState.setCurrentScrollVelocity(f);
        this.mStackScrollAlgorithm.resetViewStates(this.mAmbientState, getSpeedBumpIndex());
        if (isCurrentlyAnimating() || this.mNeedsAnimation) {
            startAnimationToState();
        } else {
            applyCurrentState();
        }
    }

    public final void onPreDrawDuringAnimation() {
        this.mShelf.updateAppearance();
        if (!this.mNeedsAnimation && !this.mChildrenUpdateRequested) {
            updateBackground();
        }
    }

    public final void updateScrollStateForAddedChildren() {
        if (!this.mChildrenToAddAnimated.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                ExpandableView expandableView = (ExpandableView) getChildAt(i);
                if (this.mChildrenToAddAnimated.contains(expandableView)) {
                    int positionInLinearLayout = getPositionInLinearLayout(expandableView);
                    int intrinsicHeight = getIntrinsicHeight(expandableView) + this.mPaddingBetweenElements;
                    int i2 = this.mOwnScrollY;
                    if (positionInLinearLayout < i2) {
                        setOwnScrollY(i2 + intrinsicHeight);
                    }
                }
            }
            clampScrollPosition();
        }
    }

    public final void updateForcedScroll() {
        View view = this.mForcedScroll;
        if (view != null && (!view.hasFocus() || !this.mForcedScroll.isAttachedToWindow())) {
            this.mForcedScroll = null;
        }
        View view2 = this.mForcedScroll;
        if (view2 != null) {
            ExpandableView expandableView = (ExpandableView) view2;
            int positionInLinearLayout = getPositionInLinearLayout(expandableView);
            int targetScrollForView = targetScrollForView(expandableView, positionInLinearLayout);
            int intrinsicHeight = positionInLinearLayout + expandableView.getIntrinsicHeight();
            int max = Math.max(0, Math.min(targetScrollForView, getScrollRange()));
            int i = this.mOwnScrollY;
            if (i < max || intrinsicHeight < i) {
                setOwnScrollY(max);
            }
        }
    }

    public void requestChildrenUpdate() {
        if (!this.mChildrenUpdateRequested) {
            getViewTreeObserver().addOnPreDrawListener(this.mChildrenUpdater);
            this.mChildrenUpdateRequested = true;
            invalidate();
        }
    }

    public final boolean isCurrentlyAnimating() {
        return this.mStateAnimator.isRunning();
    }

    public final void clampScrollPosition() {
        int scrollRange = getScrollRange();
        if (scrollRange < this.mOwnScrollY && !this.mAmbientState.isClearAllInProgress()) {
            boolean z = false;
            if (scrollRange < getScrollAmountToScrollBoundary() && this.mAnimateStackYForContentHeightChange) {
                z = true;
            }
            setOwnScrollY(scrollRange, z);
        }
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public final void setTopPadding(int i, boolean z) {
        if (this.mTopPadding != i) {
            boolean z2 = z || this.mAnimateNextTopPaddingChange;
            this.mTopPadding = i;
            updateAlgorithmHeightAndPadding();
            updateContentHeight();
            if (z2 && this.mAnimationsEnabled && this.mIsExpanded) {
                this.mTopPaddingNeedsAnimation = true;
                this.mNeedsAnimation = true;
            }
            updateStackPosition();
            requestChildrenUpdate();
            notifyHeightChangeListener((ExpandableView) null, z2);
            this.mAnimateNextTopPaddingChange = false;
        }
    }

    public final void updateStackPosition() {
        updateStackPosition(false);
    }

    public final boolean shouldSkipHeightUpdate() {
        return this.mAmbientState.isOnKeyguard() && (this.mAmbientState.isUnlockHintRunning() || this.mAmbientState.isSwipingUp() || this.mAmbientState.isFlingingAfterSwipeUpOnLockscreen());
    }

    public final void updateStackPosition(boolean z) {
        float overExpansion = ((((float) this.mTopPadding) + this.mExtraTopInsetForFullShadeTransition) + this.mAmbientState.getOverExpansion()) - getCurrentOverScrollAmount(false);
        float expansionFraction = this.mAmbientState.getExpansionFraction();
        if (this.mAmbientState.isBouncerInTransit()) {
            expansionFraction = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(expansionFraction);
        }
        this.mAmbientState.setStackY(MathUtils.lerp(0.0f, overExpansion, expansionFraction));
        Consumer<Boolean> consumer = this.mOnStackYChanged;
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        updateStackEndHeightAndStackHeight(expansionFraction);
    }

    @VisibleForTesting
    public void updateStackEndHeightAndStackHeight(float f) {
        float stackHeight = this.mAmbientState.getStackHeight();
        if (this.mQsExpansionFraction > 0.0f || shouldSkipHeightUpdate()) {
            updateStackHeight(this.mAmbientState.getStackEndHeight(), f);
        } else {
            updateStackHeight(updateStackEndHeight((float) getHeight(), (float) getEmptyBottomMargin(), (float) this.mTopPadding), f);
        }
        if (stackHeight != this.mAmbientState.getStackHeight()) {
            requestChildrenUpdate();
        }
    }

    public final float updateStackEndHeight(float f, float f2, float f3) {
        float f4;
        if (this.mMaxDisplayedNotifications != -1) {
            f4 = this.mIntrinsicContentHeight;
        } else {
            f4 = Math.max(0.0f, (f - f2) - f3);
        }
        this.mAmbientState.setStackEndHeight(f4);
        return f4;
    }

    @VisibleForTesting
    public void updateStackHeight(float f, float f2) {
        float dozeAmount = this.mAmbientState.getDozeAmount();
        if (0.0f < dozeAmount && dozeAmount < 1.0f) {
            f2 = 1.0f - dozeAmount;
        }
        this.mAmbientState.setStackHeight(MathUtils.lerp(0.5f * f, f, f2));
    }

    public void setOnStackYChanged(Consumer<Boolean> consumer) {
        this.mOnStackYChanged = consumer;
    }

    public void setExpandedHeight(float f) {
        int i;
        float f2;
        float height = (float) (getHeight() - getEmptyBottomMargin());
        boolean shouldSkipHeightUpdate = shouldSkipHeightUpdate();
        if (!shouldSkipHeightUpdate) {
            this.mAmbientState.setExpansionFraction(MathUtils.saturate(f / height));
        }
        updateStackPosition();
        boolean z = true;
        float f3 = 0.0f;
        if (!shouldSkipHeightUpdate) {
            this.mExpandedHeight = f;
            setIsExpanded(f > 0.0f);
            float minExpansionHeight = (float) getMinExpansionHeight();
            if (f < minExpansionHeight) {
                Rect rect = this.mClipRect;
                rect.left = 0;
                rect.right = getWidth();
                Rect rect2 = this.mClipRect;
                rect2.top = 0;
                rect2.bottom = (int) f;
                setRequestedClipBounds(rect2);
                f = minExpansionHeight;
            } else {
                setRequestedClipBounds((Rect) null);
            }
        }
        float appearEndPosition = getAppearEndPosition();
        float appearStartPosition = getAppearStartPosition();
        float f4 = 1.0f;
        if (f >= appearEndPosition) {
            z = false;
        }
        this.mAmbientState.setAppearing(z);
        if (z) {
            f4 = calculateAppearFraction(f);
            if (f4 >= 0.0f) {
                f2 = NotificationUtils.interpolate(getExpandTranslationStart(), 0.0f, f4);
            } else {
                f2 = (f - appearStartPosition) + getExpandTranslationStart();
            }
            f3 = f2;
            i = (int) (f - f3);
            if (isHeadsUpTransition()) {
                f3 = MathUtils.lerp(this.mHeadsUpInset - this.mTopPadding, 0, f4);
            }
        } else if (this.mShouldShowShelfOnly) {
            i = this.mTopPadding + this.mShelf.getIntrinsicHeight();
        } else {
            if (this.mQsFullScreen) {
                int i2 = (this.mContentHeight - this.mTopPadding) + this.mIntrinsicPadding;
                int intrinsicHeight = this.mMaxTopPadding + this.mShelf.getIntrinsicHeight();
                if (i2 <= intrinsicHeight) {
                    i = intrinsicHeight;
                } else if (!this.mShouldUseSplitNotificationShade) {
                    f = NotificationUtils.interpolate((float) i2, (float) intrinsicHeight, this.mQsExpansionFraction);
                }
            } else if (shouldSkipHeightUpdate) {
                f = this.mExpandedHeight;
            }
            i = (int) f;
        }
        this.mAmbientState.setAppearFraction(f4);
        if (i != this.mCurrentStackHeight && !shouldSkipHeightUpdate) {
            this.mCurrentStackHeight = i;
            updateAlgorithmHeightAndPadding();
            requestChildrenUpdate();
        }
        setStackTranslation(f3);
        notifyAppearChangedListeners();
    }

    public final void notifyAppearChangedListeners() {
        float f;
        float f2;
        if (!this.mKeyguardBypassEnabled || !onKeyguard()) {
            f2 = MathUtils.saturate(calculateAppearFraction(this.mExpandedHeight));
            f = this.mExpandedHeight;
        } else {
            f2 = calculateAppearFractionBypass();
            f = getPulseHeight();
        }
        if (f2 != this.mLastSentAppear || f != this.mLastSentExpandedHeight) {
            this.mLastSentAppear = f2;
            this.mLastSentExpandedHeight = f;
            for (int i = 0; i < this.mExpandedHeightListeners.size(); i++) {
                this.mExpandedHeightListeners.get(i).accept(Float.valueOf(f), Float.valueOf(f2));
            }
        }
    }

    public final void setRequestedClipBounds(Rect rect) {
        this.mRequestedClipBounds = rect;
        updateClipping();
    }

    public int getIntrinsicContentHeight() {
        return (int) this.mIntrinsicContentHeight;
    }

    public void updateClipping() {
        boolean z = this.mRequestedClipBounds != null && !this.mInHeadsUpPinnedMode && !this.mHeadsUpAnimatingAway;
        if (this.mIsClipped != z) {
            this.mIsClipped = z;
        }
        if (this.mAmbientState.isHiddenAtAll()) {
            invalidateOutline();
            if (isFullyHidden()) {
                setClipBounds((Rect) null);
            }
        } else if (z) {
            setClipBounds(this.mRequestedClipBounds);
        } else {
            setClipBounds((Rect) null);
        }
        setClipToOutline(false);
    }

    public final float getExpandTranslationStart() {
        return (float) (((-this.mTopPadding) + getMinExpansionHeight()) - this.mShelf.getIntrinsicHeight());
    }

    public final float getAppearStartPosition() {
        int minExpansionHeight;
        if (isHeadsUpTransition()) {
            NotificationSection firstVisibleSection = getFirstVisibleSection();
            minExpansionHeight = this.mHeadsUpInset + (firstVisibleSection != null ? firstVisibleSection.getFirstVisibleChild().getPinnedHeadsUpHeight() : 0);
        } else {
            minExpansionHeight = getMinExpansionHeight();
        }
        return (float) minExpansionHeight;
    }

    public final int getTopHeadsUpPinnedHeight() {
        NotificationEntry groupSummary;
        NotificationEntry notificationEntry = this.mTopHeadsUpEntry;
        if (notificationEntry == null) {
            return 0;
        }
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row.isChildInGroup() && (groupSummary = this.mGroupMembershipManager.getGroupSummary(row.getEntry())) != null) {
            row = groupSummary.getRow();
        }
        return row.getPinnedHeadsUpHeight();
    }

    public final float getAppearEndPosition() {
        int i;
        int visibleNotificationCount = this.mController.getVisibleNotificationCount();
        int i2 = 0;
        if (this.mEmptyShadeView.getVisibility() != 8 || visibleNotificationCount <= 0) {
            i2 = this.mEmptyShadeView.getHeight();
        } else {
            if (isHeadsUpTransition() || (this.mInHeadsUpPinnedMode && !this.mAmbientState.isDozing())) {
                if (this.mShelf.getVisibility() != 8 && visibleNotificationCount > 1) {
                    i2 = 0 + this.mShelf.getIntrinsicHeight() + this.mPaddingBetweenElements;
                }
                i = getTopHeadsUpPinnedHeight() + getPositionInLinearLayout(this.mAmbientState.getTrackedHeadsUpRow());
            } else if (this.mShelf.getVisibility() != 8) {
                i = this.mShelf.getIntrinsicHeight();
            }
            i2 += i;
        }
        return (float) (i2 + (onKeyguard() ? this.mTopPadding : this.mIntrinsicPadding));
    }

    public final boolean isHeadsUpTransition() {
        return this.mAmbientState.getTrackedHeadsUpRow() != null;
    }

    public float calculateAppearFraction(float f) {
        float appearEndPosition = getAppearEndPosition();
        float appearStartPosition = getAppearStartPosition();
        return (f - appearStartPosition) / (appearEndPosition - appearStartPosition);
    }

    public float getStackTranslation() {
        return this.mStackTranslation;
    }

    public final void setStackTranslation(float f) {
        if (f != this.mStackTranslation) {
            this.mStackTranslation = f;
            this.mAmbientState.setStackTranslation(f);
            requestChildrenUpdate();
        }
    }

    public final int getLayoutHeight() {
        return Math.min(this.mMaxLayoutHeight, this.mCurrentStackHeight);
    }

    public void setQsHeader(ViewGroup viewGroup) {
        this.mQsHeader = viewGroup;
    }

    public static boolean isPinnedHeadsUp(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        if (!expandableNotificationRow.isHeadsUp() || !expandableNotificationRow.isPinned()) {
            return false;
        }
        return true;
    }

    public final boolean isHeadsUp(View view) {
        if (view instanceof ExpandableNotificationRow) {
            return ((ExpandableNotificationRow) view).isHeadsUp();
        }
        return false;
    }

    public final ExpandableView getChildAtPosition(float f, float f2) {
        return getChildAtPosition(f, f2, true, true);
    }

    public ExpandableView getChildAtPosition(float f, float f2, boolean z, boolean z2) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() == 0 && (!z2 || !(expandableView instanceof StackScrollerDecorView))) {
                float translationY = expandableView.getTranslationY();
                float max = ((float) Math.max(0, expandableView.getClipTopAmount())) + translationY;
                float actualHeight = (((float) expandableView.getActualHeight()) + translationY) - ((float) expandableView.getClipBottomAmount());
                int width = getWidth();
                if ((actualHeight - max >= ((float) this.mMinInteractionHeight) || !z) && f2 >= max && f2 <= actualHeight && f >= ((float) 0) && f <= ((float) width)) {
                    if (!(expandableView instanceof ExpandableNotificationRow)) {
                        return expandableView;
                    }
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                    NotificationEntry entry = expandableNotificationRow.getEntry();
                    if (this.mIsExpanded || !expandableNotificationRow.isHeadsUp() || !expandableNotificationRow.isPinned() || this.mTopHeadsUpEntry.getRow() == expandableNotificationRow || this.mGroupMembershipManager.getGroupSummary(this.mTopHeadsUpEntry) == entry) {
                        return expandableNotificationRow.getViewAtPosition(f2 - translationY);
                    }
                }
            }
        }
        return null;
    }

    public ExpandableView getChildAtRawPosition(float f, float f2) {
        getLocationOnScreen(this.mTempInt2);
        int[] iArr = this.mTempInt2;
        return getChildAtPosition(f - ((float) iArr[0]), f2 - ((float) iArr[1]));
    }

    public void setScrollingEnabled(boolean z) {
        this.mScrollingEnabled = z;
    }

    public void lockScrollTo(View view) {
        if (this.mForcedScroll != view) {
            this.mForcedScroll = view;
            scrollTo(view);
        }
    }

    public boolean scrollTo(View view) {
        ExpandableView expandableView = (ExpandableView) view;
        int positionInLinearLayout = getPositionInLinearLayout(view);
        int targetScrollForView = targetScrollForView(expandableView, positionInLinearLayout);
        int intrinsicHeight = positionInLinearLayout + expandableView.getIntrinsicHeight();
        int i = this.mOwnScrollY;
        if (i >= targetScrollForView && intrinsicHeight >= i) {
            return false;
        }
        this.mScroller.startScroll(this.mScrollX, i, 0, targetScrollForView - i);
        this.mDontReportNextOverScroll = true;
        lambda$new$3();
        return true;
    }

    public final int targetScrollForView(ExpandableView expandableView, int i) {
        return (((i + expandableView.getIntrinsicHeight()) + getImeInset()) - getHeight()) + ((isExpanded() || !isPinnedHeadsUp(expandableView)) ? getTopPadding() : this.mHeadsUpInset);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mBottomInset = windowInsets.getSystemWindowInsetBottom();
        this.mWaterfallTopInset = 0;
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout != null) {
            this.mWaterfallTopInset = displayCutout.getWaterfallInsets().top;
        }
        if (this.mOwnScrollY > getScrollRange()) {
            removeCallbacks(this.mReclamp);
            postDelayed(this.mReclamp, 50);
        } else {
            View view = this.mForcedScroll;
            if (view != null) {
                scrollTo(view);
            }
        }
        return windowInsets;
    }

    public void setExpandingEnabled(boolean z) {
        this.mExpandHelper.setEnabled(z);
    }

    public final boolean isScrollingEnabled() {
        return this.mScrollingEnabled;
    }

    public boolean onKeyguard() {
        return this.mStatusBarState == 1;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Resources resources = getResources();
        updateSplitNotificationShade();
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        this.mSwipeHelper.setDensityScale(resources.getDisplayMetrics().density);
        this.mSwipeHelper.setPagingTouchSlop((float) ViewConfiguration.get(getContext()).getScaledPagingTouchSlop());
        reinitView();
    }

    public void dismissViewAnimated(View view, Consumer<Boolean> consumer, int i, long j) {
        if (view instanceof SectionHeaderView) {
            ((StackScrollerDecorView) view).setContentVisible(false, true, consumer);
            return;
        }
        this.mSwipeHelper.dismissChild(view, 0.0f, consumer, (long) i, true, j, true);
    }

    public final void snapViewIfNeeded(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        boolean z = this.mIsExpanded || isPinnedHeadsUp(row);
        if (row.getProvider() != null) {
            this.mSwipeHelper.snapChildIfNeeded(row, z, row.getProvider().isMenuVisible() ? row.getTranslation() : 0.0f);
        }
    }

    public final float overScrollUp(int i, int i2) {
        int max = Math.max(i, 0);
        float currentOverScrollAmount = getCurrentOverScrollAmount(true);
        float f = currentOverScrollAmount - ((float) max);
        if (currentOverScrollAmount > 0.0f) {
            setOverScrollAmount(f, true, false);
        }
        float f2 = f < 0.0f ? -f : 0.0f;
        float f3 = ((float) this.mOwnScrollY) + f2;
        float f4 = (float) i2;
        if (f3 <= f4) {
            return f2;
        }
        if (!this.mExpandedInThisMotion) {
            setOverScrolledPixels((getCurrentOverScrolledPixels(false) + f3) - f4, false, false);
        }
        setOwnScrollY(i2);
        return 0.0f;
    }

    public final float overScrollDown(int i) {
        int min = Math.min(i, 0);
        float currentOverScrollAmount = getCurrentOverScrollAmount(false);
        float f = ((float) min) + currentOverScrollAmount;
        if (currentOverScrollAmount > 0.0f) {
            setOverScrollAmount(f, false, false);
        }
        if (f >= 0.0f) {
            f = 0.0f;
        }
        float f2 = ((float) this.mOwnScrollY) + f;
        if (f2 >= 0.0f) {
            return f;
        }
        setOverScrolledPixels(getCurrentOverScrolledPixels(true) - f2, true, false);
        setOwnScrollY(0);
        return 0.0f;
    }

    public final void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    public final void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public final void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    public void setFinishScrollingCallback(Runnable runnable) {
        this.mFinishScrollingCallback = runnable;
    }

    /* renamed from: animateScroll */
    public final void lambda$new$3() {
        if (this.mScroller.computeScrollOffset()) {
            int i = this.mOwnScrollY;
            int currY = this.mScroller.getCurrY();
            if (i != currY) {
                int scrollRange = getScrollRange();
                if ((currY < 0 && i >= 0) || (currY > scrollRange && i <= scrollRange)) {
                    setMaxOverScrollFromCurrentVelocity();
                }
                if (this.mDontClampNextScroll) {
                    scrollRange = Math.max(scrollRange, i);
                }
                customOverScrollBy(currY - i, i, scrollRange, (int) this.mMaxOverScroll);
            }
            postOnAnimation(this.mReflingAndAnimateScroll);
            return;
        }
        this.mDontClampNextScroll = false;
        Runnable runnable = this.mFinishScrollingCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void setMaxOverScrollFromCurrentVelocity() {
        float currVelocity = this.mScroller.getCurrVelocity();
        if (currVelocity >= ((float) this.mMinimumVelocity)) {
            this.mMaxOverScroll = (Math.abs(currVelocity) / 1000.0f) * ((float) this.mOverflingDistance);
        }
    }

    public final void customOverScrollBy(int i, int i2, int i3, int i4) {
        int i5 = i2 + i;
        int i6 = -i4;
        int i7 = i4 + i3;
        boolean z = true;
        if (i5 > i7) {
            i5 = i7;
        } else if (i5 < i6) {
            i5 = i6;
        } else {
            z = false;
        }
        onCustomOverScrolled(i5, z);
    }

    public void setOverScrolledPixels(float f, boolean z, boolean z2) {
        setOverScrollAmount(f * getRubberBandFactor(z), z, z2, true);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2) {
        setOverScrollAmount(f, z, z2, true);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3) {
        setOverScrollAmount(f, z, z2, z3, isRubberbanded(z));
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3, boolean z4) {
        if (z3) {
            this.mStateAnimator.cancelOverScrollAnimators(z);
        }
        setOverScrollAmountInternal(f, z, z2, z4);
    }

    public final void setOverScrollAmountInternal(float f, boolean z, boolean z2, boolean z3) {
        float max = Math.max(0.0f, f);
        if (z2) {
            this.mStateAnimator.animateOverScrollToAmount(max, z, z3);
            return;
        }
        setOverScrolledPixels(max / getRubberBandFactor(z), z);
        this.mAmbientState.setOverScrollAmount(max, z);
        if (z) {
            notifyOverscrollTopListener(max, z3);
        }
        updateStackPosition();
        requestChildrenUpdate();
    }

    public final void notifyOverscrollTopListener(float f, boolean z) {
        this.mExpandHelper.onlyObserveMovements(f > 1.0f);
        if (this.mDontReportNextOverScroll) {
            this.mDontReportNextOverScroll = false;
            return;
        }
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            onOverscrollTopChangedListener.onOverscrollTopChanged(f, z);
        }
    }

    public void setOverscrollTopChangedListener(OnOverscrollTopChangedListener onOverscrollTopChangedListener) {
        this.mOverscrollTopChangedListener = onOverscrollTopChangedListener;
    }

    public float getCurrentOverScrollAmount(boolean z) {
        return this.mAmbientState.getOverScrollAmount(z);
    }

    public float getCurrentOverScrolledPixels(boolean z) {
        return z ? this.mOverScrolledTopPixels : this.mOverScrolledBottomPixels;
    }

    public final void setOverScrolledPixels(float f, boolean z) {
        if (z) {
            this.mOverScrolledTopPixels = f;
        } else {
            this.mOverScrolledBottomPixels = f;
        }
    }

    public final void onCustomOverScrolled(int i, boolean z) {
        if (!this.mScroller.isFinished()) {
            setOwnScrollY(i);
            if (z) {
                springBack();
                return;
            }
            float currentOverScrollAmount = getCurrentOverScrollAmount(true);
            int i2 = this.mOwnScrollY;
            if (i2 < 0) {
                notifyOverscrollTopListener((float) (-i2), isRubberbanded(true));
            } else {
                notifyOverscrollTopListener(currentOverScrollAmount, isRubberbanded(true));
            }
        } else {
            setOwnScrollY(i);
        }
    }

    public final void springBack() {
        boolean z;
        float f;
        int scrollRange = getScrollRange();
        int i = this.mOwnScrollY;
        boolean z2 = i <= 0;
        boolean z3 = i >= scrollRange;
        if (z2 || z3) {
            if (z2) {
                f = (float) (-i);
                setOwnScrollY(0);
                this.mDontReportNextOverScroll = true;
                z = true;
            } else {
                setOwnScrollY(scrollRange);
                f = (float) (i - scrollRange);
                z = false;
            }
            setOverScrollAmount(f, z, false);
            setOverScrollAmount(0.0f, z, true);
            this.mScroller.forceFinished(true);
        }
    }

    public final int getScrollRange() {
        int i = this.mContentHeight;
        if (!isExpanded() && this.mInHeadsUpPinnedMode) {
            i = this.mHeadsUpInset + getTopHeadsUpPinnedHeight();
        }
        int max = Math.max(0, i - this.mMaxLayoutHeight);
        int imeInset = getImeInset();
        int min = max + Math.min(imeInset, Math.max(0, i - (getHeight() - imeInset)));
        return min > 0 ? Math.max(getScrollAmountToScrollBoundary(), min) : min;
    }

    public final int getImeInset() {
        return Math.max(0, this.mBottomInset - (getRootView().getHeight() - getHeight()));
    }

    public ExpandableView getFirstChildNotGone() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8 && childAt != this.mShelf) {
                return (ExpandableView) childAt;
            }
        }
        return null;
    }

    public final View getFirstChildBelowTranlsationY(float f, boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                float translationY = childAt.getTranslationY();
                if (translationY >= f) {
                    return childAt;
                }
                if (!z && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    if (expandableNotificationRow.isSummaryWithChildren() && expandableNotificationRow.areChildrenExpanded()) {
                        List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                        for (int i2 = 0; i2 < attachedChildren.size(); i2++) {
                            ExpandableNotificationRow expandableNotificationRow2 = attachedChildren.get(i2);
                            if (expandableNotificationRow2.getTranslationY() + translationY >= f) {
                                return expandableNotificationRow2;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public ExpandableView getLastChildNotGone() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt.getVisibility() != 8 && childAt != this.mShelf) {
                return (ExpandableView) childAt;
            }
        }
        return null;
    }

    public int getNotGoneChildCount() {
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i2);
            if (!(expandableView.getVisibility() == 8 || expandableView.willBeGone() || expandableView == this.mShelf)) {
                i++;
            }
        }
        return i;
    }

    public final void updateContentHeight() {
        float f = this.mAmbientState.isOnKeyguard() ? 0.0f : (float) this.mMinimumPaddings;
        NotificationShelf notificationShelf = this.mShelf;
        float computeHeight = (float) (((int) f) + ((int) this.mNotificationStackSizeCalculator.computeHeight(this, this.mMaxDisplayedNotifications, (float) (notificationShelf != null ? notificationShelf.getIntrinsicHeight() : 0))));
        this.mIntrinsicContentHeight = computeHeight;
        this.mContentHeight = (int) (computeHeight + ((float) Math.max(this.mIntrinsicPadding, this.mTopPadding)) + ((float) this.mBottomPadding));
        updateScrollability();
        clampScrollPosition();
        updateStackPosition();
        this.mAmbientState.setContentHeight(this.mContentHeight);
    }

    public float calculateGapHeight(ExpandableView expandableView, ExpandableView expandableView2, int i) {
        return this.mStackScrollAlgorithm.getGapHeightForChild(this.mSectionsManager, i, expandableView2, expandableView, this.mAmbientState.getFractionToShade(), this.mAmbientState.isOnKeyguard());
    }

    public boolean hasPulsingNotifications() {
        return this.mPulsing;
    }

    public final void updateScrollability() {
        boolean z = !this.mQsFullScreen && getScrollRange() > 0;
        if (z != this.mScrollable) {
            this.mScrollable = z;
            setFocusable(z);
            updateForwardAndBackwardScrollability();
        }
    }

    public final void updateForwardAndBackwardScrollability() {
        boolean z = true;
        boolean z2 = this.mScrollable && !this.mScrollAdapter.isScrolledToBottom();
        boolean z3 = this.mScrollable && !this.mScrollAdapter.isScrolledToTop();
        if (z2 == this.mForwardScrollable && z3 == this.mBackwardScrollable) {
            z = false;
        }
        this.mForwardScrollable = z2;
        this.mBackwardScrollable = z3;
        if (z) {
            sendAccessibilityEvent(2048);
        }
    }

    public final void updateBackground() {
        if (this.mShouldDrawNotificationBackground) {
            updateBackgroundBounds();
            if (didSectionBoundsChange()) {
                boolean z = this.mAnimateNextSectionBoundsChange || this.mAnimateNextBackgroundTop || this.mAnimateNextBackgroundBottom || areSectionBoundsAnimating();
                if (!isExpanded()) {
                    abortBackgroundAnimators();
                    z = false;
                }
                if (z) {
                    startBackgroundAnimation();
                } else {
                    for (NotificationSection resetCurrentBounds : this.mSections) {
                        resetCurrentBounds.resetCurrentBounds();
                    }
                    invalidate();
                }
            } else {
                abortBackgroundAnimators();
            }
            this.mAnimateNextBackgroundTop = false;
            this.mAnimateNextBackgroundBottom = false;
            this.mAnimateNextSectionBoundsChange = false;
        }
    }

    public final void abortBackgroundAnimators() {
        for (NotificationSection cancelAnimators : this.mSections) {
            cancelAnimators.cancelAnimators();
        }
    }

    public final boolean didSectionBoundsChange() {
        for (NotificationSection didBoundsChange : this.mSections) {
            if (didBoundsChange.didBoundsChange()) {
                return true;
            }
        }
        return false;
    }

    public final boolean areSectionBoundsAnimating() {
        for (NotificationSection areBoundsAnimating : this.mSections) {
            if (areBoundsAnimating.areBoundsAnimating()) {
                return true;
            }
        }
        return false;
    }

    public final void startBackgroundAnimation() {
        boolean z;
        boolean z2;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        NotificationSection lastVisibleSection = getLastVisibleSection();
        for (NotificationSection notificationSection : this.mSections) {
            if (notificationSection == firstVisibleSection) {
                z = this.mAnimateNextBackgroundTop;
            } else {
                z = this.mAnimateNextSectionBoundsChange;
            }
            if (notificationSection == lastVisibleSection) {
                z2 = this.mAnimateNextBackgroundBottom;
            } else {
                z2 = this.mAnimateNextSectionBoundsChange;
            }
            notificationSection.startBackgroundAnimation(z, z2);
        }
    }

    public final void updateBackgroundBounds() {
        int i;
        int i2 = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        for (NotificationSection notificationSection : this.mSections) {
            notificationSection.getBounds().left = i2;
            notificationSection.getBounds().right = width;
        }
        if (!this.mIsExpanded) {
            for (NotificationSection notificationSection2 : this.mSections) {
                notificationSection2.getBounds().top = 0;
                notificationSection2.getBounds().bottom = 0;
            }
            return;
        }
        NotificationSection lastVisibleSection = getLastVisibleSection();
        boolean z = true;
        boolean z2 = this.mStatusBarState == 1;
        if (!z2) {
            i = (int) (((float) this.mTopPadding) + this.mStackTranslation);
        } else if (lastVisibleSection == null) {
            i = this.mTopPadding;
        } else {
            NotificationSection firstVisibleSection = getFirstVisibleSection();
            firstVisibleSection.updateBounds(0, 0, false);
            i = firstVisibleSection.getBounds().top;
        }
        if (this.mNumHeadsUp > 1 || (!this.mAmbientState.isDozing() && (!this.mKeyguardBypassEnabled || !z2))) {
            z = false;
        }
        NotificationSection[] notificationSectionArr = this.mSections;
        int length = notificationSectionArr.length;
        int i3 = 0;
        while (i3 < length) {
            NotificationSection notificationSection3 = notificationSectionArr[i3];
            i = notificationSection3.updateBounds(i, notificationSection3 == lastVisibleSection ? (int) (ViewState.getFinalTranslationY(this.mShelf) + ((float) this.mShelf.getIntrinsicHeight())) : i, z);
            i3++;
            z = false;
        }
    }

    public final NotificationSection getFirstVisibleSection() {
        for (NotificationSection notificationSection : this.mSections) {
            if (notificationSection.getFirstVisibleChild() != null) {
                return notificationSection;
            }
        }
        return null;
    }

    public final NotificationSection getLastVisibleSection() {
        for (int length = this.mSections.length - 1; length >= 0; length--) {
            NotificationSection notificationSection = this.mSections[length];
            if (notificationSection.getLastVisibleChild() != null) {
                return notificationSection;
            }
        }
        return null;
    }

    public final ExpandableView getLastChildWithBackground() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ExpandableView expandableView = (ExpandableView) getChildAt(childCount);
            if (expandableView.getVisibility() != 8 && !(expandableView instanceof StackScrollerDecorView) && expandableView != this.mShelf) {
                return expandableView;
            }
        }
        return null;
    }

    public final ExpandableView getFirstChildWithBackground() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8 && !(expandableView instanceof StackScrollerDecorView) && expandableView != this.mShelf) {
                return expandableView;
            }
        }
        return null;
    }

    public final List<ExpandableView> getChildrenWithBackground() {
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                arrayList.add(expandableView);
            }
        }
        return arrayList;
    }

    public void fling(int i) {
        if (getChildCount() > 0) {
            float currentOverScrollAmount = getCurrentOverScrollAmount(true);
            int i2 = 0;
            float currentOverScrollAmount2 = getCurrentOverScrollAmount(false);
            if (i < 0 && currentOverScrollAmount > 0.0f) {
                setOwnScrollY(this.mOwnScrollY - ((int) currentOverScrollAmount));
                this.mDontReportNextOverScroll = true;
                setOverScrollAmount(0.0f, true, false);
                this.mMaxOverScroll = ((((float) Math.abs(i)) / 1000.0f) * getRubberBandFactor(true) * ((float) this.mOverflingDistance)) + currentOverScrollAmount;
            } else if (i <= 0 || currentOverScrollAmount2 <= 0.0f) {
                this.mMaxOverScroll = 0.0f;
            } else {
                setOwnScrollY((int) (((float) this.mOwnScrollY) + currentOverScrollAmount2));
                setOverScrollAmount(0.0f, false, false);
                this.mMaxOverScroll = ((((float) Math.abs(i)) / 1000.0f) * getRubberBandFactor(false) * ((float) this.mOverflingDistance)) + currentOverScrollAmount2;
            }
            int max = Math.max(0, getScrollRange());
            if (this.mExpandedInThisMotion) {
                max = Math.min(max, this.mMaxScrollAfterExpand);
            }
            int i3 = max;
            OverScroller overScroller = this.mScroller;
            int i4 = this.mScrollX;
            int i5 = this.mOwnScrollY;
            if (!this.mExpandedInThisMotion || i5 < 0) {
                i2 = 1073741823;
            }
            overScroller.fling(i4, i5, 1, i, 0, 0, 0, i3, 0, i2);
            lambda$new$3();
        }
    }

    public final boolean shouldOverScrollFling(int i) {
        float currentOverScrollAmount = getCurrentOverScrollAmount(true);
        if (this.mScrolledToTopOnFirstDown && !this.mExpandedInThisMotion) {
            if (i > this.mMinimumVelocity) {
                return true;
            }
            if (currentOverScrollAmount <= this.mMinTopOverScrollToEscape || i <= 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void updateTopPadding(float f, boolean z) {
        int i = (int) f;
        int layoutMinHeight = getLayoutMinHeight() + i;
        if (layoutMinHeight > getHeight()) {
            this.mTopPaddingOverflow = (float) (layoutMinHeight - getHeight());
        } else {
            this.mTopPaddingOverflow = 0.0f;
        }
        setTopPadding(i, z && !this.mKeyguardBypassEnabled);
        setExpandedHeight(this.mExpandedHeight);
    }

    public void setMaxTopPadding(int i) {
        this.mMaxTopPadding = i;
    }

    public int getLayoutMinHeight() {
        if (isHeadsUpTransition()) {
            ExpandableNotificationRow trackedHeadsUpRow = this.mAmbientState.getTrackedHeadsUpRow();
            if (!trackedHeadsUpRow.isAboveShelf()) {
                return getTopHeadsUpPinnedHeight();
            }
            return getTopHeadsUpPinnedHeight() + ((int) MathUtils.lerp(0, getPositionInLinearLayout(trackedHeadsUpRow), this.mAmbientState.getAppearFraction()));
        } else if (this.mShelf.getVisibility() == 8) {
            return 0;
        } else {
            return this.mShelf.getIntrinsicHeight();
        }
    }

    public float getTopPaddingOverflow() {
        return this.mTopPaddingOverflow;
    }

    public final float getRubberBandFactor(boolean z) {
        if (!z) {
            return 0.35f;
        }
        if (this.mExpandedInThisMotion) {
            return 0.15f;
        }
        if (this.mIsExpansionChanging || this.mPanelTracking) {
            return 0.21f;
        }
        if (this.mScrolledToTopOnFirstDown) {
            return 1.0f;
        }
        return 0.35f;
    }

    public final boolean isRubberbanded(boolean z) {
        return !z || this.mExpandedInThisMotion || this.mIsExpansionChanging || this.mPanelTracking || !this.mScrolledToTopOnFirstDown;
    }

    public void setChildTransferInProgress(boolean z) {
        Assert.isMainThread();
        this.mChildTransferInProgress = z;
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (!this.mChildTransferInProgress) {
            onViewRemovedInternal((ExpandableView) view, this);
        }
    }

    public void cleanUpViewStateForEntry(NotificationEntry notificationEntry) {
        if (notificationEntry.getRow() == this.mSwipeHelper.getTranslatingParentView()) {
            this.mSwipeHelper.clearTranslatingParentView();
        }
    }

    public final void onViewRemovedInternal(ExpandableView expandableView, ViewGroup viewGroup) {
        if (!this.mChangePositionInProgress) {
            expandableView.setOnHeightChangedListener((ExpandableView.OnHeightChangedListener) null);
            updateScrollStateForRemovedChild(expandableView);
            if (!generateRemoveAnimation(expandableView)) {
                this.mSwipedOutViews.remove(expandableView);
            } else if (!this.mSwipedOutViews.contains(expandableView) || !isFullySwipedOut(expandableView)) {
                viewGroup.addTransientView(expandableView, 0);
                expandableView.setTransientContainer(viewGroup);
            }
            updateAnimationState(false, expandableView);
            focusNextViewIfFocused(expandableView);
        }
    }

    public boolean isFullySwipedOut(ExpandableView expandableView) {
        return Math.abs(expandableView.getTranslation()) >= Math.abs(getTotalTranslationLength(expandableView));
    }

    public final void focusNextViewIfFocused(View view) {
        float f;
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.shouldRefocusOnDismiss()) {
                View childAfterViewWhenDismissed = expandableNotificationRow.getChildAfterViewWhenDismissed();
                if (childAfterViewWhenDismissed == null) {
                    View groupParentWhenDismissed = expandableNotificationRow.getGroupParentWhenDismissed();
                    if (groupParentWhenDismissed != null) {
                        f = groupParentWhenDismissed.getTranslationY();
                    } else {
                        f = view.getTranslationY();
                    }
                    childAfterViewWhenDismissed = getFirstChildBelowTranlsationY(f, true);
                }
                if (childAfterViewWhenDismissed != null) {
                    childAfterViewWhenDismissed.requestAccessibilityFocus();
                }
            }
        }
    }

    public final boolean isChildInGroup(View view) {
        return (view instanceof ExpandableNotificationRow) && this.mGroupMembershipManager.isChildInGroup(((ExpandableNotificationRow) view).getEntry());
    }

    public boolean generateRemoveAnimation(ExpandableView expandableView) {
        String str = "";
        if (this.mDebugRemoveAnimation) {
            if (expandableView instanceof ExpandableNotificationRow) {
                str = ((ExpandableNotificationRow) expandableView).getEntry().getKey();
            }
            Log.d("StackScroller", "generateRemoveAnimation " + str);
        }
        if (removeRemovedChildFromHeadsUpChangeAnimations(expandableView)) {
            if (this.mDebugRemoveAnimation) {
                Log.d("StackScroller", "removedBecauseOfHeadsUp " + str);
            }
            this.mAddedHeadsUpChildren.remove(expandableView);
            return false;
        } else if (isClickedHeadsUp(expandableView)) {
            this.mClearTransientViewsWhenFinished.add(expandableView);
            return true;
        } else {
            if (this.mDebugRemoveAnimation) {
                StringBuilder sb = new StringBuilder();
                sb.append("generateRemove ");
                sb.append(str);
                sb.append("\nmIsExpanded ");
                sb.append(this.mIsExpanded);
                sb.append("\nmAnimationsEnabled ");
                sb.append(this.mAnimationsEnabled);
                sb.append("\n!invisible group ");
                sb.append(!isChildInInvisibleGroup(expandableView));
                Log.d("StackScroller", sb.toString());
            }
            if (this.mIsExpanded && this.mAnimationsEnabled && !isChildInInvisibleGroup(expandableView)) {
                if (!this.mChildrenToAddAnimated.contains(expandableView)) {
                    if (this.mDebugRemoveAnimation) {
                        Log.d("StackScroller", "needsAnimation = true " + str);
                    }
                    this.mChildrenToRemoveAnimated.add(expandableView);
                    this.mNeedsAnimation = true;
                    return true;
                }
                this.mChildrenToAddAnimated.remove(expandableView);
                this.mFromMoreCardAdditions.remove(expandableView);
            }
            return false;
        }
    }

    public final boolean isClickedHeadsUp(View view) {
        return HeadsUpUtil.isClickedHeadsUpNotification(view);
    }

    public final boolean removeRemovedChildFromHeadsUpChangeAnimations(View view) {
        Iterator<Pair<ExpandableNotificationRow, Boolean>> it = this.mHeadsUpChangeAnimations.iterator();
        boolean z = false;
        while (it.hasNext()) {
            Pair next = it.next();
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next.first;
            boolean booleanValue = ((Boolean) next.second).booleanValue();
            if (view == expandableNotificationRow) {
                this.mTmpList.add(next);
                z |= booleanValue;
            }
        }
        if (z) {
            this.mHeadsUpChangeAnimations.removeAll(this.mTmpList);
            ((ExpandableNotificationRow) view).setHeadsUpAnimatingAway(false);
        }
        this.mTmpList.clear();
        if (!z || !this.mAddedHeadsUpChildren.contains(view)) {
            return false;
        }
        return true;
    }

    public final boolean isChildInInvisibleGroup(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        NotificationEntry groupSummary = this.mGroupMembershipManager.getGroupSummary(expandableNotificationRow.getEntry());
        if (groupSummary == null || groupSummary.getRow() == expandableNotificationRow || expandableNotificationRow.getVisibility() != 4) {
            return false;
        }
        return true;
    }

    public final void updateScrollStateForRemovedChild(ExpandableView expandableView) {
        int positionInLinearLayout = getPositionInLinearLayout(expandableView);
        int intrinsicHeight = getIntrinsicHeight(expandableView) + this.mPaddingBetweenElements;
        int i = positionInLinearLayout + intrinsicHeight;
        int scrollAmountToScrollBoundary = getScrollAmountToScrollBoundary();
        this.mAnimateStackYForContentHeightChange = true;
        int i2 = this.mOwnScrollY;
        if (i <= i2 - scrollAmountToScrollBoundary) {
            setOwnScrollY(i2 - intrinsicHeight);
        } else if (positionInLinearLayout < i2 - scrollAmountToScrollBoundary) {
            setOwnScrollY(positionInLinearLayout + scrollAmountToScrollBoundary);
        }
    }

    public final int getScrollAmountToScrollBoundary() {
        if (this.mShouldUseSplitNotificationShade) {
            return this.mSidePaddings;
        }
        return this.mTopPadding - this.mQsScrollBoundaryPosition;
    }

    public final int getIntrinsicHeight(View view) {
        if (view instanceof ExpandableView) {
            return ((ExpandableView) view).getIntrinsicHeight();
        }
        return view.getHeight();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: android.view.View} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v2, resolved type: android.view.View} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPositionInLinearLayout(android.view.View r9) {
        /*
            r8 = this;
            boolean r0 = r8.isChildInGroup(r9)
            r1 = 0
            if (r0 == 0) goto L_0x0010
            r1 = r9
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r9 = r1.getNotificationParent()
            r0 = r9
            goto L_0x0011
        L_0x0010:
            r0 = r1
        L_0x0011:
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x0014:
            int r5 = r8.getChildCount()
            if (r3 >= r5) goto L_0x004c
            android.view.View r5 = r8.getChildAt(r3)
            com.android.systemui.statusbar.notification.row.ExpandableView r5 = (com.android.systemui.statusbar.notification.row.ExpandableView) r5
            int r6 = r5.getVisibility()
            r7 = 8
            if (r6 == r7) goto L_0x002a
            r6 = 1
            goto L_0x002b
        L_0x002a:
            r6 = r2
        L_0x002b:
            if (r6 == 0) goto L_0x0038
            boolean r7 = r5.hasNoContentHeight()
            if (r7 != 0) goto L_0x0038
            if (r4 == 0) goto L_0x0038
            int r7 = r8.mPaddingBetweenElements
            int r4 = r4 + r7
        L_0x0038:
            if (r5 != r9) goto L_0x0042
            if (r0 == 0) goto L_0x0041
            int r8 = r0.getPositionOfChild(r1)
            int r4 = r4 + r8
        L_0x0041:
            return r4
        L_0x0042:
            if (r6 == 0) goto L_0x0049
            int r5 = r8.getIntrinsicHeight(r5)
            int r4 = r4 + r5
        L_0x0049:
            int r3 = r3 + 1
            goto L_0x0014
        L_0x004c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.getPositionInLinearLayout(android.view.View):int");
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof ExpandableView) {
            onViewAddedInternal((ExpandableView) view);
        }
    }

    public final void updateFirstAndLastBackgroundViews() {
        ExpandableView expandableView;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        NotificationSection lastVisibleSection = getLastVisibleSection();
        ExpandableView expandableView2 = null;
        if (firstVisibleSection == null) {
            expandableView = null;
        } else {
            expandableView = firstVisibleSection.getFirstVisibleChild();
        }
        if (lastVisibleSection != null) {
            expandableView2 = lastVisibleSection.getLastVisibleChild();
        }
        ExpandableView firstChildWithBackground = getFirstChildWithBackground();
        ExpandableView lastChildWithBackground = getLastChildWithBackground();
        boolean updateFirstAndLastViewsForAllSections = this.mSectionsManager.updateFirstAndLastViewsForAllSections(this.mSections, getChildrenWithBackground());
        if (!this.mAnimationsEnabled || !this.mIsExpanded) {
            this.mAnimateNextBackgroundTop = false;
            this.mAnimateNextBackgroundBottom = false;
            this.mAnimateNextSectionBoundsChange = false;
        } else {
            boolean z = true;
            this.mAnimateNextBackgroundTop = firstChildWithBackground != expandableView;
            if (lastChildWithBackground == expandableView2 && !this.mAnimateBottomOnLayout) {
                z = false;
            }
            this.mAnimateNextBackgroundBottom = z;
            this.mAnimateNextSectionBoundsChange = updateFirstAndLastViewsForAllSections;
        }
        this.mAmbientState.setLastVisibleBackgroundChild(lastChildWithBackground);
        this.mController.getNoticationRoundessManager().updateRoundedChildren(this.mSections);
        this.mAnimateBottomOnLayout = false;
        invalidate();
    }

    public final void onViewAddedInternal(ExpandableView expandableView) {
        updateHideSensitiveForChild(expandableView);
        expandableView.setOnHeightChangedListener(this.mOnChildHeightChangedListener);
        generateAddAnimation(expandableView, false);
        updateAnimationState(expandableView);
        updateChronometerForChild(expandableView);
        if (expandableView instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) expandableView).setDismissUsingRowTranslationX(this.mDismissUsingRowTranslationX);
        }
    }

    public final void updateHideSensitiveForChild(ExpandableView expandableView) {
        expandableView.setHideSensitiveForIntrinsicHeight(this.mAmbientState.isHideSensitive());
    }

    public void notifyGroupChildRemoved(ExpandableView expandableView, ViewGroup viewGroup) {
        onViewRemovedInternal(expandableView, viewGroup);
    }

    public void notifyGroupChildAdded(ExpandableView expandableView) {
        onViewAddedInternal(expandableView);
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
        updateNotificationAnimationStates();
        if (!z) {
            this.mSwipedOutViews.clear();
            this.mChildrenToRemoveAnimated.clear();
            clearTemporaryViewsInGroup(this);
        }
    }

    public final void updateNotificationAnimationStates() {
        boolean z = this.mAnimationsEnabled || hasPulsingNotifications();
        this.mShelf.setAnimationsEnabled(z);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            z &= this.mIsExpanded || isPinnedHeadsUp(childAt);
            updateAnimationState(z, childAt);
        }
    }

    public void updateAnimationState(View view) {
        updateAnimationState((this.mAnimationsEnabled || hasPulsingNotifications()) && (this.mIsExpanded || isPinnedHeadsUp(view)), view);
    }

    public void setExpandingNotification(ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2 = this.mExpandingNotificationRow;
        if (expandableNotificationRow2 != null && expandableNotificationRow == null) {
            expandableNotificationRow2.setExpandingClipPath((Path) null);
            ExpandableNotificationRow notificationParent = this.mExpandingNotificationRow.getNotificationParent();
            if (notificationParent != null) {
                notificationParent.setExpandingClipPath((Path) null);
            }
        }
        this.mExpandingNotificationRow = expandableNotificationRow;
        updateLaunchedNotificationClipPath();
        requestChildrenUpdate();
    }

    public boolean containsView(View view) {
        return view.getParent() == this;
    }

    public void applyLaunchAnimationParams(LaunchAnimationParameters launchAnimationParameters) {
        this.mLaunchAnimationParams = launchAnimationParameters;
        setLaunchingNotification(launchAnimationParameters != null);
        updateLaunchedNotificationClipPath();
        requestChildrenUpdate();
    }

    public final void updateAnimationState(boolean z, View view) {
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).setIconAnimationRunning(z);
        }
    }

    public boolean isAddOrRemoveAnimationPending() {
        return this.mNeedsAnimation && (!this.mChildrenToAddAnimated.isEmpty() || !this.mChildrenToRemoveAnimated.isEmpty());
    }

    public void generateAddAnimation(ExpandableView expandableView, boolean z) {
        if (this.mIsExpanded && this.mAnimationsEnabled && !this.mChangePositionInProgress && !isFullyHidden()) {
            this.mChildrenToAddAnimated.add(expandableView);
            if (z) {
                this.mFromMoreCardAdditions.add(expandableView);
            }
            this.mNeedsAnimation = true;
        }
        if (isHeadsUp(expandableView) && this.mAnimationsEnabled && !this.mChangePositionInProgress && !isFullyHidden()) {
            this.mAddedHeadsUpChildren.add(expandableView);
            this.mChildrenToAddAnimated.remove(expandableView);
        }
    }

    public void changeViewPosition(ExpandableView expandableView, int i) {
        Assert.isMainThread();
        if (!this.mChangePositionInProgress) {
            int indexOfChild = indexOfChild(expandableView);
            boolean z = false;
            if (indexOfChild == -1) {
                if ((expandableView instanceof ExpandableNotificationRow) && expandableView.getTransientContainer() != null) {
                    z = true;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Attempting to re-position ");
                sb.append(z ? "transient" : "");
                sb.append(" view {");
                sb.append(expandableView);
                sb.append("}");
                Log.e("StackScroller", sb.toString());
            } else if (expandableView != null && expandableView.getParent() == this && indexOfChild != i) {
                this.mChangePositionInProgress = true;
                expandableView.setChangingPosition(true);
                removeView(expandableView);
                addView(expandableView, i);
                expandableView.setChangingPosition(false);
                this.mChangePositionInProgress = false;
                if (this.mIsExpanded && this.mAnimationsEnabled && expandableView.getVisibility() != 8) {
                    this.mChildrenChangingPositions.add(expandableView);
                    this.mNeedsAnimation = true;
                }
            }
        } else {
            throw new IllegalStateException("Reentrant call to changeViewPosition");
        }
    }

    public final void startAnimationToState() {
        if (this.mNeedsAnimation) {
            generateAllAnimationEvents();
            this.mNeedsAnimation = false;
        }
        if (!this.mAnimationEvents.isEmpty() || isCurrentlyAnimating()) {
            setAnimationRunning(true);
            this.mStateAnimator.startAnimationForEvents(this.mAnimationEvents, this.mGoToFullShadeDelay);
            this.mAnimationEvents.clear();
            updateBackground();
            updateViewShadows();
        } else {
            applyCurrentState();
        }
        this.mGoToFullShadeDelay = 0;
    }

    public final void generateAllAnimationEvents() {
        generateHeadsUpAnimationEvents();
        generateChildRemovalEvents();
        generateChildAdditionEvents();
        generatePositionChangeEvents();
        generateTopPaddingEvent();
        generateActivateEvent();
        generateDimmedEvent();
        generateHideSensitiveEvent();
        generateGoToFullShadeEvent();
        generateViewResizeEvent();
        generateGroupExpansionEvent();
        generateAnimateEverythingEvent();
    }

    public final void generateHeadsUpAnimationEvents() {
        Iterator<Pair<ExpandableNotificationRow, Boolean>> it = this.mHeadsUpChangeAnimations.iterator();
        while (it.hasNext()) {
            Pair next = it.next();
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next.first;
            String key = expandableNotificationRow.getEntry().getKey();
            boolean booleanValue = ((Boolean) next.second).booleanValue();
            if (booleanValue != expandableNotificationRow.isHeadsUp()) {
                logHunSkippedForUnexpectedState(key, booleanValue, expandableNotificationRow.isHeadsUp());
            } else {
                int i = 14;
                boolean z = true;
                boolean z2 = false;
                boolean z3 = expandableNotificationRow.isPinned() && !this.mIsExpanded;
                if (this.mIsExpanded && (!this.mKeyguardBypassEnabled || !onKeyguard() || !this.mInHeadsUpPinnedMode)) {
                    z = false;
                }
                if (!z || booleanValue) {
                    ExpandableViewState viewState = expandableNotificationRow.getViewState();
                    if (viewState == null) {
                        logHunAnimationSkipped(key, "row has no viewState");
                    } else if (booleanValue && (this.mAddedHeadsUpChildren.contains(expandableNotificationRow) || z3)) {
                        i = (z3 || shouldHunAppearFromBottom(viewState)) ? 11 : 0;
                        z2 = !z3;
                    }
                } else {
                    i = expandableNotificationRow.wasJustClicked() ? 13 : 12;
                    if (expandableNotificationRow.isChildInGroup()) {
                        expandableNotificationRow.setHeadsUpAnimatingAway(false);
                        logHunAnimationSkipped(key, "row is child in group");
                    }
                }
                AnimationEvent animationEvent = new AnimationEvent(expandableNotificationRow, i);
                animationEvent.headsUpFromBottom = z2;
                this.mAnimationEvents.add(animationEvent);
                if (SPEW) {
                    Log.v("StackScroller", "Generating HUN animation event:  isHeadsUp=" + booleanValue + " type=" + i + " onBottom=" + z2 + " row=" + expandableNotificationRow.getEntry().getKey());
                }
                logHunAnimationEventAdded(key, i);
            }
        }
        this.mHeadsUpChangeAnimations.clear();
        this.mAddedHeadsUpChildren.clear();
    }

    public final boolean shouldHunAppearFromBottom(ExpandableViewState expandableViewState) {
        return expandableViewState.yTranslation + ((float) expandableViewState.height) >= this.mAmbientState.getMaxHeadsUpTranslation();
    }

    public final void generateGroupExpansionEvent() {
        if (this.mExpandedGroupView != null) {
            this.mAnimationEvents.add(new AnimationEvent(this.mExpandedGroupView, 10));
            this.mExpandedGroupView = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0023 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void generateViewResizeEvent() {
        /*
            r5 = this;
            boolean r0 = r5.mNeedViewResizeAnimation
            r1 = 0
            if (r0 == 0) goto L_0x0033
            java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent> r0 = r5.mAnimationEvents
            java.util.Iterator r0 = r0.iterator()
        L_0x000b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0023
            java.lang.Object r2 = r0.next()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent r2 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.AnimationEvent) r2
            int r2 = r2.animationType
            r3 = 13
            if (r2 == r3) goto L_0x0021
            r3 = 12
            if (r2 != r3) goto L_0x000b
        L_0x0021:
            r0 = 1
            goto L_0x0024
        L_0x0023:
            r0 = r1
        L_0x0024:
            if (r0 != 0) goto L_0x0033
            java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent> r0 = r5.mAnimationEvents
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent r2 = new com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent
            r3 = 0
            r4 = 9
            r2.<init>(r3, r4)
            r0.add(r2)
        L_0x0033:
            r5.mNeedViewResizeAnimation = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.generateViewResizeEvent():void");
    }

    public final void generateChildRemovalEvents() {
        boolean z;
        Iterator<ExpandableView> it = this.mChildrenToRemoveAnimated.iterator();
        while (it.hasNext()) {
            ExpandableView next = it.next();
            boolean contains = this.mSwipedOutViews.contains(next);
            float translationY = next.getTranslationY();
            boolean z2 = next instanceof ExpandableNotificationRow;
            boolean z3 = false;
            int i = 1;
            if (z2) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next;
                if (!expandableNotificationRow.isRemoved() || !expandableNotificationRow.wasChildInGroupWhenRemoved()) {
                    z = true;
                } else {
                    translationY = expandableNotificationRow.getTranslationWhenRemoved();
                    z = false;
                }
                contains |= isFullySwipedOut(expandableNotificationRow);
            } else if (next instanceof MediaContainerView) {
                contains = true;
                z = true;
            } else {
                z = true;
            }
            if (!contains) {
                Rect clipBounds = next.getClipBounds();
                if (clipBounds != null && clipBounds.height() == 0) {
                    z3 = true;
                }
                if (z3) {
                    next.removeFromTransientContainer();
                }
                contains = z3;
            }
            if (contains) {
                i = 2;
            }
            AnimationEvent animationEvent = new AnimationEvent(next, i);
            animationEvent.viewAfterChangingView = getFirstChildBelowTranlsationY(translationY, z);
            this.mAnimationEvents.add(animationEvent);
            this.mSwipedOutViews.remove(next);
            if (this.mDebugRemoveAnimation) {
                Log.d("StackScroller", "created Remove Event - SwipedOut: " + contains + " " + (z2 ? ((ExpandableNotificationRow) next).getEntry().getKey() : ""));
            }
        }
        this.mChildrenToRemoveAnimated.clear();
    }

    public final void generatePositionChangeEvents() {
        AnimationEvent animationEvent;
        Iterator<ExpandableView> it = this.mChildrenChangingPositions.iterator();
        while (true) {
            Integer num = null;
            if (!it.hasNext()) {
                break;
            }
            ExpandableView next = it.next();
            if (next instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next;
                if (expandableNotificationRow.getEntry().isMarkedForUserTriggeredMovement()) {
                    num = 500;
                    expandableNotificationRow.getEntry().markForUserTriggeredMovement(false);
                }
            }
            if (num == null) {
                animationEvent = new AnimationEvent(next, 6);
            } else {
                animationEvent = new AnimationEvent(next, 6, (long) num.intValue());
            }
            this.mAnimationEvents.add(animationEvent);
        }
        this.mChildrenChangingPositions.clear();
        if (this.mGenerateChildOrderChangedEvent) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 6));
            this.mGenerateChildOrderChangedEvent = false;
        }
    }

    public final void generateChildAdditionEvents() {
        Iterator<ExpandableView> it = this.mChildrenToAddAnimated.iterator();
        while (it.hasNext()) {
            ExpandableView next = it.next();
            if (this.mFromMoreCardAdditions.contains(next)) {
                this.mAnimationEvents.add(new AnimationEvent(next, 0, 360));
            } else {
                this.mAnimationEvents.add(new AnimationEvent(next, 0));
            }
        }
        this.mChildrenToAddAnimated.clear();
        this.mFromMoreCardAdditions.clear();
    }

    public final void generateTopPaddingEvent() {
        AnimationEvent animationEvent;
        if (this.mTopPaddingNeedsAnimation) {
            if (this.mAmbientState.isDozing()) {
                animationEvent = new AnimationEvent((ExpandableView) null, 3, 550);
            } else {
                animationEvent = new AnimationEvent((ExpandableView) null, 3);
            }
            this.mAnimationEvents.add(animationEvent);
        }
        this.mTopPaddingNeedsAnimation = false;
    }

    public final void generateActivateEvent() {
        if (this.mActivateNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 4));
        }
        this.mActivateNeedsAnimation = false;
    }

    public final void generateAnimateEverythingEvent() {
        if (this.mEverythingNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 15));
        }
        this.mEverythingNeedsAnimation = false;
    }

    public final void generateDimmedEvent() {
        if (this.mDimmedNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 5));
        }
        this.mDimmedNeedsAnimation = false;
    }

    public final void generateHideSensitiveEvent() {
        if (this.mHideSensitiveNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 8));
        }
        this.mHideSensitiveNeedsAnimation = false;
    }

    public final void generateGoToFullShadeEvent() {
        if (this.mGoToFullShadeNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent((ExpandableView) null, 7));
        }
        this.mGoToFullShadeNeedsAnimation = false;
    }

    public StackScrollAlgorithm createStackScrollAlgorithm(Context context) {
        return new StackScrollAlgorithm(context, this);
    }

    public boolean isInContentBounds(float f) {
        return f < ((float) (getHeight() - getEmptyBottomMargin()));
    }

    public final float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public void dispatchDownEventToScroller(MotionEvent motionEvent) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(0);
        onScrollTouch(obtain);
        obtain.recycle();
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        int i = 0;
        if (!isScrollingEnabled() || !this.mIsExpanded || this.mSwipeHelper.isSwiping() || this.mExpandingNotification || this.mDisallowScrollingInThisMotion) {
            return false;
        }
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() == 8 && !this.mIsBeingDragged) {
            float axisValue = motionEvent.getAxisValue(9);
            if (axisValue != 0.0f) {
                int scrollRange = getScrollRange();
                int i2 = this.mOwnScrollY;
                int verticalScrollFactor = i2 - ((int) (axisValue * getVerticalScrollFactor()));
                if (verticalScrollFactor >= 0) {
                    i = verticalScrollFactor > scrollRange ? scrollRange : verticalScrollFactor;
                }
                if (i != i2) {
                    setOwnScrollY(i);
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    public boolean onScrollTouch(MotionEvent motionEvent) {
        float f;
        if (!isScrollingEnabled()) {
            return false;
        }
        if (isInsideQsHeader(motionEvent) && !this.mIsBeingDragged) {
            return false;
        }
        this.mForcedScroll = null;
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (motionEvent.findPointerIndex(this.mActivePointerId) != -1 || actionMasked == 0) {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex == -1) {
                            Log.e("StackScroller", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                        } else {
                            int y = (int) motionEvent.getY(findPointerIndex);
                            int i = this.mLastMotionY - y;
                            int abs = Math.abs(((int) motionEvent.getX(findPointerIndex)) - this.mDownX);
                            int abs2 = Math.abs(i);
                            float touchSlop = getTouchSlop(motionEvent);
                            if (!this.mIsBeingDragged && ((float) abs2) > touchSlop && abs2 > abs) {
                                setIsBeingDragged(true);
                                i = (int) (i > 0 ? ((float) i) - touchSlop : ((float) i) + touchSlop);
                            }
                            if (this.mIsBeingDragged) {
                                this.mLastMotionY = y;
                                int scrollRange = getScrollRange();
                                if (this.mExpandedInThisMotion) {
                                    scrollRange = Math.min(scrollRange, this.mMaxScrollAfterExpand);
                                }
                                if (i < 0) {
                                    f = overScrollDown(i);
                                } else {
                                    f = overScrollUp(i, scrollRange);
                                }
                                if (f != 0.0f) {
                                    customOverScrollBy((int) f, this.mOwnScrollY, scrollRange, getHeight() / 2);
                                    this.mController.checkSnoozeLeavebehind();
                                }
                            }
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            int actionIndex = motionEvent.getActionIndex();
                            this.mLastMotionY = (int) motionEvent.getY(actionIndex);
                            this.mDownX = (int) motionEvent.getX(actionIndex);
                            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                        } else if (actionMasked == 6) {
                            onSecondaryPointerUp(motionEvent);
                            this.mLastMotionY = (int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                            this.mDownX = (int) motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                        }
                    } else if (this.mIsBeingDragged && getChildCount() > 0) {
                        if (this.mScroller.springBack(this.mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                            lambda$new$3();
                        }
                        this.mActivePointerId = -1;
                        endDrag();
                    }
                } else if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int yVelocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                    if (shouldOverScrollFling(yVelocity)) {
                        onOverScrollFling(true, yVelocity);
                    } else if (getChildCount() > 0) {
                        if (Math.abs(yVelocity) > this.mMinimumVelocity) {
                            if (getCurrentOverScrollAmount(true) == 0.0f || yVelocity > 0) {
                                this.mFlingAfterUpEvent = true;
                                setFinishScrollingCallback(new NotificationStackScrollLayout$$ExternalSyntheticLambda9(this));
                                fling(-yVelocity);
                            } else {
                                onOverScrollFling(false, yVelocity);
                            }
                        } else if (this.mScroller.springBack(this.mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                            lambda$new$3();
                        }
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                }
            } else if (getChildCount() == 0 || !isInContentBounds(motionEvent)) {
                return false;
            } else {
                setIsBeingDragged(!this.mScroller.isFinished());
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }
                this.mLastMotionY = (int) motionEvent.getY();
                this.mDownX = (int) motionEvent.getX();
                this.mActivePointerId = motionEvent.getPointerId(0);
            }
            return true;
        }
        Log.e("StackScroller", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent " + MotionEvent.actionToString(motionEvent.getActionMasked()));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onScrollTouch$4() {
        this.mFlingAfterUpEvent = false;
        InteractionJankMonitor.getInstance().end(2);
        setFinishScrollingCallback((Runnable) null);
    }

    public boolean isFlingAfterUpEvent() {
        return this.mFlingAfterUpEvent;
    }

    public boolean isInsideQsHeader(MotionEvent motionEvent) {
        this.mQsHeader.getBoundsOnScreen(this.mQsHeaderBound);
        this.mQsHeaderBound.offsetTo(Math.round(motionEvent.getRawX() - motionEvent.getX()), Math.round(motionEvent.getRawY() - motionEvent.getY()));
        return this.mQsHeaderBound.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
    }

    public final void onOverScrollFling(boolean z, int i) {
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            onOverscrollTopChangedListener.flingTopOverscroll((float) i, z);
        }
        this.mDontReportNextOverScroll = true;
        setOverScrollAmount(0.0f, true, false);
    }

    public final void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            int i = action == 0 ? 1 : 0;
            this.mLastMotionY = (int) motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    public final void endDrag() {
        setIsBeingDragged(false);
        recycleVelocityTracker();
        if (getCurrentOverScrollAmount(true) > 0.0f) {
            setOverScrollAmount(0.0f, true, true);
        }
        if (getCurrentOverScrollAmount(false) > 0.0f) {
            setOverScrollAmount(0.0f, false, true);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public void handleEmptySpaceClick(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float touchSlop = getTouchSlop(motionEvent);
                if (!this.mTouchIsClick) {
                    return;
                }
                if (Math.abs(motionEvent.getY() - this.mInitialTouchY) > touchSlop || Math.abs(motionEvent.getX() - this.mInitialTouchX) > touchSlop) {
                    this.mTouchIsClick = false;
                }
            }
        } else if (this.mStatusBarState != 1 && this.mTouchIsClick && isBelowLastNotification(this.mInitialTouchX, this.mInitialTouchY)) {
            this.mOnEmptySpaceClickListener.onEmptySpaceClicked(this.mInitialTouchX, this.mInitialTouchY);
        }
    }

    public void initDownStates(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mExpandedInThisMotion = false;
            this.mOnlyScrollingInThisMotion = !this.mScroller.isFinished();
            this.mDisallowScrollingInThisMotion = false;
            this.mDisallowDismissInThisMotion = false;
            this.mTouchIsClick = true;
            this.mInitialTouchX = motionEvent.getX();
            this.mInitialTouchY = motionEvent.getY();
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
        if (z) {
            cancelLongPress();
        }
    }

    public boolean onInterceptTouchEventScroll(MotionEvent motionEvent) {
        if (!isScrollingEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    int i2 = this.mActivePointerId;
                    if (i2 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i2);
                        if (findPointerIndex == -1) {
                            Log.e("StackScroller", "Invalid pointerId=" + i2 + " in onInterceptTouchEvent");
                        } else {
                            int y = (int) motionEvent.getY(findPointerIndex);
                            int x = (int) motionEvent.getX(findPointerIndex);
                            int abs = Math.abs(y - this.mLastMotionY);
                            int abs2 = Math.abs(x - this.mDownX);
                            if (((float) abs) > getTouchSlop(motionEvent) && abs > abs2) {
                                setIsBeingDragged(true);
                                this.mLastMotionY = y;
                                this.mDownX = x;
                                initVelocityTrackerIfNotExists();
                                this.mVelocityTracker.addMovement(motionEvent);
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            }
            setIsBeingDragged(false);
            this.mActivePointerId = -1;
            recycleVelocityTracker();
            if (this.mScroller.springBack(this.mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                lambda$new$3();
            }
        } else {
            int y2 = (int) motionEvent.getY();
            this.mScrolledToTopOnFirstDown = this.mScrollAdapter.isScrolledToTop();
            if (getChildAtPosition(motionEvent.getX(), (float) y2, false, false) == null) {
                setIsBeingDragged(false);
                recycleVelocityTracker();
            } else {
                this.mLastMotionY = y2;
                this.mDownX = (int) motionEvent.getX();
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                setIsBeingDragged(!this.mScroller.isFinished());
            }
        }
        return this.mIsBeingDragged;
    }

    public final boolean isInContentBounds(MotionEvent motionEvent) {
        return isInContentBounds(motionEvent.getY());
    }

    @VisibleForTesting
    public void setIsBeingDragged(boolean z) {
        this.mIsBeingDragged = z;
        if (z) {
            requestDisallowInterceptTouchEvent(true);
            cancelLongPress();
            resetExposedMenuView(true, true);
        }
    }

    public void requestDisallowLongPress() {
        cancelLongPress();
    }

    public void requestDisallowDismiss() {
        this.mDisallowDismissInThisMotion = true;
    }

    public void cancelLongPress() {
        this.mSwipeHelper.cancelLongPress();
    }

    public void setOnEmptySpaceClickListener(OnEmptySpaceClickListener onEmptySpaceClickListener) {
        this.mOnEmptySpaceClickListener = onEmptySpaceClickListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
        if (r5 != 16908346) goto L_0x0059;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityActionInternal(int r5, android.os.Bundle r6) {
        /*
            r4 = this;
            boolean r6 = super.performAccessibilityActionInternal(r5, r6)
            r0 = 1
            if (r6 == 0) goto L_0x0008
            return r0
        L_0x0008:
            boolean r6 = r4.isEnabled()
            r1 = 0
            if (r6 != 0) goto L_0x0010
            return r1
        L_0x0010:
            r6 = -1
            r2 = 4096(0x1000, float:5.74E-42)
            if (r5 == r2) goto L_0x0024
            r2 = 8192(0x2000, float:1.14794E-41)
            if (r5 == r2) goto L_0x0025
            r2 = 16908344(0x1020038, float:2.3877386E-38)
            if (r5 == r2) goto L_0x0025
            r6 = 16908346(0x102003a, float:2.3877392E-38)
            if (r5 == r6) goto L_0x0024
            goto L_0x0059
        L_0x0024:
            r6 = r0
        L_0x0025:
            int r5 = r4.getHeight()
            int r2 = r4.mPaddingBottom
            int r5 = r5 - r2
            int r2 = r4.mTopPadding
            int r5 = r5 - r2
            int r2 = r4.mPaddingTop
            int r5 = r5 - r2
            com.android.systemui.statusbar.NotificationShelf r2 = r4.mShelf
            int r2 = r2.getIntrinsicHeight()
            int r5 = r5 - r2
            int r2 = r4.mOwnScrollY
            int r6 = r6 * r5
            int r2 = r2 + r6
            int r5 = r4.getScrollRange()
            int r5 = java.lang.Math.min(r2, r5)
            int r5 = java.lang.Math.max(r1, r5)
            int r6 = r4.mOwnScrollY
            if (r5 == r6) goto L_0x0059
            android.widget.OverScroller r2 = r4.mScroller
            int r3 = r4.mScrollX
            int r5 = r5 - r6
            r2.startScroll(r3, r6, r1, r5)
            r4.lambda$new$3()
            return r0
        L_0x0059:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.performAccessibilityActionInternal(int, android.os.Bundle):boolean");
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            cancelLongPress();
        }
    }

    public void clearChildFocus(View view) {
        super.clearChildFocus(view);
        if (this.mForcedScroll == view) {
            this.mForcedScroll = null;
        }
    }

    public boolean isScrolledToBottom() {
        return this.mScrollAdapter.isScrolledToBottom();
    }

    public int getEmptyBottomMargin() {
        int i;
        if (this.mShouldUseSplitNotificationShade) {
            i = Math.max(this.mSplitShadeMinContentHeight, this.mContentHeight);
        } else {
            i = this.mContentHeight;
        }
        return Math.max(this.mMaxLayoutHeight - i, 0);
    }

    public void onExpansionStarted() {
        this.mIsExpansionChanging = true;
        this.mAmbientState.setExpansionChanging(true);
    }

    public void onExpansionStopped() {
        this.mIsExpansionChanging = false;
        this.mAmbientState.setExpansionChanging(false);
        if (!this.mIsExpanded) {
            resetScrollPosition();
            this.mCentralSurfaces.resetUserExpandedStates();
            clearTemporaryViews();
            clearUserLockedViews();
            if (this.mSwipeHelper.isSwiping()) {
                this.mSwipeHelper.resetSwipeState();
                updateContinuousShadowDrawing();
            }
        }
    }

    public final void clearUserLockedViews() {
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) expandableView).setUserLocked(false);
            }
        }
    }

    public final void clearTemporaryViews() {
        clearTemporaryViewsInGroup(this);
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                clearTemporaryViewsInGroup(((ExpandableNotificationRow) expandableView).getChildrenContainer());
            }
        }
    }

    public final void clearTemporaryViewsInGroup(ViewGroup viewGroup) {
        while (viewGroup != null && viewGroup.getTransientViewCount() != 0) {
            View transientView = viewGroup.getTransientView(0);
            viewGroup.removeTransientView(transientView);
            if (transientView instanceof ExpandableView) {
                ((ExpandableView) transientView).setTransientContainer((ViewGroup) null);
            }
        }
    }

    public void onPanelTrackingStarted() {
        this.mPanelTracking = true;
        this.mAmbientState.setPanelTracking(true);
        resetExposedMenuView(true, true);
    }

    public void onPanelTrackingStopped() {
        this.mPanelTracking = false;
        this.mAmbientState.setPanelTracking(false);
    }

    public void resetScrollPosition() {
        this.mScroller.abortAnimation();
        setOwnScrollY(0);
    }

    public final void setIsExpanded(boolean z) {
        boolean z2 = z != this.mIsExpanded;
        this.mIsExpanded = z;
        this.mStackScrollAlgorithm.setIsExpanded(z);
        this.mAmbientState.setShadeExpanded(z);
        this.mStateAnimator.setShadeExpanded(z);
        this.mSwipeHelper.setIsExpanded(z);
        if (z2) {
            this.mWillExpand = false;
            if (!this.mIsExpanded) {
                this.mGroupExpansionManager.collapseGroups();
                this.mExpandHelper.cancelImmediately();
            }
            updateNotificationAnimationStates();
            updateChronometers();
            requestChildrenUpdate();
            updateUseRoundedRectClipping();
            updateDismissBehavior();
        }
    }

    public final void updateChronometers() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            updateChronometerForChild(getChildAt(i));
        }
    }

    public void updateChronometerForChild(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).setChronometerRunning(this.mIsExpanded);
        }
    }

    public void onChildHeightChanged(ExpandableView expandableView, boolean z) {
        boolean z2 = this.mAnimateStackYForContentHeightChange;
        if (z) {
            this.mAnimateStackYForContentHeightChange = true;
        }
        updateContentHeight();
        updateScrollPositionOnExpandInBottom(expandableView);
        clampScrollPosition();
        notifyHeightChangeListener(expandableView, z);
        ExpandableView expandableView2 = null;
        ExpandableNotificationRow expandableNotificationRow = expandableView instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) expandableView : null;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        if (firstVisibleSection != null) {
            expandableView2 = firstVisibleSection.getFirstVisibleChild();
        }
        if (expandableNotificationRow != null && (expandableNotificationRow == expandableView2 || expandableNotificationRow.getNotificationParent() == expandableView2)) {
            updateAlgorithmLayoutMinHeight();
        }
        if (z) {
            requestAnimationOnViewResize(expandableNotificationRow);
        }
        requestChildrenUpdate();
        this.mAnimateStackYForContentHeightChange = z2;
    }

    public void onChildHeightReset(ExpandableView expandableView) {
        updateAnimationState(expandableView);
        updateChronometerForChild(expandableView);
    }

    public final void updateScrollPositionOnExpandInBottom(ExpandableView expandableView) {
        ExpandableView expandableView2;
        if ((expandableView instanceof ExpandableNotificationRow) && !onKeyguard()) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
            if (expandableNotificationRow.isUserLocked() && expandableNotificationRow != getFirstChildNotGone() && !expandableNotificationRow.isSummaryWithChildren()) {
                float translationY = expandableNotificationRow.getTranslationY() + ((float) expandableNotificationRow.getActualHeight());
                if (expandableNotificationRow.isChildInGroup()) {
                    translationY += expandableNotificationRow.getNotificationParent().getTranslationY();
                }
                int i = this.mMaxLayoutHeight + ((int) this.mStackTranslation);
                NotificationSection lastVisibleSection = getLastVisibleSection();
                if (lastVisibleSection == null) {
                    expandableView2 = null;
                } else {
                    expandableView2 = lastVisibleSection.getLastVisibleChild();
                }
                if (!(expandableNotificationRow == expandableView2 || this.mShelf.getVisibility() == 8)) {
                    i -= this.mShelf.getIntrinsicHeight() + this.mPaddingBetweenElements;
                }
                float f = (float) i;
                if (translationY > f) {
                    setOwnScrollY((int) ((((float) this.mOwnScrollY) + translationY) - f));
                    this.mDisallowScrollingInThisMotion = true;
                }
            }
        }
    }

    public void setOnHeightChangedListener(ExpandableView.OnHeightChangedListener onHeightChangedListener) {
        this.mOnHeightChangedListener = onHeightChangedListener;
    }

    public void onChildAnimationFinished() {
        setAnimationRunning(false);
        requestChildrenUpdate();
        runAnimationFinishedRunnables();
        clearTransient();
        clearHeadsUpDisappearRunning();
        if (this.mAmbientState.isClearAllInProgress()) {
            setClearAllInProgress(false);
            if (this.mShadeNeedsToClose) {
                this.mShadeNeedsToClose = false;
                postDelayed(new NotificationStackScrollLayout$$ExternalSyntheticLambda10(this), 200);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onChildAnimationFinished$5() {
        this.mShadeController.animateCollapsePanels(0);
    }

    public final void clearHeadsUpDisappearRunning() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                expandableNotificationRow.setHeadsUpAnimatingAway(false);
                if (expandableNotificationRow.isSummaryWithChildren()) {
                    for (ExpandableNotificationRow headsUpAnimatingAway : expandableNotificationRow.getAttachedChildren()) {
                        headsUpAnimatingAway.setHeadsUpAnimatingAway(false);
                    }
                }
            }
        }
    }

    public final void clearTransient() {
        Iterator<ExpandableView> it = this.mClearTransientViewsWhenFinished.iterator();
        while (it.hasNext()) {
            it.next().removeFromTransientContainer();
        }
        this.mClearTransientViewsWhenFinished.clear();
    }

    public final void runAnimationFinishedRunnables() {
        Iterator<Runnable> it = this.mAnimationFinishedRunnables.iterator();
        while (it.hasNext()) {
            it.next().run();
        }
        this.mAnimationFinishedRunnables.clear();
    }

    public void setDimmed(boolean z, boolean z2) {
        boolean onKeyguard = z & onKeyguard();
        this.mAmbientState.setDimmed(onKeyguard);
        if (!z2 || !this.mAnimationsEnabled) {
            setDimAmount(onKeyguard ? 1.0f : 0.0f);
        } else {
            this.mDimmedNeedsAnimation = true;
            this.mNeedsAnimation = true;
            animateDimmed(onKeyguard);
        }
        requestChildrenUpdate();
    }

    @VisibleForTesting
    public boolean isDimmed() {
        return this.mAmbientState.isDimmed();
    }

    public final void setDimAmount(float f) {
        this.mDimAmount = f;
        updateBackgroundDimming();
    }

    public final void animateDimmed(boolean z) {
        ValueAnimator valueAnimator = this.mDimAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float f = z ? 1.0f : 0.0f;
        float f2 = this.mDimAmount;
        if (f != f2) {
            ValueAnimator ofFloat = TimeAnimator.ofFloat(new float[]{f2, f});
            this.mDimAnimator = ofFloat;
            ofFloat.setDuration(220);
            this.mDimAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mDimAnimator.addListener(this.mDimEndListener);
            this.mDimAnimator.addUpdateListener(this.mDimUpdateListener);
            this.mDimAnimator.start();
        }
    }

    public void updateSensitiveness(boolean z, boolean z2) {
        if (z2 != this.mAmbientState.isHideSensitive()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ((ExpandableView) getChildAt(i)).setHideSensitiveForIntrinsicHeight(z2);
            }
            this.mAmbientState.setHideSensitive(z2);
            if (z && this.mAnimationsEnabled) {
                this.mHideSensitiveNeedsAnimation = true;
                this.mNeedsAnimation = true;
            }
            updateContentHeight();
            requestChildrenUpdate();
        }
    }

    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mAmbientState.setActivatedChild(activatableNotificationView);
        if (this.mAnimationsEnabled) {
            this.mActivateNeedsAnimation = true;
            this.mNeedsAnimation = true;
        }
        requestChildrenUpdate();
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mAmbientState.getActivatedChild();
    }

    public final void applyCurrentState() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((ExpandableView) getChildAt(i)).applyViewState();
        }
        NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener = this.mListener;
        if (onChildLocationsChangedListener != null) {
            onChildLocationsChangedListener.onChildLocationsChanged();
        }
        runAnimationFinishedRunnables();
        setAnimationRunning(false);
        updateBackground();
        updateViewShadows();
    }

    public final void updateViewShadows() {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                this.mTmpSortedChildren.add(expandableView);
            }
        }
        Collections.sort(this.mTmpSortedChildren, this.mViewPositionComparator);
        ExpandableView expandableView2 = null;
        int i2 = 0;
        while (i2 < this.mTmpSortedChildren.size()) {
            ExpandableView expandableView3 = this.mTmpSortedChildren.get(i2);
            float translationZ = expandableView3.getTranslationZ();
            if (expandableView2 == null) {
                f = translationZ;
            } else {
                f = expandableView2.getTranslationZ();
            }
            float f2 = f - translationZ;
            if (f2 <= 0.0f || f2 >= 0.1f) {
                expandableView3.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
            } else {
                expandableView3.setFakeShadowIntensity(f2 / 0.1f, expandableView2.getOutlineAlpha(), (int) (((expandableView2.getTranslationY() + ((float) expandableView2.getActualHeight())) - expandableView3.getTranslationY()) - ((float) expandableView2.getExtraBottomPadding())), (int) (((float) expandableView2.getOutlineTranslation()) + expandableView2.getTranslation()));
            }
            i2++;
            expandableView2 = expandableView3;
        }
        this.mTmpSortedChildren.clear();
    }

    public void updateDecorViews() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
        this.mSectionsManager.setHeaderForegroundColor(colorAttrDefaultColor);
        this.mFooterView.updateColors();
        this.mEmptyShadeView.setTextColor(colorAttrDefaultColor);
    }

    public void goToFullShade(long j) {
        this.mGoToFullShadeNeedsAnimation = true;
        this.mGoToFullShadeDelay = j;
        this.mNeedsAnimation = true;
        requestChildrenUpdate();
    }

    public void cancelExpandHelper() {
        this.mExpandHelper.cancel();
    }

    public void setIntrinsicPadding(int i) {
        this.mIntrinsicPadding = i;
    }

    public int getIntrinsicPadding() {
        return this.mIntrinsicPadding;
    }

    public void setDozing(boolean z, boolean z2, PointF pointF) {
        if (this.mAmbientState.isDozing() != z) {
            this.mAmbientState.setDozing(z);
            requestChildrenUpdate();
            notifyHeightChangeListener(this.mShelf);
        }
    }

    public void setHideAmount(float f, float f2) {
        this.mLinearHideAmount = f;
        this.mInterpolatedHideAmount = f2;
        boolean isFullyHidden = this.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll = this.mAmbientState.isHiddenAtAll();
        this.mAmbientState.setHideAmount(f2);
        boolean isFullyHidden2 = this.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll2 = this.mAmbientState.isHiddenAtAll();
        if (isFullyHidden2 != isFullyHidden) {
            updateVisibility();
        }
        if (!isHiddenAtAll && isHiddenAtAll2) {
            resetExposedMenuView(true, true);
        }
        if (!(isFullyHidden2 == isFullyHidden && isHiddenAtAll == isHiddenAtAll2)) {
            invalidateOutline();
        }
        updateAlgorithmHeightAndPadding();
        updateBackgroundDimming();
        requestChildrenUpdate();
        updateOwnTranslationZ();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r0 = getFirstChildNotGone();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateOwnTranslationZ() {
        /*
            r2 = this;
            boolean r0 = r2.mKeyguardBypassEnabled
            if (r0 == 0) goto L_0x001d
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r2.mAmbientState
            boolean r0 = r0.isHiddenAtAll()
            if (r0 == 0) goto L_0x001d
            com.android.systemui.statusbar.notification.row.ExpandableView r0 = r2.getFirstChildNotGone()
            if (r0 == 0) goto L_0x001d
            boolean r1 = r0.showingPulsing()
            if (r1 == 0) goto L_0x001d
            float r0 = r0.getTranslationZ()
            goto L_0x001e
        L_0x001d:
            r0 = 0
        L_0x001e:
            r2.setTranslationZ(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateOwnTranslationZ():void");
    }

    public final void updateVisibility() {
        int i = 0;
        if (!(!this.mAmbientState.isFullyHidden() || !onKeyguard())) {
            i = 4;
        }
        setVisibility(i);
    }

    public void notifyHideAnimationStart(boolean z) {
        Interpolator interpolator;
        float f = this.mInterpolatedHideAmount;
        if (f == 0.0f || f == 1.0f) {
            this.mBackgroundXFactor = z ? 1.8f : 1.5f;
            if (z) {
                interpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
            } else {
                interpolator = Interpolators.FAST_OUT_SLOW_IN;
            }
            this.mHideXInterpolator = interpolator;
        }
    }

    public boolean isHistoryShown() {
        FooterView footerView = this.mFooterView;
        return footerView != null && footerView.isHistoryShown();
    }

    public void setFooterView(FooterView footerView) {
        int i;
        FooterView footerView2 = this.mFooterView;
        if (footerView2 != null) {
            i = indexOfChild(footerView2);
            removeView(this.mFooterView);
        } else {
            i = -1;
        }
        this.mFooterView = footerView;
        addView(footerView, i);
        View.OnClickListener onClickListener = this.mManageButtonClickListener;
        if (onClickListener != null) {
            this.mFooterView.setManageButtonClickListener(onClickListener);
        }
    }

    public void setEmptyShadeView(EmptyShadeView emptyShadeView) {
        int i;
        EmptyShadeView emptyShadeView2 = this.mEmptyShadeView;
        if (emptyShadeView2 != null) {
            i = indexOfChild(emptyShadeView2);
            removeView(this.mEmptyShadeView);
        } else {
            i = -1;
        }
        this.mEmptyShadeView = emptyShadeView;
        addView(emptyShadeView, i);
    }

    public void updateEmptyShadeView(boolean z, boolean z2) {
        this.mEmptyShadeView.setVisible(z, this.mIsExpanded && this.mAnimationsEnabled);
        int textResource = this.mEmptyShadeView.getTextResource();
        int i = z2 ? R$string.dnd_suppressing_shade_text : R$string.empty_shade_text;
        if (textResource != i) {
            this.mEmptyShadeView.setText(i);
        }
    }

    public void updateFooterView(boolean z, boolean z2, boolean z3) {
        FooterView footerView = this.mFooterView;
        if (footerView != null) {
            boolean z4 = this.mIsExpanded && this.mAnimationsEnabled;
            footerView.setVisible(z, z4);
            this.mFooterView.setSecondaryVisible(z2, z4);
            this.mFooterView.showHistory(z3);
        }
    }

    public void setClearAllInProgress(boolean z) {
        this.mClearAllInProgress = z;
        this.mAmbientState.setClearAllInProgress(z);
        this.mController.getNoticationRoundessManager().setClearAllInProgress(z);
        handleClearAllClipping();
    }

    public boolean getClearAllInProgress() {
        return this.mClearAllInProgress;
    }

    public final void handleClearAllClipping() {
        int childCount = getChildCount();
        boolean z = false;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                if (!this.mClearAllInProgress || !z) {
                    expandableView.setMinClipTopAmount(0);
                } else {
                    expandableView.setMinClipTopAmount(expandableView.getClipTopAmount());
                }
                z = canChildBeCleared(expandableView);
            }
        }
    }

    public int getPaddingAfterMedia() {
        return this.mGapHeight + this.mPaddingBetweenElements;
    }

    public int getEmptyShadeViewHeight() {
        return this.mEmptyShadeView.getHeight();
    }

    public float getBottomMostNotificationBottom() {
        int childCount = getChildCount();
        float f = 0.0f;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                float translationY = (expandableView.getTranslationY() + ((float) expandableView.getActualHeight())) - ((float) expandableView.getClipBottomAmount());
                if (translationY > f) {
                    f = translationY;
                }
            }
        }
        return f + getStackTranslation();
    }

    public void setCentralSurfaces(CentralSurfaces centralSurfaces) {
        this.mCentralSurfaces = centralSurfaces;
    }

    public void requestAnimateEverything() {
        if (this.mIsExpanded && this.mAnimationsEnabled) {
            this.mEverythingNeedsAnimation = true;
            this.mNeedsAnimation = true;
            requestChildrenUpdate();
        }
    }

    public boolean isBelowLastNotification(float f, float f2) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ExpandableView expandableView = (ExpandableView) getChildAt(childCount);
            if (expandableView.getVisibility() != 8) {
                float y = expandableView.getY();
                if (y > f2) {
                    return false;
                }
                boolean z = f2 > (((float) expandableView.getActualHeight()) + y) - ((float) expandableView.getClipBottomAmount());
                FooterView footerView = this.mFooterView;
                if (expandableView == footerView) {
                    if (!z && !footerView.isOnEmptySpace(f - footerView.getX(), f2 - y)) {
                        return false;
                    }
                } else if (expandableView == this.mEmptyShadeView) {
                    return true;
                } else {
                    if (!z) {
                        return false;
                    }
                }
            }
        }
        if (f2 > ((float) this.mTopPadding) + this.mStackTranslation) {
            return true;
        }
        return false;
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEventInternal(accessibilityEvent);
        accessibilityEvent.setScrollable(this.mScrollable);
        accessibilityEvent.setMaxScrollX(this.mScrollX);
        accessibilityEvent.setScrollY(this.mOwnScrollY);
        accessibilityEvent.setMaxScrollY(getScrollRange());
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        if (this.mScrollable) {
            accessibilityNodeInfo.setScrollable(true);
            if (this.mBackwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
            }
            if (this.mForwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
            }
        }
        accessibilityNodeInfo.setClassName(ScrollView.class.getName());
    }

    public void generateChildOrderChangedEvent() {
        if (this.mIsExpanded && this.mAnimationsEnabled) {
            this.mGenerateChildOrderChangedEvent = true;
            this.mNeedsAnimation = true;
            requestChildrenUpdate();
        }
    }

    public int getContainerChildCount() {
        return getChildCount();
    }

    public View getContainerChildAt(int i) {
        return getChildAt(i);
    }

    public void removeContainerView(View view) {
        Assert.isMainThread();
        removeView(view);
        if ((view instanceof ExpandableNotificationRow) && !this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public void addContainerView(View view) {
        Assert.isMainThread();
        addView(view);
        if ((view instanceof ExpandableNotificationRow) && this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public void addContainerViewAt(View view, int i) {
        Assert.isMainThread();
        ensureRemovedFromTransientContainer(view);
        addView(view, i);
        if ((view instanceof ExpandableNotificationRow) && this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public final void ensureRemovedFromTransientContainer(View view) {
        if (view.getParent() != null && (view instanceof ExpandableView)) {
            ((ExpandableView) view).removeFromTransientContainerForAdditionTo(this);
        }
    }

    public void runAfterAnimationFinished(Runnable runnable) {
        this.mAnimationFinishedRunnables.add(runnable);
    }

    public void generateHeadsUpAnimation(NotificationEntry notificationEntry, boolean z) {
        generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), z);
    }

    public void generateHeadsUpAnimation(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        boolean z2 = this.mAnimationsEnabled && (z || this.mHeadsUpGoingAwayAnimationsAllowed);
        boolean z3 = SPEW;
        if (z3) {
            Log.v("StackScroller", "generateHeadsUpAnimation: willAdd=" + z2 + " isHeadsUp=" + z + " row=" + expandableNotificationRow.getEntry().getKey());
        }
        if (!z2) {
            return;
        }
        if (z || !this.mHeadsUpChangeAnimations.remove(new Pair(expandableNotificationRow, Boolean.TRUE))) {
            this.mHeadsUpChangeAnimations.add(new Pair(expandableNotificationRow, Boolean.valueOf(z)));
            this.mNeedsAnimation = true;
            if (!this.mIsExpanded && !this.mWillExpand && !z) {
                expandableNotificationRow.setHeadsUpAnimatingAway(true);
            }
            requestChildrenUpdate();
            return;
        }
        if (z3) {
            Log.v("StackScroller", "generateHeadsUpAnimation: previous hun appear animation cancelled");
        }
        logHunAnimationSkipped(expandableNotificationRow.getEntry().getKey(), "previous hun appear animation cancelled");
    }

    public void setHeadsUpBoundaries(int i, int i2) {
        this.mAmbientState.setMaxHeadsUpTranslation((float) (i - i2));
        this.mStateAnimator.setHeadsUpAppearHeightBottom(i);
        requestChildrenUpdate();
    }

    public void setWillExpand(boolean z) {
        this.mWillExpand = z;
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        this.mAmbientState.setTrackedHeadsUpRow(expandableNotificationRow);
        this.mTrackingHeadsUp = expandableNotificationRow != null;
    }

    public void forceNoOverlappingRendering(boolean z) {
        this.mForceNoOverlappingRendering = z;
    }

    public boolean hasOverlappingRendering() {
        return !this.mForceNoOverlappingRendering && super.hasOverlappingRendering();
    }

    public void setAnimationRunning(boolean z) {
        if (z != this.mAnimationRunning) {
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mRunningAnimationUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mRunningAnimationUpdater);
            }
            this.mAnimationRunning = z;
            updateContinuousShadowDrawing();
        }
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public void setPulsing(boolean z, boolean z2) {
        if (this.mPulsing || z) {
            this.mPulsing = z;
            this.mAmbientState.setPulsing(z);
            this.mSwipeHelper.setPulsing(z);
            updateNotificationAnimationStates();
            updateAlgorithmHeightAndPadding();
            updateContentHeight();
            requestChildrenUpdate();
            notifyHeightChangeListener((ExpandableView) null, z2);
        }
    }

    public void setQsFullScreen(boolean z) {
        this.mQsFullScreen = z;
        updateAlgorithmLayoutMinHeight();
        updateScrollability();
    }

    public boolean isQsFullScreen() {
        return this.mQsFullScreen;
    }

    public void setQsExpansionFraction(float f) {
        float f2 = this.mQsExpansionFraction;
        boolean z = f2 != f && (f2 == 1.0f || f == 1.0f);
        this.mQsExpansionFraction = f;
        updateUseRoundedRectClipping();
        int i = this.mOwnScrollY;
        if (i > 0) {
            setOwnScrollY((int) MathUtils.lerp(i, 0, this.mQsExpansionFraction));
        }
        if (z) {
            updateFooter();
        }
    }

    public final void setOwnScrollY(int i) {
        setOwnScrollY(i, false);
    }

    public final void setOwnScrollY(int i, boolean z) {
        int i2 = this.mOwnScrollY;
        if (i != i2) {
            int i3 = this.mScrollX;
            onScrollChanged(i3, i, i3, i2);
            this.mOwnScrollY = i;
            this.mAmbientState.setScrollY(i);
            updateOnScrollChange();
            updateStackPosition(z);
        }
    }

    public final void updateOnScrollChange() {
        Consumer<Integer> consumer = this.mScrollListener;
        if (consumer != null) {
            consumer.accept(Integer.valueOf(this.mOwnScrollY));
        }
        updateForwardAndBackwardScrollability();
        requestChildrenUpdate();
    }

    public void setShelfController(NotificationShelfController notificationShelfController) {
        int i;
        NotificationShelf notificationShelf = this.mShelf;
        if (notificationShelf != null) {
            i = indexOfChild(notificationShelf);
            removeView(this.mShelf);
        } else {
            i = -1;
        }
        NotificationShelf view = notificationShelfController.getView();
        this.mShelf = view;
        addView(view, i);
        this.mAmbientState.setShelf(this.mShelf);
        this.mStateAnimator.setShelf(this.mShelf);
        notificationShelfController.bind(this.mAmbientState, this.mController);
    }

    public void setMaxDisplayedNotifications(int i) {
        if (this.mMaxDisplayedNotifications != i) {
            this.mMaxDisplayedNotifications = i;
            updateContentHeight();
            notifyHeightChangeListener(this.mShelf);
        }
    }

    public void setKeyguardBottomPadding(float f) {
        this.mKeyguardBottomPadding = f;
    }

    public void setShouldShowShelfOnly(boolean z) {
        this.mShouldShowShelfOnly = z;
        updateAlgorithmLayoutMinHeight();
    }

    public int getMinExpansionHeight() {
        return (this.mShelf.getIntrinsicHeight() - Math.max(0, ((this.mShelf.getIntrinsicHeight() - this.mStatusBarHeight) + this.mWaterfallTopInset) / 2)) + this.mWaterfallTopInset;
    }

    public void setInHeadsUpPinnedMode(boolean z) {
        this.mInHeadsUpPinnedMode = z;
        updateClipping();
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mHeadsUpAnimatingAway = z;
        updateClipping();
    }

    @VisibleForTesting
    public void setStatusBarState(int i) {
        this.mStatusBarState = i;
        this.mAmbientState.setStatusBarState(i);
        updateSpeedBumpIndex();
        updateDismissBehavior();
    }

    public void setUpcomingStatusBarState(int i) {
        this.mUpcomingStatusBarState = i;
        if (i != this.mStatusBarState) {
            updateFooter();
        }
    }

    public void onStatePostChange(boolean z) {
        boolean onKeyguard = onKeyguard();
        this.mAmbientState.setActivatedChild((ActivatableNotificationView) null);
        this.mAmbientState.setDimmed(onKeyguard);
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null) {
            headsUpAppearanceController.onStateChanged();
        }
        setDimmed(onKeyguard, z);
        setExpandingEnabled(!onKeyguard);
        ActivatableNotificationView activatedChild = getActivatedChild();
        setActivatedChild((ActivatableNotificationView) null);
        if (activatedChild != null) {
            activatedChild.makeInactive(false);
        }
        updateFooter();
        requestChildrenUpdate();
        onUpdateRowStates();
        updateVisibility();
    }

    public void setExpandingVelocity(float f) {
        this.mAmbientState.setExpandingVelocity(f);
    }

    public float getOpeningHeight() {
        if (this.mEmptyShadeView.getVisibility() == 8) {
            return (float) getMinExpansionHeight();
        }
        return getAppearEndPosition();
    }

    public void setIsFullWidth(boolean z) {
        this.mAmbientState.setPanelFullWidth(z);
    }

    public void setUnlockHintRunning(boolean z) {
        this.mAmbientState.setUnlockHintRunning(z);
        if (!z) {
            updateStackPosition();
        }
    }

    public void setPanelFlinging(boolean z) {
        this.mAmbientState.setIsFlinging(z);
        if (!z) {
            updateStackPosition();
        }
    }

    public void setHeadsUpGoingAwayAnimationsAllowed(boolean z) {
        this.mHeadsUpGoingAwayAnimationsAllowed = z;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        String str3;
        String str4;
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName());
        sb.append(":");
        sb.append(" pulsing=");
        String str5 = "T";
        sb.append(this.mPulsing ? str5 : "f");
        sb.append(" expanded=");
        if (this.mIsExpanded) {
            str = str5;
        } else {
            str = "f";
        }
        sb.append(str);
        sb.append(" headsUpPinned=");
        if (this.mInHeadsUpPinnedMode) {
            str2 = str5;
        } else {
            str2 = "f";
        }
        sb.append(str2);
        sb.append(" qsClipping=");
        if (this.mShouldUseRoundedRectClipping) {
            str3 = str5;
        } else {
            str3 = "f";
        }
        sb.append(str3);
        sb.append(" qsClipDismiss=");
        if (this.mDismissUsingRowTranslationX) {
            str4 = str5;
        } else {
            str4 = "f";
        }
        sb.append(str4);
        sb.append(" visibility=");
        sb.append(DumpUtilsKt.visibilityString(getVisibility()));
        sb.append(" alpha=");
        sb.append(getAlpha());
        sb.append(" scrollY=");
        sb.append(this.mAmbientState.getScrollY());
        sb.append(" maxTopPadding=");
        sb.append(this.mMaxTopPadding);
        sb.append(" showShelfOnly=");
        if (!this.mShouldShowShelfOnly) {
            str5 = "f";
        }
        sb.append(str5);
        sb.append(" qsExpandFraction=");
        sb.append(this.mQsExpansionFraction);
        sb.append(" isCurrentUserSetup=");
        sb.append(this.mIsCurrentUserSetup);
        sb.append(" hideAmount=");
        sb.append(this.mAmbientState.getHideAmount());
        sb.append(" ambientStateSwipingUp=");
        sb.append(this.mAmbientState.isSwipingUp());
        sb.append(" maxDisplayedNotifications=");
        sb.append(this.mMaxDisplayedNotifications);
        sb.append(" intrinsicContentHeight=");
        sb.append(this.mIntrinsicContentHeight);
        sb.append(" contentHeight=");
        sb.append(this.mContentHeight);
        sb.append(" intrinsicPadding=");
        sb.append(this.mIntrinsicPadding);
        sb.append(" topPadding=");
        sb.append(this.mTopPadding);
        sb.append(" bottomPadding=");
        sb.append(this.mBottomPadding);
        sb.append("]");
        asIndenting.println(sb.toString());
        DumpUtilsKt.withIncreasedIndent(asIndenting, new NotificationStackScrollLayout$$ExternalSyntheticLambda7(this, asIndenting, strArr));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$6(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        int childCount = getChildCount();
        indentingPrintWriter.println("Number of children: " + childCount);
        indentingPrintWriter.println();
        for (int i = 0; i < childCount; i++) {
            ((ExpandableView) getChildAt(i)).dump(indentingPrintWriter, strArr);
            indentingPrintWriter.println();
        }
        int transientViewCount = getTransientViewCount();
        indentingPrintWriter.println("Transient Views: " + transientViewCount);
        for (int i2 = 0; i2 < transientViewCount; i2++) {
            ((ExpandableView) getTransientView(i2)).dump(indentingPrintWriter, strArr);
        }
        View swipedView = this.mSwipeHelper.getSwipedView();
        indentingPrintWriter.println("Swiped view: " + swipedView);
        if (swipedView instanceof ExpandableView) {
            ((ExpandableView) swipedView).dump(indentingPrintWriter, strArr);
        }
    }

    public boolean isFullyHidden() {
        return this.mAmbientState.isFullyHidden();
    }

    public void addOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mExpandedHeightListeners.add(biConsumer);
    }

    public void removeOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mExpandedHeightListeners.remove(biConsumer);
    }

    public void setHeadsUpAppearanceController(HeadsUpAppearanceController headsUpAppearanceController) {
        this.mHeadsUpAppearanceController = headsUpAppearanceController;
    }

    public final boolean isVisible(View view) {
        return view.getVisibility() == 0 && (!view.getClipBounds(this.mTmpRect) || this.mTmpRect.height() > 0);
    }

    public final boolean shouldHideParent(View view, int i) {
        boolean z = !this.mController.hasNotifications(2, false);
        if ((view instanceof SectionHeaderView) && z) {
            return true;
        }
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (!isVisible(expandableNotificationRow) || !includeChildInClearAll(expandableNotificationRow, i)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public final boolean isChildrenVisible(ExpandableNotificationRow expandableNotificationRow) {
        return isVisible(expandableNotificationRow) && expandableNotificationRow.getAttachedChildren() != null && expandableNotificationRow.areChildrenExpanded();
    }

    public final ArrayList<View> getVisibleViewsToAnimateAway(int i) {
        int childCount = getChildCount();
        ArrayList<View> arrayList = new ArrayList<>(childCount);
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (shouldHideParent(childAt, i)) {
                arrayList.add(childAt);
            }
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                if (isChildrenVisible(expandableNotificationRow)) {
                    for (ExpandableNotificationRow next : expandableNotificationRow.getAttachedChildren()) {
                        if (isVisible(next) && includeChildInClearAll(next, i)) {
                            arrayList.add(next);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public final ArrayList<ExpandableNotificationRow> getRowsToDismissInBackend(int i) {
        int childCount = getChildCount();
        ArrayList<ExpandableNotificationRow> arrayList = new ArrayList<>(childCount);
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                if (includeChildInClearAll(expandableNotificationRow, i)) {
                    arrayList.add(expandableNotificationRow);
                }
                List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                if (isVisible(expandableNotificationRow) && attachedChildren != null) {
                    for (ExpandableNotificationRow next : attachedChildren) {
                        if (includeChildInClearAll(expandableNotificationRow, i)) {
                            arrayList.add(next);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    public void clearNotifications(int i, boolean z) {
        ArrayList<View> visibleViewsToAnimateAway = getVisibleViewsToAnimateAway(i);
        ArrayList<ExpandableNotificationRow> rowsToDismissInBackend = getRowsToDismissInBackend(i);
        ClearAllListener clearAllListener = this.mClearAllListener;
        if (clearAllListener != null) {
            clearAllListener.onClearAll(i);
        }
        NotificationStackScrollLayout$$ExternalSyntheticLambda0 notificationStackScrollLayout$$ExternalSyntheticLambda0 = new NotificationStackScrollLayout$$ExternalSyntheticLambda0(this, rowsToDismissInBackend, i);
        if (visibleViewsToAnimateAway.isEmpty()) {
            notificationStackScrollLayout$$ExternalSyntheticLambda0.accept(Boolean.TRUE);
            return;
        }
        setClearAllInProgress(true);
        this.mShadeNeedsToClose = z;
        int i2 = 60;
        int i3 = 0;
        int size = visibleViewsToAnimateAway.size() - 1;
        while (size >= 0) {
            dismissViewAnimated(visibleViewsToAnimateAway.get(size), size == 0 ? notificationStackScrollLayout$$ExternalSyntheticLambda0 : null, i3, 200);
            i2 = Math.max(30, i2 - 5);
            i3 += i2;
            size--;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$clearNotifications$8(ArrayList arrayList, int i, Boolean bool) {
        if (bool.booleanValue()) {
            post(new NotificationStackScrollLayout$$ExternalSyntheticLambda8(this, arrayList, i));
        } else {
            lambda$clearNotifications$7(arrayList, i);
        }
    }

    public final boolean includeChildInClearAll(ExpandableNotificationRow expandableNotificationRow, int i) {
        return canChildBeCleared(expandableNotificationRow) && matchesSelection(expandableNotificationRow, i);
    }

    public void setManageButtonClickListener(View.OnClickListener onClickListener) {
        this.mManageButtonClickListener = onClickListener;
        FooterView footerView = this.mFooterView;
        if (footerView != null) {
            footerView.setManageButtonClickListener(onClickListener);
        }
    }

    @VisibleForTesting
    public void inflateFooterView() {
        FooterView footerView = (FooterView) LayoutInflater.from(this.mContext).inflate(R$layout.status_bar_notification_footer, this, false);
        footerView.setClearAllButtonClickListener(new NotificationStackScrollLayout$$ExternalSyntheticLambda1(this, footerView));
        setFooterView(footerView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateFooterView$9(FooterView footerView, View view) {
        FooterClearAllListener footerClearAllListener = this.mFooterClearAllListener;
        if (footerClearAllListener != null) {
            footerClearAllListener.onClearAll();
        }
        clearNotifications(0, true);
        footerView.setSecondaryVisible(false, true);
    }

    public final void inflateEmptyShadeView() {
        EmptyShadeView emptyShadeView = (EmptyShadeView) LayoutInflater.from(this.mContext).inflate(R$layout.status_bar_no_notifications, this, false);
        emptyShadeView.setText(R$string.empty_shade_text);
        emptyShadeView.setOnClickListener(new NotificationStackScrollLayout$$ExternalSyntheticLambda6(this));
        setEmptyShadeView(emptyShadeView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateEmptyShadeView$10(View view) {
        Intent intent;
        if (this.mController.isHistoryEnabled()) {
            intent = new Intent("android.settings.NOTIFICATION_HISTORY");
        } else {
            intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
        }
        this.mCentralSurfaces.startActivity(intent, true, true, 536870912);
    }

    public void onUpdateRowStates() {
        changeViewPosition(this.mFooterView, getChildCount() - 1);
        changeViewPosition(this.mEmptyShadeView, getChildCount() - 2);
        changeViewPosition(this.mShelf, getChildCount() - 3);
    }

    public float setPulseHeight(float f) {
        float f2;
        this.mAmbientState.setPulseHeight(f);
        if (this.mKeyguardBypassEnabled) {
            notifyAppearChangedListeners();
            f2 = Math.max(0.0f, f - ((float) getIntrinsicPadding()));
        } else {
            f2 = Math.max(0.0f, f - ((float) this.mAmbientState.getInnerHeight(true)));
        }
        requestChildrenUpdate();
        return f2;
    }

    public float getPulseHeight() {
        return this.mAmbientState.getPulseHeight();
    }

    public void setDozeAmount(float f) {
        this.mAmbientState.setDozeAmount(f);
        updateContinuousBackgroundDrawing();
        requestChildrenUpdate();
    }

    public boolean isFullyAwake() {
        return this.mAmbientState.isFullyAwake();
    }

    public void wakeUpFromPulse() {
        setPulseHeight(getWakeUpHeight());
        int childCount = getChildCount();
        float f = -1.0f;
        boolean z = true;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                boolean z2 = expandableView == this.mShelf;
                if ((expandableView instanceof ExpandableNotificationRow) || z2) {
                    if (expandableView.getVisibility() != 0 || z2) {
                        if (!z) {
                            expandableView.setTranslationY(f);
                        }
                    } else if (z) {
                        f = (expandableView.getTranslationY() + ((float) expandableView.getActualHeight())) - ((float) this.mShelf.getIntrinsicHeight());
                        z = false;
                    }
                }
            }
        }
        this.mDimmedNeedsAnimation = true;
    }

    public void setAnimateBottomOnLayout(boolean z) {
        this.mAnimateBottomOnLayout = z;
    }

    public void setOnPulseHeightChangedListener(Runnable runnable) {
        this.mAmbientState.setOnPulseHeightChangedListener(runnable);
    }

    public float calculateAppearFractionBypass() {
        return MathUtils.smoothStep(0.0f, (float) getIntrinsicPadding(), getPulseHeight());
    }

    public void setController(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.mController = notificationStackScrollLayoutController;
        notificationStackScrollLayoutController.getNoticationRoundessManager().setAnimatedChildren(this.mChildrenToAddAnimated);
    }

    public void addSwipedOutView(View view) {
        this.mSwipedOutViews.add(view);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        if (r5.mSectionsManager.beginsSection(r6, r2) != false) goto L_0x0028;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSwipeBegin(android.view.View r6) {
        /*
            r5 = this;
            boolean r0 = r6 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            int r0 = r5.indexOfChild(r6)
            if (r0 >= 0) goto L_0x000c
            return
        L_0x000c:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r1 = r5.mSectionsManager
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r2 = r5.mSections
            java.util.List r3 = r5.getChildrenWithBackground()
            r1.updateFirstAndLastViewsForAllSections(r2, r3)
            r1 = 0
            if (r0 <= 0) goto L_0x0028
            int r2 = r0 + -1
            android.view.View r2 = r5.getChildAt(r2)
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r3 = r5.mSectionsManager
            boolean r3 = r3.beginsSection(r6, r2)
            if (r3 == 0) goto L_0x0029
        L_0x0028:
            r2 = r1
        L_0x0029:
            int r3 = r5.getChildCount()
            r4 = 1
            if (r0 >= r3) goto L_0x003f
            int r0 = r0 + r4
            android.view.View r0 = r5.getChildAt(r0)
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r3 = r5.mSectionsManager
            boolean r3 = r3.beginsSection(r0, r6)
            if (r3 == 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r1 = r0
        L_0x003f:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r5.mController
            com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager r0 = r0.getNoticationRoundessManager()
            com.android.systemui.statusbar.notification.row.ExpandableView r2 = (com.android.systemui.statusbar.notification.row.ExpandableView) r2
            com.android.systemui.statusbar.notification.row.ExpandableView r6 = (com.android.systemui.statusbar.notification.row.ExpandableView) r6
            com.android.systemui.statusbar.notification.row.ExpandableView r1 = (com.android.systemui.statusbar.notification.row.ExpandableView) r1
            r0.setViewsAffectedBySwipe(r2, r6, r1)
            r5.updateFirstAndLastBackgroundViews()
            r5.requestDisallowInterceptTouchEvent(r4)
            r5.updateContinuousShadowDrawing()
            r5.updateContinuousBackgroundDrawing()
            r5.requestChildrenUpdate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onSwipeBegin(android.view.View):void");
    }

    public void onSwipeEnd() {
        updateFirstAndLastBackgroundViews();
        this.mController.getNoticationRoundessManager().setViewsAffectedBySwipe((ExpandableView) null, (ExpandableView) null, (ExpandableView) null);
        this.mShelf.updateAppearance();
    }

    public void setTopHeadsUpEntry(NotificationEntry notificationEntry) {
        this.mTopHeadsUpEntry = notificationEntry;
    }

    public void setNumHeadsUp(long j) {
        this.mNumHeadsUp = j;
        this.mAmbientState.setHasAlertEntries(j > 0);
    }

    public boolean getIsExpanded() {
        return this.mIsExpanded;
    }

    public boolean getOnlyScrollingInThisMotion() {
        return this.mOnlyScrollingInThisMotion;
    }

    public ExpandHelper getExpandHelper() {
        return this.mExpandHelper;
    }

    public boolean isExpandingNotification() {
        return this.mExpandingNotification;
    }

    public boolean getDisallowScrollingInThisMotion() {
        return this.mDisallowScrollingInThisMotion;
    }

    public boolean isBeingDragged() {
        return this.mIsBeingDragged;
    }

    public boolean getExpandedInThisMotion() {
        return this.mExpandedInThisMotion;
    }

    public boolean getDisallowDismissInThisMotion() {
        return this.mDisallowDismissInThisMotion;
    }

    public void setCheckForLeaveBehind(boolean z) {
        this.mCheckForLeavebehind = z;
    }

    public void setTouchHandler(NotificationStackScrollLayoutController.TouchHandler touchHandler) {
        this.mTouchHandler = touchHandler;
    }

    public boolean getCheckSnoozeLeaveBehind() {
        return this.mCheckForLeavebehind;
    }

    public void setClearAllListener(ClearAllListener clearAllListener) {
        this.mClearAllListener = clearAllListener;
    }

    public void setClearAllAnimationListener(ClearAllAnimationListener clearAllAnimationListener) {
        this.mClearAllAnimationListener = clearAllAnimationListener;
    }

    public void setHighPriorityBeforeSpeedBump(boolean z) {
        this.mHighPriorityBeforeSpeedBump = z;
    }

    public void setFooterClearAllListener(FooterClearAllListener footerClearAllListener) {
        this.mFooterClearAllListener = footerClearAllListener;
    }

    public void setShadeController(ShadeController shadeController) {
        this.mShadeController = shadeController;
    }

    public void setExtraTopInsetForFullShadeTransition(float f) {
        this.mExtraTopInsetForFullShadeTransition = f;
        updateStackPosition();
        requestChildrenUpdate();
    }

    public void setFractionToShade(float f) {
        this.mAmbientState.setFractionToShade(f);
        updateContentHeight();
        requestChildrenUpdate();
    }

    public void setOnScrollListener(Consumer<Integer> consumer) {
        this.mScrollListener = consumer;
    }

    public void setRoundedClippingBounds(int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.mRoundedRectClippingLeft == i && this.mRoundedRectClippingRight == i3 && this.mRoundedRectClippingBottom == i4 && this.mRoundedRectClippingTop == i2) {
            float[] fArr = this.mBgCornerRadii;
            if (fArr[0] == ((float) i5) && fArr[5] == ((float) i6)) {
                return;
            }
        }
        this.mRoundedRectClippingLeft = i;
        this.mRoundedRectClippingTop = i2;
        this.mRoundedRectClippingBottom = i4;
        this.mRoundedRectClippingRight = i3;
        float[] fArr2 = this.mBgCornerRadii;
        float f = (float) i5;
        fArr2[0] = f;
        fArr2[1] = f;
        fArr2[2] = f;
        fArr2[3] = f;
        float f2 = (float) i6;
        fArr2[4] = f2;
        fArr2[5] = f2;
        fArr2[6] = f2;
        fArr2[7] = f2;
        this.mRoundedClipPath.reset();
        this.mRoundedClipPath.addRoundRect((float) i, (float) i2, (float) i3, (float) i4, this.mBgCornerRadii, Path.Direction.CW);
        if (this.mShouldUseRoundedRectClipping) {
            invalidate();
        }
    }

    public final void updateSplitNotificationShade() {
        boolean shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(getResources());
        if (shouldUseSplitNotificationShade != this.mShouldUseSplitNotificationShade) {
            this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
            updateDismissBehavior();
            updateUseRoundedRectClipping();
        }
    }

    public final void updateDismissBehavior() {
        boolean z = true;
        if (this.mShouldUseSplitNotificationShade && (this.mStatusBarState == 1 || !this.mIsExpanded)) {
            z = false;
        }
        if (this.mDismissUsingRowTranslationX != z) {
            this.mDismissUsingRowTranslationX = z;
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ExpandableNotificationRow) {
                    ((ExpandableNotificationRow) childAt).setDismissUsingRowTranslationX(z);
                }
            }
        }
    }

    public final void setLaunchingNotification(boolean z) {
        if (z != this.mLaunchingNotification) {
            this.mLaunchingNotification = z;
            LaunchAnimationParameters launchAnimationParameters = this.mLaunchAnimationParams;
            boolean z2 = launchAnimationParameters != null && (launchAnimationParameters.getStartRoundedTopClipping() > 0 || this.mLaunchAnimationParams.getParentStartRoundedTopClipping() > 0);
            this.mLaunchingNotificationNeedsToBeClipped = z2;
            if (!z2 || !this.mLaunchingNotification) {
                this.mLaunchedNotificationClipPath.reset();
            }
            invalidate();
        }
    }

    public final void updateUseRoundedRectClipping() {
        boolean z = false;
        boolean z2 = this.mQsExpansionFraction < 0.5f || this.mShouldUseSplitNotificationShade;
        if (this.mIsExpanded && z2) {
            z = true;
        }
        if (z != this.mShouldUseRoundedRectClipping) {
            this.mShouldUseRoundedRectClipping = z;
            invalidate();
        }
    }

    public final void updateLaunchedNotificationClipPath() {
        if (this.mLaunchingNotificationNeedsToBeClipped && this.mLaunchingNotification && this.mExpandingNotificationRow != null) {
            int min = Math.min(this.mLaunchAnimationParams.getLeft(), this.mRoundedRectClippingLeft);
            int max = Math.max(this.mLaunchAnimationParams.getRight(), this.mRoundedRectClippingRight);
            int max2 = Math.max(this.mLaunchAnimationParams.getBottom(), this.mRoundedRectClippingBottom);
            float interpolation = Interpolators.FAST_OUT_SLOW_IN.getInterpolation(this.mLaunchAnimationParams.getProgress(0, 100));
            float topCornerRadius = this.mLaunchAnimationParams.getTopCornerRadius();
            float bottomCornerRadius = this.mLaunchAnimationParams.getBottomCornerRadius();
            float[] fArr = this.mLaunchedNotificationRadii;
            fArr[0] = topCornerRadius;
            fArr[1] = topCornerRadius;
            fArr[2] = topCornerRadius;
            fArr[3] = topCornerRadius;
            fArr[4] = bottomCornerRadius;
            fArr[5] = bottomCornerRadius;
            fArr[6] = bottomCornerRadius;
            fArr[7] = bottomCornerRadius;
            this.mLaunchedNotificationClipPath.reset();
            this.mLaunchedNotificationClipPath.addRoundRect((float) min, (float) ((int) Math.min(MathUtils.lerp(this.mRoundedRectClippingTop, this.mLaunchAnimationParams.getTop(), interpolation), (float) this.mRoundedRectClippingTop)), (float) max, (float) max2, this.mLaunchedNotificationRadii, Path.Direction.CW);
            ExpandableNotificationRow expandableNotificationRow = this.mExpandingNotificationRow;
            if (expandableNotificationRow.getNotificationParent() != null) {
                expandableNotificationRow = expandableNotificationRow.getNotificationParent();
            }
            this.mLaunchedNotificationClipPath.offset(((float) (-expandableNotificationRow.getLeft())) - expandableNotificationRow.getTranslationX(), ((float) (-expandableNotificationRow.getTop())) - expandableNotificationRow.getTranslationY());
            expandableNotificationRow.setExpandingClipPath(this.mLaunchedNotificationClipPath);
            if (this.mShouldUseRoundedRectClipping) {
                invalidate();
            }
        }
    }

    public void dispatchDraw(Canvas canvas) {
        if (this.mShouldUseRoundedRectClipping && !this.mLaunchingNotification) {
            canvas.clipPath(this.mRoundedClipPath);
        }
        super.dispatchDraw(canvas);
    }

    public boolean drawChild(Canvas canvas, View view, long j) {
        if (!this.mShouldUseRoundedRectClipping || !this.mLaunchingNotification) {
            return super.drawChild(canvas, view, j);
        }
        canvas.save();
        ExpandableView expandableView = (ExpandableView) view;
        Path path = (expandableView.isExpandAnimationRunning() || expandableView.hasExpandingChild()) ? null : this.mRoundedClipPath;
        if (path != null) {
            canvas.clipPath(path);
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public float getTotalTranslationLength(View view) {
        if (!this.mDismissUsingRowTranslationX) {
            return (float) view.getMeasuredWidth();
        }
        float measuredWidth = (float) getMeasuredWidth();
        return measuredWidth - ((measuredWidth - ((float) view.getMeasuredWidth())) / 2.0f);
    }

    public int getTopClippingStartLocation() {
        if (this.mIsExpanded) {
            return this.mQsScrollBoundaryPosition;
        }
        return 0;
    }

    public void animateNextTopPaddingChange() {
        this.mAnimateNextTopPaddingChange = true;
    }

    public void setCurrentUserSetup(boolean z) {
        if (this.mIsCurrentUserSetup != z) {
            this.mIsCurrentUserSetup = z;
            updateFooter();
        }
    }

    public void setLogger(StackStateLogger stackStateLogger) {
        this.mStateAnimator.setLogger(stackStateLogger);
    }

    public final void updateSpeedBumpIndex() {
        this.mSpeedBumpIndexDirty = true;
    }

    public void updateSectionBoundaries(String str) {
        this.mSectionsManager.updateSectionBoundaries(str);
    }

    public void updateContinuousBackgroundDrawing() {
        boolean z = !this.mAmbientState.isFullyAwake() && this.mSwipeHelper.isSwiping();
        if (z != this.mContinuousBackgroundUpdate) {
            this.mContinuousBackgroundUpdate = z;
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mBackgroundUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mBackgroundUpdater);
            }
        }
    }

    public void updateContinuousShadowDrawing() {
        boolean z = this.mAnimationRunning || this.mSwipeHelper.isSwiping();
        if (z != this.mContinuousShadowUpdate) {
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mShadowUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mShadowUpdater);
            }
            this.mContinuousShadowUpdate = z;
        }
    }

    public final void resetExposedMenuView(boolean z, boolean z2) {
        this.mSwipeHelper.resetExposedMenuView(z, z2);
    }

    public static boolean matchesSelection(ExpandableNotificationRow expandableNotificationRow, int i) {
        if (i == 0) {
            return true;
        }
        if (i != 1) {
            if (i == 2) {
                return expandableNotificationRow.getEntry().getBucket() == 6;
            }
            throw new IllegalArgumentException("Unknown selection: " + i);
        } else if (expandableNotificationRow.getEntry().getBucket() < 6) {
            return true;
        } else {
            return false;
        }
    }

    public static class AnimationEvent {
        public static AnimationFilter[] FILTERS = {new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateDimmed().animateZ(), new AnimationFilter().animateZ(), new AnimationFilter().animateDimmed(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateDimmed().animateZ().hasDelays(), new AnimationFilter().animateHideSensitive(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateAlpha().animateDimmed().animateHideSensitive().animateHeight().animateTopInset().animateY().animateZ()};
        public static int[] LENGTHS = {464, 464, 360, 360, 220, 220, 360, 448, 360, 360, 360, 400, 400, 400, 360, 360};
        public final int animationType;
        public final long eventStartTime;
        public final AnimationFilter filter;
        public boolean headsUpFromBottom;
        public final long length;
        public final ExpandableView mChangingView;
        public View viewAfterChangingView;

        public AnimationEvent(ExpandableView expandableView, int i) {
            this(expandableView, i, (long) LENGTHS[i]);
        }

        public AnimationEvent(ExpandableView expandableView, int i, long j) {
            this(expandableView, i, j, FILTERS[i]);
        }

        public AnimationEvent(ExpandableView expandableView, int i, long j, AnimationFilter animationFilter) {
            this.eventStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mChangingView = expandableView;
            this.animationType = i;
            this.length = j;
            this.filter = animationFilter;
        }

        public static long combineLength(ArrayList<AnimationEvent> arrayList) {
            int size = arrayList.size();
            long j = 0;
            for (int i = 0; i < size; i++) {
                AnimationEvent animationEvent = arrayList.get(i);
                j = Math.max(j, animationEvent.length);
                if (animationEvent.animationType == 7) {
                    return animationEvent.length;
                }
            }
            return j;
        }
    }

    public static boolean canChildBeDismissed(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.areGutsExposed() || !expandableNotificationRow.getEntry().hasFinishedInitialization()) {
                return false;
            }
            return expandableNotificationRow.canViewBeDismissed();
        } else if (view instanceof PeopleHubView) {
            return ((PeopleHubView) view).getCanSwipe();
        } else {
            return false;
        }
    }

    public static boolean canChildBeCleared(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.areGutsExposed() || !expandableNotificationRow.getEntry().hasFinishedInitialization()) {
                return false;
            }
            return expandableNotificationRow.canViewBeCleared();
        } else if (view instanceof PeopleHubView) {
            return ((PeopleHubView) view).getCanSwipe();
        } else {
            return false;
        }
    }

    public void onEntryUpdated(NotificationEntry notificationEntry) {
        if (notificationEntry.rowExists() && !notificationEntry.getSbn().isClearable()) {
            snapViewIfNeeded(notificationEntry);
        }
    }

    /* renamed from: onClearAllAnimationsEnd */
    public final void lambda$clearNotifications$7(List<ExpandableNotificationRow> list, int i) {
        ClearAllAnimationListener clearAllAnimationListener = this.mClearAllAnimationListener;
        if (clearAllAnimationListener != null) {
            clearAllAnimationListener.onAnimationEnd(list, i);
        }
    }

    public void resetCheckSnoozeLeavebehind() {
        setCheckForLeaveBehind(true);
    }

    public HeadsUpTouchHelper.Callback getHeadsUpCallback() {
        return this.mHeadsUpCallback;
    }

    public void onGroupExpandChanged(final ExpandableNotificationRow expandableNotificationRow, boolean z) {
        boolean z2 = this.mAnimationsEnabled && (this.mIsExpanded || expandableNotificationRow.isPinned());
        if (z2) {
            this.mExpandedGroupView = expandableNotificationRow;
            this.mNeedsAnimation = true;
        }
        expandableNotificationRow.setChildrenExpanded(z, z2);
        onChildHeightChanged(expandableNotificationRow, false);
        runAfterAnimationFinished(new Runnable() {
            public void run() {
                expandableNotificationRow.onFinishedExpansionChange();
            }
        });
    }

    public ExpandHelper.Callback getExpandHelperCallback() {
        return this.mExpandHelperCallback;
    }

    public float getAppearFraction() {
        return this.mLastSentAppear;
    }

    public float getExpandedHeight() {
        return this.mLastSentExpandedHeight;
    }
}
