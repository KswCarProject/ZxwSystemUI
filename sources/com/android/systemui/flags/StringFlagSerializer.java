package com.android.systemui.flags;

import org.jetbrains.annotations.NotNull;

/* compiled from: FlagSerializer.kt */
public final class StringFlagSerializer extends FlagSerializer<String> {
    @NotNull
    public static final StringFlagSerializer INSTANCE = new StringFlagSerializer();

    public StringFlagSerializer() {
        super("string", AnonymousClass1.INSTANCE, AnonymousClass2.INSTANCE);
    }
}
