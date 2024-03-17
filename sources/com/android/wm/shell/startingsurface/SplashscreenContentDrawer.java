package com.android.wm.shell.startingsurface;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import android.view.SurfaceControl;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.Quantizer;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.R;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SplashscreenContentDrawer {
    public int mBrandingImageHeight;
    public int mBrandingImageWidth;
    @VisibleForTesting
    public final ColorCache mColorCache;
    public final Context mContext;
    public int mDefaultIconSize;
    public final HighResIconProvider mHighResIconProvider;
    public int mIconSize;
    public int mLastPackageContextConfigHash;
    public int mMainWindowShiftLength;
    public final Handler mSplashscreenWorkerHandler;
    public final SplashScreenWindowAttrs mTmpAttrs = new SplashScreenWindowAttrs();
    public final TransactionPool mTransactionPool;

    public static class SplashScreenWindowAttrs {
        public Drawable mBrandingImage = null;
        public int mIconBgColor = 0;
        public Drawable mSplashScreenIcon = null;
        public int mWindowBgColor = 0;
        public int mWindowBgResId = 0;
    }

    @VisibleForTesting
    public static long getShowingDuration(long j, long j2) {
        return (j > j2 && j2 < 500) ? (j > 500 || j2 < 400) ? 400 : 500 : j2;
    }

    public SplashscreenContentDrawer(Context context, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        this.mHighResIconProvider = new HighResIconProvider(context, iconProvider);
        this.mTransactionPool = transactionPool;
        HandlerThread handlerThread = new HandlerThread("wmshell.splashworker", -10);
        handlerThread.start();
        Handler threadHandler = handlerThread.getThreadHandler();
        this.mSplashscreenWorkerHandler = threadHandler;
        this.mColorCache = new ColorCache(context, threadHandler);
    }

    public void createContentView(Context context, @StartingWindowInfo.StartingWindowType int i, StartingWindowInfo startingWindowInfo, Consumer<SplashScreenView> consumer, Consumer<Runnable> consumer2) {
        this.mSplashscreenWorkerHandler.post(new SplashscreenContentDrawer$$ExternalSyntheticLambda0(this, context, startingWindowInfo, i, consumer2, consumer));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createContentView$0(Context context, StartingWindowInfo startingWindowInfo, int i, Consumer consumer, Consumer consumer2) {
        SplashScreenView splashScreenView;
        try {
            Trace.traceBegin(32, "makeSplashScreenContentView");
            splashScreenView = makeSplashScreenContentView(context, startingWindowInfo, i, consumer);
            Trace.traceEnd(32);
        } catch (RuntimeException e) {
            Slog.w("ShellStartingWindow", "failed creating starting window content at taskId: " + startingWindowInfo.taskInfo.taskId, e);
            splashScreenView = null;
        }
        consumer2.accept(splashScreenView);
    }

    public final void updateDensity() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(17105548);
        this.mDefaultIconSize = this.mContext.getResources().getDimensionPixelSize(17105547);
        this.mBrandingImageWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_brand_image_width);
        this.mBrandingImageHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_brand_image_height);
        this.mMainWindowShiftLength = this.mContext.getResources().getDimensionPixelSize(R.dimen.starting_surface_exit_animation_window_shift_length);
    }

    public static int getSystemBGColor() {
        Application currentApplication = ActivityThread.currentApplication();
        if (currentApplication != null) {
            return currentApplication.getResources().getColor(R.color.splash_window_background_default);
        }
        Slog.e("ShellStartingWindow", "System context does not exist!");
        return -16777216;
    }

    public int estimateTaskBackgroundColor(Context context) {
        SplashScreenWindowAttrs splashScreenWindowAttrs = new SplashScreenWindowAttrs();
        getWindowAttrs(context, splashScreenWindowAttrs);
        return peekWindowBGColor(context, splashScreenWindowAttrs);
    }

    public static Drawable createDefaultBackgroundDrawable() {
        return new ColorDrawable(getSystemBGColor());
    }

    public static int peekWindowBGColor(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        Drawable drawable;
        Trace.traceBegin(32, "peekWindowBGColor");
        if (splashScreenWindowAttrs.mWindowBgColor != 0) {
            drawable = new ColorDrawable(splashScreenWindowAttrs.mWindowBgColor);
        } else if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            drawable = context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        } else {
            drawable = createDefaultBackgroundDrawable();
            Slog.w("ShellStartingWindow", "Window background does not exist, using " + drawable);
        }
        int estimateWindowBGColor = estimateWindowBGColor(drawable);
        Trace.traceEnd(32);
        return estimateWindowBGColor;
    }

    public static int estimateWindowBGColor(Drawable drawable) {
        DrawableColorTester drawableColorTester = new DrawableColorTester(drawable, 1);
        if (drawableColorTester.passFilterRatio() != 0.0f) {
            return drawableColorTester.getDominateColor();
        }
        Slog.w("ShellStartingWindow", "Window background is transparent, fill background with black color");
        return getSystemBGColor();
    }

    public static Drawable peekLegacySplashscreenContent(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(com.android.internal.R.styleable.Window);
        int intValue = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda8(obtainStyledAttributes), 0)).intValue();
        obtainStyledAttributes.recycle();
        if (intValue != 0) {
            return context.getDrawable(intValue);
        }
        if (splashScreenWindowAttrs.mWindowBgResId != 0) {
            return context.getDrawable(splashScreenWindowAttrs.mWindowBgResId);
        }
        return null;
    }

    public final SplashScreenView makeSplashScreenContentView(Context context, StartingWindowInfo startingWindowInfo, @StartingWindowInfo.StartingWindowType int i, Consumer<Runnable> consumer) {
        int i2;
        updateDensity();
        getWindowAttrs(context, this.mTmpAttrs);
        this.mLastPackageContextConfigHash = context.getResources().getConfiguration().hashCode();
        Drawable peekLegacySplashscreenContent = i == 4 ? peekLegacySplashscreenContent(context, this.mTmpAttrs) : null;
        ActivityInfo activityInfo = startingWindowInfo.targetActivityInfo;
        if (activityInfo == null) {
            activityInfo = startingWindowInfo.taskInfo.topActivityInfo;
        }
        if (peekLegacySplashscreenContent != null) {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda6(peekLegacySplashscreenContent));
        } else {
            i2 = getBGColorFromCache(activityInfo, new SplashscreenContentDrawer$$ExternalSyntheticLambda7(this, context));
        }
        return new StartingWindowViewBuilder(context, activityInfo).setWindowBGColor(i2).overlayDrawable(peekLegacySplashscreenContent).chooseStyle(i).setUiThreadInitConsumer(consumer).setAllowHandleSolidColor(startingWindowInfo.allowHandleSolidColorSplashScreen()).build();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$makeSplashScreenContentView$3(Context context) {
        return peekWindowBGColor(context, this.mTmpAttrs);
    }

    public final int getBGColorFromCache(ActivityInfo activityInfo, IntSupplier intSupplier) {
        return this.mColorCache.getWindowColor(activityInfo.packageName, this.mLastPackageContextConfigHash, this.mTmpAttrs.mWindowBgColor, this.mTmpAttrs.mWindowBgResId, intSupplier).mBgColor;
    }

    public static <T> T safeReturnAttrDefault(UnaryOperator<T> unaryOperator, T t) {
        try {
            return unaryOperator.apply(t);
        } catch (RuntimeException e) {
            Slog.w("ShellStartingWindow", "Get attribute fail, return default: " + e.getMessage());
            return t;
        }
    }

    public static void getWindowAttrs(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(com.android.internal.R.styleable.Window);
        splashScreenWindowAttrs.mWindowBgResId = obtainStyledAttributes.getResourceId(1, 0);
        splashScreenWindowAttrs.mWindowBgColor = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda2(obtainStyledAttributes), 0)).intValue();
        splashScreenWindowAttrs.mSplashScreenIcon = (Drawable) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda3(obtainStyledAttributes), (Object) null);
        splashScreenWindowAttrs.mBrandingImage = (Drawable) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda4(obtainStyledAttributes), (Object) null);
        splashScreenWindowAttrs.mIconBgColor = ((Integer) safeReturnAttrDefault(new SplashscreenContentDrawer$$ExternalSyntheticLambda5(obtainStyledAttributes), 0)).intValue();
        obtainStyledAttributes.recycle();
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1867686022, 12, (String) null, String.valueOf(Integer.toHexString(splashScreenWindowAttrs.mWindowBgColor)), Boolean.valueOf(splashScreenWindowAttrs.mSplashScreenIcon != null));
        }
    }

    public ContextThemeWrapper createViewContextWrapper(Context context) {
        return new ContextThemeWrapper(context, this.mContext.getTheme());
    }

    public class StartingWindowViewBuilder {
        public final ActivityInfo mActivityInfo;
        public boolean mAllowHandleSolidColor;
        public final Context mContext;
        public Drawable[] mFinalIconDrawables;
        public int mFinalIconSize;
        public Drawable mOverlayDrawable;
        public int mSuggestType;
        public int mThemeColor;
        public Consumer<Runnable> mUiThreadInitTask;

        public StartingWindowViewBuilder(Context context, ActivityInfo activityInfo) {
            this.mFinalIconSize = SplashscreenContentDrawer.this.mIconSize;
            this.mContext = context;
            this.mActivityInfo = activityInfo;
        }

        public StartingWindowViewBuilder setWindowBGColor(int i) {
            this.mThemeColor = i;
            return this;
        }

        public StartingWindowViewBuilder overlayDrawable(Drawable drawable) {
            this.mOverlayDrawable = drawable;
            return this;
        }

        public StartingWindowViewBuilder chooseStyle(int i) {
            this.mSuggestType = i;
            return this;
        }

        public StartingWindowViewBuilder setUiThreadInitConsumer(Consumer<Runnable> consumer) {
            this.mUiThreadInitTask = consumer;
            return this;
        }

        public StartingWindowViewBuilder setAllowHandleSolidColor(boolean z) {
            this.mAllowHandleSolidColor = z;
            return this;
        }

        public SplashScreenView build() {
            int i = this.mSuggestType;
            if (i == 3 || i == 4) {
                this.mFinalIconSize = 0;
            } else if (SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon != null) {
                Drawable r0 = SplashscreenContentDrawer.this.mTmpAttrs.mSplashScreenIcon;
                if (SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == 0 || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor == this.mThemeColor) {
                    this.mFinalIconSize = (int) (((float) this.mFinalIconSize) * 1.2f);
                }
                createIconDrawable(r0, false, false);
            } else {
                float r02 = ((float) SplashscreenContentDrawer.this.mIconSize) / ((float) SplashscreenContentDrawer.this.mDefaultIconSize);
                int i2 = this.mContext.getResources().getConfiguration().densityDpi;
                int i3 = (int) ((r02 * ((float) i2) * 1.2f) + 0.5f);
                Trace.traceBegin(32, "getIcon");
                Drawable icon = SplashscreenContentDrawer.this.mHighResIconProvider.getIcon(this.mActivityInfo, i2, i3);
                Trace.traceEnd(32);
                if (!processAdaptiveIcon(icon)) {
                    if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 888452073, 0, (String) null, (Object[]) null);
                    }
                    Trace.traceBegin(32, "legacy_icon_factory");
                    Bitmap createScaledBitmapWithoutShadow = new ShapeIconFactory(SplashscreenContentDrawer.this.mContext, i3, this.mFinalIconSize).createScaledBitmapWithoutShadow(icon);
                    Trace.traceEnd(32);
                    createIconDrawable(new BitmapDrawable(createScaledBitmapWithoutShadow), true, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
                }
            }
            return fillViewWithIcon(this.mFinalIconSize, this.mFinalIconDrawables, this.mUiThreadInitTask);
        }

        public class ShapeIconFactory extends BaseIconFactory {
            public ShapeIconFactory(Context context, int i, int i2) {
                super(context, i, i2, true);
            }
        }

        public final void createIconDrawable(Drawable drawable, boolean z, boolean z2) {
            if (z) {
                this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeLegacyIconDrawable(drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, z2, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
                return;
            }
            this.mFinalIconDrawables = SplashscreenIconDrawableFactory.makeIconDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor, this.mThemeColor, drawable, SplashscreenContentDrawer.this.mDefaultIconSize, this.mFinalIconSize, z2, SplashscreenContentDrawer.this.mSplashscreenWorkerHandler);
        }

        public final boolean processAdaptiveIcon(Drawable drawable) {
            Drawable drawable2 = drawable;
            if (!(drawable2 instanceof AdaptiveIconDrawable)) {
                return false;
            }
            Trace.traceBegin(32, "processAdaptiveIcon");
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable2;
            Drawable foreground = adaptiveIconDrawable.getForeground();
            ColorCache colorCache = SplashscreenContentDrawer.this.mColorCache;
            ActivityInfo activityInfo = this.mActivityInfo;
            ColorCache.IconColor iconColor = colorCache.getIconColor(activityInfo.packageName, activityInfo.getIconResource(), SplashscreenContentDrawer.this.mLastPackageContextConfigHash, new SplashscreenContentDrawer$StartingWindowViewBuilder$$ExternalSyntheticLambda0(foreground), new SplashscreenContentDrawer$StartingWindowViewBuilder$$ExternalSyntheticLambda1(adaptiveIconDrawable));
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -1141104614, 240, (String) null, String.valueOf(Integer.toHexString(iconColor.mFgColor)), String.valueOf(Integer.toHexString(iconColor.mBgColor)), Boolean.valueOf(iconColor.mIsBgComplex), Boolean.valueOf(iconColor.mReuseCount > 0), String.valueOf(Integer.toHexString(this.mThemeColor)));
            }
            if (iconColor.mIsBgComplex || SplashscreenContentDrawer.this.mTmpAttrs.mIconBgColor != 0 || (!SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mBgColor) && (!iconColor.mIsBgGrayscale || SplashscreenContentDrawer.isRgbSimilarInHsv(this.mThemeColor, iconColor.mFgColor)))) {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1288760762, 0, (String) null, (Object[]) null);
                }
                createIconDrawable(drawable2, false, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
            } else {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1960014443, 0, (String) null, (Object[]) null);
                }
                this.mFinalIconSize = (int) ((((float) SplashscreenContentDrawer.this.mIconSize) * (iconColor.mFgNonTranslucentRatio < 0.44444445f ? 1.2f : 1.0f)) + 0.5f);
                createIconDrawable(foreground, false, SplashscreenContentDrawer.this.mHighResIconProvider.mLoadInDetail);
            }
            Trace.traceEnd(32);
            return true;
        }

        public static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$0(Drawable drawable) {
            return new DrawableColorTester(drawable, 2);
        }

        public static /* synthetic */ DrawableColorTester lambda$processAdaptiveIcon$1(AdaptiveIconDrawable adaptiveIconDrawable) {
            return new DrawableColorTester(adaptiveIconDrawable.getBackground());
        }

        public final SplashScreenView fillViewWithIcon(int i, Drawable[] drawableArr, Consumer<Runnable> consumer) {
            Drawable drawable;
            Drawable drawable2 = null;
            if (drawableArr != null) {
                drawable = drawableArr.length > 0 ? drawableArr[0] : null;
                if (drawableArr.length > 1) {
                    drawable2 = drawableArr[1];
                }
            } else {
                drawable = null;
            }
            Trace.traceBegin(32, "fillViewWithIcon");
            SplashScreenView.Builder allowHandleSolidColor = new SplashScreenView.Builder(SplashscreenContentDrawer.this.createViewContextWrapper(this.mContext)).setBackgroundColor(this.mThemeColor).setOverlayDrawable(this.mOverlayDrawable).setIconSize(i).setIconBackground(drawable2).setCenterViewDrawable(drawable).setUiThreadInitConsumer(consumer).setAllowHandleSolidColor(this.mAllowHandleSolidColor);
            if (this.mSuggestType == 1 && SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage != null) {
                allowHandleSolidColor.setBrandingDrawable(SplashscreenContentDrawer.this.mTmpAttrs.mBrandingImage, SplashscreenContentDrawer.this.mBrandingImageWidth, SplashscreenContentDrawer.this.mBrandingImageHeight);
            }
            SplashScreenView build = allowHandleSolidColor.build();
            Trace.traceEnd(32);
            return build;
        }
    }

    public static boolean isRgbSimilarInHsv(int i, int i2) {
        double d;
        boolean z;
        int i3 = i;
        int i4 = i2;
        boolean z2 = true;
        if (i3 == i4) {
            return true;
        }
        float luminance = Color.luminance(i);
        float luminance2 = Color.luminance(i2);
        float f = luminance > luminance2 ? (luminance + 0.05f) / (luminance2 + 0.05f) : (luminance2 + 0.05f) / (luminance + 0.05f);
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -853329785, 32, (String) null, String.valueOf(Integer.toHexString(i)), String.valueOf(Integer.toHexString(i2)), Double.valueOf((double) f));
        }
        if (f < 2.0f) {
            return true;
        }
        float[] fArr = new float[3];
        float[] fArr2 = new float[3];
        Color.colorToHSV(i3, fArr);
        Color.colorToHSV(i4, fArr2);
        int abs = ((((int) Math.abs(fArr[0] - fArr2[0])) + 180) % 360) - 180;
        double pow = Math.pow((double) (((float) abs) / 180.0f), 2.0d);
        double pow2 = Math.pow((double) (fArr[1] - fArr2[1]), 2.0d);
        double pow3 = Math.pow((double) (fArr[2] - fArr2[2]), 2.0d);
        double sqrt = Math.sqrt(((pow + pow2) + pow3) / 3.0d);
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            float[] fArr3 = fArr;
            double d2 = (double) fArr[0];
            double d3 = (double) fArr2[0];
            double d4 = (double) fArr3[1];
            d = sqrt;
            ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
            z = false;
            Double valueOf = Double.valueOf(d2);
            z2 = true;
            ShellProtoLogImpl.v(shellProtoLogGroup, -137676175, 2796201, (String) null, Long.valueOf((long) abs), valueOf, Double.valueOf(d3), Double.valueOf(d4), Double.valueOf((double) fArr2[1]), Double.valueOf((double) fArr3[2]), Double.valueOf((double) fArr2[2]), Double.valueOf(pow), Double.valueOf(pow2), Double.valueOf(pow3), Double.valueOf(d));
        } else {
            z = false;
            d = sqrt;
        }
        return d < 0.1d ? z2 : z;
    }

    public static class DrawableColorTester {
        public final ColorTester mColorChecker;

        public interface ColorTester {
            int getDominantColor();

            boolean isComplexColor();

            boolean isGrayscale();

            float passFilterRatio();
        }

        public DrawableColorTester(Drawable drawable) {
            this(drawable, 0);
        }

        public DrawableColorTester(Drawable drawable, int i) {
            ColorTester colorTester;
            if (drawable instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                if (layerDrawable.getNumberOfLayers() > 0) {
                    if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 428468608, 0, (String) null, (Object[]) null);
                    }
                    drawable = layerDrawable.getDrawable(0);
                }
            }
            if (drawable == null) {
                this.mColorChecker = new SingleColorTester((ColorDrawable) SplashscreenContentDrawer.createDefaultBackgroundDrawable());
                return;
            }
            if (drawable instanceof ColorDrawable) {
                colorTester = new SingleColorTester((ColorDrawable) drawable);
            } else {
                colorTester = new ComplexDrawableTester(drawable, i);
            }
            this.mColorChecker = colorTester;
        }

        public float passFilterRatio() {
            return this.mColorChecker.passFilterRatio();
        }

        public boolean isComplexColor() {
            return this.mColorChecker.isComplexColor();
        }

        public int getDominateColor() {
            return this.mColorChecker.getDominantColor();
        }

        public boolean isGrayscale() {
            return this.mColorChecker.isGrayscale();
        }

        public static boolean isGrayscaleColor(int i) {
            int red = Color.red(i);
            int green = Color.green(i);
            return red == green && green == Color.blue(i);
        }

        public static class SingleColorTester implements ColorTester {
            public final ColorDrawable mColorDrawable;

            public boolean isComplexColor() {
                return false;
            }

            public SingleColorTester(ColorDrawable colorDrawable) {
                this.mColorDrawable = colorDrawable;
            }

            public float passFilterRatio() {
                return (float) (this.mColorDrawable.getAlpha() / 255);
            }

            public int getDominantColor() {
                return this.mColorDrawable.getColor();
            }

            public boolean isGrayscale() {
                return DrawableColorTester.isGrayscaleColor(this.mColorDrawable.getColor());
            }
        }

        public static class ComplexDrawableTester implements ColorTester {
            public static final AlphaFilterQuantizer ALPHA_FILTER_QUANTIZER = new AlphaFilterQuantizer();
            public final boolean mFilterTransparent;
            public final Palette mPalette;

            public ComplexDrawableTester(Drawable drawable, int i) {
                int i2;
                Palette.Builder builder;
                Trace.traceBegin(32, "ComplexDrawableTester");
                Rect copyBounds = drawable.copyBounds();
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                int i3 = 40;
                if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                    i2 = 40;
                } else {
                    i3 = Math.min(intrinsicWidth, 40);
                    i2 = Math.min(intrinsicHeight, 40);
                }
                Bitmap createBitmap = Bitmap.createBitmap(i3, i2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                boolean z = false;
                drawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                drawable.draw(canvas);
                drawable.setBounds(copyBounds);
                z = i != 0 ? true : z;
                this.mFilterTransparent = z;
                if (z) {
                    AlphaFilterQuantizer alphaFilterQuantizer = ALPHA_FILTER_QUANTIZER;
                    alphaFilterQuantizer.setFilter(i);
                    builder = new Palette.Builder(createBitmap, alphaFilterQuantizer).maximumColorCount(5);
                } else {
                    builder = new Palette.Builder(createBitmap, (Quantizer) null).maximumColorCount(5);
                }
                this.mPalette = builder.generate();
                createBitmap.recycle();
                Trace.traceEnd(32);
            }

            public float passFilterRatio() {
                if (this.mFilterTransparent) {
                    return ALPHA_FILTER_QUANTIZER.mPassFilterRatio;
                }
                return 1.0f;
            }

            public boolean isComplexColor() {
                return this.mPalette.getSwatches().size() > 1;
            }

            public int getDominantColor() {
                Palette.Swatch dominantSwatch = this.mPalette.getDominantSwatch();
                if (dominantSwatch != null) {
                    return dominantSwatch.getInt();
                }
                return -16777216;
            }

            public boolean isGrayscale() {
                List swatches = this.mPalette.getSwatches();
                if (swatches != null) {
                    for (int size = swatches.size() - 1; size >= 0; size--) {
                        if (!DrawableColorTester.isGrayscaleColor(((Palette.Swatch) swatches.get(size)).getInt())) {
                            return false;
                        }
                    }
                }
                return true;
            }

            public static class AlphaFilterQuantizer implements Quantizer {
                public IntPredicate mFilter;
                public final Quantizer mInnerQuantizer;
                public float mPassFilterRatio;
                public final IntPredicate mTranslucentFilter;
                public final IntPredicate mTransparentFilter;

                public static /* synthetic */ boolean lambda$new$0(int i) {
                    return (i & -16777216) != 0;
                }

                public static /* synthetic */ boolean lambda$new$1(int i) {
                    return (i & -16777216) == -16777216;
                }

                public AlphaFilterQuantizer() {
                    this.mInnerQuantizer = new VariationalKMeansQuantizer();
                    SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0 = new SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0();
                    this.mTransparentFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                    this.mTranslucentFilter = new SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda1();
                    this.mFilter = splashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester$AlphaFilterQuantizer$$ExternalSyntheticLambda0;
                }

                public void setFilter(int i) {
                    if (i != 2) {
                        this.mFilter = this.mTransparentFilter;
                    } else {
                        this.mFilter = this.mTranslucentFilter;
                    }
                }

                public void quantize(int[] iArr, int i) {
                    this.mPassFilterRatio = 0.0f;
                    int i2 = 0;
                    int i3 = 0;
                    for (int length = iArr.length - 1; length > 0; length--) {
                        if (this.mFilter.test(iArr[length])) {
                            i3++;
                        }
                    }
                    if (i3 == 0) {
                        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -424415681, 0, (String) null, (Object[]) null);
                        }
                        this.mInnerQuantizer.quantize(iArr, i);
                        return;
                    }
                    this.mPassFilterRatio = ((float) i3) / ((float) iArr.length);
                    int[] iArr2 = new int[i3];
                    for (int length2 = iArr.length - 1; length2 > 0; length2--) {
                        if (this.mFilter.test(iArr[length2])) {
                            iArr2[i2] = iArr[length2];
                            i2++;
                        }
                    }
                    this.mInnerQuantizer.quantize(iArr2, i);
                }

                public List<Palette.Swatch> getQuantizedColors() {
                    return this.mInnerQuantizer.getQuantizedColors();
                }
            }
        }
    }

    @VisibleForTesting
    public static class ColorCache extends BroadcastReceiver {
        public final ArrayMap<String, Colors> mColorMap = new ArrayMap<>();

        public static class Colors {
            public final IconColor[] mIconColors;
            public final WindowColor[] mWindowColors;

            public Colors() {
                this.mWindowColors = new WindowColor[2];
                this.mIconColors = new IconColor[2];
            }
        }

        public static class Cache {
            public final int mHash;
            public int mReuseCount;

            public Cache(int i) {
                this.mHash = i;
            }
        }

        public static class WindowColor extends Cache {
            public final int mBgColor;

            public WindowColor(int i, int i2) {
                super(i);
                this.mBgColor = i2;
            }
        }

        public static class IconColor extends Cache {
            public final int mBgColor;
            public final int mFgColor;
            public final float mFgNonTranslucentRatio;
            public final boolean mIsBgComplex;
            public final boolean mIsBgGrayscale;

            public IconColor(int i, int i2, int i3, boolean z, boolean z2, float f) {
                super(i);
                this.mFgColor = i2;
                this.mBgColor = i3;
                this.mIsBgComplex = z;
                this.mIsBgGrayscale = z2;
                this.mFgNonTranslucentRatio = f;
            }
        }

        public ColorCache(Context context, Handler handler) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, (String) null, handler);
        }

        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                this.mColorMap.remove(data.getEncodedSchemeSpecificPart());
            }
        }

        public static <T extends Cache> T getCache(T[] tArr, int i, int[] iArr) {
            int i2 = Integer.MAX_VALUE;
            for (int i3 = 0; i3 < 2; i3++) {
                T t = tArr[i3];
                if (t == null) {
                    i2 = -1;
                    iArr[0] = i3;
                } else if (t.mHash == i) {
                    t.mReuseCount++;
                    return t;
                } else {
                    int i4 = t.mReuseCount;
                    if (i4 < i2) {
                        iArr[0] = i3;
                        i2 = i4;
                    }
                }
            }
            return null;
        }

        public WindowColor getWindowColor(String str, int i, int i2, int i3, IntSupplier intSupplier) {
            Colors colors = this.mColorMap.get(str);
            int i4 = (((i * 31) + i2) * 31) + i3;
            int[] iArr = {0};
            if (colors != null) {
                WindowColor windowColor = (WindowColor) getCache(colors.mWindowColors, i4, iArr);
                if (windowColor != null) {
                    return windowColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            WindowColor windowColor2 = new WindowColor(i4, intSupplier.getAsInt());
            colors.mWindowColors[iArr[0]] = windowColor2;
            return windowColor2;
        }

        public IconColor getIconColor(String str, int i, int i2, Supplier<DrawableColorTester> supplier, Supplier<DrawableColorTester> supplier2) {
            Colors colors = this.mColorMap.get(str);
            int i3 = (i * 31) + i2;
            int[] iArr = {0};
            if (colors != null) {
                IconColor iconColor = (IconColor) getCache(colors.mIconColors, i3, iArr);
                if (iconColor != null) {
                    return iconColor;
                }
            } else {
                colors = new Colors();
                this.mColorMap.put(str, colors);
            }
            DrawableColorTester drawableColorTester = supplier.get();
            DrawableColorTester drawableColorTester2 = supplier2.get();
            IconColor iconColor2 = new IconColor(i3, drawableColorTester.getDominateColor(), drawableColorTester2.getDominateColor(), drawableColorTester2.isComplexColor(), drawableColorTester2.isGrayscale(), drawableColorTester.passFilterRatio());
            colors.mIconColors[iArr[0]] = iconColor2;
            return iconColor2;
        }
    }

    public void applyExitAnimation(SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable, long j) {
        SplashscreenContentDrawer$$ExternalSyntheticLambda1 splashscreenContentDrawer$$ExternalSyntheticLambda1 = new SplashscreenContentDrawer$$ExternalSyntheticLambda1(this, splashScreenView, surfaceControl, rect, runnable);
        if (splashScreenView.getIconView() == null) {
            splashscreenContentDrawer$$ExternalSyntheticLambda1.run();
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis() - j;
        long showingDuration = getShowingDuration(splashScreenView.getIconAnimationDuration() != null ? splashScreenView.getIconAnimationDuration().toMillis() : 0, uptimeMillis) - uptimeMillis;
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 482713286, 0, (String) null, String.valueOf(showingDuration));
        }
        if (showingDuration > 0) {
            splashScreenView.postDelayed(splashscreenContentDrawer$$ExternalSyntheticLambda1, showingDuration);
        } else {
            splashscreenContentDrawer$$ExternalSyntheticLambda1.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyExitAnimation$8(SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, Runnable runnable) {
        new SplashScreenExitAnimation(this.mContext, splashScreenView, surfaceControl, rect, this.mMainWindowShiftLength, this.mTransactionPool, runnable).startAnimations();
    }

    public static class HighResIconProvider {
        public boolean mLoadInDetail;
        public final Context mSharedContext;
        public final IconProvider mSharedIconProvider;
        public Context mStandaloneContext;
        public IconProvider mStandaloneIconProvider;

        public HighResIconProvider(Context context, IconProvider iconProvider) {
            this.mSharedContext = context;
            this.mSharedIconProvider = iconProvider;
        }

        public Drawable getIcon(ActivityInfo activityInfo, int i, int i2) {
            Drawable drawable;
            this.mLoadInDetail = false;
            if (i >= i2 || i >= 320) {
                drawable = this.mSharedIconProvider.getIcon(activityInfo, i2);
            } else {
                drawable = loadFromStandalone(activityInfo, i, i2);
            }
            return drawable == null ? this.mSharedContext.getPackageManager().getDefaultActivityIcon() : drawable;
        }

        public final Drawable loadFromStandalone(ActivityInfo activityInfo, int i, int i2) {
            Resources resources;
            if (this.mStandaloneContext == null) {
                this.mStandaloneContext = this.mSharedContext.createConfigurationContext(this.mSharedContext.getResources().getConfiguration());
                this.mStandaloneIconProvider = new IconProvider(this.mStandaloneContext);
            }
            try {
                resources = this.mStandaloneContext.getPackageManager().getResourcesForApplication(activityInfo.applicationInfo);
            } catch (PackageManager.NameNotFoundException | Resources.NotFoundException unused) {
                resources = null;
            }
            if (resources != null) {
                updateResourcesDpi(resources, i2);
            }
            Drawable icon = this.mStandaloneIconProvider.getIcon(activityInfo, i2);
            this.mLoadInDetail = true;
            if (resources != null) {
                updateResourcesDpi(resources, i);
            }
            return icon;
        }

        public final void updateResourcesDpi(Resources resources, int i) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.densityDpi = i;
            displayMetrics.densityDpi = i;
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }
}
