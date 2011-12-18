package me.platonov.buildFlex;

import com.intellij.lang.javascript.flex.FlexFacet;
import com.intellij.lang.javascript.flex.FlexFacetConfigurationImpl;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author chaim
 * @since 8/11/11 3:30 PM
 */
public class FlexModulesTableModel implements TableModel {

    protected List<FlexFacet> list = new ArrayList<FlexFacet>();
    protected EventListenerList listenerList = new EventListenerList();

    public static final String[] columns = {/*icon*/"", "Name", "Build"};
    private static final Class<?>[] columnTypes = {/*icon*/Icon.class, String.class, Boolean.class};
    public static final Integer[] columnMaxWidth = {25, null, 25};

    public void add(FlexFacet item) {
        list.add(item);
        fireTableChanged(new TableModelEvent(this, list.indexOf(item)));
    }

    public void clear() {
        list.clear();
        fireTableChanged(new TableModelEvent(this));
    }

    public void sort() {
        Collections.sort(list, new Comparator<FlexFacet>() {
            @Override
            public int compare(FlexFacet o1, FlexFacet o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 == null) return -1;
                if (o2 == null) return 1;

                return o1.getModule().getName().compareToIgnoreCase(o2.getModule().getName());
            }
        }
        );
        fireTableChanged(new TableModelEvent(this));
    }

    public boolean isAllSelected() {
        boolean result = true;
        for (FlexFacet flexFacet : list) {
            final FlexFacetConfigurationImpl configuration = (FlexFacetConfigurationImpl) flexFacet.getConfiguration();
            result = result && configuration.getFlexBuildConfiguration().DO_BUILD;
        }

        return result;
    }

    public void selectAll() {
        for (FlexFacet flexFacet : list) {
            final FlexFacetConfigurationImpl configuration = (FlexFacetConfigurationImpl) flexFacet.getConfiguration();
            configuration.getFlexBuildConfiguration().DO_BUILD = true;
        }
        fireTableChanged(new TableModelEvent(this));
    }

    public void selectNone() {
        for (FlexFacet flexFacet : list) {
            final FlexFacetConfigurationImpl configuration = (FlexFacetConfigurationImpl) flexFacet.getConfiguration();
            configuration.getFlexBuildConfiguration().DO_BUILD = false;
        }
        fireTableChanged(new TableModelEvent(this));
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnTypes[columnIndex] == Boolean.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final FlexFacet flexFacet = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return flexFacet.getType().getIcon();
            case 1:
                return flexFacet.getModule().getName();
            case 2:
                final FlexFacetConfigurationImpl configuration = (FlexFacetConfigurationImpl) flexFacet.getConfiguration();
                return configuration.getFlexBuildConfiguration().DO_BUILD;
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (2 == columnIndex) {
            final FlexFacet flexFacet = list.get(rowIndex);
            final FlexFacetConfigurationImpl configuration = (FlexFacetConfigurationImpl) flexFacet.getConfiguration();
            configuration.getFlexBuildConfiguration().DO_BUILD = ((Boolean) aValue);
            fireTableChanged(new TableModelEvent(this, rowIndex));
        } else {
            throw new IllegalArgumentException("Can't update this column");
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public void fireTableChanged(TableModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(e);
            }
        }
    }

}
