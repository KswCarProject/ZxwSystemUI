package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.collection.ArrayMap;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.AlphaOptimizedFrameLayout;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.stack.AnimationFilter;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class NotificationIconContainer extends AlphaOptimizedFrameLayout {
    public static final AnimationProperties ADD_ICON_PROPERTIES = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200).setDelay(50);
    public static final AnimationProperties DOT_ANIMATION_PROPERTIES = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    public static final AnimationProperties ICON_ANIMATION_PROPERTIES = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter().animateX().animateY().animateAlpha().animateScale();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(100);
    public static final AnimationProperties UNISOLATION_PROPERTY = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(110);
    public static final AnimationProperties UNISOLATION_PROPERTY_OTHERS = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(110);
    public static final AnimationProperties sTempProperties = new AnimationProperties() {
        public AnimationFilter mAnimationFilter = new AnimationFilter();

        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    };
    public int[] mAbsolutePosition = new int[2];
    public int mActualLayoutWidth = Integer.MIN_VALUE;
    public float mActualPaddingEnd = -2.14748365E9f;
    public float mActualPaddingStart = -2.14748365E9f;
    public int mAddAnimationStartIndex = -1;
    public boolean mAnimationsEnabled = true;
    public int mCannedAnimationStartIndex = -1;
    public boolean mChangingViewPositions;
    public boolean mDisallowNextAnimation;
    public int mDotPadding;
    public boolean mDozing;
    public IconState mFirstVisibleIconState;
    public int mIconSize;
    public final HashMap<View, IconState> mIconStates = new HashMap<>();
    public boolean mInNotificationIconShelf;
    public boolean mIsStaticLayout = true;
    public StatusBarIconView mIsolatedIcon;
    public View mIsolatedIconForAnimation;
    public Rect mIsolatedIconLocation;
    public IconState mLastVisibleIconState;
    public int mNumDots;
    public boolean mOnLockScreen;
    public ArrayMap<String, ArrayList<StatusBarIcon>> mReplacingIcons;
    public int mSpeedBumpIndex = -1;
    public int mStaticDotDiameter;
    public int mStaticDotRadius;
    public int mThemedTextColorPrimary;
    public float mVisualOverflowStart;

    public NotificationIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initDimens();
        setWillNotDraw(true);
    }

    public final void initDimens() {
        this.mDotPadding = getResources().getDimensionPixelSize(R$dimen.overflow_icon_dot_padding);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.overflow_dot_radius);
        this.mStaticDotRadius = dimensionPixelSize;
        this.mStaticDotDiameter = dimensionPixelSize * 2;
        this.mThemedTextColorPrimary = Utils.getColorAttr(new ContextThemeWrapper(getContext(), 16974563), 16842806).getDefaultColor();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(getActualPaddingStart(), 0.0f, getLayoutEnd(), (float) getHeight(), paint);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float height = ((float) getHeight()) / 2.0f;
        this.mIconSize = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i6 = (int) (height - (((float) measuredHeight) / 2.0f));
            childAt.layout(0, i6, measuredWidth, measuredHeight + i6);
            if (i5 == 0) {
                setIconSize(childAt.getWidth());
            }
        }
        getLocationOnScreen(this.mAbsolutePosition);
        if (this.mIsStaticLayout) {
            updateState();
        }
    }

    public void setIconSize(int i) {
        this.mIconSize = i;
    }

    public final void updateState() {
        resetViewStates();
        calculateIconXTranslations();
        applyIconStates();
    }

    public void applyIconStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            ViewState viewState = this.mIconStates.get(childAt);
            if (viewState != null) {
                viewState.applyToView(childAt);
            }
        }
        this.mAddAnimationStartIndex = -1;
        this.mCannedAnimationStartIndex = -1;
        this.mDisallowNextAnimation = false;
        this.mIsolatedIconForAnimation = null;
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        boolean isReplacingIcon = isReplacingIcon(view);
        if (!this.mChangingViewPositions) {
            IconState iconState = new IconState(view);
            if (isReplacingIcon) {
                iconState.justAdded = false;
                iconState.justReplaced = true;
            }
            this.mIconStates.put(view, iconState);
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild < getChildCount() - 1 && !isReplacingIcon && this.mIconStates.get(getChildAt(indexOfChild + 1)).iconAppearAmount > 0.0f) {
            int i = this.mAddAnimationStartIndex;
            if (i < 0) {
                this.mAddAnimationStartIndex = indexOfChild;
            } else {
                this.mAddAnimationStartIndex = Math.min(i, indexOfChild);
            }
        }
        if (view instanceof StatusBarIconView) {
            ((StatusBarIconView) view).setDozing(this.mDozing, false, 0);
        }
    }

    public final boolean isReplacingIcon(View view) {
        if (this.mReplacingIcons == null || !(view instanceof StatusBarIconView)) {
            return false;
        }
        StatusBarIconView statusBarIconView = (StatusBarIconView) view;
        Icon sourceIcon = statusBarIconView.getSourceIcon();
        ArrayList arrayList = this.mReplacingIcons.get(statusBarIconView.getNotification().getGroupKey());
        if (arrayList == null || !sourceIcon.sameAs(((StatusBarIcon) arrayList.get(0)).icon)) {
            return false;
        }
        return true;
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof StatusBarIconView) {
            boolean isReplacingIcon = isReplacingIcon(view);
            StatusBarIconView statusBarIconView = (StatusBarIconView) view;
            if (areAnimationsEnabled(statusBarIconView) && statusBarIconView.getVisibleState() != 2 && view.getVisibility() == 0 && isReplacingIcon) {
                int findFirstViewIndexAfter = findFirstViewIndexAfter(statusBarIconView.getTranslationX());
                int i = this.mAddAnimationStartIndex;
                if (i < 0) {
                    this.mAddAnimationStartIndex = findFirstViewIndexAfter;
                } else {
                    this.mAddAnimationStartIndex = Math.min(i, findFirstViewIndexAfter);
                }
            }
            if (!this.mChangingViewPositions) {
                this.mIconStates.remove(view);
                if (areAnimationsEnabled(statusBarIconView) && !isReplacingIcon) {
                    boolean z = false;
                    addTransientView(statusBarIconView, 0);
                    if (view == this.mIsolatedIcon) {
                        z = true;
                    }
                    statusBarIconView.setVisibleState(2, true, new NotificationIconContainer$$ExternalSyntheticLambda0(this, statusBarIconView), z ? 110 : 0);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewRemoved$0(StatusBarIconView statusBarIconView) {
        removeTransientView(statusBarIconView);
    }

    public boolean hasMaxNumDot() {
        return this.mNumDots >= 1;
    }

    public final boolean areAnimationsEnabled(StatusBarIconView statusBarIconView) {
        return this.mAnimationsEnabled || statusBarIconView == this.mIsolatedIcon;
    }

    public final int findFirstViewIndexAfter(float f) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getTranslationX() > f) {
                return i;
            }
        }
        return getChildCount();
    }

    public void resetViewStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            ViewState viewState = this.mIconStates.get(childAt);
            viewState.initFrom(childAt);
            StatusBarIconView statusBarIconView = this.mIsolatedIcon;
            viewState.alpha = (statusBarIconView == null || childAt == statusBarIconView) ? 1.0f : 0.0f;
            viewState.hidden = false;
        }
    }

    public float calculateWidthFor(float f) {
        if (f == 0.0f) {
            return 0.0f;
        }
        return getActualPaddingStart() + (((float) this.mIconSize) * MathUtils.min(f, 4.0f)) + getActualPaddingEnd();
    }

    public void calculateIconXTranslations() {
        int i;
        IconState iconState;
        float actualPaddingStart = getActualPaddingStart();
        int childCount = getChildCount();
        if (this.mOnLockScreen) {
            i = 3;
        } else {
            i = this.mIsStaticLayout ? 4 : childCount;
        }
        float layoutEnd = getLayoutEnd();
        float f = 0.0f;
        this.mVisualOverflowStart = 0.0f;
        this.mFirstVisibleIconState = null;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            int i4 = 2;
            boolean z = true;
            if (i3 >= childCount) {
                break;
            }
            View childAt = getChildAt(i3);
            IconState iconState2 = this.mIconStates.get(childAt);
            float f2 = iconState2.iconAppearAmount;
            if (f2 == 1.0f) {
                iconState2.xTranslation = actualPaddingStart;
            }
            if (this.mFirstVisibleIconState == null) {
                this.mFirstVisibleIconState = iconState2;
            }
            int i5 = this.mSpeedBumpIndex;
            boolean z2 = (i5 != -1 && i3 >= i5 && f2 > f) || i3 >= i;
            boolean z3 = i3 == childCount + -1;
            float iconScaleIncreased = (!this.mOnLockScreen || !(childAt instanceof StatusBarIconView)) ? 1.0f : ((StatusBarIconView) childAt).getIconScaleIncreased();
            if (!iconState2.hidden) {
                i4 = 0;
            }
            iconState2.visibleState = i4;
            int i6 = this.mIconSize;
            if (actualPaddingStart <= layoutEnd - ((float) i6)) {
                z = false;
            }
            if (i2 == -1 && (z2 || z)) {
                i2 = (!z3 || z2) ? i3 : i3 - 1;
                float f3 = layoutEnd - ((float) i6);
                this.mVisualOverflowStart = f3;
                if (z2 || this.mIsStaticLayout) {
                    this.mVisualOverflowStart = Math.min(actualPaddingStart, f3);
                }
            }
            actualPaddingStart += iconState2.iconAppearAmount * ((float) childAt.getWidth()) * iconScaleIncreased;
            i3++;
            f = 0.0f;
        }
        this.mNumDots = 0;
        if (i2 != -1) {
            float f4 = this.mVisualOverflowStart;
            while (i2 < childCount) {
                IconState iconState3 = this.mIconStates.get(getChildAt(i2));
                int i7 = this.mStaticDotDiameter + this.mDotPadding;
                iconState3.xTranslation = f4;
                int i8 = this.mNumDots;
                if (i8 < 1) {
                    if (i8 != 0 || iconState3.iconAppearAmount >= 0.8f) {
                        iconState3.visibleState = 1;
                        this.mNumDots = i8 + 1;
                    } else {
                        iconState3.visibleState = 0;
                    }
                    if (this.mNumDots == 1) {
                        i7 *= 1;
                    }
                    f4 += ((float) i7) * iconState3.iconAppearAmount;
                    this.mLastVisibleIconState = iconState3;
                } else {
                    iconState3.visibleState = 2;
                }
                i2++;
            }
        } else if (childCount > 0) {
            this.mLastVisibleIconState = this.mIconStates.get(getChildAt(childCount - 1));
            this.mFirstVisibleIconState = this.mIconStates.get(getChildAt(0));
        }
        if (isLayoutRtl()) {
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt2 = getChildAt(i9);
                IconState iconState4 = this.mIconStates.get(childAt2);
                iconState4.xTranslation = (((float) getWidth()) - iconState4.xTranslation) - ((float) childAt2.getWidth());
            }
        }
        StatusBarIconView statusBarIconView = this.mIsolatedIcon;
        if (statusBarIconView != null && (iconState = this.mIconStates.get(statusBarIconView)) != null) {
            iconState.xTranslation = ((float) (this.mIsolatedIconLocation.left - this.mAbsolutePosition[0])) - (((1.0f - this.mIsolatedIcon.getIconScale()) * ((float) this.mIsolatedIcon.getWidth())) / 2.0f);
            iconState.visibleState = 0;
        }
    }

    public final float getLayoutEnd() {
        return ((float) getActualWidth()) - getActualPaddingEnd();
    }

    public final float getActualPaddingEnd() {
        float f = this.mActualPaddingEnd;
        return f == -2.14748365E9f ? (float) getPaddingEnd() : f;
    }

    public float getActualPaddingStart() {
        float f = this.mActualPaddingStart;
        return f == -2.14748365E9f ? (float) getPaddingStart() : f;
    }

    public void setIsStaticLayout(boolean z) {
        this.mIsStaticLayout = z;
    }

    public void setActualLayoutWidth(int i) {
        this.mActualLayoutWidth = i;
    }

    public int getActualWidth() {
        int i = this.mActualLayoutWidth;
        return i == Integer.MIN_VALUE ? getWidth() : i;
    }

    public void setChangingViewPositions(boolean z) {
        this.mChangingViewPositions = z;
    }

    public void setDozing(boolean z, boolean z2, long j) {
        this.mDozing = z;
        this.mDisallowNextAnimation |= !z2;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof StatusBarIconView) {
                ((StatusBarIconView) childAt).setDozing(z, z2, j);
            }
        }
    }

    public IconState getIconState(StatusBarIconView statusBarIconView) {
        return this.mIconStates.get(statusBarIconView);
    }

    public void setSpeedBumpIndex(int i) {
        this.mSpeedBumpIndex = i;
    }

    public void setAnimationsEnabled(boolean z) {
        if (!z && this.mAnimationsEnabled) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                ViewState viewState = this.mIconStates.get(childAt);
                if (viewState != null) {
                    viewState.cancelAnimations(childAt);
                    viewState.applyToView(childAt);
                }
            }
        }
        this.mAnimationsEnabled = z;
    }

    public void setReplacingIcons(ArrayMap<String, ArrayList<StatusBarIcon>> arrayMap) {
        this.mReplacingIcons = arrayMap;
    }

    public void showIconIsolated(StatusBarIconView statusBarIconView, boolean z) {
        if (z) {
            this.mIsolatedIconForAnimation = statusBarIconView != null ? statusBarIconView : this.mIsolatedIcon;
        }
        this.mIsolatedIcon = statusBarIconView;
        updateState();
    }

    public void setIsolatedIconLocation(Rect rect, boolean z) {
        this.mIsolatedIconLocation = rect;
        if (z) {
            updateState();
        }
    }

    public void setOnLockScreen(boolean z) {
        this.mOnLockScreen = z;
    }

    public void setInNotificationIconShelf(boolean z) {
        this.mInNotificationIconShelf = z;
    }

    public class IconState extends ViewState {
        public float clampedAppearAmount = 1.0f;
        public float iconAppearAmount = 1.0f;
        public int iconColor = 0;
        public boolean justAdded = true;
        public boolean justReplaced;
        public final Consumer<Property> mCannedAnimationEndListener;
        public final View mView;
        public boolean needsCannedAnimation;
        public boolean noAnimations;
        public int visibleState;

        public IconState(View view) {
            this.mView = view;
            this.mCannedAnimationEndListener = new NotificationIconContainer$IconState$$ExternalSyntheticLambda0(this);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(Property property) {
            if (property == View.TRANSLATION_Y && this.iconAppearAmount == 0.0f && this.mView.getVisibility() == 0) {
                this.mView.setVisibility(4);
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:49:0x009c  */
        /* JADX WARNING: Removed duplicated region for block: B:69:0x0149  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void applyToView(android.view.View r14) {
            /*
                r13 = this;
                boolean r0 = r14 instanceof com.android.systemui.statusbar.StatusBarIconView
                r1 = 0
                if (r0 == 0) goto L_0x01ba
                r0 = r14
                com.android.systemui.statusbar.StatusBarIconView r0 = (com.android.systemui.statusbar.StatusBarIconView) r0
                int r2 = r13.visibleState
                r3 = 2
                r4 = 1
                if (r2 != r3) goto L_0x0014
                int r2 = r0.getVisibleState()
                if (r2 == r4) goto L_0x001e
            L_0x0014:
                int r2 = r13.visibleState
                if (r2 != r4) goto L_0x0020
                int r2 = r0.getVisibleState()
                if (r2 != r3) goto L_0x0020
            L_0x001e:
                r2 = r4
                goto L_0x0021
            L_0x0020:
                r2 = r1
            L_0x0021:
                com.android.systemui.statusbar.phone.NotificationIconContainer r5 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                boolean r5 = r5.areAnimationsEnabled(r0)
                if (r5 == 0) goto L_0x0039
                com.android.systemui.statusbar.phone.NotificationIconContainer r5 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                boolean r5 = r5.mDisallowNextAnimation
                if (r5 != 0) goto L_0x0039
                boolean r5 = r13.noAnimations
                if (r5 != 0) goto L_0x0039
                if (r2 != 0) goto L_0x0039
                r2 = r4
                goto L_0x003a
            L_0x0039:
                r2 = r1
            L_0x003a:
                r5 = 0
                if (r2 == 0) goto L_0x0179
                boolean r6 = r13.justAdded
                if (r6 != 0) goto L_0x0053
                boolean r6 = r13.justReplaced
                if (r6 == 0) goto L_0x0046
                goto L_0x0053
            L_0x0046:
                int r6 = r13.visibleState
                int r7 = r0.getVisibleState()
                if (r6 == r7) goto L_0x006d
                com.android.systemui.statusbar.notification.stack.AnimationProperties r6 = com.android.systemui.statusbar.phone.NotificationIconContainer.DOT_ANIMATION_PROPERTIES
                goto L_0x006b
            L_0x0053:
                super.applyToView(r0)
                boolean r6 = r13.justAdded
                if (r6 == 0) goto L_0x006d
                float r6 = r13.iconAppearAmount
                r7 = 0
                int r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1))
                if (r6 == 0) goto L_0x006d
                r0.setAlpha(r7)
                r0.setVisibleState(r3, r1)
                com.android.systemui.statusbar.notification.stack.AnimationProperties r6 = com.android.systemui.statusbar.phone.NotificationIconContainer.ADD_ICON_PROPERTIES
            L_0x006b:
                r7 = r4
                goto L_0x006f
            L_0x006d:
                r7 = r1
                r6 = r5
            L_0x006f:
                if (r7 != 0) goto L_0x0096
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r8 = r8.mAddAnimationStartIndex
                if (r8 < 0) goto L_0x0096
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r8 = r8.indexOfChild(r14)
                com.android.systemui.statusbar.phone.NotificationIconContainer r9 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r9 = r9.mAddAnimationStartIndex
                if (r8 < r9) goto L_0x0096
                int r8 = r0.getVisibleState()
                if (r8 != r3) goto L_0x0091
                int r8 = r13.visibleState
                if (r8 == r3) goto L_0x0096
            L_0x0091:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r6 = com.android.systemui.statusbar.phone.NotificationIconContainer.DOT_ANIMATION_PROPERTIES
                r7 = r4
            L_0x0096:
                boolean r8 = r13.needsCannedAnimation
                r9 = 100
                if (r8 == 0) goto L_0x0102
                com.android.systemui.statusbar.notification.stack.AnimationProperties r7 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                com.android.systemui.statusbar.notification.stack.AnimationFilter r7 = r7.getAnimationFilter()
                r7.reset()
                com.android.systemui.statusbar.notification.stack.AnimationProperties r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.ICON_ANIMATION_PROPERTIES
                com.android.systemui.statusbar.notification.stack.AnimationFilter r8 = r8.getAnimationFilter()
                r7.combineFilter(r8)
                com.android.systemui.statusbar.notification.stack.AnimationProperties r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r8.resetCustomInterpolators()
                com.android.systemui.statusbar.notification.stack.AnimationProperties r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                com.android.systemui.statusbar.notification.stack.AnimationProperties r11 = com.android.systemui.statusbar.phone.NotificationIconContainer.ICON_ANIMATION_PROPERTIES
                r8.combineCustomInterpolators(r11)
                boolean r8 = r0.showsConversation()
                if (r8 == 0) goto L_0x00cd
                android.view.animation.Interpolator r8 = com.android.systemui.animation.Interpolators.ICON_OVERSHOT_LESS
                goto L_0x00cf
            L_0x00cd:
                android.view.animation.Interpolator r8 = com.android.systemui.animation.Interpolators.ICON_OVERSHOT
            L_0x00cf:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r11 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                android.util.Property r12 = android.view.View.TRANSLATION_Y
                r11.setCustomInterpolator(r12, r8)
                com.android.systemui.statusbar.notification.stack.AnimationProperties r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                java.util.function.Consumer<android.util.Property> r11 = r13.mCannedAnimationEndListener
                r8.setAnimationEndAction(r11)
                if (r6 == 0) goto L_0x00f1
                com.android.systemui.statusbar.notification.stack.AnimationFilter r8 = r6.getAnimationFilter()
                r7.combineFilter(r8)
                com.android.systemui.statusbar.notification.stack.AnimationProperties r7 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r7.combineCustomInterpolators(r6)
            L_0x00f1:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r6 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r6.setDuration(r9)
                com.android.systemui.statusbar.phone.NotificationIconContainer r7 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r8 = r7.indexOfChild(r14)
                r7.mCannedAnimationStartIndex = r8
                r7 = r4
            L_0x0102:
                if (r7 != 0) goto L_0x0141
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r8 = r8.mCannedAnimationStartIndex
                if (r8 < 0) goto L_0x0141
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r8 = r8.indexOfChild(r14)
                com.android.systemui.statusbar.phone.NotificationIconContainer r11 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r11 = r11.mCannedAnimationStartIndex
                if (r8 <= r11) goto L_0x0141
                int r8 = r0.getVisibleState()
                if (r8 != r3) goto L_0x0124
                int r8 = r13.visibleState
                if (r8 == r3) goto L_0x0141
            L_0x0124:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                com.android.systemui.statusbar.notification.stack.AnimationFilter r3 = r3.getAnimationFilter()
                r3.reset()
                r3.animateX()
                com.android.systemui.statusbar.notification.stack.AnimationProperties r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r3.resetCustomInterpolators()
                com.android.systemui.statusbar.notification.stack.AnimationProperties r6 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r6.setDuration(r9)
                r7 = r4
            L_0x0141:
                com.android.systemui.statusbar.phone.NotificationIconContainer r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                android.view.View r3 = r3.mIsolatedIconForAnimation
                if (r3 == 0) goto L_0x017b
                com.android.systemui.statusbar.phone.NotificationIconContainer r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                android.view.View r3 = r3.mIsolatedIconForAnimation
                r6 = 0
                if (r14 != r3) goto L_0x0165
                com.android.systemui.statusbar.notification.stack.AnimationProperties r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.UNISOLATION_PROPERTY
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                com.android.systemui.statusbar.StatusBarIconView r8 = r8.mIsolatedIcon
                if (r8 == 0) goto L_0x0160
                goto L_0x0161
            L_0x0160:
                r9 = r6
            L_0x0161:
                r3.setDelay(r9)
                goto L_0x0176
            L_0x0165:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.UNISOLATION_PROPERTY_OTHERS
                com.android.systemui.statusbar.phone.NotificationIconContainer r8 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                com.android.systemui.statusbar.StatusBarIconView r8 = r8.mIsolatedIcon
                if (r8 != 0) goto L_0x0172
                goto L_0x0173
            L_0x0172:
                r9 = r6
            L_0x0173:
                r3.setDelay(r9)
            L_0x0176:
                r6 = r3
                r7 = r4
                goto L_0x017b
            L_0x0179:
                r7 = r1
                r6 = r5
            L_0x017b:
                int r3 = r13.visibleState
                r0.setVisibleState(r3, r2)
                com.android.systemui.statusbar.phone.NotificationIconContainer r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                boolean r3 = r3.mInNotificationIconShelf
                if (r3 == 0) goto L_0x018f
                com.android.systemui.statusbar.phone.NotificationIconContainer r3 = com.android.systemui.statusbar.phone.NotificationIconContainer.this
                int r3 = r3.mThemedTextColorPrimary
                goto L_0x0191
            L_0x018f:
                int r3 = r13.iconColor
            L_0x0191:
                boolean r8 = r13.needsCannedAnimation
                if (r8 == 0) goto L_0x0199
                if (r2 == 0) goto L_0x0199
                r2 = r4
                goto L_0x019a
            L_0x0199:
                r2 = r1
            L_0x019a:
                r0.setIconColor(r3, r2)
                if (r7 == 0) goto L_0x01a3
                r13.animateTo(r0, r6)
                goto L_0x01a6
            L_0x01a3:
                super.applyToView(r14)
            L_0x01a6:
                com.android.systemui.statusbar.notification.stack.AnimationProperties r14 = com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties
                r14.setAnimationEndAction(r5)
                float r14 = r13.iconAppearAmount
                r2 = 1065353216(0x3f800000, float:1.0)
                int r14 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
                if (r14 != 0) goto L_0x01b6
                goto L_0x01b7
            L_0x01b6:
                r4 = r1
            L_0x01b7:
                r0.setIsInShelf(r4)
            L_0x01ba:
                r13.justAdded = r1
                r13.justReplaced = r1
                r13.needsCannedAnimation = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationIconContainer.IconState.applyToView(android.view.View):void");
        }

        public void initFrom(View view) {
            super.initFrom(view);
            if (view instanceof StatusBarIconView) {
                this.iconColor = ((StatusBarIconView) view).getStaticDrawableColor();
            }
        }
    }
}
