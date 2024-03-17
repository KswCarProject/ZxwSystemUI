package com.android.systemui.qs.carrier;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.util.Objects;

public class QSCarrier extends LinearLayout {
    public TextView mCarrierText;
    public boolean mIsSingleCarrier;
    public CellSignalState mLastSignalState;
    public View mMobileGroup;
    public ImageView mMobileRoaming;
    public ImageView mMobileSignal;
    public boolean mProviderModelInitialized = false;
    public View mSpacer;

    public QSCarrier(Context context) {
        super(context);
    }

    public QSCarrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mMobileGroup = findViewById(R$id.mobile_combo);
        this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming);
        this.mMobileSignal = (ImageView) findViewById(R$id.mobile_signal);
        this.mCarrierText = (TextView) findViewById(R$id.qs_carrier_text);
        this.mSpacer = findViewById(R$id.spacer);
    }

    public boolean updateState(CellSignalState cellSignalState, boolean z) {
        int i = 0;
        if (Objects.equals(cellSignalState, this.mLastSignalState) && z == this.mIsSingleCarrier) {
            return false;
        }
        this.mLastSignalState = cellSignalState;
        this.mIsSingleCarrier = z;
        boolean z2 = cellSignalState.visible && !z;
        this.mMobileGroup.setVisibility(z2 ? 0 : 8);
        this.mSpacer.setVisibility(z ? 0 : 8);
        if (z2) {
            ImageView imageView = this.mMobileRoaming;
            if (!cellSignalState.roaming) {
                i = 8;
            }
            imageView.setVisibility(i);
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16842806);
            this.mMobileRoaming.setImageTintList(colorAttr);
            this.mMobileSignal.setImageTintList(colorAttr);
            if (cellSignalState.providerModelBehavior) {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(this.mContext.getDrawable(R$drawable.ic_qs_no_calling_sms));
                }
                this.mMobileSignal.setImageDrawable(this.mContext.getDrawable(cellSignalState.mobileSignalIconId));
                this.mMobileSignal.setContentDescription(cellSignalState.contentDescription);
            } else {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(new SignalDrawable(this.mContext));
                }
                this.mMobileSignal.setImageLevel(cellSignalState.mobileSignalIconId);
                StringBuilder sb = new StringBuilder();
                String str = cellSignalState.contentDescription;
                if (str != null) {
                    sb.append(str);
                    sb.append(", ");
                }
                if (cellSignalState.roaming) {
                    sb.append(this.mContext.getString(R$string.data_connection_roaming));
                    sb.append(", ");
                }
                if (hasValidTypeContentDescription(cellSignalState.typeContentDescription)) {
                    sb.append(cellSignalState.typeContentDescription);
                }
                this.mMobileSignal.setContentDescription(sb);
            }
        }
        return true;
    }

    public final boolean hasValidTypeContentDescription(String str) {
        return TextUtils.equals(str, this.mContext.getString(R$string.data_connection_no_internet)) || TextUtils.equals(str, this.mContext.getString(com.android.settingslib.R$string.cell_data_off_content_description)) || TextUtils.equals(str, this.mContext.getString(com.android.settingslib.R$string.not_default_data_content_description));
    }

    public View getRSSIView() {
        return this.mMobileGroup;
    }

    public void setCarrierText(CharSequence charSequence) {
        this.mCarrierText.setText(charSequence);
    }

    public void updateTextAppearance(int i) {
        FontSizeUtils.updateFontSizeFromStyle(this.mCarrierText, i);
    }
}
