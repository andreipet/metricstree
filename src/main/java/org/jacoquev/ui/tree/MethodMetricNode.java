package org.jacoquev.ui.tree;

import org.jacoquev.model.metric.Metric;
import org.jacoquev.util.MetricsIcons;

import javax.swing.*;

public class MethodMetricNode extends MetricNode {
    public MethodMetricNode(Metric metric) {
        super(metric);
    }

    protected Icon getIcon() {

//        return AllIcons.Nodes.Method;
        return MetricsIcons.METHOD_METRIC;
    }
}
