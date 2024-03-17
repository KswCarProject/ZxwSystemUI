package com.android.systemui.statusbar.phone.shade.transition;

import android.content.Context;
import android.content.res.Configuration;
import com.android.systemui.R$bool;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelStateListener;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Function;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ShadeTransitionController.kt */
public final class ShadeTransitionController {
    @NotNull
    public final Context context;
    public boolean inSplitShade;
    @NotNull
    public final NoOpOverScroller noOpOverScroller;
    public NotificationPanelViewController notificationPanelViewController;
    public NotificationStackScrollLayoutController notificationStackScrollLayoutController;
    public QS qs;
    @NotNull
    public final Lazy splitShadeOverScroller$delegate = LazyKt__LazyJVMKt.lazy(new ShadeTransitionController$splitShadeOverScroller$2(this));
    @NotNull
    public final SplitShadeOverScroller.Factory splitShadeOverScrollerFactory;

    public ShadeTransitionController(@NotNull ConfigurationController configurationController, @NotNull PanelExpansionStateManager panelExpansionStateManager, @NotNull Context context2, @NotNull SplitShadeOverScroller.Factory factory, @NotNull NoOpOverScroller noOpOverScroller2) {
        this.context = context2;
        this.splitShadeOverScrollerFactory = factory;
        this.noOpOverScroller = noOpOverScroller2;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ ShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        panelExpansionStateManager.addExpansionListener(new Object() {
            public final boolean equals(@Nullable Object obj) {
                if (!(obj instanceof PanelExpansionListener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            @NotNull
            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ShadeTransitionController.this, ShadeTransitionController.class, "onPanelExpansionChanged", "onPanelExpansionChanged(Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onPanelExpansionChanged(@NotNull PanelExpansionChangeEvent panelExpansionChangeEvent) {
                ShadeTransitionController.this.onPanelExpansionChanged(panelExpansionChangeEvent);
            }
        });
        panelExpansionStateManager.addStateListener(new Object() {
            public final boolean equals(@Nullable Object obj) {
                if (!(obj instanceof PanelStateListener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            @NotNull
            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ShadeTransitionController.this, ShadeTransitionController.class, "onPanelStateChanged", "onPanelStateChanged(I)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onPanelStateChanged(int i) {
                ShadeTransitionController.this.onPanelStateChanged(i);
            }
        });
    }

    public final void setNotificationPanelViewController(@NotNull NotificationPanelViewController notificationPanelViewController2) {
        this.notificationPanelViewController = notificationPanelViewController2;
    }

    @NotNull
    public final NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.notificationStackScrollLayoutController;
        if (notificationStackScrollLayoutController2 != null) {
            return notificationStackScrollLayoutController2;
        }
        return null;
    }

    public final void setNotificationStackScrollLayoutController(@NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController2) {
        this.notificationStackScrollLayoutController = notificationStackScrollLayoutController2;
    }

    @NotNull
    public final QS getQs() {
        QS qs2 = this.qs;
        if (qs2 != null) {
            return qs2;
        }
        return null;
    }

    public final void setQs(@NotNull QS qs2) {
        this.qs = qs2;
    }

    public final SplitShadeOverScroller getSplitShadeOverScroller() {
        return (SplitShadeOverScroller) this.splitShadeOverScroller$delegate.getValue();
    }

    public final ShadeOverScroller getShadeOverScroller() {
        if (!this.inSplitShade || !propertiesInitialized()) {
            return this.noOpOverScroller;
        }
        return getSplitShadeOverScroller();
    }

    public final void updateResources() {
        this.inSplitShade = this.context.getResources().getBoolean(R$bool.config_use_split_notification_shade);
    }

    public final void onPanelStateChanged(int i) {
        getShadeOverScroller().onPanelStateChanged(i);
    }

    public final void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        getShadeOverScroller().onDragDownAmountChanged(panelExpansionChangeEvent.getDragDownPxAmount());
    }

    public final boolean propertiesInitialized() {
        return (this.qs == null || this.notificationPanelViewController == null || this.notificationStackScrollLayoutController == null) ? false : true;
    }
}
