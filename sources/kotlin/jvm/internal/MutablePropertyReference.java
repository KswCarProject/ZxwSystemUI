package kotlin.jvm.internal;

public abstract class MutablePropertyReference extends PropertyReference {
    public MutablePropertyReference() {
    }

    public MutablePropertyReference(Object obj, Class cls, String str, String str2, int i) {
        super(obj, cls, str, str2, i);
    }
}
