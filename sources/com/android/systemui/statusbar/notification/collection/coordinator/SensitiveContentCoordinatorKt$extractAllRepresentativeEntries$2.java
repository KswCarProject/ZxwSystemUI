package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugMetadata(c = "com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2", f = "SensitiveContentCoordinator.kt", l = {120, 122}, m = "invokeSuspend")
/* compiled from: SensitiveContentCoordinator.kt */
public final class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super NotificationEntry>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ ListEntry $listEntry;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(ListEntry listEntry, Continuation<? super SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2> continuation) {
        super(2, continuation);
        this.$listEntry = listEntry;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(this.$listEntry, continuation);
        sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2.L$0 = obj;
        return sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceScope<? super NotificationEntry> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
        return ((SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: kotlin.sequences.SequenceScope} */
    /* JADX WARNING: Multi-variable type inference failed */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r5) {
        /*
            r4 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r4.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0022
            if (r1 == r3) goto L_0x001a
            if (r1 != r2) goto L_0x0012
            kotlin.ResultKt.throwOnFailure(r5)
            goto L_0x005a
        L_0x0012:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L_0x001a:
            java.lang.Object r1 = r4.L$0
            kotlin.sequences.SequenceScope r1 = (kotlin.sequences.SequenceScope) r1
            kotlin.ResultKt.throwOnFailure(r5)
            goto L_0x003e
        L_0x0022:
            kotlin.ResultKt.throwOnFailure(r5)
            java.lang.Object r5 = r4.L$0
            r1 = r5
            kotlin.sequences.SequenceScope r1 = (kotlin.sequences.SequenceScope) r1
            com.android.systemui.statusbar.notification.collection.ListEntry r5 = r4.$listEntry
            com.android.systemui.statusbar.notification.collection.NotificationEntry r5 = r5.getRepresentativeEntry()
            if (r5 != 0) goto L_0x0033
            goto L_0x003e
        L_0x0033:
            r4.L$0 = r1
            r4.label = r3
            java.lang.Object r5 = r1.yield(r5, r4)
            if (r5 != r0) goto L_0x003e
            return r0
        L_0x003e:
            com.android.systemui.statusbar.notification.collection.ListEntry r5 = r4.$listEntry
            boolean r3 = r5 instanceof com.android.systemui.statusbar.notification.collection.GroupEntry
            if (r3 == 0) goto L_0x005a
            com.android.systemui.statusbar.notification.collection.GroupEntry r5 = (com.android.systemui.statusbar.notification.collection.GroupEntry) r5
            java.util.List r5 = r5.getChildren()
            kotlin.sequences.Sequence r5 = com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt.extractAllRepresentativeEntries((java.util.List<? extends com.android.systemui.statusbar.notification.collection.ListEntry>) r5)
            r3 = 0
            r4.L$0 = r3
            r4.label = r2
            java.lang.Object r4 = r1.yieldAll(r5, (kotlin.coroutines.Continuation<? super kotlin.Unit>) r4)
            if (r4 != r0) goto L_0x005a
            return r0
        L_0x005a:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
