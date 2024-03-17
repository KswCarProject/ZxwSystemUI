package com.google.android.setupcompat.internal;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.Logger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class SetupCompatServiceProvider {
    public static final Intent COMPAT_SERVICE_INTENT = new Intent().setPackage(PartnerConfigHelper.SUW_PACKAGE_NAME).setAction("com.google.android.setupcompat.SetupCompatService.BIND");
    public static final Logger LOG = new Logger("SetupCompatServiceProvider");
    public static boolean disableLooperCheckForTesting = false;
    @SuppressLint({"StaticFieldLeak"})
    public static volatile SetupCompatServiceProvider instance;
    public final AtomicReference<CountDownLatch> connectedConditionRef = new AtomicReference<>();
    public final Context context;
    public final ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            State state = State.CONNECTED;
            if (iBinder == null) {
                state = State.DISCONNECTED;
                SetupCompatServiceProvider.LOG.w("Binder is null when onServiceConnected was called!");
            }
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(state, ISetupCompatService.Stub.asInterface(iBinder)));
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.DISCONNECTED));
        }

        public void onBindingDied(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.REBIND_REQUIRED));
        }

        public void onNullBinding(ComponentName componentName) {
            SetupCompatServiceProvider.this.swapServiceContextAndNotify(new ServiceContext(State.SERVICE_NOT_USABLE));
        }
    };
    public volatile ServiceContext serviceContext = new ServiceContext(State.NOT_STARTED);

    public enum State {
        NOT_STARTED,
        BIND_FAILED,
        BINDING,
        CONNECTED,
        DISCONNECTED,
        SERVICE_NOT_USABLE,
        REBIND_REQUIRED
    }

    public static ISetupCompatService get(Context context2, long j, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        return getInstance(context2).getService(j, timeUnit);
    }

    public ISetupCompatService getService(long j, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        Preconditions.checkState(disableLooperCheckForTesting || Looper.getMainLooper() != Looper.myLooper(), "getService blocks and should not be called from the main thread.");
        ServiceContext currentServiceState = getCurrentServiceState();
        switch (AnonymousClass2.$SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State[currentServiceState.state.ordinal()]) {
            case 1:
                return currentServiceState.compatService;
            case 2:
            case 3:
                return null;
            case 4:
            case 5:
                return waitForConnection(j, timeUnit);
            case 6:
                requestServiceBind();
                return waitForConnection(j, timeUnit);
            case 7:
                LOG.w("NOT_STARTED state only possible before instance is created.");
                return null;
            default:
                throw new IllegalStateException("Unknown state = " + currentServiceState.state);
        }
    }

    /* renamed from: com.google.android.setupcompat.internal.SetupCompatServiceProvider$2  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State[] r0 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State = r0
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.CONNECTED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.SERVICE_NOT_USABLE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.BIND_FAILED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.DISCONNECTED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x003e }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.BINDING     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.REBIND_REQUIRED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$google$android$setupcompat$internal$SetupCompatServiceProvider$State     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.NOT_STARTED     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.internal.SetupCompatServiceProvider.AnonymousClass2.<clinit>():void");
        }
    }

    public final ISetupCompatService waitForConnection(long j, TimeUnit timeUnit) throws TimeoutException, InterruptedException {
        ServiceContext currentServiceState = getCurrentServiceState();
        if (currentServiceState.state == State.CONNECTED) {
            return currentServiceState.compatService;
        }
        CountDownLatch connectedCondition = getConnectedCondition();
        Logger logger = LOG;
        logger.atInfo("Waiting for service to get connected");
        if (connectedCondition.await(j, timeUnit)) {
            ServiceContext currentServiceState2 = getCurrentServiceState();
            logger.atInfo(String.format("Finished waiting for service to get connected. Current state = %s", new Object[]{currentServiceState2.state}));
            return currentServiceState2.compatService;
        }
        requestServiceBind();
        throw new TimeoutException(String.format("Failed to acquire connection after [%s %s]", new Object[]{Long.valueOf(j), timeUnit}));
    }

    public CountDownLatch createCountDownLatch() {
        return new CountDownLatch(1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0078, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void requestServiceBind() {
        /*
            r4 = this;
            monitor-enter(r4)
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$ServiceContext r0 = r4.getCurrentServiceState()     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r0 = r0.state     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.CONNECTED     // Catch:{ all -> 0x0079 }
            if (r0 != r1) goto L_0x0014
            com.google.android.setupcompat.util.Logger r0 = LOG     // Catch:{ all -> 0x0079 }
            java.lang.String r1 = "Refusing to rebind since current state is already connected"
            r0.atInfo(r1)     // Catch:{ all -> 0x0079 }
            monitor-exit(r4)
            return
        L_0x0014:
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.NOT_STARTED     // Catch:{ all -> 0x0079 }
            if (r0 == r1) goto L_0x0026
            com.google.android.setupcompat.util.Logger r0 = LOG     // Catch:{ all -> 0x0079 }
            java.lang.String r1 = "Unbinding existing service connection."
            r0.atInfo(r1)     // Catch:{ all -> 0x0079 }
            android.content.Context r0 = r4.context     // Catch:{ all -> 0x0079 }
            android.content.ServiceConnection r1 = r4.serviceConnection     // Catch:{ all -> 0x0079 }
            r0.unbindService(r1)     // Catch:{ all -> 0x0079 }
        L_0x0026:
            android.content.Context r0 = r4.context     // Catch:{ SecurityException -> 0x0032 }
            android.content.Intent r1 = COMPAT_SERVICE_INTENT     // Catch:{ SecurityException -> 0x0032 }
            android.content.ServiceConnection r2 = r4.serviceConnection     // Catch:{ SecurityException -> 0x0032 }
            r3 = 1
            boolean r0 = r0.bindService(r1, r2, r3)     // Catch:{ SecurityException -> 0x0032 }
            goto L_0x004a
        L_0x0032:
            r0 = move-exception
            com.google.android.setupcompat.util.Logger r1 = LOG     // Catch:{ all -> 0x0079 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0079 }
            r2.<init>()     // Catch:{ all -> 0x0079 }
            java.lang.String r3 = "Unable to bind to compat service. "
            r2.append(r3)     // Catch:{ all -> 0x0079 }
            r2.append(r0)     // Catch:{ all -> 0x0079 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0079 }
            r1.e(r0)     // Catch:{ all -> 0x0079 }
            r0 = 0
        L_0x004a:
            if (r0 == 0) goto L_0x0066
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r0 = r4.getCurrentState()     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.CONNECTED     // Catch:{ all -> 0x0079 }
            if (r0 == r1) goto L_0x0077
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$ServiceContext r0 = new com.google.android.setupcompat.internal.SetupCompatServiceProvider$ServiceContext     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.BINDING     // Catch:{ all -> 0x0079 }
            r0.<init>(r1)     // Catch:{ all -> 0x0079 }
            r4.swapServiceContextAndNotify(r0)     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.util.Logger r0 = LOG     // Catch:{ all -> 0x0079 }
            java.lang.String r1 = "Context#bindService went through, now waiting for service connection"
            r0.atInfo(r1)     // Catch:{ all -> 0x0079 }
            goto L_0x0077
        L_0x0066:
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$ServiceContext r0 = new com.google.android.setupcompat.internal.SetupCompatServiceProvider$ServiceContext     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.internal.SetupCompatServiceProvider$State r1 = com.google.android.setupcompat.internal.SetupCompatServiceProvider.State.BIND_FAILED     // Catch:{ all -> 0x0079 }
            r0.<init>(r1)     // Catch:{ all -> 0x0079 }
            r4.swapServiceContextAndNotify(r0)     // Catch:{ all -> 0x0079 }
            com.google.android.setupcompat.util.Logger r0 = LOG     // Catch:{ all -> 0x0079 }
            java.lang.String r1 = "Context#bindService did not succeed."
            r0.e(r1)     // Catch:{ all -> 0x0079 }
        L_0x0077:
            monitor-exit(r4)
            return
        L_0x0079:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.internal.SetupCompatServiceProvider.requestServiceBind():void");
    }

    public State getCurrentState() {
        return this.serviceContext.state;
    }

    public final synchronized ServiceContext getCurrentServiceState() {
        return this.serviceContext;
    }

    public void swapServiceContextAndNotify(ServiceContext serviceContext2) {
        LOG.atInfo(String.format("State changed: %s -> %s", new Object[]{this.serviceContext.state, serviceContext2.state}));
        this.serviceContext = serviceContext2;
        CountDownLatch andClearConnectedCondition = getAndClearConnectedCondition();
        if (andClearConnectedCondition != null) {
            andClearConnectedCondition.countDown();
        }
    }

    public final CountDownLatch getAndClearConnectedCondition() {
        return this.connectedConditionRef.getAndSet((Object) null);
    }

    public final CountDownLatch getConnectedCondition() {
        CountDownLatch createCountDownLatch;
        do {
            CountDownLatch countDownLatch = this.connectedConditionRef.get();
            if (countDownLatch != null) {
                return countDownLatch;
            }
            createCountDownLatch = createCountDownLatch();
        } while (!this.connectedConditionRef.compareAndSet((Object) null, createCountDownLatch));
        return createCountDownLatch;
    }

    public SetupCompatServiceProvider(Context context2) {
        this.context = context2.getApplicationContext();
    }

    public static final class ServiceContext {
        public final ISetupCompatService compatService;
        public final State state;

        public ServiceContext(State state2, ISetupCompatService iSetupCompatService) {
            this.state = state2;
            this.compatService = iSetupCompatService;
            if (state2 == State.CONNECTED) {
                Preconditions.checkNotNull(iSetupCompatService, "CompatService cannot be null when state is connected");
            }
        }

        public ServiceContext(State state2) {
            this(state2, (ISetupCompatService) null);
        }
    }

    public static SetupCompatServiceProvider getInstance(Context context2) {
        Preconditions.checkNotNull(context2, "Context object cannot be null.");
        SetupCompatServiceProvider setupCompatServiceProvider = instance;
        if (setupCompatServiceProvider == null) {
            synchronized (SetupCompatServiceProvider.class) {
                setupCompatServiceProvider = instance;
                if (setupCompatServiceProvider == null) {
                    setupCompatServiceProvider = new SetupCompatServiceProvider(context2.getApplicationContext());
                    instance = setupCompatServiceProvider;
                    instance.requestServiceBind();
                }
            }
        }
        return setupCompatServiceProvider;
    }

    public static void setInstanceForTesting(SetupCompatServiceProvider setupCompatServiceProvider) {
        instance = setupCompatServiceProvider;
    }
}
