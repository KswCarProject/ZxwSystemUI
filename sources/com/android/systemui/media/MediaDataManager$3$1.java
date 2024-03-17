package com.android.systemui.media;

import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import java.util.List;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$3$1 implements SmartspaceSession.OnTargetsAvailableListener {
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$3$1(MediaDataManager mediaDataManager) {
        this.this$0 = mediaDataManager;
    }

    public final void onTargetsAvailable(List<SmartspaceTarget> list) {
        this.this$0.smartspaceMediaDataProvider.onTargetsAvailable(list);
    }
}
