/*
 *	===============================================================================
 *	OvalShape.java : A shape that is an oval.
 *  UPI: myou129
 *	=============================================================================== */
import java.awt.*;
class OvalShape extends Shape {
	/** constructor to create an oval with default values */
	public OvalShape() { super(); }
	public OvalShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt);
	}
	public OvalShape(int x, int y, int w, int h, int mw, int mh, Color bc, Color fc, PathType pt, String text) {
		super(x ,y ,w, h ,mw ,mh, bc, fc, pt, text);
	}
	/** draw the oval with the fill colour
	 *	If it is selected, draw the handles
	 *	@param g	the Graphics control */
	@Override
	public void draw(Painter g2d) {
		g2d.setPaint(fillColor);
		g2d.fillOval(x, y, width, height);
		g2d.setPaint(borderColor);
		g2d.drawOval(x, y, width, height);
	}
	/** Returns whether the point is in the oval or not
	 * @return true if and only if the point is in the oval, false otherwise. */
	@Override
	public boolean contains(Point mousePt) {
		double dx, dy;
		Point EndPt = new Point(x + width, y + height);
		dx = (2 * mousePt.x - x - EndPt.x) / (double) width;
		dy = (2 * mousePt.y - y - EndPt.y) / (double) height;
		return dx * dx + dy * dy < 1.0;
	}
}