package com.capillary.social.base.impl;

import static com.capillary.social.FacebookServiceRunnerConstants.FACEBOOK_GATEWAY_SERVICE_NAME;
import static com.capillary.social.FacebookServiceRunnerConstants.FACEBOOK_GATEWAY_SERVICE_VERSION;
import static com.capillary.social.FacebookServiceRunnerConstants.SHUTDOWN_MSG;
import static com.capillary.social.FacebookServiceRunnerConstants.SHUTDOWN_MSG_INIT;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capillary.commons.thrift.external.RPCManager;
import com.capillary.commons.thrift.external.RPCService;
import com.capillary.servicediscovery.ServiceDiscovery;
import com.capillary.servicediscovery.model.KnownService;
import com.capillary.servicediscovery.model.Module;
import com.capillary.social.base.api.FacebookManager;
import com.capillary.social.base.api.SystemStatus;
import com.capillary.social.systems.config.SystemConfig;
import com.capillary.social.FacebookService;
import com.capillary.social.FacebookServiceRunner;
import com.capillary.social.external.impl.FacebookServiceListener;

@Service
public class FacebookManagerImpl implements FacebookManager {

    private static final int MIN_THREADS = 2;

    private static final Logger logger = LoggerFactory.getLogger(FacebookManagerImpl.class);

    private SystemStatus systemStatus;

    @Autowired
    private SystemConfig systemConfig;

    @Override
    public SystemStatus start() {
        try {

            logger.info("START -> Obtained System operations monitor");

            if (this.systemStatus == SystemStatus.READY) {

                logger.info("START -> Already started");
                return this.systemStatus;
            }

            logger.info("Starting Event manager");
            this.systemStatus = SystemStatus.STARTING;

            // WAIT QUEUE / THREAD POOL
            try {

                ServiceDiscovery.getInstance().register(KnownService.FACEBOOK_THRIFT_SERVICE.createInstance());

            } catch (IOException e) {

                logger.error("Facebook Manager : Error while starting RPC service. " + "Exception : " + e.getMessage());
                throw new RuntimeException("Service could not be started");
            } catch (Exception e) {

                logger.error("Facebook Manager : Error while registering RPC service. "
                             + "Exception : "
                             + e.getMessage());
                throw new RuntimeException("Service could not be registered with ZooKeeper");
            }

            logger.info("Facebook Manager : Starting Engine : ");
            startEngine();

            logger.info("START -> DONE");
            this.systemStatus = SystemStatus.READY;

        } catch (Exception e) {

            logger.info("Exception " + "while starting facebook service with msg " + e.getMessage());
        } finally {

            if (this.systemStatus != SystemStatus.READY)
                this.systemStatus = SystemStatus.STOPPED;
        }

        return this.systemStatus;

    }

    @Override
    public SystemStatus stop() {
        try {

            logger.info("STOP -> Obtained System operations monitor");

            if (this.systemStatus == SystemStatus.STOPPED) {

                logger.info("STOP -> Already STOPPING");
                return this.systemStatus;
            }

            logger.info("Stopping Event manager");
            this.systemStatus = SystemStatus.STOPPING;

            stopService();

            logger.info("STOP -> DONE");
            this.systemStatus = SystemStatus.STOPPED;

            return this.systemStatus;
        } finally {

            if (this.systemStatus != SystemStatus.STARTING)
                this.systemStatus = SystemStatus.STOPPED;

            logger.info("STOP -> Leaving System operations monitor");
        }
    }

    @Override
    public SystemStatus restart() {
        this.stop();
        this.start();

        return this.getCurrentStatus();
    }

    @Override
    public SystemStatus getCurrentStatus() {
        return this.systemStatus;
    }

    private boolean startAsThriftService() {

        // Start the thrift service
        try {

            logger.info("START : Registering subscription manager thrift handler");

            com.capillary.servicediscovery.Service service = ServiceDiscovery.getInstance().get(
                    KnownService.FACEBOOK_THRIFT_SERVICE);

            RPCService rpcService = RPCManager.getINSTANCE().startRPCService(service.getPort(), MIN_THREADS,
                    systemConfig.SERVICE_MAX_THREAD);

            FacebookService.Iface facebookThriftService = new FacebookServiceListener();

            rpcService.exportService(FacebookService.Iface.class, facebookThriftService);

            return true;
        } catch (Exception e) {
            logger.error("START -> Unable to start thrift server : " + "Exception : " + e.getMessage());
            logger.error("Thrift server exception stack trace " + e.getStackTrace());
        }
        return false;
    }

    @Override
    public void startEngine() {

        boolean hasServiceStarted = startAsThriftService();

        if (!hasServiceStarted)
            throw new RuntimeException("Service Could Not Be started!!");

    }

    @Override
    public RunningMode getRunningMode() {

        return RunningMode.valueOf(systemConfig.RUNNING_MODE);
    }

    @Override
    public SystemConfig getSystemConfig() {

        return this.systemConfig;
    }

    private void stopService() {
        // TODO Auto-generated method stub
        logger.info(SHUTDOWN_MSG_INIT);

        try {

            ServiceDiscovery.setModule(new Module(FACEBOOK_GATEWAY_SERVICE_NAME, FACEBOOK_GATEWAY_SERVICE_VERSION));
            ServiceDiscovery sd = ServiceDiscovery.getInstance();
            /*sd.releaseDistributedFileLock( "venenoListenerLock" );*/
            sd.close();
        } catch (Exception e) {
            logger.error("Unable to release distributed sd lock " + e);
        }

        logger.info(SHUTDOWN_MSG);

    }
}
