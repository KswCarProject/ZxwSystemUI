package com.android.systemui.controls.ui;

import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsUiControllerImpl.kt */
public /* synthetic */ class ControlsUiControllerImpl$show$2 extends FunctionReferenceImpl implements Function1<List<? extends SelectionItem>, Unit> {
    public ControlsUiControllerImpl$show$2(Object obj) {
        super(1, obj, ControlsUiControllerImpl.class, "showInitialSetupView", "showInitialSetupView(Ljava/util/List;)V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((List<SelectionItem>) (List) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull List<SelectionItem> list) {
        ((ControlsUiControllerImpl) this.receiver).showInitialSetupView(list);
    }
}
