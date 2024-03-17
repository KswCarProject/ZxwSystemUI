package com.android.systemui.statusbar.tv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.CoreStartable;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.CommandQueue;
import dagger.Lazy;

public class TvStatusBar extends CoreStartable implements CommandQueue.Callbacks {
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final CommandQueue mCommandQueue;

    public TvStatusBar(Context context, CommandQueue commandQueue, Lazy<AssistManager> lazy) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mAssistManagerLazy = lazy;
    }

    public void start() {
        IStatusBarService asInterface = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        try {
            asInterface.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException unused) {
        }
    }

    public void startAssist(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }

    public void showPictureInPictureMenu() {
        this.mContext.sendBroadcast(new Intent("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU").setPackage(this.mContext.getPackageName()), "com.android.systemui.permission.SELF");
    }
}
