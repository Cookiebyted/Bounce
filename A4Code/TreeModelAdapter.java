/*
 *    ===============================================================================
 *    TreeModelAdapter.java : The TreeModelAdapter
 *    TreeModelAdapter
 *    UPI: myou129
 *    ===============================================================================
 */

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;

class TreeModelAdapter implements TreeModel {
    private Shape nestedShape;
    private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();

    public TreeModelAdapter(NestedShape root){
        this.nestedShape = root;
    }

    public Object getRoot(){
        return this.nestedShape;
    }

    public boolean isLeaf(Object node){
        if (node instanceof NestedShape){
            return false;
        }
        return true;
    }

    public Object getChild(Object parent, int index){
        if (parent instanceof NestedShape){
            NestedShape newShape = (NestedShape) parent;
            Shape[] childShapes = newShape.getChildren();
            if (index > childShapes.length){
                return null;
            }
            return childShapes[index];
        }
        return null;
    }

    public int getChildCount(Object parent){
        if (parent instanceof NestedShape){
            NestedShape newShape = (NestedShape) parent;
            return newShape.getChildren().length;
        }
        return 0;
    }

    public int getIndexOfChild(Object parent, Object child){
        if (parent instanceof NestedShape){
            NestedShape newShape = (NestedShape) parent;
            Shape[] children = newShape.getChildren();
            for (int i=0; i<children.length; i++){
                if (children[i].equals(child)){
                    return i;
                }
            }
        }
        return -1;
    }

    public void addTreeModelListener(final TreeModelListener modelListener) {
        treeModelListeners.add(modelListener);
    }

    public void removeTreeModelListener(final TreeModelListener modelListener){
        treeModelListeners.remove(modelListener);
    }

    protected void fireTreeStructureChanged(final Object source, final Object[] path, final int[] childIndices, final Object[] children){
        final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (final TreeModelListener l : treeModelListeners){
            l.treeStructureChanged(event);
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,int[] childIndices,Object[] children){
        final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (final TreeModelListener l : treeModelListeners){
            l.treeNodesInserted(event);
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children){
        final TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
        for (final TreeModelListener l : treeModelListeners){
            l.treeNodesRemoved(event);
        }
    }

    public void addToRoot(Shape s){
        NestedShape ns = (NestedShape) nestedShape;
        int[] numOfChildren = {ns.getSize()};
        ns.add(s);
        Object[] rootElement = {nestedShape};
        Object[] shapeElement = {s};

        fireTreeNodesInserted(this, rootElement, numOfChildren, shapeElement);
    }

    public boolean addNode(TreePath selectedPath, ShapeType currentShapeType){
        Object selectedNode = selectedPath.getLastPathComponent();
        if (!(selectedNode instanceof NestedShape)){
            return false;
        }
        NestedShape ns = (NestedShape) selectedNode;
        int numOfChildren = ns.getSize();
        ns.createAddInnerShape(currentShapeType);

        int[] childIndices = {numOfChildren};
        Object[] children = ns.getChildren();

        fireTreeNodesInserted(this, selectedPath.getPath(), childIndices, children);
        return true;
    }

    public boolean removeNodeFromParent(TreePath selectedPath){
        Object selectedNode = selectedPath.getLastPathComponent();
        if (selectedNode == nestedShape){
            return false;
        }

        Shape selectedShape = (Shape) selectedNode;
        NestedShape selectedNodeParent = selectedShape.getParent();
        int index = selectedNodeParent.indexOf(selectedShape);
        selectedNodeParent.remove(selectedShape);

        int[] childIndices = {index};
        Object[] selectedNodes = {selectedShape};

        fireTreeNodesRemoved(this, selectedPath.getParentPath().getPath(), childIndices, selectedNodes);
        return true;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}
    public void fireTreeNodesChanged(TreeModelEvent e) {}
}