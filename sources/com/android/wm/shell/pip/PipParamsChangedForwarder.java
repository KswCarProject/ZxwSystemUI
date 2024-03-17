package com.android.wm.shell.pip;

import android.app.RemoteAction;
import java.util.ArrayList;
import java.util.List;

public class PipParamsChangedForwarder {
    public final List<PipParamsChangedCallback> mPipParamsChangedListeners = new ArrayList();

    public interface PipParamsChangedCallback {
        void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
        }

        void onAspectRatioChanged(float f) {
        }

        void onExpandedAspectRatioChanged(float f) {
        }

        void onSubtitleChanged(String str) {
        }

        void onTitleChanged(String str) {
        }
    }

    public void addListener(PipParamsChangedCallback pipParamsChangedCallback) {
        if (!this.mPipParamsChangedListeners.contains(pipParamsChangedCallback)) {
            this.mPipParamsChangedListeners.add(pipParamsChangedCallback);
        }
    }

    public void notifyAspectRatioChanged(float f) {
        for (PipParamsChangedCallback onAspectRatioChanged : this.mPipParamsChangedListeners) {
            onAspectRatioChanged.onAspectRatioChanged(f);
        }
    }

    public void notifyExpandedAspectRatioChanged(float f) {
        for (PipParamsChangedCallback onExpandedAspectRatioChanged : this.mPipParamsChangedListeners) {
            onExpandedAspectRatioChanged.onExpandedAspectRatioChanged(f);
        }
    }

    public void notifyTitleChanged(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? null : charSequence.toString();
        for (PipParamsChangedCallback onTitleChanged : this.mPipParamsChangedListeners) {
            onTitleChanged.onTitleChanged(charSequence2);
        }
    }

    public void notifySubtitleChanged(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? null : charSequence.toString();
        for (PipParamsChangedCallback onSubtitleChanged : this.mPipParamsChangedListeners) {
            onSubtitleChanged.onSubtitleChanged(charSequence2);
        }
    }

    public void notifyActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
        for (PipParamsChangedCallback onActionsChanged : this.mPipParamsChangedListeners) {
            onActionsChanged.onActionsChanged(list, remoteAction);
        }
    }
}
