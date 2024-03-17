package com.android.systemui.statusbar.phone.panelstate;

import dagger.internal.Factory;

public final class PanelExpansionStateManager_Factory implements Factory<PanelExpansionStateManager> {

    public static final class InstanceHolder {
        public static final PanelExpansionStateManager_Factory INSTANCE = new PanelExpansionStateManager_Factory();
    }

    public PanelExpansionStateManager get() {
        return newInstance();
    }

    public static PanelExpansionStateManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PanelExpansionStateManager newInstance() {
        return new PanelExpansionStateManager();
    }
}
