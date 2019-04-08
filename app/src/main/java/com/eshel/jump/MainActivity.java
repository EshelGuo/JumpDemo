package com.eshel.jump;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Type;
import com.eshel.jump.configs.JumpConst;
import com.eshel.jump.log.JLog;
import com.eshel.jump.router.JumpRouter;
import com.eshel.jump.router.JumpURI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AtTextWatcher.AtListener {

    private static final String TAG = JumpConst.TAG;
    private Button mButton;
    private RadioGroup mRg_types;
    private RadioButton mIbn;
    private RadioButton mFbn;
    private RadioButton mSbn;
    private EditText mEt_input;

    int mInt;
    float mFloat;
    String mString;
    private AtTextWatcher atTextWatcher;

    String[] names = {"张三", "李四", "王五"};
    Random random = new Random();
    Handler mHandler = new Handler(Looper.getMainLooper());
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        char c = ' ';
        System.out.println(Integer.valueOf(c));

        mRg_types.setOnCheckedChangeListener(this);
        mRg_types.check(mIbn.getId());

        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String text = mEt_input.getText().toString();
                    if(text == null || text.length() == 0)
                        return;
                    switch (mRg_types.getCheckedRadioButtonId()){
                        case R.id.ibn:
                            mInt = Integer.valueOf(text);
                            break;
                        case R.id.fbn:
                            mFloat = Float.valueOf(text);
                            break;
                        case R.id.sbn:
                            mString = text;
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        /*Intent intent = new Intent(MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra();
        intent.setData();

        IntentFilter filter = new IntentFilter();

        intent.setClass();
        intent.setClassName();
        intent.setType("text/plain");*/

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFloat == 0){
                    Call call = JumpFactory.getJump().jumpDemoActForBean(v.getContext(), mFloat, new Bean(mString, mInt));
                    call.execute();
                }else if(mFloat == 1){
                    JumpFactory.getJump().jumpDemoActForBeanS(v.getContext(), mFloat, new BeanS(mString, mInt));
                }else {
                    JumpFactory.getJump().jumpDemoAct(v.getContext(), mInt, mFloat, mString);
                }
            }
        });
    }


    private void findView() {
        mButton = findViewById(R.id.button);
        mRg_types = findViewById(R.id.rg_types);
        mIbn = findViewById(R.id.ibn);
        mFbn = findViewById(R.id.fbn);
        mSbn = findViewById(R.id.sbn);
        mEt_input = findViewById(R.id.et_input);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_link).setOnClickListener(this);
        et = findViewById(R.id.et_at);
        atTextWatcher = new AtTextWatcher(this);
        et.addTextChangedListener(atTextWatcher);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        boolean needClear = false;
        switch (checkedId){
            case R.id.ibn:
                needClear = mInt == 0;
                mEt_input.setHint("请输入一个整数");
                mEt_input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                mEt_input.setText(String.valueOf(mInt));
                break;
            case R.id.fbn:
                mEt_input.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                mEt_input.setHint("请输入一个小数");
                needClear = mFloat == 0;
                mEt_input.setText(String.valueOf(mFloat));
                break;
            case R.id.sbn:
                mEt_input.setHint("请输入一个字符串");
                mEt_input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                needClear = mString == null || mString.length() == 0;
                mEt_input.setText(mString);
                break;
        }

        if(needClear)
            mEt_input.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setting:
                JumpFactory.getJump().jumpSystemSetting(this, Settings.ACTION_ACCESSIBILITY_SETTINGS);
                break;
            case R.id.btn_link:
                String link = null;
                try {
                    link = "jump://com.eshel.jump.DemoAct?intentType=MemoryIntent&Int=22&Float=12.3f&String=\""+ URLEncoder.encode("http://www.baidu.com?user=gsw&test=[{你好}]", "UTF-8")+"\"";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JLog.i(JumpConst.TAG, link);
                JumpRouter.fromLink(this, link).execute();
                JumpURI uri = JumpFactory.getJump().prepareJumpDemoAct(this, 123, 123.6f, "haha").toUri(false);
                JumpURI base64Uri = JumpFactory.getJump().prepareJumpDemoAct(this, 123, 123.6f, "haha").toUri(true);
                JLog.i(JumpConst.TAG, uri.toString());
                JLog.i(JumpConst.TAG, base64Uri.toString());
                break;
        }
    }

    @Override
    public void triggerAt() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                atTextWatcher.insertTextForAt(et, names[random.nextInt(names.length)]);
            }
        }, 500);
    }
}
