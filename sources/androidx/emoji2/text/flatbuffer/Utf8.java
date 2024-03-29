package androidx.emoji2.text.flatbuffer;

public abstract class Utf8 {
    public static Utf8 DEFAULT;

    public static Utf8 getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new Utf8Safe();
        }
        return DEFAULT;
    }
}
