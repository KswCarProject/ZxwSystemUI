package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HunMutatorImpl implements HunMutator {
    @NotNull
    public final List<Pair<String, Boolean>> deferred = new ArrayList();
    @NotNull
    public final HeadsUpManager headsUpManager;

    public HunMutatorImpl(@NotNull HeadsUpManager headsUpManager2) {
        this.headsUpManager = headsUpManager2;
    }

    public void updateNotification(@NotNull String str, boolean z) {
        this.headsUpManager.updateNotification(str, z);
    }

    public void removeNotification(@NotNull String str, boolean z) {
        this.deferred.add(new Pair(str, Boolean.valueOf(z)));
    }

    public final void commitModifications() {
        for (Pair pair : this.deferred) {
            boolean booleanValue = ((Boolean) pair.component2()).booleanValue();
            this.headsUpManager.removeNotification((String) pair.component1(), booleanValue);
        }
        this.deferred.clear();
    }
}
