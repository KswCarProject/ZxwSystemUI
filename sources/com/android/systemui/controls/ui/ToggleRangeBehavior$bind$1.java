package com.android.systemui.controls.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior$bind$1 extends View.AccessibilityDelegate {
    public final /* synthetic */ ToggleRangeBehavior this$0;

    public boolean onRequestSendAccessibilityEvent(@NotNull ViewGroup viewGroup, @NotNull View view, @NotNull AccessibilityEvent accessibilityEvent) {
        return true;
    }

    public ToggleRangeBehavior$bind$1(ToggleRangeBehavior toggleRangeBehavior) {
        this.this$0 = toggleRangeBehavior;
    }

    public void onInitializeAccessibilityNodeInfo(@NotNull View view, @NotNull AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        int i = 0;
        float access$levelToRangeValue = this.this$0.levelToRangeValue(0);
        ToggleRangeBehavior toggleRangeBehavior = this.this$0;
        float access$levelToRangeValue2 = toggleRangeBehavior.levelToRangeValue(toggleRangeBehavior.getClipLayer().getLevel());
        float access$levelToRangeValue3 = this.this$0.levelToRangeValue(10000);
        double stepValue = (double) this.this$0.getRangeTemplate().getStepValue();
        if (stepValue == Math.floor(stepValue)) {
            i = 1;
        }
        int i2 = i ^ 1;
        if (this.this$0.isChecked()) {
            accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(i2, access$levelToRangeValue, access$levelToRangeValue3, access$levelToRangeValue2));
        }
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityAction(@org.jetbrains.annotations.NotNull android.view.View r7, int r8, @org.jetbrains.annotations.Nullable android.os.Bundle r9) {
        /*
            r6 = this;
            r0 = 0
            r1 = 1
            r2 = 16
            if (r8 != r2) goto L_0x0030
            com.android.systemui.controls.ui.ToggleRangeBehavior r2 = r6.this$0
            boolean r2 = r2.isToggleable()
            if (r2 != 0) goto L_0x0010
        L_0x000e:
            r2 = r0
            goto L_0x0075
        L_0x0010:
            com.android.systemui.controls.ui.ToggleRangeBehavior r2 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r2 = r2.getCvh()
            com.android.systemui.controls.ui.ControlActionCoordinator r2 = r2.getControlActionCoordinator()
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r3 = r3.getCvh()
            com.android.systemui.controls.ui.ToggleRangeBehavior r4 = r6.this$0
            java.lang.String r4 = r4.getTemplateId()
            com.android.systemui.controls.ui.ToggleRangeBehavior r5 = r6.this$0
            boolean r5 = r5.isChecked()
            r2.toggle(r3, r4, r5)
            goto L_0x0047
        L_0x0030:
            r2 = 32
            if (r8 != r2) goto L_0x0049
            com.android.systemui.controls.ui.ToggleRangeBehavior r2 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r2 = r2.getCvh()
            com.android.systemui.controls.ui.ControlActionCoordinator r2 = r2.getControlActionCoordinator()
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            com.android.systemui.controls.ui.ControlViewHolder r3 = r3.getCvh()
            r2.longPress(r3)
        L_0x0047:
            r2 = r1
            goto L_0x0075
        L_0x0049:
            android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction r2 = android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS
            int r2 = r2.getId()
            if (r8 != r2) goto L_0x000e
            if (r9 == 0) goto L_0x000e
            java.lang.String r2 = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"
            boolean r3 = r9.containsKey(r2)
            if (r3 != 0) goto L_0x005c
            goto L_0x000e
        L_0x005c:
            float r2 = r9.getFloat(r2)
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            int r2 = r3.rangeToLevelValue(r2)
            com.android.systemui.controls.ui.ToggleRangeBehavior r3 = r6.this$0
            boolean r4 = r3.isChecked()
            r3.updateRange(r2, r4, r1)
            com.android.systemui.controls.ui.ToggleRangeBehavior r2 = r6.this$0
            r2.endUpdateRange()
            goto L_0x0047
        L_0x0075:
            if (r2 != 0) goto L_0x007d
            boolean r6 = super.performAccessibilityAction(r7, r8, r9)
            if (r6 == 0) goto L_0x007e
        L_0x007d:
            r0 = r1
        L_0x007e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ToggleRangeBehavior$bind$1.performAccessibilityAction(android.view.View, int, android.os.Bundle):boolean");
    }
}
