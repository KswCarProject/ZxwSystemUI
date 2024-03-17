package com.android.systemui.sensorprivacy.television;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.tv.TvBottomSheetActivity;

public class TvUnblockSensorActivity extends TvBottomSheetActivity {
    public static final String TAG = "TvUnblockSensorActivity";
    public Button mCancelButton;
    public TextView mContent;
    public ImageView mIcon;
    public Button mPositiveButton;
    public ImageView mSecondIcon;
    public int mSensor = -1;
    public IndividualSensorPrivacyController.Callback mSensorPrivacyCallback;
    public final IndividualSensorPrivacyController mSensorPrivacyController;
    public TextView mTitle;

    public TvUnblockSensorActivity(IndividualSensorPrivacyController individualSensorPrivacyController) {
        this.mSensorPrivacyController = individualSensorPrivacyController;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        if (getIntent().getBooleanExtra(SensorPrivacyManager.EXTRA_ALL_SENSORS, false)) {
            this.mSensor = Integer.MAX_VALUE;
        } else {
            this.mSensor = getIntent().getIntExtra(SensorPrivacyManager.EXTRA_SENSOR, -1);
        }
        if (this.mSensor == -1) {
            Log.v(TAG, "Invalid extras");
            finish();
            return;
        }
        this.mSensorPrivacyCallback = new TvUnblockSensorActivity$$ExternalSyntheticLambda0(this);
        initUI();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(int i, boolean z) {
        if (this.mSensor == Integer.MAX_VALUE && !this.mSensorPrivacyController.isSensorBlocked(2) && !this.mSensorPrivacyController.isSensorBlocked(1)) {
            showToastAndFinish();
        } else if (this.mSensor != i || z) {
            updateUI();
        } else {
            showToastAndFinish();
        }
    }

    public final void showToastAndFinish() {
        int i;
        int i2 = this.mSensor;
        if (i2 == 1) {
            i = R$string.sensor_privacy_mic_unblocked_toast_content;
        } else if (i2 != 2) {
            i = R$string.sensor_privacy_mic_camera_unblocked_toast_content;
        } else {
            i = R$string.sensor_privacy_camera_unblocked_toast_content;
        }
        Toast.makeText(this, i, 0).show();
        finish();
    }

    public final boolean isBlockedByHardwareToggle() {
        int i = this.mSensor;
        if (i != Integer.MAX_VALUE) {
            return this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(i);
        }
        if (this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(2) || this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(1)) {
            return true;
        }
        return false;
    }

    public final void initUI() {
        this.mTitle = (TextView) findViewById(R$id.bottom_sheet_title);
        this.mContent = (TextView) findViewById(R$id.bottom_sheet_body);
        this.mIcon = (ImageView) findViewById(R$id.bottom_sheet_icon);
        this.mSecondIcon = (ImageView) findViewById(R$id.bottom_sheet_second_icon);
        this.mPositiveButton = (Button) findViewById(R$id.bottom_sheet_positive_button);
        Button button = (Button) findViewById(R$id.bottom_sheet_negative_button);
        this.mCancelButton = button;
        button.setText(17039360);
        this.mCancelButton.setOnClickListener(new TvUnblockSensorActivity$$ExternalSyntheticLambda1(this));
        updateUI();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initUI$1(View view) {
        finish();
    }

    public final void updateUI() {
        if (isBlockedByHardwareToggle()) {
            updateUiForHardwareToggle();
        } else {
            updateUiForSoftwareToggle();
        }
    }

    public final void updateUiForHardwareToggle() {
        Resources resources = getResources();
        int i = this.mSensor;
        boolean z = false;
        boolean z2 = (i == 1 || i == Integer.MAX_VALUE) && this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(1);
        int i2 = this.mSensor;
        if ((i2 == 2 || i2 == Integer.MAX_VALUE) && this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(2)) {
            z = true;
        }
        setIconTint(resources.getBoolean(R$bool.config_unblockHwSensorIconEnableTint));
        setIconSize(R$dimen.unblock_hw_sensor_icon_width, R$dimen.unblock_hw_sensor_icon_height);
        if (z2 && z) {
            this.mTitle.setText(R$string.sensor_privacy_start_use_mic_camera_blocked_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_mic_camera_blocked_dialog_content);
            this.mIcon.setImageResource(R$drawable.unblock_hw_sensor_all);
            Drawable drawable = resources.getDrawable(R$drawable.unblock_hw_sensor_all_second, getTheme());
            if (drawable == null) {
                this.mSecondIcon.setVisibility(8);
            } else {
                this.mSecondIcon.setImageDrawable(drawable);
            }
        } else if (z) {
            this.mTitle.setText(R$string.sensor_privacy_start_use_camera_blocked_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_camera_blocked_dialog_content);
            this.mIcon.setImageResource(R$drawable.unblock_hw_sensor_camera);
            this.mSecondIcon.setVisibility(8);
        } else if (z2) {
            this.mTitle.setText(R$string.sensor_privacy_start_use_mic_blocked_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_mic_blocked_dialog_content);
            this.mIcon.setImageResource(R$drawable.unblock_hw_sensor_microphone);
            this.mSecondIcon.setVisibility(8);
        }
        Drawable drawable2 = this.mIcon.getDrawable();
        if (drawable2 instanceof Animatable) {
            ((Animatable) drawable2).start();
        }
        this.mPositiveButton.setVisibility(8);
        this.mCancelButton.setText(17039370);
    }

    public final void updateUiForSoftwareToggle() {
        setIconTint(true);
        int i = R$dimen.bottom_sheet_icon_size;
        setIconSize(i, i);
        int i2 = this.mSensor;
        if (i2 == 1) {
            this.mTitle.setText(R$string.sensor_privacy_start_use_mic_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_mic_dialog_content);
            this.mIcon.setImageResource(17303175);
            this.mSecondIcon.setVisibility(8);
        } else if (i2 != 2) {
            this.mTitle.setText(R$string.sensor_privacy_start_use_mic_camera_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_mic_camera_dialog_content);
            this.mIcon.setImageResource(17303170);
            this.mSecondIcon.setImageResource(17303175);
        } else {
            this.mTitle.setText(R$string.sensor_privacy_start_use_camera_dialog_title);
            this.mContent.setText(R$string.sensor_privacy_start_use_camera_dialog_content);
            this.mIcon.setImageResource(17303170);
            this.mSecondIcon.setVisibility(8);
        }
        this.mPositiveButton.setText(17041477);
        this.mPositiveButton.setOnClickListener(new TvUnblockSensorActivity$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUiForSoftwareToggle$2(View view) {
        int i = this.mSensor;
        if (i == Integer.MAX_VALUE) {
            this.mSensorPrivacyController.setSensorBlocked(5, 2, false);
            this.mSensorPrivacyController.setSensorBlocked(5, 1, false);
            return;
        }
        this.mSensorPrivacyController.setSensorBlocked(5, i, false);
    }

    public final void setIconTint(boolean z) {
        Resources resources = getResources();
        if (z) {
            ColorStateList colorStateList = resources.getColorStateList(R$color.bottom_sheet_icon_color, getTheme());
            this.mIcon.setImageTintList(colorStateList);
            this.mSecondIcon.setImageTintList(colorStateList);
        } else {
            this.mIcon.setImageTintList((ColorStateList) null);
            this.mSecondIcon.setImageTintList((ColorStateList) null);
        }
        this.mIcon.invalidate();
        this.mSecondIcon.invalidate();
    }

    public final void setIconSize(int i, int i2) {
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(i);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(i2);
        this.mIcon.getLayoutParams().width = dimensionPixelSize;
        this.mIcon.getLayoutParams().height = dimensionPixelSize2;
        this.mIcon.invalidate();
        this.mSecondIcon.getLayoutParams().width = dimensionPixelSize;
        this.mSecondIcon.getLayoutParams().height = dimensionPixelSize2;
        this.mSecondIcon.invalidate();
    }

    public void onResume() {
        super.onResume();
        updateUI();
        this.mSensorPrivacyController.addCallback(this.mSensorPrivacyCallback);
    }

    public void onPause() {
        this.mSensorPrivacyController.removeCallback(this.mSensorPrivacyCallback);
        super.onPause();
    }
}
