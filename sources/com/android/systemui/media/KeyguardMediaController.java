package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: KeyguardMediaController.kt */
public final class KeyguardMediaController {
    @NotNull
    public final KeyguardBypassController bypassController;
    @NotNull
    public final Context context;
    @NotNull
    public final MediaHost mediaHost;
    @NotNull
    public final NotificationLockscreenUserManager notifLockscreenUserManager;
    @Nullable
    public MediaContainerView singlePaneContainer;
    @Nullable
    public ViewGroup splitShadeContainer;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public boolean useSplitShade;
    @Nullable
    public Function1<? super Boolean, Unit> visibilityChangedListener;
    public boolean visible;

    public static /* synthetic */ void getUseSplitShade$annotations() {
    }

    public KeyguardMediaController(@NotNull MediaHost mediaHost2, @NotNull KeyguardBypassController keyguardBypassController, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull Context context2, @NotNull ConfigurationController configurationController) {
        this.mediaHost = mediaHost2;
        this.bypassController = keyguardBypassController;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.context = context2;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            public final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            public void onStateChanged(int i) {
                this.this$0.refreshMediaPosition();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        mediaHost2.setExpansion(1.0f);
        mediaHost2.setShowsOnlyActiveMedia(true);
        mediaHost2.setFalsingProtectionNeeded(true);
        mediaHost2.init(2);
        updateResources();
    }

    public final void updateResources() {
        setUseSplitShade(LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources()));
    }

    public final void setUseSplitShade(boolean z) {
        if (this.useSplitShade != z) {
            this.useSplitShade = z;
            reattachHostView();
            refreshMediaPosition();
        }
    }

    public final void setVisibilityChangedListener(@Nullable Function1<? super Boolean, Unit> function1) {
        this.visibilityChangedListener = function1;
    }

    @Nullable
    public final MediaContainerView getSinglePaneContainer() {
        return this.singlePaneContainer;
    }

    public final void attachSinglePaneContainer(@Nullable MediaContainerView mediaContainerView) {
        boolean z = this.singlePaneContainer == null;
        this.singlePaneContainer = mediaContainerView;
        if (z) {
            this.mediaHost.addVisibilityChangeListener(new KeyguardMediaController$attachSinglePaneContainer$1(this));
        }
        reattachHostView();
        onMediaHostVisibilityChanged(this.mediaHost.getVisible());
    }

    public final void onMediaHostVisibilityChanged(boolean z) {
        refreshMediaPosition();
        if (z) {
            ViewGroup.LayoutParams layoutParams = this.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
    }

    public final void attachSplitShadeContainer(@NotNull ViewGroup viewGroup) {
        this.splitShadeContainer = viewGroup;
        reattachHostView();
        refreshMediaPosition();
    }

    public final void reattachHostView() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        boolean z;
        if (this.useSplitShade) {
            viewGroup2 = this.splitShadeContainer;
            viewGroup = this.singlePaneContainer;
        } else {
            viewGroup = this.splitShadeContainer;
            viewGroup2 = this.singlePaneContainer;
        }
        boolean z2 = true;
        if (viewGroup != null && viewGroup.getChildCount() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            viewGroup.removeAllViews();
        }
        if (viewGroup2 == null || viewGroup2.getChildCount() != 0) {
            z2 = false;
        }
        if (z2) {
            ViewParent parent = this.mediaHost.getHostView().getParent();
            if (parent != null) {
                ViewGroup viewGroup3 = parent instanceof ViewGroup ? (ViewGroup) parent : null;
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.mediaHost.getHostView());
                }
            }
            viewGroup2.addView(this.mediaHost.getHostView());
        }
    }

    public final void refreshMediaPosition() {
        boolean z = false;
        boolean z2 = this.statusBarStateController.getState() == 1;
        if (this.mediaHost.getVisible() && !this.bypassController.getBypassEnabled() && z2 && this.notifLockscreenUserManager.shouldShowLockscreenNotifications()) {
            z = true;
        }
        this.visible = z;
        if (z) {
            showMediaPlayer();
        } else {
            hideMediaPlayer();
        }
    }

    public final void showMediaPlayer() {
        if (this.useSplitShade) {
            setVisibility(this.splitShadeContainer, 0);
            setVisibility(this.singlePaneContainer, 8);
            return;
        }
        setVisibility(this.singlePaneContainer, 0);
        setVisibility(this.splitShadeContainer, 8);
    }

    public final void hideMediaPlayer() {
        setVisibility(this.splitShadeContainer, 8);
        setVisibility(this.singlePaneContainer, 8);
    }

    public final void setVisibility(ViewGroup viewGroup, int i) {
        Function1<? super Boolean, Unit> function1;
        Integer valueOf = viewGroup == null ? null : Integer.valueOf(viewGroup.getVisibility());
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
        if ((valueOf == null || valueOf.intValue() != i) && (function1 = this.visibilityChangedListener) != null) {
            function1.invoke(Boolean.valueOf(i == 0));
        }
    }
}
