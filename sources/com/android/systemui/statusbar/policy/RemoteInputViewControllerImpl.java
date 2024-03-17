package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import java.util.HashMap;
import java.util.Iterator;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.ArrayIteratorKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RemoteInputViewController.kt */
public final class RemoteInputViewControllerImpl implements RemoteInputViewController {
    @Nullable
    public NotificationRemoteInputManager.BouncerChecker bouncerChecker;
    @NotNull
    public final NotificationEntry entry;
    public boolean isBound;
    @NotNull
    public final View.OnFocusChangeListener onFocusChangeListener = new RemoteInputViewControllerImpl$onFocusChangeListener$1(this);
    @NotNull
    public final ArraySet<OnSendRemoteInputListener> onSendListeners = new ArraySet<>();
    @NotNull
    public final Runnable onSendRemoteInputListener = new RemoteInputViewControllerImpl$onSendRemoteInputListener$1(this);
    @Nullable
    public PendingIntent pendingIntent;
    @Nullable
    public RemoteInput remoteInput;
    @NotNull
    public final RemoteInputController remoteInputController;
    @NotNull
    public final RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler;
    @Nullable
    public RemoteInput[] remoteInputs;
    @Nullable
    public RemoteInputView.RevealParams revealParams;
    @NotNull
    public final ShortcutManager shortcutManager;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final RemoteInputView view;

    public RemoteInputViewControllerImpl(@NotNull RemoteInputView remoteInputView, @NotNull NotificationEntry notificationEntry, @NotNull RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler2, @NotNull RemoteInputController remoteInputController2, @NotNull ShortcutManager shortcutManager2, @NotNull UiEventLogger uiEventLogger2) {
        this.view = remoteInputView;
        this.entry = notificationEntry;
        this.remoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler2;
        this.remoteInputController = remoteInputController2;
        this.shortcutManager = shortcutManager2;
        this.uiEventLogger = uiEventLogger2;
    }

    public void stealFocusFrom(@NotNull RemoteInputViewController remoteInputViewController) {
        RemoteInputViewController.DefaultImpls.stealFocusFrom(this, remoteInputViewController);
    }

    public final Resources getResources() {
        return this.view.getResources();
    }

    @Nullable
    public NotificationRemoteInputManager.BouncerChecker getBouncerChecker() {
        return this.bouncerChecker;
    }

    public void setBouncerChecker(@Nullable NotificationRemoteInputManager.BouncerChecker bouncerChecker2) {
        this.bouncerChecker = bouncerChecker2;
    }

    @Nullable
    public RemoteInput getRemoteInput() {
        return this.remoteInput;
    }

    public void setRemoteInput(@Nullable RemoteInput remoteInput2) {
        this.remoteInput = remoteInput2;
        if (remoteInput2 != null) {
            if (!this.isBound) {
                remoteInput2 = null;
            }
            if (remoteInput2 != null) {
                this.view.setHintText(remoteInput2.getLabel());
                this.view.setSupportedMimeTypes(remoteInput2.getAllowedDataTypes());
            }
        }
    }

    @Nullable
    public PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    public void setPendingIntent(@Nullable PendingIntent pendingIntent2) {
        this.pendingIntent = pendingIntent2;
    }

    @Nullable
    public RemoteInput[] getRemoteInputs() {
        return this.remoteInputs;
    }

    public void setRemoteInputs(@Nullable RemoteInput[] remoteInputArr) {
        this.remoteInputs = remoteInputArr;
    }

    @Nullable
    public RemoteInputView.RevealParams getRevealParams() {
        return this.revealParams;
    }

    public void setRevealParams(@Nullable RemoteInputView.RevealParams revealParams2) {
        this.revealParams = revealParams2;
        if (this.isBound) {
            this.view.setRevealParameters(revealParams2);
        }
    }

    public boolean isActive() {
        return this.view.isActive();
    }

    public void bind() {
        if (!this.isBound) {
            this.isBound = true;
            RemoteInput remoteInput2 = getRemoteInput();
            if (remoteInput2 != null) {
                this.view.setHintText(remoteInput2.getLabel());
                this.view.setSupportedMimeTypes(remoteInput2.getAllowedDataTypes());
            }
            this.view.setRevealParameters(getRevealParams());
            this.view.addOnEditTextFocusChangedListener(this.onFocusChangeListener);
            this.view.addOnSendRemoteInputListener(this.onSendRemoteInputListener);
        }
    }

    public void unbind() {
        if (this.isBound) {
            this.isBound = false;
            this.view.removeOnEditTextFocusChangedListener(this.onFocusChangeListener);
            this.view.removeOnSendRemoteInputListener(this.onSendRemoteInputListener);
        }
    }

    public void setEditedSuggestionInfo(@Nullable NotificationEntry.EditedSuggestionInfo editedSuggestionInfo) {
        NotificationEntry notificationEntry = this.entry;
        notificationEntry.editedSuggestionInfo = editedSuggestionInfo;
        if (editedSuggestionInfo != null) {
            notificationEntry.remoteInputText = editedSuggestionInfo.originalText;
            notificationEntry.remoteInputAttachment = null;
        }
    }

    public boolean updatePendingIntentFromActions(@Nullable Notification.Action[] actionArr) {
        RemoteInput[] remoteInputs2;
        RemoteInput remoteInput2;
        if (actionArr == null) {
            return false;
        }
        PendingIntent pendingIntent2 = getPendingIntent();
        Intent intent = pendingIntent2 == null ? null : pendingIntent2.getIntent();
        if (intent == null) {
            return false;
        }
        Iterator it = ArrayIteratorKt.iterator(actionArr);
        while (it.hasNext()) {
            Notification.Action action = (Notification.Action) it.next();
            PendingIntent pendingIntent3 = action.actionIntent;
            if (!(pendingIntent3 == null || (remoteInputs2 = action.getRemoteInputs()) == null || !intent.filterEquals(pendingIntent3.getIntent()))) {
                int length = remoteInputs2.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        remoteInput2 = null;
                        break;
                    }
                    remoteInput2 = remoteInputs2[i];
                    i++;
                    if (remoteInput2.getAllowFreeFormInput()) {
                        break;
                    }
                }
                if (remoteInput2 != null) {
                    setPendingIntent(pendingIntent3);
                    setRemoteInput(remoteInput2);
                    setRemoteInputs(remoteInputs2);
                    setEditedSuggestionInfo((NotificationEntry.EditedSuggestionInfo) null);
                    return true;
                }
            }
        }
        return false;
    }

    public void close() {
        this.view.close();
    }

    public void focus() {
        this.view.focus();
    }

    public final void sendRemoteInput(PendingIntent pendingIntent2, Intent intent) {
        NotificationRemoteInputManager.BouncerChecker bouncerChecker2 = getBouncerChecker();
        if (bouncerChecker2 != null && bouncerChecker2.showBouncerIfNecessary()) {
            this.view.hideIme();
            for (T onSendRequestBounced : CollectionsKt___CollectionsKt.toList(this.onSendListeners)) {
                onSendRequestBounced.onSendRequestBounced();
            }
            return;
        }
        this.view.startSending();
        this.entry.lastRemoteInputSent = SystemClock.elapsedRealtime();
        NotificationEntry notificationEntry = this.entry;
        notificationEntry.mRemoteEditImeAnimatingAway = true;
        this.remoteInputController.addSpinning(notificationEntry.getKey(), this.view.mToken);
        this.remoteInputController.removeRemoteInput(this.entry, this.view.mToken);
        this.remoteInputController.remoteInputSent(this.entry);
        this.entry.setHasSentReply();
        for (T onSendRemoteInput : CollectionsKt___CollectionsKt.toList(this.onSendListeners)) {
            onSendRemoteInput.onSendRemoteInput();
        }
        this.shortcutManager.onApplicationActive(this.entry.getSbn().getPackageName(), this.entry.getSbn().getUser().getIdentifier());
        this.uiEventLogger.logWithInstanceId(RemoteInputView.NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_SEND, this.entry.getSbn().getUid(), this.entry.getSbn().getPackageName(), this.entry.getSbn().getInstanceId());
        try {
            pendingIntent2.send(this.view.getContext(), 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Log.i("RemoteInput", "Unable to send remote input result", e);
            this.uiEventLogger.logWithInstanceId(RemoteInputView.NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_FAILURE, this.entry.getSbn().getUid(), this.entry.getSbn().getPackageName(), this.entry.getSbn().getInstanceId());
        }
        this.view.clearAttachment();
    }

    public final Intent prepareRemoteInput(RemoteInput remoteInput2) {
        NotificationEntry notificationEntry = this.entry;
        if (notificationEntry.remoteInputAttachment == null) {
            return prepareRemoteInputFromText(remoteInput2);
        }
        return prepareRemoteInputFromData(remoteInput2, notificationEntry.remoteInputMimeType, this.entry.remoteInputUri);
    }

    public final Intent prepareRemoteInputFromText(RemoteInput remoteInput2) {
        Bundle bundle = new Bundle();
        bundle.putString(remoteInput2.getResultKey(), this.view.getText().toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(getRemoteInputs(), addFlags, bundle);
        this.entry.remoteInputText = this.view.getText();
        this.view.clearAttachment();
        NotificationEntry notificationEntry = this.entry;
        notificationEntry.remoteInputUri = null;
        notificationEntry.remoteInputMimeType = null;
        RemoteInput.setResultsSource(addFlags, getRemoteInputResultsSource());
        return addFlags;
    }

    public final Intent prepareRemoteInputFromData(RemoteInput remoteInput2, String str, Uri uri) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, uri);
        this.remoteInputController.grantInlineReplyUriPermission(this.entry.getSbn(), uri);
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addDataResultToIntent(remoteInput2, addFlags, hashMap);
        Bundle bundle = new Bundle();
        bundle.putString(remoteInput2.getResultKey(), this.view.getText().toString());
        RemoteInput.addResultsToIntent(getRemoteInputs(), addFlags, bundle);
        CharSequence label = this.entry.remoteInputAttachment.getClip().getDescription().getLabel();
        if (TextUtils.isEmpty(label)) {
            label = getResources().getString(R$string.remote_input_image_insertion_text);
        }
        if (!TextUtils.isEmpty(this.view.getText())) {
            label = '\"' + label + "\" " + this.view.getText();
        }
        this.entry.remoteInputText = label;
        RemoteInput.setResultsSource(addFlags, getRemoteInputResultsSource());
        return addFlags;
    }

    public final int getRemoteInputResultsSource() {
        return this.entry.editedSuggestionInfo == null ? 0 : 1;
    }
}
