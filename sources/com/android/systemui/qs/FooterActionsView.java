package com.android.systemui.qs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: FooterActionsView.kt */
public final class FooterActionsView extends LinearLayout {
    public ImageView multiUserAvatar;
    public MultiUserSwitch multiUserSwitch;
    public boolean qsDisabled;
    public View settingsContainer;

    public FooterActionsView(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.settingsContainer = findViewById(R$id.settings_button_container);
        MultiUserSwitch multiUserSwitch2 = (MultiUserSwitch) findViewById(R$id.multi_user_switch);
        this.multiUserSwitch = multiUserSwitch2;
        View view = null;
        if (multiUserSwitch2 == null) {
            multiUserSwitch2 = null;
        }
        this.multiUserAvatar = (ImageView) multiUserSwitch2.findViewById(R$id.multi_user_avatar);
        View view2 = this.settingsContainer;
        if (view2 == null) {
            view2 = null;
        }
        if (view2.getBackground() instanceof RippleDrawable) {
            View view3 = this.settingsContainer;
            if (view3 != null) {
                view = view3;
            }
            Drawable background = view.getBackground();
            if (background != null) {
                ((RippleDrawable) background).setForceSoftware(true);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            }
        }
        setImportantForAccessibility(1);
    }

    public final void disable(int i, boolean z) {
        boolean z2 = true;
        if ((i & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.qsDisabled) {
            this.qsDisabled = z2;
            updateEverything(z);
        }
    }

    public final void updateEverything(boolean z) {
        post(new FooterActionsView$updateEverything$1(this, z));
    }

    public final void updateClickabilities() {
        MultiUserSwitch multiUserSwitch2 = this.multiUserSwitch;
        View view = null;
        if (multiUserSwitch2 == null) {
            multiUserSwitch2 = null;
        }
        MultiUserSwitch multiUserSwitch3 = this.multiUserSwitch;
        if (multiUserSwitch3 == null) {
            multiUserSwitch3 = null;
        }
        boolean z = true;
        multiUserSwitch2.setClickable(multiUserSwitch3.getVisibility() == 0);
        View view2 = this.settingsContainer;
        if (view2 == null) {
            view2 = null;
        }
        View view3 = this.settingsContainer;
        if (view3 != null) {
            view = view3;
        }
        if (view.getVisibility() != 0) {
            z = false;
        }
        view2.setClickable(z);
    }

    public final void updateVisibilities(boolean z) {
        View view = this.settingsContainer;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        int i = 8;
        int i2 = 0;
        view.setVisibility(this.qsDisabled ? 8 : 0);
        MultiUserSwitch multiUserSwitch2 = this.multiUserSwitch;
        if (multiUserSwitch2 == null) {
            multiUserSwitch2 = null;
        }
        if (z) {
            i = 0;
        }
        multiUserSwitch2.setVisibility(i);
        boolean isDeviceInDemoMode = UserManager.isDeviceInDemoMode(getContext());
        View view3 = this.settingsContainer;
        if (view3 != null) {
            view2 = view3;
        }
        if (isDeviceInDemoMode) {
            i2 = 4;
        }
        view2.setVisibility(i2);
    }

    public final void onUserInfoChanged(@Nullable Drawable drawable, boolean z) {
        if (drawable != null && z && !(drawable instanceof UserIconDrawable)) {
            drawable = drawable.getConstantState().newDrawable(getResources()).mutate();
            drawable.setColorFilter(Utils.getColorAttrDefaultColor(this.mContext, 16842800), PorterDuff.Mode.SRC_IN);
        }
        ImageView imageView = this.multiUserAvatar;
        if (imageView == null) {
            imageView = null;
        }
        imageView.setImageDrawable(drawable);
    }

    public boolean onInterceptTouchEvent(@Nullable MotionEvent motionEvent) {
        if (FooterActionsViewKt.VERBOSE) {
            Log.d("FooterActionsView", Intrinsics.stringPlus("FooterActionsView onInterceptTouchEvent ", motionEvent == null ? null : FooterActionsViewKt.getString(motionEvent)));
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (FooterActionsViewKt.VERBOSE) {
            Log.d("FooterActionsView", Intrinsics.stringPlus("FooterActionsView onTouchEvent ", motionEvent == null ? null : FooterActionsViewKt.getString(motionEvent)));
        }
        return super.onTouchEvent(motionEvent);
    }
}
