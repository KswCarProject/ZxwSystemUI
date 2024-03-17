package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: StructureAdapter.kt */
public final class StructureAdapter extends RecyclerView.Adapter<StructureHolder> {
    @NotNull
    public final List<StructureContainer> models;

    public StructureAdapter(@NotNull List<StructureContainer> list) {
        this.models = list;
    }

    @NotNull
    public StructureHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new StructureHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.controls_structure_page, viewGroup, false));
    }

    public int getItemCount() {
        return this.models.size();
    }

    public void onBindViewHolder(@NotNull StructureHolder structureHolder, int i) {
        structureHolder.bind(this.models.get(i).getModel());
    }

    /* compiled from: StructureAdapter.kt */
    public static final class StructureHolder extends RecyclerView.ViewHolder {
        @NotNull
        public final ControlAdapter controlAdapter = new ControlAdapter(this.itemView.getContext().getResources().getFloat(R$dimen.control_card_elevation));
        @NotNull
        public final RecyclerView recyclerView = ((RecyclerView) this.itemView.requireViewById(R$id.listAll));

        public StructureHolder(@NotNull View view) {
            super(view);
            setUpRecyclerView();
        }

        public final void bind(@NotNull ControlsModel controlsModel) {
            this.controlAdapter.changeModel(controlsModel);
        }

        public final void setUpRecyclerView() {
            int dimensionPixelSize = this.itemView.getContext().getResources().getDimensionPixelSize(R$dimen.controls_card_margin);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            int findMaxColumns = ControlAdapter.Companion.findMaxColumns(this.itemView.getResources());
            RecyclerView recyclerView2 = this.recyclerView;
            recyclerView2.setAdapter(this.controlAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.recyclerView.getContext(), findMaxColumns);
            gridLayoutManager.setSpanSizeLookup(new StructureAdapter$StructureHolder$setUpRecyclerView$1$1$1(recyclerView2, findMaxColumns));
            recyclerView2.setLayoutManager(gridLayoutManager);
            recyclerView2.addItemDecoration(marginItemDecorator);
        }
    }
}
