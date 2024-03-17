package com.android.systemui.dreams.complication;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.touch.TouchInsetManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ComplicationLayoutEngine_Factory implements Factory<ComplicationLayoutEngine> {
    public final Provider<Integer> fadeInDurationProvider;
    public final Provider<Integer> fadeOutDurationProvider;
    public final Provider<ConstraintLayout> layoutProvider;
    public final Provider<Integer> marginProvider;
    public final Provider<TouchInsetManager.TouchInsetSession> sessionProvider;

    public ComplicationLayoutEngine_Factory(Provider<ConstraintLayout> provider, Provider<Integer> provider2, Provider<TouchInsetManager.TouchInsetSession> provider3, Provider<Integer> provider4, Provider<Integer> provider5) {
        this.layoutProvider = provider;
        this.marginProvider = provider2;
        this.sessionProvider = provider3;
        this.fadeInDurationProvider = provider4;
        this.fadeOutDurationProvider = provider5;
    }

    public ComplicationLayoutEngine get() {
        return newInstance(this.layoutProvider.get(), this.marginProvider.get().intValue(), this.sessionProvider.get(), this.fadeInDurationProvider.get().intValue(), this.fadeOutDurationProvider.get().intValue());
    }

    public static ComplicationLayoutEngine_Factory create(Provider<ConstraintLayout> provider, Provider<Integer> provider2, Provider<TouchInsetManager.TouchInsetSession> provider3, Provider<Integer> provider4, Provider<Integer> provider5) {
        return new ComplicationLayoutEngine_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ComplicationLayoutEngine newInstance(ConstraintLayout constraintLayout, int i, TouchInsetManager.TouchInsetSession touchInsetSession, int i2, int i3) {
        return new ComplicationLayoutEngine(constraintLayout, i, touchInsetSession, i2, i3);
    }
}
