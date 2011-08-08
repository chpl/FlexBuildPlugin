package me.platonov.builderTree;

import com.intellij.facet.Facet;
import com.intellij.facet.impl.DefaultFacetsProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author chaim
 * @since 8/8/11 - 4:49 PM
 */
public class BuildTreeToolWindow implements ToolWindowFactory {

    private JPanel thePanel;
    private JTree theTree;
    private JButton refreshProjectTreeButton;

    private Project myProject;

    final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
    final DefaultMutableTreeNode modulesNode = new DefaultMutableTreeNode("Modules");
    final DefaultMutableTreeNode facetsNode = new DefaultMutableTreeNode("Facets");
    final DefaultMutableTreeNode artifactsNode = new DefaultMutableTreeNode("Artifacts");
    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);

    public BuildTreeToolWindow() {
        refreshProjectTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProjectTree();
            }
        });
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        myProject = project;

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(thePanel, "", false);
        toolWindow.getContentManager().addContent(content);

        theTree.setCellRenderer(new IconTreeCellRenderer());

        rootNode.add(modulesNode);
        rootNode.add(facetsNode);
        rootNode.add(artifactsNode);
        theTree.setModel(treeModel);

        updateProjectTree();
    }

    private void updateProjectTree() {
        modulesNode.removeAllChildren();
        facetsNode.removeAllChildren();
        artifactsNode.removeAllChildren();

        Module[] modules = ModuleManager.getInstance(myProject).getModules();
        for (Module module : modules) {
            modulesNode.add(new DefaultMutableTreeNode(module));

            final Facet[] allFacets = DefaultFacetsProvider.INSTANCE.getAllFacets(module);
            for (Facet facet : allFacets) {
                facetsNode.add(new DefaultMutableTreeNode(facet));
            }
        }

        final Artifact[] artifacts = ArtifactManager.getInstance(myProject).getSortedArtifacts();
        for (Artifact artifact : artifacts) {
            artifactsNode.add(new DefaultMutableTreeNode(artifact));
        }

        treeModel.nodeStructureChanged(modulesNode);
        treeModel.nodeStructureChanged(facetsNode);
        treeModel.nodeStructureChanged(artifactsNode);
    }


}
