package com.capillary.social.base.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.capillary.social.base.api.FacebookManager;

public class FacebookManagerInitializer {

    private static Logger logger = LoggerFactory.getLogger(FacebookManagerInitializer.class);

    private static FacebookManager instance = null;

    public static void init(ApplicationContext springApplicationContext) {
        if (instance == null) {
            logger.info("Creating Event Manager instance through spring application context");
            instance = springApplicationContext.getBean(FacebookManager.class);
        }
    }

    /**
     * Get the instance of the event manager
     * 
     * @return
     */
    public static FacebookManager getFacebookManager() {
        return instance;
    }
}
