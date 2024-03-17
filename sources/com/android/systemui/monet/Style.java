package com.android.systemui.monet;

import org.jetbrains.annotations.NotNull;

/* compiled from: ColorScheme.kt */
public enum Style {
    SPRITZ(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(12.0d)), new TonalSpec(new HueSource(), new ChromaConstant(8.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueSource(), new ChromaConstant(2.0d)), new TonalSpec(new HueSource(), new ChromaConstant(2.0d)))),
    TONAL_SPOT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueAdd(60.0d), new ChromaConstant(24.0d)), new TonalSpec(new HueSource(), new ChromaConstant(4.0d)), new TonalSpec(new HueSource(), new ChromaConstant(8.0d)))),
    VIBRANT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaMaxOut()), new TonalSpec(new HueVibrantSecondary(), new ChromaConstant(24.0d)), new TonalSpec(new HueVibrantTertiary(), new ChromaConstant(32.0d)), new TonalSpec(new HueSource(), new ChromaConstant(10.0d)), new TonalSpec(new HueSource(), new ChromaConstant(12.0d)))),
    EXPRESSIVE(new CoreSpec(new TonalSpec(new HueAdd(240.0d), new ChromaConstant(40.0d)), new TonalSpec(new HueExpressiveSecondary(), new ChromaConstant(24.0d)), new TonalSpec(new HueExpressiveTertiary(), new ChromaConstant(32.0d)), new TonalSpec(new HueAdd(15.0d), new ChromaConstant(8.0d)), new TonalSpec(new HueAdd(15.0d), new ChromaConstant(12.0d)))),
    RAINBOW(new CoreSpec(new TonalSpec(new HueSource(), new ChromaConstant(48.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)), new TonalSpec(new HueAdd(60.0d), new ChromaConstant(24.0d)), new TonalSpec(new HueSource(), new ChromaConstant(0.0d)), new TonalSpec(new HueSource(), new ChromaConstant(0.0d)))),
    FRUIT_SALAD(new CoreSpec(new TonalSpec(new HueSubtract(50.0d), new ChromaConstant(48.0d)), new TonalSpec(new HueSubtract(50.0d), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(36.0d)), new TonalSpec(new HueSource(), new ChromaConstant(10.0d)), new TonalSpec(new HueSource(), new ChromaConstant(16.0d)))),
    CONTENT(new CoreSpec(new TonalSpec(new HueSource(), new ChromaSource()), new TonalSpec(new HueSource(), new ChromaMultiple(0.33d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.66d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.0833d)), new TonalSpec(new HueSource(), new ChromaMultiple(0.1666d))));
    
    @NotNull
    private final CoreSpec coreSpec;

    /* access modifiers changed from: public */
    Style(CoreSpec coreSpec2) {
        this.coreSpec = coreSpec2;
    }

    @NotNull
    public final CoreSpec getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet() {
        return this.coreSpec;
    }
}
