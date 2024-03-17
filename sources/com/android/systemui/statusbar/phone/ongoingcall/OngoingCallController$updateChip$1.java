package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.statusbar.window.StatusBarWindowController;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$updateChip$1<T> implements Consumer {
    public static final OngoingCallController$updateChip$1<T> INSTANCE = new OngoingCallController$updateChip$1<>();

    public final void accept(@NotNull StatusBarWindowController statusBarWindowController) {
        statusBarWindowController.setOngoingProcessRequiresStatusBarVisible(true);
    }
}
