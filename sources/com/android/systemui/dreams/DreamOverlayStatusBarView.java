package com.android.systemui.dreams;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.R$id;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DreamOverlayStatusBarView extends ConstraintLayout {
    public final Map<Integer, View> mStatusIcons;

    public DreamOverlayStatusBarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DreamOverlayStatusBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mStatusIcons = new HashMap();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusIcons.put(1, fetchStatusIconForResId(R$id.dream_overlay_wifi_status));
        this.mStatusIcons.put(2, fetchStatusIconForResId(R$id.dream_overlay_alarm_set));
        this.mStatusIcons.put(3, fetchStatusIconForResId(R$id.dream_overlay_camera_mic_off));
        this.mStatusIcons.put(0, fetchStatusIconForResId(R$id.dream_overlay_notification_indicator));
        this.mStatusIcons.put(4, fetchStatusIconForResId(R$id.dream_overlay_priority_mode));
    }

    public void showIcon(int i, boolean z, String str) {
        View view = this.mStatusIcons.get(Integer.valueOf(i));
        if (view != null) {
            if (z && str != null) {
                view.setContentDescription(str);
            }
            view.setVisibility(z ? 0 : 8);
        }
    }

    public final View fetchStatusIconForResId(int i) {
        View findViewById = findViewById(i);
        Objects.requireNonNull(findViewById);
        View view = findViewById;
        return findViewById;
    }
}
