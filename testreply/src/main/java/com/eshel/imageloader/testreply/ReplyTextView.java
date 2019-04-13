package com.eshel.imageloader.testreply;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ReplyTextView extends LinearLayout{
    private static final char endFlag = (char) 8198;

    private TextView firstView;
    private TextView secondView;
    private TextView thirdlyView;
    private TextView moreView;

    private int residueLine = 3;//剩余行数
    private int showReplyLength = 0;//显示回复条数

    private List<ReplyData> replyDatas = new ArrayList<>(3);
    private ReplyInitHandler handler = new ReplyInitHandler() {
        @Override
        public void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more) {
        }

        @Override
        public String getPhotoShowText(String photoUrl) {
            return "[查看图片]";
        }

        @Override
        public int getAuthorTextColor() {
            return 0xff4a4a4a;
        }

        @Override
        public int getContentTextColor() {
            return 0xffA4A4A4;
        }

        @Override
        public int getPhotoTextColor() {
            return 0xff33AFED;
        }

        @Override
        public int getMoreColor() {
            return 0xff33AFED;
        }

        @Override
        public String getMoreText(int replyLength) {
            return String.format(Locale.CHINA, "共有%d条回复", replyLength);
        }

        @Override
        public void onClickPhoto(View view, String photoUrl) {
            Toast.makeText(view.getContext(),photoUrl,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onClickMore(View view) {
            Toast.makeText(view.getContext(),"共有1条评论",Toast.LENGTH_LONG).show();
        }
    };
    {
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
    }
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

//        firstView.setEllipsize(TextUtils.TruncateAt.START);
//        secondView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
//        thirdlyView.setEllipsize(TextUtils.TruncateAt.MIDDLE);

//        firstView.setOnTouchListener(listener);
//        secondView.setOnTouchListener(listener);
//        thirdlyView.setOnTouchListener(listener);
    }

    public void initTextsStyle(ReplyInitHandler handler){
        if(handler == null)
            return;
        this.handler = handler;
        handler.initTextStyle(firstView, secondView, thirdlyView, moreView);
    }

    public void resetReply(){
        replyDatas.clear();
    }

    public void addReply(String author, String content, String photoUrl){
        replyDatas.add(new ReplyData(author, content, photoUrl));
    }

    public void notifyReply(int allReplyCount){
        residueLine = 3;
        showReplyLength = 0;

        int size = replyDatas.size();
        if(size == 0){
            setVisibility(GONE);
            return;
        }else {
            setVisibility(VISIBLE);
        }
        notifyReplyText(replyDatas.get(0), firstView);

        notifyReplyText(size >= 2 ? replyDatas.get(1) : null, secondView);
        notifyReplyText(size >= 3 ? replyDatas.get(2) : null, thirdlyView);


        if(allReplyCount > showReplyLength){
            moreView.setVisibility(VISIBLE);
            String moreText = handler.getMoreText(allReplyCount);
            SpannableString ss = new SpannableString(moreText);
            ss.setSpan(new ForegroundColorSpan(handler.getMoreColor()), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    handler.onClickMore(widget);
                }
            }, 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreView.setText(ss);
        }else {
            moreView.setVisibility(GONE);
        }
    }

    private void notifyReplyText(final ReplyData data, TextView tv) {
        if(residueLine <= 0 || data == null){
            tv.setVisibility(GONE);
            return;
        }else {
            tv.setVisibility(VISIBLE);
        }

        TextPaint paint = tv.getPaint();

        String ellipsisSymbol = "...";
        float ellipsisWidth = paint.measureText(ellipsisSymbol);

        String photoText = "";
        float authorWidth = paint.measureText(data.author);
        float photoWidth = 0;
        if(data.photoUrl != null) {
            photoText = handler.getPhotoShowText(data.photoUrl) + endFlag;
            photoWidth = paint.measureText(photoText);
        }

        int width = tv.getWidth();
        if(width == 0){
            tv.measure(0,0);
            width = tv.getMeasuredWidth();
        }
        int maxLine = 2;
        if(maxLine > residueLine)
            maxLine = residueLine;
        int lineWidth = width - tv.getPaddingLeft() -  tv.getPaddingRight();
        CharSequence contentText = TextUtils.ellipsize(data.content, paint, lineWidth * 2 - authorWidth - photoWidth - ellipsisWidth, TextUtils.TruncateAt.END);

        CharSequence wholeText = data.author + contentText + photoText;

        int authorStart = 0;
        int authorEnd = data.author.length() + authorStart;

        int contentStart = authorEnd;
        int contentEnd = contentStart + contentText.length();

        int imageStart = -1;
        int imageEnd = -1;

        if(data.photoUrl != null){
            imageStart = contentEnd;
            imageEnd = imageStart + photoText.length() - 1;
        }

        SpannableString ss = new SpannableString(wholeText);
        ss.setSpan(new ForegroundColorSpan(handler.getAuthorTextColor()), authorStart, authorEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(handler.getContentTextColor()), contentStart, contentEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(imageStart != -1){
            ss.setSpan(new ForegroundColorSpan(handler.getPhotoTextColor()), imageStart, imageEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    handler.onClickPhoto(widget, data.photoUrl);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, imageStart, imageEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tv.setText(ss);
        int lineCount = tv.getLineCount();
        if(lineCount > maxLine) {
            tv.setLines(maxLine);
            lineCount = maxLine;
        }

        residueLine -= lineCount;
        showReplyLength++;
    }

    public interface ReplyInitHandler{
        void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more);
        String getPhotoShowText(String photoUrl);
        int getAuthorTextColor();
        int getContentTextColor();
        int getPhotoTextColor();
        int getMoreColor();
        String getMoreText(int replyLength);
        void onClickPhoto(View view, String photoUrl);
        void onClickMore(View view);
    }

    public static class ReplyData{
        public String author;
        public String content;
        public String photoUrl;

        public ReplyData(String author, String content, String photoUrl) {
            this.author = author;
            this.content = content;
            this.photoUrl = photoUrl;
        }
    }
}
