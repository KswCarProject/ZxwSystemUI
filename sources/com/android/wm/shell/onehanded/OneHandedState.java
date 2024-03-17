package com.android.wm.shell.onehanded;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OneHandedState {
    public static final String TAG = "OneHandedState";
    public static int sCurrentState;
    public List<OnStateChangedListener> mStateChangeListeners = new ArrayList();

    public interface OnStateChangedListener {
        void onStateChanged(int i) {
        }
    }

    public OneHandedState() {
        sCurrentState = 0;
    }

    public void addSListeners(OnStateChangedListener onStateChangedListener) {
        this.mStateChangeListeners.add(onStateChangedListener);
    }

    public int getState() {
        return sCurrentState;
    }

    public boolean isTransitioning() {
        int i = sCurrentState;
        return i == 1 || i == 3;
    }

    public boolean isInOneHanded() {
        return sCurrentState == 2;
    }

    public void setState(int i) {
        sCurrentState = i;
        if (!this.mStateChangeListeners.isEmpty()) {
            this.mStateChangeListeners.forEach(new OneHandedState$$ExternalSyntheticLambda0(i));
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.println("  sCurrentState=" + sCurrentState);
    }
}
