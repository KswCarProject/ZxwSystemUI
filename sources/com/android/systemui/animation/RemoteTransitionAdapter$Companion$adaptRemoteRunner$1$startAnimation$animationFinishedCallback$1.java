package com.android.systemui.animation;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.SurfaceControl;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionInfo;
import android.window.WindowContainerTransaction;
import com.android.systemui.animation.RemoteTransitionAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: RemoteTransitionAdapter.kt */
public final class RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1 implements IRemoteAnimationFinishedCallback {
    public final /* synthetic */ RemoteTransitionAdapter.CounterRotator $counterLauncher;
    public final /* synthetic */ RemoteTransitionAdapter.CounterRotator $counterWallpaper;
    public final /* synthetic */ IRemoteTransitionFinishedCallback $finishCallback;
    public final /* synthetic */ TransitionInfo $info;
    public final /* synthetic */ ArrayMap<SurfaceControl, SurfaceControl> $leashMap;

    @Nullable
    public IBinder asBinder() {
        return null;
    }

    public RemoteTransitionAdapter$Companion$adaptRemoteRunner$1$startAnimation$animationFinishedCallback$1(RemoteTransitionAdapter.CounterRotator counterRotator, RemoteTransitionAdapter.CounterRotator counterRotator2, TransitionInfo transitionInfo, ArrayMap<SurfaceControl, SurfaceControl> arrayMap, IRemoteTransitionFinishedCallback iRemoteTransitionFinishedCallback) {
        this.$counterLauncher = counterRotator;
        this.$counterWallpaper = counterRotator2;
        this.$info = transitionInfo;
        this.$leashMap = arrayMap;
        this.$finishCallback = iRemoteTransitionFinishedCallback;
    }

    public void onAnimationFinished() {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        this.$counterLauncher.cleanUp(transaction);
        this.$counterWallpaper.cleanUp(transaction);
        int size = this.$info.getChanges().size() - 1;
        if (size >= 0) {
            while (true) {
                int i = size - 1;
                ((TransitionInfo.Change) this.$info.getChanges().get(size)).getLeash().release();
                if (i < 0) {
                    break;
                }
                size = i;
            }
        }
        int size2 = this.$leashMap.size() - 1;
        if (size2 >= 0) {
            while (true) {
                int i2 = size2 - 1;
                this.$leashMap.valueAt(size2).release();
                if (i2 >= 0) {
                    size2 = i2;
                }
            }
            this.$finishCallback.onTransitionFinished((WindowContainerTransaction) null, transaction);
        }
        try {
            this.$finishCallback.onTransitionFinished((WindowContainerTransaction) null, transaction);
        } catch (RemoteException e) {
            Log.e("ActivityOptionsCompat", "Failed to call app controlled animation finished callback", e);
        }
    }
}
