package com.android.systemui.decor;

import android.content.res.Resources;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: PrivacyDotDecorProviderFactory.kt */
public final class PrivacyDotDecorProviderFactory extends DecorProviderFactory {
    @NotNull
    public final Resources res;

    public PrivacyDotDecorProviderFactory(@NotNull Resources resources) {
        this.res = resources;
    }

    public final boolean isPrivacyDotEnabled() {
        return this.res.getBoolean(R$bool.config_enablePrivacyDot);
    }

    public boolean getHasProviders() {
        return isPrivacyDotEnabled();
    }

    @NotNull
    public List<DecorProvider> getProviders() {
        if (!getHasProviders()) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        return CollectionsKt__CollectionsKt.listOf(new PrivacyDotCornerDecorProviderImpl(R$id.privacy_dot_top_left_container, 1, 0, R$layout.privacy_dot_top_left), new PrivacyDotCornerDecorProviderImpl(R$id.privacy_dot_top_right_container, 1, 2, R$layout.privacy_dot_top_right), new PrivacyDotCornerDecorProviderImpl(R$id.privacy_dot_bottom_left_container, 3, 0, R$layout.privacy_dot_bottom_left), new PrivacyDotCornerDecorProviderImpl(R$id.privacy_dot_bottom_right_container, 3, 2, R$layout.privacy_dot_bottom_right));
    }
}
