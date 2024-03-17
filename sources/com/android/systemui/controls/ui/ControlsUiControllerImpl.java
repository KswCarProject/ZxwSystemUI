package com.android.systemui.controls.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Space;
import android.widget.TextView;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlAdapter;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl implements ControlsUiController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final ComponentName EMPTY_COMPONENT;
    @NotNull
    public static final StructureInfo EMPTY_STRUCTURE;
    public Context activityContext;
    @NotNull
    public final ActivityStarter activityStarter;
    public List<StructureInfo> allStructures;
    @NotNull
    public final DelayableExecutor bgExecutor;
    public final Collator collator;
    @NotNull
    public final Context context;
    @NotNull
    public final ControlActionCoordinator controlActionCoordinator;
    @NotNull
    public final Map<ControlKey, ControlViewHolder> controlViewsById = new LinkedHashMap();
    @NotNull
    public final Map<ControlKey, ControlWithState> controlsById = new LinkedHashMap();
    @NotNull
    public final Lazy<ControlsController> controlsController;
    @NotNull
    public final Lazy<ControlsListingController> controlsListingController;
    @NotNull
    public final ControlsMetricsLogger controlsMetricsLogger;
    public boolean hidden = true;
    @NotNull
    public final CustomIconCache iconCache;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    public ControlsListingController.ControlsListingCallback listingCallback;
    @NotNull
    public final Comparator<SelectionItem> localeComparator;
    public Runnable onDismiss;
    @NotNull
    public final Consumer<Boolean> onSeedingComplete;
    public ViewGroup parent;
    @Nullable
    public ListPopupWindow popup;
    @NotNull
    public final ContextThemeWrapper popupThemedContext;
    public boolean retainCache;
    @NotNull
    public StructureInfo selectedStructure = EMPTY_STRUCTURE;
    @NotNull
    public final ShadeController shadeController;
    @NotNull
    public final SharedPreferences sharedPreferences;
    @NotNull
    public final DelayableExecutor uiExecutor;

    public ControlsUiControllerImpl(@NotNull Lazy<ControlsController> lazy, @NotNull Context context2, @NotNull DelayableExecutor delayableExecutor, @NotNull DelayableExecutor delayableExecutor2, @NotNull Lazy<ControlsListingController> lazy2, @NotNull SharedPreferences sharedPreferences2, @NotNull ControlActionCoordinator controlActionCoordinator2, @NotNull ActivityStarter activityStarter2, @NotNull ShadeController shadeController2, @NotNull CustomIconCache customIconCache, @NotNull ControlsMetricsLogger controlsMetricsLogger2, @NotNull KeyguardStateController keyguardStateController2) {
        this.controlsController = lazy;
        this.context = context2;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlsListingController = lazy2;
        this.sharedPreferences = sharedPreferences2;
        this.controlActionCoordinator = controlActionCoordinator2;
        this.activityStarter = activityStarter2;
        this.shadeController = shadeController2;
        this.iconCache = customIconCache;
        this.controlsMetricsLogger = controlsMetricsLogger2;
        this.keyguardStateController = keyguardStateController2;
        this.popupThemedContext = new ContextThemeWrapper(context2, R$style.Control_ListPopupWindow);
        Collator instance = Collator.getInstance(context2.getResources().getConfiguration().getLocales().get(0));
        this.collator = instance;
        this.localeComparator = new ControlsUiControllerImpl$special$$inlined$compareBy$1(instance);
        this.onSeedingComplete = new ControlsUiControllerImpl$onSeedingComplete$1(this);
    }

    @NotNull
    public final Lazy<ControlsController> getControlsController() {
        return this.controlsController;
    }

    @NotNull
    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    @NotNull
    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    @NotNull
    public final Lazy<ControlsListingController> getControlsListingController() {
        return this.controlsListingController;
    }

    @NotNull
    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    /* compiled from: ControlsUiControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    static {
        ComponentName componentName = new ComponentName("", "");
        EMPTY_COMPONENT = componentName;
        EMPTY_STRUCTURE = new StructureInfo(componentName, "", new ArrayList());
    }

    public final ControlsListingController.ControlsListingCallback createCallback(Function1<? super List<SelectionItem>, Unit> function1) {
        return new ControlsUiControllerImpl$createCallback$1(this, function1);
    }

    public void show(@NotNull ViewGroup viewGroup, @NotNull Runnable runnable, @NotNull Context context2) {
        Log.d("ControlsUiController", "show()");
        this.parent = viewGroup;
        this.onDismiss = runnable;
        this.activityContext = context2;
        this.hidden = false;
        this.retainCache = false;
        this.controlActionCoordinator.setActivityContext(context2);
        List<StructureInfo> favorites = this.controlsController.get().getFavorites();
        this.allStructures = favorites;
        ControlsListingController.ControlsListingCallback controlsListingCallback = null;
        if (favorites == null) {
            favorites = null;
        }
        this.selectedStructure = getPreferredStructure(favorites);
        if (this.controlsController.get().addSeedingFavoritesCallback(this.onSeedingComplete)) {
            this.listingCallback = createCallback(new ControlsUiControllerImpl$show$1(this));
        } else {
            if (this.selectedStructure.getControls().isEmpty()) {
                List<StructureInfo> list = this.allStructures;
                if (list == null) {
                    list = null;
                }
                if (list.size() <= 1) {
                    this.listingCallback = createCallback(new ControlsUiControllerImpl$show$2(this));
                }
            }
            Iterable<ControlInfo> controls = this.selectedStructure.getControls();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10));
            for (ControlInfo controlWithState : controls) {
                arrayList.add(new ControlWithState(this.selectedStructure.getComponentName(), controlWithState, (Control) null));
            }
            Map<ControlKey, ControlWithState> map = this.controlsById;
            for (Object next : arrayList) {
                map.put(new ControlKey(this.selectedStructure.getComponentName(), ((ControlWithState) next).getCi().getControlId()), next);
            }
            this.listingCallback = createCallback(new ControlsUiControllerImpl$show$5(this));
            this.controlsController.get().subscribeToFavorites(this.selectedStructure);
        }
        ControlsListingController controlsListingController2 = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback2 = this.listingCallback;
        if (controlsListingCallback2 != null) {
            controlsListingCallback = controlsListingCallback2;
        }
        controlsListingController2.addCallback(controlsListingCallback);
    }

    public final void reload(ViewGroup viewGroup) {
        if (!this.hidden) {
            ControlsListingController controlsListingController2 = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback == null) {
                controlsListingCallback = null;
            }
            controlsListingController2.removeCallback(controlsListingCallback);
            this.controlsController.get().unsubscribe();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewGroup, "alpha", new float[]{1.0f, 0.0f});
            ofFloat.setInterpolator(new AccelerateInterpolator(1.0f));
            ofFloat.setDuration(200);
            ofFloat.addListener(new ControlsUiControllerImpl$reload$1(this, viewGroup));
            ofFloat.start();
        }
    }

    public final void showSeedingView(List<SelectionItem> list) {
        LayoutInflater from = LayoutInflater.from(this.context);
        int i = R$layout.controls_no_favorites;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        from.inflate(i, viewGroup, true);
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 != null) {
            viewGroup2 = viewGroup3;
        }
        ((TextView) viewGroup2.requireViewById(R$id.controls_subtitle)).setText(this.context.getResources().getString(R$string.controls_seeding_in_progress));
    }

    public final void showInitialSetupView(List<SelectionItem> list) {
        startProviderSelectorActivity();
        Runnable runnable = this.onDismiss;
        if (runnable == null) {
            runnable = null;
        }
        runnable.run();
    }

    public final void startFavoritingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsFavoritingActivity.class);
    }

    public final void startEditingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsEditingActivity.class);
    }

    public final void startTargetedActivity(StructureInfo structureInfo, Class<?> cls) {
        Context context2 = this.activityContext;
        if (context2 == null) {
            context2 = null;
        }
        Intent intent = new Intent(context2, cls);
        putIntentExtras(intent, structureInfo);
        startActivity(intent);
        this.retainCache = true;
    }

    public final void putIntentExtras(Intent intent, StructureInfo structureInfo) {
        intent.putExtra("extra_app_label", getControlsListingController().get().getAppLabel(structureInfo.getComponentName()));
        intent.putExtra("extra_structure", structureInfo.getStructure());
        intent.putExtra("android.intent.extra.COMPONENT_NAME", structureInfo.getComponentName());
    }

    public final void startProviderSelectorActivity() {
        Context context2 = this.activityContext;
        if (context2 == null) {
            context2 = null;
        }
        Intent intent = new Intent(context2, ControlsProviderSelectorActivity.class);
        intent.putExtra("back_should_exit", true);
        startActivity(intent);
    }

    public final void startActivity(Intent intent) {
        intent.putExtra("extra_animate", true);
        if (this.keyguardStateController.isShowing()) {
            this.activityStarter.postStartActivityDismissingKeyguard(intent, 0);
            return;
        }
        Context context2 = this.activityContext;
        Activity activity = null;
        if (context2 == null) {
            context2 = null;
        }
        Context context3 = this.activityContext;
        if (context3 != null) {
            activity = context3;
        }
        context2.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, new Pair[0]).toBundle());
    }

    public final void showControlsView(List<SelectionItem> list) {
        this.controlViewsById.clear();
        Iterable iterable = list;
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10)), 16));
        for (Object next : iterable) {
            linkedHashMap.put(((SelectionItem) next).getComponentName(), next);
        }
        ArrayList arrayList = new ArrayList();
        List<StructureInfo> list2 = this.allStructures;
        if (list2 == null) {
            list2 = null;
        }
        for (StructureInfo structureInfo : list2) {
            SelectionItem selectionItem = (SelectionItem) linkedHashMap.get(structureInfo.getComponentName());
            SelectionItem copy$default = selectionItem == null ? null : SelectionItem.copy$default(selectionItem, (CharSequence) null, structureInfo.getStructure(), (Drawable) null, (ComponentName) null, 0, 29, (Object) null);
            if (copy$default != null) {
                arrayList.add(copy$default);
            }
        }
        CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList, this.localeComparator);
        SelectionItem findSelectionItem = findSelectionItem(this.selectedStructure, arrayList);
        if (findSelectionItem == null) {
            findSelectionItem = list.get(0);
        }
        this.controlsMetricsLogger.refreshBegin(findSelectionItem.getUid(), !this.keyguardStateController.isUnlocked());
        createListView(findSelectionItem);
        createDropDown(arrayList, findSelectionItem);
        createMenu();
    }

    public final void createMenu() {
        String[] strArr = {this.context.getResources().getString(R$string.controls_menu_add), this.context.getResources().getString(R$string.controls_menu_edit)};
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        ref$ObjectRef.element = new ArrayAdapter(this.context, R$layout.controls_more_item, strArr);
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            viewGroup = null;
        }
        ImageView imageView = (ImageView) viewGroup.requireViewById(R$id.controls_more);
        imageView.setOnClickListener(new ControlsUiControllerImpl$createMenu$1(this, imageView, ref$ObjectRef));
    }

    public final void createDropDown(List<SelectionItem> list, SelectionItem selectionItem) {
        for (SelectionItem selectionItem2 : list) {
            RenderInfo.Companion.registerComponentIcon(selectionItem2.getComponentName(), selectionItem2.getIcon());
        }
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        T itemAdapter = new ItemAdapter(this.context, R$layout.controls_spinner_item);
        itemAdapter.addAll(list);
        ref$ObjectRef.element = itemAdapter;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        TextView textView = (TextView) viewGroup.requireViewById(R$id.app_or_structure_spinner);
        textView.setText(selectionItem.getTitle());
        Drawable background = textView.getBackground();
        if (background != null) {
            ((LayerDrawable) background).getDrawable(0).setTint(textView.getContext().getResources().getColor(R$color.control_spinner_dropdown, (Resources.Theme) null));
            if (list.size() == 1) {
                textView.setBackground((Drawable) null);
                return;
            }
            ViewGroup viewGroup3 = this.parent;
            if (viewGroup3 != null) {
                viewGroup2 = viewGroup3;
            }
            ViewGroup viewGroup4 = (ViewGroup) viewGroup2.requireViewById(R$id.controls_header);
            viewGroup4.setOnClickListener(new ControlsUiControllerImpl$createDropDown$2(this, viewGroup4, ref$ObjectRef));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public final void createListView(SelectionItem selectionItem) {
        LayoutInflater from = LayoutInflater.from(this.context);
        int i = R$layout.controls_with_favorites;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        from.inflate(i, viewGroup, true);
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            viewGroup3 = null;
        }
        ImageView imageView = (ImageView) viewGroup3.requireViewById(R$id.controls_close);
        imageView.setOnClickListener(new ControlsUiControllerImpl$createListView$1$1(this));
        imageView.setVisibility(0);
        ControlAdapter.Companion companion = ControlAdapter.Companion;
        Context context2 = this.activityContext;
        if (context2 == null) {
            context2 = null;
        }
        int findMaxColumns = companion.findMaxColumns(context2.getResources());
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 != null) {
            viewGroup2 = viewGroup4;
        }
        View requireViewById = viewGroup2.requireViewById(R$id.global_actions_controls_list);
        if (requireViewById != null) {
            ViewGroup viewGroup5 = (ViewGroup) requireViewById;
            ViewGroup createRow = createRow(from, viewGroup5);
            for (ControlInfo controlId : this.selectedStructure.getControls()) {
                ControlKey controlKey = new ControlKey(this.selectedStructure.getComponentName(), controlId.getControlId());
                ControlWithState controlWithState = this.controlsById.get(controlKey);
                if (controlWithState != null) {
                    if (createRow.getChildCount() == findMaxColumns) {
                        createRow = createRow(from, viewGroup5);
                    }
                    View inflate = from.inflate(R$layout.controls_base_item, createRow, false);
                    if (inflate != null) {
                        ViewGroup viewGroup6 = (ViewGroup) inflate;
                        createRow.addView(viewGroup6);
                        if (createRow.getChildCount() == 1) {
                            ViewGroup.LayoutParams layoutParams = viewGroup6.getLayoutParams();
                            if (layoutParams != null) {
                                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
                            } else {
                                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                            }
                        }
                        ControlViewHolder controlViewHolder = new ControlViewHolder(viewGroup6, getControlsController().get(), getUiExecutor(), getBgExecutor(), getControlActionCoordinator(), this.controlsMetricsLogger, selectionItem.getUid());
                        controlViewHolder.bindData(controlWithState, false);
                        this.controlViewsById.put(controlKey, controlViewHolder);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
                    }
                }
            }
            int size = this.selectedStructure.getControls().size() % findMaxColumns;
            int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R$dimen.control_spacing);
            for (int i2 = size == 0 ? 0 : findMaxColumns - size; i2 > 0; i2--) {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0, 1.0f);
                layoutParams2.setMarginStart(dimensionPixelSize);
                createRow.addView(new Space(this.context), layoutParams2);
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX WARNING: Multi-variable type inference failed */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.systemui.controls.controller.StructureInfo getPreferredStructure(@org.jetbrains.annotations.NotNull java.util.List<com.android.systemui.controls.controller.StructureInfo> r8) {
        /*
            r7 = this;
            boolean r0 = r8.isEmpty()
            if (r0 == 0) goto L_0x0009
            com.android.systemui.controls.controller.StructureInfo r7 = EMPTY_STRUCTURE
            return r7
        L_0x0009:
            android.content.SharedPreferences r0 = r7.sharedPreferences
            java.lang.String r1 = "controls_component"
            r2 = 0
            java.lang.String r0 = r0.getString(r1, r2)
            if (r0 != 0) goto L_0x0016
            r0 = r2
            goto L_0x001a
        L_0x0016:
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r0)
        L_0x001a:
            if (r0 != 0) goto L_0x001e
            android.content.ComponentName r0 = EMPTY_COMPONENT
        L_0x001e:
            android.content.SharedPreferences r7 = r7.sharedPreferences
            java.lang.String r1 = "controls_structure"
            java.lang.String r3 = ""
            java.lang.String r7 = r7.getString(r1, r3)
            r1 = r8
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.Iterator r1 = r1.iterator()
        L_0x002f:
            boolean r3 = r1.hasNext()
            r4 = 0
            if (r3 == 0) goto L_0x0057
            java.lang.Object r3 = r1.next()
            r5 = r3
            com.android.systemui.controls.controller.StructureInfo r5 = (com.android.systemui.controls.controller.StructureInfo) r5
            android.content.ComponentName r6 = r5.getComponentName()
            boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r6)
            if (r6 == 0) goto L_0x0053
            java.lang.CharSequence r5 = r5.getStructure()
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r7, (java.lang.Object) r5)
            if (r5 == 0) goto L_0x0053
            r5 = 1
            goto L_0x0054
        L_0x0053:
            r5 = r4
        L_0x0054:
            if (r5 == 0) goto L_0x002f
            r2 = r3
        L_0x0057:
            com.android.systemui.controls.controller.StructureInfo r2 = (com.android.systemui.controls.controller.StructureInfo) r2
            if (r2 != 0) goto L_0x0062
            java.lang.Object r7 = r8.get(r4)
            r2 = r7
            com.android.systemui.controls.controller.StructureInfo r2 = (com.android.systemui.controls.controller.StructureInfo) r2
        L_0x0062:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ControlsUiControllerImpl.getPreferredStructure(java.util.List):com.android.systemui.controls.controller.StructureInfo");
    }

    public final void updatePreferences(StructureInfo structureInfo) {
        if (!Intrinsics.areEqual((Object) structureInfo, (Object) EMPTY_STRUCTURE)) {
            this.sharedPreferences.edit().putString("controls_component", structureInfo.getComponentName().flattenToString()).putString("controls_structure", structureInfo.getStructure().toString()).commit();
        }
    }

    public final void switchAppOrStructure(SelectionItem selectionItem) {
        boolean z;
        List<StructureInfo> list = this.allStructures;
        ViewGroup viewGroup = null;
        if (list == null) {
            list = null;
        }
        for (StructureInfo structureInfo : list) {
            if (!Intrinsics.areEqual((Object) structureInfo.getStructure(), (Object) selectionItem.getStructure()) || !Intrinsics.areEqual((Object) structureInfo.getComponentName(), (Object) selectionItem.getComponentName())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                if (!Intrinsics.areEqual((Object) structureInfo, (Object) this.selectedStructure)) {
                    this.selectedStructure = structureInfo;
                    updatePreferences(structureInfo);
                    ViewGroup viewGroup2 = this.parent;
                    if (viewGroup2 != null) {
                        viewGroup = viewGroup2;
                    }
                    reload(viewGroup);
                    return;
                }
                return;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    public void closeDialogs(boolean z) {
        if (z) {
            ListPopupWindow listPopupWindow = this.popup;
            if (listPopupWindow != null) {
                listPopupWindow.dismissImmediate();
            }
        } else {
            ListPopupWindow listPopupWindow2 = this.popup;
            if (listPopupWindow2 != null) {
                listPopupWindow2.dismiss();
            }
        }
        this.popup = null;
        for (Map.Entry<ControlKey, ControlViewHolder> value : this.controlViewsById.entrySet()) {
            ((ControlViewHolder) value.getValue()).dismiss();
        }
        this.controlActionCoordinator.closeDialogs();
    }

    public void hide() {
        this.hidden = true;
        closeDialogs(true);
        this.controlsController.get().unsubscribe();
        ViewGroup viewGroup = this.parent;
        ControlsListingController.ControlsListingCallback controlsListingCallback = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        viewGroup.removeAllViews();
        this.controlsById.clear();
        this.controlViewsById.clear();
        ControlsListingController controlsListingController2 = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback2 = this.listingCallback;
        if (controlsListingCallback2 != null) {
            controlsListingCallback = controlsListingCallback2;
        }
        controlsListingController2.removeCallback(controlsListingCallback);
        if (!this.retainCache) {
            RenderInfo.Companion.clearCache();
        }
    }

    public void onRefreshState(@NotNull ComponentName componentName, @NotNull List<Control> list) {
        boolean z = !this.keyguardStateController.isUnlocked();
        for (Control control : list) {
            ControlWithState controlWithState = this.controlsById.get(new ControlKey(componentName, control.getControlId()));
            if (controlWithState != null) {
                Log.d("ControlsUiController", Intrinsics.stringPlus("onRefreshState() for id: ", control.getControlId()));
                this.iconCache.store(componentName, control.getControlId(), control.getCustomIcon());
                ControlWithState controlWithState2 = new ControlWithState(componentName, controlWithState.getCi(), control);
                ControlKey controlKey = new ControlKey(componentName, control.getControlId());
                this.controlsById.put(controlKey, controlWithState2);
                ControlViewHolder controlViewHolder = this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    getUiExecutor().execute(new ControlsUiControllerImpl$onRefreshState$1$1$1$1(controlViewHolder, controlWithState2, z));
                }
            }
        }
    }

    public void onActionResponse(@NotNull ComponentName componentName, @NotNull String str, int i) {
        this.uiExecutor.execute(new ControlsUiControllerImpl$onActionResponse$1(this, new ControlKey(componentName, str), i));
    }

    public final ViewGroup createRow(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R$layout.controls_row, viewGroup, false);
        if (inflate != null) {
            ViewGroup viewGroup2 = (ViewGroup) inflate;
            viewGroup.addView(viewGroup2);
            return viewGroup2;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final SelectionItem findSelectionItem(StructureInfo structureInfo, List<SelectionItem> list) {
        Object obj;
        boolean z;
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            SelectionItem selectionItem = (SelectionItem) obj;
            if (!Intrinsics.areEqual((Object) selectionItem.getComponentName(), (Object) structureInfo.getComponentName()) || !Intrinsics.areEqual((Object) selectionItem.getStructure(), (Object) structureInfo.getStructure())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                break;
            }
        }
        return (SelectionItem) obj;
    }
}
