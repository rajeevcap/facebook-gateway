package com.capillary.social.dao.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capillary.social.model.ChatThread;

@Service
public interface ChatThreadDao {

    public ChatThread insert(ChatThread chatThread);

    public void update(ChatThread chatThread);
    
    public void updateLastUpdatedTime(int id);

    public ChatThread findById(int id);

    public List<ChatThread> getAll();

    public ChatThread findChatThread(String userId, String pageId);

}