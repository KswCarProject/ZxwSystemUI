package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.leanback.R$dimen;

public class ShadowOverlayContainer extends FrameLayout {
    public static final Rect sTempRect = new Rect();
    public float mFocusedZ;
    public boolean mInitialized;
    public int mOverlayColor;
    public Paint mOverlayPaint;
    public int mShadowType;
    public float mUnfocusedZ;
    public View mWrappedView;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public ShadowOverlayContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ShadowOverlayContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShadowType = 1;
        useStaticShadow();
        useDynamicShadow();
    }

    public static boolean supportsShadow() {
        return StaticShadowHelper.supportsShadow();
    }

    public static boolean supportsDynamicShadow() {
        return ShadowHelper.supportsDynamicShadow();
    }

    public void useDynamicShadow() {
        useDynamicShadow(getResources().getDimension(R$dimen.lb_material_shadow_normal_z), getResources().getDimension(R$dimen.lb_material_shadow_focused_z));
    }

    public void useDynamicShadow(float f, float f2) {
        if (this.mInitialized) {
            throw new IllegalStateException("Already initialized");
        } else if (supportsDynamicShadow()) {
            this.mShadowType = 3;
            this.mUnfocusedZ = f;
            this.mFocusedZ = f2;
        }
    }

    public void useStaticShadow() {
        if (this.mInitialized) {
            throw new IllegalStateException("Already initialized");
        } else if (supportsShadow()) {
            this.mShadowType = 2;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mOverlayPaint != null && this.mOverlayColor != 0) {
            canvas.drawRect((float) this.mWrappedView.getLeft(), (float) this.mWrappedView.getTop(), (float) this.mWrappedView.getRight(), (float) this.mWrappedView.getBottom(), this.mOverlayPaint);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view;
        super.onLayout(z, i, i2, i3, i4);
        if (z && (view = this.mWrappedView) != null) {
            Rect rect = sTempRect;
            rect.left = (int) view.getPivotX();
            rect.top = (int) this.mWrappedView.getPivotY();
            offsetDescendantRectToMyCoords(this.mWrappedView, rect);
            setPivotX((float) rect.left);
            setPivotY((float) rect.top);
        }
    }
}
