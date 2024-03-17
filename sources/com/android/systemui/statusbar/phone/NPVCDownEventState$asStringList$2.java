package com.android.systemui.statusbar.phone;

import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NPVCDownEventState.kt */
public final class NPVCDownEventState$asStringList$2 extends Lambda implements Function0<List<? extends String>> {
    public final /* synthetic */ NPVCDownEventState this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NPVCDownEventState$asStringList$2(NPVCDownEventState nPVCDownEventState) {
        super(0);
        this.this$0 = nPVCDownEventState;
    }

    @NotNull
    public final List<String> invoke() {
        return CollectionsKt__CollectionsKt.listOf(NPVCDownEventStateKt.DATE_FORMAT.format(Long.valueOf(this.this$0.timeStamp)), String.valueOf(this.this$0.x), String.valueOf(this.this$0.y), String.valueOf(this.this$0.qsTouchAboveFalsingThreshold), String.valueOf(this.this$0.dozing), String.valueOf(this.this$0.collapsed), String.valueOf(this.this$0.canCollapseOnQQS), String.valueOf(this.this$0.listenForHeadsUp), String.valueOf(this.this$0.allowExpandForSmallExpansion), String.valueOf(this.this$0.touchSlopExceededBeforeDown), String.valueOf(this.this$0.lastEventSynthesized));
    }
}
