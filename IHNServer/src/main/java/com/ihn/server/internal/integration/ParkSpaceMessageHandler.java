/**
 * 
 */
package com.ihn.server.internal.integration;

/**
 *
 */
public interface ParkSpaceMessageHandler {

	public void update(ParkSpaceStatusMessage msg);
	
	public void batchUpdate(ParkSpaceStatusBatchMessage msgs);
	
}
