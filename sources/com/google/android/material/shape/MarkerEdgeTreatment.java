package com.google.android.material.shape;

public final class MarkerEdgeTreatment extends EdgeTreatment {
    public final float radius;

    public boolean forceIntersection() {
        return true;
    }

    public MarkerEdgeTreatment(float f) {
        this.radius = f - 0.001f;
    }

    public void getEdgePath(float f, float f2, float f3, ShapePath shapePath) {
        float sqrt = (float) ((((double) this.radius) * Math.sqrt(2.0d)) / 2.0d);
        float sqrt2 = (float) Math.sqrt(Math.pow((double) this.radius, 2.0d) - Math.pow((double) sqrt, 2.0d));
        shapePath.reset(f2 - sqrt, ((float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius)))) + sqrt2);
        shapePath.lineTo(f2, (float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius))));
        shapePath.lineTo(f2 + sqrt, ((float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius)))) + sqrt2);
    }
}
