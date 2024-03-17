package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.CallLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationCallTemplateViewWrapper.kt */
public final class NotificationCallTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public View appName;
    @NotNull
    public final CallLayout callLayout;
    public View conversationBadgeBg;
    public View conversationIconContainer;
    public CachingIconView conversationIconView;
    public View conversationTitleView;
    public View expandBtn;
    public final int minHeightWithActions;

    public NotificationCallTemplateViewWrapper(@NotNull Context context, @NotNull View view, @NotNull ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, R$dimen.notification_max_height);
        this.callLayout = (CallLayout) view;
    }

    public final void resolveViews() {
        CallLayout callLayout2 = this.callLayout;
        this.conversationIconContainer = callLayout2.requireViewById(16908927);
        this.conversationIconView = callLayout2.requireViewById(16908923);
        this.conversationBadgeBg = callLayout2.requireViewById(16908925);
        this.expandBtn = callLayout2.requireViewById(16908982);
        this.appName = callLayout2.requireViewById(16908784);
        this.conversationTitleView = callLayout2.requireViewById(16908929);
    }

    public void onContentUpdated(@NotNull ExpandableNotificationRow expandableNotificationRow) {
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        View[] viewArr = new View[2];
        View view = this.appName;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.conversationTitleView;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        View[] viewArr2 = new View[3];
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            cachingIconView = null;
        }
        viewArr2[0] = cachingIconView;
        View view4 = this.conversationBadgeBg;
        if (view4 == null) {
            view4 = null;
        }
        viewArr2[1] = view4;
        View view5 = this.expandBtn;
        if (view5 != null) {
            view2 = view5;
        }
        viewArr2[2] = view2;
        addViewsTransformingToSimilar(viewArr2);
    }

    public int getMinLayoutHeight() {
        return this.minHeightWithActions;
    }

    public void setNotificationFaded(boolean z) {
        View view = this.expandBtn;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        NotificationFadeAware.setLayerTypeForFaded(view, z);
        View view3 = this.conversationIconContainer;
        if (view3 != null) {
            view2 = view3;
        }
        NotificationFadeAware.setLayerTypeForFaded(view2, z);
    }
}
