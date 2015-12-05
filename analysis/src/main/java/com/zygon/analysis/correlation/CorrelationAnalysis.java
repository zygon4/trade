
package com.zygon.analysis.correlation;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author zygon
 */
public class CorrelationAnalysis {

    private final Map<String,Correlation> correlationsByCorrelateName = Maps.newLinkedHashMap();
    private final String targetName;

    public CorrelationAnalysis(String target, Collection<String> correlates) {
        correlates.forEach(c -> correlationsByCorrelateName.put(c, new Correlation(target, c)));
        this.targetName = target;
    }

    public CorrelationAnalysis(String target) {
        this (target, Collections.emptyList());
    }

    public void addData(double target, String correlateName, double correlate, long time) {
        synchronized (correlationsByCorrelateName) {
            if (!correlationsByCorrelateName.containsKey(correlateName)) {
                correlationsByCorrelateName.put(correlateName, new Correlation(targetName, correlateName));
            }

            correlationsByCorrelateName.get(correlateName).addData(target, correlate, time);
        }
    }

    public void addTargetData(double target, long targetDataTime) {
        synchronized (correlationsByCorrelateName) {
            correlationsByCorrelateName.values().forEach(c -> {
                c.addTargetData(target, targetDataTime);
            });
        }
    }

    public void addCorrelateData(String correlateName, double correlate, long correlateDataTime) {
        synchronized (correlationsByCorrelateName) {
            if (!correlationsByCorrelateName.containsKey(correlateName)) {
                correlationsByCorrelateName.put(correlateName, new Correlation(targetName, correlateName));
            }

            correlationsByCorrelateName.get(correlateName).addCorrelateData(correlate, correlateDataTime);
        }
    }

    public final Map<String, Correlation> getCorrelationsByCorrelateName() {
        return ImmutableMap.copyOf(correlationsByCorrelateName);
    }

    public Map<String,Double> getCorrelationRanking() {
        Map<String,Double> correlationRanking = Maps.newHashMap();

        for (Map.Entry<String,Correlation> correlation : correlationsByCorrelateName.entrySet()) {
            correlationRanking.put(correlation.getKey(), correlation.getValue().getCorrelation());
        }

        // reset by sorting
        correlationRanking = correlationRanking.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return correlationRanking;
    }

    public String getTargetName() {
        return targetName;
    }

    public Correlation getTopCorrelate() {
        return correlationsByCorrelateName.get(getCorrelationRanking().keySet().iterator().next());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Target", targetName)
                .add("Correlates", correlationsByCorrelateName)
                .toString();
    }
}
