package com.ca;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * @author jenshadlich@googlemail.com
 * @author jingyoko@gmail.com
 */
public class OperationResult {

    private final DescriptiveStatistics stats;

    public OperationResult(DescriptiveStatistics stats) {
        this.stats = stats;
    }

    public DescriptiveStatistics getStats() {
        return stats;
    }
}