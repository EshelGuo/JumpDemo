package com.eshel.imageloader.testreply;

import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ClickSpanInvoker{
    int downX;
    int downY;
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
            downX = (int) event.getX();
            downY = (int) event.getY();

            ClickableSpan[] clickSpan = getClickSpan(widget, downX, downY);
            return clickSpan != null && clickSpan.length != 0;
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
            CharSequence text = widget.getText();
            Class<? extends CharSequence> type = text.getClass();

            if(!(Spannable.class.isAssignableFrom(type) || text instanceof Spanned))
                return false;

            ClickableSpan[] links = getClickSpan(widget, x, y);

            if (links != null && links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    links[0].onClick(widget);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Nullable
    private ClickableSpan[] getClickSpan(TextView widget, int x, int y) {
        CharSequence text = widget.getText();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        Spannable spannable = null;
        if(text instanceof Spannable)
			spannable = (Spannable) text;

        Spanned spanned = null;
        if(text instanceof Spanned)
			spanned = (Spanned) text;

        ClickableSpan[] links = null;
        if(spannable != null)
			links = spannable.getSpans(off, off, ClickableSpan.class);
        if(spanned != null)
			links = spanned.getSpans(off, off, ClickableSpan.class);
        return links;
    }
}


