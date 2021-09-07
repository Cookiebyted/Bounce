/*
 *    ==========================================================================================
 *    AnimationPanel.java : Moves shapes around on the screen according to different paths.
 *    It is the main drawing area where shapes are added and manipulated.
 *    It also contains a popup menu to clear all shapes.
 *    UPI: myou129
 *    ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;

public class AnimationViewer extends JComponent implements Runnable {
	private Thread animationThread = null;    // the thread for animation
    private static int DELAY = 20;         // the current animation speed
	private ArrayList<Shape> shapes = new ArrayList<Shape>(); //create the ArrayList to store shapes
	private Painter painter = new GraphicsPainter();
	private ShapeType currentShapeType=Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType=Shape.DEFAULT_PATHTYPE;  // the current path type
	private Color currentBorderColor=Shape.DEFAULT_BORDER_COLOR, currentFillColor=Shape.DEFAULT_FILL_COLOR;  // the current fill colour of a shape
	private int marginWidth=Shape.DEFAULT_MARGIN_WIDTH, marginHeight = Shape.DEFAULT_MARGIN_HEIGHT, currentWidth=Shape.DEFAULT_WIDTH, currentHeight=Shape.DEFAULT_HEIGHT;
    private String currentText=Shape.DEFAULT_TEXT;

	private NestedShape root;
	private TreeModelAdapter treeModelAdapter;
	private TableModelAdapter tableModelAdapter;


     /** Constructor of the AnimationPanel */
	 public AnimationViewer(boolean isGraphicsVersion) {
		 if (isGraphicsVersion) {
			 start();
			 addMouseListener(new MyMouseAdapter());

			 root = new NestedShape(shapes, currentFillColor, currentBorderColor);
			 treeModelAdapter  = new TreeModelAdapter(root);
			 tableModelAdapter  = new TableModelAdapter(root);

		 }
	 }
    /** create a new shape
     * @param x     the x-coordinate of the mouse position
     * @param y    the y-coordinate of the mouse position */

  	//modify this
	protected void createNewShape(int x, int y) {
		switch (currentShapeType) {
			case RECTANGLE: {
				treeModelAdapter.addToRoot( new RectangleShape(x, y,currentWidth,currentHeight,marginWidth,marginHeight,currentBorderColor,currentFillColor,currentPathType,currentText) );
				tableModelAdapter.fireTableDataChanged();
				break;
			} case XRECTANGLE: {
				treeModelAdapter.addToRoot( new XRectangleShape(x, y,currentWidth,currentHeight,marginWidth,marginHeight,currentBorderColor,currentFillColor,currentPathType,currentText ) );
				tableModelAdapter.fireTableDataChanged();
				break;
			} case OVAL: {
				treeModelAdapter.addToRoot(new OvalShape(x,y,currentWidth,currentHeight,marginWidth,marginHeight,currentBorderColor,currentFillColor,currentPathType,currentText));
				tableModelAdapter.fireTableDataChanged();
				break;
			} case SQUARE: {
				treeModelAdapter.addToRoot(new SquareShape(x,y,Math.min(currentWidth,currentHeight),marginWidth,marginHeight,currentBorderColor,currentFillColor,currentPathType,currentText));
				tableModelAdapter.fireTableDataChanged();
				break;
			} case NESTED: {
				treeModelAdapter.addToRoot(new NestedShape(x,y,currentWidth,currentHeight,marginWidth,marginHeight,currentBorderColor,currentFillColor,currentPathType,currentText));
				tableModelAdapter.fireTableDataChanged();
				break;
			}
		}
	}
    /**    move and paint all shapes within the animation area
     * @param g    the Graphics control */
    public void paintComponent(Graphics g) {
		painter.setGraphics(g);
		super.paintComponent(g);
        for (Shape currentShape: root.getChildren()) {
            currentShape.move();
		    currentShape.draw(painter);
		    currentShape.drawHandles(painter);
		    currentShape.drawCenteredText(painter);
			tableModelAdapter.fireTableDataChanged();
		}
    }

	public String getCurrentText() { return currentText; }
    /** set the current text for all currently selected shapes
     * @param fc    the text value */
    public void setCurrentText(String fc) {
        currentText = fc;
		for (Shape currentShape: shapes)
			if ( currentShape.isSelected())
				currentShape.setText(currentText);
    }
    public ShapeType getCurrentShapeType() { return currentShapeType; }
    public void setCurrentShapeType(int st) {
		currentShapeType = ShapeType.getShapeType(st);
	}
	public PathType getCurrentPathType() { return currentPathType; }
	public void setCurrentPathType(int pt) {
		currentPathType = PathType.getPathType(pt);
	}
	/** get the current width
	 * @return currentWidth - the width value */
	public int getCurrentWidth() { return currentWidth; }
	/** set the current width and the width for all currently selected shapes
	 * @param w	the new width value */
	public void setCurrentWidth(int w) {
		currentWidth = w;
		for (Shape currentShape: shapes)
			if ( currentShape.isSelected())
				currentShape.setWidth(currentWidth);
	}
	/** get the current height
	 * @return currentHeight - the height value */
	public int getCurrentHeight() { return currentHeight; }
	/** set the current height and the height for all currently selected shapes
	 * @param h	the new height value */
	public void setCurrentHeight(int h) {
		currentHeight = h;
		for (Shape currentShape: shapes)
			if ( currentShape.isSelected())
				currentShape.setHeight(currentHeight);
	}
	/** get the current border colour
	 * @return currentBorderColor - the border colour value */
	public Color getCurrentBorderColor() { return currentBorderColor; }
 	/** set the current border colour and the border colour for all currently selected shapes
	 * @param bc	the new border colour value */
	public void setCurrentBorderColor(Color bc) {
		currentBorderColor = bc;
		for (Shape currentShape: shapes)
			if ( currentShape.isSelected())
				currentShape.setBorderColor(currentBorderColor);
	}
	/** get the current fill colour
	 * @return currentFillColor - the fill colour value */
	public Color getCurrentFillColor() { return currentFillColor; }
    /** set the current fill colour and the border colour for all currently selected shapes
     * @param fc    the new fill colour value */
    public void setCurrentFillColor(Color fc) {
        currentFillColor = fc;
        //complete this
		for (Shape currentShape: shapes)
			if ( currentShape.isSelected())
				currentShape.setFillColor(currentFillColor);
    }
    class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked( MouseEvent e ) {
			boolean found = false;
			for (Shape currentShape: shapes)
				if ( currentShape.contains( e.getPoint()) ) { // if the mousepoint is within a shape, then set the shape to be selected/deselected
					currentShape.setSelected( ! currentShape.isSelected() );
					found = true;
				}
			if (!found) createNewShape(e.getX(), e.getY());
		}
	}

    /**    update the painting area
     * @param g    the graphics control */
    public void update(Graphics g){ paint(g); }
    public void resetMarginSize() {
        marginWidth = getWidth();
        marginHeight = getHeight() ;
        for (Shape currentShape: shapes)
			currentShape.setMarginSize(marginWidth,marginHeight );
    }
    /**    When the "start" button is pressed, start the thread  */
    public void start() {
        animationThread = new Thread(this);
        animationThread.start();
    }
    /**    When the "stop" button is pressed, stop the thread */
    public void stop() {
        if (animationThread != null) {
            animationThread = null;
        }
    }
    /** run the animation */
    public void run() {
        Thread myThread = Thread.currentThread();
        while(animationThread==myThread) {
            repaint();
            pause(DELAY);
        }
    }
    /** Sleep for the specified amount of time */
    private void pause(int milliseconds) {
        try {
            Thread.sleep((long)milliseconds);
        } catch(InterruptedException ie) {}
    }

	public boolean addShape(TreePath selectedPath){
		if (treeModelAdapter.addNode(selectedPath, currentShapeType)){
			tableModelAdapter.fireTableDataChanged();
			return true;
		} else {
			return false;
		}
	}

	public TableModelAdapter getTableModelAdapter(){
		return tableModelAdapter;
	}

	public TreeModelAdapter getTreeModelAdapter(){
    	return treeModelAdapter;
	}

	public boolean removeShape(TreePath selectedPath){
		if (treeModelAdapter.removeNodeFromParent(selectedPath)){
			tableModelAdapter.fireTableDataChanged();
			return true;
		} else {
			return false;
		}
	}
}
