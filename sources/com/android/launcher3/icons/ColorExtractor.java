package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseArray;
import java.util.Arrays;

public class ColorExtractor {
    public final int NUM_SAMPLES = 20;
    public final float[] mTmpHsv = new float[3];
    public final float[] mTmpHueScoreHistogram = new float[360];
    public final int[] mTmpPixels = new int[20];
    public final SparseArray<Float> mTmpRgbScores = new SparseArray<>();

    public int findDominantColorByHue(Bitmap bitmap) {
        return findDominantColorByHue(bitmap, 20);
    }

    public int findDominantColorByHue(Bitmap bitmap, int i) {
        int i2;
        int i3;
        int i4 = i;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int sqrt = (int) Math.sqrt((double) ((height * width) / i4));
        if (sqrt < 1) {
            sqrt = 1;
        }
        float[] fArr = this.mTmpHsv;
        Arrays.fill(fArr, 0.0f);
        float[] fArr2 = this.mTmpHueScoreHistogram;
        Arrays.fill(fArr2, 0.0f);
        int i5 = -1;
        int[] iArr = this.mTmpPixels;
        int i6 = 0;
        Arrays.fill(iArr, 0);
        int i7 = 0;
        int i8 = 0;
        float f = -1.0f;
        while (true) {
            i2 = -16777216;
            if (i7 >= height) {
                break;
            }
            int i9 = i6;
            while (i9 < width) {
                int pixel = bitmap.getPixel(i9, i7);
                if (((pixel >> 24) & 255) < 128) {
                    i3 = height;
                } else {
                    int i10 = pixel | -16777216;
                    Color.colorToHSV(i10, fArr);
                    i3 = height;
                    int i11 = (int) fArr[0];
                    if (i11 >= 0 && i11 < fArr2.length) {
                        if (i8 < i4) {
                            iArr[i8] = i10;
                            i8++;
                        }
                        float f2 = fArr2[i11] + (fArr[1] * fArr[2]);
                        fArr2[i11] = f2;
                        if (f2 > f) {
                            i5 = i11;
                            f = f2;
                        }
                    }
                }
                i9 += sqrt;
                height = i3;
            }
            Bitmap bitmap2 = bitmap;
            int i12 = height;
            i7 += sqrt;
            i6 = 0;
        }
        SparseArray<Float> sparseArray = this.mTmpRgbScores;
        sparseArray.clear();
        float f3 = -1.0f;
        for (int i13 = 0; i13 < i8; i13++) {
            int i14 = iArr[i13];
            Color.colorToHSV(i14, fArr);
            if (((int) fArr[0]) == i5) {
                float f4 = fArr[1];
                float f5 = fArr[2];
                int i15 = ((int) (100.0f * f4)) + ((int) (10000.0f * f5));
                float f6 = f4 * f5;
                Float f7 = sparseArray.get(i15);
                if (f7 != null) {
                    f6 += f7.floatValue();
                }
                sparseArray.put(i15, Float.valueOf(f6));
                if (f6 > f3) {
                    i2 = i14;
                    f3 = f6;
                }
            }
        }
        return i2;
    }
}
