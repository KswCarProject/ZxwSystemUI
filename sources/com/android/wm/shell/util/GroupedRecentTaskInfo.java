package com.android.wm.shell.util;

import android.app.ActivityManager;
import android.app.WindowConfiguration;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class GroupedRecentTaskInfo implements Parcelable {
    public static final Parcelable.Creator<GroupedRecentTaskInfo> CREATOR = new Parcelable.Creator<GroupedRecentTaskInfo>() {
        public GroupedRecentTaskInfo createFromParcel(Parcel parcel) {
            return new GroupedRecentTaskInfo(parcel);
        }

        public GroupedRecentTaskInfo[] newArray(int i) {
            return new GroupedRecentTaskInfo[i];
        }
    };
    public StagedSplitBounds mStagedSplitBounds;
    public ActivityManager.RecentTaskInfo mTaskInfo1;
    public ActivityManager.RecentTaskInfo mTaskInfo2;

    public int describeContents() {
        return 0;
    }

    public GroupedRecentTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo) {
        this(recentTaskInfo, (ActivityManager.RecentTaskInfo) null, (StagedSplitBounds) null);
    }

    public GroupedRecentTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo, ActivityManager.RecentTaskInfo recentTaskInfo2, StagedSplitBounds stagedSplitBounds) {
        this.mTaskInfo1 = recentTaskInfo;
        this.mTaskInfo2 = recentTaskInfo2;
        this.mStagedSplitBounds = stagedSplitBounds;
    }

    public GroupedRecentTaskInfo(Parcel parcel) {
        this.mTaskInfo1 = (ActivityManager.RecentTaskInfo) parcel.readTypedObject(ActivityManager.RecentTaskInfo.CREATOR);
        this.mTaskInfo2 = (ActivityManager.RecentTaskInfo) parcel.readTypedObject(ActivityManager.RecentTaskInfo.CREATOR);
        this.mStagedSplitBounds = (StagedSplitBounds) parcel.readTypedObject(StagedSplitBounds.CREATOR);
    }

    public String toString() {
        String str = "Task1: " + getTaskInfo(this.mTaskInfo1) + ", Task2: " + getTaskInfo(this.mTaskInfo2);
        if (this.mStagedSplitBounds == null) {
            return str;
        }
        return str + ", SplitBounds: " + this.mStagedSplitBounds.toString();
    }

    public final String getTaskInfo(ActivityManager.RecentTaskInfo recentTaskInfo) {
        if (recentTaskInfo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(recentTaskInfo.taskId);
        sb.append(" baseIntent=");
        Intent intent = recentTaskInfo.baseIntent;
        sb.append(intent != null ? intent.getComponent() : "null");
        sb.append(" winMode=");
        sb.append(WindowConfiguration.windowingModeToString(recentTaskInfo.getWindowingMode()));
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mTaskInfo1, i);
        parcel.writeTypedObject(this.mTaskInfo2, i);
        parcel.writeTypedObject(this.mStagedSplitBounds, i);
    }
}
