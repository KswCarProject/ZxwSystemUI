package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$onBeforeRenderListListener$1 implements OnBeforeRenderListListener {
    public final /* synthetic */ ConversationCoordinator this$0;

    public ConversationCoordinator$onBeforeRenderListListener$1(ConversationCoordinator conversationCoordinator) {
        this.this$0 = conversationCoordinator;
    }

    public final void onBeforeRenderList(List<ListEntry> list) {
        Map access$getPromotedEntriesToSummaryOfSameChannel$p = this.this$0.promotedEntriesToSummaryOfSameChannel;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : access$getPromotedEntriesToSummaryOfSameChannel$p.entrySet()) {
            NotificationEntry notificationEntry = (NotificationEntry) entry.getKey();
            NotificationEntry notificationEntry2 = (NotificationEntry) entry.getValue();
            GroupEntry parent = notificationEntry2.getParent();
            String str = null;
            if (parent != null && !Intrinsics.areEqual((Object) parent, (Object) notificationEntry.getParent()) && parent.getParent() != null && Intrinsics.areEqual((Object) parent.getSummary(), (Object) notificationEntry2)) {
                Iterable children = parent.getChildren();
                boolean z = false;
                if (!(children instanceof Collection) || !((Collection) children).isEmpty()) {
                    Iterator it = children.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (Intrinsics.areEqual((Object) ((NotificationEntry) it.next()).getChannel(), (Object) notificationEntry2.getChannel())) {
                                z = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (!z) {
                    str = notificationEntry2.getKey();
                }
            }
            if (str != null) {
                arrayList.add(str);
            }
        }
        this.this$0.conversationIconManager.setUnimportantConversations(arrayList);
        this.this$0.promotedEntriesToSummaryOfSameChannel.clear();
    }
}
