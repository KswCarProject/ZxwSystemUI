package com.android.systemui.dreams;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.concurrent.Executor;

public class DreamOverlayService extends android.service.dreams.DreamOverlayService {
    public static final boolean DEBUG = Log.isLoggable("DreamOverlayService", 3);
    public final Context mContext;
    public boolean mDestroyed;
    public final DreamOverlayContainerViewController mDreamOverlayContainerViewController;
    public DreamOverlayTouchMonitor mDreamOverlayTouchMonitor;
    public final Executor mExecutor;
    public final Complication.Host mHost;
    public final KeyguardUpdateMonitorCallback mKeyguardCallback;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LifecycleRegistry mLifecycleRegistry;
    public DreamOverlayStateController mStateController;
    public final UiEventLogger mUiEventLogger;
    public ViewModelStore mViewModelStore = new ViewModelStore();
    public Window mWindow;

    public enum DreamOverlayEvent implements UiEventLogger.UiEventEnum {
        DREAM_OVERLAY_ENTER_START(989),
        DREAM_OVERLAY_COMPLETE_START(990);
        
        private final int mId;

        /* access modifiers changed from: public */
        DreamOverlayEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public DreamOverlayService(Context context, Executor executor, DreamOverlayComponent.Factory factory, DreamOverlayStateController dreamOverlayStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, UiEventLogger uiEventLogger) {
        AnonymousClass1 r0 = new Complication.Host() {
        };
        this.mHost = r0;
        AnonymousClass2 r1 = new KeyguardUpdateMonitorCallback() {
            public void onShadeExpandedChanged(boolean z) {
                Lifecycle.State currentState = DreamOverlayService.this.mLifecycleRegistry.getCurrentState();
                Lifecycle.State state = Lifecycle.State.RESUMED;
                if (currentState == state || DreamOverlayService.this.mLifecycleRegistry.getCurrentState() == Lifecycle.State.STARTED) {
                    LifecycleRegistry r3 = DreamOverlayService.this.mLifecycleRegistry;
                    if (z) {
                        state = Lifecycle.State.STARTED;
                    }
                    r3.setCurrentState(state);
                }
            }
        };
        this.mKeyguardCallback = r1;
        this.mContext = context;
        this.mExecutor = executor;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(r1);
        this.mStateController = dreamOverlayStateController;
        this.mUiEventLogger = uiEventLogger;
        DreamOverlayComponent create = factory.create(this.mViewModelStore, r0);
        this.mDreamOverlayContainerViewController = create.getDreamOverlayContainerViewController();
        setCurrentState(Lifecycle.State.CREATED);
        this.mLifecycleRegistry = create.getLifecycleRegistry();
        DreamOverlayTouchMonitor dreamOverlayTouchMonitor = create.getDreamOverlayTouchMonitor();
        this.mDreamOverlayTouchMonitor = dreamOverlayTouchMonitor;
        dreamOverlayTouchMonitor.init();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setCurrentState$0(Lifecycle.State state) {
        this.mLifecycleRegistry.setCurrentState(state);
    }

    public final void setCurrentState(Lifecycle.State state) {
        this.mExecutor.execute(new DreamOverlayService$$ExternalSyntheticLambda1(this, state));
    }

    public void onDestroy() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardCallback);
        setCurrentState(Lifecycle.State.DESTROYED);
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        Window window = this.mWindow;
        if (window != null) {
            windowManager.removeView(window.getDecorView());
        }
        this.mStateController.setOverlayActive(false);
        this.mDestroyed = true;
        DreamOverlayService.super.onDestroy();
    }

    public void onStartDream(WindowManager.LayoutParams layoutParams) {
        this.mUiEventLogger.log(DreamOverlayEvent.DREAM_OVERLAY_ENTER_START);
        setCurrentState(Lifecycle.State.STARTED);
        this.mExecutor.execute(new DreamOverlayService$$ExternalSyntheticLambda0(this, layoutParams));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartDream$1(WindowManager.LayoutParams layoutParams) {
        if (!this.mDestroyed) {
            this.mStateController.setShouldShowComplications(shouldShowComplications());
            addOverlayWindowLocked(layoutParams);
            setCurrentState(Lifecycle.State.RESUMED);
            this.mStateController.setOverlayActive(true);
            this.mUiEventLogger.log(DreamOverlayEvent.DREAM_OVERLAY_COMPLETE_START);
        }
    }

    public final void addOverlayWindowLocked(WindowManager.LayoutParams layoutParams) {
        PhoneWindow phoneWindow = new PhoneWindow(this.mContext);
        this.mWindow = phoneWindow;
        phoneWindow.setAttributes(layoutParams);
        this.mWindow.setWindowManager((WindowManager) null, layoutParams.token, "DreamOverlay", true);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(Integer.MIN_VALUE);
        this.mWindow.addFlags(8);
        this.mWindow.requestFeature(1);
        this.mWindow.getDecorView().getWindowInsetsController().hide(WindowInsets.Type.systemBars());
        this.mWindow.setDecorFitsSystemWindows(false);
        if (DEBUG) {
            Log.d("DreamOverlayService", "adding overlay window to dream");
        }
        this.mDreamOverlayContainerViewController.init();
        removeContainerViewFromParent();
        this.mWindow.setContentView(this.mDreamOverlayContainerViewController.getContainerView());
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mWindow.getDecorView(), this.mWindow.getAttributes());
    }

    public final void removeContainerViewFromParent() {
        ViewGroup viewGroup;
        View containerView = this.mDreamOverlayContainerViewController.getContainerView();
        if (containerView != null && (viewGroup = (ViewGroup) containerView.getParent()) != null) {
            Log.w("DreamOverlayService", "Removing dream overlay container view parent!");
            viewGroup.removeView(containerView);
        }
    }
}
