package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.PathParser;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BadgedImageView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: BubbleOverflow.kt */
public final class BubbleOverflow implements BubbleViewProvider {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public Bitmap bitmap;
    @NotNull
    public final Context context;
    public int dotColor;
    public Path dotPath;
    @Nullable
    public BubbleExpandedView expandedView = null;
    @NotNull
    public final LayoutInflater inflater;
    @Nullable
    public BadgedImageView overflowBtn = null;
    public int overflowIconInset;
    @NotNull
    public final BubblePositioner positioner;
    public boolean showDot;

    @Nullable
    public Bitmap getAppBadge() {
        return null;
    }

    @NotNull
    public String getKey() {
        return "Overflow";
    }

    public void setTaskViewVisibility(boolean z) {
    }

    public BubbleOverflow(@NotNull Context context2, @NotNull BubblePositioner bubblePositioner) {
        this.context = context2;
        this.positioner = bubblePositioner;
        this.inflater = LayoutInflater.from(context2);
        updateResources();
    }

    public final void initialize(@NotNull BubbleController bubbleController) {
        createExpandedView();
        BubbleExpandedView expandedView2 = getExpandedView();
        if (expandedView2 != null) {
            expandedView2.initialize(bubbleController, bubbleController.getStackView(), true);
        }
    }

    public final void cleanUpExpandedState() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.cleanUpExpandedState();
        }
        this.expandedView = null;
    }

    public final void update() {
        updateResources();
        BubbleExpandedView expandedView2 = getExpandedView();
        if (expandedView2 != null) {
            expandedView2.applyThemeAttrs();
        }
        BadgedImageView iconView = getIconView();
        if (iconView != null) {
            iconView.setIconImageResource(R.drawable.bubble_ic_overflow_button);
        }
        updateBtnTheme();
    }

    public final void updateResources() {
        this.overflowIconInset = this.context.getResources().getDimensionPixelSize(R.dimen.bubble_overflow_icon_inset);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setLayoutParams(new FrameLayout.LayoutParams(this.positioner.getBubbleSize(), this.positioner.getBubbleSize()));
        }
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.updateDimensions();
        }
    }

    public final void updateBtnTheme() {
        Drawable iconDrawable;
        Resources resources = this.context.getResources();
        TypedValue typedValue = new TypedValue();
        this.context.getTheme().resolveAttribute(17956900, typedValue, true);
        Path path = null;
        int color = resources.getColor(typedValue.resourceId, (Resources.Theme) null);
        this.dotColor = color;
        int color2 = resources.getColor(17170499);
        BadgedImageView badgedImageView = this.overflowBtn;
        if (!(badgedImageView == null || (iconDrawable = badgedImageView.getIconDrawable()) == null)) {
            iconDrawable.setTint(color2);
        }
        BubbleIconFactory bubbleIconFactory = new BubbleIconFactory(this.context);
        BadgedImageView badgedImageView2 = this.overflowBtn;
        this.bitmap = bubbleIconFactory.createBadgedIconBitmap(new AdaptiveIconDrawable(new ColorDrawable(color), new InsetDrawable(badgedImageView2 == null ? null : badgedImageView2.getIconDrawable(), this.overflowIconInset))).icon;
        this.dotPath = PathParser.createPathFromPathData(resources.getString(17039989));
        IconNormalizer normalizer = bubbleIconFactory.getNormalizer();
        BadgedImageView iconView = getIconView();
        Intrinsics.checkNotNull(iconView);
        float scale = normalizer.getScale(iconView.getIconDrawable(), (RectF) null, (Path) null, (boolean[]) null);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale, 50.0f, 50.0f);
        Path path2 = this.dotPath;
        if (path2 != null) {
            path = path2;
        }
        path.transform(matrix);
        BadgedImageView badgedImageView3 = this.overflowBtn;
        if (badgedImageView3 != null) {
            badgedImageView3.setRenderedBubble(this);
        }
        BadgedImageView badgedImageView4 = this.overflowBtn;
        if (badgedImageView4 != null) {
            badgedImageView4.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        }
    }

    public final void setVisible(int i) {
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.setVisibility(i);
        }
    }

    public final void setShowDot(boolean z) {
        this.showDot = z;
        BadgedImageView badgedImageView = this.overflowBtn;
        if (badgedImageView != null) {
            badgedImageView.updateDotVisibility(true);
        }
    }

    @Nullable
    public final BubbleExpandedView createExpandedView() {
        View inflate = this.inflater.inflate(R.layout.bubble_expanded_view, (ViewGroup) null, false);
        if (inflate != null) {
            BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) inflate;
            this.expandedView = bubbleExpandedView;
            bubbleExpandedView.applyThemeAttrs();
            updateResources();
            return this.expandedView;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.bubbles.BubbleExpandedView");
    }

    @Nullable
    public BubbleExpandedView getExpandedView() {
        return this.expandedView;
    }

    public int getDotColor() {
        return this.dotColor;
    }

    @NotNull
    public Bitmap getBubbleIcon() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 == null) {
            return null;
        }
        return bitmap2;
    }

    public boolean showDot() {
        return this.showDot;
    }

    @Nullable
    public Path getDotPath() {
        Path path = this.dotPath;
        if (path == null) {
            return null;
        }
        return path;
    }

    @Nullable
    public BadgedImageView getIconView() {
        if (this.overflowBtn == null) {
            View inflate = this.inflater.inflate(R.layout.bubble_overflow_button, (ViewGroup) null, false);
            if (inflate != null) {
                BadgedImageView badgedImageView = (BadgedImageView) inflate;
                this.overflowBtn = badgedImageView;
                badgedImageView.initialize(this.positioner);
                BadgedImageView badgedImageView2 = this.overflowBtn;
                if (badgedImageView2 != null) {
                    badgedImageView2.setContentDescription(this.context.getResources().getString(R.string.bubble_overflow_button_content_description));
                }
                int bubbleSize = this.positioner.getBubbleSize();
                BadgedImageView badgedImageView3 = this.overflowBtn;
                if (badgedImageView3 != null) {
                    badgedImageView3.setLayoutParams(new FrameLayout.LayoutParams(bubbleSize, bubbleSize));
                }
                updateBtnTheme();
            } else {
                throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.bubbles.BadgedImageView");
            }
        }
        return this.overflowBtn;
    }

    public int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.expandedView;
        if (bubbleExpandedView == null) {
            return -1;
        }
        Intrinsics.checkNotNull(bubbleExpandedView);
        return bubbleExpandedView.getTaskId();
    }

    /* compiled from: BubbleOverflow.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
