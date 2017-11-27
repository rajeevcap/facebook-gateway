package com.capillary.social.utils;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.collect.ObjectArrays;
import org.joda.time.DateTime;

public class FacebookGatewayUtils {
    
    public static List<String> countryCurrencyCodes = new ArrayList<String>(Arrays.asList("AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BOV","BRL","BSD","BTN","BWP","BYR","BZD","CAD","CDF","CHE","CHF","CHW","CLF","CLP","CNY","COP","COU","CRC","CUP","CVE","CYP","CZK","DJF","DKK","DOP","DZD","EEK","EGP","ERN","ETB","EUR","FJD","FKP","GBP","GEL","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","INR","IQD","IRR","ISK","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LTL","LVL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MTL","MUR","MVR","MWK","MXN","MXV","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SKK","SLL","SOS","SRD","STD","SYP","SZL","THB","TJS","TMM","TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","USD","USN","USS","UYU","UZS","VEB","VND","VUV","WST","XAF","XAG","XAU","XBA","XBB","XBC","XBD","XCD","XDR","XFO","XFU","XOF","XPD","XPF","XPT","XTS","XXX","YER","ZAR","ZMK","ZWD"));

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

    public static String[] merge(String[]...arrays) {
        int arrayCount = arrays.length;
        if(arrayCount == 1) {
            return arrays[0];
        }
        /*int arraySumLength = 0;
        for(String[] array : arrays) {
            arraySumLength += array.length;
        }
        String[] result = (String[]) Array.newInstance(String.class, arraySumLength);*/
        String[] result = arrays[0];
        for(int i = 1; i < arrayCount; i++) {
            result = merge(result, arrays[i]);
        }
        return result;
    }

    public static String[] merge(String[] a1, String[] a2) {
        return ObjectArrays.concat(a1, a2, String.class);
    }

}
