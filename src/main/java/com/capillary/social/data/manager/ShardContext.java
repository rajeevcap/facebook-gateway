package com.capillary.social.data.manager;

/**
 * Stores the context information required for resolution of each shard
 * 
 */
public class ShardContext {

    private static final ThreadLocal<ShardContext> orgIdThreadLocal = new ThreadLocal<ShardContext>();

    private int m_orgId;

    private ShardContext(int orgId) {
        super();
        m_orgId = orgId;
    }

    /**
     * @return the orgId
     */
    public int getOrgId() {
        return m_orgId;
    }

    /**
     * @param set
     *            orgId
     */
    private void setOrgId(int orgId) {
        this.m_orgId = orgId;
    }

    public static ShardContext get() {
        return orgIdThreadLocal.get();
    }

    /**
     * set the orgId to be used for shard resolution. To be invoked at every
     * entry point into the system and every place where orgId changes.
     * 
     * @param orgId
     */
    public static void set(int orgId) {
        ShardContext orgIdResolver = orgIdThreadLocal.get();
        if (orgIdResolver == null) {
            orgIdResolver = new ShardContext(orgId);
        } else {
            orgIdResolver.setOrgId(orgId);
        }
        orgIdThreadLocal.set(orgIdResolver);
    }

    public static void reset() {
        orgIdThreadLocal.set(null);
    }
}
