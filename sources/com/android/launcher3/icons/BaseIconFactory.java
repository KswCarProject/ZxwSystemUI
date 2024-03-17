package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.UserHandle;
import android.util.SparseBooleanArray;
import com.android.launcher3.icons.BitmapInfo;
import com.android.launcher3.util.FlagOp;

public class BaseIconFactory implements AutoCloseable {
    public static int PLACEHOLDER_BACKGROUND_COLOR = Color.rgb(245, 245, 245);
    public final Canvas mCanvas;
    public final ColorExtractor mColorExtractor;
    public final Context mContext;
    public boolean mDisableColorExtractor;
    public final int mFillResIconDpi;
    public final int mIconBitmapSize;
    public final SparseBooleanArray mIsUserBadged;
    public boolean mMonoIconEnabled;
    public IconNormalizer mNormalizer;
    public final Rect mOldBounds;
    public final PackageManager mPm;
    public ShadowGenerator mShadowGenerator;
    public final boolean mShapeDetection;
    public final Paint mTextPaint;
    public Bitmap mWhiteShadowLayer;
    public int mWrapperBackgroundColor;
    public Drawable mWrapperIcon;

    public static int getBadgeSizeForIconSize(int i) {
        return (int) (((float) i) * 0.444f);
    }

    public BaseIconFactory(Context context, int i, int i2, boolean z) {
        this.mOldBounds = new Rect();
        this.mIsUserBadged = new SparseBooleanArray();
        this.mWrapperBackgroundColor = -1;
        Paint paint = new Paint(3);
        this.mTextPaint = paint;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mShapeDetection = z;
        this.mFillResIconDpi = i;
        this.mIconBitmapSize = i2;
        this.mPm = applicationContext.getPackageManager();
        this.mColorExtractor = new ColorExtractor();
        Canvas canvas = new Canvas();
        this.mCanvas = canvas;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(PLACEHOLDER_BACKGROUND_COLOR);
        paint.setTextSize(context.getResources().getDisplayMetrics().density * 20.0f);
        clear();
    }

    public BaseIconFactory(Context context, int i, int i2) {
        this(context, i, i2, false);
    }

    public void clear() {
        this.mWrapperBackgroundColor = -1;
        this.mDisableColorExtractor = false;
    }

    public ShadowGenerator getShadowGenerator() {
        if (this.mShadowGenerator == null) {
            this.mShadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
        }
        return this.mShadowGenerator;
    }

    public IconNormalizer getNormalizer() {
        if (this.mNormalizer == null) {
            this.mNormalizer = new IconNormalizer(this.mContext, this.mIconBitmapSize, this.mShapeDetection);
        }
        return this.mNormalizer;
    }

    public BitmapInfo createIconBitmap(Bitmap bitmap) {
        if (!(this.mIconBitmapSize == bitmap.getWidth() && this.mIconBitmapSize == bitmap.getHeight())) {
            bitmap = createIconBitmap(new BitmapDrawable(this.mContext.getResources(), bitmap), 1.0f);
        }
        return BitmapInfo.of(bitmap, extractColor(bitmap));
    }

    public BitmapInfo createBadgedIconBitmap(Drawable drawable) {
        return createBadgedIconBitmap(drawable, (IconOptions) null);
    }

    @TargetApi(33)
    public BitmapInfo createBadgedIconBitmap(Drawable drawable, IconOptions iconOptions) {
        Drawable monochrome;
        float[] fArr = new float[1];
        Drawable normalizeAndWrapToAdaptiveIcon = normalizeAndWrapToAdaptiveIcon(drawable, iconOptions == null || iconOptions.mShrinkNonAdaptiveIcons, (RectF) null, fArr);
        Bitmap createIconBitmap = createIconBitmap(normalizeAndWrapToAdaptiveIcon, fArr[0]);
        boolean z = normalizeAndWrapToAdaptiveIcon instanceof AdaptiveIconDrawable;
        if (z) {
            this.mCanvas.setBitmap(createIconBitmap);
            getShadowGenerator().recreateIcon(Bitmap.createBitmap(createIconBitmap), this.mCanvas);
            this.mCanvas.setBitmap((Bitmap) null);
        }
        int extractColor = extractColor(createIconBitmap);
        BitmapInfo of = BitmapInfo.of(createIconBitmap, extractColor);
        if (normalizeAndWrapToAdaptiveIcon instanceof BitmapInfo.Extender) {
            of = ((BitmapInfo.Extender) normalizeAndWrapToAdaptiveIcon).getExtendedInfo(createIconBitmap, extractColor, this, fArr[0]);
        } else if (this.mMonoIconEnabled && IconProvider.ATLEAST_T && z && (monochrome = ((AdaptiveIconDrawable) normalizeAndWrapToAdaptiveIcon).getMonochrome()) != null) {
            of.setMonoIcon(createIconBitmap(new ClippedMonoDrawable(monochrome), fArr[0], this.mIconBitmapSize, Bitmap.Config.ALPHA_8), this);
        }
        return of.withFlags(getBitmapFlagOp(iconOptions));
    }

    public FlagOp getBitmapFlagOp(IconOptions iconOptions) {
        boolean z;
        FlagOp flagOp = FlagOp.NO_OP;
        if (iconOptions == null) {
            return flagOp;
        }
        if (iconOptions.mIsInstantApp) {
            flagOp = flagOp.addFlag(2);
        }
        UserHandle userHandle = iconOptions.mUserHandle;
        if (userHandle == null) {
            return flagOp;
        }
        int hashCode = userHandle.hashCode();
        int indexOfKey = this.mIsUserBadged.indexOfKey(hashCode);
        if (indexOfKey >= 0) {
            z = this.mIsUserBadged.valueAt(indexOfKey);
        } else {
            NoopDrawable noopDrawable = new NoopDrawable();
            boolean z2 = noopDrawable != this.mPm.getUserBadgedIcon(noopDrawable, iconOptions.mUserHandle);
            this.mIsUserBadged.put(hashCode, z2);
            z = z2;
        }
        return flagOp.setFlag(1, z);
    }

    public Bitmap getWhiteShadowLayer() {
        if (this.mWhiteShadowLayer == null) {
            this.mWhiteShadowLayer = createScaledBitmapWithShadow(new AdaptiveIconDrawable(new ColorDrawable(-1), (Drawable) null));
        }
        return this.mWhiteShadowLayer;
    }

    public Bitmap createScaledBitmapWithShadow(Drawable drawable) {
        Bitmap createIconBitmap = createIconBitmap(drawable, getNormalizer().getScale(drawable, (RectF) null, (Path) null, (boolean[]) null));
        this.mCanvas.setBitmap(createIconBitmap);
        getShadowGenerator().recreateIcon(Bitmap.createBitmap(createIconBitmap), this.mCanvas);
        this.mCanvas.setBitmap((Bitmap) null);
        return createIconBitmap;
    }

    public Bitmap createScaledBitmapWithoutShadow(Drawable drawable) {
        RectF rectF = new RectF();
        float[] fArr = new float[1];
        return createIconBitmap(normalizeAndWrapToAdaptiveIcon(drawable, true, rectF, fArr), Math.min(fArr[0], ShadowGenerator.getScaleForBounds(rectF)));
    }

    public final Drawable normalizeAndWrapToAdaptiveIcon(Drawable drawable, boolean z, RectF rectF, float[] fArr) {
        float f;
        if (drawable == null) {
            return null;
        }
        if (!z || (drawable instanceof AdaptiveIconDrawable)) {
            f = getNormalizer().getScale(drawable, rectF, (Path) null, (boolean[]) null);
        } else {
            if (this.mWrapperIcon == null) {
                this.mWrapperIcon = this.mContext.getDrawable(R$drawable.adaptive_icon_drawable_wrapper).mutate();
            }
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) this.mWrapperIcon;
            adaptiveIconDrawable.setBounds(0, 0, 1, 1);
            boolean[] zArr = new boolean[1];
            f = getNormalizer().getScale(drawable, rectF, adaptiveIconDrawable.getIconMask(), zArr);
            if (!zArr[0]) {
                FixedScaleDrawable fixedScaleDrawable = (FixedScaleDrawable) adaptiveIconDrawable.getForeground();
                fixedScaleDrawable.setDrawable(drawable);
                fixedScaleDrawable.setScale(f);
                f = getNormalizer().getScale(adaptiveIconDrawable, rectF, (Path) null, (boolean[]) null);
                ((ColorDrawable) adaptiveIconDrawable.getBackground()).setColor(this.mWrapperBackgroundColor);
                drawable = adaptiveIconDrawable;
            }
        }
        fArr[0] = f;
        return drawable;
    }

    public final Bitmap createIconBitmap(Drawable drawable, float f) {
        return createIconBitmap(drawable, f, this.mIconBitmapSize);
    }

    public Bitmap createIconBitmap(Drawable drawable, float f, int i) {
        return createIconBitmap(drawable, f, i, Bitmap.Config.ARGB_8888);
    }

    public final Bitmap createIconBitmap(Drawable drawable, float f, int i, Bitmap.Config config) {
        int i2;
        int i3;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, config);
        if (drawable == null) {
            return createBitmap;
        }
        this.mCanvas.setBitmap(createBitmap);
        this.mOldBounds.set(drawable.getBounds());
        if (drawable instanceof AdaptiveIconDrawable) {
            float f2 = (float) i;
            int max = Math.max((int) Math.ceil((double) (0.035f * f2)), Math.round((f2 * (1.0f - f)) / 2.0f));
            int i4 = (i - max) - max;
            drawable.setBounds(0, 0, i4, i4);
            int save = this.mCanvas.save();
            float f3 = (float) max;
            this.mCanvas.translate(f3, f3);
            if (drawable instanceof BitmapInfo.Extender) {
                ((BitmapInfo.Extender) drawable).drawForPersistence(this.mCanvas);
            } else {
                drawable.draw(this.mCanvas);
            }
            this.mCanvas.restoreToCount(save);
        } else {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (createBitmap != null && bitmap.getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                }
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                float f4 = ((float) intrinsicWidth) / ((float) intrinsicHeight);
                if (intrinsicWidth > intrinsicHeight) {
                    i2 = (int) (((float) i) / f4);
                    i3 = i;
                } else if (intrinsicHeight > intrinsicWidth) {
                    i3 = (int) (((float) i) * f4);
                    i2 = i;
                }
                int i5 = (i - i3) / 2;
                int i6 = (i - i2) / 2;
                drawable.setBounds(i5, i6, i3 + i5, i2 + i6);
                this.mCanvas.save();
                float f5 = (float) (i / 2);
                this.mCanvas.scale(f, f, f5, f5);
                drawable.draw(this.mCanvas);
                this.mCanvas.restore();
            }
            i3 = i;
            i2 = i3;
            int i52 = (i - i3) / 2;
            int i62 = (i - i2) / 2;
            drawable.setBounds(i52, i62, i3 + i52, i2 + i62);
            this.mCanvas.save();
            float f52 = (float) (i / 2);
            this.mCanvas.scale(f, f, f52, f52);
            drawable.draw(this.mCanvas);
            this.mCanvas.restore();
        }
        drawable.setBounds(this.mOldBounds);
        this.mCanvas.setBitmap((Bitmap) null);
        return createBitmap;
    }

    public void close() {
        clear();
    }

    public final int extractColor(Bitmap bitmap) {
        if (this.mDisableColorExtractor) {
            return 0;
        }
        return this.mColorExtractor.findDominantColorByHue(bitmap);
    }

    public static class IconOptions {
        public boolean mIsInstantApp;
        public boolean mShrinkNonAdaptiveIcons = true;
        public UserHandle mUserHandle;

        public IconOptions setUser(UserHandle userHandle) {
            this.mUserHandle = userHandle;
            return this;
        }
    }

    public static class NoopDrawable extends ColorDrawable {
        public int getIntrinsicHeight() {
            return 1;
        }

        public int getIntrinsicWidth() {
            return 1;
        }

        public NoopDrawable() {
        }
    }

    public static class ClippedMonoDrawable extends InsetDrawable {
        public final AdaptiveIconDrawable mCrop = new AdaptiveIconDrawable(new ColorDrawable(-16777216), (Drawable) null);

        public ClippedMonoDrawable(Drawable drawable) {
            super(drawable, -AdaptiveIconDrawable.getExtraInsetFraction());
        }

        public void draw(Canvas canvas) {
            this.mCrop.setBounds(getBounds());
            int save = canvas.save();
            canvas.clipPath(this.mCrop.getIconMask());
            super.draw(canvas);
            canvas.restoreToCount(save);
        }
    }
}
