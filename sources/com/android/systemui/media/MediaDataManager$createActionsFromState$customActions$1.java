package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$createActionsFromState$customActions$1 extends Lambda implements Function1<PlaybackState.CustomAction, MediaAction> {
    public final /* synthetic */ MediaController $controller;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ PlaybackState $state;
    public final /* synthetic */ MediaDataManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MediaDataManager$createActionsFromState$customActions$1(MediaDataManager mediaDataManager, PlaybackState playbackState, String str, MediaController mediaController) {
        super(1);
        this.this$0 = mediaDataManager;
        this.$state = playbackState;
        this.$packageName = str;
        this.$controller = mediaController;
    }

    @NotNull
    public final MediaAction invoke(@NotNull PlaybackState.CustomAction customAction) {
        return this.this$0.getCustomAction(this.$state, this.$packageName, this.$controller, customAction);
    }
}
