package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;

public class EmergencyButton extends Button {
    public int mDownX;
    public int mDownY;
    public final EmergencyAffordanceManager mEmergencyAffordanceManager;
    public final boolean mEnableEmergencyCallWhileSimLocked;
    public LockPatternUtils mLockPatternUtils;
    public boolean mLongPressWasDragged;

    public EmergencyButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public EmergencyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEnableEmergencyCallWhileSimLocked = this.mContext.getResources().getBoolean(17891655);
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternUtils = new LockPatternUtils(this.mContext);
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            setOnLongClickListener(new EmergencyButton$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$0(View view) {
        if (this.mLongPressWasDragged || !this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            return false;
        }
        this.mEmergencyAffordanceManager.performEmergencyCall();
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getActionMasked() == 0) {
            this.mDownX = x;
            this.mDownY = y;
            this.mLongPressWasDragged = false;
        } else {
            int abs = Math.abs(x - this.mDownX);
            int abs2 = Math.abs(y - this.mDownY);
            int scaledTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
            if (Math.abs(abs2) > scaledTouchSlop || Math.abs(abs) > scaledTouchSlop) {
                this.mLongPressWasDragged = true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        setTextColor(Utils.getColorAttrDefaultColor(getContext(), 17957103));
        setBackground(getContext().getDrawable(R$drawable.kg_emergency_button_background));
    }

    public boolean performLongClick() {
        return super.performLongClick();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        if (r7 != false) goto L_0x0040;
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0052  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateEmergencyCallButton(boolean r4, boolean r5, boolean r6, boolean r7) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == 0) goto L_0x003f
            if (r4 == 0) goto L_0x0007
            goto L_0x0040
        L_0x0007:
            if (r6 == 0) goto L_0x000c
            boolean r5 = r3.mEnableEmergencyCallWhileSimLocked
            goto L_0x002a
        L_0x000c:
            com.android.internal.widget.LockPatternUtils r5 = r3.mLockPatternUtils
            int r6 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            boolean r5 = r5.isSecure(r6)
            if (r5 != 0) goto L_0x0029
            android.content.Context r5 = r3.mContext
            android.content.res.Resources r5 = r5.getResources()
            int r6 = com.android.systemui.R$bool.config_showEmergencyButton
            boolean r5 = r5.getBoolean(r6)
            if (r5 == 0) goto L_0x0027
            goto L_0x0029
        L_0x0027:
            r5 = r1
            goto L_0x002a
        L_0x0029:
            r5 = r0
        L_0x002a:
            android.content.Context r6 = r3.mContext
            android.content.res.Resources r6 = r6.getResources()
            int r2 = com.android.systemui.R$bool.kg_hide_emgcy_btn_when_oos
            boolean r6 = r6.getBoolean(r2)
            if (r6 == 0) goto L_0x003d
            if (r5 == 0) goto L_0x003f
            if (r7 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003d:
            r0 = r5
            goto L_0x0040
        L_0x003f:
            r0 = r1
        L_0x0040:
            if (r0 == 0) goto L_0x0052
            r3.setVisibility(r1)
            if (r4 == 0) goto L_0x004b
            r4 = 17040635(0x10404fb, float:2.4248144E-38)
            goto L_0x004e
        L_0x004b:
            r4 = 17040608(0x10404e0, float:2.424807E-38)
        L_0x004e:
            r3.setText(r4)
            goto L_0x0057
        L_0x0052:
            r4 = 8
            r3.setVisibility(r4)
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.EmergencyButton.updateEmergencyCallButton(boolean, boolean, boolean, boolean):void");
    }
}
