package com.eshel.jump.enums;

/**
 * Intent 保存数据类型 (intent.putExtral() 的数据, flag action 不由此管理)
 * Intent 将数据保存于 Intent 对象内
 * MemoryIntent 内部使用静态变量的方式管理保存, 效率要比intent高, 但不能跨进程传递
 */
public enum IntentType {
    MemoryIntent, Intent
}
