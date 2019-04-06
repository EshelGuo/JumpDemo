package com.eshel.jump;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.enums.IntentType;

import java.util.Locale;

public class DemoAct extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mTextView = findViewById(R.id.textView2);

        float flag = JumpUtil.getFlagFloat(this, getIntent(), "Float");
        if(flag == 0){
            JumpUtil.parseIntent(0, this, getIntent(), true);
        }else if(flag == 1){
            JumpUtil.parseIntent(1, this, getIntent(), true);
        }else{
            JumpUtil.parseIntent(2, this, getIntent(), true);
        }
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
}
