package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.util.leak.RotationUtils;
import java.util.Objects;

public class PhoneStatusBarView extends FrameLayout {
    public DarkIconDispatcher.DarkReceiver mBattery;
    public DarkIconDispatcher.DarkReceiver mClock;
    public final StatusBarContentInsetsProvider mContentInsetsProvider = ((StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class));
    public int mCutoutSideNudge = 0;
    public View mCutoutSpace;
    public DisplayCutout mDisplayCutout;
    public Rect mDisplaySize;
    public int mRotationOrientation = -1;
    public int mStatusBarHeight;
    public TouchEventHandler mTouchEventHandler;

    public interface TouchEventHandler {
        boolean handleTouchEvent(MotionEvent motionEvent);

        void onInterceptTouchEvent(MotionEvent motionEvent);
    }

    public PhoneStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setTouchEventHandler(TouchEventHandler touchEventHandler) {
        this.mTouchEventHandler = touchEventHandler;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBattery = (DarkIconDispatcher.DarkReceiver) findViewById(R$id.battery);
        this.mClock = (DarkIconDispatcher.DarkReceiver) findViewById(R$id.clock);
        this.mCutoutSpace = findViewById(R$id.cutout_space_view);
        updateResources();
    }

    public void onAttachedToWindow() {
        Class cls = DarkIconDispatcher.class;
        super.onAttachedToWindow();
        ((DarkIconDispatcher) Dependency.get(cls)).addDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(cls)).addDarkReceiver(this.mClock);
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
        }
    }

    public void onDetachedFromWindow() {
        Class cls = DarkIconDispatcher.class;
        super.onDetachedFromWindow();
        ((DarkIconDispatcher) Dependency.get(cls)).removeDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(cls)).removeDarkReceiver(this.mClock);
        this.mDisplayCutout = null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
            requestLayout();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (updateDisplayParameters()) {
            updateLayoutForCutout();
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public final boolean updateDisplayParameters() {
        boolean z;
        int exactRotation = RotationUtils.getExactRotation(this.mContext);
        if (exactRotation != this.mRotationOrientation) {
            this.mRotationOrientation = exactRotation;
            z = true;
        } else {
            z = false;
        }
        if (!Objects.equals(getRootWindowInsets().getDisplayCutout(), this.mDisplayCutout)) {
            this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
            z = true;
        }
        Rect maxBounds = this.mContext.getResources().getConfiguration().windowConfiguration.getMaxBounds();
        if (Objects.equals(maxBounds, this.mDisplaySize)) {
            return z;
        }
        this.mDisplaySize = maxBounds;
        return true;
    }

    public boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        TouchEventHandler touchEventHandler = this.mTouchEventHandler;
        if (touchEventHandler != null) {
            return touchEventHandler.handleTouchEvent(motionEvent);
        }
        Log.w("PhoneStatusBarView", String.format("onTouch: No touch handler provided; eating gesture at (%d,%d)", new Object[]{Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())}));
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.mTouchEventHandler.onInterceptTouchEvent(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void updateResources() {
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(R$dimen.display_cutout_margin_consumption);
        updateStatusBarHeight();
    }

    public final void updateStatusBarHeight() {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
        this.mStatusBarHeight = statusBarHeight;
        layoutParams.height = statusBarHeight - i;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.status_bar_padding_top);
        getResources().getDimensionPixelSize(R$dimen.status_bar_padding_start);
        getResources().getDimensionPixelSize(R$dimen.status_bar_padding_end);
        findViewById(R$id.status_bar_contents).setPaddingRelative(0, dimensionPixelSize, 0, 0);
        findViewById(R$id.notification_lights_out).setPaddingRelative(0, 0, 0, 0);
        setLayoutParams(layoutParams);
    }

    public final void updateLayoutForCutout() {
        updateStatusBarHeight();
        updateCutoutLocation();
        updateSafeInsets();
    }

    public final void updateCutoutLocation() {
        if (this.mCutoutSpace != null) {
            boolean currentRotationHasCornerCutout = this.mContentInsetsProvider.currentRotationHasCornerCutout();
            DisplayCutout displayCutout = this.mDisplayCutout;
            if (displayCutout == null || displayCutout.isEmpty() || currentRotationHasCornerCutout) {
                this.mCutoutSpace.setVisibility(8);
                return;
            }
            this.mCutoutSpace.setVisibility(0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
            Rect boundingRectTop = this.mDisplayCutout.getBoundingRectTop();
            int i = boundingRectTop.left;
            int i2 = this.mCutoutSideNudge;
            boundingRectTop.left = i + i2;
            boundingRectTop.right -= i2;
            layoutParams.width = boundingRectTop.width();
            layoutParams.height = boundingRectTop.height();
        }
    }

    public final void updateSafeInsets() {
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), getPaddingTop(), ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), getPaddingBottom());
    }
}
