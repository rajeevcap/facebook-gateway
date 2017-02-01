package com.capillary.social.data.manager;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.capillary.common.jdbc.CloseableDataSource;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Autowired
    private IntouchMetaDataSourceManager intouchMetaDataSourceManager;

    @Bean
    public CloseableDataSource intouchMetaDataSource() throws SQLException {
        DataSource ds = intouchMetaDataSourceManager.getDataSource();
        ds.getConnection().setAutoCommit(false);
        return (CloseableDataSource) ds;
    }

    @Bean
    @Qualifier("intouchMetaTransactionManager")
    public PlatformTransactionManager writeIntouchMetaTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(intouchMetaDataSource());
    }
}
