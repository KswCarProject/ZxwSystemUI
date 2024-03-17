package com.android.wm.shell.transition;

import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.IWindowContainerTransactionCallback;

public class LegacyTransitions$LegacyTransition {
    public final RemoteAnimationAdapter mAdapter = new RemoteAnimationAdapter(new RemoteAnimationWrapper(), 0, 0);
    public RemoteAnimationTarget[] mApps;
    public boolean mCancelled = false;
    public IRemoteAnimationFinishedCallback mFinishCallback = null;
    public final LegacyTransitions$ILegacyTransition mLegacyTransition;
    public RemoteAnimationTarget[] mNonApps;
    public final SyncCallback mSyncCallback = new SyncCallback();
    public int mSyncId = -1;
    public SurfaceControl.Transaction mTransaction;
    public int mTransit;
    public RemoteAnimationTarget[] mWallpapers;

    public LegacyTransitions$LegacyTransition(int i, LegacyTransitions$ILegacyTransition legacyTransitions$ILegacyTransition) {
        this.mLegacyTransition = legacyTransitions$ILegacyTransition;
        this.mTransit = i;
    }

    public int getType() {
        return this.mTransit;
    }

    public IWindowContainerTransactionCallback getSyncCallback() {
        return this.mSyncCallback;
    }

    public RemoteAnimationAdapter getAdapter() {
        return this.mAdapter;
    }

    public class SyncCallback extends IWindowContainerTransactionCallback.Stub {
        public SyncCallback() {
        }

        public void onTransactionReady(int i, SurfaceControl.Transaction transaction) throws RemoteException {
            LegacyTransitions$LegacyTransition.this.mSyncId = i;
            LegacyTransitions$LegacyTransition.this.mTransaction = transaction;
            LegacyTransitions$LegacyTransition.this.checkApply();
        }
    }

    public class RemoteAnimationWrapper extends IRemoteAnimationRunner.Stub {
        public RemoteAnimationWrapper() {
        }

        public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) throws RemoteException {
            LegacyTransitions$LegacyTransition.this.mTransit = i;
            LegacyTransitions$LegacyTransition.this.mApps = remoteAnimationTargetArr;
            LegacyTransitions$LegacyTransition.this.mWallpapers = remoteAnimationTargetArr2;
            LegacyTransitions$LegacyTransition.this.mNonApps = remoteAnimationTargetArr3;
            LegacyTransitions$LegacyTransition.this.mFinishCallback = iRemoteAnimationFinishedCallback;
            LegacyTransitions$LegacyTransition.this.checkApply();
        }

        public void onAnimationCancelled(boolean z) throws RemoteException {
            LegacyTransitions$LegacyTransition.this.mCancelled = true;
            LegacyTransitions$LegacyTransition legacyTransitions$LegacyTransition = LegacyTransitions$LegacyTransition.this;
            legacyTransitions$LegacyTransition.mNonApps = null;
            legacyTransitions$LegacyTransition.mWallpapers = null;
            legacyTransitions$LegacyTransition.mApps = null;
            LegacyTransitions$LegacyTransition.this.checkApply();
        }
    }

    public final void checkApply() throws RemoteException {
        if (this.mSyncId >= 0) {
            IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mFinishCallback;
            if (iRemoteAnimationFinishedCallback != null || this.mCancelled) {
                this.mLegacyTransition.onAnimationStart(this.mTransit, this.mApps, this.mWallpapers, this.mNonApps, iRemoteAnimationFinishedCallback, this.mTransaction);
            }
        }
    }
}
