package com.eshel.imageloader.testreply;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ClickSpanOnTouchListener implements View.OnTouchListener{
    ClickSpanInvoker invoker = new ClickSpanInvoker();
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v instanceof TextView)
            return invoker.onTouch((TextView) v, event);
        return false;
    }
}
