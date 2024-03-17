package com.android.systemui.util;

import android.view.View;
import android.view.ViewGroup;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugMetadata(c = "com.android.systemui.util.ConvenienceExtensionsKt$children$1", f = "ConvenienceExtensions.kt", l = {26}, m = "invokeSuspend")
/* compiled from: ConvenienceExtensions.kt */
public final class ConvenienceExtensionsKt$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    public final /* synthetic */ ViewGroup $this_children;
    public int I$0;
    public int I$1;
    private /* synthetic */ Object L$0;
    public int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ConvenienceExtensionsKt$children$1(ViewGroup viewGroup, Continuation<? super ConvenienceExtensionsKt$children$1> continuation) {
        super(2, continuation);
        this.$this_children = viewGroup;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ConvenienceExtensionsKt$children$1 convenienceExtensionsKt$children$1 = new ConvenienceExtensionsKt$children$1(this.$this_children, continuation);
        convenienceExtensionsKt$children$1.L$0 = obj;
        return convenienceExtensionsKt$children$1;
    }

    @Nullable
    public final Object invoke(@NotNull SequenceScope<? super View> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        SequenceScope sequenceScope;
        int i;
        int i2;
        Object coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = this.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            i2 = 0;
            i = this.$this_children.getChildCount();
            sequenceScope = (SequenceScope) this.L$0;
        } else if (i3 == 1) {
            int i4 = this.I$1;
            int i5 = this.I$0;
            sequenceScope = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            int i6 = i5;
            i = i4;
            i2 = i6;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (i2 < i) {
            int i7 = i2 + 1;
            View childAt = this.$this_children.getChildAt(i2);
            this.L$0 = sequenceScope;
            this.I$0 = i7;
            this.I$1 = i;
            this.label = 1;
            if (sequenceScope.yield(childAt, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
            i2 = i7;
        }
        return Unit.INSTANCE;
    }
}
