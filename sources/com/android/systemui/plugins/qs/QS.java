package com.android.systemui.plugins.qs;

import android.view.View;
import com.android.systemui.plugins.FragmentBase;
import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import java.util.function.Consumer;

@DependsOn(target = HeightListener.class)
@ProvidesInterface(action = "com.android.systemui.action.PLUGIN_QS", version = 15)
public interface QS extends FragmentBase {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_QS";
    public static final String TAG = "QS";
    public static final int VERSION = 15;

    @ProvidesInterface(version = 1)
    public interface HeightListener {
        public static final int VERSION = 1;

        void onQsHeightChanged();
    }

    @ProvidesInterface(version = 1)
    public interface ScrollListener {
        public static final int VERSION = 1;

        void onQsPanelScrollChanged(int i);
    }

    void animateHeaderSlidingOut();

    void closeCustomizer();

    void closeDetail();

    int getDesiredHeight();

    View getHeader();

    int getHeightDiff();

    int getQsMinExpansionHeight();

    void hideImmediately();

    boolean isCustomizing();

    boolean isFullyCollapsed() {
        return true;
    }

    boolean isShowingDetail();

    void notifyCustomizeChanged();

    void setCollapseExpandAction(Runnable runnable);

    void setCollapsedMediaVisibilityChangedListener(Consumer<Boolean> consumer);

    void setContainerController(QSContainerController qSContainerController);

    void setExpanded(boolean z);

    void setFancyClipping(int i, int i2, int i3, boolean z);

    void setHasNotifications(boolean z) {
    }

    void setHeaderClickable(boolean z);

    void setHeaderListening(boolean z);

    void setHeightOverride(int i);

    void setInSplitShade(boolean z);

    void setListening(boolean z);

    void setOverScrollAmount(int i) {
    }

    void setOverscrolling(boolean z);

    void setPanelView(HeightListener heightListener);

    void setQsExpansion(float f, float f2, float f3, float f4);

    void setQsVisible(boolean z);

    void setScrollListener(ScrollListener scrollListener) {
    }

    void setTransitionToFullShadeAmount(float f, float f2) {
    }

    boolean disallowPanelTouches() {
        return isShowingDetail();
    }
}
