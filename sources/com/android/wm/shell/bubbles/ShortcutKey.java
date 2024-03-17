package com.android.wm.shell.bubbles;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BubbleDataRepository.kt */
public final class ShortcutKey {
    @NotNull
    public final String pkg;
    public final int userId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShortcutKey)) {
            return false;
        }
        ShortcutKey shortcutKey = (ShortcutKey) obj;
        return this.userId == shortcutKey.userId && Intrinsics.areEqual((Object) this.pkg, (Object) shortcutKey.pkg);
    }

    public int hashCode() {
        return (Integer.hashCode(this.userId) * 31) + this.pkg.hashCode();
    }

    @NotNull
    public String toString() {
        return "ShortcutKey(userId=" + this.userId + ", pkg=" + this.pkg + ')';
    }

    public ShortcutKey(int i, @NotNull String str) {
        this.userId = i;
        this.pkg = str;
    }

    @NotNull
    public final String getPkg() {
        return this.pkg;
    }

    public final int getUserId() {
        return this.userId;
    }
}
