package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlsServiceInfo;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

/* compiled from: AppAdapter.kt */
public final class AppAdapter extends RecyclerView.Adapter<Holder> {
    @NotNull
    public final AppAdapter$callback$1 callback;
    @NotNull
    public final FavoritesRenderer favoritesRenderer;
    @NotNull
    public final LayoutInflater layoutInflater;
    @NotNull
    public List<ControlsServiceInfo> listOfServices = CollectionsKt__CollectionsKt.emptyList();
    @NotNull
    public final Function1<ComponentName, Unit> onAppSelected;
    @NotNull
    public final Resources resources;

    public AppAdapter(@NotNull Executor executor, @NotNull Executor executor2, @NotNull Lifecycle lifecycle, @NotNull ControlsListingController controlsListingController, @NotNull LayoutInflater layoutInflater2, @NotNull Function1<? super ComponentName, Unit> function1, @NotNull FavoritesRenderer favoritesRenderer2, @NotNull Resources resources2) {
        this.layoutInflater = layoutInflater2;
        this.onAppSelected = function1;
        this.favoritesRenderer = favoritesRenderer2;
        this.resources = resources2;
        AppAdapter$callback$1 appAdapter$callback$1 = new AppAdapter$callback$1(executor, this, executor2);
        this.callback = appAdapter$callback$1;
        controlsListingController.observe(lifecycle, appAdapter$callback$1);
    }

    @NotNull
    public Holder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new Holder(this.layoutInflater.inflate(R$layout.controls_app_item, viewGroup, false), this.favoritesRenderer);
    }

    public int getItemCount() {
        return this.listOfServices.size();
    }

    public void onBindViewHolder(@NotNull Holder holder, int i) {
        holder.bindData(this.listOfServices.get(i));
        holder.itemView.setOnClickListener(new AppAdapter$onBindViewHolder$1(this, i));
    }

    /* compiled from: AppAdapter.kt */
    public static final class Holder extends RecyclerView.ViewHolder {
        @NotNull
        public final FavoritesRenderer favRenderer;
        @NotNull
        public final TextView favorites = ((TextView) this.itemView.requireViewById(R$id.favorites));
        @NotNull
        public final ImageView icon = ((ImageView) this.itemView.requireViewById(16908294));
        @NotNull
        public final TextView title = ((TextView) this.itemView.requireViewById(16908310));

        public Holder(@NotNull View view, @NotNull FavoritesRenderer favoritesRenderer) {
            super(view);
            this.favRenderer = favoritesRenderer;
        }

        public final void bindData(@NotNull ControlsServiceInfo controlsServiceInfo) {
            this.icon.setImageDrawable(controlsServiceInfo.loadIcon());
            this.title.setText(controlsServiceInfo.loadLabel());
            String renderFavoritesForComponent = this.favRenderer.renderFavoritesForComponent(controlsServiceInfo.componentName);
            this.favorites.setText(renderFavoritesForComponent);
            this.favorites.setVisibility(renderFavoritesForComponent == null ? 8 : 0);
        }
    }
}
