package com.android.systemui.statusbar.notification.row;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChannelEditorListView.kt */
public final class ChannelRow extends LinearLayout {
    @Nullable
    public NotificationChannel channel;
    public TextView channelDescription;
    public TextView channelName;
    public ChannelEditorDialogController controller;
    public boolean gentle;
    public final int highlightColor = Utils.getColorAttrDefaultColor(getContext(), 16843820);

    /* renamed from: switch  reason: not valid java name */
    public Switch f8switch;

    public ChannelRow(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @NotNull
    public final ChannelEditorDialogController getController() {
        ChannelEditorDialogController channelEditorDialogController = this.controller;
        if (channelEditorDialogController != null) {
            return channelEditorDialogController;
        }
        return null;
    }

    public final void setController(@NotNull ChannelEditorDialogController channelEditorDialogController) {
        this.controller = channelEditorDialogController;
    }

    @Nullable
    public final NotificationChannel getChannel() {
        return this.channel;
    }

    public final void setChannel(@Nullable NotificationChannel notificationChannel) {
        this.channel = notificationChannel;
        updateImportance();
        updateViews();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.channelName = (TextView) findViewById(R$id.channel_name);
        this.channelDescription = (TextView) findViewById(R$id.channel_description);
        Switch switchR = (Switch) findViewById(R$id.toggle);
        this.f8switch = switchR;
        if (switchR == null) {
            switchR = null;
        }
        switchR.setOnCheckedChangeListener(new ChannelRow$onFinishInflate$1(this));
        setOnClickListener(new ChannelRow$onFinishInflate$2(this));
    }

    public final void playHighlight() {
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{0, Integer.valueOf(this.highlightColor)});
        ofObject.setDuration(200);
        ofObject.addUpdateListener(new ChannelRow$playHighlight$1(this));
        ofObject.setRepeatMode(2);
        ofObject.setRepeatCount(5);
        ofObject.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateViews() {
        /*
            r5 = this;
            android.app.NotificationChannel r0 = r5.channel
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.widget.TextView r1 = r5.channelName
            r2 = 0
            if (r1 != 0) goto L_0x000b
            r1 = r2
        L_0x000b:
            java.lang.CharSequence r3 = r0.getName()
            if (r3 != 0) goto L_0x0013
            java.lang.String r3 = ""
        L_0x0013:
            r1.setText(r3)
            java.lang.String r1 = r0.getGroup()
            if (r1 != 0) goto L_0x001d
            goto L_0x002d
        L_0x001d:
            android.widget.TextView r3 = r5.channelDescription
            if (r3 != 0) goto L_0x0022
            r3 = r2
        L_0x0022:
            com.android.systemui.statusbar.notification.row.ChannelEditorDialogController r4 = r5.getController()
            java.lang.CharSequence r1 = r4.groupNameForId(r1)
            r3.setText(r1)
        L_0x002d:
            java.lang.String r1 = r0.getGroup()
            r3 = 0
            if (r1 == 0) goto L_0x004d
            android.widget.TextView r1 = r5.channelDescription
            if (r1 != 0) goto L_0x0039
            r1 = r2
        L_0x0039:
            java.lang.CharSequence r1 = r1.getText()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0044
            goto L_0x004d
        L_0x0044:
            android.widget.TextView r1 = r5.channelDescription
            if (r1 != 0) goto L_0x0049
            r1 = r2
        L_0x0049:
            r1.setVisibility(r3)
            goto L_0x0057
        L_0x004d:
            android.widget.TextView r1 = r5.channelDescription
            if (r1 != 0) goto L_0x0052
            r1 = r2
        L_0x0052:
            r4 = 8
            r1.setVisibility(r4)
        L_0x0057:
            android.widget.Switch r5 = r5.f8switch
            if (r5 != 0) goto L_0x005c
            goto L_0x005d
        L_0x005c:
            r2 = r5
        L_0x005d:
            int r5 = r0.getImportance()
            if (r5 == 0) goto L_0x0064
            r3 = 1
        L_0x0064:
            r2.setChecked(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ChannelRow.updateViews():void");
    }

    public final void updateImportance() {
        NotificationChannel notificationChannel = this.channel;
        boolean z = false;
        int importance = notificationChannel == null ? 0 : notificationChannel.getImportance();
        if (importance != -1000 && importance < 3) {
            z = true;
        }
        this.gentle = z;
    }
}
