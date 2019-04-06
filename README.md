## JumpLib的目的
* 封装 Intent 跳转Activity
* 对 Intent 跳转进行统一配置
* 简化跳转 Activity 代码

## 如何使用JumpLib? 

* 在app moudle 的build.gradle中添加如下依赖:

	    implementation "com.eshel.lib:jump:2.0"

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
