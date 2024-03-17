package com.android.systemui.statusbar.phone;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$id;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

public class NotificationsQuickSettingsContainer extends ConstraintLayout implements FragmentHostManager.FragmentListener, AboveShelfObserver.HasViewAboveShelfChangedListener {
    public Consumer<Configuration> mConfigurationChangedListener;
    public ArrayList<View> mDrawingOrderedChildren = new ArrayList<>();
    public final Comparator<View> mIndexComparator = Comparator.comparingInt(new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0(this));
    public Consumer<WindowInsets> mInsetsChangedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda1();
    public View mKeyguardStatusBar;
    public ArrayList<View> mLayoutDrawingOrder = new ArrayList<>();
    public View mQSContainer;
    public Consumer<QS> mQSFragmentAttachedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda2();
    public View mQSScrollView;
    public QS mQs;
    public View mQsFrame;
    public View mStackScroller;

    public static /* synthetic */ void lambda$new$0(WindowInsets windowInsets) {
    }

    public static /* synthetic */ void lambda$new$1(QS qs) {
    }

    public static /* synthetic */ void lambda$removeOnInsetsChangedListener$2(WindowInsets windowInsets) {
    }

    public static /* synthetic */ void lambda$removeQSFragmentAttachedListener$3(QS qs) {
    }

    public NotificationsQuickSettingsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mQsFrame = findViewById(R$id.qs_frame);
        this.mStackScroller = findViewById(R$id.notification_stack_scroller);
        this.mKeyguardStatusBar = findViewById(R$id.keyguard_header);
    }

    public void onFragmentViewCreated(String str, Fragment fragment) {
        QS qs = (QS) fragment;
        this.mQs = qs;
        this.mQSFragmentAttachedListener.accept(qs);
        this.mQSScrollView = this.mQs.getView().findViewById(R$id.expanded_qs_scroll_view);
        this.mQSContainer = this.mQs.getView().findViewById(R$id.quick_settings_container);
    }

    public void onHasViewsAboveShelfChanged(boolean z) {
        invalidate();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Consumer<Configuration> consumer = this.mConfigurationChangedListener;
        if (consumer != null) {
            consumer.accept(configuration);
        }
    }

    public void setConfigurationChangedListener(Consumer<Configuration> consumer) {
        this.mConfigurationChangedListener = consumer;
    }

    public void setNotificationsMarginBottom(int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mStackScroller.getLayoutParams();
        layoutParams.bottomMargin = i;
        this.mStackScroller.setLayoutParams(layoutParams);
    }

    public void setQSContainerPaddingBottom(int i) {
        View view = this.mQSContainer;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.mQSContainer.getPaddingTop(), this.mQSContainer.getPaddingRight(), i);
        }
    }

    public void setInsetsChangedListener(Consumer<WindowInsets> consumer) {
        this.mInsetsChangedListener = consumer;
    }

    public void removeOnInsetsChangedListener() {
        this.mInsetsChangedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda3();
    }

    public void setQSFragmentAttachedListener(Consumer<QS> consumer) {
        this.mQSFragmentAttachedListener = consumer;
        QS qs = this.mQs;
        if (qs != null) {
            consumer.accept(qs);
        }
    }

    public void removeQSFragmentAttachedListener() {
        this.mQSFragmentAttachedListener = new NotificationsQuickSettingsContainer$$ExternalSyntheticLambda4();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentHostManager.get(this).addTagListener(QS.TAG, this);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FragmentHostManager.get(this).removeTagListener(QS.TAG, this);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsetsChangedListener.accept(windowInsets);
        return windowInsets;
    }

    public void dispatchDraw(Canvas canvas) {
        this.mDrawingOrderedChildren.clear();
        this.mLayoutDrawingOrder.clear();
        if (this.mKeyguardStatusBar.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mKeyguardStatusBar);
            this.mLayoutDrawingOrder.add(this.mKeyguardStatusBar);
        }
        if (this.mQsFrame.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mQsFrame);
            this.mLayoutDrawingOrder.add(this.mQsFrame);
        }
        if (this.mStackScroller.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mStackScroller);
            this.mLayoutDrawingOrder.add(this.mStackScroller);
        }
        this.mLayoutDrawingOrder.sort(this.mIndexComparator);
        super.dispatchDraw(canvas);
    }

    public boolean drawChild(Canvas canvas, View view, long j) {
        int indexOf = this.mLayoutDrawingOrder.indexOf(view);
        if (indexOf >= 0) {
            return super.drawChild(canvas, this.mDrawingOrderedChildren.get(indexOf), j);
        }
        return super.drawChild(canvas, view, j);
    }

    public void applyConstraints(ConstraintSet constraintSet) {
        constraintSet.applyTo(this);
    }
}
