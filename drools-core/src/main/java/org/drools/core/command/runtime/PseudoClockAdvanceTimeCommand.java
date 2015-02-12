package org.drools.core.command.runtime;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.drools.core.command.IdentifiableResult;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.runtime.KieSession;
import org.kie.internal.command.Context;

@XmlAccessorType(XmlAccessType.NONE)
public class PseudoClockAdvanceTimeCommand
	implements
	GenericCommand<Long>, IdentifiableResult{ 

	private static final long serialVersionUID = -2517207502769584537L;
	private long amount;
	private TimeUnit unit;
	private String outIdentifier;
	
	public PseudoClockAdvanceTimeCommand(long amount, TimeUnit unit) {
		super();
		this.amount = amount;
		this.unit = unit;
	}

	public PseudoClockAdvanceTimeCommand(long amount, TimeUnit unit, String outIdentifier) {
		super();
		this.amount = amount;
		this.unit = unit;
		this.outIdentifier = outIdentifier;
	}

	@Override
	public Long execute(Context context) {
        KnowledgeCommandContext kcContext = (KnowledgeCommandContext) context;
		KieSession kieSession = kcContext.getKieSession();
        SessionPseudoClock pseudoClock = (SessionPseudoClock) kieSession.getSessionClock();
        
        long result = pseudoClock.advanceTime(amount, unit);
        
        if (this.outIdentifier != null) {
        	((StatefulKnowledgeSessionImpl) kieSession)
        		.getExecutionResult()
                .getResults()
                .put( this.outIdentifier, result );
        }
        
		return result;
	}

    @XmlAttribute(name="out-identifier")
    public String getOutIdentifier() {
        return outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

	public long getAmount() {
		return amount;
	}
	
	@XmlAttribute(name="amount", required=true)
	public void setAmount(long amount) {
		this.amount = amount;
	}

	public TimeUnit getUnit() {
		return unit;
	}
	
	@XmlAttribute(name="unit", required=true)
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

    
}
