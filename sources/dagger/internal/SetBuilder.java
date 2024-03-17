package dagger.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SetBuilder<T> {
    public final List<T> contributions;

    public SetBuilder(int i) {
        this.contributions = new ArrayList(i);
    }

    public static <T> SetBuilder<T> newSetBuilder(int i) {
        return new SetBuilder<>(i);
    }

    public SetBuilder<T> add(T t) {
        this.contributions.add(Preconditions.checkNotNull(t, "Set contributions cannot be null"));
        return this;
    }

    public Set<T> build() {
        if (this.contributions.isEmpty()) {
            return Collections.emptySet();
        }
        if (this.contributions.size() == 1) {
            return Collections.singleton(this.contributions.get(0));
        }
        return Collections.unmodifiableSet(new HashSet(this.contributions));
    }
}
