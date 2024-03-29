package com.android.wm.shell.hidedisplaycutout;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.Log;
import android.util.RotationUtils;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.SystemBarUtils;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;
import java.util.List;

public class HideDisplayCutoutOrganizer extends DisplayAreaOrganizer {
    public final Context mContext;
    public Insets mCurrentCutoutInsets;
    public final Rect mCurrentDisplayBounds = new Rect();
    public Insets mDefaultCutoutInsets;
    public final Rect mDefaultDisplayBounds = new Rect();
    public ArrayMap<WindowContainerToken, SurfaceControl> mDisplayAreaMap = new ArrayMap<>();
    public final DisplayController mDisplayController;
    public boolean mIsDefaultPortrait;
    public final DisplayController.OnDisplaysChangedListener mListener = new DisplayController.OnDisplaysChangedListener() {
        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            if (i == 0) {
                boolean z = false;
                DisplayLayout displayLayout = HideDisplayCutoutOrganizer.this.mDisplayController.getDisplayLayout(0);
                if (displayLayout != null) {
                    if (HideDisplayCutoutOrganizer.this.mRotation != displayLayout.rotation()) {
                        z = true;
                    }
                    HideDisplayCutoutOrganizer.this.mRotation = displayLayout.rotation();
                    if (z || HideDisplayCutoutOrganizer.this.isDisplayBoundsChanged()) {
                        HideDisplayCutoutOrganizer.this.updateBoundsAndOffsets(true);
                        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        HideDisplayCutoutOrganizer.this.applyAllBoundsAndOffsets(windowContainerTransaction, transaction);
                        HideDisplayCutoutOrganizer.this.applyTransaction(windowContainerTransaction, transaction);
                    }
                }
            }
        }
    };
    public int mOffsetX;
    public int mOffsetY;
    public int mRotation;
    public int mStatusBarHeight;

    public HideDisplayCutoutOrganizer(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        super(shellExecutor);
        this.mContext = context;
        this.mDisplayController = displayController;
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (addDisplayAreaInfoAndLeashToMap(displayAreaInfo, surfaceControl)) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            lambda$applyAllBoundsAndOffsets$0(displayAreaInfo.token, surfaceControl, windowContainerTransaction, transaction);
            applyTransaction(windowContainerTransaction, transaction);
        }
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        synchronized (this) {
            if (!this.mDisplayAreaMap.containsKey(displayAreaInfo.token)) {
                Log.w("HideDisplayCutoutOrganizer", "Unrecognized token: " + displayAreaInfo.token);
                return;
            }
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            WindowContainerToken windowContainerToken = displayAreaInfo.token;
            lambda$applyAllBoundsAndOffsets$0(windowContainerToken, this.mDisplayAreaMap.get(windowContainerToken), windowContainerTransaction, transaction);
            applyTransaction(windowContainerTransaction, transaction);
            this.mDisplayAreaMap.remove(displayAreaInfo.token);
        }
    }

    public final void updateDisplayAreaMap(List<DisplayAreaAppearedInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            addDisplayAreaInfoAndLeashToMap(list.get(i).getDisplayAreaInfo(), list.get(i).getLeash());
        }
    }

    public boolean addDisplayAreaInfoAndLeashToMap(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        synchronized (this) {
            if (displayAreaInfo.displayId != 0) {
                return false;
            }
            if (this.mDisplayAreaMap.containsKey(displayAreaInfo.token)) {
                Log.w("HideDisplayCutoutOrganizer", "Already appeared token: " + displayAreaInfo.token);
                return false;
            }
            this.mDisplayAreaMap.put(displayAreaInfo.token, surfaceControl);
            return true;
        }
    }

    public void enableHideDisplayCutout() {
        this.mDisplayController.addDisplayWindowListener(this.mListener);
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout != null) {
            this.mRotation = displayLayout.rotation();
        }
        updateDisplayAreaMap(registerOrganizer(6));
        updateBoundsAndOffsets(true);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        applyAllBoundsAndOffsets(windowContainerTransaction, transaction);
        applyTransaction(windowContainerTransaction, transaction);
    }

    public void disableHideDisplayCutout() {
        updateBoundsAndOffsets(false);
        this.mDisplayController.removeDisplayWindowListener(this.mListener);
        unregisterOrganizer();
    }

    public Insets getDisplayCutoutInsetsOfNaturalOrientation() {
        Display display = this.mDisplayController.getDisplay(0);
        if (display == null) {
            return Insets.NONE;
        }
        DisplayCutout cutout = display.getCutout();
        Insets of = cutout != null ? Insets.of(cutout.getSafeInsets()) : Insets.NONE;
        int i = this.mRotation;
        return i != 0 ? RotationUtils.rotateInsets(of, 4 - i) : of;
    }

    public Rect getDisplayBoundsOfNaturalOrientation() {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout == null) {
            return new Rect();
        }
        boolean isDisplaySizeFlipped = isDisplaySizeFlipped();
        return new Rect(0, 0, isDisplaySizeFlipped ? displayLayout.height() : displayLayout.width(), isDisplaySizeFlipped ? displayLayout.width() : displayLayout.height());
    }

    public final boolean isDisplaySizeFlipped() {
        int i = this.mRotation;
        return i == 1 || i == 3;
    }

    public final boolean isDisplayBoundsChanged() {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout == null) {
            return false;
        }
        boolean isDisplaySizeFlipped = isDisplaySizeFlipped();
        int height = isDisplaySizeFlipped ? displayLayout.height() : displayLayout.width();
        int width = isDisplaySizeFlipped ? displayLayout.width() : displayLayout.height();
        if (!this.mDefaultDisplayBounds.isEmpty() && this.mDefaultDisplayBounds.width() == height && this.mDefaultDisplayBounds.height() == width) {
            return false;
        }
        return true;
    }

    public void updateBoundsAndOffsets(boolean z) {
        if (!z) {
            resetBoundsAndOffsets();
            return;
        }
        initDefaultValuesIfNeeded();
        this.mCurrentDisplayBounds.set(this.mDefaultDisplayBounds);
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mCurrentCutoutInsets = RotationUtils.rotateInsets(this.mDefaultCutoutInsets, this.mRotation);
        if (isDisplaySizeFlipped()) {
            Rect rect = this.mCurrentDisplayBounds;
            rect.set(rect.top, rect.left, rect.bottom, rect.right);
        }
        this.mCurrentDisplayBounds.inset(this.mCurrentCutoutInsets);
        int statusBarHeight = getStatusBarHeight();
        this.mStatusBarHeight = statusBarHeight;
        int i = this.mCurrentCutoutInsets.top;
        if (i != 0) {
            this.mCurrentDisplayBounds.top = Math.max(statusBarHeight, i);
        }
        Rect rect2 = this.mCurrentDisplayBounds;
        this.mOffsetX = rect2.left;
        this.mOffsetY = rect2.top;
    }

    public final void resetBoundsAndOffsets() {
        this.mCurrentDisplayBounds.setEmpty();
        this.mOffsetX = 0;
        this.mOffsetY = 0;
    }

    public final void initDefaultValuesIfNeeded() {
        if (isDisplayBoundsChanged()) {
            this.mDefaultDisplayBounds.set(getDisplayBoundsOfNaturalOrientation());
            this.mDefaultCutoutInsets = getDisplayCutoutInsetsOfNaturalOrientation();
            this.mIsDefaultPortrait = this.mDefaultDisplayBounds.width() < this.mDefaultDisplayBounds.height();
        }
    }

    public final void applyAllBoundsAndOffsets(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        synchronized (this) {
            this.mDisplayAreaMap.forEach(new HideDisplayCutoutOrganizer$$ExternalSyntheticLambda0(this, windowContainerTransaction, transaction));
        }
    }

    /* renamed from: applyBoundsAndOffsets */
    public void lambda$applyAllBoundsAndOffsets$0(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        windowContainerTransaction.setBounds(windowContainerToken, this.mCurrentDisplayBounds);
        transaction.setPosition(surfaceControl, (float) this.mOffsetX, (float) this.mOffsetY);
        transaction.setWindowCrop(surfaceControl, this.mCurrentDisplayBounds.width(), this.mCurrentDisplayBounds.height());
    }

    public void applyTransaction(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        applyTransaction(windowContainerTransaction);
        transaction.apply();
    }

    public int getStatusBarHeight() {
        return SystemBarUtils.getStatusBarHeight(this.mContext);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("HideDisplayCutoutOrganizer");
        printWriter.println(" states: ");
        synchronized (this) {
            printWriter.print("  ");
            printWriter.print("mDisplayAreaMap=");
            printWriter.println(this.mDisplayAreaMap);
        }
        printWriter.print("  ");
        printWriter.print("getDisplayBoundsOfNaturalOrientation()=");
        printWriter.println(getDisplayBoundsOfNaturalOrientation());
        printWriter.print("  ");
        printWriter.print("mDefaultDisplayBounds=");
        printWriter.println(this.mDefaultDisplayBounds);
        printWriter.print("  ");
        printWriter.print("mCurrentDisplayBounds=");
        printWriter.println(this.mCurrentDisplayBounds);
        printWriter.print("  ");
        printWriter.print("mDefaultCutoutInsets=");
        printWriter.println(this.mDefaultCutoutInsets);
        printWriter.print("  ");
        printWriter.print("mCurrentCutoutInsets=");
        printWriter.println(this.mCurrentCutoutInsets);
        printWriter.print("  ");
        printWriter.print("mRotation=");
        printWriter.println(this.mRotation);
        printWriter.print("  ");
        printWriter.print("mStatusBarHeight=");
        printWriter.println(this.mStatusBarHeight);
        printWriter.print("  ");
        printWriter.print("mOffsetX=");
        printWriter.println(this.mOffsetX);
        printWriter.print("  ");
        printWriter.print("mOffsetY=");
        printWriter.println(this.mOffsetY);
    }
}
