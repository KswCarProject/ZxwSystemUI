package com.google.zxing;

public final class FormatException extends ReaderException {
    public static final FormatException instance = new FormatException();

    private FormatException() {
    }

    public static FormatException getFormatInstance() {
        return instance;
    }
}
