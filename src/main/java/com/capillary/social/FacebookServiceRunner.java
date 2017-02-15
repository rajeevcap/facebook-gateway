package com.capillary.social;

import static com.capillary.social.FacebookServiceRunnerConstants.FACEBOOK_GATEWAY_MODULE;
import static com.capillary.social.FacebookServiceRunnerConstants.SHUTDOWN_MSG;
import static com.capillary.social.FacebookServiceRunnerConstants.STARTUP_MSG;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import com.capillary.servicediscovery.ServiceDiscovery;
import com.capillary.social.base.api.FacebookManager;
import com.capillary.social.base.impl.FacebookManagerInitializer;

public class FacebookServiceRunner {
    private static final Logger logger = LoggerFactory.getLogger(FacebookServiceRunner.class);

    private static final String PROPERTIES_FILE = "facebook-gateway-config.properties";

    public static void main(String[] args) {

        logger.info("Main method of facebook gateway entered.");

        ServiceDiscovery.setModule(FACEBOOK_GATEWAY_MODULE);

        try {
            start("facebook-gateway-service.xml");
        } catch (Exception e) {
            logger.error("Error in starting facebook gateway service" + e.getMessage());
        }
    }

    private static void start(String configFile) {

        logger.info(STARTUP_MSG);
        logger.info("Trying to read spring configuration from : " + configFile);

        // Read the application context
        ClassPathXmlApplicationContext springAppContext = setupSpringContext(configFile);
        springAppContext.refresh();

        // Create the event manager and then start it first
        logger.info("Initializing facebook manager first");

        FacebookManagerInitializer.init(springAppContext);

        FacebookManager facebookManager = FacebookManagerInitializer.getFacebookManager();

        logger.info("Facebook Manager has been initialized, next start the service");
        facebookManager.start();

        // Add the shutdown hook
        logger.info("Adding the shutdown hook to stop the event manager");
        Runtime.getRuntime().addShutdownHook(new Thread("ShutdownHook") {
            @Override
            public void run() {
                facebookManager.stop();
            }
        });

    }

    private static ClassPathXmlApplicationContext setupSpringContext(String configFile) {
        ClassPathXmlApplicationContext springAppContext = new ClassPathXmlApplicationContext();

        // Local Properties
        Properties localProperties = new Properties();
        Resource resource = new ClassPathResource(PROPERTIES_FILE);
        InputStream inputStream = null;
        try {

            inputStream = resource.getInputStream();
            localProperties.load(inputStream);
        } catch (IOException e) {

            logger.error("Error in loading local properties " + e.getMessage());
        } finally {

            IOUtils.closeQuietly(inputStream);
        }

        Properties result = new Properties();
        CollectionUtils.mergePropertiesIntoMap(localProperties, result);

        PropertyPlaceholderConfigurer facebookPropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        facebookPropertyPlaceholderConfigurer.setProperties(result);
        facebookPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        springAppContext.addBeanFactoryPostProcessor(facebookPropertyPlaceholderConfigurer);
        springAppContext.setConfigLocation(configFile);
        return springAppContext;
    }
    
    public static void stop() {
        
        logger.info(SHUTDOWN_MSG);
        FacebookManager facebookManager = FacebookManagerInitializer.getFacebookManager();
        facebookManager.stop();
        logger.info("FACEBOOK GATEWAY STOPPED");

    }
}
