package com.android.systemui.dreams.complication.dagger;

import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;

public interface ComplicationViewModelComponent {

    public interface Factory {
        ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId);
    }

    ComplicationViewModelProvider getViewModelProvider();
}
