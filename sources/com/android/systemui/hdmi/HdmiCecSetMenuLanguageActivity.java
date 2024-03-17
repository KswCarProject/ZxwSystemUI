package com.android.systemui.hdmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.tv.TvBottomSheetActivity;

public class HdmiCecSetMenuLanguageActivity extends TvBottomSheetActivity implements View.OnClickListener {
    public static final String TAG = HdmiCecSetMenuLanguageActivity.class.getSimpleName();
    public final HdmiCecSetMenuLanguageHelper mHdmiCecSetMenuLanguageHelper;

    public HdmiCecSetMenuLanguageActivity(HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper) {
        this.mHdmiCecSetMenuLanguageHelper = hdmiCecSetMenuLanguageHelper;
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        this.mHdmiCecSetMenuLanguageHelper.setLocale(getIntent().getStringExtra("android.hardware.hdmi.extra.LOCALE"));
        if (this.mHdmiCecSetMenuLanguageHelper.isLocaleDenylisted()) {
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        initUI(getString(R$string.hdmi_cec_set_menu_language_title, new Object[]{this.mHdmiCecSetMenuLanguageHelper.getLocale().getDisplayLanguage()}), getString(R$string.hdmi_cec_set_menu_language_description));
    }

    public void onClick(View view) {
        if (view.getId() == R$id.bottom_sheet_positive_button) {
            this.mHdmiCecSetMenuLanguageHelper.acceptLocale();
        } else {
            this.mHdmiCecSetMenuLanguageHelper.declineLocale();
        }
        finish();
    }

    public void initUI(CharSequence charSequence, CharSequence charSequence2) {
        Button button = (Button) findViewById(R$id.bottom_sheet_positive_button);
        Button button2 = (Button) findViewById(R$id.bottom_sheet_negative_button);
        ((TextView) findViewById(R$id.bottom_sheet_title)).setText(charSequence);
        ((TextView) findViewById(R$id.bottom_sheet_body)).setText(charSequence2);
        ((ImageView) findViewById(R$id.bottom_sheet_icon)).setImageResource(17302848);
        ((ImageView) findViewById(R$id.bottom_sheet_second_icon)).setVisibility(8);
        button.setText(R$string.hdmi_cec_set_menu_language_accept);
        button.setOnClickListener(this);
        button2.setText(R$string.hdmi_cec_set_menu_language_decline);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }
}
