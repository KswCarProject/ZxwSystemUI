package androidx.leanback.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.leanback.widget.Util;

class GuidedStepRootLayout extends LinearLayout {
    public boolean mFocusOutEnd = false;
    public boolean mFocusOutStart = false;

    public GuidedStepRootLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GuidedStepRootLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public View focusSearch(View view, int i) {
        View focusSearch = super.focusSearch(view, i);
        if ((i != 17 && i != 66) || Util.isDescendant(this, focusSearch)) {
            return focusSearch;
        }
        if (getLayoutDirection() != 0 ? i != 66 : i != 17) {
            if (!this.mFocusOutEnd) {
                return view;
            }
        } else if (!this.mFocusOutStart) {
            return view;
        }
        return focusSearch;
    }
}
