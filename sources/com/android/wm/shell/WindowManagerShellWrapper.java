package com.android.wm.shell;

import android.os.RemoteException;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;

public class WindowManagerShellWrapper {
    public final PinnedStackListenerForwarder mPinnedStackListenerForwarder;

    public WindowManagerShellWrapper(ShellExecutor shellExecutor) {
        this.mPinnedStackListenerForwarder = new PinnedStackListenerForwarder(shellExecutor);
    }

    public void addPinnedStackListener(PinnedStackListenerForwarder.PinnedTaskListener pinnedTaskListener) throws RemoteException {
        this.mPinnedStackListenerForwarder.addListener(pinnedTaskListener);
        this.mPinnedStackListenerForwarder.register(0);
    }
}
