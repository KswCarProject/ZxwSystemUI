package com.android.systemui.qs.external;

import android.util.Log;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$TileServiceRequestCommand$execute$1<T> implements Consumer {
    public static final TileServiceRequestController$TileServiceRequestCommand$execute$1<T> INSTANCE = new TileServiceRequestController$TileServiceRequestCommand$execute$1<>();

    public /* bridge */ /* synthetic */ void accept(Object obj) {
        accept(((Number) obj).intValue());
    }

    public final void accept(int i) {
        Log.d("TileServiceRequestController", Intrinsics.stringPlus("Response: ", Integer.valueOf(i)));
    }
}
