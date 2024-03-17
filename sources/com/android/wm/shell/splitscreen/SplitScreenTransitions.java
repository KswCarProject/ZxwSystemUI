package com.android.wm.shell.splitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda3;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.OneShotRemoteHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;

public class SplitScreenTransitions {
    public OneShotRemoteHandler mActiveRemoteHandler = null;
    public IBinder mAnimatingTransition = null;
    public final ArrayList<Animator> mAnimations = new ArrayList<>();
    public Transitions.TransitionFinishCallback mFinishCallback = null;
    public SurfaceControl.Transaction mFinishTransaction;
    public final Runnable mOnFinish;
    public DismissTransition mPendingDismiss = null;
    public IBinder mPendingEnter = null;
    public IBinder mPendingRecent = null;
    public OneShotRemoteHandler mPendingRemoteHandler = null;
    public final Transitions.TransitionFinishCallback mRemoteFinishCB = new SplitScreenTransitions$$ExternalSyntheticLambda0(this);
    public final StageCoordinator mStageCoordinator;
    public final TransactionPool mTransactionPool;
    public final Transitions mTransitions;

    public SplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, Runnable runnable, StageCoordinator stageCoordinator) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mOnFinish = runnable;
        this.mStageCoordinator = stageCoordinator;
    }

    public void playAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishCallback = transitionFinishCallback;
        this.mAnimatingTransition = iBinder;
        OneShotRemoteHandler oneShotRemoteHandler = this.mPendingRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.startAnimation(iBinder, transitionInfo, transaction, transaction2, this.mRemoteFinishCB);
            this.mActiveRemoteHandler = this.mPendingRemoteHandler;
            this.mPendingRemoteHandler = null;
            return;
        }
        playInternalAnimation(iBinder, transitionInfo, transaction, windowContainerToken, windowContainerToken2);
    }

    public final void playInternalAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishTransaction = this.mTransactionPool.acquire();
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (mode == 6) {
                if (change.getParent() != null) {
                    TransitionInfo.Change change2 = transitionInfo.getChange(change.getParent());
                    transaction.show(change2.getLeash());
                    transaction.setAlpha(change2.getLeash(), 1.0f);
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setLayer(leash, transitionInfo.getChanges().size() - size);
                    this.mFinishTransaction.reparent(leash, change2.getLeash());
                    this.mFinishTransaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
                Rect rect = new Rect(change.getStartAbsBounds());
                Rect rect2 = new Rect(change.getEndAbsBounds());
                rect.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                rect2.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                startExampleResizeAnimation(leash, rect, rect2);
            }
            if (change.getParent() == null) {
                if (iBinder == this.mPendingEnter && (windowContainerToken.equals(change.getContainer()) || windowContainerToken2.equals(change.getContainer()))) {
                    transaction.setPosition(leash, (float) change.getEndAbsBounds().left, (float) change.getEndAbsBounds().top);
                    transaction.setWindowCrop(leash, change.getEndAbsBounds().width(), change.getEndAbsBounds().height());
                }
                boolean isOpeningTransition = isOpeningTransition(transitionInfo);
                if (isOpeningTransition && (mode == 1 || mode == 3)) {
                    startExampleAnimation(leash, true);
                } else if (!isOpeningTransition && (mode == 2 || mode == 4)) {
                    if (transitionInfo.getType() == 18) {
                        transaction.setAlpha(leash, 0.0f);
                    } else {
                        startExampleAnimation(leash, false);
                    }
                }
            }
        }
        transaction.apply();
        onFinish((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
    }

    public IBinder startEnterTransition(int i, WindowContainerTransaction windowContainerTransaction, RemoteTransition remoteTransition, Transitions.TransitionHandler transitionHandler) {
        IBinder startTransition = this.mTransitions.startTransition(i, windowContainerTransaction, transitionHandler);
        this.mPendingEnter = startTransition;
        if (remoteTransition != null) {
            OneShotRemoteHandler oneShotRemoteHandler = new OneShotRemoteHandler(this.mTransitions.getMainExecutor(), remoteTransition);
            this.mPendingRemoteHandler = oneShotRemoteHandler;
            oneShotRemoteHandler.setTransition(startTransition);
        }
        return startTransition;
    }

    public IBinder startDismissTransition(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler, int i, int i2) {
        int i3 = i2 == 4 ? 18 : 19;
        if (iBinder == null) {
            iBinder = this.mTransitions.startTransition(i3, windowContainerTransaction, transitionHandler);
        }
        this.mPendingDismiss = new DismissTransition(iBinder, i2, i);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1852066478, 0, "  splitTransition  deduced Dismiss due to %s. toTop=%s", String.valueOf(SplitScreenController.exitReasonToString(i2)), String.valueOf(SplitScreen.stageTypeToString(i)));
        }
        return iBinder;
    }

    public IBinder startRecentTransition(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler, RemoteTransition remoteTransition) {
        if (iBinder == null) {
            iBinder = this.mTransitions.startTransition(1, windowContainerTransaction, transitionHandler);
        }
        this.mPendingRecent = iBinder;
        if (remoteTransition != null) {
            OneShotRemoteHandler oneShotRemoteHandler = new OneShotRemoteHandler(this.mTransitions.getMainExecutor(), remoteTransition);
            this.mPendingRemoteHandler = oneShotRemoteHandler;
            oneShotRemoteHandler.setTransition(iBinder);
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 822752903, 0, "  splitTransition  deduced Enter recent panel", (Object[]) null);
        }
        return iBinder;
    }

    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        OneShotRemoteHandler oneShotRemoteHandler;
        if (iBinder2 == this.mAnimatingTransition && (oneShotRemoteHandler = this.mActiveRemoteHandler) != null) {
            oneShotRemoteHandler.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, transitionFinishCallback);
        }
    }

    public void onFinish(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        if (this.mAnimations.isEmpty()) {
            IBinder iBinder = this.mAnimatingTransition;
            if (iBinder == this.mPendingEnter) {
                this.mPendingEnter = null;
            }
            DismissTransition dismissTransition = this.mPendingDismiss;
            if (dismissTransition != null && dismissTransition.mTransition == iBinder) {
                this.mPendingDismiss = null;
            }
            if (iBinder == this.mPendingRecent) {
                boolean z = windowContainerTransaction == null;
                if (z) {
                    windowContainerTransaction = new WindowContainerTransaction();
                }
                this.mStageCoordinator.onRecentTransitionFinished(z, windowContainerTransaction, this.mFinishTransaction);
                this.mPendingRecent = null;
            }
            this.mPendingRemoteHandler = null;
            this.mActiveRemoteHandler = null;
            this.mAnimatingTransition = null;
            this.mOnFinish.run();
            SurfaceControl.Transaction transaction = this.mFinishTransaction;
            if (transaction != null) {
                transaction.apply();
                this.mTransactionPool.release(this.mFinishTransaction);
                this.mFinishTransaction = null;
            }
            Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
            if (transitionFinishCallback != null) {
                transitionFinishCallback.onTransitionFinished(windowContainerTransaction, windowContainerTransactionCallback);
                this.mFinishCallback = null;
            }
        }
    }

    public final void startExampleAnimation(SurfaceControl surfaceControl, boolean z) {
        float f = z ? 1.0f : 0.0f;
        float f2 = 1.0f - f;
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        ofFloat.setDuration(500);
        ofFloat.addUpdateListener(new SplitScreenTransitions$$ExternalSyntheticLambda1(acquire, surfaceControl, f2, f));
        final SplitScreenTransitions$$ExternalSyntheticLambda2 splitScreenTransitions$$ExternalSyntheticLambda2 = new SplitScreenTransitions$$ExternalSyntheticLambda2(this, acquire, surfaceControl, f, ofFloat);
        ofFloat.addListener(new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda2.run();
            }

            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda2.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda3(ofFloat));
    }

    public static /* synthetic */ void lambda$startExampleAnimation$0(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$2(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new SplitScreenTransitions$$ExternalSyntheticLambda5(this, valueAnimator));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$1(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
    }

    public final void startExampleResizeAnimation(SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(500);
        ofFloat.addUpdateListener(new SplitScreenTransitions$$ExternalSyntheticLambda3(acquire, surfaceControl, rect, rect2));
        final SplitScreenTransitions$$ExternalSyntheticLambda4 splitScreenTransitions$$ExternalSyntheticLambda4 = new SplitScreenTransitions$$ExternalSyntheticLambda4(this, acquire, surfaceControl, rect2, ofFloat);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda4.run();
            }

            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda4.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda3(ofFloat));
    }

    public static /* synthetic */ void lambda$startExampleResizeAnimation$3(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((((float) rect.width()) * f) + (((float) rect2.width()) * animatedFraction)), (int) ((((float) rect.height()) * f) + (((float) rect2.height()) * animatedFraction)));
        transaction.setPosition(surfaceControl, (((float) rect.left) * f) + (((float) rect2.left) * animatedFraction), (((float) rect.top) * f) + (((float) rect2.top) * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$5(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new SplitScreenTransitions$$ExternalSyntheticLambda6(this, valueAnimator));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$4(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
    }

    public final boolean isOpeningTransition(TransitionInfo transitionInfo) {
        return Transitions.isOpeningType(transitionInfo.getType()) || transitionInfo.getType() == 17 || transitionInfo.getType() == 16;
    }

    public static class DismissTransition {
        public int mDismissTop;
        public int mReason;
        public IBinder mTransition;

        public DismissTransition(IBinder iBinder, int i, int i2) {
            this.mTransition = iBinder;
            this.mReason = i;
            this.mDismissTop = i2;
        }
    }
}
