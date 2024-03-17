package com.android.wm.shell.pip;

public class PipTransitionState {
    public boolean mInSwipePipToHomeTransition;
    public int mState = 0;

    public void setTransitionState(int i) {
        this.mState = i;
    }

    public int getTransitionState() {
        return this.mState;
    }

    public boolean isInPip() {
        int i = this.mState;
        return i >= 1 && i != 5;
    }

    public void setInSwipePipToHomeTransition(boolean z) {
        this.mInSwipePipToHomeTransition = z;
    }

    public boolean getInSwipePipToHomeTransition() {
        return this.mInSwipePipToHomeTransition;
    }

    public boolean shouldBlockResizeRequest() {
        int i = this.mState;
        return i < 3 || i == 5;
    }
}
