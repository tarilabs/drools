package org.drools.compiler.management;

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
	String getConfiguredReleaseIdStr();
	/**
	 * The actual resolved ReleaseId. 
	 * @return
	 */
	String getResolvedReleaseIdStr();
}
