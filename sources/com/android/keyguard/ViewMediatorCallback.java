package com.android.keyguard;

public interface ViewMediatorCallback {
    CharSequence consumeCustomMessage();

    int getBouncerPromptReason();

    boolean isScreenOn();

    void keyguardDone(boolean z, int i);

    void keyguardDoneDrawing();

    void keyguardDonePending(boolean z, int i);

    void keyguardGone();

    void onCancelClicked();

    void playTrustedSound();

    void readyForKeyguardDone();

    void resetKeyguard();

    void setNeedsInput(boolean z);

    void userActivity();
}
