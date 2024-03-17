package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationIconContainer;

public class NotificationShelfController {
    public final ActivatableNotificationViewController mActivatableNotificationViewController;
    public AmbientState mAmbientState;
    public final KeyguardBypassController mKeyguardBypassController;
    public final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View view) {
            NotificationShelfController.this.mStatusBarStateController.addCallback(NotificationShelfController.this.mView, 3);
        }

        public void onViewDetachedFromWindow(View view) {
            NotificationShelfController.this.mStatusBarStateController.removeCallback(NotificationShelfController.this.mView);
        }
    };
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final NotificationShelf mView;

    public NotificationShelfController(NotificationShelf notificationShelf, ActivatableNotificationViewController activatableNotificationViewController, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController) {
        this.mView = notificationShelf;
        this.mActivatableNotificationViewController = activatableNotificationViewController;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
    }

    public void init() {
        this.mActivatableNotificationViewController.init();
        this.mView.setController(this);
        this.mView.addOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
        if (this.mView.isAttachedToWindow()) {
            this.mOnAttachStateChangeListener.onViewAttachedToWindow(this.mView);
        }
    }

    public NotificationShelf getView() {
        return this.mView;
    }

    public boolean canModifyColorOfNotifications() {
        return this.mAmbientState.isShadeExpanded() && (!this.mAmbientState.isOnKeyguard() || !this.mKeyguardBypassController.getBypassEnabled());
    }

    public NotificationIconContainer getShelfIcons() {
        return this.mView.getShelfIcons();
    }

    public void setCollapsedIcons(NotificationIconContainer notificationIconContainer) {
        this.mView.setCollapsedIcons(notificationIconContainer);
    }

    public void bind(AmbientState ambientState, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.mView.bind(ambientState, notificationStackScrollLayoutController);
        this.mAmbientState = ambientState;
    }

    public int getIntrinsicHeight() {
        return this.mView.getIntrinsicHeight();
    }

    public void setOnActivatedListener(ActivatableNotificationView.OnActivatedListener onActivatedListener) {
        this.mView.setOnActivatedListener(onActivatedListener);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mView.setOnClickListener(onClickListener);
    }
}
