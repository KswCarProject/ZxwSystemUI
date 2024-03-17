package com.android.wm.shell.bubbles.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.FloatProperty;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PhysicsAnimationLayout extends FrameLayout {
    public PhysicsAnimationController mController;
    public final HashMap<DynamicAnimation.ViewProperty, Runnable> mEndActionForProperty = new HashMap<>();

    public static abstract class PhysicsAnimationController {
        public PhysicsAnimationLayout mLayout;

        public interface ChildAnimationConfigurator {
            void configureAnimationForChildAtIndex(int i, PhysicsPropertyAnimator physicsPropertyAnimator);
        }

        public interface MultiAnimationStarter {
            void startAll(Runnable... runnableArr);
        }

        public abstract Set<DynamicAnimation.ViewProperty> getAnimatedProperties();

        public abstract int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i);

        public abstract float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i);

        public abstract SpringForce getSpringForce(DynamicAnimation.ViewProperty viewProperty, View view);

        public abstract void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout);

        public abstract void onChildAdded(View view, int i);

        public abstract void onChildRemoved(View view, int i, Runnable runnable);

        public abstract void onChildReordered(View view, int i, int i2);

        public boolean isActiveController() {
            PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
            return physicsAnimationLayout != null && this == physicsAnimationLayout.mController;
        }

        public void setLayout(PhysicsAnimationLayout physicsAnimationLayout) {
            this.mLayout = physicsAnimationLayout;
            onActiveControllerForLayout(physicsAnimationLayout);
        }

        public PhysicsPropertyAnimator animationForChild(View view) {
            int i = R.id.physics_animator_tag;
            PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsPropertyAnimator) view.getTag(i);
            if (physicsPropertyAnimator == null) {
                PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
                Objects.requireNonNull(physicsAnimationLayout);
                physicsPropertyAnimator = new PhysicsPropertyAnimator(view);
                view.setTag(i, physicsPropertyAnimator);
            }
            physicsPropertyAnimator.clearAnimator();
            physicsPropertyAnimator.setAssociatedController(this);
            return physicsPropertyAnimator;
        }

        public PhysicsPropertyAnimator animationForChildAtIndex(int i) {
            return animationForChild(this.mLayout.getChildAt(i));
        }

        public MultiAnimationStarter animationsForChildrenFromIndex(int i, ChildAnimationConfigurator childAnimationConfigurator) {
            HashSet hashSet = new HashSet();
            ArrayList arrayList = new ArrayList();
            while (i < this.mLayout.getChildCount()) {
                PhysicsPropertyAnimator animationForChildAtIndex = animationForChildAtIndex(i);
                childAnimationConfigurator.configureAnimationForChildAtIndex(i, animationForChildAtIndex);
                hashSet.addAll(animationForChildAtIndex.getAnimatedProperties());
                arrayList.add(animationForChildAtIndex);
                i++;
            }
            return new PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0(this, hashSet, arrayList);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$animationsForChildrenFromIndex$1(Set set, List list, Runnable[] runnableArr) {
            PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1 physicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1 = new PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1(runnableArr);
            if (this.mLayout.getChildCount() == 0) {
                physicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1.run();
                return;
            }
            if (runnableArr != null) {
                setEndActionForMultipleProperties(physicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1, (DynamicAnimation.ViewProperty[]) set.toArray(new DynamicAnimation.ViewProperty[0]));
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ((PhysicsPropertyAnimator) it.next()).start(new Runnable[0]);
            }
        }

        public static /* synthetic */ void lambda$animationsForChildrenFromIndex$0(Runnable[] runnableArr) {
            for (Runnable run : runnableArr) {
                run.run();
            }
        }

        public void setEndActionForProperty(Runnable runnable, DynamicAnimation.ViewProperty viewProperty) {
            this.mLayout.mEndActionForProperty.put(viewProperty, runnable);
        }

        public void setEndActionForMultipleProperties(Runnable runnable, DynamicAnimation.ViewProperty... viewPropertyArr) {
            PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda2 physicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda2 = new PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda2(this, viewPropertyArr, runnable);
            for (DynamicAnimation.ViewProperty endActionForProperty : viewPropertyArr) {
                setEndActionForProperty(physicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda2, endActionForProperty);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setEndActionForMultipleProperties$2(DynamicAnimation.ViewProperty[] viewPropertyArr, Runnable runnable) {
            if (!this.mLayout.arePropertiesAnimating(viewPropertyArr)) {
                runnable.run();
                for (DynamicAnimation.ViewProperty removeEndActionForProperty : viewPropertyArr) {
                    removeEndActionForProperty(removeEndActionForProperty);
                }
            }
        }

        public void removeEndActionForProperty(DynamicAnimation.ViewProperty viewProperty) {
            this.mLayout.mEndActionForProperty.remove(viewProperty);
        }
    }

    public PhysicsAnimationLayout(Context context) {
        super(context);
    }

    public void setActiveController(PhysicsAnimationController physicsAnimationController) {
        cancelAllAnimations();
        this.mEndActionForProperty.clear();
        this.mController = physicsAnimationController;
        physicsAnimationController.setLayout(this);
        for (DynamicAnimation.ViewProperty upAnimationsForProperty : this.mController.getAnimatedProperties()) {
            setUpAnimationsForProperty(upAnimationsForProperty);
        }
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view, i, layoutParams, false);
    }

    public void removeView(View view) {
        if (this.mController != null) {
            int indexOfChild = indexOfChild(view);
            super.removeView(view);
            addTransientView(view, indexOfChild);
            this.mController.onChildRemoved(view, indexOfChild, new PhysicsAnimationLayout$$ExternalSyntheticLambda0(this, view));
            return;
        }
        super.removeView(view);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeView$0(View view) {
        cancelAnimationsOnView(view);
        removeTransientView(view);
    }

    public void removeViewAt(int i) {
        removeView(getChildAt(i));
    }

    public void reorderView(View view, int i) {
        if (view != null) {
            int indexOfChild = indexOfChild(view);
            super.removeView(view);
            if (view.getParent() != null) {
                super.removeTransientView(view);
            }
            addViewInternal(view, i, view.getLayoutParams(), true);
            PhysicsAnimationController physicsAnimationController = this.mController;
            if (physicsAnimationController != null) {
                physicsAnimationController.onChildReordered(view, indexOfChild, i);
            }
        }
    }

    public boolean arePropertiesAnimating(DynamicAnimation.ViewProperty... viewPropertyArr) {
        for (int i = 0; i < getChildCount(); i++) {
            if (arePropertiesAnimatingOnView(getChildAt(i), viewPropertyArr)) {
                return true;
            }
        }
        return false;
    }

    public boolean arePropertiesAnimatingOnView(View view, DynamicAnimation.ViewProperty... viewPropertyArr) {
        ObjectAnimator targetAnimatorFromView = getTargetAnimatorFromView(view);
        for (DynamicAnimation.ViewProperty viewProperty : viewPropertyArr) {
            SpringAnimation springAnimationFromView = getSpringAnimationFromView(viewProperty, view);
            if (springAnimationFromView != null && springAnimationFromView.isRunning()) {
                return true;
            }
            if ((viewProperty.equals(DynamicAnimation.TRANSLATION_X) || viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) && targetAnimatorFromView != null && targetAnimatorFromView.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public void cancelAllAnimations() {
        PhysicsAnimationController physicsAnimationController = this.mController;
        if (physicsAnimationController != null) {
            cancelAllAnimationsOfProperties((DynamicAnimation.ViewProperty[]) physicsAnimationController.getAnimatedProperties().toArray(new DynamicAnimation.ViewProperty[0]));
        }
    }

    public void cancelAllAnimationsOfProperties(DynamicAnimation.ViewProperty... viewPropertyArr) {
        if (this.mController != null) {
            for (int i = 0; i < getChildCount(); i++) {
                for (DynamicAnimation.ViewProperty springAnimationAtIndex : viewPropertyArr) {
                    SpringAnimation springAnimationAtIndex2 = getSpringAnimationAtIndex(springAnimationAtIndex, i);
                    if (springAnimationAtIndex2 != null) {
                        springAnimationAtIndex2.cancel();
                    }
                }
                ViewPropertyAnimator viewPropertyAnimatorFromView = getViewPropertyAnimatorFromView(getChildAt(i));
                if (viewPropertyAnimatorFromView != null) {
                    viewPropertyAnimatorFromView.cancel();
                }
            }
        }
    }

    public void cancelAnimationsOnView(View view) {
        ObjectAnimator targetAnimatorFromView = getTargetAnimatorFromView(view);
        if (targetAnimatorFromView != null) {
            targetAnimatorFromView.cancel();
        }
        for (DynamicAnimation.ViewProperty springAnimationFromView : this.mController.getAnimatedProperties()) {
            SpringAnimation springAnimationFromView2 = getSpringAnimationFromView(springAnimationFromView, view);
            if (springAnimationFromView2 != null) {
                springAnimationFromView2.cancel();
            }
        }
    }

    public boolean isActiveController(PhysicsAnimationController physicsAnimationController) {
        return this.mController == physicsAnimationController;
    }

    public boolean isFirstChildXLeftOfCenter(float f) {
        if (getChildCount() <= 0 || f + ((float) (getChildAt(0).getWidth() / 2)) >= ((float) (getWidth() / 2))) {
            return false;
        }
        return true;
    }

    public static String getReadablePropertyName(DynamicAnimation.ViewProperty viewProperty) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            return "TRANSLATION_X";
        }
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return "TRANSLATION_Y";
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_X)) {
            return "SCALE_X";
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_Y)) {
            return "SCALE_Y";
        }
        return viewProperty.equals(DynamicAnimation.ALPHA) ? "ALPHA" : "Unknown animation property.";
    }

    public final void addViewInternal(View view, int i, ViewGroup.LayoutParams layoutParams, boolean z) {
        super.addView(view, i, layoutParams);
        PhysicsAnimationController physicsAnimationController = this.mController;
        if (physicsAnimationController != null && !z) {
            for (DynamicAnimation.ViewProperty upAnimationForChild : physicsAnimationController.getAnimatedProperties()) {
                setUpAnimationForChild(upAnimationForChild, view);
            }
            this.mController.onChildAdded(view, i);
        }
    }

    public final SpringAnimation getSpringAnimationAtIndex(DynamicAnimation.ViewProperty viewProperty, int i) {
        return getSpringAnimationFromView(viewProperty, getChildAt(i));
    }

    public final SpringAnimation getSpringAnimationFromView(DynamicAnimation.ViewProperty viewProperty, View view) {
        return (SpringAnimation) view.getTag(getTagIdForProperty(viewProperty));
    }

    public final ViewPropertyAnimator getViewPropertyAnimatorFromView(View view) {
        return (ViewPropertyAnimator) view.getTag(R.id.reorder_animator_tag);
    }

    public final ObjectAnimator getTargetAnimatorFromView(View view) {
        return (ObjectAnimator) view.getTag(R.id.target_animator_tag);
    }

    public final void setUpAnimationsForProperty(DynamicAnimation.ViewProperty viewProperty) {
        for (int i = 0; i < getChildCount(); i++) {
            setUpAnimationForChild(viewProperty, getChildAt(i));
        }
    }

    public final void setUpAnimationForChild(DynamicAnimation.ViewProperty viewProperty, View view) {
        SpringAnimation springAnimation = new SpringAnimation(view, viewProperty);
        springAnimation.addUpdateListener(new PhysicsAnimationLayout$$ExternalSyntheticLambda1(this, view, viewProperty));
        springAnimation.setSpring(this.mController.getSpringForce(viewProperty, view));
        springAnimation.addEndListener(new AllAnimationsForPropertyFinishedEndListener(viewProperty));
        view.setTag(getTagIdForProperty(viewProperty), springAnimation);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setUpAnimationForChild$1(View view, DynamicAnimation.ViewProperty viewProperty, DynamicAnimation dynamicAnimation, float f, float f2) {
        SpringAnimation springAnimationAtIndex;
        int indexOfChild = indexOfChild(view);
        int nextAnimationInChain = this.mController.getNextAnimationInChain(viewProperty, indexOfChild);
        if (nextAnimationInChain != -1 && indexOfChild >= 0) {
            float offsetForChainedPropertyAnimation = this.mController.getOffsetForChainedPropertyAnimation(viewProperty, nextAnimationInChain);
            if (nextAnimationInChain < getChildCount() && (springAnimationAtIndex = getSpringAnimationAtIndex(viewProperty, nextAnimationInChain)) != null) {
                springAnimationAtIndex.animateToFinalPosition(f + offsetForChainedPropertyAnimation);
            }
        }
    }

    public final int getTagIdForProperty(DynamicAnimation.ViewProperty viewProperty) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            return R.id.translation_x_dynamicanimation_tag;
        }
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return R.id.translation_y_dynamicanimation_tag;
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_X)) {
            return R.id.scale_x_dynamicanimation_tag;
        }
        if (viewProperty.equals(DynamicAnimation.SCALE_Y)) {
            return R.id.scale_y_dynamicanimation_tag;
        }
        if (viewProperty.equals(DynamicAnimation.ALPHA)) {
            return R.id.alpha_dynamicanimation_tag;
        }
        return -1;
    }

    public class AllAnimationsForPropertyFinishedEndListener implements DynamicAnimation.OnAnimationEndListener {
        public DynamicAnimation.ViewProperty mProperty;

        public AllAnimationsForPropertyFinishedEndListener(DynamicAnimation.ViewProperty viewProperty) {
            this.mProperty = viewProperty;
        }

        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            Runnable runnable;
            if (!PhysicsAnimationLayout.this.arePropertiesAnimating(this.mProperty) && PhysicsAnimationLayout.this.mEndActionForProperty.containsKey(this.mProperty) && (runnable = PhysicsAnimationLayout.this.mEndActionForProperty.get(this.mProperty)) != null) {
                runnable.run();
            }
        }
    }

    public class PhysicsPropertyAnimator {
        public Map<DynamicAnimation.ViewProperty, Float> mAnimatedProperties = new HashMap();
        public PhysicsAnimationController mAssociatedController;
        public PointF mCurrentPointOnPath = new PointF();
        public final FloatProperty<PhysicsPropertyAnimator> mCurrentPointOnPathXProperty = new FloatProperty<PhysicsPropertyAnimator>("PathX") {
            public void setValue(PhysicsPropertyAnimator physicsPropertyAnimator, float f) {
                PhysicsPropertyAnimator.this.mCurrentPointOnPath.x = f;
            }

            public Float get(PhysicsPropertyAnimator physicsPropertyAnimator) {
                return Float.valueOf(PhysicsPropertyAnimator.this.mCurrentPointOnPath.x);
            }
        };
        public final FloatProperty<PhysicsPropertyAnimator> mCurrentPointOnPathYProperty = new FloatProperty<PhysicsPropertyAnimator>("PathY") {
            public void setValue(PhysicsPropertyAnimator physicsPropertyAnimator, float f) {
                PhysicsPropertyAnimator.this.mCurrentPointOnPath.y = f;
            }

            public Float get(PhysicsPropertyAnimator physicsPropertyAnimator) {
                return Float.valueOf(PhysicsPropertyAnimator.this.mCurrentPointOnPath.y);
            }
        };
        public float mDampingRatio = -1.0f;
        public float mDefaultStartVelocity = -3.4028235E38f;
        public Map<DynamicAnimation.ViewProperty, Runnable[]> mEndActionsForProperty = new HashMap();
        public Map<DynamicAnimation.ViewProperty, Float> mInitialPropertyValues = new HashMap();
        public ObjectAnimator mPathAnimator;
        public Runnable[] mPositionEndActions;
        public Map<DynamicAnimation.ViewProperty, Float> mPositionStartVelocities = new HashMap();
        public long mStartDelay = 0;
        public float mStiffness = -1.0f;
        public View mView;

        public PhysicsPropertyAnimator(View view) {
            this.mView = view;
        }

        public PhysicsPropertyAnimator property(DynamicAnimation.ViewProperty viewProperty, float f, Runnable... runnableArr) {
            this.mAnimatedProperties.put(viewProperty, Float.valueOf(f));
            this.mEndActionsForProperty.put(viewProperty, runnableArr);
            return this;
        }

        public PhysicsPropertyAnimator alpha(float f, Runnable... runnableArr) {
            return property(DynamicAnimation.ALPHA, f, runnableArr);
        }

        public PhysicsPropertyAnimator translationX(float f, Runnable... runnableArr) {
            this.mPathAnimator = null;
            return property(DynamicAnimation.TRANSLATION_X, f, runnableArr);
        }

        public PhysicsPropertyAnimator translationX(float f, float f2, Runnable... runnableArr) {
            this.mInitialPropertyValues.put(DynamicAnimation.TRANSLATION_X, Float.valueOf(f));
            return translationX(f2, runnableArr);
        }

        public PhysicsPropertyAnimator translationY(float f, Runnable... runnableArr) {
            this.mPathAnimator = null;
            return property(DynamicAnimation.TRANSLATION_Y, f, runnableArr);
        }

        public PhysicsPropertyAnimator translationY(float f, float f2, Runnable... runnableArr) {
            this.mInitialPropertyValues.put(DynamicAnimation.TRANSLATION_Y, Float.valueOf(f));
            return translationY(f2, runnableArr);
        }

        public PhysicsPropertyAnimator position(float f, float f2, Runnable... runnableArr) {
            this.mPositionEndActions = runnableArr;
            translationX(f, new Runnable[0]);
            return translationY(f2, new Runnable[0]);
        }

        public PhysicsPropertyAnimator followAnimatedTargetAlongPath(Path path, int i, TimeInterpolator timeInterpolator, final Runnable... runnableArr) {
            ObjectAnimator objectAnimator = this.mPathAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, this.mCurrentPointOnPathXProperty, this.mCurrentPointOnPathYProperty, path);
            this.mPathAnimator = ofFloat;
            if (runnableArr != null) {
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        for (Runnable runnable : runnableArr) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }
                });
            }
            this.mPathAnimator.setDuration((long) i);
            this.mPathAnimator.setInterpolator(timeInterpolator);
            clearTranslationValues();
            return this;
        }

        public final void clearTranslationValues() {
            Map<DynamicAnimation.ViewProperty, Float> map = this.mAnimatedProperties;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_X;
            map.remove(viewProperty);
            Map<DynamicAnimation.ViewProperty, Float> map2 = this.mAnimatedProperties;
            DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_Y;
            map2.remove(viewProperty2);
            this.mInitialPropertyValues.remove(viewProperty);
            this.mInitialPropertyValues.remove(viewProperty2);
            PhysicsAnimationLayout.this.mEndActionForProperty.remove(viewProperty);
            PhysicsAnimationLayout.this.mEndActionForProperty.remove(viewProperty2);
        }

        public PhysicsPropertyAnimator scaleX(float f, Runnable... runnableArr) {
            return property(DynamicAnimation.SCALE_X, f, runnableArr);
        }

        public PhysicsPropertyAnimator scaleY(float f, Runnable... runnableArr) {
            return property(DynamicAnimation.SCALE_Y, f, runnableArr);
        }

        public PhysicsPropertyAnimator withStiffness(float f) {
            this.mStiffness = f;
            return this;
        }

        public PhysicsPropertyAnimator withPositionStartVelocities(float f, float f2) {
            this.mPositionStartVelocities.put(DynamicAnimation.TRANSLATION_X, Float.valueOf(f));
            this.mPositionStartVelocities.put(DynamicAnimation.TRANSLATION_Y, Float.valueOf(f2));
            return this;
        }

        public PhysicsPropertyAnimator withStartDelay(long j) {
            this.mStartDelay = j;
            return this;
        }

        public void start(Runnable... runnableArr) {
            if (!PhysicsAnimationLayout.this.isActiveController(this.mAssociatedController)) {
                Log.w("Bubbs.PAL", "Only the active animation controller is allowed to start animations. Use PhysicsAnimationLayout#setActiveController to set the active animation controller.");
                return;
            }
            Set<DynamicAnimation.ViewProperty> animatedProperties = getAnimatedProperties();
            if (runnableArr != null && runnableArr.length > 0) {
                this.mAssociatedController.setEndActionForMultipleProperties(new PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0(runnableArr), (DynamicAnimation.ViewProperty[]) animatedProperties.toArray(new DynamicAnimation.ViewProperty[0]));
            }
            if (this.mPositionEndActions != null) {
                PhysicsAnimationLayout physicsAnimationLayout = PhysicsAnimationLayout.this;
                DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_X;
                SpringAnimation r12 = physicsAnimationLayout.getSpringAnimationFromView(viewProperty, this.mView);
                PhysicsAnimationLayout physicsAnimationLayout2 = PhysicsAnimationLayout.this;
                DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_Y;
                PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1 physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1 = new PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1(this, r12, physicsAnimationLayout2.getSpringAnimationFromView(viewProperty2, this.mView));
                this.mEndActionsForProperty.put(viewProperty, new Runnable[]{physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1});
                this.mEndActionsForProperty.put(viewProperty2, new Runnable[]{physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1});
            }
            if (this.mPathAnimator != null) {
                startPathAnimation();
            }
            for (DynamicAnimation.ViewProperty next : animatedProperties) {
                if (this.mPathAnimator == null || (!next.equals(DynamicAnimation.TRANSLATION_X) && !next.equals(DynamicAnimation.TRANSLATION_Y))) {
                    if (this.mInitialPropertyValues.containsKey(next)) {
                        next.setValue(this.mView, this.mInitialPropertyValues.get(next).floatValue());
                    }
                    SpringForce springForce = PhysicsAnimationLayout.this.mController.getSpringForce(next, this.mView);
                    View view = this.mView;
                    float floatValue = this.mAnimatedProperties.get(next).floatValue();
                    float floatValue2 = this.mPositionStartVelocities.getOrDefault(next, Float.valueOf(this.mDefaultStartVelocity)).floatValue();
                    long j = this.mStartDelay;
                    float f = this.mStiffness;
                    if (f < 0.0f) {
                        f = springForce.getStiffness();
                    }
                    float f2 = f;
                    float f3 = this.mDampingRatio;
                    animateValueForChild(next, view, floatValue, floatValue2, j, f2, f3 >= 0.0f ? f3 : springForce.getDampingRatio(), this.mEndActionsForProperty.get(next));
                } else {
                    return;
                }
            }
            clearAnimator();
        }

        public static /* synthetic */ void lambda$start$0(Runnable[] runnableArr) {
            for (Runnable run : runnableArr) {
                run.run();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$start$1(SpringAnimation springAnimation, SpringAnimation springAnimation2) {
            if (!springAnimation.isRunning() && !springAnimation2.isRunning()) {
                Runnable[] runnableArr = this.mPositionEndActions;
                if (runnableArr != null) {
                    for (Runnable run : runnableArr) {
                        run.run();
                    }
                }
                this.mPositionEndActions = null;
            }
        }

        public Set<DynamicAnimation.ViewProperty> getAnimatedProperties() {
            HashSet hashSet = new HashSet(this.mAnimatedProperties.keySet());
            if (this.mPathAnimator != null) {
                hashSet.add(DynamicAnimation.TRANSLATION_X);
                hashSet.add(DynamicAnimation.TRANSLATION_Y);
            }
            return hashSet;
        }

        public void animateValueForChild(DynamicAnimation.ViewProperty viewProperty, View view, float f, float f2, long j, float f3, float f4, Runnable... runnableArr) {
            long j2 = j;
            final Runnable[] runnableArr2 = runnableArr;
            if (view != null) {
                DynamicAnimation.ViewProperty viewProperty2 = viewProperty;
                SpringAnimation springAnimation = (SpringAnimation) view.getTag(PhysicsAnimationLayout.this.getTagIdForProperty(viewProperty));
                if (springAnimation != null) {
                    if (runnableArr2 != null) {
                        springAnimation.addEndListener(new OneTimeEndListener() {
                            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                                super.onAnimationEnd(dynamicAnimation, z, f, f2);
                                for (Runnable run : runnableArr2) {
                                    run.run();
                                }
                            }
                        });
                    }
                    SpringForce spring = springAnimation.getSpring();
                    if (spring != null) {
                        PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4 physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4 = new PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4(spring, f3, f4, f2, springAnimation, f);
                        if (j2 > 0) {
                            PhysicsAnimationLayout.this.postDelayed(physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4, j2);
                        } else {
                            physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda4.run();
                        }
                    }
                }
            }
        }

        public static /* synthetic */ void lambda$animateValueForChild$2(SpringForce springForce, float f, float f2, float f3, SpringAnimation springAnimation, float f4) {
            springForce.setStiffness(f);
            springForce.setDampingRatio(f2);
            if (f3 > -3.4028235E38f) {
                springAnimation.setStartVelocity(f3);
            }
            springForce.setFinalPosition(f4);
            springAnimation.start();
        }

        public final void updateValueForChild(DynamicAnimation.ViewProperty viewProperty, View view, float f) {
            SpringAnimation springAnimation;
            SpringForce spring;
            if (view != null && (springAnimation = (SpringAnimation) view.getTag(PhysicsAnimationLayout.this.getTagIdForProperty(viewProperty))) != null && (spring = springAnimation.getSpring()) != null) {
                spring.setFinalPosition(f);
                springAnimation.start();
            }
        }

        public void startPathAnimation() {
            final SpringForce springForce = PhysicsAnimationLayout.this.mController.getSpringForce(DynamicAnimation.TRANSLATION_X, this.mView);
            final SpringForce springForce2 = PhysicsAnimationLayout.this.mController.getSpringForce(DynamicAnimation.TRANSLATION_Y, this.mView);
            long j = this.mStartDelay;
            if (j > 0) {
                this.mPathAnimator.setStartDelay(j);
            }
            final PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda2 physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda2 = new PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda2(this);
            this.mPathAnimator.addUpdateListener(new PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda3(physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda2));
            this.mPathAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animator) {
                    float f;
                    float f2;
                    PhysicsPropertyAnimator physicsPropertyAnimator = PhysicsPropertyAnimator.this;
                    DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_X;
                    View r3 = physicsPropertyAnimator.mView;
                    float f3 = PhysicsPropertyAnimator.this.mCurrentPointOnPath.x;
                    float r5 = PhysicsPropertyAnimator.this.mDefaultStartVelocity;
                    float r8 = PhysicsPropertyAnimator.this.mStiffness >= 0.0f ? PhysicsPropertyAnimator.this.mStiffness : springForce.getStiffness();
                    if (PhysicsPropertyAnimator.this.mDampingRatio >= 0.0f) {
                        f = PhysicsPropertyAnimator.this.mDampingRatio;
                    } else {
                        f = springForce.getDampingRatio();
                    }
                    physicsPropertyAnimator.animateValueForChild(viewProperty, r3, f3, r5, 0, r8, f, new Runnable[0]);
                    PhysicsPropertyAnimator physicsPropertyAnimator2 = PhysicsPropertyAnimator.this;
                    DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_Y;
                    View r15 = physicsPropertyAnimator2.mView;
                    float f4 = PhysicsPropertyAnimator.this.mCurrentPointOnPath.y;
                    float r17 = PhysicsPropertyAnimator.this.mDefaultStartVelocity;
                    float r20 = PhysicsPropertyAnimator.this.mStiffness >= 0.0f ? PhysicsPropertyAnimator.this.mStiffness : springForce2.getStiffness();
                    if (PhysicsPropertyAnimator.this.mDampingRatio >= 0.0f) {
                        f2 = PhysicsPropertyAnimator.this.mDampingRatio;
                    } else {
                        f2 = springForce2.getDampingRatio();
                    }
                    physicsPropertyAnimator2.animateValueForChild(viewProperty2, r15, f4, r17, 0, r20, f2, new Runnable[0]);
                }

                public void onAnimationEnd(Animator animator) {
                    physicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda2.run();
                }
            });
            ObjectAnimator r0 = PhysicsAnimationLayout.this.getTargetAnimatorFromView(this.mView);
            if (r0 != null) {
                r0.cancel();
            }
            this.mView.setTag(R.id.target_animator_tag, this.mPathAnimator);
            this.mPathAnimator.start();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startPathAnimation$3() {
            updateValueForChild(DynamicAnimation.TRANSLATION_X, this.mView, this.mCurrentPointOnPath.x);
            updateValueForChild(DynamicAnimation.TRANSLATION_Y, this.mView, this.mCurrentPointOnPath.y);
        }

        public final void clearAnimator() {
            this.mInitialPropertyValues.clear();
            this.mAnimatedProperties.clear();
            this.mPositionStartVelocities.clear();
            this.mDefaultStartVelocity = -3.4028235E38f;
            this.mStartDelay = 0;
            this.mStiffness = -1.0f;
            this.mDampingRatio = -1.0f;
            this.mEndActionsForProperty.clear();
            this.mPathAnimator = null;
            this.mPositionEndActions = null;
        }

        public final void setAssociatedController(PhysicsAnimationController physicsAnimationController) {
            this.mAssociatedController = physicsAnimationController;
        }
    }
}
