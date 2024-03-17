package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SectionHeaderController.kt */
public final class SectionHeaderNodeControllerImpl implements NodeController, SectionHeaderController {
    @Nullable
    public SectionHeaderView _view;
    @NotNull
    public final ActivityStarter activityStarter;
    public boolean clearAllButtonEnabled;
    @Nullable
    public View.OnClickListener clearAllClickListener;
    @NotNull
    public final String clickIntentAction;
    public final int headerTextResId;
    @NotNull
    public final LayoutInflater layoutInflater;
    @NotNull
    public final String nodeLabel;
    @NotNull
    public final View.OnClickListener onHeaderClickListener = new SectionHeaderNodeControllerImpl$onHeaderClickListener$1(this);

    public SectionHeaderNodeControllerImpl(@NotNull String str, @NotNull LayoutInflater layoutInflater2, int i, @NotNull ActivityStarter activityStarter2, @NotNull String str2) {
        this.nodeLabel = str;
        this.layoutInflater = layoutInflater2;
        this.headerTextResId = i;
        this.activityStarter = activityStarter2;
        this.clickIntentAction = str2;
    }

    public void addChildAt(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.addChildAt(this, nodeController, i);
    }

    @Nullable
    public View getChildAt(int i) {
        return NodeController.DefaultImpls.getChildAt(this, i);
    }

    public int getChildCount() {
        return NodeController.DefaultImpls.getChildCount(this);
    }

    public void moveChildTo(@NotNull NodeController nodeController, int i) {
        NodeController.DefaultImpls.moveChildTo(this, nodeController, i);
    }

    public void onViewMoved() {
        NodeController.DefaultImpls.onViewMoved(this);
    }

    public void onViewRemoved() {
        NodeController.DefaultImpls.onViewRemoved(this);
    }

    public void removeChild(@NotNull NodeController nodeController, boolean z) {
        NodeController.DefaultImpls.removeChild(this, nodeController, z);
    }

    @NotNull
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0022  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reinflateView(@org.jetbrains.annotations.NotNull android.view.ViewGroup r6) {
        /*
            r5 = this;
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = r5._view
            r1 = -1
            if (r0 != 0) goto L_0x0007
        L_0x0005:
            r2 = r1
            goto L_0x0017
        L_0x0007:
            r0.removeFromTransientContainer()
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r6) goto L_0x0005
            int r2 = r6.indexOfChild(r0)
            r6.removeView(r0)
        L_0x0017:
            android.view.LayoutInflater r0 = r5.layoutInflater
            int r3 = com.android.systemui.R$layout.status_bar_notification_section_header
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r6, r4)
            if (r0 == 0) goto L_0x0043
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = (com.android.systemui.statusbar.notification.stack.SectionHeaderView) r0
            int r3 = r5.headerTextResId
            r0.setHeaderText(r3)
            android.view.View$OnClickListener r3 = r5.onHeaderClickListener
            r0.setOnHeaderClickListener(r3)
            android.view.View$OnClickListener r3 = r5.clearAllClickListener
            if (r3 != 0) goto L_0x0033
            goto L_0x0036
        L_0x0033:
            r0.setOnClearAllClickListener(r3)
        L_0x0036:
            if (r2 == r1) goto L_0x003b
            r6.addView(r0, r2)
        L_0x003b:
            r5._view = r0
            boolean r5 = r5.clearAllButtonEnabled
            r0.setClearSectionButtonEnabled(r5)
            return
        L_0x0043:
            java.lang.NullPointerException r5 = new java.lang.NullPointerException
            java.lang.String r6 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl.reinflateView(android.view.ViewGroup):void");
    }

    @Nullable
    public SectionHeaderView getHeaderView() {
        return this._view;
    }

    public void setClearSectionEnabled(boolean z) {
        this.clearAllButtonEnabled = z;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setClearSectionButtonEnabled(z);
        }
    }

    public void setOnClearSectionClickListener(@NotNull View.OnClickListener onClickListener) {
        this.clearAllClickListener = onClickListener;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setOnClearAllClickListener(onClickListener);
        }
    }

    public void onViewAdded() {
        SectionHeaderView headerView = getHeaderView();
        if (headerView != null) {
            headerView.setContentVisible(true);
        }
    }

    @NotNull
    public View getView() {
        SectionHeaderView sectionHeaderView = this._view;
        Intrinsics.checkNotNull(sectionHeaderView);
        return sectionHeaderView;
    }
}
