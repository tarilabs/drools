package org.drools.compiler.kproject;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;

public class ReleaseIdTest {

	@Test
	public void testEnsureCoherentImplementationWithJMXDeserialization() {
		String groupId = "org.acme";	
		String artifactId = "myartifact";
		String version = "1.0.0";
		
		KieServices ks = KieServices.Factory.get();
		
		ReleaseId implementation = ks.newReleaseId(groupId, artifactId, version);
		ReleaseId jmxDeserialization = new ReleaseId.ReleaseIdJMXImpl(groupId, artifactId, version, implementation.isSnapshot());
		
		assertEquals(implementation.toExternalForm(), jmxDeserialization.toExternalForm());
	}
}
