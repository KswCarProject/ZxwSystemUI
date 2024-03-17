package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toolbar;
import androidx.preference.Preference;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import com.android.systemui.fragments.FragmentHostManager;
import java.util.Objects;

public class RadioListPreference extends CustomListPreference {
    public DialogInterface.OnClickListener mOnClickListener;
    public CharSequence mSummary;

    public RadioListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onPrepareDialogBuilder(AlertDialog.Builder builder, DialogInterface.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        this.mSummary = charSequence;
    }

    public CharSequence getSummary() {
        CharSequence charSequence = this.mSummary;
        if (charSequence == null || charSequence.toString().contains("%s")) {
            return super.getSummary();
        }
        return this.mSummary;
    }

    public Dialog onDialogCreated(DialogFragment dialogFragment, Dialog dialog) {
        Dialog dialog2 = new Dialog(getContext(), 16974371);
        Toolbar toolbar = (Toolbar) dialog2.findViewById(16908731);
        View view = new View(getContext());
        view.setId(R$id.content);
        dialog2.setContentView(view);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(Utils.getDrawable(dialog2.getContext(), 16843531));
        toolbar.setNavigationOnClickListener(new RadioListPreference$$ExternalSyntheticLambda0(dialog2));
        RadioFragment radioFragment = new RadioFragment();
        radioFragment.setPreference(this);
        FragmentHostManager.get(view).getFragmentManager().beginTransaction().add(16908290, radioFragment).commit();
        return dialog2;
    }

    public void onDialogStateRestored(DialogFragment dialogFragment, Dialog dialog, Bundle bundle) {
        super.onDialogStateRestored(dialogFragment, dialog, bundle);
        int i = R$id.content;
        RadioFragment radioFragment = (RadioFragment) FragmentHostManager.get(dialog.findViewById(i)).getFragmentManager().findFragmentById(i);
        if (radioFragment != null) {
            radioFragment.setPreference(this);
        }
    }

    public void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
    }

    public static class RadioFragment extends TunerPreferenceFragment {
        public RadioListPreference mListPref;

        public void onCreatePreferences(Bundle bundle, String str) {
            setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext()));
            if (this.mListPref != null) {
                update();
            }
        }

        public final void update() {
            Context context = getPreferenceManager().getContext();
            CharSequence[] entries = this.mListPref.getEntries();
            CharSequence[] entryValues = this.mListPref.getEntryValues();
            String value = this.mListPref.getValue();
            for (int i = 0; i < entries.length; i++) {
                CharSequence charSequence = entries[i];
                SelectablePreference selectablePreference = new SelectablePreference(context);
                getPreferenceScreen().addPreference(selectablePreference);
                selectablePreference.setTitle(charSequence);
                selectablePreference.setChecked(Objects.equals(value, entryValues[i]));
                selectablePreference.setKey(String.valueOf(i));
            }
        }

        public boolean onPreferenceTreeClick(Preference preference) {
            this.mListPref.mOnClickListener.onClick((DialogInterface) null, Integer.parseInt(preference.getKey()));
            return true;
        }

        public void setPreference(RadioListPreference radioListPreference) {
            this.mListPref = radioListPreference;
            if (getPreferenceManager() != null) {
                update();
            }
        }
    }
}