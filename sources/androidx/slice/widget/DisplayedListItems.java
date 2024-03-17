package androidx.slice.widget;

import java.util.List;

public class DisplayedListItems {
    public final List<SliceContent> mDisplayedItems;
    public final int mHiddenItemCount;

    public DisplayedListItems(List<SliceContent> list, int i) {
        this.mDisplayedItems = list;
        this.mHiddenItemCount = i;
    }

    public List<SliceContent> getDisplayedItems() {
        return this.mDisplayedItems;
    }

    public int getHiddenItemCount() {
        return this.mHiddenItemCount;
    }
}
