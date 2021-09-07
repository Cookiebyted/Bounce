/*
 *    ===============================================================================
 *    TableModelAdapter .java : The TableModelAdapter
 *    TableModelAdapter
 *    UPI: myou129
 *    ===============================================================================
 */

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;

class TableModelAdapter extends AbstractTableModel {
    private Shape nestedShape;
    private static String[] columnNames = new String[]{"Type", "X-pos", "Y-pos", "Width", "Height"};

    public TableModelAdapter(NestedShape ns){
        nestedShape = ns;
    }

    public int getColumnCount(){
        return columnNames.length;
    }

    public int getRowCount(){
        int rowCount = 1;

        if(nestedShape instanceof NestedShape) {
            NestedShape ns = (NestedShape) nestedShape;
            rowCount = ns.getChildren().length;
        }
        return rowCount;
    }

    public String getColumnName(int column){
        return columnNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex){
        Shape currentShape = nestedShape;
        Object output = null;

        if(nestedShape instanceof NestedShape) {
            NestedShape ns = (NestedShape) nestedShape;
            currentShape = ns.getShapeAt(rowIndex);
        }

        switch(columnIndex){
            case 0:
                //Return type of an inner shape
                output = currentShape.getClass().getName();
                break;
            case 1:
                //Return x position of an inner shape
                output = currentShape.getX();
                break;
            case 2:
                //Return y position of an inner shape
                output = currentShape.getY();
                break;
            case 3:
                //Return width of an inner shape
                output = currentShape.getWidth();
                break;
            case 4:
                //Return height of an inner shape
                output = currentShape.getHeight();
                break;
        }
        return output;
    }

    public void setNestedShape(Shape s){
        nestedShape = s;
    }

}