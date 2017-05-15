package application;

import java.io.File;
import java.net.MalformedURLException;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MyImage implements MyNode{
	private ImageView image;
	private Rectangle box;
	private Pane DrawingPane;
	private Boolean selected = false;
	
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
	
	public void select()
	{
		box = addBox();
		
		DrawingPane.getChildren().add(box);
		
		setSelected(true);
	}
	
	public void unselect()
	{
		DrawingPane.getChildren().remove(box);
		
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
}
