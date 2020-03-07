package org.b333vv.metric.ui.tool;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiJavaFile;
import org.b333vv.metric.model.builder.ClassModelBuilder;
import org.b333vv.metric.model.code.JavaProject;
import org.b333vv.metric.ui.tree.builder.ClassMetricTreeBuilder;
import org.b333vv.metric.util.CurrentFileController;
import org.b333vv.metric.util.MetricsService;
import org.b333vv.metric.util.MetricsUtils;
import org.jetbrains.annotations.NotNull;

public class ClassMetricsPanel extends MetricsTreePanel {

    public ClassMetricsPanel(Project project) {
        super(project, "Metrics.ClassMetricsToolbar");

        this.scope = new CurrentFileController(project);
        MetricsUtils.setClassMetricsPanel(this);
        subscribeToEvents();
    }

    protected void subscribeToEvents() {
        scope.setPanel(this);
    }

    public void update(@NotNull PsiJavaFile file) {
            psiJavaFile = file;
        if (MetricsService.isShowClassMetricsTree()) {
            MetricsUtils.getDumbService().runWhenSmart(() -> calculateMetrics(file));
        }
    }

    public void refresh() {
        if (psiJavaFile != null && MetricsService.isShowClassMetricsTree()) {
            MetricsUtils.getDumbService().runWhenSmart(() -> calculateMetrics(psiJavaFile));
        }
    }

    private void calculateMetrics(@NotNull PsiJavaFile psiJavaFile) {
        ClassModelBuilder classModelBuilder = new ClassModelBuilder();
        JavaProject javaProject = classModelBuilder.buildJavaProject(psiJavaFile);
        metricTreeBuilder = new ClassMetricTreeBuilder(javaProject);
        buildTreeModel();
        console.info("Built metrics tree for " + psiJavaFile.getName());
    }
}
