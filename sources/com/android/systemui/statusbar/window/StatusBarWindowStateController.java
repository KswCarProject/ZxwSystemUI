package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.CommandQueue;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarWindowStateController.kt */
public final class StatusBarWindowStateController {
    @NotNull
    public final StatusBarWindowStateController$commandQueueCallback$1 commandQueueCallback;
    @NotNull
    public final Set<StatusBarWindowStateListener> listeners = new HashSet();
    public final int thisDisplayId;
    public int windowState;

    public StatusBarWindowStateController(int i, @NotNull CommandQueue commandQueue) {
        this.thisDisplayId = i;
        StatusBarWindowStateController$commandQueueCallback$1 statusBarWindowStateController$commandQueueCallback$1 = new StatusBarWindowStateController$commandQueueCallback$1(this);
        this.commandQueueCallback = statusBarWindowStateController$commandQueueCallback$1;
        commandQueue.addCallback((CommandQueue.Callbacks) statusBarWindowStateController$commandQueueCallback$1);
    }

    public final void addListener(@NotNull StatusBarWindowStateListener statusBarWindowStateListener) {
        this.listeners.add(statusBarWindowStateListener);
    }

    public final boolean windowIsShowing() {
        return this.windowState == 0;
    }

    public final void setWindowState(int i, int i2, int i3) {
        if (i == this.thisDisplayId && i2 == 1 && this.windowState != i3) {
            this.windowState = i3;
            for (StatusBarWindowStateListener onStatusBarWindowStateChanged : this.listeners) {
                onStatusBarWindowStateChanged.onStatusBarWindowStateChanged(i3);
            }
        }
    }
}
