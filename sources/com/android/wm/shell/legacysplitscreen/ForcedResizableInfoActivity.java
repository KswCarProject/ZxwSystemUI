package com.android.wm.shell.legacysplitscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.wm.shell.R;

public class ForcedResizableInfoActivity extends Activity implements View.OnTouchListener {
    public final Runnable mFinishRunnable = new Runnable() {
        public void run() {
            ForcedResizableInfoActivity.this.finish();
        }
    };

    public void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onStart() {
        super.onStart();
        getWindow().getDecorView().postDelayed(this.mFinishRunnable, 2500);
    }

    public void onStop() {
        super.onStop();
        finish();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        finish();
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        finish();
        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.forced_resizable_exit);
    }
}
