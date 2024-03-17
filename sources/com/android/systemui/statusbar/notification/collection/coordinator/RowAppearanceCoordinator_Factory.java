package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.Context;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RowAppearanceCoordinator_Factory implements Factory<RowAppearanceCoordinator> {
    public final Provider<Context> contextProvider;
    public final Provider<AssistantFeedbackController> mAssistantFeedbackControllerProvider;
    public final Provider<SectionClassifier> mSectionClassifierProvider;

    public RowAppearanceCoordinator_Factory(Provider<Context> provider, Provider<AssistantFeedbackController> provider2, Provider<SectionClassifier> provider3) {
        this.contextProvider = provider;
        this.mAssistantFeedbackControllerProvider = provider2;
        this.mSectionClassifierProvider = provider3;
    }

    public RowAppearanceCoordinator get() {
        return newInstance(this.contextProvider.get(), this.mAssistantFeedbackControllerProvider.get(), this.mSectionClassifierProvider.get());
    }

    public static RowAppearanceCoordinator_Factory create(Provider<Context> provider, Provider<AssistantFeedbackController> provider2, Provider<SectionClassifier> provider3) {
        return new RowAppearanceCoordinator_Factory(provider, provider2, provider3);
    }

    public static RowAppearanceCoordinator newInstance(Context context, AssistantFeedbackController assistantFeedbackController, SectionClassifier sectionClassifier) {
        return new RowAppearanceCoordinator(context, assistantFeedbackController, sectionClassifier);
    }
}
