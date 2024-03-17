package com.android.systemui.privacy;

import com.android.systemui.Dumpable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyItemMonitor.kt */
public interface PrivacyItemMonitor extends Dumpable {

    /* compiled from: PrivacyItemMonitor.kt */
    public interface Callback {
        void onPrivacyItemsChanged();
    }

    @NotNull
    List<PrivacyItem> getActivePrivacyItems();

    void startListening(@NotNull Callback callback);

    void stopListening();
}
