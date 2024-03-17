package com.android.systemui.animation;

import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.LaunchAnimator;
import java.util.LinkedHashSet;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator {
    public static final long ANIMATION_DELAY_NAV_FADE_IN;
    @NotNull
    public static final Companion Companion;
    @NotNull
    public static final LaunchAnimator.Timings DIALOG_TIMINGS;
    @NotNull
    public static final LaunchAnimator.Interpolators INTERPOLATORS;
    public static final Interpolator NAV_FADE_IN_INTERPOLATOR = Interpolators.STANDARD_DECELERATE;
    @NotNull
    public static final PathInterpolator NAV_FADE_OUT_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 1.0f, 1.0f);
    @NotNull
    public static final LaunchAnimator.Timings TIMINGS;
    @Nullable
    public Callback callback;
    @NotNull
    public final LaunchAnimator dialogToAppAnimator;
    @NotNull
    public final LaunchAnimator launchAnimator;
    @NotNull
    public final LinkedHashSet<Listener> listeners;

    /* compiled from: ActivityLaunchAnimator.kt */
    public interface Callback {
        int getBackgroundColor(@NotNull TaskInfo taskInfo);

        void hideKeyguardWithAnimation(@NotNull IRemoteAnimationRunner iRemoteAnimationRunner);

        boolean isOnKeyguard();
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    public interface Listener {
        void onLaunchAnimationEnd() {
        }

        void onLaunchAnimationProgress(float f) {
        }

        void onLaunchAnimationStart() {
        }
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    public interface PendingIntentStarter {
        int startPendingIntent(@Nullable RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException;
    }

    public ActivityLaunchAnimator() {
        this((LaunchAnimator) null, (LaunchAnimator) null, 3, (DefaultConstructorMarker) null);
    }

    public final void startIntentWithAnimation(@Nullable Controller controller, boolean z, @Nullable String str, @NotNull Function1<? super RemoteAnimationAdapter, Integer> function1) {
        startIntentWithAnimation$default(this, controller, z, str, false, function1, 8, (Object) null);
    }

    public ActivityLaunchAnimator(@NotNull LaunchAnimator launchAnimator2, @NotNull LaunchAnimator launchAnimator3) {
        this.launchAnimator = launchAnimator2;
        this.dialogToAppAnimator = launchAnimator3;
        this.listeners = new LinkedHashSet<>();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ActivityLaunchAnimator(LaunchAnimator launchAnimator2, LaunchAnimator launchAnimator3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new LaunchAnimator(TIMINGS, INTERPOLATORS) : launchAnimator2, (i & 2) != 0 ? new LaunchAnimator(DIALOG_TIMINGS, INTERPOLATORS) : launchAnimator3);
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final LaunchAnimator.Interpolators getINTERPOLATORS() {
            return ActivityLaunchAnimator.INTERPOLATORS;
        }

        public final Interpolator createPositionXInterpolator() {
            Path path = new Path();
            path.moveTo(0.0f, 0.0f);
            Path path2 = path;
            path2.cubicTo(0.1217f, 0.0462f, 0.15f, 0.4686f, 0.1667f, 0.66f);
            path2.cubicTo(0.1834f, 0.8878f, 0.1667f, 1.0f, 1.0f, 1.0f);
            return new PathInterpolator(path);
        }
    }

    static {
        Companion companion = new Companion((DefaultConstructorMarker) null);
        Companion = companion;
        LaunchAnimator.Timings timings = new LaunchAnimator.Timings(500, 0, 150, 150, 183);
        TIMINGS = timings;
        DIALOG_TIMINGS = LaunchAnimator.Timings.copy$default(timings, 0, 0, 200, 200, 0, 19, (Object) null);
        INTERPOLATORS = new LaunchAnimator.Interpolators(Interpolators.EMPHASIZED, companion.createPositionXInterpolator(), Interpolators.LINEAR_OUT_SLOW_IN, new PathInterpolator(0.0f, 0.0f, 0.6f, 1.0f));
        ANIMATION_DELAY_NAV_FADE_IN = timings.getTotalDuration() - 266;
    }

    @Nullable
    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(@Nullable Callback callback2) {
        this.callback = callback2;
    }

    public static /* synthetic */ void startIntentWithAnimation$default(ActivityLaunchAnimator activityLaunchAnimator, Controller controller, boolean z, String str, boolean z2, Function1 function1, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        boolean z3 = z;
        if ((i & 4) != 0) {
            str = null;
        }
        String str2 = str;
        if ((i & 8) != 0) {
            z2 = false;
        }
        activityLaunchAnimator.startIntentWithAnimation(controller, z3, str2, z2, function1);
    }

    public final void startIntentWithAnimation(@Nullable Controller controller, boolean z, @Nullable String str, boolean z2, @NotNull Function1<? super RemoteAnimationAdapter, Integer> function1) {
        boolean z3;
        RemoteAnimationAdapter remoteAnimationAdapter;
        Controller controller2 = controller;
        String str2 = str;
        Function1<? super RemoteAnimationAdapter, Integer> function12 = function1;
        if (controller2 == null || !z) {
            Log.i("ActivityLaunchAnimator", "Starting intent with no animation");
            function12.invoke(null);
            if (controller2 != null) {
                callOnIntentStartedOnMainThread(controller2, false);
                return;
            }
            return;
        }
        Callback callback2 = this.callback;
        if (callback2 != null) {
            IRemoteAnimationRunner runner = new Runner(controller2);
            boolean z4 = callback2.isOnKeyguard() && !z2;
            if (!z4) {
                LaunchAnimator.Timings timings = TIMINGS;
                z3 = z4;
                remoteAnimationAdapter = new RemoteAnimationAdapter(runner, timings.getTotalDuration(), timings.getTotalDuration() - ((long) 150));
            } else {
                z3 = z4;
                remoteAnimationAdapter = null;
            }
            if (!(str2 == null || remoteAnimationAdapter == null)) {
                try {
                    ActivityTaskManager.getService().registerRemoteAnimationForNextActivityStart(str2, remoteAnimationAdapter, (IBinder) null);
                } catch (RemoteException e) {
                    Log.w("ActivityLaunchAnimator", "Unable to register the remote animation", e);
                }
            }
            int intValue = function12.invoke(remoteAnimationAdapter).intValue();
            boolean z5 = intValue == 2 || intValue == 0 || (intValue == 3 && z3);
            Log.i("ActivityLaunchAnimator", "launchResult=" + intValue + " willAnimate=" + z5 + " hideKeyguardWithAnimation=" + z3);
            callOnIntentStartedOnMainThread(controller2, z5);
            if (z5) {
                runner.postTimeout$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib();
                if (z3) {
                    callback2.hideKeyguardWithAnimation(runner);
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalStateException("ActivityLaunchAnimator.callback must be set before using this animator");
    }

    public final void callOnIntentStartedOnMainThread(Controller controller, boolean z) {
        if (!Intrinsics.areEqual((Object) Looper.myLooper(), (Object) Looper.getMainLooper())) {
            controller.getLaunchContainer().getContext().getMainExecutor().execute(new ActivityLaunchAnimator$callOnIntentStartedOnMainThread$1(controller, z));
        } else {
            controller.onIntentStarted(z);
        }
    }

    public final void startPendingIntentWithAnimation(@Nullable Controller controller, boolean z, @Nullable String str, @NotNull PendingIntentStarter pendingIntentStarter) throws PendingIntent.CanceledException {
        startIntentWithAnimation$default(this, controller, z, str, false, new ActivityLaunchAnimator$startPendingIntentWithAnimation$1(pendingIntentStarter), 8, (Object) null);
    }

    public final void addListener(@NotNull Listener listener) {
        this.listeners.add(listener);
    }

    public final void removeListener(@NotNull Listener listener) {
        this.listeners.remove(listener);
    }

    @NotNull
    @VisibleForTesting
    public final Runner createRunner(@NotNull Controller controller) {
        return new Runner(controller);
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    public interface Controller extends LaunchAnimator.Controller {
        @NotNull
        public static final Companion Companion = Companion.$$INSTANCE;

        @Nullable
        static Controller fromView(@NotNull View view, @Nullable Integer num) {
            return Companion.fromView(view, num);
        }

        boolean isDialogLaunch() {
            return false;
        }

        void onIntentStarted(boolean z) {
        }

        void onLaunchAnimationCancelled() {
        }

        /* compiled from: ActivityLaunchAnimator.kt */
        public static final class Companion {
            public static final /* synthetic */ Companion $$INSTANCE = new Companion();

            public static /* synthetic */ Controller fromView$default(Companion companion, View view, Integer num, int i, Object obj) {
                if ((i & 2) != 0) {
                    num = null;
                }
                return companion.fromView(view, num);
            }

            @Nullable
            public final Controller fromView(@NotNull View view, @Nullable Integer num) {
                if (view.getParent() instanceof ViewGroup) {
                    return new GhostedViewLaunchAnimatorController(view, num, (InteractionJankMonitor) null, 4, (DefaultConstructorMarker) null);
                }
                Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view + " is not attached to a ViewGroup", new Exception());
                return null;
            }
        }
    }

    @VisibleForTesting
    /* compiled from: ActivityLaunchAnimator.kt */
    public final class Runner extends IRemoteAnimationRunner.Stub {
        @Nullable
        public LaunchAnimator.Animation animation;
        public boolean cancelled;
        public final Context context;
        @NotNull
        public final Controller controller;
        @NotNull
        public final Matrix invertMatrix;
        @NotNull
        public final ViewGroup launchContainer;
        @NotNull
        public final Matrix matrix;
        @NotNull
        public Runnable onTimeout;
        public boolean timedOut;
        @NotNull
        public final SyncRtSurfaceTransactionApplier transactionApplier;
        @NotNull
        public final View transactionApplierView;
        @NotNull
        public Rect windowCrop;
        @NotNull
        public RectF windowCropF;

        public Runner(@NotNull Controller controller2) {
            this.controller = controller2;
            ViewGroup launchContainer2 = controller2.getLaunchContainer();
            this.launchContainer = launchContainer2;
            this.context = launchContainer2.getContext();
            View openingWindowSyncView = controller2.getOpeningWindowSyncView();
            openingWindowSyncView = openingWindowSyncView == null ? controller2.getLaunchContainer() : openingWindowSyncView;
            this.transactionApplierView = openingWindowSyncView;
            this.transactionApplier = new SyncRtSurfaceTransactionApplier(openingWindowSyncView);
            this.matrix = new Matrix();
            this.invertMatrix = new Matrix();
            this.windowCrop = new Rect();
            this.windowCropF = new RectF();
            this.onTimeout = new ActivityLaunchAnimator$Runner$onTimeout$1(this);
        }

        public final void postTimeout$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib() {
            this.launchContainer.postDelayed(this.onTimeout, 1000);
        }

        public final void removeTimeout() {
            this.launchContainer.removeCallbacks(this.onTimeout);
        }

        public void onAnimationStart(int i, @Nullable RemoteAnimationTarget[] remoteAnimationTargetArr, @Nullable RemoteAnimationTarget[] remoteAnimationTargetArr2, @Nullable RemoteAnimationTarget[] remoteAnimationTargetArr3, @Nullable IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            removeTimeout();
            if (this.timedOut) {
                if (iRemoteAnimationFinishedCallback != null) {
                    invoke(iRemoteAnimationFinishedCallback);
                }
            } else if (!this.cancelled) {
                this.context.getMainExecutor().execute(new ActivityLaunchAnimator$Runner$onAnimationStart$1(this, remoteAnimationTargetArr, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback));
            }
        }

        public final void applyStateToWindow(RemoteAnimationTarget remoteAnimationTarget, LaunchAnimator.State state) {
            if (this.transactionApplierView.getViewRootImpl() != null) {
                Rect rect = remoteAnimationTarget.screenSpaceBounds;
                int i = rect.left;
                int i2 = rect.right;
                float f = ((float) (i + i2)) / 2.0f;
                int i3 = rect.top;
                int i4 = rect.bottom;
                float f2 = (float) (i4 - i3);
                float max = Math.max(((float) state.getWidth()) / ((float) (i2 - i)), ((float) state.getHeight()) / f2);
                this.matrix.reset();
                this.matrix.setScale(max, max, f, ((float) (i3 + i4)) / 2.0f);
                float f3 = (f2 * max) - f2;
                this.matrix.postTranslate(state.getCenterX() - f, ((float) (state.getTop() - rect.top)) + (f3 / 2.0f));
                float left = ((float) state.getLeft()) - ((float) rect.left);
                float top = ((float) state.getTop()) - ((float) rect.top);
                this.windowCropF.set(left, top, ((float) state.getWidth()) + left, ((float) state.getHeight()) + top);
                this.matrix.invert(this.invertMatrix);
                this.invertMatrix.mapRect(this.windowCropF);
                this.windowCrop.set(MathKt__MathJVMKt.roundToInt(this.windowCropF.left), MathKt__MathJVMKt.roundToInt(this.windowCropF.top), MathKt__MathJVMKt.roundToInt(this.windowCropF.right), MathKt__MathJVMKt.roundToInt(this.windowCropF.bottom));
                SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withAlpha(1.0f).withMatrix(this.matrix).withWindowCrop(this.windowCrop).withCornerRadius(Math.max(state.getTopCornerRadius(), state.getBottomCornerRadius()) / max).withVisibility(true).build();
                this.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
            }
        }

        public final void applyStateToNavigationBar(RemoteAnimationTarget remoteAnimationTarget, LaunchAnimator.State state, float f) {
            if (this.transactionApplierView.getViewRootImpl() != null) {
                LaunchAnimator.Companion companion = LaunchAnimator.Companion;
                LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
                float progress = companion.getProgress(timings, f, ActivityLaunchAnimator.ANIMATION_DELAY_NAV_FADE_IN, 133);
                SyncRtSurfaceTransactionApplier.SurfaceParams.Builder builder = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash);
                if (progress > 0.0f) {
                    this.matrix.reset();
                    this.matrix.setTranslate(0.0f, (float) (state.getTop() - remoteAnimationTarget.sourceContainerBounds.top));
                    this.windowCrop.set(state.getLeft(), 0, state.getRight(), state.getHeight());
                    builder.withAlpha(ActivityLaunchAnimator.NAV_FADE_IN_INTERPOLATOR.getInterpolation(progress)).withMatrix(this.matrix).withWindowCrop(this.windowCrop).withVisibility(true);
                } else {
                    builder.withAlpha(1.0f - ActivityLaunchAnimator.NAV_FADE_OUT_INTERPOLATOR.getInterpolation(companion.getProgress(timings, f, 0, 133)));
                }
                this.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{builder.build()});
            }
        }

        public final void onAnimationTimedOut() {
            if (!this.cancelled) {
                Log.i("ActivityLaunchAnimator", "Remote animation timed out");
                this.timedOut = true;
                this.controller.onLaunchAnimationCancelled();
            }
        }

        public void onAnimationCancelled(boolean z) {
            if (!this.timedOut) {
                Log.i("ActivityLaunchAnimator", "Remote animation was cancelled");
                this.cancelled = true;
                removeTimeout();
                this.context.getMainExecutor().execute(new ActivityLaunchAnimator$Runner$onAnimationCancelled$1(this));
            }
        }

        public final void invoke(IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            try {
                iRemoteAnimationFinishedCallback.onAnimationFinished();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x0023  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0039  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void startAnimation(android.view.RemoteAnimationTarget[] r19, android.view.RemoteAnimationTarget[] r20, android.view.IRemoteAnimationFinishedCallback r21) {
            /*
                r18 = this;
                r7 = r18
                r0 = r19
                r1 = r20
                r3 = r21
                r2 = 0
                r8 = 1
                r4 = 0
                if (r0 != 0) goto L_0x000f
            L_0x000d:
                r5 = r4
                goto L_0x0021
            L_0x000f:
                int r5 = r0.length
                r6 = r2
            L_0x0011:
                if (r6 >= r5) goto L_0x000d
                r9 = r0[r6]
                int r6 = r6 + 1
                int r10 = r9.mode
                if (r10 != 0) goto L_0x001d
                r10 = r8
                goto L_0x001e
            L_0x001d:
                r10 = r2
            L_0x001e:
                if (r10 == 0) goto L_0x0011
                r5 = r9
            L_0x0021:
                if (r5 != 0) goto L_0x0039
                java.lang.String r0 = "ActivityLaunchAnimator"
                java.lang.String r1 = "Aborting the animation as no window is opening"
                android.util.Log.i(r0, r1)
                r18.removeTimeout()
                if (r3 != 0) goto L_0x0030
                goto L_0x0033
            L_0x0030:
                r7.invoke(r3)
            L_0x0033:
                com.android.systemui.animation.ActivityLaunchAnimator$Controller r0 = r7.controller
                r0.onLaunchAnimationCancelled()
                return
            L_0x0039:
                if (r1 != 0) goto L_0x003d
            L_0x003b:
                r6 = r4
                goto L_0x0051
            L_0x003d:
                int r0 = r1.length
                r6 = r2
            L_0x003f:
                if (r6 >= r0) goto L_0x003b
                r9 = r1[r6]
                int r6 = r6 + 1
                int r10 = r9.windowType
                r11 = 2019(0x7e3, float:2.829E-42)
                if (r10 != r11) goto L_0x004d
                r10 = r8
                goto L_0x004e
            L_0x004d:
                r10 = r2
            L_0x004e:
                if (r10 == 0) goto L_0x003f
                r6 = r9
            L_0x0051:
                android.graphics.Rect r0 = r5.screenSpaceBounds
                com.android.systemui.animation.LaunchAnimator$State r2 = new com.android.systemui.animation.LaunchAnimator$State
                int r10 = r0.top
                int r11 = r0.bottom
                int r12 = r0.left
                int r13 = r0.right
                r14 = 0
                r15 = 0
                r16 = 48
                r17 = 0
                r9 = r2
                r9.<init>(r10, r11, r12, r13, r14, r15, r16, r17)
                com.android.systemui.animation.ActivityLaunchAnimator r0 = com.android.systemui.animation.ActivityLaunchAnimator.this
                com.android.systemui.animation.ActivityLaunchAnimator$Callback r0 = r0.getCallback()
                kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                android.app.ActivityManager$RunningTaskInfo r1 = r5.taskInfo
                if (r1 != 0) goto L_0x0075
                goto L_0x007d
            L_0x0075:
                int r0 = r0.getBackgroundColor(r1)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r0)
            L_0x007d:
                if (r4 != 0) goto L_0x0082
                int r0 = r5.backgroundColor
                goto L_0x0086
            L_0x0082:
                int r0 = r4.intValue()
            L_0x0086:
                r9 = r0
                com.android.systemui.animation.ActivityLaunchAnimator$Controller r0 = r7.controller
                boolean r0 = r0.isDialogLaunch()
                if (r0 == 0) goto L_0x0096
                com.android.systemui.animation.ActivityLaunchAnimator r0 = com.android.systemui.animation.ActivityLaunchAnimator.this
                com.android.systemui.animation.LaunchAnimator r0 = r0.dialogToAppAnimator
                goto L_0x009c
            L_0x0096:
                com.android.systemui.animation.ActivityLaunchAnimator r0 = com.android.systemui.animation.ActivityLaunchAnimator.this
                com.android.systemui.animation.LaunchAnimator r0 = r0.launchAnimator
            L_0x009c:
                r10 = r0
                com.android.systemui.animation.ActivityLaunchAnimator$Controller r0 = r7.controller
                android.view.ViewGroup r0 = r0.getLaunchContainer()
                boolean r0 = r10.isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(r0, r2)
                if (r0 == 0) goto L_0x00b0
                android.content.Context r0 = r7.context
                float r0 = com.android.internal.policy.ScreenDecorationsUtils.getWindowCornerRadius(r0)
                goto L_0x00b1
            L_0x00b0:
                r0 = 0
            L_0x00b1:
                r2.setTopCornerRadius(r0)
                r2.setBottomCornerRadius(r0)
                com.android.systemui.animation.ActivityLaunchAnimator$Controller r1 = r7.controller
                com.android.systemui.animation.ActivityLaunchAnimator$Runner$startAnimation$controller$1 r11 = new com.android.systemui.animation.ActivityLaunchAnimator$Runner$startAnimation$controller$1
                com.android.systemui.animation.ActivityLaunchAnimator r4 = com.android.systemui.animation.ActivityLaunchAnimator.this
                r0 = r11
                r12 = r2
                r2 = r4
                r3 = r21
                r4 = r18
                r0.<init>(r1, r2, r3, r4, r5, r6)
                com.android.systemui.animation.LaunchAnimator$Animation r0 = r10.startAnimation(r11, r12, r9, r8)
                r7.animation = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.ActivityLaunchAnimator.Runner.startAnimation(android.view.RemoteAnimationTarget[], android.view.RemoteAnimationTarget[], android.view.IRemoteAnimationFinishedCallback):void");
        }
    }
}
