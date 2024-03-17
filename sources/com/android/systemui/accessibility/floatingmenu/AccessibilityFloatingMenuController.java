package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.os.UserHandle;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;

public class AccessibilityFloatingMenuController implements AccessibilityButtonModeObserver.ModeChangedListener, AccessibilityButtonTargetsObserver.TargetsChangedListener {
    public final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    public final AccessibilityButtonTargetsObserver mAccessibilityButtonTargetsObserver;
    public int mBtnMode;
    public String mBtnTargets;
    public Context mContext;
    @VisibleForTesting
    public IAccessibilityFloatingMenu mFloatingMenu;
    public boolean mIsKeyguardVisible;
    @VisibleForTesting
    public final KeyguardUpdateMonitorCallback mKeyguardCallback = new KeyguardUpdateMonitorCallback() {
        public void onUserUnlocked() {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.handleFloatingMenuVisibility(accessibilityFloatingMenuController.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            AccessibilityFloatingMenuController.this.mIsKeyguardVisible = z;
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.handleFloatingMenuVisibility(accessibilityFloatingMenuController.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
        }

        public void onUserSwitching(int i) {
            AccessibilityFloatingMenuController.this.destroyFloatingMenu();
        }

        public void onUserSwitchComplete(int i) {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.mContext = accessibilityFloatingMenuController.mContext.createContextAsUser(UserHandle.of(i), 0);
            AccessibilityFloatingMenuController accessibilityFloatingMenuController2 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController2.mBtnMode = accessibilityFloatingMenuController2.mAccessibilityButtonModeObserver.getCurrentAccessibilityButtonMode();
            AccessibilityFloatingMenuController accessibilityFloatingMenuController3 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController3.mBtnTargets = accessibilityFloatingMenuController3.mAccessibilityButtonTargetsObserver.getCurrentAccessibilityButtonTargets();
            AccessibilityFloatingMenuController accessibilityFloatingMenuController4 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController4.handleFloatingMenuVisibility(accessibilityFloatingMenuController4.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
        }
    };
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    public AccessibilityFloatingMenuController(Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mContext = context;
        this.mAccessibilityButtonTargetsObserver = accessibilityButtonTargetsObserver;
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mIsKeyguardVisible = false;
    }

    public void onAccessibilityButtonModeChanged(int i) {
        this.mBtnMode = i;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, i, this.mBtnTargets);
    }

    public void onAccessibilityButtonTargetsChanged(String str) {
        this.mBtnTargets = str;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, this.mBtnMode, str);
    }

    public void init() {
        this.mBtnMode = this.mAccessibilityButtonModeObserver.getCurrentAccessibilityButtonMode();
        this.mBtnTargets = this.mAccessibilityButtonTargetsObserver.getCurrentAccessibilityButtonTargets();
        registerContentObservers();
    }

    public final void registerContentObservers() {
        this.mAccessibilityButtonModeObserver.addListener(this);
        this.mAccessibilityButtonTargetsObserver.addListener(this);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardCallback);
    }

    public final void handleFloatingMenuVisibility(boolean z, int i, String str) {
        if (z) {
            destroyFloatingMenu();
        } else if (shouldShowFloatingMenu(i, str)) {
            showFloatingMenu();
        } else {
            destroyFloatingMenu();
        }
    }

    public final boolean shouldShowFloatingMenu(int i, String str) {
        return i == 1 && !TextUtils.isEmpty(str);
    }

    public final void showFloatingMenu() {
        if (this.mFloatingMenu == null) {
            this.mFloatingMenu = new AccessibilityFloatingMenu(this.mContext);
        }
        this.mFloatingMenu.show();
    }

    public final void destroyFloatingMenu() {
        IAccessibilityFloatingMenu iAccessibilityFloatingMenu = this.mFloatingMenu;
        if (iAccessibilityFloatingMenu != null) {
            iAccessibilityFloatingMenu.hide();
            this.mFloatingMenu = null;
        }
    }
}
