package com.android.systemui.animation;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.WindowConfiguration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.RotationUtils;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import java.util.ArrayList;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RemoteTransitionAdapter.kt */
public final class RemoteTransitionAdapter {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @NotNull
    public static final RemoteTransition adaptRemoteAnimation(@NotNull RemoteAnimationAdapter remoteAnimationAdapter) {
        return Companion.adaptRemoteAnimation(remoteAnimationAdapter);
    }

    /* compiled from: RemoteTransitionAdapter.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int newModeToLegacyMode(int i) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i != 4) {
                            return 2;
                        }
                    }
                }
                return 1;
            }
            return 0;
        }

        public Companion() {
        }

        @SuppressLint({"NewApi"})
        public final void setupLeash(SurfaceControl surfaceControl, TransitionInfo.Change change, int i, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
            boolean z = transitionInfo.getType() == 1 || transitionInfo.getType() == 3;
            int size = transitionInfo.getChanges().size();
            int mode = change.getMode();
            transaction.reparent(surfaceControl, transitionInfo.getRootLeash());
            transaction.setPosition(surfaceControl, (float) (change.getStartAbsBounds().left - transitionInfo.getRootOffset().x), (float) (change.getStartAbsBounds().top - transitionInfo.getRootOffset().y));
            transaction.show(surfaceControl);
            if (mode != 1) {
                if (mode != 2) {
                    if (mode != 3) {
                        if (mode != 4) {
                            transaction.setLayer(surfaceControl, (size + transitionInfo.getChanges().size()) - i);
                            return;
                        }
                    }
                }
                if (z) {
                    transaction.setLayer(surfaceControl, size - i);
                    return;
                } else {
                    transaction.setLayer(surfaceControl, (size + transitionInfo.getChanges().size()) - i);
                    return;
                }
            }
            if (z) {
                transaction.setLayer(surfaceControl, (size + transitionInfo.getChanges().size()) - i);
                if ((change.getFlags() & 8) == 0) {
                    transaction.setAlpha(surfaceControl, 0.0f);
                    return;
                }
                return;
            }
            transaction.setLayer(surfaceControl, size - i);
        }

        @SuppressLint({"NewApi"})
        public final SurfaceControl createLeash(TransitionInfo transitionInfo, TransitionInfo.Change change, int i, SurfaceControl.Transaction transaction) {
            SurfaceControl surfaceControl;
            if (change.getParent() != null && (change.getFlags() & 2) != 0) {
                return change.getLeash();
            }
            SurfaceControl.Builder containerLayer = new SurfaceControl.Builder().setName(Intrinsics.stringPlus(change.getLeash().toString(), "_transition-leash")).setContainerLayer();
            if (change.getParent() == null) {
                surfaceControl = transitionInfo.getRootLeash();
            } else {
                WindowContainerToken parent = change.getParent();
                Intrinsics.checkNotNull(parent);
                TransitionInfo.Change change2 = transitionInfo.getChange(parent);
                Intrinsics.checkNotNull(change2);
                surfaceControl = change2.getLeash();
            }
            SurfaceControl build = containerLayer.setParent(surfaceControl).build();
            setupLeash(build, change, transitionInfo.getChanges().size() - i, transitionInfo, transaction);
            transaction.reparent(change.getLeash(), build);
            transaction.setAlpha(change.getLeash(), 1.0f);
            transaction.show(change.getLeash());
            transaction.setPosition(change.getLeash(), 0.0f, 0.0f);
            transaction.setLayer(change.getLeash(), 0);
            return build;
        }

        public final Rect rectOffsetTo(Rect rect, Point point) {
            Rect rect2 = new Rect(rect);
            rect2.offsetTo(point.x, point.y);
            return rect2;
        }

        @NotNull
        public final RemoteAnimationTarget createTarget(@NotNull TransitionInfo.Change change, int i, @NotNull TransitionInfo transitionInfo, @NotNull SurfaceControl.Transaction transaction) {
            int i2;
            WindowConfiguration windowConfiguration;
            boolean z;
            if (change.getTaskInfo() != null) {
                ActivityManager.RunningTaskInfo taskInfo = change.getTaskInfo();
                Intrinsics.checkNotNull(taskInfo);
                i2 = taskInfo.taskId;
            } else {
                i2 = -1;
            }
            int newModeToLegacyMode = newModeToLegacyMode(change.getMode());
            SurfaceControl createLeash = createLeash(transitionInfo, change, i, transaction);
            boolean z2 = ((change.getFlags() & 4) == 0 && (change.getFlags() & 1) == 0) ? false : true;
            Rect rect = new Rect(0, 0, 0, 0);
            Rect rectOffsetTo = rectOffsetTo(change.getEndAbsBounds(), change.getEndRelOffset());
            Rect rect2 = new Rect(change.getEndAbsBounds());
            if (change.getTaskInfo() != null) {
                ActivityManager.RunningTaskInfo taskInfo2 = change.getTaskInfo();
                Intrinsics.checkNotNull(taskInfo2);
                windowConfiguration = taskInfo2.configuration.windowConfiguration;
            } else {
                windowConfiguration = new WindowConfiguration();
            }
            WindowConfiguration windowConfiguration2 = windowConfiguration;
            if (change.getTaskInfo() != null) {
                ActivityManager.RunningTaskInfo taskInfo3 = change.getTaskInfo();
                Intrinsics.checkNotNull(taskInfo3);
                if (taskInfo3.isRunning) {
                    z = false;
                    Rect rect3 = r5;
                    Rect rect4 = new Rect(change.getStartAbsBounds());
                    RemoteAnimationTarget remoteAnimationTarget = r0;
                    RemoteAnimationTarget remoteAnimationTarget2 = new RemoteAnimationTarget(i2, newModeToLegacyMode, createLeash, z2, (Rect) null, rect, i, (Point) null, rectOffsetTo, rect2, windowConfiguration2, z, (SurfaceControl) null, rect3, change.getTaskInfo(), change.getAllowEnterPip(), -1);
                    RemoteAnimationTarget remoteAnimationTarget3 = remoteAnimationTarget;
                    remoteAnimationTarget3.backgroundColor = change.getBackgroundColor();
                    return remoteAnimationTarget3;
                }
            }
            z = true;
            Rect rect32 = rect4;
            Rect rect42 = new Rect(change.getStartAbsBounds());
            RemoteAnimationTarget remoteAnimationTarget4 = remoteAnimationTarget2;
            RemoteAnimationTarget remoteAnimationTarget22 = new RemoteAnimationTarget(i2, newModeToLegacyMode, createLeash, z2, (Rect) null, rect, i, (Point) null, rectOffsetTo, rect2, windowConfiguration2, z, (SurfaceControl) null, rect32, change.getTaskInfo(), change.getAllowEnterPip(), -1);
            RemoteAnimationTarget remoteAnimationTarget32 = remoteAnimationTarget4;
            remoteAnimationTarget32.backgroundColor = change.getBackgroundColor();
            return remoteAnimationTarget32;
        }

        @NotNull
        public final RemoteAnimationTarget[] wrapTargets(@NotNull TransitionInfo transitionInfo, boolean z, @NotNull SurfaceControl.Transaction transaction, @Nullable ArrayMap<SurfaceControl, SurfaceControl> arrayMap) {
            ArrayList arrayList = new ArrayList();
            int size = transitionInfo.getChanges().size();
            int i = 0;
            while (i < size) {
                int i2 = i + 1;
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i);
                if (z == ((change.getFlags() & 2) != 0)) {
                    arrayList.add(createTarget(change, transitionInfo.getChanges().size() - i, transitionInfo, transaction));
                    if (arrayMap != null) {
                        arrayMap.put(change.getLeash(), ((RemoteAnimationTarget) arrayList.get(arrayList.size() - 1)).leash);
                    }
                }
                i = i2;
            }
            Object[] array = arrayList.toArray(new RemoteAnimationTarget[0]);
            if (array != null) {
                return (RemoteAnimationTarget[]) array;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }

        @NotNull
        public final IRemoteTransition.Stub adaptRemoteRunner(@NotNull IRemoteAnimationRunner iRemoteAnimationRunner) {
            return new RemoteTransitionAdapter$Companion$adaptRemoteRunner$1(iRemoteAnimationRunner);
        }

        @NotNull
        public final RemoteTransition adaptRemoteAnimation(@NotNull RemoteAnimationAdapter remoteAnimationAdapter) {
            return new RemoteTransition(adaptRemoteRunner(remoteAnimationAdapter.getRunner()), remoteAnimationAdapter.getCallingApplication());
        }
    }

    /* compiled from: RemoteTransitionAdapter.kt */
    public static final class CounterRotator {
        @Nullable
        public SurfaceControl surface;

        @Nullable
        public final SurfaceControl getSurface() {
            return this.surface;
        }

        public final void setup(@NotNull SurfaceControl.Transaction transaction, @NotNull SurfaceControl surfaceControl, int i, float f, float f2) {
            if (i != 0) {
                SurfaceControl build = new SurfaceControl.Builder().setName("Transition Unrotate").setContainerLayer().setParent(surfaceControl).build();
                RotationUtils.rotateSurface(transaction, build, i);
                boolean z = false;
                Point point = new Point(0, 0);
                if (i % 2 != 0) {
                    z = true;
                }
                float f3 = z ? f2 : f;
                if (!z) {
                    f = f2;
                }
                RotationUtils.rotatePoint(point, i, (int) f3, (int) f);
                transaction.setPosition(build, (float) point.x, (float) point.y);
                transaction.show(build);
            }
        }

        public final void addChild(@NotNull SurfaceControl.Transaction transaction, @Nullable SurfaceControl surfaceControl) {
            if (this.surface != null) {
                Intrinsics.checkNotNull(surfaceControl);
                transaction.reparent(surfaceControl, this.surface);
            }
        }

        public final void cleanUp(@NotNull SurfaceControl.Transaction transaction) {
            SurfaceControl surfaceControl = this.surface;
            if (surfaceControl != null) {
                Intrinsics.checkNotNull(surfaceControl);
                transaction.remove(surfaceControl);
            }
        }
    }
}
