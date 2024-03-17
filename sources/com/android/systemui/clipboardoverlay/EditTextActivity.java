package com.android.systemui.clipboardoverlay;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import java.util.Objects;

public class EditTextActivity extends Activity implements ClipboardManager.OnPrimaryClipChangedListener {
    public TextView mAttribution;
    public ClipboardManager mClipboardManager;
    public EditText mEditText;
    public boolean mSensitive;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.clipboard_edit_text_activity);
        findViewById(R$id.done_button).setOnClickListener(new EditTextActivity$$ExternalSyntheticLambda0(this));
        this.mEditText = (EditText) findViewById(R$id.edit_text);
        this.mAttribution = (TextView) findViewById(R$id.attribution);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(ClipboardManager.class);
        Objects.requireNonNull(clipboardManager);
        ClipboardManager clipboardManager2 = clipboardManager;
        this.mClipboardManager = clipboardManager;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        saveToClipboard();
    }

    public void onStart() {
        super.onStart();
        ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
        if (primaryClip == null) {
            finish();
            return;
        }
        PackageManager packageManager = getApplicationContext().getPackageManager();
        boolean z = true;
        try {
            this.mAttribution.setText(getResources().getString(R$string.clipboard_edit_source, new Object[]{packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mClipboardManager.getPrimaryClipSource(), PackageManager.ApplicationInfoFlags.of(0)))}));
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("EditTextActivity", "Package not found: " + this.mClipboardManager.getPrimaryClipSource(), e);
        }
        this.mEditText.setText(primaryClip.getItemAt(0).getText());
        this.mEditText.requestFocus();
        if (primaryClip.getDescription().getExtras() == null || !primaryClip.getDescription().getExtras().getBoolean("android.content.extra.IS_SENSITIVE")) {
            z = false;
        }
        this.mSensitive = z;
        this.mClipboardManager.addPrimaryClipChangedListener(this);
    }

    public void onPause() {
        this.mClipboardManager.removePrimaryClipChangedListener(this);
        super.onPause();
    }

    public void onPrimaryClipChanged() {
        hideIme();
        finish();
    }

    public final void saveToClipboard() {
        hideIme();
        Editable text = this.mEditText.getText();
        text.clearSpans();
        ClipData newPlainText = ClipData.newPlainText("text", text);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putBoolean("android.content.extra.IS_SENSITIVE", this.mSensitive);
        newPlainText.getDescription().setExtras(persistableBundle);
        this.mClipboardManager.setPrimaryClip(newPlainText);
        finish();
    }

    public final void hideIme() {
        ((InputMethodManager) getSystemService(InputMethodManager.class)).hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
    }
}
