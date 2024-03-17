package com.android.wm.shell.compatui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.wm.shell.R;

class CompatUILayout extends LinearLayout {
    public CompatUIWindowManager mWindowManager;

    public CompatUILayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void inject(CompatUIWindowManager compatUIWindowManager) {
        this.mWindowManager = compatUIWindowManager;
    }

    public void updateCameraTreatmentButton(int i) {
        int i2;
        int i3;
        if (i == 1) {
            i2 = R.drawable.camera_compat_treatment_suggested_ripple;
        } else {
            i2 = R.drawable.camera_compat_treatment_applied_ripple;
        }
        if (i == 1) {
            i3 = R.string.camera_compat_treatment_suggested_button_description;
        } else {
            i3 = R.string.camera_compat_treatment_applied_button_description;
        }
        ImageButton imageButton = (ImageButton) findViewById(R.id.camera_compat_treatment_button);
        imageButton.setImageResource(i2);
        imageButton.setContentDescription(getResources().getString(i3));
        ((TextView) ((LinearLayout) findViewById(R.id.camera_compat_hint)).findViewById(R.id.compat_mode_hint_text)).setText(i3);
    }

    public void setSizeCompatHintVisibility(boolean z) {
        setViewVisibility(R.id.size_compat_hint, z);
    }

    public void setCameraCompatHintVisibility(boolean z) {
        setViewVisibility(R.id.camera_compat_hint, z);
    }

    public void setRestartButtonVisibility(boolean z) {
        setViewVisibility(R.id.size_compat_restart_button, z);
        if (!z) {
            setSizeCompatHintVisibility(false);
        }
    }

    public void setCameraControlVisibility(boolean z) {
        setViewVisibility(R.id.camera_compat_control, z);
        if (!z) {
            setCameraCompatHintVisibility(false);
        }
    }

    public final void setViewVisibility(int i, boolean z) {
        View findViewById = findViewById(i);
        int i2 = z ? 0 : 8;
        if (findViewById.getVisibility() != i2) {
            findViewById.setVisibility(i2);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mWindowManager.relayout();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        ImageButton imageButton = (ImageButton) findViewById(R.id.size_compat_restart_button);
        imageButton.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda0(this));
        imageButton.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda1(this));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.size_compat_hint);
        ((TextView) linearLayout.findViewById(R.id.compat_mode_hint_text)).setText(R.string.restart_button_description);
        linearLayout.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda2(this));
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.camera_compat_treatment_button);
        imageButton2.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda3(this));
        imageButton2.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda4(this));
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.camera_compat_dismiss_button);
        imageButton3.setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda5(this));
        imageButton3.setOnLongClickListener(new CompatUILayout$$ExternalSyntheticLambda6(this));
        ((LinearLayout) findViewById(R.id.camera_compat_hint)).setOnClickListener(new CompatUILayout$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$0(View view) {
        this.mWindowManager.onRestartButtonClicked();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$1(View view) {
        this.mWindowManager.onRestartButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$2(View view) {
        setSizeCompatHintVisibility(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$3(View view) {
        this.mWindowManager.onCameraTreatmentButtonClicked();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$4(View view) {
        this.mWindowManager.onCameraButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$5(View view) {
        this.mWindowManager.onCameraDismissButtonClicked();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFinishInflate$6(View view) {
        this.mWindowManager.onCameraButtonLongClicked();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$7(View view) {
        setCameraCompatHintVisibility(false);
    }
}
