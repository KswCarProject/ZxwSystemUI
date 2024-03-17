package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PackageReference.kt */
public final class PackageReference implements ClassBasedDeclarationContainer {
    @NotNull
    public final Class<?> jClass;
    @NotNull
    public final String moduleName;

    public PackageReference(@NotNull Class<?> cls, @NotNull String str) {
        this.jClass = cls;
        this.moduleName = str;
    }

    @NotNull
    public Class<?> getJClass() {
        return this.jClass;
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof PackageReference) && Intrinsics.areEqual((Object) getJClass(), (Object) ((PackageReference) obj).getJClass());
    }

    public int hashCode() {
        return getJClass().hashCode();
    }

    @NotNull
    public String toString() {
        return Intrinsics.stringPlus(getJClass().toString(), " (Kotlin reflection is not available)");
    }
}
