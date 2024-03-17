package androidx.dynamicanimation.animation;

public abstract class FloatPropertyCompat<T> {
    public final String mPropertyName;

    public abstract float getValue(T t);

    public abstract void setValue(T t, float f);

    public FloatPropertyCompat(String str) {
        this.mPropertyName = str;
    }
}
