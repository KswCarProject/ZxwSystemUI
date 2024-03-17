package com.android.systemui.animation;

import android.app.Dialog;
import android.service.dreams.IDreamManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import java.util.HashSet;
import java.util.Iterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DialogLaunchAnimator.kt */
public final class DialogLaunchAnimator {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    @Deprecated
    public static final LaunchAnimator.Interpolators INTERPOLATORS;
    @Deprecated
    public static final int TAG_LAUNCH_ANIMATION_RUNNING = R$id.tag_launch_animation_running;
    @NotNull
    @Deprecated
    public static final LaunchAnimator.Timings TIMINGS = ActivityLaunchAnimator.TIMINGS;
    @NotNull
    public final IDreamManager dreamManager;
    public final boolean isForTesting;
    @NotNull
    public final LaunchAnimator launchAnimator;
    @NotNull
    public final HashSet<AnimatedDialog> openedDialogs;

    public DialogLaunchAnimator(@NotNull IDreamManager iDreamManager) {
        this(iDreamManager, (LaunchAnimator) null, false, 6, (DefaultConstructorMarker) null);
    }

    @Nullable
    public final ActivityLaunchAnimator.Controller createActivityLaunchController(@NotNull View view) {
        return createActivityLaunchController$default(this, view, (Integer) null, 2, (Object) null);
    }

    public final void showFromView(@NotNull Dialog dialog, @NotNull View view) {
        showFromView$default(this, dialog, view, false, 4, (Object) null);
    }

    public DialogLaunchAnimator(@NotNull IDreamManager iDreamManager, @NotNull LaunchAnimator launchAnimator2, boolean z) {
        this.dreamManager = iDreamManager;
        this.launchAnimator = launchAnimator2;
        this.isForTesting = z;
        this.openedDialogs = new HashSet<>();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ DialogLaunchAnimator(IDreamManager iDreamManager, LaunchAnimator launchAnimator2, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(iDreamManager, (i & 2) != 0 ? new LaunchAnimator(TIMINGS, INTERPOLATORS) : launchAnimator2, (i & 4) != 0 ? false : z);
    }

    /* compiled from: DialogLaunchAnimator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    static {
        ActivityLaunchAnimator.Companion companion = ActivityLaunchAnimator.Companion;
        INTERPOLATORS = LaunchAnimator.Interpolators.copy$default(companion.getINTERPOLATORS(), (Interpolator) null, companion.getINTERPOLATORS().getPositionInterpolator(), (Interpolator) null, (Interpolator) null, 13, (Object) null);
    }

    public static /* synthetic */ void showFromView$default(DialogLaunchAnimator dialogLaunchAnimator, Dialog dialog, View view, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        dialogLaunchAnimator.showFromView(dialog, view, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0043, code lost:
        r0 = r9.getDialogContentWithBackground();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void showFromView(@org.jetbrains.annotations.NotNull android.app.Dialog r12, @org.jetbrains.annotations.NotNull android.view.View r13, boolean r14) {
        /*
            r11 = this;
            android.os.Looper r0 = android.os.Looper.myLooper()
            android.os.Looper r1 = android.os.Looper.getMainLooper()
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r1)
            if (r0 == 0) goto L_0x0080
            java.util.HashSet<com.android.systemui.animation.AnimatedDialog> r0 = r11.openedDialogs
            java.util.Iterator r0 = r0.iterator()
        L_0x0014:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x003c
            java.lang.Object r1 = r0.next()
            r2 = r1
            com.android.systemui.animation.AnimatedDialog r2 = (com.android.systemui.animation.AnimatedDialog) r2
            android.app.Dialog r2 = r2.getDialog()
            android.view.Window r2 = r2.getWindow()
            android.view.View r2 = r2.getDecorView()
            android.view.ViewRootImpl r2 = r2.getViewRootImpl()
            android.view.ViewRootImpl r3 = r13.getViewRootImpl()
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r3)
            if (r2 == 0) goto L_0x0014
            goto L_0x003d
        L_0x003c:
            r1 = 0
        L_0x003d:
            r9 = r1
            com.android.systemui.animation.AnimatedDialog r9 = (com.android.systemui.animation.AnimatedDialog) r9
            if (r9 != 0) goto L_0x0043
            goto L_0x0049
        L_0x0043:
            android.view.ViewGroup r0 = r9.getDialogContentWithBackground()
            if (r0 != 0) goto L_0x004b
        L_0x0049:
            r5 = r13
            goto L_0x004c
        L_0x004b:
            r5 = r0
        L_0x004c:
            int r13 = TAG_LAUNCH_ANIMATION_RUNNING
            java.lang.Object r0 = r5.getTag(r13)
            if (r0 == 0) goto L_0x005f
            java.lang.String r11 = "DialogLaunchAnimator"
            java.lang.String r13 = "Not running dialog launch animation as there is already one running"
            android.util.Log.e(r11, r13)
            r12.show()
            return
        L_0x005f:
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            r5.setTag(r13, r0)
            com.android.systemui.animation.AnimatedDialog r13 = new com.android.systemui.animation.AnimatedDialog
            com.android.systemui.animation.LaunchAnimator r3 = r11.launchAnimator
            android.service.dreams.IDreamManager r4 = r11.dreamManager
            com.android.systemui.animation.DialogLaunchAnimator$showFromView$animatedDialog$1 r6 = new com.android.systemui.animation.DialogLaunchAnimator$showFromView$animatedDialog$1
            r6.<init>(r11)
            boolean r10 = r11.isForTesting
            r2 = r13
            r7 = r12
            r8 = r14
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            java.util.HashSet<com.android.systemui.animation.AnimatedDialog> r11 = r11.openedDialogs
            r11.add(r13)
            r13.start()
            return
        L_0x0080:
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException
            java.lang.String r12 = "showFromView must be called from the main thread and dialog must be created in the main thread"
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.DialogLaunchAnimator.showFromView(android.app.Dialog, android.view.View, boolean):void");
    }

    public static /* synthetic */ void showFromDialog$default(DialogLaunchAnimator dialogLaunchAnimator, Dialog dialog, Dialog dialog2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        dialogLaunchAnimator.showFromDialog(dialog, dialog2, z);
    }

    public final void showFromDialog(@NotNull Dialog dialog, @NotNull Dialog dialog2, boolean z) {
        ViewGroup viewGroup;
        T t;
        Iterator<T> it = this.openedDialogs.iterator();
        while (true) {
            viewGroup = null;
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            if (Intrinsics.areEqual((Object) ((AnimatedDialog) t).getDialog(), (Object) dialog2)) {
                break;
            }
        }
        AnimatedDialog animatedDialog = (AnimatedDialog) t;
        if (animatedDialog != null) {
            viewGroup = animatedDialog.getDialogContentWithBackground();
        }
        if (viewGroup != null) {
            showFromView(dialog, viewGroup, z);
            return;
        }
        throw new IllegalStateException("The animateFrom dialog was not animated using DialogLaunchAnimator.showFrom(View|Dialog)");
    }

    public static /* synthetic */ ActivityLaunchAnimator.Controller createActivityLaunchController$default(DialogLaunchAnimator dialogLaunchAnimator, View view, Integer num, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        return dialogLaunchAnimator.createActivityLaunchController(view, num);
    }

    @Nullable
    public final ActivityLaunchAnimator.Controller createActivityLaunchController(@NotNull View view, @Nullable Integer num) {
        T t;
        ViewGroup dialogContentWithBackground;
        ActivityLaunchAnimator.Controller fromView;
        Iterator<T> it = this.openedDialogs.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            if (Intrinsics.areEqual((Object) ((AnimatedDialog) t).getDialog().getWindow().getDecorView().getViewRootImpl(), (Object) view.getViewRootImpl())) {
                break;
            }
        }
        AnimatedDialog animatedDialog = (AnimatedDialog) t;
        if (animatedDialog == null) {
            return null;
        }
        animatedDialog.setExitAnimationDisabled(true);
        Dialog dialog = animatedDialog.getDialog();
        if (!dialog.isShowing() || (dialogContentWithBackground = animatedDialog.getDialogContentWithBackground()) == null || (fromView = ActivityLaunchAnimator.Controller.Companion.fromView(dialogContentWithBackground, num)) == null) {
            return null;
        }
        return new DialogLaunchAnimator$createActivityLaunchController$1(fromView, dialog, animatedDialog);
    }

    public final void disableAllCurrentDialogsExitAnimations() {
        for (AnimatedDialog exitAnimationDisabled : this.openedDialogs) {
            exitAnimationDisabled.setExitAnimationDisabled(true);
        }
    }

    public final void dismissStack(@NotNull Dialog dialog) {
        T t;
        Iterator<T> it = this.openedDialogs.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            if (Intrinsics.areEqual((Object) ((AnimatedDialog) t).getDialog(), (Object) dialog)) {
                break;
            }
        }
        AnimatedDialog animatedDialog = (AnimatedDialog) t;
        if (animatedDialog != null) {
            animatedDialog.setTouchSurface(animatedDialog.prepareForStackDismiss());
        }
        dialog.dismiss();
    }
}
