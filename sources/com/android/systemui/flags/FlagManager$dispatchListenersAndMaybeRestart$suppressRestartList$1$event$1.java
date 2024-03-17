package com.android.systemui.flags;

import com.android.systemui.flags.FlagListenable;
import kotlin.jvm.internal.Ref$BooleanRef;

/* compiled from: FlagManager.kt */
public final class FlagManager$dispatchListenersAndMaybeRestart$suppressRestartList$1$event$1 implements FlagListenable.FlagEvent {
    public final /* synthetic */ Ref$BooleanRef $didRequestNoRestart;
    public final /* synthetic */ int $id;
    public final int flagId;

    public FlagManager$dispatchListenersAndMaybeRestart$suppressRestartList$1$event$1(int i, Ref$BooleanRef ref$BooleanRef) {
        this.$id = i;
        this.$didRequestNoRestart = ref$BooleanRef;
        this.flagId = i;
    }

    public void requestNoRestart() {
        this.$didRequestNoRestart.element = true;
    }
}
