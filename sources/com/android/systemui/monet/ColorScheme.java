package com.android.systemui.monet;

import android.app.WallpaperColors;
import com.android.internal.graphics.cam.Cam;
import com.android.internal.graphics.cam.CamUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public final class ColorScheme {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final List<Integer> accent1;
    @NotNull
    public final List<Integer> accent2;
    @NotNull
    public final List<Integer> accent3;
    public final boolean darkTheme;
    @NotNull
    public final List<Integer> neutral1;
    @NotNull
    public final List<Integer> neutral2;
    public final int seed;
    @NotNull
    public final Style style;

    public ColorScheme(@NotNull WallpaperColors wallpaperColors, boolean z) {
        this(wallpaperColors, z, (Style) null, 4, (DefaultConstructorMarker) null);
    }

    public static final int getSeedColor(@NotNull WallpaperColors wallpaperColors) {
        return Companion.getSeedColor(wallpaperColors);
    }

    @NotNull
    public static final List<Integer> getSeedColors(@NotNull WallpaperColors wallpaperColors) {
        return Companion.getSeedColors(wallpaperColors);
    }

    public ColorScheme(int i, boolean z, @NotNull Style style2) {
        this.seed = i;
        this.darkTheme = z;
        this.style = style2;
        Cam fromInt = Cam.fromInt(i);
        if (i == 0 || (style2 != Style.CONTENT && fromInt.getChroma() < 5.0f)) {
            i = -14979341;
        }
        Cam fromInt2 = Cam.fromInt(i);
        this.accent1 = style2.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet().getA1().shades(fromInt2);
        this.accent2 = style2.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet().getA2().shades(fromInt2);
        this.accent3 = style2.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet().getA3().shades(fromInt2);
        this.neutral1 = style2.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet().getN1().shades(fromInt2);
        this.neutral2 = style2.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet().getN2().shades(fromInt2);
    }

    @NotNull
    public final List<Integer> getAccent1() {
        return this.accent1;
    }

    @NotNull
    public final List<Integer> getAccent2() {
        return this.accent2;
    }

    @NotNull
    public final List<Integer> getAccent3() {
        return this.accent3;
    }

    @NotNull
    public final List<Integer> getNeutral1() {
        return this.neutral1;
    }

    @NotNull
    public final List<Integer> getNeutral2() {
        return this.neutral2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ ColorScheme(WallpaperColors wallpaperColors, boolean z, Style style2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(wallpaperColors, z, (i & 4) != 0 ? Style.TONAL_SPOT : style2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ColorScheme(@NotNull WallpaperColors wallpaperColors, boolean z, @NotNull Style style2) {
        this(Companion.getSeedColor(wallpaperColors, style2 != Style.CONTENT), z, style2);
    }

    @NotNull
    public final List<Integer> getAllAccentColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.accent1);
        arrayList.addAll(this.accent2);
        arrayList.addAll(this.accent3);
        return arrayList;
    }

    @NotNull
    public final List<Integer> getAllNeutralColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.neutral1);
        arrayList.addAll(this.neutral2);
        return arrayList;
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorScheme {\n  seed color: ");
        Companion companion = Companion;
        sb.append(companion.stringForColor(this.seed));
        sb.append("\n  style: ");
        sb.append(this.style);
        sb.append("\n  palettes: \n  ");
        sb.append(companion.humanReadable("PRIMARY", this.accent1));
        sb.append("\n  ");
        sb.append(companion.humanReadable("SECONDARY", this.accent2));
        sb.append("\n  ");
        sb.append(companion.humanReadable("TERTIARY", this.accent3));
        sb.append("\n  ");
        sb.append(companion.humanReadable("NEUTRAL", this.neutral1));
        sb.append("\n  ");
        sb.append(companion.humanReadable("NEUTRAL VARIANT", this.neutral2));
        sb.append("\n}");
        return sb.toString();
    }

    /* compiled from: ColorScheme.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int getSeedColor(@NotNull WallpaperColors wallpaperColors) {
            return getSeedColor$default(this, wallpaperColors, false, 2, (Object) null);
        }

        @NotNull
        public final List<Integer> getSeedColors(@NotNull WallpaperColors wallpaperColors) {
            return getSeedColors$default(this, wallpaperColors, false, 2, (Object) null);
        }

        public final double wrapDegreesDouble(double d) {
            if (d >= 0.0d) {
                return d >= 360.0d ? d % ((double) 360) : d;
            }
            double d2 = (double) 360;
            return (d % d2) + d2;
        }

        public Companion() {
        }

        public static /* synthetic */ int getSeedColor$default(Companion companion, WallpaperColors wallpaperColors, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = true;
            }
            return companion.getSeedColor(wallpaperColors, z);
        }

        public final int getSeedColor(@NotNull WallpaperColors wallpaperColors, boolean z) {
            return ((Number) CollectionsKt___CollectionsKt.first(getSeedColors(wallpaperColors, z))).intValue();
        }

        public static /* synthetic */ List getSeedColors$default(Companion companion, WallpaperColors wallpaperColors, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = true;
            }
            return companion.getSeedColors(wallpaperColors, z);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:89:0x02ef, code lost:
            if (r3 != 15) goto L_0x02ff;
         */
        @org.jetbrains.annotations.NotNull
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.util.List<java.lang.Integer> getSeedColors(@org.jetbrains.annotations.NotNull android.app.WallpaperColors r19, boolean r20) {
            /*
                r18 = this;
                r0 = r20
                java.util.Map r1 = r19.getAllColors()
                java.util.Collection r1 = r1.values()
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.Iterator r1 = r1.iterator()
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x0302
                java.lang.Object r2 = r1.next()
            L_0x001a:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0036
                java.lang.Object r3 = r1.next()
                java.lang.Integer r3 = (java.lang.Integer) r3
                java.lang.Integer r2 = (java.lang.Integer) r2
                int r2 = r2.intValue()
                int r3 = r3.intValue()
                int r2 = r2 + r3
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
                goto L_0x001a
            L_0x0036:
                java.lang.Number r2 = (java.lang.Number) r2
                int r1 = r2.intValue()
                double r1 = (double) r1
                r3 = 0
                int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                r6 = 0
                r7 = 1
                if (r5 != 0) goto L_0x0047
                r5 = r7
                goto L_0x0048
            L_0x0047:
                r5 = r6
            L_0x0048:
                r8 = 1084227584(0x40a00000, float:5.0)
                r9 = -14979341(0xffffffffff1b6ef3, float:-2.0660642E38)
                if (r5 == 0) goto L_0x00c7
                java.util.List r1 = r19.getMainColors()
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.ArrayList r2 = new java.util.ArrayList
                r3 = 10
                int r3 = kotlin.collections.CollectionsKt__IterablesKt.collectionSizeOrDefault(r1, r3)
                r2.<init>(r3)
                java.util.Iterator r1 = r1.iterator()
            L_0x0064:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x007c
                java.lang.Object r3 = r1.next()
                android.graphics.Color r3 = (android.graphics.Color) r3
                int r3 = r3.toArgb()
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
                r2.add(r3)
                goto L_0x0064
            L_0x007c:
                java.util.List r1 = kotlin.collections.CollectionsKt___CollectionsKt.distinct(r2)
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.ArrayList r2 = new java.util.ArrayList
                r2.<init>()
                java.util.Iterator r1 = r1.iterator()
            L_0x008b:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x00b4
                java.lang.Object r3 = r1.next()
                r4 = r3
                java.lang.Number r4 = (java.lang.Number) r4
                int r4 = r4.intValue()
                if (r0 != 0) goto L_0x00a0
            L_0x009e:
                r4 = r7
                goto L_0x00ae
            L_0x00a0:
                com.android.internal.graphics.cam.Cam r4 = com.android.internal.graphics.cam.Cam.fromInt(r4)
                float r4 = r4.getChroma()
                int r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r4 < 0) goto L_0x00ad
                goto L_0x009e
            L_0x00ad:
                r4 = r6
            L_0x00ae:
                if (r4 == 0) goto L_0x008b
                r2.add(r3)
                goto L_0x008b
            L_0x00b4:
                java.util.List r0 = kotlin.collections.CollectionsKt___CollectionsKt.toList(r2)
                boolean r1 = r0.isEmpty()
                if (r1 == 0) goto L_0x00c6
                java.lang.Integer r0 = java.lang.Integer.valueOf(r9)
                java.util.List r0 = kotlin.collections.CollectionsKt__CollectionsJVMKt.listOf(r0)
            L_0x00c6:
                return r0
            L_0x00c7:
                java.util.Map r10 = r19.getAllColors()
                java.util.LinkedHashMap r11 = new java.util.LinkedHashMap
                int r12 = r10.size()
                int r12 = kotlin.collections.MapsKt__MapsJVMKt.mapCapacity(r12)
                r11.<init>(r12)
                java.util.Set r10 = r10.entrySet()
                java.lang.Iterable r10 = (java.lang.Iterable) r10
                java.util.Iterator r10 = r10.iterator()
            L_0x00e2:
                boolean r12 = r10.hasNext()
                if (r12 == 0) goto L_0x0106
                java.lang.Object r12 = r10.next()
                java.util.Map$Entry r12 = (java.util.Map.Entry) r12
                java.lang.Object r13 = r12.getKey()
                java.lang.Object r12 = r12.getValue()
                java.lang.Number r12 = (java.lang.Number) r12
                int r12 = r12.intValue()
                double r14 = (double) r12
                double r14 = r14 / r1
                java.lang.Double r12 = java.lang.Double.valueOf(r14)
                r11.put(r13, r12)
                goto L_0x00e2
            L_0x0106:
                java.util.Map r1 = r19.getAllColors()
                java.util.LinkedHashMap r2 = new java.util.LinkedHashMap
                int r10 = r1.size()
                int r10 = kotlin.collections.MapsKt__MapsJVMKt.mapCapacity(r10)
                r2.<init>(r10)
                java.util.Set r1 = r1.entrySet()
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                java.util.Iterator r1 = r1.iterator()
            L_0x0121:
                boolean r10 = r1.hasNext()
                if (r10 == 0) goto L_0x0143
                java.lang.Object r10 = r1.next()
                java.util.Map$Entry r10 = (java.util.Map.Entry) r10
                java.lang.Object r12 = r10.getKey()
                java.lang.Object r10 = r10.getKey()
                java.lang.Number r10 = (java.lang.Number) r10
                int r10 = r10.intValue()
                com.android.internal.graphics.cam.Cam r10 = com.android.internal.graphics.cam.Cam.fromInt(r10)
                r2.put(r12, r10)
                goto L_0x0121
            L_0x0143:
                r10 = r18
                java.util.List r1 = r10.huePopulations(r2, r11, r0)
                java.util.Map r10 = r19.getAllColors()
                java.util.LinkedHashMap r11 = new java.util.LinkedHashMap
                int r12 = r10.size()
                int r12 = kotlin.collections.MapsKt__MapsJVMKt.mapCapacity(r12)
                r11.<init>(r12)
                java.util.Set r10 = r10.entrySet()
                java.lang.Iterable r10 = (java.lang.Iterable) r10
                java.util.Iterator r10 = r10.iterator()
            L_0x0164:
                boolean r12 = r10.hasNext()
                r13 = 15
                if (r12 == 0) goto L_0x01b9
                java.lang.Object r12 = r10.next()
                java.util.Map$Entry r12 = (java.util.Map.Entry) r12
                java.lang.Object r14 = r12.getKey()
                java.lang.Object r12 = r12.getKey()
                java.lang.Object r12 = r2.get(r12)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r12)
                com.android.internal.graphics.cam.Cam r12 = (com.android.internal.graphics.cam.Cam) r12
                float r12 = r12.getHue()
                int r12 = kotlin.math.MathKt__MathJVMKt.roundToInt((float) r12)
                int r15 = r12 + -15
                int r12 = r12 + r13
                if (r15 > r12) goto L_0x01ad
                r16 = r3
            L_0x0192:
                int r13 = r15 + 1
                com.android.systemui.monet.ColorScheme$Companion r3 = com.android.systemui.monet.ColorScheme.Companion
                int r3 = r3.wrapDegrees(r15)
                java.lang.Object r3 = r1.get(r3)
                java.lang.Number r3 = (java.lang.Number) r3
                double r3 = r3.doubleValue()
                double r16 = r16 + r3
                if (r15 != r12) goto L_0x01a9
                goto L_0x01af
            L_0x01a9:
                r15 = r13
                r3 = 0
                goto L_0x0192
            L_0x01ad:
                r16 = 0
            L_0x01af:
                java.lang.Double r3 = java.lang.Double.valueOf(r16)
                r11.put(r14, r3)
                r3 = 0
                goto L_0x0164
            L_0x01b9:
                if (r0 != 0) goto L_0x01bd
                r0 = r2
                goto L_0x0211
            L_0x01bd:
                java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
                r0.<init>()
                java.util.Set r1 = r2.entrySet()
                java.util.Iterator r1 = r1.iterator()
            L_0x01ca:
                boolean r3 = r1.hasNext()
                if (r3 == 0) goto L_0x0211
                java.lang.Object r3 = r1.next()
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3
                java.lang.Object r4 = r3.getValue()
                com.android.internal.graphics.cam.Cam r4 = (com.android.internal.graphics.cam.Cam) r4
                java.lang.Object r10 = r3.getKey()
                java.lang.Object r10 = r11.get(r10)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r10)
                java.lang.Number r10 = (java.lang.Number) r10
                double r14 = r10.doubleValue()
                float r4 = r4.getChroma()
                int r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r4 < 0) goto L_0x0202
                if (r5 != 0) goto L_0x0200
                r16 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
                int r4 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
                if (r4 <= 0) goto L_0x0202
            L_0x0200:
                r4 = r7
                goto L_0x0203
            L_0x0202:
                r4 = r6
            L_0x0203:
                if (r4 == 0) goto L_0x01ca
                java.lang.Object r4 = r3.getKey()
                java.lang.Object r3 = r3.getValue()
                r0.put(r4, r3)
                goto L_0x01ca
            L_0x0211:
                java.util.LinkedHashMap r1 = new java.util.LinkedHashMap
                int r3 = r0.size()
                int r3 = kotlin.collections.MapsKt__MapsJVMKt.mapCapacity(r3)
                r1.<init>(r3)
                java.util.Set r0 = r0.entrySet()
                java.lang.Iterable r0 = (java.lang.Iterable) r0
                java.util.Iterator r0 = r0.iterator()
            L_0x0228:
                boolean r3 = r0.hasNext()
                if (r3 == 0) goto L_0x025d
                java.lang.Object r3 = r0.next()
                java.util.Map$Entry r3 = (java.util.Map.Entry) r3
                java.lang.Object r4 = r3.getKey()
                com.android.systemui.monet.ColorScheme$Companion r5 = com.android.systemui.monet.ColorScheme.Companion
                java.lang.Object r8 = r3.getValue()
                com.android.internal.graphics.cam.Cam r8 = (com.android.internal.graphics.cam.Cam) r8
                java.lang.Object r3 = r3.getKey()
                java.lang.Object r3 = r11.get(r3)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
                java.lang.Number r3 = (java.lang.Number) r3
                double r14 = r3.doubleValue()
                double r14 = r5.score(r8, r14)
                java.lang.Double r3 = java.lang.Double.valueOf(r14)
                r1.put(r4, r3)
                goto L_0x0228
            L_0x025d:
                java.util.Set r0 = r1.entrySet()
                java.util.Collection r0 = (java.util.Collection) r0
                java.util.List r0 = kotlin.collections.CollectionsKt___CollectionsKt.toMutableList(r0)
                int r1 = r0.size()
                if (r1 <= r7) goto L_0x0275
                com.android.systemui.monet.ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1 r1 = new com.android.systemui.monet.ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1
                r1.<init>()
                kotlin.collections.CollectionsKt__MutableCollectionsJVMKt.sortWith(r0, r1)
            L_0x0275:
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                r3 = 90
            L_0x027c:
                int r4 = r3 + -1
                r1.clear()
                java.util.Iterator r5 = r0.iterator()
            L_0x0285:
                boolean r8 = r5.hasNext()
                if (r8 == 0) goto L_0x02ef
                java.lang.Object r8 = r5.next()
                java.util.Map$Entry r8 = (java.util.Map.Entry) r8
                java.lang.Object r8 = r8.getKey()
                java.lang.Integer r8 = (java.lang.Integer) r8
                java.util.Iterator r10 = r1.iterator()
            L_0x029b:
                boolean r11 = r10.hasNext()
                if (r11 == 0) goto L_0x02db
                java.lang.Object r11 = r10.next()
                r12 = r11
                java.lang.Number r12 = (java.lang.Number) r12
                int r12 = r12.intValue()
                java.lang.Object r14 = r2.get(r8)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r14)
                com.android.internal.graphics.cam.Cam r14 = (com.android.internal.graphics.cam.Cam) r14
                float r14 = r14.getHue()
                java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
                java.lang.Object r12 = r2.get(r12)
                kotlin.jvm.internal.Intrinsics.checkNotNull(r12)
                com.android.internal.graphics.cam.Cam r12 = (com.android.internal.graphics.cam.Cam) r12
                float r12 = r12.getHue()
                com.android.systemui.monet.ColorScheme$Companion r15 = com.android.systemui.monet.ColorScheme.Companion
                float r12 = r15.hueDiff(r14, r12)
                float r14 = (float) r3
                int r12 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r12 >= 0) goto L_0x02d7
                r12 = r7
                goto L_0x02d8
            L_0x02d7:
                r12 = r6
            L_0x02d8:
                if (r12 == 0) goto L_0x029b
                goto L_0x02dc
            L_0x02db:
                r11 = 0
            L_0x02dc:
                if (r11 == 0) goto L_0x02e0
                r10 = r7
                goto L_0x02e1
            L_0x02e0:
                r10 = r6
            L_0x02e1:
                if (r10 == 0) goto L_0x02e4
                goto L_0x0285
            L_0x02e4:
                r1.add(r8)
                int r8 = r1.size()
                r10 = 4
                if (r8 < r10) goto L_0x0285
                goto L_0x02f1
            L_0x02ef:
                if (r3 != r13) goto L_0x02ff
            L_0x02f1:
                boolean r0 = r1.isEmpty()
                if (r0 == 0) goto L_0x02fe
                java.lang.Integer r0 = java.lang.Integer.valueOf(r9)
                r1.add(r0)
            L_0x02fe:
                return r1
            L_0x02ff:
                r3 = r4
                goto L_0x027c
            L_0x0302:
                java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
                java.lang.String r1 = "Empty collection can't be reduced."
                r0.<init>(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.monet.ColorScheme.Companion.getSeedColors(android.app.WallpaperColors, boolean):java.util.List");
        }

        public final int wrapDegrees(int i) {
            if (i < 0) {
                return (i % 360) + 360;
            }
            return i >= 360 ? i % 360 : i;
        }

        public final float hueDiff(float f, float f2) {
            return 180.0f - Math.abs(Math.abs(f - f2) - 180.0f);
        }

        public final String stringForColor(int i) {
            Cam fromInt = Cam.fromInt(i);
            String stringPlus = Intrinsics.stringPlus("H", StringsKt__StringsKt.padEnd$default(String.valueOf(MathKt__MathJVMKt.roundToInt(fromInt.getHue())), 4, 0, 2, (Object) null));
            String stringPlus2 = Intrinsics.stringPlus("C", StringsKt__StringsKt.padEnd$default(String.valueOf(MathKt__MathJVMKt.roundToInt(fromInt.getChroma())), 4, 0, 2, (Object) null));
            String stringPlus3 = Intrinsics.stringPlus("T", StringsKt__StringsKt.padEnd$default(String.valueOf(MathKt__MathJVMKt.roundToInt(CamUtils.lstarFromInt(i))), 4, 0, 2, (Object) null));
            String upperCase = StringsKt__StringsKt.padStart(Integer.toHexString(i & 16777215), 6, '0').toUpperCase(Locale.ROOT);
            return stringPlus + stringPlus2 + stringPlus3 + " = #" + upperCase;
        }

        public final String humanReadable(String str, List<Integer> list) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(10);
            Iterable<Number> iterable = list;
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
            for (Number intValue : iterable) {
                arrayList.add(ColorScheme.Companion.stringForColor(intValue.intValue()));
            }
            sb.append(CollectionsKt___CollectionsKt.joinToString$default(arrayList, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, ColorScheme$Companion$humanReadable$2.INSTANCE, 30, (Object) null));
            return sb.toString();
        }

        public final double score(Cam cam, double d) {
            float f;
            double d2;
            double d3 = d * 70.0d;
            if (cam.getChroma() < 48.0f) {
                d2 = 0.1d;
                f = cam.getChroma();
            } else {
                d2 = 0.3d;
                f = cam.getChroma();
            }
            return (((double) (f - 48.0f)) * d2) + d3;
        }

        public final List<Double> huePopulations(Map<Integer, ? extends Cam> map, Map<Integer, Double> map2, boolean z) {
            ArrayList arrayList = new ArrayList(360);
            int i = 0;
            while (i < 360) {
                i++;
                arrayList.add(Double.valueOf(0.0d));
            }
            List<Double> mutableList = CollectionsKt___CollectionsKt.toMutableList(arrayList);
            for (Map.Entry next : map2.entrySet()) {
                Double d = map2.get(next.getKey());
                Intrinsics.checkNotNull(d);
                double doubleValue = d.doubleValue();
                Cam cam = map.get(next.getKey());
                Intrinsics.checkNotNull(cam);
                Cam cam2 = cam;
                int roundToInt = MathKt__MathJVMKt.roundToInt(cam2.getHue()) % 360;
                if (!z || cam2.getChroma() > 5.0f) {
                    mutableList.set(roundToInt, Double.valueOf(mutableList.get(roundToInt).doubleValue() + doubleValue));
                }
            }
            return mutableList;
        }
    }
}
