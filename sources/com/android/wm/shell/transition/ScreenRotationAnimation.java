package com.android.wm.shell.transition;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.media.Image;
import android.media.ImageReader;
import android.util.RotationUtils;
import android.util.Slog;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.window.TransitionInfo;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ScreenRotationAnimation {
    public final int mAnimHint;
    public SurfaceControl mAnimLeash;
    public SurfaceControl mBackColorSurface;
    public final Context mContext;
    public final Rect mEndBounds;
    public final int mEndHeight;
    public final int mEndRotation;
    public final int mEndWidth;
    public Animation mRotateAlphaAnimation;
    public Animation mRotateEnterAnimation;
    public Animation mRotateExitAnimation;
    public SurfaceControl mScreenshotLayer;
    public final Matrix mSnapshotInitialMatrix = new Matrix();
    public final Rect mStartBounds;
    public final int mStartHeight;
    public float mStartLuma;
    public final int mStartRotation;
    public final int mStartWidth;
    public final SurfaceControl mSurfaceControl;
    public final float[] mTmpFloats = new float[9];
    public SurfaceControl.Transaction mTransaction;
    public final TransactionPool mTransactionPool;

    public ScreenRotationAnimation(Context context, SurfaceSession surfaceSession, TransactionPool transactionPool, SurfaceControl.Transaction transaction, TransitionInfo.Change change, SurfaceControl surfaceControl, int i) {
        Rect rect = new Rect();
        this.mStartBounds = rect;
        Rect rect2 = new Rect();
        this.mEndBounds = rect2;
        this.mContext = context;
        this.mTransactionPool = transactionPool;
        this.mAnimHint = i;
        SurfaceControl leash = change.getLeash();
        this.mSurfaceControl = leash;
        int width = change.getStartAbsBounds().width();
        this.mStartWidth = width;
        int height = change.getStartAbsBounds().height();
        this.mStartHeight = height;
        this.mEndWidth = change.getEndAbsBounds().width();
        this.mEndHeight = change.getEndAbsBounds().height();
        this.mStartRotation = change.getStartRotation();
        this.mEndRotation = change.getEndRotation();
        rect.set(change.getStartAbsBounds());
        rect2.set(change.getEndAbsBounds());
        this.mAnimLeash = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setEffectLayer().setCallsite("ShellRotationAnimation").setName("Animation leash of screenshot rotation").build();
        try {
            SurfaceControl.ScreenshotHardwareBuffer captureLayers = SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(leash).setCaptureSecureLayers(true).setAllowProtected(true).setSourceCrop(new Rect(0, 0, width, height)).build());
            if (captureLayers == null) {
                Slog.w("ShellTransitions", "Unable to take screenshot of display");
                return;
            }
            this.mScreenshotLayer = new SurfaceControl.Builder(surfaceSession).setParent(this.mAnimLeash).setBLASTLayer().setSecure(captureLayers.containsSecureLayers()).setCallsite("ShellRotationAnimation").setName("RotationLayer").build();
            GraphicBuffer createFromHardwareBuffer = GraphicBuffer.createFromHardwareBuffer(captureLayers.getHardwareBuffer());
            transaction.setLayer(this.mAnimLeash, 2010000);
            transaction.setPosition(this.mAnimLeash, 0.0f, 0.0f);
            transaction.setAlpha(this.mAnimLeash, 1.0f);
            transaction.show(this.mAnimLeash);
            transaction.setBuffer(this.mScreenshotLayer, createFromHardwareBuffer);
            transaction.setColorSpace(this.mScreenshotLayer, captureLayers.getColorSpace());
            transaction.show(this.mScreenshotLayer);
            if (!isCustomRotate()) {
                this.mBackColorSurface = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setColorLayer().setCallsite("ShellRotationAnimation").setName("BackColorSurface").build();
                this.mStartLuma = getMedianBorderLuma(captureLayers.getHardwareBuffer(), captureLayers.getColorSpace());
                transaction.setLayer(this.mBackColorSurface, -1);
                SurfaceControl surfaceControl2 = this.mBackColorSurface;
                float f = this.mStartLuma;
                transaction.setColor(surfaceControl2, new float[]{f, f, f});
                transaction.setAlpha(this.mBackColorSurface, 1.0f);
                transaction.show(this.mBackColorSurface);
            }
            setRotation(transaction);
            transaction.apply();
        } catch (Surface.OutOfResourcesException e) {
            Slog.w("ShellTransitions", "Unable to allocate freeze surface", e);
        }
    }

    public final boolean isCustomRotate() {
        int i = this.mAnimHint;
        return i == 1 || i == 2;
    }

    public final void setRotation(SurfaceControl.Transaction transaction) {
        createRotationMatrix(RotationUtils.deltaRotation(this.mEndRotation, this.mStartRotation), this.mStartWidth, this.mStartHeight, this.mSnapshotInitialMatrix);
        setRotationTransform(transaction, this.mSnapshotInitialMatrix);
    }

    public final void setRotationTransform(SurfaceControl.Transaction transaction, Matrix matrix) {
        if (this.mScreenshotLayer != null) {
            matrix.getValues(this.mTmpFloats);
            float[] fArr = this.mTmpFloats;
            transaction.setPosition(this.mScreenshotLayer, fArr[2], fArr[5]);
            SurfaceControl surfaceControl = this.mScreenshotLayer;
            float[] fArr2 = this.mTmpFloats;
            transaction.setMatrix(surfaceControl, fArr2[0], fArr2[3], fArr2[1], fArr2[4]);
            transaction.setAlpha(this.mScreenshotLayer, 1.0f);
            transaction.show(this.mScreenshotLayer);
        }
    }

    public boolean startAnimation(ArrayList<Animator> arrayList, Runnable runnable, float f, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        if (this.mScreenshotLayer == null) {
            return false;
        }
        boolean isCustomRotate = isCustomRotate();
        if (isCustomRotate) {
            this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, this.mAnimHint == 2 ? 17432711 : 17432712);
            this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432710);
            this.mRotateAlphaAnimation = AnimationUtils.loadAnimation(this.mContext, 17432718);
        } else {
            int deltaRotation = RotationUtils.deltaRotation(this.mEndRotation, this.mStartRotation);
            if (deltaRotation == 0) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432714);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432710);
            } else if (deltaRotation == 1) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432725);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432724);
            } else if (deltaRotation == 2) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432716);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432715);
            } else if (deltaRotation == 3) {
                this.mRotateExitAnimation = AnimationUtils.loadAnimation(this.mContext, 17432723);
                this.mRotateEnterAnimation = AnimationUtils.loadAnimation(this.mContext, 17432722);
            }
        }
        this.mRotateExitAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
        this.mRotateExitAnimation.restrictDuration(10000);
        this.mRotateExitAnimation.scaleCurrentDuration(f);
        this.mRotateEnterAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
        this.mRotateEnterAnimation.restrictDuration(10000);
        this.mRotateEnterAnimation.scaleCurrentDuration(f);
        this.mTransaction = this.mTransactionPool.acquire();
        if (isCustomRotate) {
            this.mRotateAlphaAnimation.initialize(this.mEndWidth, this.mEndHeight, this.mStartWidth, this.mStartHeight);
            this.mRotateAlphaAnimation.restrictDuration(10000);
            this.mRotateAlphaAnimation.scaleCurrentDuration(f);
            startScreenshotAlphaAnimation(arrayList, runnable, shellExecutor, shellExecutor2);
            startDisplayRotation(arrayList, runnable, shellExecutor, shellExecutor2);
        } else {
            startDisplayRotation(arrayList, runnable, shellExecutor, shellExecutor2);
            startScreenshotRotationAnimation(arrayList, runnable, shellExecutor, shellExecutor2);
        }
        return true;
    }

    public final void startDisplayRotation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateEnterAnimation, this.mSurfaceControl, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    public final void startScreenshotRotationAnimation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateExitAnimation, this.mAnimLeash, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    public final void startScreenshotAlphaAnimation(ArrayList<Animator> arrayList, Runnable runnable, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        DefaultTransitionHandler.startSurfaceAnimation(arrayList, this.mRotateAlphaAnimation, this.mAnimLeash, runnable, this.mTransactionPool, shellExecutor, shellExecutor2, (Point) null, 0.0f, (Rect) null);
    }

    public void kill() {
        SurfaceControl.Transaction transaction = this.mTransaction;
        if (transaction == null) {
            transaction = this.mTransactionPool.acquire();
        }
        if (this.mAnimLeash.isValid()) {
            transaction.remove(this.mAnimLeash);
        }
        SurfaceControl surfaceControl = this.mScreenshotLayer;
        if (surfaceControl != null) {
            if (surfaceControl.isValid()) {
                transaction.remove(this.mScreenshotLayer);
            }
            this.mScreenshotLayer = null;
        }
        SurfaceControl surfaceControl2 = this.mBackColorSurface;
        if (surfaceControl2 != null) {
            if (surfaceControl2.isValid()) {
                transaction.remove(this.mBackColorSurface);
            }
            this.mBackColorSurface = null;
        }
        transaction.apply();
        this.mTransactionPool.release(transaction);
    }

    public static float getMedianBorderLuma(HardwareBuffer hardwareBuffer, ColorSpace colorSpace) {
        if (hardwareBuffer != null && hardwareBuffer.getFormat() == 1 && !hasProtectedContent(hardwareBuffer)) {
            ImageReader newInstance = ImageReader.newInstance(hardwareBuffer.getWidth(), hardwareBuffer.getHeight(), hardwareBuffer.getFormat(), 1);
            newInstance.getSurface().attachAndQueueBufferWithColorSpace(hardwareBuffer, colorSpace);
            Image acquireLatestImage = newInstance.acquireLatestImage();
            if (!(acquireLatestImage == null || acquireLatestImage.getPlanes().length == 0)) {
                Image.Plane plane = acquireLatestImage.getPlanes()[0];
                ByteBuffer buffer = plane.getBuffer();
                int width = acquireLatestImage.getWidth();
                int height = acquireLatestImage.getHeight();
                int pixelStride = plane.getPixelStride();
                int rowStride = plane.getRowStride();
                int i = (width * 2) + (height * 2);
                float[] fArr = new float[i];
                int i2 = 0;
                for (int i3 = 0; i3 < width; i3++) {
                    int i4 = i2 + 1;
                    fArr[i2] = getPixelLuminance(buffer, i3, 0, pixelStride, rowStride);
                    i2 = i4 + 1;
                    fArr[i4] = getPixelLuminance(buffer, i3, height - 1, pixelStride, rowStride);
                }
                for (int i5 = 0; i5 < height; i5++) {
                    int i6 = i2 + 1;
                    fArr[i2] = getPixelLuminance(buffer, 0, i5, pixelStride, rowStride);
                    i2 = i6 + 1;
                    fArr[i6] = getPixelLuminance(buffer, width - 1, i5, pixelStride, rowStride);
                }
                newInstance.close();
                Arrays.sort(fArr);
                return fArr[i / 2];
            }
        }
        return 0.0f;
    }

    public static boolean hasProtectedContent(HardwareBuffer hardwareBuffer) {
        return (hardwareBuffer.getUsage() & 16384) == 16384;
    }

    public static float getPixelLuminance(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        int i5 = (i2 * i4) + (i * i3);
        return Color.valueOf(((byteBuffer.get(i5 + 3) & 255) << 24) | ((byteBuffer.get(i5) & 255) << 16) | 0 | ((byteBuffer.get(i5 + 1) & 255) << 8) | (byteBuffer.get(i5 + 2) & 255)).luminance();
    }

    public static void createRotationMatrix(int i, int i2, int i3, Matrix matrix) {
        if (i == 0) {
            matrix.reset();
        } else if (i == 1) {
            matrix.setRotate(90.0f, 0.0f, 0.0f);
            matrix.postTranslate((float) i3, 0.0f);
        } else if (i == 2) {
            matrix.setRotate(180.0f, 0.0f, 0.0f);
            matrix.postTranslate((float) i2, (float) i3);
        } else if (i == 3) {
            matrix.setRotate(270.0f, 0.0f, 0.0f);
            matrix.postTranslate(0.0f, (float) i2);
        }
    }
}
