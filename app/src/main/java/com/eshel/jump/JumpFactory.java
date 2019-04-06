package com.eshel.jump;

public class JumpFactory {

    private static Jump mJump;

    public static Jump getJump(){
        if(mJump == null)
            mJump = JumpHelper.create(Jump.class);
        return mJump;
    }
}
