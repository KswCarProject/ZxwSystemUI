package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthBiometricFingerprintView.kt */
public class AuthBiometricFingerprintView extends AuthBiometricView {
    public boolean isUdfps;
    @Nullable
    public UdfpsDialogMeasureAdapter udfpsAdapter;

    public int getDelayAfterAuthenticatedDurationMs() {
        return 0;
    }

    public int getStateForAfterError() {
        return 2;
    }

    public boolean supportsSmallDialog() {
        return false;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AuthBiometricFingerprintView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    public AuthBiometricFingerprintView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final boolean isUdfps() {
        return this.isUdfps;
    }

    public final void setSensorProperties(@NotNull FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        boolean isAnyUdfpsType = fingerprintSensorPropertiesInternal.isAnyUdfpsType();
        this.isUdfps = isAnyUdfpsType;
        this.udfpsAdapter = isAnyUdfpsType ? new UdfpsDialogMeasureAdapter(this, fingerprintSensorPropertiesInternal) : null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r1 = r1.onMeasureInternal(r2, r3, r0);
     */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.systemui.biometrics.AuthDialog.LayoutParams onMeasureInternal(int r2, int r3) {
        /*
            r1 = this;
            com.android.systemui.biometrics.AuthDialog$LayoutParams r0 = super.onMeasureInternal(r2, r3)
            com.android.systemui.biometrics.UdfpsDialogMeasureAdapter r1 = r1.udfpsAdapter
            if (r1 != 0) goto L_0x0009
            goto L_0x0011
        L_0x0009:
            com.android.systemui.biometrics.AuthDialog$LayoutParams r1 = r1.onMeasureInternal(r2, r3, r0)
            if (r1 != 0) goto L_0x0010
            goto L_0x0011
        L_0x0010:
            r0 = r1
        L_0x0011:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthBiometricFingerprintView.onMeasureInternal(int, int):com.android.systemui.biometrics.AuthDialog$LayoutParams");
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        UdfpsDialogMeasureAdapter udfpsDialogMeasureAdapter = this.udfpsAdapter;
        if (udfpsDialogMeasureAdapter != null) {
            int bottomSpacerHeight = udfpsDialogMeasureAdapter.getBottomSpacerHeight();
            Log.w("AuthBiometricFingerprintView", Intrinsics.stringPlus("bottomSpacerHeight: ", Integer.valueOf(bottomSpacerHeight)));
            if (bottomSpacerHeight < 0) {
                View findViewById = findViewById(R$id.biometric_icon_frame);
                Intrinsics.checkNotNull(findViewById);
                float f = -((float) bottomSpacerHeight);
                ((FrameLayout) findViewById).setTranslationY(f);
                View findViewById2 = findViewById(R$id.indicator);
                Intrinsics.checkNotNull(findViewById2);
                ((TextView) findViewById2).setTranslationY(f);
            }
        }
    }

    public void handleResetAfterError() {
        showTouchSensorString();
    }

    public void handleResetAfterHelp() {
        showTouchSensorString();
    }

    @NotNull
    public AuthIconController createIconController() {
        return new AuthBiometricFingerprintIconController(this.mContext, this.mIconView);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showTouchSensorString();
    }

    public final void showTouchSensorString() {
        this.mIndicatorView.setText(R$string.fingerprint_dialog_touch_sensor);
        this.mIndicatorView.setTextColor(this.mTextColorHint);
    }
}
