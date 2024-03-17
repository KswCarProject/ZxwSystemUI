package com.android.systemui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.View;
import com.android.systemui.RegionInterceptingFrameLayout;
import com.android.systemui.animation.Interpolators;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DisplayCutoutBaseView.kt */
public class DisplayCutoutBaseView extends View implements RegionInterceptingFrameLayout.RegionInterceptableView {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public ValueAnimator cameraProtectionAnimator;
    public float cameraProtectionProgress;
    @NotNull
    public final Path cutoutPath;
    @NotNull
    public final DisplayInfo displayInfo;
    @Nullable
    public Display.Mode displayMode;
    public int displayRotation;
    @Nullable
    public String displayUniqueId;
    @NotNull
    public final int[] location;
    @NotNull
    public final Paint paint;
    public boolean pendingConfigChange;
    @NotNull
    public final Path protectionPath;
    @NotNull
    public final Path protectionPathOrig;
    @NotNull
    public final RectF protectionRect;
    @NotNull
    public final RectF protectionRectOrig;
    public boolean shouldDrawCutout;
    public boolean showProtection;

    public static /* synthetic */ void getCameraProtectionProgress$annotations() {
    }

    public static /* synthetic */ void getDisplayInfo$annotations() {
    }

    public static /* synthetic */ void getProtectionPath$annotations() {
    }

    public static /* synthetic */ void getProtectionRect$annotations() {
    }

    public static final void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, @NotNull Matrix matrix) {
        Companion.transformPhysicalToLogicalCoordinates(i, i2, i3, matrix);
    }

    public void onUpdate() {
    }

    public final int getDisplayRotation() {
        return this.displayRotation;
    }

    public final float getCameraProtectionProgress() {
        return this.cameraProtectionProgress;
    }

    public final void setCameraProtectionProgress(float f) {
        this.cameraProtectionProgress = f;
    }

    public DisplayCutoutBaseView(@NotNull Context context) {
        super(context);
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display == null ? null : display.getUniqueId());
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    public DisplayCutoutBaseView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = getContext().getResources();
        Display display = getContext().getDisplay();
        this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(resources, display == null ? null : display.getUniqueId());
        this.location = new int[2];
        this.displayInfo = new DisplayInfo();
        this.paint = new Paint();
        this.cutoutPath = new Path();
        this.protectionRect = new RectF();
        this.protectionPath = new Path();
        this.protectionRectOrig = new RectF();
        this.protectionPathOrig = new Path();
        this.cameraProtectionProgress = 0.5f;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Display display = getContext().getDisplay();
        this.displayUniqueId = display == null ? null : display.getUniqueId();
        updateCutout();
        updateProtectionBoundingPath();
        onUpdate();
    }

    public final void onDisplayChanged(int i) {
        Display.Mode mode;
        String str;
        Display.Mode mode2 = this.displayMode;
        Display display = getContext().getDisplay();
        DisplayCutout displayCutout = null;
        if (display == null) {
            mode = null;
        } else {
            mode = display.getMode();
        }
        this.displayMode = mode;
        if (!Intrinsics.areEqual((Object) this.displayUniqueId, (Object) display == null ? null : display.getUniqueId())) {
            if (display == null) {
                str = null;
            } else {
                str = display.getUniqueId();
            }
            this.displayUniqueId = str;
            this.shouldDrawCutout = DisplayCutout.getFillBuiltInDisplayCutout(getContext().getResources(), this.displayUniqueId);
        }
        if (!displayModeChanged(mode2, this.displayMode)) {
            if (display != null) {
                displayCutout = display.getCutout();
            }
            if (Intrinsics.areEqual((Object) displayCutout, (Object) this.displayInfo.displayCutout)) {
                return;
            }
        }
        boolean z = false;
        if (display != null && i == display.getDisplayId()) {
            z = true;
        }
        if (z) {
            updateCutout();
            updateProtectionBoundingPath();
            onUpdate();
        }
    }

    public void updateRotation(int i) {
        this.displayRotation = i;
        updateCutout();
        updateProtectionBoundingPath();
        onUpdate();
    }

    public void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        if (this.shouldDrawCutout) {
            canvas.save();
            getLocationOnScreen(this.location);
            int[] iArr = this.location;
            canvas.translate(-((float) iArr[0]), -((float) iArr[1]));
            drawCutouts(canvas);
            drawCutoutProtection(canvas);
            canvas.restore();
        }
    }

    public boolean shouldInterceptTouch() {
        return this.displayInfo.displayCutout != null && getVisibility() == 0 && this.shouldDrawCutout;
    }

    @Nullable
    public Region getInterceptRegion() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        List<Rect> list = null;
        if (displayCutout == null) {
            return null;
        }
        if (displayCutout != null) {
            list = displayCutout.getBoundingRects();
        }
        Region rectsToRegion = rectsToRegion(list);
        getRootView().getLocationOnScreen(this.location);
        int[] iArr = this.location;
        rectsToRegion.translate(-iArr[0], -iArr[1]);
        rectsToRegion.op(getRootView().getLeft(), getRootView().getTop(), getRootView().getRight(), getRootView().getBottom(), Region.Op.INTERSECT);
        return rectsToRegion;
    }

    public void updateCutout() {
        Path cutoutPath2;
        if (!this.pendingConfigChange) {
            this.cutoutPath.reset();
            getDisplay().getDisplayInfo(this.displayInfo);
            DisplayCutout displayCutout = this.displayInfo.displayCutout;
            if (!(displayCutout == null || (cutoutPath2 = displayCutout.getCutoutPath()) == null)) {
                this.cutoutPath.set(cutoutPath2);
            }
            invalidate();
        }
    }

    public void drawCutouts(@NotNull Canvas canvas) {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if ((displayCutout == null ? null : displayCutout.getCutoutPath()) != null) {
            canvas.drawPath(this.cutoutPath, this.paint);
        }
    }

    public void drawCutoutProtection(@NotNull Canvas canvas) {
        if (this.cameraProtectionProgress > 0.5f && !this.protectionRect.isEmpty()) {
            float f = this.cameraProtectionProgress;
            canvas.scale(f, f, this.protectionRect.centerX(), this.protectionRect.centerY());
            canvas.drawPath(this.protectionPath, this.paint);
        }
    }

    @NotNull
    public final Region rectsToRegion(@Nullable List<Rect> list) {
        Region obtain = Region.obtain();
        if (list != null) {
            for (Rect next : list) {
                if (next != null && !next.isEmpty()) {
                    obtain.op(next, Region.Op.UNION);
                }
            }
        }
        return obtain;
    }

    public void enableShowProtection(boolean z) {
        if (this.showProtection != z) {
            this.showProtection = z;
            updateProtectionBoundingPath();
            if (this.showProtection) {
                requestLayout();
            }
            ValueAnimator valueAnimator = this.cameraProtectionAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.cameraProtectionProgress;
            fArr[1] = this.showProtection ? 1.0f : 0.5f;
            ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(750);
            this.cameraProtectionAnimator = duration;
            if (duration != null) {
                duration.setInterpolator(Interpolators.DECELERATE_QUINT);
            }
            ValueAnimator valueAnimator2 = this.cameraProtectionAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.addUpdateListener(new DisplayCutoutBaseView$enableShowProtection$1(this));
            }
            ValueAnimator valueAnimator3 = this.cameraProtectionAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.addListener(new DisplayCutoutBaseView$enableShowProtection$2(this));
            }
            ValueAnimator valueAnimator4 = this.cameraProtectionAnimator;
            if (valueAnimator4 != null) {
                valueAnimator4.start();
            }
        }
    }

    public void setProtection(@NotNull Path path, @NotNull Rect rect) {
        this.protectionPathOrig.reset();
        this.protectionPathOrig.set(path);
        this.protectionPath.reset();
        this.protectionRectOrig.setEmpty();
        this.protectionRectOrig.set(rect);
        this.protectionRect.setEmpty();
    }

    public void updateProtectionBoundingPath() {
        if (!this.pendingConfigChange) {
            Matrix matrix = new Matrix();
            float physicalPixelDisplaySizeRatio = getPhysicalPixelDisplaySizeRatio();
            matrix.postScale(physicalPixelDisplaySizeRatio, physicalPixelDisplaySizeRatio);
            DisplayInfo displayInfo2 = this.displayInfo;
            int i = displayInfo2.logicalWidth;
            int i2 = displayInfo2.logicalHeight;
            int i3 = displayInfo2.rotation;
            boolean z = true;
            if (!(i3 == 1 || i3 == 3)) {
                z = false;
            }
            int i4 = z ? i2 : i;
            if (!z) {
                i = i2;
            }
            transformPhysicalToLogicalCoordinates(i3, i4, i, matrix);
            if (!this.protectionPathOrig.isEmpty()) {
                this.protectionPath.set(this.protectionPathOrig);
                this.protectionPath.transform(matrix);
                matrix.mapRect(this.protectionRect, this.protectionRectOrig);
            }
        }
    }

    public float getPhysicalPixelDisplaySizeRatio() {
        DisplayCutout displayCutout = this.displayInfo.displayCutout;
        if (displayCutout == null) {
            return 1.0f;
        }
        return displayCutout.getCutoutPathParserInfo().getPhysicalPixelDisplaySizeRatio();
    }

    public final boolean displayModeChanged(Display.Mode mode, Display.Mode mode2) {
        if (mode == null) {
            return true;
        }
        Integer num = null;
        if (!Intrinsics.areEqual((Object) Integer.valueOf(mode.getPhysicalHeight()), (Object) mode2 == null ? null : Integer.valueOf(mode2.getPhysicalHeight()))) {
            return true;
        }
        Integer valueOf = Integer.valueOf(mode.getPhysicalWidth());
        if (mode2 != null) {
            num = Integer.valueOf(mode2.getPhysicalWidth());
        }
        if (!Intrinsics.areEqual((Object) valueOf, (Object) num)) {
            return true;
        }
        return false;
    }

    /* compiled from: DisplayCutoutBaseView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final void transformPhysicalToLogicalCoordinates(int i, int i2, int i3, @NotNull Matrix matrix) {
            if (i == 0) {
                return;
            }
            if (i == 1) {
                matrix.postRotate(270.0f);
                matrix.postTranslate(0.0f, (float) i2);
            } else if (i == 2) {
                matrix.postRotate(180.0f);
                matrix.postTranslate((float) i2, (float) i3);
            } else if (i == 3) {
                matrix.postRotate(90.0f);
                matrix.postTranslate((float) i3, 0.0f);
            } else {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unknown rotation: ", Integer.valueOf(i)));
            }
        }
    }
}
