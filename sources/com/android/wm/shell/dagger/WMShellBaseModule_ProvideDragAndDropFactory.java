package com.android.wm.shell.dagger;

import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.draganddrop.DragAndDropController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideDragAndDropFactory implements Factory<Optional<DragAndDrop>> {
    public final Provider<DragAndDropController> dragAndDropControllerProvider;

    public WMShellBaseModule_ProvideDragAndDropFactory(Provider<DragAndDropController> provider) {
        this.dragAndDropControllerProvider = provider;
    }

    public Optional<DragAndDrop> get() {
        return provideDragAndDrop(this.dragAndDropControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideDragAndDropFactory create(Provider<DragAndDropController> provider) {
        return new WMShellBaseModule_ProvideDragAndDropFactory(provider);
    }

    public static Optional<DragAndDrop> provideDragAndDrop(DragAndDropController dragAndDropController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDragAndDrop(dragAndDropController));
    }
}
