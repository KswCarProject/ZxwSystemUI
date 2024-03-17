package com.android.systemui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;

public class AutoReinflateContainer extends FrameLayout implements ConfigurationController.ConfigurationListener {
    public final List<InflateListener> mInflateListeners = new ArrayList();
    public final int mLayout;

    public interface InflateListener {
        void onInflated(View view);
    }

    public AutoReinflateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AutoReinflateContainer);
        int i = R$styleable.AutoReinflateContainer_android_layout;
        if (obtainStyledAttributes.hasValue(i)) {
            this.mLayout = obtainStyledAttributes.getResourceId(i, 0);
            obtainStyledAttributes.recycle();
            inflateLayout();
            return;
        }
        throw new IllegalArgumentException("AutoReinflateContainer must contain a layout");
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).removeCallback(this);
    }

    public void inflateLayoutImpl() {
        LayoutInflater.from(getContext()).inflate(this.mLayout, this);
    }

    public void inflateLayout() {
        removeAllViews();
        inflateLayoutImpl();
        int size = this.mInflateListeners.size();
        for (int i = 0; i < size; i++) {
            this.mInflateListeners.get(i).onInflated(getChildAt(0));
        }
    }

    public void onDensityOrFontScaleChanged() {
        inflateLayout();
    }

    public void onThemeChanged() {
        inflateLayout();
    }

    public void onUiModeChanged() {
        inflateLayout();
    }

    public void onLocaleListChanged() {
        inflateLayout();
    }
}
