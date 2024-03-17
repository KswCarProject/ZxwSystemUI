package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.util.HashSet;

public class NotificationRoundnessManager {
    public HashSet<ExpandableView> mAnimatedChildren;
    public float mAppearFraction;
    public boolean mExpanded;
    public final ExpandableView[] mFirstInSectionViews;
    public boolean mIsClearAllInProgress;
    public final ExpandableView[] mLastInSectionViews;
    public boolean mRoundForPulsingViews;
    public Runnable mRoundingChangedCallback;
    public ExpandableView mSwipedView = null;
    public final ExpandableView[] mTmpFirstInSectionViews;
    public final ExpandableView[] mTmpLastInSectionViews;
    public ExpandableNotificationRow mTrackedHeadsUp;
    public ExpandableView mViewAfterSwipedView = null;
    public ExpandableView mViewBeforeSwipedView = null;

    public NotificationRoundnessManager(NotificationSectionsFeatureManager notificationSectionsFeatureManager) {
        int numberOfBuckets = notificationSectionsFeatureManager.getNumberOfBuckets();
        this.mFirstInSectionViews = new ExpandableView[numberOfBuckets];
        this.mLastInSectionViews = new ExpandableView[numberOfBuckets];
        this.mTmpFirstInSectionViews = new ExpandableView[numberOfBuckets];
        this.mTmpLastInSectionViews = new ExpandableView[numberOfBuckets];
    }

    public void updateView(ExpandableView expandableView, boolean z) {
        if (updateViewWithoutCallback(expandableView, z)) {
            this.mRoundingChangedCallback.run();
        }
    }

    public boolean isViewAffectedBySwipe(ExpandableView expandableView) {
        return expandableView != null && (expandableView == this.mSwipedView || expandableView == this.mViewBeforeSwipedView || expandableView == this.mViewAfterSwipedView);
    }

    public boolean updateViewWithoutCallback(ExpandableView expandableView, boolean z) {
        if (expandableView == null || expandableView == this.mViewBeforeSwipedView || expandableView == this.mViewAfterSwipedView) {
            return false;
        }
        float roundnessFraction = getRoundnessFraction(expandableView, true);
        float roundnessFraction2 = getRoundnessFraction(expandableView, false);
        boolean topRoundness = expandableView.setTopRoundness(roundnessFraction, z);
        boolean bottomRoundness = expandableView.setBottomRoundness(roundnessFraction2, z);
        boolean isFirstInSection = isFirstInSection(expandableView);
        boolean isLastInSection = isLastInSection(expandableView);
        expandableView.setFirstInSection(isFirstInSection);
        expandableView.setLastInSection(isLastInSection);
        if (!isFirstInSection && !isLastInSection) {
            return false;
        }
        if (topRoundness || bottomRoundness) {
            return true;
        }
        return false;
    }

    public final boolean isFirstInSection(ExpandableView expandableView) {
        int i = 0;
        while (true) {
            ExpandableView[] expandableViewArr = this.mFirstInSectionViews;
            if (i >= expandableViewArr.length) {
                return false;
            }
            if (expandableView == expandableViewArr[i]) {
                return true;
            }
            i++;
        }
    }

    public final boolean isLastInSection(ExpandableView expandableView) {
        for (int length = this.mLastInSectionViews.length - 1; length >= 0; length--) {
            if (expandableView == this.mLastInSectionViews[length]) {
                return true;
            }
        }
        return false;
    }

    public void setViewsAffectedBySwipe(ExpandableView expandableView, ExpandableView expandableView2, ExpandableView expandableView3) {
        ExpandableView expandableView4 = this.mViewBeforeSwipedView;
        this.mViewBeforeSwipedView = expandableView;
        if (expandableView4 != null) {
            expandableView4.setBottomRoundness(getRoundnessFraction(expandableView4, false), true);
        }
        if (expandableView != null) {
            expandableView.setBottomRoundness(1.0f, true);
        }
        ExpandableView expandableView5 = this.mSwipedView;
        this.mSwipedView = expandableView2;
        if (expandableView5 != null) {
            float roundnessFraction = getRoundnessFraction(expandableView5, false);
            expandableView5.setTopRoundness(getRoundnessFraction(expandableView5, true), true);
            expandableView5.setBottomRoundness(roundnessFraction, true);
        }
        if (expandableView2 != null) {
            expandableView2.setTopRoundness(1.0f, true);
            expandableView2.setBottomRoundness(1.0f, true);
        }
        ExpandableView expandableView6 = this.mViewAfterSwipedView;
        this.mViewAfterSwipedView = expandableView3;
        if (expandableView6 != null) {
            expandableView6.setTopRoundness(getRoundnessFraction(expandableView6, true), true);
        }
        if (expandableView3 != null) {
            expandableView3.setTopRoundness(1.0f, true);
        }
    }

    public void setClearAllInProgress(boolean z) {
        this.mIsClearAllInProgress = z;
    }

    public final float getRoundnessFraction(ExpandableView expandableView, boolean z) {
        if (expandableView == null) {
            return 0.0f;
        }
        if (expandableView == this.mViewBeforeSwipedView || expandableView == this.mSwipedView || expandableView == this.mViewAfterSwipedView || (((expandableView instanceof ExpandableNotificationRow) && ((ExpandableNotificationRow) expandableView).canViewBeCleared() && this.mIsClearAllInProgress) || expandableView.isPinned() || (expandableView.isHeadsUpAnimatingAway() && !this.mExpanded))) {
            return 1.0f;
        }
        if (isFirstInSection(expandableView) && z) {
            return 1.0f;
        }
        if (isLastInSection(expandableView) && !z) {
            return 1.0f;
        }
        if (expandableView == this.mTrackedHeadsUp) {
            return MathUtils.saturate(1.0f - this.mAppearFraction);
        }
        if (expandableView.showingPulsing() && this.mRoundForPulsingViews) {
            return 1.0f;
        }
        Resources resources = expandableView.getResources();
        return resources.getDimension(R$dimen.notification_corner_radius_small) / resources.getDimension(R$dimen.notification_corner_radius);
    }

    public void setExpanded(float f, float f2) {
        this.mExpanded = f != 0.0f;
        this.mAppearFraction = f2;
        ExpandableNotificationRow expandableNotificationRow = this.mTrackedHeadsUp;
        if (expandableNotificationRow != null) {
            updateView(expandableNotificationRow, false);
        }
    }

    public void updateRoundedChildren(NotificationSection[] notificationSectionArr) {
        for (int i = 0; i < notificationSectionArr.length; i++) {
            ExpandableView[] expandableViewArr = this.mTmpFirstInSectionViews;
            ExpandableView[] expandableViewArr2 = this.mFirstInSectionViews;
            expandableViewArr[i] = expandableViewArr2[i];
            this.mTmpLastInSectionViews[i] = this.mLastInSectionViews[i];
            expandableViewArr2[i] = notificationSectionArr[i].getFirstVisibleChild();
            this.mLastInSectionViews[i] = notificationSectionArr[i].getLastVisibleChild();
        }
        if (handleAddedNewViews(notificationSectionArr, this.mTmpLastInSectionViews, false) || (((handleRemovedOldViews(notificationSectionArr, this.mTmpFirstInSectionViews, true) | false) | handleRemovedOldViews(notificationSectionArr, this.mTmpLastInSectionViews, false)) | handleAddedNewViews(notificationSectionArr, this.mTmpFirstInSectionViews, true))) {
            this.mRoundingChangedCallback.run();
        }
    }

    public final boolean handleRemovedOldViews(NotificationSection[] notificationSectionArr, ExpandableView[] expandableViewArr, boolean z) {
        boolean z2;
        boolean z3;
        ExpandableView expandableView;
        boolean z4 = false;
        for (ExpandableView expandableView2 : expandableViewArr) {
            if (expandableView2 != null) {
                int length = notificationSectionArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z2 = false;
                        break;
                    }
                    NotificationSection notificationSection = notificationSectionArr[i];
                    if (z) {
                        expandableView = notificationSection.getFirstVisibleChild();
                    } else {
                        expandableView = notificationSection.getLastVisibleChild();
                    }
                    if (expandableView != expandableView2) {
                        i++;
                    } else if (expandableView2.isFirstInSection() == isFirstInSection(expandableView2) && expandableView2.isLastInSection() == isLastInSection(expandableView2)) {
                        z3 = false;
                        z2 = true;
                    } else {
                        z2 = true;
                    }
                }
                z3 = z2;
                if (!z2 || z3) {
                    if (!expandableView2.isRemoved()) {
                        updateViewWithoutCallback(expandableView2, expandableView2.isShown());
                    }
                    z4 = true;
                }
            }
        }
        return z4;
    }

    public final boolean handleAddedNewViews(NotificationSection[] notificationSectionArr, ExpandableView[] expandableViewArr, boolean z) {
        boolean z2;
        boolean z3 = false;
        for (NotificationSection notificationSection : notificationSectionArr) {
            ExpandableView firstVisibleChild = z ? notificationSection.getFirstVisibleChild() : notificationSection.getLastVisibleChild();
            if (firstVisibleChild != null) {
                int length = expandableViewArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z2 = false;
                        break;
                    } else if (expandableViewArr[i] == firstVisibleChild) {
                        z2 = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!z2) {
                    updateViewWithoutCallback(firstVisibleChild, firstVisibleChild.isShown() && !this.mAnimatedChildren.contains(firstVisibleChild));
                    z3 = true;
                }
            }
        }
        return z3;
    }

    public void setAnimatedChildren(HashSet<ExpandableView> hashSet) {
        this.mAnimatedChildren = hashSet;
    }

    public void setOnRoundingChangedCallback(Runnable runnable) {
        this.mRoundingChangedCallback = runnable;
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2 = this.mTrackedHeadsUp;
        this.mTrackedHeadsUp = expandableNotificationRow;
        if (expandableNotificationRow2 != null) {
            updateView(expandableNotificationRow2, true);
        }
    }

    public void setShouldRoundPulsingViews(boolean z) {
        this.mRoundForPulsingViews = z;
    }
}
