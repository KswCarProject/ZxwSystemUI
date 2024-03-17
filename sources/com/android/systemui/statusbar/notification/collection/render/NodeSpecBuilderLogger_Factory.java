package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NodeSpecBuilderLogger_Factory implements Factory<NodeSpecBuilderLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public NodeSpecBuilderLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public NodeSpecBuilderLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NodeSpecBuilderLogger_Factory create(Provider<LogBuffer> provider) {
        return new NodeSpecBuilderLogger_Factory(provider);
    }

    public static NodeSpecBuilderLogger newInstance(LogBuffer logBuffer) {
        return new NodeSpecBuilderLogger(logBuffer);
    }
}
