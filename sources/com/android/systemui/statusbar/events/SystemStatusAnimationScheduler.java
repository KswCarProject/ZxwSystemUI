package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Process;
import android.provider.DeviceConfig;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler implements CallbackController<SystemStatusAnimationCallback>, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public int animationState;
    @NotNull
    public final SystemEventChipAnimationController chipAnimationController;
    @NotNull
    public final SystemEventCoordinator coordinator;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final DelayableExecutor executor;
    public boolean hasPersistentDot;
    @NotNull
    public final Set<SystemStatusAnimationCallback> listeners = new LinkedHashSet();
    @Nullable
    public StatusEvent scheduledEvent;
    @NotNull
    public final StatusBarWindowController statusBarWindowController;
    @NotNull
    public final SystemClock systemClock;

    public SystemStatusAnimationScheduler(@NotNull SystemEventCoordinator systemEventCoordinator, @NotNull SystemEventChipAnimationController systemEventChipAnimationController, @NotNull StatusBarWindowController statusBarWindowController2, @NotNull DumpManager dumpManager2, @NotNull SystemClock systemClock2, @NotNull DelayableExecutor delayableExecutor) {
        this.coordinator = systemEventCoordinator;
        this.chipAnimationController = systemEventChipAnimationController;
        this.statusBarWindowController = statusBarWindowController2;
        this.dumpManager = dumpManager2;
        this.systemClock = systemClock2;
        this.executor = delayableExecutor;
        systemEventCoordinator.attachScheduler(this);
        dumpManager2.registerDumpable("SystemStatusAnimationScheduler", this);
    }

    /* compiled from: SystemStatusAnimationScheduler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final boolean isImmersiveIndicatorEnabled() {
        return DeviceConfig.getBoolean("privacy", "enable_immersive_indicator", true);
    }

    public final int getAnimationState() {
        return this.animationState;
    }

    public final boolean getHasPersistentDot() {
        return this.hasPersistentDot;
    }

    public final void onStatusEvent(@NotNull StatusEvent statusEvent) {
        if (!isTooEarly() && isImmersiveIndicatorEnabled()) {
            Assert.isMainThread();
            int priority = statusEvent.getPriority();
            StatusEvent statusEvent2 = this.scheduledEvent;
            if (priority <= (statusEvent2 == null ? -1 : statusEvent2.getPriority())) {
                StatusEvent statusEvent3 = this.scheduledEvent;
                boolean z = false;
                if (statusEvent3 != null && statusEvent3.shouldUpdateFromEvent(statusEvent)) {
                    z = true;
                }
                if (!z) {
                    return;
                }
            }
            if (statusEvent.getShowAnimation()) {
                scheduleEvent(statusEvent);
            } else if (statusEvent.getForceVisible()) {
                this.hasPersistentDot = true;
                if (this.animationState == 0) {
                    notifyTransitionToPersistentDot();
                }
            }
        }
    }

    public final void clearDotIfVisible() {
        notifyHidePersistentDot();
    }

    public final void setShouldShowPersistentPrivacyIndicator(boolean z) {
        if (this.hasPersistentDot != z && isImmersiveIndicatorEnabled()) {
            this.hasPersistentDot = z;
            if (!z) {
                clearDotIfVisible();
            }
        }
    }

    public final boolean isTooEarly() {
        return this.systemClock.uptimeMillis() - Process.getStartUptimeMillis() < 5000;
    }

    public final void scheduleEvent(StatusEvent statusEvent) {
        int i = this.animationState;
        if (i == 4) {
            return;
        }
        if (i != 5 || !statusEvent.getForceVisible()) {
            StatusEvent statusEvent2 = this.scheduledEvent;
            boolean z = false;
            if (statusEvent2 != null && statusEvent2.shouldUpdateFromEvent(statusEvent)) {
                z = true;
            }
            if (z) {
                StatusEvent statusEvent3 = this.scheduledEvent;
                if (statusEvent3 != null) {
                    statusEvent3.updateFromEvent(statusEvent);
                    return;
                }
                return;
            }
            this.scheduledEvent = statusEvent;
            Intrinsics.checkNotNull(statusEvent);
            if (statusEvent.getForceVisible()) {
                this.hasPersistentDot = true;
            }
            SystemEventChipAnimationController systemEventChipAnimationController = this.chipAnimationController;
            StatusEvent statusEvent4 = this.scheduledEvent;
            Intrinsics.checkNotNull(statusEvent4);
            systemEventChipAnimationController.prepareChipAnimation(statusEvent4.getViewCreator());
            this.animationState = 1;
            this.executor.executeDelayed(new SystemStatusAnimationScheduler$scheduleEvent$1(this), 100);
        }
    }

    public final void runChipAnimation() {
        this.statusBarWindowController.setForceStatusBarVisible(true);
        this.animationState = 2;
        AnimatorSet collectStartAnimations = collectStartAnimations();
        if (collectStartAnimations.getTotalDuration() <= 500) {
            collectStartAnimations.addListener(new SystemStatusAnimationScheduler$runChipAnimation$1(this));
            collectStartAnimations.start();
            this.executor.executeDelayed(new SystemStatusAnimationScheduler$runChipAnimation$2(this), 1000);
            return;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("System animation total length exceeds budget. Expected: 500, actual: ", Long.valueOf(collectStartAnimations.getTotalDuration())));
    }

    public final AnimatorSet collectStartAnimations() {
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemEventAnimationBegin : this.listeners) {
            Animator onSystemEventAnimationBegin2 = onSystemEventAnimationBegin.onSystemEventAnimationBegin();
            if (onSystemEventAnimationBegin2 != null) {
                arrayList.add(onSystemEventAnimationBegin2);
            }
        }
        arrayList.add(this.chipAnimationController.onSystemEventAnimationBegin());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    public final AnimatorSet collectFinishAnimations() {
        Animator notifyTransitionToPersistentDot;
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemEventAnimationFinish : this.listeners) {
            Animator onSystemEventAnimationFinish2 = onSystemEventAnimationFinish.onSystemEventAnimationFinish(getHasPersistentDot());
            if (onSystemEventAnimationFinish2 != null) {
                arrayList.add(onSystemEventAnimationFinish2);
            }
        }
        arrayList.add(this.chipAnimationController.onSystemEventAnimationFinish(this.hasPersistentDot));
        if (this.hasPersistentDot && (notifyTransitionToPersistentDot = notifyTransitionToPersistentDot()) != null) {
            arrayList.add(notifyTransitionToPersistentDot);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    public final Animator notifyTransitionToPersistentDot() {
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onSystemStatusAnimationTransitionToPersistentDot : this.listeners) {
            Animator onSystemStatusAnimationTransitionToPersistentDot2 = onSystemStatusAnimationTransitionToPersistentDot.onSystemStatusAnimationTransitionToPersistentDot();
            if (onSystemStatusAnimationTransitionToPersistentDot2 != null) {
                arrayList.add(onSystemStatusAnimationTransitionToPersistentDot2);
            }
        }
        if (!(!arrayList.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    public final Animator notifyHidePersistentDot() {
        ArrayList arrayList = new ArrayList();
        for (SystemStatusAnimationCallback onHidePersistentDot : this.listeners) {
            Animator onHidePersistentDot2 = onHidePersistentDot.onHidePersistentDot();
            if (onHidePersistentDot2 != null) {
                arrayList.add(onHidePersistentDot2);
            }
        }
        if (this.animationState == 5) {
            this.animationState = 0;
        }
        if (!(!arrayList.isEmpty())) {
            return null;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        return animatorSet;
    }

    public void addCallback(@NotNull SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Assert.isMainThread();
        if (this.listeners.isEmpty()) {
            this.coordinator.startObserving();
        }
        this.listeners.add(systemStatusAnimationCallback);
    }

    public void removeCallback(@NotNull SystemStatusAnimationCallback systemStatusAnimationCallback) {
        Assert.isMainThread();
        this.listeners.remove(systemStatusAnimationCallback);
        if (this.listeners.isEmpty()) {
            this.coordinator.stopObserving();
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Scheduled event: ", this.scheduledEvent));
        printWriter.println(Intrinsics.stringPlus("Has persistent privacy dot: ", Boolean.valueOf(this.hasPersistentDot)));
        printWriter.println(Intrinsics.stringPlus("Animation state: ", Integer.valueOf(this.animationState)));
        printWriter.println("Listeners:");
        if (this.listeners.isEmpty()) {
            printWriter.println("(none)");
            return;
        }
        for (SystemStatusAnimationCallback stringPlus : this.listeners) {
            printWriter.println(Intrinsics.stringPlus("  ", stringPlus));
        }
    }
}
