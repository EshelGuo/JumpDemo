package com.eshel.imageloader.testreply;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ReplyTextView.ReplyInitHandler, View.OnClickListener {
    private static final String TAG = "Reply";
    ReplyTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.reply_text_view);
        textView.initTextsStyle(this);
        findViewById(R.id.btn_update).setOnClickListener(this);
    }

    @Override
    public void initTextStyle(TextView first, TextView second, TextView thirdly, TextView more) {

    }

    @Override
    public void onClick(View v) {
        CharSequence firstText = "我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈";

        CharSequence secondText = "我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈我有两行啊我有两行啊哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈";
        CharSequence thirdlyText = "我有\n\n一万行也没用哈哈哈....";
        firstText = setNormalReplySpan("张三: ", firstText, "www.baidu.com");
        secondText = setNormalReplySpan("李四: ", secondText, null);
        thirdlyText = setNormalReplySpan("王五: ", thirdlyText, null);

        CharSequence moreText = "共3条回复";
        moreText = setMoreReplySpan(moreText);

        textView.setText(firstText, secondText, thirdlyText, moreText, 3);
    }

    private CharSequence setMoreReplySpan(CharSequence moreText) {
        SpannableString ss = new SpannableString(moreText);
        ss.setSpan(new ForegroundColorSpan(0xff33AFED), 0, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(widget.getContext(),"点击了共x条回复",Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private CharSequence setNormalReplySpan(String author, CharSequence firstText, final String imagePath) {
        TextView tv = textView.getFirstView();
        int width = tv.getWidth();
        if(width == 0){
            tv.measure(0,0);
            width = tv.getMeasuredWidth();
        }
        firstText = TextUtils.ellipsize(firstText, textView.getTextPaint(), width - tv.getPaddingLeft() -  tv.getPaddingRight(), TextUtils.TruncateAt.MIDDLE);
        String imageText = "[查看图片]";
        String source = author + firstText + (imagePath == null ? "" : imageText);
        int authorStart = 0;
        int authorEnd = author.length() + authorStart;

        int firstTextStart = authorEnd;
        int firstTextEnd = firstTextStart + firstText.length();

        int imageStart = -1;
        int imageEnd = -1;

        if(imagePath != null) {
            imageStart = firstTextEnd;
            imageEnd = imageStart + imageText.length();
        }
        SpannableString ss = new SpannableString(source);
        Log.i(TAG, String.valueOf(ss.length()));
        ss.setSpan(new ForegroundColorSpan(0xff4a4a4a), authorStart, authorEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xffA4A4A4), firstTextStart, firstTextEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(imagePath != null) {
            ss.setSpan(new ForegroundColorSpan(0xff33AFED), imageStart, imageEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(widget.getContext(),imagePath,Toast.LENGTH_LONG).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            }, imageStart, imageEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
}
