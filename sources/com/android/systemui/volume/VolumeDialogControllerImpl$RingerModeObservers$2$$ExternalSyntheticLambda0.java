package com.android.systemui.volume;

import com.android.systemui.volume.VolumeDialogControllerImpl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VolumeDialogControllerImpl$RingerModeObservers$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass2 f$0;
    public final /* synthetic */ Integer f$1;

    public /* synthetic */ VolumeDialogControllerImpl$RingerModeObservers$2$$ExternalSyntheticLambda0(VolumeDialogControllerImpl.RingerModeObservers.AnonymousClass2 r1, Integer num) {
        this.f$0 = r1;
        this.f$1 = num;
    }

    public final void run() {
        this.f$0.lambda$onChanged$0(this.f$1);
    }
}
