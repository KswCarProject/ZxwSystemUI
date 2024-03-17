package com.airbnb.lottie.network;

public enum FileExtension {
    JSON(".json"),
    ZIP(".zip");
    
    public final String extension;

    /* access modifiers changed from: public */
    FileExtension(String str) {
        this.extension = str;
    }

    public String tempExtension() {
        return ".temp" + this.extension;
    }

    public String toString() {
        return this.extension;
    }
}
