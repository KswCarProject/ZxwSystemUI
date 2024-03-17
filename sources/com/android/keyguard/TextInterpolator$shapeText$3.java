package com.android.keyguard;

import android.graphics.text.PositionedGlyphs;
import android.text.TextPaint;
import android.text.TextShaper;
import java.util.List;

/* compiled from: TextInterpolator.kt */
public final class TextInterpolator$shapeText$3 implements TextShaper.GlyphsConsumer {
    public final /* synthetic */ List<PositionedGlyphs> $runs;

    public TextInterpolator$shapeText$3(List<PositionedGlyphs> list) {
        this.$runs = list;
    }

    public final void accept(int i, int i2, PositionedGlyphs positionedGlyphs, TextPaint textPaint) {
        this.$runs.add(positionedGlyphs);
    }
}
