package com.google.android.setupcompat.template;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.StateSet;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import java.util.HashMap;

public class FooterButtonStyleUtils {
    public static final HashMap<Integer, ColorStateList> defaultTextColor = new HashMap<>();

    public static void applyButtonPartnerResources(Context context, Button button, boolean z, boolean z2, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        saveButtonDefaultTextColor(button);
        if (!z) {
            if (button.isEnabled()) {
                updateButtonTextEnabledColorWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonTextColorConfig());
            } else {
                updateButtonTextDisabledColorWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonDisableTextColorConfig());
            }
            updateButtonBackgroundWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonBackgroundConfig(), footerButtonPartnerConfig.getButtonDisableAlphaConfig(), footerButtonPartnerConfig.getButtonDisableBackgroundConfig());
        }
        updateButtonRippleColorWithPartnerConfig(context, button, z, footerButtonPartnerConfig.getButtonTextColorConfig(), footerButtonPartnerConfig.getButtonRippleColorAlphaConfig());
        updateButtonMarginStartWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonMarginStartConfig());
        updateButtonTextSizeWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonTextSizeConfig());
        updateButtonMinHeightWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonMinHeightConfig());
        updateButtonTypeFaceWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonTextTypeFaceConfig(), footerButtonPartnerConfig.getButtonTextStyleConfig());
        updateButtonRadiusWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonRadiusConfig());
        updateButtonIconWithPartnerConfig(context, button, footerButtonPartnerConfig.getButtonIconConfig(), z2);
    }

    public static void updateButtonTextEnabledColorWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        updateButtonTextEnabledColor(button, PartnerConfigHelper.get(context).getColor(context, partnerConfig));
    }

    public static void updateButtonTextEnabledColor(Button button, int i) {
        if (i != 0) {
            button.setTextColor(ColorStateList.valueOf(i));
        }
    }

    public static void updateButtonTextDisabledColorWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig)) {
            updateButtonTextDisabledColor(button, PartnerConfigHelper.get(context).getColor(context, partnerConfig));
        } else {
            updateButtonTextDisableDefaultColor(button, getButtonDefaultTextCorlor(button));
        }
    }

    public static void updateButtonTextDisabledColor(Button button, int i) {
        if (i != 0) {
            button.setTextColor(ColorStateList.valueOf(i));
        }
    }

    public static void updateButtonTextDisableDefaultColor(Button button, ColorStateList colorStateList) {
        button.setTextColor(colorStateList);
    }

    @TargetApi(29)
    public static void updateButtonBackgroundWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3) {
        Preconditions.checkArgument(true, "Update button background only support on sdk Q or higher");
        updateButtonBackgroundTintList(context, button, PartnerConfigHelper.get(context).getColor(context, partnerConfig), PartnerConfigHelper.get(context).getFraction(context, partnerConfig2, 0.0f), PartnerConfigHelper.get(context).getColor(context, partnerConfig3));
    }

    @TargetApi(29)
    public static void updateButtonBackgroundTintList(Context context, Button button, int i, float f, int i2) {
        int[] iArr = {-16842910};
        int[] iArr2 = new int[0];
        if (i != 0) {
            if (f <= 0.0f) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842803});
                f = obtainStyledAttributes.getFloat(0, 0.26f);
                obtainStyledAttributes.recycle();
            }
            if (i2 == 0) {
                i2 = i;
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{convertRgbToArgb(i2, f), i});
            button.getBackground().mutate().setState(new int[0]);
            button.refreshDrawableState();
            button.setBackgroundTintList(colorStateList);
        }
    }

    @TargetApi(29)
    public static void updateButtonRippleColorWithPartnerConfig(Context context, Button button, boolean z, PartnerConfig partnerConfig, PartnerConfig partnerConfig2) {
        int i;
        if (z) {
            i = button.getTextColors().getDefaultColor();
        } else {
            i = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
        }
        updateButtonRippleColor(button, i, PartnerConfigHelper.get(context).getFraction(context, partnerConfig2));
    }

    public static void updateButtonRippleColor(Button button, int i, float f) {
        RippleDrawable rippleDrawable = getRippleDrawable(button);
        if (rippleDrawable != null) {
            int convertRgbToArgb = convertRgbToArgb(i, f);
            rippleDrawable.setColor(new ColorStateList(new int[][]{new int[]{16842919}, new int[]{16842908}, StateSet.NOTHING}, new int[]{convertRgbToArgb, convertRgbToArgb, 0}));
        }
    }

    public static void updateButtonMarginStartWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig) && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins((int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig), marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        }
    }

    public static void updateButtonTextSizeWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
        if (dimension > 0.0f) {
            button.setTextSize(0, dimension);
        }
    }

    public static void updateButtonMinHeightWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig)) {
            float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
            if (dimension > 0.0f) {
                button.setMinHeight((int) dimension);
            }
        }
    }

    public static void updateButtonTypeFaceWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, PartnerConfig partnerConfig2) {
        String string = PartnerConfigHelper.get(context).getString(context, partnerConfig);
        int i = 0;
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig2)) {
            i = PartnerConfigHelper.get(context).getInteger(context, partnerConfig2, 0);
        }
        Typeface create = Typeface.create(string, i);
        if (create != null) {
            button.setTypeface(create);
        }
    }

    public static void updateButtonRadiusWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        float dimension = PartnerConfigHelper.get(context).getDimension(context, partnerConfig);
        GradientDrawable gradientDrawable = getGradientDrawable(button);
        if (gradientDrawable != null) {
            gradientDrawable.setCornerRadius(dimension);
        }
    }

    public static void updateButtonIconWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig, boolean z) {
        if (button != null) {
            Drawable drawable = null;
            if (partnerConfig != null) {
                drawable = PartnerConfigHelper.get(context).getDrawable(context, partnerConfig);
            }
            setButtonIcon(button, drawable, z);
        }
    }

    public static void setButtonIcon(Button button, Drawable drawable, boolean z) {
        Drawable drawable2;
        if (button != null) {
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            if (z) {
                drawable2 = drawable;
                drawable = null;
            } else {
                drawable2 = null;
            }
            button.setCompoundDrawablesRelative(drawable, (Drawable) null, drawable2, (Drawable) null);
        }
    }

    public static void updateButtonBackground(Button button, int i) {
        button.getBackground().mutate().setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    public static void saveButtonDefaultTextColor(Button button) {
        defaultTextColor.put(Integer.valueOf(button.getId()), button.getTextColors());
    }

    public static ColorStateList getButtonDefaultTextCorlor(Button button) {
        HashMap<Integer, ColorStateList> hashMap = defaultTextColor;
        if (hashMap.containsKey(Integer.valueOf(button.getId()))) {
            return hashMap.get(Integer.valueOf(button.getId()));
        }
        throw new IllegalStateException("There is no saved default color for button");
    }

    public static void clearSavedDefaultTextColor() {
        defaultTextColor.clear();
    }

    public static GradientDrawable getGradientDrawable(Button button) {
        Drawable background = button.getBackground();
        if (background instanceof InsetDrawable) {
            return (GradientDrawable) ((LayerDrawable) ((InsetDrawable) background).getDrawable()).getDrawable(0);
        }
        if (!(background instanceof RippleDrawable)) {
            return null;
        }
        RippleDrawable rippleDrawable = (RippleDrawable) background;
        if (rippleDrawable.getDrawable(0) instanceof GradientDrawable) {
            return (GradientDrawable) rippleDrawable.getDrawable(0);
        }
        return (GradientDrawable) ((InsetDrawable) rippleDrawable.getDrawable(0)).getDrawable();
    }

    public static RippleDrawable getRippleDrawable(Button button) {
        Drawable background = button.getBackground();
        if (background instanceof InsetDrawable) {
            return (RippleDrawable) ((InsetDrawable) background).getDrawable();
        }
        if (background instanceof RippleDrawable) {
            return (RippleDrawable) background;
        }
        return null;
    }

    public static int convertRgbToArgb(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }
}
