package com.eshel.jump;

import android.graphics.Color;
import android.widget.EditText;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Created by guoshiwen on 2019/4/8.
 */

public class Test {

    public static void main(String args[]){
        char c = ' ';
        System.out.println(processingData(1, 9980));
    }

    private ImpeccableAtTextWatcher atwatcher = null;
    private void initEditText(EditText editText){
        ImpeccableAtTextWatcher.AtListener listener = new ImpeccableAtTextWatcher.AtListener() {
            @Override
            public void triggerAt() {
                //在此处跳转好友列表界面
                //... 从好友界面返回将选择的好友添加至EditText
                //键盘输入@使用该方法添加
                atwatcher.insertTextForAt("张三", 23);

                //外部点击按钮跳转好友列表返回添加使用方法 insertTextForAtIndex()
//                atwatcher.insertTextForAtIndex("张三", 23);
            }
        };
        atwatcher = new ImpeccableAtTextWatcher(editText, Color.RED, listener);
        editText.addTextChangedListener(atwatcher);
    }

    public static String processingData(int checkType, double checkNum) {

        //如果值是0 那么返回值就是赞或者评论；
        String resultContent = "";
        if (checkNum == 0) {
            switch (checkType) {
                case 1:
                    resultContent = "赞";

                    break;
                case 2:
                    resultContent = "评论";

            }
            //大于0 小于1000
        } else if (checkNum > 0 && checkNum < 1000) {
            resultContent = (int) checkNum + "".trim();
        } else {
            double result = 0;
            String resultEnd = "";
            if (checkNum >= 1000 && checkNum <= 10000) {
                double num = checkNum;
                result = num / 1000d;
                resultEnd = "k";

            } else if (checkNum >= 10000) {
                result = checkNum / 10000d;
                resultEnd = "w";
            }


            BigDecimal bg = new BigDecimal(result);
            double reslultNum = bg.setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
            resultContent = reslultNum + resultEnd;
        }
        return resultContent;


    }
}
