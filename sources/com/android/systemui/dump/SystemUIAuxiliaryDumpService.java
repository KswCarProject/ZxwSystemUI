package com.android.systemui.dump;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class SystemUIAuxiliaryDumpService extends Service {
    public final DumpHandler mDumpHandler;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public SystemUIAuxiliaryDumpService(DumpHandler dumpHandler) {
        this.mDumpHandler = dumpHandler;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mDumpHandler.dump(printWriter, new String[]{"--dump-priority", "NORMAL"});
    }
}
