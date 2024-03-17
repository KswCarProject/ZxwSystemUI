package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class StackStateAnimator {
    public AnimationFilter mAnimationFilter = new AnimationFilter();
    public Stack<AnimatorListenerAdapter> mAnimationListenerPool = new Stack<>();
    public final AnimationProperties mAnimationProperties;
    public HashSet<Animator> mAnimatorSet = new HashSet<>();
    public ValueAnimator mBottomOverScrollAnimator;
    public long mCurrentAdditionalDelay;
    public long mCurrentLength;
    public final int mGoToFullShadeAppearingTranslation;
    public HashSet<View> mHeadsUpAppearChildren = new HashSet<>();
    public int mHeadsUpAppearHeightBottom;
    public HashSet<View> mHeadsUpDisappearChildren = new HashSet<>();
    public NotificationStackScrollLayout mHostLayout;
    public StackStateLogger mLogger;
    public ArrayList<View> mNewAddChildren = new ArrayList<>();
    public ArrayList<NotificationStackScrollLayout.AnimationEvent> mNewEvents = new ArrayList<>();
    public final int mPulsingAppearingTranslation;
    public boolean mShadeExpanded;
    public NotificationShelf mShelf;
    public int[] mTmpLocation = new int[2];
    public final ExpandableViewState mTmpState = new ExpandableViewState();
    public ValueAnimator mTopOverScrollAnimator;
    public ArrayList<ExpandableView> mTransientViewsToRemove = new ArrayList<>();

    public StackStateAnimator(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mHostLayout = notificationStackScrollLayout;
        this.mGoToFullShadeAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(R$dimen.go_to_full_shade_appearing_translation);
        this.mPulsingAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(R$dimen.pulsing_notification_appear_translation);
        this.mAnimationProperties = new AnimationProperties() {
            public AnimationFilter getAnimationFilter() {
                return StackStateAnimator.this.mAnimationFilter;
            }

            public AnimatorListenerAdapter getAnimationFinishListener(Property property) {
                return StackStateAnimator.this.getGlobalAnimationFinishedListener();
            }

            public boolean wasAdded(View view) {
                return StackStateAnimator.this.mNewAddChildren.contains(view);
            }
        };
    }

    public void setLogger(StackStateLogger stackStateLogger) {
        this.mLogger = stackStateLogger;
    }

    public boolean isRunning() {
        return !this.mAnimatorSet.isEmpty();
    }

    public void startAnimationForEvents(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList, long j) {
        processAnimationEvents(arrayList);
        int childCount = this.mHostLayout.getChildCount();
        this.mAnimationFilter.applyCombination(this.mNewEvents);
        this.mCurrentAdditionalDelay = j;
        this.mCurrentLength = NotificationStackScrollLayout.AnimationEvent.combineLength(this.mNewEvents);
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) this.mHostLayout.getChildAt(i2);
            ExpandableViewState viewState = expandableView.getViewState();
            if (!(viewState == null || expandableView.getVisibility() == 8 || applyWithoutAnimation(expandableView, viewState))) {
                if (this.mAnimationProperties.wasAdded(expandableView) && i < 5) {
                    i++;
                }
                initAnimationProperties(expandableView, viewState, i);
                viewState.animateTo(expandableView, this.mAnimationProperties);
            }
        }
        if (!isRunning()) {
            onAnimationFinished();
        }
        this.mHeadsUpAppearChildren.clear();
        this.mHeadsUpDisappearChildren.clear();
        this.mNewEvents.clear();
        this.mNewAddChildren.clear();
    }

    public final void initAnimationProperties(ExpandableView expandableView, ExpandableViewState expandableViewState, int i) {
        boolean wasAdded = this.mAnimationProperties.wasAdded(expandableView);
        this.mAnimationProperties.duration = this.mCurrentLength;
        adaptDurationWhenGoingToFullShade(expandableView, expandableViewState, wasAdded, i);
        this.mAnimationProperties.delay = 0;
        if (!wasAdded) {
            if (!this.mAnimationFilter.hasDelays) {
                return;
            }
            if (expandableViewState.yTranslation == expandableView.getTranslationY() && expandableViewState.zTranslation == expandableView.getTranslationZ() && expandableViewState.alpha == expandableView.getAlpha() && expandableViewState.height == expandableView.getActualHeight() && expandableViewState.clipTopAmount == expandableView.getClipTopAmount()) {
                return;
            }
        }
        this.mAnimationProperties.delay = this.mCurrentAdditionalDelay + calculateChildAnimationDelay(expandableViewState, i);
    }

    public final void adaptDurationWhenGoingToFullShade(ExpandableView expandableView, ExpandableViewState expandableViewState, boolean z, int i) {
        boolean z2 = expandableView instanceof StackScrollerDecorView;
        int i2 = 0;
        if ((z || z2) && this.mAnimationFilter.hasGoToFullShadeEvent) {
            if (!z2) {
                i2 = this.mGoToFullShadeAppearingTranslation;
                this.mAnimationProperties.duration = ((long) (((float) Math.pow((double) i, 0.699999988079071d)) * 100.0f)) + 514;
            }
            expandableView.setTranslationY(expandableViewState.yTranslation + ((float) i2));
        }
    }

    public final boolean applyWithoutAnimation(ExpandableView expandableView, ExpandableViewState expandableViewState) {
        if (this.mShadeExpanded || ViewState.isAnimatingY(expandableView) || this.mHeadsUpDisappearChildren.contains(expandableView) || this.mHeadsUpAppearChildren.contains(expandableView) || NotificationStackScrollLayout.isPinnedHeadsUp(expandableView)) {
            return false;
        }
        expandableViewState.applyToView(expandableView);
        return true;
    }

    public final long calculateChildAnimationDelay(ExpandableViewState expandableViewState, int i) {
        ExpandableView expandableView;
        AnimationFilter animationFilter = this.mAnimationFilter;
        if (animationFilter.hasGoToFullShadeEvent) {
            return calculateDelayGoToFullShade(expandableViewState, i);
        }
        long j = animationFilter.customDelay;
        if (j != -1) {
            return j;
        }
        long j2 = 0;
        Iterator<NotificationStackScrollLayout.AnimationEvent> it = this.mNewEvents.iterator();
        while (it.hasNext()) {
            NotificationStackScrollLayout.AnimationEvent next = it.next();
            long j3 = 80;
            int i2 = next.animationType;
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 == 2) {
                        j3 = 32;
                    }
                }
                int i3 = expandableViewState.notGoneIndex;
                View view = next.viewAfterChangingView;
                if (view == null) {
                    expandableView = this.mHostLayout.getLastChildNotGone();
                } else {
                    expandableView = (ExpandableView) view;
                }
                if (expandableView != null) {
                    int i4 = expandableView.getViewState().notGoneIndex;
                    if (i3 >= i4) {
                        i3++;
                    }
                    j2 = Math.max(((long) Math.max(0, Math.min(2, Math.abs(i3 - i4) - 1))) * j3, j2);
                }
            } else {
                j2 = Math.max(((long) (2 - Math.max(0, Math.min(2, Math.abs(expandableViewState.notGoneIndex - next.mChangingView.getViewState().notGoneIndex) - 1)))) * 80, j2);
            }
        }
        return j2;
    }

    public final long calculateDelayGoToFullShade(ExpandableViewState expandableViewState, int i) {
        int notGoneIndex = this.mShelf.getNotGoneIndex();
        float f = (float) expandableViewState.notGoneIndex;
        float f2 = (float) notGoneIndex;
        long j = 0;
        if (f > f2) {
            j = 0 + ((long) (((double) (((float) Math.pow((double) i, 0.699999988079071d)) * 48.0f)) * 0.25d));
            f = f2;
        }
        return j + ((long) (((float) Math.pow((double) f, 0.699999988079071d)) * 48.0f));
    }

    public final AnimatorListenerAdapter getGlobalAnimationFinishedListener() {
        if (!this.mAnimationListenerPool.empty()) {
            return this.mAnimationListenerPool.pop();
        }
        return new AnimatorListenerAdapter() {
            public boolean mWasCancelled;

            public void onAnimationEnd(Animator animator) {
                StackStateAnimator.this.mAnimatorSet.remove(animator);
                if (StackStateAnimator.this.mAnimatorSet.isEmpty() && !this.mWasCancelled) {
                    StackStateAnimator.this.onAnimationFinished();
                }
                StackStateAnimator.this.mAnimationListenerPool.push(this);
            }

            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }

            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                StackStateAnimator.this.mAnimatorSet.add(animator);
            }
        };
    }

    public final void onAnimationFinished() {
        this.mHostLayout.onChildAnimationFinished();
        Iterator<ExpandableView> it = this.mTransientViewsToRemove.iterator();
        while (it.hasNext()) {
            it.next().removeFromTransientContainer();
        }
        this.mTransientViewsToRemove.clear();
    }

    public final void processAnimationEvents(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList) {
        boolean z;
        String str;
        boolean z2;
        Runnable runnable;
        float f;
        Iterator<NotificationStackScrollLayout.AnimationEvent> it = arrayList.iterator();
        while (it.hasNext()) {
            NotificationStackScrollLayout.AnimationEvent next = it.next();
            ExpandableView expandableView = next.mChangingView;
            boolean z3 = true;
            Runnable runnable2 = null;
            if (!(expandableView instanceof ExpandableNotificationRow) || this.mLogger == null) {
                str = null;
                z2 = false;
                z = false;
            } else {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                z2 = expandableNotificationRow.isHeadsUp();
                str = expandableNotificationRow.getEntry().getKey();
                z = true;
            }
            int i = next.animationType;
            if (i == 0) {
                ExpandableViewState viewState = expandableView.getViewState();
                if (viewState != null && !viewState.gone) {
                    if (z && z2) {
                        this.mLogger.logHUNViewAppearingWithAddEvent(str);
                    }
                    viewState.applyToView(expandableView);
                    this.mNewAddChildren.add(expandableView);
                }
            } else if (i == 1) {
                if (expandableView.getVisibility() != 0) {
                    expandableView.removeFromTransientContainer();
                } else {
                    if (next.viewAfterChangingView != null) {
                        float translationY = expandableView.getTranslationY();
                        if (expandableView instanceof ExpandableNotificationRow) {
                            View view = next.viewAfterChangingView;
                            if (view instanceof ExpandableNotificationRow) {
                                ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) expandableView;
                                ExpandableNotificationRow expandableNotificationRow3 = (ExpandableNotificationRow) view;
                                if (expandableNotificationRow2.isRemoved() && expandableNotificationRow2.wasChildInGroupWhenRemoved() && !expandableNotificationRow3.isChildInGroup()) {
                                    translationY = expandableNotificationRow2.getTranslationWhenRemoved();
                                }
                            }
                        }
                        float actualHeight = (float) expandableView.getActualHeight();
                        f = Math.max(Math.min(((((ExpandableView) next.viewAfterChangingView).getViewState().yTranslation - (translationY + (actualHeight / 2.0f))) * 2.0f) / actualHeight, 1.0f), -1.0f);
                    } else {
                        f = -1.0f;
                    }
                    Runnable stackStateAnimator$$ExternalSyntheticLambda0 = new StackStateAnimator$$ExternalSyntheticLambda0(expandableView);
                    if (z && z2) {
                        this.mLogger.logHUNViewDisappearingWithRemoveEvent(str);
                        stackStateAnimator$$ExternalSyntheticLambda0 = new StackStateAnimator$$ExternalSyntheticLambda1(this, str, expandableView);
                    }
                    expandableView.performRemoveAnimation(464, 0, f, false, 0.0f, stackStateAnimator$$ExternalSyntheticLambda0, (AnimatorListenerAdapter) null);
                }
            } else if (i == 2) {
                if (this.mHostLayout.isFullySwipedOut(expandableView)) {
                    expandableView.removeFromTransientContainer();
                }
            } else if (i == 10) {
                ((ExpandableNotificationRow) next.mChangingView).prepareExpansionChanged();
            } else if (i == 11) {
                this.mTmpState.copyFrom(expandableView.getViewState());
                if (next.headsUpFromBottom) {
                    this.mTmpState.yTranslation = (float) this.mHeadsUpAppearHeightBottom;
                } else {
                    expandableView.performAddAnimation(0, 400, true, z ? new StackStateAnimator$$ExternalSyntheticLambda2(this, str) : null);
                }
                this.mHeadsUpAppearChildren.add(expandableView);
                if (z) {
                    this.mLogger.logHUNViewAppearing(str);
                }
                this.mTmpState.applyToView(expandableView);
            } else if (i == 12 || i == 13) {
                this.mHeadsUpDisappearChildren.add(expandableView);
                if (expandableView.getParent() == null) {
                    this.mHostLayout.addTransientView(expandableView, 0);
                    expandableView.setTransientContainer(this.mHostLayout);
                    this.mTmpState.initFrom(expandableView);
                    runnable2 = new StackStateAnimator$$ExternalSyntheticLambda0(expandableView);
                }
                float f2 = 0.0f;
                if (expandableView instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow4 = (ExpandableNotificationRow) expandableView;
                    z3 = true ^ expandableNotificationRow4.isDismissed();
                    NotificationEntry entry = expandableNotificationRow4.getEntry();
                    StatusBarIconView statusBarIcon = entry.getIcons().getStatusBarIcon();
                    StatusBarIconView centeredIcon = entry.getIcons().getCenteredIcon();
                    if (!(centeredIcon == null || centeredIcon.getParent() == null)) {
                        statusBarIcon = centeredIcon;
                    }
                    if (statusBarIcon.getParent() != null) {
                        statusBarIcon.getLocationOnScreen(this.mTmpLocation);
                        float translationX = (((float) this.mTmpLocation[0]) - statusBarIcon.getTranslationX()) + ViewState.getFinalTranslationX(statusBarIcon) + (((float) statusBarIcon.getWidth()) * 0.25f);
                        this.mHostLayout.getLocationOnScreen(this.mTmpLocation);
                        f2 = translationX - ((float) this.mTmpLocation[0]);
                    }
                }
                float f3 = f2;
                if (z3) {
                    if (z) {
                        this.mLogger.logHUNViewDisappearing(str);
                        runnable = new StackStateAnimator$$ExternalSyntheticLambda3(this, str, runnable2);
                    } else {
                        runnable = runnable2;
                    }
                    this.mAnimationProperties.delay += expandableView.performRemoveAnimation(400, 0, 0.0f, true, f3, runnable, getGlobalAnimationFinishedListener());
                } else if (runnable2 != null) {
                    runnable2.run();
                }
            }
            this.mNewEvents.add(next);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$processAnimationEvents$0(String str, ExpandableView expandableView) {
        this.mLogger.disappearAnimationEnded(str);
        expandableView.removeFromTransientContainer();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$processAnimationEvents$1(String str) {
        this.mLogger.appearAnimationEnded(str);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$processAnimationEvents$2(String str, Runnable runnable) {
        this.mLogger.disappearAnimationEnded(str);
        if (runnable != null) {
            runnable.run();
        }
    }

    public void animateOverScrollToAmount(float f, final boolean z, final boolean z2) {
        float currentOverScrollAmount = this.mHostLayout.getCurrentOverScrollAmount(z);
        if (f != currentOverScrollAmount) {
            cancelOverScrollAnimators(z);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{currentOverScrollAmount, f});
            ofFloat.setDuration(360);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StackStateAnimator.this.mHostLayout.setOverScrollAmount(((Float) valueAnimator.getAnimatedValue()).floatValue(), z, false, false, z2);
                }
            });
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (z) {
                        StackStateAnimator.this.mTopOverScrollAnimator = null;
                    } else {
                        StackStateAnimator.this.mBottomOverScrollAnimator = null;
                    }
                }
            });
            ofFloat.start();
            if (z) {
                this.mTopOverScrollAnimator = ofFloat;
            } else {
                this.mBottomOverScrollAnimator = ofFloat;
            }
        }
    }

    public void cancelOverScrollAnimators(boolean z) {
        ValueAnimator valueAnimator = z ? this.mTopOverScrollAnimator : this.mBottomOverScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void setHeadsUpAppearHeightBottom(int i) {
        this.mHeadsUpAppearHeightBottom = i;
    }

    public void setShadeExpanded(boolean z) {
        this.mShadeExpanded = z;
    }

    public void setShelf(NotificationShelf notificationShelf) {
        this.mShelf = notificationShelf;
    }
}
