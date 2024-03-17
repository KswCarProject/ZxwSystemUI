package com.android.systemui.statusbar.notification.collection.coalescer;

import java.util.ArrayList;
import java.util.List;

public class EventBatch {
    public Runnable mCancelShortTimeout;
    public final long mCreatedTimestamp;
    public final String mGroupKey;
    public final List<CoalescedEvent> mMembers = new ArrayList();

    public EventBatch(long j, String str) {
        this.mCreatedTimestamp = j;
        this.mGroupKey = str;
    }
}
