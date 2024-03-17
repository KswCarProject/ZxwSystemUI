package com.android.systemui.statusbar.policy.dagger;

import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import org.jetbrains.annotations.NotNull;

/* compiled from: RemoteInput.kt */
public interface RemoteInputViewSubcomponent {

    /* compiled from: RemoteInput.kt */
    public interface Factory {
        @NotNull
        RemoteInputViewSubcomponent create(@NotNull RemoteInputView remoteInputView, @NotNull RemoteInputController remoteInputController);
    }

    @NotNull
    RemoteInputViewController getController();
}
