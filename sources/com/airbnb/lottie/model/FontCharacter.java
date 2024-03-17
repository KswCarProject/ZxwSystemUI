package com.airbnb.lottie.model;

import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.List;

public class FontCharacter {
    public final char character;
    public final String fontFamily;
    public final List<ShapeGroup> shapes;
    public final double size;
    public final String style;
    public final double width;

    public static int hashFor(char c, String str, String str2) {
        return ((((0 + c) * 31) + str.hashCode()) * 31) + str2.hashCode();
    }

    public FontCharacter(List<ShapeGroup> list, char c, double d, double d2, String str, String str2) {
        this.shapes = list;
        this.character = c;
        this.size = d;
        this.width = d2;
        this.style = str;
        this.fontFamily = str2;
    }

    public List<ShapeGroup> getShapes() {
        return this.shapes;
    }

    public double getWidth() {
        return this.width;
    }

    public int hashCode() {
        return hashFor(this.character, this.fontFamily, this.style);
    }
}