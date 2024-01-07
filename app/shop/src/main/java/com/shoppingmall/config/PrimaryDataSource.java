package com.shoppingmall.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application-dev.yaml"})
@EnableJpaRepositories(
        basePackages = {"com.shoppingmall.domain.repository"},
        entityManagerFactoryRef = "entityManagerFactory",	// entityManagerFactory의 이름
        transactionManagerRef = "transactionManager"		// transactionManager의 이름
)
@MapperScan(
        sqlSessionTemplateRef = "sqlSessionTemplate",
        basePackages = {"com.shoppingmall.mapper"}
)
public class PrimaryDataSource {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari") 	// application.properties에서 사용한 이름
    public javax.sql.DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean
    public EntityManagerFactory entityManagerFactory(javax.sql.DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.shoppingmall.domain.entity");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName()); // 네이밍
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName()); // 네이밍

        em.setJpaPropertyMap(properties);
        em.afterPropertiesSet();

        return em.getObject();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactory(javax.sql.DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        sessionFactoryBean.setTypeAliasesPackage("com.shoppingmall.vo, com.shoppingmall.dto.request");
        sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis-mapper/**/*.xml"));
        sessionFactoryBean.setTypeHandlersPackage("com.shoppingmall.handler");

        // MyBatis 구성 설정
        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        mybatisConfig.setAggressiveLazyLoading(false);
        mybatisConfig.setLazyLoadingEnabled(true);
        mybatisConfig.setLazyLoadTriggerMethods(null);
        sessionFactoryBean.setConfiguration(mybatisConfig);

        return sessionFactoryBean.getObject();
    }

    @Primary
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}