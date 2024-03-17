package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity extends LifecycleActivity {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int EMPTY_TEXT_ID = R$string.controls_favorite_removed;
    public static final int SUBTITLE_ID = R$string.controls_favorite_rearrange;
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    public ComponentName component;
    @NotNull
    public final ControlsControllerImpl controller;
    @NotNull
    public final ControlsEditingActivity$currentUserTracker$1 currentUserTracker;
    @NotNull
    public final CustomIconCache customIconCache;
    @NotNull
    public final ControlsEditingActivity$favoritesModelCallback$1 favoritesModelCallback = new ControlsEditingActivity$favoritesModelCallback$1(this);
    public FavoritesModel model;
    public View saveButton;
    public CharSequence structure;
    public TextView subtitle;
    @NotNull
    public final ControlsUiController uiController;

    public ControlsEditingActivity(@NotNull ControlsControllerImpl controlsControllerImpl, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull CustomIconCache customIconCache2, @NotNull ControlsUiController controlsUiController) {
        this.controller = controlsControllerImpl;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.customIconCache = customIconCache2;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsEditingActivity$currentUserTracker$1(this, broadcastDispatcher2);
    }

    /* compiled from: ControlsEditingActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        Unit unit;
        super.onCreate(bundle);
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        Unit unit2 = null;
        if (componentName == null) {
            unit = null;
        } else {
            this.component = componentName;
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            finish();
        }
        CharSequence charSequenceExtra = getIntent().getCharSequenceExtra("extra_structure");
        if (charSequenceExtra != null) {
            this.structure = charSequenceExtra;
            unit2 = Unit.INSTANCE;
        }
        if (unit2 == null) {
            finish();
        }
        bindViews();
        bindButtons();
    }

    public void onStart() {
        super.onStart();
        setUpList();
        this.currentUserTracker.startTracking();
    }

    public void onStop() {
        super.onStop();
        this.currentUserTracker.stopTracking();
    }

    public void onBackPressed() {
        animateExitAndFinish();
    }

    public final void animateExitAndFinish() {
        ControlsAnimations.exitAnimation((ViewGroup) requireViewById(R$id.controls_management_root), new ControlsEditingActivity$animateExitAndFinish$1(this)).start();
    }

    public final void bindViews() {
        setContentView(R$layout.controls_management);
        getLifecycle().addObserver(ControlsAnimations.INSTANCE.observerForAnimations((ViewGroup) requireViewById(R$id.controls_management_root), getWindow(), getIntent()));
        ViewStub viewStub = (ViewStub) requireViewById(R$id.stub);
        viewStub.setLayoutResource(R$layout.controls_management_editing);
        viewStub.inflate();
        TextView textView = (TextView) requireViewById(R$id.title);
        CharSequence charSequence = this.structure;
        CharSequence charSequence2 = null;
        if (charSequence == null) {
            charSequence = null;
        }
        textView.setText(charSequence);
        CharSequence charSequence3 = this.structure;
        if (charSequence3 != null) {
            charSequence2 = charSequence3;
        }
        setTitle(charSequence2);
        TextView textView2 = (TextView) requireViewById(R$id.subtitle);
        textView2.setText(SUBTITLE_ID);
        this.subtitle = textView2;
    }

    public final void bindButtons() {
        View requireViewById = requireViewById(R$id.done);
        Button button = (Button) requireViewById;
        button.setEnabled(false);
        button.setText(R$string.save);
        button.setOnClickListener(new ControlsEditingActivity$bindButtons$1$1(this));
        this.saveButton = requireViewById;
    }

    public final void saveFavorites() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        FavoritesModel favoritesModel = null;
        if (componentName == null) {
            componentName = null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            charSequence = null;
        }
        FavoritesModel favoritesModel2 = this.model;
        if (favoritesModel2 != null) {
            favoritesModel = favoritesModel2;
        }
        controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName, charSequence, favoritesModel.getFavorites()));
    }

    public final void setUpList() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        FavoritesModel favoritesModel = null;
        if (componentName == null) {
            componentName = null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            charSequence = null;
        }
        List<ControlInfo> favoritesForStructure = controlsControllerImpl.getFavoritesForStructure(componentName, charSequence);
        CustomIconCache customIconCache2 = this.customIconCache;
        ComponentName componentName2 = this.component;
        if (componentName2 == null) {
            componentName2 = null;
        }
        this.model = new FavoritesModel(customIconCache2, componentName2, favoritesForStructure, this.favoritesModelCallback);
        float f = getResources().getFloat(R$dimen.control_card_elevation);
        RecyclerView recyclerView = (RecyclerView) requireViewById(R$id.list);
        recyclerView.setAlpha(0.0f);
        ControlAdapter controlAdapter = new ControlAdapter(f);
        controlAdapter.registerAdapterDataObserver(new ControlsEditingActivity$setUpList$adapter$1$1(recyclerView));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.controls_card_margin);
        MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
        int findMaxColumns = ControlAdapter.Companion.findMaxColumns(getResources());
        recyclerView.setAdapter(controlAdapter);
        ControlsEditingActivity$setUpList$1$1 controlsEditingActivity$setUpList$1$1 = new ControlsEditingActivity$setUpList$1$1(findMaxColumns, recyclerView.getContext());
        controlsEditingActivity$setUpList$1$1.setSpanSizeLookup(new ControlsEditingActivity$setUpList$1$2$1(controlAdapter, findMaxColumns));
        recyclerView.setLayoutManager(controlsEditingActivity$setUpList$1$1);
        recyclerView.addItemDecoration(marginItemDecorator);
        FavoritesModel favoritesModel2 = this.model;
        if (favoritesModel2 == null) {
            favoritesModel2 = null;
        }
        controlAdapter.changeModel(favoritesModel2);
        FavoritesModel favoritesModel3 = this.model;
        if (favoritesModel3 == null) {
            favoritesModel3 = null;
        }
        favoritesModel3.attachAdapter(controlAdapter);
        FavoritesModel favoritesModel4 = this.model;
        if (favoritesModel4 != null) {
            favoritesModel = favoritesModel4;
        }
        new ItemTouchHelper(favoritesModel.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
    }

    public void onDestroy() {
        this.currentUserTracker.stopTracking();
        super.onDestroy();
    }
}
