package application;

import java.io.File;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.shape.Rectangle;

//TODO Move Image
//TODO Fix Line
//TODO Move Text
//TODO Fix Text
//TODO Add undo
//TODO Add timer
//TODO Big leds angle

public class designController {
	@FXML private ToggleButton Rectangle, Ellipse, Line, Delete, Select, Fill, Text, Strokecolor, Strokewidth;
	@FXML private BorderPane borderPane;
	@FXML private Button Import;
	@FXML private ToggleGroup Tools;
	@FXML private Slider SliderStrokewidth;
	@FXML private ColorPicker Colorpicker;
	@FXML private Pane DrawingPane;
	
	int selected;
	ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	ArrayList<MyText> texts = new ArrayList<MyText>();
	ArrayList<MyImage> images = new ArrayList<MyImage>();
	int resizing = -1;
	
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	
	
	@FXML protected void keyPress()
	{
		System.out.println("Key");
		borderPane.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event) 
			{
				 if(event.getCode().equals(KeyCode.DELETE))
	               {
					 	delete(selected);
	               }
			}
		});
	}
	
	
	@FXML protected void click_Import()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		File file = fileChooser.showOpenDialog(DrawingPane.getScene().getWindow());
		nodes.add(new MyImage(file, DrawingPane));
		nodes.get(nodes.size()-1).draw();
		selected = nodes.size()-1;
		select(selected);
	}
	
	/**
	 * Each time the cursor its moved it is checked what kind of cursor to use
	 */
	@FXML protected void move_DrawingPane()
	{
		DrawingPane.setOnMouseMoved(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				DrawingPane.setCursor(setCursor(event));
			}
		});
	}
	
	@FXML protected void click_DrawingPane()
	{
		DrawingPane.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					
					for(Node n : DrawingPane.getChildren())
					{
						if(event.getTarget().equals(n) && n instanceof Text)
						{
							for(MyText t : texts)
							{
								if(t.getText().equals(n))
								{
									System.out.println("Test");
									t.enableTextField();
								}
							}
						}
					}
				}
				/*
				if(event.getClickCount() == 1)
				{
					for(Node n : DrawingPane.getChildren())
					{
						System.out.println(event.getTarget());
						if(event.getTarget().equals(DrawingPane))
						{
							for(MyText t : texts)
							{
								t.disableTextField();
							}
						}
						else if(event.getTarget().equals(n) && !(n instanceof TextField))
						{
							for(MyText t : texts)
							{
								t.disableTextField();
							}
						}
					}
				}
				*/
			}
		});
	}
	
	@FXML protected void drag_DrawingPane()
	{
		DrawingPane.setOnMousePressed(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event)
			{	
				x1 = event.getX(); //mouse pressed x-coordinate
				y1 = event.getY(); //mouse pressed y-coordinate
				
				switch (toggle_to_string()) {
					case "Text":
						texts.add(new MyText(x1, y1, DrawingPane));
						break;
					case "Line":
					case "Rectangle":
					case "Ellipse":
						nodes.add(new MyShape(toggle_to_string(), DrawingPane));
						nodes.get(nodes.size()-1).draw();
						//select(shapes.indexOf(shapes.get(shapes.size()-1)));
						selected = nodes.size()-1; //TODO 
						break;
					case "Select":
						resizing = isAnchor(event);
						if(resizing == -1)
						{
							select(selected(event)); //TODO cannot select old shapes
						}
						break;
					case "Delete":
						delete(selected(event));
						break;
					case "Fill":
					case "Strokecolor":
						if(nodes.get(selected) instanceof MyShape)
							((MyShape)nodes.get(selected(event))).manipulateShape(getColor(), toggle_to_string());
						//shapes.get(selected(event)).manipulateShape(getColor(), toggle_to_string());
						break;
					case "Strokewidth":
						if(nodes.get(selected) instanceof MyShape)
							((MyShape)nodes.get(selected(event))).manipulateShape(getSlider());
						//shapes.get(selected(event)).manipulateShape(getSlider());
						break;
					default:
						break;
				}
			}
			
			
		});
		DrawingPane.setOnMouseDragged(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event)
			{	
				x2 = event.getX(); //mouse-dragged x-coordinate
				y2 = event.getY(); //mouse-dragged y-coordinate
				
				if(Tools.getSelectedToggle() == Line
				|| Tools.getSelectedToggle() == Rectangle
				|| Tools.getSelectedToggle() == Ellipse)
				{
					if(selected >= 0 && nodes.get(selected) instanceof MyShape)
					{
						nodes.get(selected).erase();
						((MyShape)nodes.get(selected)).dragShape(x1, y1, x2, y2);
						select(selected);
						nodes.get(selected).draw();
					}
				}
				if(Tools.getSelectedToggle() == Select)
				{	
					if(resizing != -1)
					{
						if(selected >= 0 && nodes.get(selected) instanceof MyShape)
						{
						Bounds b = ((MyShape)nodes.get(selected)).getShape().getBoundsInParent();
						nodes.get(selected).erase();
						switch (resizing) {
						case 1:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY(), 
									b.getWidth() - (x2 - x1), 
									b.getHeight());
							break;
						case 2:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY(), 
									b.getWidth() - (x2 - x1), 
									b.getHeight() + (y2 - y1));
							break;
						case 3:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX(), 
									b.getMinY() + (y2 - y1), 
									b.getWidth(), 
									b.getHeight() - (y2 - y1));
							break;
						case 4:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth(), 
									b.getHeight() + (y2 - y1));
							break;
						case 5:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX(), 
									b.getMinY() + (y2 - y1), 
									b.getWidth() + (x2 - x1), 
									b.getHeight() - (y2 - y1));
							break;
						case 6:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth() + (x2 - x1), 
									b.getHeight());
							break;
						case 7:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth() + (x2 - x1), 
									b.getHeight() + (y2 - y1));
							break;
						case 0:
							((MyShape)nodes.get(selected)).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY() + (y2 - y1), 
									b.getWidth() - (x2 - x1), 
									b.getHeight() - (y2 - y1));
							break;
						default:
							break;
						};
						select(selected);
						nodes.get(selected).draw();	
						
						x1 = x2;
						y1 = y2;
						}
					}
					else
					{
						nodes.get(selected).move((x2 - x1), (y2 - y1));
						select(selected);
						nodes.get(selected).draw();
						
						x1 = x2;
						y1 = y2;
					}
				}
			}
		});
		DrawingPane.setOnMouseReleased(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event)
			{	
				
			}
		});
	}
	
	
	
	public int isAnchor(MouseEvent event)
	{
		Shape temp = selectedShape(event);
		if(temp != null && selected >= 0)
		{
			if(nodes.get(selected) instanceof MyShape)
			{
				Circle[] anchors = ((MyShape)nodes.get(selected)).getAnchors();
				for(int i = 0; i < anchors.length; i++)
				{
					if(temp.equals(anchors[i]))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	public int selected(MouseEvent event)
	{
		if(!event.getTarget().equals(DrawingPane))
		{	
			if(selected >= 0 && event.getTarget().equals(nodes.get(selected).getBox()))
				return selected;
			else
			{
				for(int i = 0; i < nodes.size(); i++)
				{
					/*
					 * Target can be:
					 * -Shape
					 * -Text
					 * -Box
					 * -Image
					 */
					if(event.getTarget() instanceof Shape)
					{
						if(event.getTarget().equals(((MyShape) nodes.get(i)).getShape()))
						{
							return i;
						}
							
					}
					
					if(event.getTarget() instanceof ImageView)
					{
						if(event.getTarget().equals(((MyImage) nodes.get(i)).getImage()))
						{
							return i;
						}
							
					}
				}
			}
		}
		return -1;
	}
	
	Cursor setCursor(MouseEvent event)
	{
		Shape temp = selectedShape(event);	
		if(temp != null && selected >= 0 && nodes != null)
		{
			if(nodes.get(selected) instanceof MyShape)
			{
				Circle[] anchors = ((MyShape)nodes.get(selected)).getAnchors();
				for(int i = 0; i < anchors.length; i++)
				{
					if(temp.equals(anchors[i]))
					{
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
			}
			
		}
		
		/*
		if(temp != null && temp.equals(SHAPE) && Tools.getSelectedToggle() == Select)
		{
			Image moveCursor = new Image("Cursor-Move-2-icon.png");
			return new ImageCursor(moveCursor,
					moveCursor.getWidth() / 2,
					moveCursor.getHeight() /2);
		}
		*/
		return Cursor.DEFAULT;
	}
	
	public void delete(int i)
	{
		if(i >= 0)
		{
			if(i == selected)
				selected = -1;
			else
				selected--;
			nodes.get(i).unselect();
			nodes.get(i).erase();
			nodes.remove(i);
		}	
	}
	
	public void select(int i)
	{
		for(MyNode s: nodes)
		{
			if(s.getSelected() == true)
				s.unselect();
		}
		if(i != -1)
		{
			nodes.get(i).select();
			selected = i;
		}
	}
	
	Color getColor()
	{
		return Colorpicker.getValue();
	}
	
	Double getSlider()
	{
		return SliderStrokewidth.getValue() / 2;
	}	
	
	String toggle_to_string()
	{
		if(Tools.getSelectedToggle() == Text)
			return "Text";
		if(Tools.getSelectedToggle() == Line)
			return "Line";
		if(Tools.getSelectedToggle() == Rectangle)
			return "Rectangle";
		if(Tools.getSelectedToggle() == Ellipse)
			return "Ellipse";
		if(Tools.getSelectedToggle() == Select)
			return "Select";
		if(Tools.getSelectedToggle() == Delete)
			return "Delete";
		if(Tools.getSelectedToggle() == Fill)
			return "Fill";
		if(Tools.getSelectedToggle() == Strokecolor)
			return "Strokecolor";
		if(Tools.getSelectedToggle() == Strokewidth)
			return "Strokewidth";
		return null;
	}
	
	public Shape selectedShape(MouseEvent event)
	{
		if(!event.getTarget().equals(DrawingPane))
		{
			if(event.getTarget() instanceof Shape)
				return (Shape) event.getTarget();
		}
		return null;
	}
}


