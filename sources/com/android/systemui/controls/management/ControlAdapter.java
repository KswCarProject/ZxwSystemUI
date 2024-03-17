package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlInterface;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlAdapter.kt */
public final class ControlAdapter extends RecyclerView.Adapter<Holder> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final float elevation;
    @Nullable
    public ControlsModel model;

    public ControlAdapter(float f) {
        this.elevation = f;
    }

    /* compiled from: ControlAdapter.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x0027, code lost:
            r2 = r5.screenWidthDp;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int findMaxColumns(@org.jetbrains.annotations.NotNull android.content.res.Resources r5) {
            /*
                r4 = this;
                int r4 = com.android.systemui.R$integer.controls_max_columns
                int r4 = r5.getInteger(r4)
                int r0 = com.android.systemui.R$integer.controls_max_columns_adjust_below_width_dp
                int r0 = r5.getInteger(r0)
                android.util.TypedValue r1 = new android.util.TypedValue
                r1.<init>()
                int r2 = com.android.systemui.R$dimen.controls_max_columns_adjust_above_font_scale
                r3 = 1
                r5.getValue(r2, r1, r3)
                float r1 = r1.getFloat()
                android.content.res.Configuration r5 = r5.getConfiguration()
                int r2 = r5.orientation
                if (r2 != r3) goto L_0x0024
                goto L_0x0025
            L_0x0024:
                r3 = 0
            L_0x0025:
                if (r3 == 0) goto L_0x0035
                int r2 = r5.screenWidthDp
                if (r2 == 0) goto L_0x0035
                if (r2 > r0) goto L_0x0035
                float r5 = r5.fontScale
                int r5 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
                if (r5 < 0) goto L_0x0035
                int r4 = r4 + -1
            L_0x0035:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.ControlAdapter.Companion.findMaxColumns(android.content.res.Resources):int");
        }
    }

    @NotNull
    public Holder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            return new ZoneHolder(from.inflate(R$layout.controls_zone_header, viewGroup, false));
        }
        if (i == 1) {
            View inflate = from.inflate(R$layout.controls_base_item, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
            if (layoutParams != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.width = -1;
                marginLayoutParams.topMargin = 0;
                marginLayoutParams.bottomMargin = 0;
                marginLayoutParams.leftMargin = 0;
                marginLayoutParams.rightMargin = 0;
                inflate.setElevation(this.elevation);
                inflate.setBackground(viewGroup.getContext().getDrawable(R$drawable.control_background_ripple));
                ControlsModel controlsModel = this.model;
                return new ControlHolder(inflate, controlsModel == null ? null : controlsModel.getMoveHelper(), new ControlAdapter$onCreateViewHolder$2(this));
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        } else if (i == 2) {
            return new DividerHolder(from.inflate(R$layout.controls_horizontal_divider_with_empty, viewGroup, false));
        } else {
            throw new IllegalStateException(Intrinsics.stringPlus("Wrong viewType: ", Integer.valueOf(i)));
        }
    }

    public final void changeModel(@NotNull ControlsModel controlsModel) {
        this.model = controlsModel;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<ElementWrapper> elements;
        ControlsModel controlsModel = this.model;
        if (controlsModel == null || (elements = controlsModel.getElements()) == null) {
            return 0;
        }
        return elements.size();
    }

    public void onBindViewHolder(@NotNull Holder holder, int i) {
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            holder.bindData(controlsModel.getElements().get(i));
        }
    }

    public void onBindViewHolder(@NotNull Holder holder, int i, @NotNull List<Object> list) {
        if (list.isEmpty()) {
            super.onBindViewHolder(holder, i, list);
            return;
        }
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ControlInterface) {
                holder.updateFavorite(((ControlInterface) elementWrapper).getFavorite());
            }
        }
    }

    public int getItemViewType(int i) {
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ZoneNameWrapper) {
                return 0;
            }
            if ((elementWrapper instanceof ControlStatusWrapper) || (elementWrapper instanceof ControlInfoWrapper)) {
                return 1;
            }
            if (elementWrapper instanceof DividerWrapper) {
                return 2;
            }
            throw new NoWhenBranchMatchedException();
        }
        throw new IllegalStateException("Getting item type for null model");
    }
}
