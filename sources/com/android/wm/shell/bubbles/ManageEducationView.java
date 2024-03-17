package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.wm.shell.R;
import com.android.wm.shell.TaskView;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ManageEducationView.kt */
public final class ManageEducationView extends LinearLayout {
    public final long ANIMATE_DURATION = 200;
    @NotNull
    public final String TAG = "Bubbles";
    @Nullable
    public BubbleExpandedView bubbleExpandedView;
    @NotNull
    public final Lazy gotItButton$delegate;
    public boolean isHiding;
    @NotNull
    public final Lazy manageButton$delegate;
    @NotNull
    public final Lazy manageView$delegate;
    @NotNull
    public final BubblePositioner positioner;
    @NotNull
    public Rect realManageButtonRect;

    public ManageEducationView(@NotNull Context context, @NotNull BubblePositioner bubblePositioner) {
        super(context);
        this.positioner = bubblePositioner;
        this.manageView$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$manageView$2(this));
        this.manageButton$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$manageButton$2(this));
        this.gotItButton$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$gotItButton$2(this));
        this.realManageButtonRect = new Rect();
        LayoutInflater.from(context).inflate(R.layout.bubbles_manage_button_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    public final ViewGroup getManageView() {
        return (ViewGroup) this.manageView$delegate.getValue();
    }

    public final Button getManageButton() {
        return (Button) this.manageButton$delegate.getValue();
    }

    public final Button getGotItButton() {
        return (Button) this.gotItButton$delegate.getValue();
    }

    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
    }

    public final void setButtonColor() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{17956900});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        getManageButton().setTextColor(this.mContext.getColor(17170472));
        getManageButton().setBackgroundDrawable(new ColorDrawable(color));
        getGotItButton().setBackgroundDrawable(new ColorDrawable(color));
    }

    public final void setDrawableDirection() {
        int i;
        ViewGroup manageView = getManageView();
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        } else {
            i = R.drawable.bubble_stack_user_education_bg;
        }
        manageView.setBackgroundResource(i);
    }

    public final void show(@NotNull BubbleExpandedView bubbleExpandedView2) {
        int i;
        setButtonColor();
        if (getVisibility() != 0) {
            this.bubbleExpandedView = bubbleExpandedView2;
            TaskView taskView = bubbleExpandedView2.getTaskView();
            if (taskView != null) {
                taskView.setObscuredTouchRect(new Rect(this.positioner.getScreenRect()));
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (this.positioner.isLargeScreen() || this.positioner.isLandscape()) {
                i = getContext().getResources().getDimensionPixelSize(R.dimen.bubbles_user_education_width);
            } else {
                i = -1;
            }
            layoutParams.width = i;
            setAlpha(0.0f);
            setVisibility(0);
            bubbleExpandedView2.getManageButtonBoundsOnScreen(this.realManageButtonRect);
            boolean z = true;
            if (this.mContext.getResources().getConfiguration().getLayoutDirection() != 1) {
                z = false;
            }
            if (z) {
                getManageView().setPadding(getManageView().getPaddingLeft(), getManageView().getPaddingTop(), (this.positioner.getScreenRect().right - this.realManageButtonRect.right) - bubbleExpandedView2.getManageButtonMargin(), getManageView().getPaddingBottom());
            } else {
                getManageView().setPadding(this.realManageButtonRect.left - bubbleExpandedView2.getManageButtonMargin(), getManageView().getPaddingTop(), getManageView().getPaddingRight(), getManageView().getPaddingBottom());
            }
            post(new ManageEducationView$show$1(this, z, bubbleExpandedView2));
            setShouldShow(false);
        }
    }

    public final void hide() {
        TaskView taskView;
        BubbleExpandedView bubbleExpandedView2 = this.bubbleExpandedView;
        if (!(bubbleExpandedView2 == null || (taskView = bubbleExpandedView2.getTaskView()) == null)) {
            taskView.setObscuredTouchRect((Rect) null);
        }
        if (getVisibility() == 0 && !this.isHiding) {
            animate().withStartAction(new ManageEducationView$hide$1(this)).alpha(0.0f).setDuration(this.ANIMATE_DURATION).withEndAction(new ManageEducationView$hide$2(this));
        }
    }

    public final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesManageOnboarding", !z).apply();
    }
}
