package com.android.wm.shell.displayareahelper;

import android.view.SurfaceControl;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class DisplayAreaHelperController implements DisplayAreaHelper {
    public final Executor mExecutor;
    public final RootDisplayAreaOrganizer mRootDisplayAreaOrganizer;

    public DisplayAreaHelperController(Executor executor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        this.mExecutor = executor;
        this.mRootDisplayAreaOrganizer = rootDisplayAreaOrganizer;
    }

    public void attachToRootDisplayArea(int i, SurfaceControl.Builder builder, Consumer<SurfaceControl.Builder> consumer) {
        this.mExecutor.execute(new DisplayAreaHelperController$$ExternalSyntheticLambda0(this, i, builder, consumer));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attachToRootDisplayArea$0(int i, SurfaceControl.Builder builder, Consumer consumer) {
        this.mRootDisplayAreaOrganizer.attachToDisplayArea(i, builder);
        consumer.accept(builder);
    }
}
