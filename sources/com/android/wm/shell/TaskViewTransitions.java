package com.android.wm.shell;

import android.app.ActivityManager;
import android.os.IBinder;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;

public class TaskViewTransitions implements Transitions.TransitionHandler {
    public final ArrayList<PendingTransition> mPending = new ArrayList<>();
    public final boolean[] mRegistered = {false};
    public final ArrayList<TaskView> mTaskViews = new ArrayList<>();
    public final Transitions mTransitions;

    public static class PendingTransition {
        public IBinder mClaimed;
        public final TaskView mTaskView;
        public final int mType;
        public final WindowContainerTransaction mWct;

        public PendingTransition(int i, WindowContainerTransaction windowContainerTransaction, TaskView taskView) {
            this.mType = i;
            this.mWct = windowContainerTransaction;
            this.mTaskView = taskView;
        }
    }

    public TaskViewTransitions(Transitions transitions) {
        this.mTransitions = transitions;
    }

    public void addTaskView(TaskView taskView) {
        synchronized (this.mRegistered) {
            boolean[] zArr = this.mRegistered;
            if (!zArr[0]) {
                zArr[0] = true;
                this.mTransitions.addHandler(this);
            }
        }
        this.mTaskViews.add(taskView);
    }

    public void removeTaskView(TaskView taskView) {
        this.mTaskViews.remove(taskView);
    }

    public final PendingTransition findPending(TaskView taskView, boolean z, boolean z2) {
        int size = this.mPending.size();
        while (true) {
            size--;
            if (size < 0) {
                return null;
            }
            if (this.mPending.get(size).mTaskView == taskView) {
                if (Transitions.isClosingType(this.mPending.get(size).mType) == z) {
                    return this.mPending.get(size);
                }
                if (z2) {
                    return null;
                }
            }
        }
    }

    public final PendingTransition findPending(IBinder iBinder) {
        for (int i = 0; i < this.mPending.size(); i++) {
            if (this.mPending.get(i).mClaimed == iBinder) {
                return this.mPending.get(i);
            }
        }
        return null;
    }

    public boolean hasPending() {
        return !this.mPending.isEmpty();
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        TaskView findTaskView;
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        if (triggerTask == null || (findTaskView = findTaskView(triggerTask)) == null || !Transitions.isClosingType(transitionRequestInfo.getType())) {
            return null;
        }
        PendingTransition findPending = findPending(findTaskView, true, false);
        if (findPending == null) {
            findPending = new PendingTransition(transitionRequestInfo.getType(), (WindowContainerTransaction) null, findTaskView);
        }
        if (findPending.mClaimed == null) {
            findPending.mClaimed = iBinder;
            return new WindowContainerTransaction();
        }
        throw new IllegalStateException("Task is closing in 2 collecting transitions? This state doesn't make sense");
    }

    public final TaskView findTaskView(ActivityManager.RunningTaskInfo runningTaskInfo) {
        for (int i = 0; i < this.mTaskViews.size(); i++) {
            if (this.mTaskViews.get(i).getTaskInfo() != null && runningTaskInfo.token.equals(this.mTaskViews.get(i).getTaskInfo().token)) {
                return this.mTaskViews.get(i);
            }
        }
        return null;
    }

    public void startTaskView(WindowContainerTransaction windowContainerTransaction, TaskView taskView) {
        this.mPending.add(new PendingTransition(1, windowContainerTransaction, taskView));
        startNextTransition();
    }

    public void setTaskViewVisible(TaskView taskView, boolean z) {
        if (findPending(taskView, !z, true) == null && taskView.getTaskInfo() != null) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.setHidden(taskView.getTaskInfo().token, !z);
            this.mPending.add(new PendingTransition(z ? 3 : 4, windowContainerTransaction, taskView));
            startNextTransition();
        }
    }

    public final void startNextTransition() {
        if (!this.mPending.isEmpty()) {
            PendingTransition pendingTransition = this.mPending.get(0);
            if (pendingTransition.mClaimed == null) {
                pendingTransition.mClaimed = this.mTransitions.startTransition(pendingTransition.mType, pendingTransition.mWct, this);
            }
        }
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        TaskView taskView;
        PendingTransition findPending = findPending(iBinder);
        if (findPending == null) {
            return false;
        }
        this.mPending.remove(findPending);
        TaskView taskView2 = findPending.mTaskView;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < transitionInfo.getChanges().size(); i++) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
            if (change.getTaskInfo() != null) {
                arrayList.add(change);
            }
        }
        if (arrayList.isEmpty()) {
            Slog.e("TaskViewTransitions", "Got a TaskView transition with no task.");
            return false;
        }
        int i2 = 0;
        WindowContainerTransaction windowContainerTransaction = null;
        while (true) {
            boolean z = true;
            if (i2 < arrayList.size()) {
                TransitionInfo.Change change2 = (TransitionInfo.Change) arrayList.get(i2);
                if (Transitions.isClosingType(change2.getMode())) {
                    if (change2.getMode() != 4) {
                        z = false;
                    }
                    TaskView findTaskView = findTaskView(change2.getTaskInfo());
                    if (findTaskView == null) {
                        throw new IllegalStateException("TaskView transition is closing a non-taskview task ");
                    } else if (z) {
                        findTaskView.prepareHideAnimation(transaction2);
                    } else {
                        SurfaceControl.Transaction transaction3 = transaction2;
                        findTaskView.prepareCloseAnimation();
                    }
                } else {
                    SurfaceControl.Transaction transaction4 = transaction2;
                    if (Transitions.isOpeningType(change2.getMode())) {
                        boolean z2 = change2.getMode() == 1;
                        if (windowContainerTransaction == null) {
                            windowContainerTransaction = new WindowContainerTransaction();
                        }
                        if (!z2) {
                            TaskView findTaskView2 = findTaskView(change2.getTaskInfo());
                            if (findTaskView2 != null) {
                                taskView = findTaskView2;
                            } else {
                                throw new IllegalStateException("TaskView transition is showing a non-taskview task ");
                            }
                        } else {
                            taskView = taskView2;
                        }
                        taskView.prepareOpenAnimation(z2, transaction, transaction2, change2.getTaskInfo(), change2.getLeash(), windowContainerTransaction);
                    } else {
                        throw new IllegalStateException("Claimed transition isn't an opening or closing type: " + change2.getMode());
                    }
                }
                i2++;
            } else {
                transaction.apply();
                transaction2.apply();
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
                startNextTransition();
                return true;
            }
        }
    }
}
