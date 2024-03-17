package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.util.Property;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.widget.CallLayout;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.AboveShelfChangedListener;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.LaunchAnimationParameters;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.SwipeableView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.wmshell.BubblesManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ExpandableNotificationRow extends ActivatableNotificationView implements PluginListener<NotificationMenuRowPlugin>, SwipeableView, NotificationFadeAware.FadeOptimizedNotification {
    public static final boolean DEBUG = Log.isLoggable("ExpandableNotifRow", 3);
    public static final long RECENTLY_ALERTED_THRESHOLD_MS = TimeUnit.SECONDS.toMillis(30);
    public static final Property<ExpandableNotificationRow, Float> TRANSLATE_CONTENT = new FloatProperty<ExpandableNotificationRow>("translate") {
        public void setValue(ExpandableNotificationRow expandableNotificationRow, float f) {
            expandableNotificationRow.setTranslation(f);
        }

        public Float get(ExpandableNotificationRow expandableNotificationRow) {
            return Float.valueOf(expandableNotificationRow.getTranslation());
        }
    };
    public boolean mAboveShelf;
    public AboveShelfChangedListener mAboveShelfChangedListener;
    public String mAppName;
    public float mBottomRoundnessDuringLaunchAnimation;
    public Optional<BubblesManager> mBubblesManagerOptional;
    public KeyguardBypassController mBypassController;
    public View mChildAfterViewWhenDismissed;
    public boolean mChildIsExpanding;
    public NotificationChildrenContainer mChildrenContainer;
    public ViewStub mChildrenContainerStub;
    public boolean mChildrenExpanded;
    public ExpandableNotificationRowDragController mDragController;
    public boolean mEnableNonGroupedNotificationExpand;
    public NotificationEntry mEntry;
    public boolean mExpandAnimationRunning;
    public View.OnClickListener mExpandClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            boolean z;
            if (!ExpandableNotificationRow.this.shouldShowPublic() && ((!ExpandableNotificationRow.this.mIsLowPriority || ExpandableNotificationRow.this.isExpanded()) && ExpandableNotificationRow.this.mGroupMembershipManager.isGroupSummary(ExpandableNotificationRow.this.mEntry))) {
                ExpandableNotificationRow.this.mGroupExpansionChanging = true;
                boolean isGroupExpanded = ExpandableNotificationRow.this.mGroupExpansionManager.isGroupExpanded(ExpandableNotificationRow.this.mEntry);
                boolean z2 = ExpandableNotificationRow.this.mGroupExpansionManager.toggleGroupExpansion(ExpandableNotificationRow.this.mEntry);
                ExpandableNotificationRow.this.mOnExpandClickListener.onExpandClicked(ExpandableNotificationRow.this.mEntry, view, z2);
                ExpandableNotificationRow.this.mMetricsLogger.action(408, z2);
                ExpandableNotificationRow.this.onExpansionChanged(true, isGroupExpanded);
            } else if (ExpandableNotificationRow.this.mEnableNonGroupedNotificationExpand) {
                if (view.isAccessibilityFocused()) {
                    ExpandableNotificationRow.this.mPrivateLayout.setFocusOnVisibilityChange();
                }
                if (ExpandableNotificationRow.this.isPinned()) {
                    z = !ExpandableNotificationRow.this.mExpandedWhenPinned;
                    ExpandableNotificationRow.this.mExpandedWhenPinned = z;
                    if (ExpandableNotificationRow.this.mExpansionChangedListener != null) {
                        ExpandableNotificationRow.this.mExpansionChangedListener.onExpansionChanged(z);
                    }
                } else {
                    z = !ExpandableNotificationRow.this.isExpanded();
                    ExpandableNotificationRow.this.setUserExpanded(z);
                }
                ExpandableNotificationRow.this.notifyHeightChanged(true);
                ExpandableNotificationRow.this.mOnExpandClickListener.onExpandClicked(ExpandableNotificationRow.this.mEntry, view, z);
                ExpandableNotificationRow.this.mMetricsLogger.action(407, z);
            }
        }
    };
    public boolean mExpandable;
    public boolean mExpandedWhenPinned;
    public Path mExpandingClipPath;
    public OnExpansionChangedListener mExpansionChangedListener;
    public final Runnable mExpireRecentlyAlertedFlag = new ExpandableNotificationRow$$ExternalSyntheticLambda0(this);
    public FalsingCollector mFalsingCollector;
    public FalsingManager mFalsingManager;
    public boolean mGroupExpansionChanging;
    public GroupExpansionManager mGroupExpansionManager;
    public GroupMembershipManager mGroupMembershipManager;
    public View mGroupParentWhenDismissed;
    public NotificationGuts mGuts;
    public ViewStub mGutsStub;
    public boolean mHasUserChangedExpansion;
    public float mHeaderVisibleAmount = 1.0f;
    public Consumer<Boolean> mHeadsUpAnimatingAwayListener;
    public HeadsUpManager mHeadsUpManager;
    public boolean mHeadsupDisappearRunning;
    public boolean mHideSensitiveForIntrinsicHeight;
    public boolean mIconAnimationRunning;
    public int mIconTransformContentShift;
    public NotificationInlineImageResolver mImageResolver;
    public int mIncreasedPaddingBetweenElements;
    public boolean mIsBlockingHelperShowing;
    public boolean mIsFaded;
    public boolean mIsHeadsUp;
    public boolean mIsLowPriority;
    public boolean mIsPinned;
    public boolean mIsSnoozed;
    public boolean mIsSummaryWithChildren;
    public boolean mIsSystemChildExpanded;
    public boolean mIsSystemExpanded;
    public boolean mJustClicked;
    public boolean mKeepInParent;
    public boolean mLastChronometerRunning = true;
    public LayoutListener mLayoutListener;
    public NotificationContentView[] mLayouts;
    public ExpansionLogger mLogger;
    public String mLoggingKey;
    public LongPressListener mLongPressListener;
    public int mMaxExpandedHeight;
    public int mMaxHeadsUpHeight;
    public int mMaxHeadsUpHeightBeforeN;
    public int mMaxHeadsUpHeightBeforeP;
    public int mMaxHeadsUpHeightBeforeS;
    public int mMaxHeadsUpHeightIncreased;
    public int mMaxSmallHeight;
    public int mMaxSmallHeightBeforeN;
    public int mMaxSmallHeightBeforeP;
    public int mMaxSmallHeightBeforeS;
    public int mMaxSmallHeightLarge;
    public int mMaxSmallHeightMedia;
    public NotificationMediaManager mMediaManager;
    public NotificationMenuRowPlugin mMenuRow;
    public MetricsLogger mMetricsLogger;
    public boolean mMustStayOnScreen;
    public boolean mNeedsRedaction;
    public int mNotificationColor;
    public NotificationGutsManager mNotificationGutsManager;
    public int mNotificationLaunchHeight;
    public ExpandableNotificationRow mNotificationParent;
    public boolean mNotificationTranslationFinished = false;
    public View.OnClickListener mOnClickListener;
    public OnDragSuccessListener mOnDragSuccessListener;
    public OnExpandClickListener mOnExpandClickListener;
    public View.OnClickListener mOnFeedbackClickListener;
    public Runnable mOnIntrinsicHeightReachedRunnable;
    public boolean mOnKeyguard;
    public OnUserInteractionCallback mOnUserInteractionCallback;
    public PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    public NotificationContentView mPrivateLayout;
    public NotificationContentView mPublicLayout;
    public boolean mRemoved;
    public RowContentBindStage mRowContentBindStage;
    public BooleanSupplier mSecureStateProvider;
    public boolean mSensitive;
    public boolean mSensitiveHiddenInGeneral;
    public boolean mShowGroupBackgroundWhenExpanded;
    public boolean mShowNoBackground;
    public boolean mShowingPublic;
    public boolean mShowingPublicInitialized;
    public StatusBarStateController mStatusBarStateController;
    public float mTopRoundnessDuringLaunchAnimation;
    public Animator mTranslateAnim;
    public ArrayList<View> mTranslateableViews;
    public float mTranslationWhenRemoved;
    public boolean mUpdateBackgroundOnUpdate;
    public boolean mUseIncreasedCollapsedHeight;
    public boolean mUseIncreasedHeadsUpHeight;
    public boolean mUserExpanded;
    public boolean mUserLocked;
    public boolean mWasChildInGroupWhenRemoved;

    public interface CoordinateOnClickListener {
        boolean onClick(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem);
    }

    public interface ExpansionLogger {
        void logNotificationExpansion(String str, boolean z, boolean z2);
    }

    public interface LayoutListener {
        void onLayout();
    }

    public interface LongPressListener {
        boolean onLongPress(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem);
    }

    public interface OnDragSuccessListener {
        void onDragSuccess(NotificationEntry notificationEntry);
    }

    public interface OnExpandClickListener {
        void onExpandClicked(NotificationEntry notificationEntry, View view, boolean z);
    }

    public interface OnExpansionChangedListener {
        void onExpansionChanged(boolean z);
    }

    public NotificationContentView[] getLayouts() {
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        return (NotificationContentView[]) Arrays.copyOf(notificationContentViewArr, notificationContentViewArr.length);
    }

    public boolean isPinnedAndExpanded() {
        if (!isPinned()) {
            return false;
        }
        return this.mExpandedWhenPinned;
    }

    public boolean isGroupExpansionChanging() {
        if (isChildInGroup()) {
            return this.mNotificationParent.isGroupExpansionChanging();
        }
        return this.mGroupExpansionChanging;
    }

    public void setGroupExpansionChanging(boolean z) {
        this.mGroupExpansionChanging = z;
    }

    public void setActualHeightAnimating(boolean z) {
        NotificationContentView notificationContentView = this.mPrivateLayout;
        if (notificationContentView != null) {
            notificationContentView.setContentHeightAnimating(z);
        }
    }

    public NotificationContentView getPrivateLayout() {
        return this.mPrivateLayout;
    }

    public NotificationContentView getPublicLayout() {
        return this.mPublicLayout;
    }

    public void setIconAnimationRunning(boolean z) {
        for (NotificationContentView iconAnimationRunning : this.mLayouts) {
            setIconAnimationRunning(z, iconAnimationRunning);
        }
        if (this.mIsSummaryWithChildren) {
            NotificationViewWrapper notificationViewWrapper = this.mChildrenContainer.getNotificationViewWrapper();
            if (notificationViewWrapper != null) {
                setIconAnimationRunningForChild(z, notificationViewWrapper.getIcon());
            }
            NotificationViewWrapper lowPriorityViewWrapper = this.mChildrenContainer.getLowPriorityViewWrapper();
            if (lowPriorityViewWrapper != null) {
                setIconAnimationRunningForChild(z, lowPriorityViewWrapper.getIcon());
            }
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setIconAnimationRunning(z);
            }
        }
        this.mIconAnimationRunning = z;
    }

    public final void setIconAnimationRunning(boolean z, NotificationContentView notificationContentView) {
        if (notificationContentView != null) {
            View contractedChild = notificationContentView.getContractedChild();
            View expandedChild = notificationContentView.getExpandedChild();
            View headsUpChild = notificationContentView.getHeadsUpChild();
            setIconAnimationRunningForChild(z, contractedChild);
            setIconAnimationRunningForChild(z, expandedChild);
            setIconAnimationRunningForChild(z, headsUpChild);
        }
    }

    public final void setIconAnimationRunningForChild(boolean z, View view) {
        if (view != null) {
            setIconRunning((ImageView) view.findViewById(16908294), z);
            setIconRunning((ImageView) view.findViewById(16909431), z);
        }
    }

    public final void setIconRunning(ImageView imageView, boolean z) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                if (z) {
                    animationDrawable.start();
                } else {
                    animationDrawable.stop();
                }
            } else if (drawable instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                if (z) {
                    animatedVectorDrawable.start();
                } else {
                    animatedVectorDrawable.stop();
                }
            }
        }
    }

    public boolean getIsNonblockable() {
        NotificationEntry notificationEntry = this.mEntry;
        if (notificationEntry == null) {
            return true;
        }
        return !notificationEntry.isBlockable();
    }

    public final boolean isConversation() {
        return this.mPeopleNotificationIdentifier.getPeopleNotificationType(this.mEntry) != 0;
    }

    public void onNotificationUpdated() {
        for (NotificationContentView onNotificationUpdated : this.mLayouts) {
            onNotificationUpdated.onNotificationUpdated(this.mEntry);
        }
        this.mShowingPublicInitialized = false;
        updateNotificationColor();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.onNotificationUpdated(this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
        }
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.recreateNotificationHeader(this.mExpandClickListener, isConversation());
            this.mChildrenContainer.onNotificationUpdated();
        }
        if (this.mIconAnimationRunning) {
            setIconAnimationRunning(true);
        }
        if (this.mLastChronometerRunning) {
            setChronometerRunning(true);
        }
        ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.updateChildrenAppearance();
        }
        onAttachedChildrenCountChanged();
        this.mPublicLayout.updateExpandButtons(true);
        updateLimits();
        updateShelfIconColor();
        updateRippleAllowed();
        if (this.mUpdateBackgroundOnUpdate) {
            this.mUpdateBackgroundOnUpdate = false;
            updateBackgroundColors();
        }
    }

    public void onNotificationRankingUpdated() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.onNotificationUpdated(this.mEntry.getSbn());
        }
    }

    public void updateBubbleButton() {
        for (NotificationContentView updateBubbleButton : this.mLayouts) {
            updateBubbleButton.updateBubbleButton(this.mEntry);
        }
    }

    @VisibleForTesting
    public void updateShelfIconColor() {
        StatusBarIconView shelfIcon = this.mEntry.getIcons().getShelfIcon();
        int i = 0;
        if (!Boolean.TRUE.equals(shelfIcon.getTag(R$id.icon_is_pre_L)) || NotificationUtils.isGrayscale(shelfIcon, ContrastColorUtil.getInstance(this.mContext))) {
            i = getOriginalIconColor();
        }
        shelfIcon.setStaticDrawableColor(i);
    }

    public int getOriginalIconColor() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return this.mChildrenContainer.getVisibleWrapper().getOriginalIconColor();
        }
        int originalIconColor = getShowingLayout().getOriginalIconColor();
        boolean z = true;
        if (originalIconColor != 1) {
            return originalIconColor;
        }
        NotificationEntry notificationEntry = this.mEntry;
        Context context = this.mContext;
        if (!this.mIsLowPriority || isExpanded()) {
            z = false;
        }
        return notificationEntry.getContrastedColor(context, z, getBackgroundColorWithoutTint());
    }

    public void setAboveShelfChangedListener(AboveShelfChangedListener aboveShelfChangedListener) {
        this.mAboveShelfChangedListener = aboveShelfChangedListener;
    }

    public void setSecureStateProvider(BooleanSupplier booleanSupplier) {
        this.mSecureStateProvider = booleanSupplier;
    }

    public final void updateLimits() {
        for (NotificationContentView updateLimitsForView : this.mLayouts) {
            updateLimitsForView(updateLimitsForView);
        }
    }

    public final void updateLimitsForView(NotificationContentView notificationContentView) {
        int i;
        int i2;
        View contractedChild = notificationContentView.getContractedChild();
        boolean z = true;
        boolean z2 = (contractedChild == null || contractedChild.getId() == 16909547) ? false : true;
        int i3 = this.mEntry.targetSdk;
        boolean z3 = i3 < 24;
        boolean z4 = i3 < 28;
        boolean z5 = i3 < 31;
        boolean z6 = contractedChild instanceof CallLayout;
        if (!z2 || !z5 || this.mIsSummaryWithChildren) {
            if (z6) {
                i = this.mMaxExpandedHeight;
            } else if (!this.mUseIncreasedCollapsedHeight || notificationContentView != this.mPrivateLayout) {
                i = this.mMaxSmallHeight;
            } else {
                i = this.mMaxSmallHeightLarge;
            }
        } else if (z3) {
            i = this.mMaxSmallHeightBeforeN;
        } else if (z4) {
            i = this.mMaxSmallHeightBeforeP;
        } else {
            i = this.mMaxSmallHeightBeforeS;
        }
        if (notificationContentView.getHeadsUpChild() == null || notificationContentView.getHeadsUpChild().getId() == 16909547) {
            z = false;
        }
        if (!z || !z5) {
            if (!this.mUseIncreasedHeadsUpHeight || notificationContentView != this.mPrivateLayout) {
                i2 = this.mMaxHeadsUpHeight;
            } else {
                i2 = this.mMaxHeadsUpHeightIncreased;
            }
        } else if (z3) {
            i2 = this.mMaxHeadsUpHeightBeforeN;
        } else if (z4) {
            i2 = this.mMaxHeadsUpHeightBeforeP;
        } else {
            i2 = this.mMaxHeadsUpHeightBeforeS;
        }
        NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(2);
        if (visibleWrapper != null) {
            i2 = Math.max(i2, visibleWrapper.getMinLayoutHeight());
        }
        notificationContentView.setHeights(i, i2, this.mMaxExpandedHeight);
    }

    public NotificationEntry getEntry() {
        return this.mEntry;
    }

    public boolean isHeadsUp() {
        return this.mIsHeadsUp;
    }

    public void setHeadsUp(boolean z) {
        boolean isAboveShelf = isAboveShelf();
        int intrinsicHeight = getIntrinsicHeight();
        this.mIsHeadsUp = z;
        this.mPrivateLayout.setHeadsUp(z);
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateGroupOverflow();
        }
        if (intrinsicHeight != getIntrinsicHeight()) {
            notifyHeightChanged(false);
        }
        if (z) {
            this.mMustStayOnScreen = true;
            setAboveShelf(true);
        } else if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public boolean showingPulsing() {
        return isHeadsUpState() && (isDozing() || (this.mOnKeyguard && isBypassEnabled()));
    }

    public boolean isHeadsUpState() {
        return this.mIsHeadsUp || this.mHeadsupDisappearRunning;
    }

    public void setRemoteInputController(RemoteInputController remoteInputController) {
        this.mPrivateLayout.setRemoteInputController(remoteInputController);
    }

    public void setHeaderVisibleAmount(float f) {
        if (this.mHeaderVisibleAmount != f) {
            this.mHeaderVisibleAmount = f;
            for (NotificationContentView headerVisibleAmount : this.mLayouts) {
                headerVisibleAmount.setHeaderVisibleAmount(f);
            }
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (notificationChildrenContainer != null) {
                notificationChildrenContainer.setHeaderVisibleAmount(f);
            }
            notifyHeightChanged(false);
        }
    }

    public float getHeaderVisibleAmount() {
        return this.mHeaderVisibleAmount;
    }

    public void setHeadsUpIsVisible() {
        super.setHeadsUpIsVisible();
        this.mMustStayOnScreen = false;
    }

    public void setUntruncatedChildCount(int i) {
        if (this.mChildrenContainer == null) {
            this.mChildrenContainerStub.inflate();
        }
        this.mChildrenContainer.setUntruncatedChildCount(i);
    }

    public void resetChildSystemExpandedStates() {
        if (isSummaryWithChildren()) {
            this.mChildrenContainer.updateExpansionStates();
        }
    }

    public void addChildNotification(ExpandableNotificationRow expandableNotificationRow, int i) {
        if (this.mChildrenContainer == null) {
            this.mChildrenContainerStub.inflate();
        }
        this.mChildrenContainer.addNotification(expandableNotificationRow, i);
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(true, this);
    }

    public void removeChildNotification(ExpandableNotificationRow expandableNotificationRow) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.removeNotification(expandableNotificationRow);
        }
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(false, (ExpandableNotificationRow) null);
        expandableNotificationRow.setBottomRoundness(0.0f, false);
    }

    public ExpandableNotificationRow getChildNotificationAt(int i) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null || notificationChildrenContainer.getAttachedChildren().size() <= i) {
            return null;
        }
        return this.mChildrenContainer.getAttachedChildren().get(i);
    }

    public boolean isChildInGroup() {
        return this.mNotificationParent != null;
    }

    public ExpandableNotificationRow getNotificationParent() {
        return this.mNotificationParent;
    }

    public void setIsChildInGroup(boolean z, ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2;
        if (this.mExpandAnimationRunning && !z && (expandableNotificationRow2 = this.mNotificationParent) != null) {
            expandableNotificationRow2.setChildIsExpanding(false);
            this.mNotificationParent.setExpandingClipPath((Path) null);
            this.mNotificationParent.setExtraWidthForClipping(0.0f);
            this.mNotificationParent.setMinimumHeightForClipping(0);
        }
        if (!z) {
            expandableNotificationRow = null;
        }
        this.mNotificationParent = expandableNotificationRow;
        this.mPrivateLayout.setIsChildInGroup(z);
        updateBackgroundForGroupState();
        updateClickAndFocus();
        if (this.mNotificationParent != null) {
            setOverrideTintColor(0, 0.0f);
            this.mNotificationParent.updateBackgroundForGroupState();
        }
        updateBackgroundClipping();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            this.mFalsingManager.isFalseTap(2);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || !isChildInGroup() || isGroupExpanded()) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public boolean handleSlideBack() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            return false;
        }
        animateResetTranslation();
        return true;
    }

    public boolean isSummaryWithChildren() {
        return this.mIsSummaryWithChildren;
    }

    public boolean areChildrenExpanded() {
        return this.mChildrenExpanded;
    }

    public List<ExpandableNotificationRow> getAttachedChildren() {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null) {
            return null;
        }
        return notificationChildrenContainer.getAttachedChildren();
    }

    public boolean applyChildOrder(List<ExpandableNotificationRow> list, VisualStabilityManager visualStabilityManager, VisualStabilityManager.Callback callback) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        return notificationChildrenContainer != null && notificationChildrenContainer.applyChildOrder(list, visualStabilityManager, callback);
    }

    public void updateChildrenStates(AmbientState ambientState) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateState(getViewState(), ambientState);
        }
    }

    public void applyChildrenState() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.applyState();
        }
    }

    public void prepareExpansionChanged() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.prepareExpansionChanged();
        }
    }

    public void startChildAnimation(AnimationProperties animationProperties) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.startAnimationToState(animationProperties);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0009, code lost:
        r2 = r1.mChildrenContainer.getViewAtPosition(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.systemui.statusbar.notification.row.ExpandableNotificationRow getViewAtPosition(float r2) {
        /*
            r1 = this;
            boolean r0 = r1.mIsSummaryWithChildren
            if (r0 == 0) goto L_0x0013
            boolean r0 = r1.mChildrenExpanded
            if (r0 != 0) goto L_0x0009
            goto L_0x0013
        L_0x0009:
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r0 = r1.mChildrenContainer
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r2 = r0.getViewAtPosition(r2)
            if (r2 != 0) goto L_0x0012
            goto L_0x0013
        L_0x0012:
            r1 = r2
        L_0x0013:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.getViewAtPosition(float):com.android.systemui.statusbar.notification.row.ExpandableNotificationRow");
    }

    public NotificationGuts getGuts() {
        return this.mGuts;
    }

    public void setPinned(boolean z) {
        int intrinsicHeight = getIntrinsicHeight();
        boolean isAboveShelf = isAboveShelf();
        this.mIsPinned = z;
        if (intrinsicHeight != getIntrinsicHeight()) {
            notifyHeightChanged(false);
        }
        if (z) {
            setIconAnimationRunning(true);
            this.mExpandedWhenPinned = false;
        } else if (this.mExpandedWhenPinned) {
            setUserExpanded(true);
        }
        setChronometerRunning(this.mLastChronometerRunning);
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public boolean isPinned() {
        return this.mIsPinned;
    }

    public int getPinnedHeadsUpHeight() {
        return getPinnedHeadsUpHeight(true);
    }

    public final int getPinnedHeadsUpHeight(boolean z) {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getIntrinsicHeight();
        }
        if (this.mExpandedWhenPinned) {
            return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
        }
        if (z) {
            return Math.max(getCollapsedHeight(), getHeadsUpHeight());
        }
        return getHeadsUpHeight();
    }

    public void setJustClicked(boolean z) {
        this.mJustClicked = z;
    }

    public boolean wasJustClicked() {
        return this.mJustClicked;
    }

    public void setChronometerRunning(boolean z) {
        this.mLastChronometerRunning = z;
        setChronometerRunning(z, this.mPrivateLayout);
        setChronometerRunning(z, this.mPublicLayout);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            List<ExpandableNotificationRow> attachedChildren = notificationChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setChronometerRunning(z);
            }
        }
    }

    public final void setChronometerRunning(boolean z, NotificationContentView notificationContentView) {
        if (notificationContentView != null) {
            boolean z2 = z || isPinned();
            View contractedChild = notificationContentView.getContractedChild();
            View expandedChild = notificationContentView.getExpandedChild();
            View headsUpChild = notificationContentView.getHeadsUpChild();
            setChronometerRunningForChild(z2, contractedChild);
            setChronometerRunningForChild(z2, expandedChild);
            setChronometerRunningForChild(z2, headsUpChild);
        }
    }

    public final void setChronometerRunningForChild(boolean z, View view) {
        if (view != null) {
            View findViewById = view.findViewById(16908876);
            if (findViewById instanceof Chronometer) {
                ((Chronometer) findViewById).setStarted(z);
            }
        }
    }

    public NotificationViewWrapper getNotificationViewWrapper() {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getNotificationViewWrapper();
        }
        return this.mPrivateLayout.getNotificationViewWrapper();
    }

    public NotificationViewWrapper getVisibleNotificationViewWrapper() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getVisibleWrapper();
        }
        return this.mChildrenContainer.getVisibleWrapper();
    }

    public void setLongPressListener(LongPressListener longPressListener) {
        this.mLongPressListener = longPressListener;
    }

    public void setDragController(ExpandableNotificationRowDragController expandableNotificationRowDragController) {
        this.mDragController = expandableNotificationRowDragController;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
        updateClickAndFocus();
    }

    public View.OnClickListener getBubbleClickListener() {
        return new ExpandableNotificationRow$$ExternalSyntheticLambda6(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getBubbleClickListener$0(View view) {
        if (this.mBubblesManagerOptional.isPresent()) {
            NotificationEntry notificationEntry = this.mEntry;
            this.mBubblesManagerOptional.get().onUserChangedBubble(notificationEntry, !notificationEntry.isBubble());
        }
        this.mHeadsUpManager.removeNotification(this.mEntry.getKey(), true);
    }

    public View.OnClickListener getSnoozeClickListener(NotificationMenuRowPlugin.MenuItem menuItem) {
        return new ExpandableNotificationRow$$ExternalSyntheticLambda5(this, menuItem);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSnoozeClickListener$1(NotificationMenuRowPlugin.MenuItem menuItem, View view) {
        this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
        this.mNotificationGutsManager.openGuts(this, 0, 0, menuItem);
        this.mIsSnoozed = true;
    }

    public final void updateClickAndFocus() {
        boolean z = false;
        boolean z2 = !isChildInGroup() || isGroupExpanded();
        if (this.mOnClickListener != null && z2) {
            z = true;
        }
        if (isFocusable() != z2) {
            setFocusable(z2);
        }
        if (isClickable() != z) {
            setClickable(z);
        }
    }

    public void setGutsView(NotificationMenuRowPlugin.MenuItem menuItem) {
        if (getGuts() != null && (menuItem.getGutsView() instanceof NotificationGuts.GutsContent)) {
            getGuts().setGutsContent((NotificationGuts.GutsContent) menuItem.getGutsView());
        }
    }

    public void onPluginConnected(NotificationMenuRowPlugin notificationMenuRowPlugin, Context context) {
        NotificationMenuRowPlugin notificationMenuRowPlugin2 = this.mMenuRow;
        boolean z = (notificationMenuRowPlugin2 == null || notificationMenuRowPlugin2.getMenuView() == null) ? false : true;
        if (z) {
            removeView(this.mMenuRow.getMenuView());
        }
        if (notificationMenuRowPlugin != null) {
            this.mMenuRow = notificationMenuRowPlugin;
            if (notificationMenuRowPlugin.shouldUseDefaultMenuItems()) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(NotificationMenuRow.createConversationItem(this.mContext));
                arrayList.add(NotificationMenuRow.createPartialConversationItem(this.mContext));
                arrayList.add(NotificationMenuRow.createInfoItem(this.mContext));
                arrayList.add(NotificationMenuRow.createSnoozeItem(this.mContext));
                this.mMenuRow.setMenuItems(arrayList);
            }
            if (z) {
                createMenu();
            }
        }
    }

    public void onPluginDisconnected(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean z = this.mMenuRow.getMenuView() != null;
        this.mMenuRow = new NotificationMenuRow(this.mContext, this.mPeopleNotificationIdentifier);
        if (z) {
            createMenu();
        }
    }

    public boolean hasFinishedInitialization() {
        return getEntry().hasFinishedInitialization();
    }

    public NotificationMenuRowPlugin createMenu() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null) {
            return null;
        }
        if (notificationMenuRowPlugin.getMenuView() == null) {
            this.mMenuRow.createMenu(this, this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), 0, new FrameLayout.LayoutParams(-1, -1));
        }
        return this.mMenuRow;
    }

    public NotificationMenuRowPlugin getProvider() {
        return this.mMenuRow;
    }

    public void onDensityOrFontScaleChanged() {
        super.onDensityOrFontScaleChanged();
        initDimens();
        initBackground();
        reInflateViews();
    }

    public final void reInflateViews() {
        Trace.beginSection("ExpandableNotificationRow#reInflateViews");
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.reInflateViews(this.mExpandClickListener, this.mEntry.getSbn());
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            int indexOfChild = indexOfChild(notificationGuts);
            removeView(notificationGuts);
            NotificationGuts notificationGuts2 = (NotificationGuts) LayoutInflater.from(this.mContext).inflate(R$layout.notification_guts, this, false);
            this.mGuts = notificationGuts2;
            notificationGuts2.setVisibility(notificationGuts.isExposed() ? 0 : 8);
            addView(this.mGuts, indexOfChild);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        View menuView = notificationMenuRowPlugin == null ? null : notificationMenuRowPlugin.getMenuView();
        if (menuView != null) {
            int indexOfChild2 = indexOfChild(menuView);
            removeView(menuView);
            this.mMenuRow.createMenu(this, this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), indexOfChild2);
        }
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.reinflate();
            notificationContentView.reInflateViews();
        }
        this.mEntry.getSbn().clearPackageContext();
        ((RowContentBindParams) this.mRowContentBindStage.getStageParams(this.mEntry)).setNeedsReinflation(true);
        this.mRowContentBindStage.requestRebind(this.mEntry, (NotifBindPipeline.BindCallback) null);
        Trace.endSection();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onConfigurationChanged();
        }
        NotificationInlineImageResolver notificationInlineImageResolver = this.mImageResolver;
        if (notificationInlineImageResolver != null) {
            notificationInlineImageResolver.updateMaxImageSizes();
        }
    }

    public void onUiModeChanged() {
        this.mUpdateBackgroundOnUpdate = true;
        reInflateViews();
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            for (ExpandableNotificationRow onUiModeChanged : notificationChildrenContainer.getAttachedChildren()) {
                onUiModeChanged.onUiModeChanged();
            }
        }
    }

    public void setContentBackground(int i, boolean z, NotificationContentView notificationContentView) {
        if (getShowingLayout() == notificationContentView) {
            setTintColor(i, z);
        }
    }

    public void setBackgroundTintColor(int i) {
        super.setBackgroundTintColor(i);
        NotificationContentView showingLayout = getShowingLayout();
        if (showingLayout != null) {
            showingLayout.setBackgroundTintColor(i);
        }
    }

    public void closeRemoteInput() {
        for (NotificationContentView closeRemoteInput : this.mLayouts) {
            closeRemoteInput.closeRemoteInput();
        }
    }

    public void setSingleLineWidthIndention(int i) {
        this.mPrivateLayout.setSingleLineWidthIndention(i);
    }

    public int getNotificationColor() {
        return this.mNotificationColor;
    }

    public void updateNotificationColor() {
        this.mNotificationColor = ContrastColorUtil.resolveContrastColor(this.mContext, this.mEntry.getSbn().getNotification().color, getBackgroundColorWithoutTint(), (getResources().getConfiguration().uiMode & 48) == 32);
    }

    public HybridNotificationView getSingleLineView() {
        return this.mPrivateLayout.getSingleLineView();
    }

    public boolean isOnKeyguard() {
        return this.mOnKeyguard;
    }

    public void removeAllChildren() {
        ArrayList arrayList = new ArrayList(this.mChildrenContainer.getAttachedChildren());
        for (int i = 0; i < arrayList.size(); i++) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) arrayList.get(i);
            if (!expandableNotificationRow.keepInParent()) {
                this.mChildrenContainer.removeNotification(expandableNotificationRow);
                expandableNotificationRow.setIsChildInGroup(false, (ExpandableNotificationRow) null);
            }
        }
        onAttachedChildrenCountChanged();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0023, code lost:
        r3 = r2.mNotificationParent.getAttachedChildren();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dismiss(boolean r3) {
        /*
            r2 = this;
            super.dismiss(r3)
            r3 = 0
            r2.setLongPressListener(r3)
            r2.setDragController(r3)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = r2.mNotificationParent
            r2.mGroupParentWhenDismissed = r0
            r2.mChildAfterViewWhenDismissed = r3
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r2.mEntry
            com.android.systemui.statusbar.notification.icon.IconPack r3 = r3.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r3 = r3.getStatusBarIcon()
            r3.setDismissed()
            boolean r3 = r2.isChildInGroup()
            if (r3 == 0) goto L_0x0042
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = r2.mNotificationParent
            java.util.List r3 = r3.getAttachedChildren()
            int r0 = r3.indexOf(r2)
            r1 = -1
            if (r0 == r1) goto L_0x0042
            int r1 = r3.size()
            int r1 = r1 + -1
            if (r0 >= r1) goto L_0x0042
            int r0 = r0 + 1
            java.lang.Object r3 = r3.get(r0)
            android.view.View r3 = (android.view.View) r3
            r2.mChildAfterViewWhenDismissed = r3
        L_0x0042:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.dismiss(boolean):void");
    }

    public boolean keepInParent() {
        return this.mKeepInParent;
    }

    public void setKeepInParent(boolean z) {
        this.mKeepInParent = z;
    }

    public boolean isRemoved() {
        return this.mRemoved;
    }

    public void setRemoved() {
        this.mRemoved = true;
        this.mTranslationWhenRemoved = getTranslationY();
        this.mWasChildInGroupWhenRemoved = isChildInGroup();
        if (isChildInGroup()) {
            this.mTranslationWhenRemoved += getNotificationParent().getTranslationY();
        }
        for (NotificationContentView removed : this.mLayouts) {
            removed.setRemoved();
        }
    }

    public boolean wasChildInGroupWhenRemoved() {
        return this.mWasChildInGroupWhenRemoved;
    }

    public float getTranslationWhenRemoved() {
        return this.mTranslationWhenRemoved;
    }

    public NotificationChildrenContainer getChildrenContainer() {
        return this.mChildrenContainer;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        Consumer<Boolean> consumer;
        boolean isAboveShelf = isAboveShelf();
        boolean z2 = z != this.mHeadsupDisappearRunning;
        this.mHeadsupDisappearRunning = z;
        this.mPrivateLayout.setHeadsUpAnimatingAway(z);
        if (z2 && (consumer = this.mHeadsUpAnimatingAwayListener) != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public void setHeadsUpAnimatingAwayListener(Consumer<Boolean> consumer) {
        this.mHeadsUpAnimatingAwayListener = consumer;
    }

    public boolean isHeadsUpAnimatingAway() {
        return this.mHeadsupDisappearRunning;
    }

    public View getChildAfterViewWhenDismissed() {
        return this.mChildAfterViewWhenDismissed;
    }

    public View getGroupParentWhenDismissed() {
        return this.mGroupParentWhenDismissed;
    }

    public void performDismiss(boolean z) {
        OnUserInteractionCallback onUserInteractionCallback;
        this.mMetricsLogger.count("notification_dismissed", 1);
        dismiss(z);
        if (this.mEntry.isDismissable() && (onUserInteractionCallback = this.mOnUserInteractionCallback) != null) {
            onUserInteractionCallback.registerFutureDismissal(this.mEntry, 2).run();
        }
    }

    public boolean isBlockingHelperShowing() {
        return this.mIsBlockingHelperShowing;
    }

    public boolean isBlockingHelperShowingAndTranslationFinished() {
        return this.mIsBlockingHelperShowing && this.mNotificationTranslationFinished;
    }

    public View getShelfTransformationTarget() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getShelfTransformationTarget();
        }
        return this.mChildrenContainer.getVisibleWrapper().getShelfTransformationTarget();
    }

    public boolean isShowingIcon() {
        if (!areGutsExposed() && getShelfTransformationTarget() != null) {
            return true;
        }
        return false;
    }

    public void updateContentTransformation() {
        if (!this.mExpandAnimationRunning) {
            super.updateContentTransformation();
        }
    }

    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        if (!this.mIsLastChild) {
            f = 1.0f;
        }
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setAlpha(f);
            notificationContentView.setTranslationY(f2);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setAlpha(f);
            this.mChildrenContainer.setTranslationY(f2);
        }
    }

    public void setIsLowPriority(boolean z) {
        this.mIsLowPriority = z;
        this.mPrivateLayout.setIsLowPriority(z);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setIsLowPriority(z);
        }
    }

    public boolean isLowPriority() {
        return this.mIsLowPriority;
    }

    public void setUsesIncreasedCollapsedHeight(boolean z) {
        this.mUseIncreasedCollapsedHeight = z;
    }

    public void setUsesIncreasedHeadsUpHeight(boolean z) {
        this.mUseIncreasedHeadsUpHeight = z;
    }

    @Deprecated
    public void setNeedsRedaction(boolean z) {
        if (this.mNeedsRedaction != z) {
            this.mNeedsRedaction = z;
            if (!isRemoved()) {
                RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mRowContentBindStage.getStageParams(this.mEntry);
                if (z) {
                    rowContentBindParams.requireContentViews(8);
                } else {
                    rowContentBindParams.markContentViewsFreeable(8);
                }
                this.mRowContentBindStage.requestRebind(this.mEntry, (NotifBindPipeline.BindCallback) null);
            }
        }
    }

    public ExpandableNotificationRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mImageResolver = new NotificationInlineImageResolver(context, new NotificationInlineImageCache());
        initDimens();
    }

    public void initialize(NotificationEntry notificationEntry, RemoteInputViewSubcomponent.Factory factory, String str, String str2, ExpansionLogger expansionLogger, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, HeadsUpManager headsUpManager, RowContentBindStage rowContentBindStage, OnExpandClickListener onExpandClickListener, NotificationMediaManager notificationMediaManager, CoordinateOnClickListener coordinateOnClickListener, FalsingManager falsingManager, FalsingCollector falsingCollector, StatusBarStateController statusBarStateController, PeopleNotificationIdentifier peopleNotificationIdentifier, OnUserInteractionCallback onUserInteractionCallback, Optional<BubblesManager> optional, NotificationGutsManager notificationGutsManager, MetricsLogger metricsLogger, SmartReplyConstants smartReplyConstants, SmartReplyController smartReplyController) {
        GroupMembershipManager groupMembershipManager2 = groupMembershipManager;
        PeopleNotificationIdentifier peopleNotificationIdentifier2 = peopleNotificationIdentifier;
        this.mEntry = notificationEntry;
        this.mAppName = str;
        if (this.mMenuRow == null) {
            this.mMenuRow = new NotificationMenuRow(this.mContext, peopleNotificationIdentifier2);
        }
        if (this.mMenuRow.getMenuView() != null) {
            this.mMenuRow.setAppName(this.mAppName);
        }
        this.mLogger = expansionLogger;
        this.mLoggingKey = str2;
        this.mBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager2;
        this.mGroupExpansionManager = groupExpansionManager;
        this.mPrivateLayout.setGroupMembershipManager(groupMembershipManager2);
        this.mHeadsUpManager = headsUpManager;
        this.mRowContentBindStage = rowContentBindStage;
        this.mOnExpandClickListener = onExpandClickListener;
        this.mMediaManager = notificationMediaManager;
        setOnFeedbackClickListener(coordinateOnClickListener);
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mStatusBarStateController = statusBarStateController;
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier2;
        for (NotificationContentView initialize : this.mLayouts) {
            RemoteInputViewSubcomponent.Factory factory2 = factory;
            initialize.initialize(this.mPeopleNotificationIdentifier, factory, smartReplyConstants, smartReplyController);
        }
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mBubblesManagerOptional = optional;
        this.mNotificationGutsManager = notificationGutsManager;
        this.mMetricsLogger = metricsLogger;
    }

    public final void initDimens() {
        this.mMaxSmallHeightBeforeN = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height_legacy);
        this.mMaxSmallHeightBeforeP = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height_before_p);
        this.mMaxSmallHeightBeforeS = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height_before_s);
        this.mMaxSmallHeight = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height);
        this.mMaxSmallHeightLarge = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height_increased);
        this.mMaxSmallHeightMedia = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_min_height_media);
        this.mMaxExpandedHeight = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_height);
        this.mMaxHeadsUpHeightBeforeN = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_heads_up_height_legacy);
        this.mMaxHeadsUpHeightBeforeP = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_heads_up_height_before_p);
        this.mMaxHeadsUpHeightBeforeS = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_heads_up_height_before_s);
        this.mMaxHeadsUpHeight = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_heads_up_height);
        this.mMaxHeadsUpHeightIncreased = NotificationUtils.getFontScaledHeight(this.mContext, R$dimen.notification_max_heads_up_height_increased);
        Resources resources = getResources();
        this.mEnableNonGroupedNotificationExpand = resources.getBoolean(R$bool.config_enableNonGroupedNotificationExpand);
        this.mShowGroupBackgroundWhenExpanded = resources.getBoolean(R$bool.config_showGroupNotificationBgWhenExpanded);
    }

    public NotificationInlineImageResolver getImageResolver() {
        return this.mImageResolver;
    }

    public void reset() {
        this.mShowingPublicInitialized = false;
        unDismiss();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            resetTranslation();
        }
        onHeightReset();
        requestLayout();
        setTargetPoint((Point) null);
    }

    public void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.setFeedbackIcon(feedbackIcon);
        }
        this.mPrivateLayout.setFeedbackIcon(feedbackIcon);
        this.mPublicLayout.setFeedbackIcon(feedbackIcon);
    }

    public void setLastAudiblyAlertedMs(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        long j2 = RECENTLY_ALERTED_THRESHOLD_MS;
        boolean z = currentTimeMillis < j2;
        applyAudiblyAlertedRecently(z);
        removeCallbacks(this.mExpireRecentlyAlertedFlag);
        if (z) {
            postDelayed(this.mExpireRecentlyAlertedFlag, j2 - currentTimeMillis);
        }
    }

    @VisibleForTesting
    public void setEntry(NotificationEntry notificationEntry) {
        this.mEntry = notificationEntry;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        applyAudiblyAlertedRecently(false);
    }

    public final void applyAudiblyAlertedRecently(boolean z) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.setRecentlyAudiblyAlerted(z);
        }
        this.mPrivateLayout.setRecentlyAudiblyAlerted(z);
        this.mPublicLayout.setRecentlyAudiblyAlerted(z);
    }

    public View.OnClickListener getFeedbackOnClickListener() {
        return this.mOnFeedbackClickListener;
    }

    public void setOnFeedbackClickListener(CoordinateOnClickListener coordinateOnClickListener) {
        this.mOnFeedbackClickListener = new ExpandableNotificationRow$$ExternalSyntheticLambda7(this, coordinateOnClickListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnFeedbackClickListener$3(CoordinateOnClickListener coordinateOnClickListener, View view) {
        NotificationMenuRowPlugin.MenuItem feedbackMenuItem;
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        if (provider != null && (feedbackMenuItem = provider.getFeedbackMenuItem(this.mContext)) != null) {
            coordinateOnClickListener.onClick(this, view.getWidth() / 2, view.getHeight() / 2, feedbackMenuItem);
        }
    }

    public void onMeasure(int i, int i2) {
        Trace.beginSection(appendTraceStyleTag("ExpNotRow#onMeasure"));
        super.onMeasure(i, i2);
        Trace.endSection();
    }

    public final String appendTraceStyleTag(String str) {
        if (!Trace.isEnabled()) {
            return str;
        }
        Class notificationStyle = getEntry().getSbn().getNotification().getNotificationStyle();
        if (notificationStyle == null) {
            return str + "(nostyle)";
        }
        return str + "(" + notificationStyle.getSimpleName() + ")";
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPublicLayout = (NotificationContentView) findViewById(R$id.expandedPublic);
        NotificationContentView notificationContentView = (NotificationContentView) findViewById(R$id.expanded);
        this.mPrivateLayout = notificationContentView;
        NotificationContentView[] notificationContentViewArr = {notificationContentView, this.mPublicLayout};
        this.mLayouts = notificationContentViewArr;
        for (NotificationContentView notificationContentView2 : notificationContentViewArr) {
            notificationContentView2.setExpandClickListener(this.mExpandClickListener);
            notificationContentView2.setContainingNotification(this);
        }
        ViewStub viewStub = (ViewStub) findViewById(R$id.notification_guts_stub);
        this.mGutsStub = viewStub;
        viewStub.setOnInflateListener(new ExpandableNotificationRow$$ExternalSyntheticLambda1(this));
        ViewStub viewStub2 = (ViewStub) findViewById(R$id.child_container_stub);
        this.mChildrenContainerStub = viewStub2;
        viewStub2.setOnInflateListener(new ExpandableNotificationRow$$ExternalSyntheticLambda2(this));
        this.mTranslateableViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            this.mTranslateableViews.add(getChildAt(i));
        }
        this.mTranslateableViews.remove(this.mChildrenContainerStub);
        this.mTranslateableViews.remove(this.mGutsStub);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$4(ViewStub viewStub, View view) {
        NotificationGuts notificationGuts = (NotificationGuts) view;
        this.mGuts = notificationGuts;
        notificationGuts.setClipTopAmount(getClipTopAmount());
        this.mGuts.setActualHeight(getActualHeight());
        this.mGutsStub = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$5(ViewStub viewStub, View view) {
        NotificationChildrenContainer notificationChildrenContainer = (NotificationChildrenContainer) view;
        this.mChildrenContainer = notificationChildrenContainer;
        notificationChildrenContainer.setIsLowPriority(this.mIsLowPriority);
        this.mChildrenContainer.setContainingNotification(this);
        this.mChildrenContainer.onNotificationUpdated();
        this.mTranslateableViews.add(this.mChildrenContainer);
    }

    public void doDragCallback(float f, float f2) {
        if (this.mDragController != null) {
            setTargetPoint(new Point((int) f, (int) f2));
            this.mDragController.startDragAndDrop(this);
        }
    }

    public void setOnDragSuccessListener(OnDragSuccessListener onDragSuccessListener) {
        this.mOnDragSuccessListener = onDragSuccessListener;
    }

    public void dragAndDropSuccess() {
        OnDragSuccessListener onDragSuccessListener = this.mOnDragSuccessListener;
        if (onDragSuccessListener != null) {
            onDragSuccessListener.onDragSuccess(getEntry());
        }
    }

    public final void doLongClickCallback() {
        doLongClickCallback(getWidth() / 2, getHeight() / 2);
    }

    public void doLongClickCallback(int i, int i2) {
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        doLongClickCallback(i, i2, provider != null ? provider.getLongpressMenuItem(this.mContext) : null);
    }

    public void doSmartActionClick(int i, int i2, int i3) {
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        NotificationMenuRowPlugin.MenuItem longpressMenuItem = provider != null ? provider.getLongpressMenuItem(this.mContext) : null;
        if (11 == i3 && (longpressMenuItem.getGutsView() instanceof NotificationConversationInfo)) {
            ((NotificationConversationInfo) longpressMenuItem.getGutsView()).setSelectedAction(2);
        }
        doLongClickCallback(i, i2, longpressMenuItem);
    }

    public final void doLongClickCallback(int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        LongPressListener longPressListener = this.mLongPressListener;
        if (longPressListener != null && menuItem != null) {
            longPressListener.onLongPress(this, i, i2, menuItem);
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyDown(i, keyEvent);
        }
        keyEvent.startTracking();
        return true;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyUp(i, keyEvent);
        }
        if (keyEvent.isCanceled()) {
            return true;
        }
        performClick();
        return true;
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return false;
        }
        doLongClickCallback();
        return true;
    }

    public void resetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(0.0f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                this.mTranslateableViews.get(i).setTranslationX(0.0f);
            }
            invalidateOutline();
            getEntry().getIcons().getShelfIcon().setScrollX(0);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.resetMenu();
        }
    }

    public void onGutsOpened() {
        resetTranslation();
        updateContentAccessibilityImportanceForGuts(false);
    }

    public void onGutsClosed() {
        updateContentAccessibilityImportanceForGuts(true);
        this.mIsSnoozed = false;
    }

    public final void updateContentAccessibilityImportanceForGuts(boolean z) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            updateChildAccessibilityImportance(notificationChildrenContainer, z);
        }
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        if (notificationContentViewArr != null) {
            for (NotificationContentView updateChildAccessibilityImportance : notificationContentViewArr) {
                updateChildAccessibilityImportance(updateChildAccessibilityImportance, z);
            }
        }
        if (z) {
            requestAccessibilityFocus();
        }
    }

    public final void updateChildAccessibilityImportance(View view, boolean z) {
        view.setImportantForAccessibility(z ? 0 : 4);
    }

    public CharSequence getActiveRemoteInputText() {
        return this.mPrivateLayout.getActiveRemoteInputText();
    }

    public void animateResetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        Animator translateViewAnimator = getTranslateViewAnimator(0.0f, (ValueAnimator.AnimatorUpdateListener) null);
        this.mTranslateAnim = translateViewAnimator;
        if (translateViewAnimator != null) {
            translateViewAnimator.start();
        }
    }

    public void setDismissUsingRowTranslationX(boolean z) {
        if (z != this.mDismissUsingRowTranslationX) {
            float translation = getTranslation();
            int i = (translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1));
            if (i != 0) {
                setTranslation(0.0f);
            }
            super.setDismissUsingRowTranslationX(z);
            if (i != 0) {
                setTranslation(translation);
            }
        }
    }

    public void setTranslation(float f) {
        invalidate();
        if (isBlockingHelperShowingAndTranslationFinished()) {
            this.mGuts.setTranslationX(f);
            return;
        }
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                if (this.mTranslateableViews.get(i) != null) {
                    this.mTranslateableViews.get(i).setTranslationX(f);
                }
            }
            invalidateOutline();
            getEntry().getIcons().getShelfIcon().setScrollX((int) (-f));
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null && notificationMenuRowPlugin.getMenuView() != null) {
            this.mMenuRow.onParentTranslationUpdate(f);
        }
    }

    public float getTranslation() {
        if (this.mDismissUsingRowTranslationX) {
            return getTranslationX();
        }
        if (isBlockingHelperShowingAndCanTranslate()) {
            return this.mGuts.getTranslationX();
        }
        ArrayList<View> arrayList = this.mTranslateableViews;
        if (arrayList == null || arrayList.size() <= 0) {
            return 0.0f;
        }
        return this.mTranslateableViews.get(0).getTranslationX();
    }

    public final boolean isBlockingHelperShowingAndCanTranslate() {
        return areGutsExposed() && this.mIsBlockingHelperShowing && this.mNotificationTranslationFinished;
    }

    public Animator getTranslateViewAnimator(final float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, TRANSLATE_CONTENT, new float[]{f});
        if (animatorUpdateListener != null) {
            ofFloat.addUpdateListener(animatorUpdateListener);
        }
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public boolean cancelled = false;

            public void onAnimationCancel(Animator animator) {
                this.cancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (ExpandableNotificationRow.this.mIsBlockingHelperShowing) {
                    ExpandableNotificationRow.this.mNotificationTranslationFinished = true;
                }
                if (!this.cancelled && f == 0.0f) {
                    if (ExpandableNotificationRow.this.mMenuRow != null) {
                        ExpandableNotificationRow.this.mMenuRow.resetMenu();
                    }
                    ExpandableNotificationRow.this.mTranslateAnim = null;
                }
            }
        });
        this.mTranslateAnim = ofFloat;
        return ofFloat;
    }

    public void ensureGutsInflated() {
        if (this.mGuts == null) {
            this.mGutsStub.inflate();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        r0 = r5.mGuts;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateChildrenVisibility() {
        /*
            r5 = this;
            boolean r0 = r5.mExpandAnimationRunning
            r1 = 0
            if (r0 == 0) goto L_0x0011
            com.android.systemui.statusbar.notification.row.NotificationGuts r0 = r5.mGuts
            if (r0 == 0) goto L_0x0011
            boolean r0 = r0.isExposed()
            if (r0 == 0) goto L_0x0011
            r0 = 1
            goto L_0x0012
        L_0x0011:
            r0 = r1
        L_0x0012:
            com.android.systemui.statusbar.notification.row.NotificationContentView r2 = r5.mPrivateLayout
            boolean r3 = r5.mShowingPublic
            r4 = 4
            if (r3 != 0) goto L_0x0021
            boolean r3 = r5.mIsSummaryWithChildren
            if (r3 != 0) goto L_0x0021
            if (r0 != 0) goto L_0x0021
            r3 = r1
            goto L_0x0022
        L_0x0021:
            r3 = r4
        L_0x0022:
            r2.setVisibility(r3)
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r2 = r5.mChildrenContainer
            if (r2 == 0) goto L_0x0038
            boolean r3 = r5.mShowingPublic
            if (r3 != 0) goto L_0x0034
            boolean r3 = r5.mIsSummaryWithChildren
            if (r3 == 0) goto L_0x0034
            if (r0 != 0) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r1 = r4
        L_0x0035:
            r2.setVisibility(r1)
        L_0x0038:
            r5.updateLimits()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.updateChildrenVisibility():void");
    }

    public boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    public void applyLaunchAnimationParams(LaunchAnimationParameters launchAnimationParameters) {
        int i;
        if (launchAnimationParameters != null) {
            if (launchAnimationParameters.getVisible()) {
                Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
                float lerp = MathUtils.lerp(launchAnimationParameters.getStartTranslationZ(), (float) this.mNotificationLaunchHeight, interpolator.getInterpolation(launchAnimationParameters.getProgress(0, 50)));
                setTranslationZ(lerp);
                float width = (float) (launchAnimationParameters.getWidth() - getWidth());
                setExtraWidthForClipping(width);
                if (launchAnimationParameters.getStartRoundedTopClipping() > 0) {
                    float interpolation = interpolator.getInterpolation(launchAnimationParameters.getProgress(0, 100));
                    float startNotificationTop = launchAnimationParameters.getStartNotificationTop();
                    i = (int) Math.min(MathUtils.lerp(startNotificationTop, (float) launchAnimationParameters.getTop(), interpolation), startNotificationTop);
                } else {
                    i = launchAnimationParameters.getTop();
                }
                int bottom = launchAnimationParameters.getBottom() - i;
                setActualHeight(bottom);
                int startClipTopAmount = launchAnimationParameters.getStartClipTopAmount();
                int lerp2 = (int) MathUtils.lerp(startClipTopAmount, 0, launchAnimationParameters.getProgress());
                ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
                if (expandableNotificationRow != null) {
                    float translationY = expandableNotificationRow.getTranslationY();
                    i = (int) (((float) i) - translationY);
                    this.mNotificationParent.setTranslationZ(lerp);
                    this.mNotificationParent.setClipTopAmount(Math.min(launchAnimationParameters.getParentStartClipTopAmount(), lerp2 + i));
                    this.mNotificationParent.setExtraWidthForClipping(width);
                    this.mNotificationParent.setMinimumHeightForClipping((int) (Math.max((float) launchAnimationParameters.getBottom(), (((float) this.mNotificationParent.getActualHeight()) + translationY) - ((float) this.mNotificationParent.getClipBottomAmount())) - Math.min((float) launchAnimationParameters.getTop(), translationY)));
                } else if (startClipTopAmount != 0) {
                    setClipTopAmount(lerp2);
                }
                setTranslationY((float) i);
                this.mTopRoundnessDuringLaunchAnimation = launchAnimationParameters.getTopCornerRadius() / this.mOutlineRadius;
                this.mBottomRoundnessDuringLaunchAnimation = launchAnimationParameters.getBottomCornerRadius() / this.mOutlineRadius;
                invalidateOutline();
                this.mBackgroundNormal.setExpandAnimationSize(launchAnimationParameters.getWidth(), bottom);
            } else if (getVisibility() == 0) {
                setVisibility(4);
            }
        }
    }

    public float getCurrentTopRoundness() {
        if (this.mExpandAnimationRunning) {
            return this.mTopRoundnessDuringLaunchAnimation;
        }
        return super.getCurrentTopRoundness();
    }

    public float getCurrentBottomRoundness() {
        if (this.mExpandAnimationRunning) {
            return this.mBottomRoundnessDuringLaunchAnimation;
        }
        return super.getCurrentBottomRoundness();
    }

    public void setExpandAnimationRunning(boolean z) {
        if (z) {
            setAboveShelf(true);
            this.mExpandAnimationRunning = true;
            getViewState().cancelAnimations(this);
            this.mNotificationLaunchHeight = AmbientState.getNotificationLaunchHeight(getContext());
        } else {
            this.mExpandAnimationRunning = false;
            setAboveShelf(isAboveShelf());
            setVisibility(0);
            NotificationGuts notificationGuts = this.mGuts;
            if (notificationGuts != null) {
                notificationGuts.setAlpha(1.0f);
            }
            resetAllContentAlphas();
            setExtraWidthForClipping(0.0f);
            ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.setExtraWidthForClipping(0.0f);
                this.mNotificationParent.setMinimumHeightForClipping(0);
            }
        }
        ExpandableNotificationRow expandableNotificationRow2 = this.mNotificationParent;
        if (expandableNotificationRow2 != null) {
            expandableNotificationRow2.setChildIsExpanding(this.mExpandAnimationRunning);
        }
        updateChildrenVisibility();
        updateClipping();
        this.mBackgroundNormal.setExpandAnimationRunning(z);
    }

    public final void setChildIsExpanding(boolean z) {
        this.mChildIsExpanding = z;
        updateClipping();
        invalidate();
    }

    public boolean hasExpandingChild() {
        return this.mChildIsExpanding;
    }

    public StatusBarIconView getShelfIcon() {
        return getEntry().getIcons().getShelfIcon();
    }

    public boolean shouldClipToActualHeight() {
        return super.shouldClipToActualHeight() && !this.mExpandAnimationRunning;
    }

    public boolean isExpandAnimationRunning() {
        return this.mExpandAnimationRunning;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r0 = r3.mSecureStateProvider;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSoundEffectsEnabled() {
        /*
            r3 = this;
            com.android.systemui.plugins.statusbar.StatusBarStateController r0 = r3.mStatusBarStateController
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0018
            boolean r0 = r0.isDozing()
            if (r0 == 0) goto L_0x0018
            java.util.function.BooleanSupplier r0 = r3.mSecureStateProvider
            if (r0 == 0) goto L_0x0018
            boolean r0 = r0.getAsBoolean()
            if (r0 != 0) goto L_0x0018
            r0 = r1
            goto L_0x0019
        L_0x0018:
            r0 = r2
        L_0x0019:
            if (r0 != 0) goto L_0x0022
            boolean r3 = super.isSoundEffectsEnabled()
            if (r3 == 0) goto L_0x0022
            goto L_0x0023
        L_0x0022:
            r1 = r2
        L_0x0023:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.isSoundEffectsEnabled():boolean");
    }

    public boolean isExpandable() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return !this.mChildrenExpanded;
        }
        if (!this.mEnableNonGroupedNotificationExpand || !this.mExpandable) {
            return false;
        }
        return true;
    }

    public void setExpandable(boolean z) {
        this.mExpandable = z;
        this.mPrivateLayout.updateExpandButtons(isExpandable());
    }

    public void setClipToActualHeight(boolean z) {
        boolean z2 = false;
        super.setClipToActualHeight(z || isUserLocked());
        NotificationContentView showingLayout = getShowingLayout();
        if (z || isUserLocked()) {
            z2 = true;
        }
        showingLayout.setClipToActualHeight(z2);
    }

    public boolean hasUserChangedExpansion() {
        return this.mHasUserChangedExpansion;
    }

    public boolean isUserExpanded() {
        return this.mUserExpanded;
    }

    public void setUserExpanded(boolean z) {
        setUserExpanded(z, false);
    }

    public void setUserExpanded(boolean z, boolean z2) {
        this.mFalsingCollector.setNotificationExpanded();
        if (this.mIsSummaryWithChildren && !shouldShowPublic() && z2 && !this.mChildrenContainer.showingAsLowPriority()) {
            boolean isGroupExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
            this.mGroupExpansionManager.setGroupExpanded(this.mEntry, z);
            onExpansionChanged(true, isGroupExpanded);
        } else if (!z || this.mExpandable) {
            boolean isExpanded = isExpanded();
            this.mHasUserChangedExpansion = true;
            this.mUserExpanded = z;
            onExpansionChanged(true, isExpanded);
            if (!isExpanded && isExpanded() && getActualHeight() != getIntrinsicHeight()) {
                notifyHeightChanged(true);
            }
        }
    }

    public void resetUserExpansion() {
        boolean isExpanded = isExpanded();
        this.mHasUserChangedExpansion = false;
        this.mUserExpanded = false;
        if (isExpanded != isExpanded()) {
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.onExpansionChanged();
            }
            notifyHeightChanged(false);
        }
        updateShelfIconColor();
    }

    public boolean isUserLocked() {
        return this.mUserLocked;
    }

    public void setUserLocked(boolean z) {
        this.mUserLocked = z;
        this.mPrivateLayout.setUserExpanding(z);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setUserLocked(z);
            if (!this.mIsSummaryWithChildren) {
                return;
            }
            if (z || !isGroupExpanded()) {
                updateBackgroundForGroupState();
            }
        }
    }

    public boolean isSystemExpanded() {
        return this.mIsSystemExpanded;
    }

    public void setSystemExpanded(boolean z) {
        if (z != this.mIsSystemExpanded) {
            boolean isExpanded = isExpanded();
            this.mIsSystemExpanded = z;
            notifyHeightChanged(false);
            onExpansionChanged(false, isExpanded);
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.updateGroupOverflow();
                resetChildSystemExpandedStates();
            }
        }
    }

    public void setOnKeyguard(boolean z) {
        if (z != this.mOnKeyguard) {
            boolean isAboveShelf = isAboveShelf();
            boolean isExpanded = isExpanded();
            this.mOnKeyguard = z;
            onExpansionChanged(false, isExpanded);
            if (isExpanded != isExpanded()) {
                if (this.mIsSummaryWithChildren) {
                    this.mChildrenContainer.updateGroupOverflow();
                }
                notifyHeightChanged(false);
            }
            if (isAboveShelf() != isAboveShelf) {
                this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
            }
        }
        updateRippleAllowed();
    }

    public final void updateRippleAllowed() {
        setRippleAllowed(isOnKeyguard() || this.mEntry.getSbn().getNotification().contentIntent == null);
    }

    public void onTap() {
        if (this.mEntry.getSbn().getNotification().contentIntent != null) {
            setRippleAllowed(false);
        }
    }

    public boolean performClick() {
        updateRippleAllowed();
        return super.performClick();
    }

    public int getIntrinsicHeight() {
        if (isUserLocked()) {
            return getActualHeight();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null && notificationGuts.isExposed()) {
            return this.mGuts.getIntrinsicHeight();
        }
        if (isChildInGroup() && !isGroupExpanded()) {
            return this.mPrivateLayout.getMinHeight();
        }
        if (this.mSensitive && this.mHideSensitiveForIntrinsicHeight) {
            return getMinHeight();
        }
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getIntrinsicHeight();
        }
        if (!canShowHeadsUp() || !isHeadsUpState()) {
            if (isExpanded()) {
                return getMaxExpandHeight();
            }
            return getCollapsedHeight();
        } else if (isPinned() || this.mHeadsupDisappearRunning) {
            return getPinnedHeadsUpHeight(true);
        } else {
            if (isExpanded()) {
                return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
            }
            return Math.max(getCollapsedHeight(), getHeadsUpHeight());
        }
    }

    public boolean canShowHeadsUp() {
        return !this.mOnKeyguard || isDozing() || isBypassEnabled();
    }

    public final boolean isBypassEnabled() {
        KeyguardBypassController keyguardBypassController = this.mBypassController;
        return keyguardBypassController == null || keyguardBypassController.getBypassEnabled();
    }

    public final boolean isDozing() {
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        return statusBarStateController != null && statusBarStateController.isDozing();
    }

    public boolean isGroupExpanded() {
        return this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
    }

    public final void onAttachedChildrenCountChanged() {
        NotificationViewWrapper notificationViewWrapper;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        boolean z = notificationChildrenContainer != null && notificationChildrenContainer.getNotificationChildCount() > 0;
        this.mIsSummaryWithChildren = z;
        if (z && ((notificationViewWrapper = this.mChildrenContainer.getNotificationViewWrapper()) == null || notificationViewWrapper.getNotificationHeader() == null)) {
            this.mChildrenContainer.recreateNotificationHeader(this.mExpandClickListener, isConversation());
        }
        getShowingLayout().updateBackgroundColor(false);
        this.mPrivateLayout.updateExpandButtons(isExpandable());
        updateChildrenAppearance();
        updateChildrenVisibility();
        applyChildrenRoundness();
    }

    public void expandNotification() {
        this.mExpandClickListener.onClick(this);
    }

    public ArraySet<NotificationChannel> getUniqueChannels() {
        ArraySet<NotificationChannel> arraySet = new ArraySet<>();
        arraySet.add(this.mEntry.getChannel());
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = getAttachedChildren();
            int size = attachedChildren.size();
            for (int i = 0; i < size; i++) {
                ExpandableNotificationRow expandableNotificationRow = attachedChildren.get(i);
                NotificationChannel channel = expandableNotificationRow.getEntry().getChannel();
                StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
                if (sbn.getUser().equals(this.mEntry.getSbn().getUser()) && sbn.getPackageName().equals(this.mEntry.getSbn().getPackageName())) {
                    arraySet.add(channel);
                }
            }
        }
        return arraySet;
    }

    public void updateChildrenAppearance() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateChildrenAppearance();
        }
    }

    public boolean isExpanded() {
        return isExpanded(false);
    }

    public boolean isExpanded(boolean z) {
        return (!this.mOnKeyguard || z) && ((!hasUserChangedExpansion() && (isSystemExpanded() || isSystemChildExpanded())) || isUserExpanded());
    }

    public final boolean isSystemChildExpanded() {
        return this.mIsSystemChildExpanded;
    }

    public void setSystemChildExpanded(boolean z) {
        this.mIsSystemChildExpanded = z;
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.mLayoutListener = layoutListener;
    }

    public void removeListener() {
        this.mLayoutListener = null;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Trace.beginSection(appendTraceStyleTag("ExpNotRow#onLayout"));
        int intrinsicHeight = getIntrinsicHeight();
        super.onLayout(z, i, i2, i3, i4);
        if (intrinsicHeight != getIntrinsicHeight() && (intrinsicHeight != 0 || getActualHeight() > 0)) {
            notifyHeightChanged(true);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onParentHeightUpdate();
        }
        updateContentShiftHeight();
        LayoutListener layoutListener = this.mLayoutListener;
        if (layoutListener != null) {
            layoutListener.onLayout();
        }
        Trace.endSection();
    }

    public final void updateContentShiftHeight() {
        View view;
        NotificationViewWrapper visibleNotificationViewWrapper = getVisibleNotificationViewWrapper();
        if (visibleNotificationViewWrapper == null) {
            view = null;
        } else {
            view = visibleNotificationViewWrapper.getIcon();
        }
        if (view != null) {
            this.mIconTransformContentShift = getRelativeTopPadding(view) + view.getHeight();
        } else {
            this.mIconTransformContentShift = this.mContentShift;
        }
    }

    public float getContentTransformationShift() {
        return (float) this.mIconTransformContentShift;
    }

    public void notifyHeightChanged(boolean z) {
        super.notifyHeightChanged(z);
        getShowingLayout().requestSelectLayout(z || isUserLocked());
    }

    public void setSensitive(boolean z, boolean z2) {
        int intrinsicHeight = getIntrinsicHeight();
        this.mSensitive = z;
        this.mSensitiveHiddenInGeneral = z2;
        if (intrinsicHeight != getIntrinsicHeight()) {
            notifyHeightChanged(false);
        }
    }

    public void setHideSensitiveForIntrinsicHeight(boolean z) {
        this.mHideSensitiveForIntrinsicHeight = z;
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setHideSensitiveForIntrinsicHeight(z);
            }
        }
    }

    public void setHideSensitive(boolean z, boolean z2, long j, long j2) {
        if (getVisibility() != 8) {
            boolean z3 = this.mShowingPublic;
            int i = 0;
            boolean z4 = this.mSensitive && z;
            this.mShowingPublic = z4;
            if (!this.mShowingPublicInitialized || z4 != z3) {
                if (!z2) {
                    this.mPublicLayout.animate().cancel();
                    this.mPrivateLayout.animate().cancel();
                    NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                    if (notificationChildrenContainer != null) {
                        notificationChildrenContainer.animate().cancel();
                    }
                    resetAllContentAlphas();
                    NotificationContentView notificationContentView = this.mPublicLayout;
                    if (!this.mShowingPublic) {
                        i = 4;
                    }
                    notificationContentView.setVisibility(i);
                    updateChildrenVisibility();
                } else {
                    animateShowingPublic(j, j2, z4);
                }
                getShowingLayout().updateBackgroundColor(z2);
                this.mPrivateLayout.updateExpandButtons(isExpandable());
                updateShelfIconColor();
                this.mShowingPublicInitialized = true;
            }
        }
    }

    public final void animateShowingPublic(long j, long j2, boolean z) {
        View[] viewArr = this.mIsSummaryWithChildren ? new View[]{this.mChildrenContainer} : new View[]{this.mPrivateLayout};
        View[] viewArr2 = {this.mPublicLayout};
        View[] viewArr3 = z ? viewArr : viewArr2;
        if (z) {
            viewArr = viewArr2;
        }
        for (View view : viewArr3) {
            view.setVisibility(0);
            view.animate().cancel();
            view.animate().alpha(0.0f).setStartDelay(j).setDuration(j2).withEndAction(new ExpandableNotificationRow$$ExternalSyntheticLambda4(this, view));
        }
        for (View view2 : viewArr) {
            view2.setVisibility(0);
            view2.setAlpha(0.0f);
            view2.animate().cancel();
            view2.animate().alpha(1.0f).setStartDelay(j).setDuration(j2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateShowingPublic$6(View view) {
        view.setVisibility(4);
        resetAllContentAlphas();
    }

    public boolean mustStayOnScreen() {
        return this.mIsHeadsUp && this.mMustStayOnScreen;
    }

    public boolean canViewBeDismissed() {
        return this.mEntry.isDismissable() && (!shouldShowPublic() || !this.mSensitiveHiddenInGeneral);
    }

    public boolean canViewBeCleared() {
        return this.mEntry.isClearable() && (!shouldShowPublic() || !this.mSensitiveHiddenInGeneral);
    }

    public final boolean shouldShowPublic() {
        return this.mSensitive && this.mHideSensitiveForIntrinsicHeight;
    }

    public void makeActionsVisibile() {
        setUserExpanded(true, true);
        if (isChildInGroup()) {
            this.mGroupExpansionManager.setGroupExpanded(this.mEntry, true);
        }
        notifyHeightChanged(false);
    }

    public void setChildrenExpanded(boolean z, boolean z2) {
        this.mChildrenExpanded = z;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setChildrenExpanded(z);
        }
        updateBackgroundForGroupState();
        updateClickAndFocus();
    }

    public int getMaxExpandHeight() {
        return this.mPrivateLayout.getExpandHeight();
    }

    public final int getHeadsUpHeight() {
        return getShowingLayout().getHeadsUpHeight(false);
    }

    public boolean areGutsExposed() {
        NotificationGuts notificationGuts = this.mGuts;
        return notificationGuts != null && notificationGuts.isExposed();
    }

    public boolean isContentExpandable() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().isContentExpandable();
        }
        return true;
    }

    public View getContentView() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout();
        }
        return this.mChildrenContainer;
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        Animator translateViewAnimator;
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible() || (translateViewAnimator = getTranslateViewAnimator(0.0f, (ValueAnimator.AnimatorUpdateListener) null)) == null) {
            return super.performRemoveAnimation(j, j2, f, z, f2, runnable, animatorListenerAdapter);
        }
        final long j3 = j;
        final long j4 = j2;
        final float f3 = f;
        final boolean z2 = z;
        final float f4 = f2;
        final Runnable runnable2 = runnable;
        final AnimatorListenerAdapter animatorListenerAdapter2 = animatorListenerAdapter;
        translateViewAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                long unused = ExpandableNotificationRow.super.performRemoveAnimation(j3, j4, f3, z2, f4, runnable2, animatorListenerAdapter2);
            }
        });
        translateViewAnimator.start();
        return translateViewAnimator.getDuration();
    }

    public void onAppearAnimationFinished(boolean z) {
        super.onAppearAnimationFinished(z);
        if (z) {
            resetAllContentAlphas();
            setNotificationFaded(false);
            return;
        }
        setHeadsUpAnimatingAway(false);
    }

    public void resetAllContentAlphas() {
        this.mPrivateLayout.setAlpha(1.0f);
        this.mPrivateLayout.setLayerType(0, (Paint) null);
        this.mPublicLayout.setAlpha(1.0f);
        this.mPublicLayout.setLayerType(0, (Paint) null);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setAlpha(1.0f);
            this.mChildrenContainer.setLayerType(0, (Paint) null);
        }
    }

    public boolean isNotificationFaded() {
        return this.mIsFaded;
    }

    public void setNotificationFaded(boolean z) {
        this.mIsFaded = z;
        if (childrenRequireOverlappingRendering()) {
            NotificationFadeAware.setLayerTypeForFaded(this, z);
            setNotificationFadedOnChildren(false);
            return;
        }
        NotificationFadeAware.setLayerTypeForFaded(this, false);
        setNotificationFadedOnChildren(z);
    }

    public final void setNotificationFadedOnChildren(boolean z) {
        delegateNotificationFaded(this.mChildrenContainer, z);
        for (NotificationContentView delegateNotificationFaded : this.mLayouts) {
            delegateNotificationFaded(delegateNotificationFaded, z);
        }
    }

    public static void delegateNotificationFaded(View view, boolean z) {
        if (view instanceof NotificationFadeAware) {
            ((NotificationFadeAware) view).setNotificationFaded(z);
        } else {
            NotificationFadeAware.setLayerTypeForFaded(view, z);
        }
    }

    public boolean hasOverlappingRendering() {
        return super.hasOverlappingRendering() && childrenRequireOverlappingRendering();
    }

    public final boolean childrenRequireOverlappingRendering() {
        if (getEntry().getSbn().getNotification().isColorized()) {
            return true;
        }
        NotificationContentView showingLayout = getShowingLayout();
        if (showingLayout == null || !showingLayout.requireRowToHaveOverlappingRendering()) {
            return false;
        }
        return true;
    }

    public int getExtraBottomPadding() {
        if (!this.mIsSummaryWithChildren || !isGroupExpanded()) {
            return 0;
        }
        return this.mIncreasedPaddingBetweenElements;
    }

    public void setActualHeight(int i, boolean z) {
        ViewGroup viewGroup;
        boolean z2 = i != getActualHeight();
        super.setActualHeight(i, z);
        if (z2 && isRemoved() && (viewGroup = (ViewGroup) getParent()) != null) {
            viewGroup.invalidate();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts == null || !notificationGuts.isExposed()) {
            int max = Math.max(getMinHeight(), i);
            for (NotificationContentView contentHeight : this.mLayouts) {
                contentHeight.setContentHeight(max);
            }
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.setActualHeight(i);
            }
            NotificationGuts notificationGuts2 = this.mGuts;
            if (notificationGuts2 != null) {
                notificationGuts2.setActualHeight(i);
            }
            NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
            if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
                this.mMenuRow.onParentHeightUpdate();
            }
            handleIntrinsicHeightReached();
            return;
        }
        this.mGuts.setActualHeight(i);
    }

    public int getMaxContentHeight() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getMaxHeight();
        }
        return this.mChildrenContainer.getMaxContentHeight();
    }

    public int getMinHeight(boolean z) {
        NotificationGuts notificationGuts;
        if (!z && (notificationGuts = this.mGuts) != null && notificationGuts.isExposed()) {
            return this.mGuts.getIntrinsicHeight();
        }
        if (!z && canShowHeadsUp() && this.mIsHeadsUp && this.mHeadsUpManager.isTrackingHeadsUp()) {
            return getPinnedHeadsUpHeight(false);
        }
        if (this.mIsSummaryWithChildren && !isGroupExpanded() && !shouldShowPublic()) {
            return this.mChildrenContainer.getMinHeight();
        }
        if (z || !canShowHeadsUp() || !this.mIsHeadsUp) {
            return getShowingLayout().getMinHeight();
        }
        return getHeadsUpHeight();
    }

    public int getCollapsedHeight() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getMinHeight();
        }
        return this.mChildrenContainer.getCollapsedHeight();
    }

    public int getHeadsUpHeightWithoutHeader() {
        if (!canShowHeadsUp() || !this.mIsHeadsUp) {
            return getCollapsedHeight();
        }
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getHeadsUpHeight(true);
        }
        return this.mChildrenContainer.getCollapsedHeightWithoutHeader();
    }

    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        for (NotificationContentView clipTopAmount : this.mLayouts) {
            clipTopAmount.setClipTopAmount(i);
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            notificationGuts.setClipTopAmount(i);
        }
    }

    public void setClipBottomAmount(int i) {
        if (!this.mExpandAnimationRunning) {
            if (i != this.mClipBottomAmount) {
                super.setClipBottomAmount(i);
                for (NotificationContentView clipBottomAmount : this.mLayouts) {
                    clipBottomAmount.setClipBottomAmount(i);
                }
                NotificationGuts notificationGuts = this.mGuts;
                if (notificationGuts != null) {
                    notificationGuts.setClipBottomAmount(i);
                }
            }
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (notificationChildrenContainer != null && !this.mChildIsExpanding) {
                notificationChildrenContainer.setClipBottomAmount(i);
            }
        }
    }

    public NotificationContentView getShowingLayout() {
        return shouldShowPublic() ? this.mPublicLayout : this.mPrivateLayout;
    }

    public void setLegacy(boolean z) {
        for (NotificationContentView legacy : this.mLayouts) {
            legacy.setLegacy(z);
        }
    }

    public void updateBackgroundTint() {
        super.updateBackgroundTint();
        updateBackgroundForGroupState();
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).updateBackgroundForGroupState();
            }
        }
    }

    public void onFinishedExpansionChange() {
        this.mGroupExpansionChanging = false;
        updateBackgroundForGroupState();
    }

    public void updateBackgroundForGroupState() {
        boolean z = true;
        int i = 0;
        if (this.mIsSummaryWithChildren) {
            if (this.mShowGroupBackgroundWhenExpanded || !isGroupExpanded() || isGroupExpansionChanging() || isUserLocked()) {
                z = false;
            }
            this.mShowNoBackground = z;
            this.mChildrenContainer.updateHeaderForExpansion(z);
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            while (i < attachedChildren.size()) {
                attachedChildren.get(i).updateBackgroundForGroupState();
                i++;
            }
        } else if (isChildInGroup()) {
            int backgroundColorForExpansionState = getShowingLayout().getBackgroundColorForExpansionState();
            if (isGroupExpanded() || ((this.mNotificationParent.isGroupExpansionChanging() || this.mNotificationParent.isUserLocked()) && backgroundColorForExpansionState != 0)) {
                i = 1;
            }
            this.mShowNoBackground = i ^ true;
        } else {
            this.mShowNoBackground = false;
        }
        updateOutline();
        updateBackground();
    }

    public boolean hideBackground() {
        return this.mShowNoBackground || super.hideBackground();
    }

    public int getPositionOfChild(ExpandableNotificationRow expandableNotificationRow) {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getPositionInLinearLayout(expandableNotificationRow);
        }
        return 0;
    }

    public void onExpandedByGesture(boolean z) {
        this.mMetricsLogger.action(this.mGroupMembershipManager.isGroupSummary(this.mEntry) ? 410 : 409, z);
    }

    public final void onExpansionChanged(boolean z, boolean z2) {
        boolean isExpanded = isExpanded();
        if (this.mIsSummaryWithChildren && (!this.mIsLowPriority || z2)) {
            isExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
        }
        if (isExpanded != z2) {
            updateShelfIconColor();
            ExpansionLogger expansionLogger = this.mLogger;
            if (expansionLogger != null) {
                expansionLogger.logNotificationExpansion(this.mLoggingKey, z, isExpanded);
            }
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.onExpansionChanged();
            }
            OnExpansionChangedListener onExpansionChangedListener = this.mExpansionChangedListener;
            if (onExpansionChangedListener != null) {
                onExpansionChangedListener.onExpansionChanged(isExpanded);
            }
        }
    }

    public void setOnExpansionChangedListener(OnExpansionChangedListener onExpansionChangedListener) {
        this.mExpansionChangedListener = onExpansionChangedListener;
    }

    public void performOnIntrinsicHeightReached(Runnable runnable) {
        this.mOnIntrinsicHeightReachedRunnable = runnable;
        handleIntrinsicHeightReached();
    }

    public final void handleIntrinsicHeightReached() {
        if (this.mOnIntrinsicHeightReachedRunnable != null && getActualHeight() == getIntrinsicHeight()) {
            this.mOnIntrinsicHeightReachedRunnable.run();
            this.mOnIntrinsicHeightReachedRunnable = null;
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
        if (canViewBeDismissed() && !this.mIsSnoozed) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        }
        boolean shouldShowPublic = shouldShowPublic();
        boolean z = false;
        if (!shouldShowPublic) {
            if (this.mIsSummaryWithChildren) {
                shouldShowPublic = true;
                if (!this.mIsLowPriority || isExpanded()) {
                    z = isGroupExpanded();
                }
            } else {
                shouldShowPublic = this.mPrivateLayout.isContentExpandable();
                z = isExpanded();
            }
        }
        if (shouldShowPublic && !this.mIsSnoozed) {
            if (z) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
            } else {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            }
        }
        NotificationMenuRowPlugin provider = getProvider();
        if (provider != null && provider.getSnoozeMenuItem(getContext()) != null) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.action_snooze, getContext().getResources().getString(R$string.notification_menu_snooze_action)));
        }
    }

    public boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        NotificationMenuRowPlugin provider;
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        if (i == 32) {
            doLongClickCallback();
            return true;
        } else if (i == 262144 || i == 524288) {
            this.mExpandClickListener.onClick(this);
            return true;
        } else if (i == 1048576) {
            performDismiss(true);
            return true;
        } else if (i != R$id.action_snooze || (provider = getProvider()) == null) {
            return false;
        } else {
            NotificationMenuRowPlugin.MenuItem snoozeMenuItem = provider.getSnoozeMenuItem(getContext());
            if (snoozeMenuItem != null) {
                doLongClickCallback(getWidth() / 2, getHeight() / 2, snoozeMenuItem);
            }
            return true;
        }
    }

    public ExpandableViewState createExpandableViewState() {
        return new NotificationViewState();
    }

    public boolean isAboveShelf() {
        return canShowHeadsUp() && (this.mIsPinned || this.mHeadsupDisappearRunning || ((this.mIsHeadsUp && this.mAboveShelf) || this.mExpandAnimationRunning || this.mChildIsExpanding));
    }

    public boolean childNeedsClipping(View view) {
        if (view instanceof NotificationContentView) {
            NotificationContentView notificationContentView = (NotificationContentView) view;
            if (isClippingNeeded()) {
                return true;
            }
            if (!hasNoRounding()) {
                boolean z = false;
                boolean z2 = getCurrentTopRoundness() != 0.0f;
                if (getCurrentBottomRoundness() != 0.0f) {
                    z = true;
                }
                if (notificationContentView.shouldClipToRounding(z2, z)) {
                    return true;
                }
            }
        } else if (view == this.mChildrenContainer) {
            if (isClippingNeeded() || !hasNoRounding()) {
                return true;
            }
        } else if (view instanceof NotificationGuts) {
            return !hasNoRounding();
        }
        return super.childNeedsClipping(view);
    }

    public void setExpandingClipPath(Path path) {
        this.mExpandingClipPath = path;
        invalidate();
    }

    public void dispatchDraw(Canvas canvas) {
        canvas.save();
        Path path = this.mExpandingClipPath;
        if (path != null && (this.mExpandAnimationRunning || this.mChildIsExpanding)) {
            canvas.clipPath(path);
        }
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void applyRoundness() {
        super.applyRoundness();
        applyChildrenRoundness();
    }

    public final void applyChildrenRoundness() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.setCurrentBottomRoundness(getCurrentBottomRoundness());
        }
    }

    public Path getCustomClipPath(View view) {
        if (view instanceof NotificationGuts) {
            return getClipPath(true);
        }
        return super.getCustomClipPath(view);
    }

    public final boolean hasNoRounding() {
        return getCurrentBottomRoundness() == 0.0f && getCurrentTopRoundness() == 0.0f;
    }

    public boolean isTopLevelChild() {
        return getParent() instanceof NotificationStackScrollLayout;
    }

    public boolean isGroupNotFullyVisible() {
        return getClipTopAmount() > 0 || getTranslationY() < 0.0f;
    }

    public void setAboveShelf(boolean z) {
        boolean isAboveShelf = isAboveShelf();
        this.mAboveShelf = z;
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public static class NotificationViewState extends ExpandableViewState {
        public NotificationViewState() {
        }

        public void applyToView(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!expandableNotificationRow.isExpandAnimationRunning()) {
                    handleFixedTranslationZ(expandableNotificationRow);
                    super.applyToView(view);
                    expandableNotificationRow.applyChildrenState();
                }
            }
        }

        public final void handleFixedTranslationZ(ExpandableNotificationRow expandableNotificationRow) {
            if (expandableNotificationRow.hasExpandingChild()) {
                this.zTranslation = expandableNotificationRow.getTranslationZ();
                this.clipTopAmount = expandableNotificationRow.getClipTopAmount();
            }
        }

        public void onYTranslationAnimationFinished(View view) {
            super.onYTranslationAnimationFinished(view);
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.isHeadsUpAnimatingAway()) {
                    expandableNotificationRow.setHeadsUpAnimatingAway(false);
                }
            }
        }

        public void animateTo(View view, AnimationProperties animationProperties) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!expandableNotificationRow.isExpandAnimationRunning()) {
                    handleFixedTranslationZ(expandableNotificationRow);
                    super.animateTo(view, animationProperties);
                    expandableNotificationRow.startChildAnimation(animationProperties);
                }
            }
        }
    }

    public InflatedSmartReplyState getExistingSmartReplyState() {
        return this.mPrivateLayout.getCurrentSmartReplyState();
    }

    @VisibleForTesting
    public void setChildrenContainer(NotificationChildrenContainer notificationChildrenContainer) {
        this.mChildrenContainer = notificationChildrenContainer;
    }

    @VisibleForTesting
    public void setPrivateLayout(NotificationContentView notificationContentView) {
        this.mPrivateLayout = notificationContentView;
    }

    @VisibleForTesting
    public void setPublicLayout(NotificationContentView notificationContentView) {
        this.mPublicLayout = notificationContentView;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("Notification: " + this.mEntry.getKey());
        DumpUtilsKt.withIncreasedIndent(asIndenting, new ExpandableNotificationRow$$ExternalSyntheticLambda3(this, asIndenting, strArr));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$7(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        indentingPrintWriter.print("visibility: " + getVisibility());
        indentingPrintWriter.print(", alpha: " + getAlpha());
        indentingPrintWriter.print(", translation: " + getTranslation());
        indentingPrintWriter.print(", removed: " + isRemoved());
        indentingPrintWriter.print(", expandAnimationRunning: " + this.mExpandAnimationRunning);
        NotificationContentView showingLayout = getShowingLayout();
        StringBuilder sb = new StringBuilder();
        sb.append(", privateShowing: ");
        sb.append(showingLayout == this.mPrivateLayout);
        indentingPrintWriter.print(sb.toString());
        indentingPrintWriter.println();
        showingLayout.dump(indentingPrintWriter, strArr);
        if (getViewState() != null) {
            getViewState().dump(indentingPrintWriter, strArr);
            indentingPrintWriter.println();
        } else {
            indentingPrintWriter.println("no viewState!!!");
        }
        if (this.mIsSummaryWithChildren) {
            indentingPrintWriter.println();
            indentingPrintWriter.print("ChildrenContainer");
            indentingPrintWriter.print(" visibility: " + this.mChildrenContainer.getVisibility());
            indentingPrintWriter.print(", alpha: " + this.mChildrenContainer.getAlpha());
            indentingPrintWriter.print(", translationY: " + this.mChildrenContainer.getTranslationY());
            indentingPrintWriter.println();
            List<ExpandableNotificationRow> attachedChildren = getAttachedChildren();
            indentingPrintWriter.println("Children: " + attachedChildren.size());
            indentingPrintWriter.print("{");
            indentingPrintWriter.increaseIndent();
            for (ExpandableNotificationRow dump : attachedChildren) {
                indentingPrintWriter.println();
                dump.dump(indentingPrintWriter, strArr);
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("}");
            return;
        }
        NotificationContentView notificationContentView = this.mPrivateLayout;
        if (notificationContentView != null) {
            notificationContentView.dumpSmartReplies(indentingPrintWriter);
        }
    }

    public final void setTargetPoint(Point point) {
        this.mTargetPoint = point;
    }
}
