package me.platonov.builderTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @author chaim
 * @since 8/8/11 - 4:56 PM
 */
public class CheckboxTreeCellRenderer implements TreeCellRenderer {
    private  DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();

    private JCheckBox checkBox = new JCheckBox();

    Color selectionBorderColor, selectionForeground, selectionBackground,
        textForeground, textBackground;

    public CheckboxTreeCellRenderer() {
        Font fontValue;
        fontValue = UIManager.getFont("Tree.font");
        if (fontValue != null) {
          checkBox.setFont(fontValue);
        }
        Boolean drawsFocusBorderAroundIcon = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        checkBox.setFocusPainted(drawsFocusBorderAroundIcon != null && drawsFocusBorderAroundIcon);

        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (leaf) {
            String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
            checkBox.setText(stringValue);
            checkBox.setSelected(false);

            checkBox.setEnabled(tree.isEnabled());

            if (selected) {
              checkBox.setForeground(selectionForeground);
              checkBox.setBackground(selectionBackground);
            } else {
              checkBox.setForeground(textForeground);
              checkBox.setBackground(textBackground);
            }

            return checkBox;
        } else {
            return defaultTreeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
    }
}
