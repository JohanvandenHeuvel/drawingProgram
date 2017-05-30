package application;

import java.io.File;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

//#1
//TODO mouse heatmap
//TODO box select (delete)
//TODO grey out when cant use

//#2
//TODO edit text
//TODO double click on node not working

//#3
//TODO Add timer
//TODO Add undo
//TODO add jfx.jar to project folder
//TODO first mouse press and key press do not work
//TODO look at shapes on top of each other

public class designController {
	@FXML
	private ToggleButton Rectangle, Ellipse, Line, Delete, Select, Fill, Text, Strokecolor, Strokewidth;
	@FXML
	private BorderPane borderPane;
	@FXML
	private Button Import;
	@FXML
	private ToggleGroup Tools;
	@FXML
	private Slider SliderStrokewidth;
	@FXML
	private ColorPicker Colorpicker;
	@FXML
	private Pane DrawingPane;

	ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	ArrayList<Node> panes = new ArrayList<Node>(); // TODO make undo

	private int resizing = -1;
	private int selected;

	private double x1 = 0;
	private double y1 = 0;
	private double x2 = 0;
	private double y2 = 0;

	@FXML
	protected void keyPress() {
		borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.DELETE)) {
					delete(selected);
				}
				if (event.getCode().equals(KeyCode.Z)) {
					if (panes.size() > 0) {
						System.out.println("undo");
					}
				}
			}
		});
	}

	@FXML
	protected void click_Import() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		File file = fileChooser.showOpenDialog(DrawingPane.getScene().getWindow());
		nodes.add(new MyImage(file, DrawingPane));
		nodes.get(nodes.size() - 1).draw();
		selected = nodes.size() - 1;
		select(selected);
	}

	@FXML
	protected void move_DrawingPane() {
		DrawingPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				DrawingPane.setCursor(setCursor(event));
			}
		});
	}

	@FXML
	protected void click_DrawingPane() {
		DrawingPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (event.getButton().equals(MouseButton.SECONDARY)) {
					// Circle circle = new Circle(100,100,50);
					// ContextMenu cm =new ContextMenu();
					// MenuItem a = new MenuItem("A");
					// MenuItem b = new MenuItem("B");
					// MenuItem c = new MenuItem("C");
					// cm.getItems().addAll(a,b,c);
					// circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
					//
					// @Override
					// public void handle(MouseEvent t) {
					// if(t.getButton().toString().equals("SECONDARY"))
					// cm.show(circle,t.getScreenX(),t.getSceneY());
					// }
					// });
					//
					// DrawingPane.getChildren().add(circle);
					//
				}

				// System.out.println("click");

				if (event.getButton().equals(MouseButton.SECONDARY)) 
				{

					System.out.println("Double click");

					for (int i = 0; i < nodes.size(); i++) {
						System.out.print(event.getTarget() + " ");
						System.out.print(((MyText) nodes.get(i)).getText());
						System.out.println();
						//&& event.getTarget().equals(((MyText) nodes.get(i)).getText())
						if (nodes.get(i).getType().equals("Text") )
							((MyText) nodes.get(i)).enableTextField();
					}
				}
			}
		});
	}

	@FXML
	protected void drag_DrawingPane() {
		DrawingPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				x1 = event.getX(); // mouse pressed x-coordinate
				y1 = event.getY(); // mouse pressed y-coordinate

				int target = selected(event);

				switch ((toggle_to_string() != null) ? toggle_to_string() : "null") {
				case "Text":
					for (MyNode node : nodes)
						if (node.getType() == "Text")
							((MyText) node).disableTextField();
					nodes.add(new MyText(x1, y1, DrawingPane));
					selected = nodes.size() - 1;
					break;
				case "Line":
				case "Rectangle":
				case "Ellipse":
					nodes.add(new MyShape(toggle_to_string(), DrawingPane));
					nodes.get(nodes.size() - 1).draw();
					selected = nodes.size() - 1;
					break;
				case "Select":
					resizing = isAnchor(event);
					if (resizing == -1)
						select(target);
					break;
				case "Delete":
					delete(target);
					break;
				case "Fill":
				case "Strokecolor":
					if (target >= 0 && nodes.get(target) instanceof MyShape)
						((MyShape) nodes.get(target)).manipulateShape(getColor(), toggle_to_string());
					break;
				case "Strokewidth":
					if (target >= 0 && nodes.get(target) instanceof MyShape)
						((MyShape) nodes.get(target)).manipulateShape(getSlider());
					break;
				default:
					break;
				}
			}

		});
		DrawingPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				x2 = event.getX(); // mouse-dragged x-coordinate
				y2 = event.getY(); // mouse-dragged y-coordinate

				if (Tools.getSelectedToggle() == Line || Tools.getSelectedToggle() == Rectangle
						|| Tools.getSelectedToggle() == Ellipse) {
					if (selected >= 0 && nodes.size() != 0 && nodes.get(selected) instanceof MyShape) {
						nodes.get(selected).erase();
						((MyShape) nodes.get(selected)).dragShape(x1, y1, x2, y2);
						select(selected);
						nodes.get(selected).draw();
					}
				}
				if (Tools.getSelectedToggle() == Select && selected >= 0) {
					if (resizing != -1) {
						if (nodes.get(selected) instanceof MyShape) {
							resizingShape();
						}
						if (nodes.get(selected) instanceof MyImage) {
							resizingImage();
						}
					} else {
						nodes.get(selected).move((x2 - x1), (y2 - y1));
						select(selected);
						nodes.get(selected).draw();

						x1 = x2;
						y1 = y2;
					}
				}
			}
		});
		DrawingPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (toggle_to_string() == "Line" || toggle_to_string() == "Rectangle"
						|| toggle_to_string() == "Ellipse")
					Tools.selectToggle(Select);
			}
		});
	}
	
	public void resizingShape()
	{
		Bounds b = ((MyShape) nodes.get(selected)).getShape().getBoundsInParent();
		nodes.get(selected).erase();

		double delta_x = x2 - x1;
		double delta_y = y2 - y1;

		double minX = Math.round(b.getMinX());
		double minY = Math.round(b.getMinY());
		double width = Math.round(b.getWidth());
		double height = Math.round(b.getHeight());

		switch (resizing) {
		case 1:
			((MyShape) nodes.get(selected)).resizeShape(minX + delta_x, minY, width - delta_x,
					height);
			break;
		case 2:
			((MyShape) nodes.get(selected)).resizeShape(minX + delta_x, minY, width - delta_x,
					height + delta_y);
			break;
		case 3:
			((MyShape) nodes.get(selected)).resizeShape(minX, minY + delta_y, width,
					height - delta_y);
			break;
		case 4:
			((MyShape) nodes.get(selected)).resizeShape(minX, minY, width, height + delta_y);
			break;
		case 5:
			((MyShape) nodes.get(selected)).resizeShape(minX, minY + delta_y, width + delta_x,
					height - delta_y);
			break;
		case 6:
			((MyShape) nodes.get(selected)).resizeShape(minX, minY, width + delta_x, height);
			break;
		case 7:
			((MyShape) nodes.get(selected)).resizeShape(minX, minY, width + delta_x,
					height + delta_y);
			break;
		case 0:
			((MyShape) nodes.get(selected)).resizeShape(minX + delta_x, minY + delta_y,
					width - delta_x, height - delta_y);
			break;
		default:
			break;
		}
		;
		select(selected);
		nodes.get(selected).draw();

		x1 = x2;
		y1 = y2;
	}

	public void resizingImage()
	{
		Bounds b = nodes.get(selected).getBounds();
		nodes.get(selected).erase();

		double delta_x = x2 - x1;
		double delta_y = y2 - y1;

		double minX = Math.round(b.getMinX());
		double minY = Math.round(b.getMinY());
		double width = Math.round(b.getWidth());
		double height = Math.round(b.getHeight());

		switch (resizing) {
		case 1:
			((MyImage) nodes.get(selected)).resize(minX + delta_x, minY, width - delta_x,
					height);
			break;
		case 2:
			((MyImage) nodes.get(selected)).resize(minX + delta_x, minY, width - delta_x,
					height + delta_y);
			break;
		case 3:
			((MyImage) nodes.get(selected)).resize(minX, minY + delta_y, width,
					height - delta_y);
			break;
		case 4:
			((MyImage) nodes.get(selected)).resize(minX, minY, width, height + delta_y);
			break;
		case 5:
			((MyImage) nodes.get(selected)).resize(minX, minY + delta_y, width + delta_x,
					height - delta_y);
			break;
		case 6:
			((MyImage) nodes.get(selected)).resize(minX, minY, width + delta_x, height);
			break;
		case 7:
			((MyImage) nodes.get(selected)).resize(minX, minY, width + delta_x,
					height + delta_y);
			break;
		case 0:
			((MyImage) nodes.get(selected)).resize(minX + delta_x, minY + delta_y,
					width - delta_x, height - delta_y);
			break;
		default:
			break;
		}
		;
		select(selected);
		nodes.get(selected).draw();

		x1 = x2;
		y1 = y2;
	}

	public int isAnchor(MouseEvent event) {
		Shape temp = selectedShape(event);
		if (temp != null && selected >= 0 && nodes.get(selected) instanceof MyShape) {
			Circle[] anchors = ((MyShape) nodes.get(selected)).getAnchors();
			for (int i = 0; i < anchors.length; i++) {
				if (temp.equals(anchors[i])) {
					return i;
				}
			}
		}
		
		if (temp != null && selected >= 0 && nodes.get(selected) instanceof MyImage) {
			Circle[] anchors = ((MyImage) nodes.get(selected)).getAnchors();
			for (int i = 0; i < anchors.length; i++) {
				if (temp.equals(anchors[i])) {
					return i;
				}
			}
		}
		
		
		return -1;
	}

	public int selected(MouseEvent event) {
		if (!event.getTarget().equals(DrawingPane)) {
			if (selected >= 0 && event.getTarget().equals(nodes.get(selected).getBox())) {
				return selected;
			} else {
				for (int i = 0; i < nodes.size(); i++) {
					/*
					 * Target can be: -Shape -Text -Box -Image
					 */
					if (event.getTarget() instanceof Shape) {
						if (nodes.get(i).getType().equals("Shape")
								&& event.getTarget().equals(((MyShape) nodes.get(i)).getShape()))
							return i;
					}
					if (event.getTarget() instanceof ImageView) {
						if (nodes.get(i).getType().equals("Image")
								&& event.getTarget().equals(((MyImage) nodes.get(i)).getImage()))
							return i;
					}
					if (event.getTarget() instanceof Text) {
						if (nodes.get(i).getType().equals("Text")
								&& event.getTarget().equals(((MyText) nodes.get(i)).getText()))
							return i;
					}
				}
			}
		}
		return -1;
	}

	Cursor setCursor(MouseEvent event) {
		Image cursor = null;
		if (!(toggle_to_string() == null))
			switch (toggle_to_string()) {
			case "Text":
				return Cursor.TEXT;
			case "Select":
				Shape temp = selectedShape(event);
				if (temp != null && selected >= 0 && nodes != null) {
					if (nodes.get(selected) instanceof MyShape) {
						Circle[] anchors = ((MyShape) nodes.get(selected)).getAnchors();
						return anchorCursor(anchors, temp);
					}
					if (nodes.get(selected) instanceof MyImage) {
						Circle[] anchors = ((MyImage) nodes.get(selected)).getAnchors();
						return anchorCursor(anchors, temp);
					}
				}
				for (int i = 0; i < nodes.size(); i++) {
					/*
					 * Target can be: -Shape -Text -Box -Image
					 */
					if (event.getTarget() instanceof Shape) {
						if (nodes.get(i).getType().equals("Shape")
								&& event.getTarget().equals(((MyShape) nodes.get(i)).getShape()))
							return Cursor.HAND;
					}
					if (event.getTarget() instanceof ImageView) {
						if (nodes.get(i).getType().equals("Image")
								&& event.getTarget().equals(((MyImage) nodes.get(i)).getImage()))
							return Cursor.HAND;
					}
					if (event.getTarget() instanceof Text) {
						if (nodes.get(i).getType().equals("Text")
								&& event.getTarget().equals(((MyText) nodes.get(i)).getText()))
							return Cursor.HAND;
					}
				}
				break;
			case "Delete":
				cursor = new Image("images/Editing-Eraser-icon.png");
				break;
			case "Fill":
				cursor = new Image("images/Editing-Background-Color-icon.png");
				break;
			default:
				break;
			}

		if (cursor == null)
			return Cursor.DEFAULT;
		else {
			ImageCursor c = new ImageCursor(cursor, cursor.getWidth() / 2, cursor.getHeight() / 2);
			return c;
		}
//		return Cursor.DEFAULT;
	}
	
	public Cursor anchorCursor(Circle[] anchors, Shape temp)
	{
		for (int i = 0; i < anchors.length; i++) {
			if (temp.equals(anchors[i])) {
				switch (i) {
				case 1:
					return Cursor.E_RESIZE;
				case 2:
					return Cursor.SW_RESIZE;
				case 3:
					return Cursor.N_RESIZE;
				case 4:
					return Cursor.S_RESIZE;
				case 5:
					return Cursor.NE_RESIZE;
				case 6:
					return Cursor.E_RESIZE;
				case 7:
					return Cursor.SE_RESIZE;
				case 0:
					return Cursor.NW_RESIZE;
				default:
					break;
				}
			}
		}
		return null;
	}

	public void delete(int i) {
		if (i >= 0) {
			selected = (i == selected) ? -1 : selected - 1;
			nodes.get(i).unselect();
			nodes.get(i).erase();
			nodes.remove(i);
		}
	}

	public void select(int i) {
		for (MyNode node : nodes) {
			if (node.getSelected() == true)
				node.unselect();
		}

		selected = (i > -1) ? i : -1;

		if (selected > -1)
			nodes.get(i).select();
	}

	Color getColor() {
		return Colorpicker.getValue();
	}

	Double getSlider() {
		return SliderStrokewidth.getValue() / 2;
	}

	String toggle_to_string() {
		if (Tools.getSelectedToggle() == Text)
			return "Text";
		if (Tools.getSelectedToggle() == Line)
			return "Line";
		if (Tools.getSelectedToggle() == Rectangle)
			return "Rectangle";
		if (Tools.getSelectedToggle() == Ellipse)
			return "Ellipse";
		if (Tools.getSelectedToggle() == Select)
			return "Select";
		if (Tools.getSelectedToggle() == Delete)
			return "Delete";
		if (Tools.getSelectedToggle() == Fill)
			return "Fill";
		if (Tools.getSelectedToggle() == Strokecolor)
			return "Strokecolor";
		if (Tools.getSelectedToggle() == Strokewidth)
			return "Strokewidth";
		return null;
	}

	public Shape selectedShape(MouseEvent event) {
		if (!event.getTarget().equals(DrawingPane)) {
			if (event.getTarget() instanceof Shape)
				return (Shape) event.getTarget();
		}
		return null;
	}
}
