package me.platonov.builderTree;

import com.intellij.facet.Facet;
import com.intellij.openapi.module.Module;
import com.intellij.packaging.artifacts.Artifact;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author chaim
 * @since 8/8/11 - 6:10 PM
 */
public class IconTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (leaf) {
            final Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof Module) {
                Module module = (Module) userObject;
                this.setText(module.getName() + " (" + module.getModuleType().getName() + ")");
                this.setIcon(module.getModuleType().getNodeIcon(false));
            } else if (userObject instanceof Facet) {
                Facet facet = (Facet) userObject;
                this.setText(facet.getModule().getName() + " (" + facet.getName() + ")");
                this.setIcon(facet.getType().getIcon());
            }
            if (userObject instanceof Artifact) {
                Artifact artifact = (Artifact) userObject;

                this.setText(artifact.getName() + " (" + artifact.getArtifactType().getPresentableName() + ")");
                this.setIcon(artifact.getArtifactType().getIcon());
            }
        }

        return this;
    }
}
