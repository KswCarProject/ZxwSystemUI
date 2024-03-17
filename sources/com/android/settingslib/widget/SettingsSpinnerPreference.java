package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class SettingsSpinnerPreference extends Preference implements Preference.OnPreferenceClickListener {
    public AdapterView.OnItemSelectedListener mListener;
    public final AdapterView.OnItemSelectedListener mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (SettingsSpinnerPreference.this.mPosition != i) {
                SettingsSpinnerPreference.this.mPosition = i;
                if (SettingsSpinnerPreference.this.mListener != null) {
                    SettingsSpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            if (SettingsSpinnerPreference.this.mListener != null) {
                SettingsSpinnerPreference.this.mListener.onNothingSelected(adapterView);
            }
        }
    };
    public int mPosition;
    public boolean mShouldPerformClick;

    public SettingsSpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.settings_spinner_preference);
        setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference) {
        this.mShouldPerformClick = true;
        notifyChanged();
        return true;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Spinner spinner = (Spinner) preferenceViewHolder.findViewById(R$id.spinner);
        spinner.setAdapter((SpinnerAdapter) null);
        spinner.setSelection(this.mPosition);
        spinner.setOnItemSelectedListener(this.mOnSelectedListener);
        if (this.mShouldPerformClick) {
            this.mShouldPerformClick = false;
            spinner.performClick();
        }
    }
}
