package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyNode;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConstraintWidget {
    public static float DEFAULT_BIAS = 0.5f;
    public boolean hasBaseline = false;
    public ChainRun horizontalChainRun;
    public HorizontalWidgetRun horizontalRun = new HorizontalWidgetRun(this);
    public boolean inPlaceholder;
    public boolean[] isTerminalWidget = {true, true};
    public ArrayList<ConstraintAnchor> mAnchors;
    public ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
    public int mBaselineDistance;
    public ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
    public boolean mBottomHasCentered;
    public ConstraintAnchor mCenter;
    public ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
    public ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
    public float mCircleConstraintAngle = 0.0f;
    public Object mCompanionWidget;
    public int mContainerItemSkip;
    public String mDebugName;
    public float mDimensionRatio;
    public int mDimensionRatioSide;
    public int mDistToBottom;
    public int mDistToLeft;
    public int mDistToRight;
    public int mDistToTop;
    public boolean mGroupsToSolver;
    public int mHeight;
    public float mHorizontalBiasPercent;
    public boolean mHorizontalChainFixedPosition;
    public int mHorizontalChainStyle;
    public ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution = -1;
    public boolean mHorizontalWrapVisited;
    public boolean mInVirtuaLayout = false;
    public boolean mIsHeightWrapContent;
    public boolean mIsWidthWrapContent;
    public ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
    public boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    public ConstraintWidget[] mListNextMatchConstraintsWidget;
    public int mMatchConstraintDefaultHeight = 0;
    public int mMatchConstraintDefaultWidth = 0;
    public int mMatchConstraintMaxHeight = 0;
    public int mMatchConstraintMaxWidth = 0;
    public int mMatchConstraintMinHeight = 0;
    public int mMatchConstraintMinWidth = 0;
    public float mMatchConstraintPercentHeight = 1.0f;
    public float mMatchConstraintPercentWidth = 1.0f;
    public int[] mMaxDimension = {Integer.MAX_VALUE, Integer.MAX_VALUE};
    public int mMinHeight;
    public int mMinWidth;
    public ConstraintWidget[] mNextChainWidget;
    public int mOffsetX;
    public int mOffsetY;
    public boolean mOptimizerMeasurable;
    public ConstraintWidget mParent;
    public int mRelX;
    public int mRelY;
    public float mResolvedDimensionRatio = 1.0f;
    public int mResolvedDimensionRatioSide = -1;
    public boolean mResolvedHasRatio = false;
    public int[] mResolvedMatchConstraintDefault = new int[2];
    public ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
    public boolean mRightHasCentered;
    public ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
    public boolean mTopHasCentered;
    public String mType;
    public float mVerticalBiasPercent;
    public boolean mVerticalChainFixedPosition;
    public int mVerticalChainStyle;
    public ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution = -1;
    public boolean mVerticalWrapVisited;
    public int mVisibility;
    public float[] mWeight;
    public int mWidth;
    public int mX;
    public int mY;
    public boolean measured = false;
    public WidgetRun[] run = new WidgetRun[2];
    public ChainRun verticalChainRun;
    public VerticalWidgetRun verticalRun = new VerticalWidgetRun(this);
    public int[] wrapMeasure = {0, 0};

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public WidgetRun getRun(int i) {
        if (i == 0) {
            return this.horizontalRun;
        }
        if (i == 1) {
            return this.verticalRun;
        }
        return null;
    }

    public void setInVirtualLayout(boolean z) {
        this.mInVirtuaLayout = z;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public void setHasBaseline(boolean z) {
        this.hasBaseline = z;
    }

    public void setInPlaceholder(boolean z) {
        this.inPlaceholder = z;
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        dimensionBehaviourArr[0] = dimensionBehaviour;
        dimensionBehaviourArr[1] = dimensionBehaviour;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
        this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtuaLayout = false;
    }

    public ConstraintWidget() {
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        DimensionBehaviour dimensionBehaviour = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors = new DimensionBehaviour[]{dimensionBehaviour, dimensionBehaviour};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.mX = 0;
        this.mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mOptimizerMeasurable = false;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        addAnchors();
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    public final void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
        immediateConnect(type, constraintWidget, type, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (this.mType != null) {
            str = "type: " + this.mType + " ";
        } else {
            str = str2;
        }
        sb.append(str);
        if (this.mDebugName != null) {
            str2 = "id: " + this.mDebugName + " ";
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.mX);
        sb.append(", ");
        sb.append(this.mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.mX;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.mY;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getLength(int i) {
        if (i == 0) {
            return getWidth();
        }
        if (i == 1) {
            return getHeight();
        }
        return 0;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public int getHorizontalMargin() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        int i = 0;
        if (constraintAnchor != null) {
            i = 0 + constraintAnchor.mMargin;
        }
        ConstraintAnchor constraintAnchor2 = this.mRight;
        return constraintAnchor2 != null ? i + constraintAnchor2.mMargin : i;
    }

    public int getVerticalMargin() {
        int i = 0;
        if (this.mLeft != null) {
            i = 0 + this.mTop.mMargin;
        }
        return this.mRight != null ? i + this.mBottom.mMargin : i;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int i) {
        if (i == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (i == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public void setX(int i) {
        this.mX = i;
    }

    public void setY(int i) {
        this.mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.mX = i;
        this.mY = i2;
    }

    public void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f > 0.0f && f < 1.0f && i == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f > 0.0f && f < 1.0f && i == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDimensionRatio(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            if (r9 == 0) goto L_0x008e
            int r1 = r9.length()
            if (r1 != 0) goto L_0x000b
            goto L_0x008e
        L_0x000b:
            r1 = -1
            int r2 = r9.length()
            r3 = 44
            int r3 = r9.indexOf(r3)
            r4 = 0
            r5 = 1
            if (r3 <= 0) goto L_0x0037
            int r6 = r2 + -1
            if (r3 >= r6) goto L_0x0037
            java.lang.String r6 = r9.substring(r4, r3)
            java.lang.String r7 = "W"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x002c
            r1 = r4
            goto L_0x0035
        L_0x002c:
            java.lang.String r4 = "H"
            boolean r4 = r6.equalsIgnoreCase(r4)
            if (r4 == 0) goto L_0x0035
            r1 = r5
        L_0x0035:
            int r4 = r3 + 1
        L_0x0037:
            r3 = 58
            int r3 = r9.indexOf(r3)
            if (r3 < 0) goto L_0x0075
            int r2 = r2 - r5
            if (r3 >= r2) goto L_0x0075
            java.lang.String r2 = r9.substring(r4, r3)
            int r3 = r3 + r5
            java.lang.String r9 = r9.substring(r3)
            int r3 = r2.length()
            if (r3 <= 0) goto L_0x0084
            int r3 = r9.length()
            if (r3 <= 0) goto L_0x0084
            float r2 = java.lang.Float.parseFloat(r2)     // Catch:{ NumberFormatException -> 0x0084 }
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            int r3 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r3 <= 0) goto L_0x0084
            int r3 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r3 <= 0) goto L_0x0084
            if (r1 != r5) goto L_0x006f
            float r9 = r9 / r2
            float r9 = java.lang.Math.abs(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x006f:
            float r2 = r2 / r9
            float r9 = java.lang.Math.abs(r2)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x0075:
            java.lang.String r9 = r9.substring(r4)
            int r2 = r9.length()
            if (r2 <= 0) goto L_0x0084
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x0084:
            r9 = r0
        L_0x0085:
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x008d
            r8.mDimensionRatio = r9
            r8.mDimensionRatioSide = r1
        L_0x008d:
            return
        L_0x008e:
            r8.mDimensionRatio = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.setDimensionRatio(java.lang.String):void");
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setFrame(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i3 - i;
        int i8 = i4 - i2;
        this.mX = i;
        this.mY = i2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
        DimensionBehaviour dimensionBehaviour2 = DimensionBehaviour.FIXED;
        if (dimensionBehaviour == dimensionBehaviour2 && i7 < (i6 = this.mWidth)) {
            i7 = i6;
        }
        if (dimensionBehaviourArr[1] == dimensionBehaviour2 && i8 < (i5 = this.mHeight)) {
            i8 = i5;
        }
        this.mWidth = i7;
        this.mHeight = i8;
        int i9 = this.mMinHeight;
        if (i8 < i9) {
            this.mHeight = i9;
        }
        int i10 = this.mMinWidth;
        if (i7 < i10) {
            this.mWidth = i10;
        }
    }

    public void setHorizontalDimension(int i, int i2) {
        this.mX = i;
        int i3 = i2 - i;
        this.mWidth = i3;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.mY = i;
        int i3 = i2 - i;
        this.mHeight = i3;
        int i4 = this.mMinHeight;
        if (i3 < i4) {
            this.mHeight = i4;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
        this.hasBaseline = i > 0;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, true);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        if (constraintAnchor.getOwner() == this) {
            connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), i);
        }
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i) {
        ConstraintAnchor.Type type3;
        ConstraintAnchor.Type type4;
        boolean z;
        ConstraintAnchor.Type type5 = ConstraintAnchor.Type.CENTER;
        if (type != type5) {
            ConstraintAnchor.Type type6 = ConstraintAnchor.Type.CENTER_X;
            if (type == type6 && (type2 == (type4 = ConstraintAnchor.Type.LEFT) || type2 == ConstraintAnchor.Type.RIGHT)) {
                ConstraintAnchor anchor = getAnchor(type4);
                ConstraintAnchor anchor2 = constraintWidget.getAnchor(type2);
                ConstraintAnchor anchor3 = getAnchor(ConstraintAnchor.Type.RIGHT);
                anchor.connect(anchor2, 0);
                anchor3.connect(anchor2, 0);
                getAnchor(type6).connect(anchor2, 0);
                return;
            }
            ConstraintAnchor.Type type7 = ConstraintAnchor.Type.CENTER_Y;
            if (type == type7 && (type2 == (type3 = ConstraintAnchor.Type.TOP) || type2 == ConstraintAnchor.Type.BOTTOM)) {
                ConstraintAnchor anchor4 = constraintWidget.getAnchor(type2);
                getAnchor(type3).connect(anchor4, 0);
                getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor4, 0);
                getAnchor(type7).connect(anchor4, 0);
            } else if (type == type6 && type2 == type6) {
                ConstraintAnchor.Type type8 = ConstraintAnchor.Type.LEFT;
                getAnchor(type8).connect(constraintWidget.getAnchor(type8), 0);
                ConstraintAnchor.Type type9 = ConstraintAnchor.Type.RIGHT;
                getAnchor(type9).connect(constraintWidget.getAnchor(type9), 0);
                getAnchor(type6).connect(constraintWidget.getAnchor(type2), 0);
            } else if (type == type7 && type2 == type7) {
                ConstraintAnchor.Type type10 = ConstraintAnchor.Type.TOP;
                getAnchor(type10).connect(constraintWidget.getAnchor(type10), 0);
                ConstraintAnchor.Type type11 = ConstraintAnchor.Type.BOTTOM;
                getAnchor(type11).connect(constraintWidget.getAnchor(type11), 0);
                getAnchor(type7).connect(constraintWidget.getAnchor(type2), 0);
            } else {
                ConstraintAnchor anchor5 = getAnchor(type);
                ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
                if (anchor5.isValidConnection(anchor6)) {
                    ConstraintAnchor.Type type12 = ConstraintAnchor.Type.BASELINE;
                    if (type == type12) {
                        ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.TOP);
                        ConstraintAnchor anchor8 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                        if (anchor7 != null) {
                            anchor7.reset();
                        }
                        if (anchor8 != null) {
                            anchor8.reset();
                        }
                        i = 0;
                    } else if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                        ConstraintAnchor anchor9 = getAnchor(type12);
                        if (anchor9 != null) {
                            anchor9.reset();
                        }
                        ConstraintAnchor anchor10 = getAnchor(type5);
                        if (anchor10.getTarget() != anchor6) {
                            anchor10.reset();
                        }
                        ConstraintAnchor opposite = getAnchor(type).getOpposite();
                        ConstraintAnchor anchor11 = getAnchor(type7);
                        if (anchor11.isConnected()) {
                            opposite.reset();
                            anchor11.reset();
                        }
                    } else if (type == ConstraintAnchor.Type.LEFT || type == ConstraintAnchor.Type.RIGHT) {
                        ConstraintAnchor anchor12 = getAnchor(type5);
                        if (anchor12.getTarget() != anchor6) {
                            anchor12.reset();
                        }
                        ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                        ConstraintAnchor anchor13 = getAnchor(type6);
                        if (anchor13.isConnected()) {
                            opposite2.reset();
                            anchor13.reset();
                        }
                    }
                    anchor5.connect(anchor6, i);
                }
            }
        } else if (type2 == type5) {
            ConstraintAnchor.Type type13 = ConstraintAnchor.Type.LEFT;
            ConstraintAnchor anchor14 = getAnchor(type13);
            ConstraintAnchor.Type type14 = ConstraintAnchor.Type.RIGHT;
            ConstraintAnchor anchor15 = getAnchor(type14);
            ConstraintAnchor.Type type15 = ConstraintAnchor.Type.TOP;
            ConstraintAnchor anchor16 = getAnchor(type15);
            ConstraintAnchor.Type type16 = ConstraintAnchor.Type.BOTTOM;
            ConstraintAnchor anchor17 = getAnchor(type16);
            boolean z2 = true;
            if ((anchor14 == null || !anchor14.isConnected()) && (anchor15 == null || !anchor15.isConnected())) {
                connect(type13, constraintWidget, type13, 0);
                connect(type14, constraintWidget, type14, 0);
                z = true;
            } else {
                z = false;
            }
            if ((anchor16 == null || !anchor16.isConnected()) && (anchor17 == null || !anchor17.isConnected())) {
                connect(type15, constraintWidget, type15, 0);
                connect(type16, constraintWidget, type16, 0);
            } else {
                z2 = false;
            }
            if (z && z2) {
                getAnchor(type5).connect(constraintWidget.getAnchor(type5), 0);
            } else if (z) {
                ConstraintAnchor.Type type17 = ConstraintAnchor.Type.CENTER_X;
                getAnchor(type17).connect(constraintWidget.getAnchor(type17), 0);
            } else if (z2) {
                ConstraintAnchor.Type type18 = ConstraintAnchor.Type.CENTER_Y;
                getAnchor(type18).connect(constraintWidget.getAnchor(type18), 0);
            }
        } else {
            ConstraintAnchor.Type type19 = ConstraintAnchor.Type.LEFT;
            if (type2 == type19 || type2 == ConstraintAnchor.Type.RIGHT) {
                connect(type19, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.RIGHT, constraintWidget, type2, 0);
                getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
                return;
            }
            ConstraintAnchor.Type type20 = ConstraintAnchor.Type.TOP;
            if (type2 == type20 || type2 == ConstraintAnchor.Type.BOTTOM) {
                connect(type20, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type2, 0);
                getAnchor(type5).connect(constraintWidget.getAnchor(type2), 0);
            }
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                this.mAnchors.get(i).reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[type.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (i == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public boolean isInHorizontalChain() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mRight;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        r1 = r1.mTop;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.constraintlayout.solver.widgets.ConstraintWidget getPreviousChainMember(int r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L_0x000f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.mTarget
            if (r0 != r1) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r2.mOwner
            return r1
        L_0x000f:
            r0 = 1
            if (r2 != r0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.mTarget
            if (r0 != r1) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r2.mOwner
            return r1
        L_0x001f:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.getPreviousChainMember(int):androidx.constraintlayout.solver.widgets.ConstraintWidget");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        r1 = r1.mBottom;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.constraintlayout.solver.widgets.ConstraintWidget getNextChainMember(int r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L_0x000f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.mTarget
            if (r0 != r1) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r2.mOwner
            return r1
        L_0x000f:
            r0 = 1
            if (r2 != r0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.mTarget
            if (r0 != r1) goto L_0x001f
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r2.mOwner
            return r1
        L_0x001f:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.getNextChainMember(int):androidx.constraintlayout.solver.widgets.ConstraintWidget");
    }

    public boolean isInVerticalChain() {
        ConstraintAnchor constraintAnchor = this.mTop;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
        if (constraintAnchor2 != null && constraintAnchor2.mTarget == constraintAnchor) {
            return true;
        }
        ConstraintAnchor constraintAnchor3 = this.mBottom;
        ConstraintAnchor constraintAnchor4 = constraintAnchor3.mTarget;
        return constraintAnchor4 != null && constraintAnchor4.mTarget == constraintAnchor3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000f, code lost:
        r3 = r3[r4 + 1];
        r4 = r3.mTarget;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isChainHead(int r4) {
        /*
            r3 = this;
            int r4 = r4 * 2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r3.mListAnchors
            r0 = r3[r4]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r0.mTarget
            r2 = 1
            if (r1 == 0) goto L_0x001b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 == r0) goto L_0x001b
            int r4 = r4 + r2
            r3 = r3[r4]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r3.mTarget
            if (r4 == 0) goto L_0x001b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != r3) goto L_0x001b
            goto L_0x001c
        L_0x001b:
            r2 = 0
        L_0x001c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.isChainHead(int):boolean");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v4, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r29v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v4, resolved type: int} */
    /* JADX WARNING: Code restructure failed: missing block: B:212:0x03ec, code lost:
        if (r13.mVisibility == 8) goto L_0x03f1;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0215  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x021f  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x022f  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0236  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0239  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x024b  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x032e  */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x0350  */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x0392  */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x03a3  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x03a5  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x03a8  */
    /* JADX WARNING: Removed duplicated region for block: B:225:0x0466  */
    /* JADX WARNING: Removed duplicated region for block: B:227:0x046c  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x0495  */
    /* JADX WARNING: Removed duplicated region for block: B:234:0x049f  */
    /* JADX WARNING: Removed duplicated region for block: B:241:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r45) {
        /*
            r44 = this;
            r13 = r44
            r9 = r45
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r13.mLeft
            androidx.constraintlayout.solver.SolverVariable r7 = r9.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r13.mRight
            androidx.constraintlayout.solver.SolverVariable r6 = r9.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r13.mTop
            androidx.constraintlayout.solver.SolverVariable r4 = r9.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r13.mBottom
            androidx.constraintlayout.solver.SolverVariable r3 = r9.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r13.mBaseline
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r0)
            int r0 = androidx.constraintlayout.solver.LinearSystem.POOL_SIZE
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r13.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r2 = r0.start
            boolean r5 = r2.resolved
            r15 = 7
            r14 = 1
            r12 = 0
            if (r5 == 0) goto L_0x00bd
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x00bd
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r5 = r0.start
            boolean r5 = r5.resolved
            if (r5 == 0) goto L_0x00bd
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x00bd
            int r0 = r2.value
            r9.addEquality(r7, r0)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r13.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r9.addEquality(r6, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r9.addEquality(r4, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r9.addEquality(r3, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r9.addEquality(r1, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r13.mParent
            if (r0 == 0) goto L_0x00bc
            if (r0 == 0) goto L_0x007c
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r0.mListDimensionBehaviors
            r1 = r1[r12]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r2) goto L_0x007c
            r1 = r14
            goto L_0x007d
        L_0x007c:
            r1 = r12
        L_0x007d:
            if (r0 == 0) goto L_0x0089
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r0 = r0[r14]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r2) goto L_0x0089
            r0 = r14
            goto L_0x008a
        L_0x0089:
            r0 = r12
        L_0x008a:
            if (r1 == 0) goto L_0x00a3
            boolean[] r1 = r13.isTerminalWidget
            boolean r1 = r1[r12]
            if (r1 == 0) goto L_0x00a3
            boolean r1 = r44.isInHorizontalChain()
            if (r1 != 0) goto L_0x00a3
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r1)
            r9.addGreaterThan(r1, r6, r12, r15)
        L_0x00a3:
            if (r0 == 0) goto L_0x00bc
            boolean[] r0 = r13.isTerminalWidget
            boolean r0 = r0[r14]
            if (r0 == 0) goto L_0x00bc
            boolean r0 = r44.isInVerticalChain()
            if (r0 != 0) goto L_0x00bc
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r9.createObjectVariable(r0)
            r9.addGreaterThan(r0, r3, r12, r15)
        L_0x00bc:
            return
        L_0x00bd:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r13.mParent
            r11 = 8
            if (r0 == 0) goto L_0x014b
            if (r0 == 0) goto L_0x00cf
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r0.mListDimensionBehaviors
            r2 = r2[r12]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r2 != r5) goto L_0x00cf
            r2 = r14
            goto L_0x00d0
        L_0x00cf:
            r2 = r12
        L_0x00d0:
            if (r0 == 0) goto L_0x00dc
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r0 = r0[r14]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r5) goto L_0x00dc
            r0 = r14
            goto L_0x00dd
        L_0x00dc:
            r0 = r12
        L_0x00dd:
            boolean r5 = r13.isChainHead(r12)
            if (r5 == 0) goto L_0x00ec
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r5
            r5.addChain(r13, r12)
            r5 = r14
            goto L_0x00f0
        L_0x00ec:
            boolean r5 = r44.isInHorizontalChain()
        L_0x00f0:
            boolean r8 = r13.isChainHead(r14)
            if (r8 == 0) goto L_0x00ff
            androidx.constraintlayout.solver.widgets.ConstraintWidget r8 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r8 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r8
            r8.addChain(r13, r14)
            r8 = r14
            goto L_0x0103
        L_0x00ff:
            boolean r8 = r44.isInVerticalChain()
        L_0x0103:
            if (r5 != 0) goto L_0x0122
            if (r2 == 0) goto L_0x0122
            int r10 = r13.mVisibility
            if (r10 == r11) goto L_0x0122
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x0122
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x0122
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mRight
            androidx.constraintlayout.solver.SolverVariable r10 = r9.createObjectVariable(r10)
            r9.addGreaterThan(r10, r6, r12, r14)
        L_0x0122:
            if (r8 != 0) goto L_0x0145
            if (r0 == 0) goto L_0x0145
            int r10 = r13.mVisibility
            if (r10 == r11) goto L_0x0145
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x0145
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mTarget
            if (r10 != 0) goto L_0x0145
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mBaseline
            if (r10 != 0) goto L_0x0145
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r10.mBottom
            androidx.constraintlayout.solver.SolverVariable r10 = r9.createObjectVariable(r10)
            r9.addGreaterThan(r10, r3, r12, r14)
        L_0x0145:
            r10 = r2
            r27 = r5
            r26 = r8
            goto L_0x0151
        L_0x014b:
            r0 = r12
            r10 = r0
            r26 = r10
            r27 = r26
        L_0x0151:
            int r2 = r13.mWidth
            int r5 = r13.mMinWidth
            if (r2 >= r5) goto L_0x0158
            goto L_0x0159
        L_0x0158:
            r5 = r2
        L_0x0159:
            int r8 = r13.mHeight
            int r15 = r13.mMinHeight
            if (r8 >= r15) goto L_0x0160
            goto L_0x0161
        L_0x0160:
            r15 = r8
        L_0x0161:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r11 = r13.mListDimensionBehaviors
            r14 = r11[r12]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r12 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r20 = r1
            if (r14 == r12) goto L_0x016d
            r1 = 1
            goto L_0x016e
        L_0x016d:
            r1 = 0
        L_0x016e:
            r18 = 1
            r11 = r11[r18]
            r21 = r3
            r25 = r4
            if (r11 == r12) goto L_0x017a
            r3 = 1
            goto L_0x017b
        L_0x017a:
            r3 = 0
        L_0x017b:
            int r4 = r13.mDimensionRatioSide
            r13.mResolvedDimensionRatioSide = r4
            r22 = r5
            float r5 = r13.mDimensionRatio
            r13.mResolvedDimensionRatio = r5
            r23 = r15
            int r15 = r13.mMatchConstraintDefaultWidth
            r28 = r6
            int r6 = r13.mMatchConstraintDefaultHeight
            r24 = 0
            int r24 = (r5 > r24 ? 1 : (r5 == r24 ? 0 : -1))
            r29 = 4
            r30 = r7
            if (r24 <= 0) goto L_0x01fe
            int r7 = r13.mVisibility
            r9 = 8
            if (r7 == r9) goto L_0x0200
            r7 = 3
            if (r14 != r12) goto L_0x01a3
            if (r15 != 0) goto L_0x01a3
            r15 = r7
        L_0x01a3:
            if (r11 != r12) goto L_0x01a8
            if (r6 != 0) goto L_0x01a8
            r6 = r7
        L_0x01a8:
            if (r14 != r12) goto L_0x01b4
            if (r11 != r12) goto L_0x01b4
            if (r15 != r7) goto L_0x01b4
            if (r6 != r7) goto L_0x01b4
            r13.setupDimensionRatio(r10, r0, r1, r3)
            goto L_0x01f4
        L_0x01b4:
            if (r14 != r12) goto L_0x01cb
            if (r15 != r7) goto L_0x01cb
            r1 = 0
            r13.mResolvedDimensionRatioSide = r1
            float r1 = (float) r8
            float r5 = r5 * r1
            int r5 = (int) r5
            r32 = r6
            if (r11 == r12) goto L_0x01c8
            r33 = r29
            r7 = 0
            r29 = r23
            goto L_0x0209
        L_0x01c8:
            r33 = r15
            goto L_0x01fa
        L_0x01cb:
            if (r11 != r12) goto L_0x01f4
            if (r6 != r7) goto L_0x01f4
            r1 = 1
            r13.mResolvedDimensionRatioSide = r1
            r1 = -1
            if (r4 != r1) goto L_0x01da
            r1 = 1065353216(0x3f800000, float:1.0)
            float r1 = r1 / r5
            r13.mResolvedDimensionRatio = r1
        L_0x01da:
            float r1 = r13.mResolvedDimensionRatio
            float r2 = (float) r2
            float r1 = r1 * r2
            int r1 = (int) r1
            if (r14 == r12) goto L_0x01eb
            r33 = r15
            r5 = r22
            r32 = r29
            r7 = 0
            r29 = r1
            goto L_0x0209
        L_0x01eb:
            r29 = r1
            r32 = r6
            r33 = r15
            r5 = r22
            goto L_0x01fc
        L_0x01f4:
            r32 = r6
            r33 = r15
            r5 = r22
        L_0x01fa:
            r29 = r23
        L_0x01fc:
            r7 = 1
            goto L_0x0209
        L_0x01fe:
            r9 = 8
        L_0x0200:
            r32 = r6
            r33 = r15
            r5 = r22
            r29 = r23
            r7 = 0
        L_0x0209:
            int[] r1 = r13.mResolvedMatchConstraintDefault
            r2 = 0
            r1[r2] = r33
            r2 = 1
            r1[r2] = r32
            r13.mResolvedHasRatio = r7
            if (r7 == 0) goto L_0x021f
            int r1 = r13.mResolvedDimensionRatioSide
            r6 = -1
            if (r1 == 0) goto L_0x021c
            if (r1 != r6) goto L_0x0220
        L_0x021c:
            r17 = 1
            goto L_0x0222
        L_0x021f:
            r6 = -1
        L_0x0220:
            r17 = 0
        L_0x0222:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r13.mListDimensionBehaviors
            r2 = 0
            r1 = r1[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r4) goto L_0x0232
            boolean r1 = r13 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r1 == 0) goto L_0x0232
            r31 = 1
            goto L_0x0234
        L_0x0232:
            r31 = 0
        L_0x0234:
            if (r31 == 0) goto L_0x0239
            r34 = 0
            goto L_0x023b
        L_0x0239:
            r34 = r5
        L_0x023b:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r13.mCenter
            boolean r1 = r1.isConnected()
            r3 = 1
            r35 = r1 ^ 1
            int r1 = r13.mHorizontalResolution
            r15 = 2
            r36 = 0
            if (r1 == r15) goto L_0x032e
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r1 = r13.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r2 = r1.start
            boolean r5 = r2.resolved
            if (r5 == 0) goto L_0x02a2
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.end
            boolean r1 = r1.resolved
            if (r1 != 0) goto L_0x025a
            goto L_0x02a2
        L_0x025a:
            int r1 = r2.value
            r11 = r9
            r14 = r30
            r9 = r45
            r9.addEquality(r14, r1)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r1 = r13.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r1.end
            int r1 = r1.value
            r12 = r28
            r9.addEquality(r12, r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r13.mParent
            if (r1 == 0) goto L_0x028e
            if (r10 == 0) goto L_0x028e
            boolean[] r1 = r13.isTerminalWidget
            r2 = 0
            boolean r1 = r1[r2]
            if (r1 == 0) goto L_0x028e
            boolean r1 = r44.isInHorizontalChain()
            if (r1 != 0) goto L_0x028e
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r13.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r1)
            r8 = 7
            r9.addGreaterThan(r1, r12, r2, r8)
        L_0x028e:
            r39 = r0
            r43 = r4
            r38 = r7
            r30 = r10
            r37 = r12
            r28 = r14
            r40 = r20
            r41 = r21
            r42 = r25
            goto L_0x0342
        L_0x02a2:
            r11 = r9
            r12 = r28
            r14 = r30
            r8 = 7
            r9 = r45
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r13.mParent
            if (r1 == 0) goto L_0x02b7
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mRight
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r1)
            r18 = r1
            goto L_0x02b9
        L_0x02b7:
            r18 = r36
        L_0x02b9:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r13.mParent
            if (r1 == 0) goto L_0x02c6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mLeft
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r1)
            r28 = r1
            goto L_0x02c8
        L_0x02c6:
            r28 = r36
        L_0x02c8:
            r2 = 1
            boolean[] r1 = r13.isTerminalWidget
            r16 = 0
            boolean r5 = r1[r16]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r13.mListDimensionBehaviors
            r1 = r1[r16]
            r19 = r8
            r8 = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r13.mLeft
            r30 = r10
            r10 = r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r13.mRight
            r11 = r1
            int r1 = r13.mX
            r37 = r12
            r2 = r16
            r12 = r1
            int r1 = r13.mMinWidth
            r38 = r14
            r14 = r1
            int[] r1 = r13.mMaxDimension
            r1 = r1[r2]
            r15 = r1
            float r1 = r13.mHorizontalBiasPercent
            r16 = r1
            int r1 = r13.mMatchConstraintMinWidth
            r22 = r1
            int r1 = r13.mMatchConstraintMaxWidth
            r23 = r1
            float r1 = r13.mMatchConstraintPercentWidth
            r24 = r1
            r39 = r0
            r0 = r44
            r40 = r20
            r1 = r45
            r41 = r21
            r3 = r30
            r43 = r4
            r42 = r25
            r4 = r39
            r19 = r6
            r6 = r28
            r28 = r38
            r38 = r7
            r7 = r18
            r9 = r31
            r13 = r34
            r18 = r27
            r19 = r26
            r20 = r33
            r21 = r32
            r25 = r35
            r2 = 1
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25)
            goto L_0x0340
        L_0x032e:
            r39 = r0
            r43 = r4
            r38 = r7
            r40 = r20
            r41 = r21
            r42 = r25
            r37 = r28
            r28 = r30
            r30 = r10
        L_0x0340:
            r13 = r44
        L_0x0342:
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r1 = r0.start
            boolean r2 = r1.resolved
            if (r2 == 0) goto L_0x0392
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0392
            int r0 = r1.value
            r9 = r45
            r7 = r42
            r9.addEquality(r7, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r6 = r41
            r9.addEquality(r6, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r13.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r1 = r40
            r9.addEquality(r1, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r13.mParent
            if (r0 == 0) goto L_0x038d
            if (r26 != 0) goto L_0x038d
            if (r39 == 0) goto L_0x038d
            boolean[] r2 = r13.isTerminalWidget
            r4 = 1
            boolean r2 = r2[r4]
            if (r2 == 0) goto L_0x038a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r9.createObjectVariable(r0)
            r2 = 7
            r3 = 0
            r9.addGreaterThan(r0, r6, r3, r2)
            goto L_0x0390
        L_0x038a:
            r2 = 7
            r3 = 0
            goto L_0x0390
        L_0x038d:
            r2 = 7
            r3 = 0
            r4 = 1
        L_0x0390:
            r14 = r3
            goto L_0x039e
        L_0x0392:
            r9 = r45
            r1 = r40
            r6 = r41
            r7 = r42
            r2 = 7
            r3 = 0
            r4 = 1
            r14 = r4
        L_0x039e:
            int r0 = r13.mVerticalResolution
            r5 = 2
            if (r0 != r5) goto L_0x03a5
            r12 = r3
            goto L_0x03a6
        L_0x03a5:
            r12 = r14
        L_0x03a6:
            if (r12 == 0) goto L_0x0466
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r13.mListDimensionBehaviors
            r0 = r0[r4]
            r5 = r43
            if (r0 != r5) goto L_0x03b7
            boolean r0 = r13 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x03b7
            r17 = r4
            goto L_0x03b9
        L_0x03b7:
            r17 = r3
        L_0x03b9:
            if (r17 == 0) goto L_0x03bd
            r29 = r3
        L_0x03bd:
            if (r38 == 0) goto L_0x03c9
            int r0 = r13.mResolvedDimensionRatioSide
            if (r0 == r4) goto L_0x03c6
            r5 = -1
            if (r0 != r5) goto L_0x03c9
        L_0x03c6:
            r18 = r4
            goto L_0x03cb
        L_0x03c9:
            r18 = r3
        L_0x03cb:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r13.mParent
            if (r0 == 0) goto L_0x03d6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r9.createObjectVariable(r0)
            goto L_0x03d8
        L_0x03d6:
            r0 = r36
        L_0x03d8:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r13.mParent
            if (r5 == 0) goto L_0x03e4
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTop
            androidx.constraintlayout.solver.SolverVariable r5 = r9.createObjectVariable(r5)
            r36 = r5
        L_0x03e4:
            int r5 = r13.mBaselineDistance
            if (r5 > 0) goto L_0x03ef
            int r5 = r13.mVisibility
            r8 = 8
            if (r5 != r8) goto L_0x041b
            goto L_0x03f1
        L_0x03ef:
            r8 = 8
        L_0x03f1:
            int r5 = r44.getBaselineDistance()
            r9.addEquality(r1, r7, r5, r2)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r13.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x0414
            androidx.constraintlayout.solver.SolverVariable r5 = r9.createObjectVariable(r5)
            r9.addEquality(r1, r5, r3, r2)
            if (r39 == 0) goto L_0x0411
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r13.mBottom
            androidx.constraintlayout.solver.SolverVariable r1 = r9.createObjectVariable(r1)
            r2 = 5
            r9.addGreaterThan(r0, r1, r3, r2)
        L_0x0411:
            r25 = r3
            goto L_0x041d
        L_0x0414:
            int r5 = r13.mVisibility
            if (r5 != r8) goto L_0x041b
            r9.addEquality(r1, r7, r3, r2)
        L_0x041b:
            r25 = r35
        L_0x041d:
            r2 = 0
            boolean[] r1 = r13.isTerminalWidget
            boolean r5 = r1[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r13.mListDimensionBehaviors
            r8 = r1[r4]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r10 = r13.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r11 = r13.mBottom
            int r12 = r13.mY
            int r14 = r13.mMinHeight
            int[] r1 = r13.mMaxDimension
            r15 = r1[r4]
            float r1 = r13.mVerticalBiasPercent
            r16 = r1
            int r1 = r13.mMatchConstraintMinHeight
            r22 = r1
            int r1 = r13.mMatchConstraintMaxHeight
            r23 = r1
            float r1 = r13.mMatchConstraintPercentHeight
            r24 = r1
            r19 = r0
            r0 = r44
            r1 = r45
            r3 = r39
            r4 = r30
            r30 = r6
            r6 = r36
            r31 = r7
            r7 = r19
            r9 = r17
            r13 = r29
            r17 = r18
            r18 = r26
            r19 = r27
            r20 = r32
            r21 = r33
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25)
            goto L_0x046a
        L_0x0466:
            r30 = r6
            r31 = r7
        L_0x046a:
            if (r38 == 0) goto L_0x0495
            r6 = 7
            r7 = r44
            int r0 = r7.mResolvedDimensionRatioSide
            r1 = 1
            if (r0 != r1) goto L_0x0484
            float r5 = r7.mResolvedDimensionRatio
            r0 = r45
            r1 = r30
            r2 = r31
            r3 = r37
            r4 = r28
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x0497
        L_0x0484:
            float r5 = r7.mResolvedDimensionRatio
            r6 = 7
            r0 = r45
            r1 = r37
            r2 = r28
            r3 = r30
            r4 = r31
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x0497
        L_0x0495:
            r7 = r44
        L_0x0497:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L_0x04bf
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.getOwner()
            float r1 = r7.mCircleConstraintAngle
            r2 = 1119092736(0x42b40000, float:90.0)
            float r1 = r1 + r2
            double r1 = (double) r1
            double r1 = java.lang.Math.toRadians(r1)
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r7.mCenter
            int r2 = r2.getMargin()
            r3 = r45
            r3.addCenterPoint(r7, r0, r1, r2)
        L_0x04bf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.solver.LinearSystem):void");
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (i == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:176:0x02c1  */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x02e4  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x02f3  */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0317  */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x031d A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x035c  */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x037c  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x0382  */
    /* JADX WARNING: Removed duplicated region for block: B:235:0x038c A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:242:0x03a5  */
    /* JADX WARNING: Removed duplicated region for block: B:244:0x03ae A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:266:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:270:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01cb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void applyConstraints(androidx.constraintlayout.solver.LinearSystem r28, boolean r29, boolean r30, boolean r31, boolean r32, androidx.constraintlayout.solver.SolverVariable r33, androidx.constraintlayout.solver.SolverVariable r34, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r35, boolean r36, androidx.constraintlayout.solver.widgets.ConstraintAnchor r37, androidx.constraintlayout.solver.widgets.ConstraintAnchor r38, int r39, int r40, int r41, int r42, float r43, boolean r44, boolean r45, boolean r46, int r47, int r48, int r49, int r50, float r51, boolean r52) {
        /*
            r27 = this;
            r0 = r27
            r9 = r28
            r10 = r33
            r11 = r34
            r12 = r37
            r13 = r38
            r14 = r41
            r1 = r42
            r2 = r48
            r3 = r49
            r4 = r50
            androidx.constraintlayout.solver.SolverVariable r15 = r9.createObjectVariable(r12)
            androidx.constraintlayout.solver.SolverVariable r8 = r9.createObjectVariable(r13)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r37.getTarget()
            androidx.constraintlayout.solver.SolverVariable r7 = r9.createObjectVariable(r5)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r38.getTarget()
            androidx.constraintlayout.solver.SolverVariable r6 = r9.createObjectVariable(r5)
            androidx.constraintlayout.solver.LinearSystem.getMetrics()
            boolean r16 = r37.isConnected()
            boolean r17 = r38.isConnected()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r0.mCenter
            boolean r18 = r5.isConnected()
            if (r17 == 0) goto L_0x0044
            int r5 = r16 + 1
            goto L_0x0046
        L_0x0044:
            r5 = r16
        L_0x0046:
            if (r18 == 0) goto L_0x004a
            int r5 = r5 + 1
        L_0x004a:
            if (r44 == 0) goto L_0x004f
            r19 = 3
            goto L_0x0051
        L_0x004f:
            r19 = r47
        L_0x0051:
            int[] r20 = androidx.constraintlayout.solver.widgets.ConstraintWidget.AnonymousClass1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour
            int r21 = r35.ordinal()
            r11 = r20[r21]
            r2 = 1
            if (r11 == r2) goto L_0x0065
            r2 = 2
            if (r11 == r2) goto L_0x0065
            r2 = 3
            if (r11 == r2) goto L_0x0065
            r2 = 4
            if (r11 == r2) goto L_0x006a
        L_0x0065:
            r11 = r19
        L_0x0067:
            r19 = 0
            goto L_0x0071
        L_0x006a:
            r11 = r19
            if (r11 != r2) goto L_0x006f
            goto L_0x0067
        L_0x006f:
            r19 = 1
        L_0x0071:
            int r2 = r0.mVisibility
            r22 = r6
            r6 = 8
            if (r2 != r6) goto L_0x007d
            r2 = 0
            r19 = 0
            goto L_0x007f
        L_0x007d:
            r2 = r40
        L_0x007f:
            if (r52 == 0) goto L_0x009a
            if (r16 != 0) goto L_0x008d
            if (r17 != 0) goto L_0x008d
            if (r18 != 0) goto L_0x008d
            r6 = r39
            r9.addEquality(r15, r6)
            goto L_0x009a
        L_0x008d:
            if (r16 == 0) goto L_0x009a
            if (r17 != 0) goto L_0x009a
            int r6 = r37.getMargin()
            r13 = 7
            r9.addEquality(r15, r7, r6, r13)
            goto L_0x009b
        L_0x009a:
            r13 = 7
        L_0x009b:
            if (r19 != 0) goto L_0x00c6
            if (r36 == 0) goto L_0x00b3
            r6 = 3
            r13 = 0
            r9.addEquality(r8, r15, r13, r6)
            r6 = 7
            if (r14 <= 0) goto L_0x00aa
            r9.addGreaterThan(r8, r15, r14, r6)
        L_0x00aa:
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r1 >= r2) goto L_0x00b7
            r9.addLowerThan(r8, r15, r1, r6)
            goto L_0x00b7
        L_0x00b3:
            r6 = r13
            r9.addEquality(r8, r15, r2, r6)
        L_0x00b7:
            r24 = r3
            r2 = r7
            r1 = r8
            r25 = r19
            r14 = r22
            r13 = 7
            r19 = r32
        L_0x00c2:
            r22 = r5
            goto L_0x01c9
        L_0x00c6:
            r1 = 2
            if (r5 == r1) goto L_0x00ea
            if (r44 != 0) goto L_0x00ea
            r1 = 1
            if (r11 == r1) goto L_0x00d0
            if (r11 != 0) goto L_0x00ea
        L_0x00d0:
            int r1 = java.lang.Math.max(r3, r2)
            if (r4 <= 0) goto L_0x00da
            int r1 = java.lang.Math.min(r4, r1)
        L_0x00da:
            r2 = 7
            r9.addEquality(r8, r15, r1, r2)
            r19 = r32
            r13 = r2
            r24 = r3
            r2 = r7
            r1 = r8
            r14 = r22
            r25 = 0
            goto L_0x00c2
        L_0x00ea:
            r1 = -2
            if (r3 != r1) goto L_0x00ef
            r13 = r2
            goto L_0x00f0
        L_0x00ef:
            r13 = r3
        L_0x00f0:
            if (r4 != r1) goto L_0x00f4
            r1 = r2
            goto L_0x00f5
        L_0x00f4:
            r1 = r4
        L_0x00f5:
            if (r2 <= 0) goto L_0x00fb
            r3 = 1
            if (r11 == r3) goto L_0x00fb
            r2 = 0
        L_0x00fb:
            if (r13 <= 0) goto L_0x0105
            r3 = 7
            r9.addGreaterThan(r8, r15, r13, r3)
            int r2 = java.lang.Math.max(r2, r13)
        L_0x0105:
            if (r1 <= 0) goto L_0x011a
            if (r30 == 0) goto L_0x010e
            r3 = 1
            if (r11 != r3) goto L_0x010e
            r3 = 0
            goto L_0x010f
        L_0x010e:
            r3 = 1
        L_0x010f:
            r6 = 7
            if (r3 == 0) goto L_0x0115
            r9.addLowerThan(r8, r15, r1, r6)
        L_0x0115:
            int r2 = java.lang.Math.min(r2, r1)
            goto L_0x011b
        L_0x011a:
            r6 = 7
        L_0x011b:
            r3 = 1
            if (r11 != r3) goto L_0x0146
            if (r30 == 0) goto L_0x0125
            r9.addEquality(r8, r15, r2, r6)
            r4 = 5
            goto L_0x0136
        L_0x0125:
            if (r45 == 0) goto L_0x012f
            r4 = 5
            r9.addEquality(r8, r15, r2, r4)
            r9.addLowerThan(r8, r15, r2, r6)
            goto L_0x0136
        L_0x012f:
            r4 = 5
            r9.addEquality(r8, r15, r2, r4)
            r9.addLowerThan(r8, r15, r2, r6)
        L_0x0136:
            r4 = r1
            r2 = r7
            r1 = r8
            r24 = r13
            r25 = r19
            r14 = r22
            r19 = r32
            r22 = r5
            r13 = r6
            goto L_0x01c9
        L_0x0146:
            r2 = 2
            r4 = 5
            if (r11 != r2) goto L_0x01b6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = r37.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            if (r3 == r2) goto L_0x0174
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = r37.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r3 != r4) goto L_0x015b
            goto L_0x0174
        L_0x015b:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.getAnchor(r3)
            androidx.constraintlayout.solver.SolverVariable r2 = r9.createObjectVariable(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.getAnchor(r4)
            androidx.constraintlayout.solver.SolverVariable r3 = r9.createObjectVariable(r3)
            goto L_0x018a
        L_0x0174:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r3.getAnchor(r2)
            androidx.constraintlayout.solver.SolverVariable r2 = r9.createObjectVariable(r2)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.getAnchor(r4)
            androidx.constraintlayout.solver.SolverVariable r3 = r9.createObjectVariable(r3)
        L_0x018a:
            r19 = r2
            r2 = r3
            androidx.constraintlayout.solver.ArrayRow r3 = r28.createRow()
            r23 = 5
            r4 = r8
            r36 = r1
            r1 = r5
            r5 = r15
            r39 = r13
            r14 = r22
            r13 = r6
            r6 = r2
            r2 = r7
            r7 = r19
            r22 = r1
            r1 = r8
            r8 = r51
            androidx.constraintlayout.solver.ArrayRow r3 = r3.createRowDimensionRatio(r4, r5, r6, r7, r8)
            r9.addConstraint(r3)
            r19 = r32
            r4 = r36
            r24 = r39
            r25 = 0
            goto L_0x01c9
        L_0x01b6:
            r36 = r1
            r2 = r7
            r1 = r8
            r39 = r13
            r14 = r22
            r22 = r5
            r13 = r6
            r4 = r36
            r24 = r39
            r25 = r19
            r19 = 1
        L_0x01c9:
            if (r52 == 0) goto L_0x03a5
            if (r45 == 0) goto L_0x01d6
            r4 = r1
            r2 = r10
            r5 = r22
            r3 = 2
            r1 = r34
            goto L_0x03ac
        L_0x01d6:
            if (r16 != 0) goto L_0x01de
            if (r17 != 0) goto L_0x01de
            if (r18 != 0) goto L_0x01de
            goto L_0x0388
        L_0x01de:
            if (r16 == 0) goto L_0x01e4
            if (r17 != 0) goto L_0x01e4
            goto L_0x0388
        L_0x01e4:
            if (r16 != 0) goto L_0x01f9
            if (r17 == 0) goto L_0x01f9
            int r0 = r38.getMargin()
            int r0 = -r0
            r9.addEquality(r1, r14, r0, r13)
            if (r30 == 0) goto L_0x0388
            r0 = 5
            r3 = 0
            r9.addGreaterThan(r15, r10, r3, r0)
            goto L_0x0388
        L_0x01f9:
            r3 = 0
            if (r16 == 0) goto L_0x0388
            if (r17 == 0) goto L_0x0388
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r12.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r8 = r5.mOwner
            r7 = r38
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r7.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r5.mOwner
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r27.getParent()
            if (r25 == 0) goto L_0x02a7
            if (r11 != 0) goto L_0x023b
            if (r4 != 0) goto L_0x021b
            if (r24 != 0) goto L_0x021b
            r16 = r3
            r0 = r13
            r4 = r0
            r17 = 1
            goto L_0x0221
        L_0x021b:
            r17 = r3
            r0 = 5
            r4 = 5
            r16 = 1
        L_0x0221:
            boolean r3 = r8 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r3 != 0) goto L_0x022f
            boolean r3 = r6 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r3 == 0) goto L_0x022a
            goto L_0x022f
        L_0x022a:
            r3 = r16
            r18 = r17
            goto L_0x0234
        L_0x022f:
            r3 = r16
            r18 = r17
            r4 = 4
        L_0x0234:
            r17 = 5
            r16 = r0
            r0 = 0
            goto L_0x02b0
        L_0x023b:
            r3 = 1
            if (r11 != r3) goto L_0x0245
            r16 = r13
            r0 = 1
            r3 = 1
            r4 = 4
            goto L_0x02ac
        L_0x0245:
            r3 = 3
            if (r11 != r3) goto L_0x02a4
            int r0 = r0.mResolvedDimensionRatioSide
            r3 = -1
            if (r0 != r3) goto L_0x0264
            if (r46 == 0) goto L_0x025a
            r16 = r13
            r0 = 1
            r3 = 1
            r4 = 5
            if (r30 == 0) goto L_0x0257
            goto L_0x02a1
        L_0x0257:
            r17 = 4
            goto L_0x0261
        L_0x025a:
            r16 = r13
            r17 = r16
            r0 = 1
            r3 = 1
            r4 = 5
        L_0x0261:
            r18 = 1
            goto L_0x02b0
        L_0x0264:
            if (r44 == 0) goto L_0x027f
            r0 = r48
            r3 = 2
            if (r0 == r3) goto L_0x0271
            r3 = 1
            if (r0 != r3) goto L_0x026f
            goto L_0x0271
        L_0x026f:
            r0 = 0
            goto L_0x0272
        L_0x0271:
            r0 = 1
        L_0x0272:
            if (r0 != 0) goto L_0x0277
            r3 = r13
            r0 = 5
            goto L_0x0279
        L_0x0277:
            r0 = 4
            r3 = 5
        L_0x0279:
            r4 = r0
            r16 = r3
            r0 = 1
            r3 = 1
            goto L_0x02a1
        L_0x027f:
            if (r4 <= 0) goto L_0x0285
            r0 = 1
            r3 = 1
            r4 = 5
            goto L_0x029f
        L_0x0285:
            if (r4 != 0) goto L_0x029c
            if (r24 != 0) goto L_0x029c
            if (r46 != 0) goto L_0x028f
            r4 = r13
            r0 = 1
            r3 = 1
            goto L_0x029f
        L_0x028f:
            if (r8 == r5) goto L_0x0295
            if (r6 == r5) goto L_0x0295
            r0 = 4
            goto L_0x0296
        L_0x0295:
            r0 = 5
        L_0x0296:
            r16 = r0
            r0 = 1
            r3 = 1
            r4 = 4
            goto L_0x02a1
        L_0x029c:
            r0 = 1
            r3 = 1
            r4 = 4
        L_0x029f:
            r16 = 5
        L_0x02a1:
            r17 = 5
            goto L_0x0261
        L_0x02a4:
            r0 = 0
            r3 = 0
            goto L_0x02a9
        L_0x02a7:
            r0 = 1
            r3 = 1
        L_0x02a9:
            r4 = 4
            r16 = 5
        L_0x02ac:
            r17 = 5
            r18 = 0
        L_0x02b0:
            if (r0 == 0) goto L_0x02bb
            if (r2 != r14) goto L_0x02bb
            if (r8 == r5) goto L_0x02bb
            r21 = 0
            r22 = 0
            goto L_0x02bf
        L_0x02bb:
            r22 = r0
            r21 = 1
        L_0x02bf:
            if (r3 == 0) goto L_0x02e4
            int r3 = r37.getMargin()
            int r26 = r38.getMargin()
            r0 = r28
            r32 = r1
            r1 = r15
            r27 = r2
            r12 = 0
            r12 = r4
            r4 = r43
            r13 = r5
            r5 = r14
            r35 = r11
            r11 = r6
            r6 = r32
            r7 = r26
            r10 = r8
            r8 = r17
            r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x02ee
        L_0x02e4:
            r32 = r1
            r27 = r2
            r12 = r4
            r13 = r5
            r10 = r8
            r35 = r11
            r11 = r6
        L_0x02ee:
            r0 = 6
            r1 = r27
            if (r22 == 0) goto L_0x0317
            if (r30 == 0) goto L_0x0303
            if (r1 == r14) goto L_0x0303
            if (r25 != 0) goto L_0x0303
            boolean r2 = r10 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r2 != 0) goto L_0x0301
            boolean r2 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r2 == 0) goto L_0x0303
        L_0x0301:
            r2 = r0
            goto L_0x0305
        L_0x0303:
            r2 = r16
        L_0x0305:
            int r3 = r37.getMargin()
            r9.addGreaterThan(r15, r1, r3, r2)
            int r3 = r38.getMargin()
            int r3 = -r3
            r4 = r32
            r9.addLowerThan(r4, r14, r3, r2)
            goto L_0x031b
        L_0x0317:
            r4 = r32
            r2 = r16
        L_0x031b:
            if (r21 == 0) goto L_0x035a
            if (r18 == 0) goto L_0x0345
            if (r46 == 0) goto L_0x0323
            if (r31 == 0) goto L_0x0345
        L_0x0323:
            if (r10 == r13) goto L_0x032a
            if (r11 != r13) goto L_0x0328
            goto L_0x032a
        L_0x0328:
            r6 = r12
            goto L_0x032b
        L_0x032a:
            r6 = r0
        L_0x032b:
            boolean r0 = r10 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r0 != 0) goto L_0x0333
            boolean r0 = r11 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r0 == 0) goto L_0x0334
        L_0x0333:
            r6 = 5
        L_0x0334:
            boolean r0 = r10 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r0 != 0) goto L_0x033c
            boolean r0 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r0 == 0) goto L_0x033d
        L_0x033c:
            r6 = 5
        L_0x033d:
            if (r46 == 0) goto L_0x0340
            r6 = 5
        L_0x0340:
            int r0 = java.lang.Math.max(r6, r12)
            r12 = r0
        L_0x0345:
            if (r30 == 0) goto L_0x034b
            int r12 = java.lang.Math.min(r2, r12)
        L_0x034b:
            int r0 = r37.getMargin()
            r9.addEquality(r15, r1, r0, r12)
            int r0 = r38.getMargin()
            int r0 = -r0
            r9.addEquality(r4, r14, r0, r12)
        L_0x035a:
            if (r30 == 0) goto L_0x036c
            r2 = r33
            if (r2 != r1) goto L_0x0365
            int r0 = r37.getMargin()
            goto L_0x0366
        L_0x0365:
            r0 = 0
        L_0x0366:
            if (r1 == r2) goto L_0x036c
            r1 = 5
            r9.addGreaterThan(r15, r2, r0, r1)
        L_0x036c:
            if (r30 == 0) goto L_0x0389
            if (r25 == 0) goto L_0x0389
            r0 = r14
            if (r41 != 0) goto L_0x038a
            if (r24 != 0) goto L_0x038a
            if (r25 == 0) goto L_0x0382
            r11 = r35
            r1 = 3
            if (r11 != r1) goto L_0x0382
            r1 = 7
            r2 = 0
            r9.addGreaterThan(r4, r15, r2, r1)
            goto L_0x038a
        L_0x0382:
            r2 = 0
            r1 = 5
            r9.addGreaterThan(r4, r15, r2, r1)
            goto L_0x038a
        L_0x0388:
            r4 = r1
        L_0x0389:
            r0 = r14
        L_0x038a:
            if (r30 == 0) goto L_0x03a4
            if (r19 == 0) goto L_0x03a4
            r1 = r38
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r1.mTarget
            if (r2 == 0) goto L_0x039b
            int r2 = r38.getMargin()
            r1 = r34
            goto L_0x039e
        L_0x039b:
            r1 = r34
            r2 = 0
        L_0x039e:
            if (r0 == r1) goto L_0x03a4
            r0 = 5
            r9.addGreaterThan(r1, r4, r2, r0)
        L_0x03a4:
            return
        L_0x03a5:
            r4 = r1
            r2 = r10
            r3 = 2
            r1 = r34
            r5 = r22
        L_0x03ac:
            if (r5 >= r3) goto L_0x03ec
            if (r30 == 0) goto L_0x03ec
            if (r19 == 0) goto L_0x03ec
            r3 = 7
            r5 = 0
            r9.addGreaterThan(r15, r2, r5, r3)
            if (r29 != 0) goto L_0x03c2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r0.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x03c0
            goto L_0x03c2
        L_0x03c0:
            r2 = 0
            goto L_0x03c3
        L_0x03c2:
            r2 = 1
        L_0x03c3:
            if (r29 != 0) goto L_0x03e5
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x03e5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.mOwner
            float r2 = r0.mDimensionRatio
            r3 = 0
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 == 0) goto L_0x03e4
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r2 = 0
            r3 = r0[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r3 != r2) goto L_0x03e4
            r3 = 1
            r0 = r0[r3]
            if (r0 != r2) goto L_0x03e4
            r2 = r3
            goto L_0x03e5
        L_0x03e4:
            r2 = 0
        L_0x03e5:
            if (r2 == 0) goto L_0x03ec
            r0 = 7
            r2 = 0
            r9.addGreaterThan(r1, r4, r2, r0)
        L_0x03ec:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.solver.LinearSystem, boolean, boolean, boolean, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.ConstraintWidget$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type;
        public static final /* synthetic */ int[] $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour;

        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(31:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(32:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x008f */
        static {
            /*
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour = r0
                r1 = 1
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour     // Catch:{ NoSuchFieldError -> 0x001d }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour     // Catch:{ NoSuchFieldError -> 0x0028 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour     // Catch:{ NoSuchFieldError -> 0x0033 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type[] r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type = r4
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT     // Catch:{ NoSuchFieldError -> 0x0044 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0044 }
                r4[r5] = r1     // Catch:{ NoSuchFieldError -> 0x0044 }
            L_0x0044:
                int[] r1 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x004e }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP     // Catch:{ NoSuchFieldError -> 0x004e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r1[r4] = r0     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x0058 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x0062 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x006d }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE     // Catch:{ NoSuchFieldError -> 0x006d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006d }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006d }
            L_0x006d:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x0078 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x0083 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_X     // Catch:{ NoSuchFieldError -> 0x0083 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0083 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0083 }
            L_0x0083:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x008f }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_Y     // Catch:{ NoSuchFieldError -> 0x008f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x008f }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x008f }
            L_0x008f:
                int[] r0 = $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type     // Catch:{ NoSuchFieldError -> 0x009b }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.NONE     // Catch:{ NoSuchFieldError -> 0x009b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009b }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009b }
            L_0x009b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.AnonymousClass1.<clinit>():void");
        }
    }

    public void updateFromSolver(LinearSystem linearSystem) {
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        DependencyNode dependencyNode = horizontalWidgetRun.start;
        if (dependencyNode.resolved) {
            DependencyNode dependencyNode2 = horizontalWidgetRun.end;
            if (dependencyNode2.resolved) {
                objectVariableValue = dependencyNode.value;
                objectVariableValue3 = dependencyNode2.value;
            }
        }
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        DependencyNode dependencyNode3 = verticalWidgetRun.start;
        if (dependencyNode3.resolved) {
            DependencyNode dependencyNode4 = verticalWidgetRun.end;
            if (dependencyNode4.resolved) {
                objectVariableValue2 = dependencyNode3.value;
                objectVariableValue4 = dependencyNode4.value;
            }
        }
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue4 = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }

    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.mHorizontalResolution = constraintWidget.mHorizontalResolution;
        this.mVerticalResolution = constraintWidget.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = constraintWidget.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = constraintWidget.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = constraintWidget.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = constraintWidget.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = constraintWidget.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = constraintWidget.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = constraintWidget.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = constraintWidget.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = constraintWidget.mIsWidthWrapContent;
        this.mIsHeightWrapContent = constraintWidget.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = constraintWidget.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = constraintWidget.mResolvedDimensionRatio;
        int[] iArr3 = constraintWidget.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = constraintWidget.mCircleConstraintAngle;
        this.hasBaseline = constraintWidget.hasBaseline;
        this.inPlaceholder = constraintWidget.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget2 = null;
        this.mParent = this.mParent == null ? null : hashMap.get(constraintWidget.mParent);
        this.mWidth = constraintWidget.mWidth;
        this.mHeight = constraintWidget.mHeight;
        this.mDimensionRatio = constraintWidget.mDimensionRatio;
        this.mDimensionRatioSide = constraintWidget.mDimensionRatioSide;
        this.mX = constraintWidget.mX;
        this.mY = constraintWidget.mY;
        this.mRelX = constraintWidget.mRelX;
        this.mRelY = constraintWidget.mRelY;
        this.mOffsetX = constraintWidget.mOffsetX;
        this.mOffsetY = constraintWidget.mOffsetY;
        this.mBaselineDistance = constraintWidget.mBaselineDistance;
        this.mMinWidth = constraintWidget.mMinWidth;
        this.mMinHeight = constraintWidget.mMinHeight;
        this.mHorizontalBiasPercent = constraintWidget.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = constraintWidget.mVerticalBiasPercent;
        this.mCompanionWidget = constraintWidget.mCompanionWidget;
        this.mContainerItemSkip = constraintWidget.mContainerItemSkip;
        this.mVisibility = constraintWidget.mVisibility;
        this.mDebugName = constraintWidget.mDebugName;
        this.mType = constraintWidget.mType;
        this.mDistToTop = constraintWidget.mDistToTop;
        this.mDistToLeft = constraintWidget.mDistToLeft;
        this.mDistToRight = constraintWidget.mDistToRight;
        this.mDistToBottom = constraintWidget.mDistToBottom;
        this.mLeftHasCentered = constraintWidget.mLeftHasCentered;
        this.mRightHasCentered = constraintWidget.mRightHasCentered;
        this.mTopHasCentered = constraintWidget.mTopHasCentered;
        this.mBottomHasCentered = constraintWidget.mBottomHasCentered;
        this.mHorizontalWrapVisited = constraintWidget.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = constraintWidget.mVerticalWrapVisited;
        this.mOptimizerMeasurable = constraintWidget.mOptimizerMeasurable;
        this.mGroupsToSolver = constraintWidget.mGroupsToSolver;
        this.mHorizontalChainStyle = constraintWidget.mHorizontalChainStyle;
        this.mVerticalChainStyle = constraintWidget.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = constraintWidget.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = constraintWidget.mVerticalChainFixedPosition;
        float[] fArr = this.mWeight;
        float[] fArr2 = constraintWidget.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget3 = constraintWidget.mHorizontalNextWidget;
        this.mHorizontalNextWidget = constraintWidget3 == null ? null : hashMap.get(constraintWidget3);
        ConstraintWidget constraintWidget4 = constraintWidget.mVerticalNextWidget;
        if (constraintWidget4 != null) {
            constraintWidget2 = hashMap.get(constraintWidget4);
        }
        this.mVerticalNextWidget = constraintWidget2;
    }

    public void updateFromRuns(boolean z, boolean z2) {
        int i;
        int i2;
        boolean isResolved = z & this.horizontalRun.isResolved();
        boolean isResolved2 = z2 & this.verticalRun.isResolved();
        HorizontalWidgetRun horizontalWidgetRun = this.horizontalRun;
        int i3 = horizontalWidgetRun.start.value;
        VerticalWidgetRun verticalWidgetRun = this.verticalRun;
        int i4 = verticalWidgetRun.start.value;
        int i5 = horizontalWidgetRun.end.value;
        int i6 = verticalWidgetRun.end.value;
        int i7 = i6 - i4;
        if (i5 - i3 < 0 || i7 < 0 || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE || i5 == Integer.MIN_VALUE || i5 == Integer.MAX_VALUE || i6 == Integer.MIN_VALUE || i6 == Integer.MAX_VALUE) {
            i5 = 0;
            i3 = 0;
            i6 = 0;
            i4 = 0;
        }
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        if (isResolved) {
            this.mX = i3;
        }
        if (isResolved2) {
            this.mY = i4;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (isResolved) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i8 < (i2 = this.mWidth)) {
                i8 = i2;
            }
            this.mWidth = i8;
            int i10 = this.mMinWidth;
            if (i8 < i10) {
                this.mWidth = i10;
            }
        }
        if (isResolved2) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i9 < (i = this.mHeight)) {
                i9 = i;
            }
            this.mHeight = i9;
            int i11 = this.mMinHeight;
            if (i9 < i11) {
                this.mHeight = i11;
            }
        }
    }
}
