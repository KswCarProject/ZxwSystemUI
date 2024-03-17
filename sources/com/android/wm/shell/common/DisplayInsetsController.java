package com.android.wm.shell.common;

import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IDisplayWindowInsetsController;
import android.view.IWindowManager;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import com.android.wm.shell.common.DisplayController;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayInsetsController implements DisplayController.OnDisplaysChangedListener {
    public final DisplayController mDisplayController;
    public final SparseArray<PerDisplay> mInsetsPerDisplay = new SparseArray<>();
    public final SparseArray<CopyOnWriteArrayList<OnInsetsChangedListener>> mListeners = new SparseArray<>();
    public final ShellExecutor mMainExecutor;
    public final IWindowManager mWmService;

    public interface OnInsetsChangedListener {
        void hideInsets(int i, boolean z) {
        }

        void insetsChanged(InsetsState insetsState) {
        }

        void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        }

        void showInsets(int i, boolean z) {
        }

        void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) {
        }
    }

    public DisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mMainExecutor = shellExecutor;
    }

    public void initialize() {
        this.mDisplayController.addDisplayWindowListener(this);
    }

    public void addInsetsChangedListener(int i, OnInsetsChangedListener onInsetsChangedListener) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mListeners.get(i);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.mListeners.put(i, copyOnWriteArrayList);
        }
        if (!copyOnWriteArrayList.contains(onInsetsChangedListener)) {
            copyOnWriteArrayList.add(onInsetsChangedListener);
        }
    }

    public void removeInsetsChangedListener(int i, OnInsetsChangedListener onInsetsChangedListener) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mListeners.get(i);
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(onInsetsChangedListener);
        }
    }

    public void onDisplayAdded(int i) {
        PerDisplay perDisplay = new PerDisplay(i);
        perDisplay.register();
        this.mInsetsPerDisplay.put(i, perDisplay);
    }

    public void onDisplayRemoved(int i) {
        PerDisplay perDisplay = this.mInsetsPerDisplay.get(i);
        if (perDisplay != null) {
            perDisplay.unregister();
            this.mInsetsPerDisplay.remove(i);
        }
    }

    public class PerDisplay {
        public final int mDisplayId;
        public final DisplayWindowInsetsControllerImpl mInsetsControllerImpl = new DisplayWindowInsetsControllerImpl();

        public PerDisplay(int i) {
            this.mDisplayId = i;
        }

        public void register() {
            try {
                DisplayInsetsController.this.mWmService.setDisplayWindowInsetsController(this.mDisplayId, this.mInsetsControllerImpl);
            } catch (RemoteException unused) {
                Slog.w("DisplayInsetsController", "Unable to set insets controller on display " + this.mDisplayId);
            }
        }

        public void unregister() {
            try {
                DisplayInsetsController.this.mWmService.setDisplayWindowInsetsController(this.mDisplayId, (IDisplayWindowInsetsController) null);
            } catch (RemoteException unused) {
                Slog.w("DisplayInsetsController", "Unable to remove insets controller on display " + this.mDisplayId);
            }
        }

        public final void insetsChanged(InsetsState insetsState) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                DisplayInsetsController.this.mDisplayController.updateDisplayInsets(this.mDisplayId, insetsState);
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).insetsChanged(insetsState);
                }
            }
        }

        public final void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).insetsControlChanged(insetsState, insetsSourceControlArr);
                }
            }
        }

        public final void showInsets(int i, boolean z) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).showInsets(i, z);
                }
            }
        }

        public final void hideInsets(int i, boolean z) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).hideInsets(i, z);
                }
            }
        }

        public final void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).topFocusedWindowChanged(str, insetsVisibilities);
                }
            }
        }

        public class DisplayWindowInsetsControllerImpl extends IDisplayWindowInsetsController.Stub {
            public DisplayWindowInsetsControllerImpl() {
            }

            public void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2(this, str, insetsVisibilities));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$topFocusedWindowChanged$0(String str, InsetsVisibilities insetsVisibilities) {
                PerDisplay.this.topFocusedWindowChanged(str, insetsVisibilities);
            }

            public void insetsChanged(InsetsState insetsState) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1(this, insetsState));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$insetsChanged$1(InsetsState insetsState) {
                PerDisplay.this.insetsChanged(insetsState);
            }

            public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3(this, insetsState, insetsSourceControlArr));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$insetsControlChanged$2(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
                PerDisplay.this.insetsControlChanged(insetsState, insetsSourceControlArr);
            }

            public void showInsets(int i, boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda0(this, i, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$showInsets$3(int i, boolean z) {
                PerDisplay.this.showInsets(i, z);
            }

            public void hideInsets(int i, boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda4(this, i, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$hideInsets$4(int i, boolean z) {
                PerDisplay.this.hideInsets(i, z);
            }
        }
    }
}
