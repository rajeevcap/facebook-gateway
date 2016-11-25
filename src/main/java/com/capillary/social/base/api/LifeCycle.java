package com.capillary.social.base.api;

import com.capillary.social.base.api.SystemStatus;

public interface LifeCycle {

	/**
	 * Starts the system and returns the status after the start.
	 * 
	 * @throws IllegalStateException
	 *             If any error happens while starting the system.
	 */
	SystemStatus start();

	/**
	 * Stops the system.
	 * 
	 * @return The system status after the stop.
	 */
	SystemStatus stop();

	/**
	 * The system status after the restart.
	 * 
	 * @return The system status after the restart.
	 */
	SystemStatus restart();

	/**
	 * @return The current system status.
	 */
	SystemStatus getCurrentStatus();
}
