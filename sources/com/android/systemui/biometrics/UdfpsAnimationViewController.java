package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.RectF;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsAnimationView;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.util.ViewController;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsAnimationViewController.kt */
public abstract class UdfpsAnimationViewController<T extends UdfpsAnimationView> extends ViewController<T> implements Dumpable {
    @Nullable
    public ValueAnimator dialogAlphaAnimator;
    @NotNull
    public final SystemUIDialogManager.Listener dialogListener = new UdfpsAnimationViewController$dialogListener$1(this);
    @NotNull
    public final SystemUIDialogManager dialogManager;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final String dumpTag;
    public boolean notificationShadeVisible;
    public final int paddingX;
    public final int paddingY;
    @NotNull
    public final PanelExpansionListener panelExpansionListener;
    @NotNull
    public final PanelExpansionStateManager panelExpansionStateManager;
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final PointF touchTranslation;

    public void doAnnounceForAccessibility(@NotNull String str) {
    }

    @NotNull
    public abstract String getTag();

    public boolean listenForTouchesOutsideView() {
        return false;
    }

    public void onTouchOutsideView() {
    }

    @NotNull
    public final StatusBarStateController getStatusBarStateController() {
        return this.statusBarStateController;
    }

    @NotNull
    public final PanelExpansionStateManager getPanelExpansionStateManager() {
        return this.panelExpansionStateManager;
    }

    public UdfpsAnimationViewController(@NotNull T t, @NotNull StatusBarStateController statusBarStateController2, @NotNull PanelExpansionStateManager panelExpansionStateManager2, @NotNull SystemUIDialogManager systemUIDialogManager, @NotNull DumpManager dumpManager2) {
        super(t);
        this.statusBarStateController = statusBarStateController2;
        this.panelExpansionStateManager = panelExpansionStateManager2;
        this.dialogManager = systemUIDialogManager;
        this.dumpManager = dumpManager2;
        this.panelExpansionListener = new UdfpsAnimationViewController$panelExpansionListener$1(this, t);
        this.touchTranslation = new PointF(0.0f, 0.0f);
        this.dumpTag = getTag() + " (" + this + ')';
    }

    public final T getView() {
        T t = this.mView;
        Intrinsics.checkNotNull(t);
        return (UdfpsAnimationView) t;
    }

    public final boolean getNotificationShadeVisible() {
        return this.notificationShadeVisible;
    }

    public final void setNotificationShadeVisible(boolean z) {
        this.notificationShadeVisible = z;
    }

    @NotNull
    public PointF getTouchTranslation() {
        return this.touchTranslation;
    }

    public int getPaddingX() {
        return this.paddingX;
    }

    public int getPaddingY() {
        return this.paddingY;
    }

    public void updateAlpha() {
        getView().updateAlpha();
    }

    public final void runDialogAlphaAnimator() {
        boolean shouldHideAffordance = this.dialogManager.shouldHideAffordance();
        ValueAnimator valueAnimator = this.dialogAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float[] fArr = new float[2];
        fArr[0] = ((float) getView().calculateAlpha()) / 255.0f;
        fArr[1] = shouldHideAffordance ? 0.0f : 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setDuration(shouldHideAffordance ? 83 : 200);
        ofFloat.setInterpolator(shouldHideAffordance ? Interpolators.LINEAR : Interpolators.ALPHA_IN);
        ofFloat.addUpdateListener(new UdfpsAnimationViewController$runDialogAlphaAnimator$1$1(this));
        ofFloat.start();
        this.dialogAlphaAnimator = ofFloat;
    }

    public void onViewAttached() {
        this.panelExpansionStateManager.addExpansionListener(this.panelExpansionListener);
        this.dialogManager.registerListener(this.dialogListener);
        this.dumpManager.registerDumpable(this.dumpTag, this);
    }

    public void onViewDetached() {
        this.panelExpansionStateManager.removeExpansionListener(this.panelExpansionListener);
        this.dialogManager.unregisterListener(this.dialogListener);
        this.dumpManager.unregisterDumpable(this.dumpTag);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("mNotificationShadeVisible=", Boolean.valueOf(this.notificationShadeVisible)));
        printWriter.println(Intrinsics.stringPlus("shouldPauseAuth()=", Boolean.valueOf(shouldPauseAuth())));
        printWriter.println(Intrinsics.stringPlus("isPauseAuth=", Boolean.valueOf(getView().isPauseAuth())));
        printWriter.println(Intrinsics.stringPlus("dialogSuggestedAlpha=", Float.valueOf(getView().getDialogSuggestedAlpha())));
    }

    public boolean shouldPauseAuth() {
        return this.notificationShadeVisible || this.dialogManager.shouldHideAffordance();
    }

    public final void updatePauseAuth() {
        if (getView().setPauseAuth(shouldPauseAuth())) {
            getView().postInvalidate();
        }
    }

    public final void onSensorRectUpdated(@NotNull RectF rectF) {
        getView().onSensorRectUpdated(rectF);
    }

    public final void dozeTimeTick() {
        if (getView().dozeTimeTick()) {
            getView().postInvalidate();
        }
    }

    public final void onIlluminationStarting() {
        getView().onIlluminationStarting();
        getView().postInvalidate();
    }

    public final void onIlluminationStopped() {
        getView().onIlluminationStopped();
        getView().postInvalidate();
    }
}
