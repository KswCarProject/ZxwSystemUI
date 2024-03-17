package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.viewpager2.widget.ViewPager2;
import com.android.systemui.Prefs;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.TooltipManager;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity extends LifecycleActivity {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @Nullable
    public CharSequence appName;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    @Nullable
    public Runnable cancelLoadRunnable;
    public Comparator<StructureContainer> comparator;
    @Nullable
    public ComponentName component;
    @NotNull
    public final ControlsControllerImpl controller;
    @NotNull
    public final ControlsFavoritingActivity$controlsModelCallback$1 controlsModelCallback;
    @NotNull
    public final ControlsFavoritingActivity$currentUserTracker$1 currentUserTracker;
    public View doneButton;
    @NotNull
    public final Executor executor;
    public boolean fromProviderSelector;
    public boolean isPagerLoaded;
    @NotNull
    public List<StructureContainer> listOfStructures = CollectionsKt__CollectionsKt.emptyList();
    @NotNull
    public final ControlsFavoritingActivity$listingCallback$1 listingCallback;
    @NotNull
    public final ControlsListingController listingController;
    @Nullable
    public TooltipManager mTooltipManager;
    public View otherAppsButton;
    public ManagementPageIndicator pageIndicator;
    public TextView statusText;
    @Nullable
    public CharSequence structureExtra;
    public ViewPager2 structurePager;
    public TextView subtitleView;
    public TextView titleView;
    @NotNull
    public final ControlsUiController uiController;

    public ControlsFavoritingActivity(@NotNull Executor executor2, @NotNull ControlsControllerImpl controlsControllerImpl, @NotNull ControlsListingController controlsListingController, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull ControlsUiController controlsUiController) {
        this.executor = executor2;
        this.controller = controlsControllerImpl;
        this.listingController = controlsListingController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsFavoritingActivity$currentUserTracker$1(this, broadcastDispatcher2);
        this.listingCallback = new ControlsFavoritingActivity$listingCallback$1(this);
        this.controlsModelCallback = new ControlsFavoritingActivity$controlsModelCallback$1(this);
    }

    /* compiled from: ControlsFavoritingActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void onBackPressed() {
        if (!this.fromProviderSelector) {
            openControlsOrigin();
        }
        animateExitAndFinish();
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.comparator = new ControlsFavoritingActivity$onCreate$$inlined$compareBy$1(Collator.getInstance(getResources().getConfiguration().getLocales().get(0)));
        this.appName = getIntent().getCharSequenceExtra("extra_app_label");
        this.structureExtra = getIntent().getCharSequenceExtra("extra_structure");
        this.component = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        this.fromProviderSelector = getIntent().getBooleanExtra("extra_from_provider_selector", false);
        bindViews();
    }

    public final void loadControls() {
        ComponentName componentName = this.component;
        if (componentName != null) {
            TextView textView = this.statusText;
            if (textView == null) {
                textView = null;
            }
            textView.setText(getResources().getText(17040591));
            this.controller.loadForComponent(componentName, new ControlsFavoritingActivity$loadControls$1$1(this, getResources().getText(R$string.controls_favorite_other_zone_header)), new ControlsFavoritingActivity$loadControls$1$2(this));
        }
    }

    public final void setUpPager() {
        ViewPager2 viewPager2 = this.structurePager;
        ViewPager2 viewPager22 = null;
        if (viewPager2 == null) {
            viewPager2 = null;
        }
        viewPager2.setAlpha(0.0f);
        ManagementPageIndicator managementPageIndicator = this.pageIndicator;
        if (managementPageIndicator == null) {
            managementPageIndicator = null;
        }
        managementPageIndicator.setAlpha(0.0f);
        ViewPager2 viewPager23 = this.structurePager;
        if (viewPager23 != null) {
            viewPager22 = viewPager23;
        }
        viewPager22.setAdapter(new StructureAdapter(CollectionsKt__CollectionsKt.emptyList()));
        viewPager22.registerOnPageChangeCallback(new ControlsFavoritingActivity$setUpPager$1$1(this));
    }

    public final void bindViews() {
        setContentView(R$layout.controls_management);
        getLifecycle().addObserver(ControlsAnimations.INSTANCE.observerForAnimations((ViewGroup) requireViewById(R$id.controls_management_root), getWindow(), getIntent()));
        ViewStub viewStub = (ViewStub) requireViewById(R$id.stub);
        viewStub.setLayoutResource(R$layout.controls_management_favorites);
        viewStub.inflate();
        this.statusText = (TextView) requireViewById(R$id.status_message);
        ViewPager2 viewPager2 = null;
        if (shouldShowTooltip()) {
            TextView textView = this.statusText;
            if (textView == null) {
                textView = null;
            }
            TooltipManager tooltipManager = new TooltipManager(textView.getContext(), "ControlsStructureSwipeTooltipCount", 2, false, 8, (DefaultConstructorMarker) null);
            this.mTooltipManager = tooltipManager;
            addContentView(tooltipManager.getLayout(), new FrameLayout.LayoutParams(-2, -2, 51));
        }
        ManagementPageIndicator managementPageIndicator = (ManagementPageIndicator) requireViewById(R$id.structure_page_indicator);
        managementPageIndicator.setVisibilityListener(new ControlsFavoritingActivity$bindViews$2$1(this));
        this.pageIndicator = managementPageIndicator;
        CharSequence charSequence = this.structureExtra;
        if (charSequence == null && (charSequence = this.appName) == null) {
            charSequence = getResources().getText(R$string.controls_favorite_default_title);
        }
        TextView textView2 = (TextView) requireViewById(R$id.title);
        textView2.setText(charSequence);
        this.titleView = textView2;
        TextView textView3 = (TextView) requireViewById(R$id.subtitle);
        textView3.setText(textView3.getResources().getText(R$string.controls_favorite_subtitle));
        this.subtitleView = textView3;
        ViewPager2 viewPager22 = (ViewPager2) requireViewById(R$id.structure_pager);
        this.structurePager = viewPager22;
        if (viewPager22 != null) {
            viewPager2 = viewPager22;
        }
        viewPager2.registerOnPageChangeCallback(new ControlsFavoritingActivity$bindViews$5(this));
        bindButtons();
    }

    public final void animateExitAndFinish() {
        ControlsAnimations.exitAnimation((ViewGroup) requireViewById(R$id.controls_management_root), new ControlsFavoritingActivity$animateExitAndFinish$1(this)).start();
    }

    public final void bindButtons() {
        View requireViewById = requireViewById(R$id.other_apps);
        Button button = (Button) requireViewById;
        button.setOnClickListener(new ControlsFavoritingActivity$bindButtons$1$1(this, button));
        this.otherAppsButton = requireViewById;
        View requireViewById2 = requireViewById(R$id.done);
        Button button2 = (Button) requireViewById2;
        button2.setEnabled(false);
        button2.setOnClickListener(new ControlsFavoritingActivity$bindButtons$2$1(this));
        this.doneButton = requireViewById2;
    }

    public final void openControlsOrigin() {
        startActivity(new Intent(getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
    }

    public void onPause() {
        super.onPause();
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    public void onStart() {
        super.onStart();
        this.listingController.addCallback(this.listingCallback);
        this.currentUserTracker.startTracking();
    }

    public void onResume() {
        super.onResume();
        if (!this.isPagerLoaded) {
            setUpPager();
            loadControls();
            this.isPagerLoaded = true;
        }
    }

    public void onStop() {
        super.onStop();
        this.listingController.removeCallback(this.listingCallback);
        this.currentUserTracker.stopTracking();
    }

    public void onConfigurationChanged(@NotNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    public void onDestroy() {
        Runnable runnable = this.cancelLoadRunnable;
        if (runnable != null) {
            runnable.run();
        }
        super.onDestroy();
    }

    public final boolean shouldShowTooltip() {
        return Prefs.getInt(getApplicationContext(), "ControlsStructureSwipeTooltipCount", 0) < 2;
    }
}
