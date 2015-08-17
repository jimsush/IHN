package com.ihn.server.internal.integration;

import java.util.Set;

public interface SyncParkSpace {

	public Set<String> getUsedParkSpace(String parkingId);
	
}
