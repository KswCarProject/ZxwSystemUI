package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DebugMetadata.kt */
public final class ModuleNameRetriever {
    @NotNull
    public static final ModuleNameRetriever INSTANCE = new ModuleNameRetriever();
    @Nullable
    public static Cache cache;
    @NotNull
    public static final Cache notOnJava9 = new Cache((Method) null, (Method) null, (Method) null);

    /* compiled from: DebugMetadata.kt */
    public static final class Cache {
        @Nullable
        public final Method getDescriptorMethod;
        @Nullable
        public final Method getModuleMethod;
        @Nullable
        public final Method nameMethod;

        public Cache(@Nullable Method method, @Nullable Method method2, @Nullable Method method3) {
            this.getModuleMethod = method;
            this.getDescriptorMethod = method2;
            this.nameMethod = method3;
        }
    }

    @Nullable
    public final String getModuleName(@NotNull BaseContinuationImpl baseContinuationImpl) {
        Cache cache2 = cache;
        if (cache2 == null) {
            cache2 = buildCache(baseContinuationImpl);
        }
        if (cache2 == notOnJava9) {
            return null;
        }
        Method method = cache2.getModuleMethod;
        Object invoke = method == null ? null : method.invoke(baseContinuationImpl.getClass(), new Object[0]);
        if (invoke == null) {
            return null;
        }
        Method method2 = cache2.getDescriptorMethod;
        Object invoke2 = method2 == null ? null : method2.invoke(invoke, new Object[0]);
        if (invoke2 == null) {
            return null;
        }
        Method method3 = cache2.nameMethod;
        Object invoke3 = method3 == null ? null : method3.invoke(invoke2, new Object[0]);
        if (invoke3 instanceof String) {
            return (String) invoke3;
        }
        return null;
    }

    public final Cache buildCache(BaseContinuationImpl baseContinuationImpl) {
        try {
            Cache cache2 = new Cache(Class.class.getDeclaredMethod("getModule", new Class[0]), baseContinuationImpl.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]), baseContinuationImpl.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new Class[0]));
            cache = cache2;
            return cache2;
        } catch (Exception unused) {
            Cache cache3 = notOnJava9;
            cache = cache3;
            return cache3;
        }
    }
}
