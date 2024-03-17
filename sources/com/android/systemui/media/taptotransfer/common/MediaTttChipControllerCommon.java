package com.android.systemui.media.taptotransfer.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.widget.CachingIconView;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTttChipControllerCommon.kt */
public abstract class MediaTttChipControllerCommon<T extends ChipInfoCommon> {
    @Nullable
    public Runnable cancelChipViewTimeout;
    public final int chipLayoutRes;
    @Nullable
    public ViewGroup chipView;
    @NotNull
    public final Context context;
    @NotNull
    public final MediaTttLogger logger;
    @NotNull
    public final DelayableExecutor mainExecutor;
    @NotNull
    public final PowerManager powerManager;
    @NotNull
    public final TapGestureDetector tapGestureDetector;
    @NotNull
    public final ViewUtil viewUtil;
    @NotNull
    @SuppressLint({"WrongConstant"})
    public final WindowManager.LayoutParams windowLayoutParams;
    @NotNull
    public final WindowManager windowManager;

    @Nullable
    public Integer getIconSize(boolean z) {
        return null;
    }

    public abstract void updateChipView(@NotNull T t, @NotNull ViewGroup viewGroup);

    public MediaTttChipControllerCommon(@NotNull Context context2, @NotNull MediaTttLogger mediaTttLogger, @NotNull WindowManager windowManager2, @NotNull ViewUtil viewUtil2, @NotNull DelayableExecutor delayableExecutor, @NotNull TapGestureDetector tapGestureDetector2, @NotNull PowerManager powerManager2, int i) {
        this.context = context2;
        this.logger = mediaTttLogger;
        this.windowManager = windowManager2;
        this.viewUtil = viewUtil2;
        this.mainExecutor = delayableExecutor;
        this.tapGestureDetector = tapGestureDetector2;
        this.powerManager = powerManager2;
        this.chipLayoutRes = i;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.gravity = 49;
        layoutParams.type = 2020;
        layoutParams.flags = 32;
        layoutParams.setTitle("Media Transfer Chip View");
        layoutParams.format = -3;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
    }

    @NotNull
    public final Context getContext$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.context;
    }

    @NotNull
    public final MediaTttLogger getLogger$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.logger;
    }

    public final void displayChip(@NotNull T t) {
        ViewGroup viewGroup = this.chipView;
        if (viewGroup == null) {
            View inflate = LayoutInflater.from(this.context).inflate(this.chipLayoutRes, (ViewGroup) null);
            if (inflate != null) {
                this.chipView = (ViewGroup) inflate;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
            }
        }
        ViewGroup viewGroup2 = this.chipView;
        Intrinsics.checkNotNull(viewGroup2);
        updateChipView(t, viewGroup2);
        if (viewGroup == null) {
            this.tapGestureDetector.addOnGestureDetectedCallback(MediaTttChipControllerCommonKt.TAG, new MediaTttChipControllerCommon$displayChip$1(this));
            this.windowManager.addView(this.chipView, this.windowLayoutParams);
            this.powerManager.wakeUp(SystemClock.uptimeMillis(), 2, "com.android.systemui:media_tap_to_transfer_activated");
        }
        Runnable runnable = this.cancelChipViewTimeout;
        if (runnable != null) {
            runnable.run();
        }
        this.cancelChipViewTimeout = this.mainExecutor.executeDelayed(new MediaTttChipControllerCommon$displayChip$2(this), t.getTimeoutMs());
    }

    public void removeChip(@NotNull String str) {
        if (this.chipView != null) {
            this.logger.logChipRemoval(str);
            this.tapGestureDetector.removeOnGestureDetectedCallback(MediaTttChipControllerCommonKt.TAG);
            this.windowManager.removeView(this.chipView);
            this.chipView = null;
            Runnable runnable = this.cancelChipViewTimeout;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static /* synthetic */ void setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(MediaTttChipControllerCommon mediaTttChipControllerCommon, ViewGroup viewGroup, String str, Drawable drawable, CharSequence charSequence, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                drawable = null;
            }
            if ((i & 8) != 0) {
                charSequence = null;
            }
            mediaTttChipControllerCommon.setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(viewGroup, str, drawable, charSequence);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setIcon");
    }

    public final void setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull ViewGroup viewGroup, @Nullable String str, @Nullable Drawable drawable, @Nullable CharSequence charSequence) {
        CachingIconView requireViewById = viewGroup.requireViewById(R$id.app_icon);
        IconInfo iconInfo = getIconInfo(str);
        Integer iconSize = getIconSize(iconInfo.isAppIcon());
        if (iconSize != null) {
            int intValue = iconSize.intValue();
            ViewGroup.LayoutParams layoutParams = requireViewById.getLayoutParams();
            layoutParams.width = intValue;
            layoutParams.height = intValue;
            requireViewById.setLayoutParams(layoutParams);
        }
        if (charSequence == null) {
            charSequence = iconInfo.getIconName();
        }
        requireViewById.setContentDescription(charSequence);
        if (drawable == null) {
            drawable = iconInfo.getIcon();
        }
        requireViewById.setImageDrawable(drawable);
    }

    public final IconInfo getIconInfo(String str) {
        if (str != null) {
            try {
                return new IconInfo(this.context.getPackageManager().getApplicationInfo(str, PackageManager.ApplicationInfoFlags.of(0)).loadLabel(this.context.getPackageManager()).toString(), this.context.getPackageManager().getApplicationIcon(str), true);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(MediaTttChipControllerCommonKt.TAG, Intrinsics.stringPlus("Cannot find package ", str), e);
            }
        }
        String string = this.context.getString(R$string.media_output_dialog_unknown_launch_app_name);
        Drawable drawable = this.context.getResources().getDrawable(R$drawable.ic_cast);
        drawable.setTint(Utils.getColorAttrDefaultColor(getContext$frameworks__base__packages__SystemUI__android_common__SystemUI_core(), 16842806));
        return new IconInfo(string, drawable, false);
    }

    public final void onScreenTapped(MotionEvent motionEvent) {
        ViewGroup viewGroup = this.chipView;
        if (viewGroup != null && !this.viewUtil.touchIsWithinView(viewGroup, motionEvent.getX(), motionEvent.getY())) {
            removeChip("SCREEN_TAP");
        }
    }
}
