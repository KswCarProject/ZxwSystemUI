package com.android.wm.shell.compatui.letterboxedu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.wm.shell.R;

class LetterboxEduDialogLayout extends ConstraintLayout {
    public Drawable mBackgroundDim;
    public View mDialogContainer;
    public TextView mDialogTitle;

    public static /* synthetic */ void lambda$setDismissOnClickListener$1(View view) {
    }

    public LetterboxEduDialogLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public View getDialogContainer() {
        return this.mDialogContainer;
    }

    public TextView getDialogTitle() {
        return this.mDialogTitle;
    }

    public Drawable getBackgroundDim() {
        return this.mBackgroundDim;
    }

    public void setDismissOnClickListener(Runnable runnable) {
        View.OnClickListener onClickListener = null;
        LetterboxEduDialogLayout$$ExternalSyntheticLambda0 letterboxEduDialogLayout$$ExternalSyntheticLambda0 = runnable == null ? null : new LetterboxEduDialogLayout$$ExternalSyntheticLambda0(runnable);
        findViewById(R.id.letterbox_education_dialog_dismiss_button).setOnClickListener(letterboxEduDialogLayout$$ExternalSyntheticLambda0);
        setOnClickListener(letterboxEduDialogLayout$$ExternalSyntheticLambda0);
        View view = this.mDialogContainer;
        if (runnable != null) {
            new LetterboxEduDialogLayout$$ExternalSyntheticLambda1
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001d: CONSTRUCTOR  (r0v2 ? I:com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda1) =  call: com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda1.<init>():void type: CONSTRUCTOR in method: com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout.setDismissOnClickListener(java.lang.Runnable):void, dex: classes2.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.util.ArrayList.forEach(ArrayList.java:1259)
                	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:483)
                	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
                	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r0v2 ?
                	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:189)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:620)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	... 34 more
                */
            /*
                this = this;
                r0 = 0
                if (r4 != 0) goto L_0x0005
                r1 = r0
                goto L_0x000a
            L_0x0005:
                com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda0 r1 = new com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda0
                r1.<init>(r4)
            L_0x000a:
                int r2 = com.android.wm.shell.R.id.letterbox_education_dialog_dismiss_button
                android.view.View r2 = r3.findViewById(r2)
                r2.setOnClickListener(r1)
                r3.setOnClickListener(r1)
                android.view.View r3 = r3.mDialogContainer
                if (r4 != 0) goto L_0x001b
                goto L_0x0020
            L_0x001b:
                com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda1 r0 = new com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda1
                r0.<init>()
            L_0x0020:
                r3.setOnClickListener(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout.setDismissOnClickListener(java.lang.Runnable):void");
        }

        public void onFinishInflate() {
            super.onFinishInflate();
            this.mDialogContainer = findViewById(R.id.letterbox_education_dialog_container);
            this.mDialogTitle = (TextView) findViewById(R.id.letterbox_education_dialog_title);
            Drawable mutate = getBackground().mutate();
            this.mBackgroundDim = mutate;
            mutate.setAlpha(0);
        }
    }
