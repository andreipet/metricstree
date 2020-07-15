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

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import org.b333vv.metric.event.MetricsEventListener;
import org.b333vv.metric.model.code.JavaClass;
import org.b333vv.metric.ui.chart.builder.ProfileCategoryChartBuilder;
import org.b333vv.metric.ui.chart.builder.ProfileHeatMapChartBuilder;
import org.b333vv.metric.ui.profile.MetricProfile;
import org.b333vv.metric.util.MetricsUtils;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.HeatMapChart;

import java.util.Map;
import java.util.Set;

import static org.b333vv.metric.task.MetricTaskManager.getMetricProfilesDistribution;

public class ProfilesCategoryChartTask extends Task.Backgroundable {
    private static final String GET_FROM_CACHE_MESSAGE = "Try to get distribution of classes by metric profiles chart from cache";
    private static final String STARTED_MESSAGE = "Building distribution of classes by metric profiles chart started";
    private static final String FINISHED_MESSAGE = "Building distribution of classes by metric profiles chart finished";
    private static final String CANCELED_MESSAGE = "Building distribution of classes by metric profiles chart canceled";

    public ProfilesCategoryChartTask() {
        super(MetricsUtils.getCurrentProject(), "Build Distribution Of Classes By Metric Profiles Chart");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(GET_FROM_CACHE_MESSAGE);
        CategoryChart categoryChart = MetricTaskCache.instance().getUserData(MetricTaskCache.PROFILE_CATEGORY_CHART);
        if (categoryChart == null) {
            myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(STARTED_MESSAGE);
            Map<MetricProfile, Set<JavaClass>> classesByMetricProfile = getMetricProfilesDistribution(indicator);
            ProfileCategoryChartBuilder profileCategoryChartBuilder = new ProfileCategoryChartBuilder();
            categoryChart = profileCategoryChartBuilder.createChart(classesByMetricProfile);
            MetricTaskCache.instance().putUserData(MetricTaskCache.PROFILE_CATEGORY_CHART, categoryChart);
        }

    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(FINISHED_MESSAGE);
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).profilesCategoryChartIsReady();
    }

    @Override
    public void onCancel() {
        super.onCancel();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(CANCELED_MESSAGE);
    }
}
