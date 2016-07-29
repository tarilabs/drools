package org.drools.compiler.management;

import org.drools.compiler.kie.builder.impl.InternalKieContainer;
import org.kie.api.builder.ReleaseId;

public class KieContainerMonitor implements KieContainerMonitorMXBean {
	private InternalKieContainer kieContainer;

	public KieContainerMonitor(InternalKieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}

	@Override
	public String getContainerId() {
		return kieContainer.getContainerId();
	}

	@Override
	public ReleaseId getConfiguredReleaseId() {
		return kieContainer.getConfiguredReleaseId();
	}

	@Override
	public ReleaseId getResolvedReleaseId() {
		return kieContainer.getResolvedReleaseId();
	}

	@Override
	public String getConfiguredReleaseIdStr() {
		return getConfiguredReleaseId().toString();
	}

	@Override
	public String getResolvedReleaseIdStr() {
		return getResolvedReleaseId().toString();
	}
}
