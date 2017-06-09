package application;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MyText implements MyNode {
	private TextField textfield;
	private Text text;
	private Pane DrawingPane;
	private Boolean selected = false;
	private Boolean textFieldDisabled;
	private Rectangle box;

	public MyText(double x1, double y1, Pane DrawingPane) {
		this.DrawingPane = DrawingPane;

		textFieldDisabled = false;
		textfield = new TextField();
		textfield.setLayoutX(x1 - 1);
		textfield.setLayoutY(y1 - 1);
		DrawingPane.getChildren().add(textfield);

		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					disableTextField();
				}
			}
		});
	}

	public void disableTextField() {
		if (!textFieldDisabled) {
			textFieldDisabled = true;
			setText(new Text(textfield.getLayoutX(), textfield.getLayoutY(), textfield.getText()));
			DrawingPane.getChildren().remove(textfield);
			DrawingPane.getChildren().add(getText());
			text.setFocusTraversable(false);
		}
	}

	public void enableTextField() {
		if (textFieldDisabled) {
			textFieldDisabled = false;
			textfield = new TextField(getText().getText());
			textfield.setLayoutX(getText().getX());
			textfield.setLayoutY(getText().getY());
			DrawingPane.getChildren().remove(getText());
			DrawingPane.getChildren().add(textfield);
			selected = true;

			textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						disableTextField();
					}
				}
			});
		}

	}

	public void move(double delta_X, double delta_Y) {

		erase();

		text.setX(text.getX() + delta_X);
		text.setY(text.getY() + delta_Y);

		// draw();

	}

	public void draw() {
		DrawingPane.getChildren().add((textFieldDisabled) ? text : textfield);
	}

	public void erase() {
		DrawingPane.getChildren().remove((textFieldDisabled) ? text : textfield);
	}

	private Rectangle addBox() {
		Bounds bounds = text.getBoundsInParent();
		Rectangle box = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		box.setFill(Color.TRANSPARENT);
		box.setStrokeWidth(5.0);
		box.setStroke(Color.RED);
		box.setStrokeDashOffset(1.0);
		box.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);
		return box;
	}

	public void select() {

		box = addBox();

		DrawingPane.getChildren().add(box);

		setSelected(true);
	}

	public void toFront() {
		text.toFront();
	}

	public void toBack() {
		text.toBack();
	}

	public void unselect() {

		DrawingPane.getChildren().remove(box);
		disableTextField();
		setSelected(false);
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Rectangle getBox() {
		return box;
	}

	public Boolean getSelected() {
		return selected;
	}

	public String getType() {
		return "Text";
	}

	public Bounds getBounds() {
		return text.getBoundsInParent();
	}
}