package com.android.wm.shell.transition;

import android.window.RemoteTransition;
import android.window.TransitionFilter;

public interface ShellTransitions {
    IShellTransitions createExternalInterface() {
        return null;
    }

    void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
    }
}
