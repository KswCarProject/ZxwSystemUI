package com.android.systemui.shortcut;

import android.content.Context;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.systemui.CoreStartable;
import com.android.systemui.shortcut.ShortcutKeyServiceProxy;

public class ShortcutKeyDispatcher extends CoreStartable implements ShortcutKeyServiceProxy.Callbacks {
    public final long ALT_MASK = 8589934592L;
    public final long CTRL_MASK = 17592186044416L;
    public final long META_MASK = 281474976710656L;
    public final long SC_DOCK_LEFT = 281474976710727L;
    public final long SC_DOCK_RIGHT = 281474976710728L;
    public final long SHIFT_MASK = 4294967296L;
    public ShortcutKeyServiceProxy mShortcutKeyServiceProxy = new ShortcutKeyServiceProxy(this);
    public IWindowManager mWindowManagerService = WindowManagerGlobal.getWindowManagerService();

    public final void handleDockKey(long j) {
    }

    public ShortcutKeyDispatcher(Context context) {
        super(context);
    }

    public void registerShortcutKey(long j) {
        try {
            this.mWindowManagerService.registerShortcutKey(j, this.mShortcutKeyServiceProxy);
        } catch (RemoteException unused) {
        }
    }

    public void onShortcutKeyPressed(long j) {
        int i = this.mContext.getResources().getConfiguration().orientation;
        if ((j == 281474976710727L || j == 281474976710728L) && i == 2) {
            handleDockKey(j);
        }
    }

    public void start() {
        registerShortcutKey(281474976710727L);
        registerShortcutKey(281474976710728L);
    }
}
