package com.android.systemui.statusbar.notification.row;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.NotificationMenuRow;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationCustomViewWrapper;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt;
import com.android.systemui.statusbar.policy.SmartReplyView;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.wmshell.BubblesManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationContentView extends FrameLayout implements NotificationFadeAware {
    public static final boolean DEBUG = Log.isLoggable("NotificationContentView", 3);
    public boolean mAnimate;
    public int mAnimationStartVisibleType = -1;
    public boolean mBeforeN;
    public RemoteInputView mCachedExpandedRemoteInput;
    public RemoteInputViewController mCachedExpandedRemoteInputViewController;
    public RemoteInputView mCachedHeadsUpRemoteInput;
    public RemoteInputViewController mCachedHeadsUpRemoteInputViewController;
    public int mClipBottomAmount;
    public final Rect mClipBounds = new Rect();
    public boolean mClipToActualHeight = true;
    public int mClipTopAmount;
    public ExpandableNotificationRow mContainingNotification;
    public int mContentHeight;
    public int mContentHeightAtAnimationStart = -1;
    public View mContractedChild;
    public NotificationViewWrapper mContractedWrapper;
    public InflatedSmartReplyState mCurrentSmartReplyState;
    public final ViewTreeObserver.OnPreDrawListener mEnableAnimationPredrawListener = new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
            NotificationContentView.this.post(new Runnable() {
                public void run() {
                    NotificationContentView.this.mAnimate = true;
                }
            });
            NotificationContentView.this.getViewTreeObserver().removeOnPreDrawListener(this);
            return true;
        }
    };
    public View.OnClickListener mExpandClickListener;
    public boolean mExpandable;
    public View mExpandedChild;
    public InflatedSmartReplyViewHolder mExpandedInflatedSmartReplies;
    public RemoteInputView mExpandedRemoteInput;
    public RemoteInputViewController mExpandedRemoteInputController;
    public SmartReplyView mExpandedSmartReplyView;
    public Runnable mExpandedVisibleListener;
    public NotificationViewWrapper mExpandedWrapper;
    public boolean mFocusOnVisibilityChange;
    public boolean mForceSelectNextLayout = true;
    public boolean mHeadsUpAnimatingAway;
    public View mHeadsUpChild;
    public int mHeadsUpHeight;
    public InflatedSmartReplyViewHolder mHeadsUpInflatedSmartReplies;
    public RemoteInputView mHeadsUpRemoteInput;
    public RemoteInputViewController mHeadsUpRemoteInputController;
    public SmartReplyView mHeadsUpSmartReplyView;
    public NotificationViewWrapper mHeadsUpWrapper;
    public final HybridGroupManager mHybridGroupManager = new HybridGroupManager(getContext());
    public boolean mIsChildInGroup;
    public boolean mIsContentExpandable;
    public boolean mIsHeadsUp;
    public boolean mLegacy;
    public int mMinContractedHeight;
    public NotificationEntry mNotificationEntry;
    public int mNotificationMaxHeight;
    public final ArrayMap<View, Runnable> mOnContentViewInactiveListeners = new ArrayMap<>();
    public PeopleNotificationIdentifier mPeopleIdentifier;
    public PendingIntent mPreviousExpandedRemoteInputIntent;
    public PendingIntent mPreviousHeadsUpRemoteInputIntent;
    public RemoteInputController mRemoteInputController;
    public RemoteInputViewSubcomponent.Factory mRemoteInputSubcomponentFactory;
    public boolean mRemoteInputVisible;
    public HybridNotificationView mSingleLineView;
    public int mSingleLineWidthIndention;
    public int mSmallHeight;
    public SmartReplyConstants mSmartReplyConstants;
    public SmartReplyController mSmartReplyController;
    public int mTransformationStartVisibleType;
    public int mUnrestrictedContentHeight;
    public boolean mUserExpanding;
    public int mVisibleType = -1;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setGroupMembershipManager(GroupMembershipManager groupMembershipManager) {
    }

    public void setIsLowPriority(boolean z) {
    }

    public NotificationContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        reinflate();
    }

    public void initialize(PeopleNotificationIdentifier peopleNotificationIdentifier, RemoteInputViewSubcomponent.Factory factory, SmartReplyConstants smartReplyConstants, SmartReplyController smartReplyController) {
        this.mPeopleIdentifier = peopleNotificationIdentifier;
        this.mRemoteInputSubcomponentFactory = factory;
        this.mSmartReplyConstants = smartReplyConstants;
        this.mSmartReplyController = smartReplyController;
    }

    public void reinflate() {
        this.mMinContractedHeight = getResources().getDimensionPixelSize(R$dimen.min_notification_layout_height);
    }

    public void setHeights(int i, int i2, int i3) {
        this.mSmallHeight = i;
        this.mHeadsUpHeight = i2;
        this.mNotificationMaxHeight = i3;
    }

    public void onMeasure(int i, int i2) {
        int i3;
        boolean z;
        int i4;
        boolean z2;
        int mode = View.MeasureSpec.getMode(i2);
        boolean z3 = true;
        boolean z4 = mode == 1073741824;
        boolean z5 = mode == Integer.MIN_VALUE;
        int i5 = 1073741823;
        int size = View.MeasureSpec.getSize(i);
        if (z4 || z5) {
            i5 = View.MeasureSpec.getSize(i2);
        }
        int i6 = i5;
        if (this.mExpandedChild != null) {
            int i7 = this.mNotificationMaxHeight;
            SmartReplyView smartReplyView = this.mExpandedSmartReplyView;
            if (smartReplyView != null) {
                i7 += smartReplyView.getHeightUpperLimit();
            }
            int extraMeasureHeight = i7 + this.mExpandedWrapper.getExtraMeasureHeight();
            int i8 = this.mExpandedChild.getLayoutParams().height;
            if (i8 >= 0) {
                extraMeasureHeight = Math.min(extraMeasureHeight, i8);
                z2 = true;
            } else {
                z2 = false;
            }
            measureChildWithMargins(this.mExpandedChild, i, 0, View.MeasureSpec.makeMeasureSpec(extraMeasureHeight, z2 ? 1073741824 : Integer.MIN_VALUE), 0);
            i3 = Math.max(0, this.mExpandedChild.getMeasuredHeight());
        } else {
            i3 = 0;
        }
        View view = this.mContractedChild;
        if (view != null) {
            int i9 = this.mSmallHeight;
            int i10 = view.getLayoutParams().height;
            if (i10 >= 0) {
                i9 = Math.min(i9, i10);
                z = true;
            } else {
                z = false;
            }
            if (shouldContractedBeFixedSize() || z) {
                i4 = View.MeasureSpec.makeMeasureSpec(i9, 1073741824);
            } else {
                i4 = View.MeasureSpec.makeMeasureSpec(i9, Integer.MIN_VALUE);
            }
            measureChildWithMargins(this.mContractedChild, i, 0, i4, 0);
            int measuredHeight = this.mContractedChild.getMeasuredHeight();
            int i11 = this.mMinContractedHeight;
            if (measuredHeight < i11) {
                measureChildWithMargins(this.mContractedChild, i, 0, View.MeasureSpec.makeMeasureSpec(i11, 1073741824), 0);
            }
            i3 = Math.max(i3, measuredHeight);
            if (this.mExpandedChild != null && this.mContractedChild.getMeasuredHeight() > this.mExpandedChild.getMeasuredHeight()) {
                measureChildWithMargins(this.mExpandedChild, i, 0, View.MeasureSpec.makeMeasureSpec(this.mContractedChild.getMeasuredHeight(), 1073741824), 0);
            }
        }
        if (this.mHeadsUpChild != null) {
            int i12 = this.mHeadsUpHeight;
            SmartReplyView smartReplyView2 = this.mHeadsUpSmartReplyView;
            if (smartReplyView2 != null) {
                i12 += smartReplyView2.getHeightUpperLimit();
            }
            int extraMeasureHeight2 = i12 + this.mHeadsUpWrapper.getExtraMeasureHeight();
            int i13 = this.mHeadsUpChild.getLayoutParams().height;
            if (i13 >= 0) {
                extraMeasureHeight2 = Math.min(extraMeasureHeight2, i13);
            } else {
                z3 = false;
            }
            measureChildWithMargins(this.mHeadsUpChild, i, 0, View.MeasureSpec.makeMeasureSpec(extraMeasureHeight2, z3 ? 1073741824 : Integer.MIN_VALUE), 0);
            i3 = Math.max(i3, this.mHeadsUpChild.getMeasuredHeight());
        }
        if (this.mSingleLineView != null) {
            this.mSingleLineView.measure((this.mSingleLineWidthIndention == 0 || View.MeasureSpec.getMode(i) == 0) ? i : View.MeasureSpec.makeMeasureSpec((size - this.mSingleLineWidthIndention) + this.mSingleLineView.getPaddingEnd(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mNotificationMaxHeight, Integer.MIN_VALUE));
            i3 = Math.max(i3, this.mSingleLineView.getMeasuredHeight());
        }
        setMeasuredDimension(size, Math.min(i3, i6));
    }

    public final int getExtraRemoteInputHeight(RemoteInputView remoteInputView) {
        if (remoteInputView == null) {
            return 0;
        }
        if (remoteInputView.isActive() || remoteInputView.isSending()) {
            return getResources().getDimensionPixelSize(17105385);
        }
        return 0;
    }

    public final boolean shouldContractedBeFixedSize() {
        return this.mBeforeN && (this.mContractedWrapper instanceof NotificationCustomViewWrapper);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.mExpandedChild;
        int height = view != null ? view.getHeight() : 0;
        super.onLayout(z, i, i2, i3, i4);
        if (!(height == 0 || this.mExpandedChild.getHeight() == height)) {
            this.mContentHeightAtAnimationStart = height;
        }
        updateClipping();
        invalidateOutline();
        selectLayout(false, this.mForceSelectNextLayout);
        this.mForceSelectNextLayout = false;
        updateExpandButtonsDuringLayout(this.mExpandable, true);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateVisibility();
    }

    public View getContractedChild() {
        return this.mContractedChild;
    }

    public View getExpandedChild() {
        return this.mExpandedChild;
    }

    public View getHeadsUpChild() {
        return this.mHeadsUpChild;
    }

    public void setContractedChild(View view) {
        View view2 = this.mContractedChild;
        if (view2 != null) {
            this.mOnContentViewInactiveListeners.remove(view2);
            this.mContractedChild.animate().cancel();
            removeView(this.mContractedChild);
        }
        if (view == null) {
            this.mContractedChild = null;
            this.mContractedWrapper = null;
            if (this.mTransformationStartVisibleType == 0) {
                this.mTransformationStartVisibleType = -1;
                return;
            }
            return;
        }
        addView(view);
        this.mContractedChild = view;
        this.mContractedWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
    }

    public final NotificationViewWrapper getWrapperForView(View view) {
        if (view == this.mContractedChild) {
            return this.mContractedWrapper;
        }
        if (view == this.mExpandedChild) {
            return this.mExpandedWrapper;
        }
        if (view == this.mHeadsUpChild) {
            return this.mHeadsUpWrapper;
        }
        return null;
    }

    public void setExpandedChild(View view) {
        if (this.mExpandedChild != null) {
            this.mPreviousExpandedRemoteInputIntent = null;
            RemoteInputView remoteInputView = this.mExpandedRemoteInput;
            if (remoteInputView != null) {
                remoteInputView.onNotificationUpdateOrReset();
                if (this.mExpandedRemoteInput.isActive()) {
                    RemoteInputViewController remoteInputViewController = this.mExpandedRemoteInputController;
                    if (remoteInputViewController != null) {
                        this.mPreviousExpandedRemoteInputIntent = remoteInputViewController.getPendingIntent();
                    }
                    RemoteInputView remoteInputView2 = this.mExpandedRemoteInput;
                    this.mCachedExpandedRemoteInput = remoteInputView2;
                    this.mCachedExpandedRemoteInputViewController = this.mExpandedRemoteInputController;
                    remoteInputView2.dispatchStartTemporaryDetach();
                    ((ViewGroup) this.mExpandedRemoteInput.getParent()).removeView(this.mExpandedRemoteInput);
                }
            }
            this.mOnContentViewInactiveListeners.remove(this.mExpandedChild);
            this.mExpandedChild.animate().cancel();
            removeView(this.mExpandedChild);
            this.mExpandedRemoteInput = null;
            RemoteInputViewController remoteInputViewController2 = this.mExpandedRemoteInputController;
            if (remoteInputViewController2 != null) {
                remoteInputViewController2.unbind();
            }
            this.mExpandedRemoteInputController = null;
        }
        if (view == null) {
            this.mExpandedChild = null;
            this.mExpandedWrapper = null;
            if (this.mTransformationStartVisibleType == 1) {
                this.mTransformationStartVisibleType = -1;
            }
            if (this.mVisibleType == 1) {
                selectLayout(false, true);
                return;
            }
            return;
        }
        addView(view);
        this.mExpandedChild = view;
        this.mExpandedWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
        if (expandableNotificationRow != null) {
            applySystemActions(this.mExpandedChild, expandableNotificationRow.getEntry());
        }
    }

    public void setHeadsUpChild(View view) {
        if (this.mHeadsUpChild != null) {
            this.mPreviousHeadsUpRemoteInputIntent = null;
            RemoteInputView remoteInputView = this.mHeadsUpRemoteInput;
            if (remoteInputView != null) {
                remoteInputView.onNotificationUpdateOrReset();
                if (this.mHeadsUpRemoteInput.isActive()) {
                    RemoteInputViewController remoteInputViewController = this.mHeadsUpRemoteInputController;
                    if (remoteInputViewController != null) {
                        this.mPreviousHeadsUpRemoteInputIntent = remoteInputViewController.getPendingIntent();
                    }
                    RemoteInputView remoteInputView2 = this.mHeadsUpRemoteInput;
                    this.mCachedHeadsUpRemoteInput = remoteInputView2;
                    this.mCachedHeadsUpRemoteInputViewController = this.mHeadsUpRemoteInputController;
                    remoteInputView2.dispatchStartTemporaryDetach();
                    ((ViewGroup) this.mHeadsUpRemoteInput.getParent()).removeView(this.mHeadsUpRemoteInput);
                }
            }
            this.mOnContentViewInactiveListeners.remove(this.mHeadsUpChild);
            this.mHeadsUpChild.animate().cancel();
            removeView(this.mHeadsUpChild);
            this.mHeadsUpRemoteInput = null;
            RemoteInputViewController remoteInputViewController2 = this.mHeadsUpRemoteInputController;
            if (remoteInputViewController2 != null) {
                remoteInputViewController2.unbind();
            }
            this.mHeadsUpRemoteInputController = null;
        }
        if (view == null) {
            this.mHeadsUpChild = null;
            this.mHeadsUpWrapper = null;
            if (this.mTransformationStartVisibleType == 2) {
                this.mTransformationStartVisibleType = -1;
            }
            if (this.mVisibleType == 2) {
                selectLayout(false, true);
                return;
            }
            return;
        }
        addView(view);
        this.mHeadsUpChild = view;
        this.mHeadsUpWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
        if (expandableNotificationRow != null) {
            applySystemActions(this.mHeadsUpChild, expandableNotificationRow.getEntry());
        }
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        view.setTag(R$id.row_tag_for_content_view, this.mContainingNotification);
    }

    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        updateVisibility();
        if (i != 0 && !this.mOnContentViewInactiveListeners.isEmpty()) {
            Iterator it = new ArrayList(this.mOnContentViewInactiveListeners.values()).iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
            this.mOnContentViewInactiveListeners.clear();
        }
    }

    public final void updateVisibility() {
        setVisible(isShown());
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
    }

    public final void setVisible(boolean z) {
        if (z) {
            getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
            getViewTreeObserver().addOnPreDrawListener(this.mEnableAnimationPredrawListener);
            return;
        }
        getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
        this.mAnimate = false;
    }

    public final void focusExpandButtonIfNecessary() {
        View expandButton;
        if (this.mFocusOnVisibilityChange) {
            NotificationViewWrapper visibleWrapper = getVisibleWrapper(this.mVisibleType);
            if (!(visibleWrapper == null || (expandButton = visibleWrapper.getExpandButton()) == null)) {
                expandButton.requestAccessibilityFocus();
            }
            this.mFocusOnVisibilityChange = false;
        }
    }

    public void setContentHeight(int i) {
        this.mUnrestrictedContentHeight = Math.max(i, getMinHeight());
        this.mContentHeight = Math.min(this.mUnrestrictedContentHeight, (this.mContainingNotification.getIntrinsicHeight() - getExtraRemoteInputHeight(this.mExpandedRemoteInput)) - getExtraRemoteInputHeight(this.mHeadsUpRemoteInput));
        selectLayout(this.mAnimate, false);
        if (this.mContractedChild != null) {
            int minContentHeightHint = getMinContentHeightHint();
            NotificationViewWrapper visibleWrapper = getVisibleWrapper(this.mVisibleType);
            if (visibleWrapper != null) {
                visibleWrapper.setContentHeight(this.mUnrestrictedContentHeight, minContentHeightHint);
            }
            NotificationViewWrapper visibleWrapper2 = getVisibleWrapper(this.mTransformationStartVisibleType);
            if (visibleWrapper2 != null) {
                visibleWrapper2.setContentHeight(this.mUnrestrictedContentHeight, minContentHeightHint);
            }
            updateClipping();
            invalidateOutline();
        }
    }

    public final int getMinContentHeightHint() {
        int i;
        int i2;
        if (this.mIsChildInGroup && isVisibleOrTransitioning(3)) {
            return this.mContext.getResources().getDimensionPixelSize(17105372);
        }
        if (!(this.mHeadsUpChild == null || this.mExpandedChild == null)) {
            boolean z = isTransitioningFromTo(2, 1) || isTransitioningFromTo(1, 2);
            boolean z2 = !isVisibleOrTransitioning(0) && (this.mIsHeadsUp || this.mHeadsUpAnimatingAway) && this.mContainingNotification.canShowHeadsUp();
            if (z || z2) {
                return Math.min(getViewHeight(2), getViewHeight(1));
            }
        }
        if (this.mVisibleType == 1 && (i2 = this.mContentHeightAtAnimationStart) != -1 && this.mExpandedChild != null) {
            return Math.min(i2, getViewHeight(1));
        }
        if (this.mHeadsUpChild != null && isVisibleOrTransitioning(2)) {
            i = getViewHeight(2);
        } else if (this.mExpandedChild != null) {
            i = getViewHeight(1);
        } else if (this.mContractedChild != null) {
            i = getViewHeight(0) + this.mContext.getResources().getDimensionPixelSize(17105372);
        } else {
            i = getMinHeight();
        }
        return (this.mExpandedChild == null || !isVisibleOrTransitioning(1)) ? i : Math.min(i, getViewHeight(1));
    }

    public final boolean isTransitioningFromTo(int i, int i2) {
        return (this.mTransformationStartVisibleType == i || this.mAnimationStartVisibleType == i) && this.mVisibleType == i2;
    }

    public final boolean isVisibleOrTransitioning(int i) {
        return this.mVisibleType == i || this.mTransformationStartVisibleType == i || this.mAnimationStartVisibleType == i;
    }

    public final void updateContentTransformation() {
        int calculateVisibleType = calculateVisibleType();
        if (getTransformableViewForVisibleType(this.mVisibleType) == null) {
            this.mVisibleType = calculateVisibleType;
            updateViewVisibilities(calculateVisibleType);
            updateBackgroundColor(false);
            return;
        }
        int i = this.mVisibleType;
        if (calculateVisibleType != i) {
            this.mTransformationStartVisibleType = i;
            TransformableView transformableViewForVisibleType = getTransformableViewForVisibleType(calculateVisibleType);
            TransformableView transformableViewForVisibleType2 = getTransformableViewForVisibleType(this.mTransformationStartVisibleType);
            transformableViewForVisibleType.transformFrom(transformableViewForVisibleType2, 0.0f);
            getViewForVisibleType(calculateVisibleType).setVisibility(0);
            transformableViewForVisibleType2.transformTo(transformableViewForVisibleType, 0.0f);
            this.mVisibleType = calculateVisibleType;
            updateBackgroundColor(true);
        }
        if (this.mForceSelectNextLayout) {
            forceUpdateVisibilities();
        }
        int i2 = this.mTransformationStartVisibleType;
        if (i2 == -1 || this.mVisibleType == i2 || getViewForVisibleType(i2) == null) {
            updateViewVisibilities(calculateVisibleType);
            updateBackgroundColor(false);
            return;
        }
        TransformableView transformableViewForVisibleType3 = getTransformableViewForVisibleType(this.mVisibleType);
        TransformableView transformableViewForVisibleType4 = getTransformableViewForVisibleType(this.mTransformationStartVisibleType);
        float calculateTransformationAmount = calculateTransformationAmount();
        transformableViewForVisibleType3.transformFrom(transformableViewForVisibleType4, calculateTransformationAmount);
        transformableViewForVisibleType4.transformTo(transformableViewForVisibleType3, calculateTransformationAmount);
        updateBackgroundTransformation(calculateTransformationAmount);
    }

    public final void updateBackgroundTransformation(float f) {
        int backgroundColor = getBackgroundColor(this.mVisibleType);
        int backgroundColor2 = getBackgroundColor(this.mTransformationStartVisibleType);
        if (backgroundColor != backgroundColor2) {
            if (backgroundColor2 == 0) {
                backgroundColor2 = this.mContainingNotification.getBackgroundColorWithoutTint();
            }
            if (backgroundColor == 0) {
                backgroundColor = this.mContainingNotification.getBackgroundColorWithoutTint();
            }
            backgroundColor = NotificationUtils.interpolateColors(backgroundColor2, backgroundColor, f);
        }
        this.mContainingNotification.setContentBackground(backgroundColor, false, this);
    }

    public final float calculateTransformationAmount() {
        int viewHeight = getViewHeight(this.mTransformationStartVisibleType);
        int viewHeight2 = getViewHeight(this.mVisibleType);
        int abs = Math.abs(this.mContentHeight - viewHeight);
        int abs2 = Math.abs(viewHeight2 - viewHeight);
        if (abs2 != 0) {
            return Math.min(1.0f, ((float) abs) / ((float) abs2));
        }
        Log.wtf("NotificationContentView", "the total transformation distance is 0\n StartType: " + this.mTransformationStartVisibleType + " height: " + viewHeight + "\n VisibleType: " + this.mVisibleType + " height: " + viewHeight2 + "\n mContentHeight: " + this.mContentHeight);
        return 1.0f;
    }

    public int getMaxHeight() {
        int viewHeight;
        int extraRemoteInputHeight;
        if (this.mExpandedChild != null) {
            viewHeight = getViewHeight(1);
            extraRemoteInputHeight = getExtraRemoteInputHeight(this.mExpandedRemoteInput);
        } else if (this.mIsHeadsUp && this.mHeadsUpChild != null && this.mContainingNotification.canShowHeadsUp()) {
            viewHeight = getViewHeight(2);
            extraRemoteInputHeight = getExtraRemoteInputHeight(this.mHeadsUpRemoteInput);
        } else if (this.mContractedChild != null) {
            return getViewHeight(0);
        } else {
            return this.mNotificationMaxHeight;
        }
        return viewHeight + extraRemoteInputHeight;
    }

    public final int getViewHeight(int i) {
        return getViewHeight(i, false);
    }

    public final int getViewHeight(int i, boolean z) {
        View viewForVisibleType = getViewForVisibleType(i);
        int height = viewForVisibleType.getHeight();
        NotificationViewWrapper wrapperForView = getWrapperForView(viewForVisibleType);
        return wrapperForView != null ? height + wrapperForView.getHeaderTranslation(z) : height;
    }

    public int getMinHeight() {
        return getMinHeight(false);
    }

    public int getMinHeight(boolean z) {
        if (z || !this.mIsChildInGroup || isGroupExpanded()) {
            return this.mContractedChild != null ? getViewHeight(0) : this.mMinContractedHeight;
        }
        return this.mSingleLineView.getHeight();
    }

    public final boolean isGroupExpanded() {
        return this.mContainingNotification.isGroupExpanded();
    }

    public void setClipTopAmount(int i) {
        this.mClipTopAmount = i;
        updateClipping();
    }

    public void setClipBottomAmount(int i) {
        this.mClipBottomAmount = i;
        updateClipping();
    }

    public void setTranslationY(float f) {
        super.setTranslationY(f);
        updateClipping();
    }

    public final void updateClipping() {
        if (this.mClipToActualHeight) {
            int translationY = (int) (((float) this.mClipTopAmount) - getTranslationY());
            this.mClipBounds.set(0, translationY, getWidth(), Math.max(translationY, (int) (((float) (this.mUnrestrictedContentHeight - this.mClipBottomAmount)) - getTranslationY())));
            setClipBounds(this.mClipBounds);
            return;
        }
        setClipBounds((Rect) null);
    }

    public void setClipToActualHeight(boolean z) {
        this.mClipToActualHeight = z;
        updateClipping();
    }

    public final void selectLayout(boolean z, boolean z2) {
        if (this.mContractedChild != null) {
            if (this.mUserExpanding) {
                updateContentTransformation();
                return;
            }
            int calculateVisibleType = calculateVisibleType();
            boolean z3 = calculateVisibleType != this.mVisibleType;
            if (z3 || z2) {
                View viewForVisibleType = getViewForVisibleType(calculateVisibleType);
                if (viewForVisibleType != null) {
                    viewForVisibleType.setVisibility(0);
                    transferRemoteInputFocus(calculateVisibleType);
                }
                if (!z || ((calculateVisibleType != 1 || this.mExpandedChild == null) && ((calculateVisibleType != 2 || this.mHeadsUpChild == null) && ((calculateVisibleType != 3 || this.mSingleLineView == null) && calculateVisibleType != 0)))) {
                    updateViewVisibilities(calculateVisibleType);
                } else {
                    animateToVisibleType(calculateVisibleType);
                }
                this.mVisibleType = calculateVisibleType;
                if (z3) {
                    focusExpandButtonIfNecessary();
                }
                NotificationViewWrapper visibleWrapper = getVisibleWrapper(calculateVisibleType);
                if (visibleWrapper != null) {
                    visibleWrapper.setContentHeight(this.mUnrestrictedContentHeight, getMinContentHeightHint());
                }
                updateBackgroundColor(z);
            }
        }
    }

    public final void forceUpdateVisibilities() {
        forceUpdateVisibility(0, this.mContractedChild, this.mContractedWrapper);
        forceUpdateVisibility(1, this.mExpandedChild, this.mExpandedWrapper);
        forceUpdateVisibility(2, this.mHeadsUpChild, this.mHeadsUpWrapper);
        HybridNotificationView hybridNotificationView = this.mSingleLineView;
        forceUpdateVisibility(3, hybridNotificationView, hybridNotificationView);
        fireExpandedVisibleListenerIfVisible();
        this.mAnimationStartVisibleType = -1;
    }

    public final void fireExpandedVisibleListenerIfVisible() {
        if (this.mExpandedVisibleListener != null && this.mExpandedChild != null && isShown() && this.mExpandedChild.getVisibility() == 0) {
            Runnable runnable = this.mExpandedVisibleListener;
            this.mExpandedVisibleListener = null;
            runnable.run();
        }
    }

    public final void forceUpdateVisibility(int i, View view, TransformableView transformableView) {
        if (view != null) {
            if (!(this.mVisibleType == i || this.mTransformationStartVisibleType == i)) {
                view.setVisibility(4);
            } else {
                transformableView.setVisible(true);
            }
        }
    }

    public void updateBackgroundColor(boolean z) {
        this.mContainingNotification.setContentBackground(getBackgroundColor(this.mVisibleType), z, this);
    }

    public void setBackgroundTintColor(int i) {
        boolean isColorized = this.mNotificationEntry.getSbn().getNotification().isColorized();
        SmartReplyView smartReplyView = this.mExpandedSmartReplyView;
        if (smartReplyView != null) {
            smartReplyView.setBackgroundTintColor(i, isColorized);
        }
        SmartReplyView smartReplyView2 = this.mHeadsUpSmartReplyView;
        if (smartReplyView2 != null) {
            smartReplyView2.setBackgroundTintColor(i, isColorized);
        }
        RemoteInputView remoteInputView = this.mExpandedRemoteInput;
        if (remoteInputView != null) {
            remoteInputView.setBackgroundTintColor(i, isColorized);
        }
        RemoteInputView remoteInputView2 = this.mHeadsUpRemoteInput;
        if (remoteInputView2 != null) {
            remoteInputView2.setBackgroundTintColor(i, isColorized);
        }
    }

    public int getVisibleType() {
        return this.mVisibleType;
    }

    public int getBackgroundColorForExpansionState() {
        int i;
        if (isGroupExpanded() || this.mContainingNotification.isUserLocked()) {
            i = calculateVisibleType();
        } else {
            i = getVisibleType();
        }
        return getBackgroundColor(i);
    }

    public int getBackgroundColor(int i) {
        NotificationViewWrapper visibleWrapper = getVisibleWrapper(i);
        if (visibleWrapper != null) {
            return visibleWrapper.getCustomBackgroundColor();
        }
        return 0;
    }

    public final void updateViewVisibilities(int i) {
        updateViewVisibility(i, 0, this.mContractedChild, this.mContractedWrapper);
        updateViewVisibility(i, 1, this.mExpandedChild, this.mExpandedWrapper);
        updateViewVisibility(i, 2, this.mHeadsUpChild, this.mHeadsUpWrapper);
        HybridNotificationView hybridNotificationView = this.mSingleLineView;
        updateViewVisibility(i, 3, hybridNotificationView, hybridNotificationView);
        fireExpandedVisibleListenerIfVisible();
        this.mAnimationStartVisibleType = -1;
    }

    public final void updateViewVisibility(int i, int i2, View view, TransformableView transformableView) {
        if (view != null) {
            transformableView.setVisible(i == i2);
        }
    }

    public final void animateToVisibleType(int i) {
        TransformableView transformableViewForVisibleType = getTransformableViewForVisibleType(i);
        final TransformableView transformableViewForVisibleType2 = getTransformableViewForVisibleType(this.mVisibleType);
        if (transformableViewForVisibleType == transformableViewForVisibleType2 || transformableViewForVisibleType2 == null) {
            transformableViewForVisibleType.setVisible(true);
            return;
        }
        this.mAnimationStartVisibleType = this.mVisibleType;
        transformableViewForVisibleType.transformFrom(transformableViewForVisibleType2);
        getViewForVisibleType(i).setVisibility(0);
        transformableViewForVisibleType2.transformTo(transformableViewForVisibleType, (Runnable) new Runnable() {
            public void run() {
                TransformableView transformableView = transformableViewForVisibleType2;
                NotificationContentView notificationContentView = NotificationContentView.this;
                if (transformableView != notificationContentView.getTransformableViewForVisibleType(notificationContentView.mVisibleType)) {
                    transformableViewForVisibleType2.setVisible(false);
                }
                NotificationContentView.this.mAnimationStartVisibleType = -1;
            }
        });
        fireExpandedVisibleListenerIfVisible();
    }

    public final void transferRemoteInputFocus(int i) {
        RemoteInputViewController remoteInputViewController;
        RemoteInputViewController remoteInputViewController2;
        if (i == 2 && this.mHeadsUpRemoteInputController != null && (remoteInputViewController2 = this.mExpandedRemoteInputController) != null && remoteInputViewController2.isActive()) {
            this.mHeadsUpRemoteInputController.stealFocusFrom(this.mExpandedRemoteInputController);
        }
        if (i == 1 && this.mExpandedRemoteInputController != null && (remoteInputViewController = this.mHeadsUpRemoteInputController) != null && remoteInputViewController.isActive()) {
            this.mExpandedRemoteInputController.stealFocusFrom(this.mHeadsUpRemoteInputController);
        }
    }

    public final TransformableView getTransformableViewForVisibleType(int i) {
        if (i == 1) {
            return this.mExpandedWrapper;
        }
        if (i == 2) {
            return this.mHeadsUpWrapper;
        }
        if (i != 3) {
            return this.mContractedWrapper;
        }
        return this.mSingleLineView;
    }

    public final View getViewForVisibleType(int i) {
        if (i == 1) {
            return this.mExpandedChild;
        }
        if (i == 2) {
            return this.mHeadsUpChild;
        }
        if (i != 3) {
            return this.mContractedChild;
        }
        return this.mSingleLineView;
    }

    public View[] getAllViews() {
        return new View[]{this.mContractedChild, this.mHeadsUpChild, this.mExpandedChild, this.mSingleLineView};
    }

    public NotificationViewWrapper getVisibleWrapper() {
        return getVisibleWrapper(this.mVisibleType);
    }

    public NotificationViewWrapper getVisibleWrapper(int i) {
        if (i == 0) {
            return this.mContractedWrapper;
        }
        if (i == 1) {
            return this.mExpandedWrapper;
        }
        if (i != 2) {
            return null;
        }
        return this.mHeadsUpWrapper;
    }

    public int calculateVisibleType() {
        int i;
        int i2;
        if (this.mUserExpanding) {
            if (!this.mIsChildInGroup || isGroupExpanded() || this.mContainingNotification.isExpanded(true)) {
                i = this.mContainingNotification.getMaxContentHeight();
            } else {
                i = this.mContainingNotification.getShowingLayout().getMinHeight();
            }
            if (i == 0) {
                i = this.mContentHeight;
            }
            int visualTypeForHeight = getVisualTypeForHeight((float) i);
            if (!this.mIsChildInGroup || isGroupExpanded()) {
                i2 = getVisualTypeForHeight((float) this.mContainingNotification.getCollapsedHeight());
            } else {
                i2 = 3;
            }
            return this.mTransformationStartVisibleType == i2 ? visualTypeForHeight : i2;
        }
        int intrinsicHeight = this.mContainingNotification.getIntrinsicHeight();
        int i3 = this.mContentHeight;
        if (intrinsicHeight != 0) {
            i3 = Math.min(i3, intrinsicHeight);
        }
        return getVisualTypeForHeight((float) i3);
    }

    public final int getVisualTypeForHeight(float f) {
        boolean z = this.mExpandedChild == null;
        if (!z && f == ((float) getViewHeight(1))) {
            return 1;
        }
        if (!this.mUserExpanding && this.mIsChildInGroup && !isGroupExpanded()) {
            return 3;
        }
        if ((this.mIsHeadsUp || this.mHeadsUpAnimatingAway) && this.mHeadsUpChild != null && this.mContainingNotification.canShowHeadsUp()) {
            if (f <= ((float) getViewHeight(2)) || z) {
                return 2;
            }
            return 1;
        } else if (z || (this.mContractedChild != null && f <= ((float) getViewHeight(0)) && (!this.mIsChildInGroup || isGroupExpanded() || !this.mContainingNotification.isExpanded(true)))) {
            return 0;
        } else {
            if (!z) {
                return 1;
            }
            return -1;
        }
    }

    public boolean isContentExpandable() {
        return this.mIsContentExpandable;
    }

    public void setHeadsUp(boolean z) {
        this.mIsHeadsUp = z;
        selectLayout(false, true);
        updateExpandButtons(this.mExpandable);
    }

    public void setLegacy(boolean z) {
        this.mLegacy = z;
        updateLegacy();
    }

    public final void updateLegacy() {
        if (this.mContractedChild != null) {
            this.mContractedWrapper.setLegacy(this.mLegacy);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.setLegacy(this.mLegacy);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.setLegacy(this.mLegacy);
        }
    }

    public void setIsChildInGroup(boolean z) {
        this.mIsChildInGroup = z;
        if (this.mContractedChild != null) {
            this.mContractedWrapper.setIsChildInGroup(z);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.setIsChildInGroup(this.mIsChildInGroup);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.setIsChildInGroup(this.mIsChildInGroup);
        }
        updateAllSingleLineViews();
    }

    public void onNotificationUpdated(NotificationEntry notificationEntry) {
        this.mNotificationEntry = notificationEntry;
        this.mBeforeN = notificationEntry.targetSdk < 24;
        updateAllSingleLineViews();
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (this.mContractedChild != null) {
            this.mContractedWrapper.onContentUpdated(row);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.onContentUpdated(row);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.onContentUpdated(row);
        }
        applyRemoteInputAndSmartReply();
        updateLegacy();
        this.mForceSelectNextLayout = true;
        this.mPreviousExpandedRemoteInputIntent = null;
        this.mPreviousHeadsUpRemoteInputIntent = null;
        applySystemActions(this.mExpandedChild, notificationEntry);
        applySystemActions(this.mHeadsUpChild, notificationEntry);
    }

    public final void updateAllSingleLineViews() {
        updateSingleLineView();
    }

    public final void updateSingleLineView() {
        if (this.mIsChildInGroup) {
            HybridNotificationView hybridNotificationView = this.mSingleLineView;
            boolean z = hybridNotificationView == null;
            HybridNotificationView bindFromNotification = this.mHybridGroupManager.bindFromNotification(hybridNotificationView, this.mContractedChild, this.mNotificationEntry.getSbn(), this);
            this.mSingleLineView = bindFromNotification;
            if (z) {
                updateViewVisibility(this.mVisibleType, 3, bindFromNotification, bindFromNotification);
                return;
            }
            return;
        }
        HybridNotificationView hybridNotificationView2 = this.mSingleLineView;
        if (hybridNotificationView2 != null) {
            removeView(hybridNotificationView2);
            this.mSingleLineView = null;
        }
    }

    public static boolean hasFreeformRemoteInput(NotificationEntry notificationEntry) {
        if (notificationEntry.getSbn().getNotification().findRemoteInputActionPair(true) != null) {
            return true;
        }
        return false;
    }

    public final void applyRemoteInputAndSmartReply() {
        if (this.mRemoteInputController != null) {
            applyRemoteInput();
        }
        if (this.mCurrentSmartReplyState != null) {
            if (DEBUG) {
                Log.d("NotificationContentView", String.format("Adding suggestions for %s, %d actions, and %d replies.", new Object[]{this.mNotificationEntry.getSbn().getKey(), Integer.valueOf(this.mCurrentSmartReplyState.getSmartActionsList().size()), Integer.valueOf(this.mCurrentSmartReplyState.getSmartRepliesList().size())}));
            }
            applySmartReplyView();
        } else if (DEBUG) {
            Log.d("NotificationContentView", "InflatedSmartReplies are null, don't add smart replies.");
        }
    }

    public final void applyRemoteInput() {
        boolean hasFreeformRemoteInput = hasFreeformRemoteInput(this.mNotificationEntry);
        View view = this.mExpandedChild;
        if (view != null) {
            RemoteInputViewData applyRemoteInput = applyRemoteInput(view, this.mNotificationEntry, hasFreeformRemoteInput, this.mPreviousExpandedRemoteInputIntent, this.mCachedExpandedRemoteInput, this.mCachedExpandedRemoteInputViewController, this.mExpandedWrapper);
            this.mExpandedRemoteInput = applyRemoteInput.mView;
            RemoteInputViewController remoteInputViewController = applyRemoteInput.mController;
            this.mExpandedRemoteInputController = remoteInputViewController;
            if (remoteInputViewController != null) {
                remoteInputViewController.bind();
            }
        } else {
            this.mExpandedRemoteInput = null;
            RemoteInputViewController remoteInputViewController2 = this.mExpandedRemoteInputController;
            if (remoteInputViewController2 != null) {
                remoteInputViewController2.unbind();
            }
            this.mExpandedRemoteInputController = null;
        }
        RemoteInputView remoteInputView = this.mCachedExpandedRemoteInput;
        if (!(remoteInputView == null || remoteInputView == this.mExpandedRemoteInput)) {
            remoteInputView.dispatchFinishTemporaryDetach();
        }
        this.mCachedExpandedRemoteInput = null;
        this.mCachedExpandedRemoteInputViewController = null;
        View view2 = this.mHeadsUpChild;
        if (view2 != null) {
            RemoteInputViewData applyRemoteInput2 = applyRemoteInput(view2, this.mNotificationEntry, hasFreeformRemoteInput, this.mPreviousHeadsUpRemoteInputIntent, this.mCachedHeadsUpRemoteInput, this.mCachedHeadsUpRemoteInputViewController, this.mHeadsUpWrapper);
            this.mHeadsUpRemoteInput = applyRemoteInput2.mView;
            RemoteInputViewController remoteInputViewController3 = applyRemoteInput2.mController;
            this.mHeadsUpRemoteInputController = remoteInputViewController3;
            if (remoteInputViewController3 != null) {
                remoteInputViewController3.bind();
            }
        } else {
            this.mHeadsUpRemoteInput = null;
            RemoteInputViewController remoteInputViewController4 = this.mHeadsUpRemoteInputController;
            if (remoteInputViewController4 != null) {
                remoteInputViewController4.unbind();
            }
            this.mHeadsUpRemoteInputController = null;
        }
        RemoteInputView remoteInputView2 = this.mCachedHeadsUpRemoteInput;
        if (!(remoteInputView2 == null || remoteInputView2 == this.mHeadsUpRemoteInput)) {
            remoteInputView2.dispatchFinishTemporaryDetach();
        }
        this.mCachedHeadsUpRemoteInput = null;
        this.mCachedHeadsUpRemoteInputViewController = null;
    }

    public final RemoteInputViewData applyRemoteInput(View view, NotificationEntry notificationEntry, boolean z, PendingIntent pendingIntent, RemoteInputView remoteInputView, RemoteInputViewController remoteInputViewController, NotificationViewWrapper notificationViewWrapper) {
        RemoteInputViewData remoteInputViewData = new RemoteInputViewData();
        View findViewById = view.findViewById(16908744);
        if (findViewById instanceof FrameLayout) {
            RemoteInputView remoteInputView2 = (RemoteInputView) view.findViewWithTag(RemoteInputView.VIEW_TAG);
            remoteInputViewData.mView = remoteInputView2;
            if (remoteInputView2 != null) {
                remoteInputView2.onNotificationUpdateOrReset();
                remoteInputViewData.mController = remoteInputViewData.mView.getController();
            }
            if (remoteInputViewData.mView == null && z) {
                FrameLayout frameLayout = (FrameLayout) findViewById;
                if (remoteInputView == null) {
                    RemoteInputView inflate = RemoteInputView.inflate(this.mContext, frameLayout, notificationEntry, this.mRemoteInputController);
                    inflate.setVisibility(8);
                    frameLayout.addView(inflate, new FrameLayout.LayoutParams(-1, -1));
                    remoteInputViewData.mView = inflate;
                    RemoteInputViewController controller = this.mRemoteInputSubcomponentFactory.create(inflate, this.mRemoteInputController).getController();
                    remoteInputViewData.mController = controller;
                    remoteInputViewData.mView.setController(controller);
                } else {
                    frameLayout.addView(remoteInputView);
                    remoteInputView.dispatchFinishTemporaryDetach();
                    remoteInputView.requestFocus();
                    remoteInputViewData.mView = remoteInputView;
                    remoteInputViewData.mController = remoteInputViewController;
                }
            }
            if (z) {
                remoteInputViewData.mView.setWrapper(notificationViewWrapper);
                remoteInputViewData.mView.addOnVisibilityChangedListener(new NotificationContentView$$ExternalSyntheticLambda0(this));
                if (pendingIntent != null || remoteInputViewData.mView.isActive()) {
                    Notification.Action[] actionArr = notificationEntry.getSbn().getNotification().actions;
                    if (pendingIntent != null) {
                        remoteInputViewData.mController.setPendingIntent(pendingIntent);
                    }
                    if (remoteInputViewData.mController.updatePendingIntentFromActions(actionArr)) {
                        if (!remoteInputViewData.mView.isActive()) {
                            remoteInputViewData.mView.focus();
                        }
                    } else if (remoteInputViewData.mView.isActive()) {
                        remoteInputViewData.mView.close();
                    }
                }
            }
            if (remoteInputViewData.mView != null) {
                remoteInputViewData.mView.setBackgroundTintColor(notificationEntry.getRow().getCurrentBackgroundTint(), notificationEntry.getSbn().getNotification().isColorized());
            }
        }
        return remoteInputViewData;
    }

    public void updateBubbleButton(NotificationEntry notificationEntry) {
        applyBubbleAction(this.mExpandedChild, notificationEntry);
    }

    public final void applySystemActions(View view, NotificationEntry notificationEntry) {
        applySnoozeAction(view);
        applyBubbleAction(view, notificationEntry);
    }

    public final void applyBubbleAction(View view, NotificationEntry notificationEntry) {
        int i;
        int i2;
        if (view != null && this.mContainingNotification != null && this.mPeopleIdentifier != null) {
            ImageView imageView = (ImageView) view.findViewById(16908831);
            View findViewById = view.findViewById(16908744);
            if (imageView != null && findViewById != null) {
                boolean z = true;
                boolean z2 = this.mPeopleIdentifier.getPeopleNotificationType(notificationEntry) >= 2;
                if (!BubblesManager.areBubblesEnabled(this.mContext, notificationEntry.getSbn().getUser()) || !z2 || notificationEntry.getBubbleMetadata() == null) {
                    z = false;
                }
                if (z) {
                    Context context = this.mContext;
                    if (notificationEntry.isBubble()) {
                        i = R$drawable.bubble_ic_stop_bubble;
                    } else {
                        i = R$drawable.bubble_ic_create_bubble;
                    }
                    Drawable drawable = context.getDrawable(i);
                    Resources resources = this.mContext.getResources();
                    if (notificationEntry.isBubble()) {
                        i2 = R$string.notification_conversation_unbubble;
                    } else {
                        i2 = R$string.notification_conversation_bubble;
                    }
                    imageView.setContentDescription(resources.getString(i2));
                    imageView.setImageDrawable(drawable);
                    imageView.setOnClickListener(this.mContainingNotification.getBubbleClickListener());
                    imageView.setVisibility(0);
                    findViewById.setVisibility(0);
                    return;
                }
                imageView.setVisibility(8);
            }
        }
    }

    public final void applySnoozeAction(View view) {
        if (view != null && this.mContainingNotification != null) {
            ImageView imageView = (ImageView) view.findViewById(16909519);
            View findViewById = view.findViewById(16908744);
            if (imageView != null && findViewById != null) {
                boolean z = Settings.Secure.getInt(this.mContext.getContentResolver(), "show_notification_snooze", 0) == 1;
                boolean isEnabled = true ^ imageView.isEnabled();
                if (!z || isEnabled) {
                    imageView.setVisibility(8);
                    return;
                }
                Context context = this.mContext;
                int i = R$drawable.ic_snooze;
                imageView.setImageDrawable(context.getDrawable(i));
                Context context2 = this.mContext;
                int i2 = R$string.notification_menu_snooze_description;
                NotificationMenuRow.NotificationMenuItem notificationMenuItem = new NotificationMenuRow.NotificationMenuItem(this.mContext, context2.getString(i2), (NotificationSnooze) LayoutInflater.from(this.mContext).inflate(R$layout.notification_snooze, (ViewGroup) null, false), i);
                imageView.setContentDescription(this.mContext.getResources().getString(i2));
                imageView.setOnClickListener(this.mContainingNotification.getSnoozeClickListener(notificationMenuItem));
                imageView.setVisibility(0);
                findViewById.setVisibility(0);
            }
        }
    }

    public final void applySmartReplyView() {
        int i;
        int i2;
        boolean z;
        View view = this.mContractedChild;
        if (view != null) {
            applyExternalSmartReplyState(view, this.mCurrentSmartReplyState);
        }
        View view2 = this.mExpandedChild;
        if (view2 != null) {
            applyExternalSmartReplyState(view2, this.mCurrentSmartReplyState);
            SmartReplyView applySmartReplyView = applySmartReplyView(this.mExpandedChild, this.mCurrentSmartReplyState, this.mNotificationEntry, this.mExpandedInflatedSmartReplies);
            this.mExpandedSmartReplyView = applySmartReplyView;
            if (applySmartReplyView != null) {
                SmartReplyView.SmartReplies smartReplies = this.mCurrentSmartReplyState.getSmartReplies();
                SmartReplyView.SmartActions smartActions = this.mCurrentSmartReplyState.getSmartActions();
                if (!(smartReplies == null && smartActions == null)) {
                    boolean z2 = false;
                    if (smartReplies == null) {
                        i = 0;
                    } else {
                        i = smartReplies.choices.size();
                    }
                    if (smartActions == null) {
                        i2 = 0;
                    } else {
                        i2 = smartActions.actions.size();
                    }
                    if (smartReplies == null) {
                        z = smartActions.fromAssistant;
                    } else {
                        z = smartReplies.fromAssistant;
                    }
                    boolean z3 = z;
                    if (smartReplies != null && this.mSmartReplyConstants.getEffectiveEditChoicesBeforeSending(smartReplies.remoteInput.getEditChoicesBeforeSending())) {
                        z2 = true;
                    }
                    this.mSmartReplyController.smartSuggestionsAdded(this.mNotificationEntry, i, i2, z3, z2);
                }
            }
        }
        View view3 = this.mHeadsUpChild;
        if (view3 != null) {
            applyExternalSmartReplyState(view3, this.mCurrentSmartReplyState);
            if (this.mSmartReplyConstants.getShowInHeadsUp()) {
                this.mHeadsUpSmartReplyView = applySmartReplyView(this.mHeadsUpChild, this.mCurrentSmartReplyState, this.mNotificationEntry, this.mHeadsUpInflatedSmartReplies);
            }
        }
    }

    public final void applyExternalSmartReplyState(View view, InflatedSmartReplyState inflatedSmartReplyState) {
        List<Integer> list;
        boolean z = inflatedSmartReplyState != null && inflatedSmartReplyState.getHasPhishingAction();
        View findViewById = view.findViewById(16909349);
        if (findViewById != null) {
            if (DEBUG) {
                Log.d("NotificationContentView", "Setting 'phishing_alert' view visible=" + z + ".");
            }
            findViewById.setVisibility(z ? 0 : 8);
        }
        if (inflatedSmartReplyState != null) {
            list = inflatedSmartReplyState.getSuppressedActionIndices();
        } else {
            list = Collections.emptyList();
        }
        ViewGroup viewGroup = (ViewGroup) view.findViewById(16908743);
        if (viewGroup != null) {
            if (DEBUG && !list.isEmpty()) {
                Log.d("NotificationContentView", "Suppressing actions with indices: " + list);
            }
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                Object tag = childAt.getTag(16909280);
                childAt.setVisibility((tag instanceof Integer) && list.contains(tag) ? 8 : 0);
            }
        }
    }

    public static SmartReplyView applySmartReplyView(View view, InflatedSmartReplyState inflatedSmartReplyState, NotificationEntry notificationEntry, InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder) {
        View findViewById = view.findViewById(16909511);
        SmartReplyView smartReplyView = null;
        if (!(findViewById instanceof LinearLayout)) {
            return null;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById;
        if (!SmartReplyStateInflaterKt.shouldShowSmartReplyView(notificationEntry, inflatedSmartReplyState)) {
            linearLayout.setVisibility(8);
            return null;
        }
        int childCount = linearLayout.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt.getId() == R$id.smart_reply_view && (childAt instanceof SmartReplyView)) {
                break;
            }
            i++;
        }
        if (i < childCount) {
            linearLayout.removeViewAt(i);
        }
        if (!(inflatedSmartReplyViewHolder == null || inflatedSmartReplyViewHolder.getSmartReplyView() == null)) {
            smartReplyView = inflatedSmartReplyViewHolder.getSmartReplyView();
            linearLayout.addView(smartReplyView, i);
        }
        if (smartReplyView != null) {
            smartReplyView.resetSmartSuggestions(linearLayout);
            smartReplyView.addPreInflatedButtons(inflatedSmartReplyViewHolder.getSmartSuggestionButtons());
            smartReplyView.setBackgroundTintColor(notificationEntry.getRow().getCurrentBackgroundTint(), notificationEntry.getSbn().getNotification().isColorized());
            linearLayout.setVisibility(0);
        }
        return smartReplyView;
    }

    public void setExpandedInflatedSmartReplies(InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder) {
        this.mExpandedInflatedSmartReplies = inflatedSmartReplyViewHolder;
        if (inflatedSmartReplyViewHolder == null) {
            this.mExpandedSmartReplyView = null;
        }
    }

    public void setHeadsUpInflatedSmartReplies(InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder) {
        this.mHeadsUpInflatedSmartReplies = inflatedSmartReplyViewHolder;
        if (inflatedSmartReplyViewHolder == null) {
            this.mHeadsUpSmartReplyView = null;
        }
    }

    public void setInflatedSmartReplyState(InflatedSmartReplyState inflatedSmartReplyState) {
        this.mCurrentSmartReplyState = inflatedSmartReplyState;
    }

    public InflatedSmartReplyState getCurrentSmartReplyState() {
        return this.mCurrentSmartReplyState;
    }

    public void closeRemoteInput() {
        RemoteInputView remoteInputView = this.mHeadsUpRemoteInput;
        if (remoteInputView != null) {
            remoteInputView.close();
        }
        RemoteInputView remoteInputView2 = this.mExpandedRemoteInput;
        if (remoteInputView2 != null) {
            remoteInputView2.close();
        }
    }

    public void setRemoteInputController(RemoteInputController remoteInputController) {
        this.mRemoteInputController = remoteInputController;
    }

    public void setExpandClickListener(View.OnClickListener onClickListener) {
        this.mExpandClickListener = onClickListener;
    }

    public void updateExpandButtons(boolean z) {
        updateExpandButtonsDuringLayout(z, false);
    }

    public final void updateExpandButtonsDuringLayout(boolean z, boolean z2) {
        this.mExpandable = z;
        View view = this.mExpandedChild;
        boolean z3 = false;
        if (!(view == null || view.getHeight() == 0 || ((this.mIsHeadsUp || this.mHeadsUpAnimatingAway) && this.mHeadsUpChild != null && this.mContainingNotification.canShowHeadsUp() ? this.mExpandedChild.getHeight() > this.mHeadsUpChild.getHeight() : this.mContractedChild != null && this.mExpandedChild.getHeight() > this.mContractedChild.getHeight()))) {
            z = false;
        }
        if (z2 && this.mIsContentExpandable != z) {
            z3 = true;
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        if (this.mContractedChild != null) {
            this.mContractedWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        this.mIsContentExpandable = z;
    }

    public NotificationViewWrapper getNotificationViewWrapper() {
        NotificationViewWrapper notificationViewWrapper;
        NotificationViewWrapper notificationViewWrapper2;
        NotificationViewWrapper notificationViewWrapper3;
        if (this.mContractedChild != null && (notificationViewWrapper3 = this.mContractedWrapper) != null) {
            return notificationViewWrapper3;
        }
        if (this.mExpandedChild != null && (notificationViewWrapper2 = this.mExpandedWrapper) != null) {
            return notificationViewWrapper2;
        }
        if (this.mHeadsUpChild == null || (notificationViewWrapper = this.mHeadsUpWrapper) == null) {
            return null;
        }
        return notificationViewWrapper;
    }

    public void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        if (this.mContractedChild != null) {
            this.mContractedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.setFeedbackIcon(feedbackIcon);
        }
    }

    public void setRecentlyAudiblyAlerted(boolean z) {
        if (this.mContractedChild != null) {
            this.mContractedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.setRecentlyAudiblyAlerted(z);
        }
    }

    public void setContainingNotification(ExpandableNotificationRow expandableNotificationRow) {
        this.mContainingNotification = expandableNotificationRow;
    }

    public void requestSelectLayout(boolean z) {
        selectLayout(z, false);
    }

    public void reInflateViews() {
        HybridNotificationView hybridNotificationView;
        if (this.mIsChildInGroup && (hybridNotificationView = this.mSingleLineView) != null) {
            removeView(hybridNotificationView);
            this.mSingleLineView = null;
            updateAllSingleLineViews();
        }
    }

    public void setUserExpanding(boolean z) {
        this.mUserExpanding = z;
        if (z) {
            this.mTransformationStartVisibleType = this.mVisibleType;
            return;
        }
        this.mTransformationStartVisibleType = -1;
        int calculateVisibleType = calculateVisibleType();
        this.mVisibleType = calculateVisibleType;
        updateViewVisibilities(calculateVisibleType);
        updateBackgroundColor(false);
    }

    public void setSingleLineWidthIndention(int i) {
        if (i != this.mSingleLineWidthIndention) {
            this.mSingleLineWidthIndention = i;
            this.mContainingNotification.forceLayout();
            forceLayout();
        }
    }

    public HybridNotificationView getSingleLineView() {
        return this.mSingleLineView;
    }

    public void setRemoved() {
        RemoteInputView remoteInputView = this.mExpandedRemoteInput;
        if (remoteInputView != null) {
            remoteInputView.setRemoved();
        }
        RemoteInputView remoteInputView2 = this.mHeadsUpRemoteInput;
        if (remoteInputView2 != null) {
            remoteInputView2.setRemoved();
        }
        NotificationViewWrapper notificationViewWrapper = this.mExpandedWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setRemoved();
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mContractedWrapper;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setRemoved();
        }
        NotificationViewWrapper notificationViewWrapper3 = this.mHeadsUpWrapper;
        if (notificationViewWrapper3 != null) {
            notificationViewWrapper3.setRemoved();
        }
    }

    public void setContentHeightAnimating(boolean z) {
        if (!z) {
            this.mContentHeightAtAnimationStart = -1;
        }
    }

    @VisibleForTesting
    public boolean isAnimatingVisibleType() {
        return this.mAnimationStartVisibleType != -1;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mHeadsUpAnimatingAway = z;
        selectLayout(false, true);
    }

    public void setFocusOnVisibilityChange() {
        this.mFocusOnVisibilityChange = true;
    }

    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        if (z) {
            fireExpandedVisibleListenerIfVisible();
        }
    }

    public void setOnExpandedVisibleListener(Runnable runnable) {
        this.mExpandedVisibleListener = runnable;
        fireExpandedVisibleListenerIfVisible();
    }

    public void performWhenContentInactive(int i, Runnable runnable) {
        View viewForVisibleType = getViewForVisibleType(i);
        if (viewForVisibleType == null || isContentViewInactive(i)) {
            runnable.run();
        } else {
            this.mOnContentViewInactiveListeners.put(viewForVisibleType, runnable);
        }
    }

    public void removeContentInactiveRunnable(int i) {
        View viewForVisibleType = getViewForVisibleType(i);
        if (viewForVisibleType != null) {
            this.mOnContentViewInactiveListeners.remove(viewForVisibleType);
        }
    }

    public boolean isContentViewInactive(int i) {
        return isContentViewInactive(getViewForVisibleType(i));
    }

    public final boolean isContentViewInactive(View view) {
        if (view == null || !isShown()) {
            return true;
        }
        if (view.getVisibility() == 0 || getViewForVisibleType(this.mVisibleType) == view) {
            return false;
        }
        return true;
    }

    public void onChildVisibilityChanged(View view, int i, int i2) {
        Runnable remove;
        super.onChildVisibilityChanged(view, i, i2);
        if (isContentViewInactive(view) && (remove = this.mOnContentViewInactiveListeners.remove(view)) != null) {
            remove.run();
        }
    }

    public boolean shouldClipToRounding(boolean z, boolean z2) {
        boolean shouldClipToRounding = shouldClipToRounding(getVisibleType(), z, z2);
        return this.mUserExpanding ? shouldClipToRounding | shouldClipToRounding(this.mTransformationStartVisibleType, z, z2) : shouldClipToRounding;
    }

    public final boolean shouldClipToRounding(int i, boolean z, boolean z2) {
        NotificationViewWrapper visibleWrapper = getVisibleWrapper(i);
        if (visibleWrapper == null) {
            return false;
        }
        return visibleWrapper.shouldClipToRounding(z, z2);
    }

    public CharSequence getActiveRemoteInputText() {
        RemoteInputView remoteInputView = this.mExpandedRemoteInput;
        if (remoteInputView != null && remoteInputView.isActive()) {
            return this.mExpandedRemoteInput.getText();
        }
        RemoteInputView remoteInputView2 = this.mHeadsUpRemoteInput;
        if (remoteInputView2 == null || !remoteInputView2.isActive()) {
            return null;
        }
        return this.mHeadsUpRemoteInput.getText();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float y = motionEvent.getY();
        RemoteInputView remoteInputForView = getRemoteInputForView(getViewForVisibleType(this.mVisibleType));
        if (remoteInputForView != null && remoteInputForView.getVisibility() == 0) {
            int height = this.mUnrestrictedContentHeight - remoteInputForView.getHeight();
            if (y <= ((float) this.mUnrestrictedContentHeight) && y >= ((float) height)) {
                motionEvent.offsetLocation(0.0f, (float) (-height));
                return remoteInputForView.dispatchTouchEvent(motionEvent);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean pointInView(float f, float f2, float f3) {
        return f >= (-f3) && f2 >= ((float) this.mClipTopAmount) - f3 && f < ((float) (this.mRight - this.mLeft)) + f3 && f2 < ((float) this.mUnrestrictedContentHeight) + f3;
    }

    public final RemoteInputView getRemoteInputForView(View view) {
        if (view == this.mExpandedChild) {
            return this.mExpandedRemoteInput;
        }
        if (view == this.mHeadsUpChild) {
            return this.mHeadsUpRemoteInput;
        }
        return null;
    }

    public int getExpandHeight() {
        int i;
        if (this.mExpandedChild != null) {
            i = 1;
        } else if (this.mContractedChild == null) {
            return getMinHeight();
        } else {
            i = 0;
        }
        return getViewHeight(i) + getExtraRemoteInputHeight(this.mExpandedRemoteInput);
    }

    public int getHeadsUpHeight(boolean z) {
        int i;
        if (this.mHeadsUpChild != null) {
            i = 2;
        } else if (this.mContractedChild == null) {
            return getMinHeight();
        } else {
            i = 0;
        }
        return getViewHeight(i, z) + getExtraRemoteInputHeight(this.mHeadsUpRemoteInput) + getExtraRemoteInputHeight(this.mExpandedRemoteInput);
    }

    public void setRemoteInputVisible(boolean z) {
        this.mRemoteInputVisible = z;
        setClipChildren(!z);
    }

    public void setClipChildren(boolean z) {
        super.setClipChildren(z && !this.mRemoteInputVisible);
    }

    public void setHeaderVisibleAmount(float f) {
        NotificationViewWrapper notificationViewWrapper = this.mContractedWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setHeaderVisibleAmount(f);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mHeadsUpWrapper;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setHeaderVisibleAmount(f);
        }
        NotificationViewWrapper notificationViewWrapper3 = this.mExpandedWrapper;
        if (notificationViewWrapper3 != null) {
            notificationViewWrapper3.setHeaderVisibleAmount(f);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("contentView visibility: " + getVisibility());
        printWriter.print(", alpha: " + getAlpha());
        printWriter.print(", clipBounds: " + getClipBounds());
        printWriter.print(", contentHeight: " + this.mContentHeight);
        printWriter.print(", visibleType: " + this.mVisibleType);
        View viewForVisibleType = getViewForVisibleType(this.mVisibleType);
        printWriter.print(", visibleView ");
        if (viewForVisibleType != null) {
            printWriter.print(" visibility: " + viewForVisibleType.getVisibility());
            printWriter.print(", alpha: " + viewForVisibleType.getAlpha());
            printWriter.print(", clipBounds: " + viewForVisibleType.getClipBounds());
        } else {
            printWriter.print("null");
        }
        printWriter.println();
    }

    public void dumpSmartReplies(IndentingPrintWriter indentingPrintWriter) {
        if (this.mHeadsUpSmartReplyView != null) {
            indentingPrintWriter.println("HeadsUp SmartReplyView:");
            indentingPrintWriter.increaseIndent();
            this.mHeadsUpSmartReplyView.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        if (this.mExpandedSmartReplyView != null) {
            indentingPrintWriter.println("Expanded SmartReplyView:");
            indentingPrintWriter.increaseIndent();
            this.mExpandedSmartReplyView.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
    }

    public RemoteInputView getExpandedRemoteInput() {
        return this.mExpandedRemoteInput;
    }

    public View getShelfTransformationTarget() {
        NotificationViewWrapper visibleWrapper = getVisibleWrapper(this.mVisibleType);
        if (visibleWrapper != null) {
            return visibleWrapper.getShelfTransformationTarget();
        }
        return null;
    }

    public int getOriginalIconColor() {
        NotificationViewWrapper visibleWrapper = getVisibleWrapper(this.mVisibleType);
        if (visibleWrapper != null) {
            return visibleWrapper.getOriginalIconColor();
        }
        return 1;
    }

    public void setNotificationFaded(boolean z) {
        NotificationViewWrapper notificationViewWrapper = this.mContractedWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setNotificationFaded(z);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mHeadsUpWrapper;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setNotificationFaded(z);
        }
        NotificationViewWrapper notificationViewWrapper3 = this.mExpandedWrapper;
        if (notificationViewWrapper3 != null) {
            notificationViewWrapper3.setNotificationFaded(z);
        }
        HybridNotificationView hybridNotificationView = this.mSingleLineView;
        if (hybridNotificationView != null) {
            hybridNotificationView.setNotificationFaded(z);
        }
    }

    public boolean requireRowToHaveOverlappingRendering() {
        RemoteInputView remoteInputView = this.mHeadsUpRemoteInput;
        if (remoteInputView != null && remoteInputView.isActive()) {
            return true;
        }
        RemoteInputView remoteInputView2 = this.mExpandedRemoteInput;
        if (remoteInputView2 == null || !remoteInputView2.isActive()) {
            return false;
        }
        return true;
    }

    public static class RemoteInputViewData {
        public RemoteInputViewController mController;
        public RemoteInputView mView;

        public RemoteInputViewData() {
        }
    }
}
