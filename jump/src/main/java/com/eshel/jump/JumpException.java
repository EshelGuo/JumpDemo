package com.eshel.jump;


import java.lang.reflect.Method;

public class JumpException extends RuntimeException{
    public JumpException(JumpExpType type, Object interface_, Method method) {
        super(type.getAllMessage(interface_, method));
    }

    public JumpException(JumpExpType type) {
        super(type.message);
    }

    /**
     * 异常类型
     */
    public enum JumpExpType{

        HaveMethodNoIntentAnno("接口中有方法没有被[@Intent]注解修饰")
      , NoneContext("方法中没有上下文 [Context] 参数, 必须有一个没有被 [@Params] 修饰的 [Context] 参数")
      , TargetIsNull("要跳转的目标Activity为 null")
      , MethodNoneParams("方法没有参数")
      , NoneParamsKey("要通过Intent传递的参数没有使用注解[@Params]修饰")
      , ParamsNeedImplSerializable("参数必须是基本数据类型或需要实现 Serializable 接口")
      , ContextNotActivity("使用注解[@Intent(jumpType = JumpType.StartActForResult)]时, 提供的 [Context] 必须是一个 Activity")
      , NoneRequestCode("使用[@Intent(jumpType = JumpType.StartActForResult)]方式跳转Activity但是没有提供 requestCode, 需要这样提供: [@Intent(jumpType = JumpType.StartActForResult, requestCode = 1001)]")
      , NoneIntentParser("使用了 JumpUtil.parseIntent() ,但调用的类中没有方法使用[@IntentParser]注解标注, 故该调用无效")
      , InvokeParseIntentIsNull("使用了 JumpUtil.parseIntent()但传入的Intent是null")
      , AnnoParamsLenthUnlikeness("注解和参数长度不相等, 请检查:")  ;
        public String message;

        JumpExpType(String message) {
            this.message = message;
        }

        public String getAllMessage(Object invoker, Method method){
//          Proxy
            StringBuilder name = new StringBuilder(invoker.getClass().getSimpleName());
            Class<?>[] interfaces = invoker.getClass().getInterfaces();
            Class<?> superclass = invoker.getClass().getSuperclass();
            if(superclass != null) {
                name.append(" extends ");
                name.append(superclass.getSimpleName());
            }

            if(interfaces != null && interfaces.length != 0){
                name.append(" implements");
                for (int i = 0; i < interfaces.length; i++) {
                    name.append(i == 0 ? " " : ", " + interfaces[i].getName());
                }
            }
            return message + " - InterfaceName: " + name.toString() + "methodName: " + method.getName();
        }
    }
}
