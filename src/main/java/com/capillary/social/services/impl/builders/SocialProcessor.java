package com.capillary.social.services.impl.builders;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by rajeev on 13/11/17.
 */
public class SocialProcessor {

    private static final String API_ADWORDS_CLIENT_ID_KEY = "api.adwords.clientId";

    private static final String API_ADWORDS_CLIENT_SECRET_KEY = "api.adwords.clientSecret";

    private static final String API_ADWORDS_REFRESH_TOKEN_KEY = "api.adwords.refreshToken";

    private static final String API_ADWORDS_DEVELOPER_TOKEN_KEY = "api.adwords.developerToken";

    private static final String API_ADWORDS_CLIENT_CONSUMER_ID_KEY = "api.adwords.clientCustomerId";

    private static final String API_ADWORDS_CLIENT_ID_VALUE = "503263785661-5vcvoetfs9cg13h4d6nh3l3ap95seq8u.apps.googleusercontent.com";

    private static final String API_ADWORDS_CLIENT_SECRET_VALUE = "jHZSpfYV-gqbGpt-HTbwVMMj";

    private static final String API_ADWORDS_REFRESH_TOKEN_VALUE = "1/ax1jbbIEI0qwUKCDKtvphQ72hvWBJot1bqcLGpYmAmE";

    private static final String API_ADWORDS_DEVELOPER_TOKEN_VALUE = "eayfsLBDHibJVApSj-L31g";

    protected static final String API_ADWORDS_CLIENT_CONSUMER_ID_VALUE = "240-214-3248";

    //TODO: Read from master.config_keys
    protected static PropertiesConfiguration getPropertiesConfiguration() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.setProperty(API_ADWORDS_CLIENT_ID_KEY, API_ADWORDS_CLIENT_ID_VALUE);
        config.setProperty(API_ADWORDS_CLIENT_SECRET_KEY, API_ADWORDS_CLIENT_SECRET_VALUE);
        config.setProperty(API_ADWORDS_REFRESH_TOKEN_KEY, API_ADWORDS_REFRESH_TOKEN_VALUE);
        config.setProperty(API_ADWORDS_DEVELOPER_TOKEN_KEY, API_ADWORDS_DEVELOPER_TOKEN_VALUE);
        config.setProperty(API_ADWORDS_CLIENT_CONSUMER_ID_KEY, API_ADWORDS_CLIENT_CONSUMER_ID_VALUE);
        return config;
    }

}
