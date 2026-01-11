# Global Language Translate Service (web_content_translate)

[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java25)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GraalVM](https://img.shields.io/badge/GraalVM-Native%20Image-blue.svg)](https://www.graalvm.org/)

这是一个基于最新技术栈构建的高性能全球语言翻译与网页内容管理服务。本项目不仅实现了多语言内容的高效管理，更在架构上适配了下一代云原生技术，支持 GraalVM 原生镜像编译。

## 🚀 核心亮点

- **Spring Boot 4.0 + Java 25**: 采用前沿版本，充分利用 Java 25 的语法特性与性能优化。
- **GraalVM Native Image**: 深度适配原生镜像，支持将应用编译为本地二进制文件，实现 **毫秒级启动** 和 **极低内存占用**。
- **Virtual Threads (虚拟线程)**: 默认开启虚拟线程支持，显著提升高并发场景下的吞吐量与资源利用率。
- **MyBatis Native 适配**: 通过 `NativeRuntimeHints` 完美解决 MyBatis 在原生镜像下的反射与代理问题。
- **云原生配置**: 支持通过环境变量灵活配置数据库连接。

## 🛠 技术栈

- **核心框架**: Spring Boot 4.0.0
- **数据库**: MySQL 8.3.0 + MyBatis 3.0.4
- **文档工具**: SpringDoc OpenAPI 2.6.0 (Swagger UI)
- **文件处理**: Apache POI 5.2.5 (支持内容批量处理)
- **工具库**: Lombok, Jackson, Javassist

## 📂 项目结构

```text
com.global.language.web_content_translate
├── config          # 配置类（MyBatis, SpringDoc, NativeHints）
├── controller      # 接口层（语言管理、内容翻译、网页内容）
├── model           # 模型层（Entity, BO, Param, Result）
├── repository      # 持久层（MyBatis Mapper 接口）
├── service         # 业务层（接口与实现类）
└── WebContentTranslateApplication.java  # 启动类
```

## 📖 核心功能

1.  **语言管理 (`/language`)**:
    - 支持多种语言的 CRUD 维护（ISO 代码、本地化名称等）。
2.  **内容管理 (`/webContent`)**:
    - 网页内容抓取后的存储与管理。
    - **批量导入**: 支持通过 JSON/文件快速导入网页内容。
3.  **翻译系统 (`/translation`)**:
    - 维护内容在不同语言版本间的对应关系。
4.  **接口文档**:
    - 集成 Swagger UI，启动后访问 `/swagger-ui.html` 即可查看完整 API 定义。

## 🛠 快速上手

### 环境要求
- JDK 25+
- GraalVM (若需构建原生镜像)
- MySQL 8.0+

### 1. 数据库准备
执行项目根目录下的 `init.sql` 初始化数据库表结构。

### 2. 配置文件
在 `application.yaml` 中配置数据库连接，或通过环境变量注入：
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/content_global_translate
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=your_password
```

### 3. 编译与运行

**标准模式 (JAR):**
```bash
./mvnw clean package
java -jar target/web_content_translate-0.0.1-SNAPSHOT.jar
```

**原生镜像模式 (Native Image):**
```bash
./mvnw clean package -Pnative
# 运行生成的二进制文件
./target/web_content_translate
```

## ⚙ 运行时优化

项目已针对 GraalVM 进行了深度优化，在 `NativeRuntimeHints` 中预注册了以下元数据：
- MyBatis Mapper 接口的动态代理。
- 实体类的反射调用。
- MySQL 驱动与日志实现的 SPI 加载。

## 📄 开源协议
[MIT License](LICENSE)
