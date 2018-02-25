package com.ca.operations;

import com.ca.OperationResult;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.concurrent.Callable;

/**
 * @author jenshadlich@googlemail.com
 * @author jingyoko@gmail.com
 */
public abstract class AbstractOperation implements Callable<OperationResult> {

    private final DescriptiveStatistics stats = new DescriptiveStatistics();

    protected DescriptiveStatistics getStats() {
        return stats;
    }

}
