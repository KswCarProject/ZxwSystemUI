package com.google.zxing.pdf417.encoder;

public final class Dimensions {
    public final int maxCols;
    public final int maxRows;
    public final int minCols;
    public final int minRows;

    public int getMinCols() {
        return this.minCols;
    }

    public int getMaxCols() {
        return this.maxCols;
    }

    public int getMinRows() {
        return this.minRows;
    }

    public int getMaxRows() {
        return this.maxRows;
    }
}
