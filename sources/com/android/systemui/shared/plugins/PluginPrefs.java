package com.android.systemui.shared.plugins;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
import java.util.Set;

public class PluginPrefs {
    public final Set<String> mPluginActions;
    public final SharedPreferences mSharedPrefs;

    public PluginPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("plugin_prefs", 0);
        this.mSharedPrefs = sharedPreferences;
        this.mPluginActions = new ArraySet(sharedPreferences.getStringSet("actions", (Set) null));
    }

    public Set<String> getPluginList() {
        return new ArraySet(this.mPluginActions);
    }

    public synchronized void addAction(String str) {
        if (this.mPluginActions.add(str)) {
            this.mSharedPrefs.edit().putStringSet("actions", this.mPluginActions).apply();
        }
    }

    public static boolean hasPlugins(Context context) {
        return context.getSharedPreferences("plugin_prefs", 0).getBoolean("plugins", false);
    }

    public static void setHasPlugins(Context context) {
        context.getSharedPreferences("plugin_prefs", 0).edit().putBoolean("plugins", true).apply();
    }
}
