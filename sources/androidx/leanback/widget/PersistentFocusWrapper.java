package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;

class PersistentFocusWrapper extends FrameLayout {
    public boolean mPersistFocusVertical = true;
    public int mSelectedPosition = -1;

    public PersistentFocusWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getGrandChildCount() {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getChildCount();
    }

    public final boolean shouldPersistFocusFromDirection(int i) {
        boolean z = this.mPersistFocusVertical;
        return (z && (i == 33 || i == 130)) || (!z && (i == 17 || i == 66));
    }

    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        if (hasFocus() || getGrandChildCount() == 0 || !shouldPersistFocusFromDirection(i)) {
            super.addFocusables(arrayList, i, i2);
        } else {
            arrayList.add(this);
        }
    }

    public void requestChildFocus(View view, View view2) {
        int i;
        super.requestChildFocus(view, view2);
        while (view2 != null && view2.getParent() != view) {
            view2 = (View) view2.getParent();
        }
        if (view2 == null) {
            i = -1;
        } else {
            i = ((ViewGroup) view).indexOfChild(view2);
        }
        this.mSelectedPosition = i;
    }

    public boolean requestFocus(int i, Rect rect) {
        int i2;
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || (i2 = this.mSelectedPosition) < 0 || i2 >= getGrandChildCount() || !viewGroup.getChildAt(this.mSelectedPosition).requestFocus(i, rect)) {
            return super.requestFocus(i, rect);
        }
        return true;
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public int mSelectedPosition;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mSelectedPosition = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mSelectedPosition);
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = this.mSelectedPosition;
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mSelectedPosition = savedState.mSelectedPosition;
        super.onRestoreInstanceState(savedState.getSuperState());
    }
}
