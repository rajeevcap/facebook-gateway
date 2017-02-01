package com.capillary.social.data.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capillary.common.jdbc.CloseableDataSource;

@Component
public class DataSourceFactory {

    @Autowired
    private InteractiveShardedDataSourceManager interactiveShardedDataSourceManager;

    @Autowired
    private IntouchMetaDataSourceManager intouchMetaDataSourceManager;

    public InteractiveShardedDataSourceManager getShardedDataSourceFactory() {
        return interactiveShardedDataSourceManager;
    }

    public CloseableDataSource createDataSource() {
        return (CloseableDataSource) getShardedDataSourceFactory().getDataSource();
    }

    public IntouchMetaDataSourceManager getIntouchMetaDataSourceManager() {
        return intouchMetaDataSourceManager;
    }
}
