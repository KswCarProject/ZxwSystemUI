package androidx.viewpager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class PagerTabStrip extends PagerTitleStrip {
    public boolean mDrawFullUnderline = false;
    public boolean mDrawFullUnderlineSet = false;
    public int mFullUnderlineHeight;
    public boolean mIgnoreTap;
    public int mIndicatorColor;
    public int mIndicatorHeight;
    public float mInitialMotionX;
    public float mInitialMotionY;
    public int mMinPaddingBottom;
    public int mMinStripHeight;
    public int mMinTextSpacing;
    public int mTabAlpha = 255;
    public int mTabPadding;
    public final Paint mTabPaint;
    public int mTouchSlop;

    public PagerTabStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mTabPaint = paint;
        int i = this.mTextColor;
        this.mIndicatorColor = i;
        paint.setColor(i);
        float f = context.getResources().getDisplayMetrics().density;
        this.mIndicatorHeight = (int) ((3.0f * f) + 0.5f);
        this.mMinPaddingBottom = (int) ((6.0f * f) + 0.5f);
        this.mMinTextSpacing = (int) (64.0f * f);
        this.mTabPadding = (int) ((16.0f * f) + 0.5f);
        this.mFullUnderlineHeight = (int) ((1.0f * f) + 0.5f);
        this.mMinStripHeight = (int) ((f * 32.0f) + 0.5f);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setTextSpacing(getTextSpacing());
        setWillNotDraw(false);
        this.mPrevText.setFocusable(true);
        this.mPrevText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ViewPager viewPager = PagerTabStrip.this.mPager;
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        this.mNextText.setFocusable(true);
        this.mNextText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ViewPager viewPager = PagerTabStrip.this.mPager;
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        if (getBackground() == null) {
            this.mDrawFullUnderline = true;
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        int i5 = this.mMinPaddingBottom;
        if (i4 < i5) {
            i4 = i5;
        }
        super.setPadding(i, i2, i3, i4);
    }

    public void setTextSpacing(int i) {
        int i2 = this.mMinTextSpacing;
        if (i < i2) {
            i = i2;
        }
        super.setTextSpacing(i);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = drawable == null;
        }
    }

    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = (i & -16777216) == 0;
        }
    }

    public void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        if (!this.mDrawFullUnderlineSet) {
            this.mDrawFullUnderline = i == 0;
        }
    }

    public int getMinHeight() {
        return Math.max(super.getMinHeight(), this.mMinStripHeight);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0 && this.mIgnoreTap) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (action == 0) {
            this.mInitialMotionX = x;
            this.mInitialMotionY = y;
            this.mIgnoreTap = false;
        } else if (action != 1) {
            if (action == 2 && (Math.abs(x - this.mInitialMotionX) > ((float) this.mTouchSlop) || Math.abs(y - this.mInitialMotionY) > ((float) this.mTouchSlop))) {
                this.mIgnoreTap = true;
            }
        } else if (x < ((float) (this.mCurrText.getLeft() - this.mTabPadding))) {
            ViewPager viewPager = this.mPager;
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else if (x > ((float) (this.mCurrText.getRight() + this.mTabPadding))) {
            ViewPager viewPager2 = this.mPager;
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int left = this.mCurrText.getLeft() - this.mTabPadding;
        int right = this.mCurrText.getRight() + this.mTabPadding;
        this.mTabPaint.setColor((this.mTabAlpha << 24) | (this.mIndicatorColor & 16777215));
        float f = (float) height;
        canvas.drawRect((float) left, (float) (height - this.mIndicatorHeight), (float) right, f, this.mTabPaint);
        if (this.mDrawFullUnderline) {
            this.mTabPaint.setColor(-16777216 | (this.mIndicatorColor & 16777215));
            canvas.drawRect((float) getPaddingLeft(), (float) (height - this.mFullUnderlineHeight), (float) (getWidth() - getPaddingRight()), f, this.mTabPaint);
        }
    }

    public void updateTextPositions(int i, float f, boolean z) {
        super.updateTextPositions(i, f, z);
        this.mTabAlpha = (int) (Math.abs(f - 0.5f) * 2.0f * 255.0f);
        invalidate();
    }
}
