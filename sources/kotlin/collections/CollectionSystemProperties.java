package kotlin.collections;

import org.jetbrains.annotations.NotNull;

/* compiled from: CollectionsJVM.kt */
public final class CollectionSystemProperties {
    @NotNull
    public static final CollectionSystemProperties INSTANCE = new CollectionSystemProperties();
    public static final boolean brittleContainsOptimizationEnabled;

    static {
        String property = System.getProperty("kotlin.collections.convert_arg_to_set_in_removeAll");
        brittleContainsOptimizationEnabled = property == null ? false : Boolean.parseBoolean(property);
    }
}
