package com.capillary.social;

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
import com.capillary.servicediscovery.model.Module;
import com.capillary.social.base.api.FacebookManager;
import com.capillary.social.base.impl.FacebookManagerInitializer;

public class FacebookServiceRunner {

	private static final Logger logger = LoggerFactory
			.getLogger(FacebookServiceRunner.class);
	private static final String STARTUP_MSG = " Starting Facebook Gateway Service";

	private static final String SHUTDOWN_MSG_INIT = " Stopping Facebook Gateway Service";

	private static final String SHUTDOWN_MSG = "\n"
			+ "   SSSSSSSSSSSSSSS  TTTTTTTTTTTTTTTTTTTTTTT      OOOOOOOOO      PPPPPPPPPPPPPPPPP    PPPPPPPPPPPPPPPPP    EEEEEEEEEEEEEEEEEEEEEE DDDDDDDDDDDDD\n"
			+ " SS:::::::::::::::S T:::::::::::::::::::::T    OO:::::::::OO    P::::::::::::::::P   P::::::::::::::::P   E::::::::::::::::::::E D::::::::::::DDD\n"
			+ "S:::::SSSSSS::::::S T:::::::::::::::::::::T  OO:::::::::::::OO  P::::::PPPPPP:::::P  P::::::PPPPPP:::::P  E::::::::::::::::::::E D:::::::::::::::DD\n"
			+ "S:::::S     SSSSSSS T:::::TT:::::::TT:::::T O:::::::OOO:::::::O PP:::::P     P:::::P PP:::::P     P:::::P EE::::::EEEEEEEEE::::E DDD:::::DDDDD:::::D\n"
			+ "S:::::S             TTTTTT  T:::::T  TTTTTT O::::::O   O::::::O   P::::P     P:::::P   P::::P     P:::::P   E:::::E       EEEEEE   D:::::D    D:::::D\n"
			+ "S:::::S                     T:::::T         O:::::O     O:::::O   P::::P     P:::::P   P::::P     P:::::P   E:::::E                D:::::D     D:::::D\n"
			+ " S::::SSSS                  T:::::T         O:::::O     O:::::O   P::::PPPPPP:::::P    P::::PPPPPP:::::P    E::::::EEEEEEEEEE      D:::::D     D:::::D\n"
			+ "  SS::::::SSSSS             T:::::T         O:::::O     O:::::O   P:::::::::::::PP     P:::::::::::::PP     E:::::::::::::::E      D:::::D     D:::::D\n"
			+ "    SSS::::::::SS           T:::::T         O:::::O     O:::::O   P::::PPPPPPPPP       P::::PPPPPPPPP       E:::::::::::::::E      D:::::D     D:::::D\n"
			+ "       SSSSSS::::S          T:::::T         O:::::O     O:::::O   P::::P               P::::P               E::::::EEEEEEEEEE      D:::::D     D:::::D\n"
			+ "            S:::::S         T:::::T         O:::::O     O:::::O   P::::P               P::::P               E:::::E                D:::::D     D:::::D\n"
			+ "            S:::::S         T:::::T         O::::::O   O::::::O   P::::P               P::::P               E:::::E       EEEEEE   D:::::D    D:::::D\n"
			+ "SSSSSSS     S:::::S       TT:::::::TT       O:::::::OOO:::::::O PP::::::PP           PP::::::PP           EE::::::EEEEEEEE:::::E DDD:::::DDDDD:::::D\n"
			+ "S::::::SSSSSS:::::S       T:::::::::T        OO:::::::::::::OO  P::::::::P           P::::::::P           E::::::::::::::::::::E D:::::::::::::::DD\n"
			+ "S:::::::::::::::SS        T:::::::::T          OO:::::::::OO    P::::::::P           P::::::::P           E::::::::::::::::::::E D::::::::::::DDD\n"
			+ " SSSSSSSSSSSSSSS          TTTTTTTTTTT            OOOOOOOOO      PPPPPPPPPP           PPPPPPPPPP           EEEEEEEEEEEEEEEEEEEEEE DDDDDDDDDDDDD";

	private static final String PROPERTIES_FILE = "facebook-gateway-config.properties";

	public static void main(String[] args) {

		logger.info("Main method of facebook gateway entered.");

		ServiceDiscovery.setModule(new Module("facebook-gateway-service",
				"1.0.0"));

		try {

			try {

				start("facebook-gateway-service.xml");
			} catch (Exception e) {

				e.printStackTrace();
				logger.error("Error in starting facebook gateway service"
						+ e.getMessage());
			} catch (Throwable th) {

				logger.error("Error in starting facebook gateway service Throwable"
						+ th.getMessage());
				th.printStackTrace();
			}
			/* } */
		} catch (Exception e) {

			logger.info(" Could not acquire sd lock. " + e.getMessage());
		}
	}

	private static void start(String configFile) {

		logger.info(STARTUP_MSG);
		logger.info("Trying to read spring configuration from : " + configFile);

		// Read the application context
		ClassPathXmlApplicationContext springAppContext = new ClassPathXmlApplicationContext();
		Properties facebookProps = null;

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
		CollectionUtils.mergePropertiesIntoMap(facebookProps, result);

		PropertyPlaceholderConfigurer facebookPropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		facebookPropertyPlaceholderConfigurer.setProperties(result);
		facebookPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		springAppContext
				.addBeanFactoryPostProcessor(facebookPropertyPlaceholderConfigurer);
		springAppContext.setConfigLocation(configFile);
		springAppContext.refresh();

		// Create the event manager and then start it first
		logger.info("Initializing facebook manager first");

		FacebookManagerInitializer.init(springAppContext);

		FacebookManager facebookManager = FacebookManagerInitializer
				.getFacebookManager();

		logger.info("Facebook Manager has been initialized, next start the service");
		facebookManager.start();

		// Add the shutdown hook
		logger.info("Adding the shutdown hook to stop the event manager");
		Runtime.getRuntime().addShutdownHook(new Thread("ShutdownHook") {
			@Override
			public void run() {
				FacebookServiceRunner.stop();
			}
		});

	}

	public static void stop() {
		// TODO Auto-generated method stub
		logger.info( SHUTDOWN_MSG_INIT );
        logger.info( "EXECUTING SHUTDOWN HOOK" );
        
        try{
        	
            logger.info( "STOPPING FACEBOOK MANAGER" );
            FacebookManager venenoManager = 
            		FacebookManagerInitializer.getFacebookManager();

    		venenoManager.stop();
            logger.info( "STOPPED FACEBOOK MANAGER" );
            
        }catch (Exception e) {
        	
        	logger.error( "Error while stopping veneno manager : " , e);
		}
        
        //Release locks
        logger.info( "Removing lock" );
        try {

        	ServiceDiscovery.setModule( new Module( "facebook-gateway-service", "1.0.0" ) );
        	ServiceDiscovery sd = ServiceDiscovery.getInstance();
        	/*sd.releaseDistributedFileLock( "venenoListenerLock" );*/
        	sd.close();
        } catch ( Exception e ) {
            logger.error( "Unable to release distributed sd lock " + e);
        }
        
        logger.info( SHUTDOWN_MSG );

	}
}
