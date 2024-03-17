package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.RotationUtils;
import android.util.Size;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.InsetsSource;
import android.view.InsetsState;
import com.android.internal.policy.SystemBarUtils;
import java.util.Objects;

public class DisplayLayout {
    public boolean mAllowSeamlessRotationDespiteNavBarMoving = false;
    public DisplayCutout mCutout;
    public int mDensityDpi;
    public boolean mHasNavigationBar = false;
    public boolean mHasStatusBar = false;
    public int mHeight;
    public InsetsState mInsetsState = new InsetsState();
    public int mNavBarFrameHeight = 0;
    public boolean mNavigationBarCanMove = false;
    public final Rect mNonDecorInsets = new Rect();
    public boolean mReverseDefaultRotation = false;
    public int mRotation;
    public final Rect mStableInsets = new Rect();
    public int mUiMode;
    public int mWidth;

    public static int getBoundIndexFromRotation(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    public static boolean hasStatusBar(int i) {
        return i == 0;
    }

    public boolean isSameGeometry(DisplayLayout displayLayout) {
        return this.mWidth == displayLayout.mWidth && this.mHeight == displayLayout.mHeight && this.mRotation == displayLayout.mRotation && this.mDensityDpi == displayLayout.mDensityDpi && Objects.equals(this.mCutout, displayLayout.mCutout);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DisplayLayout)) {
            return false;
        }
        DisplayLayout displayLayout = (DisplayLayout) obj;
        if (this.mUiMode == displayLayout.mUiMode && this.mWidth == displayLayout.mWidth && this.mHeight == displayLayout.mHeight && Objects.equals(this.mCutout, displayLayout.mCutout) && this.mRotation == displayLayout.mRotation && this.mDensityDpi == displayLayout.mDensityDpi && Objects.equals(this.mNonDecorInsets, displayLayout.mNonDecorInsets) && Objects.equals(this.mStableInsets, displayLayout.mStableInsets) && this.mHasNavigationBar == displayLayout.mHasNavigationBar && this.mHasStatusBar == displayLayout.mHasStatusBar && this.mAllowSeamlessRotationDespiteNavBarMoving == displayLayout.mAllowSeamlessRotationDespiteNavBarMoving && this.mNavigationBarCanMove == displayLayout.mNavigationBarCanMove && this.mReverseDefaultRotation == displayLayout.mReverseDefaultRotation && this.mNavBarFrameHeight == displayLayout.mNavBarFrameHeight && Objects.equals(this.mInsetsState, displayLayout.mInsetsState)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mUiMode), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), this.mCutout, Integer.valueOf(this.mRotation), Integer.valueOf(this.mDensityDpi), this.mNonDecorInsets, this.mStableInsets, Boolean.valueOf(this.mHasNavigationBar), Boolean.valueOf(this.mHasStatusBar), Integer.valueOf(this.mNavBarFrameHeight), Boolean.valueOf(this.mAllowSeamlessRotationDespiteNavBarMoving), Boolean.valueOf(this.mNavigationBarCanMove), Boolean.valueOf(this.mReverseDefaultRotation), this.mInsetsState});
    }

    public DisplayLayout() {
    }

    public DisplayLayout(Context context, Display display) {
        int displayId = display.getDisplayId();
        DisplayInfo displayInfo = new DisplayInfo();
        display.getDisplayInfo(displayInfo);
        init(displayInfo, context.getResources(), hasNavigationBar(displayInfo, context, displayId), hasStatusBar(displayId));
    }

    public DisplayLayout(DisplayLayout displayLayout) {
        set(displayLayout);
    }

    public void set(DisplayLayout displayLayout) {
        this.mUiMode = displayLayout.mUiMode;
        this.mWidth = displayLayout.mWidth;
        this.mHeight = displayLayout.mHeight;
        this.mCutout = displayLayout.mCutout;
        this.mRotation = displayLayout.mRotation;
        this.mDensityDpi = displayLayout.mDensityDpi;
        this.mHasNavigationBar = displayLayout.mHasNavigationBar;
        this.mHasStatusBar = displayLayout.mHasStatusBar;
        this.mAllowSeamlessRotationDespiteNavBarMoving = displayLayout.mAllowSeamlessRotationDespiteNavBarMoving;
        this.mNavigationBarCanMove = displayLayout.mNavigationBarCanMove;
        this.mReverseDefaultRotation = displayLayout.mReverseDefaultRotation;
        this.mNavBarFrameHeight = displayLayout.mNavBarFrameHeight;
        this.mNonDecorInsets.set(displayLayout.mNonDecorInsets);
        this.mStableInsets.set(displayLayout.mStableInsets);
        this.mInsetsState.set(displayLayout.mInsetsState, true);
    }

    public final void init(DisplayInfo displayInfo, Resources resources, boolean z, boolean z2) {
        this.mUiMode = resources.getConfiguration().uiMode;
        this.mWidth = displayInfo.logicalWidth;
        this.mHeight = displayInfo.logicalHeight;
        this.mRotation = displayInfo.rotation;
        this.mCutout = displayInfo.displayCutout;
        this.mDensityDpi = displayInfo.logicalDensityDpi;
        this.mHasNavigationBar = z;
        this.mHasStatusBar = z2;
        this.mAllowSeamlessRotationDespiteNavBarMoving = resources.getBoolean(17891351);
        this.mNavigationBarCanMove = resources.getBoolean(17891710);
        this.mReverseDefaultRotation = resources.getBoolean(17891736);
        recalcInsets(resources);
    }

    public void setInsets(Resources resources, InsetsState insetsState) {
        this.mInsetsState = insetsState;
        recalcInsets(resources);
    }

    public void recalcInsets(Resources resources) {
        computeNonDecorInsets(resources, this.mRotation, this.mWidth, this.mHeight, this.mCutout, this.mInsetsState, this.mUiMode, this.mNonDecorInsets, this.mHasNavigationBar);
        this.mStableInsets.set(this.mNonDecorInsets);
        boolean z = this.mHasStatusBar;
        if (z) {
            convertNonDecorInsetsToStableInsets(resources, this.mStableInsets, this.mCutout, z);
        }
        this.mNavBarFrameHeight = getNavigationBarFrameHeight(resources, this.mWidth > this.mHeight);
    }

    public void rotateTo(Resources resources, int i) {
        int i2 = ((i - this.mRotation) + 4) % 4;
        boolean z = i2 % 2 != 0;
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        this.mRotation = i;
        if (z) {
            this.mWidth = i4;
            this.mHeight = i3;
        }
        DisplayCutout displayCutout = this.mCutout;
        if (displayCutout != null && !displayCutout.isEmpty()) {
            this.mCutout = calculateDisplayCutoutForRotation(this.mCutout, i2, i3, i4);
        }
        recalcInsets(resources);
    }

    public Rect nonDecorInsets() {
        return this.mNonDecorInsets;
    }

    public Rect stableInsets() {
        return this.mStableInsets;
    }

    public int width() {
        return this.mWidth;
    }

    public int height() {
        return this.mHeight;
    }

    public int rotation() {
        return this.mRotation;
    }

    public float density() {
        return ((float) this.mDensityDpi) * 0.00625f;
    }

    public boolean isLandscape() {
        return this.mWidth > this.mHeight;
    }

    public int navBarFrameHeight() {
        return this.mNavBarFrameHeight;
    }

    public boolean allowSeamlessRotationDespiteNavBarMoving() {
        return this.mAllowSeamlessRotationDespiteNavBarMoving;
    }

    public boolean navigationBarCanMove() {
        return this.mNavigationBarCanMove;
    }

    public int getUpsideDownRotation() {
        boolean z = this.mWidth > this.mHeight;
        if (this.mRotation % 2 != 0) {
            z = !z;
        }
        if (!z) {
            return 2;
        }
        if (this.mReverseDefaultRotation) {
            return 3;
        }
        return 1;
    }

    public int getOrientation() {
        return this.mWidth > this.mHeight ? 2 : 1;
    }

    public void getStableBounds(Rect rect) {
        rect.set(0, 0, this.mWidth, this.mHeight);
        rect.inset(this.mStableInsets);
    }

    public int getNavigationBarPosition(Resources resources) {
        return navigationBarPosition(resources, this.mWidth, this.mHeight, this.mRotation);
    }

    public DisplayCutout getDisplayCutout() {
        return this.mCutout;
    }

    public final void convertNonDecorInsetsToStableInsets(Resources resources, Rect rect, DisplayCutout displayCutout, boolean z) {
        if (z) {
            rect.top = Math.max(rect.top, SystemBarUtils.getStatusBarHeight(resources, displayCutout));
        }
    }

    public static void computeNonDecorInsets(Resources resources, int i, int i2, int i3, DisplayCutout displayCutout, InsetsState insetsState, int i4, Rect rect, boolean z) {
        rect.setEmpty();
        if (z) {
            InsetsSource source = insetsState.getSource(21);
            boolean z2 = false;
            boolean z3 = source != null && source.isVisible();
            int navigationBarPosition = navigationBarPosition(resources, i2, i3, i);
            if (i2 > i3) {
                z2 = true;
            }
            int navigationBarSize = getNavigationBarSize(resources, navigationBarPosition, z2, i4);
            if (navigationBarPosition == 4) {
                if (z3) {
                    navigationBarSize = Math.max(navigationBarSize, source.getFrame().height());
                }
                rect.bottom = navigationBarSize;
            } else if (navigationBarPosition == 2) {
                if (z3) {
                    navigationBarSize = Math.max(navigationBarSize, source.getFrame().width());
                }
                rect.right = navigationBarSize;
            } else if (navigationBarPosition == 1) {
                if (z3) {
                    navigationBarSize = Math.max(navigationBarSize, source.getFrame().width());
                }
                rect.left = navigationBarSize;
            }
        }
        if (displayCutout != null) {
            rect.left += displayCutout.getSafeInsetLeft();
            rect.top += displayCutout.getSafeInsetTop();
            rect.right += displayCutout.getSafeInsetRight();
            rect.bottom += displayCutout.getSafeInsetBottom();
        }
    }

    public static DisplayCutout calculateDisplayCutoutForRotation(DisplayCutout displayCutout, int i, int i2, int i3) {
        DisplayCutout displayCutout2 = displayCutout;
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (displayCutout2 == null || displayCutout2 == DisplayCutout.NO_CUTOUT) {
            return null;
        }
        if (i4 == 0) {
            return computeSafeInsets(displayCutout2, i5, i6);
        }
        Insets rotateInsets = RotationUtils.rotateInsets(displayCutout.getWaterfallInsets(), i4);
        boolean z = i4 == 1 || i4 == 3;
        Rect[] boundingRectsAll = displayCutout.getBoundingRectsAll();
        Rect[] rectArr = new Rect[boundingRectsAll.length];
        Rect rect = new Rect(0, 0, i5, i6);
        for (int i7 = 0; i7 < boundingRectsAll.length; i7++) {
            Rect rect2 = new Rect(boundingRectsAll[i7]);
            if (!rect2.isEmpty()) {
                RotationUtils.rotateBounds(rect2, rect, i4);
            }
            rectArr[getBoundIndexFromRotation(i7, i4)] = rect2;
        }
        DisplayCutout.CutoutPathParserInfo cutoutPathParserInfo = displayCutout.getCutoutPathParserInfo();
        DisplayCutout constructDisplayCutout = DisplayCutout.constructDisplayCutout(rectArr, rotateInsets, new DisplayCutout.CutoutPathParserInfo(cutoutPathParserInfo.getDisplayWidth(), cutoutPathParserInfo.getDisplayHeight(), cutoutPathParserInfo.getPhysicalDisplayWidth(), cutoutPathParserInfo.getPhysicalDisplayHeight(), cutoutPathParserInfo.getDensity(), cutoutPathParserInfo.getCutoutSpec(), i, cutoutPathParserInfo.getScale(), cutoutPathParserInfo.getPhysicalPixelDisplaySizeRatio()));
        int i8 = z ? i6 : i5;
        if (!z) {
            i5 = i6;
        }
        return computeSafeInsets(constructDisplayCutout, i8, i5);
    }

    public static DisplayCutout computeSafeInsets(DisplayCutout displayCutout, int i, int i2) {
        if (displayCutout == DisplayCutout.NO_CUTOUT) {
            return null;
        }
        return displayCutout.replaceSafeInsets(computeSafeInsets(new Size(i, i2), displayCutout));
    }

    public static Rect computeSafeInsets(Size size, DisplayCutout displayCutout) {
        if (size.getWidth() != size.getHeight()) {
            return new Rect(Math.max(displayCutout.getWaterfallInsets().left, findCutoutInsetForSide(size, displayCutout.getBoundingRectLeft(), 3)), Math.max(displayCutout.getWaterfallInsets().top, findCutoutInsetForSide(size, displayCutout.getBoundingRectTop(), 48)), Math.max(displayCutout.getWaterfallInsets().right, findCutoutInsetForSide(size, displayCutout.getBoundingRectRight(), 5)), Math.max(displayCutout.getWaterfallInsets().bottom, findCutoutInsetForSide(size, displayCutout.getBoundingRectBottom(), 80)));
        }
        throw new UnsupportedOperationException("not implemented: display=" + size + " cutout=" + displayCutout);
    }

    public static int findCutoutInsetForSide(Size size, Rect rect, int i) {
        if (rect.isEmpty()) {
            return 0;
        }
        if (i == 3) {
            return Math.max(0, rect.right);
        }
        if (i == 5) {
            return Math.max(0, size.getWidth() - rect.left);
        }
        if (i == 48) {
            return Math.max(0, rect.bottom);
        }
        if (i == 80) {
            return Math.max(0, size.getHeight() - rect.top);
        }
        throw new IllegalArgumentException("unknown gravity: " + i);
    }

    public static boolean hasNavigationBar(DisplayInfo displayInfo, Context context, int i) {
        if (i == 0) {
            String str = SystemProperties.get("qemu.hw.mainkeys");
            if ("1".equals(str)) {
                return false;
            }
            if ("0".equals(str)) {
                return true;
            }
            return context.getResources().getBoolean(17891752);
        }
        boolean z = displayInfo.type == 5 && displayInfo.ownerUid != 1000;
        boolean z2 = Settings.Global.getInt(context.getContentResolver(), "force_desktop_mode_on_external_displays", 0) != 0;
        if ((displayInfo.flags & 64) != 0) {
            return true;
        }
        if (!z2 || z) {
            return false;
        }
        return true;
    }

    public static int navigationBarPosition(Resources resources, int i, int i2, int i3) {
        if (!(i != i2 && resources.getBoolean(17891710)) || i <= i2) {
            return 4;
        }
        return i3 == 1 ? 2 : 1;
    }

    public static int getNavigationBarSize(Resources resources, int i, boolean z, int i2) {
        if ((i2 & 15) == 3) {
            if (i != 4) {
                return resources.getDimensionPixelSize(17105368);
            }
            return resources.getDimensionPixelSize(z ? 17105365 : 17105363);
        } else if (i != 4) {
            return resources.getDimensionPixelSize(17105367);
        } else {
            return resources.getDimensionPixelSize(z ? 17105364 : 17105362);
        }
    }

    public static int getNavigationBarFrameHeight(Resources resources, boolean z) {
        return resources.getDimensionPixelSize(z ? 17105359 : 17105358);
    }
}
