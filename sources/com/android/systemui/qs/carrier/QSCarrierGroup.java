package com.android.systemui.qs.carrier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$id;

public class QSCarrierGroup extends LinearLayout {
    public QSCarrierGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TextView getNoSimTextView() {
        return (TextView) findViewById(R$id.no_carrier_text);
    }

    public QSCarrier getCarrier1View() {
        return (QSCarrier) findViewById(R$id.carrier1);
    }

    public QSCarrier getCarrier2View() {
        return (QSCarrier) findViewById(R$id.carrier2);
    }

    public QSCarrier getCarrier3View() {
        return (QSCarrier) findViewById(R$id.carrier3);
    }

    public View getCarrierDivider1() {
        return findViewById(R$id.qs_carrier_divider1);
    }

    public View getCarrierDivider2() {
        return findViewById(R$id.qs_carrier_divider2);
    }

    public void updateTextAppearance(int i) {
        FontSizeUtils.updateFontSizeFromStyle(getNoSimTextView(), i);
        getCarrier1View().updateTextAppearance(i);
        getCarrier2View().updateTextAppearance(i);
        getCarrier3View().updateTextAppearance(i);
    }
}
