package org.b333vv.metric.model.visitor.method;

import com.intellij.psi.*;
import org.b333vv.metric.model.metric.value.Value;
import org.b333vv.metric.model.metric.Metric;
import org.b333vv.metric.model.metric.util.MethodUtils;

public class NumberOfLoopsVisitor extends JavaMethodVisitor {
    private long methodNestingDepth = 0;
    private long elementCount = 0;
    private long numberOfLoops = 0;

    @Override
    public void visitMethod(PsiMethod method) {
        metric = Metric.of("NOL", "Number Of Loops",
                "/html/NumberOfLoops.html", Value.UNDEFINED);
        if (methodNestingDepth == 0) {
            elementCount = 0;
        }
        methodNestingDepth++;
        super.visitMethod(method);
        methodNestingDepth--;
        if (methodNestingDepth == 0 && !MethodUtils.isAbstract(method)) {
            numberOfLoops = elementCount;
        }
        metric = Metric.of("NOL", "Number Of Loops",
                "/html/NumberOfLoops.html", numberOfLoops);
    }

    @Override
    public void visitForStatement(PsiForStatement statement) {
        super.visitForStatement(statement);
        elementCount++;
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        super.visitForeachStatement(statement);
        elementCount++;
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        super.visitDoWhileStatement(statement);
        elementCount++;
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        super.visitWhileStatement(statement);
        elementCount++;
    }
}
