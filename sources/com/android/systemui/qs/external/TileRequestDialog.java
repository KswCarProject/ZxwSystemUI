package com.android.systemui.qs.external;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.tileimpl.QSIconViewImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TileRequestDialog.kt */
public final class TileRequestDialog extends SystemUIDialog {
    public static final int CONTENT_ID = R$id.content;
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public TileRequestDialog(@NotNull Context context) {
        super(context);
    }

    /* compiled from: TileRequestDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public final void setTileData(@NotNull TileData tileData) {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.tile_service_request_dialog, (ViewGroup) null);
        if (inflate != null) {
            ViewGroup viewGroup = (ViewGroup) inflate;
            TextView textView = (TextView) viewGroup.requireViewById(R$id.text);
            textView.setText(textView.getContext().getString(R$string.qs_tile_request_dialog_text, new Object[]{tileData.getAppName()}));
            viewGroup.addView(createTileView(tileData), viewGroup.getContext().getResources().getDimensionPixelSize(R$dimen.qs_tile_service_request_tile_width), viewGroup.getContext().getResources().getDimensionPixelSize(R$dimen.qs_quick_tile_size));
            viewGroup.setSelected(true);
            setView(viewGroup, 0, 0, 0, 0);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final QSTileView createTileView(TileData tileData) {
        Drawable loadDrawable;
        QSTileViewImpl qSTileViewImpl = new QSTileViewImpl(getContext(), new QSIconViewImpl(getContext()), true);
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = tileData.getLabel();
        booleanState.handlesLongClick = false;
        Icon icon = tileData.getIcon();
        QSTile.Icon icon2 = null;
        if (!(icon == null || (loadDrawable = icon.loadDrawable(getContext())) == null)) {
            icon2 = new QSTileImpl.DrawableIcon(loadDrawable);
        }
        if (icon2 == null) {
            icon2 = QSTileImpl.ResourceIcon.get(R$drawable.f11android);
        }
        booleanState.icon = icon2;
        qSTileViewImpl.onStateChanged(booleanState);
        qSTileViewImpl.post(new TileRequestDialog$createTileView$1(qSTileViewImpl));
        return qSTileViewImpl;
    }

    /* compiled from: TileRequestDialog.kt */
    public static final class TileData {
        @NotNull
        public final CharSequence appName;
        @Nullable
        public final Icon icon;
        @NotNull
        public final CharSequence label;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TileData)) {
                return false;
            }
            TileData tileData = (TileData) obj;
            return Intrinsics.areEqual((Object) this.appName, (Object) tileData.appName) && Intrinsics.areEqual((Object) this.label, (Object) tileData.label) && Intrinsics.areEqual((Object) this.icon, (Object) tileData.icon);
        }

        public int hashCode() {
            int hashCode = ((this.appName.hashCode() * 31) + this.label.hashCode()) * 31;
            Icon icon2 = this.icon;
            return hashCode + (icon2 == null ? 0 : icon2.hashCode());
        }

        @NotNull
        public String toString() {
            return "TileData(appName=" + this.appName + ", label=" + this.label + ", icon=" + this.icon + ')';
        }

        public TileData(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @Nullable Icon icon2) {
            this.appName = charSequence;
            this.label = charSequence2;
            this.icon = icon2;
        }

        @NotNull
        public final CharSequence getAppName() {
            return this.appName;
        }

        @NotNull
        public final CharSequence getLabel() {
            return this.label;
        }

        @Nullable
        public final Icon getIcon() {
            return this.icon;
        }
    }
}
