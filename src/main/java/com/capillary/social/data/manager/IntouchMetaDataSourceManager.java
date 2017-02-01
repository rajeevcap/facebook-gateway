package com.capillary.social.data.manager;

import static com.capillary.common.io.PropertiesUtil.loadPropertiesFromClasspath;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.stereotype.Component;

import com.capillary.common.jdbc.CloseableDataSource;
import com.capillary.servicediscovery.ServiceDiscovery;
import com.capillary.servicediscovery.model.KnownService;
import com.capillary.servicediscovery.services.MySQLDBService;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

@Component
public class IntouchMetaDataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(IntouchMetaDataSourceManager.class);

    private final String DB_CONFIG_FILE = "database.config.properties";

    private String m_datasourceName;
    private DataSource dataSource;
    private Map<String, String> datasourceProperties = new HashMap<String, String>();

    @PostConstruct
    private void init() {
        loadDbProperties();
        dataSource = createShardDataSource();
    }

    protected void loadDbProperties() {
        logger.info("Loading db properties");
        populateDatasourceProperties();
        m_datasourceName = datasourceProperties.get("datasource.database") == null ? "masters" : datasourceProperties
                .get("datasource.database");
        logger.info("Loaded db properties");
    }

    /**
     * 
     * @return prefix for db config to check from database.config.properties
     *         file , override for different dbconfig prefix
     */
    protected String getDbConfigPrefix() {
        return "intouchmeta.";
    }

    /**
     * prepares map for properties of datasource. <br/>
     * Takes properties of multiple sources based on prefix for properties in
     * config file
     */
    private void populateDatasourceProperties() {
        logger.info("loading db properties");
        String prefix = getDbConfigPrefix();
        try {
            Properties localProperties = loadPropertiesFromClasspath(DB_CONFIG_FILE);
            Set<Object> keys = localProperties.keySet();

            for (Object key : keys) {
                if (((String) key).startsWith(prefix)) {
                    String keyWithoutPrefix = ((String) key).replace(prefix, "");
                    datasourceProperties.put((String) keyWithoutPrefix, (String) localProperties.get(key));
                }
            }
        } catch (IOException e) {
            logger.warn("error while loading properties from file: {} prefix: {}", DB_CONFIG_FILE, prefix);
            throw new RuntimeException("error while loading properties from file:" + DB_CONFIG_FILE, e);
        }
        logger.info("loaded db properties");
    }

    protected CloseableDataSource createShardDataSource() {

        MySQLDBService intouchMetaDbManager = null;

        try {
            intouchMetaDbManager = (MySQLDBService) ServiceDiscovery.getInstance().get(
                    KnownService.INTOUCH_META_DB_MYSQL_MASTER);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("exception : {}", e.getMessage());
        }

        String user = intouchMetaDbManager.getUserName();
        String password = intouchMetaDbManager.getPassword();
        String jdbcUrl = intouchMetaDbManager.getURI();
        HashMap<String, String> propertiesMap = Maps.newHashMap(datasourceProperties);
        ComboPooledDataSource rootDataSource = new ComboPooledDataSource();
        try {
            rootDataSource.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            throw Throwables.propagate(e);
        }

        // TODO - generic way to set properties directly from file
        rootDataSource.setDataSourceName(m_datasourceName);
        rootDataSource.setJdbcUrl(jdbcUrl
                                  + "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8");
        rootDataSource.setUser(user);
        rootDataSource.setPassword(password);
        rootDataSource.setMinPoolSize(Integer.parseInt(propertiesMap.get("datasource.minPoolSize")));
        rootDataSource.setMaxPoolSize(Integer.parseInt(propertiesMap.get("datasource.maxPoolSize")));
        rootDataSource.setAcquireIncrement(Integer.parseInt(propertiesMap.get("datasource.acquireIncrement")));
        rootDataSource.setIdleConnectionTestPeriod(Integer.parseInt(propertiesMap
                .get("datasource.idleConnectionTestPeriod")));
        rootDataSource.setNumHelperThreads(Integer.parseInt(propertiesMap.get("datasource.numHelperThreads")));
        rootDataSource.setMaxStatements(Integer.parseInt(propertiesMap.get("datasource.maxStatements")));
        rootDataSource.setMaxAdministrativeTaskTime(Integer.parseInt(propertiesMap
                .get("datasource.maxAdministrativeTaskTime")));
        rootDataSource.setMaxConnectionAge(Integer.parseInt(propertiesMap.get("datasource.maxConnectionAge")));
        rootDataSource.setUnreturnedConnectionTimeout(Integer.parseInt(propertiesMap
                .get("datasource.unreturnedConnectionTimeout")));
        rootDataSource.setPreferredTestQuery(propertiesMap.get("datasource.preferredTestQuery"));

        logger.info("C3P0 pool created!");
        return new CloseableDelegatingDataSource(rootDataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static class CloseableDelegatingDataSource extends DelegatingDataSource implements CloseableDataSource {

        public CloseableDelegatingDataSource(final PooledDataSource rootDataSource) {
            super(rootDataSource);
            logger.debug("creating datasource: {}", rootDataSource.getDataSourceName());
        }

        @Override
        public void close() throws IOException {
            try {
                ((PooledDataSource) getTargetDataSource()).close();
                logger.debug("Closed datasource: {}", ((PooledDataSource) getTargetDataSource()).getDataSourceName());
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }
}