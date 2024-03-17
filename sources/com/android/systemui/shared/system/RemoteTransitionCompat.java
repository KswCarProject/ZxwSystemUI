package com.android.systemui.shared.system;

import android.annotation.NonNull;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.SurfaceControl;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.PictureInPictureSurfaceTransaction;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import java.util.ArrayList;

public class RemoteTransitionCompat implements Parcelable {
    public static final Parcelable.Creator<RemoteTransitionCompat> CREATOR = new Parcelable.Creator<RemoteTransitionCompat>() {
        public RemoteTransitionCompat[] newArray(int i) {
            return new RemoteTransitionCompat[i];
        }

        public RemoteTransitionCompat createFromParcel(Parcel parcel) {
            return new RemoteTransitionCompat(parcel);
        }
    };
    public TransitionFilter mFilter = null;
    public final RemoteTransition mTransition;

    @VisibleForTesting
    public static class RecentsControllerWrap extends RecentsAnimationControllerCompat {
        public IRemoteTransitionFinishedCallback mFinishCB = null;
        public TransitionInfo mInfo = null;
        public ArrayMap<SurfaceControl, SurfaceControl> mLeashMap = null;
        public ArrayList<SurfaceControl> mOpeningLeashes = null;
        public ArrayList<WindowContainerToken> mPausingTasks = null;
        public WindowContainerToken mPipTask = null;
        public PictureInPictureSurfaceTransaction mPipTransaction = null;
        public WindowContainerToken mRecentsTask = null;
        public IBinder mTransition = null;
        public RecentsAnimationControllerCompat mWrapped = null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mFilter != null ? (byte) 2 : 0);
        parcel.writeTypedObject(this.mTransition, i);
        TransitionFilter transitionFilter = this.mFilter;
        if (transitionFilter != null) {
            parcel.writeTypedObject(transitionFilter, i);
        }
    }

    public RemoteTransitionCompat(Parcel parcel) {
        TransitionFilter transitionFilter;
        byte readByte = parcel.readByte();
        RemoteTransition remoteTransition = (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR);
        if ((readByte & 2) == 0) {
            transitionFilter = null;
        } else {
            transitionFilter = (TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR);
        }
        this.mTransition = remoteTransition;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, remoteTransition);
        this.mFilter = transitionFilter;
    }
}
