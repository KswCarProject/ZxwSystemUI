package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.SignalIcon$IconGroup;
import java.util.BitSet;

public class EthernetSignalController extends SignalController<ConnectivityState, SignalIcon$IconGroup> {
    public EthernetSignalController(Context context, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        super("EthernetSignalController", context, 3, callbackHandler, networkControllerImpl);
        T t = this.mCurrentState;
        T t2 = this.mLastState;
        int[][] iArr = EthernetIcons.ETHERNET_ICONS;
        int[] iArr2 = AccessibilityContentDescriptions.ETHERNET_CONNECTION_VALUES;
        SignalIcon$IconGroup signalIcon$IconGroup = new SignalIcon$IconGroup("Ethernet Icons", iArr, (int[][]) null, iArr2, 0, 0, 0, 0, iArr2[0]);
        t2.iconGroup = signalIcon$IconGroup;
        t.iconGroup = signalIcon$IconGroup;
    }

    public void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        this.mCurrentState.connected = bitSet.get(this.mTransportType);
        super.updateConnectivity(bitSet, bitSet2);
    }

    public void notifyListeners(SignalCallback signalCallback) {
        signalCallback.setEthernetIndicators(new IconState(this.mCurrentState.connected, getCurrentIconId(), getTextIfExists(getContentDescription()).toString()));
    }

    public int getContentDescription() {
        if (this.mCurrentState.connected) {
            return getIcons().contentDesc[1];
        }
        return getIcons().discContentDesc;
    }

    public ConnectivityState cleanState() {
        return new ConnectivityState();
    }
}
