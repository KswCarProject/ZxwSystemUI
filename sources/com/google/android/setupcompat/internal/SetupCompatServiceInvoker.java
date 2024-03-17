package com.google.android.setupcompat.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.util.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SetupCompatServiceInvoker {
    public static final Logger LOG = new Logger("SetupCompatServiceInvoker");
    public static final long MAX_WAIT_TIME_FOR_CONNECTION_MS = TimeUnit.SECONDS.toMillis(10);
    @SuppressLint({"StaticFieldLeak"})
    public static SetupCompatServiceInvoker instance;
    public final Context context;
    public final ExecutorService loggingExecutor = ExecutorProvider.setupCompatServiceInvoker.get();
    public final long waitTimeInMillisForServiceConnection = MAX_WAIT_TIME_FOR_CONNECTION_MS;

    @SuppressLint({"DefaultLocale"})
    public void logMetricEvent(int i, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda0(this, i, bundle));
        } catch (RejectedExecutionException e) {
            LOG.e(String.format("Metric of type %d dropped since queue is full.", new Object[]{Integer.valueOf(i)}), e);
        }
    }

    public void bindBack(String str, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda1(this, str, bundle));
        } catch (RejectedExecutionException e) {
            LOG.e(String.format("Screen %s bind back fail.", new Object[]{str}), e);
        }
    }

    public void onFocusStatusChanged(String str, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda2(this, str, bundle));
        } catch (RejectedExecutionException e) {
            LOG.e(String.format("Screen %s report focus changed failed.", new Object[]{str}), e);
        }
    }

    /* renamed from: invokeLogMetric */
    public final void lambda$logMetricEvent$0(int i, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.logMetric(i, bundle, Bundle.EMPTY);
            } else {
                LOG.w("logMetric failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | IllegalStateException | InterruptedException | TimeoutException e) {
            LOG.e(String.format("Exception occurred while trying to log metric = [%s]", new Object[]{bundle}), e);
        }
    }

    /* renamed from: invokeOnWindowFocusChanged */
    public final void lambda$onFocusStatusChanged$2(String str, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.onFocusStatusChanged(bundle);
            } else {
                LOG.w("Report focusChange failed since service reference is null. Are the permission valid?");
            }
        } catch (RemoteException | InterruptedException | UnsupportedOperationException | TimeoutException e) {
            LOG.e(String.format("Exception occurred while %s trying report windowFocusChange to SetupWizard.", new Object[]{str}), e);
        }
    }

    /* renamed from: invokeBindBack */
    public final void lambda$bindBack$1(String str, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.validateActivity(str, bundle);
            } else {
                LOG.w("BindBack failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | InterruptedException | TimeoutException e) {
            LOG.e(String.format("Exception occurred while %s trying bind back to SetupWizard.", new Object[]{str}), e);
        }
    }

    public SetupCompatServiceInvoker(Context context2) {
        this.context = context2;
    }

    public static synchronized SetupCompatServiceInvoker get(Context context2) {
        SetupCompatServiceInvoker setupCompatServiceInvoker;
        synchronized (SetupCompatServiceInvoker.class) {
            if (instance == null) {
                instance = new SetupCompatServiceInvoker(context2.getApplicationContext());
            }
            setupCompatServiceInvoker = instance;
        }
        return setupCompatServiceInvoker;
    }

    public static void setInstanceForTesting(SetupCompatServiceInvoker setupCompatServiceInvoker) {
        instance = setupCompatServiceInvoker;
    }
}
