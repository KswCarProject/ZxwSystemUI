package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.RenderInfo;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlAdapter.kt */
public final class ControlHolder extends Holder {
    @NotNull
    public final ControlHolderAccessibilityDelegate accessibilityDelegate;
    @NotNull
    public final CheckBox favorite;
    @NotNull
    public final Function2<String, Boolean, Unit> favoriteCallback;
    public final String favoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_favorite);
    @NotNull
    public final ImageView icon = ((ImageView) this.itemView.requireViewById(R$id.icon));
    @Nullable
    public final ControlsModel.MoveHelper moveHelper;
    public final String notFavoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_not_favorite);
    @NotNull
    public final TextView removed = ((TextView) this.itemView.requireViewById(R$id.status));
    @NotNull
    public final TextView subtitle = ((TextView) this.itemView.requireViewById(R$id.subtitle));
    @NotNull
    public final TextView title = ((TextView) this.itemView.requireViewById(R$id.title));

    @NotNull
    public final Function2<String, Boolean, Unit> getFavoriteCallback() {
        return this.favoriteCallback;
    }

    public ControlHolder(@NotNull View view, @Nullable ControlsModel.MoveHelper moveHelper2, @NotNull Function2<? super String, ? super Boolean, Unit> function2) {
        super(view, (DefaultConstructorMarker) null);
        this.moveHelper = moveHelper2;
        this.favoriteCallback = function2;
        CheckBox checkBox = (CheckBox) this.itemView.requireViewById(R$id.favorite);
        checkBox.setVisibility(0);
        this.favorite = checkBox;
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = new ControlHolderAccessibilityDelegate(new ControlHolder$accessibilityDelegate$1(this), new ControlHolder$accessibilityDelegate$2(this), moveHelper2);
        this.accessibilityDelegate = controlHolderAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this.itemView, controlHolderAccessibilityDelegate);
    }

    public final CharSequence stateDescription(boolean z) {
        if (!z) {
            return this.notFavoriteStateDescription;
        }
        if (this.moveHelper == null) {
            return this.favoriteStateDescription;
        }
        return this.itemView.getContext().getString(R$string.accessibility_control_favorite_position, new Object[]{Integer.valueOf(getLayoutPosition() + 1)});
    }

    public void bindData(@NotNull ElementWrapper elementWrapper) {
        ControlInterface controlInterface = (ControlInterface) elementWrapper;
        RenderInfo renderInfo = getRenderInfo(controlInterface.getComponent(), controlInterface.getDeviceType());
        this.title.setText(controlInterface.getTitle());
        this.subtitle.setText(controlInterface.getSubtitle());
        updateFavorite(controlInterface.getFavorite());
        this.removed.setText(controlInterface.getRemoved() ? this.itemView.getContext().getText(R$string.controls_removed) : "");
        this.itemView.setOnClickListener(new ControlHolder$bindData$1(this, elementWrapper));
        applyRenderInfo(renderInfo, controlInterface);
    }

    public void updateFavorite(boolean z) {
        this.favorite.setChecked(z);
        this.accessibilityDelegate.setFavorite(z);
        this.itemView.setStateDescription(stateDescription(z));
    }

    public final RenderInfo getRenderInfo(ComponentName componentName, int i) {
        return RenderInfo.Companion.lookup$default(RenderInfo.Companion, this.itemView.getContext(), componentName, i, 0, 8, (Object) null);
    }

    public final void applyRenderInfo(RenderInfo renderInfo, ControlInterface controlInterface) {
        Context context = this.itemView.getContext();
        ColorStateList colorStateList = context.getResources().getColorStateList(renderInfo.getForeground(), context.getTheme());
        Unit unit = null;
        this.icon.setImageTintList((ColorStateList) null);
        Icon customIcon = controlInterface.getCustomIcon();
        if (customIcon != null) {
            this.icon.setImageIcon(customIcon);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            this.icon.setImageDrawable(renderInfo.getIcon());
            if (controlInterface.getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
    }
}
