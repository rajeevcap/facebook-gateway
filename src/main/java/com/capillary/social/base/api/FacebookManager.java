package com.capillary.social.base.api;

import com.capillary.social.systems.config.SystemConfig;

public interface FacebookManager extends LifeCycle {

	
	/**
	 *starts the engine by registering the server 
	 */
	public void startEngine( );

	public enum RunningMode { PROD, TEST };
	
    /**
     * To check if the running mode is testing or of production level
     * @return
     */
    RunningMode getRunningMode();

    /**
     * Returns the configs needed by service
     * @return
     */
    SystemConfig getSystemConfig();
  
}
