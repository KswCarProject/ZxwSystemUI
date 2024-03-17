package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import dagger.Lazy;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WMShellModule$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ TransactionPool f$1;
    public final /* synthetic */ DisplayInsetsController f$2;
    public final /* synthetic */ Lazy f$3;
    public final /* synthetic */ ShellExecutor f$4;

    public /* synthetic */ WMShellModule$$ExternalSyntheticLambda0(Context context, TransactionPool transactionPool, DisplayInsetsController displayInsetsController, Lazy lazy, ShellExecutor shellExecutor) {
        this.f$0 = context;
        this.f$1 = transactionPool;
        this.f$2 = displayInsetsController;
        this.f$3 = lazy;
        this.f$4 = shellExecutor;
    }

    public final Object apply(Object obj) {
        return WMShellModule.lambda$provideStageTaskUnfoldController$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (ShellUnfoldProgressProvider) obj);
    }
}
