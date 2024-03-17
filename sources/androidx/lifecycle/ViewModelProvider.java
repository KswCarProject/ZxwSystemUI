package androidx.lifecycle;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ViewModelProvider.kt */
public class ViewModelProvider {
    @NotNull
    public final Factory factory;
    @NotNull
    public final ViewModelStore store;

    /* compiled from: ViewModelProvider.kt */
    public interface Factory {
        @NotNull
        <T extends ViewModel> T create(@NotNull Class<T> cls);
    }

    /* compiled from: ViewModelProvider.kt */
    public static class OnRequeryFactory {
        public void onRequery(@NotNull ViewModel viewModel) {
        }
    }

    public ViewModelProvider(@NotNull ViewModelStore viewModelStore, @NotNull Factory factory2) {
        this.store = viewModelStore;
        this.factory = factory2;
    }

    /* compiled from: ViewModelProvider.kt */
    public static abstract class KeyedFactory extends OnRequeryFactory implements Factory {
        @NotNull
        public abstract <T extends ViewModel> T create(@NotNull String str, @NotNull Class<T> cls);

        @NotNull
        public <T extends ViewModel> T create(@NotNull Class<T> cls) {
            throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementations of KeyedFactory");
        }
    }

    @NotNull
    public <T extends ViewModel> T get(@NotNull Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            return get(Intrinsics.stringPlus("androidx.lifecycle.ViewModelProvider.DefaultKey:", canonicalName), cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @NotNull
    public <T extends ViewModel> T get(@NotNull String str, @NotNull Class<T> cls) {
        T t;
        T t2 = this.store.get(str);
        if (cls.isInstance(t2)) {
            Factory factory2 = this.factory;
            OnRequeryFactory onRequeryFactory = factory2 instanceof OnRequeryFactory ? (OnRequeryFactory) factory2 : null;
            if (onRequeryFactory != null) {
                onRequeryFactory.onRequery(t2);
            }
            if (t2 != null) {
                return t2;
            }
            throw new NullPointerException("null cannot be cast to non-null type T of androidx.lifecycle.ViewModelProvider.get");
        }
        Factory factory3 = this.factory;
        if (factory3 instanceof KeyedFactory) {
            t = ((KeyedFactory) factory3).create(str, cls);
        } else {
            t = factory3.create(cls);
        }
        this.store.put(str, t);
        return t;
    }
}
