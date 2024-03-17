package com.android.systemui.statusbar.phone;

import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Optional;

public class ShadeControllerImpl implements ShadeController {
    public final Lazy<AssistManager> mAssistManagerLazy;
    public final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    public final CommandQueue mCommandQueue;
    public final int mDisplayId;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public final ArrayList<Runnable> mPostCollapseRunnables = new ArrayList<>();
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final StatusBarStateController mStatusBarStateController;

    public ShadeControllerImpl(CommandQueue commandQueue, StatusBarStateController statusBarStateController, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, WindowManager windowManager, Lazy<Optional<CentralSurfaces>> lazy, Lazy<AssistManager> lazy2) {
        this.mCommandQueue = commandQueue;
        this.mStatusBarStateController = statusBarStateController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mDisplayId = windowManager.getDefaultDisplay().getDisplayId();
        this.mCentralSurfacesOptionalLazy = lazy;
        this.mAssistManagerLazy = lazy2;
    }

    public void instantExpandNotificationsPanel() {
        getCentralSurfaces().makeExpandedVisible(true);
        getNotificationPanelViewController().expand(false);
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, false);
    }

    public void animateCollapsePanels() {
        animateCollapsePanels(0);
    }

    public void animateCollapsePanels(int i) {
        animateCollapsePanels(i, false, false, 1.0f);
    }

    public void animateCollapsePanels(int i, boolean z) {
        animateCollapsePanels(i, z, false, 1.0f);
    }

    public void animateCollapsePanels(int i, boolean z, boolean z2) {
        animateCollapsePanels(i, z, z2, 1.0f);
    }

    public void animateCollapsePanels(int i, boolean z, boolean z2, float f) {
        if (z || this.mStatusBarStateController.getState() == 0) {
            Log.v("ShadeControllerImpl", "NotificationShadeWindow: " + getNotificationShadeWindowView() + " canPanelBeCollapsed(): " + getNotificationPanelViewController().canPanelBeCollapsed());
            if (getNotificationShadeWindowView() != null && getNotificationPanelViewController().canPanelBeCollapsed() && (i & 4) == 0) {
                this.mNotificationShadeWindowController.setNotificationShadeFocusable(false);
                getCentralSurfaces().getNotificationShadeWindowViewController().cancelExpandHelper();
                getNotificationPanelViewController().collapsePanel(true, z2, f);
                return;
            }
            return;
        }
        runPostCollapseRunnables();
    }

    public boolean closeShadeIfOpen() {
        if (!getNotificationPanelViewController().isFullyCollapsed()) {
            this.mCommandQueue.animateCollapsePanels(2, true);
            getCentralSurfaces().visibilityChanged(false);
            this.mAssistManagerLazy.get().hideAssist();
        }
        return false;
    }

    public boolean isShadeOpen() {
        NotificationPanelViewController notificationPanelViewController = getNotificationPanelViewController();
        return notificationPanelViewController.isExpanding() || notificationPanelViewController.isFullyExpanded();
    }

    public void postOnShadeExpanded(final Runnable runnable) {
        getNotificationPanelViewController().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (ShadeControllerImpl.this.getCentralSurfaces().getNotificationShadeWindowView().isVisibleToUser()) {
                    ShadeControllerImpl.this.getNotificationPanelViewController().removeOnGlobalLayoutListener(this);
                    ShadeControllerImpl.this.getNotificationPanelViewController().getView().post(runnable);
                }
            }
        });
    }

    public void addPostCollapseAction(Runnable runnable) {
        this.mPostCollapseRunnables.add(runnable);
    }

    public void runPostCollapseRunnables() {
        ArrayList arrayList = new ArrayList(this.mPostCollapseRunnables);
        this.mPostCollapseRunnables.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ((Runnable) arrayList.get(i)).run();
        }
        this.mStatusBarKeyguardViewManager.readyForKeyguardDone();
    }

    public boolean collapsePanel() {
        if (getNotificationPanelViewController().isFullyCollapsed()) {
            return false;
        }
        animateCollapsePanels(2, true, true);
        getCentralSurfaces().visibilityChanged(false);
        return true;
    }

    public void collapsePanel(boolean z) {
        if (z) {
            if (!collapsePanel()) {
                runPostCollapseRunnables();
            }
        } else if (!getPresenter().isPresenterFullyCollapsed()) {
            getCentralSurfaces().instantCollapseNotificationPanel();
            getCentralSurfaces().visibilityChanged(false);
        } else {
            runPostCollapseRunnables();
        }
    }

    public final CentralSurfaces getCentralSurfaces() {
        return (CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get();
    }

    public final NotificationPresenter getPresenter() {
        return getCentralSurfaces().getPresenter();
    }

    public NotificationShadeWindowView getNotificationShadeWindowView() {
        return getCentralSurfaces().getNotificationShadeWindowView();
    }

    public final NotificationPanelViewController getNotificationPanelViewController() {
        return getCentralSurfaces().getPanelController();
    }
}
