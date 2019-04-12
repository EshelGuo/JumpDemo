package com.eshel.imageloader.testreply;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ReplyTextView extends LinearLayout{

    private TextView firstView;
    private TextView secondView;
    private TextView thirdlyView;
    private TextView moreView;

    /*private OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            CharSequence text = ((TextView) v).getText();
            Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
            TextView widget = (TextView) v;
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    }
                    ret = true;
                }
            }
            return ret;
        }
    };*/

    public ReplyTextView(Context context) {
        this(context, null);
    }

    public ReplyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.view_reply, this);
        firstView = findViewById(R.id.reply_first);
        secondView = findViewById(R.id.reply_second);
        thirdlyView = findViewById(R.id.reply_thirdly);
        moreView = findViewById(R.id.reply_more);

//        firstView.setMovementMethod(NoScrollLinkMovementMethod.getInstance());
//        secondView.setMovementMethod(NoScrollLinkMovementMethod.getInstance());
//        thirdlyView.setMovementMethod(NoScrollLinkMovementMethod.getInstance());
//        moreView.setMovementMethod(NoScrollLinkMovementMethod.getInstance());

        firstView.setOnTouchListener(new ClickSpanOnTouchListener());
        secondView.setOnTouchListener(new ClickSpanOnTouchListener());
        thirdlyView.setOnTouchListener(new ClickSpanOnTouchListener());
        moreView.setOnTouchListener(new ClickSpanOnTouchListener());

        firstView.setEllipsize(TextUtils.TruncateAt.END);
        secondView.setEllipsize(TextUtils.TruncateAt.END);
        thirdlyView.setEllipsize(TextUtils.TruncateAt.END);

//        firstView.setOnTouchListener(listener);
//        secondView.setOnTouchListener(listener);
//        thirdlyView.setOnTouchListener(listener);
    }

    public void initTextsStyle(ReplyInitHandler handler){
        if(handler == null)
            return;
        handler.initTextStyle(firstView, secondView, thirdlyView, moreView);
    }

    public void setMoreClickListener(View.OnClickListener listener){
        moreView.setOnClickListener(listener);
    }

    /**
     * 设置文字(单位是 条 不是 行)
     * @param firstText 第一条
     * @param secondText 第二条
     * @param thirdlyText 第三条
     * @param allLength 回复总条数
     */
    public void setText(CharSequence firstText, CharSequence secondText, CharSequence thirdlyText, CharSequence moreText, int allLength){
        firstView.setVisibility(VISIBLE);
        secondView.setVisibility(VISIBLE);
        thirdlyView.setVisibility(VISIBLE);
        moreView.setVisibility(VISIBLE);

        firstView.setText(firstText);
        secondView.setText(secondText);
        thirdlyView.setText(thirdlyText);
        moreView.setText(moreText);
        int residueLine = 3;//剩余行数
        int showLength = 0;//显示回复条数
        int firstLine = firstView.getLineCount();
        int secondLine = secondView.getLineCount();
        int thirdlyLine = thirdlyView.getLineCount();
        Toast.makeText(getContext(),String.format(Locale.CHINA, "firstLine: %d, secondLine: %d, thirdlyLine: %d", firstLine, secondLine, thirdlyLine),Toast.LENGTH_LONG).show();
        if(firstLine > 2){
            firstLine = 2;
            firstView.setMaxLines(firstLine);
            firstView.setEllipsize(TextUtils.TruncateAt.END);
            firstView.setText(firstText);
        }

        showLength+=1;
        residueLine -= firstLine;

        if(secondLine > 2){
            secondLine = 2;
        }

        if(secondLine > residueLine){
            secondLine = residueLine;
        }
        secondView.setMaxLines(secondLine);
        residueLine -= secondLine;
        showLength+=1;

        if(residueLine == 0){
            thirdlyView.setVisibility(GONE);
        }else {
            thirdlyLine = residueLine;
            thirdlyView.setMaxLines(thirdlyLine);
            showLength+=1;
        }

        if(showLength == allLength)
            moreView.setVisibility(GONE);
    }

    public interface ReplyInitHandler{
        void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more);
    }
}
