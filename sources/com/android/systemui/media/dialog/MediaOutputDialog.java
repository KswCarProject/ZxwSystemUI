package com.android.systemui.media.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastSender;

public class MediaOutputDialog extends MediaOutputBaseDialog {
    public final UiEventLogger mUiEventLogger;

    public int getHeaderIconRes() {
        return 0;
    }

    public MediaOutputDialog(Context context, boolean z, BroadcastSender broadcastSender, MediaOutputController mediaOutputController, UiEventLogger uiEventLogger) {
        super(context, broadcastSender, mediaOutputController);
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new MediaOutputAdapter(this.mMediaOutputController, this);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUiEventLogger.log(MediaOutputEvent.MEDIA_OUTPUT_DIALOG_SHOW);
    }

    public IconCompat getHeaderIcon() {
        return this.mMediaOutputController.getHeaderIcon();
    }

    public int getHeaderIconSize() {
        return this.mContext.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_header_album_icon_size);
    }

    public CharSequence getHeaderText() {
        return this.mMediaOutputController.getHeaderTitle();
    }

    public CharSequence getHeaderSubtitle() {
        return this.mMediaOutputController.getHeaderSubTitle();
    }

    public Drawable getAppSourceIcon() {
        return this.mMediaOutputController.getAppSourceIcon();
    }

    public int getStopButtonVisibility() {
        boolean z;
        if (this.mMediaOutputController.getCurrentConnectedMediaDevice() != null) {
            MediaOutputController mediaOutputController = this.mMediaOutputController;
            z = mediaOutputController.isActiveRemoteDevice(mediaOutputController.getCurrentConnectedMediaDevice());
        } else {
            z = false;
        }
        boolean z2 = isBroadcastSupported() && this.mMediaOutputController.isPlaying();
        if (z || z2) {
            return 0;
        }
        return 8;
    }

    public boolean isBroadcastSupported() {
        return this.mMediaOutputController.isBroadcastSupported();
    }

    public CharSequence getStopButtonText() {
        int i = R$string.media_output_dialog_button_stop_casting;
        if (isBroadcastSupported() && this.mMediaOutputController.isPlaying() && !this.mMediaOutputController.isBluetoothLeBroadcastEnabled()) {
            i = R$string.media_output_broadcast;
        }
        return this.mContext.getText(i);
    }

    public void onStopButtonClick() {
        if (!isBroadcastSupported() || !this.mMediaOutputController.isPlaying()) {
            this.mMediaOutputController.releaseSession();
            dismiss();
        } else if (this.mMediaOutputController.isBluetoothLeBroadcastEnabled()) {
            stopLeBroadcast();
        } else if (!startLeBroadcastDialogForFirstTime()) {
            startLeBroadcast();
        }
    }

    @VisibleForTesting
    public enum MediaOutputEvent implements UiEventLogger.UiEventEnum {
        MEDIA_OUTPUT_DIALOG_SHOW(655);
        
        private final int mId;

        /* access modifiers changed from: public */
        MediaOutputEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
