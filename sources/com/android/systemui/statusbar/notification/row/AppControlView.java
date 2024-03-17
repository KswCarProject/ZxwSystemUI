package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.R$id;
import org.jetbrains.annotations.NotNull;

/* compiled from: ChannelEditorListView.kt */
public final class AppControlView extends LinearLayout {
    public TextView channelName;
    public ImageView iconView;

    /* renamed from: switch  reason: not valid java name */
    public Switch f7switch;

    public AppControlView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @NotNull
    public final ImageView getIconView() {
        ImageView imageView = this.iconView;
        if (imageView != null) {
            return imageView;
        }
        return null;
    }

    public final void setIconView(@NotNull ImageView imageView) {
        this.iconView = imageView;
    }

    @NotNull
    public final TextView getChannelName() {
        TextView textView = this.channelName;
        if (textView != null) {
            return textView;
        }
        return null;
    }

    public final void setChannelName(@NotNull TextView textView) {
        this.channelName = textView;
    }

    @NotNull
    public final Switch getSwitch() {
        Switch switchR = this.f7switch;
        if (switchR != null) {
            return switchR;
        }
        return null;
    }

    public final void setSwitch(@NotNull Switch switchR) {
        this.f7switch = switchR;
    }

    public void onFinishInflate() {
        setIconView((ImageView) findViewById(R$id.icon));
        setChannelName((TextView) findViewById(R$id.app_name));
        setSwitch((Switch) findViewById(R$id.toggle));
        setOnClickListener(new AppControlView$onFinishInflate$1(this));
    }
}
