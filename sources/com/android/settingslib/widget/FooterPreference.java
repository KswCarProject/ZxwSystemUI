package com.android.settingslib.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class FooterPreference extends Preference {
    public CharSequence mContentDescription;
    public int mIconVisibility = 0;
    public View.OnClickListener mLearnMoreListener;
    public FooterLearnMoreSpan mLearnMoreSpan;
    public CharSequence mLearnMoreText;

    public FooterPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, R$attr.footerPreferenceStyle);
        init();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(16908310);
        if (!TextUtils.isEmpty(this.mContentDescription)) {
            textView.setContentDescription(this.mContentDescription);
        }
        TextView textView2 = (TextView) preferenceViewHolder.itemView.findViewById(R$id.settingslib_learn_more);
        if (textView2 == null || this.mLearnMoreListener == null) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            if (TextUtils.isEmpty(this.mLearnMoreText)) {
                this.mLearnMoreText = textView2.getText();
            } else {
                textView2.setText(this.mLearnMoreText);
            }
            SpannableString spannableString = new SpannableString(this.mLearnMoreText);
            FooterLearnMoreSpan footerLearnMoreSpan = this.mLearnMoreSpan;
            if (footerLearnMoreSpan != null) {
                spannableString.removeSpan(footerLearnMoreSpan);
            }
            FooterLearnMoreSpan footerLearnMoreSpan2 = new FooterLearnMoreSpan(this.mLearnMoreListener);
            this.mLearnMoreSpan = footerLearnMoreSpan2;
            spannableString.setSpan(footerLearnMoreSpan2, 0, spannableString.length(), 0);
            textView2.setText(spannableString);
        }
        preferenceViewHolder.itemView.findViewById(R$id.icon_frame).setVisibility(this.mIconVisibility);
    }

    public void setSummary(CharSequence charSequence) {
        setTitle(charSequence);
    }

    public void setSummary(int i) {
        setTitle(i);
    }

    public CharSequence getSummary() {
        return getTitle();
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public final void init() {
        setLayoutResource(R$layout.preference_footer);
        if (getIcon() == null) {
            setIcon(R$drawable.settingslib_ic_info_outline_24);
        }
        setOrder(2147483646);
        if (TextUtils.isEmpty(getKey())) {
            setKey("footer_preference");
        }
        setSelectable(false);
    }

    public static class FooterLearnMoreSpan extends URLSpan {
        public final View.OnClickListener mClickListener;

        public FooterLearnMoreSpan(View.OnClickListener onClickListener) {
            super("");
            this.mClickListener = onClickListener;
        }

        public void onClick(View view) {
            View.OnClickListener onClickListener = this.mClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        }
    }
}
