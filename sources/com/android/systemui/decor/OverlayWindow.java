package com.android.systemui.decor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.RegionInterceptingFrameLayout;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: OverlayWindow.kt */
public final class OverlayWindow {
    @NotNull
    public final Context context;
    @NotNull
    public final ViewGroup rootView;
    @NotNull
    public final Map<Integer, Pair<View, DecorProvider>> viewProviderMap = new LinkedHashMap();

    public OverlayWindow(@NotNull Context context2) {
        this.context = context2;
        this.rootView = new RegionInterceptingFrameLayout(context2);
    }

    @NotNull
    public final ViewGroup getRootView() {
        return this.rootView;
    }

    @NotNull
    public final List<Integer> getViewIds() {
        return CollectionsKt___CollectionsKt.toList(this.viewProviderMap.keySet());
    }

    public final void addDecorProvider(@NotNull DecorProvider decorProvider, int i) {
        this.viewProviderMap.put(Integer.valueOf(decorProvider.getViewId()), new Pair(decorProvider.inflateView(this.context, this.rootView, i), decorProvider));
    }

    @Nullable
    public final View getView(int i) {
        Pair pair = this.viewProviderMap.get(Integer.valueOf(i));
        if (pair == null) {
            return null;
        }
        return (View) pair.getFirst();
    }

    public final void removeView(int i) {
        View view = getView(i);
        if (view != null) {
            this.rootView.removeView(view);
            this.viewProviderMap.remove(Integer.valueOf(i));
        }
    }

    public final void removeRedundantViews(@Nullable int[] iArr) {
        for (Number intValue : getViewIds()) {
            int intValue2 = intValue.intValue();
            if (iArr == null || !ArraysKt___ArraysKt.contains(iArr, intValue2)) {
                removeView(intValue2);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasSameProviders(@org.jetbrains.annotations.NotNull java.util.List<? extends com.android.systemui.decor.DecorProvider> r5) {
        /*
            r4 = this;
            int r0 = r5.size()
            java.util.Map<java.lang.Integer, kotlin.Pair<android.view.View, com.android.systemui.decor.DecorProvider>> r1 = r4.viewProviderMap
            int r1 = r1.size()
            r2 = 0
            r3 = 1
            if (r0 != r1) goto L_0x0042
            java.lang.Iterable r5 = (java.lang.Iterable) r5
            boolean r0 = r5 instanceof java.util.Collection
            if (r0 == 0) goto L_0x001f
            r0 = r5
            java.util.Collection r0 = (java.util.Collection) r0
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x001f
        L_0x001d:
            r4 = r3
            goto L_0x003f
        L_0x001f:
            java.util.Iterator r5 = r5.iterator()
        L_0x0023:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x001d
            java.lang.Object r0 = r5.next()
            com.android.systemui.decor.DecorProvider r0 = (com.android.systemui.decor.DecorProvider) r0
            int r0 = r0.getViewId()
            android.view.View r0 = r4.getView(r0)
            if (r0 == 0) goto L_0x003b
            r0 = r3
            goto L_0x003c
        L_0x003b:
            r0 = r2
        L_0x003c:
            if (r0 != 0) goto L_0x0023
            r4 = r2
        L_0x003f:
            if (r4 == 0) goto L_0x0042
            r2 = r3
        L_0x0042:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.decor.OverlayWindow.hasSameProviders(java.util.List):boolean");
    }

    public final void dump(@NotNull PrintWriter printWriter, @NotNull String str) {
        printWriter.println("  " + str + '=');
        printWriter.println(Intrinsics.stringPlus("    rootView=", this.rootView));
        int childCount = this.rootView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.rootView.getChildAt(i);
            printWriter.println("    child[" + i + "]=" + childAt);
        }
    }

    public final void onReloadResAndMeasure(@Nullable Integer[] numArr, int i, int i2, @Nullable String str) {
        Unit unit;
        if (numArr == null) {
            unit = null;
        } else {
            int i3 = 0;
            int length = numArr.length;
            while (i3 < length) {
                Integer num = numArr[i3];
                i3++;
                Pair pair = this.viewProviderMap.get(Integer.valueOf(num.intValue()));
                if (pair != null) {
                    ((DecorProvider) pair.getSecond()).onReloadResAndMeasure((View) pair.getFirst(), i, i2, str);
                }
            }
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            for (Pair pair2 : this.viewProviderMap.values()) {
                ((DecorProvider) pair2.getSecond()).onReloadResAndMeasure((View) pair2.getFirst(), i, i2, str);
            }
        }
    }
}
