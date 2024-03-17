package com.android.systemui.sensorprivacy;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.android.internal.widget.DialogTitle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SensorUseDialog.kt */
public final class SensorUseDialog extends SystemUIDialog {
    @NotNull
    public final DialogInterface.OnClickListener clickListener;
    @NotNull
    public final DialogInterface.OnDismissListener dismissListener;
    public final int sensor;

    public SensorUseDialog(@NotNull Context context, int i, @NotNull DialogInterface.OnClickListener onClickListener, @NotNull DialogInterface.OnDismissListener onDismissListener) {
        super(context);
        int i2;
        int i3;
        this.sensor = i;
        this.clickListener = onClickListener;
        this.dismissListener = onDismissListener;
        Window window = getWindow();
        Intrinsics.checkNotNull(window);
        window.addFlags(524288);
        Window window2 = getWindow();
        Intrinsics.checkNotNull(window2);
        window2.addSystemFlags(524288);
        View inflate = LayoutInflater.from(context).inflate(R$layout.sensor_use_started_title, (ViewGroup) null);
        DialogTitle requireViewById = inflate.requireViewById(R$id.sensor_use_started_title_message);
        if (i == 1) {
            i2 = R$string.sensor_privacy_start_use_mic_dialog_title;
        } else if (i == 2) {
            i2 = R$string.sensor_privacy_start_use_camera_dialog_title;
        } else if (i != Integer.MAX_VALUE) {
            i2 = 0;
        } else {
            i2 = R$string.sensor_privacy_start_use_mic_camera_dialog_title;
        }
        requireViewById.setText(i2);
        int i4 = 8;
        ((ImageView) inflate.requireViewById(R$id.sensor_use_microphone_icon)).setVisibility((i == 1 || i == Integer.MAX_VALUE) ? 0 : 8);
        ((ImageView) inflate.requireViewById(R$id.sensor_use_camera_icon)).setVisibility((i == 2 || i == Integer.MAX_VALUE) ? 0 : i4);
        setCustomTitle(inflate);
        if (i == 1) {
            i3 = R$string.sensor_privacy_start_use_mic_dialog_content;
        } else if (i == 2) {
            i3 = R$string.sensor_privacy_start_use_camera_dialog_content;
        } else if (i != Integer.MAX_VALUE) {
            i3 = 0;
        } else {
            i3 = R$string.sensor_privacy_start_use_mic_camera_dialog_content;
        }
        setMessage(Html.fromHtml(context.getString(i3), 0));
        setButton(-1, context.getString(17041477), onClickListener);
        setButton(-2, context.getString(17039360), onClickListener);
        setOnDismissListener(onDismissListener);
        setCancelable(false);
    }
}
