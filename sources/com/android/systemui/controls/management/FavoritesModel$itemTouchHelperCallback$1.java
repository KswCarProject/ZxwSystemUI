package com.android.systemui.controls.management;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

/* compiled from: FavoritesModel.kt */
public final class FavoritesModel$itemTouchHelperCallback$1 extends ItemTouchHelper.SimpleCallback {
    public final int MOVEMENT = 15;
    public final /* synthetic */ FavoritesModel this$0;

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FavoritesModel$itemTouchHelperCallback$1(FavoritesModel favoritesModel) {
        super(0, 0);
        this.this$0 = favoritesModel;
    }

    public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder viewHolder2) {
        this.this$0.onMoveItem(viewHolder.getBindingAdapterPosition(), viewHolder2.getBindingAdapterPosition());
        return true;
    }

    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getBindingAdapterPosition() < this.this$0.dividerPosition) {
            return ItemTouchHelper.Callback.makeMovementFlags(this.MOVEMENT, 0);
        }
        return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
    }

    public boolean canDropOver(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder viewHolder2) {
        return viewHolder2.getBindingAdapterPosition() < this.this$0.dividerPosition;
    }
}
