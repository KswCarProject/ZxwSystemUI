package com.google.zxing.pdf417.encoder;

import java.lang.reflect.Array;

public final class BarcodeMatrix {
    public int currentRow;
    public final int height;
    public final BarcodeRow[] matrix;
    public final int width;

    public BarcodeMatrix(int i, int i2) {
        BarcodeRow[] barcodeRowArr = new BarcodeRow[i];
        this.matrix = barcodeRowArr;
        int length = barcodeRowArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            this.matrix[i3] = new BarcodeRow(((i2 + 4) * 17) + 1);
        }
        this.width = i2 * 17;
        this.height = i;
        this.currentRow = -1;
    }

    public void startRow() {
        this.currentRow++;
    }

    public BarcodeRow getCurrentRow() {
        return this.matrix[this.currentRow];
    }

    public byte[][] getScaledMatrix(int i, int i2) {
        int[] iArr = new int[2];
        iArr[1] = this.width * i;
        iArr[0] = this.height * i2;
        byte[][] bArr = (byte[][]) Array.newInstance(byte.class, iArr);
        int i3 = this.height * i2;
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[(i3 - i4) - 1] = this.matrix[i4 / i2].getScaledRow(i);
        }
        return bArr;
    }
}