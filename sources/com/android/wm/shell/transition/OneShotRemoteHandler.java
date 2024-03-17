package com.android.wm.shell.transition;

import android.app.ActivityTaskManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;

public class OneShotRemoteHandler implements Transitions.TransitionHandler {
    public final ShellExecutor mMainExecutor;
    public final RemoteTransition mRemote;
    public IBinder mTransition = null;

    public OneShotRemoteHandler(ShellExecutor shellExecutor, RemoteTransition remoteTransition) {
        this.mMainExecutor = shellExecutor;
        this.mRemote = remoteTransition;
    }

    public void setTransition(IBinder iBinder) {
        this.mTransition = iBinder;
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final SurfaceControl.Transaction transaction2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (this.mTransition != iBinder) {
            return false;
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            String valueOf = String.valueOf(this.mRemote);
            String valueOf2 = String.valueOf(iBinder);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", valueOf, valueOf2);
        }
        final OneShotRemoteHandler$$ExternalSyntheticLambda0 oneShotRemoteHandler$$ExternalSyntheticLambda0 = new OneShotRemoteHandler$$ExternalSyntheticLambda0(this, transitionFinishCallback);
        AnonymousClass1 r4 = new IRemoteTransitionFinishedCallback.Stub() {
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                if (OneShotRemoteHandler.this.mRemote.asBinder() != null) {
                    OneShotRemoteHandler.this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
                }
                OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$1$$ExternalSyntheticLambda0(transaction, transaction2, transitionFinishCallback, windowContainerTransaction));
            }

            public static /* synthetic */ void lambda$onTransitionFinished$0(SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
                if (transaction != null) {
                    transaction2.merge(transaction);
                }
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction, (WindowContainerTransactionCallback) null);
            }
        };
        try {
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().linkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            try {
                ActivityTaskManager.getService().setRunningRemoteTransitionDelegate(this.mRemote.getAppThread());
            } catch (SecurityException unused) {
                Slog.e("ShellTransitions", "Unable to boost animation thread. This should only happen during unit tests");
            }
            this.mRemote.getRemoteTransition().startAnimation(iBinder, transitionInfo, transaction, r4);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$1(Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new OneShotRemoteHandler$$ExternalSyntheticLambda1(transitionFinishCallback));
    }

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            String valueOf = String.valueOf(this.mRemote);
            String valueOf2 = String.valueOf(iBinder);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", valueOf, valueOf2);
        }
        final Transitions.TransitionFinishCallback transitionFinishCallback2 = transitionFinishCallback;
        try {
            this.mRemote.getRemoteTransition().mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() {
                public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
                    OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(transitionFinishCallback2, windowContainerTransaction));
                }
            });
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error merging remote transition.", e);
        }
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        RemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if ((remoteTransition != null ? remoteTransition.getRemoteTransition() : null) != this.mRemote.getRemoteTransition()) {
            return null;
        }
        this.mTransition = iBinder;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 967375804, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
