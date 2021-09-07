/*
 *	===============================================================================
 *	NestedShape.java : A shape that have other shapes nested inside.
 *  UPI: myou129
 *	=============================================================================== */
import java.awt.*;
import java.util.ArrayList;

class NestedShape extends RectangleShape {
	private ArrayList<Shape> nestedShapes = new ArrayList<Shape>();
	private static ShapeType nextShapeType = ShapeType.NESTED;

	public NestedShape(){
		super();
	}

	public NestedShape(int x, int y, int width, int height, int mw, int mh, Color bc, Color fc, PathType pt){
		super(x, y, width, height, mw, mh, bc, fc, pt);
		nextShapeType = nextShapeType.next();
		createAddInnerShape(nextShapeType);
	}

	public NestedShape(int x, int y, int width, int height, int mw, int mh, Color bc, Color fc, PathType pt, String text){
		super(x, y, width, height, mw, mh, bc, fc, pt, text);
		nextShapeType = nextShapeType.next();
		createAddInnerShape(nextShapeType);
	}
	public NestedShape(ArrayList<Shape> listOfShapes, Color fc, Color bc){
		x = 0;
		y = 0;
		marginWidth = DEFAULT_MARGIN_WIDTH;
		marginHeight = DEFAULT_MARGIN_HEIGHT;
		width = DEFAULT_MARGIN_WIDTH;
		height = DEFAULT_MARGIN_HEIGHT;
		fillColor = fc;
		borderColor = bc;
		setPath(PathType.BOUNCE);
		text = DEFAULT_TEXT;

		for(Shape s: listOfShapes){
			nestedShapes.add(s);
		}

	}

	public void createAddInnerShape(ShapeType st){
		switch (st) {
			case RECTANGLE: {
				RectangleShape newShape = new RectangleShape(0, 0, this.width/2, this.height/2, this.width, this.height, this.borderColor, this.fillColor, PathType.BOUNCE, this.text);
				nestedShapes.add(newShape);
				newShape.setParent(this);
				break;
			} case XRECTANGLE: {
				XRectangleShape newShape = new XRectangleShape(0, 0, this.width/2, this.height/2, this.width, this.height, this.borderColor, this.fillColor, PathType.BOUNCE, this.text);
				nestedShapes.add(newShape);
				newShape.setParent(this);
				break;
			} case OVAL: {
				OvalShape newShape = new OvalShape(0, 0, this.width/2, this.height/2, this.width, this.height, this.borderColor, this.fillColor, PathType.BOUNCE, this.text);
				nestedShapes.add(newShape);
				newShape.setParent(this);
				break;
			} case SQUARE: {
				SquareShape newShape = new SquareShape(0, 0, this.width/2, this.height/2, this.width/2, this.borderColor, this.fillColor, PathType.BOUNCE, this.text);
				nestedShapes.add(newShape);
				newShape.setParent(this);
				break;
			} case NESTED: {
				NestedShape newShape = new NestedShape(0, 0, this.width/2, this.height/2, this.width, this.height, this.borderColor, this.fillColor, PathType.BOUNCE, this.text);
				nestedShapes.add(newShape);
				newShape.setParent(this);
				break;
			}
		}
	}



	public Shape getShapeAt(int index){
		return nestedShapes.get(index);
	}

	public int getSize(){
		return nestedShapes.size();
	}

	public void draw(Painter painter){
		painter.setPaint(Color.BLACK);
		painter.drawRect(x, y, width, height);
		painter.translate(x, y);
		for (Shape s: nestedShapes) {
			s.draw(painter);
		}
		painter.translate(-x, -y);
	}

	public void move(){
		super.move();
		for (Shape s: nestedShapes) {
			s.move();
		}
	}

	public void add(Shape s){
		nestedShapes.add(s);
		s.setParent(this);
	}

	public void remove(Shape s){
		nestedShapes.remove(s);
		s.setParent(null);
	}

	public int indexOf(Shape s){
		return nestedShapes.indexOf(s);
	}

	public Shape[] getChildren(){
		Shape[] childrenList = new Shape[nestedShapes.size()];
		for (int i=0; i < nestedShapes.size(); i++){
			childrenList[i] = nestedShapes.get(i);
		}
		return childrenList;
	}
}