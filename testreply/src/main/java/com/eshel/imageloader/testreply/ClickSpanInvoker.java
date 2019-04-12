package com.eshel.imageloader.testreply;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ClickSpanInvoker{
    float downX;
    float downY;
    private ViewConfiguration vc;
    private int mSlop;

    public ClickSpanInvoker() {

    }

    public boolean onTouch(TextView widget, MotionEvent event) {
        if(vc == null) {
            vc = ViewConfiguration.get(widget.getContext());
        }
        if(mSlop == 0) {
            mSlop = vc.getScaledTouchSlop();
        }
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            downX = event.getX();
            downY = event.getY();
            return true;
        }

        if(action == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float y = event.getY();

            float moveX = Math.abs(x - downX);
            float moveY = Math.abs(y - downY);

            if(moveX > mSlop || moveY > mSlop){
                return false;
            }
        }

        if(action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            if (y > widget.getHeight() || x > widget.getWidth() || y < 0 || x < 0) {
                return true;
            }

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            Spannable spannable = (Spannable) widget.getText();
            ClickableSpan[] links = spannable.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    links[0].onClick(widget);
                    return true;
                }/* else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(spannable,
                            spannable.getSpanStart(links[0]),
                            spannable.getSpanEnd(links[0]));
                }*/
                return false;
            }
        }
        return false;
    }
}


