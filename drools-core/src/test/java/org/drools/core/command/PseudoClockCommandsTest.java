package org.drools.core.command;

import static org.junit.Assert.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.drools.core.RuleBaseConfiguration;
import org.drools.core.SessionConfiguration;
import org.drools.core.command.impl.ContextImpl;
import org.drools.core.command.impl.DefaultCommandService;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.PseudoClockAdvanceTimeCommand;
import org.drools.core.command.runtime.PseudoClockCompareAndSetCommand;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.runtime.impl.ExecutionResultImpl;
import org.drools.core.time.SessionPseudoClock;
import org.drools.core.world.impl.WorldImpl;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.time.SessionClock;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class PseudoClockCommandsTest {

	private KieSession kieSession;
	
	@Before
	public void setup() {
		RuleBaseConfiguration kieBaseConf = new RuleBaseConfiguration();
		kieBaseConf.setOption( EventProcessingOption.STREAM );
		
		KieSessionConfiguration kieSessionConf = new SessionConfiguration();
		kieSessionConf.setOption( ClockTypeOption.get("pseudo") );
		
		KnowledgeBaseImpl kieBase = new KnowledgeBaseImpl("id1", kieBaseConf);
		kieSession = kieBase.newKieSession(kieSessionConf, null);
	}

	@Test
	public void testGetSessionClockCommand() {
		SessionPseudoClock pseudoClock = kieSession.getSessionClock();
		
		SessionClock exec1 = kieSession.execute(new GetSessionClockCommand());
		
		assertTrue("I expected to be able create kieSession with pseudoclock", exec1 instanceof SessionPseudoClock);
		assertEquals(pseudoClock.getCurrentTime(), exec1.getCurrentTime());
		
		pseudoClock.advanceTime(1, TimeUnit.HOURS);
		
		SessionClock exec2 = kieSession.execute(new GetSessionClockCommand());
		
		assertEquals(pseudoClock.getCurrentTime(), exec2.getCurrentTime());
	}
	
	@Test
	public void testPseudoClockAdvanceTimeCommand() {
		SessionPseudoClock pseudoClock = kieSession.getSessionClock();
		
		assertEquals(0L, pseudoClock.getCurrentTime());
		
		// pseudoClock.advanceTime(1, TimeUnit.HOURS);
		PseudoClockAdvanceTimeCommand cmd1 = new PseudoClockAdvanceTimeCommand(1, TimeUnit.HOURS);
		Long exec1 = kieSession.execute(cmd1);
		
		assertEquals(3600000L, exec1.longValue());
		assertEquals(3600000L, pseudoClock.getCurrentTime());
		
		
		
		String outIdentifier = "outid";
		PseudoClockAdvanceTimeCommand cmd2 = new PseudoClockAdvanceTimeCommand(1, TimeUnit.MINUTES, outIdentifier);
		BatchExecutionCommandImpl beCmd = buildBatchCmd(cmd2);
		
		try {
			JAXBContext ctx = JAXBContext.newInstance(PseudoClockAdvanceTimeCommand.class, BatchExecutionCommandImpl.class);
			ctx.createMarshaller().marshal(beCmd, System.out);
			System.out.println("---");
			for ( PropertyDescriptor pd : Introspector.getBeanInfo(beCmd.getClass(), Object.class).getPropertyDescriptors() ) {
				System.out.println(pd.getReadMethod());
			}
			for ( PropertyDescriptor pd : Introspector.getBeanInfo(cmd2.getClass(), Object.class).getPropertyDescriptors() ) {
				System.out.println(pd.getReadMethod());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ExecutionResults exec2 = kieSession.execute(beCmd);
		
		Object exec2Result = exec2.getValue(outIdentifier);
		assertEquals(3660000L, ((Long) exec2Result).longValue());
		assertEquals(3660000L, pseudoClock.getCurrentTime());
	}
	
	@Test
	public void testPseudoClockCompareAndSetCommand() {
		SessionPseudoClock pseudoClock = kieSession.getSessionClock();
		
		assertEquals(0L, pseudoClock.getCurrentTime());
		
		
		
		PseudoClockCompareAndSetCommand cmd1 = new PseudoClockCompareAndSetCommand(0L, 1L);
		Boolean exec1 = kieSession.execute(cmd1);
		
		assertEquals(true, exec1);
		assertEquals(1L, pseudoClock.getCurrentTime());
		

		
		String outIdentifier = "outid";
		PseudoClockCompareAndSetCommand cmd2 = new PseudoClockCompareAndSetCommand(1L, 2L, outIdentifier);
		BatchExecutionCommandImpl beCmd = buildBatchCmd(cmd2);
		ExecutionResults exec2 = kieSession.execute(beCmd);
		
		assertEquals(true, exec2.getValue(outIdentifier));
		assertEquals(2L, pseudoClock.getCurrentTime());
		
		
		
		PseudoClockCompareAndSetCommand cmd3 = new PseudoClockCompareAndSetCommand(47L, 99L);
		Boolean exec3 = kieSession.execute(cmd3);
		
		assertEquals(false, exec3);
		assertEquals(2L, pseudoClock.getCurrentTime());
	}

	private static BatchExecutionCommandImpl buildBatchCmd(GenericCommand<?> cmd2) {
		List<GenericCommand<?>> cmdList = new ArrayList<GenericCommand<?>>();
		cmdList.add(cmd2);
		BatchExecutionCommandImpl beCmd = new BatchExecutionCommandImpl(cmdList);
		return beCmd;
	}

}
