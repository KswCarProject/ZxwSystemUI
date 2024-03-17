package com.google.android.material.badge;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.google.android.material.R$dimen;
import com.google.android.material.R$plurals;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import java.util.Locale;

public final class BadgeState {
    public final float badgeRadius;
    public final float badgeWidePadding;
    public final float badgeWithTextRadius;
    public final State currentState;
    public final State overridingState;

    public BadgeState(Context context, int i, int i2, int i3, State state) {
        CharSequence charSequence;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        State state2 = new State();
        this.currentState = state2;
        state = state == null ? new State() : state;
        if (i != 0) {
            int unused = state.badgeResId = i;
        }
        TypedArray generateTypedArray = generateTypedArray(context, state.badgeResId, i2, i3);
        Resources resources = context.getResources();
        this.badgeRadius = (float) generateTypedArray.getDimensionPixelSize(R$styleable.Badge_badgeRadius, resources.getDimensionPixelSize(R$dimen.mtrl_badge_radius));
        this.badgeWidePadding = (float) generateTypedArray.getDimensionPixelSize(R$styleable.Badge_badgeWidePadding, resources.getDimensionPixelSize(R$dimen.mtrl_badge_long_text_horizontal_padding));
        this.badgeWithTextRadius = (float) generateTypedArray.getDimensionPixelSize(R$styleable.Badge_badgeWithTextRadius, resources.getDimensionPixelSize(R$dimen.mtrl_badge_with_text_radius));
        int unused2 = state2.alpha = state.alpha == -2 ? 255 : state.alpha;
        if (state.contentDescriptionNumberless == null) {
            charSequence = context.getString(R$string.mtrl_badge_numberless_content_description);
        } else {
            charSequence = state.contentDescriptionNumberless;
        }
        CharSequence unused3 = state2.contentDescriptionNumberless = charSequence;
        if (state.contentDescriptionQuantityStrings == 0) {
            i4 = R$plurals.mtrl_badge_content_description;
        } else {
            i4 = state.contentDescriptionQuantityStrings;
        }
        int unused4 = state2.contentDescriptionQuantityStrings = i4;
        if (state.contentDescriptionExceedsMaxBadgeNumberRes == 0) {
            i5 = R$string.mtrl_exceed_max_badge_number_content_description;
        } else {
            i5 = state.contentDescriptionExceedsMaxBadgeNumberRes;
        }
        int unused5 = state2.contentDescriptionExceedsMaxBadgeNumberRes = i5;
        int i13 = 0;
        Boolean unused6 = state2.isVisible = Boolean.valueOf(state.isVisible == null || state.isVisible.booleanValue());
        if (state.maxCharacterCount == -2) {
            i6 = generateTypedArray.getInt(R$styleable.Badge_maxCharacterCount, 4);
        } else {
            i6 = state.maxCharacterCount;
        }
        int unused7 = state2.maxCharacterCount = i6;
        if (state.number != -2) {
            int unused8 = state2.number = state.number;
        } else {
            int i14 = R$styleable.Badge_number;
            if (generateTypedArray.hasValue(i14)) {
                int unused9 = state2.number = generateTypedArray.getInt(i14, 0);
            } else {
                int unused10 = state2.number = -1;
            }
        }
        if (state.backgroundColor == null) {
            i7 = readColorFromAttributes(context, generateTypedArray, R$styleable.Badge_backgroundColor);
        } else {
            i7 = state.backgroundColor.intValue();
        }
        Integer unused11 = state2.backgroundColor = Integer.valueOf(i7);
        if (state.badgeTextColor != null) {
            Integer unused12 = state2.badgeTextColor = state.badgeTextColor;
        } else {
            int i15 = R$styleable.Badge_badgeTextColor;
            if (generateTypedArray.hasValue(i15)) {
                Integer unused13 = state2.badgeTextColor = Integer.valueOf(readColorFromAttributes(context, generateTypedArray, i15));
            } else {
                Integer unused14 = state2.badgeTextColor = Integer.valueOf(new TextAppearance(context, R$style.TextAppearance_MaterialComponents_Badge).getTextColor().getDefaultColor());
            }
        }
        if (state.badgeGravity == null) {
            i8 = generateTypedArray.getInt(R$styleable.Badge_badgeGravity, 8388661);
        } else {
            i8 = state.badgeGravity.intValue();
        }
        Integer unused15 = state2.badgeGravity = Integer.valueOf(i8);
        if (state.horizontalOffsetWithoutText == null) {
            i9 = generateTypedArray.getDimensionPixelOffset(R$styleable.Badge_horizontalOffset, 0);
        } else {
            i9 = state.horizontalOffsetWithoutText.intValue();
        }
        Integer unused16 = state2.horizontalOffsetWithoutText = Integer.valueOf(i9);
        if (state.horizontalOffsetWithoutText == null) {
            i10 = generateTypedArray.getDimensionPixelOffset(R$styleable.Badge_verticalOffset, 0);
        } else {
            i10 = state.verticalOffsetWithoutText.intValue();
        }
        Integer unused17 = state2.verticalOffsetWithoutText = Integer.valueOf(i10);
        if (state.horizontalOffsetWithText == null) {
            i11 = generateTypedArray.getDimensionPixelOffset(R$styleable.Badge_horizontalOffsetWithText, state2.horizontalOffsetWithoutText.intValue());
        } else {
            i11 = state.horizontalOffsetWithText.intValue();
        }
        Integer unused18 = state2.horizontalOffsetWithText = Integer.valueOf(i11);
        if (state.verticalOffsetWithText == null) {
            i12 = generateTypedArray.getDimensionPixelOffset(R$styleable.Badge_verticalOffsetWithText, state2.verticalOffsetWithoutText.intValue());
        } else {
            i12 = state.verticalOffsetWithText.intValue();
        }
        Integer unused19 = state2.verticalOffsetWithText = Integer.valueOf(i12);
        Integer unused20 = state2.additionalHorizontalOffset = Integer.valueOf(state.additionalHorizontalOffset == null ? 0 : state.additionalHorizontalOffset.intValue());
        Integer unused21 = state2.additionalVerticalOffset = Integer.valueOf(state.additionalVerticalOffset != null ? state.additionalVerticalOffset.intValue() : i13);
        generateTypedArray.recycle();
        if (state.numberLocale == null) {
            Locale unused22 = state2.numberLocale = Locale.getDefault(Locale.Category.FORMAT);
        } else {
            Locale unused23 = state2.numberLocale = state.numberLocale;
        }
        this.overridingState = state;
    }

    public final TypedArray generateTypedArray(Context context, int i, int i2, int i3) {
        AttributeSet attributeSet;
        int i4;
        if (i != 0) {
            attributeSet = DrawableUtils.parseDrawableXml(context, i, "badge");
            i4 = attributeSet.getStyleAttribute();
        } else {
            attributeSet = null;
            i4 = 0;
        }
        return ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.Badge, i2, i4 == 0 ? i3 : i4, new int[0]);
    }

    public State getOverridingState() {
        return this.overridingState;
    }

    public boolean isVisible() {
        return this.currentState.isVisible.booleanValue();
    }

    public boolean hasNumber() {
        return this.currentState.number != -1;
    }

    public int getNumber() {
        return this.currentState.number;
    }

    public int getAlpha() {
        return this.currentState.alpha;
    }

    public void setAlpha(int i) {
        int unused = this.overridingState.alpha = i;
        int unused2 = this.currentState.alpha = i;
    }

    public int getMaxCharacterCount() {
        return this.currentState.maxCharacterCount;
    }

    public int getBackgroundColor() {
        return this.currentState.backgroundColor.intValue();
    }

    public int getBadgeTextColor() {
        return this.currentState.badgeTextColor.intValue();
    }

    public int getBadgeGravity() {
        return this.currentState.badgeGravity.intValue();
    }

    public int getHorizontalOffsetWithoutText() {
        return this.currentState.horizontalOffsetWithoutText.intValue();
    }

    public int getVerticalOffsetWithoutText() {
        return this.currentState.verticalOffsetWithoutText.intValue();
    }

    public int getHorizontalOffsetWithText() {
        return this.currentState.horizontalOffsetWithText.intValue();
    }

    public int getVerticalOffsetWithText() {
        return this.currentState.verticalOffsetWithText.intValue();
    }

    public int getAdditionalHorizontalOffset() {
        return this.currentState.additionalHorizontalOffset.intValue();
    }

    public void setAdditionalHorizontalOffset(int i) {
        Integer unused = this.overridingState.additionalHorizontalOffset = Integer.valueOf(i);
        Integer unused2 = this.currentState.additionalHorizontalOffset = Integer.valueOf(i);
    }

    public int getAdditionalVerticalOffset() {
        return this.currentState.additionalVerticalOffset.intValue();
    }

    public void setAdditionalVerticalOffset(int i) {
        Integer unused = this.overridingState.additionalVerticalOffset = Integer.valueOf(i);
        Integer unused2 = this.currentState.additionalVerticalOffset = Integer.valueOf(i);
    }

    public CharSequence getContentDescriptionNumberless() {
        return this.currentState.contentDescriptionNumberless;
    }

    public int getContentDescriptionQuantityStrings() {
        return this.currentState.contentDescriptionQuantityStrings;
    }

    public int getContentDescriptionExceedsMaxBadgeNumberStringResource() {
        return this.currentState.contentDescriptionExceedsMaxBadgeNumberRes;
    }

    public Locale getNumberLocale() {
        return this.currentState.numberLocale;
    }

    public static int readColorFromAttributes(Context context, TypedArray typedArray, int i) {
        return MaterialResources.getColorStateList(context, typedArray, i).getDefaultColor();
    }

    public static final class State implements Parcelable {
        public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() {
            public State createFromParcel(Parcel parcel) {
                return new State(parcel);
            }

            public State[] newArray(int i) {
                return new State[i];
            }
        };
        public Integer additionalHorizontalOffset;
        public Integer additionalVerticalOffset;
        public int alpha = 255;
        public Integer backgroundColor;
        public Integer badgeGravity;
        public int badgeResId;
        public Integer badgeTextColor;
        public int contentDescriptionExceedsMaxBadgeNumberRes;
        public CharSequence contentDescriptionNumberless;
        public int contentDescriptionQuantityStrings;
        public Integer horizontalOffsetWithText;
        public Integer horizontalOffsetWithoutText;
        public Boolean isVisible = Boolean.TRUE;
        public int maxCharacterCount = -2;
        public int number = -2;
        public Locale numberLocale;
        public Integer verticalOffsetWithText;
        public Integer verticalOffsetWithoutText;

        public int describeContents() {
            return 0;
        }

        public State() {
        }

        public State(Parcel parcel) {
            this.badgeResId = parcel.readInt();
            this.backgroundColor = (Integer) parcel.readSerializable();
            this.badgeTextColor = (Integer) parcel.readSerializable();
            this.alpha = parcel.readInt();
            this.number = parcel.readInt();
            this.maxCharacterCount = parcel.readInt();
            this.contentDescriptionNumberless = parcel.readString();
            this.contentDescriptionQuantityStrings = parcel.readInt();
            this.badgeGravity = (Integer) parcel.readSerializable();
            this.horizontalOffsetWithoutText = (Integer) parcel.readSerializable();
            this.verticalOffsetWithoutText = (Integer) parcel.readSerializable();
            this.horizontalOffsetWithText = (Integer) parcel.readSerializable();
            this.verticalOffsetWithText = (Integer) parcel.readSerializable();
            this.additionalHorizontalOffset = (Integer) parcel.readSerializable();
            this.additionalVerticalOffset = (Integer) parcel.readSerializable();
            this.isVisible = (Boolean) parcel.readSerializable();
            this.numberLocale = (Locale) parcel.readSerializable();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.badgeResId);
            parcel.writeSerializable(this.backgroundColor);
            parcel.writeSerializable(this.badgeTextColor);
            parcel.writeInt(this.alpha);
            parcel.writeInt(this.number);
            parcel.writeInt(this.maxCharacterCount);
            CharSequence charSequence = this.contentDescriptionNumberless;
            parcel.writeString(charSequence == null ? null : charSequence.toString());
            parcel.writeInt(this.contentDescriptionQuantityStrings);
            parcel.writeSerializable(this.badgeGravity);
            parcel.writeSerializable(this.horizontalOffsetWithoutText);
            parcel.writeSerializable(this.verticalOffsetWithoutText);
            parcel.writeSerializable(this.horizontalOffsetWithText);
            parcel.writeSerializable(this.verticalOffsetWithText);
            parcel.writeSerializable(this.additionalHorizontalOffset);
            parcel.writeSerializable(this.additionalVerticalOffset);
            parcel.writeSerializable(this.isVisible);
            parcel.writeSerializable(this.numberLocale);
        }
    }
}
