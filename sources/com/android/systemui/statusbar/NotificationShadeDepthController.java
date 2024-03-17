package com.android.systemui.statusbar;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.WallpaperController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationShadeDepthController.kt */
public final class NotificationShadeDepthController implements PanelExpansionListener, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final BiometricUnlockController biometricUnlockController;
    @Nullable
    public View blurRoot;
    @NotNull
    public final BlurUtils blurUtils;
    public boolean blursDisabledForAppLaunch;
    public boolean blursDisabledForUnlock;
    @NotNull
    public DepthAnimation brightnessMirrorSpring = new DepthAnimation();
    public boolean brightnessMirrorVisible;
    @NotNull
    public final Choreographer choreographer;
    @NotNull
    public final Context context;
    @NotNull
    public final DozeParameters dozeParameters;
    public boolean inSplitShade;
    public boolean isBlurred;
    public boolean isClosed = true;
    public boolean isOpen;
    @Nullable
    public Animator keyguardAnimator;
    @NotNull
    public final NotificationShadeDepthController$keyguardStateCallback$1 keyguardStateCallback;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    public int lastAppliedBlur;
    @NotNull
    public List<DepthListener> listeners = new ArrayList();
    @Nullable
    public Animator notificationAnimator;
    @NotNull
    public final NotificationShadeWindowController notificationShadeWindowController;
    public float panelPullDownMinFraction;
    public int prevShadeDirection;
    public float prevShadeVelocity;
    public long prevTimestamp = -1;
    public boolean prevTracking;
    public float qsPanelExpansion;
    public View root;
    public boolean scrimsVisible;
    @NotNull
    public DepthAnimation shadeAnimation = new DepthAnimation();
    public float shadeExpansion;
    @NotNull
    public final NotificationShadeDepthController$statusBarStateCallback$1 statusBarStateCallback;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    public float transitionToFullShadeProgress;
    @NotNull
    public final Choreographer.FrameCallback updateBlurCallback = new NotificationShadeDepthController$updateBlurCallback$1(this);
    public boolean updateScheduled;
    public float wakeAndUnlockBlurRadius;
    @NotNull
    public final WallpaperController wallpaperController;

    /* compiled from: NotificationShadeDepthController.kt */
    public interface DepthListener {
        void onBlurRadiusChanged(int i) {
        }

        void onWallpaperZoomOutChanged(float f);
    }

    public static /* synthetic */ void getBrightnessMirrorSpring$annotations() {
    }

    public static /* synthetic */ void getShadeExpansion$annotations() {
    }

    public static /* synthetic */ void getUpdateBlurCallback$annotations() {
    }

    public NotificationShadeDepthController(@NotNull StatusBarStateController statusBarStateController2, @NotNull BlurUtils blurUtils2, @NotNull BiometricUnlockController biometricUnlockController2, @NotNull KeyguardStateController keyguardStateController2, @NotNull Choreographer choreographer2, @NotNull WallpaperController wallpaperController2, @NotNull NotificationShadeWindowController notificationShadeWindowController2, @NotNull DozeParameters dozeParameters2, @NotNull Context context2, @NotNull DumpManager dumpManager, @NotNull ConfigurationController configurationController) {
        this.statusBarStateController = statusBarStateController2;
        this.blurUtils = blurUtils2;
        this.biometricUnlockController = biometricUnlockController2;
        this.keyguardStateController = keyguardStateController2;
        this.choreographer = choreographer2;
        this.wallpaperController = wallpaperController2;
        this.notificationShadeWindowController = notificationShadeWindowController2;
        this.dozeParameters = dozeParameters2;
        this.context = context2;
        NotificationShadeDepthController$keyguardStateCallback$1 notificationShadeDepthController$keyguardStateCallback$1 = new NotificationShadeDepthController$keyguardStateCallback$1(this);
        this.keyguardStateCallback = notificationShadeDepthController$keyguardStateCallback$1;
        NotificationShadeDepthController$statusBarStateCallback$1 notificationShadeDepthController$statusBarStateCallback$1 = new NotificationShadeDepthController$statusBarStateCallback$1(this);
        this.statusBarStateCallback = notificationShadeDepthController$statusBarStateCallback$1;
        dumpManager.registerDumpable(NotificationShadeDepthController.class.getName(), this);
        keyguardStateController2.addCallback(notificationShadeDepthController$keyguardStateCallback$1);
        statusBarStateController2.addCallback(notificationShadeDepthController$statusBarStateCallback$1);
        notificationShadeWindowController2.setScrimsVisibilityListener(new Consumer(this) {
            public final /* synthetic */ NotificationShadeDepthController this$0;

            {
                this.this$0 = r1;
            }

            public final void accept(Integer num) {
                this.this$0.setScrimsVisible(num != null && num.intValue() == 2);
            }
        });
        this.shadeAnimation.setStiffness(200.0f);
        this.shadeAnimation.setDampingRatio(1.0f);
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ NotificationShadeDepthController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    /* compiled from: NotificationShadeDepthController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public final View getRoot() {
        View view = this.root;
        if (view != null) {
            return view;
        }
        return null;
    }

    public final void setRoot(@NotNull View view) {
        this.root = view;
    }

    public final float getShadeExpansion() {
        return this.shadeExpansion;
    }

    public final void setPanelPullDownMinFraction(float f) {
        this.panelPullDownMinFraction = f;
    }

    @NotNull
    public final DepthAnimation getShadeAnimation() {
        return this.shadeAnimation;
    }

    @NotNull
    public final DepthAnimation getBrightnessMirrorSpring() {
        return this.brightnessMirrorSpring;
    }

    public final void setBrightnessMirrorVisible(boolean z) {
        this.brightnessMirrorVisible = z;
        DepthAnimation.animateTo$default(this.brightnessMirrorSpring, z ? (int) this.blurUtils.blurRadiusOfRatio(1.0f) : 0, (View) null, 2, (Object) null);
    }

    public final float getQsPanelExpansion() {
        return this.qsPanelExpansion;
    }

    public final void setQsPanelExpansion(float f) {
        if (Float.isNaN(f)) {
            Log.w("DepthController", "Invalid qs expansion");
            return;
        }
        if (!(this.qsPanelExpansion == f)) {
            this.qsPanelExpansion = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final float getTransitionToFullShadeProgress() {
        return this.transitionToFullShadeProgress;
    }

    public final void setTransitionToFullShadeProgress(float f) {
        if (!(this.transitionToFullShadeProgress == f)) {
            this.transitionToFullShadeProgress = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final boolean getBlursDisabledForAppLaunch() {
        return this.blursDisabledForAppLaunch;
    }

    public final void setBlursDisabledForAppLaunch(boolean z) {
        if (this.blursDisabledForAppLaunch != z) {
            this.blursDisabledForAppLaunch = z;
            boolean z2 = true;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
            if (this.shadeExpansion == 0.0f) {
                if (this.shadeAnimation.getRadius() != 0.0f) {
                    z2 = false;
                }
                if (z2) {
                    return;
                }
            }
            if (z) {
                DepthAnimation.animateTo$default(this.shadeAnimation, 0, (View) null, 2, (Object) null);
                this.shadeAnimation.finishIfRunning();
            }
        }
    }

    public final boolean getBlursDisabledForUnlock() {
        return this.blursDisabledForUnlock;
    }

    public final void setBlursDisabledForUnlock(boolean z) {
        if (this.blursDisabledForUnlock != z) {
            this.blursDisabledForUnlock = z;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final void setScrimsVisible(boolean z) {
        if (this.scrimsVisible != z) {
            this.scrimsVisible = z;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final void setWakeAndUnlockBlurRadius(float f) {
        if (!(this.wakeAndUnlockBlurRadius == f)) {
            this.wakeAndUnlockBlurRadius = f;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
        }
    }

    public final void updateResources() {
        this.inSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources());
    }

    public final void addListener(@NotNull DepthListener depthListener) {
        this.listeners.add(depthListener);
    }

    public final void removeListener(@NotNull DepthListener depthListener) {
        this.listeners.remove(depthListener);
    }

    public void onPanelExpansionChanged(@NotNull PanelExpansionChangeEvent panelExpansionChangeEvent) {
        float fraction = panelExpansionChangeEvent.getFraction();
        boolean tracking = panelExpansionChangeEvent.getTracking();
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        float f = this.panelPullDownMinFraction;
        float f2 = 1.0f;
        float saturate = MathUtils.saturate((fraction - f) / (1.0f - f));
        if (!(this.shadeExpansion == saturate) || this.prevTracking != tracking) {
            long j = this.prevTimestamp;
            if (j < 0) {
                this.prevTimestamp = elapsedRealtimeNanos;
            } else {
                f2 = MathUtils.constrain((float) (((double) (elapsedRealtimeNanos - j)) / 1.0E9d), 1.0E-5f, 1.0f);
            }
            float f3 = saturate - this.shadeExpansion;
            int signum = (int) Math.signum(f3);
            float constrain = MathUtils.constrain((f3 * 100.0f) / f2, -3000.0f, 3000.0f);
            updateShadeAnimationBlur(saturate, tracking, constrain, signum);
            this.prevShadeDirection = signum;
            this.prevShadeVelocity = constrain;
            this.shadeExpansion = saturate;
            this.prevTracking = tracking;
            this.prevTimestamp = elapsedRealtimeNanos;
            scheduleUpdate$default(this, (View) null, 1, (Object) null);
            return;
        }
        this.prevTimestamp = elapsedRealtimeNanos;
    }

    public final void updateShadeAnimationBlur(float f, boolean z, float f2, int i) {
        if (!shouldApplyShadeBlur()) {
            animateBlur(false, 0.0f);
            this.isClosed = true;
            this.isOpen = false;
        } else if (f > 0.0f) {
            if (this.isClosed) {
                animateBlur(true, f2);
                this.isClosed = false;
            }
            if (z && !this.isBlurred) {
                animateBlur(true, 0.0f);
            }
            if (!z && i < 0 && this.isBlurred) {
                animateBlur(false, f2);
            }
            if (!(f == 1.0f)) {
                this.isOpen = false;
            } else if (!this.isOpen) {
                this.isOpen = true;
                if (!this.isBlurred) {
                    animateBlur(true, f2);
                }
            }
        } else if (!this.isClosed) {
            this.isClosed = true;
            if (this.isBlurred) {
                animateBlur(false, f2);
            }
        }
    }

    public final void animateBlur(boolean z, float f) {
        this.isBlurred = z;
        float f2 = (!z || !shouldApplyShadeBlur()) ? 0.0f : 1.0f;
        this.shadeAnimation.setStartVelocity(f);
        DepthAnimation.animateTo$default(this.shadeAnimation, (int) this.blurUtils.blurRadiusOfRatio(f2), (View) null, 2, (Object) null);
    }

    public static /* synthetic */ void scheduleUpdate$default(NotificationShadeDepthController notificationShadeDepthController, View view, int i, Object obj) {
        if ((i & 1) != 0) {
            view = null;
        }
        notificationShadeDepthController.scheduleUpdate(view);
    }

    public final void scheduleUpdate(View view) {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            this.blurRoot = view;
            this.choreographer.postFrameCallback(this.updateBlurCallback);
        }
    }

    public final boolean shouldApplyShadeBlur() {
        int state = this.statusBarStateController.getState();
        return (state == 0 || state == 2) && !this.keyguardStateController.isKeyguardFadingAway();
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("StatusBarWindowBlurController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeExpansion: ", Float.valueOf(getShadeExpansion())));
        indentingPrintWriter.println(Intrinsics.stringPlus("shouldApplyShadeBlur: ", Boolean.valueOf(shouldApplyShadeBlur())));
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeAnimation: ", Float.valueOf(getShadeAnimation().getRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("brightnessMirrorRadius: ", Float.valueOf(getBrightnessMirrorSpring().getRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("wakeAndUnlockBlur: ", Float.valueOf(this.wakeAndUnlockBlurRadius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("blursDisabledForAppLaunch: ", Boolean.valueOf(getBlursDisabledForAppLaunch())));
        indentingPrintWriter.println(Intrinsics.stringPlus("qsPanelExpansion: ", Float.valueOf(getQsPanelExpansion())));
        indentingPrintWriter.println(Intrinsics.stringPlus("transitionToFullShadeProgress: ", Float.valueOf(getTransitionToFullShadeProgress())));
        indentingPrintWriter.println(Intrinsics.stringPlus("lastAppliedBlur: ", Integer.valueOf(this.lastAppliedBlur)));
    }

    /* compiled from: NotificationShadeDepthController.kt */
    public final class DepthAnimation {
        public int pendingRadius = -1;
        public float radius;
        @NotNull
        public SpringAnimation springAnimation;
        @Nullable
        public View view;

        public DepthAnimation() {
            SpringAnimation springAnimation2 = new SpringAnimation(this, new NotificationShadeDepthController$DepthAnimation$springAnimation$1(this, NotificationShadeDepthController.this));
            this.springAnimation = springAnimation2;
            springAnimation2.setSpring(new SpringForce(0.0f));
            this.springAnimation.getSpring().setDampingRatio(1.0f);
            this.springAnimation.getSpring().setStiffness(10000.0f);
            this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener(this) {
                public final /* synthetic */ DepthAnimation this$0;

                {
                    this.this$0 = r1;
                }

                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    this.this$0.pendingRadius = -1;
                }
            });
        }

        public final float getRadius() {
            return this.radius;
        }

        public final void setRadius(float f) {
            this.radius = f;
        }

        public final float getRatio() {
            return NotificationShadeDepthController.this.blurUtils.ratioOfBlurRadius(this.radius);
        }

        public static /* synthetic */ void animateTo$default(DepthAnimation depthAnimation, int i, View view2, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                view2 = null;
            }
            depthAnimation.animateTo(i, view2);
        }

        public final void animateTo(int i, @Nullable View view2) {
            if (this.pendingRadius != i || !Intrinsics.areEqual((Object) this.view, (Object) view2)) {
                this.view = view2;
                this.pendingRadius = i;
                this.springAnimation.animateToFinalPosition((float) i);
            }
        }

        public final void finishIfRunning() {
            if (this.springAnimation.isRunning()) {
                this.springAnimation.skipToEnd();
            }
        }

        public final void setStiffness(float f) {
            this.springAnimation.getSpring().setStiffness(f);
        }

        public final void setDampingRatio(float f) {
            this.springAnimation.getSpring().setDampingRatio(f);
        }

        public final void setStartVelocity(float f) {
            this.springAnimation.setStartVelocity(f);
        }
    }
}
