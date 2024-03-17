package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.ShapeContent;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class ShapePath implements ContentModel {
    public final boolean hidden;
    public final int index;
    public final String name;
    public final AnimatableShapeValue shapePath;

    public ShapePath(String str, int i, AnimatableShapeValue animatableShapeValue, boolean z) {
        this.name = str;
        this.index = i;
        this.shapePath = animatableShapeValue;
        this.hidden = z;
    }

    public String getName() {
        return this.name;
    }

    public AnimatableShapeValue getShapePath() {
        return this.shapePath;
    }

    public Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return new ShapeContent(lottieDrawable, baseLayer, this);
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        return "ShapePath{name=" + this.name + ", index=" + this.index + '}';
    }
}