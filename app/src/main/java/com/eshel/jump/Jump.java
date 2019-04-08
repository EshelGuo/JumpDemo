package com.eshel.jump;

import android.content.Context;
import android.provider.Settings;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.Params;
import com.eshel.jump.enums.IntentType;

public interface Jump {
    @Intent(target = DemoAct.class, intentType = IntentType.MemoryIntent)
    void jumpDemoAct(Context context, @Params("Int") int pInt, @Params("Float") float pFloat, @Params("String") String pString);
    @Intent(target = DemoAct.class, intentType = IntentType.MemoryIntent)
    Call prepareJumpDemoAct(Context context, @Params("Int") int pInt, @Params("Float") float pFloat, @Params("String") String pString);

    @Intent(target = DemoAct.class, intentType = IntentType.MemoryIntent)
    Call jumpDemoActForBean(Context context, @Params("Float") float pFloat, @Params("Bean") Bean bean);

    @Intent(target = DemoAct.class, intentType = IntentType.Intent)
    void jumpDemoActForBeanS(Context context, @Params("Float") float pFloat, @Params("BeanS") BeanS bean);

    @Intent
    @Action(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    void jumpSystemSetting(Context context);

    @Intent
    void jumpSystemSetting(Context context, @Action String action);
/*
    @Intent(target = DemoAct.class)
    void v2_0test(@AContext Context context, @Flag int flag, @Action String action);*/
}
