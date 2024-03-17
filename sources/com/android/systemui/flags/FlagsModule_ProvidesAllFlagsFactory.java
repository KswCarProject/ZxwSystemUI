package com.android.systemui.flags;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Map;

public final class FlagsModule_ProvidesAllFlagsFactory implements Factory<Map<Integer, Flag<?>>> {

    public static final class InstanceHolder {
        public static final FlagsModule_ProvidesAllFlagsFactory INSTANCE = new FlagsModule_ProvidesAllFlagsFactory();
    }

    public Map<Integer, Flag<?>> get() {
        return providesAllFlags();
    }

    public static FlagsModule_ProvidesAllFlagsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Map<Integer, Flag<?>> providesAllFlags() {
        return (Map) Preconditions.checkNotNullFromProvides(FlagsModule.providesAllFlags());
    }
}
