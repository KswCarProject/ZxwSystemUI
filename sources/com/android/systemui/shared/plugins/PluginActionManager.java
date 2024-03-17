package com.android.systemui.shared.plugins;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.Plugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.VersionInfo;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class PluginActionManager<T extends Plugin> {
    public final String mAction;
    public final boolean mAllowMultiple;
    public final Executor mBgExecutor;
    public final Context mContext;
    public final boolean mIsDebuggable;
    public final PluginListener<T> mListener;
    public final Executor mMainExecutor;
    public final NotificationManager mNotificationManager;
    public final Class<T> mPluginClass;
    public final PluginEnabler mPluginEnabler;
    public final PluginInstance.Factory mPluginInstanceFactory;
    @VisibleForTesting
    private final ArrayList<PluginInstance<T>> mPluginInstances;
    public final PackageManager mPm;
    public final ArraySet<String> mPrivilegedPlugins;

    public PluginActionManager(Context context, PackageManager packageManager, String str, PluginListener<T> pluginListener, Class<T> cls, boolean z, Executor executor, Executor executor2, boolean z2, NotificationManager notificationManager, PluginEnabler pluginEnabler, List<String> list, PluginInstance.Factory factory) {
        ArraySet<String> arraySet = new ArraySet<>();
        this.mPrivilegedPlugins = arraySet;
        this.mPluginInstances = new ArrayList<>();
        this.mPluginClass = cls;
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mContext = context;
        this.mPm = packageManager;
        this.mAction = str;
        this.mListener = pluginListener;
        this.mAllowMultiple = z;
        this.mNotificationManager = notificationManager;
        this.mPluginEnabler = pluginEnabler;
        this.mPluginInstanceFactory = factory;
        arraySet.addAll(list);
        this.mIsDebuggable = z2;
    }

    public void loadAll() {
        this.mBgExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda3(this));
    }

    public void destroy() {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        while (it.hasNext()) {
            this.mMainExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda0(this, (PluginInstance) it.next()));
        }
    }

    public void onPackageRemoved(String str) {
        this.mBgExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda2(this, str));
    }

    public void reloadPackage(String str) {
        this.mBgExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda1(this, str));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$reloadPackage$2(String str) {
        lambda$onPackageRemoved$1(str);
        queryPkg(str);
    }

    public boolean checkAndDisable(String str) {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        boolean z = false;
        while (it.hasNext()) {
            PluginInstance pluginInstance = (PluginInstance) it.next();
            if (str.startsWith(pluginInstance.getPackage())) {
                z |= disable(pluginInstance, 3);
            }
        }
        return z;
    }

    public boolean disableAll() {
        ArrayList arrayList = new ArrayList(this.mPluginInstances);
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            z |= disable((PluginInstance) arrayList.get(i), 4);
        }
        return z;
    }

    public boolean isPluginPrivileged(ComponentName componentName) {
        Iterator<String> it = this.mPrivilegedPlugins.iterator();
        while (it.hasNext()) {
            String next = it.next();
            ComponentName unflattenFromString = ComponentName.unflattenFromString(next);
            if (unflattenFromString == null) {
                if (next.equals(componentName.getPackageName())) {
                    return true;
                }
            } else if (unflattenFromString.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    public final boolean disable(PluginInstance<T> pluginInstance, int i) {
        ComponentName componentName = pluginInstance.getComponentName();
        if (isPluginPrivileged(componentName)) {
            return false;
        }
        Log.w("PluginInstanceManager", "Disabling plugin " + componentName.flattenToShortString());
        this.mPluginEnabler.setDisabled(componentName, i);
        return true;
    }

    public <C> boolean dependsOn(Plugin plugin, Class<C> cls) {
        Iterator it = new ArrayList(this.mPluginInstances).iterator();
        while (it.hasNext()) {
            PluginInstance pluginInstance = (PluginInstance) it.next();
            if (pluginInstance.containsPluginClass(plugin.getClass())) {
                if (pluginInstance.getVersionInfo() == null || !pluginInstance.getVersionInfo().hasClass(cls)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return String.format("%s@%s (action=%s)", new Object[]{getClass().getSimpleName(), Integer.valueOf(hashCode()), this.mAction});
    }

    /* renamed from: onPluginConnected */
    public final void lambda$handleQueryPlugins$5(PluginInstance<T> pluginInstance) {
        PluginPrefs.setHasPlugins(this.mContext);
        pluginInstance.onCreate(this.mContext, this.mListener);
    }

    /* renamed from: onPluginDisconnected */
    public final void lambda$removePkg$4(PluginInstance<T> pluginInstance) {
        pluginInstance.onDestroy(this.mListener);
    }

    public final void queryAll() {
        for (int size = this.mPluginInstances.size() - 1; size >= 0; size--) {
            this.mMainExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda4(this, this.mPluginInstances.get(size)));
        }
        this.mPluginInstances.clear();
        handleQueryPlugins((String) null);
    }

    /* renamed from: removePkg */
    public final void lambda$onPackageRemoved$1(String str) {
        for (int size = this.mPluginInstances.size() - 1; size >= 0; size--) {
            PluginInstance pluginInstance = this.mPluginInstances.get(size);
            if (pluginInstance.getPackage().equals(str)) {
                this.mMainExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda5(this, pluginInstance));
                this.mPluginInstances.remove(size);
            }
        }
    }

    public final void queryPkg(String str) {
        if (this.mAllowMultiple || this.mPluginInstances.size() == 0) {
            handleQueryPlugins(str);
        }
    }

    public final void handleQueryPlugins(String str) {
        Intent intent = new Intent(this.mAction);
        if (str != null) {
            intent.setPackage(str);
        }
        List<ResolveInfo> queryIntentServices = this.mPm.queryIntentServices(intent, 0);
        if (queryIntentServices.size() <= 1 || this.mAllowMultiple) {
            for (ResolveInfo resolveInfo : queryIntentServices) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                PluginInstance loadPluginComponent = loadPluginComponent(new ComponentName(serviceInfo.packageName, serviceInfo.name));
                if (loadPluginComponent != null) {
                    this.mPluginInstances.add(loadPluginComponent);
                    this.mMainExecutor.execute(new PluginActionManager$$ExternalSyntheticLambda6(this, loadPluginComponent));
                }
            }
            return;
        }
        Log.w("PluginInstanceManager", "Multiple plugins found for " + this.mAction);
    }

    public final PluginInstance<T> loadPluginComponent(ComponentName componentName) {
        if (!this.mIsDebuggable && !isPluginPrivileged(componentName)) {
            Log.w("PluginInstanceManager", "Plugin cannot be loaded on production build: " + componentName);
            return null;
        } else if (!this.mPluginEnabler.isEnabled(componentName)) {
            return null;
        } else {
            String packageName = componentName.getPackageName();
            try {
                if (this.mPm.checkPermission("com.android.systemui.permission.PLUGIN", packageName) != 0) {
                    Log.d("PluginInstanceManager", "Plugin doesn't have permission: " + packageName);
                    return null;
                }
                return this.mPluginInstanceFactory.create(this.mContext, this.mPm.getApplicationInfo(packageName, 0), componentName, this.mPluginClass);
            } catch (VersionInfo.InvalidVersionException e) {
                reportInvalidVersion(componentName, componentName.getClassName(), e);
                return null;
            } catch (Throwable th) {
                Log.w("PluginInstanceManager", "Couldn't load plugin: " + packageName, th);
                return null;
            }
        }
    }

    public final void reportInvalidVersion(ComponentName componentName, String str, VersionInfo.InvalidVersionException invalidVersionException) {
        Notification.Builder color = new Notification.Builder(this.mContext, "ALR").setStyle(new Notification.BigTextStyle()).setSmallIcon(Resources.getSystem().getIdentifier("stat_sys_warning", "drawable", ThemeOverlayApplier.ANDROID_PACKAGE)).setWhen(0).setShowWhen(false).setVisibility(1).setColor(this.mContext.getColor(Resources.getSystem().getIdentifier("system_notification_accent_color", "color", ThemeOverlayApplier.ANDROID_PACKAGE)));
        try {
            str = this.mPm.getServiceInfo(componentName, 0).loadLabel(this.mPm).toString();
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (!invalidVersionException.isTooNew()) {
            Notification.Builder contentTitle = color.setContentTitle("Plugin \"" + str + "\" is too old");
            StringBuilder sb = new StringBuilder();
            sb.append("Contact plugin developer to get an updated version.\n");
            sb.append(invalidVersionException.getMessage());
            contentTitle.setContentText(sb.toString());
        } else {
            Notification.Builder contentTitle2 = color.setContentTitle("Plugin \"" + str + "\" is too new");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Check to see if an OTA is available.\n");
            sb2.append(invalidVersionException.getMessage());
            contentTitle2.setContentText(sb2.toString());
        }
        Intent intent = new Intent("com.android.systemui.action.DISABLE_PLUGIN");
        color.addAction(new Notification.Action.Builder((Icon) null, "Disable plugin", PendingIntent.getBroadcast(this.mContext, 0, intent.setData(Uri.parse("package://" + componentName.flattenToString())), 67108864)).build());
        this.mNotificationManager.notify(6, color.build());
        Log.w("PluginInstanceManager", "Plugin has invalid interface version " + invalidVersionException.getActualVersion() + ", expected " + invalidVersionException.getExpectedVersion());
    }

    public static class Factory {
        public final Executor mBgExecutor;
        public final Context mContext;
        public final Executor mMainExecutor;
        public final NotificationManager mNotificationManager;
        public final PackageManager mPackageManager;
        public final PluginEnabler mPluginEnabler;
        public final PluginInstance.Factory mPluginInstanceFactory;
        public final List<String> mPrivilegedPlugins;

        public Factory(Context context, PackageManager packageManager, Executor executor, Executor executor2, NotificationManager notificationManager, PluginEnabler pluginEnabler, List<String> list, PluginInstance.Factory factory) {
            this.mContext = context;
            this.mPackageManager = packageManager;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mNotificationManager = notificationManager;
            this.mPluginEnabler = pluginEnabler;
            this.mPrivilegedPlugins = list;
            this.mPluginInstanceFactory = factory;
        }

        public <T extends Plugin> PluginActionManager<T> create(String str, PluginListener<T> pluginListener, Class<T> cls, boolean z, boolean z2) {
            return new PluginActionManager(this.mContext, this.mPackageManager, str, pluginListener, cls, z, this.mMainExecutor, this.mBgExecutor, z2, this.mNotificationManager, this.mPluginEnabler, this.mPrivilegedPlugins, this.mPluginInstanceFactory);
        }
    }

    public static class PluginContextWrapper extends ContextWrapper {
        public final ClassLoader mClassLoader;
        public LayoutInflater mInflater;

        public PluginContextWrapper(Context context, ClassLoader classLoader) {
            super(context);
            this.mClassLoader = classLoader;
        }

        public ClassLoader getClassLoader() {
            return this.mClassLoader;
        }

        public Object getSystemService(String str) {
            if (!"layout_inflater".equals(str)) {
                return getBaseContext().getSystemService(str);
            }
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return this.mInflater;
        }
    }
}
