package com.android.wm.shell.pip;

import android.app.ActivityTaskManager;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import android.window.TaskSnapshot;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.List;
import java.util.Objects;

public class PipUtils {
    public static Pair<ComponentName, Integer> getTopPipActivity(Context context) {
        int[] iArr;
        try {
            String packageName = context.getPackageName();
            ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
            if (!(rootTaskInfo == null || (iArr = rootTaskInfo.childTaskIds) == null || iArr.length <= 0)) {
                for (int length = rootTaskInfo.childTaskNames.length - 1; length >= 0; length--) {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(rootTaskInfo.childTaskNames[length]);
                    if (unflattenFromString != null && !unflattenFromString.getPackageName().equals(packageName)) {
                        return new Pair<>(unflattenFromString, Integer.valueOf(rootTaskInfo.childTaskUserIds[length]));
                    }
                }
            }
        } catch (RemoteException unused) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -1022141965, 0, (String) null, "PipUtils");
            }
        }
        return new Pair<>((Object) null, 0);
    }

    public static boolean aspectRatioChanged(float f, float f2) {
        return ((double) Math.abs(f - f2)) > 1.0E-7d;
    }

    public static boolean remoteActionsMatch(RemoteAction remoteAction, RemoteAction remoteAction2) {
        if (remoteAction == remoteAction2) {
            return true;
        }
        if (remoteAction == null || remoteAction2 == null) {
            return false;
        }
        if (!Objects.equals(remoteAction.getTitle(), remoteAction2.getTitle()) || !Objects.equals(remoteAction.getContentDescription(), remoteAction2.getContentDescription()) || !Objects.equals(remoteAction.getActionIntent(), remoteAction2.getActionIntent())) {
            return false;
        }
        return true;
    }

    public static boolean remoteActionsChanged(List<RemoteAction> list, List<RemoteAction> list2) {
        if (list == null && list2 == null) {
            return false;
        }
        if (list == null || list2 == null || list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!remoteActionsMatch(list.get(i), list2.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static TaskSnapshot getTaskSnapshot(int i, boolean z) {
        if (i <= 0) {
            return null;
        }
        try {
            return ActivityTaskManager.getService().getTaskSnapshot(i, z);
        } catch (RemoteException e) {
            Log.e("PipUtils", "Failed to get task snapshot, taskId=" + i, e);
            return null;
        }
    }
}
