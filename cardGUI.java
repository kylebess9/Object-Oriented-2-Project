//This class will hold the card class for the GUI
//In order to make the card I'm going to use a 3x3 grid
//3 hBoxes in a main Vbox
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class cardGUI extends VBox{
	
	String Value = "";
	String Suit = "";
	Image heart = new Image("images/heart.png", 50, 50, true, true);
	Image club = new Image("images/club.png", 50, 50, true, true);
	Image spade = new Image("images/spade.png", 50, 50, true, true);
	Image diamonds = new Image("images/diamonds.png", 50, 50, true, true);
	cardHBox row1, row2, row3;
	cardGUI(){
		//Create spacing
		super(0);
		row1 = new cardHBox(heart, "A");
		row2 = new cardHBox();
		row3 = new cardHBox("A", heart);
		
		this.setStyle("-fx-border-color: black");
		this.getChildren().addAll(row1, row2, row3);
	}
	//The real way to create a card
	cardGUI(String suit, String value){
		this.Value = value;
		
		
		Image suitImg = heart;
		switch(suit){
		case "Hearts":
			suitImg = heart;
			break;
		case "Spades":
			suitImg = spade;
			break;
		case "Clubs":
			suitImg = club;
			break;
		case "Diamonds":
			suitImg = diamonds;
			break;
			
		}
		
		row1 = new cardHBox(suitImg, value);
		row2 = new cardHBox();
		row3 = new cardHBox(value, suitImg);
		this.setStyle("-fx-border-color: black");
		this.getChildren().addAll(row1, row2, row3);
	}
	public void removeCard(){
			row1.removeImages();
			row2.removeImages();
			row3.removeImages();
			this.getChildren().removeAll();
	}
	//The hBox classes
	class cardHBox extends HBox{
		cardHBox(){
			super(0);
			for(int i = 0; i < 3; i++){
				Rectangle shape1 = new Rectangle();
				shape1.setWidth(50);
				shape1.setHeight(100);
				shape1.setFill(Color.WHITE);
				this.getChildren().add(shape1);
			}
		}
		public void removeImages(){
			this.getChildren().removeAll();
		}
		//Row 1 Image first, then blank space, then value
		cardHBox(Image img, String letter){
			super(0);
			
			//Use a stackPane to put a background behind the image
			StackPane stackImage = new StackPane();
			
			ImageView image = new ImageView(img);
			image.setFitWidth(50);
			image.setFitHeight(50);
			this.getChildren().add(image);
			
			Rectangle background1 = new Rectangle();
			background1.setWidth(50);
			background1.setHeight(50);
			background1.setFill(Color.WHITE);
			
			stackImage.getChildren().addAll(background1, image);
			
			this.getChildren().add(stackImage);
			
			Rectangle shape1 = new Rectangle();
			shape1.setWidth(50);
			shape1.setHeight(50);
			shape1.setFill(Color.WHITE);
			this.getChildren().add(shape1);
			
			
			//Create a stackLabel to add a white background behind the letter
			StackPane stackLabel = new StackPane();
			
			Label charLetter = new Label (letter+"");
			charLetter.setFont(new Font(40));
			charLetter.setAlignment(Pos.CENTER);
			charLetter.setTextAlignment(TextAlignment.CENTER);
			
			Rectangle background = new Rectangle();
			background.setWidth(50);
			background.setHeight(50);
			background.setFill(Color.WHITE);
			stackLabel.getChildren().addAll(background, charLetter);
			
			this.getChildren().add(stackLabel);
		}
		cardHBox(String letter, Image img){
			super(0);
			//Create a stackLabel to add a white background behind the letter
			StackPane stackLabel = new StackPane();
			
			Label charLetter = new Label (letter+"");
			charLetter.setFont(new Font(40));
			charLetter.setAlignment(Pos.CENTER);
			charLetter.setTextAlignment(TextAlignment.CENTER);
			
			Rectangle background = new Rectangle();
			background.setWidth(50);
			background.setHeight(50);
			background.setFill(Color.WHITE);
			stackLabel.getChildren().addAll(background, charLetter);
			
			this.getChildren().add(stackLabel);
			
			//The inner row in order to fill the center
			Rectangle shape1 = new Rectangle();
			shape1.setWidth(50);
			shape1.setHeight(50);
			shape1.setFill(Color.WHITE);
			this.getChildren().add(shape1);
			
			
			StackPane stackImage = new StackPane();
			
			ImageView image = new ImageView(img);
			image.setFitWidth(50);
			image.setFitHeight(50);
			
			Rectangle background1 = new Rectangle();
			background1.setWidth(50);
			background1.setHeight(50);
			background1.setFill(Color.WHITE);
			
			stackImage.getChildren().addAll(background1, image);
			
			this.getChildren().add(stackImage);
		}
	}
}
