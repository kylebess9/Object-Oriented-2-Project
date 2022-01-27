/*
 * Kyle Bess
 * 4/6/2018
 * JavaSE 1.8
 * COP2552.001
 * 
 */


import java.util.Random;
import java.util.Scanner;
import java.io.*;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;


//The main application class
public class BlackJack{
	//Some functions/classes require access to these variables/objects
	Stage primaryStage;
	playerStats statLoader;
	
	deck mainDeck;
	CardHolder oppHand = new CardHolder();
	CardHolder playHand = new CardHolder();
	
	//highScores highScoreStuff = new highScores();
	
	int playerWins;
	int playerLosses;
	int playerTies;
	
	String playerName = "";
	
	Label OpponentScore = new Label("Dealer's Hand: 0");
	Label PlayerScore = new Label("Player's Hand: 0");
	Label playerStats;
	
	
	//Scene highScoreTable = new Scene(highScoreStuff);
	Scene gameScreen;
	betTable betArea;
	
	//Button highScores = new Button("Check Highscores");
	/*class highScoreButton extends AnchorPane{
		highScoreButton(){
			this.getChildren().add(highScores);
			highScores.setOnAction(e -> {
				showHighScores();
			});
		}
	}
	*/
	BlackJack(Stage primaryStage1){
		
		primaryStage = primaryStage1;
		OpponentScore.setFont(new Font(15));
		PlayerScore.setFont(new Font(15));
		
		
		Scene nameScreen1 = new Scene(new nameScreen());
		
		
		gameScreen = new Scene(new gameBoard());
		primaryStage.setTitle("Bess Black Jack");
		primaryStage.setScene(nameScreen1);
		primaryStage.show();
		
		mainDeck = new deck(1);
		mainDeck.fillDeck();
		
		
	}
	
	//public void showHighScores(){
	//	primaryStage1.setScene(highScoreTable);
	//}
	
	class nameScreen extends VBox{
		
		nameScreen(){
			super(20);
			Label presetName = new Label("Please enter your name.");
			TextField nameIn = new TextField();
			Button submitName = new Button("Submit Name");
			Label errorText = new Label("");
			
			this.setPrefSize(300, 250);
			
			nameIn.prefWidth(200);
			this.getChildren().addAll(presetName, nameIn, submitName, errorText);
			this.setAlignment(Pos.CENTER);
			presetName.setTextAlignment(TextAlignment.CENTER);
			nameIn.setOnAction(e ->{
				String entry = nameIn.getText();
				if(entry.equals("") && !entry.contains(" ")){
					errorText.setText("Please enter a name.");
				}
				else if(entry.contains(" ")){
					errorText.setText("No spaces in the name");
				}
				else if(entry.length() > 20){
					errorText.setText("Please do not enter anything longer than 20 letters.");
				}
				else{
					//If the entry is okay reset the error text and text box
					playerName = nameIn.getText();
					errorText.setText("");
					nameIn.setText("");
					goToMain();
				}
			});
			submitName.setOnAction(e -> {
				String entry = nameIn.getText();
				if(entry.equals("") && !entry.contains(" ")){
					errorText.setText("Please enter a name.");
				}
				else if(entry.contains(" ")){
					errorText.setText("No spaces in the name");
				}
				else if(entry.length() > 20){
					errorText.setText("Please do not enter anything longer than 20 letters.");
				}
				else{
					//If the entry is okay reset the error text and text box
					playerName = nameIn.getText();
					errorText.setText("");
					nameIn.setText("");
					goToMain();
				}
			});
		}
	}
	
	public void goToMain(){
		primaryStage.setScene(gameScreen);
		statLoader = new playerStats(playerName);
		betArea.updateBank(statLoader.getBank());
		playerWins = statLoader.getWins();
		playerLosses = statLoader.getLosses();
		playerTies = statLoader.getTies();
		updateStats();
	}
	
	public card drawCard(){
		card checkCard = mainDeck.drawCard();
		if(checkCard == null){
			mainDeck.shuffleDeck();
			checkCard = mainDeck.drawCard();
		}
		return checkCard;
	}
	
	public void prepGame(){
		oppHand.resetHand();
		playHand.resetHand();
		
		//Give the dealer one card
		for(int i = 0; i < 1; i++){
			card cardToAdd = drawCard();
			String suit = cardToAdd.getSuit();
			String val = cardToAdd.getValue();
			oppHand.addCard(suit, val);
		}
		//Give the player two cards
		for(int i = 0; i < 2; i++){
			card cardToAdd = drawCard();
			String suit = cardToAdd.getSuit();
			String val = cardToAdd.getValue();
			playHand.addCard(suit, val);
		}
		updateCenterText();
	}
	
	public void updateStats(){
		playerStats.setText("Player Wins: " + playerWins + "\nPlayer Losses: " + playerLosses + "\nTies: " + playerTies);
	}
	
	public void endGame(){
		if(playHand.getHandValue() > 21){
			playerLosses++;
			betArea.betLost();
		}
		else if(oppHand.getHandValue() > 21){
			playerWins++;
			betArea.betWon();
		}
		else if(playHand.getHandValue() == oppHand.getHandValue()){
			playerTies++;
			betArea.betTied();
		}
		else if(playHand.getHandValue() > oppHand.getHandValue()){
			playerWins++;
			betArea.betWon();
		}
		else{
			playerLosses++;
			betArea.betLost();
		}
		updateStats();
		statLoader.saveStats(playerWins, playerLosses, playerTies, betArea.getBank(), playerName);
	}
	public void dealerTurn(){
		while(oppHand.getHandValue() < 16){
			card cardToAdd = drawCard();
			String suit = cardToAdd.getSuit();
			String val = cardToAdd.getValue();
			oppHand.addCard(suit, val);
		}
		updateCenterText();
		endGame();
	}
	
	public void updateCenterText(){
		OpponentScore.setText("Dealer's Hand: " + oppHand.getHandValue());
		PlayerScore.setText("Player's Hand: " + playHand.getHandValue());
	}
	class gameBoard extends BorderPane{
		//GameBoard Constructor
		gameBoard(){
			String sInstructions = "This is a replication of the game of Black Jack. Your goal is to get as close to 21 as possible."
					+ "Hit 'New Game' to start a game. Click on 'Hit' to get another card. Click on 'Hold' to keep your hand and "
					+ "allow the dealer to draw. The dealer will draw until the dealer has more than 16 or busts. \n"
					+ "You win if you have a higher value compared to the dealer or if the dealer busts. The dealer wins"
					+  " if the dealer has a higher value compared to the player or if the player busts. \n"
					+ "If the player chooses to make a bet, the bet must be made before hitting 'New Game'. "
					+ "If the player bets and wins, the player receives double their bet back. In the case of a "
					+ "tie, the player will have their bet returned back to them. If the player loses, then the bet is lost.";
			
			innerBoard inBoard = new innerBoard();
			
			Label Title = new Label("Black Jack");
			Title.setPadding(new Insets(20,0,20,0));
			Title.setAlignment(Pos.CENTER);
			Title.setTextAlignment(TextAlignment.CENTER);
			Title.setFont(new Font(40));
			
			VBox titleBox = new VBox(0);
			titleBox.getChildren().add(Title);
			titleBox.setAlignment(Pos.CENTER);
			
			this.setPrefHeight(600);
			this.setPrefWidth(1400);
			
			Label Instructions = new Label(sInstructions);
			Instructions.setPrefWidth(350);
			Instructions.setWrapText(true);
			Instructions.setPadding(new Insets(20,20,20,10));
			Instructions.setFont(new Font(15));
			
			playerStats = new Label("Player Wins: 0 \nPlayer Losses: 0 \nTies: 0");
			playerStats.setFont(new Font(15));
			playerStats.setPadding(new Insets(0,20,0,10));
			VBox rightInfo = new VBox(20);
			
			rightInfo.getChildren().addAll(Instructions, playerStats);
			
			betArea = new betTable();
			
			this.setLeft(betArea);
			this.setCenter(inBoard);
			this.setTop(titleBox);
			this.setRight(rightInfo);
			updateStats();
		}
		
		
		class innerBoard extends BorderPane{
			
			
			innerBoard(){
				this.setStyle("-fx-border-color: black");
				
				HBox buttonPane = new HBox(20);
				
				Button hitBtn = new Button("Hit");
				
				Button startGame = new Button("New Game");
				
				Button foldBtn = new Button("Hold");
				
				Button shuffleBtn = new Button("Shuffle Deck");
				hitBtn.setOnAction(e -> {
					if(playHand.getHandValue() <= 21){
						card cardToAdd = drawCard();
						String sendSuit = cardToAdd.getSuit();
						String sendValue = cardToAdd.getValue();
						playHand.addCard(sendSuit, sendValue);
						if(playHand.getHandValue() > 21){
							foldBtn.setText("Bust");
							hitBtn.setDisable(true);
						}
						
					}
					updateCenterText();
				});
				
				
				foldBtn.setOnAction(e -> {
					hitBtn.setDisable(true);
					foldBtn.setDisable(true);
					startGame.setDisable(false);
					shuffleBtn.setDisable(false);
					dealerTurn();
					betArea.enableBetting();
				});
				
				shuffleBtn.setOnAction(e ->{
					mainDeck.shuffleDeck();
					playHand.resetHand();
					oppHand.resetHand();
					shuffleBtn.setDisable(true);
					updateCenterText();
				});
				
				startGame.setOnAction(e ->{
					foldBtn.setText("Hold");
					prepGame();
					startGame.setDisable(true);
					hitBtn.setDisable(false);
					foldBtn.setDisable(false);
					shuffleBtn.setDisable(true);
					betArea.disableBetting();
				});
				
				buttonPane.getChildren().addAll(hitBtn, startGame, foldBtn, shuffleBtn);
				shuffleBtn.setDisable(true);
				hitBtn.setDisable(true);
				foldBtn.setDisable(true);
				
				VBox centerFrame = new VBox(10);
				
				buttonPane.setAlignment(Pos.CENTER);
				
				OpponentScore.setAlignment(Pos.CENTER);
				PlayerScore.setAlignment(Pos.CENTER);
				
				centerFrame.setAlignment(Pos.CENTER);
				centerFrame.getChildren().addAll(OpponentScore, buttonPane, PlayerScore);
				this.setCenter(centerFrame);
				this.setTop(oppHand);
				this.setBottom(playHand);
			}
			
		}
		
	}
	
}