package com.eshel.jump;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Data;
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

    @Data("tel:1008611")
    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone(Context context);

    @Intent(data = "tel:15001045147")
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone1(Context context);

    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone2(Context context, @Data Uri data);

    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone3(Context context, @Data String data);

    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone4(Context context, @Data("tel:") String phone);

    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone5(Context context, @Data("tel:%s") String phone);

    @Intent
    @Action(android.content.Intent.ACTION_DIAL)
    void jumpCallPhone6(Context context, @Data("tel:%d") int phone);

    @Intent(target = AtListActivity.class, intentType = IntentType.MemoryIntent)
    void jumpAtList(Context context, @Params(AtListActivity.AT_CALLBACK) AtListActivity.AtCallback callback);
/*
    @Intent(target = DemoAct.class)
    void v2_0test(@AContext Context context, @Flag int flag, @Action String action);*/
}
