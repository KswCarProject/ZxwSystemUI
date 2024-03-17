package com.android.settingslib.users;

import android.net.Uri;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AvatarPhotoController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AvatarPhotoController f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ AvatarPhotoController$$ExternalSyntheticLambda0(AvatarPhotoController avatarPhotoController, Uri uri, boolean z) {
        this.f$0 = avatarPhotoController;
        this.f$1 = uri;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$copyAndCropPhoto$1(this.f$1, this.f$2);
    }
}
