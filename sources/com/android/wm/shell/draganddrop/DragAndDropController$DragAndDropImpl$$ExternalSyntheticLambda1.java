package com.android.wm.shell.draganddrop;

import android.content.res.Configuration;
import com.android.wm.shell.draganddrop.DragAndDropController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DragAndDropController$DragAndDropImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DragAndDropController.DragAndDropImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ DragAndDropController$DragAndDropImpl$$ExternalSyntheticLambda1(DragAndDropController.DragAndDropImpl dragAndDropImpl, Configuration configuration) {
        this.f$0 = dragAndDropImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigChanged$1(this.f$1);
    }
}
