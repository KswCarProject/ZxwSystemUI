package com.android.systemui.touch;

import android.graphics.Region;
import android.view.View;
import com.android.systemui.touch.TouchInsetManager;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ Region f$0;

    public /* synthetic */ TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda3(Region region) {
        this.f$0 = region;
    }

    public final void accept(Object obj) {
        TouchInsetManager.TouchInsetSession.lambda$updateTouchRegion$3(this.f$0, (View) obj);
    }
}
