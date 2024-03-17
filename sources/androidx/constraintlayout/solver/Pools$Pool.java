package androidx.constraintlayout.solver;

public interface Pools$Pool<T> {
    T acquire();

    boolean release(T t);

    void releaseAll(T[] tArr, int i);
}
