package com.google.zxing.oned;

public final class ITFReader extends OneDReader {
    public static final int[] DEFAULT_ALLOWED_LENGTHS = {48, 44, 24, 20, 18, 16, 14, 12, 10, 8, 6};
    public static final int[] END_PATTERN_REVERSED = {1, 1, 3};
    public static final int[][] PATTERNS = {new int[]{1, 1, 3, 3, 1}, new int[]{3, 1, 1, 1, 3}, new int[]{1, 3, 1, 1, 3}, new int[]{3, 3, 1, 1, 1}, new int[]{1, 1, 3, 1, 3}, new int[]{3, 1, 3, 1, 1}, new int[]{1, 3, 3, 1, 1}, new int[]{1, 1, 1, 3, 3}, new int[]{3, 1, 1, 3, 1}, new int[]{1, 3, 1, 3, 1}};
    public static final int[] START_PATTERN = {1, 1, 1, 1};
}
