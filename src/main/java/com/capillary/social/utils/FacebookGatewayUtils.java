package com.capillary.social.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;

public class FacebookGatewayUtils {
    public static final String REQUEST_ORG_ID_MDC = "requestOrgId";
    public static final String REQUEST_ID_MDC = "requestId";
    public static final String REQUEST_TYPE_MDC = "requestType";
    public static final String REQUEST_USER_ID_MDC = "userID";

    public static DateTime getDateTimeFromRs(ResultSet rs, String columnName) throws SQLException {
        if (null == rs) {
            return null;
        }
        Object dateObj = rs.getObject(columnName);
        if (null == dateObj) {
            return null;
        }
        Timestamp timestamp = rs.getTimestamp(columnName);
        if (timestamp != null) {
            return new DateTime(timestamp.getTime());
        }
        return null;
    }

    public static Date getDateFromRs(ResultSet rs, String columnName) throws SQLException {
        if (null == rs) {
            return null;
        }
        Object dateObj = rs.getObject(columnName);
        if (null == dateObj) {
            return null;
        }
        Timestamp timestamp = rs.getTimestamp(columnName);
        if (timestamp != null) {
            return new DateTime(timestamp.getTime()).toDate();
        }
        return null;
    }

    public static String getDateAsYMDHMS(Date date, boolean addDoubleQuotes) {
        if (date == null)
            return addDoubleQuotes ? "NULL" : null;
        String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        return addDoubleQuotes ? ('"' + dateStr + '"') : dateStr;
    }

}
