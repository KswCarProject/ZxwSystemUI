package com.android.wm.shell;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class TaskViewFactoryController {
    public final TaskViewFactory mImpl = new TaskViewFactoryImpl();
    public final ShellExecutor mShellExecutor;
    public final SyncTransactionQueue mSyncQueue;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TaskViewTransitions mTaskViewTransitions;

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = taskViewTransitions;
    }

    public TaskViewFactory asTaskViewFactory() {
        return this.mImpl;
    }

    public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
        executor.execute(new TaskViewFactoryController$$ExternalSyntheticLambda0(consumer, new TaskView(context, this.mTaskOrganizer, this.mTaskViewTransitions, this.mSyncQueue)));
    }

    public class TaskViewFactoryImpl implements TaskViewFactory {
        public TaskViewFactoryImpl() {
        }

        public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
            TaskViewFactoryController.this.mShellExecutor.execute(new TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0(this, context, executor, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$create$0(Context context, Executor executor, Consumer consumer) {
            TaskViewFactoryController.this.create(context, executor, consumer);
        }
    }
}
