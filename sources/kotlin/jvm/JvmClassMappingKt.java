package kotlin.jvm;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;

/* compiled from: JvmClassMapping.kt */
public final class JvmClassMappingKt {
    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull KClass<T> kClass) {
        Class<?> jClass = ((ClassBasedDeclarationContainer) kClass).getJClass();
        if (!jClass.isPrimitive()) {
            return jClass;
        }
        String name = jClass.getName();
        switch (name.hashCode()) {
            case -1325958191:
                return !name.equals("double") ? jClass : Double.class;
            case 104431:
                return !name.equals("int") ? jClass : Integer.class;
            case 3039496:
                return !name.equals("byte") ? jClass : Byte.class;
            case 3052374:
                return !name.equals("char") ? jClass : Character.class;
            case 3327612:
                return !name.equals("long") ? jClass : Long.class;
            case 3625364:
                return !name.equals("void") ? jClass : Void.class;
            case 64711720:
                return !name.equals("boolean") ? jClass : Boolean.class;
            case 97526364:
                return !name.equals("float") ? jClass : Float.class;
            case 109413500:
                return !name.equals("short") ? jClass : Short.class;
            default:
                return jClass;
        }
    }

    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull Class<T> cls) {
        return Reflection.getOrCreateKotlinClass(cls);
    }
}
