package kotlin.collections;

import java.util.AbstractList;
import kotlin.jvm.internal.markers.KMappedMarker;

/* compiled from: AbstractMutableList.kt */
public abstract class AbstractMutableList<E> extends AbstractList<E> implements KMappedMarker {
    public abstract int getSize();

    public abstract E removeAt(int i);

    public final /* bridge */ E remove(int i) {
        return removeAt(i);
    }

    public final /* bridge */ int size() {
        return getSize();
    }
}
