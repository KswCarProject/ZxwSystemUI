package com.android.wm.shell.draganddrop;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.WindowConfiguration;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.logging.InstanceId;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.List;

public class DragAndDropPolicy {
    public static final String TAG = "DragAndDropPolicy";
    public final ActivityTaskManager mActivityTaskManager;
    public final Context mContext;
    public InstanceId mLoggerSessionId;
    public DragSession mSession;
    public final SplitScreenController mSplitScreen;
    public final Starter mStarter;
    public final ArrayList<Target> mTargets;

    public interface Starter {
        void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle);

        void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle);

        void startTask(int i, int i2, Bundle bundle);
    }

    public DragAndDropPolicy(Context context, SplitScreenController splitScreenController) {
        this(context, ActivityTaskManager.getInstance(), splitScreenController, new DefaultStarter(context));
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.android.wm.shell.draganddrop.DragAndDropPolicy$Starter] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DragAndDropPolicy(android.content.Context r2, android.app.ActivityTaskManager r3, com.android.wm.shell.splitscreen.SplitScreenController r4, com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter r5) {
        /*
            r1 = this;
            r1.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1.mTargets = r0
            r1.mContext = r2
            r1.mActivityTaskManager = r3
            r1.mSplitScreen = r4
            if (r4 == 0) goto L_0x0013
            goto L_0x0014
        L_0x0013:
            r4 = r5
        L_0x0014:
            r1.mStarter = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.draganddrop.DragAndDropPolicy.<init>(android.content.Context, android.app.ActivityTaskManager, com.android.wm.shell.splitscreen.SplitScreenController, com.android.wm.shell.draganddrop.DragAndDropPolicy$Starter):void");
    }

    public void start(DisplayLayout displayLayout, ClipData clipData, InstanceId instanceId) {
        this.mLoggerSessionId = instanceId;
        DragSession dragSession = new DragSession(this.mActivityTaskManager, displayLayout, clipData);
        this.mSession = dragSession;
        dragSession.update();
    }

    public ActivityManager.RunningTaskInfo getLatestRunningTask() {
        return this.mSession.runningTaskInfo;
    }

    public int getNumTargets() {
        return this.mTargets.size();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ff  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy.Target> getTargets(android.graphics.Insets r11) {
        /*
            r10 = this;
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r0 = r10.mTargets
            r0.clear()
            com.android.wm.shell.draganddrop.DragAndDropPolicy$DragSession r0 = r10.mSession
            if (r0 != 0) goto L_0x000c
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r10 = r10.mTargets
            return r10
        L_0x000c:
            com.android.wm.shell.common.DisplayLayout r0 = r0.displayLayout
            int r0 = r0.width()
            com.android.wm.shell.draganddrop.DragAndDropPolicy$DragSession r1 = r10.mSession
            com.android.wm.shell.common.DisplayLayout r1 = r1.displayLayout
            int r1 = r1.height()
            int r2 = r11.left
            int r0 = r0 - r2
            int r3 = r11.right
            int r0 = r0 - r3
            int r3 = r11.top
            int r1 = r1 - r3
            int r11 = r11.bottom
            int r1 = r1 - r11
            android.graphics.Rect r11 = new android.graphics.Rect
            int r0 = r0 + r2
            int r1 = r1 + r3
            r11.<init>(r2, r3, r0, r1)
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>(r11)
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>(r11)
            com.android.wm.shell.draganddrop.DragAndDropPolicy$DragSession r2 = r10.mSession
            com.android.wm.shell.common.DisplayLayout r2 = r2.displayLayout
            boolean r2 = r2.isLandscape()
            com.android.wm.shell.splitscreen.SplitScreenController r3 = r10.mSplitScreen
            r4 = 0
            r5 = 1
            if (r3 == 0) goto L_0x004d
            boolean r3 = r3.isSplitScreenVisible()
            if (r3 == 0) goto L_0x004d
            r3 = r5
            goto L_0x004e
        L_0x004d:
            r3 = r4
        L_0x004e:
            android.content.Context r6 = r10.mContext
            android.content.res.Resources r6 = r6.getResources()
            int r7 = com.android.wm.shell.R.dimen.split_divider_bar_width
            int r6 = r6.getDimensionPixelSize(r7)
            float r6 = (float) r6
            if (r3 != 0) goto L_0x006a
            com.android.wm.shell.draganddrop.DragAndDropPolicy$DragSession r7 = r10.mSession
            int r8 = r7.runningTaskActType
            if (r8 != r5) goto L_0x0068
            int r7 = r7.runningTaskWinMode
            if (r7 != r5) goto L_0x0068
            goto L_0x006a
        L_0x0068:
            r7 = r4
            goto L_0x006b
        L_0x006a:
            r7 = r5
        L_0x006b:
            if (r7 == 0) goto L_0x00ff
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>()
            com.android.wm.shell.splitscreen.SplitScreenController r7 = r10.mSplitScreen
            r7.getStageBounds(r0, r1)
            r0.intersect(r11)
            r1.intersect(r11)
            r7 = 1073741824(0x40000000, float:2.0)
            r8 = 2
            if (r2 == 0) goto L_0x00c3
            android.graphics.Rect r2 = new android.graphics.Rect
            r2.<init>()
            android.graphics.Rect r9 = new android.graphics.Rect
            r9.<init>()
            if (r3 == 0) goto L_0x00a4
            int r3 = r0.right
            float r3 = (float) r3
            float r6 = r6 / r7
            float r3 = r3 + r6
            r2.set(r11)
            int r3 = (int) r3
            r2.right = r3
            r9.set(r11)
            r9.left = r3
            goto L_0x00ad
        L_0x00a4:
            android.graphics.Rect[] r3 = new android.graphics.Rect[r8]
            r3[r4] = r2
            r3[r5] = r9
            r11.splitVertically(r3)
        L_0x00ad:
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r11 = r10.mTargets
            com.android.wm.shell.draganddrop.DragAndDropPolicy$Target r3 = new com.android.wm.shell.draganddrop.DragAndDropPolicy$Target
            r3.<init>(r5, r2, r0)
            r11.add(r3)
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r11 = r10.mTargets
            com.android.wm.shell.draganddrop.DragAndDropPolicy$Target r0 = new com.android.wm.shell.draganddrop.DragAndDropPolicy$Target
            r2 = 3
            r0.<init>(r2, r9, r1)
            r11.add(r0)
            goto L_0x0109
        L_0x00c3:
            android.graphics.Rect r2 = new android.graphics.Rect
            r2.<init>()
            android.graphics.Rect r9 = new android.graphics.Rect
            r9.<init>()
            if (r3 == 0) goto L_0x00e0
            int r3 = r0.bottom
            float r3 = (float) r3
            float r6 = r6 / r7
            float r3 = r3 + r6
            r2.set(r11)
            int r3 = (int) r3
            r2.bottom = r3
            r9.set(r11)
            r9.top = r3
            goto L_0x00e9
        L_0x00e0:
            android.graphics.Rect[] r3 = new android.graphics.Rect[r8]
            r3[r4] = r2
            r3[r5] = r9
            r11.splitHorizontally(r3)
        L_0x00e9:
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r11 = r10.mTargets
            com.android.wm.shell.draganddrop.DragAndDropPolicy$Target r3 = new com.android.wm.shell.draganddrop.DragAndDropPolicy$Target
            r3.<init>(r8, r2, r0)
            r11.add(r3)
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r11 = r10.mTargets
            com.android.wm.shell.draganddrop.DragAndDropPolicy$Target r0 = new com.android.wm.shell.draganddrop.DragAndDropPolicy$Target
            r2 = 4
            r0.<init>(r2, r9, r1)
            r11.add(r0)
            goto L_0x0109
        L_0x00ff:
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r11 = r10.mTargets
            com.android.wm.shell.draganddrop.DragAndDropPolicy$Target r2 = new com.android.wm.shell.draganddrop.DragAndDropPolicy$Target
            r2.<init>(r4, r1, r0)
            r11.add(r2)
        L_0x0109:
            java.util.ArrayList<com.android.wm.shell.draganddrop.DragAndDropPolicy$Target> r10 = r10.mTargets
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.draganddrop.DragAndDropPolicy.getTargets(android.graphics.Insets):java.util.ArrayList");
    }

    public Target getTargetAtLocation(int i, int i2) {
        for (int size = this.mTargets.size() - 1; size >= 0; size--) {
            Target target = this.mTargets.get(size);
            if (target.hitRegion.contains(i, i2)) {
                return target;
            }
        }
        return null;
    }

    public void handleDrop(Target target, ClipData clipData) {
        SplitScreenController splitScreenController;
        if (target != null && this.mTargets.contains(target)) {
            int i = target.type;
            int i2 = (i == 2 || i == 1) ? 1 : 0;
            int i3 = -1;
            if (!(i == 0 || (splitScreenController = this.mSplitScreen) == null)) {
                i3 = i2 ^ 1;
                splitScreenController.logOnDroppedToSplit(i3, this.mLoggerSessionId);
            }
            startClipDescription(clipData.getDescription(), this.mSession.dragData, i3);
        }
    }

    public final void startClipDescription(ClipDescription clipDescription, Intent intent, int i) {
        boolean hasMimeType = clipDescription.hasMimeType("application/vnd.android.task");
        boolean hasMimeType2 = clipDescription.hasMimeType("application/vnd.android.shortcut");
        Bundle bundleExtra = intent.hasExtra("android.intent.extra.ACTIVITY_OPTIONS") ? intent.getBundleExtra("android.intent.extra.ACTIVITY_OPTIONS") : new Bundle();
        if (hasMimeType) {
            this.mStarter.startTask(intent.getIntExtra("android.intent.extra.TASK_ID", -1), i, bundleExtra);
        } else if (hasMimeType2) {
            this.mStarter.startShortcut(intent.getStringExtra("android.intent.extra.PACKAGE_NAME"), intent.getStringExtra("android.intent.extra.shortcut.ID"), i, bundleExtra, (UserHandle) intent.getParcelableExtra("android.intent.extra.USER"));
        } else {
            PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android.intent.extra.PENDING_INTENT");
            this.mStarter.startIntent(pendingIntent, getStartIntentFillInIntent(pendingIntent, i), i, bundleExtra);
        }
    }

    public Intent getStartIntentFillInIntent(PendingIntent pendingIntent, int i) {
        ComponentName componentName;
        List queryIntentComponents = pendingIntent.queryIntentComponents(0);
        ComponentName componentName2 = !queryIntentComponents.isEmpty() ? ((ResolveInfo) queryIntentComponents.get(0)).activityInfo.getComponentName() : null;
        SplitScreenController splitScreenController = this.mSplitScreen;
        int i2 = 1;
        if (!(splitScreenController != null && splitScreenController.isSplitScreenVisible())) {
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mSession.runningTaskInfo;
            componentName = runningTaskInfo != null ? runningTaskInfo.baseActivity : null;
        } else {
            if (i != 0) {
                i2 = 0;
            }
            componentName = this.mSplitScreen.getTaskInfo(i2).baseActivity;
        }
        if (!componentName.equals(componentName2)) {
            return null;
        }
        Intent intent = new Intent();
        intent.addFlags(134217728);
        if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, -32505692, 0, (String) null, (Object[]) null);
        }
        return intent;
    }

    public static class DragSession {
        public final DisplayLayout displayLayout;
        public Intent dragData;
        public boolean dragItemSupportsSplitscreen;
        public final ActivityTaskManager mActivityTaskManager;
        public final ClipData mInitialDragData;
        @WindowConfiguration.ActivityType
        public int runningTaskActType = 1;
        public ActivityManager.RunningTaskInfo runningTaskInfo;
        @WindowConfiguration.WindowingMode
        public int runningTaskWinMode = 0;

        public DragSession(ActivityTaskManager activityTaskManager, DisplayLayout displayLayout2, ClipData clipData) {
            this.mActivityTaskManager = activityTaskManager;
            this.mInitialDragData = clipData;
            this.displayLayout = displayLayout2;
        }

        public void update() {
            boolean z = true;
            List tasks = this.mActivityTaskManager.getTasks(1, false);
            if (!tasks.isEmpty()) {
                ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) tasks.get(0);
                this.runningTaskInfo = runningTaskInfo2;
                this.runningTaskWinMode = runningTaskInfo2.getWindowingMode();
                this.runningTaskActType = runningTaskInfo2.getActivityType();
            }
            ActivityInfo activityInfo = this.mInitialDragData.getItemAt(0).getActivityInfo();
            if (activityInfo != null && !ActivityInfo.isResizeableMode(activityInfo.resizeMode)) {
                z = false;
            }
            this.dragItemSupportsSplitscreen = z;
            this.dragData = this.mInitialDragData.getItemAt(0).getIntent();
        }
    }

    public static class DefaultStarter implements Starter {
        public final Context mContext;

        public DefaultStarter(Context context) {
            this.mContext = context;
        }

        public void startTask(int i, int i2, Bundle bundle) {
            try {
                ActivityTaskManager.getService().startActivityFromRecents(i, bundle);
            } catch (RemoteException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch task", e);
            }
        }

        public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
            try {
                ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, (Rect) null, bundle, userHandle);
            } catch (ActivityNotFoundException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch shortcut", e);
            }
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
            try {
                pendingIntent.send(this.mContext, 0, intent, (PendingIntent.OnFinished) null, (Handler) null, (String) null, bundle);
            } catch (PendingIntent.CanceledException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch activity", e);
            }
        }
    }

    public static class Target {
        public final Rect drawRegion;
        public final Rect hitRegion;
        public final int type;

        public Target(int i, Rect rect, Rect rect2) {
            this.type = i;
            this.hitRegion = rect;
            this.drawRegion = rect2;
        }

        public String toString() {
            return "Target {hit=" + this.hitRegion + " draw=" + this.drawRegion + "}";
        }
    }
}
