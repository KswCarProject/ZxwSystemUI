package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R$id;
import androidx.constraintlayout.widget.R$styleable;
import androidx.constraintlayout.widget.StateSet;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class MotionScene {
    public boolean DEBUG_DESKTOP = false;
    public ArrayList<Transition> mAbstractTransitionList = new ArrayList<>();
    public HashMap<String, Integer> mConstraintSetIdMap = new HashMap<>();
    public SparseArray<ConstraintSet> mConstraintSetMap = new SparseArray<>();
    public Transition mCurrentTransition = null;
    public int mDefaultDuration = 100;
    public Transition mDefaultTransition = null;
    public SparseIntArray mDeriveMap = new SparseIntArray();
    public boolean mDisableAutoTransition = false;
    public MotionEvent mLastTouchDown;
    public float mLastTouchX;
    public float mLastTouchY;
    public int mLayoutDuringTransition = 0;
    public final MotionLayout mMotionLayout;
    public boolean mMotionOutsideRegion = false;
    public boolean mRtl;
    public StateSet mStateSet = null;
    public ArrayList<Transition> mTransitionList = new ArrayList<>();
    public MotionLayout.MotionTracker mVelocityTracker;

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0013, code lost:
        if (r2 != -1) goto L_0x0018;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTransition(int r7, int r8) {
        /*
            r6 = this;
            androidx.constraintlayout.widget.StateSet r0 = r6.mStateSet
            r1 = -1
            if (r0 == 0) goto L_0x0016
            int r0 = r0.stateGetConstraintID(r7, r1, r1)
            if (r0 == r1) goto L_0x000c
            goto L_0x000d
        L_0x000c:
            r0 = r7
        L_0x000d:
            androidx.constraintlayout.widget.StateSet r2 = r6.mStateSet
            int r2 = r2.stateGetConstraintID(r8, r1, r1)
            if (r2 == r1) goto L_0x0017
            goto L_0x0018
        L_0x0016:
            r0 = r7
        L_0x0017:
            r2 = r8
        L_0x0018:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r3 = r6.mTransitionList
            java.util.Iterator r3 = r3.iterator()
        L_0x001e:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0058
            java.lang.Object r4 = r3.next()
            androidx.constraintlayout.motion.widget.MotionScene$Transition r4 = (androidx.constraintlayout.motion.widget.MotionScene.Transition) r4
            int r5 = r4.mConstraintSetEnd
            if (r5 != r2) goto L_0x0036
            int r5 = r4.mConstraintSetStart
            if (r5 == r0) goto L_0x0042
        L_0x0036:
            int r5 = r4.mConstraintSetEnd
            if (r5 != r8) goto L_0x001e
            int r5 = r4.mConstraintSetStart
            if (r5 != r7) goto L_0x001e
        L_0x0042:
            r6.mCurrentTransition = r4
            if (r4 == 0) goto L_0x0057
            androidx.constraintlayout.motion.widget.TouchResponse r7 = r4.mTouchResponse
            if (r7 == 0) goto L_0x0057
            androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r6.mCurrentTransition
            androidx.constraintlayout.motion.widget.TouchResponse r7 = r7.mTouchResponse
            boolean r6 = r6.mRtl
            r7.setRTL(r6)
        L_0x0057:
            return
        L_0x0058:
            androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r6.mDefaultTransition
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r3 = r6.mAbstractTransitionList
            java.util.Iterator r3 = r3.iterator()
        L_0x0060:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0074
            java.lang.Object r4 = r3.next()
            androidx.constraintlayout.motion.widget.MotionScene$Transition r4 = (androidx.constraintlayout.motion.widget.MotionScene.Transition) r4
            int r5 = r4.mConstraintSetEnd
            if (r5 != r8) goto L_0x0060
            r7 = r4
            goto L_0x0060
        L_0x0074:
            androidx.constraintlayout.motion.widget.MotionScene$Transition r8 = new androidx.constraintlayout.motion.widget.MotionScene$Transition
            r8.<init>(r6, r7)
            int unused = r8.mConstraintSetStart = r0
            int unused = r8.mConstraintSetEnd = r2
            if (r0 == r1) goto L_0x0086
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r7 = r6.mTransitionList
            r7.add(r8)
        L_0x0086:
            r6.mCurrentTransition = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.setTransition(int, int):void");
    }

    public void setTransition(Transition transition) {
        this.mCurrentTransition = transition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r1 = r1.stateGetConstraintID(r2, -1, -1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int getRealID(int r2) {
        /*
            r1 = this;
            androidx.constraintlayout.widget.StateSet r1 = r1.mStateSet
            if (r1 == 0) goto L_0x000c
            r0 = -1
            int r1 = r1.stateGetConstraintID(r2, r0, r0)
            if (r1 == r0) goto L_0x000c
            return r1
        L_0x000c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.getRealID(int):int");
    }

    public List<Transition> getTransitionsWithState(int i) {
        int realID = getRealID(i);
        ArrayList arrayList = new ArrayList();
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mConstraintSetStart == realID || next.mConstraintSetEnd == realID) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public void addOnClickListeners(MotionLayout motionLayout, int i) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mOnClicks.size() > 0) {
                Iterator it2 = next.mOnClicks.iterator();
                while (it2.hasNext()) {
                    ((Transition.TransitionOnClick) it2.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it3 = this.mAbstractTransitionList.iterator();
        while (it3.hasNext()) {
            Transition next2 = it3.next();
            if (next2.mOnClicks.size() > 0) {
                Iterator it4 = next2.mOnClicks.iterator();
                while (it4.hasNext()) {
                    ((Transition.TransitionOnClick) it4.next()).removeOnClickListeners(motionLayout);
                }
            }
        }
        Iterator<Transition> it5 = this.mTransitionList.iterator();
        while (it5.hasNext()) {
            Transition next3 = it5.next();
            if (next3.mOnClicks.size() > 0) {
                Iterator it6 = next3.mOnClicks.iterator();
                while (it6.hasNext()) {
                    ((Transition.TransitionOnClick) it6.next()).addOnClickListeners(motionLayout, i, next3);
                }
            }
        }
        Iterator<Transition> it7 = this.mAbstractTransitionList.iterator();
        while (it7.hasNext()) {
            Transition next4 = it7.next();
            if (next4.mOnClicks.size() > 0) {
                Iterator it8 = next4.mOnClicks.iterator();
                while (it8.hasNext()) {
                    ((Transition.TransitionOnClick) it8.next()).addOnClickListeners(motionLayout, i, next4);
                }
            }
        }
    }

    public Transition bestTransitionFor(int i, float f, float f2, MotionEvent motionEvent) {
        RectF touchRegion;
        if (i == -1) {
            return this.mCurrentTransition;
        }
        List<Transition> transitionsWithState = getTransitionsWithState(i);
        float f3 = 0.0f;
        Transition transition = null;
        RectF rectF = new RectF();
        for (Transition next : transitionsWithState) {
            if (!next.mDisable && next.mTouchResponse != null) {
                next.mTouchResponse.setRTL(this.mRtl);
                RectF touchRegion2 = next.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF);
                if ((touchRegion2 == null || motionEvent == null || touchRegion2.contains(motionEvent.getX(), motionEvent.getY())) && ((touchRegion = next.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF)) == null || motionEvent == null || touchRegion.contains(motionEvent.getX(), motionEvent.getY()))) {
                    float dot = next.mTouchResponse.dot(f, f2) * (next.mConstraintSetEnd == i ? -1.0f : 1.1f);
                    if (dot > f3) {
                        transition = next;
                        f3 = dot;
                    }
                }
            }
        }
        return transition;
    }

    public ArrayList<Transition> getDefinedTransitions() {
        return this.mTransitionList;
    }

    public Transition getTransitionById(int i) {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mId == i) {
                return next;
            }
        }
        return null;
    }

    public boolean autoTransition(MotionLayout motionLayout, int i) {
        if (isProcessingTouch() || this.mDisableAutoTransition) {
            return false;
        }
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            Transition next = it.next();
            if (next.mAutoTransition != 0) {
                if (i == next.mConstraintSetStart && (next.mAutoTransition == 4 || next.mAutoTransition == 2)) {
                    motionLayout.setTransition(next);
                    if (next.mAutoTransition == 4) {
                        motionLayout.transitionToEnd();
                    } else {
                        motionLayout.setProgress(1.0f);
                    }
                    return true;
                } else if (i == next.mConstraintSetEnd && (next.mAutoTransition == 3 || next.mAutoTransition == 1)) {
                    motionLayout.setTransition(next);
                    if (next.mAutoTransition == 3) {
                        motionLayout.transitionToStart();
                    } else {
                        motionLayout.setProgress(0.0f);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean isProcessingTouch() {
        return this.mVelocityTracker != null;
    }

    public void setRtl(boolean z) {
        this.mRtl = z;
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    public static class Transition {
        public int mAutoTransition = 0;
        public int mConstraintSetEnd = -1;
        public int mConstraintSetStart = -1;
        public int mDefaultInterpolator = 0;
        public int mDefaultInterpolatorID = -1;
        public String mDefaultInterpolatorString = null;
        public boolean mDisable = false;
        public int mDuration = 400;
        public int mId = -1;
        public boolean mIsAbstract = false;
        public ArrayList<KeyFrames> mKeyFramesList = new ArrayList<>();
        public int mLayoutDuringTransition = 0;
        public final MotionScene mMotionScene;
        public ArrayList<TransitionOnClick> mOnClicks = new ArrayList<>();
        public int mPathMotionArc = -1;
        public float mStagger = 0.0f;
        public TouchResponse mTouchResponse = null;

        public int getLayoutDuringTransition() {
            return this.mLayoutDuringTransition;
        }

        public void addOnClick(Context context, XmlPullParser xmlPullParser) {
            this.mOnClicks.add(new TransitionOnClick(context, this, xmlPullParser));
        }

        public int getEndConstraintSetId() {
            return this.mConstraintSetEnd;
        }

        public int getStartConstraintSetId() {
            return this.mConstraintSetStart;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public TouchResponse getTouchResponse() {
            return this.mTouchResponse;
        }

        public boolean isEnabled() {
            return !this.mDisable;
        }

        public String debugString(Context context) {
            String str;
            if (this.mConstraintSetStart == -1) {
                str = "null";
            } else {
                str = context.getResources().getResourceEntryName(this.mConstraintSetStart);
            }
            if (this.mConstraintSetEnd == -1) {
                return str + " -> null";
            }
            return str + " -> " + context.getResources().getResourceEntryName(this.mConstraintSetEnd);
        }

        public static class TransitionOnClick implements View.OnClickListener {
            public int mMode = 17;
            public int mTargetId = -1;
            public final Transition mTransition;

            public TransitionOnClick(Context context, Transition transition, XmlPullParser xmlPullParser) {
                this.mTransition = transition;
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.OnClick);
                int indexCount = obtainStyledAttributes.getIndexCount();
                for (int i = 0; i < indexCount; i++) {
                    int index = obtainStyledAttributes.getIndex(i);
                    if (index == R$styleable.OnClick_targetId) {
                        this.mTargetId = obtainStyledAttributes.getResourceId(index, this.mTargetId);
                    } else if (index == R$styleable.OnClick_clickAction) {
                        this.mMode = obtainStyledAttributes.getInt(index, this.mMode);
                    }
                }
                obtainStyledAttributes.recycle();
            }

            public void addOnClickListeners(MotionLayout motionLayout, int i, Transition transition) {
                int i2 = this.mTargetId;
                View view = motionLayout;
                if (i2 != -1) {
                    view = motionLayout.findViewById(i2);
                }
                if (view == null) {
                    Log.e("MotionScene", "OnClick could not find id " + this.mTargetId);
                    return;
                }
                int access$100 = transition.mConstraintSetStart;
                int access$000 = transition.mConstraintSetEnd;
                if (access$100 == -1) {
                    view.setOnClickListener(this);
                    return;
                }
                int i3 = this.mMode;
                boolean z = false;
                boolean z2 = ((i3 & 1) != 0 && i == access$100) | ((i3 & 1) != 0 && i == access$100) | ((i3 & 256) != 0 && i == access$100) | ((i3 & 16) != 0 && i == access$000);
                if ((i3 & 4096) != 0 && i == access$000) {
                    z = true;
                }
                if (z2 || z) {
                    view.setOnClickListener(this);
                }
            }

            public void removeOnClickListeners(MotionLayout motionLayout) {
                View findViewById = motionLayout.findViewById(this.mTargetId);
                if (findViewById == null) {
                    Log.e("MotionScene", " (*)  could not find id " + this.mTargetId);
                    return;
                }
                findViewById.setOnClickListener((View.OnClickListener) null);
            }

            public boolean isTransitionViable(Transition transition, MotionLayout motionLayout) {
                Transition transition2 = this.mTransition;
                if (transition2 == transition) {
                    return true;
                }
                int access$000 = transition2.mConstraintSetEnd;
                int access$100 = this.mTransition.mConstraintSetStart;
                if (access$100 != -1) {
                    int i = motionLayout.mCurrentState;
                    if (i == access$100 || i == access$000) {
                        return true;
                    }
                    return false;
                } else if (motionLayout.mCurrentState != access$000) {
                    return true;
                } else {
                    return false;
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:39:0x00a3  */
            /* JADX WARNING: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onClick(android.view.View r8) {
                /*
                    r7 = this;
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r8 = r7.mTransition
                    androidx.constraintlayout.motion.widget.MotionScene r8 = r8.mMotionScene
                    androidx.constraintlayout.motion.widget.MotionLayout r8 = r8.mMotionLayout
                    boolean r0 = r8.isInteractionEnabled()
                    if (r0 != 0) goto L_0x0011
                    return
                L_0x0011:
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r0 = r7.mTransition
                    int r0 = r0.mConstraintSetStart
                    r1 = -1
                    if (r0 != r1) goto L_0x004a
                    int r0 = r8.getCurrentState()
                    if (r0 != r1) goto L_0x002a
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    int r7 = r7.mConstraintSetEnd
                    r8.transitionToState(r7)
                    return
                L_0x002a:
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r1 = new androidx.constraintlayout.motion.widget.MotionScene$Transition
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r2 = r7.mTransition
                    androidx.constraintlayout.motion.widget.MotionScene r2 = r2.mMotionScene
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r3 = r7.mTransition
                    r1.<init>(r2, r3)
                    int unused = r1.mConstraintSetStart = r0
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    int r7 = r7.mConstraintSetEnd
                    int unused = r1.mConstraintSetEnd = r7
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r1)
                    r8.transitionToEnd()
                    return
                L_0x004a:
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r0 = r7.mTransition
                    androidx.constraintlayout.motion.widget.MotionScene r0 = r0.mMotionScene
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r0 = r0.mCurrentTransition
                    int r1 = r7.mMode
                    r2 = r1 & 1
                    r3 = 0
                    r4 = 1
                    if (r2 != 0) goto L_0x0061
                    r2 = r1 & 256(0x100, float:3.59E-43)
                    if (r2 == 0) goto L_0x005f
                    goto L_0x0061
                L_0x005f:
                    r2 = r3
                    goto L_0x0062
                L_0x0061:
                    r2 = r4
                L_0x0062:
                    r5 = r1 & 16
                    if (r5 != 0) goto L_0x006d
                    r1 = r1 & 4096(0x1000, float:5.74E-42)
                    if (r1 == 0) goto L_0x006b
                    goto L_0x006d
                L_0x006b:
                    r1 = r3
                    goto L_0x006e
                L_0x006d:
                    r1 = r4
                L_0x006e:
                    if (r2 == 0) goto L_0x0074
                    if (r1 == 0) goto L_0x0074
                    r5 = r4
                    goto L_0x0075
                L_0x0074:
                    r5 = r3
                L_0x0075:
                    if (r5 == 0) goto L_0x009c
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r5 = r7.mTransition
                    androidx.constraintlayout.motion.widget.MotionScene r5 = r5.mMotionScene
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r5 = r5.mCurrentTransition
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r6 = r7.mTransition
                    if (r5 == r6) goto L_0x0086
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r6)
                L_0x0086:
                    int r5 = r8.getCurrentState()
                    int r6 = r8.getEndState()
                    if (r5 == r6) goto L_0x009d
                    float r5 = r8.getProgress()
                    r6 = 1056964608(0x3f000000, float:0.5)
                    int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
                    if (r5 <= 0) goto L_0x009b
                    goto L_0x009d
                L_0x009b:
                    r1 = r3
                L_0x009c:
                    r3 = r2
                L_0x009d:
                    boolean r0 = r7.isTransitionViable(r0, r8)
                    if (r0 == 0) goto L_0x00e8
                    if (r3 == 0) goto L_0x00b3
                    int r0 = r7.mMode
                    r0 = r0 & r4
                    if (r0 == 0) goto L_0x00b3
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r7)
                    r8.transitionToEnd()
                    goto L_0x00e8
                L_0x00b3:
                    if (r1 == 0) goto L_0x00c4
                    int r0 = r7.mMode
                    r0 = r0 & 16
                    if (r0 == 0) goto L_0x00c4
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r7)
                    r8.transitionToStart()
                    goto L_0x00e8
                L_0x00c4:
                    if (r3 == 0) goto L_0x00d7
                    int r0 = r7.mMode
                    r0 = r0 & 256(0x100, float:3.59E-43)
                    if (r0 == 0) goto L_0x00d7
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r7)
                    r7 = 1065353216(0x3f800000, float:1.0)
                    r8.setProgress(r7)
                    goto L_0x00e8
                L_0x00d7:
                    if (r1 == 0) goto L_0x00e8
                    int r0 = r7.mMode
                    r0 = r0 & 4096(0x1000, float:5.74E-42)
                    if (r0 == 0) goto L_0x00e8
                    androidx.constraintlayout.motion.widget.MotionScene$Transition r7 = r7.mTransition
                    r8.setTransition((androidx.constraintlayout.motion.widget.MotionScene.Transition) r7)
                    r7 = 0
                    r8.setProgress(r7)
                L_0x00e8:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick.onClick(android.view.View):void");
            }
        }

        public Transition(MotionScene motionScene, Transition transition) {
            this.mMotionScene = motionScene;
            if (transition != null) {
                this.mPathMotionArc = transition.mPathMotionArc;
                this.mDefaultInterpolator = transition.mDefaultInterpolator;
                this.mDefaultInterpolatorString = transition.mDefaultInterpolatorString;
                this.mDefaultInterpolatorID = transition.mDefaultInterpolatorID;
                this.mDuration = transition.mDuration;
                this.mKeyFramesList = transition.mKeyFramesList;
                this.mStagger = transition.mStagger;
                this.mLayoutDuringTransition = transition.mLayoutDuringTransition;
            }
        }

        public Transition(MotionScene motionScene, Context context, XmlPullParser xmlPullParser) {
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
            this.mMotionScene = motionScene;
            fillFromAttributeList(motionScene, context, Xml.asAttributeSet(xmlPullParser));
        }

        public final void fillFromAttributeList(MotionScene motionScene, Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Transition);
            fill(motionScene, context, obtainStyledAttributes);
            obtainStyledAttributes.recycle();
        }

        public final void fill(MotionScene motionScene, Context context, TypedArray typedArray) {
            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = typedArray.getIndex(i);
                if (index == R$styleable.Transition_constraintSetEnd) {
                    this.mConstraintSetEnd = typedArray.getResourceId(index, this.mConstraintSetEnd);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetEnd))) {
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.load(context, this.mConstraintSetEnd);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetEnd, constraintSet);
                    }
                } else if (index == R$styleable.Transition_constraintSetStart) {
                    this.mConstraintSetStart = typedArray.getResourceId(index, this.mConstraintSetStart);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetStart))) {
                        ConstraintSet constraintSet2 = new ConstraintSet();
                        constraintSet2.load(context, this.mConstraintSetStart);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetStart, constraintSet2);
                    }
                } else if (index == R$styleable.Transition_motionInterpolator) {
                    int i2 = typedArray.peekValue(index).type;
                    if (i2 == 1) {
                        int resourceId = typedArray.getResourceId(index, -1);
                        this.mDefaultInterpolatorID = resourceId;
                        if (resourceId != -1) {
                            this.mDefaultInterpolator = -2;
                        }
                    } else if (i2 == 3) {
                        String string = typedArray.getString(index);
                        this.mDefaultInterpolatorString = string;
                        if (string.indexOf("/") > 0) {
                            this.mDefaultInterpolatorID = typedArray.getResourceId(index, -1);
                            this.mDefaultInterpolator = -2;
                        } else {
                            this.mDefaultInterpolator = -1;
                        }
                    } else {
                        this.mDefaultInterpolator = typedArray.getInteger(index, this.mDefaultInterpolator);
                    }
                } else if (index == R$styleable.Transition_duration) {
                    this.mDuration = typedArray.getInt(index, this.mDuration);
                } else if (index == R$styleable.Transition_staggered) {
                    this.mStagger = typedArray.getFloat(index, this.mStagger);
                } else if (index == R$styleable.Transition_autoTransition) {
                    this.mAutoTransition = typedArray.getInteger(index, this.mAutoTransition);
                } else if (index == R$styleable.Transition_android_id) {
                    this.mId = typedArray.getResourceId(index, this.mId);
                } else if (index == R$styleable.Transition_transitionDisable) {
                    this.mDisable = typedArray.getBoolean(index, this.mDisable);
                } else if (index == R$styleable.Transition_pathMotionArc) {
                    this.mPathMotionArc = typedArray.getInteger(index, -1);
                } else if (index == R$styleable.Transition_layoutDuringTransition) {
                    this.mLayoutDuringTransition = typedArray.getInteger(index, 0);
                }
            }
            if (this.mConstraintSetStart == -1) {
                this.mIsAbstract = true;
            }
        }
    }

    public MotionScene(Context context, MotionLayout motionLayout, int i) {
        this.mMotionLayout = motionLayout;
        load(context, i);
        SparseArray<ConstraintSet> sparseArray = this.mConstraintSetMap;
        int i2 = R$id.motion_base;
        sparseArray.put(i2, new ConstraintSet());
        this.mConstraintSetIdMap.put("motion_base", Integer.valueOf(i2));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void load(android.content.Context r9, int r10) {
        /*
            r8 = this;
            android.content.res.Resources r0 = r9.getResources()
            android.content.res.XmlResourceParser r0 = r0.getXml(r10)
            r1 = 0
            int r2 = r0.getEventType()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x000d:
            r3 = 1
            if (r2 == r3) goto L_0x0151
            if (r2 == 0) goto L_0x013f
            r4 = 2
            if (r2 == r4) goto L_0x0017
            goto L_0x0142
        L_0x0017:
            java.lang.String r2 = r0.getName()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            boolean r5 = r8.DEBUG_DESKTOP     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r5 == 0) goto L_0x0035
            java.io.PrintStream r5 = java.lang.System.out     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r6.<init>()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r7 = "parsing = "
            r6.append(r7)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r6.append(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r6 = r6.toString()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r5.println(r6)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x0035:
            int r5 = r2.hashCode()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r6 = -1
            java.lang.String r7 = "MotionScene"
            switch(r5) {
                case -1349929691: goto L_0x0079;
                case -1239391468: goto L_0x006f;
                case 269306229: goto L_0x0066;
                case 312750793: goto L_0x005c;
                case 327855227: goto L_0x0052;
                case 793277014: goto L_0x004a;
                case 1382829617: goto L_0x0040;
                default: goto L_0x003f;
            }
        L_0x003f:
            goto L_0x0083
        L_0x0040:
            java.lang.String r3 = "StateSet"
            boolean r3 = r2.equals(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = 4
            goto L_0x0084
        L_0x004a:
            boolean r3 = r2.equals(r7)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = 0
            goto L_0x0084
        L_0x0052:
            java.lang.String r3 = "OnSwipe"
            boolean r3 = r2.equals(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = r4
            goto L_0x0084
        L_0x005c:
            java.lang.String r3 = "OnClick"
            boolean r3 = r2.equals(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = 3
            goto L_0x0084
        L_0x0066:
            java.lang.String r4 = "Transition"
            boolean r4 = r2.equals(r4)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r4 == 0) goto L_0x0083
            goto L_0x0084
        L_0x006f:
            java.lang.String r3 = "KeyFrameSet"
            boolean r3 = r2.equals(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = 6
            goto L_0x0084
        L_0x0079:
            java.lang.String r3 = "ConstraintSet"
            boolean r3 = r2.equals(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r3 == 0) goto L_0x0083
            r3 = 5
            goto L_0x0084
        L_0x0083:
            r3 = r6
        L_0x0084:
            switch(r3) {
                case 0: goto L_0x0126;
                case 1: goto L_0x00e4;
                case 2: goto L_0x00aa;
                case 3: goto L_0x00a5;
                case 4: goto L_0x009c;
                case 5: goto L_0x0097;
                case 6: goto L_0x0089;
                default: goto L_0x0087;
            }     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x0087:
            goto L_0x012a
        L_0x0089:
            androidx.constraintlayout.motion.widget.KeyFrames r2 = new androidx.constraintlayout.motion.widget.KeyFrames     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r2.<init>(r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.util.ArrayList r3 = r1.mKeyFramesList     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r3.add(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x0097:
            r8.parseConstraintSet(r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x009c:
            androidx.constraintlayout.widget.StateSet r2 = new androidx.constraintlayout.widget.StateSet     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r2.<init>(r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r8.mStateSet = r2     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x00a5:
            r1.addOnClick(r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x00aa:
            if (r1 != 0) goto L_0x00d9
            android.content.res.Resources r2 = r9.getResources()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r2 = r2.getResourceEntryName(r10)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            int r3 = r0.getLineNumber()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r4.<init>()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r5 = " OnSwipe ("
            r4.append(r5)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r4.append(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r2 = ".xml:"
            r4.append(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r4.append(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r2 = ")"
            r4.append(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r2 = r4.toString()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            android.util.Log.v(r7, r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x00d9:
            androidx.constraintlayout.motion.widget.TouchResponse r2 = new androidx.constraintlayout.motion.widget.TouchResponse     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.MotionLayout r3 = r8.mMotionLayout     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r2.<init>(r9, r3, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.TouchResponse unused = r1.mTouchResponse = r2     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x00e4:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r1 = r8.mTransitionList     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r2 = new androidx.constraintlayout.motion.widget.MotionScene$Transition     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r2.<init>(r8, r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r1.add(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.MotionScene$Transition r1 = r8.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r1 != 0) goto L_0x010b
            boolean r1 = r2.mIsAbstract     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r1 != 0) goto L_0x010b
            r8.mCurrentTransition = r2     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.TouchResponse r1 = r2.mTouchResponse     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r1 == 0) goto L_0x010b
            androidx.constraintlayout.motion.widget.MotionScene$Transition r1 = r8.mCurrentTransition     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            androidx.constraintlayout.motion.widget.TouchResponse r1 = r1.mTouchResponse     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            boolean r3 = r8.mRtl     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r1.setRTL(r3)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x010b:
            boolean r1 = r2.mIsAbstract     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r1 == 0) goto L_0x0124
            int r1 = r2.mConstraintSetEnd     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            if (r1 != r6) goto L_0x011a
            r8.mDefaultTransition = r2     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x011f
        L_0x011a:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r1 = r8.mAbstractTransitionList     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r1.add(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x011f:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionScene$Transition> r1 = r8.mTransitionList     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r1.remove(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x0124:
            r1 = r2
            goto L_0x0142
        L_0x0126:
            r8.parseMotionSceneTags(r9, r0)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x012a:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r3.<init>()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r4 = "WARNING UNKNOWN ATTRIBUTE "
            r3.append(r4)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            r3.append(r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            java.lang.String r2 = r3.toString()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            android.util.Log.v(r7, r2)     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x0142
        L_0x013f:
            r0.getName()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
        L_0x0142:
            int r2 = r0.next()     // Catch:{ XmlPullParserException -> 0x014d, IOException -> 0x0148 }
            goto L_0x000d
        L_0x0148:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0151
        L_0x014d:
            r8 = move-exception
            r8.printStackTrace()
        L_0x0151:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionScene.load(android.content.Context, int):void");
    }

    public final void parseMotionSceneTags(Context context, XmlPullParser xmlPullParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), R$styleable.MotionScene);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R$styleable.MotionScene_defaultDuration) {
                this.mDefaultDuration = obtainStyledAttributes.getInt(index, this.mDefaultDuration);
            } else if (index == R$styleable.MotionScene_layoutDuringTransition) {
                this.mLayoutDuringTransition = obtainStyledAttributes.getInteger(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }

    public final int getId(Context context, String str) {
        int i;
        if (str.contains("/")) {
            i = context.getResources().getIdentifier(str.substring(str.indexOf(47) + 1), "id", context.getPackageName());
            if (this.DEBUG_DESKTOP) {
                System.out.println("id getMap res = " + i);
            }
        } else {
            i = -1;
        }
        if (i != -1) {
            return i;
        }
        if (str.length() > 1) {
            return Integer.parseInt(str.substring(1));
        }
        Log.e("MotionScene", "error in parsing id");
        return i;
    }

    public final void parseConstraintSet(Context context, XmlPullParser xmlPullParser) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.setForceId(false);
        int attributeCount = xmlPullParser.getAttributeCount();
        int i = -1;
        int i2 = -1;
        for (int i3 = 0; i3 < attributeCount; i3++) {
            String attributeName = xmlPullParser.getAttributeName(i3);
            String attributeValue = xmlPullParser.getAttributeValue(i3);
            if (this.DEBUG_DESKTOP) {
                System.out.println("id string = " + attributeValue);
            }
            attributeName.hashCode();
            if (attributeName.equals("deriveConstraintsFrom")) {
                i2 = getId(context, attributeValue);
            } else if (attributeName.equals("id")) {
                i = getId(context, attributeValue);
                this.mConstraintSetIdMap.put(stripID(attributeValue), Integer.valueOf(i));
            }
        }
        if (i != -1) {
            if (this.mMotionLayout.mDebugPath != 0) {
                constraintSet.setValidateOnParse(true);
            }
            constraintSet.load(context, xmlPullParser);
            if (i2 != -1) {
                this.mDeriveMap.put(i, i2);
            }
            this.mConstraintSetMap.put(i, constraintSet);
        }
    }

    public ConstraintSet getConstraintSet(int i) {
        return getConstraintSet(i, -1, -1);
    }

    public ConstraintSet getConstraintSet(int i, int i2, int i3) {
        int stateGetConstraintID;
        if (this.DEBUG_DESKTOP) {
            PrintStream printStream = System.out;
            printStream.println("id " + i);
            PrintStream printStream2 = System.out;
            printStream2.println("size " + this.mConstraintSetMap.size());
        }
        StateSet stateSet = this.mStateSet;
        if (!(stateSet == null || (stateGetConstraintID = stateSet.stateGetConstraintID(i, i2, i3)) == -1)) {
            i = stateGetConstraintID;
        }
        if (this.mConstraintSetMap.get(i) != null) {
            return this.mConstraintSetMap.get(i);
        }
        Log.e("MotionScene", "Warning could not find ConstraintSet id/" + Debug.getName(this.mMotionLayout.getContext(), i) + " In MotionScene");
        SparseArray<ConstraintSet> sparseArray = this.mConstraintSetMap;
        return sparseArray.get(sparseArray.keyAt(0));
    }

    public void setConstraintSet(int i, ConstraintSet constraintSet) {
        this.mConstraintSetMap.put(i, constraintSet);
    }

    public void getKeyFrames(MotionController motionController) {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            Transition transition2 = this.mDefaultTransition;
            if (transition2 != null) {
                Iterator it = transition2.mKeyFramesList.iterator();
                while (it.hasNext()) {
                    ((KeyFrames) it.next()).addFrames(motionController);
                }
                return;
            }
            return;
        }
        Iterator it2 = transition.mKeyFramesList.iterator();
        while (it2.hasNext()) {
            ((KeyFrames) it2.next()).addFrames(motionController);
        }
    }

    public boolean supportTouch() {
        Iterator<Transition> it = this.mTransitionList.iterator();
        while (it.hasNext()) {
            if (it.next().mTouchResponse != null) {
                return true;
            }
        }
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return false;
        }
        return true;
    }

    public void processTouchEvent(MotionEvent motionEvent, int i, MotionLayout motionLayout) {
        MotionLayout.MotionTracker motionTracker;
        MotionEvent motionEvent2;
        RectF rectF = new RectF();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (i != -1) {
            int action = motionEvent.getAction();
            boolean z = false;
            if (action == 0) {
                this.mLastTouchX = motionEvent.getRawX();
                this.mLastTouchY = motionEvent.getRawY();
                this.mLastTouchDown = motionEvent;
                if (this.mCurrentTransition.mTouchResponse != null) {
                    RectF limitBoundsTo = this.mCurrentTransition.mTouchResponse.getLimitBoundsTo(this.mMotionLayout, rectF);
                    if (limitBoundsTo == null || limitBoundsTo.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                        RectF touchRegion = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF);
                        if (touchRegion == null || touchRegion.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            this.mMotionOutsideRegion = false;
                        } else {
                            this.mMotionOutsideRegion = true;
                        }
                        this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
                        return;
                    }
                    this.mLastTouchDown = null;
                    return;
                }
                return;
            } else if (action == 2) {
                float rawY = motionEvent.getRawY() - this.mLastTouchY;
                float rawX = motionEvent.getRawX() - this.mLastTouchX;
                if ((((double) rawX) != 0.0d || ((double) rawY) != 0.0d) && (motionEvent2 = this.mLastTouchDown) != null) {
                    Transition bestTransitionFor = bestTransitionFor(i, rawX, rawY, motionEvent2);
                    if (bestTransitionFor != null) {
                        motionLayout.setTransition(bestTransitionFor);
                        RectF touchRegion2 = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF);
                        if (touchRegion2 != null && !touchRegion2.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            z = true;
                        }
                        this.mMotionOutsideRegion = z;
                        this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
                    }
                } else {
                    return;
                }
            }
        }
        Transition transition = this.mCurrentTransition;
        if (!(transition == null || transition.mTouchResponse == null || this.mMotionOutsideRegion)) {
            this.mCurrentTransition.mTouchResponse.processTouchEvent(motionEvent, this.mVelocityTracker, i, this);
        }
        this.mLastTouchX = motionEvent.getRawX();
        this.mLastTouchY = motionEvent.getRawY();
        if (motionEvent.getAction() == 1 && (motionTracker = this.mVelocityTracker) != null) {
            motionTracker.recycle();
            this.mVelocityTracker = null;
            int i2 = motionLayout.mCurrentState;
            if (i2 != -1) {
                autoTransition(motionLayout, i2);
            }
        }
    }

    public void processScrollMove(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollMove(f, f2);
        }
    }

    public void processScrollUp(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollUp(f, f2);
        }
    }

    public float getProgressDirection(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getProgressDirection(f, f2);
    }

    public int getStartId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetStart;
    }

    public int getEndId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetEnd;
    }

    public Interpolator getInterpolator() {
        int access$1400 = this.mCurrentTransition.mDefaultInterpolator;
        if (access$1400 == -2) {
            return AnimationUtils.loadInterpolator(this.mMotionLayout.getContext(), this.mCurrentTransition.mDefaultInterpolatorID);
        }
        if (access$1400 == -1) {
            final Easing interpolator = Easing.getInterpolator(this.mCurrentTransition.mDefaultInterpolatorString);
            return new Interpolator() {
                public float getInterpolation(float f) {
                    return (float) interpolator.get((double) f);
                }
            };
        } else if (access$1400 == 0) {
            return new AccelerateDecelerateInterpolator();
        } else {
            if (access$1400 == 1) {
                return new AccelerateInterpolator();
            }
            if (access$1400 == 2) {
                return new DecelerateInterpolator();
            }
            if (access$1400 == 4) {
                return new AnticipateInterpolator();
            }
            if (access$1400 != 5) {
                return null;
            }
            return new BounceInterpolator();
        }
    }

    public int getDuration() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mDuration;
        }
        return this.mDefaultDuration;
    }

    public int gatPathMotionArc() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mPathMotionArc;
        }
        return -1;
    }

    public float getStaggered() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mStagger;
        }
        return 0.0f;
    }

    public float getMaxAcceleration() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxAcceleration();
    }

    public float getMaxVelocity() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return 0.0f;
        }
        return this.mCurrentTransition.mTouchResponse.getMaxVelocity();
    }

    public void setupTouch() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setupTouch();
        }
    }

    public boolean getMoveWhenScrollAtTop() {
        Transition transition = this.mCurrentTransition;
        if (transition == null || transition.mTouchResponse == null) {
            return false;
        }
        return this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop();
    }

    public void readFallback(MotionLayout motionLayout) {
        int i = 0;
        while (i < this.mConstraintSetMap.size()) {
            int keyAt = this.mConstraintSetMap.keyAt(i);
            if (hasCycleDependency(keyAt)) {
                Log.e("MotionScene", "Cannot be derived from yourself");
                return;
            } else {
                readConstraintChain(keyAt);
                i++;
            }
        }
        for (int i2 = 0; i2 < this.mConstraintSetMap.size(); i2++) {
            this.mConstraintSetMap.valueAt(i2).readFallback((ConstraintLayout) motionLayout);
        }
    }

    public final boolean hasCycleDependency(int i) {
        int i2 = this.mDeriveMap.get(i);
        int size = this.mDeriveMap.size();
        while (i2 > 0) {
            if (i2 == i) {
                return true;
            }
            int i3 = size - 1;
            if (size < 0) {
                return true;
            }
            i2 = this.mDeriveMap.get(i2);
            size = i3;
        }
        return false;
    }

    public final void readConstraintChain(int i) {
        int i2 = this.mDeriveMap.get(i);
        if (i2 > 0) {
            readConstraintChain(this.mDeriveMap.get(i));
            ConstraintSet constraintSet = this.mConstraintSetMap.get(i);
            ConstraintSet constraintSet2 = this.mConstraintSetMap.get(i2);
            if (constraintSet2 == null) {
                Log.e("MotionScene", "invalid deriveConstraintsFrom id");
            }
            constraintSet.readFallback(constraintSet2);
            this.mDeriveMap.put(i, -1);
        }
    }

    public static String stripID(String str) {
        if (str == null) {
            return "";
        }
        int indexOf = str.indexOf(47);
        if (indexOf < 0) {
            return str;
        }
        return str.substring(indexOf + 1);
    }
}
