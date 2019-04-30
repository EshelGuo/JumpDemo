## JumpLib的目的
* 封装 Intent 跳转Activity
* 对 Intent 跳转进行统一配置
* 简化跳转 Activity 代码

## 如何使用JumpLib? 

* 在app moudle 的build.gradle中添加如下依赖:

	    implementation "com.eshel.lib:jump:2.1"

* 定义接口配置:
    
```java
public interface Jump{//接口名随意
    @Intent(target = MainActivity.class)
    void jumpMainActivity(Context context, @Params("Param1") String params);//方法名随意
}
```

* 获取 Jump 接口并跳转:

```
Jump jump = JumpHelper.create(Jump.class);
jump.jumpMainActivity(this, "params");
```

* 在 MainActivity 中解析参数:

```java
public class MainActivity extends Activity{
    public static final String TAG = MainActivity.class.getSimpleName();
    public void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_main);
        JumpHelper.parseIntent(this, getIntent());
    }
    
    @IntentParser
    public void testAbcParams(@Params("Param1") String params){//方法名随便起
        Log.i(TAG, params);
    }
}
```

## [JumpRouter 通过链接显式意图跳转Activity](https://github.com/EshelGuo/JumpDemo/blob/version_2.0/jump/JumpRouter%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.md)


## 更新内容: 
### 1.0
* 支持Activity显式意图跳转
* 加入 `@Intent` 注解
* 加入`@IntentParser` 注解
* 加入 `@Params` 注解

### 1.1
* 新增 **parserId:** `@Intent(parserId = 0)` -- `@IntentParser(id = 0)`, 解析时会执行与之相对应的被`@IntentParser`注解的方法

### 2.0
* 支持隐示意图跳转
* 支持发送广播
* 新增注解`@Action`
* 新增注解`@Flag`
* 新增注解`@Category`
* 新增注解`@ExtraParams`
* 新增注解`@TargetClass`
* 新增注解`@TargetName`
* 新增注解`@Type`
* 新增注解`@AContext`
* 增加日志配置

### 2.1
* 新增注解 `@Data`
* 新增 JumpRouter 链接跳转Activity
### 2.2 
* 增加解析Intent参数时直接在成员变量上加注解, 调用`JumpHelper.inject(this, getIntent())`会自动将数据注入成员变量