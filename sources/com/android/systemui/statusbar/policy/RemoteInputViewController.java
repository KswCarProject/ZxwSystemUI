package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RemoteInputViewController.kt */
public interface RemoteInputViewController {
    void bind();

    void close();

    void focus();

    @Nullable
    PendingIntent getPendingIntent();

    @Nullable
    RemoteInput getRemoteInput();

    @Nullable
    RemoteInput[] getRemoteInputs();

    @Nullable
    RemoteInputView.RevealParams getRevealParams();

    boolean isActive();

    void setBouncerChecker(@Nullable NotificationRemoteInputManager.BouncerChecker bouncerChecker);

    void setEditedSuggestionInfo(@Nullable NotificationEntry.EditedSuggestionInfo editedSuggestionInfo);

    void setPendingIntent(@Nullable PendingIntent pendingIntent);

    void setRemoteInput(@Nullable RemoteInput remoteInput);

    void setRemoteInputs(@Nullable RemoteInput[] remoteInputArr);

    void setRevealParams(@Nullable RemoteInputView.RevealParams revealParams);

    void stealFocusFrom(@NotNull RemoteInputViewController remoteInputViewController);

    void unbind();

    boolean updatePendingIntentFromActions(@Nullable Notification.Action[] actionArr);

    /* compiled from: RemoteInputViewController.kt */
    public static final class DefaultImpls {
        public static void stealFocusFrom(@NotNull RemoteInputViewController remoteInputViewController, @NotNull RemoteInputViewController remoteInputViewController2) {
            remoteInputViewController2.close();
            remoteInputViewController.setRemoteInput(remoteInputViewController2.getRemoteInput());
            remoteInputViewController.setRemoteInputs(remoteInputViewController2.getRemoteInputs());
            remoteInputViewController.setRevealParams(remoteInputViewController2.getRevealParams());
            remoteInputViewController.setPendingIntent(remoteInputViewController2.getPendingIntent());
            remoteInputViewController.focus();
        }
    }
}
