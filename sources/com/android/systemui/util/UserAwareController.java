package com.android.systemui.util;

import android.os.UserHandle;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserAwareController.kt */
public interface UserAwareController {
    void changeUser(@NotNull UserHandle userHandle) {
    }

    int getCurrentUserId();
}
