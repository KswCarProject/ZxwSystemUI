package com.android.keyguard;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: KeyguardListenModel.kt */
public abstract class KeyguardListenModel {
    public /* synthetic */ KeyguardListenModel(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract boolean getListening();

    public abstract long getTimeMillis();

    public KeyguardListenModel() {
    }
}
