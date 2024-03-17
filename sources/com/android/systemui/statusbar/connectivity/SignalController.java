package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.util.Log;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.systemui.dump.DumpsysTableLogger;
import com.android.systemui.statusbar.connectivity.ConnectivityState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public abstract class SignalController<T extends ConnectivityState, I extends SignalIcon$IconGroup> {
    public static final boolean CHATTY = NetworkControllerImpl.CHATTY;
    public static final boolean DEBUG = NetworkControllerImpl.DEBUG;
    public final CallbackHandler mCallbackHandler;
    public final Context mContext;
    public final T mCurrentState = cleanState();
    public final ConnectivityState[] mHistory = new ConnectivityState[64];
    public int mHistoryIndex;
    public final T mLastState = cleanState();
    public final NetworkControllerImpl mNetworkController;
    public final String mTag;
    public final int mTransportType;

    public abstract T cleanState();

    public abstract void notifyListeners(SignalCallback signalCallback);

    public SignalController(String str, Context context, int i, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        this.mTag = "NetworkController." + str;
        this.mNetworkController = networkControllerImpl;
        this.mTransportType = i;
        this.mContext = context;
        this.mCallbackHandler = callbackHandler;
        for (int i2 = 0; i2 < 64; i2++) {
            this.mHistory[i2] = cleanState();
        }
    }

    public T getState() {
        return this.mCurrentState;
    }

    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        this.mCurrentState.inetCondition = bitSet2.get(this.mTransportType) ? 1 : 0;
        notifyListenersIfNecessary();
    }

    public void resetLastState() {
        this.mCurrentState.copyFrom(this.mLastState);
    }

    public boolean isDirty() {
        if (this.mLastState.equals(this.mCurrentState)) {
            return false;
        }
        if (!DEBUG) {
            return true;
        }
        String str = this.mTag;
        Log.d(str, "Change in state from: " + this.mLastState + "\n\tto: " + this.mCurrentState);
        return true;
    }

    public void saveLastState() {
        recordLastState();
        this.mCurrentState.time = System.currentTimeMillis();
        this.mLastState.copyFrom(this.mCurrentState);
    }

    public int getQsCurrentIconId() {
        T t = this.mCurrentState;
        if (t.connected) {
            int[][] iArr = getIcons().qsIcons;
            T t2 = this.mCurrentState;
            return iArr[t2.inetCondition][t2.level];
        } else if (t.enabled) {
            return getIcons().qsDiscState;
        } else {
            return getIcons().qsNullState;
        }
    }

    public int getCurrentIconId() {
        T t = this.mCurrentState;
        if (t.connected) {
            int[][] iArr = getIcons().sbIcons;
            T t2 = this.mCurrentState;
            return iArr[t2.inetCondition][t2.level];
        } else if (t.enabled) {
            return getIcons().sbDiscState;
        } else {
            return getIcons().sbNullState;
        }
    }

    public int getContentDescription() {
        if (this.mCurrentState.connected) {
            return getIcons().contentDesc[this.mCurrentState.level];
        }
        return getIcons().discContentDesc;
    }

    public void notifyListenersIfNecessary() {
        if (isDirty()) {
            saveLastState();
            notifyListeners();
        }
    }

    public final void notifyCallStateChange(IconState iconState, int i) {
        this.mCallbackHandler.setCallIndicator(iconState, i);
    }

    public CharSequence getTextIfExists(int i) {
        return i != 0 ? this.mContext.getText(i) : "";
    }

    public I getIcons() {
        return this.mCurrentState.iconGroup;
    }

    public void recordLastState() {
        this.mHistory[this.mHistoryIndex].copyFrom(this.mLastState);
        this.mHistoryIndex = (this.mHistoryIndex + 1) % 64;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("  - " + this.mTag + " -----");
        StringBuilder sb = new StringBuilder();
        sb.append("  Current State: ");
        sb.append(this.mCurrentState);
        printWriter.println(sb.toString());
        List<ConnectivityState> orderedHistoryExcludingCurrentState = getOrderedHistoryExcludingCurrentState();
        int i = 0;
        while (i < orderedHistoryExcludingCurrentState.size()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Previous State(");
            int i2 = i + 1;
            sb2.append(i2);
            sb2.append("): ");
            sb2.append(this.mHistory[i]);
            printWriter.println(sb2.toString());
            i = i2;
        }
    }

    public List<ConnectivityState> getOrderedHistoryExcludingCurrentState() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mHistory[i2].time != 0) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 < (this.mHistoryIndex + 64) - i) {
                return arrayList;
            }
            arrayList.add(this.mHistory[i3 & 63]);
        }
    }

    public List<ConnectivityState> getOrderedHistory() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mCurrentState);
        arrayList.addAll(getOrderedHistoryExcludingCurrentState());
        return arrayList;
    }

    public void dumpTableData(PrintWriter printWriter) {
        ArrayList arrayList = new ArrayList();
        List<ConnectivityState> orderedHistory = getOrderedHistory();
        for (int i = 0; i < orderedHistory.size(); i++) {
            arrayList.add(orderedHistory.get(i).tableData());
        }
        new DumpsysTableLogger(this.mTag, this.mCurrentState.tableColumns(), arrayList).printTableData(printWriter);
    }

    public final void notifyListeners() {
        notifyListeners(this.mCallbackHandler);
    }
}
