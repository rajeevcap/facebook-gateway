package com.capillary.social.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.capillary.social.ButtonMessage;
import com.capillary.social.GenericMessage;
import com.capillary.social.ListMessage;
import com.capillary.social.QuickReplyMessage;
import com.capillary.social.ReceiptMessage;
import com.capillary.social.TextMessage;
import com.capillary.social.dao.api.ChatDao;
import com.capillary.social.dao.impl.ChatDaoImpl;
import com.capillary.social.services.impl.FacebookButtonMessage;
import com.capillary.social.services.impl.FacebookGenericMessage;
import com.capillary.social.services.impl.FacebookListMessage;
import com.capillary.social.services.impl.FacebookQuickReplyMessage;
import com.capillary.social.services.impl.FacebookReceiptMessage;
import com.capillary.social.services.impl.FacebookTextMessage;

@Component
public class ApplicationContextAwareHandler implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return appContext;
    }
}
