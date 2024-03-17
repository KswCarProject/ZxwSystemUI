package com.android.wm.shell;

import java.io.PrintWriter;

public interface ShellCommandHandler {
    void dump(PrintWriter printWriter);

    boolean handleCommand(String[] strArr, PrintWriter printWriter);
}
