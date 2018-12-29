package com.eshel.jump;

import android.content.Context;

import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.Params;

public interface Jump {
    @Intent(target = DemoAct.class, intentType = IntentType.MemoryIntent)
    void jumpDemoAct(Context context, @Params("Int") int pInt, @Params("Float") float pFloat, @Params("String") String pString);

    @Intent(target = DemoAct.class, intentType = IntentType.MemoryIntent)
    void jumpDemoActForBean(Context context, @Params("Float") float pFloat, @Params("Bean") Bean bean);

    @Intent(target = DemoAct.class, intentType = IntentType.Intent)
    void jumpDemoActForBeanS(Context context, @Params("Float") float pFloat, @Params("BeanS") BeanS bean);
}
