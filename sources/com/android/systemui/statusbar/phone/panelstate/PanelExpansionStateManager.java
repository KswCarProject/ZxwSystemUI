package com.android.systemui.statusbar.phone.panelstate;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: PanelExpansionStateManager.kt */
public final class PanelExpansionStateManager {
    public float dragDownPxAmount;
    public boolean expanded;
    @NotNull
    public final List<PanelExpansionListener> expansionListeners = new ArrayList();
    public float fraction;
    public int state;
    @NotNull
    public final List<PanelStateListener> stateListeners = new ArrayList();
    public boolean tracking;

    public final void debugLog(String str) {
    }

    public final void addExpansionListener(@NotNull PanelExpansionListener panelExpansionListener) {
        this.expansionListeners.add(panelExpansionListener);
        panelExpansionListener.onPanelExpansionChanged(new PanelExpansionChangeEvent(this.fraction, this.expanded, this.tracking, this.dragDownPxAmount));
    }

    public final void removeExpansionListener(@NotNull PanelExpansionListener panelExpansionListener) {
        this.expansionListeners.remove(panelExpansionListener);
    }

    public final void addStateListener(@NotNull PanelStateListener panelStateListener) {
        this.stateListeners.add(panelStateListener);
    }

    public final boolean isClosed() {
        return this.state == 0;
    }

    public final void onPanelExpansionChanged(float f, boolean z, boolean z2, float f2) {
        boolean z3;
        boolean z4 = true;
        if (!Float.isNaN(f)) {
            int i = this.state;
            this.fraction = f;
            this.expanded = z;
            this.tracking = z2;
            this.dragDownPxAmount = f2;
            if (z) {
                if (i == 0) {
                    updateStateInternal(1);
                }
                if (f < 1.0f) {
                    z4 = false;
                }
                z3 = false;
            } else {
                z3 = true;
                z4 = false;
            }
            if (z4 && !z2) {
                updateStateInternal(2);
            } else if (z3 && !z2 && this.state != 0) {
                updateStateInternal(0);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("panelExpansionChanged:start state=");
            sb.append(PanelExpansionStateManagerKt.stateToString(i));
            sb.append(" end state=");
            sb.append(PanelExpansionStateManagerKt.stateToString(this.state));
            sb.append(" f=");
            sb.append(f);
            sb.append(" expanded=");
            sb.append(z);
            sb.append(" tracking=");
            sb.append(z2);
            sb.append("drawDownPxAmount=");
            sb.append(f2);
            sb.append(' ');
            String str = "";
            sb.append(z4 ? " fullyOpened" : str);
            sb.append(' ');
            if (z3) {
                str = " fullyClosed";
            }
            sb.append(str);
            debugLog(sb.toString());
            PanelExpansionChangeEvent panelExpansionChangeEvent = new PanelExpansionChangeEvent(f, z, z2, f2);
            for (PanelExpansionListener onPanelExpansionChanged : this.expansionListeners) {
                onPanelExpansionChanged.onPanelExpansionChanged(panelExpansionChangeEvent);
            }
            return;
        }
        throw new IllegalArgumentException("fraction cannot be NaN".toString());
    }

    public final void updateState(int i) {
        debugLog("update state: " + PanelExpansionStateManagerKt.stateToString(this.state) + " -> " + PanelExpansionStateManagerKt.stateToString(i));
        if (this.state != i) {
            updateStateInternal(i);
        }
    }

    public final void updateStateInternal(int i) {
        debugLog("go state: " + PanelExpansionStateManagerKt.stateToString(this.state) + " -> " + PanelExpansionStateManagerKt.stateToString(i));
        this.state = i;
        for (PanelStateListener onPanelStateChanged : this.stateListeners) {
            onPanelStateChanged.onPanelStateChanged(i);
        }
    }
}
