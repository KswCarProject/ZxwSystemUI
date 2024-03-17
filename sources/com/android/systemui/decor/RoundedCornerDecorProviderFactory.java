package com.android.systemui.decor;

import com.android.systemui.R$id;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: RoundedCornerDecorProviderFactory.kt */
public final class RoundedCornerDecorProviderFactory extends DecorProviderFactory {
    @NotNull
    public final RoundedCornerResDelegate roundedCornerResDelegate;

    public RoundedCornerDecorProviderFactory(@NotNull RoundedCornerResDelegate roundedCornerResDelegate2) {
        this.roundedCornerResDelegate = roundedCornerResDelegate2;
    }

    public boolean getHasProviders() {
        RoundedCornerResDelegate roundedCornerResDelegate2 = this.roundedCornerResDelegate;
        return roundedCornerResDelegate2.getHasTop() || roundedCornerResDelegate2.getHasBottom();
    }

    @NotNull
    public List<DecorProvider> getProviders() {
        boolean hasTop = this.roundedCornerResDelegate.getHasTop();
        boolean hasBottom = this.roundedCornerResDelegate.getHasBottom();
        if (hasTop && hasBottom) {
            return CollectionsKt__CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(R$id.rounded_corner_top_left, 1, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(R$id.rounded_corner_top_right, 1, 2, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(R$id.rounded_corner_bottom_left, 3, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(R$id.rounded_corner_bottom_right, 3, 2, this.roundedCornerResDelegate));
        } else if (hasTop) {
            return CollectionsKt__CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(R$id.rounded_corner_top_left, 1, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(R$id.rounded_corner_top_right, 1, 2, this.roundedCornerResDelegate));
        } else if (!hasBottom) {
            return CollectionsKt__CollectionsKt.emptyList();
        } else {
            return CollectionsKt__CollectionsKt.listOf(new RoundedCornerDecorProviderImpl(R$id.rounded_corner_bottom_left, 3, 0, this.roundedCornerResDelegate), new RoundedCornerDecorProviderImpl(R$id.rounded_corner_bottom_right, 3, 2, this.roundedCornerResDelegate));
        }
    }
}
