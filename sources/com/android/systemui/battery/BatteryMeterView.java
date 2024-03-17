package com.android.systemui.battery;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.DejankUtils;
import com.android.systemui.DualToneHandler;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;

public class BatteryMeterView extends LinearLayout implements DarkIconDispatcher.DarkReceiver {
    public BatteryEstimateFetcher mBatteryEstimateFetcher;
    public final ImageView mBatteryIconView;
    public TextView mBatteryPercentView;
    public boolean mBatteryStateUnknown;
    public boolean mCharging;
    public final ThemedBatteryDrawable mDrawable;
    public DualToneHandler mDualToneHandler;
    public int mLevel;
    public int mNonAdaptedBackgroundColor;
    public int mNonAdaptedForegroundColor;
    public int mNonAdaptedSingleToneColor;
    public final int mPercentageStyleId;
    public boolean mShowPercentAvailable;
    public int mShowPercentMode;
    public int mTextColor;
    public Drawable mUnknownStateDrawable;

    public interface BatteryEstimateFetcher {
        void fetchBatteryTimeRemainingEstimate(BatteryController.EstimateFetchCompletion estimateFetchCompletion);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowPercentMode = 0;
        setOrientation(0);
        setGravity(8388627);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BatteryMeterView, i, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.BatteryMeterView_frameColor, context.getColor(R$color.meter_background_color));
        this.mPercentageStyleId = obtainStyledAttributes.getResourceId(R$styleable.BatteryMeterView_textAppearance, 0);
        ThemedBatteryDrawable themedBatteryDrawable = new ThemedBatteryDrawable(context, color);
        this.mDrawable = themedBatteryDrawable;
        obtainStyledAttributes.recycle();
        this.mShowPercentAvailable = context.getResources().getBoolean(17891384);
        setupLayoutTransition();
        ImageView imageView = new ImageView(context);
        this.mBatteryIconView = imageView;
        imageView.setImageDrawable(themedBatteryDrawable);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_width), getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_height));
        marginLayoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R$dimen.battery_margin_bottom));
        addView(imageView, marginLayoutParams);
        updateShowPercent();
        this.mDualToneHandler = new DualToneHandler(context);
        onDarkChanged(new ArrayList(), 0.0f, -1);
        setClipChildren(false);
        setClipToPadding(false);
    }

    public final void setupLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200);
        layoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, "alpha", new float[]{0.0f, 1.0f}));
        layoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", new float[]{1.0f, 0.0f});
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimator(3, ofFloat);
        layoutTransition.setAnimator(0, (Animator) null);
        layoutTransition.setAnimator(1, (Animator) null);
        layoutTransition.setAnimator(4, (Animator) null);
        setLayoutTransition(layoutTransition);
    }

    public void setForceShowPercent(boolean z) {
        setPercentShowMode(z ? 1 : 0);
    }

    public void setPercentShowMode(int i) {
        if (i != this.mShowPercentMode) {
            this.mShowPercentMode = i;
            updateShowPercent();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updatePercentView();
    }

    public void setColorsFromContext(Context context) {
        if (context != null) {
            this.mDualToneHandler.setColorsFromContext(context);
        }
    }

    public void onBatteryLevelChanged(int i, boolean z) {
        this.mDrawable.setCharging(z);
        this.mDrawable.setBatteryLevel(i);
        this.mCharging = z;
        this.mLevel = i;
        updatePercentText();
    }

    public void onPowerSaveChanged(boolean z) {
        this.mDrawable.setPowerSaveEnabled(z);
    }

    public final TextView loadPercentView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R$layout.battery_percentage_view, (ViewGroup) null);
    }

    public void updatePercentView() {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            removeView(textView);
            this.mBatteryPercentView = null;
        }
        updateShowPercent();
    }

    public void setBatteryEstimateFetcher(BatteryEstimateFetcher batteryEstimateFetcher) {
        this.mBatteryEstimateFetcher = batteryEstimateFetcher;
    }

    public void updatePercentText() {
        int i;
        if (this.mBatteryStateUnknown) {
            setContentDescription(getContext().getString(R$string.accessibility_battery_unknown));
            return;
        }
        BatteryEstimateFetcher batteryEstimateFetcher = this.mBatteryEstimateFetcher;
        if (batteryEstimateFetcher != null) {
            if (this.mBatteryPercentView == null) {
                Context context = getContext();
                if (this.mCharging) {
                    i = R$string.accessibility_battery_level_charging;
                } else {
                    i = R$string.accessibility_battery_level;
                }
                setContentDescription(context.getString(i, new Object[]{Integer.valueOf(this.mLevel)}));
            } else if (this.mShowPercentMode != 3 || this.mCharging) {
                setPercentTextAtCurrentLevel();
            } else {
                batteryEstimateFetcher.fetchBatteryTimeRemainingEstimate(new BatteryMeterView$$ExternalSyntheticLambda1(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePercentText$0(String str) {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            if (str == null || this.mShowPercentMode != 3) {
                setPercentTextAtCurrentLevel();
                return;
            }
            textView.setText(str);
            setContentDescription(getContext().getString(R$string.accessibility_battery_level_with_estimate, new Object[]{Integer.valueOf(this.mLevel), str}));
        }
    }

    public final void setPercentTextAtCurrentLevel() {
        int i;
        if (this.mBatteryPercentView != null) {
            String format = NumberFormat.getPercentInstance().format((double) (((float) this.mLevel) / 100.0f));
            if (!TextUtils.equals(this.mBatteryPercentView.getText(), format)) {
                this.mBatteryPercentView.setText(format);
            }
            Context context = getContext();
            if (this.mCharging) {
                i = R$string.accessibility_battery_level_charging;
            } else {
                i = R$string.accessibility_battery_level;
            }
            setContentDescription(context.getString(i, new Object[]{Integer.valueOf(this.mLevel)}));
        }
    }

    public void updateShowPercent() {
        int i;
        boolean z = false;
        boolean z2 = this.mBatteryPercentView != null;
        if (((this.mShowPercentAvailable && (((Integer) DejankUtils.whitelistIpcs(new BatteryMeterView$$ExternalSyntheticLambda0(this))).intValue() != 0) && this.mShowPercentMode != 2) || (i = this.mShowPercentMode) == 1 || i == 3) && !this.mBatteryStateUnknown) {
            z = true;
        }
        if (z) {
            if (!z2) {
                TextView loadPercentView = loadPercentView();
                this.mBatteryPercentView = loadPercentView;
                int i2 = this.mPercentageStyleId;
                if (i2 != 0) {
                    loadPercentView.setTextAppearance(i2);
                }
                int i3 = this.mTextColor;
                if (i3 != 0) {
                    this.mBatteryPercentView.setTextColor(i3);
                }
                updatePercentText();
                addView(this.mBatteryPercentView, new ViewGroup.LayoutParams(-2, -1));
            }
        } else if (z2) {
            removeView(this.mBatteryPercentView);
            this.mBatteryPercentView = null;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Integer lambda$updateShowPercent$1() {
        return Integer.valueOf(Settings.System.getIntForUser(getContext().getContentResolver(), "status_bar_show_battery_percent", 0, -2));
    }

    public final Drawable getUnknownStateDrawable() {
        if (this.mUnknownStateDrawable == null) {
            Drawable drawable = this.mContext.getDrawable(R$drawable.ic_battery_unknown);
            this.mUnknownStateDrawable = drawable;
            drawable.setTint(this.mTextColor);
        }
        return this.mUnknownStateDrawable;
    }

    public void onBatteryUnknownStateChanged(boolean z) {
        if (this.mBatteryStateUnknown != z) {
            this.mBatteryStateUnknown = z;
            if (z) {
                this.mBatteryIconView.setImageDrawable(getUnknownStateDrawable());
            } else {
                this.mBatteryIconView.setImageDrawable(this.mDrawable);
            }
            updateShowPercent();
        }
    }

    public void scaleBatteryMeterViews() {
        Resources resources = getContext().getResources();
        TypedValue typedValue = new TypedValue();
        resources.getValue(R$dimen.status_bar_icon_scale_factor, typedValue, true);
        float f = typedValue.getFloat();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.status_bar_battery_icon_height);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.status_bar_battery_icon_width);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R$dimen.battery_margin_bottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (((float) dimensionPixelSize2) * f), (int) (((float) dimensionPixelSize) * f));
        layoutParams.setMargins(0, 0, 0, dimensionPixelSize3);
        this.mBatteryIconView.setLayoutParams(layoutParams);
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        if (!DarkIconDispatcher.isInAreas(arrayList, this)) {
            f = 0.0f;
        }
        this.mNonAdaptedSingleToneColor = this.mDualToneHandler.getSingleColor(f);
        this.mNonAdaptedForegroundColor = this.mDualToneHandler.getFillColor(f);
        int backgroundColor = this.mDualToneHandler.getBackgroundColor(f);
        this.mNonAdaptedBackgroundColor = backgroundColor;
        updateColors(this.mNonAdaptedForegroundColor, backgroundColor, this.mNonAdaptedSingleToneColor);
    }

    public void updateColors(int i, int i2, int i3) {
        this.mDrawable.setColors(i, i2, i3);
        this.mTextColor = i3;
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            textView.setTextColor(i3);
        }
        Drawable drawable = this.mUnknownStateDrawable;
        if (drawable != null) {
            drawable.setTint(i3);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        String str;
        CharSequence charSequence = null;
        if (this.mDrawable == null) {
            str = null;
        } else {
            str = this.mDrawable.getPowerSaveEnabled() + "";
        }
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            charSequence = textView.getText();
        }
        printWriter.println("  BatteryMeterView:");
        printWriter.println("    mDrawable.getPowerSave: " + str);
        printWriter.println("    mBatteryPercentView.getText(): " + charSequence);
        printWriter.println("    mTextColor: #" + Integer.toHexString(this.mTextColor));
        printWriter.println("    mBatteryStateUnknown: " + this.mBatteryStateUnknown);
        printWriter.println("    mLevel: " + this.mLevel);
        printWriter.println("    mMode: " + this.mShowPercentMode);
    }

    public CharSequence getBatteryPercentViewText() {
        return this.mBatteryPercentView.getText();
    }
}
