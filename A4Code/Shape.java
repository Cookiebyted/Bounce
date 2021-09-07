/*
 *    ===============================================================================
 *    Shape.java : The superclass of all shapes.
 *    A shape defines various properties, including selected, colour, width and height.
 *    UPI: myou129
 *    ===============================================================================
 */

import java.awt.*;
public abstract class Shape {
    public static final PathType DEFAULT_PATHTYPE = PathType.BOUNCE;
    public static final ShapeType DEFAULT_SHAPETYPE = ShapeType.RECTANGLE;
    public static final int DEFAULT_X = 0, DEFAULT_Y = 0, DEFAULT_WIDTH=100, DEFAULT_HEIGHT=50, DEFAULT_MARGIN_WIDTH=600, DEFAULT_MARGIN_HEIGHT=800;
    public static final Color DEFAULT_FILL_COLOR=Color.orange, DEFAULT_BORDER_COLOR=Color.blue;
    public static final String DEFAULT_TEXT = "A4";

    public int x, y, width, height, marginWidth, marginHeight;
    protected MovingPath path;            // the moving path
    protected Color borderColor, fillColor; // the border colour
    protected boolean selected = false;    // draw handles if selected
    protected String text = DEFAULT_TEXT;
	protected NestedShape parent;

    /** default constructor to create a shape with default values */
    public Shape() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MARGIN_WIDTH, DEFAULT_MARGIN_HEIGHT, DEFAULT_BORDER_COLOR,DEFAULT_FILL_COLOR, DEFAULT_PATHTYPE); // the default properties
    }
    /** constructor to create a shape with default values for square */
    public Shape(int size) {
        this(DEFAULT_X, DEFAULT_Y, size, size, DEFAULT_MARGIN_WIDTH, DEFAULT_MARGIN_HEIGHT, DEFAULT_BORDER_COLOR,DEFAULT_FILL_COLOR, DEFAULT_PATHTYPE); // the default properties
    }
    /** constructor to create a shape
     * @param x         the x-coordinate of the new shape
     * @param y        the y-coordinate of the new shape
     * @param w         the width of the new shape
     * @param h         the height of the new shape
     * @param mw         the margin width of the animation panel
     * @param mh        the margin height of the animation panel
     * @param bc        the bordercolour of the new shape
     * @param fc         the fillcolour of the new shape
     * @param pt         the pathtype of the new shape **/

    public Shape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        marginWidth = mw;
        marginHeight = mh;
        borderColor = bc;
        fillColor = fc;
        setPath(pt);
    }
    public Shape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt, String text) {
        this(x,y,w,h,mw,mh,bc,fc,pt);
        this.text = text;
    }
	public String getText() { return text; }
	public void setText(String t) { text = t; }
	public void drawCenteredText(Painter g) {
		g.drawCenteredText(text, x, y, width, height);
	}
    public String toString() {
        return String.format("[%s:(%d,%d) %d x %d %s]", this.getClass().getName(), x,y,width,height,text);
    }
    /** Set the path of the shape.
     * @param pathID     the path  */
    public void setPath(PathType pathID) {
        switch (pathID) {
            case BOUNCE : {
                path = new BouncingPath(1, 2);
                break;
            }
			case FALL: {
				path = new FallingPath(2);
				break;
			}
		}
    }
    public NestedShape getParent() {return parent;}
	public void setParent(NestedShape s) {parent = s;}
    /** Return the x-coordinate of the shape.
     * @return the x coordinate */
    public int getX() { return this.x; }
	/** Set the x-coordinate of the shape.
	 * @param x	 the x-coordinate */
	public void setX(int x) { this.x = x; }

	/** Return the y-coordinate of the shape.
     * @return the y coordinate */
    public int getY() { return this.y;}
	/** Set the y-coordinate of the shape.
	 * @param y	 the y-coordinate	 */
	public void setY(int y) { this.y = y; }

    /** Return the width of the shape.
     * @return the width     */
	public int getWidth() { return width; }
	/** Set the width of the shape.
	 * @param w	 the width */
	public void setWidth(int w) { width = validateWidth(w); }
	public int validateWidth(int w) {
		return x+w > marginWidth?marginWidth - x:w;
	}
	public int validateHeight(int h) {
		return y+h > marginHeight?marginHeight - y:h;
	}
    /** Return the height of the shape.
     * @return the height     */
	public int getHeight() {return height; }
    /** Set the height of the shape.
     * @param h     the height value */
    public void setHeight(int h) { height = validateHeight(h);}
    /** Return the selected property of the shape.
     * @return the selected property  */
    public boolean isSelected() { return selected; }
    /** Set the selected property of the shape.
     *    When the shape is selected, its handles are shown.
     * @param s     the selected value */
    public void setSelected(boolean s) { selected = s; }

    /** Return the border colour of the shape.
     * @return the border colour */
	public Color getBorderColor() { return borderColor; }
    /** Set the border colour of the shape.
     * @param c     the border colour */
    public void setBorderColor(Color c) { borderColor = c; }
    /** Return the fill colour of the shape.
     * @return the fill colour */
	public Color getFillColor() { return fillColor; }
    /** Set the fill colour of the shape.
     * @param fc     the fill colour */
    public void setFillColor(Color fc) { fillColor = fc; }
    /** Reset the margin for the shape
     * @param w     the margin width
     * @param h     the margin height */
    public void setMarginSize(int w, int h) {
        marginWidth = w;
        marginHeight = h;
    }
    /** Draw the handles of the shape
     * @param g     the Graphics control */
    public void drawHandles(Painter g) {
        g.drawHandles(selected, x, y, width, height);
    }
	/** Returns whether the point is in the shape
	 * @param p	 the point variable */
    public abstract boolean contains(Point p);
    /** draw the shape
     * @param g     the Graphics control */
    public abstract void draw(Painter g);
    /** move the shape by the path  */
    public void move() {
        path.move();
    }
    // Inner class ===================================================================== Inner class
    /*
     *    ===============================================================================
     *    MovingPath : The superclass of all paths. It is an inner class.
     *    A path can change the current position of the shape.
     *    =============================================================================== */
    abstract class MovingPath {
        protected int deltaX, deltaY; // moving distance
        /** constructor  */
        public MovingPath() { }
        /** move the shape according to the path */
        public abstract void move();
    }
    /*
     *  ===============================================================================
     *  BouncingPath : A Bouncing path.
     *  ===============================================================================
     */
    class BouncingPath extends MovingPath {
         /** constructor to initialise values for a bouncing path */
        public BouncingPath(int dx, int dy) {
            deltaX = dx;
            deltaY = dy;
         }
        /** move the shape */
        public void move() {
             x = x + deltaX;
             y = y + deltaY;

             if ((x < 0) && (deltaX < 0)) {
                 deltaX = -deltaX;
                 x = 0;
             }
             else if ((x + width > marginWidth) && (deltaX > 0)) {
                 deltaX = -deltaX;
                 x = marginWidth - width;
             }
             if ((y< 0) && (deltaY < 0)) {
                 deltaY = -deltaY;
                 y = 0;
             }
             else if((y + height > marginHeight) && (deltaY > 0)) {
                 deltaY = -deltaY;
                 y = marginHeight - height;
             }
        }
    }
	/*
	 *  ===============================================================================
	 *  BouncingPath : A Bouncing path.
	 *  ===============================================================================
	 */
	class FallingPath extends MovingPath {
		 /** constructor to initialise values for a bouncing path */
		public FallingPath(int dy) {
			deltaY = dy;
		 }
		/** move the shape */
		public void move() {
			 y = y + deltaY;
			 if(y + height > marginHeight) {
				 y = 0;
			 }
		}
    }
}