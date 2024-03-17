package com.android.systemui.qs;

import android.content.Context;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import java.util.Collection;

public interface QSHost {

    public interface Callback {
        void onTilesChanged();
    }

    void collapsePanels();

    Context getContext();

    InstanceId getNewInstanceId();

    UiEventLogger getUiEventLogger();

    Context getUserContext();

    int getUserId();

    int indexOf(String str);

    void openPanels();

    void removeTile(String str);

    void removeTiles(Collection<String> collection);

    void unmarkTileAsAutoAdded(String str);

    void warn(String str, Throwable th);
}
