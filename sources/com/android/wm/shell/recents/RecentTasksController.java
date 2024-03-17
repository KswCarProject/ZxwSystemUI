package com.android.wm.shell.recents;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskInfo;
import android.content.Context;
import android.os.RemoteException;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.recents.IRecentTasks;
import com.android.wm.shell.util.GroupedRecentTaskInfo;
import com.android.wm.shell.util.StagedSplitBounds;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentTasksController implements TaskStackListenerCallback, RemoteCallable<RecentTasksController> {
    public static final String TAG = "RecentTasksController";
    public final ArrayList<Runnable> mCallbacks = new ArrayList<>();
    public final Context mContext;
    public final RecentTasks mImpl = new RecentTasksImpl();
    public final ShellExecutor mMainExecutor;
    public final SparseIntArray mSplitTasks = new SparseIntArray();
    public final Map<Integer, StagedSplitBounds> mTaskSplitBoundsMap = new HashMap();
    public final TaskStackListenerImpl mTaskStackListener;

    public static RecentTasksController create(Context context, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor) {
        if (!context.getResources().getBoolean(17891679)) {
            return null;
        }
        return new RecentTasksController(context, taskStackListenerImpl, shellExecutor);
    }

    public RecentTasksController(Context context, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mTaskStackListener = taskStackListenerImpl;
        this.mMainExecutor = shellExecutor;
    }

    public RecentTasks asRecentTasks() {
        return this.mImpl;
    }

    public void init() {
        this.mTaskStackListener.addListener(this);
    }

    public void addSplitPair(int i, int i2, StagedSplitBounds stagedSplitBounds) {
        if (i != i2) {
            if (this.mSplitTasks.get(i, -1) != i2 || !this.mTaskSplitBoundsMap.get(Integer.valueOf(i)).equals(stagedSplitBounds)) {
                removeSplitPair(i);
                removeSplitPair(i2);
                this.mTaskSplitBoundsMap.remove(Integer.valueOf(i));
                this.mTaskSplitBoundsMap.remove(Integer.valueOf(i2));
                this.mSplitTasks.put(i, i2);
                this.mSplitTasks.put(i2, i);
                this.mTaskSplitBoundsMap.put(Integer.valueOf(i), stagedSplitBounds);
                this.mTaskSplitBoundsMap.put(Integer.valueOf(i2), stagedSplitBounds);
                notifyRecentTasksChanged();
                if (ShellProtoLogCache.WM_SHELL_RECENT_TASKS_enabled) {
                    long j = (long) i2;
                    String valueOf = String.valueOf(stagedSplitBounds);
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, 1423767195, 5, (String) null, Long.valueOf((long) i), Long.valueOf(j), valueOf);
                }
            }
        }
    }

    public void removeSplitPair(int i) {
        int i2 = this.mSplitTasks.get(i, -1);
        if (i2 != -1) {
            this.mSplitTasks.delete(i);
            this.mSplitTasks.delete(i2);
            this.mTaskSplitBoundsMap.remove(Integer.valueOf(i));
            this.mTaskSplitBoundsMap.remove(Integer.valueOf(i2));
            notifyRecentTasksChanged();
            if (ShellProtoLogCache.WM_SHELL_RECENT_TASKS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, 927833074, 5, (String) null, Long.valueOf((long) i), Long.valueOf((long) i2));
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onTaskStackChanged() {
        notifyRecentTasksChanged();
    }

    public void onRecentTaskListUpdated() {
        notifyRecentTasksChanged();
    }

    public void onTaskRemoved(TaskInfo taskInfo) {
        removeSplitPair(taskInfo.taskId);
        notifyRecentTasksChanged();
    }

    public void onTaskWindowingModeChanged(TaskInfo taskInfo) {
        notifyRecentTasksChanged();
    }

    public void notifyRecentTasksChanged() {
        if (ShellProtoLogCache.WM_SHELL_RECENT_TASKS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_RECENT_TASKS, -1066960526, 0, (String) null, (Object[]) null);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).run();
        }
    }

    public final void registerRecentTasksListener(Runnable runnable) {
        if (!this.mCallbacks.contains(runnable)) {
            this.mCallbacks.add(runnable);
        }
    }

    public final void unregisterRecentTasksListener(Runnable runnable) {
        this.mCallbacks.remove(runnable);
    }

    public List<ActivityManager.RecentTaskInfo> getRawRecentTasks(int i, int i2, int i3) {
        return ActivityTaskManager.getInstance().getRecentTasks(i, i2, i3);
    }

    public ArrayList<GroupedRecentTaskInfo> getRecentTasks(int i, int i2, int i3) {
        List<ActivityManager.RecentTaskInfo> rawRecentTasks = getRawRecentTasks(i, i2, i3);
        SparseArray sparseArray = new SparseArray();
        for (int i4 = 0; i4 < rawRecentTasks.size(); i4++) {
            ActivityManager.RecentTaskInfo recentTaskInfo = rawRecentTasks.get(i4);
            sparseArray.put(recentTaskInfo.taskId, recentTaskInfo);
        }
        ArrayList<GroupedRecentTaskInfo> arrayList = new ArrayList<>();
        for (int i5 = 0; i5 < rawRecentTasks.size(); i5++) {
            ActivityManager.RecentTaskInfo recentTaskInfo2 = rawRecentTasks.get(i5);
            if (sparseArray.contains(recentTaskInfo2.taskId)) {
                int i6 = this.mSplitTasks.get(recentTaskInfo2.taskId);
                if (i6 == -1 || !sparseArray.contains(i6)) {
                    arrayList.add(new GroupedRecentTaskInfo(recentTaskInfo2));
                } else {
                    sparseArray.remove(i6);
                    arrayList.add(new GroupedRecentTaskInfo(recentTaskInfo2, (ActivityManager.RecentTaskInfo) sparseArray.get(i6), this.mTaskSplitBoundsMap.get(Integer.valueOf(i6))));
                }
            }
        }
        return arrayList;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        ArrayList<GroupedRecentTaskInfo> recentTasks = getRecentTasks(Integer.MAX_VALUE, 2, ActivityManager.getCurrentUser());
        for (int i = 0; i < recentTasks.size(); i++) {
            printWriter.println(str2 + recentTasks.get(i));
        }
    }

    public class RecentTasksImpl implements RecentTasks {
        public IRecentTasksImpl mIRecentTasks;

        public RecentTasksImpl() {
        }

        public IRecentTasks createExternalInterface() {
            IRecentTasksImpl iRecentTasksImpl = this.mIRecentTasks;
            if (iRecentTasksImpl != null) {
                iRecentTasksImpl.invalidate();
            }
            IRecentTasksImpl iRecentTasksImpl2 = new IRecentTasksImpl(RecentTasksController.this);
            this.mIRecentTasks = iRecentTasksImpl2;
            return iRecentTasksImpl2;
        }
    }

    public static class IRecentTasksImpl extends IRecentTasks.Stub {
        public RecentTasksController mController;
        public final SingleInstanceRemoteListener<RecentTasksController, IRecentTasksListener> mListener;
        public final Runnable mRecentTasksListener = new Runnable() {
            public void run() {
                IRecentTasksImpl.this.mListener.call(new RecentTasksController$IRecentTasksImpl$1$$ExternalSyntheticLambda0());
            }
        };

        public IRecentTasksImpl(RecentTasksController recentTasksController) {
            this.mController = recentTasksController;
            this.mListener = new SingleInstanceRemoteListener<>(recentTasksController, new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda0(this), new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(RecentTasksController recentTasksController) {
            recentTasksController.registerRecentTasksListener(this.mRecentTasksListener);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(RecentTasksController recentTasksController) {
            recentTasksController.unregisterRecentTasksListener(this.mRecentTasksListener);
        }

        public void invalidate() {
            this.mController = null;
        }

        public void registerRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerRecentTasksListener", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda3(this, iRecentTasksListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerRecentTasksListener$2(IRecentTasksListener iRecentTasksListener, RecentTasksController recentTasksController) {
            this.mListener.register(iRecentTasksListener);
        }

        public void unregisterRecentTasksListener(IRecentTasksListener iRecentTasksListener) throws RemoteException {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterRecentTasksListener", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$unregisterRecentTasksListener$3(RecentTasksController recentTasksController) {
            this.mListener.unregister();
        }

        public GroupedRecentTaskInfo[] getRecentTasks(int i, int i2, int i3) throws RemoteException {
            RecentTasksController recentTasksController = this.mController;
            if (recentTasksController == null) {
                return new GroupedRecentTaskInfo[0];
            }
            GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr = {null};
            ExecutorUtils.executeRemoteCallWithTaskPermission(recentTasksController, "getRecentTasks", new RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda2(groupedRecentTaskInfoArr, i, i2, i3), true);
            return groupedRecentTaskInfoArr[0];
        }

        public static /* synthetic */ void lambda$getRecentTasks$4(GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr, int i, int i2, int i3, RecentTasksController recentTasksController) {
            groupedRecentTaskInfoArr[0] = (GroupedRecentTaskInfo[]) recentTasksController.getRecentTasks(i, i2, i3).toArray(new GroupedRecentTaskInfo[0]);
        }
    }
}
