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

public class MyImage {
	private ImageView image;
	private Rectangle box;
	private Pane DrawingPane;
	private Boolean selected;
	
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
			drawImage();
			select();
		}
	}
	
	public void moveImage(double x1, double y1)
	{
		image.setX(x1);
		image.setY(y1);
	}
	
	private Rectangle addBox()
	{	
		Bounds bounds = image.getBoundsInParent();
		Rectangle box = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		box.setFill(Color.TRANSPARENT);
		box.setStrokeWidth(2.0);
		box.setStroke(Color.BLACK);
		box.setStrokeDashOffset(1.0);
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
	
	public void drawImage()
	{
		DrawingPane.getChildren().add(image);
	}
	
	public void eraseImage()
	{
		DrawingPane.getChildren().remove(image);
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}
