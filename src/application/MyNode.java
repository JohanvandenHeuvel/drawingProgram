package application;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public interface MyNode {
	public void move(double delta_x, double delta_y);
	public void select();
	public void unselect();
	public Boolean getSelected();
	public Rectangle getBox();
	public String getType();
	public void draw();
	public void erase();
	public Bounds getBounds();
}
