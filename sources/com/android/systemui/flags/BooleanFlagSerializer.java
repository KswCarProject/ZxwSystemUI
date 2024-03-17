package com.android.systemui.flags;

import org.jetbrains.annotations.NotNull;

/* compiled from: FlagSerializer.kt */
public final class BooleanFlagSerializer extends FlagSerializer<Boolean> {
    @NotNull
    public static final BooleanFlagSerializer INSTANCE = new BooleanFlagSerializer();

    public BooleanFlagSerializer() {
        super("boolean", AnonymousClass1.INSTANCE, AnonymousClass2.INSTANCE);
    }
}
