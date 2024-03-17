package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import java.text.DecimalFormat;

public class DataUsageDetailView extends LinearLayout {
    public final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public DataUsageDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = R$dimen.qs_data_usage_text_size;
        FontSizeUtils.updateFontSize(this, 16908310, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_text, R$dimen.qs_data_usage_usage_text_size);
        FontSizeUtils.updateFontSize(this, R$id.usage_carrier_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_top_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_period_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_bottom_text, i);
    }
}
