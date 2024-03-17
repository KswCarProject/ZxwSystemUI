package com.google.android.setupdesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.SystemNavBarMixin;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.template.NavigationBarMixin;
import com.google.android.setupdesign.template.ProgressBarMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.template.ScrollViewScrollHandlingDelegate;
import com.google.android.setupdesign.view.Illustration;

public class SetupWizardLayout extends TemplateLayout {
    public SetupWizardLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, R$attr.sudLayoutTheme);
    }

    public final void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            registerMixin(SystemNavBarMixin.class, new SystemNavBarMixin(this, (Window) null));
            registerMixin(HeaderMixin.class, new HeaderMixin(this, attributeSet, i));
            registerMixin(DescriptionMixin.class, new DescriptionMixin(this, attributeSet, i));
            registerMixin(ProgressBarMixin.class, new ProgressBarMixin(this));
            registerMixin(NavigationBarMixin.class, new NavigationBarMixin(this));
            RequireScrollMixin requireScrollMixin = new RequireScrollMixin(this);
            registerMixin(RequireScrollMixin.class, requireScrollMixin);
            ScrollView scrollView = getScrollView();
            if (scrollView != null) {
                requireScrollMixin.setScrollHandlingDelegate(new ScrollViewScrollHandlingDelegate(requireScrollMixin, scrollView));
            }
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SudSetupWizardLayout, i, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.SudSetupWizardLayout_sudBackground);
            if (drawable != null) {
                setLayoutBackground(drawable);
            } else {
                Drawable drawable2 = obtainStyledAttributes.getDrawable(R$styleable.SudSetupWizardLayout_sudBackgroundTile);
                if (drawable2 != null) {
                    setBackgroundTile(drawable2);
                }
            }
            Drawable drawable3 = obtainStyledAttributes.getDrawable(R$styleable.SudSetupWizardLayout_sudIllustration);
            if (drawable3 != null) {
                setIllustration(drawable3);
            } else {
                Drawable drawable4 = obtainStyledAttributes.getDrawable(R$styleable.SudSetupWizardLayout_sudIllustrationImage);
                Drawable drawable5 = obtainStyledAttributes.getDrawable(R$styleable.SudSetupWizardLayout_sudIllustrationHorizontalTile);
                if (!(drawable4 == null || drawable5 == null)) {
                    setIllustration(drawable4, drawable5);
                }
            }
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.SudSetupWizardLayout_sudDecorPaddingTop, -1);
            if (dimensionPixelSize == -1) {
                dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.sud_decor_padding_top);
            }
            setDecorPaddingTop(dimensionPixelSize);
            float f = obtainStyledAttributes.getFloat(R$styleable.SudSetupWizardLayout_sudIllustrationAspectRatio, -1.0f);
            if (f == -1.0f) {
                TypedValue typedValue = new TypedValue();
                getResources().getValue(R$dimen.sud_illustration_aspect_ratio, typedValue, true);
                f = typedValue.getFloat();
            }
            setIllustrationAspectRatio(f);
            obtainStyledAttributes.recycle();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isProgressBarShown = isProgressBarShown();
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            Log.w("SetupWizardLayout", "Ignoring restore instance state " + parcelable);
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setProgressBarShown(savedState.isProgressBarShown);
    }

    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = R$layout.sud_template;
        }
        return inflateTemplate(layoutInflater, R$style.SudThemeMaterial_Light, i);
    }

    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = R$id.sud_layout_content;
        }
        return super.findContainer(i);
    }

    public ScrollView getScrollView() {
        View findManagedViewById = findManagedViewById(R$id.sud_bottom_scroll_view);
        if (findManagedViewById instanceof ScrollView) {
            return (ScrollView) findManagedViewById;
        }
        return null;
    }

    public void setIllustration(Drawable drawable) {
        View findManagedViewById = findManagedViewById(R$id.sud_layout_decor);
        if (findManagedViewById instanceof Illustration) {
            ((Illustration) findManagedViewById).setIllustration(drawable);
        }
    }

    public final void setIllustration(Drawable drawable, Drawable drawable2) {
        View findManagedViewById = findManagedViewById(R$id.sud_layout_decor);
        if (findManagedViewById instanceof Illustration) {
            ((Illustration) findManagedViewById).setIllustration(getIllustration(drawable, drawable2));
        }
    }

    public void setIllustrationAspectRatio(float f) {
        View findManagedViewById = findManagedViewById(R$id.sud_layout_decor);
        if (findManagedViewById instanceof Illustration) {
            ((Illustration) findManagedViewById).setAspectRatio(f);
        }
    }

    public void setDecorPaddingTop(int i) {
        View findManagedViewById = findManagedViewById(R$id.sud_layout_decor);
        if (findManagedViewById != null) {
            findManagedViewById.setPadding(findManagedViewById.getPaddingLeft(), i, findManagedViewById.getPaddingRight(), findManagedViewById.getPaddingBottom());
        }
    }

    public void setLayoutBackground(Drawable drawable) {
        View findManagedViewById = findManagedViewById(R$id.sud_layout_decor);
        if (findManagedViewById != null) {
            findManagedViewById.setBackgroundDrawable(drawable);
        }
    }

    public final void setBackgroundTile(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Shader.TileMode tileMode = Shader.TileMode.REPEAT;
            ((BitmapDrawable) drawable).setTileModeXY(tileMode, tileMode);
        }
        setLayoutBackground(drawable);
    }

    @SuppressLint({"RtlHardcoded"})
    public final Drawable getIllustration(Drawable drawable, Drawable drawable2) {
        if (getContext().getResources().getBoolean(R$bool.sudUseTabletLayout)) {
            if (drawable2 instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable2;
                bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
                bitmapDrawable.setGravity(48);
            }
            if (drawable instanceof BitmapDrawable) {
                ((BitmapDrawable) drawable).setGravity(51);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable2, drawable});
            layerDrawable.setAutoMirrored(true);
            return layerDrawable;
        }
        drawable.setAutoMirrored(true);
        return drawable;
    }

    public boolean isProgressBarShown() {
        return ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).isShown();
    }

    public void setProgressBarShown(boolean z) {
        ((ProgressBarMixin) getMixin(ProgressBarMixin.class)).setShown(z);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean isProgressBarShown = false;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            boolean z = false;
            this.isProgressBarShown = parcel.readInt() != 0 ? true : z;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isProgressBarShown ? 1 : 0);
        }
    }
}
