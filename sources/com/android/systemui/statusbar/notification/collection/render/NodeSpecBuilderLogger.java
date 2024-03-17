package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt___SetsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: NodeSpecBuilderLogger.kt */
public final class NodeSpecBuilderLogger {
    @NotNull
    public final LogBuffer buffer;

    public NodeSpecBuilderLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logBuildNodeSpec(@NotNull Set<NotifSection> set, @NotNull Map<NotifSection, ? extends NodeController> map, @NotNull Map<NotifSection, Integer> map2, @NotNull List<NotifSection> list) {
        String str;
        String str2;
        NotifSectioner sectioner;
        String nodeLabel;
        NotifSectioner sectioner2;
        String name;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NodeSpecBuilder", LogLevel.DEBUG, NodeSpecBuilderLogger$logBuildNodeSpec$2.INSTANCE);
        obtain.setInt1(list.size());
        logBuffer.commit(obtain);
        Iterator<NotifSection> it = list.iterator();
        while (true) {
            str = "(null)";
            if (!it.hasNext()) {
                break;
            }
            NotifSection next = it.next();
            LogBuffer logBuffer2 = this.buffer;
            LogMessageImpl obtain2 = logBuffer2.obtain("NodeSpecBuilder", LogLevel.DEBUG, NodeSpecBuilderLogger$logBuildNodeSpec$4.INSTANCE);
            if (!(next == null || (sectioner2 = next.getSectioner()) == null || (name = sectioner2.getName()) == null)) {
                str = name;
            }
            obtain2.setStr1(str);
            NodeController nodeController = (NodeController) map.get(next);
            String str3 = "(none)";
            if (!(nodeController == null || (nodeLabel = nodeController.getNodeLabel()) == null)) {
                str3 = nodeLabel;
            }
            obtain2.setStr2(str3);
            Integer num = map2.get(next);
            obtain2.setInt1(num == null ? -1 : num.intValue());
            logBuffer2.commit(obtain2);
        }
        for (T t : SetsKt___SetsKt.minus(set, CollectionsKt___CollectionsKt.toSet(list))) {
            LogBuffer logBuffer3 = this.buffer;
            LogMessageImpl obtain3 = logBuffer3.obtain("NodeSpecBuilder", LogLevel.DEBUG, NodeSpecBuilderLogger$logBuildNodeSpec$6.INSTANCE);
            if (t == null || (sectioner = t.getSectioner()) == null || (str2 = sectioner.getName()) == null) {
                str2 = str;
            }
            obtain3.setStr1(str2);
            logBuffer3.commit(obtain3);
        }
    }
}
