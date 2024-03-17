package com.android.systemui.flags;

/* compiled from: FlagSerializer.kt */
public final class InvalidFlagStorageException extends Exception {
    public InvalidFlagStorageException() {
        super("Data found but is invalid");
    }
}
