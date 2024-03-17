package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsProviderSelectorActivity.kt */
public final class ControlsProviderSelectorActivity extends LifecycleActivity {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final Executor backExecutor;
    public boolean backShouldExit;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    public final ControlsController controlsController;
    @NotNull
    public final ControlsProviderSelectorActivity$currentUserTracker$1 currentUserTracker;
    @NotNull
    public final Executor executor;
    @NotNull
    public final ControlsListingController listingController;
    public RecyclerView recyclerView;
    @NotNull
    public final ControlsUiController uiController;

    public ControlsProviderSelectorActivity(@NotNull Executor executor2, @NotNull Executor executor3, @NotNull ControlsListingController controlsListingController, @NotNull ControlsController controlsController2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull ControlsUiController controlsUiController) {
        this.executor = executor2;
        this.backExecutor = executor3;
        this.listingController = controlsListingController;
        this.controlsController = controlsController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsProviderSelectorActivity$currentUserTracker$1(this, broadcastDispatcher2);
    }

    /* compiled from: ControlsProviderSelectorActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.controls_management);
        getLifecycle().addObserver(ControlsAnimations.INSTANCE.observerForAnimations((ViewGroup) requireViewById(R$id.controls_management_root), getWindow(), getIntent()));
        ViewStub viewStub = (ViewStub) requireViewById(R$id.stub);
        viewStub.setLayoutResource(R$layout.controls_management_apps);
        viewStub.inflate();
        RecyclerView recyclerView2 = (RecyclerView) requireViewById(R$id.list);
        this.recyclerView = recyclerView2;
        if (recyclerView2 == null) {
            recyclerView2 = null;
        }
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        TextView textView = (TextView) requireViewById(R$id.title);
        textView.setText(textView.getResources().getText(R$string.controls_providers_title));
        Button button = (Button) requireViewById(R$id.other_apps);
        button.setVisibility(0);
        button.setText(17039360);
        button.setOnClickListener(new ControlsProviderSelectorActivity$onCreate$3$1(this));
        requireViewById(R$id.done).setVisibility(8);
        this.backShouldExit = getIntent().getBooleanExtra("back_should_exit", false);
    }

    public void onBackPressed() {
        if (!this.backShouldExit) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext(), ControlsActivity.class));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        animateExitAndFinish();
    }

    public void onStart() {
        super.onStart();
        this.currentUserTracker.startTracking();
        RecyclerView recyclerView2 = this.recyclerView;
        RecyclerView recyclerView3 = null;
        if (recyclerView2 == null) {
            recyclerView2 = null;
        }
        recyclerView2.setAlpha(0.0f);
        RecyclerView recyclerView4 = this.recyclerView;
        if (recyclerView4 != null) {
            recyclerView3 = recyclerView4;
        }
        AppAdapter appAdapter = new AppAdapter(this.backExecutor, this.executor, getLifecycle(), this.listingController, LayoutInflater.from(this), new ControlsProviderSelectorActivity$onStart$1(this), new FavoritesRenderer(getResources(), new ControlsProviderSelectorActivity$onStart$2(this.controlsController)), getResources());
        appAdapter.registerAdapterDataObserver(new ControlsProviderSelectorActivity$onStart$3$1(this));
        recyclerView3.setAdapter(appAdapter);
    }

    public void onStop() {
        super.onStop();
        this.currentUserTracker.stopTracking();
    }

    public final void launchFavoritingActivity(@Nullable ComponentName componentName) {
        this.executor.execute(new ControlsProviderSelectorActivity$launchFavoritingActivity$1(componentName, this));
    }

    public void onDestroy() {
        this.currentUserTracker.stopTracking();
        super.onDestroy();
    }

    public final void animateExitAndFinish() {
        ControlsAnimations.exitAnimation((ViewGroup) requireViewById(R$id.controls_management_root), new ControlsProviderSelectorActivity$animateExitAndFinish$1(this)).start();
    }
}
