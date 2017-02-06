package com.capillary.social.dao.impl;

import static com.capillary.social.model.Chat.ChatStatus.DELIVERED;
import static com.capillary.social.model.Chat.ChatStatus.READ;
import static com.capillary.social.model.Chat.ChatStatus.SENT;
import static com.capillary.social.utils.FacebookGatewayUtils.getDateFromRs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.capillary.common.sql.SqlUtil;
import com.capillary.social.dao.api.ChatDao;
import com.capillary.social.model.Chat;
import com.capillary.social.model.Chat.ChatStatus;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ChatDaoImpl extends InteractiveShardedBaseDaoImpl<Chat> implements ChatDao {

    private static Logger logger = LoggerFactory.getLogger(ChatDaoImpl.class);

    private static final String TABLE = "chats";
    private static final String ID_COL = "id";
    private static final String ORG_ID_COL = "org_id";
    private static final String THREAD_ID_COL = "thread_id";
    private static final String USER_ID_COL = "user_id";
    private static final String PAGE_ID_COL = "page_id";
    private static final String MESSAGE_ID_COL = "message_id";
    private static final String CONTENT_COL = "content";
    private static final String CHAT_STATUS_COL = "chat_status";
    private static final String SEND_ERROR_COL = "send_error";
    private static final String SENT_TIME_COL = "sent_time";
    private static final String DELIVERED_TIME_COL = "delivered_time";
    private static final String READ_TIME_COL = "read_time";
    private static final String RECEIVED_TIME_COL = "received_time";

    private static final String UPDATE_CHAT_Q = SqlUtil
            .formatQuery(
                    "UPDATE %s SET %s = :%s,  %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                    TABLE, ORG_ID_COL, THREAD_ID_COL, USER_ID_COL, PAGE_ID_COL, MESSAGE_ID_COL, CONTENT_COL,
                    CHAT_STATUS_COL, SEND_ERROR_COL, SENT_TIME_COL, DELIVERED_TIME_COL, READ_TIME_COL,
                    RECEIVED_TIME_COL, ID_COL);

    private static final String GET_ALL_Q = SqlUtil.formatQuery("SELECT * FROM %s", TABLE);

    private static final String FIND_BY_ID_Q = SqlUtil.formatQuery("SELECT * FROM %s WHERE %s = :%s", TABLE, ID_COL);

    private static final String SET_STAT_AS_DEL_Q = SqlUtil.formatQuery(
            "UPDATE %s SET %s = ?, %s = ? WHERE %s = ? AND %s = ? AND %s <= ?", TABLE, CHAT_STATUS_COL,
            DELIVERED_TIME_COL, THREAD_ID_COL, CHAT_STATUS_COL, SENT_TIME_COL);

    private static final String SET_STAT_AS_READ_Q = SqlUtil.formatQuery(
            "UPDATE %s SET %s = ?, %s = ? WHERE %s = ? AND (%s = ? OR %s = ?) AND %s <= ?", TABLE, CHAT_STATUS_COL,
            READ_TIME_COL, THREAD_ID_COL, CHAT_STATUS_COL, CHAT_STATUS_COL, SENT_TIME_COL);

    private static final String FIND_CHAT_Q = SqlUtil.formatQuery(
            "SELECT * FROM %s WHERE %s = ? AND %s = ? AND %s = ? order by received_time desc limit 1", TABLE,
            USER_ID_COL, PAGE_ID_COL, CHAT_STATUS_COL);

    private static ResultSetExtractor<List<Chat>> CHAT_ROW_MAPPER = new ResultSetExtractor<List<Chat>>() {
        @Override
        public List<Chat> extractData(final ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<Chat> list = Lists.newArrayList();
            while (rs.next()) {
                Chat chat = parse(rs);
                list.add(chat);
            }
            return list;
        }
    };

    private static Chat parse(final ResultSet rs) throws SQLException {
        Chat chat = new Chat();
        chat.setId(rs.getInt(ID_COL));
        chat.setOrgId(rs.getLong(ORG_ID_COL));
        chat.setThreadId(rs.getInt(THREAD_ID_COL));
        chat.setUserId(rs.getString(USER_ID_COL));
        chat.setPageId(rs.getString(PAGE_ID_COL));
        chat.setMessageId(rs.getString(MESSAGE_ID_COL));
        chat.setContent(rs.getString(CONTENT_COL));
        chat.setChatStatus(ChatStatus.valueOf(rs.getString(CHAT_STATUS_COL)));
        chat.setSendError(rs.getString(SEND_ERROR_COL));
        chat.setSentTime(getDateFromRs(rs, SENT_TIME_COL));
        chat.setDeliveredTime(getDateFromRs(rs, DELIVERED_TIME_COL));
        chat.setReadTime(getDateFromRs(rs, READ_TIME_COL));
        chat.setReceivedTime(getDateFromRs(rs, RECEIVED_TIME_COL));
        return chat;
    }

    @Override
    public Chat insert(Chat chat) {
        logger.info("inserting chat with recipient : {}, page : {}",
                new Object[] { chat.getUserId(), chat.getPageId() });
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ORG_ID_COL, chat.getOrgId());
        paramMap.put(THREAD_ID_COL, chat.getThreadId());
        paramMap.put(USER_ID_COL, chat.getUserId());
        paramMap.put(PAGE_ID_COL, chat.getPageId());
        paramMap.put(MESSAGE_ID_COL, chat.getMessageId());
        paramMap.put(CONTENT_COL, chat.getContent());
        paramMap.put(CHAT_STATUS_COL, chat.getChatStatus().name());
        paramMap.put(SEND_ERROR_COL, chat.getSendError());
        paramMap.put(SENT_TIME_COL, chat.getSentTime());
        paramMap.put(DELIVERED_TIME_COL, chat.getDeliveredTime());
        paramMap.put(READ_TIME_COL, chat.getReadTime());
        paramMap.put(RECEIVED_TIME_COL, chat.getReceivedTime());
        Number id = insert()
                .withTableName(TABLE)
                .usingColumns(paramMap.keySet().toArray(new String[] {}))
                .usingGeneratedKeyColumns(ID_COL)
                .executeAndReturnKey(paramMap);
        chat.setId(id.intValue());
        return chat;
    }

    @Override
    public void update(Chat chat) {
        logger
                .info("updating chat with recipient : {}, page : {}",
                        new Object[] { chat.getUserId(), chat.getPageId() });
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ID_COL, chat.getId());
        paramMap.put(ORG_ID_COL, chat.getOrgId());
        paramMap.put(THREAD_ID_COL, chat.getThreadId());
        paramMap.put(USER_ID_COL, chat.getUserId());
        paramMap.put(PAGE_ID_COL, chat.getPageId());
        paramMap.put(MESSAGE_ID_COL, chat.getMessageId());
        paramMap.put(CONTENT_COL, chat.getContent());
        paramMap.put(CHAT_STATUS_COL, chat.getChatStatus().name());
        paramMap.put(SEND_ERROR_COL, chat.getSendError());
        paramMap.put(SENT_TIME_COL, chat.getSentTime());
        paramMap.put(DELIVERED_TIME_COL, chat.getDeliveredTime());
        paramMap.put(READ_TIME_COL, chat.getReadTime());
        paramMap.put(RECEIVED_TIME_COL, chat.getReceivedTime());
        jdbc().update(UPDATE_CHAT_Q, paramMap);
    }

    @Override
    public List<Chat> getAll() {
        return jdbc().query(GET_ALL_Q, new MapSqlParameterSource(), CHAT_ROW_MAPPER);
    }

    @Override
    public Chat findById(int id) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ID_COL, id);
        List<Chat> chats = jdbc().query(FIND_BY_ID_Q, paramMap, CHAT_ROW_MAPPER);
        if (chats != null)
            return chats.get(0);
        return null;
    }

    @Override
    public void updateStatusAsDelivered(int threadId, Date watermark) {
        logger.info("inside update status as delivered of chat dao");
        Date now = new Date();
        jdbc().getJdbcOperations().update(SET_STAT_AS_DEL_Q,
                new Object[] { DELIVERED.name(), now, threadId, SENT.name(), watermark });
    }

    @Override
    public void updateStatusAsRead(int threadId, Date watermark) {
        logger.info("inside update status as read of chat dao");
        Date now = new Date();
        jdbc().getJdbcOperations().update(SET_STAT_AS_READ_Q,
                new Object[] { READ.name(), now, threadId, DELIVERED.name(), SENT.name(), watermark });
    }

    @Override
    public Chat findChat(String userId, String pageId, ChatStatus chatStatus) {
        logger.info("inside find chat method");
        List<Chat> chats = jdbc().getJdbcOperations().query(FIND_CHAT_Q,
                new Object[] { userId, pageId, chatStatus.name() }, CHAT_ROW_MAPPER);
        if (chats != null && !chats.isEmpty()) {
            return chats.get(0);
        }
        return null;
    }

}
