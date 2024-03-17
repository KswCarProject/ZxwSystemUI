package androidx.leanback.widget;

import android.view.View;
import androidx.leanback.widget.ItemAlignmentFacet;

public class ItemAlignment {
    public final Axis horizontal;
    public Axis mMainAxis;
    public int mOrientation = 0;
    public Axis mSecondAxis;
    public final Axis vertical;

    public ItemAlignment() {
        Axis axis = new Axis(1);
        this.vertical = axis;
        Axis axis2 = new Axis(0);
        this.horizontal = axis2;
        this.mMainAxis = axis2;
        this.mSecondAxis = axis;
    }

    public static final class Axis extends ItemAlignmentFacet.ItemAlignmentDef {
        public int mOrientation;

        public Axis(int i) {
            this.mOrientation = i;
        }

        public int getAlignmentPosition(View view) {
            return ItemAlignmentFacetHelper.getAlignmentPosition(view, this, this.mOrientation);
        }
    }

    public final void setOrientation(int i) {
        this.mOrientation = i;
        if (i == 0) {
            this.mMainAxis = this.horizontal;
            this.mSecondAxis = this.vertical;
            return;
        }
        this.mMainAxis = this.vertical;
        this.mSecondAxis = this.horizontal;
    }
}
