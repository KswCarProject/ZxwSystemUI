package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.NotificationPersonExtractorPlugin;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PeopleHubNotificationListener.kt */
public final class NotificationPersonExtractorPluginBoundary implements NotificationPersonExtractor {
    @Nullable
    public NotificationPersonExtractorPlugin plugin;

    public NotificationPersonExtractorPluginBoundary(@NotNull ExtensionController extensionController) {
        Class<NotificationPersonExtractorPlugin> cls = NotificationPersonExtractorPlugin.class;
        this.plugin = extensionController.newExtension(cls).withPlugin(cls).withCallback(new Consumer(this) {
            public final /* synthetic */ NotificationPersonExtractorPluginBoundary this$0;

            {
                this.this$0 = r1;
            }

            public final void accept(NotificationPersonExtractorPlugin notificationPersonExtractorPlugin) {
                this.this$0.plugin = notificationPersonExtractorPlugin;
            }
        }).build().get();
    }

    public boolean isPersonNotification(@NotNull StatusBarNotification statusBarNotification) {
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin == null) {
            return false;
        }
        return notificationPersonExtractorPlugin.isPersonNotification(statusBarNotification);
    }
}
