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
	public String getConfiguredReleaseIdStr() {
		return kieContainer.getConfiguredReleaseId().toString();
	}

	@Override
	public String getResolvedReleaseIdStr() {
		return kieContainer.getResolvedReleaseId().toString();
	}
}
