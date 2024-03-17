package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.os.BuildCompat;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Calendar;
import java.util.function.Supplier;

public class IconProvider {
    public static final boolean ATLEAST_T = BuildCompat.isAtLeastT();
    public static final int CONFIG_ICON_MASK_RES_ID = Resources.getSystem().getIdentifier("config_icon_mask", "string", ThemeOverlayApplier.ANDROID_PACKAGE);
    public final String ACTION_OVERLAY_CHANGED = "android.intent.action.OVERLAY_CHANGED";
    public final ComponentName mCalendar;
    public final ComponentName mClock;
    public final Context mContext;

    public ThemeData getThemeDataForPackage(String str) {
        return null;
    }

    public IconProvider(Context context) {
        this.mContext = context;
        this.mCalendar = parseComponentOrNull(context, R$string.calendar_component_name);
        this.mClock = parseComponentOrNull(context, R$string.clock_component_name);
    }

    public Drawable getIcon(ActivityInfo activityInfo) {
        return getIcon(activityInfo, this.mContext.getResources().getConfiguration().densityDpi);
    }

    public Drawable getIcon(ActivityInfo activityInfo, int i) {
        return getIconWithOverrides(activityInfo.applicationInfo.packageName, i, new IconProvider$$ExternalSyntheticLambda0(this, activityInfo, i));
    }

    @TargetApi(33)
    public final Drawable getIconWithOverrides(String str, int i, Supplier<Drawable> supplier) {
        Drawable drawable;
        ThemeData themeDataForPackage = getThemeDataForPackage(str);
        ComponentName componentName = this.mCalendar;
        if (componentName == null || !componentName.getPackageName().equals(str)) {
            ComponentName componentName2 = this.mClock;
            drawable = (componentName2 == null || !componentName2.getPackageName().equals(str)) ? null : ClockDrawableWrapper.forPackage(this.mContext, this.mClock.getPackageName(), i, themeDataForPackage);
        } else {
            drawable = loadCalendarDrawable(i, themeDataForPackage);
        }
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = supplier.get();
        if (!ATLEAST_T || !(drawable2 instanceof AdaptiveIconDrawable) || themeDataForPackage == null) {
            return drawable2;
        }
        AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable2;
        return adaptiveIconDrawable.getMonochrome() == null ? new AdaptiveIconDrawable(adaptiveIconDrawable.getBackground(), adaptiveIconDrawable.getForeground(), themeDataForPackage.loadPaddedDrawable()) : drawable2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001c  */
    /* renamed from: loadActivityInfoIcon */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.drawable.Drawable lambda$getIcon$1(android.content.pm.ActivityInfo r4, int r5) {
        /*
            r3 = this;
            int r0 = r4.getIconResource()
            if (r5 == 0) goto L_0x0019
            if (r0 == 0) goto L_0x0019
            android.content.Context r1 = r3.mContext     // Catch:{ NameNotFoundException | NotFoundException -> 0x0019 }
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch:{ NameNotFoundException | NotFoundException -> 0x0019 }
            android.content.pm.ApplicationInfo r2 = r4.applicationInfo     // Catch:{ NameNotFoundException | NotFoundException -> 0x0019 }
            android.content.res.Resources r1 = r1.getResourcesForApplication(r2)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0019 }
            android.graphics.drawable.Drawable r5 = r1.getDrawableForDensity(r0, r5)     // Catch:{ NameNotFoundException | NotFoundException -> 0x0019 }
            goto L_0x001a
        L_0x0019:
            r5 = 0
        L_0x001a:
            if (r5 != 0) goto L_0x0026
            android.content.Context r3 = r3.mContext
            android.content.pm.PackageManager r3 = r3.getPackageManager()
            android.graphics.drawable.Drawable r5 = r4.loadIcon(r3)
        L_0x0026:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconProvider.lambda$getIcon$1(android.content.pm.ActivityInfo, int):android.graphics.drawable.Drawable");
    }

    @TargetApi(33)
    public final Drawable loadCalendarDrawable(int i, ThemeData themeData) {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            Bundle bundle = packageManager.getActivityInfo(this.mCalendar, 8320).metaData;
            Resources resourcesForApplication = packageManager.getResourcesForApplication(this.mCalendar.getPackageName());
            int dynamicIconId = getDynamicIconId(bundle, resourcesForApplication);
            if (dynamicIconId != 0) {
                Drawable drawableForDensity = resourcesForApplication.getDrawableForDensity(dynamicIconId, i, (Resources.Theme) null);
                if (!ATLEAST_T || !(drawableForDensity instanceof AdaptiveIconDrawable) || themeData == null) {
                    return drawableForDensity;
                }
                AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawableForDensity;
                if (adaptiveIconDrawable.getMonochrome() != null || !"array".equals(themeData.mResources.getResourceTypeName(themeData.mResID))) {
                    return drawableForDensity;
                }
                TypedArray obtainTypedArray = themeData.mResources.obtainTypedArray(themeData.mResID);
                int resourceId = obtainTypedArray.getResourceId(getDay(), 0);
                obtainTypedArray.recycle();
                return resourceId == 0 ? drawableForDensity : new AdaptiveIconDrawable(adaptiveIconDrawable.getBackground(), adaptiveIconDrawable.getForeground(), new ThemeData(themeData.mResources, resourceId).loadPaddedDrawable());
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return null;
    }

    public final int getDynamicIconId(Bundle bundle, Resources resources) {
        if (bundle == null) {
            return 0;
        }
        int i = bundle.getInt(this.mCalendar.getPackageName() + ".dynamic_icons", 0);
        if (i == 0) {
            return 0;
        }
        try {
            return resources.obtainTypedArray(i).getResourceId(getDay(), 0);
        } catch (Resources.NotFoundException unused) {
            return 0;
        }
    }

    public static int getDay() {
        return Calendar.getInstance().get(5) - 1;
    }

    public static ComponentName parseComponentOrNull(Context context, int i) {
        String string = context.getString(i);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return ComponentName.unflattenFromString(string);
    }

    public static class ThemeData {
        public final int mResID;
        public final Resources mResources;

        public ThemeData(Resources resources, int i) {
            this.mResources = resources;
            this.mResID = i;
        }

        public Drawable loadPaddedDrawable() {
            if (!"drawable".equals(this.mResources.getResourceTypeName(this.mResID))) {
                return null;
            }
            return new InsetDrawable(new InsetDrawable(this.mResources.getDrawable(this.mResID).mutate(), 0.2f), AdaptiveIconDrawable.getExtraInsetFraction() / ((AdaptiveIconDrawable.getExtraInsetFraction() * 2.0f) + 1.0f));
        }
    }
}
