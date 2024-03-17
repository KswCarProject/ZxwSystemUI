package com.google.android.setupdesign.span;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LinkSpan extends ClickableSpan {
    public final String id;

    @Deprecated
    public interface OnClickListener {
        void onClick(LinkSpan linkSpan);
    }

    public interface OnLinkClickListener {
        boolean onLinkClick(LinkSpan linkSpan);
    }

    public LinkSpan(String str) {
        this.id = str;
    }

    public void onClick(View view) {
        if (dispatchClick(view)) {
            view.cancelPendingInputEvents();
        } else {
            Log.w("LinkSpan", "Dropping click event. No listener attached.");
        }
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text instanceof Spannable) {
                Selection.setSelection((Spannable) text, 0);
            }
        }
    }

    public final boolean dispatchClick(View view) {
        OnClickListener legacyListenerFromContext;
        boolean onLinkClick = view instanceof OnLinkClickListener ? ((OnLinkClickListener) view).onLinkClick(this) : false;
        if (onLinkClick || (legacyListenerFromContext = getLegacyListenerFromContext(view.getContext())) == null) {
            return onLinkClick;
        }
        legacyListenerFromContext.onClick(this);
        return true;
    }

    @Deprecated
    public final OnClickListener getLegacyListenerFromContext(Context context) {
        while (!(context instanceof OnClickListener)) {
            if (!(context instanceof ContextWrapper)) {
                return null;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (OnClickListener) context;
    }

    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
    }
}
