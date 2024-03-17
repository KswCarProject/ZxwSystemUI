package kotlin.internal;

import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: PlatformImplementations.kt */
public final class PlatformImplementationsKt {
    @NotNull
    public static final PlatformImplementations IMPLEMENTATIONS;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: kotlin.internal.PlatformImplementations} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            java.lang.Class<kotlin.internal.PlatformImplementations> r0 = kotlin.internal.PlatformImplementations.class
            int r1 = getJavaVersion()
            java.lang.String r2 = ", base type classloader: "
            java.lang.String r3 = "Instance classloader: "
            r4 = 65544(0x10008, float:9.1847E-41)
            if (r1 < r4) goto L_0x007f
            java.lang.Class<kotlin.internal.jdk8.JDK8PlatformImplementations> r4 = kotlin.internal.jdk8.JDK8PlatformImplementations.class
            java.lang.Object r4 = r4.newInstance()     // Catch:{ ClassNotFoundException -> 0x0045 }
            kotlin.internal.PlatformImplementations r4 = (kotlin.internal.PlatformImplementations) r4     // Catch:{ ClassCastException -> 0x0019 }
            goto L_0x00f9
        L_0x0019:
            r5 = move-exception
            java.lang.Class r4 = r4.getClass()     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.ClassLoader r4 = r4.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.ClassLoader r6 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.ClassCastException r7 = new java.lang.ClassCastException     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x0045 }
            r8.<init>()     // Catch:{ ClassNotFoundException -> 0x0045 }
            r8.append(r3)     // Catch:{ ClassNotFoundException -> 0x0045 }
            r8.append(r4)     // Catch:{ ClassNotFoundException -> 0x0045 }
            r8.append(r2)     // Catch:{ ClassNotFoundException -> 0x0045 }
            r8.append(r6)     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.String r4 = r8.toString()     // Catch:{ ClassNotFoundException -> 0x0045 }
            r7.<init>(r4)     // Catch:{ ClassNotFoundException -> 0x0045 }
            java.lang.Throwable r4 = r7.initCause(r5)     // Catch:{ ClassNotFoundException -> 0x0045 }
            throw r4     // Catch:{ ClassNotFoundException -> 0x0045 }
        L_0x0045:
            java.lang.String r4 = "kotlin.internal.JRE8PlatformImplementations"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.Object r4 = r4.newInstance()     // Catch:{ ClassNotFoundException -> 0x007f }
            kotlin.internal.PlatformImplementations r4 = (kotlin.internal.PlatformImplementations) r4     // Catch:{ ClassCastException -> 0x0053 }
            goto L_0x00f9
        L_0x0053:
            r5 = move-exception
            java.lang.Class r4 = r4.getClass()     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.ClassLoader r4 = r4.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.ClassLoader r6 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.ClassCastException r7 = new java.lang.ClassCastException     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x007f }
            r8.<init>()     // Catch:{ ClassNotFoundException -> 0x007f }
            r8.append(r3)     // Catch:{ ClassNotFoundException -> 0x007f }
            r8.append(r4)     // Catch:{ ClassNotFoundException -> 0x007f }
            r8.append(r2)     // Catch:{ ClassNotFoundException -> 0x007f }
            r8.append(r6)     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.String r4 = r8.toString()     // Catch:{ ClassNotFoundException -> 0x007f }
            r7.<init>(r4)     // Catch:{ ClassNotFoundException -> 0x007f }
            java.lang.Throwable r4 = r7.initCause(r5)     // Catch:{ ClassNotFoundException -> 0x007f }
            throw r4     // Catch:{ ClassNotFoundException -> 0x007f }
        L_0x007f:
            r4 = 65543(0x10007, float:9.1845E-41)
            if (r1 < r4) goto L_0x00f4
            java.lang.Class<kotlin.internal.jdk7.JDK7PlatformImplementations> r1 = kotlin.internal.jdk7.JDK7PlatformImplementations.class
            java.lang.Object r1 = r1.newInstance()     // Catch:{ ClassNotFoundException -> 0x00ba }
            r4 = r1
            kotlin.internal.PlatformImplementations r4 = (kotlin.internal.PlatformImplementations) r4     // Catch:{ ClassCastException -> 0x008e }
            goto L_0x00f9
        L_0x008e:
            r4 = move-exception
            java.lang.Class r1 = r1.getClass()     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.ClassLoader r5 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.ClassCastException r6 = new java.lang.ClassCastException     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x00ba }
            r7.<init>()     // Catch:{ ClassNotFoundException -> 0x00ba }
            r7.append(r3)     // Catch:{ ClassNotFoundException -> 0x00ba }
            r7.append(r1)     // Catch:{ ClassNotFoundException -> 0x00ba }
            r7.append(r2)     // Catch:{ ClassNotFoundException -> 0x00ba }
            r7.append(r5)     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.String r1 = r7.toString()     // Catch:{ ClassNotFoundException -> 0x00ba }
            r6.<init>(r1)     // Catch:{ ClassNotFoundException -> 0x00ba }
            java.lang.Throwable r1 = r6.initCause(r4)     // Catch:{ ClassNotFoundException -> 0x00ba }
            throw r1     // Catch:{ ClassNotFoundException -> 0x00ba }
        L_0x00ba:
            java.lang.String r1 = "kotlin.internal.JRE7PlatformImplementations"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.Object r1 = r1.newInstance()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r4 = r1
            kotlin.internal.PlatformImplementations r4 = (kotlin.internal.PlatformImplementations) r4     // Catch:{ ClassCastException -> 0x00c8 }
            goto L_0x00f9
        L_0x00c8:
            r4 = move-exception
            java.lang.Class r1 = r1.getClass()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.ClassCastException r5 = new java.lang.ClassCastException     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r6.<init>()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r6.append(r3)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r6.append(r1)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r6.append(r2)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r6.append(r0)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.String r0 = r6.toString()     // Catch:{ ClassNotFoundException -> 0x00f4 }
            r5.<init>(r0)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            java.lang.Throwable r0 = r5.initCause(r4)     // Catch:{ ClassNotFoundException -> 0x00f4 }
            throw r0     // Catch:{ ClassNotFoundException -> 0x00f4 }
        L_0x00f4:
            kotlin.internal.PlatformImplementations r4 = new kotlin.internal.PlatformImplementations
            r4.<init>()
        L_0x00f9:
            IMPLEMENTATIONS = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementationsKt.<clinit>():void");
    }

    public static final int getJavaVersion() {
        String property = System.getProperty("java.specification.version");
        if (property == null) {
            return 65542;
        }
        int indexOf$default = StringsKt__StringsKt.indexOf$default((CharSequence) property, '.', 0, false, 6, (Object) null);
        if (indexOf$default < 0) {
            try {
                return Integer.parseInt(property) * 65536;
            } catch (NumberFormatException unused) {
                return 65542;
            }
        } else {
            int i = indexOf$default + 1;
            int indexOf$default2 = StringsKt__StringsKt.indexOf$default((CharSequence) property, '.', i, false, 4, (Object) null);
            if (indexOf$default2 < 0) {
                indexOf$default2 = property.length();
            }
            try {
                return (Integer.parseInt(property.substring(0, indexOf$default)) * 65536) + Integer.parseInt(property.substring(i, indexOf$default2));
            } catch (NumberFormatException unused2) {
                return 65542;
            }
        }
    }
}
