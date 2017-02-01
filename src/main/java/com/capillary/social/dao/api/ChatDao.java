package com.capillary.social.dao.api;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capillary.social.model.Chat;
import com.capillary.social.model.Chat.ChatStatus;

@Service
public interface ChatDao {

    public Chat insert(Chat chat);

    public void update(Chat chat);

    public Chat findById(int id);

    public List<Chat> getAll();

    public void updateStatusAsDelivered(int threadId, Date watermark);

    // all msgs before sent time watermark were delivered  

    public void updateStatusAsRead(int threadId, Date watermark);

    public Chat findChat(String userId, String pageId, ChatStatus chatStatus);

}
