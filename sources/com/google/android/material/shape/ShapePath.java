package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.List;

public class ShapePath {
    public boolean containsIncompatibleShadowOp;
    @Deprecated
    public float currentShadowAngle;
    @Deprecated
    public float endShadowAngle;
    @Deprecated
    public float endX;
    @Deprecated
    public float endY;
    public final List<PathOperation> operations = new ArrayList();
    public final List<ShadowCompatOperation> shadowCompatOperations = new ArrayList();
    @Deprecated
    public float startX;
    @Deprecated
    public float startY;

    public static abstract class PathOperation {
        public final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix matrix2, Path path);
    }

    public ShapePath() {
        reset(0.0f, 0.0f);
    }

    public void reset(float f, float f2) {
        reset(f, f2, 270.0f, 0.0f);
    }

    public void reset(float f, float f2, float f3, float f4) {
        setStartX(f);
        setStartY(f2);
        setEndX(f);
        setEndY(f2);
        setCurrentShadowAngle(f3);
        setEndShadowAngle((f3 + f4) % 360.0f);
        this.operations.clear();
        this.shadowCompatOperations.clear();
        this.containsIncompatibleShadowOp = false;
    }

    public void lineTo(float f, float f2) {
        PathLineOperation pathLineOperation = new PathLineOperation();
        float unused = pathLineOperation.x = f;
        float unused2 = pathLineOperation.y = f2;
        this.operations.add(pathLineOperation);
        LineShadowOperation lineShadowOperation = new LineShadowOperation(pathLineOperation, getEndX(), getEndY());
        addShadowCompatOperation(lineShadowOperation, lineShadowOperation.getAngle() + 270.0f, lineShadowOperation.getAngle() + 270.0f);
        setEndX(f);
        setEndY(f2);
    }

    public void addArc(float f, float f2, float f3, float f4, float f5, float f6) {
        PathArcOperation pathArcOperation = new PathArcOperation(f, f2, f3, f4);
        pathArcOperation.setStartAngle(f5);
        pathArcOperation.setSweepAngle(f6);
        this.operations.add(pathArcOperation);
        ArcShadowOperation arcShadowOperation = new ArcShadowOperation(pathArcOperation);
        float f7 = f5 + f6;
        boolean z = f6 < 0.0f;
        if (z) {
            f5 = (f5 + 180.0f) % 360.0f;
        }
        addShadowCompatOperation(arcShadowOperation, f5, z ? (180.0f + f7) % 360.0f : f7);
        double d = (double) f7;
        setEndX(((f + f3) * 0.5f) + (((f3 - f) / 2.0f) * ((float) Math.cos(Math.toRadians(d)))));
        setEndY(((f2 + f4) * 0.5f) + (((f4 - f2) / 2.0f) * ((float) Math.sin(Math.toRadians(d)))));
    }

    public void applyToPath(Matrix matrix, Path path) {
        int size = this.operations.size();
        for (int i = 0; i < size; i++) {
            this.operations.get(i).applyToPath(matrix, path);
        }
    }

    public ShadowCompatOperation createShadowCompatOperation(Matrix matrix) {
        addConnectingShadowIfNecessary(getEndShadowAngle());
        final Matrix matrix2 = new Matrix(matrix);
        final ArrayList arrayList = new ArrayList(this.shadowCompatOperations);
        return new ShadowCompatOperation() {
            public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas) {
                for (ShadowCompatOperation draw : arrayList) {
                    draw.draw(matrix2, shadowRenderer, i, canvas);
                }
            }
        };
    }

    public final void addShadowCompatOperation(ShadowCompatOperation shadowCompatOperation, float f, float f2) {
        addConnectingShadowIfNecessary(f);
        this.shadowCompatOperations.add(shadowCompatOperation);
        setCurrentShadowAngle(f2);
    }

    public boolean containsIncompatibleShadowOp() {
        return this.containsIncompatibleShadowOp;
    }

    public final void addConnectingShadowIfNecessary(float f) {
        if (getCurrentShadowAngle() != f) {
            float currentShadowAngle2 = ((f - getCurrentShadowAngle()) + 360.0f) % 360.0f;
            if (currentShadowAngle2 <= 180.0f) {
                PathArcOperation pathArcOperation = new PathArcOperation(getEndX(), getEndY(), getEndX(), getEndY());
                pathArcOperation.setStartAngle(getCurrentShadowAngle());
                pathArcOperation.setSweepAngle(currentShadowAngle2);
                this.shadowCompatOperations.add(new ArcShadowOperation(pathArcOperation));
                setCurrentShadowAngle(f);
            }
        }
    }

    public float getStartX() {
        return this.startX;
    }

    public float getStartY() {
        return this.startY;
    }

    public float getEndX() {
        return this.endX;
    }

    public float getEndY() {
        return this.endY;
    }

    public final float getCurrentShadowAngle() {
        return this.currentShadowAngle;
    }

    public final float getEndShadowAngle() {
        return this.endShadowAngle;
    }

    public final void setStartX(float f) {
        this.startX = f;
    }

    public final void setStartY(float f) {
        this.startY = f;
    }

    public final void setEndX(float f) {
        this.endX = f;
    }

    public final void setEndY(float f) {
        this.endY = f;
    }

    public final void setCurrentShadowAngle(float f) {
        this.currentShadowAngle = f;
    }

    public final void setEndShadowAngle(float f) {
        this.endShadowAngle = f;
    }

    public static abstract class ShadowCompatOperation {
        public static final Matrix IDENTITY_MATRIX = new Matrix();

        public abstract void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas);

        public final void draw(ShadowRenderer shadowRenderer, int i, Canvas canvas) {
            draw(IDENTITY_MATRIX, shadowRenderer, i, canvas);
        }
    }

    public static class LineShadowOperation extends ShadowCompatOperation {
        public final PathLineOperation operation;
        public final float startX;
        public final float startY;

        public LineShadowOperation(PathLineOperation pathLineOperation, float f, float f2) {
            this.operation = pathLineOperation;
            this.startX = f;
            this.startY = f2;
        }

        public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) Math.hypot((double) (this.operation.y - this.startY), (double) (this.operation.x - this.startX)), 0.0f);
            Matrix matrix2 = new Matrix(matrix);
            matrix2.preTranslate(this.startX, this.startY);
            matrix2.preRotate(getAngle());
            shadowRenderer.drawEdgeShadow(canvas, matrix2, rectF, i);
        }

        public float getAngle() {
            return (float) Math.toDegrees(Math.atan((double) ((this.operation.y - this.startY) / (this.operation.x - this.startX))));
        }
    }

    public static class ArcShadowOperation extends ShadowCompatOperation {
        public final PathArcOperation operation;

        public ArcShadowOperation(PathArcOperation pathArcOperation) {
            this.operation = pathArcOperation;
        }

        public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas) {
            float access$800 = this.operation.getStartAngle();
            float access$900 = this.operation.getSweepAngle();
            shadowRenderer.drawCornerShadow(canvas, matrix, new RectF(this.operation.getLeft(), this.operation.getTop(), this.operation.getRight(), this.operation.getBottom()), i, access$800, access$900);
        }
    }

    public static class PathLineOperation extends PathOperation {
        public float x;
        public float y;

        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            path.lineTo(this.x, this.y);
            path.transform(matrix);
        }
    }

    public static class PathArcOperation extends PathOperation {
        public static final RectF rectF = new RectF();
        @Deprecated
        public float bottom;
        @Deprecated
        public float left;
        @Deprecated
        public float right;
        @Deprecated
        public float startAngle;
        @Deprecated
        public float sweepAngle;
        @Deprecated
        public float top;

        public PathArcOperation(float f, float f2, float f3, float f4) {
            setLeft(f);
            setTop(f2);
            setRight(f3);
            setBottom(f4);
        }

        public void applyToPath(Matrix matrix, Path path) {
            Matrix matrix2 = this.matrix;
            matrix.invert(matrix2);
            path.transform(matrix2);
            RectF rectF2 = rectF;
            rectF2.set(getLeft(), getTop(), getRight(), getBottom());
            path.arcTo(rectF2, getStartAngle(), getSweepAngle(), false);
            path.transform(matrix);
        }

        public final float getLeft() {
            return this.left;
        }

        public final float getTop() {
            return this.top;
        }

        public final float getRight() {
            return this.right;
        }

        public final float getBottom() {
            return this.bottom;
        }

        public final void setLeft(float f) {
            this.left = f;
        }

        public final void setTop(float f) {
            this.top = f;
        }

        public final void setRight(float f) {
            this.right = f;
        }

        public final void setBottom(float f) {
            this.bottom = f;
        }

        public final float getStartAngle() {
            return this.startAngle;
        }

        public final float getSweepAngle() {
            return this.sweepAngle;
        }

        public final void setStartAngle(float f) {
            this.startAngle = f;
        }

        public final void setSweepAngle(float f) {
            this.sweepAngle = f;
        }
    }
}
