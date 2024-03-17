package com.android.systemui.shared.rotation;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FloatingRotationButtonPositionCalculator.kt */
public final class FloatingRotationButtonPositionCalculator {
    public final int defaultMargin;
    public final int taskbarMarginBottom;
    public final int taskbarMarginLeft;

    public FloatingRotationButtonPositionCalculator(int i, int i2, int i3) {
        this.defaultMargin = i;
        this.taskbarMarginLeft = i2;
        this.taskbarMarginBottom = i3;
    }

    @NotNull
    public final Position calculatePosition(int i, boolean z, boolean z2) {
        int i2;
        boolean z3 = false;
        if ((i == 0 || i == 1) && z && !z2) {
            z3 = true;
        }
        int resolveGravity = resolveGravity(i);
        int i3 = z3 ? this.taskbarMarginLeft : this.defaultMargin;
        if (z3) {
            i2 = this.taskbarMarginBottom;
        } else {
            i2 = this.defaultMargin;
        }
        if ((resolveGravity & 5) == 5) {
            i3 = -i3;
        }
        if ((resolveGravity & 80) == 80) {
            i2 = -i2;
        }
        return new Position(resolveGravity, i3, i2);
    }

    /* compiled from: FloatingRotationButtonPositionCalculator.kt */
    public static final class Position {
        public final int gravity;
        public final int translationX;
        public final int translationY;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Position)) {
                return false;
            }
            Position position = (Position) obj;
            return this.gravity == position.gravity && this.translationX == position.translationX && this.translationY == position.translationY;
        }

        public int hashCode() {
            return (((Integer.hashCode(this.gravity) * 31) + Integer.hashCode(this.translationX)) * 31) + Integer.hashCode(this.translationY);
        }

        @NotNull
        public String toString() {
            return "Position(gravity=" + this.gravity + ", translationX=" + this.translationX + ", translationY=" + this.translationY + ')';
        }

        public Position(int i, int i2, int i3) {
            this.gravity = i;
            this.translationX = i2;
            this.translationY = i3;
        }

        public final int getGravity() {
            return this.gravity;
        }

        public final int getTranslationX() {
            return this.translationX;
        }

        public final int getTranslationY() {
            return this.translationY;
        }
    }

    public final int resolveGravity(int i) {
        if (i == 0) {
            return 83;
        }
        if (i == 1) {
            return 85;
        }
        if (i == 2) {
            return 53;
        }
        if (i == 3) {
            return 51;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("Invalid rotation ", Integer.valueOf(i)));
    }
}
