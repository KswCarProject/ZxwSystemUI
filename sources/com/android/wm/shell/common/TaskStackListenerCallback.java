package com.android.wm.shell.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.window.TaskSnapshot;

public interface TaskStackListenerCallback {
    void onActivityDismissingDockedStack() {
    }

    void onActivityForcedResizable(String str, int i, int i2) {
    }

    void onActivityLaunchOnSecondaryDisplayFailed() {
    }

    void onActivityLaunchOnSecondaryDisplayRerouted() {
    }

    void onActivityPinned(String str, int i, int i2, int i3) {
    }

    void onActivityRequestedOrientationChanged(int i, int i2) {
    }

    void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
    }

    void onActivityRotation(int i) {
    }

    void onActivityUnpinned() {
    }

    void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    void onRecentTaskListFrozenChanged(boolean z) {
    }

    void onRecentTaskListUpdated() {
    }

    void onTaskCreated(int i, ComponentName componentName) {
    }

    void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    void onTaskDisplayChanged(int i, int i2) {
    }

    void onTaskMovedToFront(int i) {
    }

    void onTaskProfileLocked(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    void onTaskRemoved(int i) {
    }

    boolean onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) {
        return false;
    }

    void onTaskStackChanged() {
    }

    void onTaskStackChangedBackground() {
    }

    void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onTaskMovedToFront(runningTaskInfo.taskId);
    }

    void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayFailed();
    }

    void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayRerouted();
    }
}
