package com.android.systemui.statusbar.phone;

import com.android.systemui.util.collection.RingBuffer;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: NPVCDownEventState.kt */
public final class NPVCDownEventState {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final List<String> TABLE_HEADERS = CollectionsKt__CollectionsKt.listOf("Timestamp", "X", "Y", "QSTouchAboveFalsingThreshold", "Dozing", "Collapsed", "CanCollapseOnQQS", "ListenForHeadsUp", "AllowExpandForSmallExpansion", "TouchSlopExceededBeforeDown", "LastEventSynthesized");
    public boolean allowExpandForSmallExpansion;
    @NotNull
    public final Lazy asStringList$delegate;
    public boolean canCollapseOnQQS;
    public boolean collapsed;
    public boolean dozing;
    public boolean lastEventSynthesized;
    public boolean listenForHeadsUp;
    public boolean qsTouchAboveFalsingThreshold;
    public long timeStamp;
    public boolean touchSlopExceededBeforeDown;
    public float x;
    public float y;

    public NPVCDownEventState(long j, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
        this.timeStamp = j;
        this.x = f;
        this.y = f2;
        this.qsTouchAboveFalsingThreshold = z;
        this.dozing = z2;
        this.collapsed = z3;
        this.canCollapseOnQQS = z4;
        this.listenForHeadsUp = z5;
        this.allowExpandForSmallExpansion = z6;
        this.touchSlopExceededBeforeDown = z7;
        this.lastEventSynthesized = z8;
        this.asStringList$delegate = LazyKt__LazyJVMKt.lazy(new NPVCDownEventState$asStringList$2(this));
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ NPVCDownEventState(long r14, float r16, float r17, boolean r18, boolean r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24, boolean r25, int r26, kotlin.jvm.internal.DefaultConstructorMarker r27) {
        /*
            r13 = this;
            r0 = r26
            r1 = r0 & 1
            if (r1 == 0) goto L_0x0009
            r1 = 0
            goto L_0x000a
        L_0x0009:
            r1 = r14
        L_0x000a:
            r3 = r0 & 2
            r4 = 0
            if (r3 == 0) goto L_0x0011
            r3 = r4
            goto L_0x0013
        L_0x0011:
            r3 = r16
        L_0x0013:
            r5 = r0 & 4
            if (r5 == 0) goto L_0x0018
            goto L_0x001a
        L_0x0018:
            r4 = r17
        L_0x001a:
            r5 = r0 & 8
            r6 = 0
            if (r5 == 0) goto L_0x0021
            r5 = r6
            goto L_0x0023
        L_0x0021:
            r5 = r18
        L_0x0023:
            r7 = r0 & 16
            if (r7 == 0) goto L_0x0029
            r7 = r6
            goto L_0x002b
        L_0x0029:
            r7 = r19
        L_0x002b:
            r8 = r0 & 32
            if (r8 == 0) goto L_0x0031
            r8 = r6
            goto L_0x0033
        L_0x0031:
            r8 = r20
        L_0x0033:
            r9 = r0 & 64
            if (r9 == 0) goto L_0x0039
            r9 = r6
            goto L_0x003b
        L_0x0039:
            r9 = r21
        L_0x003b:
            r10 = r0 & 128(0x80, float:1.794E-43)
            if (r10 == 0) goto L_0x0041
            r10 = r6
            goto L_0x0043
        L_0x0041:
            r10 = r22
        L_0x0043:
            r11 = r0 & 256(0x100, float:3.59E-43)
            if (r11 == 0) goto L_0x0049
            r11 = r6
            goto L_0x004b
        L_0x0049:
            r11 = r23
        L_0x004b:
            r12 = r0 & 512(0x200, float:7.175E-43)
            if (r12 == 0) goto L_0x0051
            r12 = r6
            goto L_0x0053
        L_0x0051:
            r12 = r24
        L_0x0053:
            r0 = r0 & 1024(0x400, float:1.435E-42)
            if (r0 == 0) goto L_0x0058
            goto L_0x005a
        L_0x0058:
            r6 = r25
        L_0x005a:
            r14 = r1
            r16 = r3
            r17 = r4
            r18 = r5
            r19 = r7
            r20 = r8
            r21 = r9
            r22 = r10
            r23 = r11
            r24 = r12
            r25 = r6
            r13.<init>(r14, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NPVCDownEventState.<init>(long, float, float, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @NotNull
    public final List<String> getAsStringList() {
        return (List) this.asStringList$delegate.getValue();
    }

    /* compiled from: NPVCDownEventState.kt */
    public static final class Buffer {
        @NotNull
        public final RingBuffer<NPVCDownEventState> buffer;

        public Buffer(int i) {
            this.buffer = new RingBuffer<>(i, NPVCDownEventState$Buffer$buffer$1.INSTANCE);
        }

        public final void insert(long j, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
            NPVCDownEventState advance = this.buffer.advance();
            advance.timeStamp = j;
            advance.x = f;
            advance.y = f2;
            advance.qsTouchAboveFalsingThreshold = z;
            advance.dozing = z2;
            advance.collapsed = z3;
            advance.canCollapseOnQQS = z4;
            advance.listenForHeadsUp = z5;
            advance.allowExpandForSmallExpansion = z6;
            advance.touchSlopExceededBeforeDown = z7;
            advance.lastEventSynthesized = z8;
        }

        @NotNull
        public final List<List<String>> toList() {
            return SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.map(CollectionsKt___CollectionsKt.asSequence(this.buffer), NPVCDownEventState$Buffer$toList$1.INSTANCE));
        }
    }

    /* compiled from: NPVCDownEventState.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
