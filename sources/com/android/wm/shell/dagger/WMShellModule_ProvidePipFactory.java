package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipParamsChangedForwarder;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<OneHandedController>> oneHandedControllerProvider;
    public final Provider<PhonePipMenuController> phonePipMenuControllerProvider;
    public final Provider<PipAppOpsListener> pipAppOpsListenerProvider;
    public final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipMediaController> pipMediaControllerProvider;
    public final Provider<PipParamsChangedForwarder> pipParamsChangedForwarderProvider;
    public final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    public final Provider<PipTouchHandler> pipTouchHandlerProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public WMShellModule_ProvidePipFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<PipParamsChangedForwarder> provider13, Provider<Optional<OneHandedController>> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.pipAppOpsListenerProvider = provider3;
        this.pipBoundsAlgorithmProvider = provider4;
        this.pipBoundsStateProvider = provider5;
        this.pipMediaControllerProvider = provider6;
        this.phonePipMenuControllerProvider = provider7;
        this.pipTaskOrganizerProvider = provider8;
        this.pipTouchHandlerProvider = provider9;
        this.pipTransitionControllerProvider = provider10;
        this.windowManagerShellWrapperProvider = provider11;
        this.taskStackListenerProvider = provider12;
        this.pipParamsChangedForwarderProvider = provider13;
        this.oneHandedControllerProvider = provider14;
        this.mainExecutorProvider = provider15;
    }

    public Optional<Pip> get() {
        return providePip(this.contextProvider.get(), this.displayControllerProvider.get(), this.pipAppOpsListenerProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipBoundsStateProvider.get(), this.pipMediaControllerProvider.get(), this.phonePipMenuControllerProvider.get(), this.pipTaskOrganizerProvider.get(), this.pipTouchHandlerProvider.get(), this.pipTransitionControllerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.taskStackListenerProvider.get(), this.pipParamsChangedForwarderProvider.get(), this.oneHandedControllerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvidePipFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<PipParamsChangedForwarder> provider13, Provider<Optional<OneHandedController>> provider14, Provider<ShellExecutor> provider15) {
        return new WMShellModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    /* JADX WARNING: type inference failed for: r13v0, types: [java.util.Optional, java.util.Optional<com.android.wm.shell.onehanded.OneHandedController>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.pip.Pip> providePip(android.content.Context r0, com.android.wm.shell.common.DisplayController r1, com.android.wm.shell.pip.PipAppOpsListener r2, com.android.wm.shell.pip.PipBoundsAlgorithm r3, com.android.wm.shell.pip.PipBoundsState r4, com.android.wm.shell.pip.PipMediaController r5, com.android.wm.shell.pip.phone.PhonePipMenuController r6, com.android.wm.shell.pip.PipTaskOrganizer r7, com.android.wm.shell.pip.phone.PipTouchHandler r8, com.android.wm.shell.pip.PipTransitionController r9, com.android.wm.shell.WindowManagerShellWrapper r10, com.android.wm.shell.common.TaskStackListenerImpl r11, com.android.wm.shell.pip.PipParamsChangedForwarder r12, java.util.Optional<com.android.wm.shell.onehanded.OneHandedController> r13, com.android.wm.shell.common.ShellExecutor r14) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellModule.providePip(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellModule_ProvidePipFactory.providePip(android.content.Context, com.android.wm.shell.common.DisplayController, com.android.wm.shell.pip.PipAppOpsListener, com.android.wm.shell.pip.PipBoundsAlgorithm, com.android.wm.shell.pip.PipBoundsState, com.android.wm.shell.pip.PipMediaController, com.android.wm.shell.pip.phone.PhonePipMenuController, com.android.wm.shell.pip.PipTaskOrganizer, com.android.wm.shell.pip.phone.PipTouchHandler, com.android.wm.shell.pip.PipTransitionController, com.android.wm.shell.WindowManagerShellWrapper, com.android.wm.shell.common.TaskStackListenerImpl, com.android.wm.shell.pip.PipParamsChangedForwarder, java.util.Optional, com.android.wm.shell.common.ShellExecutor):java.util.Optional");
    }
}
