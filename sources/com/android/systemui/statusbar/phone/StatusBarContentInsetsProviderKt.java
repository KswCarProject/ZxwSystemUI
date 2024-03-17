package com.android.systemui.statusbar.phone;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Pair;
import android.view.DisplayCutout;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarContentInsetsProvider.kt */
public final class StatusBarContentInsetsProviderKt {
    public static final boolean isHorizontal(int i) {
        return i == 1 || i == 3;
    }

    public static final Rect getRotationZeroDisplayBounds(Rect rect, int i) {
        return (i == 0 || i == 2) ? rect : new Rect(0, 0, rect.bottom, rect.right);
    }

    @NotNull
    public static final Rect getPrivacyChipBoundingRectForInsets(@NotNull Rect rect, int i, int i2, boolean z) {
        if (z) {
            int i3 = rect.left;
            return new Rect(i3 - i, rect.top, i3 + i2, rect.bottom);
        }
        int i4 = rect.right;
        return new Rect(i4 - i2, rect.top, i4 + i, rect.bottom);
    }

    @NotNull
    public static final Rect calculateInsetsForRotationWithRotatedResources(int i, int i2, @Nullable DisplayCutout displayCutout, @NotNull Rect rect, int i3, int i4, int i5, boolean z, int i6) {
        Rect rect2 = rect;
        Rect rotationZeroDisplayBounds = getRotationZeroDisplayBounds(rect, i);
        int i7 = rotationZeroDisplayBounds.right;
        int i8 = rotationZeroDisplayBounds.bottom;
        return getStatusBarLeftRight(displayCutout, i3, i7, i8, rect.width(), rect.height(), i4, i5, z, i6, i2, i);
    }

    public static final Rect getStatusBarLeftRight(DisplayCutout displayCutout, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, int i8, int i9, int i10) {
        List<Rect> list;
        if (isHorizontal(i9)) {
            i2 = i3;
        }
        if (displayCutout == null) {
            list = null;
        } else {
            list = displayCutout.getBoundingRects();
        }
        if (list == null || list.isEmpty()) {
            return new Rect(i6, 0, i2 - i7, i);
        }
        int i11 = i10 - i9;
        if (i11 < 0) {
            i11 += 4;
        }
        Rect sbRect = sbRect(i11, i, new Pair(Integer.valueOf(i4), Integer.valueOf(i5)));
        for (Rect next : list) {
            if (shareShortEdge(sbRect, next, i4, i5)) {
                if (touchesLeftEdge(next, i11, i4, i5)) {
                    int logicalWidth = logicalWidth(next, i11);
                    if (z) {
                        logicalWidth += i8;
                    }
                    i6 = Math.max(logicalWidth, i6);
                } else if (touchesRightEdge(next, i11, i4, i5)) {
                    int logicalWidth2 = logicalWidth(next, i11);
                    if (!z) {
                        logicalWidth2 += i8;
                    }
                    i7 = Math.max(i7, logicalWidth2);
                }
            }
        }
        return new Rect(i6, 0, i2 - i7, i);
    }

    public static final Rect sbRect(int i, int i2, Pair<Integer, Integer> pair) {
        Integer num = (Integer) pair.first;
        Integer num2 = (Integer) pair.second;
        if (i == 0) {
            return new Rect(0, 0, num.intValue(), i2);
        }
        if (i == 1) {
            return new Rect(0, 0, i2, num2.intValue());
        }
        if (i != 2) {
            return new Rect(num.intValue() - i2, 0, num.intValue(), num2.intValue());
        }
        return new Rect(0, num2.intValue() - i2, num.intValue(), num2.intValue());
    }

    public static final boolean shareShortEdge(Rect rect, Rect rect2, int i, int i2) {
        if (i < i2) {
            return rect.intersects(0, rect2.top, i, rect2.bottom);
        }
        if (i > i2) {
            return rect.intersects(rect2.left, 0, rect2.right, i2);
        }
        return false;
    }

    public static final boolean touchesRightEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.bottom < i3) {
                        return false;
                    }
                } else if (rect.left > 0) {
                    return false;
                }
            } else if (rect.top > 0) {
                return false;
            }
        } else if (rect.right < i2) {
            return false;
        }
        return true;
    }

    public static final boolean touchesLeftEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.top > 0) {
                        return false;
                    }
                } else if (rect.right < i2) {
                    return false;
                }
            } else if (rect.bottom < i3) {
                return false;
            }
        } else if (rect.left > 0) {
            return false;
        }
        return true;
    }

    public static final int logicalWidth(Rect rect, int i) {
        if (i == 0 || i == 2) {
            return rect.width();
        }
        return rect.height();
    }

    public static final void orientToRotZero(Point point, int i) {
        if (i != 0 && i != 2) {
            int i2 = point.y;
            point.y = point.x;
            point.x = i2;
        }
    }

    public static final int logicalWidth(Point point, int i) {
        if (i == 0 || i == 2) {
            return point.x;
        }
        return point.y;
    }
}
