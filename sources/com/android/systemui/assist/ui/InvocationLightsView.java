package com.android.systemui.assist.ui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.R$attr;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import java.util.ArrayList;
import java.util.Iterator;

public class InvocationLightsView extends View implements NavigationBarTransitions.DarkIntensityListener {
    public final ArrayList<EdgeLight> mAssistInvocationLights;
    public final int mDarkColor;
    public final PerimeterPathGuide mGuide;
    public final int mLightColor;
    public final Paint mPaint;
    public final Path mPath;
    public boolean mRegistered;
    public int[] mScreenLocation;
    public final int mStrokeWidth;
    public boolean mUseNavBarColor;
    public final int mViewHeight;

    public InvocationLightsView(Context context) {
        this(context, (AttributeSet) null);
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public InvocationLightsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAssistInvocationLights = new ArrayList<>();
        Paint paint = new Paint();
        this.mPaint = paint;
        this.mPath = new Path();
        this.mScreenLocation = new int[2];
        this.mRegistered = false;
        this.mUseNavBarColor = true;
        int convertDpToPx = DisplayUtils.convertDpToPx(3.0f, context);
        this.mStrokeWidth = convertDpToPx;
        paint.setStrokeWidth((float) convertDpToPx);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setAntiAlias(true);
        this.mGuide = new PerimeterPathGuide(context, createCornerPathRenderer(context), convertDpToPx / 2, DisplayUtils.getWidth(context), DisplayUtils.getHeight(context));
        this.mViewHeight = Math.max(Math.max(DisplayUtils.getCornerRadiusBottom(context), DisplayUtils.getCornerRadiusTop(context)), DisplayUtils.convertDpToPx(3.0f, context));
        int themeAttr = Utils.getThemeAttr(this.mContext, R$attr.darkIconTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.mContext, Utils.getThemeAttr(this.mContext, R$attr.lightIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(this.mContext, themeAttr);
        int i3 = R$attr.singleToneColor;
        this.mLightColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i3);
        this.mDarkColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, i3);
        for (int i4 = 0; i4 < 4; i4++) {
            this.mAssistInvocationLights.add(new EdgeLight(0, 0.0f, 0.0f));
        }
    }

    public void onInvocationProgress(float f) {
        if (f == 0.0f) {
            setVisibility(8);
        } else {
            attemptRegisterNavBarListener();
            float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM_LEFT);
            float f2 = (regionWidth - (0.6f * regionWidth)) / 2.0f;
            PerimeterPathGuide perimeterPathGuide = this.mGuide;
            PerimeterPathGuide.Region region = PerimeterPathGuide.Region.BOTTOM;
            float lerp = MathUtils.lerp(0.0f, perimeterPathGuide.getRegionWidth(region) / 4.0f, f);
            float f3 = 1.0f - f;
            float f4 = ((-regionWidth) + f2) * f3;
            float regionWidth2 = this.mGuide.getRegionWidth(region) + ((regionWidth - f2) * f3);
            float f5 = f4 + lerp;
            setLight(0, f4, f5);
            float f6 = 2.0f * lerp;
            setLight(1, f5, f4 + f6);
            float f7 = regionWidth2 - lerp;
            setLight(2, regionWidth2 - f6, f7);
            setLight(3, f7, regionWidth2);
            setVisibility(0);
        }
        invalidate();
    }

    public void hide() {
        setVisibility(8);
        Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
        while (it.hasNext()) {
            it.next().setEndpoints(0.0f, 0.0f);
        }
        attemptUnregisterNavBarListener();
    }

    public void onDarkIntensity(float f) {
        updateDarkness(f);
    }

    public void onFinishInflate() {
        getLayoutParams().height = this.mViewHeight;
        requestLayout();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mGuide.setRotation(getContext().getDisplay().getRotation());
    }

    public void onDraw(Canvas canvas) {
        getLocationOnScreen(this.mScreenLocation);
        int[] iArr = this.mScreenLocation;
        canvas.translate((float) (-iArr[0]), (float) (-iArr[1]));
        if (this.mUseNavBarColor) {
            Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
            while (it.hasNext()) {
                renderLight(it.next(), canvas);
            }
            return;
        }
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        renderLight(this.mAssistInvocationLights.get(0), canvas);
        renderLight(this.mAssistInvocationLights.get(3), canvas);
        this.mPaint.setStrokeCap(Paint.Cap.BUTT);
        renderLight(this.mAssistInvocationLights.get(1), canvas);
        renderLight(this.mAssistInvocationLights.get(2), canvas);
    }

    public void setLight(int i, float f, float f2) {
        if (i < 0 || i >= 4) {
            Log.w("InvocationLightsView", "invalid invocation light index: " + i);
        }
        this.mAssistInvocationLights.get(i).setEndpoints(f, f2);
    }

    public CornerPathRenderer createCornerPathRenderer(Context context) {
        return new CircularCornerPathRenderer(context);
    }

    public void updateDarkness(float f) {
        if (this.mUseNavBarColor) {
            int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightColor), Integer.valueOf(this.mDarkColor))).intValue();
            boolean z = true;
            Iterator<EdgeLight> it = this.mAssistInvocationLights.iterator();
            while (it.hasNext()) {
                z &= it.next().setColor(intValue);
            }
            if (z) {
                invalidate();
            }
        }
    }

    public final void renderLight(EdgeLight edgeLight, Canvas canvas) {
        if (edgeLight.getLength() > 0.0f) {
            this.mGuide.strokeSegment(this.mPath, edgeLight.getStart(), edgeLight.getStart() + edgeLight.getLength());
            this.mPaint.setColor(edgeLight.getColor());
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    public final void attemptRegisterNavBarListener() {
        NavigationBarController navigationBarController;
        NavigationBar defaultNavigationBar;
        if (!this.mRegistered && (navigationBarController = (NavigationBarController) Dependency.get(NavigationBarController.class)) != null && (defaultNavigationBar = navigationBarController.getDefaultNavigationBar()) != null) {
            updateDarkness(defaultNavigationBar.getBarTransitions().addDarkIntensityListener(this));
            this.mRegistered = true;
        }
    }

    public final void attemptUnregisterNavBarListener() {
        NavigationBarController navigationBarController;
        NavigationBar defaultNavigationBar;
        if (this.mRegistered && (navigationBarController = (NavigationBarController) Dependency.get(NavigationBarController.class)) != null && (defaultNavigationBar = navigationBarController.getDefaultNavigationBar()) != null) {
            defaultNavigationBar.getBarTransitions().removeDarkIntensityListener(this);
            this.mRegistered = false;
        }
    }
}