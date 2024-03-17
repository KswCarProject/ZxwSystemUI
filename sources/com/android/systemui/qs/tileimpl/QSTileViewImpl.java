package com.android.systemui.qs.tileimpl;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.LaunchableView;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: QSTileViewImpl.kt */
public class QSTileViewImpl extends QSTileView implements HeightOverrideable, LaunchableView {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final QSIconView _icon;
    public int _position = -1;
    @Nullable
    public String accessibilityClass;
    public boolean blockVisibilityChanges;
    public ImageView chevronView;
    public final boolean collapsed;
    public final int colorActive;
    public Drawable colorBackgroundDrawable;
    public final int colorInactive;
    public final int colorLabelActive;
    public final int colorLabelInactive;
    public final int colorLabelUnavailable;
    public final int colorSecondaryLabelActive;
    public final int colorSecondaryLabelInactive;
    public final int colorSecondaryLabelUnavailable;
    public final int colorUnavailable;
    public ImageView customDrawableView;
    public int heightOverride = -1;
    public TextView label;
    public IgnorableChildLinearLayout labelContainer;
    public int lastState;
    @Nullable
    public CharSequence lastStateDescription;
    public int lastVisibility;
    @NotNull
    public final int[] locInScreen;
    public int paintColor;
    public RippleDrawable ripple;
    public TextView secondaryLabel;
    public boolean showRippleEffect;
    public ViewGroup sideView;
    @NotNull
    public final ValueAnimator singleAnimator;
    public float squishinessFraction = 1.0f;
    @Nullable
    public CharSequence stateDescriptionDeltas;
    public boolean tileState;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public QSTileViewImpl(@NotNull Context context, @NotNull QSIconView qSIconView, boolean z) {
        super(context);
        this._icon = qSIconView;
        this.collapsed = z;
        this.colorActive = Utils.getColorAttrDefaultColor(context, 17956900);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(context, R$attr.offStateColor);
        this.colorInactive = colorAttrDefaultColor;
        this.colorUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor);
        this.colorLabelActive = Utils.getColorAttrDefaultColor(context, 17957103);
        int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(context, 16842806);
        this.colorLabelInactive = colorAttrDefaultColor2;
        this.colorLabelUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor2);
        this.colorSecondaryLabelActive = Utils.getColorAttrDefaultColor(context, 16842810);
        int colorAttrDefaultColor3 = Utils.getColorAttrDefaultColor(context, 16842808);
        this.colorSecondaryLabelInactive = colorAttrDefaultColor3;
        this.colorSecondaryLabelUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor3);
        this.showRippleEffect = true;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.addUpdateListener(new QSTileViewImpl$singleAnimator$1$1(this));
        this.singleAnimator = valueAnimator;
        this.lastState = -1;
        this.locInScreen = new int[2];
        setId(LinearLayout.generateViewId());
        setOrientation(0);
        setGravity(8388627);
        setImportantForAccessibility(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setBackground(createTileBackground());
        setColor(getBackgroundColorForState(2));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        addView(qSIconView, new LinearLayout.LayoutParams(dimensionPixelSize2, dimensionPixelSize2));
        createAndAddLabels();
        createAndAddSideView();
    }

    /* compiled from: QSTileViewImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getTILE_STATE_RES_PREFIX$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
        }

        public Companion() {
        }
    }

    public void setPosition(int i) {
        this._position = i;
    }

    public int getHeightOverride() {
        return this.heightOverride;
    }

    public void setHeightOverride(int i) {
        if (this.heightOverride != i) {
            this.heightOverride = i;
            updateHeight();
        }
    }

    public float getSquishinessFraction() {
        return this.squishinessFraction;
    }

    public void setSquishinessFraction(float f) {
        if (!(this.squishinessFraction == f)) {
            this.squishinessFraction = f;
            updateHeight();
        }
    }

    @NotNull
    /* renamed from: getSecondaryLabel  reason: collision with other method in class */
    public final TextView m1836getSecondaryLabel() {
        TextView textView = this.secondaryLabel;
        if (textView != null) {
            return textView;
        }
        return null;
    }

    public final void setSecondaryLabel(@NotNull TextView textView) {
        this.secondaryLabel = textView;
    }

    @NotNull
    public final ViewGroup getSideView() {
        ViewGroup viewGroup = this.sideView;
        if (viewGroup != null) {
            return viewGroup;
        }
        return null;
    }

    public final void setSideView(@NotNull ViewGroup viewGroup) {
        this.sideView = viewGroup;
    }

    public final void setShowRippleEffect(boolean z) {
        this.showRippleEffect = z;
    }

    public void onConfigurationChanged(@Nullable Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    public void onMeasure(int i, int i2) {
        Trace.traceBegin(4096, "QSTileViewImpl#onMeasure");
        super.onMeasure(i, i2);
        Trace.endSection();
    }

    public void resetOverride() {
        setHeightOverride(-1);
        updateHeight();
    }

    public final void updateResources() {
        TextView textView = this.label;
        ImageView imageView = null;
        if (textView == null) {
            textView = null;
        }
        int i = R$dimen.qs_tile_text_size;
        FontSizeUtils.updateFontSize(textView, i);
        FontSizeUtils.updateFontSize(getSecondaryLabel(), i);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        ViewGroup.LayoutParams layoutParams = this._icon.getLayoutParams();
        layoutParams.height = dimensionPixelSize;
        layoutParams.width = dimensionPixelSize;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.qs_label_container_margin);
        IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
        if (ignorableChildLinearLayout == null) {
            ignorableChildLinearLayout = null;
        }
        ViewGroup.LayoutParams layoutParams2 = ignorableChildLinearLayout.getLayoutParams();
        if (layoutParams2 != null) {
            ((ViewGroup.MarginLayoutParams) layoutParams2).setMarginStart(dimensionPixelSize3);
            ViewGroup.LayoutParams layoutParams3 = getSideView().getLayoutParams();
            if (layoutParams3 != null) {
                ((ViewGroup.MarginLayoutParams) layoutParams3).setMarginStart(dimensionPixelSize3);
                ImageView imageView2 = this.chevronView;
                if (imageView2 == null) {
                    imageView2 = null;
                }
                ViewGroup.LayoutParams layoutParams4 = imageView2.getLayoutParams();
                if (layoutParams4 != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams4;
                    marginLayoutParams.height = dimensionPixelSize;
                    marginLayoutParams.width = dimensionPixelSize;
                    int dimensionPixelSize4 = getResources().getDimensionPixelSize(R$dimen.qs_drawable_end_margin);
                    ImageView imageView3 = this.customDrawableView;
                    if (imageView3 != null) {
                        imageView = imageView3;
                    }
                    ViewGroup.LayoutParams layoutParams5 = imageView.getLayoutParams();
                    if (layoutParams5 != null) {
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams5;
                        marginLayoutParams2.height = dimensionPixelSize;
                        marginLayoutParams2.setMarginEnd(dimensionPixelSize4);
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
    }

    public final void createAndAddLabels() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.qs_tile_label, this, false);
        if (inflate != null) {
            IgnorableChildLinearLayout ignorableChildLinearLayout = (IgnorableChildLinearLayout) inflate;
            this.labelContainer = ignorableChildLinearLayout;
            this.label = (TextView) ignorableChildLinearLayout.requireViewById(R$id.tile_label);
            IgnorableChildLinearLayout ignorableChildLinearLayout2 = this.labelContainer;
            IgnorableChildLinearLayout ignorableChildLinearLayout3 = null;
            if (ignorableChildLinearLayout2 == null) {
                ignorableChildLinearLayout2 = null;
            }
            setSecondaryLabel((TextView) ignorableChildLinearLayout2.requireViewById(R$id.app_label));
            if (this.collapsed) {
                IgnorableChildLinearLayout ignorableChildLinearLayout4 = this.labelContainer;
                if (ignorableChildLinearLayout4 == null) {
                    ignorableChildLinearLayout4 = null;
                }
                ignorableChildLinearLayout4.setIgnoreLastView(true);
                IgnorableChildLinearLayout ignorableChildLinearLayout5 = this.labelContainer;
                if (ignorableChildLinearLayout5 == null) {
                    ignorableChildLinearLayout5 = null;
                }
                ignorableChildLinearLayout5.setForceUnspecifiedMeasure(true);
                getSecondaryLabel().setAlpha(0.0f);
            }
            setLabelColor(getLabelColorForState(2));
            setSecondaryLabelColor(getSecondaryLabelColorForState(2));
            IgnorableChildLinearLayout ignorableChildLinearLayout6 = this.labelContainer;
            if (ignorableChildLinearLayout6 != null) {
                ignorableChildLinearLayout3 = ignorableChildLinearLayout6;
            }
            addView(ignorableChildLinearLayout3);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout");
    }

    public final void createAndAddSideView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.qs_tile_side_icon, this, false);
        if (inflate != null) {
            setSideView((ViewGroup) inflate);
            this.customDrawableView = (ImageView) getSideView().requireViewById(R$id.customDrawable);
            this.chevronView = (ImageView) getSideView().requireViewById(R$id.chevron);
            setChevronColor(getChevronColorForState(2));
            addView(getSideView());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    @NotNull
    public final Drawable createTileBackground() {
        Drawable drawable = this.mContext.getDrawable(R$drawable.qs_tile_background);
        if (drawable != null) {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            this.ripple = rippleDrawable;
            this.colorBackgroundDrawable = rippleDrawable.findDrawableByLayerId(R$id.background);
            RippleDrawable rippleDrawable2 = this.ripple;
            if (rippleDrawable2 == null) {
                return null;
            }
            return rippleDrawable2;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateHeight();
    }

    public final void updateHeight() {
        int i;
        if (getHeightOverride() != -1) {
            i = getHeightOverride();
        } else {
            i = getMeasuredHeight();
        }
        setBottom(getTop() + ((int) (((float) i) * QSTileViewImplKt.constrainSquishiness(getSquishinessFraction()))));
        setScrollY((i - getHeight()) / 2);
    }

    @NotNull
    public View updateAccessibilityOrder(@Nullable View view) {
        setAccessibilityTraversalAfter(view == null ? 0 : view.getId());
        return this;
    }

    @NotNull
    public QSIconView getIcon() {
        return this._icon;
    }

    @NotNull
    public View getIconWithBackground() {
        return getIcon();
    }

    public void init(@NotNull QSTile qSTile) {
        init(new QSTileViewImpl$init$1(qSTile, this), new QSTileViewImpl$init$2(qSTile, this));
    }

    public final void init(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        setOnClickListener(onClickListener);
        setOnLongClickListener(onLongClickListener);
    }

    public void onStateChanged(@NotNull QSTile.State state) {
        post(new QSTileViewImpl$onStateChanged$1(this, state));
    }

    public int getDetailY() {
        return getTop() + (getHeight() / 2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        if (r3 == null) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setClickable(boolean r3) {
        /*
            r2 = this;
            super.setClickable(r3)
            r0 = 0
            if (r3 == 0) goto L_0x0019
            boolean r3 = r2.showRippleEffect
            if (r3 == 0) goto L_0x0019
            android.graphics.drawable.RippleDrawable r3 = r2.ripple
            if (r3 != 0) goto L_0x000f
            r3 = r0
        L_0x000f:
            android.graphics.drawable.Drawable r1 = r2.colorBackgroundDrawable
            if (r1 != 0) goto L_0x0014
            goto L_0x0015
        L_0x0014:
            r0 = r1
        L_0x0015:
            r0.setCallback(r3)
            goto L_0x001e
        L_0x0019:
            android.graphics.drawable.Drawable r3 = r2.colorBackgroundDrawable
            if (r3 != 0) goto L_0x001e
            goto L_0x001f
        L_0x001e:
            r0 = r3
        L_0x001f:
            r2.setBackground(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tileimpl.QSTileViewImpl.setClickable(boolean):void");
    }

    @NotNull
    public View getLabelContainer() {
        IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
        if (ignorableChildLinearLayout == null) {
            return null;
        }
        return ignorableChildLinearLayout;
    }

    @NotNull
    public View getLabel() {
        TextView textView = this.label;
        if (textView == null) {
            return null;
        }
        return textView;
    }

    @NotNull
    public View getSecondaryLabel() {
        return getSecondaryLabel();
    }

    @NotNull
    public View getSecondaryIcon() {
        return getSideView();
    }

    public void setShouldBlockVisibilityChanges(boolean z) {
        this.blockVisibilityChanges = z;
        if (z) {
            this.lastVisibility = getVisibility();
        } else {
            setVisibility(this.lastVisibility);
        }
    }

    public void setVisibility(int i) {
        if (this.blockVisibilityChanges) {
            this.lastVisibility = i;
        } else {
            super.setVisibility(i);
        }
    }

    public void setTransitionVisibility(int i) {
        if (this.blockVisibilityChanges) {
            this.lastVisibility = i;
        } else {
            super.setTransitionVisibility(i);
        }
    }

    public void onInitializeAccessibilityEvent(@NotNull AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityEvent.setClassName(this.accessibilityClass);
        }
        if (accessibilityEvent.getContentChangeTypes() == 64 && this.stateDescriptionDeltas != null) {
            accessibilityEvent.getText().add(this.stateDescriptionDeltas);
            this.stateDescriptionDeltas = null;
        }
    }

    public void onInitializeAccessibilityNodeInfo(@NotNull AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setSelected(false);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityNodeInfo.setClassName(this.accessibilityClass);
            if (Intrinsics.areEqual((Object) Switch.class.getName(), (Object) this.accessibilityClass)) {
                accessibilityNodeInfo.setText(getResources().getString(this.tileState ? R$string.switch_bar_on : R$string.switch_bar_off));
                accessibilityNodeInfo.setChecked(this.tileState);
                accessibilityNodeInfo.setCheckable(true);
                if (isLongClickable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK.getId(), getResources().getString(R$string.accessibility_long_click_tile)));
                }
            }
        }
        if (this._position != -1) {
            accessibilityNodeInfo.setCollectionItemInfo(new AccessibilityNodeInfo.CollectionItemInfo(this._position, 1, 0, 1, false));
        }
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("locInScreen=(" + this.locInScreen[0] + ", " + this.locInScreen[1] + ')');
        sb.append(Intrinsics.stringPlus(", iconView=", this._icon));
        sb.append(Intrinsics.stringPlus(", tileState=", Boolean.valueOf(this.tileState)));
        sb.append("]");
        return sb.toString();
    }

    public void handleStateChanged(@NotNull QSTile.State state) {
        String str;
        boolean z;
        boolean animationsEnabled = animationsEnabled();
        this.showRippleEffect = state.showRippleEffect;
        setClickable(state.state != 0);
        setLongClickable(state.handlesLongClick);
        getIcon().setIcon(state, animationsEnabled);
        setContentDescription(state.contentDescription);
        StringBuilder sb = new StringBuilder();
        String stateText = getStateText(state);
        if (!TextUtils.isEmpty(stateText)) {
            sb.append(stateText);
            if (TextUtils.isEmpty(state.secondaryLabel)) {
                state.secondaryLabel = stateText;
            }
        }
        if (!TextUtils.isEmpty(state.stateDescription)) {
            sb.append(", ");
            sb.append(state.stateDescription);
            int i = this.lastState;
            if (i != -1 && state.state == i && !Intrinsics.areEqual((Object) state.stateDescription, (Object) this.lastStateDescription)) {
                this.stateDescriptionDeltas = state.stateDescription;
            }
        }
        setStateDescription(sb.toString());
        this.lastStateDescription = state.stateDescription;
        TextView textView = null;
        if (state.state == 0) {
            str = null;
        } else {
            str = state.expandedAccessibilityClassName;
        }
        this.accessibilityClass = str;
        if ((state instanceof QSTile.BooleanState) && this.tileState != (z = ((QSTile.BooleanState) state).value)) {
            this.tileState = z;
        }
        TextView textView2 = this.label;
        if (textView2 == null) {
            textView2 = null;
        }
        if (!Objects.equals(textView2.getText(), state.label)) {
            TextView textView3 = this.label;
            if (textView3 == null) {
                textView3 = null;
            }
            textView3.setText(state.label);
        }
        if (!Objects.equals(getSecondaryLabel().getText(), state.secondaryLabel)) {
            getSecondaryLabel().setText(state.secondaryLabel);
            getSecondaryLabel().setVisibility(TextUtils.isEmpty(state.secondaryLabel) ? 8 : 0);
        }
        if (state.state != this.lastState) {
            this.singleAnimator.cancel();
            if (animationsEnabled) {
                ValueAnimator valueAnimator = this.singleAnimator;
                PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[4];
                propertyValuesHolderArr[0] = QSTileViewImplKt.colorValuesHolder("background", this.paintColor, getBackgroundColorForState(state.state));
                int[] iArr = new int[2];
                TextView textView4 = this.label;
                if (textView4 == null) {
                    textView4 = null;
                }
                iArr[0] = textView4.getCurrentTextColor();
                iArr[1] = getLabelColorForState(state.state);
                propertyValuesHolderArr[1] = QSTileViewImplKt.colorValuesHolder("label", iArr);
                propertyValuesHolderArr[2] = QSTileViewImplKt.colorValuesHolder("secondaryLabel", getSecondaryLabel().getCurrentTextColor(), getSecondaryLabelColorForState(state.state));
                int[] iArr2 = new int[2];
                ImageView imageView = this.chevronView;
                if (imageView == null) {
                    imageView = null;
                }
                ColorStateList imageTintList = imageView.getImageTintList();
                iArr2[0] = imageTintList == null ? 0 : imageTintList.getDefaultColor();
                iArr2[1] = getChevronColorForState(state.state);
                propertyValuesHolderArr[3] = QSTileViewImplKt.colorValuesHolder("chevron", iArr2);
                valueAnimator.setValues(propertyValuesHolderArr);
                this.singleAnimator.start();
            } else {
                setAllColors(getBackgroundColorForState(state.state), getLabelColorForState(state.state), getSecondaryLabelColorForState(state.state), getChevronColorForState(state.state));
            }
        }
        loadSideViewDrawableIfNecessary(state);
        TextView textView5 = this.label;
        if (textView5 != null) {
            textView = textView5;
        }
        textView.setEnabled(!state.disabledByPolicy);
        this.lastState = state.state;
    }

    public final void setAllColors(int i, int i2, int i3, int i4) {
        setColor(i);
        setLabelColor(i2);
        setSecondaryLabelColor(i3);
        setChevronColor(i4);
    }

    public final void setColor(int i) {
        Drawable drawable = this.colorBackgroundDrawable;
        if (drawable == null) {
            drawable = null;
        }
        drawable.mutate().setTint(i);
        this.paintColor = i;
    }

    public final void setLabelColor(int i) {
        TextView textView = this.label;
        if (textView == null) {
            textView = null;
        }
        textView.setTextColor(i);
    }

    public final void setSecondaryLabelColor(int i) {
        getSecondaryLabel().setTextColor(i);
    }

    public final void setChevronColor(int i) {
        ImageView imageView = this.chevronView;
        if (imageView == null) {
            imageView = null;
        }
        imageView.setImageTintList(ColorStateList.valueOf(i));
    }

    public final void loadSideViewDrawableIfNecessary(QSTile.State state) {
        ImageView imageView = null;
        if (state.sideViewCustomDrawable != null) {
            ImageView imageView2 = this.customDrawableView;
            if (imageView2 == null) {
                imageView2 = null;
            }
            imageView2.setImageDrawable(state.sideViewCustomDrawable);
            ImageView imageView3 = this.customDrawableView;
            if (imageView3 == null) {
                imageView3 = null;
            }
            imageView3.setVisibility(0);
            ImageView imageView4 = this.chevronView;
            if (imageView4 != null) {
                imageView = imageView4;
            }
            imageView.setVisibility(8);
        } else if (!(state instanceof QSTile.BooleanState) || ((QSTile.BooleanState) state).forceExpandIcon) {
            ImageView imageView5 = this.customDrawableView;
            if (imageView5 == null) {
                imageView5 = null;
            }
            imageView5.setImageDrawable((Drawable) null);
            ImageView imageView6 = this.customDrawableView;
            if (imageView6 == null) {
                imageView6 = null;
            }
            imageView6.setVisibility(8);
            ImageView imageView7 = this.chevronView;
            if (imageView7 != null) {
                imageView = imageView7;
            }
            imageView.setVisibility(0);
        } else {
            ImageView imageView8 = this.customDrawableView;
            if (imageView8 == null) {
                imageView8 = null;
            }
            imageView8.setImageDrawable((Drawable) null);
            ImageView imageView9 = this.customDrawableView;
            if (imageView9 == null) {
                imageView9 = null;
            }
            imageView9.setVisibility(8);
            ImageView imageView10 = this.chevronView;
            if (imageView10 != null) {
                imageView = imageView10;
            }
            imageView.setVisibility(8);
        }
    }

    public final String getStateText(QSTile.State state) {
        if (state.disabledByPolicy) {
            return getContext().getString(R$string.tile_disabled);
        }
        if (state.state != 0 && !(state instanceof QSTile.BooleanState)) {
            return "";
        }
        return getResources().getStringArray(SubtitleArrayMapping.INSTANCE.getSubtitleId(state.spec))[state.state];
    }

    public boolean animationsEnabled() {
        if (!isShown()) {
            return false;
        }
        if (!(getAlpha() == 1.0f)) {
            return false;
        }
        getLocationOnScreen(this.locInScreen);
        if (this.locInScreen[1] >= (-getHeight())) {
            return true;
        }
        return false;
    }

    public final int getBackgroundColorForState(int i) {
        if (i == 0) {
            return this.colorUnavailable;
        }
        if (i == 1) {
            return this.colorInactive;
        }
        if (i == 2) {
            return this.colorActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    public final int getLabelColorForState(int i) {
        if (i == 0) {
            return this.colorLabelUnavailable;
        }
        if (i == 1) {
            return this.colorLabelInactive;
        }
        if (i == 2) {
            return this.colorLabelActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    public final int getSecondaryLabelColorForState(int i) {
        if (i == 0) {
            return this.colorSecondaryLabelUnavailable;
        }
        if (i == 1) {
            return this.colorSecondaryLabelInactive;
        }
        if (i == 2) {
            return this.colorSecondaryLabelActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    public final int getChevronColorForState(int i) {
        return getSecondaryLabelColorForState(i);
    }
}
