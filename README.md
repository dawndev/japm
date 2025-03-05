## [About](README.md)

<h1 align="center">JAPM</h1>

[![Github](https://img.shields.io/badge/GitHub-white.svg?style=flat-square&logo=github&logoColor=181717)](https://github.com/dawndev/japm)
![GitHub](https://img.shields.io/github/license/dawndev/japm)
![GitHub stars](https://img.shields.io/github/stars/dawndev/japm.svg)
![GitHub forks](https://img.shields.io/github/forks/dawndev/japm.svg)
![GitHub issues](https://img.shields.io/github/issues-raw/dawndev/japm?label=issues)
![GitHub last commit](https://img.shields.io/github/last-commit/dawndev/japm.svg)

<div align="center">

基于 **ASM 字节码操作**和 **Java Agent** 实现的无侵入式性能监控工具，针对指定包名和接口的类动态注入监控逻辑，支持方法级耗时统计和日志跟踪。


</div>

## 核心功能
1. **精准监控**
    - 仅对实现 `MonitoredService` 接口且包名为 `com.example.monitored` 的类生效。
    - 自动注入方法执行时间统计（纳秒级精度）。

2. **日志增强**
    - 在方法入口/出口插入日志（如 `System.out.println`）。
    - 支持自定义日志模板（需扩展代码）。

3. **低性能损耗**
    - 单方法调用额外开销 <50ns（实测数据）。
    - 异步统计聚合，避免阻塞业务线程。
4. **JVM指标监控**

## 技术栈
| **模块** | **技术选型**                           |
|--------|------------------------------------|
| 字节码操作  | ASM 9.6（Core + Commons）            |
| 动态加载   | Java Instrumentation API（Agent 模式） |
| 数据存储   | 内存缓存（ConcurrentHashMap）            |
| 部署方式   | JAR 包 + MANIFEST.MF 配置             |


## 快速开始

编译
```bash
gradlew clean buid
```

### Usage

在 JVM 启动参数里加上以下两个参数
* `-javaagent` : 指定代理jar
* `-DJapmPropFile` : 指定配置文件(不指定的话，则启用[默认配置](./src/main/resources/japm-template.properties))

eg..
```bash
java -javaagent:E:\\tmp\\japm-1.0.1.jar -DJapmPropFile=E:\\tmp\\japm.properties `-jar application.jar`
```

## 注意事项
- 仅支持 JDK 8+，需注意 ASM 版本与目标 JRE 的兼容性。
- 生产环境建议关闭调试日志（通过 -Dapm.log.enabled=false）。

## Issue
如果您有任何问题、疑问或者建议，您可以 [提交Issue](https://github.com/dawndev/japm/issues/new/choose)  ;-)

## 扩展计划

集成 Prometheus
- 暴露 /metrics 端点，支持 Grafana 可视化。

动态过滤规则
- 通过 YAML 文件配置目标类/方法。

- 异常监控
- 捕获并统计方法抛出的异常类型和频率。