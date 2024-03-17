package com.android.wm.shell;

import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.io.PrintWriter;
import java.util.Optional;

public final class ShellCommandHandlerImpl {
    public final Optional<AppPairsController> mAppPairsOptional;
    public final Optional<HideDisplayCutoutController> mHideDisplayCutout;
    public final HandlerImpl mImpl = new HandlerImpl();
    public final KidsModeTaskOrganizer mKidsModeTaskOrganizer;
    public final Optional<LegacySplitScreenController> mLegacySplitScreenOptional;
    public final ShellExecutor mMainExecutor;
    public final Optional<OneHandedController> mOneHandedOptional;
    public final Optional<Pip> mPipOptional;
    public final Optional<RecentTasksController> mRecentTasks;
    public final ShellTaskOrganizer mShellTaskOrganizer;
    public final Optional<SplitScreenController> mSplitScreenOptional;

    public ShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, ShellExecutor shellExecutor) {
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mKidsModeTaskOrganizer = kidsModeTaskOrganizer;
        this.mRecentTasks = optional7;
        this.mLegacySplitScreenOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mPipOptional = optional3;
        this.mOneHandedOptional = optional4;
        this.mHideDisplayCutout = optional5;
        this.mAppPairsOptional = optional6;
        this.mMainExecutor = shellExecutor;
    }

    public ShellCommandHandler asShellCommandHandler() {
        return this.mImpl;
    }

    public final void dump(PrintWriter printWriter) {
        this.mShellTaskOrganizer.dump(printWriter, "");
        printWriter.println();
        printWriter.println();
        this.mPipOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda0(printWriter));
        this.mLegacySplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(printWriter));
        this.mOneHandedOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(printWriter));
        this.mHideDisplayCutout.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda3(printWriter));
        printWriter.println();
        printWriter.println();
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda4(printWriter));
        printWriter.println();
        printWriter.println();
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda5(printWriter));
        printWriter.println();
        printWriter.println();
        this.mRecentTasks.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda6(printWriter));
        printWriter.println();
        printWriter.println();
        this.mKidsModeTaskOrganizer.dump(printWriter, "");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        if (r3.equals("help") == false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean handleCommand(java.lang.String[] r7, java.io.PrintWriter r8) {
        /*
            r6 = this;
            int r0 = r7.length
            r1 = 2
            r2 = 0
            if (r0 >= r1) goto L_0x0006
            return r2
        L_0x0006:
            r0 = 1
            r3 = r7[r0]
            r3.hashCode()
            r4 = -1
            int r5 = r3.hashCode()
            switch(r5) {
                case -840336141: goto L_0x004b;
                case -91197669: goto L_0x0040;
                case 3198785: goto L_0x0037;
                case 3433178: goto L_0x002c;
                case 295561529: goto L_0x0021;
                case 1522429422: goto L_0x0016;
                default: goto L_0x0014;
            }
        L_0x0014:
            r1 = r4
            goto L_0x0055
        L_0x0016:
            java.lang.String r0 = "setSideStagePosition"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x001f
            goto L_0x0014
        L_0x001f:
            r1 = 5
            goto L_0x0055
        L_0x0021:
            java.lang.String r0 = "removeFromSideStage"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x002a
            goto L_0x0014
        L_0x002a:
            r1 = 4
            goto L_0x0055
        L_0x002c:
            java.lang.String r0 = "pair"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0035
            goto L_0x0014
        L_0x0035:
            r1 = 3
            goto L_0x0055
        L_0x0037:
            java.lang.String r0 = "help"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0055
            goto L_0x0014
        L_0x0040:
            java.lang.String r1 = "moveToSideStage"
            boolean r1 = r3.equals(r1)
            if (r1 != 0) goto L_0x0049
            goto L_0x0014
        L_0x0049:
            r1 = r0
            goto L_0x0055
        L_0x004b:
            java.lang.String r0 = "unpair"
            boolean r0 = r3.equals(r0)
            if (r0 != 0) goto L_0x0054
            goto L_0x0014
        L_0x0054:
            r1 = r2
        L_0x0055:
            switch(r1) {
                case 0: goto L_0x0072;
                case 1: goto L_0x006d;
                case 2: goto L_0x0068;
                case 3: goto L_0x0063;
                case 4: goto L_0x005e;
                case 5: goto L_0x0059;
                default: goto L_0x0058;
            }
        L_0x0058:
            return r2
        L_0x0059:
            boolean r6 = r6.runSetSideStagePosition(r7, r8)
            return r6
        L_0x005e:
            boolean r6 = r6.runRemoveFromSideStage(r7, r8)
            return r6
        L_0x0063:
            boolean r6 = r6.runPair(r7, r8)
            return r6
        L_0x0068:
            boolean r6 = r6.runHelp(r8)
            return r6
        L_0x006d:
            boolean r6 = r6.runMoveToSideStage(r7, r8)
            return r6
        L_0x0072:
            boolean r6 = r6.runUnpair(r7, r8)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.ShellCommandHandlerImpl.handleCommand(java.lang.String[], java.io.PrintWriter):boolean");
    }

    public final boolean runPair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 4) {
            printWriter.println("Error: two task ids should be provided as arguments");
            return false;
        }
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda9(new Integer(strArr[2]).intValue(), new Integer(strArr[3]).intValue()));
        return true;
    }

    public final boolean runUnpair(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as an argument");
            return false;
        }
        this.mAppPairsOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda8(new Integer(strArr[2]).intValue()));
        return true;
    }

    public final boolean runMoveToSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda11(new Integer(strArr[2]).intValue(), strArr.length > 3 ? new Integer(strArr[3]).intValue() : 1));
        return true;
    }

    public final boolean runRemoveFromSideStage(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: task id should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda10(new Integer(strArr[2]).intValue()));
        return true;
    }

    public final boolean runSetSideStagePosition(String[] strArr, PrintWriter printWriter) {
        if (strArr.length < 3) {
            printWriter.println("Error: side stage position should be provided as arguments");
            return false;
        }
        this.mSplitScreenOptional.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda7(new Integer(strArr[2]).intValue()));
        return true;
    }

    public final boolean runHelp(PrintWriter printWriter) {
        printWriter.println("Window Manager Shell commands:");
        printWriter.println("  help");
        printWriter.println("      Print this help text.");
        printWriter.println("  <no arguments provided>");
        printWriter.println("    Dump Window Manager Shell internal state");
        printWriter.println("  pair <taskId1> <taskId2>");
        printWriter.println("  unpair <taskId>");
        printWriter.println("    Pairs/unpairs tasks with given ids.");
        printWriter.println("  moveToSideStage <taskId> <SideStagePosition>");
        printWriter.println("    Move a task with given id in split-screen mode.");
        printWriter.println("  removeFromSideStage <taskId>");
        printWriter.println("    Remove a task with given id in split-screen mode.");
        printWriter.println("  setSideStageOutline <true/false>");
        printWriter.println("    Enable/Disable outline on the side-stage.");
        printWriter.println("  setSideStagePosition <SideStagePosition>");
        printWriter.println("    Sets the position of the side-stage.");
        return true;
    }

    public class HandlerImpl implements ShellCommandHandler {
        public HandlerImpl() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$0(PrintWriter printWriter) {
            ShellCommandHandlerImpl.this.dump(printWriter);
        }

        public void dump(PrintWriter printWriter) {
            try {
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1(this, printWriter));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to dump the Shell in 2s", e);
            }
        }

        public boolean handleCommand(String[] strArr, PrintWriter printWriter) {
            try {
                boolean[] zArr = new boolean[1];
                ShellCommandHandlerImpl.this.mMainExecutor.executeBlocking(new ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0(this, zArr, strArr, printWriter));
                return zArr[0];
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to handle Shell command in 2s", e);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$handleCommand$1(boolean[] zArr, String[] strArr, PrintWriter printWriter) {
            zArr[0] = ShellCommandHandlerImpl.this.handleCommand(strArr, printWriter);
        }
    }
}
