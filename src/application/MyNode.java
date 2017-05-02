package application;

public interface MyNode {
	public void move(double delta_x, double delta_y);
	public void select();
	public void unselect();
	public Boolean getSelected();
	public void draw();
	public void erase();
}
