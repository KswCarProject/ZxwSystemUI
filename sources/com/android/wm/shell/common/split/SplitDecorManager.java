package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.R;
import com.android.wm.shell.common.SurfaceUtils;

public class SplitDecorManager extends WindowlessWindowManager {
    public static final String TAG = SplitDecorManager.class.getSimpleName();
    public SurfaceControl mBackgroundLeash;
    public Rect mBounds = new Rect();
    public ValueAnimator mFadeAnimator;
    public SurfaceControl mHostLeash;
    public Drawable mIcon;
    public SurfaceControl mIconLeash;
    public final IconProvider mIconProvider;
    public int mIconSize;
    public boolean mIsResizing;
    public ImageView mResizingIconView;
    public boolean mShown;
    public final SurfaceSession mSurfaceSession;
    public SurfaceControlViewHost mViewHost;

    public SplitDecorManager(Configuration configuration, IconProvider iconProvider, SurfaceSession surfaceSession) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mIconProvider = iconProvider;
        this.mSurfaceSession = surfaceSession;
    }

    public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName(TAG).setHidden(true).setParent(this.mHostLeash).setCallsite("SplitDecorManager#attachToParentSurface").build();
        this.mIconLeash = build;
        builder.setParent(build);
    }

    public void inflate(Context context, SurfaceControl surfaceControl, Rect rect) {
        if (this.mIconLeash == null || this.mViewHost == null) {
            Context createWindowContext = context.createWindowContext(context.getDisplay(), 2038, (Bundle) null);
            this.mHostLeash = surfaceControl;
            this.mViewHost = new SurfaceControlViewHost(createWindowContext, createWindowContext.getDisplay(), this);
            this.mIconSize = createWindowContext.getResources().getDimensionPixelSize(R.dimen.split_icon_size);
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(createWindowContext).inflate(R.layout.split_decor, (ViewGroup) null);
            this.mResizingIconView = (ImageView) frameLayout.findViewById(R.id.split_resizing_icon);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, 2038, 24, -3);
            layoutParams.width = rect.width();
            layoutParams.height = rect.height();
            layoutParams.token = new Binder();
            layoutParams.setTitle(TAG);
            layoutParams.privateFlags |= 536870976;
            this.mViewHost.setView(frameLayout, layoutParams);
        }
    }

    public void release(SurfaceControl.Transaction transaction) {
        ValueAnimator valueAnimator = this.mFadeAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mFadeAnimator.cancel();
        }
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        SurfaceControl surfaceControl = this.mIconLeash;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mIconLeash = null;
        }
        SurfaceControl surfaceControl2 = this.mBackgroundLeash;
        if (surfaceControl2 != null) {
            transaction.remove(surfaceControl2);
            this.mBackgroundLeash = null;
        }
        this.mHostLeash = null;
        this.mIcon = null;
        this.mResizingIconView = null;
        this.mIsResizing = false;
        this.mShown = false;
    }

    public void onResizing(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, SurfaceControl.Transaction transaction) {
        ActivityInfo activityInfo;
        ValueAnimator valueAnimator;
        if (this.mResizingIconView != null) {
            boolean z = true;
            if (!this.mIsResizing) {
                this.mIsResizing = true;
                this.mBounds.set(rect);
            }
            boolean z2 = rect.width() > this.mBounds.width() || rect.height() > this.mBounds.height();
            if (z2 == this.mShown) {
                z = false;
            }
            if (z && (valueAnimator = this.mFadeAnimator) != null && valueAnimator.isRunning()) {
                this.mFadeAnimator.cancel();
            }
            if (this.mBackgroundLeash == null) {
                SurfaceControl makeColorLayer = SurfaceUtils.makeColorLayer(this.mHostLeash, "ResizingBackground", this.mSurfaceSession);
                this.mBackgroundLeash = makeColorLayer;
                transaction.setColor(makeColorLayer, getResizingBackgroundColor(runningTaskInfo)).setLayer(this.mBackgroundLeash, 2147483646);
            }
            if (this.mIcon == null && (activityInfo = runningTaskInfo.topActivityInfo) != null) {
                Drawable icon = this.mIconProvider.getIcon(activityInfo);
                this.mIcon = icon;
                this.mResizingIconView.setImageDrawable(icon);
                this.mResizingIconView.setVisibility(0);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mViewHost.getView().getLayoutParams();
                int i = this.mIconSize;
                layoutParams.width = i;
                layoutParams.height = i;
                this.mViewHost.relayout(layoutParams);
                transaction.setLayer(this.mIconLeash, Integer.MAX_VALUE);
            }
            transaction.setPosition(this.mIconLeash, (float) ((rect.width() / 2) - (this.mIconSize / 2)), (float) ((rect.height() / 2) - (this.mIconSize / 2)));
            if (z) {
                startFadeAnimation(z2, false);
                this.mShown = z2;
            }
        }
    }

    public void onResized(SurfaceControl.Transaction transaction) {
        if (this.mResizingIconView != null) {
            this.mIsResizing = false;
            ValueAnimator valueAnimator = this.mFadeAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                if (!this.mShown) {
                    final SurfaceControl.Transaction transaction2 = new SurfaceControl.Transaction();
                    this.mFadeAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            SplitDecorManager.this.releaseDecor(transaction2);
                            transaction2.apply();
                            transaction2.close();
                        }
                    });
                    return;
                }
                this.mFadeAnimator.cancel();
            }
            if (this.mShown) {
                startFadeAnimation(false, true);
            } else {
                releaseDecor(transaction);
            }
        }
    }

    public final void startFadeAnimation(final boolean z, final boolean z2) {
        final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mFadeAnimator = ofFloat;
        ofFloat.setDuration(133);
        this.mFadeAnimator.addUpdateListener(new SplitDecorManager$$ExternalSyntheticLambda0(this, transaction, z));
        this.mFadeAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                if (z) {
                    transaction.show(SplitDecorManager.this.mBackgroundLeash).show(SplitDecorManager.this.mIconLeash).apply();
                }
            }

            public void onAnimationEnd(Animator animator) {
                if (!z) {
                    if (SplitDecorManager.this.mBackgroundLeash != null) {
                        transaction.hide(SplitDecorManager.this.mBackgroundLeash);
                    }
                    if (SplitDecorManager.this.mIconLeash != null) {
                        transaction.hide(SplitDecorManager.this.mIconLeash);
                    }
                }
                if (z2) {
                    SplitDecorManager.this.releaseDecor(transaction);
                }
                transaction.apply();
                transaction.close();
            }
        });
        this.mFadeAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startFadeAnimation$0(SurfaceControl.Transaction transaction, boolean z, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        SurfaceControl surfaceControl = this.mBackgroundLeash;
        if (surfaceControl != null) {
            transaction.setAlpha(surfaceControl, z ? floatValue : 1.0f - floatValue);
        }
        SurfaceControl surfaceControl2 = this.mIconLeash;
        if (surfaceControl2 != null) {
            if (!z) {
                floatValue = 1.0f - floatValue;
            }
            transaction.setAlpha(surfaceControl2, floatValue);
        }
        transaction.apply();
    }

    public final void releaseDecor(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mBackgroundLeash;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mBackgroundLeash = null;
        }
        if (this.mIcon != null) {
            this.mResizingIconView.setVisibility(8);
            this.mResizingIconView.setImageDrawable((Drawable) null);
            transaction.hide(this.mIconLeash);
            this.mIcon = null;
        }
    }

    public static float[] getResizingBackgroundColor(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int backgroundColor = runningTaskInfo.taskDescription.getBackgroundColor();
        if (backgroundColor == -1) {
            backgroundColor = -1;
        }
        return Color.valueOf(backgroundColor).getComponents();
    }
}
