package application;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public interface MyNode {
	public void move(double delta_x, double delta_y);
	public void select();
	public void unselect();
	public Boolean getSelected();
	public Rectangle getBox();
	public Shape getShape();
	public void draw();
	public void erase();
}
