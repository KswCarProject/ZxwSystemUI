package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.R$bool;
import org.jetbrains.annotations.NotNull;

/* compiled from: SectionHeaderVisibilityProvider.kt */
public final class SectionHeaderVisibilityProvider {
    public boolean neverShowSectionHeaders;
    public boolean sectionHeadersVisible = true;

    public SectionHeaderVisibilityProvider(@NotNull Context context) {
        this.neverShowSectionHeaders = context.getResources().getBoolean(R$bool.config_notification_never_show_section_headers);
    }

    public final boolean getNeverShowSectionHeaders() {
        return this.neverShowSectionHeaders;
    }

    public final boolean getSectionHeadersVisible() {
        return this.sectionHeadersVisible;
    }

    public final void setSectionHeadersVisible(boolean z) {
        this.sectionHeadersVisible = z;
    }
}
