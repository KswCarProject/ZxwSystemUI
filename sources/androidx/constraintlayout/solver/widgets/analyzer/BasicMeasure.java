package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.constraintlayout.solver.widgets.Optimizer;
import androidx.constraintlayout.solver.widgets.VirtualLayout;
import java.util.ArrayList;
import java.util.Iterator;

public class BasicMeasure {
    public ConstraintWidgetContainer constraintWidgetContainer;
    public Measure mMeasure = new Measure();
    public final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<>();

    public static class Measure {
        public ConstraintWidget.DimensionBehaviour horizontalBehavior;
        public int horizontalDimension;
        public int measuredBaseline;
        public boolean measuredHasBaseline;
        public int measuredHeight;
        public boolean measuredNeedsSolverPass;
        public int measuredWidth;
        public boolean useDeprecated;
        public ConstraintWidget.DimensionBehaviour verticalBehavior;
        public int verticalDimension;
    }

    public interface Measurer {
        void didMeasures();

        void measure(ConstraintWidget constraintWidget, Measure measure);
    }

    public void updateHierarchy(ConstraintWidgetContainer constraintWidgetContainer2) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour;
        this.mVariableDimensionsWidgets.clear();
        int size = constraintWidgetContainer2.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = constraintWidgetContainer2.mChildren.get(i);
            ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = constraintWidget.getHorizontalDimensionBehaviour();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
            if (horizontalDimensionBehaviour == dimensionBehaviour2 || constraintWidget.getHorizontalDimensionBehaviour() == (dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_PARENT) || constraintWidget.getVerticalDimensionBehaviour() == dimensionBehaviour2 || constraintWidget.getVerticalDimensionBehaviour() == dimensionBehaviour) {
                this.mVariableDimensionsWidgets.add(constraintWidget);
            }
        }
        constraintWidgetContainer2.invalidateGraph();
    }

    public BasicMeasure(ConstraintWidgetContainer constraintWidgetContainer2) {
        this.constraintWidgetContainer = constraintWidgetContainer2;
    }

    public final void measureChildren(ConstraintWidgetContainer constraintWidgetContainer2) {
        int size = constraintWidgetContainer2.mChildren.size();
        Measurer measurer = constraintWidgetContainer2.getMeasurer();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = constraintWidgetContainer2.mChildren.get(i);
            if (!(constraintWidget instanceof Guideline) && (!constraintWidget.horizontalRun.dimension.resolved || !constraintWidget.verticalRun.dimension.resolved)) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget.getDimensionBehaviour(0);
                boolean z = true;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.getDimensionBehaviour(1);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                if (dimensionBehaviour != dimensionBehaviour3 || constraintWidget.mMatchConstraintDefaultWidth == 1 || dimensionBehaviour2 != dimensionBehaviour3 || constraintWidget.mMatchConstraintDefaultHeight == 1) {
                    z = false;
                }
                if (!z) {
                    measure(measurer, constraintWidget, false);
                }
            }
        }
        measurer.didMeasures();
    }

    public final void solveLinearSystem(ConstraintWidgetContainer constraintWidgetContainer2, String str, int i, int i2) {
        int minWidth = constraintWidgetContainer2.getMinWidth();
        int minHeight = constraintWidgetContainer2.getMinHeight();
        constraintWidgetContainer2.setMinWidth(0);
        constraintWidgetContainer2.setMinHeight(0);
        constraintWidgetContainer2.setWidth(i);
        constraintWidgetContainer2.setHeight(i2);
        constraintWidgetContainer2.setMinWidth(minWidth);
        constraintWidgetContainer2.setMinHeight(minHeight);
        this.constraintWidgetContainer.layout();
    }

    public void solverMeasure(ConstraintWidgetContainer constraintWidgetContainer2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10;
        boolean z;
        int i11;
        boolean z2;
        boolean z3;
        Measurer measurer;
        int i12;
        int i13;
        int i14;
        boolean z4;
        boolean z5;
        int i15;
        ConstraintWidgetContainer constraintWidgetContainer3 = constraintWidgetContainer2;
        int i16 = i;
        int i17 = i4;
        int i18 = i6;
        Measurer measurer2 = constraintWidgetContainer2.getMeasurer();
        int size = constraintWidgetContainer3.mChildren.size();
        int width = constraintWidgetContainer2.getWidth();
        int height = constraintWidgetContainer2.getHeight();
        boolean enabled = Optimizer.enabled(i16, 128);
        boolean z6 = enabled || Optimizer.enabled(i16, 64);
        if (z6) {
            int i19 = 0;
            while (true) {
                if (i19 >= size) {
                    break;
                }
                ConstraintWidget constraintWidget = constraintWidgetContainer3.mChildren.get(i19);
                ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = constraintWidget.getHorizontalDimensionBehaviour();
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                boolean z7 = (horizontalDimensionBehaviour == dimensionBehaviour) && (constraintWidget.getVerticalDimensionBehaviour() == dimensionBehaviour) && constraintWidget.getDimensionRatio() > 0.0f;
                if ((!constraintWidget.isInHorizontalChain() || !z7) && ((!constraintWidget.isInVerticalChain() || !z7) && !(constraintWidget instanceof VirtualLayout) && !constraintWidget.isInHorizontalChain() && !constraintWidget.isInVerticalChain())) {
                    i19++;
                }
            }
            z6 = false;
        }
        if (z6) {
            int i20 = LinearSystem.POOL_SIZE;
        }
        int i21 = 2;
        if (z6 && ((i17 == 1073741824 && i18 == 1073741824) || enabled)) {
            int min = Math.min(constraintWidgetContainer2.getMaxWidth(), i5);
            int min2 = Math.min(constraintWidgetContainer2.getMaxHeight(), i7);
            if (i17 == 1073741824 && constraintWidgetContainer2.getWidth() != min) {
                constraintWidgetContainer3.setWidth(min);
                constraintWidgetContainer2.invalidateGraph();
            }
            if (i18 == 1073741824 && constraintWidgetContainer2.getHeight() != min2) {
                constraintWidgetContainer3.setHeight(min2);
                constraintWidgetContainer2.invalidateGraph();
            }
            if (i17 == 1073741824 && i18 == 1073741824) {
                z = constraintWidgetContainer3.directMeasure(enabled);
                i10 = 2;
            } else {
                boolean directMeasureSetup = constraintWidgetContainer3.directMeasureSetup(enabled);
                if (i17 == 1073741824) {
                    z5 = directMeasureSetup & constraintWidgetContainer3.directMeasureWithOrientation(enabled, 0);
                    i15 = 1;
                } else {
                    z5 = directMeasureSetup;
                    i15 = 0;
                }
                if (i18 == 1073741824) {
                    boolean directMeasureWithOrientation = constraintWidgetContainer3.directMeasureWithOrientation(enabled, 1) & z5;
                    i10 = i15 + 1;
                    z = directMeasureWithOrientation;
                } else {
                    i10 = i15;
                    z = z5;
                }
            }
            if (z) {
                constraintWidgetContainer3.updateFromRuns(i17 == 1073741824, i18 == 1073741824);
            }
        } else {
            constraintWidgetContainer3.horizontalRun.clear();
            constraintWidgetContainer3.verticalRun.clear();
            Iterator<ConstraintWidget> it = constraintWidgetContainer2.getChildren().iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.horizontalRun.clear();
                next.verticalRun.clear();
            }
            z = false;
            i10 = 0;
        }
        if (!z || i10 != 2) {
            if (size > 0) {
                measureChildren(constraintWidgetContainer2);
            }
            int optimizationLevel = constraintWidgetContainer2.getOptimizationLevel();
            int size2 = this.mVariableDimensionsWidgets.size();
            if (size > 0) {
                solveLinearSystem(constraintWidgetContainer3, "First pass", width, height);
            }
            if (size2 > 0) {
                ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour2 = constraintWidgetContainer2.getHorizontalDimensionBehaviour();
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                boolean z8 = horizontalDimensionBehaviour2 == dimensionBehaviour2;
                boolean z9 = constraintWidgetContainer2.getVerticalDimensionBehaviour() == dimensionBehaviour2;
                int max = Math.max(constraintWidgetContainer2.getWidth(), this.constraintWidgetContainer.getMinWidth());
                int max2 = Math.max(constraintWidgetContainer2.getHeight(), this.constraintWidgetContainer.getMinHeight());
                int i22 = 0;
                boolean z10 = false;
                while (i22 < size2) {
                    ConstraintWidget constraintWidget2 = this.mVariableDimensionsWidgets.get(i22);
                    if (!(constraintWidget2 instanceof VirtualLayout)) {
                        i14 = optimizationLevel;
                    } else {
                        int width2 = constraintWidget2.getWidth();
                        int height2 = constraintWidget2.getHeight();
                        i14 = optimizationLevel;
                        boolean measure = z10 | measure(measurer2, constraintWidget2, true);
                        int width3 = constraintWidget2.getWidth();
                        boolean z11 = measure;
                        int height3 = constraintWidget2.getHeight();
                        if (width3 != width2) {
                            constraintWidget2.setWidth(width3);
                            if (z8 && constraintWidget2.getRight() > max) {
                                max = Math.max(max, constraintWidget2.getRight() + constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                            }
                            z4 = true;
                        } else {
                            z4 = z11;
                        }
                        if (height3 != height2) {
                            constraintWidget2.setHeight(height3);
                            if (z9 && constraintWidget2.getBottom() > max2) {
                                max2 = Math.max(max2, constraintWidget2.getBottom() + constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                            }
                            z4 = true;
                        }
                        z10 = z4 | ((VirtualLayout) constraintWidget2).needSolverPass();
                    }
                    i22++;
                    optimizationLevel = i14;
                    i21 = 2;
                }
                int i23 = optimizationLevel;
                int i24 = 0;
                for (int i25 = i21; i24 < i25; i25 = 2) {
                    int i26 = 0;
                    while (i26 < size2) {
                        ConstraintWidget constraintWidget3 = this.mVariableDimensionsWidgets.get(i26);
                        if ((!(constraintWidget3 instanceof Helper) || (constraintWidget3 instanceof VirtualLayout)) && !(constraintWidget3 instanceof Guideline) && constraintWidget3.getVisibility() != 8 && ((!constraintWidget3.horizontalRun.dimension.resolved || !constraintWidget3.verticalRun.dimension.resolved) && !(constraintWidget3 instanceof VirtualLayout))) {
                            int width4 = constraintWidget3.getWidth();
                            int height4 = constraintWidget3.getHeight();
                            i12 = size2;
                            int baselineDistance = constraintWidget3.getBaselineDistance();
                            i13 = i24;
                            z10 |= measure(measurer2, constraintWidget3, true);
                            int width5 = constraintWidget3.getWidth();
                            measurer = measurer2;
                            int height5 = constraintWidget3.getHeight();
                            if (width5 != width4) {
                                constraintWidget3.setWidth(width5);
                                if (z8 && constraintWidget3.getRight() > max) {
                                    max = Math.max(max, constraintWidget3.getRight() + constraintWidget3.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin());
                                }
                                z10 = true;
                            }
                            if (height5 != height4) {
                                constraintWidget3.setHeight(height5);
                                if (z9 && constraintWidget3.getBottom() > max2) {
                                    max2 = Math.max(max2, constraintWidget3.getBottom() + constraintWidget3.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin());
                                }
                                z10 = true;
                            }
                            if (constraintWidget3.hasBaseline() && baselineDistance != constraintWidget3.getBaselineDistance()) {
                                z10 = true;
                            }
                        } else {
                            i13 = i24;
                            i12 = size2;
                            measurer = measurer2;
                        }
                        i26++;
                        size2 = i12;
                        measurer2 = measurer;
                        i24 = i13;
                    }
                    int i27 = i24;
                    int i28 = size2;
                    Measurer measurer3 = measurer2;
                    if (z10) {
                        solveLinearSystem(constraintWidgetContainer3, "intermediate pass", width, height);
                        z10 = false;
                    }
                    i24 = i27 + 1;
                    size2 = i28;
                    measurer2 = measurer3;
                }
                if (z10) {
                    solveLinearSystem(constraintWidgetContainer3, "2nd pass", width, height);
                    if (constraintWidgetContainer2.getWidth() < max) {
                        constraintWidgetContainer3.setWidth(max);
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (constraintWidgetContainer2.getHeight() < max2) {
                        constraintWidgetContainer3.setHeight(max2);
                        z3 = true;
                    } else {
                        z3 = z2;
                    }
                    if (z3) {
                        solveLinearSystem(constraintWidgetContainer3, "3rd pass", width, height);
                    }
                }
                i11 = i23;
            } else {
                i11 = optimizationLevel;
            }
            constraintWidgetContainer3.setOptimizationLevel(i11);
        }
    }

    public final boolean measure(Measurer measurer, ConstraintWidget constraintWidget, boolean z) {
        this.mMeasure.horizontalBehavior = constraintWidget.getHorizontalDimensionBehaviour();
        this.mMeasure.verticalBehavior = constraintWidget.getVerticalDimensionBehaviour();
        this.mMeasure.horizontalDimension = constraintWidget.getWidth();
        this.mMeasure.verticalDimension = constraintWidget.getHeight();
        Measure measure = this.mMeasure;
        measure.measuredNeedsSolverPass = false;
        measure.useDeprecated = z;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = measure.horizontalBehavior;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z2 = dimensionBehaviour == dimensionBehaviour2;
        boolean z3 = measure.verticalBehavior == dimensionBehaviour2;
        boolean z4 = z2 && constraintWidget.mDimensionRatio > 0.0f;
        boolean z5 = z3 && constraintWidget.mDimensionRatio > 0.0f;
        if (z4 && constraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (z5 && constraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        measurer.measure(constraintWidget, measure);
        constraintWidget.setWidth(this.mMeasure.measuredWidth);
        constraintWidget.setHeight(this.mMeasure.measuredHeight);
        constraintWidget.setHasBaseline(this.mMeasure.measuredHasBaseline);
        constraintWidget.setBaselineDistance(this.mMeasure.measuredBaseline);
        Measure measure2 = this.mMeasure;
        measure2.useDeprecated = false;
        return measure2.measuredNeedsSolverPass;
    }
}
