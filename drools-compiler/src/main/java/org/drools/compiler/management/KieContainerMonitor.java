package org.drools.compiler.management;

import org.drools.compiler.kie.builder.impl.InternalKieContainer;
import org.kie.api.builder.ReleaseId;

public class KieContainerMonitor implements KieContainerMonitorMXBean {
	private InternalKieContainer kieContainer;

	public KieContainerMonitor(InternalKieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}

	@Override
	public ReleaseId getContainerReleaseId() {
		return kieContainer.getContainerReleaseId();
	}

	@Override
	public String getName() {
		return kieContainer.getName();
	}

	@Override
	public ReleaseId getOriginReleaseId() {
		return kieContainer.getOriginReleaseId();
	}
}
