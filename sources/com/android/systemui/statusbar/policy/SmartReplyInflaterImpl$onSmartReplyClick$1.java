package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.util.Log;
import android.widget.Button;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyInflaterImpl$onSmartReplyClick$1 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ Button $button;
    public final /* synthetic */ CharSequence $choice;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ int $replyIndex;
    public final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    public final /* synthetic */ SmartReplyView $smartReplyView;
    public final /* synthetic */ SmartReplyInflaterImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SmartReplyInflaterImpl$onSmartReplyClick$1(SmartReplyInflaterImpl smartReplyInflaterImpl, SmartReplyView.SmartReplies smartReplies, Button button, CharSequence charSequence, int i, NotificationEntry notificationEntry, SmartReplyView smartReplyView) {
        super(0);
        this.this$0 = smartReplyInflaterImpl;
        this.$smartReplies = smartReplies;
        this.$button = button;
        this.$choice = charSequence;
        this.$replyIndex = i;
        this.$entry = notificationEntry;
        this.$smartReplyView = smartReplyView;
    }

    @NotNull
    public final Boolean invoke() {
        if (this.this$0.constants.getEffectiveEditChoicesBeforeSending(this.$smartReplies.remoteInput.getEditChoicesBeforeSending())) {
            NotificationRemoteInputManager access$getRemoteInputManager$p = this.this$0.remoteInputManager;
            Button button = this.$button;
            SmartReplyView.SmartReplies smartReplies = this.$smartReplies;
            RemoteInput remoteInput = smartReplies.remoteInput;
            access$getRemoteInputManager$p.activateRemoteInput(button, new RemoteInput[]{remoteInput}, remoteInput, smartReplies.pendingIntent, new NotificationEntry.EditedSuggestionInfo(this.$choice, this.$replyIndex));
        } else {
            this.this$0.smartReplyController.smartReplySent(this.$entry, this.$replyIndex, this.$button.getText(), NotificationLogger.getNotificationLocation(this.$entry).toMetricsEventEnum(), false);
            this.$entry.setHasSentReply();
            try {
                this.$smartReplies.pendingIntent.send(this.this$0.context, 0, this.this$0.createRemoteInputIntent(this.$smartReplies, this.$choice));
            } catch (PendingIntent.CanceledException e) {
                Log.w("SmartReplyViewInflater", "Unable to send smart reply", e);
            }
            this.$smartReplyView.hideSmartSuggestions();
        }
        return Boolean.FALSE;
    }
}
