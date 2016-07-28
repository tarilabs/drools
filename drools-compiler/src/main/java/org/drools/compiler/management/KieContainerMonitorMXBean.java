package org.drools.compiler.management;

import org.kie.api.builder.ReleaseId;

/** 
 * TODO to be ported on Kie API
 * 
 */
public interface KieContainerMonitorMXBean {
	
	
	ReleaseId getContainerReleaseId();

	String getName();
	
	ReleaseId getOriginReleaseId();
}
