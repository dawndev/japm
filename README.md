## [About](README.md)

<h1 align="center">JAPM</h1>

[![Github](https://img.shields.io/badge/GitHub-white.svg?style=flat-square&logo=github&logoColor=181717)](https://github.com/tagwan/jspm)
![GitHub](https://img.shields.io/github/license/tagwan/japm)
![GitHub stars](https://img.shields.io/github/stars/tagwan/japm.svg)
![GitHub forks](https://img.shields.io/github/forks/tagwan/japm.svg)
![GitHub issues](https://img.shields.io/github/issues-raw/tagwan/japm?label=issues)
![GitHub last commit](https://img.shields.io/github/last-commit/tagwan/japm.svg)


<div align="center">

一个针对高并发、低延迟应用设计的高性能JVM程序（java、kotlin, eg..）的性能监控和统计工具
  
  (当前只统计方法执行耗时)。


</div>


## Compile

如果你想要编译本项目, 你可能需要提前准备以下环境

| Target      | Version |
| ----------- | ----------- |
| JDK      | 11       |
| Kotlin   | 1.5.31        |
| Gradle   | 6.8.3        |

编译
```bash
gradlew clean buid
```

## Usage

在 JVM 启动参数里加上以下两个参数
* `-javaagent` : 指定代理jar
* `-DJapmPropFile` : 指定配置文件(不指定的话，则启用[默认配置](./src/main/resources/japm-template.properties))

eg..
```bash
java -javaagent:E:\\tmp\\japm-0.0.1.jar -DJapmPropFile=E:\\tmp\\japm.properties `-jar application.jar`
```

## Issue
如果您有任何问题、疑问或者建议，您可以 [提交Issue](https://github.com/tagwan/japm/issues/new/choose)  ;-)

## TODO
- 监控方法命中次数
- 上报http服务器
- 上报es
- 未完待续
