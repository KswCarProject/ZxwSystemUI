package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ViewClippingUtil;
import com.android.systemui.R$id;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HeadsUpAppearanceController extends ViewController<HeadsUpStatusBarView> implements OnHeadsUpChangedListener, DarkIconDispatcher.DarkReceiver, NotificationWakeUpCoordinator.WakeUpListener {
    public boolean mAnimationsEnabled = true;
    @VisibleForTesting
    public float mAppearFraction;
    public final KeyguardBypassController mBypassController;
    public final View mClockView;
    public final CommandQueue mCommandQueue;
    public final DarkIconDispatcher mDarkIconDispatcher;
    @VisibleForTesting
    public float mExpandedHeight;
    public final HeadsUpManagerPhone mHeadsUpManager;
    public final KeyguardStateController mKeyguardStateController;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public final Optional<View> mOperatorNameViewOptional;
    public final ViewClippingUtil.ClippingParameters mParentClippingParams = new ViewClippingUtil.ClippingParameters() {
        public boolean shouldFinish(View view) {
            return view.getId() == R$id.status_bar;
        }
    };
    public final BiConsumer<Float, Float> mSetExpandedHeight = new HeadsUpAppearanceController$$ExternalSyntheticLambda1(this);
    public final Consumer<ExpandableNotificationRow> mSetTrackingHeadsUp = new HeadsUpAppearanceController$$ExternalSyntheticLambda0(this);
    public boolean mShown;
    public final NotificationStackScrollLayoutController mStackScrollerController;
    public final StatusBarStateController mStatusBarStateController;
    public ExpandableNotificationRow mTrackedChild;
    public final NotificationWakeUpCoordinator mWakeUpCoordinator;

    @VisibleForTesting
    public HeadsUpAppearanceController(NotificationIconAreaController notificationIconAreaController, HeadsUpManagerPhone headsUpManagerPhone, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, DarkIconDispatcher darkIconDispatcher, KeyguardStateController keyguardStateController, CommandQueue commandQueue, NotificationStackScrollLayoutController notificationStackScrollLayoutController, NotificationPanelViewController notificationPanelViewController, HeadsUpStatusBarView headsUpStatusBarView, Clock clock, Optional<View> optional) {
        super(headsUpStatusBarView);
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mHeadsUpManager = headsUpManagerPhone;
        this.mTrackedChild = notificationPanelViewController.getTrackedHeadsUpNotification();
        this.mAppearFraction = notificationStackScrollLayoutController.getAppearFraction();
        this.mExpandedHeight = notificationStackScrollLayoutController.getExpandedHeight();
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        notificationStackScrollLayoutController.setHeadsUpAppearanceController(this);
        this.mClockView = clock;
        this.mOperatorNameViewOptional = optional;
        this.mDarkIconDispatcher = darkIconDispatcher;
        ((HeadsUpStatusBarView) this.mView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (HeadsUpAppearanceController.this.shouldBeVisible()) {
                    HeadsUpAppearanceController.this.updateTopEntry();
                    HeadsUpAppearanceController.this.mStackScrollerController.requestLayout();
                }
                ((HeadsUpStatusBarView) HeadsUpAppearanceController.this.mView).removeOnLayoutChangeListener(this);
            }
        });
        this.mBypassController = keyguardBypassController;
        this.mStatusBarStateController = statusBarStateController;
        this.mWakeUpCoordinator = notificationWakeUpCoordinator;
        this.mCommandQueue = commandQueue;
        this.mKeyguardStateController = keyguardStateController;
    }

    public void onViewAttached() {
        this.mHeadsUpManager.addListener(this);
        ((HeadsUpStatusBarView) this.mView).setOnDrawingRectChangedListener(new HeadsUpAppearanceController$$ExternalSyntheticLambda2(this));
        this.mWakeUpCoordinator.addListener(this);
        this.mNotificationPanelViewController.addTrackingHeadsUpListener(this.mSetTrackingHeadsUp);
        this.mNotificationPanelViewController.setHeadsUpAppearanceController(this);
        this.mStackScrollerController.addOnExpandedHeightChangedListener(this.mSetExpandedHeight);
        this.mDarkIconDispatcher.addDarkReceiver((DarkIconDispatcher.DarkReceiver) this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$0() {
        updateIsolatedIconLocation(true);
    }

    public void onViewDetached() {
        this.mHeadsUpManager.removeListener(this);
        ((HeadsUpStatusBarView) this.mView).setOnDrawingRectChangedListener((Runnable) null);
        this.mWakeUpCoordinator.removeListener(this);
        this.mNotificationPanelViewController.removeTrackingHeadsUpListener(this.mSetTrackingHeadsUp);
        this.mNotificationPanelViewController.setHeadsUpAppearanceController((HeadsUpAppearanceController) null);
        this.mStackScrollerController.removeOnExpandedHeightChangedListener(this.mSetExpandedHeight);
        this.mDarkIconDispatcher.removeDarkReceiver((DarkIconDispatcher.DarkReceiver) this);
    }

    public final void updateIsolatedIconLocation(boolean z) {
        this.mNotificationIconAreaController.setIsolatedIconLocation(((HeadsUpStatusBarView) this.mView).getIconDrawingRect(), z);
    }

    public void onHeadsUpPinned(NotificationEntry notificationEntry) {
        updateTopEntry();
        lambda$updateHeadsUpHeaders$4(notificationEntry);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateTopEntry() {
        /*
            r5 = this;
            boolean r0 = r5.shouldBeVisible()
            r1 = 0
            if (r0 == 0) goto L_0x000e
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone r0 = r5.mHeadsUpManager
            com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.getTopEntry()
            goto L_0x000f
        L_0x000e:
            r0 = r1
        L_0x000f:
            T r2 = r5.mView
            com.android.systemui.statusbar.HeadsUpStatusBarView r2 = (com.android.systemui.statusbar.HeadsUpStatusBarView) r2
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r2.getShowingEntry()
            T r3 = r5.mView
            com.android.systemui.statusbar.HeadsUpStatusBarView r3 = (com.android.systemui.statusbar.HeadsUpStatusBarView) r3
            r3.setEntry(r0)
            if (r0 == r2) goto L_0x004b
            r3 = 1
            r4 = 0
            if (r0 != 0) goto L_0x002d
            r5.setShown(r4)
            boolean r2 = r5.isExpanded()
        L_0x002b:
            r2 = r2 ^ r3
            goto L_0x0038
        L_0x002d:
            if (r2 != 0) goto L_0x0037
            r5.setShown(r3)
            boolean r2 = r5.isExpanded()
            goto L_0x002b
        L_0x0037:
            r2 = r4
        L_0x0038:
            r5.updateIsolatedIconLocation(r4)
            com.android.systemui.statusbar.phone.NotificationIconAreaController r5 = r5.mNotificationIconAreaController
            if (r0 != 0) goto L_0x0040
            goto L_0x0048
        L_0x0040:
            com.android.systemui.statusbar.notification.icon.IconPack r0 = r0.getIcons()
            com.android.systemui.statusbar.StatusBarIconView r1 = r0.getStatusBarIcon()
        L_0x0048:
            r5.showIconIsolated(r1, r2)
        L_0x004b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.HeadsUpAppearanceController.updateTopEntry():void");
    }

    public final void setShown(boolean z) {
        if (this.mShown != z) {
            this.mShown = z;
            if (z) {
                updateParentClipping(false);
                ((HeadsUpStatusBarView) this.mView).setVisibility(0);
                show(this.mView);
                hide(this.mClockView, 4);
                this.mOperatorNameViewOptional.ifPresent(new HeadsUpAppearanceController$$ExternalSyntheticLambda4(this));
            } else {
                show(this.mClockView);
                this.mOperatorNameViewOptional.ifPresent(new HeadsUpAppearanceController$$ExternalSyntheticLambda5(this));
                hide(this.mView, 8, new HeadsUpAppearanceController$$ExternalSyntheticLambda6(this));
            }
            if (this.mStatusBarStateController.getState() != 0) {
                this.mCommandQueue.recomputeDisableFlags(((HeadsUpStatusBarView) this.mView).getContext().getDisplayId(), false);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setShown$1(View view) {
        hide(view, 4);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setShown$2() {
        updateParentClipping(true);
    }

    public final void updateParentClipping(boolean z) {
        ViewClippingUtil.setClippingDeactivated(this.mView, !z, this.mParentClippingParams);
    }

    public final void hide(View view, int i) {
        hide(view, i, (Runnable) null);
    }

    public final void hide(View view, int i, Runnable runnable) {
        if (this.mAnimationsEnabled) {
            CrossFadeHelper.fadeOut(view, 110, 0, new HeadsUpAppearanceController$$ExternalSyntheticLambda7(view, i, runnable));
            return;
        }
        view.setVisibility(i);
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$hide$3(View view, int i, Runnable runnable) {
        view.setVisibility(i);
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void show(View view) {
        if (this.mAnimationsEnabled) {
            CrossFadeHelper.fadeIn(view, 110, 100);
        } else {
            view.setVisibility(0);
        }
    }

    @VisibleForTesting
    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
    }

    @VisibleForTesting
    public boolean isShown() {
        return this.mShown;
    }

    public boolean shouldBeVisible() {
        boolean z = !this.mWakeUpCoordinator.getNotificationsFullyHidden();
        boolean z2 = !isExpanded() && z;
        if (this.mBypassController.getBypassEnabled() && ((this.mStatusBarStateController.getState() == 1 || this.mKeyguardStateController.isKeyguardGoingAway()) && z)) {
            z2 = true;
        }
        if (!z2 || !this.mHeadsUpManager.hasPinnedHeadsUp()) {
            return false;
        }
        return true;
    }

    public void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
        updateTopEntry();
        lambda$updateHeadsUpHeaders$4(notificationEntry);
    }

    public void setAppearFraction(float f, float f2) {
        boolean z = f != this.mExpandedHeight;
        boolean isExpanded = isExpanded();
        this.mExpandedHeight = f;
        this.mAppearFraction = f2;
        if (z) {
            updateHeadsUpHeaders();
        }
        if (isExpanded() != isExpanded) {
            updateTopEntry();
        }
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2 = this.mTrackedChild;
        this.mTrackedChild = expandableNotificationRow;
        if (expandableNotificationRow2 != null) {
            lambda$updateHeadsUpHeaders$4(expandableNotificationRow2.getEntry());
        }
    }

    public final boolean isExpanded() {
        return this.mExpandedHeight > 0.0f;
    }

    public final void updateHeadsUpHeaders() {
        this.mHeadsUpManager.getAllEntries().forEach(new HeadsUpAppearanceController$$ExternalSyntheticLambda3(this));
    }

    /* renamed from: updateHeader */
    public void lambda$updateHeadsUpHeaders$4(NotificationEntry notificationEntry) {
        float f;
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row.isPinned() || row.isHeadsUpAnimatingAway() || row == this.mTrackedChild || row.showingPulsing()) {
            f = this.mAppearFraction;
        } else {
            f = 1.0f;
        }
        row.setHeaderVisibleAmount(f);
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        ((HeadsUpStatusBarView) this.mView).onDarkChanged(arrayList, f, i);
    }

    public void onStateChanged() {
        updateTopEntry();
    }

    public void onFullyHiddenChanged(boolean z) {
        updateTopEntry();
    }
}
