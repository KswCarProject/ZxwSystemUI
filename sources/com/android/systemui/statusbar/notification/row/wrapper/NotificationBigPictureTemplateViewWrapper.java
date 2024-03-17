package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.statusbar.notification.ImageTransformState;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

public class NotificationBigPictureTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public NotificationBigPictureTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
    }

    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        super.onContentUpdated(expandableNotificationRow);
        updateImageTag(expandableNotificationRow.getEntry().getSbn());
    }

    public final void updateImageTag(StatusBarNotification statusBarNotification) {
        Icon icon = (Icon) statusBarNotification.getNotification().extras.getParcelable("android.largeIcon.big", Icon.class);
        if (icon != null) {
            ImageView imageView = this.mRightIcon;
            int i = ImageTransformState.ICON_TAG;
            imageView.setTag(i, icon);
            this.mLeftIcon.setTag(i, icon);
            return;
        }
        this.mRightIcon.setTag(ImageTransformState.ICON_TAG, getLargeIcon(statusBarNotification.getNotification()));
    }
}
