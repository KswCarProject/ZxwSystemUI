package androidx.emoji2.viewsintegration;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class EmojiTextWatcher implements TextWatcher {
    public final EditText mEditText;
    public int mEmojiReplaceStrategy = 0;
    public boolean mEnabled;
    public final boolean mExpectInitializedEmojiCompat;
    public EmojiCompat.InitCallback mInitCallback;
    public int mMaxEmojiCount = Integer.MAX_VALUE;

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public EmojiTextWatcher(EditText editText, boolean z) {
        this.mEditText = editText;
        this.mExpectInitializedEmojiCompat = z;
        this.mEnabled = true;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (!this.mEditText.isInEditMode() && !shouldSkipForDisabledOrNotConfigured() && i2 <= i3 && (charSequence instanceof Spannable)) {
            int loadState = EmojiCompat.get().getLoadState();
            if (loadState != 0) {
                if (loadState == 1) {
                    EmojiCompat.get().process((Spannable) charSequence, i, i + i3, this.mMaxEmojiCount, this.mEmojiReplaceStrategy);
                    return;
                } else if (loadState != 3) {
                    return;
                }
            }
            EmojiCompat.get().registerInitCallback(getInitCallback());
        }
    }

    public final boolean shouldSkipForDisabledOrNotConfigured() {
        return !this.mEnabled || (!this.mExpectInitializedEmojiCompat && !EmojiCompat.isConfigured());
    }

    public final EmojiCompat.InitCallback getInitCallback() {
        if (this.mInitCallback == null) {
            this.mInitCallback = new InitCallbackImpl(this.mEditText);
        }
        return this.mInitCallback;
    }

    public void setEnabled(boolean z) {
        if (this.mEnabled != z) {
            if (this.mInitCallback != null) {
                EmojiCompat.get().unregisterInitCallback(this.mInitCallback);
            }
            this.mEnabled = z;
            if (z) {
                processTextOnEnablingEvent(this.mEditText, EmojiCompat.get().getLoadState());
            }
        }
    }

    public static class InitCallbackImpl extends EmojiCompat.InitCallback {
        public final Reference<EditText> mViewRef;

        public InitCallbackImpl(EditText editText) {
            this.mViewRef = new WeakReference(editText);
        }

        public void onInitialized() {
            super.onInitialized();
            EmojiTextWatcher.processTextOnEnablingEvent(this.mViewRef.get(), 1);
        }
    }

    public static void processTextOnEnablingEvent(EditText editText, int i) {
        if (i == 1 && editText != null && editText.isAttachedToWindow()) {
            Editable editableText = editText.getEditableText();
            int selectionStart = Selection.getSelectionStart(editableText);
            int selectionEnd = Selection.getSelectionEnd(editableText);
            EmojiCompat.get().process(editableText);
            EmojiInputFilter.updateSelection(editableText, selectionStart, selectionEnd);
        }
    }
}
