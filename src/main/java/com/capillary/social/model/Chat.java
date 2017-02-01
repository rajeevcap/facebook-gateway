package com.capillary.social.model;

import java.util.Date;

public class Chat {

    private int id;
    private long orgId;
    private int threadId;
    private String userId;
    private String pageId;
    private String messageId;
    private String content;
    private ChatStatus chatStatus;
    private String sendError;
    private Date sentTime;
    private Date deliveredTime;
    private Date readTime;
    private Date receivedTime;

    public static enum ChatStatus {
        SENT, DELIVERED, READ, RECEIVED, FAILED
    }

    public Chat() {
    }

    public Chat(long orgId, int threadId, String userId, String pageId, String messageId, String content,
            ChatStatus chatStatus, String sendError, Date sentTime, Date deliveredTime, Date readTime, Date receivedTime) {
        super();
        this.orgId = orgId;
        this.threadId = threadId;
        this.userId = userId;
        this.pageId = pageId;
        this.messageId = messageId;
        this.content = content;
        this.chatStatus = chatStatus;
        this.sendError = sendError;
        this.sentTime = sentTime;
        this.deliveredTime = deliveredTime;
        this.readTime = readTime;
        this.receivedTime = receivedTime;
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

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getSendError() {
        return sendError;
    }

    public void setSendError(String sendError) {
        this.sendError = sendError;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Date getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(Date deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    @Override
    public String toString() {
        return "Chat [id="
               + id
               + ", orgId="
               + orgId
               + ", threadId="
               + threadId
               + ", userId="
               + userId
               + ", pageId="
               + pageId
               + ", messageId="
               + messageId
               + ", content="
               + content
               + ", chatStatus="
               + chatStatus
               + ", sendError="
               + sendError
               + ", sentTime="
               + sentTime
               + ", deliveredTime="
               + deliveredTime
               + ", readTime="
               + readTime
               + ", receivedTime="
               + receivedTime
               + "]";
    }

}
