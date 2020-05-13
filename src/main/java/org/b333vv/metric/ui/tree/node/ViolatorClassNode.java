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

package org.b333vv.metric.ui.tree.node;

import com.intellij.ui.SimpleTextAttributes;
import org.b333vv.metric.model.code.JavaClass;
import org.b333vv.metric.model.metric.Metric;
import org.b333vv.metric.model.metric.value.Value;
import org.b333vv.metric.ui.tree.TreeCellRenderer;

public class ViolatorClassNode extends ClassNode {

    private final Metric metric;

    public ViolatorClassNode(JavaClass javaClass, Metric metric) {
        super(javaClass);
        this.metric = metric;
    }

    public JavaClass getJavaClass() {
        return javaClass;
    }

    private String getDelta() {
        String delta = "";
        if (metric.getValue() != Value.UNDEFINED) {
            if (metric.getValue().isLessThan(metric.getRange().getFrom())) {
                delta += " [-" + metric.getRange().getFrom().minus(metric.getValue()) + "]";
            }
            if (metric.getValue().isGreaterThan(metric.getRange().getTo())) {
                delta += " [+" + metric.getValue().minus(metric.getRange().getTo()) + "]";
            }
        }
        return delta;
    }

    @Override
    public void render(TreeCellRenderer renderer) {
        renderer.setIcon(getIcon());
        renderer.append(javaClass.getName());
        renderer.append(": " + metric.getFormattedValue());
        renderer.append(getDelta(), SimpleTextAttributes.ERROR_ATTRIBUTES);
    }
}
