package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.statusbar.window.StatusBarWindowController;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$removeChip$1<T> implements Consumer {
    public static final OngoingCallController$removeChip$1<T> INSTANCE = new OngoingCallController$removeChip$1<>();

    public final void accept(@NotNull StatusBarWindowController statusBarWindowController) {
        statusBarWindowController.setOngoingProcessRequiresStatusBarVisible(false);
    }
}
