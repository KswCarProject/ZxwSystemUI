package com.android.systemui.shared.system.smartspace;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SmartspaceState.kt */
public final class SmartspaceState implements Parcelable {
    @NotNull
    public static final CREATOR CREATOR = new CREATOR((DefaultConstructorMarker) null);
    @NotNull
    public Rect boundsOnScreen;
    public int selectedPage;
    public boolean visibleOnScreen;

    public int describeContents() {
        return 0;
    }

    public SmartspaceState() {
        this.boundsOnScreen = new Rect();
    }

    public final boolean getVisibleOnScreen() {
        return this.visibleOnScreen;
    }

    public SmartspaceState(@NotNull Parcel parcel) {
        this();
        this.boundsOnScreen = (Rect) parcel.readParcelable(AnonymousClass1.INSTANCE.getClass().getClassLoader());
        this.selectedPage = parcel.readInt();
        this.visibleOnScreen = parcel.readBoolean();
    }

    public void writeToParcel(@Nullable Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeParcelable(this.boundsOnScreen, 0);
        }
        if (parcel != null) {
            parcel.writeInt(this.selectedPage);
        }
        if (parcel != null) {
            parcel.writeBoolean(this.visibleOnScreen);
        }
    }

    @NotNull
    public String toString() {
        return "boundsOnScreen: " + this.boundsOnScreen + ", selectedPage: " + this.selectedPage + ", visibleOnScreen: " + this.visibleOnScreen;
    }

    /* compiled from: SmartspaceState.kt */
    public static final class CREATOR implements Parcelable.Creator<SmartspaceState> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public CREATOR() {
        }

        @NotNull
        public SmartspaceState createFromParcel(@NotNull Parcel parcel) {
            return new SmartspaceState(parcel);
        }

        @NotNull
        public SmartspaceState[] newArray(int i) {
            return new SmartspaceState[i];
        }
    }
}
