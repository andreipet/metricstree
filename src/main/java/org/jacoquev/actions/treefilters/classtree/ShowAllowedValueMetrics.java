package org.jacoquev.actions.treefilters.classtree;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jacoquev.util.MetricsUtils;
import org.jetbrains.annotations.NotNull;

public class ShowAllowedValueMetrics extends ToggleAction {

    public ShowAllowedValueMetrics() {
        super("Show Metrics with Allowed Values", "Show or dont show metrics with allowed values",
                AllIcons.Actions.Commit);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return MetricsUtils.getClassMetricsTreeFilter().isAllowedValueMetricsVisible();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        MetricsUtils.getClassMetricsTreeFilter().setAllowedValueMetricsVisible(state);
        MetricsUtils.getClassMetricsPanel().buildTreeModel();
    }
}
