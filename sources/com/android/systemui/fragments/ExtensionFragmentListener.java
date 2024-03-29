package com.android.systemui.fragments;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.FragmentBase;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.function.Consumer;

public class ExtensionFragmentListener<T extends FragmentBase> implements Consumer<T> {
    public final ExtensionController.Extension<T> mExtension;
    public final FragmentHostManager mFragmentHostManager;
    public final int mId;
    public String mOldClass;
    public final String mTag;

    public ExtensionFragmentListener(View view, String str, int i, ExtensionController.Extension<T> extension) {
        this.mTag = str;
        FragmentHostManager fragmentHostManager = FragmentHostManager.get(view);
        this.mFragmentHostManager = fragmentHostManager;
        this.mExtension = extension;
        this.mId = i;
        fragmentHostManager.getFragmentManager().beginTransaction().replace(i, (Fragment) extension.get(), str).commit();
        extension.clearItem(false);
    }

    public void accept(T t) {
        try {
            Fragment.class.cast(t);
            this.mFragmentHostManager.getExtensionManager().setCurrentExtension(this.mId, this.mTag, this.mOldClass, t.getClass().getName(), this.mExtension.getContext());
            this.mOldClass = t.getClass().getName();
        } catch (ClassCastException e) {
            Log.e("ExtensionFragmentListener", t.getClass().getName() + " must be a Fragment", e);
        }
        this.mExtension.clearItem(true);
    }

    public static <T> void attachExtensonToFragment(View view, String str, int i, ExtensionController.Extension<T> extension) {
        extension.addCallback(new ExtensionFragmentListener(view, str, i, extension));
    }
}
