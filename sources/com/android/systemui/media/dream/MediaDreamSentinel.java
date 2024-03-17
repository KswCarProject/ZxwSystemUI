package com.android.systemui.media.dream;

import com.android.systemui.CoreStartable;
import com.android.systemui.media.MediaDataManager;

public class MediaDreamSentinel extends CoreStartable {
    public MediaDataManager.Listener mListener;
    public final MediaDataManager mMediaDataManager;

    public void start() {
        this.mMediaDataManager.addListener(this.mListener);
    }
}
