package com.android.wm.shell.pip;

import android.content.ComponentName;
import android.os.RemoteException;
import android.view.IPinnedTaskListener;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;

public class PinnedStackListenerForwarder {
    public final IPinnedTaskListener mListenerImpl = new PinnedTaskListenerImpl();
    public final ArrayList<PinnedTaskListener> mListeners = new ArrayList<>();
    public final ShellExecutor mMainExecutor;

    public static class PinnedTaskListener {
        public void onActivityHidden(ComponentName componentName) {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
        }

        public void onMovementBoundsChanged(boolean z) {
        }
    }

    public PinnedStackListenerForwarder(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void addListener(PinnedTaskListener pinnedTaskListener) {
        this.mListeners.add(pinnedTaskListener);
    }

    public void register(int i) throws RemoteException {
        WindowManagerGlobal.getWindowManagerService().registerPinnedTaskListener(i, this.mListenerImpl);
    }

    public final void onMovementBoundsChanged(boolean z) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onMovementBoundsChanged(z);
        }
    }

    public final void onImeVisibilityChanged(boolean z, int i) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onImeVisibilityChanged(z, i);
        }
    }

    public final void onActivityHidden(ComponentName componentName) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActivityHidden(componentName);
        }
    }

    public class PinnedTaskListenerImpl extends IPinnedTaskListener.Stub {
        public PinnedTaskListenerImpl() {
        }

        public void onMovementBoundsChanged(boolean z) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onMovementBoundsChanged$0(boolean z) {
            PinnedStackListenerForwarder.this.onMovementBoundsChanged(z);
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0(this, z, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onImeVisibilityChanged$1(boolean z, int i) {
            PinnedStackListenerForwarder.this.onImeVisibilityChanged(z, i);
        }

        public void onActivityHidden(ComponentName componentName) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1(this, componentName));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onActivityHidden$2(ComponentName componentName) {
            PinnedStackListenerForwarder.this.onActivityHidden(componentName);
        }
    }
}
