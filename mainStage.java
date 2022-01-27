import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mainStage extends Application{
	BlackJack blackJack;
	GUIYahtzeeBess yahtzee;
	Hangman hangman;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		VBox menu = new VBox(20);
		Scene menuScene = new Scene(menu);
		
		
		Label titleLbl = new Label("Pick a game to play!");
		
		Button hangmanBtn = new Button("Play Hangman");
		Button blackjackBtn = new Button("Play Black Jack");
		Button yahtzeeBtn = new Button("Play Yahtzee");
		
		blackjackBtn.setOnAction(e -> {
			blackJack = new BlackJack(primaryStage);
		});
		yahtzeeBtn.setOnAction(e -> {
			yahtzee = new GUIYahtzeeBess(primaryStage);
		});
		hangmanBtn.setOnAction(e -> {
			try{
				hangman = new Hangman(primaryStage);
			}
			catch(Exception x){
				System.out.print("Error: " + x);
			}
		});
		
		menu.getChildren().addAll(titleLbl, hangmanBtn, blackjackBtn, yahtzeeBtn);
		
		menu.setPrefSize(200, 200);
		menu.setAlignment(Pos.CENTER);
		
		primaryStage.setScene(menuScene);
		primaryStage.setTitle("Main Menu");
		primaryStage.show();
	}

}
