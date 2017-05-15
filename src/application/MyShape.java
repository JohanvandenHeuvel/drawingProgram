package application;

import java.awt.Point;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MyShape implements MyNode {
	
	private Shape shape;
	private Circle[] anchors = new Circle[8];
	private Rectangle box;
	private Pane DrawingPane;
	private Boolean selected = false;
	
	public MyShape(String shape_type, Pane DrawingPane) 
	{
		if(shape_type == "Line")
			shape = new Line();
		if(shape_type == "Rectangle")
			shape = new Rectangle(); 
		if(shape_type == "Ellipse")
			shape = new Ellipse();  
		this.DrawingPane = DrawingPane;
	}
	
	public MyShape(double x1, double y1, double x2, double y2, String shape_type, Pane DrawingPane)
	{
		if(shape_type == "Line")
			shape = new Line(x1, y1, x2, y2);
		if(shape_type == "Rectangle")
			shape = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2)); 
		if(shape_type == "Ellipse")
			shape = new Ellipse(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));  
		this.DrawingPane = DrawingPane;
	}
	
	public void dragShape(double x1, double y1, double x2, double y2)
	{
		if(shape instanceof Line)
			shape = new Line(x1, y1, x2, y2);
		if(shape instanceof Rectangle)
			shape = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2)); 
		if(shape instanceof Ellipse)
			shape = new Ellipse(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
	
	public void resizeShape(double x1, double y1, double x2, double y2)
	{
		if(shape instanceof Line)
			shape = new Line(x1, y1, x2, y2);
		if(shape instanceof Rectangle)
			shape = new Rectangle(x1, y1, Math.max(x2, 10.0), Math.max(y2, 10.0)); 
		if(shape instanceof Ellipse)
			shape = new Ellipse(x1 + (x2/2), y1 + (y2/2),Math.max(x2/2, 10.0), Math.max(y2/2, 10.0));
		
	}
	
	public void move(double delta_x, double delta_y)
	{
		Bounds b = shape.getBoundsInParent();
		
		double x1 = b.getMinX() + delta_x;
		double y1 = b.getMinY() + delta_y; 
		double x2 = b.getWidth();
		double y2 = b.getHeight();
		
		erase();
		
		if(shape instanceof Line)
			shape = new Line(x1, y1, x1+x2, y1+y2);
		if(shape instanceof Rectangle)
			shape = new Rectangle(x1, y1, x2, y2); 
		if(shape instanceof Ellipse)
			shape = new Ellipse(x1 + (x2/2), y1 + (y2/2), x2/2, y2/2);
		
		//draw();
	}
	
	
	private Circle[] addAnchors()
	{
		Bounds bounds = shape.getBoundsInParent();
		
		int x = (int) (bounds.getMinX());
		int y = (int) (bounds.getMinY());
		int width = (int) (bounds.getWidth());
		int height = (int) (bounds.getHeight());
		
		Circle[] anchor = new Circle[8];
		Point[] positions = { 	new Point(x, y), 			new Point(x, y+height/2), 		new Point(x, y+height), 
								new Point(x+width/2, y), 									new Point(x+width/2, y+height), 
								new Point(x+width, y), 		new Point(x+width, y+height/2), new Point(x+width, y+height)};
		
		for(int i = 0; i < positions.length; i++)
		{
			Circle c = new Circle(positions[i].getX(),positions[i].getY(), 10.0);
			c.setFill(Color.TRANSPARENT);
			c.setStroke(Color.YELLOW);
			c.setStrokeWidth(2.5);
			anchor[i] = c;
		}
		return anchor;
	}
	
	public void select()
	{
		box = addBox();
		anchors = addAnchors();
		
		DrawingPane.getChildren().add(box);
		DrawingPane.getChildren().addAll(anchors);
		
		setSelected(true);
	}
	
	public void unselect()
	{
		DrawingPane.getChildren().remove(box);
		DrawingPane.getChildren().removeAll(anchors);
		
		setSelected(false);
	}
	
	private Rectangle addBox()
	{	
		Bounds bounds = shape.getBoundsInParent();
		Rectangle box = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		box.setFill(Color.TRANSPARENT);
		box.setStrokeWidth(5.0);
		box.setStroke(Color.RED);
		box.setStrokeDashOffset(1.0);
		box.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
		return box;
	}
	
	public void draw()
	{
		DrawingPane.getChildren().add(shape);
	}
	
	public void erase()
	{
		DrawingPane.getChildren().remove(shape);
	}
	
	public void manipulateShape(Paint color, String choice)
	{
		if(choice == "Strokecolor")
		{
			shape.setStroke(color);
		}
		if(choice == "Fill" && !(shape instanceof Line))
		{
			shape.setFill(color);
		}
	}

	public void manipulateShape(double stroke_width)
	{
		shape.setStrokeWidth(stroke_width);
	}

	public Shape getShape() {
		return shape;
	}

	
	public void setShape(Shape shape) {
		this.shape = shape;
	}


	public Circle[] getAnchors() {
		return anchors;
	}


	public void setAnchors(Circle[] anchors) {
		this.anchors = anchors;
	}


	public Rectangle getBox() {
		return box;
	}


	public void setBox(Rectangle box) {
		this.box = box;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}
