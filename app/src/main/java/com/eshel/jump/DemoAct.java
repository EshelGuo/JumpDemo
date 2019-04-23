package com.eshel.jump;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.enums.IntentType;

import java.util.Locale;

public class DemoAct extends Activity {

    @Params("Int")
    int pInt;
    @Params("Float")
    float pFloat;
    @Params("String")
    String pString;
    @Params("Bean")
    Bean bean;
    @Params("BeanS") BeanS beanS;

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mTextView = findViewById(R.id.textView2);
        JumpHelper.inject(this, getIntent());
        parseIntent(pInt, pFloat, pString, bean, beanS);

 /*       float flag = JumpUtil.getFlagFloat(this, getIntent(), "Float");
        if(flag == 0){
            JumpUtil.parseIntent(0, this, getIntent(), true);
        }else if(flag == 1){
            JumpUtil.parseIntent(1, this, getIntent(), true);
        }else{
            JumpUtil.parseIntent(2, this, getIntent(), true);
        }*/

        Toast.makeText(this,"jump://"+JUtils.base64Encode("com.eshel.MainActivity?title=你好&id=2&id=S_2"),Toast.LENGTH_LONG).show();
//        JumpUtil.parseMemoryIntent(this/*, getIntent()*/);
    }

    @IntentParser(intentType = IntentType.MemoryIntent, id = 2)
    public void parseIntent(@Params("Int") int pInt, @Params("Float") float pFloat, @Params("String") String pString){
        mTextView.setText(String.format(Locale.getDefault(),
                "parseIntent() called with: pInt = [%d], pFloat = [%s], pString = [%s]", pInt, pFloat, pString));
    }

    @IntentParser(intentType = IntentType.MemoryIntent, id = 0)
    public void parseIntent(@Params("Float") float pFloat, @Params("Bean") Bean bean){
        if(bean == null)
            return;
        mTextView.setText(String.format(Locale.getDefault(),
                "parseIntent() called with: pFloat = [%s], bean = [%s]", pFloat, bean.toString()));
    }

    @IntentParser(intentType = IntentType.Intent, id = 1)
    public void parseIntent(@Params("Float") float pFloat, @Params("BeanS") BeanS bean){
        if(bean == null)
            return;
        mTextView.setText(String.format(Locale.getDefault(),
                "parseIntent() called with: pFloat = [%s], BeanS = [%s]", pFloat, bean.toString()));
    }

    public void parseIntent(int pInt, float pFloat, String pString, Bean bean, BeanS beanS){
        mTextView.setText(String.format(Locale.getDefault(),
                "parseIntent() called with: pInt = [%d], pFloat = [%s], pString = [%s], bean = [%s], beanS = [%s]",
                pInt, pFloat, pString, bean, beanS));
    }
}
