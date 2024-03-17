package com.android.wm.shell.transition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.ITransitionPlayer;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionMetrics;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.IShellTransitions;
import java.util.ArrayList;
import java.util.Arrays;

public class Transitions implements RemoteCallable<Transitions> {
    public static final boolean ENABLE_SHELL_TRANSITIONS;
    public static final boolean SHELL_TRANSITIONS_ROTATION;
    public final ArrayList<ActiveTransition> mActiveTransitions;
    public final ShellExecutor mAnimExecutor;
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final ArrayList<TransitionHandler> mHandlers;
    public final ShellTransitionImpl mImpl;
    public final ShellExecutor mMainExecutor;
    public final WindowOrganizer mOrganizer;
    public final TransitionPlayerImpl mPlayerImpl;
    public final RemoteTransitionHandler mRemoteTransitionHandler;
    public final ArrayList<Runnable> mRunWhenIdleQueue;
    public float mTransitionAnimationScaleSetting;

    public interface TransitionFinishCallback {
        void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback);
    }

    public interface TransitionHandler {
        WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo);

        void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, TransitionFinishCallback transitionFinishCallback) {
        }

        void onTransitionMerged(IBinder iBinder) {
        }

        void setAnimScaleSetting(float f) {
        }

        boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, TransitionFinishCallback transitionFinishCallback);
    }

    public static boolean isClosingType(int i) {
        return i == 2 || i == 4;
    }

    public static boolean isOpeningType(int i) {
        return i == 1 || i == 3 || i == 7;
    }

    static {
        boolean z = false;
        boolean z2 = SystemProperties.getBoolean("persist.wm.debug.shell_transit", false);
        ENABLE_SHELL_TRANSITIONS = z2;
        if (z2 && SystemProperties.getBoolean("persist.wm.debug.shell_transit_rotate", false)) {
            z = true;
        }
        SHELL_TRANSITIONS_ROTATION = z;
    }

    public static final class ActiveTransition {
        public boolean mAborted;
        public SurfaceControl.Transaction mFinishT;
        public TransitionHandler mHandler;
        public TransitionInfo mInfo;
        public boolean mMerged;
        public SurfaceControl.Transaction mStartT;
        public IBinder mToken;

        public ActiveTransition() {
        }
    }

    public Transitions(WindowOrganizer windowOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        ShellExecutor shellExecutor3 = shellExecutor;
        this.mImpl = new ShellTransitionImpl();
        ArrayList<TransitionHandler> arrayList = new ArrayList<>();
        this.mHandlers = arrayList;
        this.mRunWhenIdleQueue = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = windowOrganizer;
        this.mContext = context;
        this.mMainExecutor = shellExecutor3;
        ShellExecutor shellExecutor4 = shellExecutor2;
        this.mAnimExecutor = shellExecutor4;
        this.mDisplayController = displayController;
        this.mPlayerImpl = new TransitionPlayerImpl();
        arrayList.add(new DefaultTransitionHandler(displayController, transactionPool, context, shellExecutor, handler, shellExecutor4));
        RemoteTransitionHandler remoteTransitionHandler = new RemoteTransitionHandler(shellExecutor3);
        this.mRemoteTransitionHandler = remoteTransitionHandler;
        arrayList.add(remoteTransitionHandler);
        ContentResolver contentResolver = context.getContentResolver();
        float f = Settings.Global.getFloat(contentResolver, "transition_animation_scale", context.getResources().getFloat(17105063));
        this.mTransitionAnimationScaleSetting = f;
        dispatchAnimScaleSetting(f);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("transition_animation_scale"), false, new SettingsObserver());
    }

    public Transitions() {
        this.mImpl = new ShellTransitionImpl();
        this.mHandlers = new ArrayList<>();
        this.mRunWhenIdleQueue = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = null;
        this.mContext = null;
        this.mMainExecutor = null;
        this.mAnimExecutor = null;
        this.mDisplayController = null;
        this.mPlayerImpl = null;
        this.mRemoteTransitionHandler = null;
    }

    public ShellTransitions asRemoteTransitions() {
        return this.mImpl;
    }

    public Context getContext() {
        return this.mContext;
    }

    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public final void dispatchAnimScaleSetting(float f) {
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            this.mHandlers.get(size).setAnimScaleSetting(f);
        }
    }

    public void register(ShellTaskOrganizer shellTaskOrganizer) {
        TransitionPlayerImpl transitionPlayerImpl = this.mPlayerImpl;
        if (transitionPlayerImpl != null) {
            shellTaskOrganizer.registerTransitionPlayer(transitionPlayerImpl);
            TransitionMetrics.getInstance();
        }
    }

    public void addHandler(TransitionHandler transitionHandler) {
        this.mHandlers.add(transitionHandler);
    }

    public ShellExecutor getMainExecutor() {
        return this.mMainExecutor;
    }

    public ShellExecutor getAnimExecutor() {
        return this.mAnimExecutor;
    }

    @VisibleForTesting
    public void replaceDefaultHandlerForTest(TransitionHandler transitionHandler) {
        this.mHandlers.set(0, transitionHandler);
    }

    public void runOnIdle(Runnable runnable) {
        if (this.mActiveTransitions.isEmpty()) {
            runnable.run();
        } else {
            this.mRunWhenIdleQueue.add(runnable);
        }
    }

    public static void setupStartState(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (!TransitionInfo.isIndependent(change, transitionInfo)) {
                if (mode == 1 || mode == 3 || mode == 6) {
                    transaction.show(leash);
                    transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                    transaction.setAlpha(leash, 1.0f);
                    transaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
            } else if (mode == 1 || mode == 3) {
                transaction.show(leash);
                transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                if (isOpeningType && (change.getFlags() & 8) == 0) {
                    transaction.setAlpha(leash, 0.0f);
                    transaction2.setAlpha(leash, 1.0f);
                }
            } else if ((mode == 2 || mode == 4) && (change.getFlags() & 2) == 0) {
                transaction2.hide(leash);
            }
        }
    }

    public static void setupAnimHierarchy(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        if (transitionInfo.getRootLeash().isValid()) {
            transaction.show(transitionInfo.getRootLeash());
        }
        int size = transitionInfo.getChanges().size();
        for (int size2 = transitionInfo.getChanges().size() - 1; size2 >= 0; size2--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
            if (TransitionInfo.isIndependent(change, transitionInfo)) {
                if (!(change.getParent() != null)) {
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setPosition(leash, (float) (change.getStartAbsBounds().left - transitionInfo.getRootOffset().x), (float) (change.getStartAbsBounds().top - transitionInfo.getRootOffset().y));
                }
                if (mode == 1 || mode == 3) {
                    if (isOpeningType) {
                        transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                    } else {
                        transaction.setLayer(leash, size - size2);
                    }
                } else if (mode != 2 && mode != 4) {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                } else if (isOpeningType) {
                    transaction.setLayer(leash, size - size2);
                } else {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                }
            }
        }
    }

    public final int findActiveTransition(IBinder iBinder) {
        for (int size = this.mActiveTransitions.size() - 1; size >= 0; size--) {
            if (this.mActiveTransitions.get(size).mToken == iBinder) {
                return size;
            }
        }
        return -1;
    }

    @VisibleForTesting
    public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean z = true;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1070270131, 0, "onTransitionReady %s: %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
        }
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            throw new IllegalStateException("Got transitionReady for non-active transition " + iBinder + ". expecting one of " + Arrays.toString(this.mActiveTransitions.stream().map(new Transitions$$ExternalSyntheticLambda0()).toArray()));
        } else if (!transitionInfo.getRootLeash().isValid()) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 410592459, 0, "Invalid root leash (%s): %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            transaction.apply();
            onAbort(iBinder);
        } else {
            int size = transitionInfo.getChanges().size();
            if (size == 2) {
                int i = size - 1;
                boolean z2 = false;
                while (true) {
                    if (i < 0) {
                        break;
                    }
                    TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                    if (change.getTaskInfo() != null) {
                        z = false;
                        break;
                    }
                    if ((change.getFlags() & 8) != 0) {
                        z2 = true;
                    }
                    i--;
                }
                if (z && z2) {
                    transaction.apply();
                    onAbort(iBinder);
                    return;
                }
            }
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mInfo = transitionInfo;
            activeTransition.mStartT = transaction;
            activeTransition.mFinishT = transaction2;
            setupStartState(transitionInfo, transaction, transaction2);
            if (findActiveTransition > 0) {
                attemptMergeTransition(this.mActiveTransitions.get(0), activeTransition);
            } else {
                playTransition(activeTransition);
            }
        }
    }

    public void attemptMergeTransition(ActiveTransition activeTransition, ActiveTransition activeTransition2) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            String valueOf = String.valueOf(activeTransition2.mToken);
            String valueOf2 = String.valueOf(activeTransition.mToken);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 313857748, 0, "Transition %s ready while another transition %s is still animating. Notify the animating transition in case they can be merged", valueOf, valueOf2);
        }
        activeTransition.mHandler.mergeAnimation(activeTransition2.mToken, activeTransition2.mInfo, activeTransition2.mStartT, activeTransition.mToken, new Transitions$$ExternalSyntheticLambda1(this, activeTransition2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attemptMergeTransition$1(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    public boolean startAnimation(ActiveTransition activeTransition, TransitionHandler transitionHandler) {
        return transitionHandler.startAnimation(activeTransition.mToken, activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT, new Transitions$$ExternalSyntheticLambda2(this, activeTransition));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$2(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    public void playTransition(ActiveTransition activeTransition) {
        setupAnimHierarchy(activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT);
        TransitionHandler transitionHandler = activeTransition.mHandler;
        if (transitionHandler != null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 138343607, 0, " try firstHandler %s", String.valueOf(transitionHandler));
            }
            if (startAnimation(activeTransition, activeTransition.mHandler)) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 707170340, 0, " animated by firstHandler", (Object[]) null);
                    return;
                }
                return;
            }
        }
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            if (this.mHandlers.get(size) != activeTransition.mHandler) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1308483871, 0, " try handler %s", String.valueOf(this.mHandlers.get(size)));
                }
                if (startAnimation(activeTransition, this.mHandlers.get(size))) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1297259344, 0, " animated by %s", String.valueOf(this.mHandlers.get(size)));
                    }
                    activeTransition.mHandler = this.mHandlers.get(size);
                    return;
                }
            }
        }
        throw new IllegalStateException("This shouldn't happen, maybe the default handler is broken.");
    }

    public final void onAbort(IBinder iBinder) {
        onFinish(iBinder, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null, true);
    }

    public final void onFinish(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(iBinder, windowContainerTransaction, windowContainerTransactionCallback, false);
    }

    public final void onFinish(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback, boolean z) {
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            Log.e("ShellTransitions", "Trying to finish a non-running transition. Either remote crashed or  a handler didn't properly deal with a merge.", new RuntimeException());
            return;
        }
        if (findActiveTransition > 0) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                String valueOf = String.valueOf(iBinder);
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1533341034, 3, "Transition was merged (abort=%b: %s", Boolean.valueOf(z), valueOf);
            }
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mMerged = true;
            activeTransition.mAborted = z;
            TransitionHandler transitionHandler = activeTransition.mHandler;
            if (transitionHandler != null) {
                transitionHandler.onTransitionMerged(activeTransition.mToken);
                return;
            }
            return;
        }
        this.mActiveTransitions.get(findActiveTransition).mAborted = z;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            String valueOf2 = String.valueOf(iBinder);
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1799955124, 3, "Transition animation finished (abort=%b), notifying core %s", Boolean.valueOf(z), valueOf2);
        }
        SurfaceControl.Transaction transaction = this.mActiveTransitions.get(findActiveTransition).mFinishT;
        int i = findActiveTransition + 1;
        while (i < this.mActiveTransitions.size() && this.mActiveTransitions.get(i).mMerged && this.mActiveTransitions.get(i).mStartT != null) {
            if (transaction == null) {
                transaction = new SurfaceControl.Transaction();
            }
            transaction.merge(this.mActiveTransitions.get(i).mStartT);
            transaction.merge(this.mActiveTransitions.get(i).mFinishT);
            i++;
        }
        if (transaction != null) {
            transaction.apply();
        }
        this.mActiveTransitions.remove(findActiveTransition);
        this.mOrganizer.finishTransition(iBinder, windowContainerTransaction, windowContainerTransactionCallback);
        while (findActiveTransition < this.mActiveTransitions.size() && this.mActiveTransitions.get(findActiveTransition).mMerged) {
            this.mOrganizer.finishTransition(this.mActiveTransitions.remove(findActiveTransition).mToken, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        while (this.mActiveTransitions.size() > findActiveTransition && this.mActiveTransitions.get(findActiveTransition).mAborted) {
            this.mOrganizer.finishTransition(this.mActiveTransitions.remove(findActiveTransition).mToken, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
        if (this.mActiveTransitions.size() <= findActiveTransition) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 490784151, 0, "All active transition animations finished", (Object[]) null);
            }
            for (int i2 = 0; i2 < this.mRunWhenIdleQueue.size(); i2++) {
                this.mRunWhenIdleQueue.get(i2).run();
            }
            this.mRunWhenIdleQueue.clear();
            return;
        }
        ActiveTransition activeTransition2 = this.mActiveTransitions.get(findActiveTransition);
        if (activeTransition2.mInfo != null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -821069485, 0, "Pending transitions after one finished, so start the next one.", (Object[]) null);
            }
            playTransition(activeTransition2);
            int findActiveTransition2 = findActiveTransition(activeTransition2.mToken);
            if (findActiveTransition2 >= 0) {
                int i3 = findActiveTransition2 + 1;
                while (i3 < this.mActiveTransitions.size()) {
                    ActiveTransition activeTransition3 = this.mActiveTransitions.get(i3);
                    if (!activeTransition3.mAborted) {
                        if (!activeTransition3.mMerged) {
                            attemptMergeTransition(activeTransition2, activeTransition3);
                            i3 = findActiveTransition(activeTransition3.mToken);
                            if (i3 < 0) {
                                return;
                            }
                        } else {
                            throw new IllegalStateException("Can't merge a transition after not-merging a preceding one.");
                        }
                    }
                    i3++;
                }
            }
        } else if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 305060772, 0, "Pending transition after one finished, but it isn't ready yet.", (Object[]) null);
        }
    }

    public void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -2076257741, 0, "Transition requested: %s %s", String.valueOf(iBinder), String.valueOf(transitionRequestInfo));
        }
        if (findActiveTransition(iBinder) < 0) {
            WindowContainerTransaction windowContainerTransaction = null;
            ActiveTransition activeTransition = new ActiveTransition();
            int size = this.mHandlers.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                windowContainerTransaction = this.mHandlers.get(size).handleRequest(iBinder, transitionRequestInfo);
                if (windowContainerTransaction != null) {
                    activeTransition.mHandler = this.mHandlers.get(size);
                    break;
                }
                size--;
            }
            if (transitionRequestInfo.getDisplayChange() != null) {
                TransitionRequestInfo.DisplayChange displayChange = transitionRequestInfo.getDisplayChange();
                if (displayChange.getEndRotation() != displayChange.getStartRotation()) {
                    if (windowContainerTransaction == null) {
                        windowContainerTransaction = new WindowContainerTransaction();
                    }
                    this.mDisplayController.getChangeController().dispatchOnRotateDisplay(windowContainerTransaction, displayChange.getDisplayId(), displayChange.getStartRotation(), displayChange.getEndRotation());
                }
            }
            activeTransition.mToken = this.mOrganizer.startTransition(transitionRequestInfo.getType(), iBinder, windowContainerTransaction);
            this.mActiveTransitions.add(activeTransition);
            return;
        }
        throw new RuntimeException("Transition already started " + iBinder);
    }

    public IBinder startTransition(int i, WindowContainerTransaction windowContainerTransaction, TransitionHandler transitionHandler) {
        ActiveTransition activeTransition = new ActiveTransition();
        activeTransition.mHandler = transitionHandler;
        activeTransition.mToken = this.mOrganizer.startTransition(i, (IBinder) null, windowContainerTransaction);
        this.mActiveTransitions.add(activeTransition);
        return activeTransition.mToken;
    }

    public class TransitionPlayerImpl extends ITransitionPlayer.Stub {
        public TransitionPlayerImpl() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransitionReady$0(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
            Transitions.this.onTransitionReady(iBinder, transitionInfo, transaction, transaction2);
        }

        public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0(this, iBinder, transitionInfo, transaction, transaction2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$requestStartTransition$1(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
            Transitions.this.requestStartTransition(iBinder, transitionRequestInfo);
        }

        public void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda1(this, iBinder, transitionRequestInfo));
        }
    }

    public class ShellTransitionImpl implements ShellTransitions {
        public IShellTransitionsImpl mIShellTransitions;

        public ShellTransitionImpl() {
        }

        public IShellTransitions createExternalInterface() {
            IShellTransitionsImpl iShellTransitionsImpl = this.mIShellTransitions;
            if (iShellTransitionsImpl != null) {
                iShellTransitionsImpl.invalidate();
            }
            IShellTransitionsImpl iShellTransitionsImpl2 = new IShellTransitionsImpl(Transitions.this);
            this.mIShellTransitions = iShellTransitionsImpl2;
            return iShellTransitionsImpl2;
        }

        public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            Transitions.this.mMainExecutor.execute(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(this, transitionFilter, remoteTransition));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerRemote$0(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            Transitions.this.mRemoteTransitionHandler.addFiltered(transitionFilter, remoteTransition);
        }
    }

    public static class IShellTransitionsImpl extends IShellTransitions.Stub {
        public Transitions mTransitions;

        public IShellTransitionsImpl(Transitions transitions) {
            this.mTransitions = transitions;
        }

        public void invalidate() {
            this.mTransitions = null;
        }

        public void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "registerRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0(transitionFilter, remoteTransition));
        }

        public void unregisterRemote(RemoteTransition remoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "unregisterRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(remoteTransition));
        }
    }

    public class SettingsObserver extends ContentObserver {
        public SettingsObserver() {
            super((Handler) null);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            Transitions transitions = Transitions.this;
            transitions.mTransitionAnimationScaleSetting = Settings.Global.getFloat(transitions.mContext.getContentResolver(), "transition_animation_scale", Transitions.this.mTransitionAnimationScaleSetting);
            Transitions.this.mMainExecutor.execute(new Transitions$SettingsObserver$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$0() {
            Transitions transitions = Transitions.this;
            transitions.dispatchAnimScaleSetting(transitions.mTransitionAnimationScaleSetting);
        }
    }
}
