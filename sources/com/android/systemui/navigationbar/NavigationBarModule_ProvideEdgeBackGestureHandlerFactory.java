package com.android.systemui.navigationbar;

import android.content.Context;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NavigationBarModule_ProvideEdgeBackGestureHandlerFactory implements Factory<EdgeBackGestureHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<EdgeBackGestureHandler.Factory> factoryProvider;

    public NavigationBarModule_ProvideEdgeBackGestureHandlerFactory(Provider<EdgeBackGestureHandler.Factory> provider, Provider<Context> provider2) {
        this.factoryProvider = provider;
        this.contextProvider = provider2;
    }

    public EdgeBackGestureHandler get() {
        return provideEdgeBackGestureHandler(this.factoryProvider.get(), this.contextProvider.get());
    }

    public static NavigationBarModule_ProvideEdgeBackGestureHandlerFactory create(Provider<EdgeBackGestureHandler.Factory> provider, Provider<Context> provider2) {
        return new NavigationBarModule_ProvideEdgeBackGestureHandlerFactory(provider, provider2);
    }

    public static EdgeBackGestureHandler provideEdgeBackGestureHandler(EdgeBackGestureHandler.Factory factory, Context context) {
        return (EdgeBackGestureHandler) Preconditions.checkNotNullFromProvides(NavigationBarModule.provideEdgeBackGestureHandler(factory, context));
    }
}
