package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyDialog;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* renamed from: com.android.systemui.privacy.PrivacyDialogController$filterAndSelect$lambda-6$$inlined$sortedByDescending$1  reason: invalid class name */
/* compiled from: Comparisons.kt */
public final class PrivacyDialogController$filterAndSelect$lambda6$$inlined$sortedByDescending$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((PrivacyDialog.PrivacyElement) t2).getLastActiveTimestamp()), Long.valueOf(((PrivacyDialog.PrivacyElement) t).getLastActiveTimestamp()));
    }
}
