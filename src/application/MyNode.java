package application;

public interface MyNode {
	public void move(double x1, double y1, double x2, double y2);
	public void select();
	public void unselect();
	public Boolean getSelected();
	public void draw();
	public void erase();
}
