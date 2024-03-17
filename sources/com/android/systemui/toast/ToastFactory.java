package com.android.systemui.toast;

import android.content.Context;
import android.view.LayoutInflater;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.ToastPlugin;
import com.android.systemui.shared.plugins.PluginManager;
import java.io.PrintWriter;

public class ToastFactory implements Dumpable {
    public final LayoutInflater mLayoutInflater;
    public ToastPlugin mPlugin;

    public ToastFactory(LayoutInflater layoutInflater, PluginManager pluginManager, DumpManager dumpManager) {
        this.mLayoutInflater = layoutInflater;
        dumpManager.registerDumpable("ToastFactory", this);
        pluginManager.addPluginListener(new PluginListener<ToastPlugin>() {
            public void onPluginConnected(ToastPlugin toastPlugin, Context context) {
                ToastFactory.this.mPlugin = toastPlugin;
            }

            public void onPluginDisconnected(ToastPlugin toastPlugin) {
                if (toastPlugin.equals(ToastFactory.this.mPlugin)) {
                    ToastFactory.this.mPlugin = null;
                }
            }
        }, ToastPlugin.class, false);
    }

    public SystemUIToast createToast(Context context, CharSequence charSequence, String str, int i, int i2) {
        if (isPluginAvailable()) {
            CharSequence charSequence2 = charSequence;
            String str2 = str;
            int i3 = i;
            return new SystemUIToast(this.mLayoutInflater, context, charSequence, this.mPlugin.createToast(charSequence, str, i), str, i, i2);
        }
        return new SystemUIToast(this.mLayoutInflater, context, charSequence, str, i, i2);
    }

    public final boolean isPluginAvailable() {
        return this.mPlugin != null;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ToastFactory:");
        printWriter.println("    mAttachedPlugin=" + this.mPlugin);
    }
}
