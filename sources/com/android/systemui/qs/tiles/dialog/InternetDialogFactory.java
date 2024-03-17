package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: InternetDialogFactory.kt */
public final class InternetDialogFactory {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public static InternetDialog internetDialog;
    @NotNull
    public final Context context;
    @NotNull
    public final DialogLaunchAnimator dialogLaunchAnimator;
    @NotNull
    public final Executor executor;
    @NotNull
    public final Handler handler;
    @NotNull
    public final InternetDialogController internetDialogController;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final UiEventLogger uiEventLogger;

    public InternetDialogFactory(@NotNull Handler handler2, @NotNull Executor executor2, @NotNull InternetDialogController internetDialogController2, @NotNull Context context2, @NotNull UiEventLogger uiEventLogger2, @NotNull DialogLaunchAnimator dialogLaunchAnimator2, @NotNull KeyguardStateController keyguardStateController2) {
        this.handler = handler2;
        this.executor = executor2;
        this.internetDialogController = internetDialogController2;
        this.context = context2;
        this.uiEventLogger = uiEventLogger2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.keyguardStateController = keyguardStateController2;
    }

    /* compiled from: InternetDialogFactory.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void create(boolean z, boolean z2, boolean z3, @Nullable View view) {
        View view2 = view;
        if (internetDialog == null) {
            InternetDialog internetDialog2 = new InternetDialog(this.context, this, this.internetDialogController, z2, z3, z, this.uiEventLogger, this.handler, this.executor, this.keyguardStateController);
            internetDialog = internetDialog2;
            if (view2 != null) {
                DialogLaunchAnimator dialogLaunchAnimator2 = this.dialogLaunchAnimator;
                Intrinsics.checkNotNull(internetDialog2);
                dialogLaunchAnimator2.showFromView(internetDialog2, view2, true);
                return;
            }
            internetDialog2.show();
        } else if (InternetDialogFactoryKt.DEBUG) {
            Log.d("InternetDialogFactory", "InternetDialog is showing, do not create it twice.");
        }
    }

    public final void destroyDialog() {
        if (InternetDialogFactoryKt.DEBUG) {
            Log.d("InternetDialogFactory", "destroyDialog");
        }
        internetDialog = null;
    }
}
