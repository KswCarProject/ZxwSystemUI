package androidx.leanback.widget;

public final class ItemAlignmentFacet {
    public ItemAlignmentDef[] mAlignmentDefs = {new ItemAlignmentDef()};

    public static class ItemAlignmentDef {
        public boolean mAlignToBaseline;
        public int mFocusViewId = -1;
        public int mOffset = 0;
        public float mOffsetPercent = 50.0f;
        public boolean mOffsetWithPadding = false;
        public int mViewId = -1;

        public final int getItemAlignmentFocusViewId() {
            int i = this.mFocusViewId;
            return i != -1 ? i : this.mViewId;
        }

        public boolean isAlignedToTextViewBaseLine() {
            return this.mAlignToBaseline;
        }
    }

    public ItemAlignmentDef[] getAlignmentDefs() {
        return this.mAlignmentDefs;
    }
}
