package com.android.wm.shell.common;

import android.content.Context;

public interface RemoteCallable<T> {
    Context getContext();

    ShellExecutor getRemoteCallExecutor();
}
