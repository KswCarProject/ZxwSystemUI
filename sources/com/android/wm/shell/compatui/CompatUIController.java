package com.android.wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.compatui.CompatUIWindowManager;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager;
import com.android.wm.shell.transition.Transitions;
import dagger.Lazy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CompatUIController implements DisplayController.OnDisplaysChangedListener, DisplayImeController.ImePositionProcessor {
    public final SparseArray<CompatUIWindowManager> mActiveCompatLayouts = new SparseArray<>(0);
    public LetterboxEduWindowManager mActiveLetterboxEduLayout;
    public CompatUICallback mCallback;
    public final CompatUIWindowManager.CompatUIHintsState mCompatUIHintsState;
    public final Context mContext;
    public final SparseArray<WeakReference<Context>> mDisplayContextCache = new SparseArray<>(0);
    public final DisplayController mDisplayController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final Set<Integer> mDisplaysWithIme = new ArraySet(1);
    public final DisplayImeController mImeController;
    public final CompatUIImpl mImpl = new CompatUIImpl();
    public boolean mKeyguardShowing;
    public final ShellExecutor mMainExecutor;
    public final SparseArray<PerDisplayOnInsetsChangedListener> mOnInsetsChangedListeners = new SparseArray<>(0);
    public final SyncTransactionQueue mSyncQueue;
    public final Lazy<Transitions> mTransitionsLazy;

    public interface CompatUICallback {
        void onCameraControlStateUpdated(int i, int i2);

        void onSizeCompatRestartButtonAppeared(int i);

        void onSizeCompatRestartButtonClicked(int i);
    }

    public static /* synthetic */ boolean lambda$forAllLayouts$5(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract) {
        return true;
    }

    public CompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, ShellExecutor shellExecutor, Lazy<Transitions> lazy) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mImeController = displayImeController;
        this.mSyncQueue = syncTransactionQueue;
        this.mMainExecutor = shellExecutor;
        this.mTransitionsLazy = lazy;
        displayController.addDisplayWindowListener(this);
        displayImeController.addPositionProcessor(this);
        this.mCompatUIHintsState = new CompatUIWindowManager.CompatUIHintsState();
    }

    public CompatUI asCompatUI() {
        return this.mImpl;
    }

    public void setCompatUICallback(CompatUICallback compatUICallback) {
        this.mCallback = compatUICallback;
    }

    public void onCompatInfoChanged(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        if (taskInfo.configuration == null || taskListener == null) {
            removeLayouts(taskInfo.taskId);
            return;
        }
        createOrUpdateCompatLayout(taskInfo, taskListener);
        createOrUpdateLetterboxEduLayout(taskInfo, taskListener);
    }

    public void onDisplayAdded(int i) {
        addOnInsetsChangedListener(i);
    }

    public void onDisplayRemoved(int i) {
        this.mDisplayContextCache.remove(i);
        removeOnInsetsChangedListener(i);
        ArrayList arrayList = new ArrayList();
        forAllLayoutsOnDisplay(i, new CompatUIController$$ExternalSyntheticLambda3(arrayList));
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            removeLayouts(((Integer) arrayList.get(size)).intValue());
        }
    }

    public final void addOnInsetsChangedListener(int i) {
        PerDisplayOnInsetsChangedListener perDisplayOnInsetsChangedListener = new PerDisplayOnInsetsChangedListener(i);
        perDisplayOnInsetsChangedListener.register();
        this.mOnInsetsChangedListeners.put(i, perDisplayOnInsetsChangedListener);
    }

    public final void removeOnInsetsChangedListener(int i) {
        PerDisplayOnInsetsChangedListener perDisplayOnInsetsChangedListener = this.mOnInsetsChangedListeners.get(i);
        if (perDisplayOnInsetsChangedListener != null) {
            perDisplayOnInsetsChangedListener.unregister();
            this.mOnInsetsChangedListeners.remove(i);
        }
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        updateDisplayLayout(i);
    }

    public final void updateDisplayLayout(int i) {
        forAllLayoutsOnDisplay(i, new CompatUIController$$ExternalSyntheticLambda6(this.mDisplayController.getDisplayLayout(i)));
    }

    public void onImeVisibilityChanged(int i, boolean z) {
        if (z) {
            this.mDisplaysWithIme.add(Integer.valueOf(i));
        } else {
            this.mDisplaysWithIme.remove(Integer.valueOf(i));
        }
        forAllLayoutsOnDisplay(i, new CompatUIController$$ExternalSyntheticLambda4(this, i));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onImeVisibilityChanged$2(int i, CompatUIWindowManagerAbstract compatUIWindowManagerAbstract) {
        compatUIWindowManagerAbstract.updateVisibility(showOnDisplay(i));
    }

    @VisibleForTesting
    public void onKeyguardShowingChanged(boolean z) {
        this.mKeyguardShowing = z;
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onKeyguardShowingChanged$3(CompatUIWindowManagerAbstract compatUIWindowManagerAbstract) {
        compatUIWindowManagerAbstract.updateVisibility(showOnDisplay(compatUIWindowManagerAbstract.getDisplayId()));
    }

    public final boolean showOnDisplay(int i) {
        return !this.mKeyguardShowing && !isImeShowingOnDisplay(i);
    }

    public final boolean isImeShowingOnDisplay(int i) {
        return this.mDisplaysWithIme.contains(Integer.valueOf(i));
    }

    public final void createOrUpdateCompatLayout(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(taskInfo.taskId);
        if (compatUIWindowManager == null) {
            Context orCreateDisplayContext = getOrCreateDisplayContext(taskInfo.displayId);
            if (orCreateDisplayContext != null) {
                CompatUIWindowManager createCompatUiWindowManager = createCompatUiWindowManager(orCreateDisplayContext, taskInfo, taskListener);
                if (createCompatUiWindowManager.createLayout(showOnDisplay(taskInfo.displayId))) {
                    this.mActiveCompatLayouts.put(taskInfo.taskId, createCompatUiWindowManager);
                }
            }
        } else if (!compatUIWindowManager.updateCompatInfo(taskInfo, taskListener, showOnDisplay(compatUIWindowManager.getDisplayId()))) {
            this.mActiveCompatLayouts.remove(taskInfo.taskId);
        }
    }

    @VisibleForTesting
    public CompatUIWindowManager createCompatUiWindowManager(Context context, TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        return new CompatUIWindowManager(context, taskInfo, this.mSyncQueue, this.mCallback, taskListener, this.mDisplayController.getDisplayLayout(taskInfo.displayId), this.mCompatUIHintsState);
    }

    public final void createOrUpdateLetterboxEduLayout(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager == null || letterboxEduWindowManager.getTaskId() != taskInfo.taskId) {
            Context orCreateDisplayContext = getOrCreateDisplayContext(taskInfo.displayId);
            if (orCreateDisplayContext != null) {
                LetterboxEduWindowManager createLetterboxEduWindowManager = createLetterboxEduWindowManager(orCreateDisplayContext, taskInfo, taskListener);
                if (createLetterboxEduWindowManager.createLayout(showOnDisplay(taskInfo.displayId))) {
                    LetterboxEduWindowManager letterboxEduWindowManager2 = this.mActiveLetterboxEduLayout;
                    if (letterboxEduWindowManager2 != null) {
                        letterboxEduWindowManager2.release();
                    }
                    this.mActiveLetterboxEduLayout = createLetterboxEduWindowManager;
                    return;
                }
                return;
            }
            return;
        }
        LetterboxEduWindowManager letterboxEduWindowManager3 = this.mActiveLetterboxEduLayout;
        if (!letterboxEduWindowManager3.updateCompatInfo(taskInfo, taskListener, showOnDisplay(letterboxEduWindowManager3.getDisplayId()))) {
            this.mActiveLetterboxEduLayout = null;
        }
    }

    @VisibleForTesting
    public LetterboxEduWindowManager createLetterboxEduWindowManager(Context context, TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener) {
        return new LetterboxEduWindowManager(context, taskInfo, this.mSyncQueue, taskListener, this.mDisplayController.getDisplayLayout(taskInfo.displayId), this.mTransitionsLazy.get(), new CompatUIController$$ExternalSyntheticLambda0(this));
    }

    public final void onLetterboxEduDismissed() {
        this.mActiveLetterboxEduLayout = null;
    }

    public final void removeLayouts(int i) {
        CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(i);
        if (compatUIWindowManager != null) {
            compatUIWindowManager.release();
            this.mActiveCompatLayouts.remove(i);
        }
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager != null && letterboxEduWindowManager.getTaskId() == i) {
            this.mActiveLetterboxEduLayout.release();
            this.mActiveLetterboxEduLayout = null;
        }
    }

    public final Context getOrCreateDisplayContext(int i) {
        if (i == 0) {
            return this.mContext;
        }
        Context context = null;
        WeakReference weakReference = this.mDisplayContextCache.get(i);
        if (weakReference != null) {
            context = (Context) weakReference.get();
        }
        if (context != null) {
            return context;
        }
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
        if (display != null) {
            Context createDisplayContext = this.mContext.createDisplayContext(display);
            this.mDisplayContextCache.put(i, new WeakReference(createDisplayContext));
            return createDisplayContext;
        }
        Log.e("CompatUIController", "Cannot get context for display " + i);
        return context;
    }

    public static /* synthetic */ boolean lambda$forAllLayoutsOnDisplay$4(int i, CompatUIWindowManagerAbstract compatUIWindowManagerAbstract) {
        return compatUIWindowManagerAbstract.getDisplayId() == i;
    }

    public final void forAllLayoutsOnDisplay(int i, Consumer<CompatUIWindowManagerAbstract> consumer) {
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda5(i), consumer);
    }

    public final void forAllLayouts(Consumer<CompatUIWindowManagerAbstract> consumer) {
        forAllLayouts(new CompatUIController$$ExternalSyntheticLambda2(), consumer);
    }

    public final void forAllLayouts(Predicate<CompatUIWindowManagerAbstract> predicate, Consumer<CompatUIWindowManagerAbstract> consumer) {
        for (int i = 0; i < this.mActiveCompatLayouts.size(); i++) {
            CompatUIWindowManager compatUIWindowManager = this.mActiveCompatLayouts.get(this.mActiveCompatLayouts.keyAt(i));
            if (compatUIWindowManager != null && predicate.test(compatUIWindowManager)) {
                consumer.accept(compatUIWindowManager);
            }
        }
        LetterboxEduWindowManager letterboxEduWindowManager = this.mActiveLetterboxEduLayout;
        if (letterboxEduWindowManager != null && predicate.test(letterboxEduWindowManager)) {
            consumer.accept(this.mActiveLetterboxEduLayout);
        }
    }

    public class CompatUIImpl implements CompatUI {
        public CompatUIImpl() {
        }

        public void onKeyguardShowingChanged(boolean z) {
            CompatUIController.this.mMainExecutor.execute(new CompatUIController$CompatUIImpl$$ExternalSyntheticLambda0(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardShowingChanged$0(boolean z) {
            CompatUIController.this.onKeyguardShowingChanged(z);
        }
    }

    public class PerDisplayOnInsetsChangedListener implements DisplayInsetsController.OnInsetsChangedListener {
        public final int mDisplayId;
        public final InsetsState mInsetsState = new InsetsState();

        public PerDisplayOnInsetsChangedListener(int i) {
            this.mDisplayId = i;
        }

        public void register() {
            CompatUIController.this.mDisplayInsetsController.addInsetsChangedListener(this.mDisplayId, this);
        }

        public void unregister() {
            CompatUIController.this.mDisplayInsetsController.removeInsetsChangedListener(this.mDisplayId, this);
        }

        public void insetsChanged(InsetsState insetsState) {
            if (!this.mInsetsState.equals(insetsState)) {
                this.mInsetsState.set(insetsState);
                CompatUIController.this.updateDisplayLayout(this.mDisplayId);
            }
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            insetsChanged(insetsState);
        }
    }
}
