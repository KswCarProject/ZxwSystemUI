package com.android.systemui.statusbar.notification.row;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import org.jetbrains.annotations.NotNull;

/* compiled from: ChannelEditorDialogController.kt */
public final class ChannelEditorDialog extends Dialog {
    public ChannelEditorDialog(@NotNull Context context) {
        super(context);
    }

    public final void updateDoneButtonText(boolean z) {
        int i;
        TextView textView = (TextView) findViewById(R$id.done_button);
        if (textView != null) {
            if (z) {
                i = R$string.inline_ok_button;
            } else {
                i = R$string.inline_done_button;
            }
            textView.setText(i);
        }
    }

    /* compiled from: ChannelEditorDialogController.kt */
    public static final class Builder {
        public Context context;

        @NotNull
        public final Builder setContext(@NotNull Context context2) {
            this.context = context2;
            return this;
        }

        @NotNull
        public final ChannelEditorDialog build() {
            Context context2 = this.context;
            if (context2 == null) {
                context2 = null;
            }
            return new ChannelEditorDialog(context2);
        }
    }
}
