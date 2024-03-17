package com.android.systemui.user;

import android.graphics.drawable.Drawable;
import com.android.systemui.R$color;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2 extends Lambda implements Function1<UserSwitcherController.UserRecord, Drawable> {
    public final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    @NotNull
    public final Drawable invoke(@NotNull UserSwitcherController.UserRecord userRecord) {
        Drawable mutate = this.this$0.adapter.findUserIcon(userRecord).mutate();
        UserSwitcherActivity userSwitcherActivity = this.this$0;
        mutate.setTint(userSwitcherActivity.getResources().getColor(R$color.user_switcher_fullscreen_popup_item_tint, userSwitcherActivity.getTheme()));
        return mutate;
    }
}
