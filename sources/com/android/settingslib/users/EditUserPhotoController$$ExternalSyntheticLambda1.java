package com.android.settingslib.users;

import android.net.Uri;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EditUserPhotoController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ EditUserPhotoController f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ EditUserPhotoController$$ExternalSyntheticLambda1(EditUserPhotoController editUserPhotoController, Uri uri) {
        this.f$0 = editUserPhotoController;
        this.f$1 = uri;
    }

    public final void run() {
        this.f$0.lambda$onPhotoCropped$4(this.f$1);
    }
}
