package com.android.systemui.qs.customize;

import android.os.Bundle;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.qs.customize.TileAdapter;
import java.util.List;

public class TileAdapterDelegate extends AccessibilityDelegateCompat {
    public static final int ADD_TO_POSITION_ID = R$id.accessibility_action_qs_add_to_position;
    public static final int MOVE_TO_POSITION_ID = R$id.accessibility_action_qs_move_to_position;

    public final TileAdapter.Holder getHolder(View view) {
        return (TileAdapter.Holder) view.getTag();
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        TileAdapter.Holder holder = getHolder(view);
        accessibilityNodeInfoCompat.setCollectionItemInfo((Object) null);
        accessibilityNodeInfoCompat.setStateDescription("");
        if (holder != null && holder.canTakeAccessibleAction()) {
            addClickAction(view, accessibilityNodeInfoCompat, holder);
            maybeAddActionAddToPosition(view, accessibilityNodeInfoCompat, holder);
            maybeAddActionMoveToPosition(view, accessibilityNodeInfoCompat, holder);
            if (holder.isCurrentTile()) {
                accessibilityNodeInfoCompat.setStateDescription(view.getContext().getString(R$string.accessibility_qs_edit_position, new Object[]{Integer.valueOf(holder.getLayoutPosition())}));
            }
        }
    }

    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        TileAdapter.Holder holder = getHolder(view);
        if (holder == null || !holder.canTakeAccessibleAction()) {
            return super.performAccessibilityAction(view, i, bundle);
        }
        if (i == 16) {
            holder.toggleState();
            return true;
        } else if (i == MOVE_TO_POSITION_ID) {
            holder.startAccessibleMove();
            return true;
        } else if (i != ADD_TO_POSITION_ID) {
            return super.performAccessibilityAction(view, i, bundle);
        } else {
            holder.startAccessibleAdd();
            return true;
        }
    }

    public final void addClickAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, TileAdapter.Holder holder) {
        String str;
        if (holder.canAdd()) {
            str = view.getContext().getString(R$string.accessibility_qs_edit_tile_add_action);
        } else if (holder.canRemove()) {
            str = view.getContext().getString(R$string.accessibility_qs_edit_remove_tile_action);
        } else {
            List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = accessibilityNodeInfoCompat.getActionList();
            int size = actionList.size();
            for (int i = 0; i < size; i++) {
                if (actionList.get(i).getId() == 16) {
                    accessibilityNodeInfoCompat.removeAction(actionList.get(i));
                }
            }
            return;
        }
        accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, str));
    }

    public final void maybeAddActionMoveToPosition(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, TileAdapter.Holder holder) {
        if (holder.isCurrentTile()) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_TO_POSITION_ID, view.getContext().getString(R$string.accessibility_qs_edit_tile_start_move)));
        }
    }

    public final void maybeAddActionAddToPosition(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, TileAdapter.Holder holder) {
        if (holder.canAdd()) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(ADD_TO_POSITION_ID, view.getContext().getString(R$string.accessibility_qs_edit_tile_start_add)));
        }
    }
}
