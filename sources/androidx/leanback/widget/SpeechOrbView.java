package androidx.leanback.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import androidx.leanback.R$color;
import androidx.leanback.R$drawable;
import androidx.leanback.R$fraction;
import androidx.leanback.R$layout;
import androidx.leanback.widget.SearchOrbView;

public class SpeechOrbView extends SearchOrbView {
    public int mCurrentLevel;
    public boolean mListening;
    public SearchOrbView.Colors mListeningOrbColors;
    public SearchOrbView.Colors mNotListeningOrbColors;
    public final float mSoundLevelMaxZoom;

    public SpeechOrbView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SpeechOrbView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SpeechOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCurrentLevel = 0;
        this.mListening = false;
        Resources resources = context.getResources();
        this.mSoundLevelMaxZoom = resources.getFraction(R$fraction.lb_search_bar_speech_orb_max_level_zoom, 1, 1);
        this.mNotListeningOrbColors = new SearchOrbView.Colors(resources.getColor(R$color.lb_speech_orb_not_recording), resources.getColor(R$color.lb_speech_orb_not_recording_pulsed), resources.getColor(R$color.lb_speech_orb_not_recording_icon));
        int i2 = R$color.lb_speech_orb_recording;
        this.mListeningOrbColors = new SearchOrbView.Colors(resources.getColor(i2), resources.getColor(i2), 0);
        showNotListening();
    }

    public int getLayoutResourceId() {
        return R$layout.lb_speech_orb;
    }

    public void showListening() {
        setOrbColors(this.mListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R$drawable.lb_ic_search_mic));
        animateOnFocus(true);
        enableOrbColorAnimation(false);
        scaleOrbViewOnly(1.0f);
        this.mCurrentLevel = 0;
        this.mListening = true;
    }

    public void showNotListening() {
        setOrbColors(this.mNotListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R$drawable.lb_ic_search_mic_out));
        animateOnFocus(hasFocus());
        scaleOrbViewOnly(1.0f);
        this.mListening = false;
    }

    public void setSoundLevel(int i) {
        if (this.mListening) {
            int i2 = this.mCurrentLevel;
            if (i > i2) {
                this.mCurrentLevel = i2 + ((i - i2) / 2);
            } else {
                this.mCurrentLevel = (int) (((float) i2) * 0.7f);
            }
            scaleOrbViewOnly((((this.mSoundLevelMaxZoom - getFocusedZoom()) * ((float) this.mCurrentLevel)) / 100.0f) + 1.0f);
        }
    }
}
