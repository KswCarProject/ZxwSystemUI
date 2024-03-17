package com.android.systemui.keyguard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Lifecycle<T> {
    public ArrayList<T> mObservers = new ArrayList<>();

    public void addObserver(T t) {
        ArrayList<T> arrayList = this.mObservers;
        Objects.requireNonNull(t);
        arrayList.add(t);
    }

    public void removeObserver(T t) {
        this.mObservers.remove(t);
    }

    public void dispatch(Consumer<T> consumer) {
        for (int i = 0; i < this.mObservers.size(); i++) {
            consumer.accept(this.mObservers.get(i));
        }
    }

    public <U> void dispatch(BiConsumer<T, U> biConsumer, U u) {
        for (int i = 0; i < this.mObservers.size(); i++) {
            biConsumer.accept(this.mObservers.get(i), u);
        }
    }
}
