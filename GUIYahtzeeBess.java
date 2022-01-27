/*
 * Kyle Bess
 * 4/6/2018
 * JavaSE 1.8
 * COP2552.001
 * This program takes the yahtzee assignment and utilizes JavaFX to design a UI.
 * I will want to fiddle around with some of the styling, but with time being as it is, this is what I have
 * 
 * I left a highscore in there to challenge you, but the game will create the file if it is not found
 * There is currently no way to reset it from in game (maybe in the future)
 * 
 */


import java.util.Random;
import java.util.Scanner;
import java.io.*;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;


//The main application class
public class GUIYahtzeeBess{
	//Some functions/classes require access to these variables/objects
	Scene mainMenuScreen;
	Scene newHighScoreScene;
	fileOperations fileOp;
	Stage primaryStage1;

	GUIYahtzeeBess(Stage primaryStage){
		//Pointing the primaryStage1 variable at the primaryStage
		//Could not figure out how to point directly at the primaryStage, but this worked
		this.primaryStage1 = primaryStage;
		//Create fileOperations object
		fileOp = new fileOperations();
		try{
			fileOp.checkHighscores();
			fileOp.sortHighscores();
		}
		catch(Exception e){
			System.out.println("Could not find/create highscore.txt");
		}
		//Set the title of the stage, the fun stuff
		primaryStage.setTitle("Yahtzee");
		//This is the highscore menu
		//I created a separate object for the highscore menu
		highScoreMenu highMenu = new highScoreMenu();
		Scene highScene = new Scene(highMenu);
		
		//The main game pane/scene
		mainPane gamePane = new mainPane();
		gamePane.setPrefHeight(350);
		Scene mainGameScene = new Scene(gamePane);
		//The main menu screen
		mainMenu mainMenu = new mainMenu();
		//The startBtn opens up the main game
		Button startBtn = new Button("Start Game");
		startBtn.setOnAction(e -> {
			//The score needs to be reset here in order to make sure that a fresh game occurs
			//Restarting it in the gamePane is risky because the score is not saved until a name is given
			gamePane.resetTotScore();
			//The score is reset and then also updated so that the score text is updated
			gamePane.updateScore();
			primaryStage.setScene(mainGameScene);
		});
		
		Button highBtn = new Button("High Scores");
		//The welcome message
		String introString = "Welcome to Bess' Yahtzee game. This is a simplified version of Yahtzee so certain rules might"
				+ " have changed. Hit 'Start Game' to open up the game menu. Hit 'High Scores' to check out your best scores.";
		Text introText = new Text(introString);
		introText.setWrappingWidth(300);
		mainMenu.setAlignment(Pos.CENTER);
		mainMenu.getChildren().addAll(introText, startBtn, highBtn);
		mainMenuScreen = new Scene(mainMenu);
		
		
		
		primaryStage.setScene(mainMenuScreen);
		primaryStage.show();
		
		//The highscore button was fairly finicky and I might come back and revise it
		//It pretty much builds the highscore menu everytime it's requested
		highBtn.setOnAction(e -> {
			primaryStage.setScene(highScene);
			fileOp.sortHighscores();
			//This function is a mess since I wanted to build the menu everytime it was loaded up
			//This means having to update the button's functionality
			//I couldn't link to the main stage without making it a parameter/arguement
			highMenu.updateHighScoreMenu(fileOp.getHighNames(), fileOp.getHighscores(), fileOp.getNumScores(), mainMenuScreen, primaryStage);
			
			
		});
		
		VBox newHighScoreMenu = new VBox(30);
		newHighScoreMenu.setPrefWidth(300);
		newHighScoreMenu.setPadding(new Insets(20,20,20,20));
		Text highMenuTitle = new Text("Thats a High Score! Enter your name and then hit 'Enter'.");
		TextField nameBox = new TextField();
		nameBox.setMaxWidth(300);
		Text errorText = new Text("");
		
		//When the user hits enter on the text box, it will make sure of a few things:
		//That the entry does not contain a space (this will make it alot harder to enter back into the game
		//That the entry is not just a space (same reason as above)
		//And that the entry is not longer than 10 characters (this might be too strict)
		nameBox.setOnAction(e ->{
			String entry = nameBox.getText();
			if(entry.equals("") && !entry.contains(" ")){
				errorText.setText("Please enter a name.");
			}
			else if(entry.contains(" ")){
				errorText.setText("No spaces in the name");
			}
			else if(entry.length() > 10){
				errorText.setText("Please do not enter anything longer than 10 letters.");
			}
			else{
				//If the entry is okay reset the error text and text box
				errorText.setText("");
				nameBox.setText("");
				//Attempt to add the highscore
				fileOp.addHighscore(gamePane.getScore(), entry);
				//Go back to the main menu
				primaryStage1.setScene(mainMenuScreen);
			}
		});
		newHighScoreMenu.getChildren().addAll(highMenuTitle, nameBox, errorText);
		newHighScoreScene = new Scene(newHighScoreMenu);
		
		
		
		
	}
	
	//This is the highscore menu class
	class highScoreMenu extends VBox{
		Text highScoreTitle;
		highScoreMenu(){
			super(10);
			
			highScoreTitle = new Text("High Scores");
			highScoreTitle.setFont(new Font(30));
			this.setPrefSize(200, 400);
			this.setAlignment(Pos.CENTER);
			this.getChildren().add(highScoreTitle);
			
		}
		
		//This might need to be redone before the final
		//Kind of a finicky solution that requires a rebuild everytime
		public void updateHighScoreMenu(String[] scoreNames, int[] scores, int count, Scene mainMenu, Stage PrimaryStage){
			this.getChildren().clear();
			highScoreTitle = new Text("High Scores");
			highScoreTitle.setFont(new Font(30));
			this.getChildren().add(highScoreTitle);
			for(int i = 0; i < count; i++){
				this.getChildren().add(new Text(scoreNames[i] + " : " + scores[i]));
			}
			//This is the main reason
			//Having to add the button everytime makes this function 10X more ridiculous
			backMenu backBtn = new backMenu();
			backBtn.setOnAction(e ->{
				PrimaryStage.setScene(mainMenu);
			});
			this.getChildren().add(backBtn);
		}
	}
	//See above, this button could probably be cleaned up
	//I thought I'd need to do more with it
	class backMenu extends Button{
		
		backMenu(){
			super("Return to Menu");
		}
		
	}
	//My I/O class
	//It's probably not necessary and it actually created a lot of weird relationships
	//For example, moving from the game to the fileOperations object is pretty janky
	//Another thing I will want to revise
	class fileOperations{
		//I used parallel arrays for the Names and scores.
		String[] highScoreName = new String[10];
		int[] highScores = new int[10];
		//Count the number of highscores
		int highCount = 0;
		File highscore = new File("highscore.txt");
		fileOperations(){
			//Reset the arrays
			for(int i = 0; i < 10; i++){
				highScoreName[i] = "";
				highScores[i] = 0;
			}
		}
		//returns how many highscores are active
		public int getNumScores(){
			return highCount;
		}
		//This function will save the highscores to the file
		//Used everytime there is a new score added
		public void saveHighScore() throws IOException{
			PrintWriter saveTo = new PrintWriter(highscore);
			for(int i = 0; i < highCount; i++){
				saveTo.write(highScoreName[i] + " " + highScores[i] + "\n");
			}
			saveTo.close();
		}
		//This is a really major function in my program.
		//It will figure out where the highscore is placed and shift around the scores to make it work
		public void addHighscore(int scoreIn, String nameIn){
			//If there is less than 10 highscores, add it in
			//The highscores are sorted everytime regardless
			if(highCount < 10){
				highScoreName[highCount] = nameIn;
				highScores[highCount] = scoreIn;
				highCount++;
			}
			else{
				//Just to double check that the lowest number is in fact lower than the new score
				if(highScores[9] < scoreIn){
					int mark = 0;
					//Find where to place the new highscore
					for(int i = 0; i < 10; i++){
						//Work backwards from 9 to 0
						if(highScores[9 - i] < scoreIn){
							//Mark where the score is higher
							mark = i;
						}
					}
					
					if(mark == 0){
						//if the highscore is only good enough for the lowest, set it there
						highScores[9] = scoreIn;
						highScoreName[9] = nameIn;
					}
					else{
						//if the highscore is higher on the list
						//Start bumping the scores down the list
						for(int i = 0; i < mark; i++){
							highScores[9 - i] = highScores[(9-i)-1];
							highScoreName[9 - i] = highScoreName[(9-i) - 1];
						}
						//Place the new score in it's rightfull place
						highScores[9 - mark] = scoreIn;
						highScoreName[9 - mark] = nameIn;
					}
				}
			}
			try{
				//save the highscore
				saveHighScore();
			}
			catch(Exception e){
				System.out.println("Problems saving to highscore.txt");
			}
		}
		//Used when loading up the highscores in the UI
		public void sortHighscores(){
			boolean sorted = false;
			while(!sorted){
				sorted = true;
				for(int i = 0; i < highCount - 1; i++){
					if(highScores[i] < highScores[i + 1]){
						int tempScore = highScores[i];
						String tempName = highScoreName[i];
						highScoreName[i] = highScoreName[i + 1];
						highScores[i] = highScores[i + 1];
						highScores[i + 1] = tempScore;
						highScoreName[i + 1] = tempName;
						sorted = false;
					}
				}
			}
		}
		//Return the names
		public String[] getHighNames(){
			return highScoreName;
		}
		//Return the scores
		public int[] getHighscores(){
			return highScores;
		}
		//Pull in the highscores from the text file
		//Create the file if it's not there
		public void checkHighscores() throws IOException{
			try{
				if(highscore.exists()){
					Scanner readIn = new Scanner(highscore);
					while(readIn.hasNext()){
						
						
						if(highCount < 10){
							highScoreName[highCount] = readIn.next();
							if(readIn.hasNextInt())
								highScores[highCount] = readIn.nextInt();
							else{
								System.out.println("Broken file: highscore.txt");
								//reset the file
								PrintWriter createFile = new PrintWriter(highscore);
								createFile.close();
								readIn.close();
								break;
							}
							highCount++;
						}
					}
					readIn.close();
				}
				else{
					//Create the file
					PrintWriter createFile = new PrintWriter(highscore);
					createFile.close();
				}
			}
			catch(Exception e){
				System.out.println("Could not load/create highscore.txt");
			}
		}
	}
	
	//The MainGame pane
	class mainPane extends BorderPane{
		//Number of rounds
		int roundNum = 1;
		//The current count (1st round, 2nd round, 3rd round)
		//See below
		int count = 1;
		//The temp score
		int score = 0;
		//The total score
		int totScore = 0;
		//A potentially useless boolean
		//Will need to revise potentially
		boolean play = true;
		//Create the pane
		diceHBox diceBox;
		//Create the gameSummary pane
		gameSummary GS;
		Text txtScore;
		//I created a different class for scoring
		//Maybe a bit useless, but it worked out for this system
		scoringSystem scoreObj = new scoringSystem();
		String INSTRUCTIONS = "Click 'Roll Dice' to roll unheld dice. Hold dice by clicking on the dice themselves.\n\n"
				+ "Scoring: \n\n Yahtzee(5 matching dice): 50\n Large Straight (5 consecutive dice): 40\n" 
				+ "Small Straight (4 consecutive dice): 30\n Full House (3 matches + 2 matches): 25\n"
				+ "4 of a kind (4 matches): Sum of dice + 10\n 3 of a kind (3 matches): Sum of dice + 5\n"
				+ "Chance (None of the above): Sum of dice\n\n Highest result will be added to score";
		//Constructor
		mainPane(){
			//All the formating for the main game screen
			this.setPadding(new Insets(20, 20, 20, 20));
			diceBox = new diceHBox();
			
			txtScore = new Text("Score: 0");
			
			BorderPane centerBox = new BorderPane();
			
			this.setTop(txtScore);
			
			Text introText = new Text(INSTRUCTIONS);
			introText.setWrappingWidth(300);
			introText.setFont(new Font(14));
			this.setRight(introText);
			
			
			HBox buttonPane = new HBox(10);
			
			Button rollBtn = new Button("Roll Dice");
			//The main game loop
			rollBtn.setOnAction(e -> {
				//Round 1, roll the dice
				if(count == 1 && roundNum <= 10 && play){
					diceBox.rollDice();
					count++;
				}
				//Round 2, last time to roll the dice, display 'next round'
				else if(count == 2 && roundNum <= 10 && play){
					diceBox.rollDice();
					count++;
					rollBtn.setText("Next Round");
				}
				//Round 3, start with new dice (no holding)
				else if(count == 3 && roundNum < 10 && play){
					scoreRound();
					totScore += score;
					newRound();
					roundNum++;
					count = 1;
					rollBtn.setText("Roll Dice");
					diceBox.resetDice();
					updateScore();
				}
				//End of game, display 'new game' button
				else if(count == 3 && roundNum == 10 && play){
						scoreRound();
						totScore += score;
						newRound();
						rollBtn.setText("New Game");
						updateScore();
						play = false;
				}
				//if the round > 10 (new game)
				//Check for highscores or create a new game
				//This allows for the player to be able to review the round summaries
				else{
					System.out.println(fileOp.highScores[9]);
					if(fileOp.highScores[9] < totScore){
						primaryStage1.setScene(newHighScoreScene);
					}
					else
						totScore = 0;
					updateScore();
					play = true;
					GS.reset();
					roundNum = 1;
					count = 1;
					rollBtn.setText("Roll Dice");
					diceBox.resetDice();
				}
			});
			//You can exit the game prematurely, does not save game/allow for highscore
			Button endGameBtn = new Button("Exit Game");
			endGameBtn.setOnAction(e->{
				play = true;
				GS.reset();
				roundNum = 1;
				count = 1;
				rollBtn.setText("Roll Dice");
				diceBox.resetDice();
				primaryStage1.setScene(mainMenuScreen);
			});
			buttonPane.getChildren().addAll(rollBtn, endGameBtn);
			centerBox.setCenter(diceBox);
			centerBox.setBottom(buttonPane);
			buttonPane.setAlignment(Pos.CENTER);
			this.setCenter(centerBox);
			
			GS = new gameSummary();
			this.setLeft(GS);
			
			
			
		}
		public void updateScore(){
			txtScore.setText("Score: " + totScore);
		}
		public int getScore(){
			return totScore;
		}
		//Add the new round to gameSummary;
		public void newRound(){
			GS.addNewRound(roundNum, diceBox.getValues(), score);
		}
		//The scoreRound function is similar to the old function, but I sent all the scoring logic to the scoring object
		public void scoreRound(){
			score = 0;
			int[] tempDice = diceBox.getValues();
			int[] countVals = new int[6];
			
			for(int i = 0; i < 5; i++){
				//Gets the value of the dice and adds 1 to the number count
				//It looks a bit awkward but in essence, if you roll 2 1's, it will add 2 to the 0 spot (arrays start at 0)
				countVals[tempDice[i] - 1]++;
			}
			
			//Check for a Yahtzee
			if(scoreObj.Yahtzee(countVals))
				score = 50;
			if(scoreObj.largeStraight(tempDice))
				score = 40;
			if(scoreObj.smallStraight(countVals)){
				if (score < 30){
					score = 30;
				}
			}
			if(scoreObj.fullHouse(countVals))
				score = 25;
			if(scoreObj.fourOfAKind(countVals)){
				if(score < (countHand() + 10)){
					score = countHand() + 10;
				}
			}
			if(scoreObj.threeOfAKind(countVals)){
				if(score < (countHand() + 5)){
					score = countHand() + 5;
				}
			}
			if(score < countHand()){
				score = countHand();
			}
			
		}
		public void resetTotScore(){
			totScore = 0;
		}
		//Sum up the hand
		public int countHand(){
			int[] tempDice = diceBox.getValues();
			int retScore = 0;
			for(int i = 0; i < 5; i++){
				retScore += tempDice[i];
			}
			return retScore;
		}
		//The scoring object
		//Handles all the logic for scoring the dice
		class scoringSystem{
			public boolean Yahtzee(int[] count){
				for(int i = 0; i < 6; i++){
					if(count[i] == 5){
						return true;
					}
				}
				return false;
			}
			
			public boolean largeStraight(int[] rollsArray){
				int[] tempArray = new int[5];
				for(int i = 0; i < 5; i++){
					tempArray[i] = rollsArray[i];
				}
				sortArray(tempArray);
				Boolean lStraight = true;
				for(int i = 0; i < 4 && lStraight; i++){
					if(tempArray[i] != tempArray[i + 1] + 1){
						lStraight = false;
					}
				}
				return lStraight;
			}
			
			public boolean smallStraight(int[] countArray){
				for(int i = 0; i < 3; i++){
					Boolean sStraight = true;
					for(int j = i; j < 4 + i && sStraight; j++){
						if(countArray[j] == 0){
							sStraight = false;
						}
					}
					if(sStraight)
						return true;
				}
				return false;
			}
			
			public boolean fullHouse(int[] countArray){
				for(int i = 0; i < 6; i++){
					//if we have 3 of anything
					if(countArray[i] == 3){
						for(int j = 0; j < 6; j++){
							//check for 2 of anything
							if(countArray[j] == 2){
								return true;
							}
						}
					}
				}
				return false;
			}
			
			public boolean fourOfAKind(int[] countArray){
				for(int i = 0; i < 6; i++){
					if(countArray[i] == 4){
						return true;
					}
				}
				return false;
			}
			
			public boolean threeOfAKind(int[] countArray){
				for(int i = 0; i < 6; i++){
					if(countArray[i] == 3){
						return true;
					}
				}
				return false;
			}
			//sort the many arrays to make the logic a bit easier
			public int[] sortArray(int rollsArray[]){
				Boolean sorted = false;
				while(!sorted){
					sorted = true;
					for(int i = 0; i < 4; i++){
						if(rollsArray[i] < rollsArray[i + 1]){
							sorted = false;
							int temp = rollsArray[i];
							rollsArray[i] = rollsArray[i + 1];
							rollsArray[i + 1] = temp;
						}
					}
				}
				return rollsArray;
	}
		}
		//gameSummary box will hold text boxes that show the results of each round
		class gameSummary extends VBox{
			
			roundText[] rounds = new roundText[10];
			int holding = 0;
			gameSummary(){
				super(10);
				this.setPrefWidth(260);
				this.getChildren().add(new Text("Round Summary"));
			}
			//Resets the whole object
			public void reset(){
				holding = 0;
				for(int i = 0; i < 10; i++){
					rounds[i] = null;
				}
				updateText();
			}
			public void addNewRound(int round, int[] dice, int score){
				if(holding < 10){
					rounds[holding] = new roundText(round, dice, score);
					holding++;
					updateText();
					
				}
			}
			//Clears the pane and then rebuilds it
			//On a reset, it will just have the title 'Round Summary'
			public void updateText(){
				this.getChildren().clear();
				this.getChildren().add(new Text("Round Summary"));
				for(int i = 0; i < holding; i++){
					this.getChildren().add(rounds[i]);
				}
			}
			//The subclass
			//Makes it easier to generate the text boxes
			class roundText extends Text{
				roundText(int round, int[] dice, int score){
					String roundString;
					roundString = "Round: " + round;
					for(int i = 0; i < 5; i++){
						roundString += " [" + dice[i] + "]";
					}
					roundString += " Scored: " + score;
					System.out.println(roundString);
					this.setText(roundString);
				}
			}
		}
	}

}


//Fairly simple VBox setup
//Doesn't need to be a separate class, but it doesn't hurt to stay a little organized
class mainMenu extends VBox{
	Text titleText;
	
	mainMenu(){
		super(30);
		
		titleText = new Text("Bess' Yahtzee Game");
		titleText.setFont(new Font(20));
		
		this.getChildren().addAll(titleText);
		this.setPadding(new Insets(10, 50, 50, 50));
		
	}
	
	
}
//This is the class that holds the dicePanes
//Also holds the functions that roll/reset the dice
//Also returns an array of the dice values
class diceHBox extends HBox{
	
	//Create the dicePane Objects
	dicePane dice1 = new dicePane("Dice 1");
    dicePane dice2 = new dicePane("Dice 2");
    dicePane dice3 = new dicePane("Dice 3");
    dicePane dice4 = new dicePane("Dice 4");
    dicePane dice5 = new dicePane("Dice 5");
    
	diceHBox(){
		//Call HBox(20) constructor
		super(20);
		this.setPadding(new Insets(20, 20, 20, 20));
		this.getChildren().add(dice1);
		this.getChildren().add(dice2);
		this.getChildren().add(dice3);
		this.getChildren().add(dice4);
		this.getChildren().add(dice5);
	}
	public int[] getValues(){
		int[] diceVals = new int[5];
		diceVals[0] = dice1.localDice.getValue();
		diceVals[1] = dice2.localDice.getValue();
		diceVals[2] = dice3.localDice.getValue();
		diceVals[3] = dice4.localDice.getValue();
		diceVals[4] = dice5.localDice.getValue();
		return diceVals;
	}
	public void rollDice(){
		dice1.localDice.attemptRoll();
		dice2.localDice.attemptRoll();
		dice3.localDice.attemptRoll();
		dice4.localDice.attemptRoll();
		dice5.localDice.attemptRoll();
	}
	public void resetDice(){
		dice1.localDice.resetDice();
		dice2.localDice.resetDice();
		dice3.localDice.resetDice();
		dice4.localDice.resetDice();
		dice5.localDice.resetDice();
	}
	
}

//dicePane just strictly holds the diceImages
class dicePane extends Pane{
	dice localDice;
//This constructor assigns a name to the dice
//Mainly used with debugging
public dicePane(String name){
	localDice = new dice(name);
	this.getChildren().add(localDice);
}
//Regular constructor
//Probably the more frequently used one
public dicePane(){
	localDice = new dice("Unnamed Dice");
	this.getChildren().add(localDice);
}
//The dice object holds the majority of the main dice functionality:
	//Holds the value of the dice
	//Whether the dice is currently being held or not
	//Rolls the dice and sets the image
class dice extends ImageView{
	Random rand = new Random();
	//Holds the original images
	Image di1 = new Image("image/dice1.png");
	Image di2 = new Image("image/dice2.png");
	Image di3 = new Image("image/dice3.png");
	Image di4 = new Image("image/dice4.png");
	Image di5 = new Image("image/dice5.png");
	Image di6 = new Image("image/dice6.png");  
	//for highlight
	Image image = new Image("image/orange.png");
	//alternate darker image
	Image di1d = new Image("image/dice1d.png");
	Image di2d = new Image("image/dice2d.png");
	Image di3d = new Image("image/dice3d.png");
	Image di4d = new Image("image/dice4d.png");
	Image di5d = new Image("image/dice5d.png");
	Image di6d = new Image("image/dice6d.png"); 
	
	
	//This sets whether the dice can be rolled or not
	boolean held = false;
	//The value of the dice(1-6)
	int value = 1;
	//Name of the dice
	//This is used to make the console easier to read
	String name = "";
	//Constructor for the dice Object
	//this one requires a name to make the console look prettier
	public dice(String nameIn){
		name = nameIn;
		this.setImage(rollDice());
		this.setOnMousePressed(e ->{
			this.hold();
		});
	}
	public dice(){
		name = "Unnamed Dice";
		this.setImage(rollDice());
		this.setOnMousePressed(e ->{
			this.hold();
		});
	}
	//Set the name of the dice
	public void setName(String nameIn){
		this.name = nameIn;
	}
	//Doesn't check for holding status
	//Used inbetween rounds
	public void resetDice(){
		this.setImage(rollDice());
	}
	//This function checks for hold before rolling the dice
	//Used during round 1 & 2
	public void attemptRoll(){
		if(!held){
			this.setImage(rollDice());
		}
	}
	//When the dice is clicked, it will either hold or revert a hold
	//This function takes the value of the dice and sets it to the
	//highlighted version of the current image
	//Inversely, if the dice it currently held, it calls the unhold() function
	public void hold(){
		if(held){
			unhold();
		}
		else{
			switch(value){
				case 1:
					held = true;
					this.setImage(di1d);
					break;
				case 2:
					held = true;
					this.setImage(di2d);
					break;
				case 3:
					held = true;
					this.setImage(di3d);
					break;
				case 4:
					held = true;
					this.setImage(di4d);
					break;
				case 5:
					held = true;
					this.setImage(di5d);
					break;
				case 6:
					held = true;
					this.setImage(di6d);
					break;
			}
		}
	}
	//This function reverts the image back to the un-highlighted version of the dice image
	//It also sets the held variable to false
	private void unhold(){
		held = false;	
		switch(value){
			case 1:
				this.setImage(di1);
				break;
			case 2:
				this.setImage(di2);
				break;
			case 3:
				this.setImage(di3);
				break;
			case 4:
				this.setImage(di4);
				break;
			case 5:
				this.setImage(di5);
				break;
			case 6:
				this.setImage(di6);
				break;
			}
	}
	//This function rolls a new dice and returns the Image object of the
	//Corresponding dice image. Used to update the ImageView.
	private Image rollDice(){
		held = false;
		value = rand.nextInt(6)+1;
		switch(value){
		case 1:
			return di1;
		case 2:
			return di2;
		case 3:
			return di3;
		case 4:
			return di4;
		case 5:
			return di5;
		case 6:
			return di6;
		default:
			return di1;
		}
	}
	//For extra functionality, you can pull the dice value;
	public int getValue(){
		return value;
	}
	//For extra functionality, you can pull the held boolean value;
	public boolean isHeld(){
		return held;
	}
	//This function prints out the dice name and the value rolled
	//Used mainly for debugging dice rolls
	public void printRollResults(){
		System.out.println(name + " rolled a " + value + ".");
	}
}
}
