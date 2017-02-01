package com.capillary.social.dao.impl;

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
import org.springframework.stereotype.Service;

import com.capillary.social.model.ChatThread;
import com.capillary.social.dao.api.ChatThreadDao;
import com.capillary.common.sql.SqlUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ChatThreadDaoImpl extends InteractiveShardedBaseDaoImpl<ChatThread> implements ChatThreadDao {

    private static Logger logger = LoggerFactory.getLogger(ChatThreadDaoImpl.class);

    private static final String TABLE = "chat_thread";
    private static final String ID_COL = "id";
    private static final String ORG_ID_COL = "org_id";
    private static final String PAGE_ID_COL = "page_id";
    private static final String USER_ID_COL = "user_id";
    private static final String CREATED_TIME_COL = "created_time";
    private static final String LAST_UPDATED_TIME_COL = "last_updated_time";

    private static final String UPDATE_CHAT_THREAD_Q = SqlUtil.formatQuery(
            "UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s", TABLE, ORG_ID_COL,
            PAGE_ID_COL, USER_ID_COL, CREATED_TIME_COL, LAST_UPDATED_TIME_COL, ID_COL);

    private static final String FIND_CHAT_THREAD_Q = SqlUtil.formatQuery(
            "SELECT * FROM %s WHERE %s = :%s AND %s = :%s", TABLE, USER_ID_COL, PAGE_ID_COL);

    private static final String FIND_CHAT_THREAD_BY_ID_Q = SqlUtil.formatQuery("SELECT * FROM %s WHERE %s = :%s",
            TABLE, ID_COL);

    private static final String GET_ALL_Q = SqlUtil.formatQuery("SELECT * FROM %s", TABLE);

    private static final String LAST_UPD_TIME_Q = SqlUtil.formatQuery("UPDATE %s SET %s = :%s WHERE %s = :%s", TABLE,
            LAST_UPDATED_TIME_COL, ID_COL);

    private static ResultSetExtractor<List<ChatThread>> CHAT_THREAD_ROW_MAPPER = new ResultSetExtractor<List<ChatThread>>() {
        @Override
        public List<ChatThread> extractData(final ResultSet rs) throws SQLException, DataAccessException {
            ArrayList<ChatThread> list = Lists.newArrayList();
            while (rs.next()) {
                ChatThread chatThread = parse(rs);
                list.add(chatThread);
            }
            return list;
        }
    };

    private static ChatThread parse(final ResultSet rs) throws SQLException {
        ChatThread chatThread = new ChatThread();
        chatThread.setId(rs.getInt(ID_COL));
        chatThread.setOrgId(rs.getLong(ORG_ID_COL));
        chatThread.setPageId(rs.getString(PAGE_ID_COL));
        chatThread.setUserId(rs.getString(USER_ID_COL));
        chatThread.setCreatedtime(getDateFromRs(rs, CREATED_TIME_COL));
        chatThread.setLastUpdatedTime(getDateFromRs(rs, LAST_UPDATED_TIME_COL));
        return chatThread;
    }

    @Override
    public ChatThread insert(ChatThread chatThread) {
        logger.info("inserting chat thread : " + chatThread);
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ORG_ID_COL, chatThread.getOrgId());
        paramMap.put(PAGE_ID_COL, chatThread.getPageId());
        paramMap.put(USER_ID_COL, chatThread.getUserId());
        paramMap.put(CREATED_TIME_COL, chatThread.getCreatedtime());
        paramMap.put(LAST_UPDATED_TIME_COL, chatThread.getLastUpdatedTime());
        Number id = insert()
                .withTableName(TABLE)
                .usingColumns(paramMap.keySet().toArray(new String[] {}))
                .usingGeneratedKeyColumns(ID_COL)
                .executeAndReturnKey(paramMap);
        chatThread.setId(id.intValue());
        return chatThread;
    }

    @Override
    public void update(ChatThread chatThread) {
        logger.info("updating chat thread : " + chatThread);
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ID_COL, chatThread.getId());
        paramMap.put(ORG_ID_COL, chatThread.getOrgId());
        paramMap.put(PAGE_ID_COL, chatThread.getPageId());
        paramMap.put(USER_ID_COL, chatThread.getUserId());
        paramMap.put(CREATED_TIME_COL, chatThread.getCreatedtime());
        paramMap.put(LAST_UPDATED_TIME_COL, chatThread.getLastUpdatedTime());
        jdbc().update(UPDATE_CHAT_THREAD_Q, paramMap);
    }

    @Override
    public ChatThread findById(int id) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(ID_COL, id);
        List<ChatThread> chatThreads = jdbc().query(FIND_CHAT_THREAD_BY_ID_Q, paramMap, CHAT_THREAD_ROW_MAPPER);
        if (chatThreads != null)
            return chatThreads.get(0);
        return null;
    }

    @Override
    public List<ChatThread> getAll() {
        return jdbc().query(GET_ALL_Q, ImmutableMap.of(), CHAT_THREAD_ROW_MAPPER);
    }

    @Override
    public ChatThread findChatThread(String userId, String pageId) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(USER_ID_COL, userId);
        paramMap.put(PAGE_ID_COL, pageId);
        List<ChatThread> chatThreads = jdbc().query(FIND_CHAT_THREAD_Q, paramMap, CHAT_THREAD_ROW_MAPPER);
        if (!chatThreads.isEmpty())
            return chatThreads.get(0);
        return null;
    }

    @Override
    public void updateLastUpdatedTime(int id) {
        Map<String, Object> paramMap = Maps.newHashMap();
        Date now = new Date();
        paramMap.put(LAST_UPDATED_TIME_COL, now);
        paramMap.put(ID_COL, id);
        jdbc().update(LAST_UPD_TIME_Q, paramMap);
    }

}
