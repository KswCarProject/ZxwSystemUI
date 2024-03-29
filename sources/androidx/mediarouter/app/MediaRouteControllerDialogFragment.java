package androidx.mediarouter.app;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.mediarouter.media.MediaRouteSelector;

public class MediaRouteControllerDialogFragment extends DialogFragment {
    public Dialog mDialog;
    public MediaRouteSelector mSelector;
    public boolean mUseDynamicGroup = false;

    public MediaRouteControllerDialogFragment() {
        setCancelable(true);
    }

    public final void ensureRouteSelector() {
        if (this.mSelector == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.mSelector = MediaRouteSelector.fromBundle(arguments.getBundle("selector"));
            }
            if (this.mSelector == null) {
                this.mSelector = MediaRouteSelector.EMPTY;
            }
        }
    }

    public void setUseDynamicGroup(boolean z) {
        if (this.mDialog == null) {
            this.mUseDynamicGroup = z;
            return;
        }
        throw new IllegalStateException("This must be called before creating dialog");
    }

    public void setRouteSelector(MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector != null) {
            ensureRouteSelector();
            if (!this.mSelector.equals(mediaRouteSelector)) {
                this.mSelector = mediaRouteSelector;
                Bundle arguments = getArguments();
                if (arguments == null) {
                    arguments = new Bundle();
                }
                arguments.putBundle("selector", mediaRouteSelector.asBundle());
                setArguments(arguments);
                Dialog dialog = this.mDialog;
                if (dialog != null && this.mUseDynamicGroup) {
                    ((MediaRouteDynamicControllerDialog) dialog).setRouteSelector(mediaRouteSelector);
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("selector must not be null");
    }

    public MediaRouteDynamicControllerDialog onCreateDynamicControllerDialog(Context context) {
        return new MediaRouteDynamicControllerDialog(context);
    }

    public MediaRouteControllerDialog onCreateControllerDialog(Context context, Bundle bundle) {
        return new MediaRouteControllerDialog(context);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        if (this.mUseDynamicGroup) {
            MediaRouteDynamicControllerDialog onCreateDynamicControllerDialog = onCreateDynamicControllerDialog(getContext());
            this.mDialog = onCreateDynamicControllerDialog;
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = onCreateDynamicControllerDialog;
            onCreateDynamicControllerDialog.setRouteSelector(this.mSelector);
        } else {
            this.mDialog = onCreateControllerDialog(getContext(), bundle);
        }
        return this.mDialog;
    }

    public void onStop() {
        super.onStop();
        Dialog dialog = this.mDialog;
        if (dialog != null && !this.mUseDynamicGroup) {
            ((MediaRouteControllerDialog) dialog).clearGroupListAnimation(false);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Dialog dialog = this.mDialog;
        if (dialog == null) {
            return;
        }
        if (this.mUseDynamicGroup) {
            ((MediaRouteDynamicControllerDialog) dialog).updateLayout();
        } else {
            ((MediaRouteControllerDialog) dialog).updateLayout();
        }
    }
}
