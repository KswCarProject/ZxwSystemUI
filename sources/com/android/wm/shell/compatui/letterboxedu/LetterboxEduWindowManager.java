package com.android.wm.shell.compatui.letterboxedu;

import android.app.TaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIWindowManagerAbstract;
import com.android.wm.shell.transition.Transitions;

public class LetterboxEduWindowManager extends CompatUIWindowManagerAbstract {
    @VisibleForTesting
    public static final String HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME = "has_seen_letterbox_education";
    public final LetterboxEduAnimationController mAnimationController;
    public final int mDialogVerticalMargin;
    public boolean mEligibleForLetterboxEducation;
    @VisibleForTesting
    public LetterboxEduDialogLayout mLayout;
    public final Runnable mOnDismissCallback;
    public final SharedPreferences mSharedPreferences;
    public final Transitions mTransitions;
    public final int mUserId;

    public int getZOrder() {
        return Integer.MAX_VALUE;
    }

    public void updateSurfacePosition() {
    }

    public LetterboxEduWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, Transitions transitions, Runnable runnable) {
        this(context, taskInfo, syncTransactionQueue, taskListener, displayLayout, transitions, runnable, new LetterboxEduAnimationController(context));
    }

    @VisibleForTesting
    public LetterboxEduWindowManager(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout, Transitions transitions, Runnable runnable, LetterboxEduAnimationController letterboxEduAnimationController) {
        super(context, taskInfo, syncTransactionQueue, taskListener, displayLayout);
        this.mTransitions = transitions;
        this.mOnDismissCallback = runnable;
        this.mAnimationController = letterboxEduAnimationController;
        this.mUserId = taskInfo.userId;
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
        this.mSharedPreferences = this.mContext.getSharedPreferences(HAS_SEEN_LETTERBOX_EDUCATION_PREF_NAME, 0);
        this.mDialogVerticalMargin = (int) this.mContext.getResources().getDimension(R.dimen.letterbox_education_dialog_margin);
    }

    public View getLayout() {
        return this.mLayout;
    }

    public void removeLayout() {
        this.mLayout = null;
    }

    public boolean eligibleToShowLayout() {
        return this.mEligibleForLetterboxEducation && !isTaskbarEduShowing() && (this.mLayout != null || !getHasSeenLetterboxEducation());
    }

    public View createLayout() {
        this.mLayout = inflateLayout();
        updateDialogMargins();
        this.mTransitions.runOnIdle(new LetterboxEduWindowManager$$ExternalSyntheticLambda0(this));
        return this.mLayout;
    }

    public final void updateDialogMargins() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            View dialogContainer = letterboxEduDialogLayout.getDialogContainer();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) dialogContainer.getLayoutParams();
            Rect taskBounds = getTaskBounds();
            Rect taskStableBounds = getTaskStableBounds();
            int i = this.mDialogVerticalMargin;
            marginLayoutParams.topMargin = (taskStableBounds.top - taskBounds.top) + i;
            marginLayoutParams.bottomMargin = (taskBounds.bottom - taskStableBounds.bottom) + i;
            dialogContainer.setLayoutParams(marginLayoutParams);
        }
    }

    public final LetterboxEduDialogLayout inflateLayout() {
        return (LetterboxEduDialogLayout) LayoutInflater.from(this.mContext).inflate(R.layout.letterbox_education_dialog_layout, (ViewGroup) null);
    }

    public final void startEnterAnimation() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            this.mAnimationController.startEnterAnimation(letterboxEduDialogLayout, new LetterboxEduWindowManager$$ExternalSyntheticLambda1(this));
        }
    }

    public final void onDialogEnterAnimationEnded() {
        if (this.mLayout != null) {
            setSeenLetterboxEducation();
            this.mLayout.setDismissOnClickListener(new LetterboxEduWindowManager$$ExternalSyntheticLambda2(this));
            this.mLayout.getDialogTitle().sendAccessibilityEvent(8);
        }
    }

    public final void onDismiss() {
        LetterboxEduDialogLayout letterboxEduDialogLayout = this.mLayout;
        if (letterboxEduDialogLayout != null) {
            letterboxEduDialogLayout.setDismissOnClickListener((Runnable) null);
            this.mAnimationController.startExitAnimation(this.mLayout, new LetterboxEduWindowManager$$ExternalSyntheticLambda3(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDismiss$0() {
        release();
        this.mOnDismissCallback.run();
    }

    public void release() {
        this.mAnimationController.cancelAnimation();
        super.release();
    }

    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        this.mEligibleForLetterboxEducation = taskInfo.topActivityEligibleForLetterboxEducation;
        return super.updateCompatInfo(taskInfo, taskListener, z);
    }

    public void onParentBoundsChanged() {
        if (this.mLayout != null) {
            WindowManager.LayoutParams windowLayoutParams = getWindowLayoutParams();
            this.mLayout.setLayoutParams(windowLayoutParams);
            updateDialogMargins();
            relayout(windowLayoutParams);
        }
    }

    public WindowManager.LayoutParams getWindowLayoutParams() {
        Rect taskBounds = getTaskBounds();
        return getWindowLayoutParams(taskBounds.width(), taskBounds.height());
    }

    public final boolean getHasSeenLetterboxEducation() {
        return this.mSharedPreferences.getBoolean(getPrefKey(), false);
    }

    public final void setSeenLetterboxEducation() {
        this.mSharedPreferences.edit().putBoolean(getPrefKey(), true).apply();
    }

    public final String getPrefKey() {
        return String.valueOf(this.mUserId);
    }

    @VisibleForTesting
    public boolean isTaskbarEduShowing() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "launcher_taskbar_education_showing", 0) == 1;
    }
}
