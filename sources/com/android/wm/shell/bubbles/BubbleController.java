package com.android.wm.shell.bubbles;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseSetArray;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import kotlin.Unit;

public class BubbleController {
    public boolean mAddedToWindowManager = false;
    public final ShellExecutor mBackgroundExecutor;
    public final IStatusBarService mBarService;
    public final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (BubbleController.this.isStackExpanded()) {
                String action = intent.getAction();
                String stringExtra = intent.getStringExtra("reason");
                if (("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) && "gestureNav".equals(stringExtra)) || "android.intent.action.SCREEN_OFF".equals(action)) {
                    BubbleController.this.mMainExecutor.execute(new BubbleController$5$$ExternalSyntheticLambda0(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            BubbleController.this.collapseStack();
        }
    };
    public BubbleBadgeIconFactory mBubbleBadgeIconFactory;
    public BubbleData mBubbleData;
    public final BubbleData.Listener mBubbleDataListener = new BubbleData.Listener() {
        public void applyUpdate(BubbleData.Update update) {
            BubbleController.this.ensureStackViewCreated();
            BubbleController.this.loadOverflowBubblesFromDisk();
            BubbleController.this.mStackView.updateOverflowButtonDot();
            if (BubbleController.this.mOverflowListener != null) {
                BubbleController.this.mOverflowListener.applyUpdate(update);
            }
            ArrayList arrayList = new ArrayList(update.removedBubbles);
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                Bubble bubble = (Bubble) pair.first;
                int intValue = ((Integer) pair.second).intValue();
                if (BubbleController.this.mStackView != null) {
                    BubbleController.this.mStackView.removeBubble(bubble);
                }
                if (!(intValue == 8 || intValue == 14)) {
                    if (intValue == 5 || intValue == 12) {
                        arrayList2.add(bubble);
                    }
                    if (!BubbleController.this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                        if (BubbleController.this.mBubbleData.hasOverflowBubbleWithKey(bubble.getKey()) || !(!bubble.showInShade() || intValue == 5 || intValue == 9)) {
                            if (bubble.isBubble()) {
                                BubbleController.this.setIsBubble(bubble, false);
                            }
                            BubbleController.this.mSysuiProxy.updateNotificationBubbleButton(bubble.getKey());
                        } else {
                            BubbleController.this.mSysuiProxy.notifyRemoveNotification(bubble.getKey(), 2);
                        }
                    }
                    BubbleController.this.mSysuiProxy.getPendingOrActiveEntry(bubble.getKey(), new BubbleController$6$$ExternalSyntheticLambda0(this, bubble));
                }
            }
            BubbleController.this.mDataRepository.removeBubbles(BubbleController.this.mCurrentUserId, arrayList2);
            if (!(update.addedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mDataRepository.addBubble(BubbleController.this.mCurrentUserId, update.addedBubble);
                BubbleController.this.mStackView.addBubble(update.addedBubble);
            }
            if (!(update.updatedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.updateBubble(update.updatedBubble);
            }
            if (!(update.suppressedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.setBubbleSuppressed(update.suppressedBubble, true);
            }
            if (!(update.unsuppressedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.setBubbleSuppressed(update.unsuppressedBubble, false);
            }
            if (update.orderChanged && BubbleController.this.mStackView != null) {
                BubbleController.this.mDataRepository.addBubbles(BubbleController.this.mCurrentUserId, update.bubbles);
                BubbleController.this.mStackView.updateBubbleOrder(update.bubbles);
            }
            if (update.expandedChanged && !update.expanded) {
                BubbleController.this.mStackView.setExpanded(false);
                BubbleController.this.mSysuiProxy.requestNotificationShadeTopUi(false, "Bubbles");
            }
            if (update.selectionChanged && BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.setSelectedBubble(update.selectedBubble);
                if (update.selectedBubble != null) {
                    BubbleController.this.mSysuiProxy.updateNotificationSuppression(update.selectedBubble.getKey());
                }
            }
            if (update.expandedChanged && update.expanded && BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.setExpanded(true);
                BubbleController.this.mSysuiProxy.requestNotificationShadeTopUi(true, "Bubbles");
            }
            BubbleController.this.mSysuiProxy.notifyInvalidateNotifications("BubbleData.Listener.applyUpdate");
            BubbleController.this.updateStack();
            BubbleController.this.mImpl.mCachedState.update(update);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$applyUpdate$1(Bubble bubble, BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$6$$ExternalSyntheticLambda1(this, bubbleEntry, bubble));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$applyUpdate$0(BubbleEntry bubbleEntry, Bubble bubble) {
            if (bubbleEntry != null) {
                if (BubbleController.this.getBubblesInGroup(bubbleEntry.getStatusBarNotification().getGroupKey()).isEmpty()) {
                    BubbleController.this.mSysuiProxy.notifyMaybeCancelSummary(bubble.getKey());
                }
            }
        }
    };
    public BubbleIconFactory mBubbleIconFactory;
    public BubblePositioner mBubblePositioner;
    public final Context mContext;
    public SparseArray<UserInfo> mCurrentProfiles;
    public int mCurrentUserId;
    public final BubbleDataRepository mDataRepository;
    public int mDensityDpi = 0;
    public final DisplayController mDisplayController;
    public DragAndDropController mDragAndDropController;
    public Bubbles.BubbleExpandListener mExpandListener;
    public final FloatingContentCoordinator mFloatingContentCoordinator;
    public float mFontScale = 0.0f;
    public final BubblesImpl mImpl = new BubblesImpl();
    public boolean mInflateSynchronously;
    public boolean mIsStatusBarShade = true;
    public final LauncherApps mLauncherApps;
    public int mLayoutDirection = -1;
    public BubbleLogger mLogger;
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public BubbleEntry mNotifEntryToExpandOnShadeUnlock;
    public Optional<OneHandedController> mOneHandedOptional;
    public boolean mOverflowDataLoadNeeded = true;
    public BubbleData.Listener mOverflowListener = null;
    public final SparseSetArray<String> mSavedBubbleKeysPerUser;
    public Rect mScreenBounds = new Rect();
    public BubbleStackView mStackView;
    public BubbleStackView.SurfaceSynchronizer mSurfaceSynchronizer;
    public final SyncTransactionQueue mSyncQueue;
    public Bubbles.SysuiProxy mSysuiProxy;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TaskStackListenerImpl mTaskStackListener;
    public final TaskViewTransitions mTaskViewTransitions;
    public NotificationListenerService.Ranking mTmpRanking;
    public final UserManager mUserManager;
    public WindowInsets mWindowInsets;
    public final WindowManager mWindowManager;
    public final WindowManagerShellWrapper mWindowManagerShellWrapper;
    public WindowManager.LayoutParams mWmLayoutParams;

    public static BubbleController create(Context context, BubbleStackView.SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, UserManager userManager, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, Optional<OneHandedController> optional, DragAndDropController dragAndDropController, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        Context context2 = context;
        ShellExecutor shellExecutor3 = shellExecutor;
        BubbleLogger bubbleLogger = r2;
        BubbleLogger bubbleLogger2 = new BubbleLogger(uiEventLogger);
        BubblePositioner bubblePositioner = new BubblePositioner(context2, windowManager);
        BubbleLogger bubbleLogger3 = bubbleLogger2;
        BubbleData bubbleData = r2;
        BubbleData bubbleData2 = r2;
        BubbleData bubbleData3 = new BubbleData(context2, bubbleLogger3, bubblePositioner, shellExecutor3);
        Context context3 = context2;
        BubbleDataRepository bubbleDataRepository = r2;
        BubbleDataRepository bubbleDataRepository2 = new BubbleDataRepository(context3, launcherApps, shellExecutor3);
        return new BubbleController(context, bubbleData2, surfaceSynchronizer, floatingContentCoordinator, bubbleDataRepository, iStatusBarService, windowManager, windowManagerShellWrapper, userManager, launcherApps, bubbleLogger, taskStackListenerImpl, shellTaskOrganizer, bubblePositioner, displayController, optional, dragAndDropController, shellExecutor, handler, shellExecutor2, taskViewTransitions, syncTransactionQueue);
    }

    @VisibleForTesting
    public BubbleController(Context context, BubbleData bubbleData, BubbleStackView.SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, BubbleDataRepository bubbleDataRepository, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, UserManager userManager, LauncherApps launcherApps, BubbleLogger bubbleLogger, TaskStackListenerImpl taskStackListenerImpl, ShellTaskOrganizer shellTaskOrganizer, BubblePositioner bubblePositioner, DisplayController displayController, Optional<OneHandedController> optional, DragAndDropController dragAndDropController, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        this.mContext = context;
        this.mLauncherApps = launcherApps;
        this.mBarService = iStatusBarService == null ? IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar")) : iStatusBarService;
        this.mWindowManager = windowManager;
        this.mWindowManagerShellWrapper = windowManagerShellWrapper;
        this.mUserManager = userManager;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        this.mDataRepository = bubbleDataRepository;
        this.mLogger = bubbleLogger;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mBackgroundExecutor = shellExecutor2;
        this.mTaskStackListener = taskStackListenerImpl;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSurfaceSynchronizer = surfaceSynchronizer;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mBubblePositioner = bubblePositioner;
        this.mBubbleData = bubbleData;
        this.mSavedBubbleKeysPerUser = new SparseSetArray<>();
        this.mBubbleIconFactory = new BubbleIconFactory(context);
        this.mBubbleBadgeIconFactory = new BubbleBadgeIconFactory(context);
        this.mDisplayController = displayController;
        this.mTaskViewTransitions = taskViewTransitions;
        this.mOneHandedOptional = optional;
        this.mDragAndDropController = dragAndDropController;
        this.mSyncQueue = syncTransactionQueue;
    }

    public final void registerOneHandedState(OneHandedController oneHandedController) {
        oneHandedController.registerTransitionCallback(new OneHandedTransitionCallback() {
            public void onStartFinished(Rect rect) {
                if (BubbleController.this.mStackView != null) {
                    BubbleController.this.mStackView.onVerticalOffsetChanged(rect.top);
                }
            }

            public void onStopFinished(Rect rect) {
                if (BubbleController.this.mStackView != null) {
                    BubbleController.this.mStackView.onVerticalOffsetChanged(rect.top);
                }
            }
        });
    }

    public void initialize() {
        this.mBubbleData.setListener(this.mBubbleDataListener);
        this.mBubbleData.setSuppressionChangedListener(new BubbleController$$ExternalSyntheticLambda11(this));
        this.mBubbleData.setPendingIntentCancelledListener(new BubbleController$$ExternalSyntheticLambda12(this));
        try {
            this.mWindowManagerShellWrapper.addPinnedStackListener(new BubblesImeListener());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mBubbleData.setCurrentUserId(this.mCurrentUserId);
        this.mTaskOrganizer.addLocusIdListener(new BubbleController$$ExternalSyntheticLambda13(this));
        this.mLauncherApps.registerCallback(new LauncherApps.Callback() {
            public void onPackageAdded(String str, UserHandle userHandle) {
            }

            public void onPackageChanged(String str, UserHandle userHandle) {
            }

            public void onPackagesAvailable(String[] strArr, UserHandle userHandle, boolean z) {
            }

            public void onPackageRemoved(String str, UserHandle userHandle) {
                BubbleController.this.mBubbleData.removeBubblesWithPackageName(str, 13);
            }

            public void onPackagesUnavailable(String[] strArr, UserHandle userHandle, boolean z) {
                for (String removeBubblesWithPackageName : strArr) {
                    BubbleController.this.mBubbleData.removeBubblesWithPackageName(removeBubblesWithPackageName, 13);
                }
            }

            public void onShortcutsChanged(String str, List<ShortcutInfo> list, UserHandle userHandle) {
                super.onShortcutsChanged(str, list, userHandle);
                BubbleController.this.mBubbleData.removeBubblesWithInvalidShortcuts(str, list, 12);
            }
        }, this.mMainHandler);
        this.mTaskStackListener.addListener(new TaskStackListenerCallback() {
            public void onTaskMovedToFront(int i) {
                BubbleController.this.mMainExecutor.execute(new BubbleController$3$$ExternalSyntheticLambda0(this, i));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onTaskMovedToFront$0(int i) {
                int taskId = (BubbleController.this.mStackView == null || BubbleController.this.mStackView.getExpandedBubble() == null || !BubbleController.this.isStackExpanded() || BubbleController.this.mStackView.isExpansionAnimating() || BubbleController.this.mStackView.isSwitchAnimating()) ? -1 : BubbleController.this.mStackView.getExpandedBubble().getTaskId();
                if (taskId != -1 && taskId != i) {
                    BubbleController.this.mBubbleData.setExpanded(false);
                }
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                for (Bubble next : BubbleController.this.mBubbleData.getBubbles()) {
                    if (runningTaskInfo.taskId == next.getTaskId()) {
                        BubbleController.this.mBubbleData.setSelectedBubble(next);
                        BubbleController.this.mBubbleData.setExpanded(true);
                        return;
                    }
                }
                for (Bubble next2 : BubbleController.this.mBubbleData.getOverflowBubbles()) {
                    if (runningTaskInfo.taskId == next2.getTaskId()) {
                        BubbleController.this.promoteBubbleFromOverflow(next2);
                        BubbleController.this.mBubbleData.setExpanded(true);
                        return;
                    }
                }
            }
        });
        this.mDisplayController.addDisplayChangingController(new DisplayChangeController.OnDisplayChangingListener() {
            public void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
                if (i2 != i3 && BubbleController.this.mStackView != null) {
                    BubbleController.this.mStackView.onOrientationChanged();
                }
            }
        });
        this.mOneHandedOptional.ifPresent(new BubbleController$$ExternalSyntheticLambda14(this));
        this.mDragAndDropController.addListener(new BubbleController$$ExternalSyntheticLambda15(this));
        this.mDataRepository.sanitizeBubbles(this.mUserManager.getAliveUsers());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$1(Bubble bubble) {
        if (bubble.getBubbleIntent() != null) {
            if (bubble.isIntentActive() || this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                bubble.setPendingIntentCanceled();
            } else {
                this.mMainExecutor.execute(new BubbleController$$ExternalSyntheticLambda16(this, bubble));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$0(Bubble bubble) {
        removeBubble(bubble.getKey(), 10);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$2(int i, LocusId locusId, boolean z) {
        this.mBubbleData.onLocusVisibilityChanged(i, locusId, z);
    }

    @VisibleForTesting
    public Bubbles asBubbles() {
        return this.mImpl;
    }

    @VisibleForTesting
    public BubblesImpl.CachedState getImplCachedState() {
        return this.mImpl.mCachedState;
    }

    public ShellExecutor getMainExecutor() {
        return this.mMainExecutor;
    }

    public void hideCurrentInputMethod() {
        try {
            this.mBarService.hideCurrentInputMethodForBubbles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public final void onStatusBarVisibilityChanged(boolean z) {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setTemporarilyInvisible(!z && !isStackExpanded());
        }
    }

    public final void onZenStateChanged() {
        for (Bubble next : this.mBubbleData.getBubbles()) {
            next.setShowDot(next.showInShade());
        }
    }

    @VisibleForTesting
    public void onStatusBarStateChanged(boolean z) {
        this.mIsStatusBarShade = z;
        if (!z) {
            collapseStack();
        }
        BubbleEntry bubbleEntry = this.mNotifEntryToExpandOnShadeUnlock;
        if (bubbleEntry != null) {
            expandStackAndSelectBubble(bubbleEntry);
            this.mNotifEntryToExpandOnShadeUnlock = null;
        }
        updateStack();
    }

    @VisibleForTesting
    public void onBubbleMetadataFlagChanged(Bubble bubble) {
        try {
            this.mBarService.onBubbleMetadataFlagChanged(bubble.getKey(), bubble.getFlags());
        } catch (RemoteException unused) {
        }
        this.mImpl.mCachedState.updateBubbleSuppressedState(bubble);
    }

    @VisibleForTesting
    public void onUserChanged(int i) {
        saveBubbles(this.mCurrentUserId);
        this.mCurrentUserId = i;
        this.mBubbleData.dismissAll(8);
        this.mBubbleData.clearOverflow();
        this.mOverflowDataLoadNeeded = true;
        restoreBubbles(i);
        this.mBubbleData.setCurrentUserId(i);
    }

    public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        this.mCurrentProfiles = sparseArray;
    }

    public void onUserRemoved(int i) {
        UserInfo profileParent = this.mUserManager.getProfileParent(i);
        int identifier = profileParent != null ? profileParent.getUserHandle().getIdentifier() : -1;
        this.mBubbleData.removeBubblesForUser(i);
        this.mDataRepository.removeBubblesForUser(i, identifier);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r1 = r1.mCurrentProfiles;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isCurrentProfile(int r2) {
        /*
            r1 = this;
            r0 = -1
            if (r2 == r0) goto L_0x0010
            android.util.SparseArray<android.content.pm.UserInfo> r1 = r1.mCurrentProfiles
            if (r1 == 0) goto L_0x000e
            java.lang.Object r1 = r1.get(r2)
            if (r1 == 0) goto L_0x000e
            goto L_0x0010
        L_0x000e:
            r1 = 0
            goto L_0x0011
        L_0x0010:
            r1 = 1
        L_0x0011:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.isCurrentProfile(int):boolean");
    }

    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    public void setOverflowListener(BubbleData.Listener listener) {
        this.mOverflowListener = listener;
    }

    public List<Bubble> getOverflowBubbles() {
        return this.mBubbleData.getOverflowBubbles();
    }

    public ShellTaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    public SyncTransactionQueue getSyncTransactionQueue() {
        return this.mSyncQueue;
    }

    public TaskViewTransitions getTaskViewTransitions() {
        return this.mTaskViewTransitions;
    }

    @VisibleForTesting
    public BubblePositioner getPositioner() {
        return this.mBubblePositioner;
    }

    public Bubbles.SysuiProxy getSysuiProxy() {
        return this.mSysuiProxy;
    }

    public final void ensureStackViewCreated() {
        if (this.mStackView == null) {
            BubbleStackView bubbleStackView = new BubbleStackView(this.mContext, this, this.mBubbleData, this.mSurfaceSynchronizer, this.mFloatingContentCoordinator, this.mMainExecutor);
            this.mStackView = bubbleStackView;
            bubbleStackView.onOrientationChanged();
            Bubbles.BubbleExpandListener bubbleExpandListener = this.mExpandListener;
            if (bubbleExpandListener != null) {
                this.mStackView.setExpandListener(bubbleExpandListener);
            }
            BubbleStackView bubbleStackView2 = this.mStackView;
            Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
            Objects.requireNonNull(sysuiProxy);
            bubbleStackView2.setUnbubbleConversationCallback(new BubbleController$$ExternalSyntheticLambda2(sysuiProxy));
        }
        addToWindowManagerMaybe();
    }

    public final void addToWindowManagerMaybe() {
        if (this.mStackView != null && !this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 16777256, -3);
            this.mWmLayoutParams = layoutParams;
            layoutParams.setTrustedOverlay();
            this.mWmLayoutParams.setFitInsetsTypes(0);
            WindowManager.LayoutParams layoutParams2 = this.mWmLayoutParams;
            layoutParams2.softInputMode = 16;
            layoutParams2.token = new Binder();
            this.mWmLayoutParams.setTitle("Bubbles!");
            this.mWmLayoutParams.packageName = this.mContext.getPackageName();
            WindowManager.LayoutParams layoutParams3 = this.mWmLayoutParams;
            layoutParams3.layoutInDisplayCutoutMode = 3;
            layoutParams3.privateFlags = 16 | layoutParams3.privateFlags;
            try {
                this.mAddedToWindowManager = true;
                registerBroadcastReceiver();
                this.mBubbleData.getOverflow().initialize(this);
                this.mWindowManager.addView(this.mStackView, this.mWmLayoutParams);
                this.mStackView.setOnApplyWindowInsetsListener(new BubbleController$$ExternalSyntheticLambda5(this));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$addToWindowManagerMaybe$3(View view, WindowInsets windowInsets) {
        if (!windowInsets.equals(this.mWindowInsets)) {
            this.mWindowInsets = windowInsets;
            this.mBubblePositioner.update();
            this.mStackView.onDisplaySizeChanged();
        }
        return windowInsets;
    }

    public void updateWindowFlagsForBackpress(boolean z) {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null && this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = this.mWmLayoutParams;
            layoutParams.flags = (z ? 0 : 40) | 16777216;
            this.mWindowManager.updateViewLayout(bubbleStackView, layoutParams);
        }
    }

    public final void removeFromWindowManagerMaybe() {
        if (this.mAddedToWindowManager) {
            try {
                this.mAddedToWindowManager = false;
                this.mBackgroundExecutor.execute(new BubbleController$$ExternalSyntheticLambda4(this));
                BubbleStackView bubbleStackView = this.mStackView;
                if (bubbleStackView != null) {
                    this.mWindowManager.removeView(bubbleStackView);
                    this.mBubbleData.getOverflow().cleanUpExpandedState();
                    return;
                }
                Log.w("Bubbles", "StackView added to WindowManager, but was null when removing!");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeFromWindowManagerMaybe$4() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public final void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    @VisibleForTesting
    public void onAllBubblesAnimatedOut() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setVisibility(4);
            removeFromWindowManagerMaybe();
        }
    }

    public final void saveBubbles(int i) {
        this.mSavedBubbleKeysPerUser.remove(i);
        for (Bubble key : this.mBubbleData.getBubbles()) {
            this.mSavedBubbleKeysPerUser.add(i, key.getKey());
        }
    }

    public final void restoreBubbles(int i) {
        ArraySet arraySet = this.mSavedBubbleKeysPerUser.get(i);
        if (arraySet != null) {
            this.mSysuiProxy.getShouldRestoredEntries(arraySet, new BubbleController$$ExternalSyntheticLambda3(this));
            this.mSavedBubbleKeysPerUser.remove(i);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreBubbles$6(List list) {
        this.mMainExecutor.execute(new BubbleController$$ExternalSyntheticLambda7(this, list));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreBubbles$5(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            BubbleEntry bubbleEntry = (BubbleEntry) it.next();
            if (canLaunchInTaskView(this.mContext, bubbleEntry)) {
                updateBubble(bubbleEntry, true, false);
            }
        }
    }

    public final void updateForThemeChanges() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.onThemeChanged();
        }
        this.mBubbleIconFactory = new BubbleIconFactory(this.mContext);
        this.mBubbleBadgeIconFactory = new BubbleBadgeIconFactory(this.mContext);
        for (Bubble inflate : this.mBubbleData.getBubbles()) {
            inflate.inflate((BubbleViewInfoTask.Callback) null, this.mContext, this, this.mStackView, this.mBubbleIconFactory, this.mBubbleBadgeIconFactory, false);
        }
        for (Bubble inflate2 : this.mBubbleData.getOverflowBubbles()) {
            inflate2.inflate((BubbleViewInfoTask.Callback) null, this.mContext, this, this.mStackView, this.mBubbleIconFactory, this.mBubbleBadgeIconFactory, false);
        }
    }

    public final void onConfigChanged(Configuration configuration) {
        BubblePositioner bubblePositioner = this.mBubblePositioner;
        if (bubblePositioner != null) {
            bubblePositioner.update();
        }
        if (this.mStackView != null && configuration != null) {
            if (configuration.densityDpi != this.mDensityDpi || !configuration.windowConfiguration.getBounds().equals(this.mScreenBounds)) {
                this.mDensityDpi = configuration.densityDpi;
                this.mScreenBounds.set(configuration.windowConfiguration.getBounds());
                this.mBubbleData.onMaxBubblesChanged();
                this.mBubbleIconFactory = new BubbleIconFactory(this.mContext);
                this.mBubbleBadgeIconFactory = new BubbleBadgeIconFactory(this.mContext);
                this.mStackView.onDisplaySizeChanged();
            }
            float f = configuration.fontScale;
            if (f != this.mFontScale) {
                this.mFontScale = f;
                this.mStackView.updateFontScale();
            }
            if (configuration.getLayoutDirection() != this.mLayoutDirection) {
                int layoutDirection = configuration.getLayoutDirection();
                this.mLayoutDirection = layoutDirection;
                this.mStackView.onLayoutDirectionChanged(layoutDirection);
            }
        }
    }

    public final void setSysuiProxy(Bubbles.SysuiProxy sysuiProxy) {
        this.mSysuiProxy = sysuiProxy;
    }

    @VisibleForTesting
    public void setExpandListener(Bubbles.BubbleExpandListener bubbleExpandListener) {
        BubbleController$$ExternalSyntheticLambda1 bubbleController$$ExternalSyntheticLambda1 = new BubbleController$$ExternalSyntheticLambda1(bubbleExpandListener);
        this.mExpandListener = bubbleController$$ExternalSyntheticLambda1;
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setExpandListener(bubbleController$$ExternalSyntheticLambda1);
        }
    }

    public static /* synthetic */ void lambda$setExpandListener$7(Bubbles.BubbleExpandListener bubbleExpandListener, boolean z, String str) {
        if (bubbleExpandListener != null) {
            bubbleExpandListener.onBubbleExpandChanged(z, str);
        }
    }

    @VisibleForTesting
    public boolean hasBubbles() {
        if (this.mStackView == null) {
            return false;
        }
        if (this.mBubbleData.hasBubbles() || this.mBubbleData.isShowingOverflow()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean isStackExpanded() {
        return this.mBubbleData.isExpanded();
    }

    public void collapseStack() {
        this.mBubbleData.setExpanded(false);
    }

    @VisibleForTesting
    public boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
        boolean z = this.mBubbleData.hasAnyBubbleWithKey(str) && !this.mBubbleData.getAnyBubbleWithkey(str).showInShade();
        boolean isSummarySuppressed = this.mBubbleData.isSummarySuppressed(str2);
        if ((!str.equals(this.mBubbleData.getSummaryKey(str2)) || !isSummarySuppressed) && !z) {
            return false;
        }
        return true;
    }

    public final void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer) {
        if (this.mBubbleData.isSummarySuppressed(str)) {
            this.mBubbleData.removeSuppressedSummary(str);
            if (consumer != null) {
                consumer.accept(this.mBubbleData.getSummaryKey(str));
            }
        }
    }

    public void promoteBubbleFromOverflow(Bubble bubble) {
        this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_BACK_TO_STACK);
        bubble.setInflateSynchronously(this.mInflateSynchronously);
        bubble.setShouldAutoExpand(true);
        bubble.markAsAccessedAt(System.currentTimeMillis());
        setIsBubble(bubble, true);
    }

    public void expandStackAndSelectBubble(Bubble bubble) {
        if (bubble != null) {
            if (this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                this.mBubbleData.setSelectedBubble(bubble);
                this.mBubbleData.setExpanded(true);
            } else if (this.mBubbleData.hasOverflowBubbleWithKey(bubble.getKey())) {
                promoteBubbleFromOverflow(bubble);
            }
        }
    }

    public void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
        if (this.mIsStatusBarShade) {
            this.mNotifEntryToExpandOnShadeUnlock = null;
            String key = bubbleEntry.getKey();
            Bubble bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(key);
            if (bubbleInStackWithKey != null) {
                this.mBubbleData.setSelectedBubble(bubbleInStackWithKey);
                this.mBubbleData.setExpanded(true);
                return;
            }
            Bubble overflowBubbleWithKey = this.mBubbleData.getOverflowBubbleWithKey(key);
            if (overflowBubbleWithKey != null) {
                promoteBubbleFromOverflow(overflowBubbleWithKey);
            } else if (bubbleEntry.canBubble()) {
                setIsBubble(bubbleEntry, true, true);
            }
        } else {
            this.mNotifEntryToExpandOnShadeUnlock = bubbleEntry;
        }
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry) {
        updateBubble(bubbleEntry, false, true);
    }

    public void loadOverflowBubblesFromDisk() {
        if (this.mOverflowDataLoadNeeded) {
            this.mOverflowDataLoadNeeded = false;
            this.mDataRepository.loadBubbles(this.mCurrentUserId, new BubbleController$$ExternalSyntheticLambda9(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$loadOverflowBubblesFromDisk$10(List list) {
        list.forEach(new BubbleController$$ExternalSyntheticLambda10(this));
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOverflowBubblesFromDisk$9(Bubble bubble) {
        if (!this.mBubbleData.hasAnyBubbleWithKey(bubble.getKey())) {
            bubble.inflate(new BubbleController$$ExternalSyntheticLambda17(this, bubble), this.mContext, this, this.mStackView, this.mBubbleIconFactory, this.mBubbleBadgeIconFactory, true);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOverflowBubblesFromDisk$8(Bubble bubble, Bubble bubble2) {
        this.mBubbleData.overflowBubble(15, bubble);
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry, boolean z, boolean z2) {
        this.mSysuiProxy.setNotificationInterruption(bubbleEntry.getKey());
        if (!bubbleEntry.getRanking().isTextChanged() && bubbleEntry.getBubbleMetadata() != null && !bubbleEntry.getBubbleMetadata().getAutoExpandBubble() && this.mBubbleData.hasOverflowBubbleWithKey(bubbleEntry.getKey())) {
            this.mBubbleData.getOverflowBubbleWithKey(bubbleEntry.getKey()).setEntry(bubbleEntry);
        } else if (this.mBubbleData.isSuppressedWithLocusId(bubbleEntry.getLocusId())) {
            Bubble suppressedBubbleWithKey = this.mBubbleData.getSuppressedBubbleWithKey(bubbleEntry.getKey());
            if (suppressedBubbleWithKey != null) {
                suppressedBubbleWithKey.setEntry(bubbleEntry);
            }
        } else {
            Bubble orCreateBubble = this.mBubbleData.getOrCreateBubble(bubbleEntry, (Bubble) null);
            if (!bubbleEntry.shouldSuppressNotificationList()) {
                inflateAndAdd(orCreateBubble, z, z2);
            } else if (orCreateBubble.shouldAutoExpand()) {
                orCreateBubble.setShouldAutoExpand(false);
            }
        }
    }

    public void inflateAndAdd(Bubble bubble, boolean z, boolean z2) {
        ensureStackViewCreated();
        bubble.setInflateSynchronously(this.mInflateSynchronously);
        bubble.inflate(new BubbleController$$ExternalSyntheticLambda0(this, z, z2), this.mContext, this, this.mStackView, this.mBubbleIconFactory, this.mBubbleBadgeIconFactory, false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateAndAdd$11(boolean z, boolean z2, Bubble bubble) {
        this.mBubbleData.notificationEntryUpdated(bubble, z, z2);
    }

    @VisibleForTesting
    public void removeBubble(String str, int i) {
        if (this.mBubbleData.hasAnyBubbleWithKey(str)) {
            this.mBubbleData.dismissBubbleWithKey(str, i);
        }
    }

    public final void onEntryAdded(BubbleEntry bubbleEntry) {
        if (canLaunchInTaskView(this.mContext, bubbleEntry)) {
            updateBubble(bubbleEntry);
        }
    }

    @VisibleForTesting
    public void onEntryUpdated(BubbleEntry bubbleEntry, boolean z) {
        boolean z2 = z && canLaunchInTaskView(this.mContext, bubbleEntry);
        if (!z2 && this.mBubbleData.hasAnyBubbleWithKey(bubbleEntry.getKey())) {
            removeBubble(bubbleEntry.getKey(), 7);
        } else if (z2 && bubbleEntry.isBubble()) {
            updateBubble(bubbleEntry);
        }
    }

    public final void onEntryRemoved(BubbleEntry bubbleEntry) {
        if (isSummaryOfBubbles(bubbleEntry)) {
            String groupKey = bubbleEntry.getStatusBarNotification().getGroupKey();
            this.mBubbleData.removeSuppressedSummary(groupKey);
            ArrayList<Bubble> bubblesInGroup = getBubblesInGroup(groupKey);
            for (int i = 0; i < bubblesInGroup.size(); i++) {
                removeBubble(bubblesInGroup.get(i).getKey(), 9);
            }
            return;
        }
        removeBubble(bubbleEntry.getKey(), 5);
    }

    @VisibleForTesting
    public void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap) {
        if (this.mTmpRanking == null) {
            this.mTmpRanking = new NotificationListenerService.Ranking();
        }
        String[] orderedKeys = rankingMap.getOrderedKeys();
        int i = 0;
        while (i < orderedKeys.length) {
            String str = orderedKeys[i];
            Pair pair = hashMap.get(str);
            BubbleEntry bubbleEntry = (BubbleEntry) pair.first;
            boolean booleanValue = ((Boolean) pair.second).booleanValue();
            if (bubbleEntry == null || isCurrentProfile(bubbleEntry.getStatusBarNotification().getUser().getIdentifier())) {
                if (bubbleEntry != null && (bubbleEntry.shouldSuppressNotificationList() || bubbleEntry.getRanking().isSuspended())) {
                    booleanValue = false;
                }
                rankingMap.getRanking(str, this.mTmpRanking);
                boolean hasAnyBubbleWithKey = this.mBubbleData.hasAnyBubbleWithKey(str);
                boolean hasBubbleInStackWithKey = this.mBubbleData.hasBubbleInStackWithKey(str);
                if (hasAnyBubbleWithKey && !this.mTmpRanking.canBubble()) {
                    this.mBubbleData.dismissBubbleWithKey(str, 4);
                } else if (hasAnyBubbleWithKey && !booleanValue) {
                    this.mBubbleData.dismissBubbleWithKey(str, 14);
                } else if (bubbleEntry != null && this.mTmpRanking.isBubble() && !hasBubbleInStackWithKey) {
                    bubbleEntry.setFlagBubble(true);
                    onEntryUpdated(bubbleEntry, booleanValue);
                }
                i++;
            } else {
                return;
            }
        }
    }

    @VisibleForTesting
    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        ArrayList arrayList = new ArrayList(this.mBubbleData.getOverflowBubbles());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Bubble bubble = (Bubble) arrayList.get(i2);
            if (Objects.equals(bubble.getShortcutId(), notificationChannel.getConversationId()) && bubble.getPackageName().equals(str) && bubble.getUser().getIdentifier() == userHandle.getIdentifier() && (!notificationChannel.canBubble() || notificationChannel.isDeleted())) {
                this.mBubbleData.dismissBubbleWithKey(bubble.getKey(), 7);
            }
        }
    }

    public final ArrayList<Bubble> getBubblesInGroup(String str) {
        ArrayList<Bubble> arrayList = new ArrayList<>();
        if (str == null) {
            return arrayList;
        }
        for (Bubble next : this.mBubbleData.getActiveBubbles()) {
            if (next.getGroupKey() != null && str.equals(next.getGroupKey())) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public final void setIsBubble(BubbleEntry bubbleEntry, boolean z, boolean z2) {
        Objects.requireNonNull(bubbleEntry);
        bubbleEntry.setFlagBubble(z);
        int i = 0;
        if (z2) {
            i = 3;
        }
        try {
            this.mBarService.onNotificationBubbleChanged(bubbleEntry.getKey(), z, i);
        } catch (RemoteException unused) {
        }
    }

    public final void setIsBubble(Bubble bubble, boolean z) {
        Objects.requireNonNull(bubble);
        bubble.setIsBubble(z);
        this.mSysuiProxy.getPendingOrActiveEntry(bubble.getKey(), new BubbleController$$ExternalSyntheticLambda6(this, z, bubble));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsBubble$13(boolean z, Bubble bubble, BubbleEntry bubbleEntry) {
        this.mMainExecutor.execute(new BubbleController$$ExternalSyntheticLambda8(this, bubbleEntry, z, bubble));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsBubble$12(BubbleEntry bubbleEntry, boolean z, Bubble bubble) {
        if (bubbleEntry != null) {
            setIsBubble(bubbleEntry, z, bubble.shouldAutoExpand());
        } else if (z) {
            Bubble orCreateBubble = this.mBubbleData.getOrCreateBubble((BubbleEntry) null, bubble);
            inflateAndAdd(orCreateBubble, orCreateBubble.shouldAutoExpand(), !orCreateBubble.shouldAutoExpand());
        }
    }

    public final boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer) {
        if (isSummaryOfBubbles(bubbleEntry)) {
            handleSummaryDismissalInterception(bubbleEntry, list, intConsumer);
        } else {
            Bubble bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(bubbleEntry.getKey());
            if (bubbleInStackWithKey == null || !bubbleEntry.isBubble()) {
                bubbleInStackWithKey = this.mBubbleData.getOverflowBubbleWithKey(bubbleEntry.getKey());
            }
            if (bubbleInStackWithKey == null) {
                return false;
            }
            bubbleInStackWithKey.setSuppressNotification(true);
            bubbleInStackWithKey.setShowDot(false);
        }
        this.mSysuiProxy.notifyInvalidateNotifications("BubbleController.handleDismissalInterception");
        return true;
    }

    public final boolean isSummaryOfBubbles(BubbleEntry bubbleEntry) {
        String groupKey = bubbleEntry.getStatusBarNotification().getGroupKey();
        ArrayList<Bubble> bubblesInGroup = getBubblesInGroup(groupKey);
        boolean z = this.mBubbleData.isSummarySuppressed(groupKey) && this.mBubbleData.getSummaryKey(groupKey).equals(bubbleEntry.getKey());
        boolean isGroupSummary = bubbleEntry.getStatusBarNotification().getNotification().isGroupSummary();
        if ((z || isGroupSummary) && !bubblesInGroup.isEmpty()) {
            return true;
        }
        return false;
    }

    public final void handleSummaryDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                BubbleEntry bubbleEntry2 = list.get(i);
                if (this.mBubbleData.hasAnyBubbleWithKey(bubbleEntry2.getKey())) {
                    Bubble anyBubbleWithkey = this.mBubbleData.getAnyBubbleWithkey(bubbleEntry2.getKey());
                    if (anyBubbleWithkey != null) {
                        this.mSysuiProxy.removeNotificationEntry(anyBubbleWithkey.getKey());
                        anyBubbleWithkey.setSuppressNotification(true);
                        anyBubbleWithkey.setShowDot(false);
                    }
                } else {
                    intConsumer.accept(i);
                }
            }
        }
        intConsumer.accept(-1);
        this.mBubbleData.addSummaryToSuppress(bubbleEntry.getStatusBarNotification().getGroupKey(), bubbleEntry.getKey());
    }

    public void updateStack() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            if (!this.mIsStatusBarShade) {
                bubbleStackView.setVisibility(4);
            } else if (hasBubbles()) {
                this.mStackView.setVisibility(0);
            }
            this.mStackView.updateContentDescription();
            this.mStackView.updateBubblesAcessibillityStates();
        }
    }

    @VisibleForTesting
    public BubbleStackView getStackView() {
        return this.mStackView;
    }

    public final void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("BubbleController state:");
        this.mBubbleData.dump(printWriter, strArr);
        printWriter.println();
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.dump(printWriter, strArr);
        }
        printWriter.println();
    }

    public static boolean canLaunchInTaskView(Context context, BubbleEntry bubbleEntry) {
        PendingIntent intent = bubbleEntry.getBubbleMetadata() != null ? bubbleEntry.getBubbleMetadata().getIntent() : null;
        if (bubbleEntry.getBubbleMetadata() != null && bubbleEntry.getBubbleMetadata().getShortcutId() != null) {
            return true;
        }
        if (intent == null) {
            Log.w("Bubbles", "Unable to create bubble -- no intent: " + bubbleEntry.getKey());
            return false;
        }
        ActivityInfo resolveActivityInfo = intent.getIntent().resolveActivityInfo(getPackageManagerForUser(context, bubbleEntry.getStatusBarNotification().getUser().getIdentifier()), 0);
        if (resolveActivityInfo == null) {
            Log.w("Bubbles", "Unable to send as bubble, " + bubbleEntry.getKey() + " couldn't find activity info for intent: " + intent);
            return false;
        } else if (ActivityInfo.isResizeableMode(resolveActivityInfo.resizeMode)) {
            return true;
        } else {
            Log.w("Bubbles", "Unable to send as bubble, " + bubbleEntry.getKey() + " activity is not resizable for intent: " + intent);
            return false;
        }
    }

    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    public class BubblesImeListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        public BubblesImeListener() {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            BubbleController.this.mBubblePositioner.setImeVisible(z, i);
            if (BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.animateForIme(z);
            }
        }
    }

    public class BubblesImpl implements Bubbles {
        public CachedState mCachedState;

        public BubblesImpl() {
            this.mCachedState = new CachedState();
        }

        @VisibleForTesting
        public class CachedState {
            public boolean mIsStackExpanded;
            public String mSelectedBubbleKey;
            public HashMap<String, Bubble> mShortcutIdToBubble = new HashMap<>();
            public HashSet<String> mSuppressedBubbleKeys = new HashSet<>();
            public HashMap<String, String> mSuppressedGroupToNotifKeys = new HashMap<>();
            public ArrayList<Bubble> mTmpBubbles = new ArrayList<>();

            public CachedState() {
            }

            public synchronized void update(BubbleData.Update update) {
                if (update.selectionChanged) {
                    BubbleViewProvider bubbleViewProvider = update.selectedBubble;
                    this.mSelectedBubbleKey = bubbleViewProvider != null ? bubbleViewProvider.getKey() : null;
                }
                if (update.expandedChanged) {
                    this.mIsStackExpanded = update.expanded;
                }
                if (update.suppressedSummaryChanged) {
                    String summaryKey = BubbleController.this.mBubbleData.getSummaryKey(update.suppressedSummaryGroup);
                    if (summaryKey != null) {
                        this.mSuppressedGroupToNotifKeys.put(update.suppressedSummaryGroup, summaryKey);
                    } else {
                        this.mSuppressedGroupToNotifKeys.remove(update.suppressedSummaryGroup);
                    }
                }
                this.mTmpBubbles.clear();
                this.mTmpBubbles.addAll(update.bubbles);
                this.mTmpBubbles.addAll(update.overflowBubbles);
                this.mSuppressedBubbleKeys.clear();
                this.mShortcutIdToBubble.clear();
                Iterator<Bubble> it = this.mTmpBubbles.iterator();
                while (it.hasNext()) {
                    Bubble next = it.next();
                    this.mShortcutIdToBubble.put(next.getShortcutId(), next);
                    updateBubbleSuppressedState(next);
                }
            }

            public synchronized void updateBubbleSuppressedState(Bubble bubble) {
                if (!bubble.showInShade()) {
                    this.mSuppressedBubbleKeys.add(bubble.getKey());
                } else {
                    this.mSuppressedBubbleKeys.remove(bubble.getKey());
                }
            }

            public synchronized boolean isBubbleExpanded(String str) {
                return this.mIsStackExpanded && str.equals(this.mSelectedBubbleKey);
            }

            public synchronized boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
                return this.mSuppressedBubbleKeys.contains(str) || (this.mSuppressedGroupToNotifKeys.containsKey(str2) && str.equals(this.mSuppressedGroupToNotifKeys.get(str2)));
            }

            public synchronized Bubble getBubbleWithShortcutId(String str) {
                return this.mShortcutIdToBubble.get(str);
            }

            public synchronized void dump(PrintWriter printWriter) {
                printWriter.println("BubbleImpl.CachedState state:");
                printWriter.println("mIsStackExpanded: " + this.mIsStackExpanded);
                printWriter.println("mSelectedBubbleKey: " + this.mSelectedBubbleKey);
                printWriter.print("mSuppressedBubbleKeys: ");
                printWriter.println(this.mSuppressedBubbleKeys.size());
                Iterator<String> it = this.mSuppressedBubbleKeys.iterator();
                while (it.hasNext()) {
                    printWriter.println("   suppressing: " + it.next());
                }
                printWriter.print("mSuppressedGroupToNotifKeys: ");
                printWriter.println(this.mSuppressedGroupToNotifKeys.size());
                for (String str : this.mSuppressedGroupToNotifKeys.keySet()) {
                    printWriter.println("   suppressing: " + str);
                }
            }
        }

        public boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
            return this.mCachedState.isBubbleNotificationSuppressedFromShade(str, str2);
        }

        public boolean isBubbleExpanded(String str) {
            return this.mCachedState.isBubbleExpanded(str);
        }

        public Bubble getBubbleWithShortcutId(String str) {
            return this.mCachedState.getBubbleWithShortcutId(str);
        }

        public void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer, Executor executor) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda21(this, consumer, executor, str));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$removeSuppressedSummaryIfNecessary$2(Consumer consumer, Executor executor, String str) {
            BubbleController.this.removeSuppressedSummaryIfNecessary(str, consumer != null ? new BubbleController$BubblesImpl$$ExternalSyntheticLambda23(executor, consumer) : null);
        }

        public void collapseStack() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda20(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$collapseStack$3() {
            BubbleController.this.collapseStack();
        }

        public void updateForThemeChanges() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$updateForThemeChanges$4() {
            BubbleController.this.updateForThemeChanges();
        }

        public void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda16(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$expandStackAndSelectBubble$5(BubbleEntry bubbleEntry) {
            BubbleController.this.expandStackAndSelectBubble(bubbleEntry);
        }

        public void expandStackAndSelectBubble(Bubble bubble) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda17(this, bubble));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$expandStackAndSelectBubble$6(Bubble bubble) {
            BubbleController.this.expandStackAndSelectBubble(bubble);
        }

        public boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer, Executor executor) {
            return ((Boolean) BubbleController.this.mMainExecutor.executeBlockingForResult(new BubbleController$BubblesImpl$$ExternalSyntheticLambda11(this, bubbleEntry, list, intConsumer != null ? new BubbleController$BubblesImpl$$ExternalSyntheticLambda10(executor, intConsumer) : null), Boolean.class)).booleanValue();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$handleDismissalInterception$11(BubbleEntry bubbleEntry, List list, IntConsumer intConsumer) {
            return Boolean.valueOf(BubbleController.this.handleDismissalInterception(bubbleEntry, list, intConsumer));
        }

        public void setSysuiProxy(Bubbles.SysuiProxy sysuiProxy) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda1(this, sysuiProxy));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setSysuiProxy$12(Bubbles.SysuiProxy sysuiProxy) {
            BubbleController.this.setSysuiProxy(sysuiProxy);
        }

        public void setExpandListener(Bubbles.BubbleExpandListener bubbleExpandListener) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda22(this, bubbleExpandListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setExpandListener$13(Bubbles.BubbleExpandListener bubbleExpandListener) {
            BubbleController.this.setExpandListener(bubbleExpandListener);
        }

        public void onEntryAdded(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda19(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryAdded$14(BubbleEntry bubbleEntry) {
            BubbleController.this.onEntryAdded(bubbleEntry);
        }

        public void onEntryUpdated(BubbleEntry bubbleEntry, boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda18(this, bubbleEntry, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryUpdated$15(BubbleEntry bubbleEntry, boolean z) {
            BubbleController.this.onEntryUpdated(bubbleEntry, z);
        }

        public void onEntryRemoved(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda5(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryRemoved$16(BubbleEntry bubbleEntry) {
            BubbleController.this.onEntryRemoved(bubbleEntry);
        }

        public void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda12(this, rankingMap, hashMap));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRankingUpdated$17(NotificationListenerService.RankingMap rankingMap, HashMap hashMap) {
            BubbleController.this.onRankingUpdated(rankingMap, hashMap);
        }

        public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            if (i == 2 || i == 3) {
                BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda4(this, str, userHandle, notificationChannel, i));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onNotificationChannelModified$18(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            BubbleController.this.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }

        public void onStatusBarVisibilityChanged(boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda14(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarVisibilityChanged$19(boolean z) {
            BubbleController.this.onStatusBarVisibilityChanged(z);
        }

        public void onZenStateChanged() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda9(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onZenStateChanged$20() {
            BubbleController.this.onZenStateChanged();
        }

        public void onStatusBarStateChanged(boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda6(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarStateChanged$21(boolean z) {
            BubbleController.this.onStatusBarStateChanged(z);
        }

        public void onUserChanged(int i) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda8(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserChanged$22(int i) {
            BubbleController.this.onUserChanged(i);
        }

        public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda15(this, sparseArray));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onCurrentProfilesChanged$23(SparseArray sparseArray) {
            BubbleController.this.onCurrentProfilesChanged(sparseArray);
        }

        public void onUserRemoved(int i) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda7(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserRemoved$24(int i) {
            BubbleController.this.onUserRemoved(i);
        }

        public void onConfigChanged(Configuration configuration) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda3(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigChanged$25(Configuration configuration) {
            BubbleController.this.onConfigChanged(configuration);
        }

        public void dump(PrintWriter printWriter, String[] strArr) {
            try {
                BubbleController.this.mMainExecutor.executeBlocking(new BubbleController$BubblesImpl$$ExternalSyntheticLambda0(this, printWriter, strArr));
            } catch (InterruptedException unused) {
                Slog.e("Bubbles", "Failed to dump BubbleController in 2s");
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$26(PrintWriter printWriter, String[] strArr) {
            BubbleController.this.dump(printWriter, strArr);
            this.mCachedState.dump(printWriter);
        }
    }
}
