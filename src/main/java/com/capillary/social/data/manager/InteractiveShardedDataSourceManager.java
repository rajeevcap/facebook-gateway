package com.capillary.social.data.manager;

import static com.capillary.common.io.PropertiesUtil.loadPropertiesFromClasspath;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.capillary.common.jdbc.CloseableDataSource;
import com.capillary.common.jdbc.sharding.CurrentOrgIdResolver;
import com.capillary.common.jdbc.sharding.DatabaseNameFormatter;
import com.capillary.common.jdbc.sharding.ServiceDiscoveryShardedDataSourceManager;
import com.capillary.servicediscovery.model.KnownService;
import com.capillary.social.utils.FacebookGatewayUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

@Component
public class InteractiveShardedDataSourceManager extends ServiceDiscoveryShardedDataSourceManager {
    private static Logger logger = LoggerFactory.getLogger(InteractiveShardedDataSourceManager.class);
    private static final int DEFAULT_DATA_SOURCE_ID = -1;
    private final String DB_CONFIG_FILE = "database.config.properties";

    private String m_datasourceName;
    private DataSourceFactory dataSource;
    private Map<String, String> datasourceProperties = new HashMap<String, String>();

    private ShardContextOrgIdResolver m_shardContextOrgIdResolver;
    private SingleNameDatabaseNameFormatter m_databaseNameFormatter;

    /* @Autowired
     private RegistrarService registrarService;
    */
    public InteractiveShardedDataSourceManager() {
        this(KnownService.INTERACTIVE_MSGING_DB_MYSQL_MASTER);
    }

    public InteractiveShardedDataSourceManager(KnownService service) {
        super(service);
        m_shardContextOrgIdResolver = new ShardContextOrgIdResolver();
        m_databaseNameFormatter = new SingleNameDatabaseNameFormatter();
    }

    @PostConstruct
    private void init() {
        loadDbProperties();
        super.init(10);
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
        return "masters.";
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

    public String getDatasourceName() {
        return m_datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.m_datasourceName = datasourceName;
    }

    public Map<String, String> getDatasourceProperties() {
        return datasourceProperties;
    }

    public void setDatasourceProperties(Map<String, String> datasourceProperties) {
        this.datasourceProperties = datasourceProperties;
    }

    @Override
    protected List<Integer> getOrgIds() {
        List<Integer> allOrgIdsList = new ArrayList<Integer>();//.getAllOrgIdsList(true);
        logger.debug("initializing shards for orgs: {}", allOrgIdsList);
        return allOrgIdsList;
    }

    @Override
    public DatabaseNameFormatter getOrgDatabaseNameFormatter() {
        return m_databaseNameFormatter;
    }

    @Override
    public CurrentOrgIdResolver getCurrentOrgIdResolver() {
        return m_shardContextOrgIdResolver;
    }

    @Override
    protected CloseableDataSource createShardDataSource(String jdbcUrl, String user, String password) {
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
        logger.info("C3P0 pool created for intouch shard");
        return new CloseableDelegatingDataSource(rootDataSource);
    }

    /**
     * Fetch orgId from shardContext. Fallback added to MDC orgId resolver, to
     * be removed once stable.
     * 
     * @author 
     */
    private class ShardContextOrgIdResolver implements CurrentOrgIdResolver {

        private MdcOrgIdResolver m_mdcOrgIdResolver;

        public ShardContextOrgIdResolver() {
            super();
            m_mdcOrgIdResolver = new MdcOrgIdResolver();
        }

        @Override
        public int getCurrentOrgId() {
            ShardContext shardContext = ShardContext.get();
            if (shardContext != null) {
                int orgId = shardContext.getOrgId();
                if (orgId < 0) {
                    return m_mdcOrgIdResolver.getDefaultActiveOrgId();
                }
                return orgId;
            }
            // FALLBACK to MDC resolver
            logger.warn("using MDC orgID resolution as shard context not found");
            return m_mdcOrgIdResolver.getCurrentOrgId();
        }
    }

    /**
     * Resolve the org id based on the MDC
     * 
     * @author 
     */
    private class MdcOrgIdResolver implements CurrentOrgIdResolver {
        private int defaultActiveOrgId = 0;//-1;

        @Override
        public int getCurrentOrgId() {
            try {
                int orgId = Integer.parseInt((String) MDC.get(FacebookGatewayUtils.REQUEST_ORG_ID_MDC).substring(7));
                if (orgId < 0) {
                    return getDefaultActiveOrgId();
                }
                return orgId;
            } catch (ClassCastException e) {
                logger.error("could not get orgId from MDC", e);
                return getDefaultActiveOrgId();
            } catch (Exception ex) {
                logger.warn("MDC has not been initialized yet", ex);
                return getDefaultActiveOrgId();
            }
        }

        public int getDefaultActiveOrgId() {
            if (defaultActiveOrgId == DEFAULT_DATA_SOURCE_ID) {
                defaultActiveOrgId = 1;//registrarService.getAnActiveOrgId();
                logger.debug("setting default active org id: {}", defaultActiveOrgId);
            }
            return defaultActiveOrgId;
        }
    }

    /**
     * returns the same database regardless of orgId
     * 
     * 
     */
    private class SingleNameDatabaseNameFormatter implements DatabaseNameFormatter {
        @Override
        public String getDatabaseName(int orgId) {
            return m_datasourceName;
        }
    }

    /**
     * basic delegating data source
     * 
     * @author 
     */
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

    /**
     * fetch the data source based on the current orgId
     * 
     * @return
     */
    public CloseableDataSource getDataSource() {
        return getOrgDataSource(m_shardContextOrgIdResolver.getCurrentOrgId());
    }

    /**
     * fetch the data source based on the current orgId
     * 
     * @return
     */
    public TransactionInterceptor getOrgTransactionInterceptor() {
        logger.debug("fetching transaction interceptor for org: {}", m_shardContextOrgIdResolver.getCurrentOrgId());
        return getOrgTransactionInterceptor(m_shardContextOrgIdResolver.getCurrentOrgId());
    }

}