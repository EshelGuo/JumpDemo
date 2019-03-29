package com.eshel.jump.enums;

import android.app.Activity;
import android.view.View;

/**
 * Created by guoshiwen on 2019/3/29.
 * 上下文类型
 * interface Jump{
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin0(@AContext(ContextType.Context)Activity context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin(@AContext(ContextType.Context)Context context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin2(@AContext(ContextType.Activity)Context context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin3(@AContext(ContextType.Activity)Activity context);
 *     @Intent(targetClass = LoginActivity.class)
 *     void jumpLogin4(@AContext(ContextType.Fragment)Fragment context);
 * }
 */

public enum  ContextType {
    /**
     * 优先以 Activity 解析
     * 其次以 Context 解析
     */
    Context,
    /**
     * 必须以 Activity 解析
     */
    Activity,
    /**
     * 必须为 Fragment
     */
    Fragment,
    /**
     * 必须为 View
     */
    View
}
