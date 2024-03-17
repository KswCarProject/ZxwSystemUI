package com.android.wm.shell.tasksurfacehelper;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;

public class TaskSurfaceHelperController {
    public final TaskSurfaceHelperImpl mImpl = new TaskSurfaceHelperImpl();
    public final ShellExecutor mMainExecutor;
    public final ShellTaskOrganizer mTaskOrganizer;

    public TaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
    }

    public TaskSurfaceHelper asTaskSurfaceHelper() {
        return this.mImpl;
    }

    public class TaskSurfaceHelperImpl implements TaskSurfaceHelper {
        public TaskSurfaceHelperImpl() {
        }
    }
}
