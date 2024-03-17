package com.android.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
import com.android.systemui.R$integer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SideLabelTileLayout.kt */
public class SideLabelTileLayout extends TileLayout {
    public boolean useSidePadding() {
        return false;
    }

    public SideLabelTileLayout(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean updateResources() {
        boolean updateResources = super.updateResources();
        this.mMaxAllowedRows = getContext().getResources().getInteger(R$integer.quick_settings_max_rows);
        return updateResources;
    }

    public final int getPhantomTopPosition(int i) {
        return getRowTop(i / this.mColumns);
    }

    public boolean updateMaxRows(int i, int i2) {
        int i3 = this.mRows;
        int i4 = this.mMaxAllowedRows;
        this.mRows = i4;
        int i5 = this.mColumns;
        if (i4 > ((i2 + i5) - 1) / i5) {
            this.mRows = ((i2 + i5) - 1) / i5;
        }
        if (i3 != this.mRows) {
            return true;
        }
        return false;
    }
}
