package com.capillary.social.model;

import java.util.Date;

public class ChatThread {

    private int id;
    private long orgId;
    private String pageId;
    private String userId;
    private Date createdtime;
    private Date lastUpdatedTime;

    public ChatThread() {
    }

    public ChatThread(long orgId, String pageId, String userId, Date createdtime, Date lastUpdatedTime) {
        super();
        this.orgId = orgId;
        this.pageId = pageId;
        this.userId = userId;
        this.createdtime = createdtime;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public String toString() {
        return "ChatThread [id="
               + id
               + ", orgId="
               + orgId
               + ", pageId="
               + pageId
               + ", userId="
               + userId
               + ", createdtime="
               + createdtime
               + ", lastUpdatedTime="
               + lastUpdatedTime
               + "]";
    }

}
