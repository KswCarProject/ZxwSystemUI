package androidx.emoji2.viewsintegration;

import android.annotation.SuppressLint;
import android.text.Editable;
import androidx.emoji2.text.SpannableBuilder;

public final class EmojiEditableFactory extends Editable.Factory {
    public static final Object INSTANCE_LOCK = new Object();
    public static volatile Editable.Factory sInstance;
    public static Class<?> sWatcherClass;

    @SuppressLint({"PrivateApi"})
    public EmojiEditableFactory() {
        try {
            sWatcherClass = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, EmojiEditableFactory.class.getClassLoader());
        } catch (Throwable unused) {
        }
    }

    public static Editable.Factory getInstance() {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new EmojiEditableFactory();
                }
            }
        }
        return sInstance;
    }

    public Editable newEditable(CharSequence charSequence) {
        Class<?> cls = sWatcherClass;
        if (cls != null) {
            return SpannableBuilder.create(cls, charSequence);
        }
        return super.newEditable(charSequence);
    }
}
