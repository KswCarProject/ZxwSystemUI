package com.android.wm.shell.legacysplitscreen;

import android.content.Context;
import android.util.ArraySet;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.legacysplitscreen.DividerView;
import java.util.function.Consumer;

public final class ForcedResizableInfoActivityController implements DividerView.DividerCallbacks {
    public final Context mContext;
    public boolean mDividerDragging;
    public final Consumer<Boolean> mDockedStackExistsListener;
    public final ShellExecutor mMainExecutor;
    public final ArraySet<String> mPackagesShownInSession = new ArraySet<>();
    public final ArraySet<PendingTaskRecord> mPendingTasks = new ArraySet<>();
    public final Runnable mTimeoutRunnable = new ForcedResizableInfoActivityController$$ExternalSyntheticLambda0(this);

    public void activityDismissingSplitScreen() {
    }

    public void activityLaunchOnSecondaryDisplayFailed() {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        if (!bool.booleanValue()) {
            this.mPackagesShownInSession.clear();
        }
    }

    public class PendingTaskRecord {
        public int mReason;
        public int mTaskId;

        public PendingTaskRecord(int i, int i2) {
            this.mTaskId = i;
            this.mReason = i2;
        }
    }

    public ForcedResizableInfoActivityController(Context context, LegacySplitScreenController legacySplitScreenController, ShellExecutor shellExecutor) {
        ForcedResizableInfoActivityController$$ExternalSyntheticLambda1 forcedResizableInfoActivityController$$ExternalSyntheticLambda1 = new ForcedResizableInfoActivityController$$ExternalSyntheticLambda1(this);
        this.mDockedStackExistsListener = forcedResizableInfoActivityController$$ExternalSyntheticLambda1;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        legacySplitScreenController.registerInSplitScreenListener(forcedResizableInfoActivityController$$ExternalSyntheticLambda1);
    }

    public void onDraggingStart() {
        this.mDividerDragging = true;
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
    }

    public void onDraggingEnd() {
        this.mDividerDragging = false;
        showPending();
    }

    public void activityForcedResizable(String str, int i, int i2) {
        if (!debounce(str)) {
            this.mPendingTasks.add(new PendingTaskRecord(i, i2));
            postTimeout();
        }
    }

    public final void showPending() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        this.mPendingTasks.clear();
    }

    public final void postTimeout() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        this.mMainExecutor.executeDelayed(this.mTimeoutRunnable, 1000);
    }

    public final boolean debounce(String str) {
        if (str == null) {
            return false;
        }
        if (ThemeOverlayApplier.SYSUI_PACKAGE.equals(str)) {
            return true;
        }
        boolean contains = this.mPackagesShownInSession.contains(str);
        this.mPackagesShownInSession.add(str);
        return contains;
    }
}
