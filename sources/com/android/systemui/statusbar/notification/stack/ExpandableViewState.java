package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;

public class ExpandableViewState extends ViewState {
    public static final int TAG_ANIMATOR_BOTTOM_INSET = R$id.bottom_inset_animator_tag;
    public static final int TAG_ANIMATOR_HEIGHT = R$id.height_animator_tag;
    public static final int TAG_ANIMATOR_TOP_INSET = R$id.top_inset_animator_tag;
    public static final int TAG_END_BOTTOM_INSET = R$id.bottom_inset_animator_end_value_tag;
    public static final int TAG_END_HEIGHT = R$id.height_animator_end_value_tag;
    public static final int TAG_END_TOP_INSET = R$id.top_inset_animator_end_value_tag;
    public static final int TAG_START_BOTTOM_INSET = R$id.bottom_inset_animator_start_value_tag;
    public static final int TAG_START_HEIGHT = R$id.height_animator_start_value_tag;
    public static final int TAG_START_TOP_INSET = R$id.top_inset_animator_start_value_tag;
    public boolean belowSpeedBump;
    public int clipBottomAmount;
    public int clipTopAmount;
    public boolean dimmed;
    public boolean headsUpIsVisible;
    public int height;
    public boolean hideSensitive;
    public boolean inShelf;
    public int location;
    public int notGoneIndex;

    public void copyFrom(ViewState viewState) {
        super.copyFrom(viewState);
        if (viewState instanceof ExpandableViewState) {
            ExpandableViewState expandableViewState = (ExpandableViewState) viewState;
            this.height = expandableViewState.height;
            this.dimmed = expandableViewState.dimmed;
            this.hideSensitive = expandableViewState.hideSensitive;
            this.belowSpeedBump = expandableViewState.belowSpeedBump;
            this.clipTopAmount = expandableViewState.clipTopAmount;
            this.notGoneIndex = expandableViewState.notGoneIndex;
            this.location = expandableViewState.location;
            this.headsUpIsVisible = expandableViewState.headsUpIsVisible;
        }
    }

    public void applyToView(View view) {
        super.applyToView(view);
        if (view instanceof ExpandableView) {
            ExpandableView expandableView = (ExpandableView) view;
            int actualHeight = expandableView.getActualHeight();
            int i = this.height;
            if (actualHeight != i) {
                expandableView.setActualHeight(i, false);
            }
            expandableView.setDimmed(this.dimmed, false);
            expandableView.setHideSensitive(this.hideSensitive, false, 0, 0);
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            int i2 = this.clipTopAmount;
            if (((float) expandableView.getClipTopAmount()) != ((float) i2)) {
                expandableView.setClipTopAmount(i2);
            }
            int i3 = this.clipBottomAmount;
            if (((float) expandableView.getClipBottomAmount()) != ((float) i3)) {
                expandableView.setClipBottomAmount(i3);
            }
            expandableView.setTransformingInShelf(false);
            expandableView.setInShelf(this.inShelf);
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    public void animateTo(View view, AnimationProperties animationProperties) {
        super.animateTo(view, animationProperties);
        if (view instanceof ExpandableView) {
            ExpandableView expandableView = (ExpandableView) view;
            AnimationFilter animationFilter = animationProperties.getAnimationFilter();
            if (this.height != expandableView.getActualHeight()) {
                startHeightAnimation(expandableView, animationProperties);
            } else {
                abortAnimation(view, TAG_ANIMATOR_HEIGHT);
            }
            if (this.clipTopAmount != expandableView.getClipTopAmount()) {
                startClipAnimation(expandableView, animationProperties, true);
            } else {
                abortAnimation(view, TAG_ANIMATOR_TOP_INSET);
            }
            if (this.clipBottomAmount != expandableView.getClipBottomAmount()) {
                startClipAnimation(expandableView, animationProperties, false);
            } else {
                abortAnimation(view, TAG_ANIMATOR_BOTTOM_INSET);
            }
            expandableView.setDimmed(this.dimmed, animationFilter.animateDimmed);
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            expandableView.setHideSensitive(this.hideSensitive, animationFilter.animateHideSensitive, animationProperties.delay, animationProperties.duration);
            if (animationProperties.wasAdded(view) && !this.hidden) {
                expandableView.performAddAnimation(animationProperties.delay, animationProperties.duration, false);
            }
            if (!expandableView.isInShelf() && this.inShelf) {
                expandableView.setTransformingInShelf(true);
            }
            expandableView.setInShelf(this.inShelf);
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    public final void startHeightAnimation(final ExpandableView expandableView, AnimationProperties animationProperties) {
        int i = TAG_START_HEIGHT;
        Integer num = (Integer) ViewState.getChildTag(expandableView, i);
        int i2 = TAG_END_HEIGHT;
        Integer num2 = (Integer) ViewState.getChildTag(expandableView, i2);
        int i3 = this.height;
        if (num2 == null || num2.intValue() != i3) {
            int i4 = TAG_ANIMATOR_HEIGHT;
            ValueAnimator valueAnimator = (ValueAnimator) ViewState.getChildTag(expandableView, i4);
            if (animationProperties.getAnimationFilter().animateHeight) {
                ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{expandableView.getActualHeight(), i3});
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        expandableView.setActualHeight(((Integer) valueAnimator.getAnimatedValue()).intValue(), false);
                    }
                });
                ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofInt.setDuration(ViewState.cancelAnimatorAndGetNewDuration(animationProperties.duration, valueAnimator));
                if (animationProperties.delay > 0 && (valueAnimator == null || valueAnimator.getAnimatedFraction() == 0.0f)) {
                    ofInt.setStartDelay(animationProperties.delay);
                }
                AnimatorListenerAdapter animationFinishListener = animationProperties.getAnimationFinishListener((Property) null);
                if (animationFinishListener != null) {
                    ofInt.addListener(animationFinishListener);
                }
                ofInt.addListener(new AnimatorListenerAdapter() {
                    public boolean mWasCancelled;

                    public void onAnimationEnd(Animator animator) {
                        expandableView.setTag(ExpandableViewState.TAG_ANIMATOR_HEIGHT, (Object) null);
                        expandableView.setTag(ExpandableViewState.TAG_START_HEIGHT, (Object) null);
                        expandableView.setTag(ExpandableViewState.TAG_END_HEIGHT, (Object) null);
                        expandableView.setActualHeightAnimating(false);
                        if (!this.mWasCancelled) {
                            ExpandableView expandableView = expandableView;
                            if (expandableView instanceof ExpandableNotificationRow) {
                                ((ExpandableNotificationRow) expandableView).setGroupExpansionChanging(false);
                            }
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        this.mWasCancelled = false;
                    }

                    public void onAnimationCancel(Animator animator) {
                        this.mWasCancelled = true;
                    }
                });
                ViewState.startAnimator(ofInt, animationFinishListener);
                expandableView.setTag(i4, ofInt);
                expandableView.setTag(i, Integer.valueOf(expandableView.getActualHeight()));
                expandableView.setTag(i2, Integer.valueOf(i3));
                expandableView.setActualHeightAnimating(true);
            } else if (valueAnimator != null) {
                PropertyValuesHolder[] values = valueAnimator.getValues();
                int intValue = num.intValue() + (i3 - num2.intValue());
                values[0].setIntValues(new int[]{intValue, i3});
                expandableView.setTag(i, Integer.valueOf(intValue));
                expandableView.setTag(i2, Integer.valueOf(i3));
                valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
            } else {
                expandableView.setActualHeight(i3, false);
            }
        }
    }

    public final void startClipAnimation(final ExpandableView expandableView, AnimationProperties animationProperties, final boolean z) {
        Integer num = (Integer) ViewState.getChildTag(expandableView, z ? TAG_START_TOP_INSET : TAG_START_BOTTOM_INSET);
        Integer num2 = (Integer) ViewState.getChildTag(expandableView, z ? TAG_END_TOP_INSET : TAG_END_BOTTOM_INSET);
        int i = z ? this.clipTopAmount : this.clipBottomAmount;
        if (num2 == null || num2.intValue() != i) {
            ValueAnimator valueAnimator = (ValueAnimator) ViewState.getChildTag(expandableView, z ? TAG_ANIMATOR_TOP_INSET : TAG_ANIMATOR_BOTTOM_INSET);
            AnimationFilter animationFilter = animationProperties.getAnimationFilter();
            if ((!z || animationFilter.animateTopInset) && z) {
                int[] iArr = new int[2];
                iArr[0] = z ? expandableView.getClipTopAmount() : expandableView.getClipBottomAmount();
                iArr[1] = i;
                ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                ofInt.addUpdateListener(new ExpandableViewState$$ExternalSyntheticLambda0(z, expandableView));
                ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofInt.setDuration(ViewState.cancelAnimatorAndGetNewDuration(animationProperties.duration, valueAnimator));
                if (animationProperties.delay > 0 && (valueAnimator == null || valueAnimator.getAnimatedFraction() == 0.0f)) {
                    ofInt.setStartDelay(animationProperties.delay);
                }
                AnimatorListenerAdapter animationFinishListener = animationProperties.getAnimationFinishListener((Property) null);
                if (animationFinishListener != null) {
                    ofInt.addListener(animationFinishListener);
                }
                ofInt.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        expandableView.setTag(z ? ExpandableViewState.TAG_ANIMATOR_TOP_INSET : ExpandableViewState.TAG_ANIMATOR_BOTTOM_INSET, (Object) null);
                        expandableView.setTag(z ? ExpandableViewState.TAG_START_TOP_INSET : ExpandableViewState.TAG_START_BOTTOM_INSET, (Object) null);
                        expandableView.setTag(z ? ExpandableViewState.TAG_END_TOP_INSET : ExpandableViewState.TAG_END_BOTTOM_INSET, (Object) null);
                    }
                });
                ViewState.startAnimator(ofInt, animationFinishListener);
                expandableView.setTag(z ? TAG_ANIMATOR_TOP_INSET : TAG_ANIMATOR_BOTTOM_INSET, ofInt);
                expandableView.setTag(z ? TAG_START_TOP_INSET : TAG_START_BOTTOM_INSET, Integer.valueOf(z ? expandableView.getClipTopAmount() : expandableView.getClipBottomAmount()));
                expandableView.setTag(z ? TAG_END_TOP_INSET : TAG_END_BOTTOM_INSET, Integer.valueOf(i));
            } else if (valueAnimator != null) {
                PropertyValuesHolder[] values = valueAnimator.getValues();
                int intValue = num.intValue() + (i - num2.intValue());
                values[0].setIntValues(new int[]{intValue, i});
                expandableView.setTag(z ? TAG_START_TOP_INSET : TAG_START_BOTTOM_INSET, Integer.valueOf(intValue));
                expandableView.setTag(z ? TAG_END_TOP_INSET : TAG_END_BOTTOM_INSET, Integer.valueOf(i));
                valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
            } else if (z) {
                expandableView.setClipTopAmount(i);
            } else {
                expandableView.setClipBottomAmount(i);
            }
        }
    }

    public static /* synthetic */ void lambda$startClipAnimation$0(boolean z, ExpandableView expandableView, ValueAnimator valueAnimator) {
        if (z) {
            expandableView.setClipTopAmount(((Integer) valueAnimator.getAnimatedValue()).intValue());
        } else {
            expandableView.setClipBottomAmount(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    public static int getFinalActualHeight(ExpandableView expandableView) {
        if (expandableView == null) {
            return 0;
        }
        if (((ValueAnimator) ViewState.getChildTag(expandableView, TAG_ANIMATOR_HEIGHT)) == null) {
            return expandableView.getActualHeight();
        }
        return ((Integer) ViewState.getChildTag(expandableView, TAG_END_HEIGHT)).intValue();
    }

    public void cancelAnimations(View view) {
        super.cancelAnimations(view);
        Animator animator = (Animator) ViewState.getChildTag(view, TAG_ANIMATOR_HEIGHT);
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = (Animator) ViewState.getChildTag(view, TAG_ANIMATOR_TOP_INSET);
        if (animator2 != null) {
            animator2.cancel();
        }
    }
}
