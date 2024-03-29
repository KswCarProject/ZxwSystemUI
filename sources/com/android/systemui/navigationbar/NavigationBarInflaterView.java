package com.android.systemui.navigationbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.buttons.ReverseLinearLayout;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
import java.io.PrintWriter;
import java.util.Objects;

public class NavigationBarInflaterView extends FrameLayout implements NavigationModeController.ModeChangedListener {
    public boolean mAlternativeOrder;
    @VisibleForTesting
    public SparseArray<ButtonDispatcher> mButtonDispatchers;
    public String mCurrentLayout;
    public FrameLayout mHorizontal;
    public boolean mIsVertical;
    public LayoutInflater mLandscapeInflater;
    public View mLastLandscape;
    public View mLastPortrait;
    public LayoutInflater mLayoutInflater;
    public int mNavBarMode = 0;
    public OverviewProxyService mOverviewProxyService;
    public FrameLayout mVertical;

    public NavigationBarInflaterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        createInflaters();
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    }

    @VisibleForTesting
    public void createInflaters() {
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        Configuration configuration = new Configuration();
        configuration.setTo(this.mContext.getResources().getConfiguration());
        configuration.orientation = 2;
        this.mLandscapeInflater = LayoutInflater.from(this.mContext.createConfigurationContext(configuration));
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        inflateChildren();
        clearViews();
        inflateLayout(getDefaultLayout());
    }

    public final void inflateChildren() {
        removeAllViews();
        FrameLayout frameLayout = (FrameLayout) this.mLayoutInflater.inflate(R$layout.navigation_layout, this, false);
        this.mHorizontal = frameLayout;
        addView(frameLayout);
        FrameLayout frameLayout2 = (FrameLayout) this.mLayoutInflater.inflate(R$layout.navigation_layout_vertical, this, false);
        this.mVertical = frameLayout2;
        addView(frameLayout2);
        updateAlternativeOrder();
    }

    public String getDefaultLayout() {
        int i;
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            i = R$string.config_navBarLayoutHandle;
        } else if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
            i = R$string.config_navBarLayoutQuickstep;
        } else {
            i = R$string.config_navBarLayout;
        }
        return getContext().getString(i);
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    public void onDetachedFromWindow() {
        ((NavigationModeController) Dependency.get(NavigationModeController.class)).removeListener(this);
        super.onDetachedFromWindow();
    }

    public void onLikelyDefaultLayoutChange() {
        String defaultLayout = getDefaultLayout();
        if (!Objects.equals(this.mCurrentLayout, defaultLayout)) {
            clearViews();
            inflateLayout(defaultLayout);
        }
    }

    public void setButtonDispatchers(SparseArray<ButtonDispatcher> sparseArray) {
        this.mButtonDispatchers = sparseArray;
        clearDispatcherViews();
        for (int i = 0; i < sparseArray.size(); i++) {
            initiallyFill(sparseArray.valueAt(i));
        }
    }

    public void updateButtonDispatchersCurrentView() {
        if (this.mButtonDispatchers != null) {
            FrameLayout frameLayout = this.mIsVertical ? this.mVertical : this.mHorizontal;
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                this.mButtonDispatchers.valueAt(i).setCurrentView(frameLayout);
            }
        }
    }

    public void setVertical(boolean z) {
        Log.e("NavBarInflater", "setVertical vertical : " + z);
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
        }
    }

    public void setAlternativeOrder(boolean z) {
        if (z != this.mAlternativeOrder) {
            this.mAlternativeOrder = z;
            updateAlternativeOrder();
        }
    }

    public final void updateAlternativeOrder() {
        FrameLayout frameLayout = this.mHorizontal;
        int i = R$id.ends_group;
        updateAlternativeOrder(frameLayout.findViewById(i));
        FrameLayout frameLayout2 = this.mHorizontal;
        int i2 = R$id.center_group;
        updateAlternativeOrder(frameLayout2.findViewById(i2));
        updateAlternativeOrder(this.mVertical.findViewById(i));
        updateAlternativeOrder(this.mVertical.findViewById(i2));
    }

    public final void updateAlternativeOrder(View view) {
        if (view instanceof ReverseLinearLayout) {
            ((ReverseLinearLayout) view).setAlternativeOrder(this.mAlternativeOrder);
        }
    }

    public final void initiallyFill(ButtonDispatcher buttonDispatcher) {
        FrameLayout frameLayout = this.mHorizontal;
        int i = R$id.ends_group;
        addAll(buttonDispatcher, (ViewGroup) frameLayout.findViewById(i));
        FrameLayout frameLayout2 = this.mHorizontal;
        int i2 = R$id.center_group;
        addAll(buttonDispatcher, (ViewGroup) frameLayout2.findViewById(i2));
        addAll(buttonDispatcher, (ViewGroup) this.mVertical.findViewById(i));
        addAll(buttonDispatcher, (ViewGroup) this.mVertical.findViewById(i2));
    }

    public final void addAll(ButtonDispatcher buttonDispatcher, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i).getId() == buttonDispatcher.getId()) {
                buttonDispatcher.addView(viewGroup.getChildAt(i));
            }
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                addAll(buttonDispatcher, (ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    public void inflateLayout(String str) {
        this.mCurrentLayout = str;
        if (str == null) {
            str = getDefaultLayout();
        }
        String[] split = str.split(";", 3);
        if (split.length != 3) {
            Log.d("NavBarInflater", "Invalid layout.");
            split = getDefaultLayout().split(";", 3);
        }
        String[] split2 = split[0].split(",");
        String[] split3 = split[1].split(",");
        String[] split4 = split[2].split(",");
        FrameLayout frameLayout = this.mHorizontal;
        int i = R$id.ends_group;
        inflateButtons(split2, (ViewGroup) frameLayout.findViewById(i), false, true);
        inflateButtons(split2, (ViewGroup) this.mVertical.findViewById(i), true, true);
        FrameLayout frameLayout2 = this.mHorizontal;
        int i2 = R$id.center_group;
        inflateButtons(split3, (ViewGroup) frameLayout2.findViewById(i2), false, false);
        inflateButtons(split3, (ViewGroup) this.mVertical.findViewById(i2), true, false);
        addGravitySpacer((LinearLayout) this.mHorizontal.findViewById(i));
        addGravitySpacer((LinearLayout) this.mVertical.findViewById(i));
        inflateButtons(split4, (ViewGroup) this.mHorizontal.findViewById(i), false, false);
        inflateButtons(split4, (ViewGroup) this.mVertical.findViewById(i), true, false);
        updateButtonDispatchersCurrentView();
    }

    public final void addGravitySpacer(LinearLayout linearLayout) {
        linearLayout.addView(new Space(this.mContext), new LinearLayout.LayoutParams(0, 0, 1.0f));
    }

    public final void inflateButtons(String[] strArr, ViewGroup viewGroup, boolean z, boolean z2) {
        for (String inflateButton : strArr) {
            inflateButton(inflateButton, viewGroup, z, z2);
        }
    }

    public View inflateButton(String str, ViewGroup viewGroup, boolean z, boolean z2) {
        View createView = createView(str, viewGroup, z ? this.mLandscapeInflater : this.mLayoutInflater);
        if (createView == null) {
            return null;
        }
        View applySize = applySize(createView, str, z, z2);
        viewGroup.addView(applySize);
        addToDispatchers(applySize);
        View view = z ? this.mLastLandscape : this.mLastPortrait;
        View childAt = applySize instanceof ReverseLinearLayout.ReverseRelativeLayout ? ((ReverseLinearLayout.ReverseRelativeLayout) applySize).getChildAt(0) : applySize;
        if (view != null) {
            childAt.setAccessibilityTraversalAfter(view.getId());
        }
        if (z) {
            this.mLastLandscape = childAt;
        } else {
            this.mLastPortrait = childAt;
        }
        return applySize;
    }

    public final View applySize(View view, String str, boolean z, boolean z2) {
        String extractSize = extractSize(str);
        if (extractSize == null) {
            return view;
        }
        if (extractSize.contains("W") || extractSize.contains("A")) {
            ReverseLinearLayout.ReverseRelativeLayout reverseRelativeLayout = new ReverseLinearLayout.ReverseRelativeLayout(this.mContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
            int i = z ? z2 ? 48 : 80 : z2 ? 8388611 : 8388613;
            if (extractSize.endsWith("WC")) {
                i = 17;
            } else if (extractSize.endsWith("C")) {
                i = 16;
            }
            reverseRelativeLayout.setDefaultGravity(i);
            reverseRelativeLayout.setGravity(i);
            reverseRelativeLayout.addView(view, layoutParams);
            if (extractSize.contains("W")) {
                reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, -1, Float.parseFloat(extractSize.substring(0, extractSize.indexOf("W")))));
            } else {
                reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams((int) convertDpToPx(this.mContext, Float.parseFloat(extractSize.substring(0, extractSize.indexOf("A")))), -1));
            }
            reverseRelativeLayout.setClipChildren(false);
            reverseRelativeLayout.setClipToPadding(false);
            return reverseRelativeLayout;
        }
        float parseFloat = Float.parseFloat(extractSize);
        ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
        layoutParams2.width = (int) (((float) layoutParams2.width) * parseFloat);
        return view;
    }

    public View createView(String str, ViewGroup viewGroup, LayoutInflater layoutInflater) {
        String extractButton = extractButton(str);
        if ("left".equals(extractButton)) {
            extractButton = extractButton("space");
        } else if ("right".equals(extractButton)) {
            extractButton = extractButton("menu_ime");
        }
        if ("home".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.home, viewGroup, false);
        }
        if ("back".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.back, viewGroup, false);
        }
        if ("recent".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.recent_apps, viewGroup, false);
        }
        if ("menu_ime".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.menu_ime, viewGroup, false);
        }
        if ("space".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.nav_key_space, viewGroup, false);
        }
        if ("clipboard".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.clipboard, viewGroup, false);
        }
        if ("contextual".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.contextual, viewGroup, false);
        }
        if ("home_handle".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.home_handle, viewGroup, false);
        }
        if ("ime_switcher".equals(extractButton)) {
            return layoutInflater.inflate(R$layout.ime_switcher, viewGroup, false);
        }
        if (!extractButton.startsWith("key")) {
            return null;
        }
        String extractImage = extractImage(extractButton);
        int extractKeycode = extractKeycode(extractButton);
        View inflate = layoutInflater.inflate(R$layout.custom_key, viewGroup, false);
        KeyButtonView keyButtonView = (KeyButtonView) inflate;
        keyButtonView.setCode(extractKeycode);
        if (extractImage != null) {
            if (extractImage.contains(":")) {
                keyButtonView.loadAsync(Icon.createWithContentUri(extractImage));
            } else if (extractImage.contains("/")) {
                int indexOf = extractImage.indexOf(47);
                keyButtonView.loadAsync(Icon.createWithResource(extractImage.substring(0, indexOf), Integer.parseInt(extractImage.substring(indexOf + 1))));
            }
        }
        return inflate;
    }

    public static String extractImage(String str) {
        if (!str.contains(":")) {
            return null;
        }
        return str.substring(str.indexOf(":") + 1, str.indexOf(")"));
    }

    public static int extractKeycode(String str) {
        if (!str.contains("(")) {
            return 1;
        }
        return Integer.parseInt(str.substring(str.indexOf("(") + 1, str.indexOf(":")));
    }

    public static String extractSize(String str) {
        if (!str.contains("[")) {
            return null;
        }
        return str.substring(str.indexOf("[") + 1, str.indexOf("]"));
    }

    public static String extractButton(String str) {
        if (!str.contains("[")) {
            return str;
        }
        return str.substring(0, str.indexOf("["));
    }

    public final void addToDispatchers(View view) {
        SparseArray<ButtonDispatcher> sparseArray = this.mButtonDispatchers;
        if (sparseArray != null) {
            int indexOfKey = sparseArray.indexOfKey(view.getId());
            if (indexOfKey >= 0) {
                this.mButtonDispatchers.valueAt(indexOfKey).addView(view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    addToDispatchers(viewGroup.getChildAt(i));
                }
            }
        }
    }

    public final void clearDispatcherViews() {
        if (this.mButtonDispatchers != null) {
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                this.mButtonDispatchers.valueAt(i).clear();
            }
        }
    }

    public final void clearViews() {
        clearDispatcherViews();
        FrameLayout frameLayout = this.mHorizontal;
        int i = R$id.nav_buttons;
        clearAllChildren((ViewGroup) frameLayout.findViewById(i));
        clearAllChildren((ViewGroup) this.mVertical.findViewById(i));
    }

    public final void clearAllChildren(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ((ViewGroup) viewGroup.getChildAt(i)).removeAllViews();
        }
    }

    public static float convertDpToPx(Context context, float f) {
        return f * context.getResources().getDisplayMetrics().density;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarInflaterView");
        printWriter.println("  mCurrentLayout: " + this.mCurrentLayout);
    }
}
