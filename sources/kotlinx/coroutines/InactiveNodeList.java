package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: JobSupport.kt */
public final class InactiveNodeList implements Incomplete {
    @NotNull
    public final NodeList list;

    public boolean isActive() {
        return false;
    }

    public InactiveNodeList(@NotNull NodeList nodeList) {
        this.list = nodeList;
    }

    @NotNull
    public NodeList getList() {
        return this.list;
    }

    @NotNull
    public String toString() {
        return DebugKt.getDEBUG() ? getList().getString("New") : super.toString();
    }
}
