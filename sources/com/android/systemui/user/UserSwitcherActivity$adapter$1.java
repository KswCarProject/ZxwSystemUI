package com.android.systemui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$adapter$1 extends UserSwitcherController.BaseUserAdapter {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$adapter$1(UserSwitcherActivity userSwitcherActivity, UserSwitcherController userSwitcherController) {
        super(userSwitcherController);
        this.this$0 = userSwitcherActivity;
    }

    @NotNull
    public View getView(int i, @Nullable View view, @NotNull ViewGroup viewGroup) {
        UserSwitcherController.UserRecord item = getItem(i);
        ViewGroup viewGroup2 = (ViewGroup) view;
        if (viewGroup2 == null) {
            View inflate = this.this$0.layoutInflater.inflate(R$layout.user_switcher_fullscreen_item, viewGroup, false);
            if (inflate != null) {
                viewGroup2 = (ViewGroup) inflate;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
            }
        }
        View childAt = viewGroup2.getChildAt(0);
        if (childAt != null) {
            ((ImageView) childAt).setImageDrawable(getDrawable(item));
            View childAt2 = viewGroup2.getChildAt(1);
            if (childAt2 != null) {
                TextView textView = (TextView) childAt2;
                textView.setText(getName(textView.getContext(), item));
                viewGroup2.setEnabled(item.isSwitchToEnabled);
                viewGroup2.setAlpha(viewGroup2.isEnabled() ? 1.0f : 0.38f);
                viewGroup2.setTag("user_view");
                return viewGroup2;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.ImageView");
    }

    @NotNull
    public String getName(@NotNull Context context, @NotNull UserSwitcherController.UserRecord userRecord) {
        if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.manageUserRecord)) {
            return this.this$0.getString(R$string.manage_users);
        }
        return super.getName(context, userRecord);
    }

    @NotNull
    public final Drawable findUserIcon(@NotNull UserSwitcherController.UserRecord userRecord) {
        if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.manageUserRecord)) {
            return this.this$0.getDrawable(R$drawable.ic_manage_users);
        }
        if (userRecord.info == null) {
            return UserSwitcherController.BaseUserAdapter.getIconDrawable(this.this$0, userRecord);
        }
        Bitmap userIcon = this.this$0.userManager.getUserIcon(userRecord.info.id);
        if (userIcon != null) {
            return new BitmapDrawable(userIcon);
        }
        return UserIcons.getDefaultUserIcon(this.this$0.getResources(), userRecord.info.id, false);
    }

    public final int getTotalUserViews() {
        ArrayList<UserSwitcherController.UserRecord> users = getUsers();
        int i = 0;
        if (!(users instanceof Collection) || !users.isEmpty()) {
            for (UserSwitcherController.UserRecord doNotRenderUserView : users) {
                if ((!doNotRenderUserView(doNotRenderUserView)) && (i = i + 1) < 0) {
                    CollectionsKt__CollectionsKt.throwCountOverflow();
                }
            }
        }
        return i;
    }

    public final boolean doNotRenderUserView(@NotNull UserSwitcherController.UserRecord userRecord) {
        return userRecord.isAddUser || userRecord.isAddSupervisedUser || (userRecord.isGuest && userRecord.info == null);
    }

    public final Drawable getDrawable(UserSwitcherController.UserRecord userRecord) {
        Drawable drawable;
        if (userRecord.isGuest) {
            drawable = this.this$0.getDrawable(R$drawable.ic_account_circle);
        } else {
            drawable = findUserIcon(userRecord);
        }
        drawable.mutate();
        if (!userRecord.isCurrent && !userRecord.isSwitchToEnabled) {
            drawable.setTint(this.this$0.getResources().getColor(R$color.kg_user_switcher_restricted_avatar_icon_color, this.this$0.getTheme()));
        }
        Drawable mutate = this.this$0.getDrawable(R$drawable.user_switcher_icon_large).mutate();
        if (mutate != null) {
            LayerDrawable layerDrawable = (LayerDrawable) mutate;
            if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.userSwitcherController.getCurrentUserRecord())) {
                Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(R$id.ring);
                if (findDrawableByLayerId != null) {
                    UserSwitcherActivity userSwitcherActivity = this.this$0;
                    ((GradientDrawable) findDrawableByLayerId).setStroke(userSwitcherActivity.getResources().getDimensionPixelSize(R$dimen.user_switcher_icon_selected_width), Utils.getColorAttrDefaultColor(userSwitcherActivity, 17956900));
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
                }
            }
            layerDrawable.setDrawableByLayerId(R$id.user_avatar, drawable);
            return layerDrawable;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.this$0.buildUserViews();
    }
}
