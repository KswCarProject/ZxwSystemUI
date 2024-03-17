package com.google.android.setupdesign.util;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.R$dimen;
import com.google.android.setupdesign.util.TextViewPartnerStyler;

public final class HeaderAreaStyler {
    public static final String WARN_TO_USE_DRAWABLE = "To achieve scaling icon in SetupDesign lib, should use vector drawable icon from ";

    public static void applyPartnerCustomizationHeaderStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_HEADER_TEXT_COLOR, (PartnerConfig) null, PartnerConfig.CONFIG_HEADER_TEXT_SIZE, PartnerConfig.CONFIG_HEADER_FONT_FAMILY, (PartnerConfig) null, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationDescriptionHeavyStyle(TextView textView) {
        if (textView != null) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_DESCRIPTION_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_LINK_TEXT_COLOR, PartnerConfig.CONFIG_DESCRIPTION_TEXT_SIZE, PartnerConfig.CONFIG_DESCRIPTION_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
        }
    }

    public static void applyPartnerCustomizationHeaderAreaStyle(ViewGroup viewGroup) {
        if (viewGroup != null) {
            Context context = viewGroup.getContext();
            viewGroup.setBackgroundColor(PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_HEADER_AREA_BACKGROUND_COLOR));
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_HEADER_CONTAINER_MARGIN_BOTTOM;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig));
                    viewGroup.setLayoutParams(layoutParams);
                }
            }
        }
    }

    public static void applyPartnerCustomizationProgressBarStyle(ProgressBar progressBar) {
        if (progressBar != null) {
            Context context = progressBar.getContext();
            ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                int i = marginLayoutParams.topMargin;
                PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig = PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_TOP;
                if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                    i = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig, context.getResources().getDimension(R$dimen.sud_progress_bar_margin_top));
                }
                int i2 = marginLayoutParams.bottomMargin;
                PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_BOTTOM;
                if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                    i2 = (int) PartnerConfigHelper.get(context).getDimension(context, partnerConfig2, context.getResources().getDimension(R$dimen.sud_progress_bar_margin_bottom));
                }
                if (i != marginLayoutParams.topMargin || i2 != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.setMargins(marginLayoutParams.leftMargin, i, marginLayoutParams.rightMargin, i2);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004c, code lost:
        r5 = (int) r0.getResources().getDimension(com.google.android.setupdesign.R$dimen.sud_horizontal_icon_height);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void applyPartnerCustomizationIconStyle(android.widget.ImageView r5, android.widget.FrameLayout r6) {
        /*
            if (r5 == 0) goto L_0x0088
            if (r6 != 0) goto L_0x0006
            goto L_0x0088
        L_0x0006:
            android.content.Context r0 = r5.getContext()
            r1 = 0
            int r2 = com.google.android.setupdesign.util.PartnerStyleHelper.getLayoutGravity(r0)
            if (r2 == 0) goto L_0x0014
            setGravity(r5, r2)
        L_0x0014:
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r2 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r3 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_SIZE
            boolean r2 = r2.isPartnerConfigAvailable(r3)
            if (r2 == 0) goto L_0x005f
            checkImageType(r5)
            android.view.ViewGroup$LayoutParams r2 = r5.getLayoutParams()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r4 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            float r3 = r4.getDimension(r0, r3)
            int r3 = (int) r3
            r2.height = r3
            r3 = -2
            r2.width = r3
            android.widget.ImageView$ScaleType r3 = android.widget.ImageView.ScaleType.FIT_CENTER
            r5.setScaleType(r3)
            android.graphics.drawable.Drawable r5 = r5.getDrawable()
            if (r5 == 0) goto L_0x005f
            int r3 = r5.getIntrinsicWidth()
            int r5 = r5.getIntrinsicHeight()
            int r5 = r5 * 2
            if (r3 <= r5) goto L_0x005f
            android.content.res.Resources r5 = r0.getResources()
            int r3 = com.google.android.setupdesign.R$dimen.sud_horizontal_icon_height
            float r5 = r5.getDimension(r3)
            int r5 = (int) r5
            int r3 = r2.height
            if (r3 <= r5) goto L_0x005f
            int r1 = r3 - r5
            r2.height = r5
        L_0x005f:
            android.view.ViewGroup$LayoutParams r5 = r6.getLayoutParams()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r6 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r2 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_ICON_MARGIN_TOP
            boolean r6 = r6.isPartnerConfigAvailable(r2)
            if (r6 == 0) goto L_0x0088
            boolean r6 = r5 instanceof android.view.ViewGroup.MarginLayoutParams
            if (r6 == 0) goto L_0x0088
            android.view.ViewGroup$MarginLayoutParams r5 = (android.view.ViewGroup.MarginLayoutParams) r5
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r6 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            float r6 = r6.getDimension(r0, r2)
            int r6 = (int) r6
            int r6 = r6 + r1
            int r0 = r5.leftMargin
            int r1 = r5.rightMargin
            int r2 = r5.bottomMargin
            r5.setMargins(r0, r6, r1, r2)
        L_0x0088:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.util.HeaderAreaStyler.applyPartnerCustomizationIconStyle(android.widget.ImageView, android.widget.FrameLayout):void");
    }

    public static void checkImageType(final ImageView imageView) {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!BuildCompatUtils.isAtLeastS() || imageView.getDrawable() == null || (imageView.getDrawable() instanceof VectorDrawable) || (imageView.getDrawable() instanceof VectorDrawableCompat)) {
                    return true;
                }
                String str = Build.TYPE;
                if (!str.equals("userdebug") && !str.equals("eng")) {
                    return true;
                }
                Log.w("HeaderAreaStyler", HeaderAreaStyler.WARN_TO_USE_DRAWABLE + imageView.getContext().getPackageName());
                return true;
            }
        });
    }

    public static void setGravity(ImageView imageView, int i) {
        if (imageView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.gravity = i;
            imageView.setLayoutParams(layoutParams);
        }
    }
}
