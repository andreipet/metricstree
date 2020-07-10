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

package org.b333vv.metric.task;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.b333vv.metric.event.MetricsEventListener;
import org.b333vv.metric.builder.ClassAndMethodsMetricsCalculator;
import org.b333vv.metric.model.code.JavaProject;
import org.b333vv.metric.util.MetricsUtils;
import org.jetbrains.annotations.NotNull;

import static org.b333vv.metric.task.MetricTaskManager.sureDependenciesAreInCache;

public class ClassAndMethodMetricTask extends Task.Backgroundable {
    private static final String GET_FROM_CACHE_MESSAGE = "Try to get class and method levels metrics from cache";
    private static final String STARTED_MESSAGE = "Building class and method levels metrics";
    private static final String FINISHED_MESSAGE = "Building class and method levels metrics finished";
    private static final String CANCELED_MESSAGE = "Building class and method levels metrics canceled";

    public ClassAndMethodMetricTask() {
        super(MetricsUtils.getCurrentProject(), "Calculating Class And Method Metrics");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        sureDependenciesAreInCache(indicator);
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(GET_FROM_CACHE_MESSAGE);
        JavaProject javaProject = MetricTaskCache.instance().getUserData(MetricTaskCache.CLASS_AND_METHODS_METRICS);
        if (javaProject == null) {
            myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(STARTED_MESSAGE);
            AnalysisScope scope = new AnalysisScope(MetricsUtils.getCurrentProject());
            scope.setIncludeTestSource(false);
            javaProject = new JavaProject(MetricsUtils.getCurrentProject().getName());
            ClassAndMethodsMetricsCalculator metricsCalculator = new ClassAndMethodsMetricsCalculator(scope, javaProject);
            metricsCalculator.calculateMetrics();
            MetricTaskCache.instance().putUserData(MetricTaskCache.CLASS_AND_METHODS_METRICS, javaProject);
        }
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(FINISHED_MESSAGE);
    }

    @Override
    public void onCancel() {
        super.onCancel();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(CANCELED_MESSAGE);
    }
}
