package application;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MyText {
	private TextField textfield;
	private Text text;
	private Pane DrawingPane;
	private Boolean selected;
	
	public MyText(double x1, double y1, Pane DrawingPane)
	{
		this.DrawingPane = DrawingPane;
		
		textfield = new TextField("Enter text here");
		textfield.setLayoutX(x1-1);
		textfield.setLayoutY(y1-1);
		DrawingPane.getChildren().add(textfield);
		
		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() 
		{
			public void handle(KeyEvent event) {
               if(event.getCode().equals(KeyCode.ENTER))
               {
            	   disableTextField();
               }
			}
		});
	}
	
	public void disableTextField()
	{
		setText(new Text(textfield.getLayoutX(), textfield.getLayoutY(), textfield.getText()));
		DrawingPane.getChildren().remove(textfield);
 	   	DrawingPane.getChildren().add(getText());
	}
	
	public void enableTextField()
	{
		textfield = new TextField(getText().getText());
		textfield.setLayoutX(getText().getX());
		textfield.setLayoutY(getText().getY());
		DrawingPane.getChildren().remove(getText());
		DrawingPane.getChildren().add(textfield);
		
		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() 
		{
			public void handle(KeyEvent event) {
               if(event.getCode().equals(KeyCode.ENTER))
               {
            	   disableTextField();
               }
			}
		});
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
	
	public Rectangle getBox()
	{
		return null;
	}
}
