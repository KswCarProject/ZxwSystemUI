package kotlinx.coroutines.internal;

import org.jetbrains.annotations.NotNull;

/* compiled from: Symbol.kt */
public final class Symbol {
    @NotNull
    public final String symbol;

    public Symbol(@NotNull String str) {
        this.symbol = str;
    }

    @NotNull
    public String toString() {
        return '<' + this.symbol + '>';
    }
}
