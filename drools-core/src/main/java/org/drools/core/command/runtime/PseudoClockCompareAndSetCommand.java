package org.drools.core.command.runtime;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.drools.core.command.IdentifiableResult;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.Context;

/**
 * Modeled after JDK's java.util.concurrent.atomic.AtomicLong#compareAndSet(long expect, long update)
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class PseudoClockCompareAndSetCommand
	implements
	GenericCommand<Boolean>, IdentifiableResult{ 
	
	private static final long serialVersionUID = -4832516002006145984L;
	
	@XmlAttribute(name="expect", required=true)
	private long expect;
    
	@XmlAttribute(name="update", required=true)
	private long update;
    
	@XmlAttribute(name="out-identifier")
    private String outIdentifier;
	
	public PseudoClockCompareAndSetCommand(long expect, long update) {
		super();
		this.expect = expect;
		this.update = update;
	}

	public PseudoClockCompareAndSetCommand(long expect, long update, String outIdentifier) {
		super();
		this.expect = expect;
		this.update = update;
		this.outIdentifier = outIdentifier;
	}

	@Override
	public Boolean execute(Context context) {
        KnowledgeCommandContext kcContext = (KnowledgeCommandContext) context;
		KieSession kieSession = kcContext.getKieSession();
        SessionPseudoClock pseudoClock = (SessionPseudoClock) kieSession.getSessionClock();
        final long currentTime = pseudoClock.getCurrentTime();
        
		boolean isExpectSatisfied = ( currentTime == expect );
        
        if (isExpectSatisfied) {
        	long delta = update - currentTime;
        	// because PseudoClockScheduler advanceTime internally uses millis.
        	pseudoClock.advanceTime(delta, TimeUnit.MILLISECONDS);
        }
        
        if (this.outIdentifier != null) {
        	((StatefulKnowledgeSessionImpl) kieSession)
        		.getExecutionResult()
                .getResults()
                .put( this.outIdentifier, isExpectSatisfied );
        }
        
		return isExpectSatisfied;
	}

    public String getOutIdentifier() {
        return outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

	public long getExpect() {
		return expect;
	}

	public void setExpect(long expect) {
		this.expect = expect;
	}

	public long getUpdate() {
		return update;
	}

	public void setUpdate(long update) {
		this.update = update;
	}
}
