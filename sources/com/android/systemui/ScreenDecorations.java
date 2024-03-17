package com.android.systemui;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.graphics.common.DisplayDecorationSupport;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.util.DisplayUtils;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.internal.util.Preconditions;
import com.android.systemui.CameraAvailabilityListener;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.decor.DecorProvider;
import com.android.systemui.decor.DecorProviderFactory;
import com.android.systemui.decor.DecorProviderKt;
import com.android.systemui.decor.OverlayWindow;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.systemui.decor.RoundedCornerDecorProviderFactory;
import com.android.systemui.decor.RoundedCornerResDelegate;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.settings.SecureSettings;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Pair;

public class ScreenDecorations extends CoreStartable implements TunerService.Tunable {
    public static final boolean DEBUG_COLOR;
    public static final boolean DEBUG_DISABLE_SCREEN_DECORATIONS = SystemProperties.getBoolean("debug.disable_screen_decorations", false);
    public static final boolean DEBUG_SCREENSHOT_ROUNDED_CORNERS;
    public static int mDisableRoundedCorner = SystemProperties.getInt("vendor.display.disable_rounded_corner", 0);
    public final BroadcastDispatcher mBroadcastDispatcher;
    public CameraAvailabilityListener mCameraListener;
    public CameraAvailabilityListener.CameraTransitionCallback mCameraTransitionCallback = new CameraAvailabilityListener.CameraTransitionCallback() {
        public void onApplyCameraProtection(Path path, Rect rect) {
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            ScreenDecorHwcLayer screenDecorHwcLayer = screenDecorations.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.setProtection(path, rect);
                ScreenDecorations.this.mScreenDecorHwcLayer.enableShowProtection(true);
                return;
            }
            DisplayCutoutView[] displayCutoutViewArr = screenDecorations.mCutoutViews;
            if (displayCutoutViewArr == null) {
                Log.w("ScreenDecorations", "DisplayCutoutView do not initialized");
                return;
            }
            for (DisplayCutoutView displayCutoutView : displayCutoutViewArr) {
                if (displayCutoutView != null) {
                    displayCutoutView.setProtection(path, rect);
                    displayCutoutView.enableShowProtection(true);
                }
            }
        }

        public void onHideCameraProtection() {
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            ScreenDecorHwcLayer screenDecorHwcLayer = screenDecorations.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.enableShowProtection(false);
                return;
            }
            DisplayCutoutView[] displayCutoutViewArr = screenDecorations.mCutoutViews;
            if (displayCutoutViewArr == null) {
                Log.w("ScreenDecorations", "DisplayCutoutView do not initialized");
                return;
            }
            for (DisplayCutoutView displayCutoutView : displayCutoutViewArr) {
                if (displayCutoutView != null) {
                    displayCutoutView.enableShowProtection(false);
                }
            }
        }
    };
    public SettingObserver mColorInversionSetting;
    public DisplayCutoutView[] mCutoutViews;
    public DisplayInfo mDisplayInfo = new DisplayInfo();
    public DisplayManager.DisplayListener mDisplayListener;
    public DisplayManager mDisplayManager;
    public Display.Mode mDisplayMode;
    public String mDisplayUniqueId;
    public final DecorProviderFactory mDotFactory;
    public final PrivacyDotViewController mDotViewController;
    public DelayableExecutor mExecutor;
    public Handler mHandler;
    public DisplayDecorationSupport mHwcScreenDecorationSupport;
    public boolean mIsRegistered;
    public final Executor mMainExecutor;
    public OverlayWindow[] mOverlays = null;
    public boolean mPendingConfigChange;
    public PrivacyDotViewController.ShowingListener mPrivacyDotShowingListener = new PrivacyDotViewController.ShowingListener() {
        public void onPrivacyDotShown(View view) {
            ScreenDecorations.this.setOverlayWindowVisibilityIfViewExist(view, 0);
        }

        public void onPrivacyDotHidden(View view) {
            ScreenDecorations.this.setOverlayWindowVisibilityIfViewExist(view, 4);
        }
    };
    public int mProviderRefreshToken = 0;
    public int mRotation;
    public DecorProviderFactory mRoundedCornerFactory;
    public RoundedCornerResDelegate mRoundedCornerResDelegate;
    public ScreenDecorHwcLayer mScreenDecorHwcLayer;
    public ViewGroup mScreenDecorHwcWindow;
    public final SecureSettings mSecureSettings;
    public final ThreadFactory mThreadFactory;
    public int mTintColor = -16777216;
    public final TunerService mTunerService;
    public final BroadcastReceiver mUserSwitchIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ScreenDecorations.this.mColorInversionSetting.setUserId(ActivityManager.getCurrentUser());
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            screenDecorations.updateColorInversion(screenDecorations.mColorInversionSetting.getValue());
        }
    };
    public final UserTracker mUserTracker;
    public WindowManager mWindowManager;

    public static int getBoundPositionFromRotation(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    static {
        boolean z = SystemProperties.getBoolean("debug.screenshot_rounded_corners", false);
        DEBUG_SCREENSHOT_ROUNDED_CORNERS = z;
        DEBUG_COLOR = z;
    }

    public void setOverlayWindowVisibilityIfViewExist(View view, int i) {
        if (view != null) {
            this.mExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda1(this, view, i));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOverlayWindowVisibilityIfViewExist$0(View view, int i) {
        if (this.mOverlays != null && isOnlyPrivacyDotInSwLayer()) {
            OverlayWindow[] overlayWindowArr = this.mOverlays;
            int length = overlayWindowArr.length;
            int i2 = 0;
            while (i2 < length) {
                OverlayWindow overlayWindow = overlayWindowArr[i2];
                if (overlayWindow == null || overlayWindow.getView(view.getId()) == null) {
                    i2++;
                } else {
                    overlayWindow.getRootView().setVisibility(i);
                    return;
                }
            }
        }
    }

    public static boolean eq(DisplayDecorationSupport displayDecorationSupport, DisplayDecorationSupport displayDecorationSupport2) {
        if (displayDecorationSupport == null) {
            return displayDecorationSupport2 == null;
        }
        if (displayDecorationSupport2 == null) {
            return false;
        }
        return displayDecorationSupport.format == displayDecorationSupport2.format && displayDecorationSupport.alphaInterpretation == displayDecorationSupport2.alphaInterpretation;
    }

    public ScreenDecorations(Context context, Executor executor, SecureSettings secureSettings, BroadcastDispatcher broadcastDispatcher, TunerService tunerService, UserTracker userTracker, PrivacyDotViewController privacyDotViewController, ThreadFactory threadFactory, PrivacyDotDecorProviderFactory privacyDotDecorProviderFactory) {
        super(context);
        this.mMainExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTunerService = tunerService;
        this.mUserTracker = userTracker;
        this.mDotViewController = privacyDotViewController;
        this.mThreadFactory = threadFactory;
        this.mDotFactory = privacyDotDecorProviderFactory;
    }

    public void start() {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
            return;
        }
        Handler buildHandlerOnNewThread = this.mThreadFactory.buildHandlerOnNewThread("ScreenDecorations");
        this.mHandler = buildHandlerOnNewThread;
        DelayableExecutor buildDelayableExecutorOnHandler = this.mThreadFactory.buildDelayableExecutorOnHandler(buildHandlerOnNewThread);
        this.mExecutor = buildDelayableExecutorOnHandler;
        buildDelayableExecutorOnHandler.execute(new ScreenDecorations$$ExternalSyntheticLambda7(this));
        this.mDotViewController.setUiExecutor(this.mExecutor);
    }

    public final boolean isPrivacyDotEnabled() {
        return this.mDotFactory.getHasProviders();
    }

    public final List<DecorProvider> getProviders(boolean z) {
        ArrayList arrayList = new ArrayList(this.mDotFactory.getProviders());
        if (!z) {
            arrayList.addAll(this.mRoundedCornerFactory.getProviders());
        }
        return arrayList;
    }

    public boolean hasSameProviders(List<DecorProvider> list) {
        ArrayList arrayList = new ArrayList();
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr != null) {
            for (OverlayWindow overlayWindow : overlayWindowArr) {
                if (overlayWindow != null) {
                    arrayList.addAll(overlayWindow.getViewIds());
                }
            }
        }
        if (arrayList.size() != list.size()) {
            return false;
        }
        for (DecorProvider viewId : list) {
            if (!arrayList.contains(Integer.valueOf(viewId.getViewId()))) {
                return false;
            }
        }
        return true;
    }

    public final void startOnScreenDecorationsThread() {
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        this.mContext.getDisplay().getDisplayInfo(this.mDisplayInfo);
        DisplayInfo displayInfo = this.mDisplayInfo;
        this.mRotation = displayInfo.rotation;
        this.mDisplayMode = displayInfo.getMode();
        this.mDisplayUniqueId = this.mDisplayInfo.uniqueId;
        RoundedCornerResDelegate roundedCornerResDelegate = new RoundedCornerResDelegate(this.mContext.getResources(), this.mDisplayUniqueId);
        this.mRoundedCornerResDelegate = roundedCornerResDelegate;
        roundedCornerResDelegate.setPhysicalPixelDisplaySizeRatio(getPhysicalPixelDisplaySizeRatio());
        this.mRoundedCornerFactory = new RoundedCornerDecorProviderFactory(this.mRoundedCornerResDelegate);
        this.mHwcScreenDecorationSupport = this.mContext.getDisplay().getDisplayDecorationSupport();
        updateHwLayerRoundedCornerDrawable();
        setupDecorations();
        setupCameraListener();
        AnonymousClass3 r0 = new DisplayManager.DisplayListener() {
            public void onDisplayAdded(int i) {
            }

            public void onDisplayRemoved(int i) {
            }

            public void onDisplayChanged(int i) {
                ScreenDecorations.this.mContext.getDisplay().getDisplayInfo(ScreenDecorations.this.mDisplayInfo);
                DisplayInfo displayInfo = ScreenDecorations.this.mDisplayInfo;
                int i2 = displayInfo.rotation;
                Display.Mode mode = displayInfo.getMode();
                ScreenDecorations screenDecorations = ScreenDecorations.this;
                boolean z = true;
                if (!(screenDecorations.mOverlays == null && screenDecorations.mScreenDecorHwcWindow == null) && (screenDecorations.mRotation != i2 || ScreenDecorations.displayModeChanged(ScreenDecorations.this.mDisplayMode, mode))) {
                    ScreenDecorations screenDecorations2 = ScreenDecorations.this;
                    screenDecorations2.mPendingConfigChange = true;
                    if (screenDecorations2.mOverlays != null) {
                        for (int i3 = 0; i3 < 4; i3++) {
                            OverlayWindow overlayWindow = ScreenDecorations.this.mOverlays[i3];
                            if (overlayWindow != null) {
                                ViewGroup rootView = overlayWindow.getRootView();
                                rootView.getViewTreeObserver().addOnPreDrawListener(new RestartingPreDrawListener(rootView, i3, i2, mode));
                            }
                        }
                    }
                    ViewGroup viewGroup = ScreenDecorations.this.mScreenDecorHwcWindow;
                    if (viewGroup != null) {
                        ViewTreeObserver viewTreeObserver = viewGroup.getViewTreeObserver();
                        ScreenDecorations screenDecorations3 = ScreenDecorations.this;
                        viewTreeObserver.addOnPreDrawListener(new RestartingPreDrawListener(screenDecorations3.mScreenDecorHwcWindow, -1, i2, mode));
                    }
                    ScreenDecorHwcLayer screenDecorHwcLayer = ScreenDecorations.this.mScreenDecorHwcLayer;
                    if (screenDecorHwcLayer != null) {
                        screenDecorHwcLayer.pendingConfigChange = true;
                    }
                }
                ScreenDecorations screenDecorations4 = ScreenDecorations.this;
                String str = screenDecorations4.mDisplayInfo.uniqueId;
                if (!Objects.equals(str, screenDecorations4.mDisplayUniqueId)) {
                    ScreenDecorations screenDecorations5 = ScreenDecorations.this;
                    screenDecorations5.mDisplayUniqueId = str;
                    DisplayDecorationSupport displayDecorationSupport = screenDecorations5.mContext.getDisplay().getDisplayDecorationSupport();
                    ScreenDecorations.this.mRoundedCornerResDelegate.updateDisplayUniqueId(str, (Integer) null);
                    ScreenDecorations screenDecorations6 = ScreenDecorations.this;
                    if (displayDecorationSupport == null) {
                        z = false;
                    }
                    if (!screenDecorations6.hasSameProviders(screenDecorations6.getProviders(z)) || !ScreenDecorations.eq(displayDecorationSupport, ScreenDecorations.this.mHwcScreenDecorationSupport)) {
                        ScreenDecorations screenDecorations7 = ScreenDecorations.this;
                        screenDecorations7.mHwcScreenDecorationSupport = displayDecorationSupport;
                        screenDecorations7.removeAllOverlays();
                        ScreenDecorations.this.setupDecorations();
                        return;
                    }
                    ScreenDecorations screenDecorations8 = ScreenDecorations.this;
                    if (screenDecorations8.mScreenDecorHwcLayer != null) {
                        screenDecorations8.updateHwLayerRoundedCornerDrawable();
                        ScreenDecorations.this.updateHwLayerRoundedCornerExistAndSize();
                    }
                    ScreenDecorations.this.updateOverlayProviderViews();
                }
                float physicalPixelDisplaySizeRatio = ScreenDecorations.this.getPhysicalPixelDisplaySizeRatio();
                if (ScreenDecorations.this.mRoundedCornerResDelegate.getPhysicalPixelDisplaySizeRatio() != physicalPixelDisplaySizeRatio) {
                    ScreenDecorations.this.mRoundedCornerResDelegate.setPhysicalPixelDisplaySizeRatio(physicalPixelDisplaySizeRatio);
                    ScreenDecorations screenDecorations9 = ScreenDecorations.this;
                    if (screenDecorations9.mScreenDecorHwcLayer != null) {
                        screenDecorations9.updateHwLayerRoundedCornerExistAndSize();
                    }
                    ScreenDecorations.this.updateOverlayProviderViews();
                }
                DisplayCutoutView[] displayCutoutViewArr = ScreenDecorations.this.mCutoutViews;
                if (displayCutoutViewArr != null) {
                    int length = displayCutoutViewArr.length;
                    for (int i4 = 0; i4 < length; i4++) {
                        DisplayCutoutView displayCutoutView = ScreenDecorations.this.mCutoutViews[i4];
                        if (displayCutoutView != null) {
                            displayCutoutView.onDisplayChanged(i);
                        }
                    }
                }
                ScreenDecorHwcLayer screenDecorHwcLayer2 = ScreenDecorations.this.mScreenDecorHwcLayer;
                if (screenDecorHwcLayer2 != null) {
                    screenDecorHwcLayer2.onDisplayChanged(i);
                }
            }
        };
        this.mDisplayListener = r0;
        this.mDisplayManager.registerDisplayListener(r0, this.mHandler);
        updateConfiguration();
    }

    public final View getOverlayView(int i) {
        View view;
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr == null) {
            return null;
        }
        for (OverlayWindow overlayWindow : overlayWindowArr) {
            if (overlayWindow != null && (view = overlayWindow.getView(i)) != null) {
                return view;
            }
        }
        return null;
    }

    public final void removeRedundantOverlayViews(List<DecorProvider> list) {
        if (this.mOverlays != null) {
            int[] array = list.stream().mapToInt(new ScreenDecorations$$ExternalSyntheticLambda5()).toArray();
            for (OverlayWindow overlayWindow : this.mOverlays) {
                if (overlayWindow != null) {
                    overlayWindow.removeRedundantViews(array);
                }
            }
        }
    }

    public final void removeOverlayView(int i) {
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr != null) {
            for (OverlayWindow overlayWindow : overlayWindowArr) {
                if (overlayWindow != null) {
                    overlayWindow.removeView(i);
                }
            }
        }
    }

    public final void setupDecorations() {
        View overlayView;
        View overlayView2;
        View overlayView3;
        if (hasRoundedCorners() || shouldDrawCutout() || isPrivacyDotEnabled()) {
            List<DecorProvider> providers = getProviders(this.mHwcScreenDecorationSupport != null);
            removeRedundantOverlayViews(providers);
            if (this.mHwcScreenDecorationSupport != null) {
                createHwcOverlay();
            } else {
                removeHwcOverlay();
            }
            DisplayCutout cutout = getCutout();
            boolean isOnlyPrivacyDotInSwLayer = isOnlyPrivacyDotInSwLayer();
            for (int i = 0; i < 4; i++) {
                if (shouldShowSwLayerCutout(i, cutout) || shouldShowSwLayerRoundedCorner(i, cutout) || shouldShowSwLayerPrivacyDot(i, cutout)) {
                    Pair<List<DecorProvider>, List<DecorProvider>> partitionAlignedBound = DecorProviderKt.partitionAlignedBound(providers, i);
                    createOverlay(i, partitionAlignedBound.getFirst(), isOnlyPrivacyDotInSwLayer);
                    providers = partitionAlignedBound.getSecond();
                } else {
                    removeOverlay(i);
                }
            }
            if (isOnlyPrivacyDotInSwLayer) {
                this.mDotViewController.setShowingListener(this.mPrivacyDotShowingListener);
            } else {
                this.mDotViewController.setShowingListener((PrivacyDotViewController.ShowingListener) null);
            }
            View overlayView4 = getOverlayView(R$id.privacy_dot_top_left_container);
            if (!(overlayView4 == null || (overlayView = getOverlayView(R$id.privacy_dot_top_right_container)) == null || (overlayView2 = getOverlayView(R$id.privacy_dot_bottom_left_container)) == null || (overlayView3 = getOverlayView(R$id.privacy_dot_bottom_right_container)) == null)) {
                this.mDotViewController.initialize(overlayView4, overlayView, overlayView2, overlayView3);
            }
        } else {
            removeAllOverlays();
            removeHwcOverlay();
        }
        if (!hasOverlays() && !hasHwcOverlay()) {
            this.mMainExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda4(this));
            SettingObserver settingObserver = this.mColorInversionSetting;
            if (settingObserver != null) {
                settingObserver.setListening(false);
            }
            this.mBroadcastDispatcher.unregisterReceiver(this.mUserSwitchIntentReceiver);
            this.mIsRegistered = false;
        } else if (!this.mIsRegistered) {
            this.mMainExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda3(this));
            if (this.mColorInversionSetting == null) {
                this.mColorInversionSetting = new SettingObserver(this.mSecureSettings, this.mHandler, "accessibility_display_inversion_enabled", this.mUserTracker.getUserId()) {
                    public void handleValueChanged(int i, boolean z) {
                        ScreenDecorations.this.updateColorInversion(i);
                    }
                };
            }
            this.mColorInversionSetting.setListening(true);
            this.mColorInversionSetting.onChange(false);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            this.mBroadcastDispatcher.registerReceiver(this.mUserSwitchIntentReceiver, intentFilter, this.mExecutor, UserHandle.ALL);
            this.mIsRegistered = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupDecorations$1() {
        this.mTunerService.addTunable(this, "sysui_rounded_size");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupDecorations$2() {
        this.mTunerService.removeTunable(this);
    }

    public DisplayCutout getCutout() {
        return this.mContext.getDisplay().getCutout();
    }

    public boolean hasOverlays() {
        if (this.mOverlays == null) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (this.mOverlays[i] != null) {
                return true;
            }
        }
        this.mOverlays = null;
        return false;
    }

    public final void removeAllOverlays() {
        if (this.mOverlays != null) {
            for (int i = 0; i < 4; i++) {
                if (this.mOverlays[i] != null) {
                    removeOverlay(i);
                }
            }
            this.mOverlays = null;
        }
    }

    public final void removeOverlay(int i) {
        OverlayWindow overlayWindow;
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr != null && (overlayWindow = overlayWindowArr[i]) != null) {
            this.mWindowManager.removeViewImmediate(overlayWindow.getRootView());
            this.mOverlays[i] = null;
        }
    }

    public final int getWindowVisibility(OverlayWindow overlayWindow, boolean z) {
        if (!z) {
            return 0;
        }
        int[] iArr = {R$id.privacy_dot_top_left_container, R$id.privacy_dot_top_right_container, R$id.privacy_dot_bottom_left_container, R$id.privacy_dot_bottom_right_container};
        for (int i = 0; i < 4; i++) {
            View view = overlayWindow.getView(iArr[i]);
            if (view != null && view.getVisibility() == 0) {
                return 0;
            }
        }
        return 4;
    }

    public final void createOverlay(int i, List<DecorProvider> list, boolean z) {
        if (this.mOverlays == null) {
            this.mOverlays = new OverlayWindow[4];
        }
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        OverlayWindow overlayWindow = overlayWindowArr[i];
        if (overlayWindow != null) {
            initOverlay(overlayWindow, list, z);
            return;
        }
        overlayWindowArr[i] = new OverlayWindow(this.mContext);
        initOverlay(this.mOverlays[i], list, z);
        final ViewGroup rootView = this.mOverlays[i].getRootView();
        rootView.setSystemUiVisibility(256);
        rootView.setAlpha(0.0f);
        rootView.setForceDarkAllowed(false);
        if (this.mHwcScreenDecorationSupport == null) {
            if (this.mCutoutViews == null) {
                this.mCutoutViews = new DisplayCutoutView[4];
            }
            this.mCutoutViews[i] = new DisplayCutoutView(this.mContext, i);
            this.mCutoutViews[i].setColor(this.mTintColor);
            rootView.addView(this.mCutoutViews[i]);
            this.mCutoutViews[i].updateRotation(this.mRotation);
        }
        this.mWindowManager.addView(rootView, getWindowLayoutParams(i));
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                rootView.removeOnLayoutChangeListener(this);
                rootView.animate().alpha(1.0f).setDuration(1000).start();
            }
        });
        rootView.getRootView().getViewTreeObserver().addOnPreDrawListener(new ValidatingPreDrawListener(rootView.getRootView()));
    }

    public final boolean hasHwcOverlay() {
        return this.mScreenDecorHwcWindow != null;
    }

    public final void removeHwcOverlay() {
        ViewGroup viewGroup = this.mScreenDecorHwcWindow;
        if (viewGroup != null) {
            this.mWindowManager.removeViewImmediate(viewGroup);
            this.mScreenDecorHwcWindow = null;
            this.mScreenDecorHwcLayer = null;
        }
    }

    public final void createHwcOverlay() {
        if (this.mScreenDecorHwcWindow == null) {
            this.mScreenDecorHwcWindow = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R$layout.screen_decor_hwc_layer, (ViewGroup) null);
            ScreenDecorHwcLayer screenDecorHwcLayer = new ScreenDecorHwcLayer(this.mContext, this.mHwcScreenDecorationSupport);
            this.mScreenDecorHwcLayer = screenDecorHwcLayer;
            this.mScreenDecorHwcWindow.addView(screenDecorHwcLayer, new FrameLayout.LayoutParams(-1, -1, 8388659));
            this.mWindowManager.addView(this.mScreenDecorHwcWindow, getHwcWindowLayoutParams());
            updateHwLayerRoundedCornerExistAndSize();
            updateHwLayerRoundedCornerDrawable();
            this.mScreenDecorHwcWindow.getViewTreeObserver().addOnPreDrawListener(new ValidatingPreDrawListener(this.mScreenDecorHwcWindow));
        }
    }

    public final void initOverlay(OverlayWindow overlayWindow, List<DecorProvider> list, boolean z) {
        if (!overlayWindow.hasSameProviders(list)) {
            list.forEach(new ScreenDecorations$$ExternalSyntheticLambda6(this, overlayWindow));
        }
        overlayWindow.getRootView().setVisibility(getWindowVisibility(overlayWindow, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initOverlay$3(OverlayWindow overlayWindow, DecorProvider decorProvider) {
        if (overlayWindow.getView(decorProvider.getViewId()) == null) {
            removeOverlayView(decorProvider.getViewId());
            overlayWindow.addDecorProvider(decorProvider, this.mRotation);
        }
    }

    public WindowManager.LayoutParams getWindowLayoutParams(int i) {
        WindowManager.LayoutParams windowLayoutBaseParams = getWindowLayoutBaseParams();
        windowLayoutBaseParams.width = getWidthLayoutParamByPos(i);
        windowLayoutBaseParams.height = getHeightLayoutParamByPos(i);
        windowLayoutBaseParams.setTitle(getWindowTitleByPos(i));
        windowLayoutBaseParams.gravity = getOverlayWindowGravity(i);
        return windowLayoutBaseParams;
    }

    public final WindowManager.LayoutParams getHwcWindowLayoutParams() {
        WindowManager.LayoutParams windowLayoutBaseParams = getWindowLayoutBaseParams();
        windowLayoutBaseParams.width = -1;
        windowLayoutBaseParams.height = -1;
        windowLayoutBaseParams.setTitle("ScreenDecorHwcOverlay");
        windowLayoutBaseParams.gravity = 8388659;
        if (!DEBUG_COLOR) {
            windowLayoutBaseParams.setColorMode(4);
        }
        return windowLayoutBaseParams;
    }

    public final WindowManager.LayoutParams getWindowLayoutBaseParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2024, 545259816, -3);
        int i = layoutParams.privateFlags | 80 | 536870912;
        layoutParams.privateFlags = i;
        if (!DEBUG_SCREENSHOT_ROUNDED_CORNERS) {
            layoutParams.privateFlags = i | 1048576;
        }
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.privateFlags |= 16777216;
        return layoutParams;
    }

    public final int getWidthLayoutParamByPos(int i) {
        int boundPositionFromRotation = getBoundPositionFromRotation(i, this.mRotation);
        return (boundPositionFromRotation == 1 || boundPositionFromRotation == 3) ? -1 : -2;
    }

    public final int getHeightLayoutParamByPos(int i) {
        int boundPositionFromRotation = getBoundPositionFromRotation(i, this.mRotation);
        return (boundPositionFromRotation == 1 || boundPositionFromRotation == 3) ? -2 : -1;
    }

    public static String getWindowTitleByPos(int i) {
        if (i == 0) {
            return "ScreenDecorOverlayLeft";
        }
        if (i == 1) {
            return "ScreenDecorOverlay";
        }
        if (i == 2) {
            return "ScreenDecorOverlayRight";
        }
        if (i == 3) {
            return "ScreenDecorOverlayBottom";
        }
        throw new IllegalArgumentException("unknown bound position: " + i);
    }

    public static boolean displayModeChanged(Display.Mode mode, Display.Mode mode2) {
        if (mode != null && mode.getPhysicalWidth() == mode2.getPhysicalWidth() && mode.getPhysicalHeight() == mode2.getPhysicalHeight()) {
            return false;
        }
        return true;
    }

    public final int getOverlayWindowGravity(int i) {
        int boundPositionFromRotation = getBoundPositionFromRotation(i, this.mRotation);
        if (boundPositionFromRotation == 0) {
            return 3;
        }
        if (boundPositionFromRotation == 1) {
            return 48;
        }
        if (boundPositionFromRotation == 2) {
            return 5;
        }
        if (boundPositionFromRotation == 3) {
            return 80;
        }
        throw new IllegalArgumentException("unknown bound position: " + i);
    }

    public final void setupCameraListener() {
        if (this.mContext.getResources().getBoolean(R$bool.config_enableDisplayCutoutProtection)) {
            CameraAvailabilityListener build = CameraAvailabilityListener.Factory.build(this.mContext, this.mExecutor);
            this.mCameraListener = build;
            build.addTransitionCallback(this.mCameraTransitionCallback);
            this.mCameraListener.startListening();
        }
    }

    public final void updateColorInversion(int i) {
        this.mTintColor = i != 0 ? -1 : -16777216;
        if (DEBUG_COLOR) {
            this.mTintColor = -65536;
        }
        if (this.mOverlays != null && this.mHwcScreenDecorationSupport == null) {
            this.mRoundedCornerResDelegate.setColorTintList(ColorStateList.valueOf(this.mTintColor));
            Integer[] numArr = {Integer.valueOf(R$id.rounded_corner_top_left), Integer.valueOf(R$id.rounded_corner_top_right), Integer.valueOf(R$id.rounded_corner_bottom_left), Integer.valueOf(R$id.rounded_corner_bottom_right)};
            for (int i2 = 0; i2 < 4; i2++) {
                OverlayWindow overlayWindow = this.mOverlays[i2];
                if (overlayWindow != null) {
                    ViewGroup rootView = overlayWindow.getRootView();
                    int childCount = rootView.getChildCount();
                    for (int i3 = 0; i3 < childCount; i3++) {
                        View childAt = rootView.getChildAt(i3);
                        if (childAt instanceof DisplayCutoutView) {
                            ((DisplayCutoutView) childAt).setColor(this.mTintColor);
                        }
                    }
                    this.mOverlays[i2].onReloadResAndMeasure(numArr, this.mProviderRefreshToken, this.mRotation, this.mDisplayUniqueId);
                }
            }
        }
    }

    public float getPhysicalPixelDisplaySizeRatio() {
        this.mContext.getDisplay().getDisplayInfo(this.mDisplayInfo);
        Display.Mode maximumResolutionDisplayMode = DisplayUtils.getMaximumResolutionDisplayMode(this.mDisplayInfo.supportedModes);
        if (maximumResolutionDisplayMode == null) {
            return 1.0f;
        }
        return DisplayUtils.getPhysicalPixelDisplaySizeRatio(maximumResolutionDisplayMode.getPhysicalWidth(), maximumResolutionDisplayMode.getPhysicalHeight(), this.mDisplayInfo.getNaturalWidth(), this.mDisplayInfo.getNaturalHeight());
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
        } else {
            this.mExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onConfigurationChanged$4() {
        this.mPendingConfigChange = false;
        updateConfiguration();
        setupDecorations();
        if (this.mOverlays != null) {
            updateLayoutParams();
        }
    }

    public static String alphaInterpretationToString(int i) {
        if (i == 0) {
            return "COVERAGE";
        }
        if (i == 1) {
            return "MASK";
        }
        return "Unknown: " + i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ScreenDecorations state:");
        printWriter.println("  DEBUG_DISABLE_SCREEN_DECORATIONS:" + DEBUG_DISABLE_SCREEN_DECORATIONS);
        printWriter.println("  mIsPrivacyDotEnabled:" + isPrivacyDotEnabled());
        printWriter.println("  isOnlyPrivacyDotInSwLayer:" + isOnlyPrivacyDotInSwLayer());
        printWriter.println("  mPendingConfigChange:" + this.mPendingConfigChange);
        if (this.mHwcScreenDecorationSupport != null) {
            printWriter.println("  mHwcScreenDecorationSupport:");
            printWriter.println("    format=" + PixelFormat.formatToString(this.mHwcScreenDecorationSupport.format));
            printWriter.println("    alphaInterpretation=" + alphaInterpretationToString(this.mHwcScreenDecorationSupport.alphaInterpretation));
        } else {
            printWriter.println("  mHwcScreenDecorationSupport: null");
        }
        if (this.mScreenDecorHwcLayer != null) {
            printWriter.println("  mScreenDecorHwcLayer:");
            printWriter.println("    transparentRegion=" + this.mScreenDecorHwcLayer.transparentRect);
        } else {
            printWriter.println("  mScreenDecorHwcLayer: null");
        }
        if (this.mOverlays != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("  mOverlays(left,top,right,bottom)=(");
            boolean z = true;
            sb.append(this.mOverlays[0] != null);
            sb.append(",");
            sb.append(this.mOverlays[1] != null);
            sb.append(",");
            sb.append(this.mOverlays[2] != null);
            sb.append(",");
            if (this.mOverlays[3] == null) {
                z = false;
            }
            sb.append(z);
            sb.append(")");
            printWriter.println(sb.toString());
            for (int i = 0; i < 4; i++) {
                OverlayWindow overlayWindow = this.mOverlays[i];
                if (overlayWindow != null) {
                    overlayWindow.dump(printWriter, getWindowTitleByPos(i));
                }
            }
        }
        this.mRoundedCornerResDelegate.dump(printWriter, strArr);
    }

    public final void updateConfiguration() {
        Preconditions.checkState(this.mHandler.getLooper().getThread() == Thread.currentThread(), "must call on " + this.mHandler.getLooper().getThread() + ", but was " + Thread.currentThread());
        this.mContext.getDisplay().getDisplayInfo(this.mDisplayInfo);
        int i = this.mDisplayInfo.rotation;
        if (this.mRotation != i) {
            this.mDotViewController.setNewRotation(i);
        }
        Display.Mode mode = this.mDisplayInfo.getMode();
        if (this.mPendingConfigChange) {
            return;
        }
        if (i != this.mRotation || displayModeChanged(this.mDisplayMode, mode)) {
            this.mRotation = i;
            this.mDisplayMode = mode;
            ScreenDecorHwcLayer screenDecorHwcLayer = this.mScreenDecorHwcLayer;
            if (screenDecorHwcLayer != null) {
                screenDecorHwcLayer.pendingConfigChange = false;
                screenDecorHwcLayer.updateRotation(i);
                updateHwLayerRoundedCornerExistAndSize();
                updateHwLayerRoundedCornerDrawable();
            }
            updateLayoutParams();
            DisplayCutoutView[] displayCutoutViewArr = this.mCutoutViews;
            if (displayCutoutViewArr != null) {
                for (DisplayCutoutView displayCutoutView : displayCutoutViewArr) {
                    if (displayCutoutView != null) {
                        displayCutoutView.updateRotation(this.mRotation);
                    }
                }
            }
            updateOverlayProviderViews();
        }
    }

    public final boolean hasRoundedCorners() {
        return this.mRoundedCornerFactory.getHasProviders();
    }

    public final boolean isDefaultShownOverlayPos(int i, DisplayCutout displayCutout) {
        boolean z = displayCutout == null || displayCutout.isBoundsEmpty();
        int boundPositionFromRotation = getBoundPositionFromRotation(1, this.mRotation);
        int boundPositionFromRotation2 = getBoundPositionFromRotation(3, this.mRotation);
        if (z || !displayCutout.getBoundingRectsAll()[boundPositionFromRotation].isEmpty() || !displayCutout.getBoundingRectsAll()[boundPositionFromRotation2].isEmpty()) {
            if (i == 1 || i == 3) {
                return true;
            }
            return false;
        } else if (i == 0 || i == 2) {
            return true;
        } else {
            return false;
        }
    }

    public final boolean shouldShowSwLayerRoundedCorner(int i, DisplayCutout displayCutout) {
        return hasRoundedCorners() && isDefaultShownOverlayPos(i, displayCutout) && this.mHwcScreenDecorationSupport == null;
    }

    public final boolean shouldShowSwLayerPrivacyDot(int i, DisplayCutout displayCutout) {
        return isPrivacyDotEnabled() && isDefaultShownOverlayPos(i, displayCutout);
    }

    public final boolean shouldShowSwLayerCutout(int i, DisplayCutout displayCutout) {
        Rect[] boundingRectsAll = displayCutout == null ? null : displayCutout.getBoundingRectsAll();
        return boundingRectsAll != null && !boundingRectsAll[getBoundPositionFromRotation(i, this.mRotation)].isEmpty() && this.mHwcScreenDecorationSupport == null;
    }

    public final boolean isOnlyPrivacyDotInSwLayer() {
        return isPrivacyDotEnabled() && (this.mHwcScreenDecorationSupport != null || (!hasRoundedCorners() && !shouldDrawCutout()));
    }

    public final boolean shouldDrawCutout() {
        return shouldDrawCutout(this.mContext);
    }

    public static boolean shouldDrawCutout(Context context) {
        if (mDisableRoundedCorner == 1) {
            return false;
        }
        return DisplayCutout.getFillBuiltInDisplayCutout(context.getResources(), context.getDisplay().getUniqueId());
    }

    public final void updateOverlayProviderViews() {
        OverlayWindow[] overlayWindowArr = this.mOverlays;
        if (overlayWindowArr != null) {
            this.mProviderRefreshToken++;
            for (OverlayWindow overlayWindow : overlayWindowArr) {
                if (overlayWindow != null) {
                    overlayWindow.onReloadResAndMeasure((Integer[]) null, this.mProviderRefreshToken, this.mRotation, this.mDisplayUniqueId);
                }
            }
        }
    }

    public final void updateLayoutParams() {
        Trace.beginSection("ScreenDecorations#updateLayoutParams");
        ViewGroup viewGroup = this.mScreenDecorHwcWindow;
        if (viewGroup != null) {
            this.mWindowManager.updateViewLayout(viewGroup, getHwcWindowLayoutParams());
        }
        if (this.mOverlays != null) {
            for (int i = 0; i < 4; i++) {
                OverlayWindow overlayWindow = this.mOverlays[i];
                if (overlayWindow != null) {
                    this.mWindowManager.updateViewLayout(overlayWindow.getRootView(), getWindowLayoutParams(i));
                }
            }
        }
        Trace.endSection();
    }

    public void onTuningChanged(String str, String str2) {
        if (DEBUG_DISABLE_SCREEN_DECORATIONS) {
            Log.i("ScreenDecorations", "ScreenDecorations is disabled");
        } else {
            this.mExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda2(this, str, str2));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTuningChanged$5(String str, String str2) {
        if (this.mOverlays != null && "sysui_rounded_size".equals(str)) {
            try {
                this.mRoundedCornerResDelegate.setTuningSizeFactor(Integer.valueOf(Integer.parseInt(str2)));
            } catch (NumberFormatException unused) {
                this.mRoundedCornerResDelegate.setTuningSizeFactor((Integer) null);
            }
            Integer[] numArr = {Integer.valueOf(R$id.rounded_corner_top_left), Integer.valueOf(R$id.rounded_corner_top_right), Integer.valueOf(R$id.rounded_corner_bottom_left), Integer.valueOf(R$id.rounded_corner_bottom_right)};
            for (OverlayWindow overlayWindow : this.mOverlays) {
                if (overlayWindow != null) {
                    overlayWindow.onReloadResAndMeasure(numArr, this.mProviderRefreshToken, this.mRotation, this.mDisplayUniqueId);
                }
            }
            updateHwLayerRoundedCornerExistAndSize();
        }
    }

    public final void updateHwLayerRoundedCornerDrawable() {
        if (this.mScreenDecorHwcLayer != null) {
            Drawable topRoundedDrawable = this.mRoundedCornerResDelegate.getTopRoundedDrawable();
            Drawable bottomRoundedDrawable = this.mRoundedCornerResDelegate.getBottomRoundedDrawable();
            if (topRoundedDrawable != null && bottomRoundedDrawable != null) {
                this.mScreenDecorHwcLayer.updateRoundedCornerDrawable(topRoundedDrawable, bottomRoundedDrawable);
            }
        }
    }

    public final void updateHwLayerRoundedCornerExistAndSize() {
        ScreenDecorHwcLayer screenDecorHwcLayer = this.mScreenDecorHwcLayer;
        if (screenDecorHwcLayer != null) {
            screenDecorHwcLayer.updateRoundedCornerExistenceAndSize(this.mRoundedCornerResDelegate.getHasTop(), this.mRoundedCornerResDelegate.getHasBottom(), this.mRoundedCornerResDelegate.getTopRoundedSize().getWidth(), this.mRoundedCornerResDelegate.getBottomRoundedSize().getWidth());
        }
    }

    public void setSize(View view, Size size) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = size.getWidth();
        layoutParams.height = size.getHeight();
        view.setLayoutParams(layoutParams);
    }

    public static class DisplayCutoutView extends DisplayCutoutBaseView {
        public final Rect mBoundingRect = new Rect();
        public final List<Rect> mBounds = new ArrayList();
        public int mColor = -16777216;
        public int mInitialPosition;
        public int mPosition;
        public int mRotation;
        public Rect mTotalBounds = new Rect();

        public DisplayCutoutView(Context context, int i) {
            super(context);
            this.mInitialPosition = i;
            this.paint.setColor(-16777216);
            this.paint.setStyle(Paint.Style.FILL);
            setId(R$id.display_cutout);
        }

        public void setColor(int i) {
            this.mColor = i;
            this.paint.setColor(i);
            invalidate();
        }

        public void updateRotation(int i) {
            this.mRotation = i;
            super.updateRotation(i);
        }

        public void updateCutout() {
            int i;
            if (isAttachedToWindow() && !this.pendingConfigChange) {
                this.mPosition = ScreenDecorations.getBoundPositionFromRotation(this.mInitialPosition, this.mRotation);
                requestLayout();
                getDisplay().getDisplayInfo(this.displayInfo);
                this.mBounds.clear();
                this.mBoundingRect.setEmpty();
                this.cutoutPath.reset();
                if (!ScreenDecorations.shouldDrawCutout(getContext()) || !hasCutout()) {
                    i = 8;
                } else {
                    this.mBounds.addAll(this.displayInfo.displayCutout.getBoundingRects());
                    localBounds(this.mBoundingRect);
                    updateGravity();
                    updateBoundingPath();
                    invalidate();
                    i = 0;
                }
                if (i != getVisibility()) {
                    setVisibility(i);
                }
            }
        }

        public final void updateBoundingPath() {
            Path cutoutPath = this.displayInfo.displayCutout.getCutoutPath();
            if (cutoutPath != null) {
                this.cutoutPath.set(cutoutPath);
            } else {
                this.cutoutPath.reset();
            }
        }

        public final void updateGravity() {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
                int gravity = getGravity(this.displayInfo.displayCutout);
                if (layoutParams2.gravity != gravity) {
                    layoutParams2.gravity = gravity;
                    setLayoutParams(layoutParams2);
                }
            }
        }

        public final boolean hasCutout() {
            DisplayCutout displayCutout = this.displayInfo.displayCutout;
            if (displayCutout == null) {
                return false;
            }
            int i = this.mPosition;
            if (i == 0) {
                return !displayCutout.getBoundingRectLeft().isEmpty();
            }
            if (i == 1) {
                return !displayCutout.getBoundingRectTop().isEmpty();
            }
            if (i == 3) {
                return !displayCutout.getBoundingRectBottom().isEmpty();
            }
            if (i == 2) {
                return !displayCutout.getBoundingRectRight().isEmpty();
            }
            return false;
        }

        public void onMeasure(int i, int i2) {
            if (this.mBounds.isEmpty()) {
                super.onMeasure(i, i2);
            } else if (this.showProtection) {
                this.mTotalBounds.union(this.mBoundingRect);
                Rect rect = this.mTotalBounds;
                RectF rectF = this.protectionRect;
                rect.union((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                setMeasuredDimension(View.resolveSizeAndState(this.mTotalBounds.width(), i, 0), View.resolveSizeAndState(this.mTotalBounds.height(), i2, 0));
            } else {
                setMeasuredDimension(View.resolveSizeAndState(this.mBoundingRect.width(), i, 0), View.resolveSizeAndState(this.mBoundingRect.height(), i2, 0));
            }
        }

        public static void boundsFromDirection(DisplayCutout displayCutout, int i, Rect rect) {
            if (i == 3) {
                rect.set(displayCutout.getBoundingRectLeft());
            } else if (i == 5) {
                rect.set(displayCutout.getBoundingRectRight());
            } else if (i == 48) {
                rect.set(displayCutout.getBoundingRectTop());
            } else if (i != 80) {
                rect.setEmpty();
            } else {
                rect.set(displayCutout.getBoundingRectBottom());
            }
        }

        public final void localBounds(Rect rect) {
            DisplayCutout displayCutout = this.displayInfo.displayCutout;
            boundsFromDirection(displayCutout, getGravity(displayCutout), rect);
        }

        public final int getGravity(DisplayCutout displayCutout) {
            int i = this.mPosition;
            if (i == 0) {
                if (!displayCutout.getBoundingRectLeft().isEmpty()) {
                    return 3;
                }
                return 0;
            } else if (i == 1) {
                if (!displayCutout.getBoundingRectTop().isEmpty()) {
                    return 48;
                }
                return 0;
            } else if (i != 3) {
                return (i != 2 || displayCutout.getBoundingRectRight().isEmpty()) ? 0 : 5;
            } else {
                if (!displayCutout.getBoundingRectBottom().isEmpty()) {
                    return 80;
                }
                return 0;
            }
        }
    }

    public class RestartingPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        public final int mPosition;
        public final Display.Mode mTargetDisplayMode;
        public final int mTargetRotation;
        public final View mView;

        public RestartingPreDrawListener(View view, int i, int i2, Display.Mode mode) {
            this.mView = view;
            this.mTargetRotation = i2;
            this.mTargetDisplayMode = mode;
            this.mPosition = i;
        }

        public boolean onPreDraw() {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (this.mTargetRotation == ScreenDecorations.this.mRotation && !ScreenDecorations.displayModeChanged(ScreenDecorations.this.mDisplayMode, this.mTargetDisplayMode)) {
                return true;
            }
            ScreenDecorations screenDecorations = ScreenDecorations.this;
            screenDecorations.mPendingConfigChange = false;
            screenDecorations.updateConfiguration();
            this.mView.invalidate();
            return false;
        }
    }

    public class ValidatingPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        public final View mView;

        public ValidatingPreDrawListener(View view) {
            this.mView = view;
        }

        public boolean onPreDraw() {
            ScreenDecorations.this.mContext.getDisplay().getDisplayInfo(ScreenDecorations.this.mDisplayInfo);
            DisplayInfo displayInfo = ScreenDecorations.this.mDisplayInfo;
            int i = displayInfo.rotation;
            Display.Mode mode = displayInfo.getMode();
            if ((i == ScreenDecorations.this.mRotation && !ScreenDecorations.displayModeChanged(ScreenDecorations.this.mDisplayMode, mode)) || ScreenDecorations.this.mPendingConfigChange) {
                return true;
            }
            this.mView.invalidate();
            return false;
        }
    }
}
