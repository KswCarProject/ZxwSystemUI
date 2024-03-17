package com.android.systemui.statusbar.notification.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupEntry extends ListEntry {
    public static final GroupEntry ROOT_ENTRY = new GroupEntry("<root>", 0);
    public final List<NotificationEntry> mChildren;
    public NotificationEntry mSummary;
    public final List<NotificationEntry> mUnmodifiableChildren;

    public GroupEntry(String str, long j) {
        super(str, j);
        ArrayList arrayList = new ArrayList();
        this.mChildren = arrayList;
        this.mUnmodifiableChildren = Collections.unmodifiableList(arrayList);
    }

    public NotificationEntry getRepresentativeEntry() {
        return this.mSummary;
    }

    public NotificationEntry getSummary() {
        return this.mSummary;
    }

    public List<NotificationEntry> getChildren() {
        return this.mUnmodifiableChildren;
    }

    public void setSummary(NotificationEntry notificationEntry) {
        this.mSummary = notificationEntry;
    }

    public void clearChildren() {
        this.mChildren.clear();
    }

    public void addChild(NotificationEntry notificationEntry) {
        this.mChildren.add(notificationEntry);
    }

    public void sortChildren(Comparator<? super NotificationEntry> comparator) {
        this.mChildren.sort(comparator);
    }

    public List<NotificationEntry> getRawChildren() {
        return this.mChildren;
    }
}
