package com.eshel.jump;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class AtProxy implements AtListActivity.AtCallback, View.OnClickListener {
    Activity activity;
    AtTextWatcher atw;
    EditText et;

    public AtProxy(Activity activity, AtTextWatcher attw, EditText et) {
        this.activity = activity;
        atw = attw;
        this.et = et;
        activity.findViewById(R.id.btn_at).setOnClickListener(this);
    }

    @Override
    public void onChooseResult(int userId, String name) {
        int selectionStart = et.getSelectionStart();
        int selectionEnd = et.getSelectionEnd();

        if(selectionEnd > selectionStart){
            selectionStart = selectionEnd;
        }
        atw.insertTextForAtIndex(name, selectionStart, userId);
        /*SpannableString ss = new SpannableString(String.format("@%s%c", name, ImpeccableAtTextWatcher.atEndFlag));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int index = atw.getSelectionStart();
        Editable text = atw.getText();
        text.insert(index, ss);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_at:
                JumpFactory.getJump().jumpAtList(activity, this);
                break;
        }
    }
}
