package com.longdai.task.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * <p>
 * Contributors:
 * DonQuixote  on 2/26/16 - 9:19 AM
 *******************************************************************************/
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.longdai.task.mybatis.mapper")
@ComponentScan(basePackages = {"com.longdai.task.service"})
public class DataBase {
    private final Logger log = LoggerFactory.getLogger(DataBase.class);

    @Value("${datasource.url}")
    private String connectUrl;

    @Value("${datasource.driverClassName}")
    private String driverClassName;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;



    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(connectUrl);
        datasource.setDriverClassName(driverClassName);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setInitialSize(3); //物理连接数
        datasource.setMinIdle(1); //最小连接数
        datasource.setMaxWait(60000); //60秒超时
        datasource.setRemoveAbandoned(true);
        datasource.setConnectionProperties("config.decrypt=true;");//加密连接
        datasource.setFilters("config,stat,log4j,wall");
        return datasource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:com/longdai/task/mybatis/mapper/xml/*.xml");
        sessionFactory.setMapperLocations(resources);
        return sessionFactory.getObject();
    }
}
