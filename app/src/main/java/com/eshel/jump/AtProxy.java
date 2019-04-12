package com.eshel.jump;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class AtProxy implements AtListActivity.AtCallback, View.OnClickListener {
    Activity activity;
    EditText mEtAt;

    public AtProxy(Activity activity, EditText mEtAt2) {
        this.activity = activity;
        mEtAt = mEtAt2;
        ImpeccableAtTextWatcher iaTextWatcher = new ImpeccableAtTextWatcher(mEtAt, Color.RED);
        mEtAt2.addTextChangedListener(iaTextWatcher);
    }

    @Override
    public void onChooseResult(int userId, String name) {
        SpannableString ss = new SpannableString(String.format("@%s%c", name, ImpeccableAtTextWatcher.atEndFlag));
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int index = mEtAt.getSelectionStart();
        Editable text = mEtAt.getText();
        text.insert(index, ss);
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
