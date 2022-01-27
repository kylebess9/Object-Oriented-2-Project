//This class will be what holds the cards
//
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class CardHolder extends HBox {
	int handValue;
	int aces;
	ArrayList<cardGUI> hand = new ArrayList<cardGUI>();
	CardHolder(){
		super(-100);
		this.setStyle("-fx-border-color: black");
		this.setPrefHeight(200);
		
		handValue = 0;
		aces = 0;
	}
	public void addCard(String suit, String value){
		
		cardGUI newCard = new cardGUI(suit, value);
		hand.add(newCard);
		this.getChildren().add(newCard);
		addValue(newCard.Value);
		System.out.println(getHandValue());
	}
	public void addValue(String testVal){
		if(testVal.equals("Q") || testVal.equals("K") || testVal.equals("J") || testVal.equals("A")){
			if(testVal.equals("A")){
				if(handValue > 10){
					handValue += 1;
				}
				else{
					aces++;
					handValue += 11;
				}
			}
			else{
				handValue += 10;
			}
		}
		else{
			handValue += Integer.parseInt(testVal);
		}
		if(handValue > 21 && aces >= 1){
			handValue -= 10;
			aces--;
		}
	}
	public void resetHand(){
		handValue = 0;
		aces = 0;
		for(int i = 0; i < hand.size(); i++){
			hand.get(i).removeCard();
		}
		this.getChildren().removeAll(hand);
	}
	public int getHandValue(){
		return handValue;
	}
}
