package com.android.wm.shell.onehanded;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import java.io.PrintWriter;

public final class BackgroundWindowManager extends WindowlessWindowManager {
    public static final String TAG = BackgroundWindowManager.class.getSimpleName();
    public View mBackgroundView;
    public Context mContext;
    public int mCurrentState;
    public Rect mDisplayBounds;
    public SurfaceControl mLeash;
    public final OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory mTransactionFactory = new BackgroundWindowManager$$ExternalSyntheticLambda0();
    public SurfaceControlViewHost mViewHost;

    public BackgroundWindowManager(Context context) {
        super(context.getResources().getConfiguration(), (SurfaceControl) null, (IBinder) null);
        this.mContext = context;
    }

    public SurfaceControl getSurfaceControl(IWindow iWindow) {
        return BackgroundWindowManager.super.getSurfaceControl(iWindow);
    }

    public void setConfiguration(Configuration configuration) {
        BackgroundWindowManager.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }

    public void onConfigurationChanged() {
        int i = this.mCurrentState;
        if (i == 1 || i == 2) {
            updateThemeOnly();
        }
    }

    public void onStateChanged(int i) {
        this.mCurrentState = i;
    }

    public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setColorLayer().setBufferSize(this.mDisplayBounds.width(), this.mDisplayBounds.height()).setFormat(3).setOpaque(true).setName(TAG).setCallsite("BackgroundWindowManager#attachToParentSurface").build();
        this.mLeash = build;
        builder.setParent(build);
    }

    public boolean initView() {
        if (this.mBackgroundView != null || this.mViewHost != null) {
            return false;
        }
        Context context = this.mContext;
        this.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), this);
        this.mBackgroundView = LayoutInflater.from(this.mContext).inflate(R.layout.background_panel, (ViewGroup) null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mDisplayBounds.width(), this.mDisplayBounds.height(), 0, 537133096, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle("background-panel");
        layoutParams.privateFlags |= 536870976;
        this.mBackgroundView.setBackgroundColor(getThemeColorForBackground());
        this.mViewHost.setView(this.mBackgroundView, layoutParams);
        return true;
    }

    public void onDisplayChanged(DisplayLayout displayLayout) {
        this.mDisplayBounds = new Rect(0, 0, displayLayout.width(), displayLayout.height());
    }

    public final void updateThemeOnly() {
        View view = this.mBackgroundView;
        if (view == null || this.mViewHost == null || this.mLeash == null) {
            Slog.w(TAG, "Background view or SurfaceControl does not exist when trying to update theme only!");
            return;
        }
        this.mBackgroundView.setBackgroundColor(getThemeColorForBackground());
        this.mViewHost.setView(this.mBackgroundView, (WindowManager.LayoutParams) view.getLayoutParams());
    }

    public void showBackgroundLayer() {
        if (!initView()) {
            updateThemeOnly();
        } else if (this.mLeash == null) {
            Slog.w(TAG, "SurfaceControl mLeash is null, can't show One-handed mode background panel!");
        } else {
            this.mTransactionFactory.getTransaction().setAlpha(this.mLeash, 1.0f).setLayer(this.mLeash, -1).show(this.mLeash).apply();
        }
    }

    public void removeBackgroundLayer() {
        if (this.mBackgroundView != null) {
            this.mBackgroundView = null;
        }
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        if (this.mLeash != null) {
            this.mTransactionFactory.getTransaction().remove(this.mLeash).apply();
            this.mLeash = null;
        }
    }

    public final int getThemeColor() {
        return new ContextThemeWrapper(this.mContext, 16974563).getColor(R.color.one_handed_tutorial_background_color);
    }

    public int getThemeColorForBackground() {
        int themeColor = getThemeColor();
        return Color.argb(Color.alpha(themeColor), Color.red(themeColor) - 10, Color.green(themeColor) - 10, Color.blue(themeColor) - 10);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.print("  mDisplayBounds=");
        printWriter.println(this.mDisplayBounds);
        printWriter.print("  mViewHost=");
        printWriter.println(this.mViewHost);
        printWriter.print("  mLeash=");
        printWriter.println(this.mLeash);
        printWriter.print("  mBackgroundView=");
        printWriter.println(this.mBackgroundView);
    }
}
