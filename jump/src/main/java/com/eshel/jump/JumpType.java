package com.eshel.jump;

/**
 * 跳转方式, startActivity() 或者 startActivityForResult()
 */
public enum JumpType {
    /**
     * startActivity()
     */
    StartAct,
    /**
     * startActivityForResult()
     */
    StartActForResult,
    /**
     * 发送广播
     */
    SendBroadcastReceiver,
    /**
     * 开启服务
     */
    StartService
}
