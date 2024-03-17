package androidx.emoji2.viewsintegration;

import android.text.method.KeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.core.util.Preconditions;

public final class EmojiEditTextHelper {
    public int mEmojiReplaceStrategy = 0;
    public final HelperInternal mHelper;
    public int mMaxEmojiCount = Integer.MAX_VALUE;

    public static class HelperInternal {
        public KeyListener getKeyListener(KeyListener keyListener) {
            throw null;
        }

        public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo) {
            throw null;
        }

        public void setEnabled(boolean z) {
            throw null;
        }
    }

    public EmojiEditTextHelper(EditText editText, boolean z) {
        Preconditions.checkNotNull(editText, "editText cannot be null");
        this.mHelper = new HelperInternal19(editText, z);
    }

    public KeyListener getKeyListener(KeyListener keyListener) {
        return this.mHelper.getKeyListener(keyListener);
    }

    public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo) {
        if (inputConnection == null) {
            return null;
        }
        return this.mHelper.onCreateInputConnection(inputConnection, editorInfo);
    }

    public void setEnabled(boolean z) {
        this.mHelper.setEnabled(z);
    }

    public static class HelperInternal19 extends HelperInternal {
        public final EditText mEditText;
        public final EmojiTextWatcher mTextWatcher;

        public HelperInternal19(EditText editText, boolean z) {
            this.mEditText = editText;
            EmojiTextWatcher emojiTextWatcher = new EmojiTextWatcher(editText, z);
            this.mTextWatcher = emojiTextWatcher;
            editText.addTextChangedListener(emojiTextWatcher);
            editText.setEditableFactory(EmojiEditableFactory.getInstance());
        }

        public KeyListener getKeyListener(KeyListener keyListener) {
            if (keyListener instanceof EmojiKeyListener) {
                return keyListener;
            }
            if (keyListener == null) {
                return null;
            }
            return new EmojiKeyListener(keyListener);
        }

        public InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo) {
            if (inputConnection instanceof EmojiInputConnection) {
                return inputConnection;
            }
            return new EmojiInputConnection(this.mEditText, inputConnection, editorInfo);
        }

        public void setEnabled(boolean z) {
            this.mTextWatcher.setEnabled(z);
        }
    }
}
