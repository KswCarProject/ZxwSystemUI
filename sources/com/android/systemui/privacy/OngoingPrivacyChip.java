package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.events.BackgroundAnimatableView;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: OngoingPrivacyChip.kt */
public final class OngoingPrivacyChip extends FrameLayout implements BackgroundAnimatableView {
    public int iconColor;
    public int iconMargin;
    public int iconSize;
    public LinearLayout iconsContainer;
    @NotNull
    public List<PrivacyItem> privacyList;

    public OngoingPrivacyChip(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
    }

    public OngoingPrivacyChip(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
    }

    public OngoingPrivacyChip(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public int getChipWidth() {
        return BackgroundAnimatableView.DefaultImpls.getChipWidth(this);
    }

    @NotNull
    public View getView() {
        return BackgroundAnimatableView.DefaultImpls.getView(this);
    }

    public OngoingPrivacyChip(@NotNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.privacyList = CollectionsKt__CollectionsKt.emptyList();
    }

    @NotNull
    public final List<PrivacyItem> getPrivacyList() {
        return this.privacyList;
    }

    public final void setPrivacyList(@NotNull List<PrivacyItem> list) {
        this.privacyList = list;
        updateView(new PrivacyChipBuilder(getContext(), this.privacyList));
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.iconsContainer = (LinearLayout) requireViewById(R$id.icons_container);
        updateResources();
    }

    public void setBoundsForAnimation(int i, int i2, int i3, int i4) {
        LinearLayout linearLayout = this.iconsContainer;
        if (linearLayout == null) {
            linearLayout = null;
        }
        linearLayout.setLeftTopRightBottom(i - getLeft(), i2 - getTop(), i3 - getLeft(), i4 - getTop());
    }

    public static final void updateView$setIcons(OngoingPrivacyChip ongoingPrivacyChip, PrivacyChipBuilder privacyChipBuilder, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        int i = 0;
        for (Object next : privacyChipBuilder.generateIcons()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            Drawable drawable = (Drawable) next;
            drawable.mutate();
            drawable.setTint(ongoingPrivacyChip.iconColor);
            ImageView imageView = new ImageView(ongoingPrivacyChip.getContext());
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int i3 = ongoingPrivacyChip.iconSize;
            viewGroup.addView(imageView, i3, i3);
            if (i != 0) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (layoutParams != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMarginStart(ongoingPrivacyChip.iconMargin);
                    imageView.setLayoutParams(marginLayoutParams);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                }
            }
            i = i2;
        }
    }

    public final void updateView(PrivacyChipBuilder privacyChipBuilder) {
        LinearLayout linearLayout = null;
        if (!this.privacyList.isEmpty()) {
            generateContentDescription(privacyChipBuilder);
            LinearLayout linearLayout2 = this.iconsContainer;
            if (linearLayout2 != null) {
                linearLayout = linearLayout2;
            }
            updateView$setIcons(this, privacyChipBuilder, linearLayout);
        } else {
            LinearLayout linearLayout3 = this.iconsContainer;
            if (linearLayout3 != null) {
                linearLayout = linearLayout3;
            }
            linearLayout.removeAllViews();
        }
        requestLayout();
    }

    public final void generateContentDescription(PrivacyChipBuilder privacyChipBuilder) {
        String joinTypes = privacyChipBuilder.joinTypes();
        setContentDescription(getContext().getString(R$string.ongoing_privacy_chip_content_multiple_apps, new Object[]{joinTypes}));
    }

    public final void updateResources() {
        this.iconMargin = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_icon_margin);
        this.iconSize = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_icon_size);
        this.iconColor = Utils.getColorAttrDefaultColor(getContext(), 16843827);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_side_padding);
        LinearLayout linearLayout = this.iconsContainer;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            linearLayout = null;
        }
        linearLayout.setPaddingRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
        LinearLayout linearLayout3 = this.iconsContainer;
        if (linearLayout3 != null) {
            linearLayout2 = linearLayout3;
        }
        linearLayout2.setBackground(getContext().getDrawable(R$drawable.privacy_chip_bg));
    }
}
