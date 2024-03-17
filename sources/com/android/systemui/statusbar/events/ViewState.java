package com.android.systemui.statusbar.events;

import android.graphics.Rect;
import android.view.View;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDotViewController.kt */
public final class ViewState {
    public final int cornerIndex;
    @Nullable
    public final View designatedCorner;
    @Nullable
    public final Rect landscapeRect;
    public final boolean layoutRtl;
    public final int paddingTop;
    @Nullable
    public final Rect portraitRect;
    public final boolean qsExpanded;
    public final int rotation;
    @Nullable
    public final Rect seascapeRect;
    public final boolean shadeExpanded;
    public final boolean systemPrivacyEventIsActive;
    @Nullable
    public final Rect upsideDownRect;
    public final boolean viewInitialized;

    public ViewState() {
        this(false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ ViewState copy$default(ViewState viewState, boolean z, boolean z2, boolean z3, boolean z4, Rect rect, Rect rect2, Rect rect3, Rect rect4, boolean z5, int i, int i2, int i3, View view, int i4, Object obj) {
        ViewState viewState2 = viewState;
        int i5 = i4;
        return viewState.copy((i5 & 1) != 0 ? viewState2.viewInitialized : z, (i5 & 2) != 0 ? viewState2.systemPrivacyEventIsActive : z2, (i5 & 4) != 0 ? viewState2.shadeExpanded : z3, (i5 & 8) != 0 ? viewState2.qsExpanded : z4, (i5 & 16) != 0 ? viewState2.portraitRect : rect, (i5 & 32) != 0 ? viewState2.landscapeRect : rect2, (i5 & 64) != 0 ? viewState2.upsideDownRect : rect3, (i5 & 128) != 0 ? viewState2.seascapeRect : rect4, (i5 & 256) != 0 ? viewState2.layoutRtl : z5, (i5 & 512) != 0 ? viewState2.rotation : i, (i5 & 1024) != 0 ? viewState2.paddingTop : i2, (i5 & 2048) != 0 ? viewState2.cornerIndex : i3, (i5 & 4096) != 0 ? viewState2.designatedCorner : view);
    }

    @NotNull
    public final ViewState copy(boolean z, boolean z2, boolean z3, boolean z4, @Nullable Rect rect, @Nullable Rect rect2, @Nullable Rect rect3, @Nullable Rect rect4, boolean z5, int i, int i2, int i3, @Nullable View view) {
        return new ViewState(z, z2, z3, z4, rect, rect2, rect3, rect4, z5, i, i2, i3, view);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ViewState)) {
            return false;
        }
        ViewState viewState = (ViewState) obj;
        return this.viewInitialized == viewState.viewInitialized && this.systemPrivacyEventIsActive == viewState.systemPrivacyEventIsActive && this.shadeExpanded == viewState.shadeExpanded && this.qsExpanded == viewState.qsExpanded && Intrinsics.areEqual((Object) this.portraitRect, (Object) viewState.portraitRect) && Intrinsics.areEqual((Object) this.landscapeRect, (Object) viewState.landscapeRect) && Intrinsics.areEqual((Object) this.upsideDownRect, (Object) viewState.upsideDownRect) && Intrinsics.areEqual((Object) this.seascapeRect, (Object) viewState.seascapeRect) && this.layoutRtl == viewState.layoutRtl && this.rotation == viewState.rotation && this.paddingTop == viewState.paddingTop && this.cornerIndex == viewState.cornerIndex && Intrinsics.areEqual((Object) this.designatedCorner, (Object) viewState.designatedCorner);
    }

    public int hashCode() {
        boolean z = this.viewInitialized;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        boolean z3 = this.systemPrivacyEventIsActive;
        if (z3) {
            z3 = true;
        }
        int i2 = (i + (z3 ? 1 : 0)) * 31;
        boolean z4 = this.shadeExpanded;
        if (z4) {
            z4 = true;
        }
        int i3 = (i2 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.qsExpanded;
        if (z5) {
            z5 = true;
        }
        int i4 = (i3 + (z5 ? 1 : 0)) * 31;
        Rect rect = this.portraitRect;
        int i5 = 0;
        int hashCode = (i4 + (rect == null ? 0 : rect.hashCode())) * 31;
        Rect rect2 = this.landscapeRect;
        int hashCode2 = (hashCode + (rect2 == null ? 0 : rect2.hashCode())) * 31;
        Rect rect3 = this.upsideDownRect;
        int hashCode3 = (hashCode2 + (rect3 == null ? 0 : rect3.hashCode())) * 31;
        Rect rect4 = this.seascapeRect;
        int hashCode4 = (hashCode3 + (rect4 == null ? 0 : rect4.hashCode())) * 31;
        boolean z6 = this.layoutRtl;
        if (!z6) {
            z2 = z6;
        }
        int hashCode5 = (((((((hashCode4 + (z2 ? 1 : 0)) * 31) + Integer.hashCode(this.rotation)) * 31) + Integer.hashCode(this.paddingTop)) * 31) + Integer.hashCode(this.cornerIndex)) * 31;
        View view = this.designatedCorner;
        if (view != null) {
            i5 = view.hashCode();
        }
        return hashCode5 + i5;
    }

    @NotNull
    public String toString() {
        return "ViewState(viewInitialized=" + this.viewInitialized + ", systemPrivacyEventIsActive=" + this.systemPrivacyEventIsActive + ", shadeExpanded=" + this.shadeExpanded + ", qsExpanded=" + this.qsExpanded + ", portraitRect=" + this.portraitRect + ", landscapeRect=" + this.landscapeRect + ", upsideDownRect=" + this.upsideDownRect + ", seascapeRect=" + this.seascapeRect + ", layoutRtl=" + this.layoutRtl + ", rotation=" + this.rotation + ", paddingTop=" + this.paddingTop + ", cornerIndex=" + this.cornerIndex + ", designatedCorner=" + this.designatedCorner + ')';
    }

    public ViewState(boolean z, boolean z2, boolean z3, boolean z4, @Nullable Rect rect, @Nullable Rect rect2, @Nullable Rect rect3, @Nullable Rect rect4, boolean z5, int i, int i2, int i3, @Nullable View view) {
        this.viewInitialized = z;
        this.systemPrivacyEventIsActive = z2;
        this.shadeExpanded = z3;
        this.qsExpanded = z4;
        this.portraitRect = rect;
        this.landscapeRect = rect2;
        this.upsideDownRect = rect3;
        this.seascapeRect = rect4;
        this.layoutRtl = z5;
        this.rotation = i;
        this.paddingTop = i2;
        this.cornerIndex = i3;
        this.designatedCorner = view;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ ViewState(boolean r15, boolean r16, boolean r17, boolean r18, android.graphics.Rect r19, android.graphics.Rect r20, android.graphics.Rect r21, android.graphics.Rect r22, boolean r23, int r24, int r25, int r26, android.view.View r27, int r28, kotlin.jvm.internal.DefaultConstructorMarker r29) {
        /*
            r14 = this;
            r0 = r28
            r1 = r0 & 1
            r2 = 0
            if (r1 == 0) goto L_0x0009
            r1 = r2
            goto L_0x000a
        L_0x0009:
            r1 = r15
        L_0x000a:
            r3 = r0 & 2
            if (r3 == 0) goto L_0x0010
            r3 = r2
            goto L_0x0012
        L_0x0010:
            r3 = r16
        L_0x0012:
            r4 = r0 & 4
            if (r4 == 0) goto L_0x0018
            r4 = r2
            goto L_0x001a
        L_0x0018:
            r4 = r17
        L_0x001a:
            r5 = r0 & 8
            if (r5 == 0) goto L_0x0020
            r5 = r2
            goto L_0x0022
        L_0x0020:
            r5 = r18
        L_0x0022:
            r6 = r0 & 16
            r7 = 0
            if (r6 == 0) goto L_0x0029
            r6 = r7
            goto L_0x002b
        L_0x0029:
            r6 = r19
        L_0x002b:
            r8 = r0 & 32
            if (r8 == 0) goto L_0x0031
            r8 = r7
            goto L_0x0033
        L_0x0031:
            r8 = r20
        L_0x0033:
            r9 = r0 & 64
            if (r9 == 0) goto L_0x0039
            r9 = r7
            goto L_0x003b
        L_0x0039:
            r9 = r21
        L_0x003b:
            r10 = r0 & 128(0x80, float:1.794E-43)
            if (r10 == 0) goto L_0x0041
            r10 = r7
            goto L_0x0043
        L_0x0041:
            r10 = r22
        L_0x0043:
            r11 = r0 & 256(0x100, float:3.59E-43)
            if (r11 == 0) goto L_0x0049
            r11 = r2
            goto L_0x004b
        L_0x0049:
            r11 = r23
        L_0x004b:
            r12 = r0 & 512(0x200, float:7.175E-43)
            if (r12 == 0) goto L_0x0051
            r12 = r2
            goto L_0x0053
        L_0x0051:
            r12 = r24
        L_0x0053:
            r13 = r0 & 1024(0x400, float:1.435E-42)
            if (r13 == 0) goto L_0x0058
            goto L_0x005a
        L_0x0058:
            r2 = r25
        L_0x005a:
            r13 = r0 & 2048(0x800, float:2.87E-42)
            if (r13 == 0) goto L_0x0060
            r13 = -1
            goto L_0x0062
        L_0x0060:
            r13 = r26
        L_0x0062:
            r0 = r0 & 4096(0x1000, float:5.74E-42)
            if (r0 == 0) goto L_0x0067
            goto L_0x0069
        L_0x0067:
            r7 = r27
        L_0x0069:
            r15 = r1
            r16 = r3
            r17 = r4
            r18 = r5
            r19 = r6
            r20 = r8
            r21 = r9
            r22 = r10
            r23 = r11
            r24 = r12
            r25 = r2
            r26 = r13
            r27 = r7
            r14.<init>(r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.events.ViewState.<init>(boolean, boolean, boolean, boolean, android.graphics.Rect, android.graphics.Rect, android.graphics.Rect, android.graphics.Rect, boolean, int, int, int, android.view.View, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final boolean getViewInitialized() {
        return this.viewInitialized;
    }

    public final boolean getLayoutRtl() {
        return this.layoutRtl;
    }

    public final int getRotation() {
        return this.rotation;
    }

    public final int getPaddingTop() {
        return this.paddingTop;
    }

    @Nullable
    public final View getDesignatedCorner() {
        return this.designatedCorner;
    }

    public final boolean shouldShowDot() {
        return this.systemPrivacyEventIsActive && !this.shadeExpanded && !this.qsExpanded;
    }

    public final boolean needsLayout(@NotNull ViewState viewState) {
        return this.rotation != viewState.rotation || this.layoutRtl != viewState.layoutRtl || !Intrinsics.areEqual((Object) this.portraitRect, (Object) viewState.portraitRect) || !Intrinsics.areEqual((Object) this.landscapeRect, (Object) viewState.landscapeRect) || !Intrinsics.areEqual((Object) this.upsideDownRect, (Object) viewState.upsideDownRect) || !Intrinsics.areEqual((Object) this.seascapeRect, (Object) viewState.seascapeRect);
    }

    @NotNull
    public final Rect contentRectForRotation(int i) {
        if (i == 0) {
            Rect rect = this.portraitRect;
            Intrinsics.checkNotNull(rect);
            return rect;
        } else if (i == 1) {
            Rect rect2 = this.landscapeRect;
            Intrinsics.checkNotNull(rect2);
            return rect2;
        } else if (i == 2) {
            Rect rect3 = this.upsideDownRect;
            Intrinsics.checkNotNull(rect3);
            return rect3;
        } else if (i == 3) {
            Rect rect4 = this.seascapeRect;
            Intrinsics.checkNotNull(rect4);
            return rect4;
        } else {
            throw new IllegalArgumentException("not a rotation (" + i + ')');
        }
    }
}
