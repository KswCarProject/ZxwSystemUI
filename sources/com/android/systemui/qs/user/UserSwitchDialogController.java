package com.android.systemui.qs.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import javax.inject.Provider;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitchDialogController.kt */
public final class UserSwitchDialogController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Intent USER_SETTINGS_INTENT = new Intent("android.settings.USER_SETTINGS");
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final Function1<Context, SystemUIDialog> dialogFactory;
    @NotNull
    public final DialogLaunchAnimator dialogLaunchAnimator;
    @NotNull
    public final FalsingManager falsingManager;
    @NotNull
    public final UiEventLogger uiEventLogger;
    @NotNull
    public final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    /* compiled from: UserSwitchDialogController.kt */
    public interface DialogShower extends DialogInterface {
        void showDialog(@NotNull Dialog dialog);
    }

    public UserSwitchDialogController(@NotNull Provider<UserDetailView.Adapter> provider, @NotNull ActivityStarter activityStarter2, @NotNull FalsingManager falsingManager2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull UiEventLogger uiEventLogger2, @NotNull Function1<? super Context, ? extends SystemUIDialog> function1) {
        this.userDetailViewAdapterProvider = provider;
        this.activityStarter = activityStarter2;
        this.falsingManager = falsingManager2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogFactory = function1;
    }

    public UserSwitchDialogController(@NotNull Provider<UserDetailView.Adapter> provider, @NotNull ActivityStarter activityStarter2, @NotNull FalsingManager falsingManager2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull UiEventLogger uiEventLogger2) {
        this(provider, activityStarter2, falsingManager2, dialogLaunchAnimator2, uiEventLogger2, AnonymousClass1.INSTANCE);
    }

    /* compiled from: UserSwitchDialogController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void showDialog(@NotNull View view) {
        SystemUIDialog invoke = this.dialogFactory.invoke(view.getContext());
        invoke.setShowForAllUsers(true);
        invoke.setCanceledOnTouchOutside(true);
        invoke.setTitle(R$string.qs_user_switch_dialog_title);
        invoke.setPositiveButton(R$string.quick_settings_done, new UserSwitchDialogController$showDialog$1$1(this));
        invoke.setNeutralButton(R$string.quick_settings_more_user_settings, new UserSwitchDialogController$showDialog$1$2(this, invoke), false);
        View inflate = LayoutInflater.from(invoke.getContext()).inflate(R$layout.qs_user_dialog_content, (ViewGroup) null);
        invoke.setView(inflate);
        UserDetailView.Adapter adapter = this.userDetailViewAdapterProvider.get();
        adapter.linkToViewGroup((ViewGroup) inflate.findViewById(R$id.grid));
        DialogLaunchAnimator.showFromView$default(this.dialogLaunchAnimator, invoke, view, false, 4, (Object) null);
        this.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_OPEN);
        adapter.injectDialogShower(new DialogShowerImpl(invoke, this.dialogLaunchAnimator));
    }

    /* compiled from: UserSwitchDialogController.kt */
    public static final class DialogShowerImpl implements DialogInterface, DialogShower {
        @NotNull
        public final Dialog animateFrom;
        @NotNull
        public final DialogLaunchAnimator dialogLaunchAnimator;

        public void cancel() {
            this.animateFrom.cancel();
        }

        public void dismiss() {
            this.animateFrom.dismiss();
        }

        public DialogShowerImpl(@NotNull Dialog dialog, @NotNull DialogLaunchAnimator dialogLaunchAnimator2) {
            this.animateFrom = dialog;
            this.dialogLaunchAnimator = dialogLaunchAnimator2;
        }

        public void showDialog(@NotNull Dialog dialog) {
            DialogLaunchAnimator.showFromDialog$default(this.dialogLaunchAnimator, dialog, this.animateFrom, false, 4, (Object) null);
        }
    }
}
