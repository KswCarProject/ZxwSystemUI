package com.android.systemui.qs;

import android.view.View;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class QSFgsManagerFooter_Factory implements Factory<QSFgsManagerFooter> {
    public final Provider<Executor> executorProvider;
    public final Provider<FgsManagerController> fgsManagerControllerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<View> rootViewProvider;

    public QSFgsManagerFooter_Factory(Provider<View> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<FgsManagerController> provider4) {
        this.rootViewProvider = provider;
        this.mainExecutorProvider = provider2;
        this.executorProvider = provider3;
        this.fgsManagerControllerProvider = provider4;
    }

    public QSFgsManagerFooter get() {
        return newInstance(this.rootViewProvider.get(), this.mainExecutorProvider.get(), this.executorProvider.get(), this.fgsManagerControllerProvider.get());
    }

    public static QSFgsManagerFooter_Factory create(Provider<View> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<FgsManagerController> provider4) {
        return new QSFgsManagerFooter_Factory(provider, provider2, provider3, provider4);
    }

    public static QSFgsManagerFooter newInstance(View view, Executor executor, Executor executor2, FgsManagerController fgsManagerController) {
        return new QSFgsManagerFooter(view, executor, executor2, fgsManagerController);
    }
}
