package com.android.systemui.navigationbar;

import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.SparseArray;
import android.view.IWallpaperVisibilityListener;
import android.view.IWindowManager;
import android.view.View;
import androidx.appcompat.R$styleable;
import com.android.systemui.R$bool;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.util.Utils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class NavigationBarTransitions extends BarTransitions implements LightBarTransitionsController.DarkIntensityApplier {
    public final boolean mAllowAutoDimWallpaperNotVisible;
    public boolean mAutoDim;
    public List<DarkIntensityListener> mDarkIntensityListeners;
    public final Handler mHandler = Handler.getMain();
    public final LightBarTransitionsController mLightTransitionsController;
    public boolean mLightsOut;
    public List<Listener> mListeners = new ArrayList();
    public int mNavBarMode = 0;
    public View mNavButtons;
    public final NavigationBarView mView;
    public final IWallpaperVisibilityListener mWallpaperVisibilityListener;
    public boolean mWallpaperVisible;
    @NotNull
    public final IWindowManager mWindowManagerService;

    public interface DarkIntensityListener {
        void onDarkIntensity(float f);
    }

    public interface Listener {
        void onTransition(int i);
    }

    public NavigationBarTransitions(NavigationBarView navigationBarView, IWindowManager iWindowManager, LightBarTransitionsController.Factory factory) {
        super(navigationBarView, R$drawable.nav_background);
        AnonymousClass1 r1 = new IWallpaperVisibilityListener.Stub() {
            public void onWallpaperVisibilityChanged(boolean z, int i) throws RemoteException {
                NavigationBarTransitions.this.mWallpaperVisible = z;
                NavigationBarTransitions.this.mHandler.post(new NavigationBarTransitions$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onWallpaperVisibilityChanged$0() {
                NavigationBarTransitions.this.applyLightsOut(true, false);
            }
        };
        this.mWallpaperVisibilityListener = r1;
        this.mView = navigationBarView;
        this.mWindowManagerService = iWindowManager;
        this.mLightTransitionsController = factory.create(this);
        this.mAllowAutoDimWallpaperNotVisible = navigationBarView.getContext().getResources().getBoolean(R$bool.config_navigation_bar_enable_auto_dim_no_visible_wallpaper);
        this.mDarkIntensityListeners = new ArrayList();
        try {
            this.mWallpaperVisible = iWindowManager.registerWallpaperVisibilityListener(r1, 0);
        } catch (RemoteException unused) {
        }
        this.mView.addOnLayoutChangeListener(new NavigationBarTransitions$$ExternalSyntheticLambda0(this));
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(R$id.nav_buttons);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        View currentView = this.mView.getCurrentView();
        if (currentView != null) {
            this.mNavButtons = currentView.findViewById(R$id.nav_buttons);
            applyLightsOut(false, true);
        }
    }

    public void init() {
        applyModeBackground(-1, getMode(), false);
        applyLightsOut(false, true);
    }

    public void destroy() {
        try {
            this.mWindowManagerService.unregisterWallpaperVisibilityListener(this.mWallpaperVisibilityListener, 0);
        } catch (RemoteException unused) {
        }
        this.mLightTransitionsController.destroy();
    }

    public void setAutoDim(boolean z) {
        if ((!z || !Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) && this.mAutoDim != z) {
            this.mAutoDim = z;
            applyLightsOut(true, false);
        }
    }

    public void setBackgroundFrame(Rect rect) {
        this.mBarBackground.setFrame(rect);
    }

    public void setBackgroundOverrideAlpha(float f) {
        this.mBarBackground.setOverrideAlpha(f);
    }

    public boolean isLightsOut(int i) {
        return super.isLightsOut(i) || (this.mAllowAutoDimWallpaperNotVisible && this.mAutoDim && !this.mWallpaperVisible && i != 5);
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mLightTransitionsController;
    }

    public void onTransition(int i, int i2, boolean z) {
        super.onTransition(i, i2, z);
        applyLightsOut(z, false);
        for (Listener onTransition : this.mListeners) {
            onTransition.onTransition(i2);
        }
    }

    public final void applyLightsOut(boolean z, boolean z2) {
        applyLightsOut(isLightsOut(getMode()), z, z2);
    }

    public final void applyLightsOut(boolean z, boolean z2, boolean z3) {
        if (z3 || z != this.mLightsOut) {
            this.mLightsOut = z;
            View view = this.mNavButtons;
            if (view != null) {
                view.animate().cancel();
                float currentDarkIntensity = z ? (this.mLightTransitionsController.getCurrentDarkIntensity() / 10.0f) + 0.6f : 1.0f;
                if (!z2) {
                    this.mNavButtons.setAlpha(currentDarkIntensity);
                } else {
                    this.mNavButtons.animate().alpha(currentDarkIntensity).setDuration((long) (z ? 1500 : 250)).start();
                }
            }
        }
    }

    public void reapplyDarkIntensity() {
        applyDarkIntensity(this.mLightTransitionsController.getCurrentDarkIntensity());
    }

    public void applyDarkIntensity(float f) {
        SparseArray<ButtonDispatcher> buttonDispatchers = this.mView.getButtonDispatchers();
        for (int size = buttonDispatchers.size() - 1; size >= 0; size--) {
            buttonDispatchers.valueAt(size).setDarkIntensity(f);
        }
        this.mView.getRotationButtonController().setDarkIntensity(f);
        for (DarkIntensityListener onDarkIntensity : this.mDarkIntensityListeners) {
            onDarkIntensity.onDarkIntensity(f);
        }
        if (this.mAutoDim) {
            applyLightsOut(false, true);
        }
    }

    public int getTintAnimationDuration() {
        return Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode) ? Math.max(1700, 400) : R$styleable.AppCompatTheme_windowFixedHeightMajor;
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public float addDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.add(darkIntensityListener);
        return this.mLightTransitionsController.getCurrentDarkIntensity();
    }

    public void removeDarkIntensityListener(DarkIntensityListener darkIntensityListener) {
        this.mDarkIntensityListeners.remove(darkIntensityListener);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarTransitions:");
        printWriter.println("  mMode: " + getMode());
        printWriter.println("  mAlwaysOpaque: " + isAlwaysOpaque());
        printWriter.println("  mAllowAutoDimWallpaperNotVisible: " + this.mAllowAutoDimWallpaperNotVisible);
        printWriter.println("  mWallpaperVisible: " + this.mWallpaperVisible);
        printWriter.println("  mLightsOut: " + this.mLightsOut);
        printWriter.println("  mAutoDim: " + this.mAutoDim);
        printWriter.println("  bg overrideAlpha: " + this.mBarBackground.getOverrideAlpha());
        printWriter.println("  bg color: " + this.mBarBackground.getColor());
        printWriter.println("  bg frame: " + this.mBarBackground.getFrame());
    }

    public void addListener(Listener listener) {
        this.mListeners.add(listener);
    }
}
