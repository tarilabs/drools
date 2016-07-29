package org.drools.compiler.management;

import org.kie.api.builder.ReleaseId;

/** 
 * TODO to be ported on Kie API
 * 
 */
public interface KieContainerMonitorMXBean {

	String getContainerId();
	/**
	 * The RelaseId configured while creating the Kiecontainer.
	 * @return
	 */
	ReleaseId getConfiguredReleaseId();
	String getConfiguredReleaseIdStr();
	/**
	 * The actual resolved ReleaseId. 
	 * @return
	 */
	ReleaseId getResolvedReleaseId();
	String getResolvedReleaseIdStr();
}
