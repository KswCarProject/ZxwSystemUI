package androidx.leanback.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.leanback.R$color;
import androidx.leanback.R$dimen;
import androidx.leanback.R$id;
import androidx.leanback.R$integer;
import androidx.leanback.R$layout;
import androidx.leanback.R$raw;
import androidx.leanback.R$string;
import androidx.leanback.widget.SearchEditText;
import java.util.ArrayList;

public class SearchBar extends RelativeLayout {
    public static final String TAG = SearchBar.class.getSimpleName();
    public boolean mAutoStartRecognition;
    public int mBackgroundAlpha;
    public int mBackgroundSpeechAlpha;
    public Drawable mBadgeDrawable;
    public ImageView mBadgeView;
    public Drawable mBarBackground;
    public int mBarHeight;
    public final Context mContext;
    public final Handler mHandler;
    public String mHint;
    public final InputMethodManager mInputMethodManager;
    public boolean mListening;
    public boolean mRecognizing;
    public String mSearchQuery;
    public SearchEditText mSearchTextEditor;
    public SparseIntArray mSoundMap;
    public SoundPool mSoundPool;
    public SpeechOrbView mSpeechOrbView;
    public SpeechRecognizer mSpeechRecognizer;
    public final int mTextColor;
    public final int mTextColorSpeechMode;
    public final int mTextHintColor;
    public final int mTextHintColorSpeechMode;
    public String mTitle;

    public SearchBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public SearchBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SearchBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new Handler();
        this.mAutoStartRecognition = false;
        this.mSoundMap = new SparseIntArray();
        this.mRecognizing = false;
        this.mContext = context;
        Resources resources = getResources();
        LayoutInflater.from(getContext()).inflate(R$layout.lb_search_bar, this, true);
        this.mBarHeight = getResources().getDimensionPixelSize(R$dimen.lb_search_bar_height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, this.mBarHeight);
        layoutParams.addRule(10, -1);
        setLayoutParams(layoutParams);
        setBackgroundColor(0);
        setClipChildren(false);
        this.mSearchQuery = "";
        this.mInputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        this.mTextColorSpeechMode = resources.getColor(R$color.lb_search_bar_text_speech_mode);
        this.mTextColor = resources.getColor(R$color.lb_search_bar_text);
        this.mBackgroundSpeechAlpha = resources.getInteger(R$integer.lb_search_bar_speech_mode_background_alpha);
        this.mBackgroundAlpha = resources.getInteger(R$integer.lb_search_bar_text_mode_background_alpha);
        this.mTextHintColorSpeechMode = resources.getColor(R$color.lb_search_bar_hint_speech_mode);
        this.mTextHintColor = resources.getColor(R$color.lb_search_bar_hint);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBarBackground = ((RelativeLayout) findViewById(R$id.lb_search_bar_items)).getBackground();
        this.mSearchTextEditor = (SearchEditText) findViewById(R$id.lb_search_text_editor);
        ImageView imageView = (ImageView) findViewById(R$id.lb_search_bar_badge);
        this.mBadgeView = imageView;
        Drawable drawable = this.mBadgeDrawable;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        this.mSearchTextEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    SearchBar.this.showNativeKeyboard();
                } else {
                    SearchBar.this.hideNativeKeyboard();
                }
                SearchBar.this.updateUi(z);
            }
        });
        final AnonymousClass2 r0 = new Runnable() {
            public void run() {
                SearchBar searchBar = SearchBar.this;
                searchBar.setSearchQueryInternal(searchBar.mSearchTextEditor.getText().toString());
            }
        };
        this.mSearchTextEditor.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SearchBar searchBar = SearchBar.this;
                if (!searchBar.mRecognizing) {
                    searchBar.mHandler.removeCallbacks(r0);
                    SearchBar.this.mHandler.post(r0);
                }
            }
        });
        this.mSearchTextEditor.setOnKeyboardDismissListener(new SearchEditText.OnKeyboardDismissListener() {
            public void onKeyboardDismiss() {
                SearchBar.this.getClass();
            }
        });
        this.mSearchTextEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (3 == i || i == 0) {
                    SearchBar.this.getClass();
                }
                if (1 == i) {
                    SearchBar.this.getClass();
                }
                if (2 != i) {
                    return false;
                }
                SearchBar.this.hideNativeKeyboard();
                SearchBar.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SearchBar searchBar = SearchBar.this;
                        searchBar.mAutoStartRecognition = true;
                        searchBar.mSpeechOrbView.requestFocus();
                    }
                }, 500);
                return true;
            }
        });
        this.mSearchTextEditor.setPrivateImeOptions("escapeNorth,voiceDismiss");
        SpeechOrbView speechOrbView = (SpeechOrbView) findViewById(R$id.lb_search_bar_speech_orb);
        this.mSpeechOrbView = speechOrbView;
        speechOrbView.setOnOrbClickedListener(new View.OnClickListener() {
            public void onClick(View view) {
                SearchBar.this.toggleRecognition();
            }
        });
        this.mSpeechOrbView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    SearchBar.this.hideNativeKeyboard();
                    SearchBar searchBar = SearchBar.this;
                    if (searchBar.mAutoStartRecognition) {
                        searchBar.startRecognition();
                        SearchBar.this.mAutoStartRecognition = false;
                    }
                } else {
                    SearchBar.this.stopRecognition();
                }
                SearchBar.this.updateUi(z);
            }
        });
        updateUi(hasFocus());
        updateHint();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSoundPool = new SoundPool(2, 1, 0);
        loadSounds(this.mContext);
    }

    public void onDetachedFromWindow() {
        stopRecognition();
        this.mSoundPool.release();
        super.onDetachedFromWindow();
    }

    public void setSearchQueryInternal(String str) {
        if (!TextUtils.equals(this.mSearchQuery, str)) {
            this.mSearchQuery = str;
        }
    }

    public void hideNativeKeyboard() {
        this.mInputMethodManager.hideSoftInputFromWindow(this.mSearchTextEditor.getWindowToken(), 0);
    }

    public void showNativeKeyboard() {
        this.mHandler.post(new Runnable() {
            public void run() {
                SearchBar.this.mSearchTextEditor.requestFocusFromTouch();
                SearchBar.this.mSearchTextEditor.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, (float) SearchBar.this.mSearchTextEditor.getWidth(), (float) SearchBar.this.mSearchTextEditor.getHeight(), 0));
                SearchBar.this.mSearchTextEditor.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, (float) SearchBar.this.mSearchTextEditor.getWidth(), (float) SearchBar.this.mSearchTextEditor.getHeight(), 0));
            }
        });
    }

    public final void updateHint() {
        String string = getResources().getString(R$string.lb_search_bar_hint);
        if (!TextUtils.isEmpty(this.mTitle)) {
            if (isVoiceMode()) {
                string = getResources().getString(R$string.lb_search_bar_hint_with_title_speech, new Object[]{this.mTitle});
            } else {
                string = getResources().getString(R$string.lb_search_bar_hint_with_title, new Object[]{this.mTitle});
            }
        } else if (isVoiceMode()) {
            string = getResources().getString(R$string.lb_search_bar_hint_speech);
        }
        this.mHint = string;
        SearchEditText searchEditText = this.mSearchTextEditor;
        if (searchEditText != null) {
            searchEditText.setHint(string);
        }
    }

    public void toggleRecognition() {
        if (this.mRecognizing) {
            stopRecognition();
        } else {
            startRecognition();
        }
    }

    public void stopRecognition() {
        if (this.mRecognizing) {
            this.mSearchTextEditor.setText(this.mSearchQuery);
            this.mSearchTextEditor.setHint(this.mHint);
            this.mRecognizing = false;
            if (this.mSpeechRecognizer != null) {
                this.mSpeechOrbView.showNotListening();
                if (this.mListening) {
                    this.mSpeechRecognizer.cancel();
                    this.mListening = false;
                }
                this.mSpeechRecognizer.setRecognitionListener((RecognitionListener) null);
            }
        }
    }

    public void startRecognition() {
        if (!this.mRecognizing) {
            if (!hasFocus()) {
                requestFocus();
            }
            if (this.mSpeechRecognizer != null) {
                if (getContext().checkCallingOrSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    this.mRecognizing = true;
                    this.mSearchTextEditor.setText("");
                    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                    intent.putExtra("android.speech.extra.PARTIAL_RESULTS", true);
                    this.mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                        public void onBeginningOfSpeech() {
                        }

                        public void onBufferReceived(byte[] bArr) {
                        }

                        public void onEndOfSpeech() {
                        }

                        public void onEvent(int i, Bundle bundle) {
                        }

                        public void onReadyForSpeech(Bundle bundle) {
                            SearchBar.this.mSpeechOrbView.showListening();
                            SearchBar.this.playSearchOpen();
                        }

                        public void onRmsChanged(float f) {
                            SearchBar.this.mSpeechOrbView.setSoundLevel(f < 0.0f ? 0 : (int) (f * 10.0f));
                        }

                        public void onError(int i) {
                            switch (i) {
                                case 1:
                                    Log.w(SearchBar.TAG, "recognizer network timeout");
                                    break;
                                case 2:
                                    Log.w(SearchBar.TAG, "recognizer network error");
                                    break;
                                case 3:
                                    Log.w(SearchBar.TAG, "recognizer audio error");
                                    break;
                                case 4:
                                    Log.w(SearchBar.TAG, "recognizer server error");
                                    break;
                                case 5:
                                    Log.w(SearchBar.TAG, "recognizer client error");
                                    break;
                                case 6:
                                    Log.w(SearchBar.TAG, "recognizer speech timeout");
                                    break;
                                case 7:
                                    Log.w(SearchBar.TAG, "recognizer no match");
                                    break;
                                case 8:
                                    Log.w(SearchBar.TAG, "recognizer busy");
                                    break;
                                case 9:
                                    Log.w(SearchBar.TAG, "recognizer insufficient permissions");
                                    break;
                                default:
                                    Log.d(SearchBar.TAG, "recognizer other error");
                                    break;
                            }
                            SearchBar.this.stopRecognition();
                            SearchBar.this.playSearchFailure();
                        }

                        public void onResults(Bundle bundle) {
                            ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                            if (stringArrayList != null) {
                                SearchBar.this.mSearchQuery = stringArrayList.get(0);
                                SearchBar searchBar = SearchBar.this;
                                searchBar.mSearchTextEditor.setText(searchBar.mSearchQuery);
                                SearchBar.this.submitQuery();
                            }
                            SearchBar.this.stopRecognition();
                            SearchBar.this.playSearchSuccess();
                        }

                        public void onPartialResults(Bundle bundle) {
                            ArrayList<String> stringArrayList = bundle.getStringArrayList("results_recognition");
                            if (stringArrayList != null && stringArrayList.size() != 0) {
                                SearchBar.this.mSearchTextEditor.updateRecognizedText(stringArrayList.get(0), stringArrayList.size() > 1 ? stringArrayList.get(1) : null);
                            }
                        }
                    });
                    this.mListening = true;
                    this.mSpeechRecognizer.startListening(intent);
                    return;
                }
                throw new IllegalStateException("android.permission.RECORD_AUDIO required for search");
            }
        }
    }

    public void updateUi(boolean z) {
        if (z) {
            this.mBarBackground.setAlpha(this.mBackgroundSpeechAlpha);
            if (isVoiceMode()) {
                this.mSearchTextEditor.setTextColor(this.mTextHintColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            } else {
                this.mSearchTextEditor.setTextColor(this.mTextColorSpeechMode);
                this.mSearchTextEditor.setHintTextColor(this.mTextHintColorSpeechMode);
            }
        } else {
            this.mBarBackground.setAlpha(this.mBackgroundAlpha);
            this.mSearchTextEditor.setTextColor(this.mTextColor);
            this.mSearchTextEditor.setHintTextColor(this.mTextHintColor);
        }
        updateHint();
    }

    public final boolean isVoiceMode() {
        return this.mSpeechOrbView.isFocused();
    }

    public void submitQuery() {
        TextUtils.isEmpty(this.mSearchQuery);
    }

    public final void loadSounds(Context context) {
        int[] iArr = {R$raw.lb_voice_failure, R$raw.lb_voice_open, R$raw.lb_voice_no_input, R$raw.lb_voice_success};
        for (int i = 0; i < 4; i++) {
            int i2 = iArr[i];
            this.mSoundMap.put(i2, this.mSoundPool.load(context, i2, 1));
        }
    }

    public final void play(final int i) {
        this.mHandler.post(new Runnable() {
            public void run() {
                SearchBar.this.mSoundPool.play(SearchBar.this.mSoundMap.get(i), 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
    }

    public void playSearchOpen() {
        play(R$raw.lb_voice_open);
    }

    public void playSearchFailure() {
        play(R$raw.lb_voice_failure);
    }

    public void playSearchSuccess() {
        play(R$raw.lb_voice_success);
    }

    public void setNextFocusDownId(int i) {
        this.mSpeechOrbView.setNextFocusDownId(i);
        this.mSearchTextEditor.setNextFocusDownId(i);
    }
}
