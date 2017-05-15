package application;

import javafx.scene.shape.Rectangle;

public interface MyNode {
	public void move(double delta_x, double delta_y);
	public void select();
	public void unselect();
	public Boolean getSelected();
	public Rectangle getBox();
	public void draw();
	public void erase();
}
