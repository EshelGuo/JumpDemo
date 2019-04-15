package com.eshel.imageloader.testreply;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Reply";
    ReplyTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.reply_text_view);
        findViewById(R.id.btn_update).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String firstText = "我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行我有两行啊我有两行啊哈哈我有两行";
        String secondText = "我有两行啊";
        String thirdlyText = "我有行啊我有两行啊哈哈哈\n\n一万行也没用哈哈哈....";

        textView.resetReply();
        textView.addReply("张三: ", firstText, "www.baidu.com");
        textView.addReply("李四: ", secondText, null);
        textView.addReply("王五: ", thirdlyText, null);
        textView.notifyReply(2);

        textView.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"共有1条评论",Toast.LENGTH_LONG).show();
            }
        });

        textView.setAuthorClickListener(new ReplyTextView.AuthorOnClickListener() {
            @Override
            public void onClick(View widget, int index, String authorName) {
                Toast.makeText(widget.getContext(),"点击了作者 -- "+authorName,Toast.LENGTH_LONG).show();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext()   ,"点击了外部...",Toast.LENGTH_LONG).show();
            }
        });

        textView.setPhotoOnClickListener(new ReplyTextView.PhotoOnClickListener() {
            @Override
            public void onClick(View widget, int index, String photoUrl) {
                Toast.makeText(widget.getContext(),photoUrl,Toast.LENGTH_LONG).show();
            }
        });
    }
}
