package com.android.systemui.dreams.complication;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.concurrent.Executor;

public class ComplicationTypesUpdater extends CoreStartable {
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final Executor mExecutor;
    public final SecureSettings mSecureSettings;

    public void start() {
        AnonymousClass1 r0 = new ContentObserver((Handler) null) {
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onChange$0() {
                ComplicationTypesUpdater.this.mDreamOverlayStateController.setAvailableComplicationTypes(ComplicationTypesUpdater.this.getAvailableComplicationTypes());
            }

            public void onChange(boolean z) {
                ComplicationTypesUpdater.this.mExecutor.execute(new ComplicationTypesUpdater$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mSecureSettings.registerContentObserverForUser("screensaver_enabled_complications", (ContentObserver) r0, UserHandle.myUserId());
        r0.onChange(false);
    }

    public final int getAvailableComplicationTypes() {
        throw null;
    }
}
