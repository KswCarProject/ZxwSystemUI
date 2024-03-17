package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;

public abstract class ListEntry {
    public final ListAttachState mAttachState = ListAttachState.create();
    public final long mCreationTime;
    public final String mKey;
    public final ListAttachState mPreviousAttachState = ListAttachState.create();

    public abstract NotificationEntry getRepresentativeEntry();

    public ListEntry(String str, long j) {
        this.mKey = str;
        this.mCreationTime = j;
    }

    public String getKey() {
        return this.mKey;
    }

    public long getCreationTime() {
        return this.mCreationTime;
    }

    public GroupEntry getParent() {
        return this.mAttachState.getParent();
    }

    public void setParent(GroupEntry groupEntry) {
        this.mAttachState.setParent(groupEntry);
    }

    public NotifSection getSection() {
        return this.mAttachState.getSection();
    }

    public int getSectionIndex() {
        if (this.mAttachState.getSection() != null) {
            return this.mAttachState.getSection().getIndex();
        }
        return -1;
    }

    public ListAttachState getAttachState() {
        return this.mAttachState;
    }

    public ListAttachState getPreviousAttachState() {
        return this.mPreviousAttachState;
    }

    public void beginNewAttachState() {
        this.mPreviousAttachState.clone(this.mAttachState);
        this.mAttachState.reset();
    }

    public boolean wasAttachedInPreviousPass() {
        return getPreviousAttachState().getParent() != null;
    }
}
