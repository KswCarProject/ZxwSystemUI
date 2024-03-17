package com.android.systemui.unfold;

import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LinearLightRevealEffect;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
public final class UnfoldLightRevealOverlayAnimation {
    @NotNull
    public final Executor backgroundExecutor;
    @NotNull
    public final Context context;
    public int currentRotation;
    @NotNull
    public final DeviceStateManager deviceStateManager;
    @NotNull
    public final Optional<DisplayAreaHelper> displayAreaHelper;
    @NotNull
    public final DisplayManager displayManager;
    @NotNull
    public final Executor executor;
    public boolean isFolded;
    public boolean isUnfoldHandled = true;
    public SurfaceControl overlayContainer;
    @Nullable
    public SurfaceControlViewHost root;
    @NotNull
    public final RotationWatcher rotationWatcher = new RotationWatcher();
    @Nullable
    public LightRevealScrim scrimView;
    @NotNull
    public final TransitionListener transitionListener = new TransitionListener();
    @NotNull
    public final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    public DisplayInfo unfoldedDisplayInfo;
    @NotNull
    public final IWindowManager windowManagerInterface;
    public WindowlessWindowManager wwm;

    public UnfoldLightRevealOverlayAnimation(@NotNull Context context2, @NotNull DeviceStateManager deviceStateManager2, @NotNull DisplayManager displayManager2, @NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, @NotNull Optional<DisplayAreaHelper> optional, @NotNull Executor executor2, @NotNull Executor executor3, @NotNull IWindowManager iWindowManager) {
        this.context = context2;
        this.deviceStateManager = deviceStateManager2;
        this.displayManager = displayManager2;
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider2;
        this.displayAreaHelper = optional;
        this.executor = executor2;
        this.backgroundExecutor = executor3;
        this.windowManagerInterface = iWindowManager;
        Display display = context2.getDisplay();
        Intrinsics.checkNotNull(display);
        this.currentRotation = display.getRotation();
    }

    public final void init() {
        this.deviceStateManager.registerCallback(this.executor, new FoldListener());
        this.unfoldTransitionProgressProvider.addCallback(this.transitionListener);
        this.windowManagerInterface.watchRotation(this.rotationWatcher, this.context.getDisplay().getDisplayId());
        this.displayAreaHelper.get().attachToRootDisplayArea(0, new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("unfold-overlay-container"), new UnfoldLightRevealOverlayAnimation$init$1(this));
        this.unfoldedDisplayInfo = getUnfoldedDisplayInfo();
    }

    public final void onScreenTurningOn(@NotNull Runnable runnable) {
        Trace.beginSection("UnfoldLightRevealOverlayAnimation#onScreenTurningOn");
        try {
            if (this.isFolded || this.isUnfoldHandled || !ValueAnimator.areAnimatorsEnabled()) {
                ensureOverlayRemoved();
                runnable.run();
            } else {
                addView(runnable);
                this.isUnfoldHandled = true;
            }
        } finally {
            Trace.endSection();
        }
    }

    public static /* synthetic */ void addView$default(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        unfoldLightRevealOverlayAnimation.addView(runnable);
    }

    public final void addView(Runnable runnable) {
        if (this.wwm != null) {
            ensureOverlayRemoved();
            Context context2 = this.context;
            Display display = context2.getDisplay();
            Intrinsics.checkNotNull(display);
            WindowlessWindowManager windowlessWindowManager = this.wwm;
            if (windowlessWindowManager == null) {
                windowlessWindowManager = null;
            }
            SurfaceControlViewHost surfaceControlViewHost = new SurfaceControlViewHost(context2, display, windowlessWindowManager, false);
            LightRevealScrim lightRevealScrim = new LightRevealScrim(this.context, (AttributeSet) null);
            lightRevealScrim.setRevealEffect(createLightRevealEffect());
            lightRevealScrim.setScrimOpaqueChangedListener(UnfoldLightRevealOverlayAnimation$addView$newView$1$1.INSTANCE);
            lightRevealScrim.setRevealAmount(0.0f);
            WindowManager.LayoutParams layoutParams = getLayoutParams();
            surfaceControlViewHost.setView(lightRevealScrim, layoutParams);
            if (runnable != null) {
                Trace.beginAsyncSection("UnfoldLightRevealOverlayAnimation#relayout", 0);
                surfaceControlViewHost.relayout(layoutParams, new UnfoldLightRevealOverlayAnimation$addView$2$1(this, runnable));
            }
            this.scrimView = lightRevealScrim;
            this.root = surfaceControlViewHost;
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public final WindowManager.LayoutParams getLayoutParams() {
        int i;
        int i2;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int i3 = this.currentRotation;
        boolean z = i3 == 0 || i3 == 2;
        DisplayInfo displayInfo = null;
        DisplayInfo displayInfo2 = this.unfoldedDisplayInfo;
        if (z) {
            if (displayInfo2 == null) {
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalHeight();
        } else {
            if (displayInfo2 == null) {
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalWidth();
        }
        layoutParams.height = i;
        if (z) {
            DisplayInfo displayInfo3 = this.unfoldedDisplayInfo;
            if (displayInfo3 != null) {
                displayInfo = displayInfo3;
            }
            i2 = displayInfo.getNaturalWidth();
        } else {
            DisplayInfo displayInfo4 = this.unfoldedDisplayInfo;
            if (displayInfo4 != null) {
                displayInfo = displayInfo4;
            }
            i2 = displayInfo.getNaturalHeight();
        }
        layoutParams.width = i2;
        layoutParams.format = -3;
        layoutParams.type = 2026;
        layoutParams.setTitle("Unfold Light Reveal Animation");
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.flags = 8;
        layoutParams.setTrustedOverlay();
        layoutParams.packageName = this.context.getOpPackageName();
        return layoutParams;
    }

    public final LightRevealEffect createLightRevealEffect() {
        int i = this.currentRotation;
        return new LinearLightRevealEffect(i == 0 || i == 2);
    }

    public final void ensureOverlayRemoved() {
        SurfaceControlViewHost surfaceControlViewHost = this.root;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
        }
        this.root = null;
        this.scrimView = null;
    }

    public final DisplayInfo getUnfoldedDisplayInfo() {
        Object obj;
        Iterator it = SequencesKt___SequencesKt.filter(SequencesKt___SequencesKt.map(ArraysKt___ArraysKt.asSequence(this.displayManager.getDisplays()), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1.INSTANCE), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2.INSTANCE).iterator();
        if (!it.hasNext()) {
            obj = null;
        } else {
            Object next = it.next();
            if (!it.hasNext()) {
                obj = next;
            } else {
                int naturalWidth = ((DisplayInfo) next).getNaturalWidth();
                do {
                    Object next2 = it.next();
                    int naturalWidth2 = ((DisplayInfo) next2).getNaturalWidth();
                    if (naturalWidth < naturalWidth2) {
                        next = next2;
                        naturalWidth = naturalWidth2;
                    }
                } while (it.hasNext());
            }
            obj = next;
        }
        Intrinsics.checkNotNull(obj);
        return (DisplayInfo) obj;
    }

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    public final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionProgress(float f) {
            LightRevealScrim access$getScrimView$p = UnfoldLightRevealOverlayAnimation.this.scrimView;
            if (access$getScrimView$p != null) {
                access$getScrimView$p.setRevealAmount(f);
            }
        }

        public void onTransitionFinished() {
            UnfoldLightRevealOverlayAnimation.this.ensureOverlayRemoved();
        }

        public void onTransitionStarted() {
            if (UnfoldLightRevealOverlayAnimation.this.scrimView == null) {
                UnfoldLightRevealOverlayAnimation.addView$default(UnfoldLightRevealOverlayAnimation.this, (Runnable) null, 1, (Object) null);
            }
            InputManager.getInstance().cancelCurrentTouch();
        }
    }

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    public final class RotationWatcher extends IRotationWatcher.Stub {
        public RotationWatcher() {
        }

        public void onRotationChanged(int i) {
            UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = UnfoldLightRevealOverlayAnimation.this;
            Trace.beginSection("UnfoldLightRevealOverlayAnimation#onRotationChanged");
            try {
                if (unfoldLightRevealOverlayAnimation.currentRotation != i) {
                    unfoldLightRevealOverlayAnimation.currentRotation = i;
                    LightRevealScrim access$getScrimView$p = unfoldLightRevealOverlayAnimation.scrimView;
                    if (access$getScrimView$p != null) {
                        access$getScrimView$p.setRevealEffect(unfoldLightRevealOverlayAnimation.createLightRevealEffect());
                    }
                    SurfaceControlViewHost access$getRoot$p = unfoldLightRevealOverlayAnimation.root;
                    if (access$getRoot$p != null) {
                        access$getRoot$p.relayout(unfoldLightRevealOverlayAnimation.getLayoutParams());
                    }
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
    }

    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    public final class FoldListener extends DeviceStateManager.FoldStateListener {
        public FoldListener() {
            super(UnfoldLightRevealOverlayAnimation.this.context, new Consumer(UnfoldLightRevealOverlayAnimation.this) {
                public final void accept(Boolean bool) {
                    if (bool.booleanValue()) {
                        r3.ensureOverlayRemoved();
                        r3.isUnfoldHandled = false;
                    }
                    r3.isFolded = bool.booleanValue();
                }
            });
        }
    }
}
