package application;

import java.awt.Point;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.*;

public class designController {
	@FXML private ToggleButton Rectangle, Ellipse, Line, Delete, Select, Fill, Text, Strokecolor, Strokewidth, Import;
	@FXML private ToggleGroup Tools;
	@FXML private Slider SliderStrokewidth;
	@FXML private ColorPicker Colorpicker;
	@FXML private Pane DrawingPane;
	
	int selected;
	ArrayList<MyShape> shapes = new ArrayList<MyShape>();
	int resizing = -1;
	
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	
	
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
	
	@FXML protected void click_DrawingPane(){}
	
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
						Text t = new Text(x1, y1, "This is a test");
						DrawingPane.getChildren().addAll(t);
					case "Line":
					case "Rectangle":
					case "Ellipse":
						shapes.add(new MyShape(toggle_to_string(), DrawingPane));
						shapes.get(shapes.size()-1).drawShape();
						selected = shapes.size()-1; //TODO 
						break;
					case "Select":
						resizing = isAnchor(event);
						if(resizing == -1)
						{
							select(selected(event));
						}
						break;
					case "Delete":
						delete(selected(event));
						break;
					case "Fill":
					case "Strokecolor":
						shapes.get(selected(event)).manipulateShape(getColor(), toggle_to_string());
						break;
					case "Strokewidth":
						shapes.get(selected(event)).manipulateShape(getSlider());
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
					shapes.get(selected).eraseShape();
					shapes.get(selected).dragShape(x1, y1, x2, y2);
					select(selected);
					shapes.get(selected).drawShape();
				}
				if(Tools.getSelectedToggle() == Select)
				{	
					if(resizing != -1)
					{
						System.out.println("resizing");
						Bounds b = shapes.get(selected).getShape().getBoundsInParent();
						shapes.get(selected).eraseShape();
						switch (resizing) {
						case 1:
							shapes.get(selected).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY(), 
									b.getWidth() - (x2 - x1), 
									b.getHeight());
							break;
						case 2:
							shapes.get(selected).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY(), 
									b.getWidth() - (x2 - x1), 
									b.getHeight() + (y2 - y1));
							break;
						case 3:
							shapes.get(selected).resizeShape(	
									b.getMinX(), 
									b.getMinY() + (y2 - y1), 
									b.getWidth(), 
									b.getHeight() - (y2 - y1));
							break;
						case 4:
							shapes.get(selected).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth(), 
									b.getHeight() + (y2 - y1));
							break;
						case 5:
							shapes.get(selected).resizeShape(	
									b.getMinX(), 
									b.getMinY() + (y2 - y1), 
									b.getWidth() + (x2 - x1), 
									b.getHeight() - (y2 - y1));
							break;
						case 6:
							shapes.get(selected).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth() + (x2 - x1), 
									b.getHeight());
							break;
						case 7:
							shapes.get(selected).resizeShape(	
									b.getMinX(), 
									b.getMinY(), 
									b.getWidth() + (x2 - x1), 
									b.getHeight() + (y2 - y1));
							break;
						case 0:
							shapes.get(selected).resizeShape(	
									b.getMinX() + (x2 - x1), 
									b.getMinY() + (y2 - y1), 
									b.getWidth() - (x2 - x1), 
									b.getHeight() - (y2 - y1));
							break;
						default:
							break;
						};
						select(selected);
						shapes.get(selected).drawShape();	
						
						x1 = x2;
						y1 = y2;
					}
					else
					{
						System.out.println("Moving");
						Bounds b = shapes.get(selected).getShape().getBoundsInParent();
						shapes.get(selected).eraseShape();
						/*
						shapes.get(selected).moveShape(	b.getMinX() + (x2 - x1) , 
														b.getMinY() + (y2 - y1), 
														b.getWidth(), b.getHeight());
														*/
						shapes.get(selected).moveShape(	
								shapes.get(selected).getShape().getStartX, 
								50, 
								10, 
								10);
						select(selected);
						shapes.get(selected).drawShape();
						
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
	
	public Shape selectedShape(MouseEvent event)
	{
		if(!event.getTarget().equals(DrawingPane))
		{
			return (Shape) event.getTarget();
		}
		return null;
	}
	
	public int isAnchor(MouseEvent event)
	{
		Shape temp = selectedShape(event);
		if(temp != null && selected >= 0)
		{
			Circle[] anchors = shapes.get(selected).getAnchors();
			for(int i = 0; i < anchors.length; i++)
			{
				if(temp.equals(anchors[i]))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public int selected(MouseEvent event)
	{
		if(!event.getTarget().equals(DrawingPane))
		{
			for(int i = 0; i < shapes.size(); i++)
			{
				if (event.getTarget().equals(shapes.get(i).getShape()))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	Cursor setCursor(MouseEvent event)
	{
		Shape temp = selectedShape(event);	
		if(temp != null && selected >= 0)
		{
			Circle[] anchors = shapes.get(selected).getAnchors();
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
		shapes.get(i).eraseShape();
		shapes.remove(i);
	}
	
	public void select(int i)
	{
		for(MyShape s: shapes)
		{
			if(s.getSelected() == true)
				s.unselect();
		}
		if(i != -1)
		{
			shapes.get(i).select();
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
		if(Tools.getSelectedToggle() == Import)
			return "Import";
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
}


