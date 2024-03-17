package com.android.systemui.usb.tv;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.tv.TvBottomSheetActivity;
import com.android.systemui.usb.UsbDialogHelper;

public abstract class TvUsbDialogActivity extends TvBottomSheetActivity implements View.OnClickListener {
    public static final String TAG = TvUsbDialogActivity.class.getSimpleName();
    public UsbDialogHelper mDialogHelper;

    public abstract void onConfirm();

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        try {
            this.mDialogHelper = new UsbDialogHelper(getApplicationContext(), getIntent());
        } catch (IllegalStateException e) {
            Log.e(TAG, "unable to initialize", e);
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        this.mDialogHelper.registerUsbDisconnectedReceiver(this);
    }

    public void onPause() {
        UsbDialogHelper usbDialogHelper = this.mDialogHelper;
        if (usbDialogHelper != null) {
            usbDialogHelper.unregisterUsbDisconnectedReceiver(this);
        }
        super.onPause();
    }

    public void onClick(View view) {
        if (view.getId() == R$id.bottom_sheet_positive_button) {
            onConfirm();
        } else {
            finish();
        }
    }

    public void initUI(CharSequence charSequence, CharSequence charSequence2) {
        Button button = (Button) findViewById(R$id.bottom_sheet_positive_button);
        Button button2 = (Button) findViewById(R$id.bottom_sheet_negative_button);
        ((TextView) findViewById(R$id.bottom_sheet_title)).setText(charSequence);
        ((TextView) findViewById(R$id.bottom_sheet_body)).setText(charSequence2);
        ((ImageView) findViewById(R$id.bottom_sheet_icon)).setImageResource(17302882);
        ((ImageView) findViewById(R$id.bottom_sheet_second_icon)).setVisibility(8);
        button.setText(17039370);
        button.setOnClickListener(this);
        button2.setText(17039360);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }
}
