package com.global.language.web_content_translate.config;

import com.global.language.web_content_translate.repository.LanguageMapper;
import com.global.language.web_content_translate.repository.TranslationMapper;
import com.global.language.web_content_translate.repository.WebContentMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis 配置类：
 * - 明确声明 SqlSessionFactory / SqlSessionTemplate
 * - 手动注册每个 Mapper Bean（避免 @MapperScan 在 Native Image 中的兼容性问题）
 */
@Configuration(proxyBeanMethods = false)
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        
        // 配置 MyBatis 设置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
        // 禁用懒加载（Native Image 不支持运行时字节码生成）
        configuration.setLazyLoadingEnabled(false);
        configuration.setAggressiveLazyLoading(false);
        // 显式指定默认脚本语言，避免注册时触发其他驱动的反射
        configuration.setDefaultScriptingLanguage(org.apache.ibatis.scripting.xmltags.XMLLanguageDriver.class);
        factoryBean.setConfiguration(configuration);
        
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // 手动注册 Mapper Bean，确保 Native Image 兼容
    @Bean
    public MapperFactoryBean<LanguageMapper> languageMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<LanguageMapper> factoryBean = new MapperFactoryBean<>(LanguageMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean
    public MapperFactoryBean<TranslationMapper> translationMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<TranslationMapper> factoryBean = new MapperFactoryBean<>(TranslationMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }

    @Bean
    public MapperFactoryBean<WebContentMapper> webContentMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<WebContentMapper> factoryBean = new MapperFactoryBean<>(WebContentMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory);
        return factoryBean;
    }
}
