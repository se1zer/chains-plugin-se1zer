<div align="center">

# chains-plugin-se1zer

<img src="https://img.shields.io/badge/Java-8+-orange?style=flat-square&logo=java" alt="Java">
<img src="https://img.shields.io/badge/Maven-1.0--SNAPSHOT-blue?style=flat-square&logo=apache-maven" alt="Maven">
<img src="https://img.shields.io/badge/License-MIT-green?style=flat-square" alt="License">

**Java-Chains 自定义 Gadget 插件集**

</div>

---

## 📖 简介

本项目是 [Java-Chains](https://github.com/vulhub/java-chains) 的自定义 Gadget 插件集合，基于 [chains-plugin-demo](https://github.com/Java-Chains/chains-plugin-demo) 模板开发。

Java-Chains 是一款强大的 Java 反序列化利用链生成工具，通过插件机制可以灵活扩展自定义的 Gadget 链。

## ✨ 特性

- 🚀 **开箱即用** - 基于 Maven 构建，快速集成到 Java-Chains
- 🔧 **高度可扩展** - 支持自定义 Gadget 链和参数配置
- 📦 **模块化设计** - 每个 Gadget 独立封装，便于维护
- 🎯 **优先级控制** - 支持自定义 Gadget 在界面中的展示顺序

## 📦 包含的 Gadget

### 反序列化链

| Gadget | 依赖 | JDK 版本 | 描述 |
|--------|------|---------|------|
| `SpringAopToString` | org.springframework:spring-aop | | JdkDynamicAopProxy#invoke 方法调用任意对象的 toString() 方法 |

### 字节码 Gadget

| Gadget    | 描述 |
|-----------|------|
| `字节码切片加载` | 适用于字节码长度限制, 通过环境变量获取内存马切片并加载 |
| `执行Js命令`  | 使用ScriptEngine执行JS代码 |

## 🚀 快速开始

### 环境要求

- JDK 8+
- Maven 3.x
- SDK - [Java-Chains](https://github.com/vulhub/java-chains) 

### 编译打包

```bash
# 克隆项目
git clone https://github.com/your-username/chains-plugin-se1zer.git

# 编译打包
mvn clean package

# 生成的 JAR 文件位于 target/ 目录
```

### 安装插件

1. 将编译好的 `chains-plugin-se1zer.jar` 复制到 Java-Chains 的 `plugins/` 目录
2. 重启 Java-Chains 或刷新插件列表
3. 在 Gadget 选择器中找到自定义的 Gadget

## 🛠️ 开发指南

### 下载SDK

下载
[Java-Chains-Cli](https://github.com/vulhub/java-chains/releases)
或者
[Java-Chains-SDK](https://github.com/Java-Chains/chains-plugin-demo/releases)
放入`sdk`目录即可

### 创建自定义 Gadget

```java
@GadgetTags(
    tags = {JavaNativeDeserialize, END}
)
@GadgetAnnotation(
    name = "YourGadgetName",
    description = "Gadget 描述",
    dependencies = {"groupId:artifactId:version"},
    authors = {"your-name"},
    priority = 10
)
public class YourGadget implements Gadget {

    @Param(name = "cmd", description = "执行的命令")
    public String cmd = "calc";

    @Override
    public Object invoke(GadgetContext context, GadgetChain chain) throws Exception {
        // 实现你的 Gadget 逻辑
        return yourPayload;
    }
}
```

### 注解说明

| 注解 | 说明 |
|------|------|
| `@GadgetTags` | 定义 Gadget 的标签类型（如序列化类型、是否为终点） |
| `@GadgetAnnotation` | 定义 Gadget 的元信息（名称、描述、依赖、作者、优先级） |
| `@Param` | 定义可配置的参数，支持在 UI 界面中动态修改 |

## 📝 更新日志

### v1.0.0 (2026-03-04)
- 初始版本
- 添加字节码分离加载、执行Js命令 Gadget

### v1.0.1 (2026-03-05)
- 删除原工具已有的cc链
- 添加SpringAopToString，通过 JdkDynamicAopProxy#invoke 方法调用任意对象的 toString() 方法

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证。

## 🔗 相关链接

- [Java-Chains](https://github.com/vulhub/java-chains) - Java 反序列化利用链生成工具
- [chains-plugin-demo](https://github.com/Java-Chains/chains-plugin-demo) - 官方插件开发模板

---

<div align="center">

**⚠️ 免责声明：本项目仅供安全研究和授权测试使用，请勿用于非法用途。**

Made with ❤️ by [se1zer](https://github.com/se1zer)

</div>
