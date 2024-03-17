package com.android.systemui.statusbar.phone;

import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowStateListener;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarHideIconsForBouncerManager.kt */
public final class StatusBarHideIconsForBouncerManager implements Dumpable {
    public boolean bouncerShowing;
    public boolean bouncerWasShowingWhenHidden;
    @NotNull
    public final CommandQueue commandQueue;
    public int displayId;
    public boolean hideIconsForBouncer;
    public boolean isOccluded;
    @NotNull
    public final DelayableExecutor mainExecutor;
    public boolean panelExpanded;
    public boolean statusBarWindowHidden;
    public boolean topAppHidesStatusBar;
    public boolean wereIconsJustHidden;

    public StatusBarHideIconsForBouncerManager(@NotNull CommandQueue commandQueue2, @NotNull DelayableExecutor delayableExecutor, @NotNull StatusBarWindowStateController statusBarWindowStateController, @NotNull DumpManager dumpManager) {
        this.commandQueue = commandQueue2;
        this.mainExecutor = delayableExecutor;
        dumpManager.registerDumpable(this);
        statusBarWindowStateController.addListener(new StatusBarWindowStateListener(this) {
            public final /* synthetic */ StatusBarHideIconsForBouncerManager this$0;

            {
                this.this$0 = r1;
            }

            public final void onStatusBarWindowStateChanged(int i) {
                this.this$0.setStatusBarStateAndTriggerUpdate(i);
            }
        });
    }

    public final boolean getShouldHideStatusBarIconsForBouncer() {
        return this.hideIconsForBouncer || this.wereIconsJustHidden;
    }

    public final void setStatusBarStateAndTriggerUpdate(int i) {
        this.statusBarWindowHidden = i == 2;
        updateHideIconsForBouncer(false);
    }

    public final void setDisplayId(int i) {
        this.displayId = i;
    }

    public final void setPanelExpandedAndTriggerUpdate(boolean z) {
        this.panelExpanded = z;
        updateHideIconsForBouncer(false);
    }

    public final void setIsOccludedAndTriggerUpdate(boolean z) {
        this.isOccluded = z;
        updateHideIconsForBouncer(false);
    }

    public final void setBouncerShowingAndTriggerUpdate(boolean z) {
        this.bouncerShowing = z;
        updateHideIconsForBouncer(true);
    }

    public final void setTopAppHidesStatusBarAndTriggerUpdate(boolean z) {
        this.topAppHidesStatusBar = z;
        if (!z && this.wereIconsJustHidden) {
            this.wereIconsJustHidden = false;
            this.commandQueue.recomputeDisableFlags(this.displayId, true);
        }
        updateHideIconsForBouncer(true);
    }

    public final void updateHideIconsForBouncer(boolean z) {
        boolean z2 = false;
        boolean z3 = this.topAppHidesStatusBar && this.isOccluded && (this.statusBarWindowHidden || this.bouncerShowing);
        boolean z4 = !this.panelExpanded && !this.isOccluded && this.bouncerShowing;
        if (z3 || z4) {
            z2 = true;
        }
        if (this.hideIconsForBouncer != z2) {
            this.hideIconsForBouncer = z2;
            if (z2 || !this.bouncerWasShowingWhenHidden) {
                this.commandQueue.recomputeDisableFlags(this.displayId, z);
            } else {
                this.wereIconsJustHidden = true;
                this.mainExecutor.executeDelayed(new StatusBarHideIconsForBouncerManager$updateHideIconsForBouncer$1(this), 500);
            }
        }
        if (z2) {
            this.bouncerWasShowingWhenHidden = this.bouncerShowing;
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("---- State variables set externally ----");
        printWriter.println(Intrinsics.stringPlus("panelExpanded=", Boolean.valueOf(this.panelExpanded)));
        printWriter.println(Intrinsics.stringPlus("isOccluded=", Boolean.valueOf(this.isOccluded)));
        printWriter.println(Intrinsics.stringPlus("bouncerShowing=", Boolean.valueOf(this.bouncerShowing)));
        printWriter.println(Intrinsics.stringPlus("topAppHideStatusBar=", Boolean.valueOf(this.topAppHidesStatusBar)));
        printWriter.println(Intrinsics.stringPlus("statusBarWindowHidden=", Boolean.valueOf(this.statusBarWindowHidden)));
        printWriter.println(Intrinsics.stringPlus("displayId=", Integer.valueOf(this.displayId)));
        printWriter.println("---- State variables calculated internally ----");
        printWriter.println(Intrinsics.stringPlus("hideIconsForBouncer=", Boolean.valueOf(this.hideIconsForBouncer)));
        printWriter.println(Intrinsics.stringPlus("bouncerWasShowingWhenHidden=", Boolean.valueOf(this.bouncerWasShowingWhenHidden)));
        printWriter.println(Intrinsics.stringPlus("wereIconsJustHidden=", Boolean.valueOf(this.wereIconsJustHidden)));
    }
}
