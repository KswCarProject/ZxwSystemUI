package com.google.android.setupdesign.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import com.google.android.setupdesign.accessibility.LinkAccessibilityHelper;
import com.google.android.setupdesign.span.LinkSpan;
import com.google.android.setupdesign.span.SpanHelper;
import com.google.android.setupdesign.view.TouchableMovementMethod;

public class RichTextView extends AppCompatTextView implements LinkSpan.OnLinkClickListener {
    public static Typeface spanTypeface;
    public LinkAccessibilityHelper accessibilityHelper;
    public LinkSpan.OnLinkClickListener onLinkClickListener;

    @SuppressLint({"NewApi"})
    @TargetApi(28)
    public static CharSequence getRichText(Context context, CharSequence charSequence) {
        TypefaceSpan typefaceSpan;
        if (!(charSequence instanceof Spanned)) {
            return charSequence;
        }
        SpannableString spannableString = new SpannableString(charSequence);
        for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
            String key = annotation.getKey();
            if ("textAppearance".equals(key)) {
                int identifier = context.getResources().getIdentifier(annotation.getValue(), "style", context.getPackageName());
                if (identifier == 0) {
                    Log.w("RichTextView", "Cannot find resource: " + identifier);
                }
                SpanHelper.replaceSpan(spannableString, annotation, new TextAppearanceSpan(context, identifier));
            } else if ("link".equals(key)) {
                LinkSpan linkSpan = new LinkSpan(annotation.getValue());
                if (spanTypeface != null) {
                    typefaceSpan = new TypefaceSpan(spanTypeface);
                } else {
                    typefaceSpan = new TypefaceSpan("sans-serif-medium");
                }
                SpanHelper.replaceSpan(spannableString, annotation, linkSpan, typefaceSpan);
            }
        }
        return spannableString;
    }

    public RichTextView(Context context) {
        super(context);
        init();
    }

    public RichTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        if (!isInEditMode()) {
            LinkAccessibilityHelper linkAccessibilityHelper = new LinkAccessibilityHelper((TextView) this);
            this.accessibilityHelper = linkAccessibilityHelper;
            ViewCompat.setAccessibilityDelegate(this, linkAccessibilityHelper);
        }
    }

    @TargetApi(28)
    public void setSpanTypeface(Typeface typeface) {
        spanTypeface = typeface;
    }

    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        CharSequence richText = getRichText(getContext(), charSequence);
        super.setText(richText, bufferType);
        boolean hasLinks = hasLinks(richText);
        if (hasLinks) {
            setMovementMethod(TouchableMovementMethod.TouchableLinkMovementMethod.getInstance());
        } else {
            setMovementMethod((MovementMethod) null);
        }
        setFocusable(hasLinks);
        setRevealOnFocusHint(false);
        setFocusableInTouchMode(hasLinks);
    }

    public final boolean hasLinks(CharSequence charSequence) {
        if (!(charSequence instanceof Spanned) || ((ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class)).length <= 0) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        MovementMethod movementMethod = getMovementMethod();
        if (movementMethod instanceof TouchableMovementMethod) {
            TouchableMovementMethod touchableMovementMethod = (TouchableMovementMethod) movementMethod;
            if (touchableMovementMethod.getLastTouchEvent() == motionEvent) {
                return touchableMovementMethod.isLastTouchEventHandled();
            }
        }
        return onTouchEvent;
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        LinkAccessibilityHelper linkAccessibilityHelper = this.accessibilityHelper;
        if (linkAccessibilityHelper == null || !linkAccessibilityHelper.dispatchHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        for (Drawable drawable : getCompoundDrawablesRelative()) {
            if (drawable != null && drawable.setState(drawableState)) {
                invalidateDrawable(drawable);
            }
        }
    }

    public boolean onLinkClick(LinkSpan linkSpan) {
        LinkSpan.OnLinkClickListener onLinkClickListener2 = this.onLinkClickListener;
        if (onLinkClickListener2 != null) {
            return onLinkClickListener2.onLinkClick(linkSpan);
        }
        return false;
    }
}
