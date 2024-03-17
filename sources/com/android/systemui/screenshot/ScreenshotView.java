package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Looper;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.DisplayCutout;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScrollCaptureResponse;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.screenshot.DraggableConstraintLayout;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.InputMonitorCompat;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class ScreenshotView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final String TAG = LogConfig.logTag(ScreenshotView.class);
    public final Interpolator mAccelerateInterpolator;
    public final AccessibilityManager mAccessibilityManager;
    public HorizontalScrollView mActionsContainer;
    public ImageView mActionsContainerBackground;
    public LinearLayout mActionsView;
    public ScreenshotViewCallback mCallbacks;
    public long mDefaultTimeoutOfTimeoutHandler;
    public boolean mDirectionLTR;
    public FrameLayout mDismissButton;
    public final DisplayMetrics mDisplayMetrics;
    public OverlayActionChip mEditChip;
    public final Interpolator mFastOutSlowIn;
    public final float mFixedSize;
    public InputChannelCompat$InputEventReceiver mInputEventReceiver;
    public InputMonitorCompat mInputMonitor;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public int mNavMode;
    public boolean mOrientationPortrait;
    public String mPackageName;
    public PendingInteraction mPendingInteraction;
    public boolean mPendingSharedTransition;
    public OverlayActionChip mQuickShareChip;
    public final Resources mResources;
    public ImageView mScreenshotFlash;
    public ImageView mScreenshotPreview;
    public View mScreenshotPreviewBorder;
    public ScreenshotSelectorView mScreenshotSelectorView;
    public DraggableConstraintLayout mScreenshotStatic;
    public OverlayActionChip mScrollChip;
    public ImageView mScrollablePreview;
    public ImageView mScrollingScrim;
    public OverlayActionChip mShareChip;
    public boolean mShowScrollablePreview;
    public final ArrayList<OverlayActionChip> mSmartChips;
    public final GestureDetector mSwipeDetector;
    public UiEventLogger mUiEventLogger;

    public enum PendingInteraction {
        PREVIEW,
        EDIT,
        SHARE,
        QUICK_SHARE
    }

    public interface ScreenshotViewCallback {
        void onDismiss();

        void onTouchOutside();

        void onUserInteraction();
    }

    public ScreenshotView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAccelerateInterpolator = new AccelerateInterpolator();
        this.mPackageName = "";
        this.mSmartChips = new ArrayList<>();
        Resources resources = this.mContext.getResources();
        this.mResources = resources;
        this.mInteractionJankMonitor = getInteractionJankMonitorInstance();
        this.mFixedSize = (float) resources.getDimensionPixelSize(R$dimen.overlay_x_scale);
        this.mFastOutSlowIn = AnimationUtils.loadInterpolator(this.mContext, 17563661);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        this.mContext.getDisplay().getRealMetrics(displayMetrics);
        this.mAccessibilityManager = AccessibilityManager.getInstance(this.mContext);
        GestureDetector gestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() {
            public final Rect mActionsRect = new Rect();

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                ScreenshotView.this.mActionsContainer.getBoundsOnScreen(this.mActionsRect);
                return !this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !ScreenshotView.this.mActionsContainer.canScrollHorizontally((int) f);
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View view) {
                ScreenshotView.this.startInputListening();
            }

            public void onViewDetachedFromWindow(View view) {
                ScreenshotView.this.stopInputListening();
            }
        });
    }

    public final InteractionJankMonitor getInteractionJankMonitorInstance() {
        return InteractionJankMonitor.getInstance();
    }

    public void setDefaultTimeoutMillis(long j) {
        this.mDefaultTimeoutOfTimeoutHandler = j;
    }

    public void hideScrollChip() {
        this.mScrollChip.setVisibility(8);
    }

    public void showScrollChip(String str, Runnable runnable) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_IMPRESSION, 0, str);
        this.mScrollChip.setVisibility(0);
        this.mScrollChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda5(this, str, runnable));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showScrollChip$0(String str, Runnable runnable, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_REQUESTED, 0, str);
        runnable.run();
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(getTouchRegion(true));
    }

    public final Region getSwipeRegion() {
        Region region = new Region();
        Rect rect = new Rect();
        this.mScreenshotPreview.getBoundsOnScreen(rect);
        rect.inset((int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f));
        region.op(rect, Region.Op.UNION);
        this.mActionsContainerBackground.getBoundsOnScreen(rect);
        rect.inset((int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, -12.0f));
        region.op(rect, Region.Op.UNION);
        this.mDismissButton.getBoundsOnScreen(rect);
        region.op(rect, Region.Op.UNION);
        return region;
    }

    public final Region getTouchRegion(boolean z) {
        Region swipeRegion = getSwipeRegion();
        if (z && this.mScrollingScrim.getVisibility() == 0) {
            Rect rect = new Rect();
            this.mScrollingScrim.getBoundsOnScreen(rect);
            swipeRegion.op(rect, Region.Op.UNION);
        }
        if (QuickStepContract.isGesturalMode(this.mNavMode)) {
            Insets insets = ((WindowManager) this.mContext.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
            Rect rect2 = new Rect(0, 0, insets.left, this.mDisplayMetrics.heightPixels);
            swipeRegion.op(rect2, Region.Op.UNION);
            DisplayMetrics displayMetrics = this.mDisplayMetrics;
            int i = displayMetrics.widthPixels;
            rect2.set(i - insets.right, 0, i, displayMetrics.heightPixels);
            swipeRegion.op(rect2, Region.Op.UNION);
        }
        return swipeRegion;
    }

    public final void startInputListening() {
        stopInputListening();
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat("Screenshot", 0);
        this.mInputMonitor = inputMonitorCompat;
        this.mInputEventReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new ScreenshotView$$ExternalSyntheticLambda10(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startInputListening$1(InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            if (motionEvent.getActionMasked() == 0 && !getTouchRegion(false).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                this.mCallbacks.onTouchOutside();
            }
        }
    }

    public void stopInputListening() {
        InputMonitorCompat inputMonitorCompat = this.mInputMonitor;
        if (inputMonitorCompat != null) {
            inputMonitorCompat.dispose();
            this.mInputMonitor = null;
        }
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.mInputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
    }

    public void onFinishInflate() {
        ImageView imageView = (ImageView) findViewById(R$id.screenshot_scrolling_scrim);
        Objects.requireNonNull(imageView);
        ImageView imageView2 = imageView;
        this.mScrollingScrim = imageView;
        DraggableConstraintLayout draggableConstraintLayout = (DraggableConstraintLayout) findViewById(R$id.screenshot_static);
        Objects.requireNonNull(draggableConstraintLayout);
        DraggableConstraintLayout draggableConstraintLayout2 = draggableConstraintLayout;
        this.mScreenshotStatic = draggableConstraintLayout;
        ImageView imageView3 = (ImageView) findViewById(R$id.screenshot_preview);
        Objects.requireNonNull(imageView3);
        ImageView imageView4 = imageView3;
        this.mScreenshotPreview = imageView3;
        View findViewById = findViewById(R$id.screenshot_preview_border);
        Objects.requireNonNull(findViewById);
        View view = findViewById;
        this.mScreenshotPreviewBorder = findViewById;
        this.mScreenshotPreview.setClipToOutline(true);
        ImageView imageView5 = (ImageView) findViewById(R$id.actions_container_background);
        Objects.requireNonNull(imageView5);
        ImageView imageView6 = imageView5;
        this.mActionsContainerBackground = imageView5;
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R$id.actions_container);
        Objects.requireNonNull(horizontalScrollView);
        HorizontalScrollView horizontalScrollView2 = horizontalScrollView;
        this.mActionsContainer = horizontalScrollView;
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.screenshot_actions);
        Objects.requireNonNull(linearLayout);
        LinearLayout linearLayout2 = linearLayout;
        this.mActionsView = linearLayout;
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.screenshot_dismiss_button);
        Objects.requireNonNull(frameLayout);
        FrameLayout frameLayout2 = frameLayout;
        this.mDismissButton = frameLayout;
        ImageView imageView7 = (ImageView) findViewById(R$id.screenshot_scrollable_preview);
        Objects.requireNonNull(imageView7);
        ImageView imageView8 = imageView7;
        this.mScrollablePreview = imageView7;
        ImageView imageView9 = (ImageView) findViewById(R$id.screenshot_flash);
        Objects.requireNonNull(imageView9);
        ImageView imageView10 = imageView9;
        this.mScreenshotFlash = imageView9;
        ScreenshotSelectorView screenshotSelectorView = (ScreenshotSelectorView) findViewById(R$id.screenshot_selector);
        Objects.requireNonNull(screenshotSelectorView);
        ScreenshotSelectorView screenshotSelectorView2 = screenshotSelectorView;
        this.mScreenshotSelectorView = screenshotSelectorView;
        OverlayActionChip overlayActionChip = (OverlayActionChip) this.mActionsContainer.findViewById(R$id.screenshot_share_chip);
        Objects.requireNonNull(overlayActionChip);
        OverlayActionChip overlayActionChip2 = overlayActionChip;
        this.mShareChip = overlayActionChip;
        OverlayActionChip overlayActionChip3 = (OverlayActionChip) this.mActionsContainer.findViewById(R$id.screenshot_edit_chip);
        Objects.requireNonNull(overlayActionChip3);
        OverlayActionChip overlayActionChip4 = overlayActionChip3;
        this.mEditChip = overlayActionChip3;
        OverlayActionChip overlayActionChip5 = (OverlayActionChip) this.mActionsContainer.findViewById(R$id.screenshot_scroll_chip);
        Objects.requireNonNull(overlayActionChip5);
        OverlayActionChip overlayActionChip6 = overlayActionChip5;
        this.mScrollChip = overlayActionChip5;
        int dpToPx = (int) FloatingWindowUtil.dpToPx(this.mDisplayMetrics, 12.0f);
        this.mScreenshotPreview.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mScreenshotPreview));
        this.mActionsContainerBackground.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mActionsContainerBackground));
        setFocusable(true);
        this.mScreenshotSelectorView.setFocusable(true);
        this.mScreenshotSelectorView.setFocusableInTouchMode(true);
        boolean z = false;
        this.mActionsContainer.setScrollX(0);
        this.mNavMode = getResources().getInteger(17694881);
        this.mOrientationPortrait = getResources().getConfiguration().orientation == 1;
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            z = true;
        }
        this.mDirectionLTR = z;
        setFocusableInTouchMode(true);
        requestFocus();
        this.mScreenshotStatic.setCallbacks(new DraggableConstraintLayout.SwipeDismissCallbacks() {
            public void onInteraction() {
                ScreenshotView.this.mCallbacks.onUserInteraction();
            }

            public void onSwipeDismissInitiated(Animator animator) {
                ScreenshotView.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SWIPE_DISMISSED, 0, ScreenshotView.this.mPackageName);
            }

            public void onDismissComplete() {
                if (ScreenshotView.this.mInteractionJankMonitor.isInstrumenting(54)) {
                    ScreenshotView.this.mInteractionJankMonitor.end(54);
                }
                ScreenshotView.this.mCallbacks.onDismiss();
            }
        });
    }

    public View getScreenshotPreview() {
        return this.mScreenshotPreview;
    }

    public void init(UiEventLogger uiEventLogger, ScreenshotViewCallback screenshotViewCallback) {
        this.mUiEventLogger = uiEventLogger;
        this.mCallbacks = screenshotViewCallback;
    }

    public void takePartialScreenshot(Consumer<Rect> consumer) {
        this.mScreenshotSelectorView.setOnScreenshotSelected(consumer);
        this.mScreenshotSelectorView.setVisibility(0);
        this.mScreenshotSelectorView.requestFocus();
    }

    public void setScreenshot(Bitmap bitmap, Insets insets) {
        this.mScreenshotPreview.setImageDrawable(createScreenDrawable(this.mResources, bitmap, insets));
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }

    public void updateInsets(WindowInsets windowInsets) {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mScreenshotStatic.getLayoutParams();
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Insets insets = windowInsets.getInsets(WindowInsets.Type.navigationBars());
        if (displayCutout == null) {
            layoutParams.setMargins(0, 0, 0, insets.bottom);
        } else {
            Insets waterfallInsets = displayCutout.getWaterfallInsets();
            if (this.mOrientationPortrait) {
                layoutParams.setMargins(waterfallInsets.left, Math.max(displayCutout.getSafeInsetTop(), waterfallInsets.top), waterfallInsets.right, Math.max(displayCutout.getSafeInsetBottom(), Math.max(insets.bottom, waterfallInsets.bottom)));
            } else {
                layoutParams.setMargins(Math.max(displayCutout.getSafeInsetLeft(), waterfallInsets.left), waterfallInsets.top, Math.max(displayCutout.getSafeInsetRight(), waterfallInsets.right), Math.max(insets.bottom, waterfallInsets.bottom));
            }
        }
        this.mScreenshotStatic.setLayoutParams(layoutParams);
        this.mScreenshotStatic.requestLayout();
    }

    public void updateOrientation(WindowInsets windowInsets) {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        updateInsets(windowInsets);
        ViewGroup.LayoutParams layoutParams = this.mScreenshotPreview.getLayoutParams();
        if (this.mOrientationPortrait) {
            layoutParams.width = (int) this.mFixedSize;
            layoutParams.height = -2;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            layoutParams.width = -2;
            layoutParams.height = (int) this.mFixedSize;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_END);
        }
        this.mScreenshotPreview.setLayoutParams(layoutParams);
    }

    public AnimatorSet createScreenshotDropInAnimation(Rect rect, boolean z) {
        Rect rect2 = new Rect();
        this.mScreenshotPreview.getHitRect(rect2);
        final float width = this.mFixedSize / ((float) (this.mOrientationPortrait ? rect.width() : rect.height()));
        final float f = 1.0f / width;
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(133);
        ofFloat.setInterpolator(this.mFastOutSlowIn);
        ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda11(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        ofFloat2.setDuration(217);
        ofFloat2.setInterpolator(this.mFastOutSlowIn);
        ofFloat2.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda12(this));
        PointF pointF = new PointF((float) rect.centerX(), (float) rect.centerY());
        final PointF pointF2 = new PointF(rect2.exactCenterX(), rect2.exactCenterY());
        int[] locationOnScreen = this.mScreenshotPreview.getLocationOnScreen();
        pointF.offset((float) (rect2.left - locationOnScreen[0]), (float) (rect2.top - locationOnScreen[1]));
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat3.setDuration(500);
        ofFloat3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                ScreenshotView.this.mScreenshotPreview.setScaleX(f);
                ScreenshotView.this.mScreenshotPreview.setScaleY(f);
                ScreenshotView.this.mScreenshotPreview.setVisibility(0);
                if (ScreenshotView.this.mAccessibilityManager.isEnabled()) {
                    ScreenshotView.this.mDismissButton.setAlpha(0.0f);
                    ScreenshotView.this.mDismissButton.setVisibility(0);
                }
            }
        });
        ScreenshotView$$ExternalSyntheticLambda13 screenshotView$$ExternalSyntheticLambda13 = r0;
        ValueAnimator valueAnimator = ofFloat3;
        ScreenshotView$$ExternalSyntheticLambda13 screenshotView$$ExternalSyntheticLambda132 = new ScreenshotView$$ExternalSyntheticLambda13(this, 0.468f, f, 0.468f, pointF, pointF2, 0.4f);
        valueAnimator.addUpdateListener(screenshotView$$ExternalSyntheticLambda13);
        this.mScreenshotFlash.setAlpha(0.0f);
        this.mScreenshotFlash.setVisibility(0);
        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat4.setDuration(100);
        ofFloat4.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda14(this));
        if (z) {
            animatorSet.play(ofFloat2).after(ofFloat);
            animatorSet.play(ofFloat2).with(valueAnimator);
        } else {
            animatorSet.play(valueAnimator);
        }
        animatorSet.play(ofFloat4).after(valueAnimator);
        final Rect rect3 = rect;
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                ScreenshotView.this.mInteractionJankMonitor.cancel(54);
            }

            public void onAnimationStart(Animator animator) {
                ScreenshotView.this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(54, ScreenshotView.this.mScreenshotPreview).setTag("DropIn"));
            }

            public void onAnimationEnd(Animator animator) {
                float f;
                ScreenshotView.this.mDismissButton.setOnClickListener(new ScreenshotView$5$$ExternalSyntheticLambda0(this));
                ScreenshotView.this.mDismissButton.setAlpha(1.0f);
                float width = ((float) ScreenshotView.this.mDismissButton.getWidth()) / 2.0f;
                if (ScreenshotView.this.mDirectionLTR) {
                    f = (pointF2.x - width) + ((((float) rect3.width()) * width) / 2.0f);
                } else {
                    f = (pointF2.x - width) - ((((float) rect3.width()) * width) / 2.0f);
                }
                ScreenshotView.this.mDismissButton.setX(f);
                ScreenshotView.this.mDismissButton.setY((pointF2.y - width) - ((((float) rect3.height()) * width) / 2.0f));
                ScreenshotView.this.mScreenshotPreview.setScaleX(1.0f);
                ScreenshotView.this.mScreenshotPreview.setScaleY(1.0f);
                ScreenshotView.this.mScreenshotPreview.setX(pointF2.x - (((float) ScreenshotView.this.mScreenshotPreview.getWidth()) / 2.0f));
                ScreenshotView.this.mScreenshotPreview.setY(pointF2.y - (((float) ScreenshotView.this.mScreenshotPreview.getHeight()) / 2.0f));
                ScreenshotView.this.requestLayout();
                ScreenshotView.this.mInteractionJankMonitor.end(54);
                ScreenshotView.this.createScreenshotActionsShadeAnimation().start();
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationEnd$0(View view) {
                ScreenshotView.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_EXPLICIT_DISMISSAL, 0, ScreenshotView.this.mPackageName);
                ScreenshotView.this.animateDismissal();
            }
        });
        return animatorSet;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$2(ValueAnimator valueAnimator) {
        this.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$3(ValueAnimator valueAnimator) {
        this.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$4(float f, float f2, float f3, PointF pointF, PointF pointF2, float f4, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (animatedFraction < f) {
            float lerp = MathUtils.lerp(f2, 1.0f, this.mFastOutSlowIn.getInterpolation(animatedFraction / f));
            this.mScreenshotPreview.setScaleX(lerp);
            this.mScreenshotPreview.setScaleY(lerp);
        } else {
            this.mScreenshotPreview.setScaleX(1.0f);
            this.mScreenshotPreview.setScaleY(1.0f);
        }
        if (animatedFraction < f3) {
            float lerp2 = MathUtils.lerp(pointF.x, pointF2.x, this.mFastOutSlowIn.getInterpolation(animatedFraction / f3));
            ImageView imageView = this.mScreenshotPreview;
            imageView.setX(lerp2 - (((float) imageView.getWidth()) / 2.0f));
        } else {
            ImageView imageView2 = this.mScreenshotPreview;
            imageView2.setX(pointF2.x - (((float) imageView2.getWidth()) / 2.0f));
        }
        float lerp3 = MathUtils.lerp(pointF.y, pointF2.y, this.mFastOutSlowIn.getInterpolation(animatedFraction));
        ImageView imageView3 = this.mScreenshotPreview;
        imageView3.setY(lerp3 - (((float) imageView3.getHeight()) / 2.0f));
        if (animatedFraction >= f4) {
            this.mDismissButton.setAlpha((animatedFraction - f4) / (1.0f - f4));
            float x = this.mScreenshotPreview.getX();
            float y = this.mScreenshotPreview.getY();
            FrameLayout frameLayout = this.mDismissButton;
            frameLayout.setY(y - (((float) frameLayout.getHeight()) / 2.0f));
            if (this.mDirectionLTR) {
                this.mDismissButton.setX((x + ((float) this.mScreenshotPreview.getWidth())) - (((float) this.mDismissButton.getWidth()) / 2.0f));
                return;
            }
            FrameLayout frameLayout2 = this.mDismissButton;
            frameLayout2.setX(x - (((float) frameLayout2.getWidth()) / 2.0f));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$5(ValueAnimator valueAnimator) {
        this.mScreenshotPreviewBorder.setAlpha(valueAnimator.getAnimatedFraction());
    }

    public ValueAnimator createScreenshotActionsShadeAnimation() {
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        ArrayList arrayList = new ArrayList();
        this.mShareChip.setContentDescription(this.mContext.getString(R$string.screenshot_share_description));
        this.mShareChip.setIcon(Icon.createWithResource(this.mContext, R$drawable.ic_screenshot_share), true);
        this.mShareChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda0(this));
        arrayList.add(this.mShareChip);
        this.mEditChip.setContentDescription(this.mContext.getString(R$string.screenshot_edit_description));
        this.mEditChip.setIcon(Icon.createWithResource(this.mContext, R$drawable.ic_screenshot_edit), true);
        this.mEditChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda1(this));
        arrayList.add(this.mEditChip);
        this.mScreenshotPreview.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda2(this));
        this.mScrollChip.setText(this.mContext.getString(R$string.screenshot_scroll_label));
        this.mScrollChip.setIcon(Icon.createWithResource(this.mContext, R$drawable.ic_screenshot_scroll), true);
        arrayList.add(this.mScrollChip);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mActionsView.getChildAt(0).getLayoutParams();
        layoutParams.setMarginEnd(0);
        this.mActionsView.getChildAt(0).setLayoutParams(layoutParams);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(400);
        this.mActionsContainer.setAlpha(0.0f);
        this.mActionsContainerBackground.setAlpha(0.0f);
        this.mActionsContainer.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                ScreenshotView.this.mInteractionJankMonitor.cancel(54);
            }

            public void onAnimationEnd(Animator animator) {
                ScreenshotView.this.mInteractionJankMonitor.end(54);
            }

            public void onAnimationStart(Animator animator) {
                ScreenshotView.this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(54, ScreenshotView.this.mScreenshotStatic).setTag("Actions").setTimeout(ScreenshotView.this.mDefaultTimeoutOfTimeoutHandler));
            }
        });
        ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda3(this, 0.25f, arrayList));
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$6(View view) {
        this.mShareChip.setIsPending(true);
        this.mEditChip.setIsPending(false);
        OverlayActionChip overlayActionChip = this.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.SHARE;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$7(View view) {
        this.mEditChip.setIsPending(true);
        this.mShareChip.setIsPending(false);
        OverlayActionChip overlayActionChip = this.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.EDIT;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$8(View view) {
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        OverlayActionChip overlayActionChip = this.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.PREVIEW;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$9(float f, ArrayList arrayList, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f2 = animatedFraction < f ? animatedFraction / f : 1.0f;
        this.mActionsContainer.setAlpha(f2);
        this.mActionsContainerBackground.setAlpha(f2);
        float f3 = (0.3f * animatedFraction) + 0.7f;
        this.mActionsContainer.setScaleX(f3);
        this.mActionsContainerBackground.setScaleX(f3);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            OverlayActionChip overlayActionChip = (OverlayActionChip) it.next();
            overlayActionChip.setAlpha(animatedFraction);
            overlayActionChip.setScaleX(1.0f / f3);
        }
        HorizontalScrollView horizontalScrollView = this.mActionsContainer;
        horizontalScrollView.setScrollX(this.mDirectionLTR ? 0 : horizontalScrollView.getWidth());
        HorizontalScrollView horizontalScrollView2 = this.mActionsContainer;
        float f4 = 0.0f;
        horizontalScrollView2.setPivotX(this.mDirectionLTR ? 0.0f : (float) horizontalScrollView2.getWidth());
        ImageView imageView = this.mActionsContainerBackground;
        if (!this.mDirectionLTR) {
            f4 = (float) imageView.getWidth();
        }
        imageView.setPivotX(f4);
    }

    public void setChipIntents(ScreenshotController.SavedImageData savedImageData) {
        this.mShareChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda15(this, savedImageData));
        this.mEditChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda16(this, savedImageData));
        this.mScreenshotPreview.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda17(this, savedImageData));
        OverlayActionChip overlayActionChip = this.mQuickShareChip;
        if (overlayActionChip != null) {
            overlayActionChip.setPendingIntent(savedImageData.quickShareAction.actionIntent, new ScreenshotView$$ExternalSyntheticLambda18(this));
        }
        PendingInteraction pendingInteraction = this.mPendingInteraction;
        if (pendingInteraction != null) {
            int i = AnonymousClass10.$SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction[pendingInteraction.ordinal()];
            if (i == 1) {
                this.mScreenshotPreview.callOnClick();
            } else if (i == 2) {
                this.mShareChip.callOnClick();
            } else if (i == 3) {
                this.mEditChip.callOnClick();
            } else if (i == 4) {
                this.mQuickShareChip.callOnClick();
            }
        } else {
            LayoutInflater from = LayoutInflater.from(this.mContext);
            for (Notification.Action next : savedImageData.smartActions) {
                OverlayActionChip overlayActionChip2 = (OverlayActionChip) from.inflate(R$layout.overlay_action_chip, this.mActionsView, false);
                overlayActionChip2.setText(next.title);
                overlayActionChip2.setIcon(next.getIcon(), false);
                overlayActionChip2.setPendingIntent(next.actionIntent, new ScreenshotView$$ExternalSyntheticLambda19(this));
                overlayActionChip2.setAlpha(1.0f);
                LinearLayout linearLayout = this.mActionsView;
                linearLayout.addView(overlayActionChip2, linearLayout.getChildCount() - 1);
                this.mSmartChips.add(overlayActionChip2);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$10(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SHARE_TAPPED, 0, this.mPackageName);
        startSharedTransition(savedImageData.shareTransition.get());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$11(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_EDIT_TAPPED, 0, this.mPackageName);
        startSharedTransition(savedImageData.editTransition.get());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$12(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_PREVIEW_TAPPED, 0, this.mPackageName);
        startSharedTransition(savedImageData.editTransition.get());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$13() {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SMART_ACTION_TAPPED, 0, this.mPackageName);
        animateDismissal();
    }

    /* renamed from: com.android.systemui.screenshot.ScreenshotView$10  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass10 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.android.systemui.screenshot.ScreenshotView$PendingInteraction[] r0 = com.android.systemui.screenshot.ScreenshotView.PendingInteraction.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction = r0
                com.android.systemui.screenshot.ScreenshotView$PendingInteraction r1 = com.android.systemui.screenshot.ScreenshotView.PendingInteraction.PREVIEW     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.screenshot.ScreenshotView$PendingInteraction r1 = com.android.systemui.screenshot.ScreenshotView.PendingInteraction.SHARE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.screenshot.ScreenshotView$PendingInteraction r1 = com.android.systemui.screenshot.ScreenshotView.PendingInteraction.EDIT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.screenshot.ScreenshotView$PendingInteraction r1 = com.android.systemui.screenshot.ScreenshotView.PendingInteraction.QUICK_SHARE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenshot.ScreenshotView.AnonymousClass10.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$14() {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SMART_ACTION_TAPPED, 0, this.mPackageName);
        animateDismissal();
    }

    public void addQuickShareChip(Notification.Action action) {
        if (this.mPendingInteraction == null) {
            OverlayActionChip overlayActionChip = (OverlayActionChip) LayoutInflater.from(this.mContext).inflate(R$layout.overlay_action_chip, this.mActionsView, false);
            this.mQuickShareChip = overlayActionChip;
            overlayActionChip.setText(action.title);
            this.mQuickShareChip.setIcon(action.getIcon(), false);
            this.mQuickShareChip.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda20(this));
            this.mQuickShareChip.setAlpha(1.0f);
            this.mActionsView.addView(this.mQuickShareChip);
            this.mSmartChips.add(this.mQuickShareChip);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addQuickShareChip$15(View view) {
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        this.mQuickShareChip.setIsPending(true);
        this.mPendingInteraction = PendingInteraction.QUICK_SHARE;
    }

    public final Rect scrollableAreaOnScreen(ScrollCaptureResponse scrollCaptureResponse) {
        Rect rect = new Rect(scrollCaptureResponse.getBoundsInWindow());
        Rect windowBounds = scrollCaptureResponse.getWindowBounds();
        rect.offset(windowBounds.left, windowBounds.top);
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        rect.intersect(new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
        return rect;
    }

    public void startLongScreenshotTransition(Rect rect, final Runnable runnable, ScrollCaptureController.LongScreenshot longScreenshot) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda7(this));
        if (this.mShowScrollablePreview) {
            this.mScrollablePreview.setImageBitmap(longScreenshot.toBitmap());
            float x = this.mScrollablePreview.getX();
            float y = this.mScrollablePreview.getY();
            int[] locationOnScreen = this.mScrollablePreview.getLocationOnScreen();
            rect.offset(((int) x) - locationOnScreen[0], ((int) y) - locationOnScreen[1]);
            this.mScrollablePreview.setPivotX(0.0f);
            this.mScrollablePreview.setPivotY(0.0f);
            this.mScrollablePreview.setAlpha(1.0f);
            float width = ((float) this.mScrollablePreview.getWidth()) / ((float) longScreenshot.getWidth());
            Matrix matrix = new Matrix();
            matrix.setScale(width, width);
            matrix.postTranslate(((float) longScreenshot.getLeft()) * width, ((float) longScreenshot.getTop()) * width);
            this.mScrollablePreview.setImageMatrix(matrix);
            float width2 = ((float) rect.width()) / ((float) this.mScrollablePreview.getWidth());
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat2.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda8(this, width2, x, rect, y));
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat3.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda9(this));
            animatorSet.play(ofFloat2).with(ofFloat).before(ofFloat3);
            ofFloat2.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    runnable.run();
                }
            });
        } else {
            animatorSet.play(ofFloat);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    runnable.run();
                }
            });
        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ScreenshotView.this.mCallbacks.onDismiss();
            }
        });
        animatorSet.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$16(ValueAnimator valueAnimator) {
        this.mScrollingScrim.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$17(float f, float f2, Rect rect, float f3, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float lerp = MathUtils.lerp(1.0f, f, animatedFraction);
        this.mScrollablePreview.setScaleX(lerp);
        this.mScrollablePreview.setScaleY(lerp);
        this.mScrollablePreview.setX(MathUtils.lerp(f2, (float) rect.left, animatedFraction));
        this.mScrollablePreview.setY(MathUtils.lerp(f3, (float) rect.top, animatedFraction));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$18(ValueAnimator valueAnimator) {
        this.mScrollablePreview.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    public void prepareScrollingTransition(ScrollCaptureResponse scrollCaptureResponse, Bitmap bitmap, Bitmap bitmap2, boolean z) {
        this.mShowScrollablePreview = z == this.mOrientationPortrait;
        this.mScrollingScrim.setImageBitmap(bitmap2);
        this.mScrollingScrim.setVisibility(0);
        if (this.mShowScrollablePreview) {
            Rect scrollableAreaOnScreen = scrollableAreaOnScreen(scrollCaptureResponse);
            float width = this.mFixedSize / ((float) (this.mOrientationPortrait ? bitmap.getWidth() : bitmap.getHeight()));
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mScrollablePreview.getLayoutParams();
            layoutParams.width = (int) (((float) scrollableAreaOnScreen.width()) * width);
            layoutParams.height = (int) (((float) scrollableAreaOnScreen.height()) * width);
            Matrix matrix = new Matrix();
            matrix.setScale(width, width);
            matrix.postTranslate(((float) (-scrollableAreaOnScreen.left)) * width, ((float) (-scrollableAreaOnScreen.top)) * width);
            this.mScrollablePreview.setTranslationX(((float) (this.mDirectionLTR ? scrollableAreaOnScreen.left : scrollableAreaOnScreen.right - getWidth())) * width);
            this.mScrollablePreview.setTranslationY(width * ((float) scrollableAreaOnScreen.top));
            this.mScrollablePreview.setImageMatrix(matrix);
            this.mScrollablePreview.setImageBitmap(bitmap);
            this.mScrollablePreview.setVisibility(0);
        }
        this.mDismissButton.setVisibility(8);
        this.mActionsContainer.setVisibility(8);
        this.mActionsContainerBackground.setVisibility(4);
        this.mScreenshotPreviewBorder.setVisibility(4);
        this.mScreenshotPreview.setVisibility(4);
        this.mScrollingScrim.setImageTintBlendMode(BlendMode.SRC_ATOP);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 0.3f});
        ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda6(this));
        ofFloat.setDuration(200);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareScrollingTransition$19(ValueAnimator valueAnimator) {
        this.mScrollingScrim.setImageTintList(ColorStateList.valueOf(Color.argb(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f, 0.0f, 0.0f)));
    }

    public void restoreNonScrollingUi() {
        this.mScrollChip.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDismissButton.setVisibility(0);
        }
        this.mActionsContainer.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        this.mScreenshotPreviewBorder.setVisibility(0);
        this.mScreenshotPreview.setVisibility(0);
        this.mCallbacks.onUserInteraction();
    }

    public boolean isDismissing() {
        return this.mScreenshotStatic.isDismissing();
    }

    public boolean isPendingSharedTransition() {
        return this.mPendingSharedTransition;
    }

    public void animateDismissal() {
        this.mScreenshotStatic.dismiss();
    }

    public void reset() {
        this.mScreenshotStatic.cancelDismissal();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        this.mScreenshotPreview.setImageDrawable((Drawable) null);
        this.mScreenshotPreview.setVisibility(4);
        this.mScreenshotPreviewBorder.setAlpha(0.0f);
        this.mPendingSharedTransition = false;
        this.mActionsContainerBackground.setVisibility(8);
        this.mActionsContainer.setVisibility(8);
        this.mDismissButton.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScreenshotStatic.setTranslationX(0.0f);
        this.mScreenshotPreview.setContentDescription(this.mContext.getResources().getString(R$string.screenshot_preview_description));
        this.mScreenshotPreview.setOnClickListener((View.OnClickListener) null);
        this.mShareChip.setOnClickListener((View.OnClickListener) null);
        this.mScrollingScrim.setVisibility(8);
        this.mEditChip.setOnClickListener((View.OnClickListener) null);
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        this.mPendingInteraction = null;
        Iterator<OverlayActionChip> it = this.mSmartChips.iterator();
        while (it.hasNext()) {
            this.mActionsView.removeView(it.next());
        }
        this.mSmartChips.clear();
        this.mQuickShareChip = null;
        setAlpha(1.0f);
        this.mScreenshotStatic.setAlpha(1.0f);
        this.mScreenshotSelectorView.stop();
    }

    public final void startSharedTransition(ScreenshotController.SavedImageData.ActionTransition actionTransition) {
        try {
            this.mPendingSharedTransition = true;
            actionTransition.action.actionIntent.send();
            createScreenshotFadeDismissAnimation().start();
        } catch (PendingIntent.CanceledException e) {
            this.mPendingSharedTransition = false;
            Runnable runnable = actionTransition.onCancelRunnable;
            if (runnable != null) {
                runnable.run();
            }
            Log.e(TAG, "Intent cancelled", e);
        }
    }

    public ValueAnimator createScreenshotFadeDismissAnimation() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda4(this));
        ofFloat.setDuration(600);
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotFadeDismissAnimation$20(ValueAnimator valueAnimator) {
        float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
        this.mDismissButton.setAlpha(animatedFraction);
        this.mActionsContainerBackground.setAlpha(animatedFraction);
        this.mActionsContainer.setAlpha(animatedFraction);
        this.mScreenshotPreviewBorder.setAlpha(animatedFraction);
    }

    public static Drawable createScreenDrawable(Resources resources, Bitmap bitmap, Insets insets) {
        int width = (bitmap.getWidth() - insets.left) - insets.right;
        int height = (bitmap.getHeight() - insets.top) - insets.bottom;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        if (height == 0 || width == 0 || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            String str = TAG;
            Log.e(str, "Can't create inset drawable, using 0 insets bitmap and insets create degenerate region: " + bitmap.getWidth() + "x" + bitmap.getHeight() + " " + bitmapDrawable);
            return bitmapDrawable;
        }
        float f = (float) width;
        float f2 = (float) height;
        InsetDrawable insetDrawable = new InsetDrawable(bitmapDrawable, (((float) insets.left) * -1.0f) / f, (((float) insets.top) * -1.0f) / f2, (((float) insets.right) * -1.0f) / f, (((float) insets.bottom) * -1.0f) / f2);
        if (insets.left >= 0 && insets.top >= 0 && insets.right >= 0 && insets.bottom >= 0) {
            return insetDrawable;
        }
        return new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), insetDrawable});
    }
}
