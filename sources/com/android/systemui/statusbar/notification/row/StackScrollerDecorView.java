package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.Interpolators;
import java.util.function.Consumer;

public abstract class StackScrollerDecorView extends ExpandableView {
    public View mContent;
    public boolean mContentAnimating;
    public final Runnable mContentVisibilityEndRunnable = new StackScrollerDecorView$$ExternalSyntheticLambda0(this);
    public boolean mContentVisible = true;
    public int mDuration = 260;
    public boolean mIsSecondaryVisible = true;
    public boolean mIsVisible = true;
    public boolean mSecondaryAnimating = false;
    public View mSecondaryView;
    public final Consumer<Boolean> mSecondaryVisibilityEndRunnable = new StackScrollerDecorView$$ExternalSyntheticLambda1(this);

    public abstract View findContentView();

    public abstract View findSecondaryView();

    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isTransparent() {
        return true;
    }

    public boolean needsClippingToShelf() {
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mContentAnimating = false;
        if (getVisibility() != 8 && !this.mIsVisible) {
            setVisibility(8);
            setWillBeGone(false);
            notifyHeightChanged(false);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Boolean bool) {
        this.mSecondaryAnimating = false;
        if (this.mSecondaryView != null && getVisibility() != 8 && this.mSecondaryView.getVisibility() != 8 && !this.mIsSecondaryVisible) {
            this.mSecondaryView.setVisibility(8);
        }
    }

    public StackScrollerDecorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContent = findContentView();
        this.mSecondaryView = findSecondaryView();
        setVisible(false, false);
        setSecondaryVisible(false, false);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setOutlineProvider((ViewOutlineProvider) null);
    }

    public void setContentVisible(boolean z) {
        setContentVisible(z, true, (Consumer<Boolean>) null);
    }

    public void setContentVisible(boolean z, boolean z2, Consumer<Boolean> consumer) {
        if (this.mContentVisible != z) {
            this.mContentAnimating = z2;
            this.mContentVisible = z;
            setViewVisible(this.mContent, z, z2, new StackScrollerDecorView$$ExternalSyntheticLambda2(this, consumer));
        } else if (consumer != null) {
            consumer.accept(Boolean.TRUE);
        }
        if (!this.mContentAnimating) {
            this.mContentVisibilityEndRunnable.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setContentVisible$2(Consumer consumer, Boolean bool) {
        this.mContentVisibilityEndRunnable.run();
        if (consumer != null) {
            consumer.accept(bool);
        }
    }

    public void setVisible(boolean z, boolean z2) {
        if (this.mIsVisible != z) {
            this.mIsVisible = z;
            if (z2) {
                if (z) {
                    setVisibility(0);
                    setWillBeGone(false);
                    notifyHeightChanged(false);
                } else {
                    setWillBeGone(true);
                }
                setContentVisible(z, true, (Consumer<Boolean>) null);
                return;
            }
            setVisibility(z ? 0 : 8);
            setContentVisible(z, false, (Consumer<Boolean>) null);
            setWillBeGone(false);
            notifyHeightChanged(false);
        }
    }

    public void setSecondaryVisible(boolean z, boolean z2) {
        if (this.mIsSecondaryVisible != z) {
            this.mSecondaryAnimating = z2;
            this.mIsSecondaryVisible = z;
            setViewVisible(this.mSecondaryView, z, z2, this.mSecondaryVisibilityEndRunnable);
        }
        if (!this.mSecondaryAnimating) {
            this.mSecondaryVisibilityEndRunnable.accept(Boolean.TRUE);
        }
    }

    @VisibleForTesting
    public boolean isSecondaryVisible() {
        return this.mIsSecondaryVisible;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public final void setViewVisible(View view, boolean z, boolean z2, final Consumer<Boolean> consumer) {
        if (view != null) {
            if (view.getVisibility() != 0) {
                view.setVisibility(0);
            }
            view.animate().cancel();
            float f = z ? 1.0f : 0.0f;
            if (!z2) {
                view.setAlpha(f);
                if (consumer != null) {
                    consumer.accept(Boolean.TRUE);
                    return;
                }
                return;
            }
            view.animate().alpha(f).setInterpolator(z ? Interpolators.ALPHA_IN : Interpolators.ALPHA_OUT).setDuration((long) this.mDuration).setListener(new AnimatorListenerAdapter() {
                public boolean mCancelled;

                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    consumer.accept(Boolean.valueOf(this.mCancelled));
                }
            });
        }
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        setContentVisible(false, true, new StackScrollerDecorView$$ExternalSyntheticLambda3(runnable));
        return 0;
    }

    public void performAddAnimation(long j, long j2, boolean z) {
        setContentVisible(true);
    }

    public void performAddAnimation(long j, long j2, boolean z, Runnable runnable) {
        setContentVisible(true);
    }
}
