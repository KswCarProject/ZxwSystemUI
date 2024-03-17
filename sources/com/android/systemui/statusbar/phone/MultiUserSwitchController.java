package com.android.systemui.statusbar.phone;

import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.FooterActionsView;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.util.ViewController;

public class MultiUserSwitchController extends ViewController<MultiUserSwitch> {
    public final ActivityStarter mActivityStarter;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final View.OnClickListener mOnClickListener;
    public UserSwitcherController.BaseUserAdapter mUserListener;
    public final UserManager mUserManager;
    public final UserSwitchDialogController mUserSwitchDialogController;
    public final UserSwitcherController mUserSwitcherController;

    public static class Factory {
        public final ActivityStarter mActivityStarter;
        public final FalsingManager mFalsingManager;
        public final FeatureFlags mFeatureFlags;
        public final UserManager mUserManager;
        public final UserSwitchDialogController mUserSwitchDialogController;
        public final UserSwitcherController mUserSwitcherController;

        public Factory(UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
            this.mUserManager = userManager;
            this.mUserSwitcherController = userSwitcherController;
            this.mFalsingManager = falsingManager;
            this.mUserSwitchDialogController = userSwitchDialogController;
            this.mActivityStarter = activityStarter;
            this.mFeatureFlags = featureFlags;
        }

        public MultiUserSwitchController create(FooterActionsView footerActionsView) {
            return new MultiUserSwitchController((MultiUserSwitch) footerActionsView.findViewById(R$id.multi_user_switch), this.mUserManager, this.mUserSwitcherController, this.mFalsingManager, this.mUserSwitchDialogController, this.mFeatureFlags, this.mActivityStarter);
        }
    }

    public MultiUserSwitchController(MultiUserSwitch multiUserSwitch, UserManager userManager, UserSwitcherController userSwitcherController, FalsingManager falsingManager, UserSwitchDialogController userSwitchDialogController, FeatureFlags featureFlags, ActivityStarter activityStarter) {
        super(multiUserSwitch);
        this.mOnClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (!MultiUserSwitchController.this.mFalsingManager.isFalseTap(1)) {
                    if (MultiUserSwitchController.this.mFeatureFlags.isEnabled(Flags.FULL_SCREEN_USER_SWITCHER)) {
                        Intent intent = new Intent(view.getContext(), UserSwitcherActivity.class);
                        intent.addFlags(335544320);
                        MultiUserSwitchController.this.mActivityStarter.startActivity(intent, true, ActivityLaunchAnimator.Controller.fromView(view, (Integer) null), true, UserHandle.SYSTEM);
                        return;
                    }
                    MultiUserSwitchController.this.mUserSwitchDialogController.showDialog(view);
                }
            }
        };
        this.mUserManager = userManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFalsingManager = falsingManager;
        this.mUserSwitchDialogController = userSwitchDialogController;
        this.mFeatureFlags = featureFlags;
        this.mActivityStarter = activityStarter;
    }

    public void onInit() {
        registerListener();
        ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
    }

    public void onViewAttached() {
        ((MultiUserSwitch) this.mView).setOnClickListener(this.mOnClickListener);
    }

    public void onViewDetached() {
        ((MultiUserSwitch) this.mView).setOnClickListener((View.OnClickListener) null);
    }

    public final void registerListener() {
        UserSwitcherController userSwitcherController;
        if (this.mUserManager.isUserSwitcherEnabled() && this.mUserListener == null && (userSwitcherController = this.mUserSwitcherController) != null) {
            this.mUserListener = new UserSwitcherController.BaseUserAdapter(userSwitcherController) {
                public View getView(int i, View view, ViewGroup viewGroup) {
                    return null;
                }

                public void notifyDataSetChanged() {
                    ((MultiUserSwitch) MultiUserSwitchController.this.mView).refreshContentDescription(MultiUserSwitchController.this.getCurrentUser());
                }
            };
            ((MultiUserSwitch) this.mView).refreshContentDescription(getCurrentUser());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$getCurrentUser$0() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled());
    }

    public final String getCurrentUser() {
        if (((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda1(this))).booleanValue()) {
            return this.mUserSwitcherController.getCurrentUserName();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isMultiUserEnabled$1() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled(getResources().getBoolean(R$bool.qs_show_user_switcher_for_single_user)));
    }

    public boolean isMultiUserEnabled() {
        return ((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda0(this))).booleanValue();
    }
}
