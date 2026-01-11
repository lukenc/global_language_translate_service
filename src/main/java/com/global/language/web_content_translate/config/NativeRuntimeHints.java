package com.global.language.web_content_translate.config;

import com.global.language.web_content_translate.model.enity.IdCount;
import com.global.language.web_content_translate.model.enity.Language;
import com.global.language.web_content_translate.model.enity.Translation;
import com.global.language.web_content_translate.model.enity.WebContent;
import com.global.language.web_content_translate.repository.LanguageMapper;
import com.global.language.web_content_translate.repository.TranslationMapper;
import com.global.language.web_content_translate.repository.WebContentMapper;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Native Image 运行时提示：
 *  - 注册 MyBatis 映射 XML 资源
 *  - 为实体与 Mapper 代理开启反射，以便 AOT 生成完整的元数据
 *  - 配置 MyBatis 日志实现以避免 Native Image 中的日志初始化问题
 */
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(NativeRuntimeHints.MyBatisHints.class)
public class NativeRuntimeHints {

    // 在类加载时立即设置 MyBatis 日志实现，避免 LogFactory 自动检测
    static {
        LogFactory.useSlf4jLogging();
    }

    static class MyBatisHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("mapper/*.xml");
            // MyBatis 内置 DTD，避免外部 HTTP 访问被阻止
            hints.resources().registerPattern("org/apache/ibatis/builder/xml/mybatis-3-mapper.dtd");
            hints.resources().registerPattern("org/apache/ibatis/builder/xml/mybatis-3-config.dtd");
            // MySQL JDBC 驱动 SPI 与资源
            hints.resources().registerPattern("META-INF/services/java.sql.Driver");

            // 实体类反射（使用 getter/setter，无需字段直接访问）
            List<Class<?>> entities = List.of(Language.class, Translation.class, WebContent.class, IdCount.class);
            entities.forEach(entity ->
                    hints.reflection().registerType(entity,
                            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                            MemberCategory.INVOKE_PUBLIC_METHODS)
            );

            // MyBatis 日志相关类
            hints.reflection().registerType(Slf4jImpl.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);
            hints.reflection().registerType(LogFactory.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS);

            // MapperFactoryBean 反射（Native Image 需要）
            hints.reflection().registerType(MapperFactoryBean.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // Javassist 代理工厂（MyBatis Configuration 初始化需要）
            hints.reflection().registerType(JavassistProxyFactory.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // Javassist 代理类（默认懒加载代理依赖）
            hints.reflection().registerType(TypeReference.of("org.apache.ibatis.javassist.util.proxy.ProxyFactory"),
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);
            hints.reflection().registerType(TypeReference.of("org.apache.ibatis.javassist.util.proxy.MethodHandler"),
                    MemberCategory.INVOKE_PUBLIC_METHODS);
            hints.reflection().registerType(TypeReference.of("org.apache.ibatis.javassist.util.proxy.ProxyObject"),
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // MyBatis XML 语言驱动（解析 mapper XML 需要）
            hints.reflection().registerType(XMLLanguageDriver.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);
            // MyBatis Raw 语言驱动（解析内联 SQL 需要，避免 NoSuchMethodException）
            hints.reflection().registerType(RawLanguageDriver.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // MySQL 驱动反射（确保驱动被包含）
            hints.reflection().registerType(TypeReference.of("com.mysql.cj.jdbc.Driver"),
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // 常用集合类型（部分 resultType 直接使用 ArrayList）
            hints.reflection().registerType(ArrayList.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);
            // 常见 Map 结果类型（resultType=map 等场景）
            hints.reflection().registerType(HashMap.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);
            hints.reflection().registerType(LinkedHashMap.class,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_METHODS);

            // Mapper 接口反射与代理
            List<Class<?>> mappers = List.of(LanguageMapper.class, TranslationMapper.class, WebContentMapper.class);
            mappers.forEach(mapper -> {
                hints.reflection().registerType(mapper,
                        MemberCategory.INVOKE_PUBLIC_METHODS);
                hints.proxies().registerJdkProxy(mapper);
            });
        }
    }
}
