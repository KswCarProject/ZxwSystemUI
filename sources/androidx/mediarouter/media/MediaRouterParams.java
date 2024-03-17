package androidx.mediarouter.media;

import android.os.Bundle;

public class MediaRouterParams {
    public final int mDialogType;
    public final Bundle mExtras;
    public final boolean mMediaTransferReceiverEnabled;
    public final boolean mOutputSwitcherEnabled;
    public final boolean mTransferToLocalEnabled;

    public int getDialogType() {
        return this.mDialogType;
    }

    public boolean isMediaTransferReceiverEnabled() {
        return this.mMediaTransferReceiverEnabled;
    }

    public boolean isOutputSwitcherEnabled() {
        return this.mOutputSwitcherEnabled;
    }

    public boolean isTransferToLocalEnabled() {
        return this.mTransferToLocalEnabled;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }
}
