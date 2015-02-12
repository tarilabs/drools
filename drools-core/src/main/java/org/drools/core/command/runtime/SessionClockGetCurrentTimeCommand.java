package org.drools.core.command.runtime;

import javax.xml.bind.annotation.XmlAttribute;

import org.drools.core.command.IdentifiableResult;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionClock;
import org.kie.internal.command.Context;

/**
 * To serialize over-the-wire e.g.: REST API / XML the session clock current time value.
 * Otherwise a simple GetSessionClockCommand would attempt to serialize the entire reference to the "realtime" or "pseudoclock" session clock. Here we just want the session clock current time value.
 */
public class SessionClockGetCurrentTimeCommand 
		implements
		GenericCommand<Long>, IdentifiableResult {
	
	private static final long serialVersionUID = 2005558019928221415L;
	
	@XmlAttribute(name="out-identifier", required=true)
    private String outIdentifier;
	
	public SessionClockGetCurrentTimeCommand(String outIdentifier) {
		super();
		this.outIdentifier = outIdentifier;
	}

	@Override
	public Long execute(Context context) {
        KnowledgeCommandContext kcContext = (KnowledgeCommandContext) context;
		KieSession kieSession = kcContext.getKieSession();
        SessionClock sessionClock = kieSession.<SessionClock>getSessionClock();
        
        long result = sessionClock.getCurrentTime();
        
        if (this.outIdentifier != null) {
        	((StatefulKnowledgeSessionImpl) kieSession)
        		.getExecutionResult()
                .getResults()
                .put( this.outIdentifier, result );
        }
        
		return result;
	}

    public String getOutIdentifier() {
        return outIdentifier;
    }

    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }
}
