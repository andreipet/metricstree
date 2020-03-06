package org.b333vv.metric.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.DumbServiceImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.concurrency.AppExecutorUtil;
import org.b333vv.metric.ui.tool.ClassMetricsPanel;
import org.b333vv.metric.ui.tool.ProjectMetricsPanel;
import org.b333vv.metric.ui.tree.MetricsTreeFilter;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class MetricsUtils {

    private static final Logger LOG = Logger.getInstance(MetricsUtils.class);
    private static Project project;
    private static final MetricsTreeFilter classMetricsTreeFilter = new MetricsTreeFilter();
    private static final MetricsTreeFilter projectMetricsTreeFilter = new MetricsTreeFilter();
    private static ClassMetricsPanel classMetricsPanel;
    private static ProjectMetricsPanel projectMetricsPanel;
    private static boolean autoScrollable = true;

    private MetricsUtils() {
        // Utility class
    }

    public static <T> T get(ComponentManager container, Class<T> clazz) {
        T t = container.getComponent(clazz);
        if (t == null) {
            LOG.error("Could not find class in container: " + clazz.getName());
            throw new IllegalArgumentException("Class not found: " + clazz.getName());
        }
        return t;
    }

    public static <T> T get(Class<T> clazz) {
        return get(ApplicationManager.getApplication(), clazz);
    }

    @CheckForNull
    public static VirtualFile getSelectedFile(Project project) {
        if (project.isDisposed()) {
            return null;
        }
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        Editor editor = editorManager.getSelectedTextEditor();
        if (editor != null) {
            Document doc = editor.getDocument();
            FileDocumentManager docManager = FileDocumentManager.getInstance();
            return docManager.getFile(doc);
        }
        return null;
    }

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project value) {
        project = value;
    }

    public static DumbService getDumbService() {
        return DumbServiceImpl.getInstance(project);
    }

    public static MetricsTreeFilter getClassMetricsTreeFilter() {
        return classMetricsTreeFilter;
    }

    public static MetricsTreeFilter getProjectMetricsTreeFilter() {
        return projectMetricsTreeFilter;
    }

    public static ClassMetricsPanel getClassMetricsPanel() {
        return classMetricsPanel;
    }

    public static void setClassMetricsPanel(ClassMetricsPanel value) {
        classMetricsPanel = value;
    }

    public static <T> T callInReadAction(Callable<T> task) {
        T result = null;
        try {
            result = ReadAction.nonBlocking(task)
                    .inSmartMode(getProject())
                    .submit(AppExecutorUtil.getAppExecutorService())
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            classMetricsPanel.getConsole().error(e.getMessage());
        }
        return result;
    }

    public static void runInReadAction(Runnable task) {
        try {
            ReadAction.nonBlocking(task)
                    .inSmartMode(getProject())
                    .submit(AppExecutorUtil.getAppExecutorService());
        } catch (Exception e) {
            classMetricsPanel.getConsole().error(e.getMessage());
        }
    }

    public static void refreshMetricsTree() {
        classMetricsPanel.refresh();
    }

    public static void setProjectMetricsPanel(ProjectMetricsPanel value) {
        projectMetricsPanel = value;
    }

    public static ProjectMetricsPanel getProjectMetricsPanel() {
        return projectMetricsPanel;
    }

    public static void calculateProjectMetrics() {
        projectMetricsPanel.calculateMetrics();
    }

    public static boolean isAutoScrollable() {
        return autoScrollable;
    }

    public static void setAutoScrollable(boolean autoScrollable) {
        MetricsUtils.autoScrollable = autoScrollable;
    }

    @Nullable
    private static VirtualFile getVirtualFile(PsiElement psiElement) {
        if (psiElement == null) {
            return null;
        }
        final PsiFile containingFile = psiElement.getContainingFile();
        if (containingFile == null) {
            return null;
        }
        return containingFile.getVirtualFile();
    }

    public static boolean isElementInSelectedFile(Project project,
                                                  PsiElement psiElement) {
        final VirtualFile elementFile = getVirtualFile(psiElement);
        if (elementFile == null) {
            return false;
        }
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        final VirtualFile[] currentEditedFiles = fileEditorManager.getSelectedFiles();

        for (final VirtualFile file : currentEditedFiles) {
            if (elementFile.equals(file)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Editor getEditorIfSelected(Project project, PsiElement psiElement) {
        final VirtualFile elementFile = getVirtualFile(psiElement);
        if (elementFile == null) {
            return null;
        }
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        final FileEditor fileEditor = fileEditorManager.getSelectedEditor(elementFile);
        Editor editor = null;
        if (fileEditor instanceof TextEditor) {
            editor = ((TextEditor) fileEditor).getEditor();
        }
        return editor;
    }
}
