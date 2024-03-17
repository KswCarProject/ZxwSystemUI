package com.android.systemui.model;

import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.shared.system.QuickStepContract;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SysUiState implements Dumpable {
    public static final String TAG = "SysUiState";
    public final List<SysUiStateCallback> mCallbacks = new ArrayList();
    public int mFlags;
    public int mFlagsToClear = 0;
    public int mFlagsToSet = 0;

    public interface SysUiStateCallback {
        void onSystemUiStateChanged(int i);
    }

    public void addCallback(SysUiStateCallback sysUiStateCallback) {
        this.mCallbacks.add(sysUiStateCallback);
        sysUiStateCallback.onSystemUiStateChanged(this.mFlags);
    }

    public void removeCallback(SysUiStateCallback sysUiStateCallback) {
        this.mCallbacks.remove(sysUiStateCallback);
    }

    public int getFlags() {
        return this.mFlags;
    }

    public SysUiState setFlag(int i, boolean z) {
        if (z) {
            this.mFlagsToSet = i | this.mFlagsToSet;
        } else {
            this.mFlagsToClear = i | this.mFlagsToClear;
        }
        return this;
    }

    public void commitUpdate(int i) {
        updateFlags(i);
        this.mFlagsToSet = 0;
        this.mFlagsToClear = 0;
    }

    public final void updateFlags(int i) {
        if (i != 0) {
            String str = TAG;
            Log.w(str, "Ignoring flag update for display: " + i, new Throwable());
            return;
        }
        int i2 = this.mFlags;
        notifyAndSetSystemUiStateChanged((this.mFlagsToSet | i2) & (~this.mFlagsToClear), i2);
    }

    public final void notifyAndSetSystemUiStateChanged(int i, int i2) {
        if (i != i2) {
            this.mCallbacks.forEach(new SysUiState$$ExternalSyntheticLambda0(i));
            this.mFlags = i;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("SysUiState state:");
        printWriter.print("  mSysUiStateFlags=");
        printWriter.println(this.mFlags);
        printWriter.println("    " + QuickStepContract.getSystemUiStateString(this.mFlags));
        printWriter.print("    backGestureDisabled=");
        printWriter.println(QuickStepContract.isBackGestureDisabled(this.mFlags));
        printWriter.print("    assistantGestureDisabled=");
        printWriter.println(QuickStepContract.isAssistantGestureDisabled(this.mFlags));
    }
}
