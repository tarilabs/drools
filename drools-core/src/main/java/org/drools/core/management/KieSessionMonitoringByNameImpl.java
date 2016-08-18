package org.drools.core.management;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;

import org.drools.core.management.KieSessionMonitoringImpl.AgendaStats.AgendaStatsData;
import org.drools.core.management.KieSessionMonitoringImpl.ProcessStats.GlobalProcessStatsData;
import org.drools.core.management.KieSessionMonitoringImpl.ProcessStats.ProcessInstanceStatsData;
import org.drools.core.management.KieSessionMonitoringImpl.ProcessStats.ProcessStatsData;
import org.kie.api.management.KieSessionMonitoringMBean;

public class KieSessionMonitoringByNameImpl implements KieSessionMonitoringMBean {
    private static final long NANO_TO_MILLISEC = 1000000;

    private static final int BUFFER_THRESHOLD = 1000;

    private final ObjectName name;
    private final String kbaseId;
    
    private AgendaStatsData consolidated = new AgendaStatsData();
    private ConcurrentHashMap<String, AgendaStatsData> ruleStats = new ConcurrentHashMap<String, AgendaStatsData>();
    
    private GlobalProcessStatsData processConsolidated = new GlobalProcessStatsData();
    private ConcurrentHashMap<String, ProcessStatsData> processStats = new ConcurrentHashMap<String, ProcessStatsData>();
    private ConcurrentHashMap<Long, ProcessInstanceStatsData> processInstanceStats = new ConcurrentHashMap<Long, ProcessInstanceStatsData>();
    
    private SynchronizedFastTakeAllList<StatEvent> buffer = new SynchronizedFastTakeAllList<StatEvent>();
    
    public KieSessionMonitoringByNameImpl(ObjectName name, String kbaseId) {
        super();
        this.name = name;
        this.kbaseId = kbaseId;
    }
    
    /**
     * In this case, there is no unique session Id.
     */
    @Override
    public int getKieSessionId() {
        return -1;
    }
    
    public ObjectName getName() {
        return name;
    }

    public String getKieBaseId() {
        return kbaseId;
    }
    
    
    public void reset() {
        this.consolidated.reset();
        this.ruleStats.clear();
        this.processConsolidated.reset();
        this.processStats.clear();
        this.processInstanceStats.clear();
    }


    // FIXME ?!?!??!
    
    public long getTotalFactCount() {
        return -1;
    }
    

    public long getTotalMatchesFired() {
        processBuffer();
        return this.consolidated.matchesFired.get();
    }
    

    public long getTotalMatchesCancelled() {
        processBuffer();
        return this.consolidated.matchesCancelled.get();
    }
    

    public long getTotalMatchesCreated() {
        processBuffer();
        return this.consolidated.matchesCreated.get();
    }
    

    public long getTotalFiringTime() {
        processBuffer();
        // converting nano secs to milli secs
        return this.consolidated.firingTime.get()/NANO_TO_MILLISEC;
    }
    
    public Date getLastReset() {
        return this.consolidated.lastReset.get();
    }
    
    public double getAverageFiringTime() {
        processBuffer();
        long fires = this.consolidated.matchesFired.get();
        long time = this.consolidated.firingTime.get();
        // calculating the average and converting it from nano secs to milli secs
        return fires > 0 ? (((double) time / (double) fires) / (double) NANO_TO_MILLISEC) : 0;
    }
    
    public String getStatsForRule( String ruleName ) {
        processBuffer();
        AgendaStatsData data = this.ruleStats.get( ruleName );
        String result = data == null ? "matchesCreated=0 matchesCancelled=0 matchesFired=0 firingTime=0ms" : data.toString();
        return result;
    }
    
    public Map<String,String> getStatsByRule() {
        processBuffer();
        Map<String, String> result = new HashMap<String, String>();
        for( Map.Entry<String, AgendaStatsData> entry : this.ruleStats.entrySet() ) {
            result.put( entry.getKey(), entry.getValue().toString() );
        }
        return result;
    }
    
    public long getTotalProcessInstancesStarted() {
        return this.processConsolidated.processInstancesStarted.get();
    }
    
    public long getTotalProcessInstancesCompleted() {
        return this.processConsolidated.processInstancesCompleted.get();
    }
    
    public String getStatsForProcess( String processId ) {
        ProcessStatsData data = this.processStats.get( processId );
        String result = data == null ? "processInstancesStarted=0 processInstancesCompleted=0 processNodesTriggered=0" : data.toString();
        return result;
    }
    
    public Map<String,String> getStatsByProcess() {
        Map<String, String> result = new HashMap<String, String>();
        for( Map.Entry<String, ProcessStatsData> entry : this.processStats.entrySet() ) {
            result.put( entry.getKey(), entry.getValue().toString() );
        }
        return result;
    }
    
    public String getStatsForProcessInstance( long processInstanceId ) {
        ProcessInstanceStatsData data = this.processInstanceStats.get( processInstanceId );
        String result = data == null ? "Process instance not found" : data.toString();
        return result;
    }
    
    public Map<Long,String> getStatsByProcessInstance() {
        Map<Long, String> result = new HashMap<Long, String>();
        for( Map.Entry<Long, ProcessInstanceStatsData> entry : this.processInstanceStats.entrySet() ) {
            result.put( entry.getKey(), entry.getValue().toString() );
        }
        return result;
    }
    
    private AgendaStatsData getRuleStatsInstance(String ruleName) {
        AgendaStatsData data = this.ruleStats.get( ruleName );
        if( data == null ) {
            data = new AgendaStatsData();
            this.ruleStats.put( ruleName, data );
        }
        return data;
    }
    
    public void notify(StatEvent e) {
        buffer.add(e);
        if (buffer.size > BUFFER_THRESHOLD) {
            // TODO make async with an ad-hoc "short-lived" thread/executor?
            processBuffer();
        }
    }
    
    private void processBuffer() {
        for (Iterator<StatEvent> iterator = buffer.takeAll(); iterator.hasNext(); ) {
            StatEvent e = iterator.next();
            process(e);
        }
    }

    private void process(StatEvent e) {
        if (e == null) {
            return;
        }
        if ( e instanceof IncrementMatchCreated ) {
            this.consolidated.matchesCreated.incrementAndGet();
            AgendaStatsData data = getRuleStatsInstance( ((IncrementMatchCreated) e).getRuleName() );
            data.matchesCreated.incrementAndGet();
        } else if ( e instanceof IncrementMatchCancelled ) {
            this.consolidated.matchesCancelled.incrementAndGet();
            AgendaStatsData data = getRuleStatsInstance( ((IncrementMatchCancelled) e).getRuleName() );
            data.matchesCancelled.incrementAndGet();
        } else if ( e instanceof IncrementMatchFired ) {
            this.consolidated.matchesFired.incrementAndGet();
            AgendaStatsData data = getRuleStatsInstance( ((IncrementMatchFired) e).getRuleName() );
            data.matchesFired.incrementAndGet();
        } else if ( e instanceof AddToFiringTime ) {
            AddToFiringTime add = (AddToFiringTime) e;
            this.consolidated.firingTime.addAndGet( add.getDelta() );
            AgendaStatsData data = getRuleStatsInstance( add.getRuleName() );
            data.firingTime.addAndGet( add.getDelta() );
        }
    }

    public static interface StatEvent { }
    public static abstract class AgendaStatEvent implements StatEvent {
        private final String ruleName;

        public AgendaStatEvent(String ruleName) {
            this.ruleName = ruleName;
        }
        public String getRuleName() {
            return ruleName;
        }
    }
    public static class IncrementMatchCreated extends AgendaStatEvent {
        public IncrementMatchCreated(String ruleName) {
            super(ruleName);
        }
    }
    public static class IncrementMatchCancelled extends AgendaStatEvent {
        public IncrementMatchCancelled(String ruleName) {
            super(ruleName);
        }
    }
    public static class IncrementMatchFired extends AgendaStatEvent {
        public IncrementMatchFired(String ruleName) {
            super(ruleName);
        }
    }
    public static class AddToFiringTime extends AgendaStatEvent {
        private final long delta;

        public AddToFiringTime(String ruleName, long delta) {
            super(ruleName);
            this.delta = delta;
        }
        public long getDelta() {
            return delta;
        }
    }
}
