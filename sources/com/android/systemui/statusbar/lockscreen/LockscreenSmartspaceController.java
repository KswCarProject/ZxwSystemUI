package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final ActivityStarter activityStarter;
    @NotNull
    public final LockscreenSmartspaceController$configChangeListener$1 configChangeListener;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final ContentResolver contentResolver;
    @NotNull
    public final Context context;
    @NotNull
    public final DeviceProvisionedController deviceProvisionedController;
    @NotNull
    public final LockscreenSmartspaceController$deviceProvisionedListener$1 deviceProvisionedListener;
    @NotNull
    public final Execution execution;
    @NotNull
    public final FalsingManager falsingManager;
    @NotNull
    public final FeatureFlags featureFlags;
    @NotNull
    public final Handler handler;
    @Nullable
    public UserHandle managedUserHandle;
    @Nullable
    public final BcSmartspaceDataPlugin plugin;
    @NotNull
    public final SecureSettings secureSettings;
    @Nullable
    public SmartspaceSession session;
    @NotNull
    public final SmartspaceSession.OnTargetsAvailableListener sessionListener = new LockscreenSmartspaceController$sessionListener$1(this);
    @NotNull
    public final LockscreenSmartspaceController$settingsObserver$1 settingsObserver;
    public boolean showNotifications;
    public boolean showSensitiveContentForCurrentUser;
    public boolean showSensitiveContentForManagedUser;
    @NotNull
    public final SmartspaceManager smartspaceManager;
    @NotNull
    public Set<BcSmartspaceDataPlugin.SmartspaceView> smartspaceViews = new LinkedHashSet();
    @NotNull
    public View.OnAttachStateChangeListener stateChangeListener = new LockscreenSmartspaceController$stateChangeListener$1(this);
    @NotNull
    public final StatusBarStateController statusBarStateController;
    @NotNull
    public final LockscreenSmartspaceController$statusBarStateListener$1 statusBarStateListener;
    @NotNull
    public final Executor uiExecutor;
    @NotNull
    public final UserTracker userTracker;
    @NotNull
    public final LockscreenSmartspaceController$userTrackerCallback$1 userTrackerCallback = new LockscreenSmartspaceController$userTrackerCallback$1(this);

    public LockscreenSmartspaceController(@NotNull Context context2, @NotNull FeatureFlags featureFlags2, @NotNull SmartspaceManager smartspaceManager2, @NotNull ActivityStarter activityStarter2, @NotNull FalsingManager falsingManager2, @NotNull SecureSettings secureSettings2, @NotNull UserTracker userTracker2, @NotNull ContentResolver contentResolver2, @NotNull ConfigurationController configurationController2, @NotNull StatusBarStateController statusBarStateController2, @NotNull DeviceProvisionedController deviceProvisionedController2, @NotNull Execution execution2, @NotNull Executor executor, @NotNull Handler handler2, @NotNull Optional<BcSmartspaceDataPlugin> optional) {
        this.context = context2;
        this.featureFlags = featureFlags2;
        this.smartspaceManager = smartspaceManager2;
        this.activityStarter = activityStarter2;
        this.falsingManager = falsingManager2;
        this.secureSettings = secureSettings2;
        this.userTracker = userTracker2;
        this.contentResolver = contentResolver2;
        this.configurationController = configurationController2;
        this.statusBarStateController = statusBarStateController2;
        this.deviceProvisionedController = deviceProvisionedController2;
        this.execution = execution2;
        this.uiExecutor = executor;
        this.handler = handler2;
        this.plugin = optional.orElse((Object) null);
        this.settingsObserver = new LockscreenSmartspaceController$settingsObserver$1(this, handler2);
        this.configChangeListener = new LockscreenSmartspaceController$configChangeListener$1(this);
        this.statusBarStateListener = new LockscreenSmartspaceController$statusBarStateListener$1(this);
        LockscreenSmartspaceController$deviceProvisionedListener$1 lockscreenSmartspaceController$deviceProvisionedListener$1 = new LockscreenSmartspaceController$deviceProvisionedListener$1(this);
        this.deviceProvisionedListener = lockscreenSmartspaceController$deviceProvisionedListener$1;
        deviceProvisionedController2.addCallback(lockscreenSmartspaceController$deviceProvisionedListener$1);
    }

    /* compiled from: LockscreenSmartspaceController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public final View.OnAttachStateChangeListener getStateChangeListener() {
        return this.stateChangeListener;
    }

    public final boolean isEnabled() {
        this.execution.assertIsMainThread();
        return this.featureFlags.isEnabled(Flags.SMARTSPACE) && this.plugin != null;
    }

    @Nullable
    public final View buildAndConnectView(@NotNull ViewGroup viewGroup) {
        this.execution.assertIsMainThread();
        if (isEnabled()) {
            View buildView = buildView(viewGroup);
            connectSession();
            return buildView;
        }
        throw new RuntimeException("Cannot build view when not enabled");
    }

    public final void requestSmartspaceUpdate() {
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    public final View buildView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin == null) {
            return null;
        }
        BcSmartspaceDataPlugin.SmartspaceView view = bcSmartspaceDataPlugin.getView(viewGroup);
        view.registerDataProvider(this.plugin);
        view.setIntentStarter(new LockscreenSmartspaceController$buildView$1(this));
        view.setFalsingManager(this.falsingManager);
        View view2 = (View) view;
        view2.addOnAttachStateChangeListener(getStateChangeListener());
        return view2;
    }

    public final void connectSession() {
        if (this.plugin != null && this.session == null && !this.smartspaceViews.isEmpty() && this.deviceProvisionedController.isDeviceProvisioned() && this.deviceProvisionedController.isCurrentUserSetup()) {
            SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "lockscreen").build());
            Log.d("LockscreenSmartspaceController", "Starting smartspace session for lockscreen");
            createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
            this.session = createSmartspaceSession;
            this.deviceProvisionedController.removeCallback(this.deviceProvisionedListener);
            this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_show_notifications"), true, this.settingsObserver, -1);
            this.configurationController.addCallback(this.configChangeListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
            this.plugin.registerSmartspaceEventNotifier(new LockscreenSmartspaceController$connectSession$1(this));
            reloadSmartspace();
        }
    }

    public final void disconnect() {
        if (this.smartspaceViews.isEmpty()) {
            this.execution.assertIsMainThread();
            SmartspaceSession smartspaceSession = this.session;
            if (smartspaceSession != null) {
                if (smartspaceSession != null) {
                    smartspaceSession.removeOnTargetsAvailableListener(this.sessionListener);
                    smartspaceSession.close();
                }
                this.userTracker.removeCallback(this.userTrackerCallback);
                this.contentResolver.unregisterContentObserver(this.settingsObserver);
                this.configurationController.removeCallback(this.configChangeListener);
                this.statusBarStateController.removeCallback(this.statusBarStateListener);
                this.session = null;
                BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
                if (bcSmartspaceDataPlugin != null) {
                    bcSmartspaceDataPlugin.registerSmartspaceEventNotifier((BcSmartspaceDataPlugin.SmartspaceEventNotifier) null);
                }
                BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.plugin;
                if (bcSmartspaceDataPlugin2 != null) {
                    bcSmartspaceDataPlugin2.onTargetsAvailable(CollectionsKt__CollectionsKt.emptyList());
                }
                Log.d("LockscreenSmartspaceController", "Ending smartspace session for lockscreen");
            }
        }
    }

    public final void addListener(@NotNull BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.registerListener(smartspaceTargetListener);
        }
    }

    public final boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget) {
        if (this.showNotifications) {
            UserHandle userHandle = smartspaceTarget.getUserHandle();
            if (Intrinsics.areEqual((Object) userHandle, (Object) this.userTracker.getUserHandle())) {
                if (smartspaceTarget.isSensitive() && !this.showSensitiveContentForCurrentUser) {
                    return false;
                }
            } else if (!Intrinsics.areEqual((Object) userHandle, (Object) this.managedUserHandle) || this.userTracker.getUserHandle().getIdentifier() != 0) {
                return false;
            } else {
                if (smartspaceTarget.isSensitive() && !this.showSensitiveContentForManagedUser) {
                    return false;
                }
            }
            return true;
        } else if (smartspaceTarget.getFeatureType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public final void updateTextColorFromWallpaper() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.context, R$attr.wallpaperTextColor);
        for (BcSmartspaceDataPlugin.SmartspaceView primaryTextColor : this.smartspaceViews) {
            primaryTextColor.setPrimaryTextColor(colorAttrDefaultColor);
        }
    }

    public final void reloadSmartspace() {
        Integer num;
        boolean z = false;
        this.showNotifications = this.secureSettings.getIntForUser("lock_screen_show_notifications", 0, this.userTracker.getUserId()) == 1;
        this.showSensitiveContentForCurrentUser = this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1;
        UserHandle workProfileUser = getWorkProfileUser();
        this.managedUserHandle = workProfileUser;
        if (workProfileUser == null) {
            num = null;
        } else {
            num = Integer.valueOf(workProfileUser.getIdentifier());
        }
        if (num != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, num.intValue()) == 1) {
                z = true;
            }
            this.showSensitiveContentForManagedUser = z;
        }
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    public final UserHandle getWorkProfileUser() {
        for (UserInfo next : this.userTracker.getUserProfiles()) {
            if (next.isManagedProfile()) {
                return next.getUserHandle();
            }
        }
        return null;
    }
}
