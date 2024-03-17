package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.LocaleList;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConfigurationControllerImpl.kt */
public final class ConfigurationControllerImpl implements ConfigurationController {
    @NotNull
    public final Context context;
    public int density;
    public float fontScale;
    public final boolean inCarMode;
    @NotNull
    public final Configuration lastConfig = new Configuration();
    public int layoutDirection;
    @NotNull
    public final List<ConfigurationController.ConfigurationListener> listeners = new ArrayList();
    @Nullable
    public LocaleList localeList;
    @Nullable
    public Rect maxBounds;
    public int smallestScreenWidth;
    public int uiMode;

    public ConfigurationControllerImpl(@NotNull Context context2) {
        Configuration configuration = context2.getResources().getConfiguration();
        this.context = context2;
        this.fontScale = configuration.fontScale;
        this.density = configuration.densityDpi;
        this.smallestScreenWidth = configuration.smallestScreenWidthDp;
        int i = configuration.uiMode;
        this.inCarMode = (i & 15) == 3;
        this.uiMode = i & 48;
        this.localeList = configuration.getLocales();
        this.layoutDirection = configuration.getLayoutDirection();
    }

    public void notifyThemeChanged() {
        for (ConfigurationController.ConfigurationListener configurationListener : new ArrayList(this.listeners)) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0049, code lost:
        if (r4 == false) goto L_0x006b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:110:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0150  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigurationChanged(@org.jetbrains.annotations.NotNull android.content.res.Configuration r11) {
        /*
            r10 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r10.listeners
            java.util.Collection r1 = (java.util.Collection) r1
            r0.<init>(r1)
            java.util.Iterator r1 = r0.iterator()
        L_0x000d:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0025
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x000d
            r2.onConfigChanged(r11)
            goto L_0x000d
        L_0x0025:
            float r1 = r11.fontScale
            int r2 = r11.densityDpi
            int r3 = r11.uiMode
            r3 = r3 & 48
            int r4 = r10.uiMode
            r5 = 0
            r6 = 1
            if (r3 == r4) goto L_0x0035
            r4 = r6
            goto L_0x0036
        L_0x0035:
            r4 = r5
        L_0x0036:
            int r7 = r10.density
            if (r2 != r7) goto L_0x004b
            float r7 = r10.fontScale
            int r7 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r7 != 0) goto L_0x0042
            r7 = r6
            goto L_0x0043
        L_0x0042:
            r7 = r5
        L_0x0043:
            if (r7 == 0) goto L_0x004b
            boolean r7 = r10.inCarMode
            if (r7 == 0) goto L_0x006b
            if (r4 == 0) goto L_0x006b
        L_0x004b:
            java.util.Iterator r7 = r0.iterator()
        L_0x004f:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x0067
            java.lang.Object r8 = r7.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r8 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r8
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r9 = r10.listeners
            boolean r9 = r9.contains(r8)
            if (r9 == 0) goto L_0x004f
            r8.onDensityOrFontScaleChanged()
            goto L_0x004f
        L_0x0067:
            r10.density = r2
            r10.fontScale = r1
        L_0x006b:
            int r1 = r11.smallestScreenWidthDp
            int r2 = r10.smallestScreenWidth
            if (r1 == r2) goto L_0x008f
            r10.smallestScreenWidth = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x0077:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x008f
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x0077
            r2.onSmallestScreenWidthChanged()
            goto L_0x0077
        L_0x008f:
            android.app.WindowConfiguration r1 = r11.windowConfiguration
            android.graphics.Rect r1 = r1.getMaxBounds()
            android.graphics.Rect r2 = r10.maxBounds
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00bb
            r10.maxBounds = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00a3:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00bb
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x00a3
            r2.onMaxBoundsChanged()
            goto L_0x00a3
        L_0x00bb:
            android.os.LocaleList r1 = r11.getLocales()
            android.os.LocaleList r2 = r10.localeList
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1, (java.lang.Object) r2)
            if (r2 != 0) goto L_0x00e5
            r10.localeList = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x00cd:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00e5
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r7 = r10.listeners
            boolean r7 = r7.contains(r2)
            if (r7 == 0) goto L_0x00cd
            r2.onLocaleListChanged()
            goto L_0x00cd
        L_0x00e5:
            if (r4 == 0) goto L_0x0114
            android.content.Context r1 = r10.context
            android.content.res.Resources$Theme r1 = r1.getTheme()
            android.content.Context r2 = r10.context
            int r2 = r2.getThemeResId()
            r1.applyStyle(r2, r6)
            r10.uiMode = r3
            java.util.Iterator r1 = r0.iterator()
        L_0x00fc:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0114
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x00fc
            r2.onUiModeChanged()
            goto L_0x00fc
        L_0x0114:
            int r1 = r10.layoutDirection
            int r2 = r11.getLayoutDirection()
            if (r1 == r2) goto L_0x0145
            int r1 = r11.getLayoutDirection()
            r10.layoutDirection = r1
            java.util.Iterator r1 = r0.iterator()
        L_0x0126:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x0145
            java.lang.Object r2 = r1.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r2 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r2
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r3 = r10.listeners
            boolean r3 = r3.contains(r2)
            if (r3 == 0) goto L_0x0126
            int r3 = r10.layoutDirection
            if (r3 != r6) goto L_0x0140
            r3 = r6
            goto L_0x0141
        L_0x0140:
            r3 = r5
        L_0x0141:
            r2.onLayoutDirectionChanged(r3)
            goto L_0x0126
        L_0x0145:
            android.content.res.Configuration r1 = r10.lastConfig
            int r11 = r1.updateFrom(r11)
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r11 = r11 & r1
            if (r11 == 0) goto L_0x016c
            java.util.Iterator r11 = r0.iterator()
        L_0x0154:
            boolean r0 = r11.hasNext()
            if (r0 == 0) goto L_0x016c
            java.lang.Object r0 = r11.next()
            com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener r0 = (com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener) r0
            java.util.List<com.android.systemui.statusbar.policy.ConfigurationController$ConfigurationListener> r1 = r10.listeners
            boolean r1 = r1.contains(r0)
            if (r1 == 0) goto L_0x0154
            r0.onThemeChanged()
            goto L_0x0154
        L_0x016c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ConfigurationControllerImpl.onConfigurationChanged(android.content.res.Configuration):void");
    }

    public void addCallback(@NotNull ConfigurationController.ConfigurationListener configurationListener) {
        this.listeners.add(configurationListener);
        configurationListener.onDensityOrFontScaleChanged();
    }

    public void removeCallback(@NotNull ConfigurationController.ConfigurationListener configurationListener) {
        this.listeners.remove(configurationListener);
    }

    public boolean isLayoutRtl() {
        return this.layoutDirection == 1;
    }
}
