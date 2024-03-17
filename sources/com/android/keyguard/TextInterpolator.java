package com.android.keyguard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.text.PositionedGlyphs;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextShaper;
import android.util.MathUtils;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TextInterpolator.kt */
public final class TextInterpolator {
    @NotNull
    public final TextPaint basePaint;
    @NotNull
    public final FontInterpolator fontInterpolator = new FontInterpolator();
    @NotNull
    public Layout layout;
    @NotNull
    public List<Line> lines = CollectionsKt__CollectionsKt.emptyList();
    public float progress;
    @NotNull
    public final TextPaint targetPaint;
    @NotNull
    public final TextPaint tmpDrawPaint = new TextPaint();
    @NotNull
    public float[] tmpPositionArray = new float[20];

    public TextInterpolator(@NotNull Layout layout2) {
        this.basePaint = new TextPaint(layout2.getPaint());
        this.targetPaint = new TextPaint(layout2.getPaint());
        this.layout = layout2;
        shapeText(layout2);
    }

    @NotNull
    public final TextPaint getTargetPaint() {
        return this.targetPaint;
    }

    /* compiled from: TextInterpolator.kt */
    public static final class FontRun {
        @NotNull
        public Font baseFont;
        public final int end;
        public final int start;
        @NotNull
        public Font targetFont;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FontRun)) {
                return false;
            }
            FontRun fontRun = (FontRun) obj;
            return this.start == fontRun.start && this.end == fontRun.end && Intrinsics.areEqual((Object) this.baseFont, (Object) fontRun.baseFont) && Intrinsics.areEqual((Object) this.targetFont, (Object) fontRun.targetFont);
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.start) * 31) + Integer.hashCode(this.end)) * 31) + this.baseFont.hashCode()) * 31) + this.targetFont.hashCode();
        }

        @NotNull
        public String toString() {
            return "FontRun(start=" + this.start + ", end=" + this.end + ", baseFont=" + this.baseFont + ", targetFont=" + this.targetFont + ')';
        }

        public FontRun(int i, int i2, @NotNull Font font, @NotNull Font font2) {
            this.start = i;
            this.end = i2;
            this.baseFont = font;
            this.targetFont = font2;
        }

        public final int getStart() {
            return this.start;
        }

        public final int getEnd() {
            return this.end;
        }

        @NotNull
        public final Font getBaseFont() {
            return this.baseFont;
        }

        public final void setBaseFont(@NotNull Font font) {
            this.baseFont = font;
        }

        @NotNull
        public final Font getTargetFont() {
            return this.targetFont;
        }

        public final void setTargetFont(@NotNull Font font) {
            this.targetFont = font;
        }

        public final int getLength() {
            return this.end - this.start;
        }
    }

    /* compiled from: TextInterpolator.kt */
    public static final class Run {
        @NotNull
        public final float[] baseX;
        @NotNull
        public final float[] baseY;
        @NotNull
        public final List<FontRun> fontRuns;
        @NotNull
        public final int[] glyphIds;
        @NotNull
        public final float[] targetX;
        @NotNull
        public final float[] targetY;

        public Run(@NotNull int[] iArr, @NotNull float[] fArr, @NotNull float[] fArr2, @NotNull float[] fArr3, @NotNull float[] fArr4, @NotNull List<FontRun> list) {
            this.glyphIds = iArr;
            this.baseX = fArr;
            this.baseY = fArr2;
            this.targetX = fArr3;
            this.targetY = fArr4;
            this.fontRuns = list;
        }

        @NotNull
        public final int[] getGlyphIds() {
            return this.glyphIds;
        }

        @NotNull
        public final float[] getBaseX() {
            return this.baseX;
        }

        @NotNull
        public final float[] getBaseY() {
            return this.baseY;
        }

        @NotNull
        public final float[] getTargetX() {
            return this.targetX;
        }

        @NotNull
        public final float[] getTargetY() {
            return this.targetY;
        }

        @NotNull
        public final List<FontRun> getFontRuns() {
            return this.fontRuns;
        }
    }

    /* compiled from: TextInterpolator.kt */
    public static final class Line {
        @NotNull
        public final List<Run> runs;

        public Line(@NotNull List<Run> list) {
            this.runs = list;
        }

        @NotNull
        public final List<Run> getRuns() {
            return this.runs;
        }
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    @NotNull
    public final Layout getLayout() {
        return this.layout;
    }

    public final void setLayout(@NotNull Layout layout2) {
        this.layout = layout2;
        shapeText(layout2);
    }

    public final void onTargetPaintModified() {
        updatePositionsAndFonts(shapeText(getLayout(), this.targetPaint), false);
    }

    public final void rebase() {
        float f = this.progress;
        boolean z = true;
        if (!(f == 0.0f)) {
            if (f != 1.0f) {
                z = false;
            }
            if (z) {
                this.basePaint.set(this.targetPaint);
            } else {
                lerp(this.basePaint, this.targetPaint, f, this.tmpDrawPaint);
                this.basePaint.set(this.tmpDrawPaint);
            }
            for (Line runs : this.lines) {
                for (Run run : runs.getRuns()) {
                    int length = run.getBaseX().length;
                    for (int i = 0; i < length; i++) {
                        run.getBaseX()[i] = MathUtils.lerp(run.getBaseX()[i], run.getTargetX()[i], getProgress());
                        run.getBaseY()[i] = MathUtils.lerp(run.getBaseY()[i], run.getTargetY()[i], getProgress());
                    }
                    for (FontRun fontRun : run.getFontRuns()) {
                        fontRun.setBaseFont(this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), getProgress()));
                    }
                }
            }
            this.progress = 0.0f;
        }
    }

    public final void draw(@NotNull Canvas canvas) {
        lerp(this.basePaint, this.targetPaint, this.progress, this.tmpDrawPaint);
        int i = 0;
        for (Object next : this.lines) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            for (Run run : ((Line) next).getRuns()) {
                canvas.save();
                try {
                    canvas.translate(TextInterpolatorKt.getDrawOrigin(getLayout(), i), (float) getLayout().getLineBaseline(i));
                    for (FontRun drawFontRun : run.getFontRuns()) {
                        drawFontRun(canvas, run, drawFontRun, this.tmpDrawPaint);
                    }
                } finally {
                    canvas.restore();
                }
            }
            i = i2;
        }
    }

    public final void shapeText(Layout layout2) {
        ArrayList arrayList;
        ArrayList arrayList2;
        Iterator it;
        Iterator it2;
        float[] fArr;
        float[] fArr2;
        int i;
        PositionedGlyphs positionedGlyphs;
        TextInterpolator textInterpolator = this;
        Layout layout3 = layout2;
        List<List<PositionedGlyphs>> shapeText = textInterpolator.shapeText(layout3, textInterpolator.basePaint);
        List<List<PositionedGlyphs>> shapeText2 = textInterpolator.shapeText(layout3, textInterpolator.targetPaint);
        if (shapeText.size() == shapeText2.size()) {
            Iterable iterable = shapeText;
            Iterator it3 = iterable.iterator();
            Iterable iterable2 = shapeText2;
            Iterator it4 = iterable2.iterator();
            int i2 = 10;
            ArrayList arrayList3 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10), CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable2, 10)));
            int i3 = 0;
            while (it3.hasNext() && it4.hasNext()) {
                Iterable iterable3 = (List) it3.next();
                Iterator it5 = iterable3.iterator();
                Iterable iterable4 = (List) it4.next();
                Iterator it6 = iterable4.iterator();
                ArrayList arrayList4 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable3, i2), CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable4, i2)));
                while (it5.hasNext() && it6.hasNext()) {
                    Object next = it5.next();
                    PositionedGlyphs positionedGlyphs2 = (PositionedGlyphs) it6.next();
                    PositionedGlyphs positionedGlyphs3 = (PositionedGlyphs) next;
                    if (positionedGlyphs3.glyphCount() == positionedGlyphs2.glyphCount()) {
                        int glyphCount = positionedGlyphs3.glyphCount();
                        int[] iArr = new int[glyphCount];
                        int i4 = 0;
                        while (i4 < glyphCount) {
                            int glyphId = positionedGlyphs3.getGlyphId(i4);
                            if (glyphId == positionedGlyphs2.getGlyphId(i4)) {
                                Unit unit = Unit.INSTANCE;
                                iArr[i4] = glyphId;
                                i4++;
                            } else {
                                throw new IllegalArgumentException(("Inconsistent glyph ID at " + i4 + " in line " + textInterpolator.lines.size()).toString());
                            }
                        }
                        float[] fArr3 = new float[glyphCount];
                        for (int i5 = 0; i5 < glyphCount; i5++) {
                            fArr3[i5] = positionedGlyphs3.getGlyphX(i5);
                        }
                        float[] fArr4 = new float[glyphCount];
                        for (int i6 = 0; i6 < glyphCount; i6++) {
                            fArr4[i6] = positionedGlyphs3.getGlyphY(i6);
                        }
                        float[] fArr5 = new float[glyphCount];
                        for (int i7 = 0; i7 < glyphCount; i7++) {
                            fArr5[i7] = positionedGlyphs2.getGlyphX(i7);
                        }
                        float[] fArr6 = new float[glyphCount];
                        int i8 = i3;
                        for (int i9 = 0; i9 < glyphCount; i9++) {
                            fArr6[i9] = positionedGlyphs2.getGlyphY(i9);
                        }
                        ArrayList arrayList5 = new ArrayList();
                        Iterator it7 = it3;
                        Iterator it8 = it4;
                        if (glyphCount != 0) {
                            Font font = positionedGlyphs3.getFont(0);
                            it2 = it5;
                            Font font2 = positionedGlyphs2.getFont(0);
                            it = it6;
                            fArr2 = fArr5;
                            arrayList2 = arrayList3;
                            if (FontInterpolator.Companion.canInterpolate(font, font2)) {
                                Font font3 = font;
                                arrayList = arrayList4;
                                int i10 = i8;
                                int i11 = 1;
                                Font font4 = font2;
                                int i12 = 0;
                                while (i11 < glyphCount) {
                                    int i13 = i11 + 1;
                                    float[] fArr7 = fArr6;
                                    Font font5 = positionedGlyphs3.getFont(i11);
                                    PositionedGlyphs positionedGlyphs4 = positionedGlyphs3;
                                    Font font6 = positionedGlyphs2.getFont(i11);
                                    if (font3 != font5) {
                                        if (font4 != font6) {
                                            positionedGlyphs = positionedGlyphs2;
                                            arrayList5.add(new FontRun(i12, i11, font3, font4));
                                            i10 = Math.max(i10, i11 - i12);
                                            if (FontInterpolator.Companion.canInterpolate(font5, font6)) {
                                                font4 = font6;
                                                i12 = i11;
                                                font3 = font5;
                                            } else {
                                                throw new IllegalArgumentException(("Cannot interpolate font at " + i11 + " (" + font5 + " vs " + font6 + ')').toString());
                                            }
                                        } else {
                                            throw new IllegalArgumentException(("Base font has changed at " + i11 + " but target font has not changed.").toString());
                                        }
                                    } else {
                                        positionedGlyphs = positionedGlyphs2;
                                        if (!(font4 == font6)) {
                                            throw new IllegalArgumentException(("Base font has not changed at " + i11 + " but target font has changed.").toString());
                                        }
                                    }
                                    i11 = i13;
                                    fArr6 = fArr7;
                                    positionedGlyphs3 = positionedGlyphs4;
                                    positionedGlyphs2 = positionedGlyphs;
                                }
                                fArr = fArr6;
                                arrayList5.add(new FontRun(i12, glyphCount, font3, font4));
                                i = Math.max(i10, glyphCount - i12);
                            } else {
                                throw new IllegalArgumentException(("Cannot interpolate font at " + 0 + " (" + font + " vs " + font2 + ')').toString());
                            }
                        } else {
                            fArr = fArr6;
                            arrayList2 = arrayList3;
                            it2 = it5;
                            it = it6;
                            arrayList = arrayList4;
                            fArr2 = fArr5;
                            i = i8;
                        }
                        float[] fArr8 = fArr2;
                        ArrayList arrayList6 = arrayList;
                        arrayList6.add(new Run(iArr, fArr3, fArr4, fArr2, fArr, arrayList5));
                        arrayList4 = arrayList6;
                        it3 = it7;
                        it4 = it8;
                        it5 = it2;
                        it6 = it;
                        arrayList3 = arrayList2;
                        i3 = i;
                        textInterpolator = this;
                    } else {
                        throw new IllegalArgumentException(Intrinsics.stringPlus("Inconsistent glyph count at line ", Integer.valueOf(textInterpolator.lines.size())).toString());
                    }
                }
                Iterator it9 = it4;
                ArrayList arrayList7 = arrayList3;
                arrayList7.add(new Line(arrayList4));
                arrayList3 = arrayList7;
                i3 = i3;
                it3 = it3;
                it4 = it9;
                i2 = 10;
            }
            textInterpolator.lines = arrayList3;
            int i14 = i3 * 2;
            if (textInterpolator.tmpPositionArray.length < i14) {
                textInterpolator.tmpPositionArray = new float[i14];
                return;
            }
            return;
        }
        throw new IllegalArgumentException("The new layout result has different line count.".toString());
    }

    public final void drawFontRun(Canvas canvas, Run run, FontRun fontRun, Paint paint) {
        int start = fontRun.getStart();
        int end = fontRun.getEnd();
        int i = 0;
        while (start < end) {
            int i2 = i + 1;
            this.tmpPositionArray[i] = MathUtils.lerp(run.getBaseX()[start], run.getTargetX()[start], this.progress);
            this.tmpPositionArray[i2] = MathUtils.lerp(run.getBaseY()[start], run.getTargetY()[start], this.progress);
            start++;
            i = i2 + 1;
        }
        canvas.drawGlyphs(run.getGlyphIds(), fontRun.getStart(), this.tmpPositionArray, 0, fontRun.getLength(), this.fontInterpolator.lerp(fontRun.getBaseFont(), fontRun.getTargetFont(), this.progress), paint);
    }

    public final void updatePositionsAndFonts(List<? extends List<PositionedGlyphs>> list, boolean z) {
        if (list.size() == this.lines.size()) {
            Iterable iterable = this.lines;
            Iterator it = iterable.iterator();
            Iterable iterable2 = list;
            Iterator it2 = iterable2.iterator();
            ArrayList arrayList = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10), CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable2, 10)));
            while (it.hasNext() && it2.hasNext()) {
                Iterable runs = ((Line) it.next()).getRuns();
                Iterator it3 = runs.iterator();
                Iterable iterable3 = (List) it2.next();
                Iterator it4 = iterable3.iterator();
                ArrayList arrayList2 = new ArrayList(Math.min(CollectionsKt__IterablesKt.collectionSizeOrDefault(runs, 10), CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable3, 10)));
                while (it3.hasNext() && it4.hasNext()) {
                    Object next = it3.next();
                    PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                    Run run = (Run) next;
                    if (positionedGlyphs.glyphCount() == run.getGlyphIds().length) {
                        for (FontRun fontRun : run.getFontRuns()) {
                            Font font = positionedGlyphs.getFont(fontRun.getStart());
                            int start = fontRun.getStart();
                            int end = fontRun.getEnd();
                            while (start < end) {
                                int i = start + 1;
                                if (positionedGlyphs.getGlyphId(fontRun.getStart()) == run.getGlyphIds()[fontRun.getStart()]) {
                                    if (font == positionedGlyphs.getFont(start)) {
                                        start = i;
                                    } else {
                                        throw new IllegalArgumentException(("The new layout has different font run. " + font + " vs " + positionedGlyphs.getFont(start) + " at " + start).toString());
                                    }
                                } else {
                                    throw new IllegalArgumentException(Intrinsics.stringPlus("The new layout has different glyph ID at ", Integer.valueOf(fontRun.getStart())).toString());
                                }
                            }
                            if (!FontInterpolator.Companion.canInterpolate(font, fontRun.getBaseFont())) {
                                throw new IllegalArgumentException(("New font cannot be interpolated with existing font. " + font + ", " + fontRun.getBaseFont()).toString());
                            } else if (z) {
                                fontRun.setBaseFont(font);
                            } else {
                                fontRun.setTargetFont(font);
                            }
                        }
                        if (z) {
                            int length = run.getBaseX().length;
                            for (int i2 = 0; i2 < length; i2++) {
                                run.getBaseX()[i2] = positionedGlyphs.getGlyphX(i2);
                                run.getBaseY()[i2] = positionedGlyphs.getGlyphY(i2);
                            }
                        } else {
                            int length2 = run.getBaseX().length;
                            for (int i3 = 0; i3 < length2; i3++) {
                                run.getTargetX()[i3] = positionedGlyphs.getGlyphX(i3);
                                run.getTargetY()[i3] = positionedGlyphs.getGlyphY(i3);
                            }
                        }
                        arrayList2.add(Unit.INSTANCE);
                    } else {
                        throw new IllegalArgumentException("The new layout has different glyph count.".toString());
                    }
                }
                arrayList.add(arrayList2);
            }
            return;
        }
        throw new IllegalStateException("The new layout result has different line count.".toString());
    }

    public final void lerp(Paint paint, Paint paint2, float f, Paint paint3) {
        paint3.set(paint);
        paint3.setTextSize(MathUtils.lerp(paint.getTextSize(), paint2.getTextSize(), f));
        paint3.setColor(ColorUtils.blendARGB(paint.getColor(), paint2.getColor(), f));
    }

    public final List<List<PositionedGlyphs>> shapeText(Layout layout2, TextPaint textPaint) {
        ArrayList arrayList = new ArrayList();
        int lineCount = layout2.getLineCount();
        int i = 0;
        while (i < lineCount) {
            int i2 = i + 1;
            int lineStart = layout2.getLineStart(i);
            int lineEnd = layout2.getLineEnd(i) - lineStart;
            ArrayList arrayList2 = new ArrayList();
            TextShaper.shapeText(layout2.getText(), lineStart, lineEnd, layout2.getTextDirectionHeuristic(), textPaint, new TextInterpolator$shapeText$3(arrayList2));
            arrayList.add(arrayList2);
            i = i2;
        }
        return arrayList;
    }
}
