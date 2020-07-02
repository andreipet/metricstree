/*
 * Copyright 2020 b333vv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.b333vv.metric.exec;

import com.intellij.util.messages.Topic;
import org.b333vv.metric.model.code.JavaClass;
import org.b333vv.metric.model.metric.Metric;
import org.b333vv.metric.model.metric.MetricType;
import org.b333vv.metric.ui.profile.MetricProfile;
import org.b333vv.metric.ui.tree.builder.ProjectMetricTreeBuilder;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XYChart;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.b333vv.metric.ui.chart.builder.MetricPieChartBuilder.PieChartStructure;

public interface MetricsEventListener {
    Topic<MetricsEventListener> TOPIC = new Topic<>("MetricsEventListener", MetricsEventListener.class);

    default void projectMetricsCalculated(ProjectMetricTreeBuilder projectMetricTreeBuilder, @NotNull DefaultTreeModel defaultTreeModel) {
    }

    default void classesSortedByMetricsValues(@NotNull DefaultTreeModel defaultTreeModel) {
    }

    default void metricsChartBuilt(Set<MetricType> metricTypes, @NotNull CategoryChart categoryChart) {
    }

    default void projectMetricsChartBuilt(@NotNull XYChart xyChart, Map<String, Double> instability, Map<String, Double> abstractness) {
    }

    default void metricsByMetricTypesChartBuilt(@NotNull List<PieChartStructure> chartList, Map<MetricType, Map<JavaClass, Metric>> classesByMetricTypes) {
    }

    default void classMetricsValuesEvolutionCalculated(@NotNull DefaultTreeModel defaultTreeModel) {
    }

    default void clearProjectMetricsTree() {
    }

    default void clearChartsPanel() {
    }

    default void clearProfilesPanel() {
    }

    default void clearClassMetricsValuesEvolutionTree() {
    }

    default void buildClassMetricsTree() {
    }

    default void buildProjectMetricsTree() {
    }

    default void showClassMetricsTree(boolean showClassMetricsTree) {
    }

    default void refreshClassMetricsTree() {
    }

    default void cancelMetricsValuesEvolutionCalculation() {
    }

    default void metricsProfileBuilt(@NotNull Map<MetricProfile, Set<JavaClass>> distribution) {
    }

    default void metricsProfileSelected(MetricProfile profile) {
    }

    default void javaClassSelected(JavaClass javaClass) {
    }
}
