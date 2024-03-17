package com.android.systemui.dreams.complication;

import android.view.View;

public interface Complication {

    public interface Host {
    }

    public interface ViewHolder {
        int getCategory() {
            return 1;
        }

        ComplicationLayoutParams getLayoutParams();

        View getView();
    }

    public interface VisibilityController {
        void setVisibility(int i, boolean z);
    }

    ViewHolder createView(ComplicationViewModel complicationViewModel);

    int getRequiredTypeAvailability() {
        return 0;
    }
}
