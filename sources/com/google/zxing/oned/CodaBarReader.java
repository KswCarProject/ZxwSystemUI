package com.google.zxing.oned;

public final class CodaBarReader extends OneDReader {
    public static final char[] ALPHABET = "0123456789-$:/.+ABCD".toCharArray();
    public static final int[] CHARACTER_ENCODINGS = {3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
    public static final char[] STARTEND_ENCODING = {'A', 'B', 'C', 'D'};

    public static boolean arrayContains(char[] cArr, char c) {
        if (cArr != null) {
            for (char c2 : cArr) {
                if (c2 == c) {
                    return true;
                }
            }
        }
        return false;
    }
}
