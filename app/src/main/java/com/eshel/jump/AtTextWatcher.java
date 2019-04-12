package com.eshel.jump;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import static com.eshel.jump.configs.JumpConst.TAG;

/**
 * Created by guoshiwen on 2019/4/8.
 */

public class AtTextWatcher implements TextWatcher {

    char atEndFlag = (char) 8197;
    AtListener mListener;
    private int atIndex = -1;
    private int endFlagIndex = -1;
    public AtTextWatcher(AtListener listener) {
        this.mListener = listener;
    }

    //该方法未测试
    public void insertTextForAtIndex(EditText et, CharSequence text, int atIndex){
        this.atIndex = atIndex;
        StringBuilder sb = new StringBuilder();
        sb.append('@');
        insertTextForAtInternal(et, text, sb);
    }

    public void insertTextForAt(EditText et, CharSequence text){
        if(atIndex == -1)
            return;
        StringBuilder sb = new StringBuilder();
        insertTextForAtInternal(et, text, sb);
//        et.invalidate();
    }

    private void insertTextForAtInternal(EditText et, CharSequence text, StringBuilder sb){
        sb.append(text);
        sb.append(atEndFlag);
        text = sb.toString();
        Editable text1 = et.getText();
        text1.insert(atIndex+1, text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(count == 1){//删除一个字符
            char c = s.charAt(start);
            if(c == atEndFlag){
                endFlagIndex = start;
            }
        }
    }

    /**
     * @param s 新文本内容，即文本改变之后的内容
     * @param start 被修改文本的起始偏移量
     * @param before 被替换旧文本长度
     * @param count 替换的新文本长度
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count == 1){//新增(输入)一个字符
            char c = s.charAt(start);
            if(c == '@'){
                atIndex = start;
                if(mListener != null){
                    mListener.triggerAt();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i(TAG, "afterTextChanged() called with: s = [" + s + "]");
        if(endFlagIndex != -1){
            int index = endFlagIndex;
            while ((index -= 1) != -1){
                char c = s.charAt(index);
                if(c == '@'){
                    break;
                }
            }
            int endFlagIndex = this.endFlagIndex;
            this.endFlagIndex = -1;
            if(index != -1)
                s.delete(index, endFlagIndex);
        }
    }

    /**
     * 输入 @ 监听
     */
    public interface AtListener{
        void triggerAt();
    }
}
