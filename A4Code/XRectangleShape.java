/*
 *	===============================================================================
 *	XRectangleShape.java : A shape that is a xrectangle.
 *  UPI: myou129
 *	=============================================================================== */
import java.awt.*;
class XRectangleShape extends RectangleShape {

	/** constructor to create a rectangle with default values */
	public XRectangleShape() { super(); }
	public XRectangleShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt);
	}
	public XRectangleShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt, String text) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt, text);
	}
	/** draw the rectangle with the fill colour
	 *	If it is selected, draw the handles
	 *	@param g	the Graphics control */
	@Override
	public void draw(Painter g2d) {
		super.draw(g2d);
		g2d.drawLine(x, y, x+width, y+height);
		g2d.drawLine(x+width, y, x, y+height);
	}
}