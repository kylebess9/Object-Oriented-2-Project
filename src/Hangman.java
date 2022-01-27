/*


Group Hangman

//Jacques Favre
//Kyle Bess
//William Caldwell
April-7-2018
COP2552.001
Project 3 Yahtzee GUI
Java
Version 1.8
JavaFx GUI interface


alert box and tutorial for clean close from here: https://github.com/buckyroberts/Source-Code-from-Tutorials/blob/master/JavaFX/005_creatingAlertBoxes/AlertBox.java





Rounds will roll 5 dices. For 10 times. That is 30 rolls.

10 rounds with 3 rolls
hold and roll remaining
scoring
read and print to file



top: 	buttonBox top for buttons
left: buttons
center: image
right: keybord 
bottom:	 instruction



Order of events
Start programm, display window to get name, get name
get and read file, 
find name (EXTRA 5 POINT)
start game
word 1, gessing game, get and check letter, count till won or lost round
.
.
.
.
last word
get score 
write to file name and score, group by name


cosmetics:
instructions
position of buttons
color and font buttons
improve key board, go to qwerty
comments


*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Hangman{
  Hangman(Stage primaryStage) throws IOException {
	  


	  
/************************/	  
    BorderPane pane = new BorderPane();// Whole window
    pane.setStyle("-fx-background-color: darkgrey");//whole window grey    dimgrey:too dark
    pane.setPadding(new Insets(20, 20, 20, 20));


    
    
    HangmanBox hangmanBox = new HangmanBox();
    pane.setTop(hangmanBox);

    
    
    Instruction instruction = new Instruction();
    pane.setBottom(instruction);

    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 1000, 800);
    primaryStage.setTitle("The Game of Hangman"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    //Event handler, close window, 2 ways of closing
    //Event handler, close window THIS IS NOT EXIT BUTTON!!!!
    //https://www.youtube.com/watch?v=ZuHcl5MmRck
    primaryStage.setOnCloseRequest(e -> {
    	e.consume();					// consume to override
    	HangmanBox.closeProgram();
    });
    

/**********************/    
    
  }//End Start


/**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
}



/***********************************  HangBox  **********************************************************************************************/

class HangmanBox extends BorderPane{
	
	boolean isFile = false;// if no word file
	boolean disable = false;
	
	int count = 0;// count number of misses 11 images so 11 miises is a LOST
	
    int counting = 0;// number of words in file
	
	String name = "";// get player name
	
	// label
	Label label1 = new Label("Press Start to play Hangman");//for one roll score
//	label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
	
	//The word array that will allow us to choose a word;
//	String[] wordsToChoose = loadWords();
//	String[] wordsToChoose = new String[];
	ArrayList<String> wordsToChoose = new ArrayList<String>();
//	wordsToChoose.add("ONE");
//	String[] defaultArray = {"PLEASANT", "TILTED", "JOCKEY", "TESTING"};
	
	ArrayList<String> wordsOut = new ArrayList<String>();//from text file
	
	private letterBox[] lblletter = new letterBox[28];
	private char[] letters = {'A', 'B', 'C', 'D', 'E',
		    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '-'};//Notice two extra to make 28, empty string and - 
	//Word to guess
//	String wordGuess = getAWord(wordsToChoose);
	String wordGuess = "";
	//The hidden word string
	StringBuilder guessedWord = new StringBuilder();
	
	
	
	Label guessWord = new Label(guessedWord.toString());
	
//	String  tryLetter = "";//letter pick on mouse click
//	  int choice = 0;// int for index keyboard and array letters
	
	 //buttons
	 Button btPlay = new Button("START");										//button at TOP  on
	    Button btExit = new Button("EXIT");	

	    Pane pane1 = new Pane(); // pane for picture
	    
	   
	    
	    //Hides the word
	    //Used for starting a new game
	    public void hideWord(){
	    	
	    	int wLength = wordGuess.length();
	    	
	    	System.out.println("line 208 WORD to guess: " + wordGuess);// PRINT TO CONSOLE TESTT
	    	
	    	
	    	for(int i = 0; i < wLength; i++){
	    		guessedWord.append("*");
	    	}
	    	guessWord.setText(guessedWord.toString());
	    }
	    public void updateWord(){
	    	guessWord.setText(guessedWord.toString());
	    }
	    
	    
	    public void resetLetters(){
	    	for(int i = 0; i < lblletter.length; i++)
	    		lblletter[i].reset();
	    }
	    public void guessLetter(char letter){
	    	//letter is in the word
	    	if(wordGuess.indexOf(letter) >= 0){
	    		//has the letter been guessed?
	    		//If not load the letters in
	    		if(!(guessedWord.indexOf(letter + "") >= 0)){
	    			int k = wordGuess.indexOf(letter);
	    			while(k >= 0){
	    				
	    				guessedWord.setCharAt(k, letter);
	    				k = wordGuess.indexOf(letter, k + 1);
	    			}
	    			if(guessedWord.indexOf("*") < 0){
	    				//No more * in the word
	    				//It's a win!
	    				label1.setText("Good Job, you got it! press start for next word.");
	    				disable = true;
	    		//		letterBox.setDisable(true);
	    				
	    		//		resetLetters();
	    		//		guessedWord.delete(0, guessedWord.length());
	    		//		wordGuess = getAWord(wordsToChoose);
	    		//		hideWord();
	    		//		count = 0;
	    		//		output(count, pane1);
	    				
	    			}
	    		}
	    		else{
	    			//If it's already a letter that's been guessed
	    			System.out.println("Letter already exists");
	    		}
	    	}
	    	//letter is not in the word
	    	else{
	    		count++;// count missed
	    		output(count, pane1);
	    		
	    		System.out.println("COUNT missed: " + count);// PRINT TO CONSOLE TESTT
	    		
	    		if (count >= 10) {
	    		label1.setText("Sorry, you lost! press start for next word.");// end of game message// moved to scoreGame function
	    		
	    		
	    		}
	    	}
	    	updateWord();// get a new word
	    }
	    
/*	    
	    // Pick random word in aaray
	    public String getAWord(ArrayList<String> wordsToChoose2){
//	    	public String getAWord(ArrayList<String> wordsToChoose2){	
	    	
			int numOfWords = wordsToChoose2.size();
			Random randNum = new Random();
			
			int choice = randNum.nextInt(numOfWords);
//			int choice = 0;
			System.out.println(" 281word choice array index: " + choice);
			System.out.println("282word choice array index: " + wordsToChoose2.size());
			
//			String word = wordsToChoose2.get(0);
			return wordsToChoose2.get(choice);
		}
*/		
	    
	    public String getAWord(ArrayList<String> wordsToChoose2){
	    	int c = 0;
	    
//	    java.util.Collections.shuffle(wordsToChoose2);
	    c = wordsToChoose2.size() ;
	    System.out.println("NUM WORDS : " + c );
//	    int counting = 0;
//		int i = counting;
	    //Kyle 4/18 this statement will initialize an empty string for now
	    //If the count is below the number of words in the array then it will be set to a word.
	    String word = "";
	    		  	
	    		  
	    		  System.out.println("Counting: " + counting );	  
	    if (counting >= c)	{
	    	label1.setText("Good Job, you got THEM ALL! press start for a new ROUND.");
	    	
	    	//Kyle 4/18 deactivateLetters function sets all the letter boxes playing boolean to false
	    	//NOTE - The letters are not currently being reactivated just yet
	    	//We need to figure out how to get into the next game and then reactivate letters
	    	//When we get the new game settled, just call activateLetters()
		    deactivateLetters();
	   
		    // 	endgame = true;
	    	
	    	//Kyle 4/18 We will probably want to reset the letterboxes here
	    	resetLetters();
	    	//Start button should 
	    	 btPlay.setOnAction(e -> {
	    		 //Kyle Note- We may need to create a function to shuffle the words rather than add the same words to the array
	    		 	loadWords();
	    		 	
	    		});
	    	
	    	
	    	
	    }
	    //Kyle 4/18 I moved this statement below.
	    //Saves the program from throwing an IndexOutOfBoundsException
	    else{
	    	word = wordsToChoose2.get(counting);
	    }
	    //Kyle 4/18 I moved the counting increase below the counting check so that the game doesn't prematurely end
	    //Before, it would end on the last word
	    counting ++;
		return word;
	    
	    }   
	
/******  Constructor  
 * @throws FileNotFoundException *****************/	
	public HangmanBox() throws FileNotFoundException	{
		// 
		
		
		wordsToChoose.add("ONE");    	// default word if file not found
		wordsToChoose.add("TWO"); 
/*		wordsToChoose.add("THREE"); 
		wordsToChoose.add("ADDED");    	// default word if file not found
		wordsToChoose.add("WORDS"); 
		wordsToChoose.add("JUST"); 
		wordsToChoose.add("IN");    	// default word if file not found
		wordsToChoose.add("CASE"); 
		wordsToChoose.add("NO"); 
		wordsToChoose.add("FILE"); 
		wordsToChoose.add("FOUND"); 
		
		
*/		 
		java.util.Collections.shuffle(wordsToChoose);
		
		
//		HBox title = new HBox();
//		this.setTop(title);
//		title.getChildren().addAll(label1);
//		label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));		
//		  title.setPadding(new Insets(20, 20, 20, 20));
//		  title.setAlignment( Pos.CENTER);
		
		//VBox for buttons
	//	   VBox buttonPane = new VBox(200);
	//	  this.setLeft(buttonPane);
		   HBox buttonPane = new HBox(800); 
		   this.setTop(buttonPane);
	 	   buttonPane.setPadding(new Insets(20, 20, 60, 20));
	 	   buttonPane.getChildren().addAll(btPlay, btExit);
	 	   buttonPane.setAlignment( Pos.CENTER);	
	 	  this.setStyle("-fx-background-color: gainsboro");//lightgrey
	 	 
	 	  
	 	  //Box for hangman  image and word to guess
	// 	  VBox imagePane = new VBox(20);// distance between picture and word to guess
	 	 HBox imagePane = new HBox(50);
	 	 this.setCenter(imagePane);
	 	imagePane.setMinHeight(150);// 
	 	imagePane.setMinWidth(300);// 
	 	imagePane.setStyle("-fx-background-color: wheat");//lightgrey
	// 	imagePane.setPadding(new Insets(40, 100, 40, 105));// 100 105 set position picture hangman
	 	imagePane.setPadding(new Insets(30, 20, 20, 40));// 100 105 set position picture hangman
	 	
	 	output(0, pane1);// this call image hangman
	 	imagePane.setAlignment( Pos.CENTER);
	 
	 	imagePane.getChildren().addAll(pane1);// this place imge
	 	imagePane.getChildren().addAll(guessWord);// label for word to guess ***********
	 	guessWord.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
	 	
	 	
	 	//Message below game
	 	 HBox textPane = new HBox(50);
	 	 this.setBottom(textPane);
	 	textPane.setPadding(new Insets(40, 20, 60, 20));
	 	textPane.getChildren().addAll(label1);// this place label for message
	 	label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
	 	textPane.setAlignment( Pos.CENTER);
	 	
	 	
//	 	btHit.setDisable(true);

	 	
	 	// box for letters
	 	
	 	GridPane letterPane = new GridPane();
	 	this.setRight(letterPane);
	 	this.setStyle("-fx-background-color: darkgrey");
	 	 letterPane.setPadding(new Insets(0, 20, 0, 20));
	 	letterPane.setAlignment(Pos.CENTER);  
	    letterPane.setHgap(10);
	    letterPane.setVgap(10);
	 // Create labels for letters
	    for (int i = 0; i < 28; i++) {

	    	lblletter[i] = new letterBox(letters[i]);
		    lblletter[i].setTextAlignment(TextAlignment.CENTER);
	        letterPane.add(lblletter[i], i % 7, i / 7);//set up letterPane grid with col and row, 7 col, 7 row
	        lblletter[i].setText(letters[i] + "");
	        lblletter[i].setStyle("-fx-background-color: lightgrey");
	        lblletter[i].setMinHeight(60);
	        lblletter[i].setMinWidth(60);
	        lblletter[i].setPadding(new Insets(20, 20, 20, 20));
	        lblletter[i].setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));
	        
	      }
	    //start or start new word reset and get word
	    btPlay.setOnAction(e -> {
	    	//Kyle 4/18 activateLetters function sets all the letter boxes playing boolean to true
	    activateLetters();
		startRound();
		});
	    
	    // call a closeProgram method to  save files than close
	    btExit.setOnAction(e -> {
	
	    	closeProgram();
		
		});// call a closeProgram method to  save files thaen close
	    
	    
	    //Calling Set-Up functions
	    

//	    startGame();// get name// get window to get name
	    
//	    hideWord();
	    
	    loadWords();
	    
	 	
	}//End constructor
/******************************************************/
	//Kyle 4/18 added this function to disable/enable the letterboxes
	//Activate letters will set the playing value to true
	//This will allow the player to manipulate the keyboard
	private void activateLetters(){
		for(int i = 0; i < lblletter.length; i++)
    		lblletter[i].setPlaying(true);
	}
	//Deactivate letters sets the playing value to false
	//This will no longer allow access to the keyboard
	//Should be used between rounds
	private void deactivateLetters(){
		for(int i = 0; i < lblletter.length; i++)
    		lblletter[i].setPlaying(false);
	}
	
	
	private void startRound() {
		// TODO Auto-generated method stub
		System.out.println(" IS FILE" + isFile);
		
	    updateWord();
		resetLetters();
		guessedWord.delete(0, guessedWord.length());
		
		if (isFile) {
			wordGuess = getAWord(wordsOut);
		}
		else if (!isFile){
			wordGuess = getAWord(wordsToChoose);
		}
//		getAWord(wordsToChoose);
//		wordGuess = getAWord(wordsToChoose);
//		wordGuess = getAWord(wordsOut);
		hideWord();
		count = 0;
		output(count, pane1);
		label1.setText("Use on-screen keybord to play!");// end of game message// moved to scoreGame function



	}
	
	
	
	public static void closeProgram() {

//		allScoreFile();// save files
		System.out.println(" Clean close");
		System.exit(0);

	}
	
	


	
	public void loadWords(){
		
		System.out.println("loading");
		
//		File wordFile = new File("words.txt");
		int wordCount = 0;
		
		//Does the word file exist?
//		if(wordFile.exists()){
			try{
				System.out.println("loading");
				isFile = true;

				Scanner inFile = new Scanner(new File("wordsb.txt"));
//				 while (fileInput.hasNext()) {// counts words, not line  !!!white space!!!
				while(inFile.hasNext()){
					String word = inFile.nextLine().toUpperCase();
//					String word.toUpperCase():
					wordsOut.add(word);
					wordCount++;
//					inFile.nextLine();
//					System.out.println("wordCount" + wordCount);
					java.util.Collections.shuffle(wordsOut);

//					word.toUpperCase():

					
					
				}
				inFile.close();

				
				System.out.println("wordsOut" + wordsOut);
			}
			catch(Exception e){
			//	No File Found
				isFile = false;

				//We shouldn't get this error, but just in case
				System.out.println("No File Found");

			}

	}//endloadwords
	
	
	
	//to get name, to get file 
	public void startGame() { 

		name = getName();//get name in new window to avoid crownding main window
		
		System.out.println("Name in start: " + name);
		
	}
	
	
	private String getName() {//call a new window get and validate name, try, catch very basic
		// TODO Auto-generated method stub

		//open a window to get name
		name = nameBox.display("Welcome to the Hangman", "Enter your name to start the game, \n"
				+ "Press Enter\nOr simply press Enter to be unkwnon! ");// call a  window to enter name
		
		return name;
	}		
	
	
	
	// key bord stuff
	class letterBox extends Label{

		char lValue;// letter selected bu keybord press mouse
		boolean used = false;
		//Kyle 4/18 this variable (playing) dictates whether the player can or cannot interact with the board
		boolean playing = false;
		//function to get letter
		letterBox(char letter){
			lValue = letter;
			
			// to get letter value on click on keyboard
			this.setOnMousePressed(e -> {
				//Kyle 4-18 added the playing variable
				if(!used && playing){
					
					System.out.println(this.lValue);// Print letter to console
					used = true;
					this.setStyle("-fx-background-color: grey");
					guessLetter(this.lValue);
				}
			});
//			if(disable) {
//				System.out.println("disable " + disable);	
				
//			this.setDisable(true);
//			}
//			else {
//				this.setDisable(false);	
//			}
			//change to darker color on mouse hover
			this.setOnMouseEntered(e -> {
				//Kyle 4-18 added the playing variable
				if(playing)
					this.setStyle("-fx-background-color: grey");
			});
			//color back to normal
			this.setOnMouseExited(e -> {
				//Kyle 4-18 added the playing variable
				if(!used && playing)
					this.setStyle("-fx-background-color: lightgrey");
			});
		}
		public void reset(){
			used = false;
			this.setStyle("-fx-background-color: lightgrey");
		}
		//Kyle 4/18 I don't plan for this function to be used a whole lot
		//This function will flip whatever the playing value is
		public void togglePlaying(){
			if(!playing)
				this.playing = true;
			else
				this.playing = false;
		}
		//Kyle 4/18 This is probably the most reliable way to set when the keyboard can be used
		//just call the function with (true) or (false)
		public void setPlaying(boolean t){
			this.playing = t;
		}
	}//class letter box






public void output(int X, Pane pane) {// match variable image with an URL
    Image di0 = new Image("image/hangman0.gif");
    Image di1 = new Image("image/hangman1.gif");
    Image di2 = new Image("image/hangman2.gif");
    Image di3 = new Image("image/hangman3.gif");
    Image di4 = new Image("image/hangman4.gif");
    Image di5 = new Image("image/hangman5.gif");
    Image di6 = new Image("image/hangman6.gif");
    Image di7 = new Image("image/hangman7.gif");
    Image di8 = new Image("image/hangman8.gif");
    Image di9 = new Image("image/hangman9.gif");
    Image di10 = new Image("image/hangman10.gif");
  
	

      if(X==0) {pane.getChildren().add(new ImageView(di0));}// match number and image
      if(X==1) {pane.getChildren().add(new ImageView(di1));}
      if(X==2) {pane.getChildren().add(new ImageView(di2));}
      if(X==3) {pane.getChildren().add(new ImageView(di3));}
      if(X==4) {pane.getChildren().add(new ImageView(di4));}
      if(X==5) {pane.getChildren().add(new ImageView(di5));}
      if(X==6) {pane.getChildren().add(new ImageView(di6));}
      if(X==7) {pane.getChildren().add(new ImageView(di7));}
      if(X==8) {pane.getChildren().add(new ImageView(di8));}
      if(X==9) {pane.getChildren().add(new ImageView(di9));}
      if(X==10) {pane.getChildren().add(new ImageView(di10));}
	}//end output




	
  }// end class


/***************************  Instruction Pane Class  ***************************************************************************************/

class Instruction extends BorderPane {	//Instruction box at bottom
	
  
  //NEW LABEL 
  private Label oneDice = new Label("Instructions.\n\nPress Start to play the game.\nChose a letter using the on-screen keyboard.\n"
  		+ "You have some  chances to guess right or wrong.... .\nTry again."
  		+ "\nWhen you get a WIN, or a LOSS, press start for the next word.");
  

 
  		//constructor*****************************
	  public Instruction() {
    
	    this.setCenter(oneDice);
	    BorderPane.setAlignment(oneDice, Pos.CENTER);
	    this.setStyle("-fx-background-color: gainsboro");//lightgrey
	    this.setPadding(new Insets(20, 20, 20, 20));
  }// end 
	//constructor********************************

}//end class instructions

















