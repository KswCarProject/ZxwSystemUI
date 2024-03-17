package com.android.settingslib.core.instrumentation;

import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SettingsJankMonitor.kt */
public final class SettingsJankMonitor {
    @NotNull
    public static final SettingsJankMonitor INSTANCE = new SettingsJankMonitor();
    public static final InteractionJankMonitor jankMonitor = InteractionJankMonitor.getInstance();
    public static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static /* synthetic */ void getMONITORED_ANIMATION_DURATION_MS$annotations() {
    }

    public static final void detectToggleJank(@Nullable String str, @NotNull View view) {
        InteractionJankMonitor.Configuration.Builder withView = InteractionJankMonitor.Configuration.Builder.withView(57, view);
        if (str != null) {
            withView.setTag(str);
        }
        if (jankMonitor.begin(withView)) {
            scheduledExecutorService.schedule(SettingsJankMonitor$detectToggleJank$1.INSTANCE, 300, TimeUnit.MILLISECONDS);
        }
    }
}
