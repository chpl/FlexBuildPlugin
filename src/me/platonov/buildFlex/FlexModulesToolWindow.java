package me.platonov.buildFlex;

import com.intellij.facet.Facet;
import com.intellij.facet.impl.DefaultFacetsProvider;
import com.intellij.lang.javascript.flex.FlexFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author chaim
 * @since 8/8/11 - 4:49 PM
 */
public class FlexModulesToolWindow implements ToolWindowFactory {

    private JPanel thePanel;
    private JButton refreshProjectTreeButton;
    private JTable theTable;
    private JCheckBox selectAllCheckBox;

    private Project myProject;

    FlexModulesTableModel model;

    public FlexModulesToolWindow() {
        model = new FlexModulesTableModel();

        refreshProjectTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFacets();
            }
        });

        selectAllCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectAllCheckBox.isSelected()) model.selectAll();
                else model.selectNone();
            }
        });

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                selectAllCheckBox.setSelected(model.isAllSelected());
            }
        });
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        myProject = project;

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(thePanel, "", false);
        toolWindow.getContentManager().addContent(content);


        theTable.setAutoCreateColumnsFromModel(true);
        theTable.setAutoscrolls(true);

        theTable.setTableHeader(new JTableHeader());
        theTable.setModel(model);

        for (int i = 0; i < theTable.getColumnModel().getColumnCount(); i++) {
            TableColumn column = theTable.getColumnModel().getColumn(i);
            if (null != FlexModulesTableModel.columnMaxWidth[i]) {
                column.setMaxWidth(FlexModulesTableModel.columnMaxWidth[i]);
            }
        }

        updateFacets();
    }

    private void updateFacets() {
        model.clear();

        Module[] modules = ModuleManager.getInstance(myProject).getModules();
        for (Module module : modules) {
            final Facet[] allFacets = DefaultFacetsProvider.INSTANCE.getAllFacets(module);
            for (Facet facet : allFacets) {
                if (facet instanceof FlexFacet) {
                    model.add((FlexFacet) facet);
                }
            }
        }

        model.sort();
    }


}
