package com.android.keyguard;

import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import dagger.Lazy;
import java.util.concurrent.Executor;

public class KeyguardDisplayManager {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final Context mContext;
    public final DisplayManager.DisplayListener mDisplayListener;
    public final DisplayManager mDisplayService;
    public final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    public MediaRouter mMediaRouter = null;
    public final MediaRouter.SimpleCallback mMediaRouterCallback;
    public final Lazy<NavigationBarController> mNavigationBarControllerLazy;
    public final SparseArray<Presentation> mPresentations = new SparseArray<>();
    public boolean mShowing;
    public final DisplayInfo mTmpDisplayInfo = new DisplayInfo();

    public KeyguardDisplayManager(Context context, Lazy<NavigationBarController> lazy, KeyguardStatusViewComponent.Factory factory, Executor executor) {
        AnonymousClass1 r1 = new DisplayManager.DisplayListener() {
            public void onDisplayChanged(int i) {
            }

            public void onDisplayAdded(int i) {
                Trace.beginSection("KeyguardDisplayManager#onDisplayAdded(displayId=" + i + ")");
                Display display = KeyguardDisplayManager.this.mDisplayService.getDisplay(i);
                if (KeyguardDisplayManager.this.mShowing) {
                    KeyguardDisplayManager.this.updateNavigationBarVisibility(i, false);
                    boolean unused = KeyguardDisplayManager.this.showPresentation(display);
                }
                Trace.endSection();
            }

            public void onDisplayRemoved(int i) {
                Trace.beginSection("KeyguardDisplayManager#onDisplayRemoved(displayId=" + i + ")");
                KeyguardDisplayManager.this.hidePresentation(i);
                Trace.endSection();
            }
        };
        this.mDisplayListener = r1;
        this.mMediaRouterCallback = new MediaRouter.SimpleCallback() {
            public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d("KeyguardDisplayManager", "onRouteSelected: type=" + i + ", info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }

            public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d("KeyguardDisplayManager", "onRouteUnselected: type=" + i + ", info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }

            public void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
                if (KeyguardDisplayManager.DEBUG) {
                    Log.d("KeyguardDisplayManager", "onRoutePresentationDisplayChanged: info=" + routeInfo);
                }
                KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
                keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
            }
        };
        this.mContext = context;
        this.mNavigationBarControllerLazy = lazy;
        this.mKeyguardStatusViewComponentFactory = factory;
        executor.execute(new KeyguardDisplayManager$$ExternalSyntheticLambda0(this));
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayService = displayManager;
        displayManager.registerDisplayListener(r1, (Handler) null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mMediaRouter = (MediaRouter) this.mContext.getSystemService(MediaRouter.class);
    }

    public final boolean isKeyguardShowable(Display display) {
        if (display == null) {
            if (DEBUG) {
                Log.i("KeyguardDisplayManager", "Cannot show Keyguard on null display");
            }
            return false;
        } else if (display.getDisplayId() == 0) {
            if (DEBUG) {
                Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on the default display");
            }
            return false;
        } else {
            display.getDisplayInfo(this.mTmpDisplayInfo);
            int i = this.mTmpDisplayInfo.flags;
            if ((i & 4) != 0) {
                if (DEBUG) {
                    Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on a private display");
                }
                return false;
            } else if ((i & 512) == 0) {
                return true;
            } else {
                if (DEBUG) {
                    Log.i("KeyguardDisplayManager", "Do not show KeyguardPresentation on an unlocked display");
                }
                return false;
            }
        }
    }

    public final boolean showPresentation(Display display) {
        if (!isKeyguardShowable(display)) {
            return false;
        }
        if (DEBUG) {
            Log.i("KeyguardDisplayManager", "Keyguard enabled on display: " + display);
        }
        int displayId = display.getDisplayId();
        if (this.mPresentations.get(displayId) == null) {
            KeyguardPresentation createPresentation = createPresentation(display);
            createPresentation.setOnDismissListener(new KeyguardDisplayManager$$ExternalSyntheticLambda1(this, createPresentation, displayId));
            try {
                createPresentation.show();
            } catch (WindowManager.InvalidDisplayException e) {
                Log.w("KeyguardDisplayManager", "Invalid display:", e);
                createPresentation = null;
            }
            if (createPresentation != null) {
                this.mPresentations.append(displayId, createPresentation);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showPresentation$1(Presentation presentation, int i, DialogInterface dialogInterface) {
        if (presentation.equals(this.mPresentations.get(i))) {
            this.mPresentations.remove(i);
        }
    }

    public KeyguardPresentation createPresentation(Display display) {
        return new KeyguardPresentation(this.mContext, display, this.mKeyguardStatusViewComponentFactory);
    }

    public final void hidePresentation(int i) {
        Presentation presentation = this.mPresentations.get(i);
        if (presentation != null) {
            presentation.dismiss();
            this.mPresentations.remove(i);
        }
    }

    public void show() {
        if (!this.mShowing) {
            if (DEBUG) {
                Log.v("KeyguardDisplayManager", "show");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.addCallback(4, this.mMediaRouterCallback, 8);
            } else {
                Log.w("KeyguardDisplayManager", "MediaRouter not yet initialized");
            }
            updateDisplays(true);
        }
        this.mShowing = true;
    }

    public void hide() {
        if (this.mShowing) {
            if (DEBUG) {
                Log.v("KeyguardDisplayManager", "hide");
            }
            MediaRouter mediaRouter = this.mMediaRouter;
            if (mediaRouter != null) {
                mediaRouter.removeCallback(this.mMediaRouterCallback);
            }
            updateDisplays(false);
        }
        this.mShowing = false;
    }

    public boolean updateDisplays(boolean z) {
        boolean z2 = false;
        if (z) {
            boolean z3 = false;
            for (Display display : this.mDisplayService.getDisplays()) {
                updateNavigationBarVisibility(display.getDisplayId(), false);
                z3 |= showPresentation(display);
            }
            return z3;
        }
        if (this.mPresentations.size() > 0) {
            z2 = true;
        }
        for (int size = this.mPresentations.size() - 1; size >= 0; size--) {
            updateNavigationBarVisibility(this.mPresentations.keyAt(size), true);
            this.mPresentations.valueAt(size).dismiss();
        }
        this.mPresentations.clear();
        return z2;
    }

    public final void updateNavigationBarVisibility(int i, boolean z) {
        NavigationBarView navigationBarView;
        if (i != 0 && (navigationBarView = this.mNavigationBarControllerLazy.get().getNavigationBarView(i)) != null) {
            if (z) {
                navigationBarView.getRootView().setVisibility(0);
            } else {
                navigationBarView.getRootView().setVisibility(8);
            }
        }
    }

    @VisibleForTesting
    public static final class KeyguardPresentation extends Presentation {
        public View mClock;
        public KeyguardClockSwitchController mKeyguardClockSwitchController;
        public final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
        public int mMarginLeft;
        public int mMarginTop;
        public Runnable mMoveTextRunnable = new Runnable() {
            public void run() {
                int r0 = KeyguardPresentation.this.mMarginLeft + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableWidth - KeyguardPresentation.this.mClock.getWidth()))));
                int r1 = KeyguardPresentation.this.mMarginTop + ((int) (Math.random() * ((double) (KeyguardPresentation.this.mUsableHeight - KeyguardPresentation.this.mClock.getHeight()))));
                KeyguardPresentation.this.mClock.setTranslationX((float) r0);
                KeyguardPresentation.this.mClock.setTranslationY((float) r1);
                KeyguardPresentation.this.mClock.postDelayed(KeyguardPresentation.this.mMoveTextRunnable, 10000);
            }
        };
        public int mUsableHeight;
        public int mUsableWidth;

        public void cancel() {
        }

        public KeyguardPresentation(Context context, Display display, KeyguardStatusViewComponent.Factory factory) {
            super(context, display, R$style.Theme_SystemUI_KeyguardPresentation, 2009);
            this.mKeyguardStatusViewComponentFactory = factory;
            setCancelable(false);
        }

        public void onDetachedFromWindow() {
            this.mClock.removeCallbacks(this.mMoveTextRunnable);
        }

        public void onDisplayChanged() {
            updateBounds();
            getWindow().getDecorView().requestLayout();
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            updateBounds();
            setContentView(LayoutInflater.from(getContext()).inflate(R$layout.keyguard_presentation, (ViewGroup) null));
            getWindow().getDecorView().setSystemUiVisibility(1792);
            getWindow().getAttributes().setFitInsetsTypes(0);
            getWindow().setNavigationBarContrastEnforced(false);
            getWindow().setNavigationBarColor(0);
            int i = R$id.clock;
            View findViewById = findViewById(i);
            this.mClock = findViewById;
            findViewById.post(this.mMoveTextRunnable);
            KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardStatusViewComponentFactory.build((KeyguardStatusView) findViewById(i)).getKeyguardClockSwitchController();
            this.mKeyguardClockSwitchController = keyguardClockSwitchController;
            keyguardClockSwitchController.setOnlyClock(true);
            this.mKeyguardClockSwitchController.init();
        }

        public final void updateBounds() {
            Rect bounds = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds();
            this.mUsableWidth = (bounds.width() * 80) / 100;
            this.mUsableHeight = (bounds.height() * 80) / 100;
            this.mMarginLeft = (bounds.width() * 20) / 200;
            this.mMarginTop = (bounds.height() * 20) / 200;
        }
    }
}
