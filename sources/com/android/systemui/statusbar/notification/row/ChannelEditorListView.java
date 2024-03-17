package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChannelEditorListView.kt */
public final class ChannelEditorListView extends LinearLayout {
    public AppControlView appControlRow;
    @Nullable
    public Drawable appIcon;
    @Nullable
    public String appName;
    @NotNull
    public final List<ChannelRow> channelRows = new ArrayList();
    @NotNull
    public List<NotificationChannel> channels = new ArrayList();
    public ChannelEditorDialogController controller;

    public ChannelEditorListView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
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

    public final void setAppIcon(@Nullable Drawable drawable) {
        this.appIcon = drawable;
    }

    public final void setAppName(@Nullable String str) {
        this.appName = str;
    }

    public final void setChannels(@NotNull List<NotificationChannel> list) {
        this.channels = list;
        updateRows();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.appControlRow = (AppControlView) findViewById(R$id.app_control);
    }

    public final void highlightChannel(@NotNull NotificationChannel notificationChannel) {
        Assert.isMainThread();
        for (ChannelRow next : this.channelRows) {
            if (Intrinsics.areEqual((Object) next.getChannel(), (Object) notificationChannel)) {
                next.playHighlight();
            }
        }
    }

    public final void updateRows() {
        boolean areAppNotificationsEnabled = getController().areAppNotificationsEnabled();
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200);
        autoTransition.addListener(new ChannelEditorListView$updateRows$1(this));
        TransitionManager.beginDelayedTransition(this, autoTransition);
        for (ChannelRow removeView : this.channelRows) {
            removeView(removeView);
        }
        this.channelRows.clear();
        updateAppControlRow(areAppNotificationsEnabled);
        if (areAppNotificationsEnabled) {
            LayoutInflater from = LayoutInflater.from(getContext());
            for (NotificationChannel addChannelRow : this.channels) {
                addChannelRow(addChannelRow, from);
            }
        }
    }

    public final void addChannelRow(NotificationChannel notificationChannel, LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R$layout.notif_half_shelf_row, (ViewGroup) null);
        if (inflate != null) {
            ChannelRow channelRow = (ChannelRow) inflate;
            channelRow.setController(getController());
            channelRow.setChannel(notificationChannel);
            this.channelRows.add(channelRow);
            addView(channelRow);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.ChannelRow");
    }

    public final void updateAppControlRow(boolean z) {
        AppControlView appControlView = this.appControlRow;
        AppControlView appControlView2 = null;
        if (appControlView == null) {
            appControlView = null;
        }
        appControlView.getIconView().setImageDrawable(this.appIcon);
        AppControlView appControlView3 = this.appControlRow;
        if (appControlView3 == null) {
            appControlView3 = null;
        }
        appControlView3.getChannelName().setText(getContext().getResources().getString(R$string.notification_channel_dialog_title, new Object[]{this.appName}));
        AppControlView appControlView4 = this.appControlRow;
        if (appControlView4 == null) {
            appControlView4 = null;
        }
        appControlView4.getSwitch().setChecked(z);
        AppControlView appControlView5 = this.appControlRow;
        if (appControlView5 != null) {
            appControlView2 = appControlView5;
        }
        appControlView2.getSwitch().setOnCheckedChangeListener(new ChannelEditorListView$updateAppControlRow$1(this));
    }
}
