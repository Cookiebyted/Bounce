/*
 *	===============================================================================
 *	RectangleShape.java : A shape that is a rectangle.
 *  UPI: myou129
 *	=============================================================================== */
import java.awt.*;
class RectangleShape extends Shape {

	/** constructor to create a rectangle with default values */
	public RectangleShape() { super(); }
	public RectangleShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt);
	}
	public RectangleShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt, String text) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt, text);
	}
	/** draw the rectangle with the fill colour
	 *	If it is selected, draw the handles
	 *	@param g2d	the Graphics control */
	@Override
	public void draw(Painter g2d) {
		g2d.setPaint(fillColor);
		g2d.fillRect(x, y, width, height);
		g2d.setPaint(borderColor);
		g2d.drawRect(x, y, width, height);
	}
	/** Returns whether the point is in the rectangle or not
	 * @return true if and only if the point is in the rectangle, false otherwise. */
	@Override
	public boolean contains(Point mousePt) {
		return (x <= mousePt.x && mousePt.x <= (x + width + 1)	&&	y <= mousePt.y && mousePt.y <= (y + height + 1));
	}
}