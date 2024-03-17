package com.android.systemui.media;

import com.android.systemui.monet.ColorScheme;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaColorSchemes.kt */
public final class MediaColorSchemesKt {
    public static final int surfaceFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getAccent2().get(9).intValue();
    }

    public static final int accentPrimaryFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getAccent1().get(2).intValue();
    }

    public static final int accentSecondaryFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getAccent1().get(3).intValue();
    }

    public static final int textPrimaryFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getNeutral1().get(1).intValue();
    }

    public static final int textPrimaryInverseFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getNeutral1().get(10).intValue();
    }

    public static final int textSecondaryFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getNeutral2().get(3).intValue();
    }

    public static final int textTertiaryFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getNeutral2().get(5).intValue();
    }

    public static final int backgroundStartFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getAccent2().get(8).intValue();
    }

    public static final int backgroundEndFromScheme(@NotNull ColorScheme colorScheme) {
        return colorScheme.getAccent1().get(8).intValue();
    }
}
