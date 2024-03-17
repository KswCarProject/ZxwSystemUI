package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;

public final class RenderStageManager_Factory implements Factory<RenderStageManager> {

    public static final class InstanceHolder {
        public static final RenderStageManager_Factory INSTANCE = new RenderStageManager_Factory();
    }

    public RenderStageManager get() {
        return newInstance();
    }

    public static RenderStageManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static RenderStageManager newInstance() {
        return new RenderStageManager();
    }
}
