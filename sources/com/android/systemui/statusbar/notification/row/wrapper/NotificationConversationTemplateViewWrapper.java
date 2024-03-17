package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationConversationTemplateViewWrapper.kt */
public final class NotificationConversationTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public View appName;
    public View conversationBadgeBg;
    public View conversationIconContainer;
    public CachingIconView conversationIconView;
    @NotNull
    public final ConversationLayout conversationLayout;
    public View conversationTitleView;
    public View expandBtn;
    public View expandBtnContainer;
    @Nullable
    public View facePileBottom;
    @Nullable
    public View facePileBottomBg;
    @Nullable
    public View facePileTop;
    public ViewGroup imageMessageContainer;
    public View importanceRing;
    public MessagingLinearLayout messagingLinearLayout;
    public final int minHeightWithActions;

    public NotificationConversationTemplateViewWrapper(@NotNull Context context, @NotNull View view, @NotNull ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, R$dimen.notification_messaging_actions_min_height);
        this.conversationLayout = (ConversationLayout) view;
    }

    public final void resolveViews() {
        this.messagingLinearLayout = this.conversationLayout.getMessagingLinearLayout();
        this.imageMessageContainer = this.conversationLayout.getImageMessageContainer();
        ConversationLayout conversationLayout2 = this.conversationLayout;
        this.conversationIconContainer = conversationLayout2.requireViewById(16908927);
        this.conversationIconView = conversationLayout2.requireViewById(16908923);
        this.conversationBadgeBg = conversationLayout2.requireViewById(16908925);
        this.expandBtn = conversationLayout2.requireViewById(16908982);
        this.expandBtnContainer = conversationLayout2.requireViewById(16908984);
        this.importanceRing = conversationLayout2.requireViewById(16908926);
        this.appName = conversationLayout2.requireViewById(16908784);
        this.conversationTitleView = conversationLayout2.requireViewById(16908929);
        this.facePileTop = conversationLayout2.findViewById(16908921);
        this.facePileBottom = conversationLayout2.findViewById(16908919);
        this.facePileBottomBg = conversationLayout2.findViewById(16908920);
    }

    public void onContentUpdated(@NotNull ExpandableNotificationRow expandableNotificationRow) {
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        View view = this.conversationTitleView;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewTransformationHelper.addTransformedView(1, view);
        View[] viewArr = new View[2];
        MessagingLinearLayout messagingLinearLayout2 = this.messagingLinearLayout;
        if (messagingLinearLayout2 == null) {
            messagingLinearLayout2 = null;
        }
        viewArr[0] = messagingLinearLayout2;
        View view3 = this.appName;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        ViewTransformationHelper viewTransformationHelper2 = this.mTransformationHelper;
        ViewGroup viewGroup = this.imageMessageContainer;
        if (viewGroup == null) {
            viewGroup = null;
        }
        NotificationMessagingTemplateViewWrapper.setCustomImageMessageTransform(viewTransformationHelper2, viewGroup);
        View[] viewArr2 = new View[7];
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
        if (view5 == null) {
            view5 = null;
        }
        viewArr2[2] = view5;
        View view6 = this.importanceRing;
        if (view6 != null) {
            view2 = view6;
        }
        viewArr2[3] = view2;
        viewArr2[4] = this.facePileTop;
        viewArr2[5] = this.facePileBottom;
        viewArr2[6] = this.facePileBottomBg;
        addViewsTransformingToSimilar(viewArr2);
    }

    @Nullable
    public View getShelfTransformationTarget() {
        if (!this.conversationLayout.isImportantConversation()) {
            return super.getShelfTransformationTarget();
        }
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            cachingIconView = null;
        }
        if (cachingIconView.getVisibility() == 8) {
            return super.getShelfTransformationTarget();
        }
        View view = this.conversationIconView;
        if (view == null) {
            return null;
        }
        return view;
    }

    public void setRemoteInputVisible(boolean z) {
        this.conversationLayout.showHistoricMessages(z);
    }

    public void updateExpandability(boolean z, @NotNull View.OnClickListener onClickListener, boolean z2) {
        this.conversationLayout.updateExpandability(z, onClickListener);
    }

    public int getMinLayoutHeight() {
        View view = this.mActionsContainer;
        if (view == null || view.getVisibility() == 8) {
            return super.getMinLayoutHeight();
        }
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
