package com.android.systemui.qs;

import org.jetbrains.annotations.NotNull;

/* compiled from: QSSquishinessController.kt */
public final class QSSquishinessController {
    @NotNull
    public final QSAnimator qsAnimator;
    @NotNull
    public final QSPanelController qsPanelController;
    @NotNull
    public final QuickQSPanelController quickQSPanelController;
    public float squishiness = 1.0f;

    public QSSquishinessController(@NotNull QSAnimator qSAnimator, @NotNull QSPanelController qSPanelController, @NotNull QuickQSPanelController quickQSPanelController2) {
        this.qsAnimator = qSAnimator;
        this.qsPanelController = qSPanelController;
        this.quickQSPanelController = quickQSPanelController2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
        if ((r6 == 1.0f) == false) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0032, code lost:
        if (r2 != false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0034, code lost:
        r5.qsAnimator.requestAnimatorUpdate();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setSquishiness(float r6) {
        /*
            r5 = this;
            float r0 = r5.squishiness
            int r1 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x000a
            r1 = r2
            goto L_0x000b
        L_0x000a:
            r1 = r3
        L_0x000b:
            if (r1 == 0) goto L_0x000e
            return
        L_0x000e:
            r1 = 1065353216(0x3f800000, float:1.0)
            int r4 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r4 != 0) goto L_0x0016
            r4 = r2
            goto L_0x0017
        L_0x0016:
            r4 = r3
        L_0x0017:
            if (r4 != 0) goto L_0x0022
            int r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x001f
            r1 = r2
            goto L_0x0020
        L_0x001f:
            r1 = r3
        L_0x0020:
            if (r1 != 0) goto L_0x0034
        L_0x0022:
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L_0x0029
            r0 = r2
            goto L_0x002a
        L_0x0029:
            r0 = r3
        L_0x002a:
            if (r0 != 0) goto L_0x0039
            int r0 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r0 != 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r2 = r3
        L_0x0032:
            if (r2 == 0) goto L_0x0039
        L_0x0034:
            com.android.systemui.qs.QSAnimator r0 = r5.qsAnimator
            r0.requestAnimatorUpdate()
        L_0x0039:
            r5.squishiness = r6
            r5.updateSquishiness()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSSquishinessController.setSquishiness(float):void");
    }

    public final void updateSquishiness() {
        this.qsPanelController.setSquishinessFraction(this.squishiness);
        this.quickQSPanelController.setSquishinessFraction(this.squishiness);
    }
}
