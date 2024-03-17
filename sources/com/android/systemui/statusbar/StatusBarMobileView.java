package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.DualToneHandler;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import java.util.ArrayList;

public class StatusBarMobileView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    public StatusBarIconView mDotView;
    public DualToneHandler mDualToneHandler;
    public boolean mForceHidden;
    public ImageView mIn;
    public View mInoutContainer;
    public ImageView mMobile;
    public SignalDrawable mMobileDrawable;
    public LinearLayout mMobileGroup;
    public ImageView mMobileRoaming;
    public View mMobileRoamingSpace;
    public ImageView mMobileType;
    public ImageView mOut;
    public boolean mProviderModel;
    public String mSlot;
    public StatusBarSignalPolicy.MobileIconState mState;
    public int mVisibleState = -1;
    public ImageView mVolte;

    public static StatusBarMobileView fromContext(Context context, String str, boolean z) {
        StatusBarMobileView statusBarMobileView = (StatusBarMobileView) LayoutInflater.from(context).inflate(R$layout.status_bar_mobile_signal_group, (ViewGroup) null);
        statusBarMobileView.setSlot(str);
        statusBarMobileView.init(z);
        statusBarMobileView.setVisibleState(0);
        return statusBarMobileView;
    }

    public StatusBarMobileView(Context context) {
        super(context);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (((float) rect.left) + translationX);
        rect.right = (int) (((float) rect.right) + translationX);
        rect.top = (int) (((float) rect.top) + translationY);
        rect.bottom = (int) (((float) rect.bottom) + translationY);
    }

    public final void init(boolean z) {
        this.mProviderModel = z;
        this.mDualToneHandler = new DualToneHandler(getContext());
        this.mMobileGroup = (LinearLayout) findViewById(R$id.mobile_group);
        this.mMobile = (ImageView) findViewById(R$id.mobile_signal);
        this.mMobileType = (ImageView) findViewById(R$id.mobile_type);
        if (this.mProviderModel) {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming_large);
        } else {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming);
        }
        this.mMobileRoamingSpace = findViewById(R$id.mobile_roaming_space);
        this.mIn = (ImageView) findViewById(R$id.mobile_in);
        this.mOut = (ImageView) findViewById(R$id.mobile_out);
        this.mInoutContainer = findViewById(R$id.inout_container);
        this.mVolte = (ImageView) findViewById(R$id.mobile_volte);
        SignalDrawable signalDrawable = new SignalDrawable(getContext());
        this.mMobileDrawable = signalDrawable;
        this.mMobile.setImageDrawable(signalDrawable);
        initDotView();
    }

    public final void initDotView() {
        StatusBarIconView statusBarIconView = new StatusBarIconView(this.mContext, this.mSlot, (StatusBarNotification) null);
        this.mDotView = statusBarIconView;
        statusBarIconView.setVisibleState(1);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_icon_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 8388627;
        addView(this.mDotView, layoutParams);
    }

    public void applyMobileState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z = true;
        if (mobileIconState == null) {
            if (getVisibility() == 8) {
                z = false;
            }
            setVisibility(8);
            this.mState = null;
        } else {
            StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
            if (mobileIconState2 == null) {
                this.mState = mobileIconState.copy();
                initViewState();
            } else {
                z = !mobileIconState2.equals(mobileIconState) ? updateState(mobileIconState.copy()) : false;
            }
        }
        if (z) {
            requestLayout();
        }
    }

    public final void initViewState() {
        setContentDescription(this.mState.contentDescription);
        if (!this.mState.visible || this.mForceHidden) {
            this.mMobileGroup.setVisibility(8);
        } else {
            this.mMobileGroup.setVisibility(0);
        }
        if (this.mState.strengthId >= 0) {
            this.mMobile.setVisibility(0);
            this.mMobileDrawable.setLevel(this.mState.strengthId);
        } else {
            this.mMobile.setVisibility(8);
        }
        StatusBarSignalPolicy.MobileIconState mobileIconState = this.mState;
        if (mobileIconState.typeId > 0) {
            this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
            this.mMobileType.setImageResource(this.mState.typeId);
            this.mMobileType.setVisibility(0);
        } else {
            this.mMobileType.setVisibility(8);
        }
        this.mMobile.setVisibility(this.mState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(this.mState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(this.mState.roaming ? 0 : 8);
        this.mIn.setVisibility(this.mState.activityIn ? 0 : 8);
        this.mOut.setVisibility(this.mState.activityOut ? 0 : 8);
        View view = this.mInoutContainer;
        StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
        view.setVisibility((mobileIconState2.activityIn || mobileIconState2.activityOut) ? 0 : 8);
        int i = this.mState.volteId;
        if (i > 0) {
            this.mVolte.setImageResource(i);
            this.mVolte.setVisibility(0);
            return;
        }
        this.mVolte.setVisibility(8);
    }

    public final boolean updateState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z;
        setContentDescription(mobileIconState.contentDescription);
        boolean z2 = false;
        int i = (!mobileIconState.visible || this.mForceHidden) ? 8 : 0;
        if (i == this.mMobileGroup.getVisibility() || this.mVisibleState != 0) {
            z = false;
        } else {
            this.mMobileGroup.setVisibility(i);
            z = true;
        }
        int i2 = mobileIconState.strengthId;
        if (i2 >= 0) {
            this.mMobileDrawable.setLevel(i2);
            this.mMobile.setVisibility(0);
        } else {
            this.mMobile.setVisibility(8);
        }
        int i3 = this.mState.typeId;
        int i4 = mobileIconState.typeId;
        if (i3 != i4) {
            z |= i4 == 0 || i3 == 0;
            if (i4 != 0) {
                this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
                this.mMobileType.setImageResource(mobileIconState.typeId);
                this.mMobileType.setVisibility(0);
            } else {
                this.mMobileType.setVisibility(8);
            }
        }
        this.mMobile.setVisibility(mobileIconState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mIn.setVisibility(mobileIconState.activityIn ? 0 : 8);
        this.mOut.setVisibility(mobileIconState.activityOut ? 0 : 8);
        this.mInoutContainer.setVisibility((mobileIconState.activityIn || mobileIconState.activityOut) ? 0 : 8);
        int i5 = this.mState.volteId;
        int i6 = mobileIconState.volteId;
        if (i5 != i6) {
            if (i6 != 0) {
                this.mVolte.setImageResource(i6);
                this.mVolte.setVisibility(0);
            } else {
                this.mVolte.setVisibility(8);
            }
        }
        boolean z3 = mobileIconState.roaming;
        StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
        if (!(z3 == mobileIconState2.roaming && mobileIconState.activityIn == mobileIconState2.activityIn && mobileIconState.activityOut == mobileIconState2.activityOut && mobileIconState.showTriangle == mobileIconState2.showTriangle)) {
            z2 = true;
        }
        boolean z4 = z | z2;
        this.mState = mobileIconState;
        return z4;
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        if (!DarkIconDispatcher.isInAreas(arrayList, this)) {
            f = 0.0f;
        }
        this.mMobileDrawable.setTintList(ColorStateList.valueOf(this.mDualToneHandler.getSingleColor(f)));
        ColorStateList valueOf = ColorStateList.valueOf(DarkIconDispatcher.getTint(arrayList, this, i));
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
        this.mDotView.setIconColor(i, false);
    }

    public String getSlot() {
        return this.mSlot;
    }

    public void setSlot(String str) {
        this.mSlot = str;
    }

    public void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mMobileDrawable.setTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mVolte.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    public void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    public boolean isIconVisible() {
        return this.mState.visible && !this.mForceHidden;
    }

    public void setVisibleState(int i, boolean z) {
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            if (i == 0) {
                this.mMobileGroup.setVisibility(0);
                this.mDotView.setVisibility(8);
            } else if (i != 1) {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(4);
            } else {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(0);
            }
        }
    }

    public int getVisibleState() {
        return this.mVisibleState;
    }

    @VisibleForTesting
    public StatusBarSignalPolicy.MobileIconState getState() {
        return this.mState;
    }

    public String toString() {
        return "StatusBarMobileView(slot=" + this.mSlot + " state=" + this.mState + ")";
    }
}
