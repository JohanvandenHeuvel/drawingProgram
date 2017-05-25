package application;

import java.awt.Point;
import java.io.File;
import java.net.MalformedURLException;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MyImage implements MyNode{
	private ImageView image;
	private Rectangle box;
	private Pane DrawingPane;
	private Boolean selected = false;
	private Circle[] anchors = new Circle[8];
	
	public MyImage(File file, Pane DrawingPane)
	{
		if(file != null)
		{
			this.DrawingPane = DrawingPane;
			try {
				image = new ImageView(file.toURI().toURL().toExternalForm());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void move(double delta_X, double delta_Y)
	{
		Bounds b = image.getBoundsInParent();
		
		erase();
		
		image.setX(b.getMinX() + delta_X);
		image.setY(b.getMinY() + delta_Y);
		
		//draw();
		
	}
	
	private Rectangle addBox()
	{	
		Bounds bounds = image.getBoundsInParent();
		Rectangle box = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		box.setFill(Color.TRANSPARENT);
		box.setStrokeWidth(5.0);
		box.setStroke(Color.RED);
		box.setStrokeDashOffset(1.0);
		box.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
		return box;
	}
	
	private Circle[] addAnchors()
	{
		Bounds bounds = image.getBoundsInParent();
		Circle[] anchor = new Circle[8];
		
		int x = (int) (bounds.getMinX());
		int y = (int) (bounds.getMinY());
		int width = (int) (bounds.getWidth());
		int height = (int) (bounds.getHeight());
		
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
	
	public void resize(double x1, double y1, double x2, double y2)
	{
		image.setX(x1);
		image.setY(y1);
		image.setFitWidth(x2);
		image.setFitHeight(y2);
	}
	
	public void unselect()
	{
		DrawingPane.getChildren().remove(box);
		DrawingPane.getChildren().removeAll(anchors);
		
		setSelected(false);
	}
	
	public void draw()
	{
		DrawingPane.getChildren().add(image);
	}
	
	public void erase()
	{
		DrawingPane.getChildren().remove(image);
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Boolean getSelected() {
		return selected;
	}
	
	public ImageView getImage()
	{
		return image;
	}
	
	public Rectangle getBox()
	{
		return box;
	}
	
	public Circle[] getAnchors() {
		return anchors;
	}
	
	public String getType()
	{
		return "Image";
	}
	
	public Bounds getBounds()
	{
		return image.getBoundsInParent();
	}
}
