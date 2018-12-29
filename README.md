说起跳转Activity, 那肯定离不开 Intent, 那么我们平时怎么书写Activity显示跳转呢?
我们可能会这么写:

```java

    Intent intent = new Intent();
    intent.putExtra("NAME", "张三");
    intent.putExtra("AGE", 18);
    intent.putExtra("PHONE", 123456);
    intent.putExtra("MONEY", 0.1);
    startActivity(intent);

```

或者优化一下:

```java

	startActivity(
		new Intent()
            .putExtra("NAME", "张三")
            .putExtra("AGE", 18)
            .putExtra("PHONE", 123456)
            .putExtra("MONEY", 0.1)
	);

```

或者再封装一下:

```java

	TUtils.startActivityForResult(contextWrap,
        new TIntentWap(IntentUtils.getPickMultipleIntent(contextWrap, limit), TConstant.RC_PICK_MULTIPLE));

```
但实际上IntentUtils内部依然需要这样写:

```java

    public static Intent getPickMultipleIntent(TContextWrap contextWrap, int limit) {
        Intent intent = new Intent(contextWrap.getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, limit > 0 ? limit : 1);
        return intent;
    }

```

使用IntentUtils封装一下固然好, 但依旧会写很多重复的 `new Intent()` 的代码. 在 AndroidStudio 中全局搜索 `new Intent` 如下图, 会发现项目中有很多重复的 `new Intent`:
<center> ![](https://i.imgur.com/JZmuNBm.png) </center>

以及很多重复的 `putExtra` :

<center> ![](https://i.imgur.com/J5S991D.png) </center>

那么能不能使用一种方法, 类似Retrofit2 的方式, 在一个接口中定义方法并使用注解配置一下, 不需要写接口的实现类就能实现Activity的跳转? 看了Retrofit2的源码后发现底层是利用动态代理, 于是我开始造轮子, 花了一天时间终于实现了效果:

<center> ![](https://i.imgur.com/LQzqIxo.png) </center>

如上图是定义的接口, Activity的跳转可以定义在接口中, 其中每一个方法必须要有一个 **Context** 对象, 方法需要使用 `@Intent` 注解, 其中 **target** 是要跳转的目标 Activity , intentType 是 Intent 的类型(**MemoryIntent** 是使用Map保存变量的方式来传递参数, **Intent** 是使用系统的Intent).

使用的时候:
<center> ![](https://i.imgur.com/WzTP1ur.png) </center>
JumpFactory 是Demo中的一个类: 
<center> ![](https://i.imgur.com/n0z5ALD.png) </center>

**JumpUtil** 是 **JumpLib** 即我造的轮子中的类, 只需要调用 **create()** 方法, 不需要实现 Jump 接口, 就可以进行Activity的跳转并能够传递参数.

那么 A 通过这种方式跳转到 BActivity, B 要怎么获取这些参数呢? JumpLib 库提供了这样的方式来解析 Intent 中的参数, 如下图所示, 我们首先定义方法 parseIntent(方法名随意取), 需要给方法加上 **@IntentParser** 的注解, 其中 intentType 是你跳转的传参方式, id 即 调用 `JumpUtil.parseIntent()`  传入的id, **@Params** 注解中的值即 使用 Jump 接口方法中相同注解的值.
<center> ![](https://i.imgur.com/ZRFXX3R.png) </center>
如上图, 在B Activity 中的onCreate() 方法调用 `JumpUtil.parseIntent()` 方法, 就会执行被`@IntentParser` 所标注的对应ID的方法, 在该方法中就是 A要传递给B的参数. 感兴趣的可以前往GitHub 查看Demo [GitHub地址:https://github.com/EshelGuo/ClassLoaderDemo/tree/master/jumpdemo](https://github.com/EshelGuo/ClassLoaderDemo/tree/master/jumpdemo)