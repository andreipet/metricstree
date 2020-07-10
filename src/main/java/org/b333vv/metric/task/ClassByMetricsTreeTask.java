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
import org.b333vv.metric.model.code.JavaProject;
import org.b333vv.metric.ui.tree.builder.SortedByMetricsValuesClassesTreeBuilder;
import org.b333vv.metric.util.MetricsUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultTreeModel;

import static org.b333vv.metric.task.MetricTaskManager.getClassAndMethodModel;

public class ClassByMetricsTreeTask extends Task.Backgroundable {
    private static final String GET_FROM_CACHE_MESSAGE = "Try to get classes distribution by metric values tree from cache";
    private static final String STARTED_MESSAGE = "Building classes distribution by metric values tree started";
    private static final String FINISHED_MESSAGE = "Building classes distribution by metric values tree finished";
    private static final String CANCELED_MESSAGE = "Building classes distribution by metric values tree canceled";

    public ClassByMetricsTreeTask() {
        super(MetricsUtils.getCurrentProject(), "Building Class Distribution by Metric Values");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(GET_FROM_CACHE_MESSAGE);
        DefaultTreeModel treeModel = MetricTaskCache.instance().getUserData(MetricTaskCache.CLASSES_BY_METRIC_TREE);
        if (treeModel == null) {
            myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(STARTED_MESSAGE);
            JavaProject javaProject = getClassAndMethodModel(indicator);
            SortedByMetricsValuesClassesTreeBuilder builder = new SortedByMetricsValuesClassesTreeBuilder();
            treeModel = builder.createMetricTreeModel(javaProject);
            MetricTaskCache.instance().putUserData(MetricTaskCache.CLASSES_BY_METRIC_TREE, treeModel);
        }
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(FINISHED_MESSAGE);
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).classByMetricTreeIsReady();
    }

    @Override
    public void onCancel() {
        super.onCancel();
        myProject.getMessageBus().syncPublisher(MetricsEventListener.TOPIC).printInfo(CANCELED_MESSAGE);
    }
}
