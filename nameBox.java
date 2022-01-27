/*
Jacques Favre
April-7-2018
COP2552.001
Project 3 Yahtzee GUI
Java
Version 1.8
JavaFx GUI interface


alert box and tutorial for clean close from here: https://github.com/buckyroberts/Source-Code-from-Tutorials/blob/master/JavaFX/005_creatingAlertBoxes/AlertBox.java
*/


import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.IOException;

import javafx.geometry.*;

public class nameBox {
	
	//bolean
	static boolean answer;
	static String name = "";
	static String wrong ="";
	static String unknown = "Unknown";
	

    public static String display(String title, String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(300);
        Label label1 = new Label();
        label1.setText(message);
        TextField nameInput = new TextField();
        Label wrongInput = new Label(); 
        wrongInput.setText(wrong);
      //Event handler,  finish action, return name, close window,
 //       window.setOnCloseRequest(e -> {
 //       	e.consume();					// consume to override
 //       	name = isString(nameInput, nameInput.getText(), wrongInput);
 //       	window.close();
 //       });
         
        //create  buttons
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> {//Event handler, finish action, return name, close window,
        // call validate method
       name = isString(nameInput, nameInput.getText(), wrongInput);      	
        window.close();
        });

        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, nameInput, enterButton, wrongInput);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 40, 10, 40));
        layout.setStyle("-fx-background-color: gainsboro");//lightgrey
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        System.out.println(" box returnNAME: " + name);
        return name;
    }
    private static boolean validateInput(String input){
    	//Illegal characters
    	char[] invalidChar = {'!', '?', '/', '.', '*', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    	//Variable counter for checking
    	int lCount = 0;
    	if(input.length() > 30){
    		//the name is longer than 30 characters which is not allowed
    		return false;
    	}
    	else{
    		while(lCount < invalidChar.length){
    			if(input.indexOf(invalidChar[lCount]) >= 0){
    				//The string has a character that is invalid
    				return false;
    			}
    		}
    	}
    	//nothing is wrong with the string return true
    	return true;
    }
//get string and validate
	private static String isString(TextField nameInput, String text, Label wrongInput) {
		// TODO Auto-generated method stub
		try {
			String namein = nameInput.getText();  			
			System.out.println(" box NAME: " + namein);
			if(namein.length() == 0)  {// if no input, set name to unknown
				name = unknown;
			}
			else if(validateInput(namein)){// else pass input
			name = namein;
			}
			
		}
		catch(IllegalArgumentException ex){
		
//			message
//			message = ("input not valid");
			wrongInput.setText("Click on ROLL to start a new game");
		}	
		return name;
	}// end is string
}// end programm window 