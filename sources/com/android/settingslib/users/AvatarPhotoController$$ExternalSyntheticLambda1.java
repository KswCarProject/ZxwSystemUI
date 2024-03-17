package com.android.settingslib.users;

import android.net.Uri;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AvatarPhotoController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AvatarPhotoController f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ AvatarPhotoController$$ExternalSyntheticLambda1(AvatarPhotoController avatarPhotoController, Uri uri) {
        this.f$0 = avatarPhotoController;
        this.f$1 = uri;
    }

    public final void run() {
        this.f$0.lambda$onPhotoNotCropped$3(this.f$1);
    }
}
