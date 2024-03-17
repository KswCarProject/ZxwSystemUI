package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import android.view.View;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewManager.kt */
public final class ShadeViewManager {
    @NotNull
    public final RootNodeController rootController;
    @NotNull
    public final NodeSpecBuilder specBuilder;
    @NotNull
    public final NotifStackController stackController;
    @NotNull
    public final NotifViewBarn viewBarn;
    @NotNull
    public final ShadeViewDiffer viewDiffer;
    @NotNull
    public final ShadeViewManager$viewRenderer$1 viewRenderer = new ShadeViewManager$viewRenderer$1(this);

    public ShadeViewManager(@NotNull Context context, @NotNull NotificationListContainer notificationListContainer, @NotNull NotifStackController notifStackController, @NotNull MediaContainerController mediaContainerController, @NotNull NotificationSectionsFeatureManager notificationSectionsFeatureManager, @NotNull SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider, @NotNull NodeSpecBuilderLogger nodeSpecBuilderLogger, @NotNull ShadeViewDifferLogger shadeViewDifferLogger, @NotNull NotifViewBarn notifViewBarn) {
        this.stackController = notifStackController;
        NotifViewBarn notifViewBarn2 = notifViewBarn;
        this.viewBarn = notifViewBarn2;
        Context context2 = context;
        NotificationListContainer notificationListContainer2 = notificationListContainer;
        RootNodeController rootNodeController = new RootNodeController(notificationListContainer, new View(context));
        this.rootController = rootNodeController;
        this.specBuilder = new NodeSpecBuilder(mediaContainerController, notificationSectionsFeatureManager, sectionHeaderVisibilityProvider, notifViewBarn2, nodeSpecBuilderLogger);
        this.viewDiffer = new ShadeViewDiffer(rootNodeController, shadeViewDifferLogger);
    }

    public final void attach(@NotNull RenderStageManager renderStageManager) {
        renderStageManager.setViewRenderer(this.viewRenderer);
    }
}
