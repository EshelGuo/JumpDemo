#JumpRouter说明文档

###作用
* 实现超链接跳转本地 Activity
* 目前仅支持显示意图跳转
* 目前仅支持传输基本数据类型

###使用方法
* 生成超链接: 比如`jump://com.eshel.DemoAct?intentType=MemoryIntent&Int=22&Float=12.3f`
* 使用 `JumpRouter.fromLink(context, link).execute()` 执行链接跳转
* 使用 `JumpRouter.generateLink(Call)` 自动生成链接 

###生成超链接规则
**名词:**

* 以超链接`jump://com.eshel.DemoAct?intentType=MemoryIntent&Int=22&Float=12.3f&String=haha`为例
* 整个链接我将其称为 **link**
* 前面的**jump**我将其称为**scheme** 
* 我将 `com.eshel.DemoAct?intentType=MemoryIntent&Int=22&Float=12.3f&String=haha` 称为 **body**
* `com.eshel.DemoAct` 是 **path**
* `intentType=MemoryIntent&Int=22&Float=12.3f&String=haha` 是 **params**(所有参数)
* `intentType=MemoryIntent` 是**param**(一个参数)
* `intentType`为 **key** , `MemoryIntent` 为 **value**

**scheme规则**

* **scheme**有两种, `jump` 和 `jumps`
* `jump` 和 `jumps`的区别是是否需要对**body**体进行 **Base64**编码, `jumps` 需要编码, 而'jump'不需要编码， 示例如下：
* `jump://com.eshel.jump.DemoAct?Float=123.6F123.6F&Int=123123&String=hahahaha&intentType=MemoryIntentMemoryIntent`
* `jumps://Y29tLmVzaGVsLmp1bXAuRGVtb0FjdD9GbG9hdD0xMjMuNkYxMjMuNkYmSW50PTEyMzEyMyZTdHJpbmc9aGFoYWhhaGEmaW50ZW50VHlwZT1NZW1vcnlJbnRlbnRNZW1vcnlJbnRlbnQ=`
* 以上两种链接跳转的界面和传递的参数完全相同
* 注意: base64 编码格式应为 **UTF-8**

**其他规则**

* path 为要跳转的Activity的全类名
* 参数的值如果包含特殊字符则需要对所有有效字符进行编码(URLEncode.encode()) 编码同样也需要charset为 **UTF-8**
* 如果参数的值特殊, 比如参数值为`12.0f`, 但实际上该参数应该以string类型解析而不是 float 类型, 这用情况下需要给值的左右添加双引号, 标志该参数时string类型 : `"12.0f"`
* 注意, 上一条规则所说双引号不参与 `URLEncode.encode()` 编码