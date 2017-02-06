package com.capillary.social.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.capillary.common.spring.jdbc.BaseDaoImpl;
import com.capillary.social.data.manager.DataSourceFactory;

public class InteractiveShardedBaseDaoImpl<T> extends BaseDaoImpl<T> {

    @Autowired
    private DataSourceFactory m_dataSourceFactory;

    @Override
    protected DataSource getDataSource() {
        return m_dataSourceFactory.getShardedDataSourceFactory().getDataSource();
    }

}