package com.android.systemui.people.widget;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.backup.BackupManager;
import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.app.people.PeopleManager;
import android.app.people.PeopleSpaceTile;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.service.notification.ConversationChannelWrapper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.people.NotificationHelper;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.PeopleTileViewHelper;
import com.android.systemui.people.SharedPreferencesHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PeopleSpaceWidgetManager {
    @GuardedBy({"mLock"})
    public static Map<PeopleTileKey, TileConversationListener> mListeners = new HashMap();
    @GuardedBy({"mLock"})
    public static Map<Integer, PeopleSpaceTile> mTiles = new HashMap();
    public AppWidgetManager mAppWidgetManager;
    public BackupManager mBackupManager;
    public final BroadcastReceiver mBaseBroadcastReceiver = new BroadcastReceiver() {
        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(Intent intent) {
            PeopleSpaceWidgetManager.this.updateWidgetsFromBroadcastInBackground(intent.getAction());
        }

        public void onReceive(Context context, Intent intent) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0(this, intent));
        }
    };
    public Executor mBgExecutor;
    public BroadcastDispatcher mBroadcastDispatcher;
    public Optional<Bubbles> mBubblesOptional;
    public final Context mContext;
    public INotificationManager mINotificationManager;
    public IPeopleManager mIPeopleManager;
    public LauncherApps mLauncherApps;
    public final NotificationListener.NotificationHandler mListener = new NotificationListener.NotificationHandler() {
        public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        }

        public void onNotificationsInitialized() {
        }

        public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.POSTED);
        }

        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.REMOVED);
        }

        public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            if (notificationChannel.isConversation()) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                peopleSpaceWidgetManager.updateWidgets(peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)));
            }
        }
    };
    public final Object mLock = new Object();
    public PeopleSpaceWidgetManager mManager;
    public CommonNotifCollection mNotifCollection;
    @GuardedBy({"mLock"})
    public Map<String, Set<String>> mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
    public NotificationManager mNotificationManager;
    public PackageManager mPackageManager;
    public PeopleManager mPeopleManager;
    public boolean mRegisteredReceivers;
    public SharedPreferences mSharedPrefs;
    public UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    public UserManager mUserManager;

    public PeopleSpaceWidgetManager(Context context, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        this.mContext = context;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mLauncherApps = launcherApps;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPeopleManager = (PeopleManager) context.getSystemService(PeopleManager.class);
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = new BackupManager(context);
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBgExecutor = executor;
    }

    public void init() {
        synchronized (this.mLock) {
            if (!this.mRegisteredReceivers) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");
                intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
                intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
                intentFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
                intentFilter.addAction("android.intent.action.USER_UNLOCKED");
                this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, (Executor) null, UserHandle.ALL);
                IntentFilter intentFilter2 = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
                intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
                intentFilter2.addDataScheme("package");
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2);
                IntentFilter intentFilter3 = new IntentFilter("android.intent.action.BOOT_COMPLETED");
                intentFilter3.setPriority(1000);
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter3);
                this.mRegisteredReceivers = true;
            }
        }
    }

    public class TileConversationListener implements PeopleManager.ConversationListener {
        public TileConversationListener() {
        }

        public void onConversationUpdate(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$TileConversationListener$$ExternalSyntheticLambda0(this, conversationChannel));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConversationUpdate$0(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.updateWidgetsWithConversationChanged(conversationChannel);
        }
    }

    @VisibleForTesting
    public PeopleSpaceWidgetManager(Context context, AppWidgetManager appWidgetManager, IPeopleManager iPeopleManager, PeopleManager peopleManager, LauncherApps launcherApps, CommonNotifCollection commonNotifCollection, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, BackupManager backupManager, INotificationManager iNotificationManager, NotificationManager notificationManager, Executor executor) {
        this.mContext = context;
        this.mAppWidgetManager = appWidgetManager;
        this.mIPeopleManager = iPeopleManager;
        this.mPeopleManager = peopleManager;
        this.mLauncherApps = launcherApps;
        this.mNotifCollection = commonNotifCollection;
        this.mPackageManager = packageManager;
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = backupManager;
        this.mINotificationManager = iNotificationManager;
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mBgExecutor = executor;
    }

    public void updateWidgets(int[] iArr) {
        this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda1(this, iArr));
    }

    /* renamed from: updateWidgetsInBackground */
    public final void lambda$updateWidgets$0(int[] iArr) {
        try {
            if (iArr.length != 0) {
                synchronized (this.mLock) {
                    updateSingleConversationWidgets(iArr);
                }
            }
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "failed to update widgets", e);
        }
    }

    public void updateSingleConversationWidgets(int[] iArr) {
        HashMap hashMap = new HashMap();
        for (int i : iArr) {
            PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
            if (tileForExistingWidget == null) {
                Log.e("PeopleSpaceWidgetMgr", "Matching conversation not found for shortcut ID");
            }
            lambda$addNewWidget$5(i, tileForExistingWidget);
            hashMap.put(Integer.valueOf(i), tileForExistingWidget);
            if (tileForExistingWidget != null) {
                registerConversationListenerIfNeeded(i, new PeopleTileKey(tileForExistingWidget));
            }
        }
        PeopleSpaceUtils.getDataFromContactsOnBackgroundThread(this.mContext, this.mManager, hashMap, iArr);
    }

    public final void updateAppWidgetViews(int i, PeopleSpaceTile peopleSpaceTile, Bundle bundle) {
        PeopleTileKey keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
        if (!PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot update invalid widget");
            return;
        }
        this.mAppWidgetManager.updateAppWidget(i, PeopleTileViewHelper.createRemoteViews(this.mContext, peopleSpaceTile, i, bundle, keyFromStorageByWidgetId));
    }

    public void updateAppWidgetOptionsAndViewOptional(int i, Optional<PeopleSpaceTile> optional) {
        if (optional.isPresent()) {
            lambda$addNewWidget$5(i, optional.get());
        }
    }

    /* renamed from: updateAppWidgetOptionsAndView */
    public void lambda$addNewWidget$5(int i, PeopleSpaceTile peopleSpaceTile) {
        synchronized (mTiles) {
            mTiles.put(Integer.valueOf(i), peopleSpaceTile);
        }
        updateAppWidgetViews(i, peopleSpaceTile, this.mAppWidgetManager.getAppWidgetOptions(i));
    }

    public PeopleSpaceTile getTileForExistingWidget(int i) {
        try {
            return getTileForExistingWidgetThrowing(i);
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "failed to retrieve tile for widget ID " + i, e);
            return null;
        }
    }

    public final PeopleSpaceTile getTileForExistingWidgetThrowing(int i) throws PackageManager.NameNotFoundException {
        PeopleSpaceTile peopleSpaceTile;
        synchronized (mTiles) {
            peopleSpaceTile = mTiles.get(Integer.valueOf(i));
        }
        if (peopleSpaceTile != null) {
            return peopleSpaceTile;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return getTileFromPersistentStorage(new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", "")), i, true);
    }

    public PeopleSpaceTile getTileFromPersistentStorage(PeopleTileKey peopleTileKey, int i, boolean z) throws PackageManager.NameNotFoundException {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            Log.e("PeopleSpaceWidgetMgr", "PeopleTileKey invalid: " + peopleTileKey.toString());
            return null;
        }
        IPeopleManager iPeopleManager = this.mIPeopleManager;
        if (iPeopleManager == null || this.mLauncherApps == null) {
            Log.d("PeopleSpaceWidgetMgr", "System services are null");
            return null;
        }
        try {
            ConversationChannel conversation = iPeopleManager.getConversation(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId());
            if (conversation == null) {
                return null;
            }
            PeopleSpaceTile.Builder builder = new PeopleSpaceTile.Builder(conversation, this.mLauncherApps);
            String string = this.mSharedPrefs.getString(String.valueOf(i), (String) null);
            if (z && string != null && builder.build().getContactUri() == null) {
                builder.setContactUri(Uri.parse(string));
            }
            return getTileWithCurrentState(builder.build(), "android.intent.action.BOOT_COMPLETED");
        } catch (RemoteException e) {
            Log.e("PeopleSpaceWidgetMgr", "getTileFromPersistentStorage failing", e);
            return null;
        }
    }

    public void updateWidgetsWithNotificationChanged(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda11(this, statusBarNotification, notificationAction, this.mNotifCollection.getAllNotifs()));
    }

    /* renamed from: updateWidgetsWithNotificationChangedInBackground */
    public final void lambda$updateWidgetsWithNotificationChanged$1(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction, Collection<NotificationEntry> collection) {
        try {
            PeopleTileKey peopleTileKey = new PeopleTileKey(statusBarNotification.getShortcutId(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getPackageName());
            if (PeopleTileKey.isValid(peopleTileKey)) {
                if (this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class)).length == 0) {
                    Log.d("PeopleSpaceWidgetMgr", "No app widget ids returned");
                    return;
                }
                synchronized (this.mLock) {
                    Set<String> matchingKeyWidgetIds = getMatchingKeyWidgetIds(peopleTileKey);
                    matchingKeyWidgetIds.addAll(getMatchingUriWidgetIds(statusBarNotification, notificationAction));
                    updateWidgetIdsBasedOnNotifications(matchingKeyWidgetIds, collection);
                }
            }
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "updateWidgetsWithNotificationChangedInBackground failing", e);
        }
    }

    public final void updateWidgetIdsBasedOnNotifications(Set<String> set, Collection<NotificationEntry> collection) {
        if (!set.isEmpty()) {
            try {
                ((Map) set.stream().map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda12()).collect(Collectors.toMap(Function.identity(), new PeopleSpaceWidgetManager$$ExternalSyntheticLambda13(this, groupConversationNotifications(collection))))).forEach(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda14(this));
            } catch (Exception e) {
                Log.e("PeopleSpaceWidgetMgr", "updateWidgetIdsBasedOnNotifications failing", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Optional lambda$updateWidgetIdsBasedOnNotifications$2(Map map, Integer num) {
        return getAugmentedTileForExistingWidget(num.intValue(), map);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWidgetIdsBasedOnNotifications$3(Integer num, Optional optional) {
        updateAppWidgetOptionsAndViewOptional(num.intValue(), optional);
    }

    public PeopleSpaceTile augmentTileFromNotificationEntryManager(PeopleSpaceTile peopleSpaceTile, Optional<Integer> optional) {
        return augmentTileFromNotifications(peopleSpaceTile, new PeopleTileKey(peopleSpaceTile), peopleSpaceTile.getContactUri() != null ? peopleSpaceTile.getContactUri().toString() : null, groupConversationNotifications(this.mNotifCollection.getAllNotifs()), optional);
    }

    public Map<PeopleTileKey, Set<NotificationEntry>> groupConversationNotifications(Collection<NotificationEntry> collection) {
        return (Map) collection.stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda2(this)).collect(Collectors.groupingBy(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda3(), Collectors.mapping(Function.identity(), Collectors.toSet())));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$groupConversationNotifications$4(NotificationEntry notificationEntry) {
        return NotificationHelper.isValid(notificationEntry) && NotificationHelper.isMissedCallOrHasContent(notificationEntry) && !NotificationHelper.shouldFilterOut(this.mBubblesOptional, notificationEntry);
    }

    public PeopleSpaceTile augmentTileFromNotifications(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, String str, Map<PeopleTileKey, Set<NotificationEntry>> map, Optional<Integer> optional) {
        boolean z = this.mPackageManager.checkPermission("android.permission.READ_CONTACTS", peopleSpaceTile.getPackageName()) == 0;
        List<NotificationEntry> arrayList = new ArrayList<>();
        if (z) {
            arrayList = PeopleSpaceUtils.getNotificationsByUri(this.mPackageManager, str, map);
            arrayList.isEmpty();
        }
        Set set = map.get(peopleTileKey);
        if (set == null) {
            set = new HashSet();
        }
        if (set.isEmpty() && arrayList.isEmpty()) {
            return PeopleSpaceUtils.removeNotificationFields(peopleSpaceTile);
        }
        set.addAll(arrayList);
        return PeopleSpaceUtils.augmentTileFromNotification(this.mContext, peopleSpaceTile, peopleTileKey, NotificationHelper.getHighestPriorityNotification(set), PeopleSpaceUtils.getMessagesCount(set), optional, this.mBackupManager);
    }

    public Optional<PeopleSpaceTile> getAugmentedTileForExistingWidget(int i, Map<PeopleTileKey, Set<NotificationEntry>> map) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(augmentTileFromNotifications(tileForExistingWidget, new PeopleTileKey(tileForExistingWidget), this.mSharedPrefs.getString(String.valueOf(i), (String) null), map, Optional.of(Integer.valueOf(i))));
    }

    public Set<String> getMatchingKeyWidgetIds(PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return new HashSet();
        }
        return new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
    }

    public final Set<String> getMatchingUriWidgetIds(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        if (notificationAction.equals(PeopleSpaceUtils.NotificationAction.POSTED)) {
            Set<String> fetchMatchingUriWidgetIds = fetchMatchingUriWidgetIds(statusBarNotification);
            if (fetchMatchingUriWidgetIds != null && !fetchMatchingUriWidgetIds.isEmpty()) {
                this.mNotificationKeyToWidgetIdsMatchedByUri.put(statusBarNotification.getKey(), fetchMatchingUriWidgetIds);
                return fetchMatchingUriWidgetIds;
            }
        } else {
            Set<String> remove = this.mNotificationKeyToWidgetIdsMatchedByUri.remove(statusBarNotification.getKey());
            if (remove != null && !remove.isEmpty()) {
                return remove;
            }
        }
        return new HashSet();
    }

    public final Set<String> fetchMatchingUriWidgetIds(StatusBarNotification statusBarNotification) {
        String contactUri;
        if (!NotificationHelper.shouldMatchNotificationByUri(statusBarNotification) || (contactUri = NotificationHelper.getContactUri(statusBarNotification)) == null) {
            return null;
        }
        HashSet hashSet = new HashSet(this.mSharedPrefs.getStringSet(contactUri, new HashSet()));
        if (hashSet.isEmpty()) {
            return null;
        }
        return hashSet;
    }

    public void updateWidgetsWithConversationChanged(ConversationChannel conversationChannel) {
        ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
        synchronized (this.mLock) {
            for (String parseInt : getMatchingKeyWidgetIds(new PeopleTileKey(shortcutInfo.getId(), shortcutInfo.getUserId(), shortcutInfo.getPackage()))) {
                updateStorageAndViewWithConversationData(conversationChannel, Integer.parseInt(parseInt));
            }
        }
    }

    public final void updateStorageAndViewWithConversationData(ConversationChannel conversationChannel, int i) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget != null) {
            PeopleSpaceTile.Builder builder = tileForExistingWidget.toBuilder();
            ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
            Uri uri = null;
            if (shortcutInfo.getPersons() != null && shortcutInfo.getPersons().length > 0) {
                Person person = shortcutInfo.getPersons()[0];
                if (person.getUri() != null) {
                    uri = Uri.parse(person.getUri());
                }
            }
            CharSequence label = shortcutInfo.getLabel();
            if (label != null) {
                builder.setUserName(label);
            }
            Icon convertDrawableToIcon = PeopleSpaceTile.convertDrawableToIcon(this.mLauncherApps.getShortcutIconDrawable(shortcutInfo, 0));
            if (convertDrawableToIcon != null) {
                builder.setUserIcon(convertDrawableToIcon);
            }
            NotificationChannel notificationChannel = conversationChannel.getNotificationChannel();
            if (notificationChannel != null) {
                builder.setIsImportantConversation(notificationChannel.isImportantConversation());
            }
            builder.setContactUri(uri).setStatuses(conversationChannel.getStatuses()).setLastInteractionTimestamp(conversationChannel.getLastEventTimestamp());
            lambda$addNewWidget$5(i, builder.build());
        }
    }

    public void attach(NotificationListener notificationListener) {
        notificationListener.addNotificationHandler(this.mListener);
    }

    public void onAppWidgetOptionsChanged(int i, Bundle bundle) {
        PeopleTileKey peopleTileKeyFromBundle = AppWidgetOptionsHelper.getPeopleTileKeyFromBundle(bundle);
        if (PeopleTileKey.isValid(peopleTileKeyFromBundle)) {
            AppWidgetOptionsHelper.removePeopleTileKey(this.mAppWidgetManager, i);
            addNewWidget(i, peopleTileKeyFromBundle);
        }
        updateWidgets(new int[]{i});
    }

    public void addNewWidget(int i, PeopleTileKey peopleTileKey) {
        PeopleTileKey keyFromStorageByWidgetId;
        try {
            PeopleSpaceTile tileFromPersistentStorage = getTileFromPersistentStorage(peopleTileKey, i, false);
            if (tileFromPersistentStorage != null) {
                PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tileFromPersistentStorage, Optional.of(Integer.valueOf(i)));
                synchronized (this.mLock) {
                    keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
                }
                if (PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
                    deleteWidgets(new int[]{i});
                } else {
                    this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_ADDED);
                }
                synchronized (this.mLock) {
                    PeopleSpaceUtils.setSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, augmentTileFromNotificationEntryManager.getContactUri(), this.mBackupManager);
                }
                registerConversationListenerIfNeeded(i, peopleTileKey);
                try {
                    this.mLauncherApps.cacheShortcuts(augmentTileFromNotificationEntryManager.getPackageName(), Collections.singletonList(augmentTileFromNotificationEntryManager.getId()), augmentTileFromNotificationEntryManager.getUserHandle(), 2);
                } catch (Exception e) {
                    Log.w("PeopleSpaceWidgetMgr", "failed to cache shortcut", e);
                }
                this.mBgExecutor.execute(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda0(this, i, augmentTileFromNotificationEntryManager));
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot add widget since app was uninstalled");
        }
    }

    public void registerConversationListenerIfNeeded(int i, PeopleTileKey peopleTileKey) {
        if (PeopleTileKey.isValid(peopleTileKey)) {
            TileConversationListener tileConversationListener = new TileConversationListener();
            synchronized (mListeners) {
                if (!mListeners.containsKey(peopleTileKey)) {
                    mListeners.put(peopleTileKey, tileConversationListener);
                    this.mPeopleManager.registerConversationListener(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId(), tileConversationListener, this.mContext.getMainExecutor());
                }
            }
        }
    }

    public final PeopleTileKey getKeyFromStorageByWidgetId(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", ""));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005d, code lost:
        r8 = r11.mLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x005f, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        com.android.systemui.people.PeopleSpaceUtils.removeSharedPreferencesStorageForTile(r11.mContext, r6, r3, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0065, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006e, code lost:
        if (r5.contains(java.lang.String.valueOf(r3)) == false) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0075, code lost:
        if (r5.size() != 1) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0077, code lost:
        unregisterConversationListener(r6, r3);
        uncacheConversationShortcut(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007d, code lost:
        r2 = r2 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void deleteWidgets(int[] r12) {
        /*
            r11 = this;
            int r0 = r12.length
            r1 = 0
            r2 = r1
        L_0x0003:
            if (r2 >= r0) goto L_0x0086
            r3 = r12[r2]
            com.android.internal.logging.UiEventLogger r4 = r11.mUiEventLogger
            com.android.systemui.people.PeopleSpaceUtils$PeopleSpaceWidgetEvent r5 = com.android.systemui.people.PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_DELETED
            r4.log(r5)
            java.lang.Object r4 = r11.mLock
            monitor-enter(r4)
            android.content.Context r5 = r11.mContext     // Catch:{ all -> 0x0083 }
            java.lang.String r6 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x0083 }
            android.content.SharedPreferences r5 = r5.getSharedPreferences(r6, r1)     // Catch:{ all -> 0x0083 }
            com.android.systemui.people.widget.PeopleTileKey r6 = new com.android.systemui.people.widget.PeopleTileKey     // Catch:{ all -> 0x0083 }
            java.lang.String r7 = "shortcut_id"
            r8 = 0
            java.lang.String r7 = r5.getString(r7, r8)     // Catch:{ all -> 0x0083 }
            java.lang.String r9 = "user_id"
            r10 = -1
            int r9 = r5.getInt(r9, r10)     // Catch:{ all -> 0x0083 }
            java.lang.String r10 = "package_name"
            java.lang.String r5 = r5.getString(r10, r8)     // Catch:{ all -> 0x0083 }
            r6.<init>(r7, r9, r5)     // Catch:{ all -> 0x0083 }
            boolean r5 = com.android.systemui.people.widget.PeopleTileKey.isValid(r6)     // Catch:{ all -> 0x0083 }
            if (r5 != 0) goto L_0x003e
            monitor-exit(r4)     // Catch:{ all -> 0x0083 }
            return
        L_0x003e:
            java.util.HashSet r5 = new java.util.HashSet     // Catch:{ all -> 0x0083 }
            android.content.SharedPreferences r7 = r11.mSharedPrefs     // Catch:{ all -> 0x0083 }
            java.lang.String r9 = r6.toString()     // Catch:{ all -> 0x0083 }
            java.util.HashSet r10 = new java.util.HashSet     // Catch:{ all -> 0x0083 }
            r10.<init>()     // Catch:{ all -> 0x0083 }
            java.util.Set r7 = r7.getStringSet(r9, r10)     // Catch:{ all -> 0x0083 }
            r5.<init>(r7)     // Catch:{ all -> 0x0083 }
            android.content.SharedPreferences r7 = r11.mSharedPrefs     // Catch:{ all -> 0x0083 }
            java.lang.String r9 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x0083 }
            java.lang.String r7 = r7.getString(r9, r8)     // Catch:{ all -> 0x0083 }
            monitor-exit(r4)     // Catch:{ all -> 0x0083 }
            java.lang.Object r8 = r11.mLock
            monitor-enter(r8)
            android.content.Context r4 = r11.mContext     // Catch:{ all -> 0x0080 }
            com.android.systemui.people.PeopleSpaceUtils.removeSharedPreferencesStorageForTile(r4, r6, r3, r7)     // Catch:{ all -> 0x0080 }
            monitor-exit(r8)     // Catch:{ all -> 0x0080 }
            java.lang.String r4 = java.lang.String.valueOf(r3)
            boolean r4 = r5.contains(r4)
            if (r4 == 0) goto L_0x007d
            int r4 = r5.size()
            r5 = 1
            if (r4 != r5) goto L_0x007d
            r11.unregisterConversationListener(r6, r3)
            r11.uncacheConversationShortcut(r6)
        L_0x007d:
            int r2 = r2 + 1
            goto L_0x0003
        L_0x0080:
            r11 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0080 }
            throw r11
        L_0x0083:
            r11 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0083 }
            throw r11
        L_0x0086:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.deleteWidgets(int[]):void");
    }

    public final void unregisterConversationListener(PeopleTileKey peopleTileKey, int i) {
        synchronized (mListeners) {
            TileConversationListener tileConversationListener = mListeners.get(peopleTileKey);
            if (tileConversationListener != null) {
                mListeners.remove(peopleTileKey);
                this.mPeopleManager.unregisterConversationListener(tileConversationListener);
            }
        }
    }

    public final void uncacheConversationShortcut(PeopleTileKey peopleTileKey) {
        try {
            this.mLauncherApps.uncacheShortcuts(peopleTileKey.getPackageName(), Collections.singletonList(peopleTileKey.getShortcutId()), UserHandle.of(peopleTileKey.getUserId()), 2);
        } catch (Exception e) {
            Log.d("PeopleSpaceWidgetMgr", "failed to uncache shortcut", e);
        }
    }

    public boolean requestPinAppWidget(ShortcutInfo shortcutInfo, Bundle bundle) {
        RemoteViews preview = getPreview(shortcutInfo.getId(), shortcutInfo.getUserHandle(), shortcutInfo.getPackage(), bundle);
        if (preview == null) {
            Log.w("PeopleSpaceWidgetMgr", "Skipping pinning widget: no tile for shortcutId: " + shortcutInfo.getId());
            return false;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("appWidgetPreview", preview);
        PendingIntent pendingIntent = PeopleSpaceWidgetPinnedReceiver.getPendingIntent(this.mContext, shortcutInfo);
        return this.mAppWidgetManager.requestPinAppWidget(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class), bundle2, pendingIntent);
    }

    public List<PeopleSpaceTile> getPriorityTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, this.mINotificationManager.getConversations(true).getList().stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda4()).map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda5()));
    }

    public static /* synthetic */ boolean lambda$getPriorityTiles$6(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() != null && conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public List<PeopleSpaceTile> getRecentTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, Stream.concat(this.mINotificationManager.getConversations(false).getList().stream().filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda6()).map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda7()), this.mIPeopleManager.getRecentConversations().getList().stream().map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda8())));
    }

    public static /* synthetic */ boolean lambda$getRecentTiles$8(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() == null || !conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public RemoteViews getPreview(String str, UserHandle userHandle, String str2, Bundle bundle) {
        try {
            PeopleSpaceTile tile = PeopleSpaceUtils.getTile(this.mIPeopleManager.getConversation(str2, userHandle.getIdentifier(), str), this.mLauncherApps);
            if (tile == null) {
                return null;
            }
            PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tile, Optional.empty());
            return PeopleTileViewHelper.createRemoteViews(this.mContext, augmentTileFromNotificationEntryManager, 0, bundle, new PeopleTileKey(augmentTileFromNotificationEntryManager));
        } catch (Exception e) {
            Log.w("PeopleSpaceWidgetMgr", "failed to get conversation or tile", e);
            return null;
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:659)
        	at java.util.ArrayList.get(ArrayList.java:435)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    @com.android.internal.annotations.VisibleForTesting
    public void updateWidgetsFromBroadcastInBackground(java.lang.String r10) {
        /*
            r9 = this;
            android.appwidget.AppWidgetManager r0 = r9.mAppWidgetManager
            android.content.ComponentName r1 = new android.content.ComponentName
            android.content.Context r2 = r9.mContext
            java.lang.Class<com.android.systemui.people.widget.PeopleSpaceWidgetProvider> r3 = com.android.systemui.people.widget.PeopleSpaceWidgetProvider.class
            r1.<init>(r2, r3)
            int[] r0 = r0.getAppWidgetIds(r1)
            if (r0 != 0) goto L_0x0012
            return
        L_0x0012:
            int r1 = r0.length
            r2 = 0
            r3 = r2
        L_0x0015:
            if (r3 >= r1) goto L_0x006b
            r4 = r0[r3]
            r5 = 0
            java.lang.Object r6 = r9.mLock     // Catch:{ NameNotFoundException -> 0x0038 }
            monitor-enter(r6)     // Catch:{ NameNotFoundException -> 0x0038 }
            android.app.people.PeopleSpaceTile r7 = r9.getTileForExistingWidgetThrowing(r4)     // Catch:{ all -> 0x0035 }
            if (r7 != 0) goto L_0x002c
            java.lang.String r7 = "PeopleSpaceWidgetMgr"
            java.lang.String r8 = "Matching conversation not found for shortcut ID"
            android.util.Log.e(r7, r8)     // Catch:{ all -> 0x0035 }
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            goto L_0x0065
        L_0x002c:
            android.app.people.PeopleSpaceTile r5 = r9.getTileWithCurrentState(r7, r10)     // Catch:{ all -> 0x0035 }
            r9.lambda$addNewWidget$5(r4, r5)     // Catch:{ all -> 0x0035 }
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            goto L_0x0065
        L_0x0035:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0035 }
            throw r7     // Catch:{ NameNotFoundException -> 0x0038 }
        L_0x0038:
            r6 = move-exception
            java.lang.String r7 = "PeopleSpaceWidgetMgr"
            java.lang.String r8 = "package no longer found for tile"
            android.util.Log.e(r7, r8, r6)
            android.content.Context r6 = r9.mContext
            java.lang.Class<android.app.job.JobScheduler> r7 = android.app.job.JobScheduler.class
            java.lang.Object r6 = r6.getSystemService(r7)
            android.app.job.JobScheduler r6 = (android.app.job.JobScheduler) r6
            if (r6 == 0) goto L_0x0056
            r7 = 74823873(0x475b8c1, float:2.8884446E-36)
            android.app.job.JobInfo r6 = r6.getPendingJob(r7)
            if (r6 == 0) goto L_0x0056
            goto L_0x0065
        L_0x0056:
            java.lang.Object r6 = r9.mLock
            monitor-enter(r6)
            r9.lambda$addNewWidget$5(r4, r5)     // Catch:{ all -> 0x0068 }
            monitor-exit(r6)     // Catch:{ all -> 0x0068 }
            r5 = 1
            int[] r5 = new int[r5]
            r5[r2] = r4
            r9.deleteWidgets(r5)
        L_0x0065:
            int r3 = r3 + 1
            goto L_0x0015
        L_0x0068:
            r9 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0068 }
            throw r9
        L_0x006b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.updateWidgetsFromBroadcastInBackground(java.lang.String):void");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.app.people.PeopleSpaceTile getTileWithCurrentState(android.app.people.PeopleSpaceTile r3, java.lang.String r4) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            r2 = this;
            android.app.people.PeopleSpaceTile$Builder r0 = r3.toBuilder()
            int r1 = r4.hashCode()
            switch(r1) {
                case -1238404651: goto L_0x0052;
                case -1001645458: goto L_0x0048;
                case -864107122: goto L_0x003e;
                case -19011148: goto L_0x0034;
                case 798292259: goto L_0x002a;
                case 833559602: goto L_0x0020;
                case 1290767157: goto L_0x0016;
                case 2106958107: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x005c
        L_0x000c:
            java.lang.String r1 = "android.app.action.INTERRUPTION_FILTER_CHANGED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 0
            goto L_0x005d
        L_0x0016:
            java.lang.String r1 = "android.intent.action.PACKAGES_UNSUSPENDED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 2
            goto L_0x005d
        L_0x0020:
            java.lang.String r1 = "android.intent.action.USER_UNLOCKED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 5
            goto L_0x005d
        L_0x002a:
            java.lang.String r1 = "android.intent.action.BOOT_COMPLETED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 7
            goto L_0x005d
        L_0x0034:
            java.lang.String r1 = "android.intent.action.LOCALE_CHANGED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 6
            goto L_0x005d
        L_0x003e:
            java.lang.String r1 = "android.intent.action.MANAGED_PROFILE_AVAILABLE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 3
            goto L_0x005d
        L_0x0048:
            java.lang.String r1 = "android.intent.action.PACKAGES_SUSPENDED"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 1
            goto L_0x005d
        L_0x0052:
            java.lang.String r1 = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x005c
            r4 = 4
            goto L_0x005d
        L_0x005c:
            r4 = -1
        L_0x005d:
            switch(r4) {
                case 0: goto L_0x0088;
                case 1: goto L_0x0080;
                case 2: goto L_0x0080;
                case 3: goto L_0x0078;
                case 4: goto L_0x0078;
                case 5: goto L_0x0078;
                case 6: goto L_0x008f;
                default: goto L_0x0060;
            }
        L_0x0060:
            boolean r4 = r2.getUserQuieted(r3)
            android.app.people.PeopleSpaceTile$Builder r4 = r0.setIsUserQuieted(r4)
            boolean r3 = r2.getPackageSuspended(r3)
            android.app.people.PeopleSpaceTile$Builder r3 = r4.setIsPackageSuspended(r3)
            int r2 = r2.getNotificationPolicyState()
            r3.setNotificationPolicyState(r2)
            goto L_0x008f
        L_0x0078:
            boolean r2 = r2.getUserQuieted(r3)
            r0.setIsUserQuieted(r2)
            goto L_0x008f
        L_0x0080:
            boolean r2 = r2.getPackageSuspended(r3)
            r0.setIsPackageSuspended(r2)
            goto L_0x008f
        L_0x0088:
            int r2 = r2.getNotificationPolicyState()
            r0.setNotificationPolicyState(r2)
        L_0x008f:
            android.app.people.PeopleSpaceTile r2 = r0.build()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getTileWithCurrentState(android.app.people.PeopleSpaceTile, java.lang.String):android.app.people.PeopleSpaceTile");
    }

    public final boolean getPackageSuspended(PeopleSpaceTile peopleSpaceTile) throws PackageManager.NameNotFoundException {
        boolean z = !TextUtils.isEmpty(peopleSpaceTile.getPackageName()) && this.mPackageManager.isPackageSuspended(peopleSpaceTile.getPackageName());
        this.mPackageManager.getApplicationInfoAsUser(peopleSpaceTile.getPackageName(), 128, PeopleSpaceUtils.getUserId(peopleSpaceTile));
        return z;
    }

    public final boolean getUserQuieted(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getUserHandle() != null && this.mUserManager.isQuietModeEnabled(peopleSpaceTile.getUserHandle());
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int getNotificationPolicyState() {
        /*
            r4 = this;
            android.app.NotificationManager r0 = r4.mNotificationManager
            android.app.NotificationManager$Policy r0 = r0.getNotificationPolicy()
            int r1 = r0.suppressedVisualEffects
            boolean r1 = android.app.NotificationManager.Policy.areAllVisualEffectsSuppressed(r1)
            r2 = 1
            if (r1 != 0) goto L_0x0010
            return r2
        L_0x0010:
            android.app.NotificationManager r4 = r4.mNotificationManager
            int r4 = r4.getCurrentInterruptionFilter()
            if (r4 == r2) goto L_0x0045
            r1 = 2
            if (r4 == r1) goto L_0x001c
            goto L_0x0044
        L_0x001c:
            boolean r4 = r0.allowConversations()
            if (r4 == 0) goto L_0x002b
            int r4 = r0.priorityConversationSenders
            if (r4 != r2) goto L_0x0027
            return r2
        L_0x0027:
            if (r4 != r1) goto L_0x002b
            r4 = 4
            goto L_0x002c
        L_0x002b:
            r4 = 0
        L_0x002c:
            boolean r3 = r0.allowMessages()
            if (r3 == 0) goto L_0x0041
            int r0 = r0.allowMessagesFrom()
            if (r0 == r2) goto L_0x003e
            if (r0 == r1) goto L_0x003b
            return r2
        L_0x003b:
            r4 = r4 | 8
            return r4
        L_0x003e:
            r4 = r4 | 16
            return r4
        L_0x0041:
            if (r4 == 0) goto L_0x0044
            return r4
        L_0x0044:
            return r1
        L_0x0045:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getNotificationPolicyState():int");
    }

    public void remapWidgets(int[] iArr, int[] iArr2) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < iArr.length; i++) {
            hashMap.put(String.valueOf(iArr[i]), String.valueOf(iArr2[i]));
        }
        remapWidgetFiles(hashMap);
        remapSharedFile(hashMap);
        remapFollowupFile(hashMap);
        int[] appWidgetIds = this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class));
        Bundle bundle = new Bundle();
        bundle.putBoolean("appWidgetRestoreCompleted", true);
        for (int updateAppWidgetOptions : appWidgetIds) {
            this.mAppWidgetManager.updateAppWidgetOptions(updateAppWidgetOptions, bundle);
        }
        updateWidgets(appWidgetIds);
    }

    public void remapWidgetFiles(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            String valueOf = String.valueOf(next.getKey());
            String valueOf2 = String.valueOf(next.getValue());
            if (!valueOf.equals(valueOf2)) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(valueOf, 0);
                PeopleTileKey peopleTileKey = SharedPreferencesHelper.getPeopleTileKey(sharedPreferences);
                if (PeopleTileKey.isValid(peopleTileKey)) {
                    hashMap.put(valueOf2, peopleTileKey);
                    SharedPreferencesHelper.clear(sharedPreferences);
                }
            }
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            SharedPreferencesHelper.setPeopleTileKey(this.mContext.getSharedPreferences((String) entry.getKey(), 0), (PeopleTileKey) entry.getValue());
        }
    }

    public void remapSharedFile(Map<String, String> map) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        for (Map.Entry next : defaultSharedPreferences.getAll().entrySet()) {
            String str = (String) next.getKey();
            int i = AnonymousClass3.$SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[PeopleBackupHelper.getEntryType(next).ordinal()];
            if (i == 1) {
                String str2 = map.get(str);
                if (TextUtils.isEmpty(str2)) {
                    Log.w("PeopleSpaceWidgetMgr", "Key is widget id without matching new id, skipping: " + str);
                } else {
                    try {
                        edit.putString(str2, (String) next.getValue());
                    } catch (Exception e) {
                        Log.e("PeopleSpaceWidgetMgr", "malformed entry value: " + next.getValue(), e);
                    }
                    edit.remove(str);
                }
            } else if (i == 2 || i == 3) {
                try {
                    edit.putStringSet(str, getNewWidgets((Set) next.getValue(), map));
                } catch (Exception e2) {
                    Log.e("PeopleSpaceWidgetMgr", "malformed entry value: " + next.getValue(), e2);
                    edit.remove(str);
                }
            } else if (i == 4) {
                Log.e("PeopleSpaceWidgetMgr", "Key not identified:" + str);
            }
        }
        edit.apply();
    }

    /* renamed from: com.android.systemui.people.widget.PeopleSpaceWidgetManager$3  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType[] r0 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType = r0
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.WIDGET_ID     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.CONTACT_URI     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.AnonymousClass3.<clinit>():void");
        }
    }

    public void remapFollowupFile(Map<String, String> map) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("shared_follow_up", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (Map.Entry next : sharedPreferences.getAll().entrySet()) {
            String str = (String) next.getKey();
            try {
                edit.putStringSet(str, getNewWidgets((Set) next.getValue(), map));
            } catch (Exception e) {
                Log.e("PeopleSpaceWidgetMgr", "malformed entry value: " + next.getValue(), e);
                edit.remove(str);
            }
        }
        edit.apply();
    }

    public final Set<String> getNewWidgets(Set<String> set, Map<String, String> map) {
        Stream stream = set.stream();
        Objects.requireNonNull(map);
        return (Set) stream.map(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda9(map)).filter(new PeopleSpaceWidgetManager$$ExternalSyntheticLambda10()).collect(Collectors.toSet());
    }

    public static /* synthetic */ boolean lambda$getNewWidgets$11(String str) {
        return !TextUtils.isEmpty(str);
    }
}
