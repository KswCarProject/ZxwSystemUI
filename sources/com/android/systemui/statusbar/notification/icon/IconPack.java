package com.android.systemui.statusbar.notification.icon;

import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.StatusBarIconView;

public final class IconPack {
    public final StatusBarIconView mAodIcon;
    public final boolean mAreIconsAvailable;
    public final StatusBarIconView mCenteredIcon;
    public boolean mIsImportantConversation;
    public StatusBarIcon mPeopleAvatarDescriptor;
    public final StatusBarIconView mShelfIcon;
    public StatusBarIcon mSmallIconDescriptor;
    public final StatusBarIconView mStatusBarIcon;

    public static IconPack buildEmptyPack(IconPack iconPack) {
        return new IconPack(false, (StatusBarIconView) null, (StatusBarIconView) null, (StatusBarIconView) null, (StatusBarIconView) null, iconPack);
    }

    public static IconPack buildPack(StatusBarIconView statusBarIconView, StatusBarIconView statusBarIconView2, StatusBarIconView statusBarIconView3, StatusBarIconView statusBarIconView4, IconPack iconPack) {
        return new IconPack(true, statusBarIconView, statusBarIconView2, statusBarIconView3, statusBarIconView4, iconPack);
    }

    public IconPack(boolean z, StatusBarIconView statusBarIconView, StatusBarIconView statusBarIconView2, StatusBarIconView statusBarIconView3, StatusBarIconView statusBarIconView4, IconPack iconPack) {
        this.mAreIconsAvailable = z;
        this.mStatusBarIcon = statusBarIconView;
        this.mShelfIcon = statusBarIconView2;
        this.mCenteredIcon = statusBarIconView4;
        this.mAodIcon = statusBarIconView3;
        if (iconPack != null) {
            this.mIsImportantConversation = iconPack.mIsImportantConversation;
        }
    }

    public StatusBarIconView getStatusBarIcon() {
        return this.mStatusBarIcon;
    }

    public StatusBarIconView getShelfIcon() {
        return this.mShelfIcon;
    }

    public StatusBarIconView getCenteredIcon() {
        return this.mCenteredIcon;
    }

    public StatusBarIconView getAodIcon() {
        return this.mAodIcon;
    }

    public StatusBarIcon getSmallIconDescriptor() {
        return this.mSmallIconDescriptor;
    }

    public void setSmallIconDescriptor(StatusBarIcon statusBarIcon) {
        this.mSmallIconDescriptor = statusBarIcon;
    }

    public StatusBarIcon getPeopleAvatarDescriptor() {
        return this.mPeopleAvatarDescriptor;
    }

    public void setPeopleAvatarDescriptor(StatusBarIcon statusBarIcon) {
        this.mPeopleAvatarDescriptor = statusBarIcon;
    }

    public boolean isImportantConversation() {
        return this.mIsImportantConversation;
    }

    public void setImportantConversation(boolean z) {
        this.mIsImportantConversation = z;
    }

    public boolean getAreIconsAvailable() {
        return this.mAreIconsAvailable;
    }
}
