package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Trace;
import android.util.LruCache;
import android.util.Pair;
import android.view.DisplayCutout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarContentInsetsProvider.kt */
public final class StatusBarContentInsetsProvider implements CallbackController<StatusBarContentInsetsChangedListener>, ConfigurationController.ConfigurationListener, Dumpable {
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final Context context;
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final LruCache<CacheKey, Rect> insetsCache = new LruCache<>(16);
    @NotNull
    public final Lazy isPrivacyDotEnabled$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.PUBLICATION, new StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(this));
    @NotNull
    public final Set<StatusBarContentInsetsChangedListener> listeners = new LinkedHashSet();

    @NotNull
    public final Pair<Integer, Integer> getStatusBarContentInsetsForRotation(int i) {
        Trace.beginSection("StatusBarContentInsetsProvider.getStatusBarContentInsetsForRotation");
        try {
            DisplayCutout cutout = getContext().getDisplay().getCutout();
            CacheKey cacheKey = getCacheKey(i, cutout);
            Rect maxBounds = getContext().getResources().getConfiguration().windowConfiguration.getMaxBounds();
            Point point = new Point(maxBounds.width(), maxBounds.height());
            StatusBarContentInsetsProviderKt.orientToRotZero(point, RotationUtils.getExactRotation(getContext()));
            int access$logicalWidth = StatusBarContentInsetsProviderKt.logicalWidth(point, i);
            Rect rect = this.insetsCache.get(cacheKey);
            if (rect == null) {
                rect = getAndSetCalculatedAreaForRotation(i, cutout, RotationUtils.getResourcesForRotation(i, getContext()), cacheKey);
            }
            return new Pair<>(Integer.valueOf(rect.left), Integer.valueOf(access$logicalWidth - rect.right));
        } finally {
            Trace.endSection();
        }
    }

    public StatusBarContentInsetsProvider(@NotNull Context context2, @NotNull ConfigurationController configurationController2, @NotNull DumpManager dumpManager2) {
        this.context = context2;
        this.configurationController = configurationController2;
        this.dumpManager = dumpManager2;
        configurationController2.addCallback(this);
        dumpManager2.registerDumpable("StatusBarInsetsProvider", this);
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    public final boolean isPrivacyDotEnabled() {
        return ((Boolean) this.isPrivacyDotEnabled$delegate.getValue()).booleanValue();
    }

    public void addCallback(@NotNull StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        this.listeners.add(statusBarContentInsetsChangedListener);
    }

    public void removeCallback(@NotNull StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        this.listeners.remove(statusBarContentInsetsChangedListener);
    }

    public void onDensityOrFontScaleChanged() {
        clearCachedInsets();
    }

    public void onThemeChanged() {
        clearCachedInsets();
    }

    public void onMaxBoundsChanged() {
        notifyInsetsChanged();
    }

    public final void clearCachedInsets() {
        this.insetsCache.evictAll();
        notifyInsetsChanged();
    }

    public final void notifyInsetsChanged() {
        for (StatusBarContentInsetsChangedListener onStatusBarContentInsetsChanged : this.listeners) {
            onStatusBarContentInsetsChanged.onStatusBarContentInsetsChanged();
        }
    }

    public final boolean currentRotationHasCornerCutout() {
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        if (cutout == null) {
            return false;
        }
        Rect boundingRectTop = cutout.getBoundingRectTop();
        Point point = new Point();
        this.context.getDisplay().getRealSize(point);
        if (boundingRectTop.left <= 0 || boundingRectTop.right >= point.y) {
            return true;
        }
        return false;
    }

    @NotNull
    public final Rect getBoundingRectForPrivacyChipForRotation(int i, @Nullable DisplayCutout displayCutout) {
        Rect rect = this.insetsCache.get(getCacheKey(i, displayCutout));
        if (rect == null) {
            rect = getStatusBarContentAreaForRotation(i);
        }
        Resources resourcesForRotation = RotationUtils.getResourcesForRotation(i, this.context);
        return StatusBarContentInsetsProviderKt.getPrivacyChipBoundingRectForInsets(rect, resourcesForRotation.getDimensionPixelSize(R$dimen.ongoing_appops_dot_diameter), resourcesForRotation.getDimensionPixelSize(R$dimen.ongoing_appops_chip_max_width), this.configurationController.isLayoutRtl());
    }

    @NotNull
    public final Pair<Integer, Integer> getStatusBarContentInsetsForCurrentRotation() {
        return getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(this.context));
    }

    @NotNull
    public final Rect getStatusBarContentAreaForRotation(int i) {
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        CacheKey cacheKey = getCacheKey(i, cutout);
        Rect rect = this.insetsCache.get(cacheKey);
        return rect == null ? getAndSetCalculatedAreaForRotation(i, cutout, RotationUtils.getResourcesForRotation(i, this.context), cacheKey) : rect;
    }

    @NotNull
    public final Rect getStatusBarContentAreaForCurrentRotation() {
        return getStatusBarContentAreaForRotation(RotationUtils.getExactRotation(this.context));
    }

    public final Rect getAndSetCalculatedAreaForRotation(int i, DisplayCutout displayCutout, Resources resources, CacheKey cacheKey) {
        Rect calculatedAreaForRotation = getCalculatedAreaForRotation(displayCutout, i, resources);
        this.insetsCache.put(cacheKey, calculatedAreaForRotation);
        return calculatedAreaForRotation;
    }

    public final Rect getCalculatedAreaForRotation(DisplayCutout displayCutout, int i, Resources resources) {
        int i2;
        int i3;
        int exactRotation = RotationUtils.getExactRotation(this.context);
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
        int dimensionPixelSize2 = isPrivacyDotEnabled() ? resources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_min_padding) : 0;
        int dimensionPixelSize3 = isPrivacyDotEnabled() ? resources.getDimensionPixelSize(R$dimen.ongoing_appops_dot_diameter) : 0;
        if (this.configurationController.isLayoutRtl()) {
            i3 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i2 = dimensionPixelSize;
        } else {
            i2 = Math.max(dimensionPixelSize2, dimensionPixelSize);
            i3 = dimensionPixelSize;
        }
        return StatusBarContentInsetsProviderKt.calculateInsetsForRotationWithRotatedResources(exactRotation, i, displayCutout, this.context.getResources().getConfiguration().windowConfiguration.getMaxBounds(), SystemBarUtils.getStatusBarHeightForRotation(this.context, i), i3, i2, this.configurationController.isLayoutRtl(), dimensionPixelSize3);
    }

    public static /* synthetic */ int getStatusBarPaddingTop$default(StatusBarContentInsetsProvider statusBarContentInsetsProvider, Integer num, int i, Object obj) {
        if ((i & 1) != 0) {
            num = null;
        }
        return statusBarContentInsetsProvider.getStatusBarPaddingTop(num);
    }

    public final int getStatusBarPaddingTop(@Nullable Integer num) {
        Resources resourcesForRotation = num == null ? null : RotationUtils.getResourcesForRotation(num.intValue(), getContext());
        if (resourcesForRotation == null) {
            resourcesForRotation = this.context.getResources();
        }
        return resourcesForRotation.getDimensionPixelSize(R$dimen.status_bar_padding_top);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        for (Map.Entry next : this.insetsCache.snapshot().entrySet()) {
            printWriter.println(((CacheKey) next.getKey()) + " -> " + ((Rect) next.getValue()));
        }
        printWriter.println(this.insetsCache);
    }

    public final CacheKey getCacheKey(int i, DisplayCutout displayCutout) {
        return new CacheKey(i, new Rect(this.context.getResources().getConfiguration().windowConfiguration.getMaxBounds()), displayCutout);
    }

    /* compiled from: StatusBarContentInsetsProvider.kt */
    public static final class CacheKey {
        @Nullable
        public final DisplayCutout displayCutout;
        @NotNull
        public final Rect displaySize;
        public final int rotation;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CacheKey)) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) obj;
            return this.rotation == cacheKey.rotation && Intrinsics.areEqual((Object) this.displaySize, (Object) cacheKey.displaySize) && Intrinsics.areEqual((Object) this.displayCutout, (Object) cacheKey.displayCutout);
        }

        public int hashCode() {
            int hashCode = ((Integer.hashCode(this.rotation) * 31) + this.displaySize.hashCode()) * 31;
            DisplayCutout displayCutout2 = this.displayCutout;
            return hashCode + (displayCutout2 == null ? 0 : displayCutout2.hashCode());
        }

        @NotNull
        public String toString() {
            return "CacheKey(rotation=" + this.rotation + ", displaySize=" + this.displaySize + ", displayCutout=" + this.displayCutout + ')';
        }

        public CacheKey(int i, @NotNull Rect rect, @Nullable DisplayCutout displayCutout2) {
            this.rotation = i;
            this.displaySize = rect;
            this.displayCutout = displayCutout2;
        }
    }
}
