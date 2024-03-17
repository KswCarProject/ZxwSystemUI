package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugMetadata(c = "com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1", f = "NotificationStackSizeCalculator.kt", l = {197, 220}, m = "invokeSuspend")
/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super NotificationStackSizeCalculator.StackHeight>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ float $shelfHeight;
    public final /* synthetic */ NotificationStackScrollLayout $stack;
    public float F$0;
    public int I$0;
    private /* synthetic */ Object L$0;
    public Object L$1;
    public Object L$2;
    public Object L$3;
    public Object L$4;
    public Object L$5;
    public Object L$6;
    public boolean Z$0;
    public int label;
    public final /* synthetic */ NotificationStackSizeCalculator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1(NotificationStackSizeCalculator notificationStackSizeCalculator, NotificationStackScrollLayout notificationStackScrollLayout, float f, Continuation<? super NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1> continuation) {
        super(2, continuation);
        this.this$0 = notificationStackSizeCalculator;
        this.$stack = notificationStackScrollLayout;
        this.$shelfHeight = f;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1 notificationStackSizeCalculator$computeHeightPerNotificationLimit$1 = new NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1(this.this$0, this.$stack, this.$shelfHeight, continuation);
        notificationStackSizeCalculator$computeHeightPerNotificationLimit$1.L$0 = obj;
        return notificationStackSizeCalculator$computeHeightPerNotificationLimit$1;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceScope<? super NotificationStackSizeCalculator.StackHeight> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
        return ((NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v11, resolved type: kotlin.sequences.SequenceScope} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x00d0  */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r22) {
        /*
            r21 = this;
            r0 = r21
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            java.lang.String r3 = "NotifStackSizeCalc"
            r4 = 0
            r5 = 2
            r6 = 1
            if (r2 == 0) goto L_0x0065
            if (r2 == r6) goto L_0x004f
            if (r2 != r5) goto L_0x0047
            int r2 = r0.I$0
            float r6 = r0.F$0
            boolean r7 = r0.Z$0
            java.lang.Object r8 = r0.L$6
            java.util.Iterator r8 = (java.util.Iterator) r8
            java.lang.Object r9 = r0.L$5
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r9 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout) r9
            java.lang.Object r10 = r0.L$4
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator r10 = (com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator) r10
            java.lang.Object r11 = r0.L$3
            kotlin.jvm.internal.Ref$ObjectRef r11 = (kotlin.jvm.internal.Ref$ObjectRef) r11
            java.lang.Object r12 = r0.L$2
            kotlin.jvm.internal.Ref$FloatRef r12 = (kotlin.jvm.internal.Ref$FloatRef) r12
            java.lang.Object r13 = r0.L$1
            java.util.List r13 = (java.util.List) r13
            java.lang.Object r14 = r0.L$0
            kotlin.sequences.SequenceScope r14 = (kotlin.sequences.SequenceScope) r14
            kotlin.ResultKt.throwOnFailure(r22)
            r4 = r2
            r2 = r7
            r15 = r10
            r10 = r13
            r13 = r8
            r8 = r14
            r14 = r9
            r9 = r5
            r20 = r12
            r12 = r11
            r11 = r20
            goto L_0x017f
        L_0x0047:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L_0x004f:
            boolean r2 = r0.Z$0
            java.lang.Object r6 = r0.L$3
            kotlin.jvm.internal.Ref$ObjectRef r6 = (kotlin.jvm.internal.Ref$ObjectRef) r6
            java.lang.Object r7 = r0.L$2
            kotlin.jvm.internal.Ref$FloatRef r7 = (kotlin.jvm.internal.Ref$FloatRef) r7
            java.lang.Object r8 = r0.L$1
            java.util.List r8 = (java.util.List) r8
            java.lang.Object r9 = r0.L$0
            kotlin.sequences.SequenceScope r9 = (kotlin.sequences.SequenceScope) r9
            kotlin.ResultKt.throwOnFailure(r22)
            goto L_0x00b0
        L_0x0065:
            kotlin.ResultKt.throwOnFailure(r22)
            java.lang.Object r2 = r0.L$0
            r9 = r2
            kotlin.sequences.SequenceScope r9 = (kotlin.sequences.SequenceScope) r9
            boolean r2 = com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculatorKt.DEBUG
            if (r2 == 0) goto L_0x0078
            java.lang.String r2 = "computeHeightPerNotificationLimit"
            android.util.Log.d(r3, r2)
        L_0x0078:
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator r2 = r0.this$0
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r7 = r0.$stack
            kotlin.sequences.Sequence r2 = r2.showableChildren(r7)
            java.util.List r8 = kotlin.sequences.SequencesKt___SequencesKt.toList(r2)
            kotlin.jvm.internal.Ref$FloatRef r7 = new kotlin.jvm.internal.Ref$FloatRef
            r7.<init>()
            kotlin.jvm.internal.Ref$ObjectRef r2 = new kotlin.jvm.internal.Ref$ObjectRef
            r2.<init>()
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator r10 = r0.this$0
            boolean r10 = r10.onLockscreen()
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$StackHeight r11 = new com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$StackHeight
            float r12 = r0.$shelfHeight
            r11.<init>(r4, r12)
            r0.L$0 = r9
            r0.L$1 = r8
            r0.L$2 = r7
            r0.L$3 = r2
            r0.Z$0 = r10
            r0.label = r6
            java.lang.Object r6 = r9.yield(r11, r0)
            if (r6 != r1) goto L_0x00ae
            return r1
        L_0x00ae:
            r6 = r2
            r2 = r10
        L_0x00b0:
            r10 = r8
            java.lang.Iterable r10 = (java.lang.Iterable) r10
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator r11 = r0.this$0
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r12 = r0.$stack
            float r13 = r0.$shelfHeight
            r14 = 0
            java.util.Iterator r10 = r10.iterator()
            r15 = r11
            r11 = r7
            r20 = r12
            r12 = r6
            r6 = r13
            r13 = r10
            r10 = r8
            r8 = r9
            r9 = r14
            r14 = r20
        L_0x00ca:
            boolean r7 = r13.hasNext()
            if (r7 == 0) goto L_0x0184
            java.lang.Object r7 = r13.next()
            int r4 = r9 + 1
            if (r9 >= 0) goto L_0x00db
            kotlin.collections.CollectionsKt__CollectionsKt.throwIndexOverflow()
        L_0x00db:
            com.android.systemui.statusbar.notification.row.ExpandableView r7 = (com.android.systemui.statusbar.notification.row.ExpandableView) r7
            float r5 = r11.element
            r21 = r7
            T r7 = r12.element
            r16 = r7
            com.android.systemui.statusbar.notification.row.ExpandableView r16 = (com.android.systemui.statusbar.notification.row.ExpandableView) r16
            r22 = r21
            r7 = r15
            r21 = r1
            r1 = r8
            r8 = r22
            r17 = r9
            r18 = r10
            r10 = r16
            r16 = r13
            r13 = r11
            r11 = r14
            r19 = r0
            r0 = r12
            r12 = r2
            float r7 = r7.spaceNeeded(r8, r9, r10, r11, r12)
            float r5 = r5 + r7
            r13.element = r5
            r7 = r22
            r0.element = r7
            int r5 = kotlin.collections.CollectionsKt__CollectionsKt.getLastIndex(r18)
            r8 = r17
            if (r8 != r5) goto L_0x0114
            r5 = r18
            r7 = 0
            goto L_0x0121
        L_0x0114:
            r5 = r18
            java.lang.Object r9 = r5.get(r4)
            com.android.systemui.statusbar.notification.row.ExpandableView r9 = (com.android.systemui.statusbar.notification.row.ExpandableView) r9
            float r7 = r15.calculateGapAndDividerHeight(r14, r7, r9, r4)
            float r7 = r7 + r6
        L_0x0121:
            boolean r9 = com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculatorKt.DEBUG
            if (r9 == 0) goto L_0x014d
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "i="
            r9.append(r10)
            r9.append(r8)
            java.lang.String r8 = " notificationsHeight="
            r9.append(r8)
            float r8 = r13.element
            r9.append(r8)
            java.lang.String r8 = " shelfHeightWithSpaceBefore="
            r9.append(r8)
            r9.append(r7)
            java.lang.String r8 = r9.toString()
            android.util.Log.d(r3, r8)
        L_0x014d:
            com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$StackHeight r8 = new com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$StackHeight
            float r9 = r13.element
            r8.<init>(r9, r7)
            r7 = r19
            r7.L$0 = r1
            r7.L$1 = r5
            r7.L$2 = r13
            r7.L$3 = r0
            r7.L$4 = r15
            r7.L$5 = r14
            r10 = r16
            r7.L$6 = r10
            r7.Z$0 = r2
            r7.F$0 = r6
            r7.I$0 = r4
            r9 = 2
            r7.label = r9
            java.lang.Object r8 = r1.yield(r8, r7)
            r11 = r21
            if (r8 != r11) goto L_0x0178
            return r11
        L_0x0178:
            r12 = r0
            r8 = r1
            r0 = r7
            r1 = r11
            r11 = r13
            r13 = r10
            r10 = r5
        L_0x017f:
            r5 = r9
            r9 = r4
            r4 = 0
            goto L_0x00ca
        L_0x0184:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
