package com.eshel.jump;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by guoshiwen on 2019/4/12.
 */

public class ImpeccableAtTextWatcher implements TextWatcher {
    int color;
    public static char atEndFlag = (char) 8197;
    EditText et;

    public ImpeccableAtTextWatcher(EditText et, int highLightColor) {
        this.color = highLightColor;
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
