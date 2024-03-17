package com.android.systemui.flags;

import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.annotations.Keep;
import com.android.systemui.R$bool;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flags {
    public static final ResourceBooleanFlag BOUNCER_USER_SWITCHER = new ResourceBooleanFlag(204, R$bool.config_enableBouncerUserSwitcher);
    public static final ResourceBooleanFlag CHARGING_RIPPLE = new ResourceBooleanFlag(203, R$bool.flag_charging_ripple);
    public static final BooleanFlag COMBINED_QS_HEADERS = new BooleanFlag(501, false);
    public static final BooleanFlag COMBINED_STATUS_BAR_SIGNAL_ICONS = new BooleanFlag(601, false);
    public static final ResourceBooleanFlag FULL_SCREEN_USER_SWITCHER = new ResourceBooleanFlag(506, R$bool.config_enableFullscreenUserSwitcher);
    public static final BooleanFlag LOCKSCREEN_ANIMATIONS = new BooleanFlag(201, true);
    public static final BooleanFlag MEDIA_MUTE_AWAIT = new BooleanFlag(904, true);
    public static final BooleanFlag MEDIA_NEARBY_DEVICES = new BooleanFlag(903, true);
    public static final BooleanFlag MEDIA_SESSION_ACTIONS = new BooleanFlag(901, false);
    public static final BooleanFlag MEDIA_TAP_TO_TRANSFER = new BooleanFlag(900, false);
    public static final ResourceBooleanFlag MONET = new ResourceBooleanFlag(800, R$bool.flag_monet);
    @Deprecated
    public static final BooleanFlag NEW_FOOTER = new BooleanFlag(504, true);
    public static final BooleanFlag NEW_HEADER = new BooleanFlag(505, false);
    public static final BooleanFlag NEW_NOTIFICATION_PIPELINE_RENDERING = new BooleanFlag((int) R$styleable.Constraint_layout_goneMarginRight, true);
    public static final BooleanFlag NEW_PIPELINE_CRASH_ON_CALL_TO_OLD_PIPELINE = new BooleanFlag((int) R$styleable.Constraint_progress, false);
    public static final BooleanFlag NEW_UNLOCK_SWIPE_ANIMATION = new BooleanFlag(202, true);
    @Deprecated
    public static final BooleanFlag NEW_USER_SWITCHER = new BooleanFlag(500, true);
    public static final ResourceBooleanFlag NOTIFICATION_DRAG_TO_CONTENTS = new ResourceBooleanFlag(R$styleable.Constraint_transitionEasing, R$bool.config_notificationToContents);
    public static final BooleanFlag NOTIFICATION_PIPELINE_DEVELOPER_LOGGING = new BooleanFlag((int) R$styleable.Constraint_layout_goneMarginTop, false);
    public static final BooleanFlag NSSL_DEBUG_LINES = new BooleanFlag((int) R$styleable.Constraint_pathMotionArc, false);
    public static final BooleanFlag NSSL_DEBUG_REMOVE_ANIMATION = new BooleanFlag(106, false);
    public static final BooleanFlag ONGOING_CALL_IN_IMMERSIVE = new BooleanFlag(701, true);
    public static final BooleanFlag ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP = new BooleanFlag(702, true);
    public static final BooleanFlag ONGOING_CALL_STATUS_BAR_CHIP = new BooleanFlag(700, true);
    public static final ResourceBooleanFlag PEOPLE_TILE = new ResourceBooleanFlag(502, R$bool.flag_conversations);
    public static final BooleanFlag POWER_MENU_LITE = new BooleanFlag(300, true);
    public static final ResourceBooleanFlag QS_USER_DETAIL_SHORTCUT = new ResourceBooleanFlag(503, R$bool.flag_lockscreen_qs_user_detail_shortcut);
    public static final BooleanFlag SIMULATE_DOCK_THROUGH_CHARGING = new BooleanFlag(1000, true);
    public static final ResourceBooleanFlag SMARTSPACE = new ResourceBooleanFlag(402, R$bool.flag_smartspace);
    public static final BooleanFlag SMARTSPACE_DEDUPING = new BooleanFlag(400, true);
    public static final BooleanFlag SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED = new BooleanFlag(401, true);
    public static final ResourceBooleanFlag STATUS_BAR_USER_SWITCHER = new ResourceBooleanFlag(602, R$bool.flag_user_switcher_chip);
    public static final BooleanFlag TEAMFOOD = new BooleanFlag(1, false);
    @Keep
    public static final SysPropBooleanFlag WM_ALWAYS_ENFORCE_PREDICTIVE_BACK = new SysPropBooleanFlag(1202, "persist.wm.debug.predictive_back_always_enforce", false);
    @Keep
    public static final SysPropBooleanFlag WM_ENABLE_PREDICTIVE_BACK = new SysPropBooleanFlag(1200, "persist.wm.debug.predictive_back", true);
    @Keep
    public static final SysPropBooleanFlag WM_ENABLE_PREDICTIVE_BACK_ANIM = new SysPropBooleanFlag(1201, "persist.wm.debug.predictive_back_anim", false);
    @Keep
    public static final SysPropBooleanFlag WM_ENABLE_SHELL_TRANSITIONS = new SysPropBooleanFlag(1100, "persist.wm.debug.shell_transit", false);
    public static Map<Integer, Flag<?>> sFlagMap;

    public static Map<Integer, Flag<?>> collectFlags() {
        Map<Integer, Flag<?>> map = sFlagMap;
        if (map != null) {
            return map;
        }
        HashMap hashMap = new HashMap();
        for (Field field : getFlagFields()) {
            try {
                Flag flag = (Flag) field.get((Object) null);
                hashMap.put(Integer.valueOf(flag.getId()), flag);
            } catch (IllegalAccessException unused) {
            }
        }
        sFlagMap = hashMap;
        return hashMap;
    }

    public static List<Field> getFlagFields() {
        Field[] fields = Flags.class.getFields();
        ArrayList arrayList = new ArrayList();
        for (Field field : fields) {
            if (Flag.class.isAssignableFrom(field.getType())) {
                arrayList.add(field);
            }
        }
        return arrayList;
    }
}
